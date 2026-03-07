curl -sS -X POST http://localhost:7070/api/private-app/transactions/actions/filter-cursor \
  -H "Accept: application/json" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer test" \
  -d '{
  "domain": null,
  "status": null,
  "pagination": {
    "pageSize": 5,
    "sortBy": null,
    "sortOrder": null,
    "nextPageToken": 200000,
    "previousPageToken": null
  }
}'
