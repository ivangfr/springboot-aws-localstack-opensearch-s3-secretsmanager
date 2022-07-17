#!/usr/bin/env bash

./mvnw clean compile jib:dockerBuild --projects movie-api
./mvnw clean compile jib:dockerBuild --projects movie-ui
