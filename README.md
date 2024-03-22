# springboot-aws-localstack-opensearch-s3-secretsmanager

In this project, we are going to use [`LocalStack`](https://localstack.cloud/) to simulate locally, some services provided by [`AWS Cloud`](https://aws.amazon.com/) such as [`OpenSearch`](https://aws.amazon.com/opensearch-service/), [`S3`](https://aws.amazon.com/s3/), and [`Secrets Manager`](https://aws.amazon.com/secrets-manager/). Also, in order to simplify the use of AWS managed services, we are going to use [`Spring Cloud AWS`](https://spring.io/projects/spring-cloud-aws).

> **Note**: Check out the [`springboot-aws-localstack-dynamodb-lambda-sns-sqs`](https://github.com/ivangfr/springboot-aws-localstack-dynamodb-lambda-sns-sqs) repository. In it, we've developed two Spring Boot applications for producing and listening to news updates. We also utilized LocalStack to locally simulate AWS Cloud services like [`DynamoDB`](https://aws.amazon.com/dynamodb/), [`Lambda`](https://aws.amazon.com/lambda/), [`SNS`](https://aws.amazon.com/sns/) and [`SQS`](https://aws.amazon.com/sqs/).

## Proof-of-Concepts & Articles

On [ivangfr.github.io](https://ivangfr.github.io), I have compiled my Proof-of-Concepts (PoCs) and articles. You can easily search for the technology you are interested in by using the filter. Who knows, perhaps I have already implemented a PoC or written an article about what you are looking for.

## Additional Readings

- \[**Medium**\] [**Spring Boot Apps for Movie Indexing/Search with AWS OpenSearch, S3 and Secrets Manager**](https://medium.com/@ivangfr/spring-boot-apps-for-movie-indexing-search-with-aws-opensearch-s3-and-secrets-manager-a95ad0697e51)
- \[**Medium**\] [**Spring Boot apps to trigger and consume DynamoDB News table updates using AWS Lambda, SNS and SQS**](https://medium.com/@ivangfr/spring-boot-apps-to-trigger-and-consume-dynamodb-news-table-updates-using-aws-lambda-sns-and-sqs-957570cf9a3a)
- \[**Medium**\] [**Implementing a Spring Boot App using AWS DynamoDB as database**](https://medium.com/@ivangfr/implementing-a-spring-boot-app-using-aws-dynamodb-as-database-5dbf8b7fc924)
- \[**Medium**\] [**Implementing a Spring Boot App that uses AWS Secrets Manager to store its MongoDB credentials**](https://medium.com/@ivangfr/implementing-a-spring-boot-app-that-uses-aws-secrets-manager-to-store-its-mongodb-credentials-f805a4c74d9a)

## Project Diagram

![project-diagram](documentation/project-diagram.jpeg)

## Applications

- ### movie-api

  [`Spring Boot`](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/) Java Web application that exposes a REST API and provides a UI for indexing movies.

  It has the following endpoints:
  ```
   GET /api/movies/{imdb}
  POST /api/movies {"imdb":"...", "title":"...", "posterUrl":"...", "year":"...", "released":"...", "imdbRating":"...", "genre":"...", "runtime":"...", "director":"...", "writer":"...", "actors":"...", "plot":"...", "language":"...", "country":"...", "awards":"..."}
  POST /api/movies/{imdb}/uploadPoster
  ```

  The information of the movies, such as `imdb`, `title`, `year`, etc, are stored in `OpenSearch` that is hosted in `LocalStack`. The movie's `poster` are stored in `S3` buckets.

  The `movie-api` has access to [`OMDb API`](https://www.omdbapi.com/) to search and add easily new movies. In order to make request to `OMDb API`, an `apiKey` is needed. This key is stored as a secret in `Secrets Manager`.

- ### movie-ui

  `Spring Boot` Java Web application with a user interface designed for searching movies indexed in `movie-api`. To populate its UI with movie data, `movie-ui` communicates with `movie-api` by making requests to its endpoints. The movie’s poster is retrieved from the `S3` bucket.

## Prerequisites

- [`Java 17+`](https://www.oracle.com/java/technologies/downloads/#java17)
- [`Docker`](https://www.docker.com/)
- [`OMDb API`](https://www.omdbapi.com/) KEY

  To search movies in `OMDb API`, we need to obtain an API KEY from `OMDb API`. In order to do it, access https://www.omdbapi.com/apikey.aspx and follow the steps provided by the website.

## Start and Initialize LocalStack

- In a terminal, make sure you are in inside `springboot-aws-localstack-opensearch-s3-secretsmanager` root folder

- Start `LocalStack` Docker container
  ```
  DEBUG=1 docker compose up -d
  ```

- \[Optional\] Debug logs are enabled so that we have more insights about what is happening. To monitor `localstack` Docker container logs, run the command below
  ```
  docker logs localstack -f
  ```

- Initialize `LocalStack` by running the following script
  ```
  ./init-localstack.sh <OMDB_API_KEY>
  ```
  The script requires `OMDB_API_KEY` as first and unique argument. The script will create:
  - a domain for `OpenSearch` as well as the `movies` index using the `movies-settings.json` provided;
  - bucket `com.ivanfranchin.movieapi.posters` in `S3`;
  - a secret for `OMDB_API_KEY` in `Secrets Manager`.

## Running applications with Maven

- **movie-api**
  
  In a terminal and, inside `springboot-aws-localstack-opensearch-s3-secretsmanager` root folder, run the following command
  ```
  export AWS_REGION=eu-west-1 && export AWS_ACCESS_KEY_ID=key && export AWS_SECRET_ACCESS_KEY=secret && \
    ./mvnw clean spring-boot:run --projects movie-api
  ```

- **movie-ui**

  In another terminal and, inside `springboot-aws-localstack-opensearch-s3-secretsmanager` root folder, run the command below
  ```
  ./mvnw clean spring-boot:run --projects movie-ui
  ```

## Running applications as Docker container

- ### Build Docker images

  In a terminal and, inside `springboot-aws-localstack-opensearch-s3-secretsmanager` root folder, run the following script
  ```
  ./docker-build.sh
  ```

- ### Run Docker containers

  - **movie-api**
    
    In a terminal, run the following command
    ```
    docker run --rm --name movie-api -p 9080:9080 \
      -e AWS_REGION=eu-west-1 -e AWS_ACCESS_KEY_ID=key -e AWS_SECRET_ACCESS_KEY=secret \
      --network=springboot-aws-localstack-opensearch-s3-secretsmanager_default \
      ivanfranchin/movie-api:1.0.0
    ```

  - **movie-ui**

    In another terminal, run the command below
    ```
    docker run --rm --name movie-ui -p 9081:9081 \
      -e MOVIE_API_URL=http://movie-api:9080 \
      --network=springboot-aws-localstack-opensearch-s3-secretsmanager_default \
      ivanfranchin/movie-ui:1.0.0
    ```

## Application URL

| Application | Type    | URL                                   |
|-------------|---------|---------------------------------------|
| `movie-api` | Swagger | http://localhost:9080/swagger-ui.html |
| `movie-api` | UI      | http://localhost:9080                 |
| `movie-ui`  | UI      | http://localhost:9081                 |

## Demo

- **Adding movie**: in the GIF below, we are using `movie-api` to add the movie _"American Pie 2"_

  ![demo-adding-movie](documentation/demo-adding-movie.gif)

- **Searching movies**: in the GIF below, we are using `movie-ui` to search for movies

  ![demo-searching-movies](documentation/demo-searching-movies.gif)

## Useful Links

- **OpenSearch**

  Check indexes
  ```
  curl "http://localhost.localstack.cloud:4566/opensearch/eu-west-1/my-domain/_cat/indices?v"
  ```
  
  Simple search
  ```
  curl "http://localhost.localstack.cloud:4566/opensearch/eu-west-1/my-domain/movies/_search?pretty"
  ```

## Shutdown

- To stop the applications, go to the terminal where they are running and press `Ctrl+C`
- To stop and remove docker compose containers, network and volumes, go to a terminal and, inside `springboot-aws-localstack-opensearch-s3-secretsmanager` root folder, run the following command
  ```
  docker compose down -v
  ```

## Cleanup

To remove the Docker images created by this project, go to a terminal and, inside `springboot-aws-localstack-opensearch-s3-secretsmanager` root folder, run the script below
```
./remove-docker-images.sh
```
