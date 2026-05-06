  #!/usr/bin/env bash
  set -euo pipefail

  BASE_URL="http://localhost:8080/seats"

  for screen_id in {1..20}; do
    for seat_number in {1..20}; do
      if [ "$seat_number" -le 8 ]; then
        row_label="A"
        display_number="$seat_number"
        seat_type="NORMAL"
      elif [ "$seat_number" -le 16 ]; then
        row_label="B"
        display_number=$((seat_number - 8))
        seat_type="PREMIUM"
      else
        row_label="C"
        display_number=$((seat_number - 16))
        seat_type="RECLINER"
      fi

      curl --fail-with-body -X POST "$BASE_URL" \
        -H "Content-Type: application/json" \
        -d "{
          \"screen\": {\"id\": $screen_id},
          \"rowLabel\": \"$row_label\",
          \"seatNumber\": $display_number,
          \"seatType\": \"$seat_type\"
        }"

      echo
    done
  done

