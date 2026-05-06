import { useEffect, useMemo, useState } from 'react';

const API_BASE = '/api';

async function api(path, options = {}) {
  const response = await fetch(`${API_BASE}${path}`, {
    headers: { 'Content-Type': 'application/json', ...(options.headers || {}) },
    ...options,
  });

  if (!response.ok) {
    const message = await response.text();
    throw new Error(message || `Request failed with status ${response.status}`);
  }

  if (response.status === 204) {
    return null;
  }
  return response.json();
}

function App() {
  const [activeTab, setActiveTab] = useState('book');
  const [snapshot, setSnapshot] = useState({
    movies: [],
    venues: [],
    screens: [],
    listings: [],
    bookings: [],
  });
  const [loadingSnapshot, setLoadingSnapshot] = useState(false);
  const [error, setError] = useState('');

  const [query, setQuery] = useState('dune');
  const [date, setDate] = useState('2026-05-06');
  const [movieResults, setMovieResults] = useState([]);
  const [selectedMovie, setSelectedMovie] = useState(null);
  const [movieListings, setMovieListings] = useState([]);
  const [selectedListing, setSelectedListing] = useState(null);
  const [seatRows, setSeatRows] = useState([]);
  const [selectedBooking, setSelectedBooking] = useState(null);
  const [customerName, setCustomerName] = useState('Ashish');
  const [bookingResult, setBookingResult] = useState(null);
  const [busy, setBusy] = useState(false);

  async function loadSnapshot() {
    setLoadingSnapshot(true);
    setError('');
    try {
      const [movies, venues, screens, listings, bookings] = await Promise.all([
        api('/movies'),
        api('/venues'),
        api('/screens'),
        api('/listings'),
        api('/bookings'),
      ]);
      setSnapshot({ movies, venues, screens, listings, bookings });
    } catch (err) {
      setError(err.message);
    } finally {
      setLoadingSnapshot(false);
    }
  }

  useEffect(() => {
    loadSnapshot();
  }, []);

  async function searchMovies(event) {
    event.preventDefault();
    setBusy(true);
    setError('');
    setBookingResult(null);
    try {
      const results = await api(`/movies/search?query=${encodeURIComponent(query)}`);
      setMovieResults(results);
      setSelectedMovie(null);
      setMovieListings([]);
      setSelectedListing(null);
      setSeatRows([]);
      setSelectedBooking(null);
    } catch (err) {
      setError(err.message);
    } finally {
      setBusy(false);
    }
  }

  async function chooseMovie(movie) {
    setBusy(true);
    setError('');
    setSelectedMovie(movie);
    setSelectedListing(null);
    setSeatRows([]);
    setSelectedBooking(null);
    setBookingResult(null);
    try {
      const suffix = date ? `?date=${encodeURIComponent(date)}` : '';
      const listings = await api(`/movies/${movie.id}/listings${suffix}`);
      setMovieListings(listings);
    } catch (err) {
      setError(err.message);
    } finally {
      setBusy(false);
    }
  }

  async function chooseListing(listing) {
    setBusy(true);
    setError('');
    setSelectedListing(listing);
    setSelectedBooking(null);
    setBookingResult(null);
    try {
      const seats = await api(`/listings/${listing.listingId}/seats`);
      setSeatRows(seats);
    } catch (err) {
      setError(err.message);
    } finally {
      setBusy(false);
    }
  }

  async function bookSeat(event) {
    event.preventDefault();
    if (!selectedBooking) {
      setError('Select an available seat first.');
      return;
    }
    setBusy(true);
    setError('');
    try {
      const booked = await api(`/bookings/${selectedBooking.bookingId}`, {
        method: 'PATCH',
        body: JSON.stringify({ customerName }),
      });
      setBookingResult(booked);
      await chooseListing(selectedListing);
      await loadSnapshot();
    } catch (err) {
      setError(err.message);
    } finally {
      setBusy(false);
    }
  }

  const availableSeats = useMemo(
    () => seatRows.filter((seat) => seat.status === 'AVAILABLE' && seat.bookingId),
    [seatRows],
  );

  return (
    <main className="app">
      <header className="topbar">
        <div>
          <h1>BookMyShow Console</h1>
          <p>Search movies, inspect listings, view inventory, and book seats.</p>
        </div>
        <button className="secondary" onClick={loadSnapshot} disabled={loadingSnapshot}>
          Refresh
        </button>
      </header>

      {error && <div className="alert">{error}</div>}

      <nav className="tabs" aria-label="Views">
        {[
          ['book', 'Book Ticket'],
          ['movies', 'Movies'],
          ['venues', 'Venues'],
          ['screens', 'Screens'],
          ['listings', 'Listings'],
          ['bookings', 'Bookings'],
        ].map(([id, label]) => (
          <button
            key={id}
            className={activeTab === id ? 'active' : ''}
            onClick={() => setActiveTab(id)}
          >
            {label}
          </button>
        ))}
      </nav>

      {activeTab === 'book' && (
        <section className="workspace">
          <div className="panel wide">
            <div className="panelHeader">
              <h2>Booking Flow</h2>
              <span>{busy ? 'Working' : 'Ready'}</span>
            </div>

            <form className="searchbar" onSubmit={searchMovies}>
              <label>
                Movie name
                <input value={query} onChange={(event) => setQuery(event.target.value)} />
              </label>
              <label>
                Date
                <input type="date" value={date} onChange={(event) => setDate(event.target.value)} />
              </label>
              <button disabled={busy}>Search</button>
            </form>

            <div className="columns">
              <Step title="1. Pick Movie">
                <div className="list">
                  {movieResults.map((movie) => (
                    <button
                      key={movie.id}
                      className={selectedMovie?.id === movie.id ? 'row selected' : 'row'}
                      onClick={() => chooseMovie(movie)}
                    >
                      <strong>{movie.name}</strong>
                      <span>{movie.director} · {movie.rating}</span>
                    </button>
                  ))}
                  {!movieResults.length && <Empty text="Search for a movie to begin." />}
                </div>
              </Step>

              <Step title="2. Pick Listing">
                <div className="list">
                  {movieListings.map((listing) => (
                    <button
                      key={listing.listingId}
                      className={selectedListing?.listingId === listing.listingId ? 'row selected' : 'row'}
                      onClick={() => chooseListing(listing)}
                    >
                      <strong>{listing.listingName}</strong>
                      <span>
                        {listing.start} · {listing.screenType} · Rs {listing.price}
                      </span>
                      <span>{listing.venueAddress}</span>
                    </button>
                  ))}
                  {!movieListings.length && <Empty text="Select a movie to view listings." />}
                </div>
              </Step>
            </div>
          </div>

          <div className="panel">
            <div className="panelHeader">
              <h2>Seats</h2>
              <span>{availableSeats.length} available</span>
            </div>
            <div className="seatGrid">
              {seatRows.map((seat) => (
                <button
                  key={`${seat.seatId}-${seat.bookingId}`}
                  className={[
                    'seat',
                    seat.status === 'BOOKED' ? 'booked' : 'available',
                    selectedBooking?.bookingId === seat.bookingId ? 'selected' : '',
                  ].join(' ')}
                  disabled={seat.status === 'BOOKED' || !seat.bookingId}
                  onClick={() => setSelectedBooking(seat)}
                  title={`${seat.rowLabel}${seat.seatNumber} ${seat.status}`}
                >
                  {seat.rowLabel}{seat.seatNumber}
                </button>
              ))}
              {!seatRows.length && <Empty text="Select a listing to load seats." />}
            </div>

            <form className="bookingForm" onSubmit={bookSeat}>
              <label>
                Customer name
                <input value={customerName} onChange={(event) => setCustomerName(event.target.value)} />
              </label>
              <button disabled={busy || !selectedBooking}>Book Selected Seat</button>
            </form>
          </div>

          {bookingResult && (
            <div className="panel result">
              <div className="panelHeader">
                <h2>Booking Confirmed</h2>
                <span>{bookingResult.status}</span>
              </div>
              <dl>
                <dt>Movie</dt><dd>{bookingResult.movieName}</dd>
                <dt>Venue</dt><dd>{bookingResult.venueAddress}</dd>
                <dt>Show</dt><dd>{bookingResult.date} at {bookingResult.start}</dd>
                <dt>Seat</dt><dd>{bookingResult.seat} · {bookingResult.seatType}</dd>
                <dt>Price</dt><dd>Rs {bookingResult.price}</dd>
                <dt>Customer</dt><dd>{bookingResult.customerName}</dd>
              </dl>
            </div>
          )}
        </section>
      )}

      {activeTab === 'movies' && <DataTable title="Movies" rows={snapshot.movies} columns={['id', 'name', 'director', 'rating', 'uaRating']} />}
      {activeTab === 'venues' && <DataTable title="Venues" rows={snapshot.venues} columns={['id', 'address', 'seats', 'type']} />}
      {activeTab === 'screens' && <DataTable title="Screens" rows={snapshot.screens.map(flattenScreen)} columns={['id', 'type', 'venueId', 'venueAddress']} />}
      {activeTab === 'listings' && <DataTable title="Listings" rows={snapshot.listings.map(flattenListing)} columns={['id', 'name', 'movieName', 'screenId', 'date', 'start', 'end', 'price']} />}
      {activeTab === 'bookings' && <DataTable title="Bookings" rows={snapshot.bookings} columns={['id', 'listingId', 'movieName', 'seat', 'status', 'price', 'customerName']} />}
    </main>
  );
}

