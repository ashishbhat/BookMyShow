#!/usr/bin/env bash
  set -euo pipefail

  BASE_URL="http://localhost:8080/movies"

  movies=(
    '{"name":"Inception","director":"Christopher Nolan","rating":8.8,"uaRating":"UA"}'
    '{"name":"Interstellar","director":"Christopher Nolan","rating":8.7,"uaRating":"UA"}'
    '{"name":"The Dark Knight","director":"Christopher Nolan","rating":9.0,"uaRating":"UA"}'
    '{"name":"Dune","director":"Denis Villeneuve","rating":8.0,"uaRating":"UA"}'
    '{"name":"Dune Part Two","director":"Denis Villeneuve","rating":8.5,"uaRating":"UA"}'
    '{"name":"Arrival","director":"Denis Villeneuve","rating":7.9,"uaRating":"UA"}'
    '{"name":"The Matrix","director":"Lana Wachowski, Lilly Wachowski","rating":8.7,"uaRating":"A"}'
    '{"name":"Mad Max Fury Road","director":"George Miller","rating":8.1,"uaRating":"A"}'
    '{"name":"Top Gun Maverick","director":"Joseph Kosinski","rating":8.2,"uaRating":"UA"}'
    '{"name":"Avatar","director":"James Cameron","rating":7.9,"uaRating":"UA"}'
    '{"name":"Avatar The Way of Water","director":"James Cameron","rating":7.5,"uaRating":"UA"}'
    '{"name":"Oppenheimer","director":"Christopher Nolan","rating":8.3,"uaRating":"UA"}'
    '{"name":"Jawan","director":"Atlee","rating":7.0,"uaRating":"UA"}'
    '{"name":"Pathaan","director":"Siddharth Anand","rating":5.8,"uaRating":"UA"}'
    '{"name":"RRR","director":"S. S. Rajamouli","rating":7.8,"uaRating":"UA"}'
    '{"name":"Baahubali The Beginning","director":"S. S. Rajamouli","rating":8.0,"uaRating":"UA"}'
    '{"name":"Baahubali The Conclusion","director":"S. S. Rajamouli","rating":8.2,"uaRating":"UA"}'
    '{"name":"3 Idiots","director":"Rajkumar Hirani","rating":8.4,"uaRating":"U"}'
    '{"name":"PK","director":"Rajkumar Hirani","rating":8.1,"uaRating":"UA"}'
    '{"name":"Lagaan","director":"Ashutosh Gowariker","rating":8.1,"uaRating":"U"}'
  )

  for movie in "${movies[@]}"; do
    curl --fail-with-body -X POST "$BASE_URL" \
      -H "Content-Type: application/json" \
      -d "$movie"
    echo
  done

