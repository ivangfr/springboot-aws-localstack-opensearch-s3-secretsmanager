#!/usr/bin/env bash

if [ -z "$1" ]; then
  echo "WARNING: OMDB_API_KEY must be informed as 1st parameter"
  exit 1
fi

AWS_REGION=eu-west-1
OPENSEARCH_DOMAIN_NAME=my-domain
OMDB_API_KEY=$1
TIMEOUT=600

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
while true ; do
  VAR=$(docker exec -t localstack aws --endpoint-url=http://localhost:4566 opensearch describe-domain --domain-name $OPENSEARCH_DOMAIN_NAME | jq ".DomainStatus.Processing")
  if [ "$VAR" = false ] ; then
    echo "Processing completed!"
    break
  fi

  if [ $SECONDS -ge $TIMEOUT ] ; then
    echo "${log_waiting} TIMEOUT"
    break;
  else
    printf "."
  fi
  sleep 1
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