function Step({ title, children }) {
  return (
    <section className="step">
      <h3>{title}</h3>
      {children}
    </section>
  );
}

function Empty({ text }) {
  return <p className="empty">{text}</p>;
}

function DataTable({ title, rows, columns }) {
  return (
    <section className="panel tablePanel">
      <div className="panelHeader">
        <h2>{title}</h2>
        <span>{rows.length} rows</span>
      </div>
      <div className="tableWrap">
        <table>
          <thead>
            <tr>{columns.map((column) => <th key={column}>{column}</th>)}</tr>
          </thead>
          <tbody>
            {rows.map((row, index) => (
              <tr key={row.id ?? index}>
                {columns.map((column) => <td key={column}>{formatCell(row[column])}</td>)}
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </section>
  );
}

function flattenScreen(screen) {
  return {
    id: screen.id,
    type: screen.type,
    venueId: screen.venue?.id,
    venueAddress: screen.venue?.address,
  };
}

function flattenListing(listing) {
  return {
    id: listing.id,
    name: listing.name,
    movieName: listing.movie?.name,
    screenId: listing.screen?.id,
    date: listing.date,
    start: listing.start,
    end: listing.end,
    price: listing.price,
  };
}

function formatCell(value) {
  if (value === null || value === undefined || value === '') {
    return '-';
  }
  if (typeof value === 'object') {
    return JSON.stringify(value);
  }
  return value;
}

export default App;
