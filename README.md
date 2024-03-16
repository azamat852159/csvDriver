 Following documentation there are 4 endpoints in the service.
 - /api/v1/csv/upload (multipart/form-data, the file is being sent with key 'file'. Returning the message if uploaded successfully or error message)
 - /api/v1/csv/get?code={code} (returning the record in csv format)
 - /api/v1/csv/getAll (returning all records in csv format)
 - /api/v1/csv/deleteAll (empty response with 204 status code)
