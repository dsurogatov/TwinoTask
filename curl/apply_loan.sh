#!/bin/bash

url=http://localhost:8080
data='{"term" : "term", "amount" : 777.12, "firstName" : "Denis", "surName" : "Orlov"}'

curl -H "Content-Type: application/json" --data "$data" $url/api/v1/loan/ -w "\n"
