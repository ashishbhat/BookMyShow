#!/usr/bin/env bash

  BASE_URL="http://localhost:8080/screens"

  curl -X POST "$BASE_URL" -H "Content-Type: application/json" \
    -d '{"venue":{"id":1},"type":"IMAX"}'

  curl -X POST "$BASE_URL" -H "Content-Type: application/json" \
    -d '{"venue":{"id":1},"type":"STANDARD"}'

  curl -X POST "$BASE_URL" -H "Content-Type: application/json" \
    -d '{"venue":{"id":1},"type":"VIP"}'

  curl -X POST "$BASE_URL" -H "Content-Type: application/json" \
    -d '{"venue":{"id":1},"type":"DOLBY"}'

  curl -X POST "$BASE_URL" -H "Content-Type: application/json" \
    -d '{"venue":{"id":2},"type":"STANDARD"}'

  curl -X POST "$BASE_URL" -H "Content-Type: application/json" \
    -d '{"venue":{"id":2},"type":"VIP"}'

  curl -X POST "$BASE_URL" -H "Content-Type: application/json" \
    -d '{"venue":{"id":2},"type":"DOLBY"}'

  curl -X POST "$BASE_URL" -H "Content-Type: application/json" \
    -d '{"venue":{"id":3},"type":"IMAX"}'

  curl -X POST "$BASE_URL" -H "Content-Type: application/json" \
    -d '{"venue":{"id":3},"type":"STANDARD"}'

  curl -X POST "$BASE_URL" -H "Content-Type: application/json" \
    -d '{"venue":{"id":3},"type":"VIP"}'

  curl -X POST "$BASE_URL" -H "Content-Type: application/json" \
    -d '{"venue":{"id":3},"type":"DOLBY"}'

  curl -X POST "$BASE_URL" -H "Content-Type: application/json" \
    -d '{"venue":{"id":3},"type":"STANDARD"}'

  curl -X POST "$BASE_URL" -H "Content-Type: application/json" \
    -d '{"venue":{"id":4},"type":"STANDARD"}'

  curl -X POST "$BASE_URL" -H "Content-Type: application/json" \
    -d '{"venue":{"id":4},"type":"VIP"}'

  curl -X POST "$BASE_URL" -H "Content-Type: application/json" \
    -d '{"venue":{"id":4},"type":"DOLBY"}'

  curl -X POST "$BASE_URL" -H "Content-Type: application/json" \
    -d '{"venue":{"id":4},"type":"STANDARD"}'

  curl -X POST "$BASE_URL" -H "Content-Type: application/json" \
    -d '{"venue":{"id":5},"type":"IMAX"}'

  curl -X POST "$BASE_URL" -H "Content-Type: application/json" \
    -d '{"venue":{"id":5},"type":"STANDARD"}'

  curl -X POST "$BASE_URL" -H "Content-Type: application/json" \
    -d '{"venue":{"id":5},"type":"VIP"}'

  curl -X POST "$BASE_URL" -H "Content-Type: application/json" \
    -d '{"venue":{"id":5},"type":"DOLBY"}'
