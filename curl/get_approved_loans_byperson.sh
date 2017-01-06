#!/bin/bash

url=http://localhost:8080
personId=1

curl -H "Content-Type: application/json" -X GET $url/api/v1/loan/approved/person/$personId -w "\n"
