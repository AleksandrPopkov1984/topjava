# Curl requests

##### getAll
curl http://localhost:8080/topjava/rest/meals

##### get
curl http://localhost:8080/topjava/rest/meals/100003

##### getBetween
curl "http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-30T00:00:00&startTime=2020-01-30T19:59:00&endDate=2020-01-31T00:00:00&endTime=2020-01-31T23:59:00"

##### getBetweenWithoutValues
curl "http://localhost:8080/topjava/rest/meals/filter/?startDate=&startTime&endDate&endTime"

##### delete
curl -X DELETE http://localhost:8080/topjava/rest/meals/100003

##### post
curl -H "Content-Type: application/json" -X POST http://localhost:8080/topjava/rest/meals -d "{\"dateTime\":\"2022-11-29T17:45:00\",\"description\":\"lunch\",\"calories\":\"1500\"}"

##### put
curl -H "Content-Type: application/json" -X PUT http://localhost:8080/topjava/rest/meals/100003 -d "{\"dateTime\":\"2020-01-30T09:00:00\",\"description\":\"Renewed breakfast\",\"calories\":\"200\"}"