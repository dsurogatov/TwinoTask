#!/bin/bash

url=http://localhost:8080

curl -H "Content-Type: application/json" -X GET $url/api/v1/loan/approved -w "\n"
