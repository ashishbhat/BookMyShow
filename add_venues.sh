  #!/usr/bin/env bash

  BASE_URL="http://localhost:8080/venues"

  curl -X POST "$BASE_URL" \
    -H "Content-Type: application/json" \
    -d '{"address":"PVR Phoenix Mall, Mumbai","seats":180,"type":"DUPLEX"}'

  curl -X POST "$BASE_URL" \
    -H "Content-Type: application/json" \
    -d '{"address":"INOX City Centre, Delhi","seats":120,"type":"SINGLE"}'

  curl -X POST "$BASE_URL" \
    -H "Content-Type: application/json" \
    -d '{"address":"Cinepolis Forum Mall, Bengaluru","seats":220,"type":"DUPLEX"}'

  curl -X POST "$BASE_URL" \
    -H "Content-Type: application/json" \
    -d '{"address":"Miraj Cinemas, Pune","seats":95,"type":"SINGLE"}'

  curl -X POST "$BASE_URL" \
    -H "Content-Type: application/json" \
    -d '{"address":"Carnival Cinemas, Hyderabad","seats":160,"type":"DUPLEX"}'

