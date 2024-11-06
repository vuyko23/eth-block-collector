# eth-block-collector


## Requirements
- Java 21
- Spring Boot 3.3.5
- Elasticsearch 8.13.4

## Installation

To run elastic search, you can use the following command:
```bash docker run -d --name elasticsearch \
  docker run -p 8081:9200 \
  -e "discovery.type=single-node" \
  -e "xpack.security.enabled=false" \
  docker.elastic.co/elasticsearch/elasticsearch:8.13.4
```
If you like to see data in Kibana, you can use the following command:
```bash
docker run -d --name kibana \
  -p 5601:5601 \
  --add-host=host.docker.internal:host-gateway \
  -e ELASTICSEARCH_HOSTS="http://host.docker.internal:8081" \
  docker.elastic.co/kibana/kibana:8.13.4
  ```

## How to Use
Swagger URL: ``` http://localhost:8080/swagger-ui.html ```
By default app starting to collect Ethereum blocks from the latest block or last saved to DB.
Application is using buffer to store blocks and save them to DB in batch mode. You can change buffer size in application.properties file.
Also, you can change the thread rate in application.properties file.

## Url
- POST /api/v1/pool/start - start collecting blocks
- DELETE /api/v1/pool/stop - stop collecting blocks
- POST /api/v1/blocks/search - search blocks by filters
- POST /api/v1/transaction/search - search transactions by filters

Filter example:
```
{
  {
    "fieldName": "hash",
    "fieldValue": "0xbda221ad74fff38171a1a3113bb4e1ba5a19c465c979c5367cac3457adede5f7",
    "operation": "="
  }
}
```

Operation that can be used is:
- Equals '='
- Not equals '!='
- Like '%'
