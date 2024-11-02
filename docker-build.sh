#!/usr/bin/env bash

DOCKER_IMAGE_PREFIX="ivanfranchin"
APP_VERSION="1.0.0"

MOVIE_API_APP_NAME="movie-api"
MOVIE_UI_APP_NAME="movie-ui"
MOVIE_API_DOCKER_IMAGE_NAME="${DOCKER_IMAGE_PREFIX}/${MOVIE_API_APP_NAME}:${APP_VERSION}"
MOVIE_UI_DOCKER_IMAGE_NAME="${DOCKER_IMAGE_PREFIX}/${MOVIE_UI_APP_NAME}:${APP_VERSION}"

SKIP_TESTS="true"

./mvnw clean spring-boot:build-image --projects "$MOVIE_API_APP_NAME" -DskipTests="$SKIP_TESTS" -Dspring-boot.build-image.imageName="$MOVIE_API_DOCKER_IMAGE_NAME"
./mvnw clean spring-boot:build-image --projects "$MOVIE_UI_APP_NAME" -DskipTests="$SKIP_TESTS" -Dspring-boot.build-image.imageName="$MOVIE_UI_DOCKER_IMAGE_NAME"
