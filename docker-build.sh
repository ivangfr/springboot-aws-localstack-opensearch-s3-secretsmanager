#!/usr/bin/env bash

./mvnw clean compile jib:dockerBuild --projects movie-api
