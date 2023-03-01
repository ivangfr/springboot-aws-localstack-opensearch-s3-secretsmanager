#!/usr/bin/env bash

if ! [[ $(docker ps -q -f name=localstack) ]]; then
  echo "WARNING: The localstack Docker container is not running. Please, start it first."
  exit 1
fi

if [ -z "$1" ]; then
  echo "WARNING: OMDB_API_KEY must be informed as 1st parameter"
  exit 1
fi

AWS_REGION=eu-west-1
OPENSEARCH_DOMAIN_NAME=my-domain
OMDB_API_KEY=$1

echo
echo "Initializing LocalStack"
echo "======================="

echo
echo "Installing jq"
echo "-------------"
docker exec -t localstack apt-get -y install jq

echo
echo "Creating OpenSearch domain"
echo "--------------------------"
docker exec -t localstack aws --endpoint-url=http://localhost:4566 opensearch create-domain --domain-name $OPENSEARCH_DOMAIN_NAME

echo
echo "Waiting for OpenSearch domain creation to complete"
echo "--------------------------------------------------"
TIMEOUT=$((7 * 60))  # set timeout to 7 minutes
WAIT_INTERVAL=1
for ((i=0; i<TIMEOUT; i+=WAIT_INTERVAL)); do
  VAR=$(docker exec -t localstack aws --endpoint-url=http://localhost:4566 opensearch describe-domain --domain-name $OPENSEARCH_DOMAIN_NAME | jq ".DomainStatus.Processing")
  if [ "$VAR" = false ] ; then
    echo
    echo "Processing completed!"
    break
  fi
  if [ $i -ge $TIMEOUT ] ; then
    echo
    echo "The process TIMEOUT"
    break
  fi
  printf "."
  sleep $WAIT_INTERVAL
done

AWS_LOCALSTACK_URL="http://localhost.localstack.cloud:4566"
AWS_LOCALSTACK_OPENSEARCH_URL="${AWS_LOCALSTACK_URL}/opensearch/${AWS_REGION}/${OPENSEARCH_DOMAIN_NAME}"

echo
echo "Testing OpenSearch endpoint"
echo "---------------------------"
curl $AWS_LOCALSTACK_OPENSEARCH_URL

echo
echo "Deleting existing indexes"
echo "-------------------------"
curl -X DELETE $AWS_LOCALSTACK_OPENSEARCH_URL/movies
echo

echo
echo "Creating movies index"
echo "---------------------"
curl -X PUT $AWS_LOCALSTACK_OPENSEARCH_URL/movies -H "Content-Type: application/json" -d @opensearch/movies-settings.json

echo
echo
echo "Creating S3 bucket"
echo "------------------"

docker exec -t localstack aws --endpoint-url=http://localhost:4566 s3 mb s3://com.ivanfranchin.movieapi.posters

echo
echo "Creating secrets in secretsmanager"
echo "----------------------------------"

docker exec -t localstack aws --endpoint-url=http://localhost:4566 secretsmanager create-secret --name omdbApiKey --secret-string $OMDB_API_KEY

echo
echo "----------------------------------------"
echo "           AWS_LOCALSTACK_URL=$AWS_LOCALSTACK_URL"
echo "AWS_LOCALSTACK_OPENSEARCH_URL=$AWS_LOCALSTACK_OPENSEARCH_URL"
echo "----------------------------------------"

echo
echo "LocalStack initialized successfully"
echo "==================================="
echo