#!/bin/bash

COMMAND="$1"
if [ "$2" != "" ]; then
    COMMAND="$COMMAND $2"
fi
if [ "$3" != "" ]; then
    COMMAND="$COMMAND $3"
fi

liquibase $COMMAND \
    --url="$POSTGRES_URL" \
    --username="$POSTGRES_USER" \
    --password="$POSTGRES_PASSWORD" \
    --changelog-file=db.changelog-master.yml \