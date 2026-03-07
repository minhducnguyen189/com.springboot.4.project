curl -X POST http://localhost:7070/private-app/transactions/actions/filter-cursor \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer test" \
  -d '{
  "domain": null,
  "status": null,
  "pagination": {
    "pageSize": 200,
    "sortBy": null,
    "sortOrder": null,
    "nextPageToken": 200000,
    "previousPageToken": null
  }
}'
