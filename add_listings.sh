  #!/usr/bin/env bash
  set -euo pipefail

  BASE_URL="http://localhost:8080/listings"
  SHOW_DATE="2026-05-06"

  for screen_id in {1..20}; do
    # Morning
    movie_id=$(( ((screen_id + 0) % 5) + 1 ))
    curl --fail-with-body -X POST "$BASE_URL" \
      -H "Content-Type: application/json" \
      -d "{\"name\":\"Morning Show - Screen $screen_id\",\"price\":180.00,\"movie\":{\"id\":$movie_id},\"start\":\"09:00:00\",\"end\":\"12:00:00\",\"date\":\"$SHOW_DATE\",\"screen\":{\"id\":$screen_id}}"

    echo

    # Afternoon
    movie_id=$(( ((screen_id + 1) % 5) + 1 ))
    curl --fail-with-body -X POST "$BASE_URL" \
      -H "Content-Type: application/json" \
      -d "{\"name\":\"Afternoon Show - Screen $screen_id\",\"price\":240.00,\"movie\":{\"id\":$movie_id},\"start\":\"13:00:00\",\"end\":\"16:00:00\",\"date\":\"$SHOW_DATE\",\"screen\":{\"id\":$screen_id}}"

    echo

    # Evening
    movie_id=$(( ((screen_id + 2) % 5) + 1 ))
    curl --fail-with-body -X POST "$BASE_URL" \
      -H "Content-Type: application/json" \
      -d "{\"name\":\"Evening Show - Screen $screen_id\",\"price\":320.00,\"movie\":{\"id\":$movie_id},\"start\":\"18:00:00\",\"end\":\"21:00:00\",\"date\":\"$SHOW_DATE\",\"screen\":{\"id\":$screen_id}}"

    echo
  done

