#!/bin/bash

COMMAND="$1"
if [ "$2" != "" ]; then
    COMMAND="$COMMAND $2"
fi

# Exists if command is invalid
validate_command() {
    if [ "$COMMAND" = "update" ]; then
        return 0
    fi

    if [ "$COMMAND" = "update --log-level=SEVERE" ]; then
        return 0
    fi
        
    if [ "$COMMAND" = "update --log-level=DEBUG" ]; then
        return 0
    fi

    if [ "$COMMAND" = "rollback-count --count=1" ]; then
        return 0
    fi

    echo "Invalid command. Please provide a valid command next time."
    exit 1
}

validate_command 

liquibase $COMMAND \
    --url="$POSTGRES_URL" \
    --username="$POSTGRES_USER" \
    --password="$POSTGRES_PASSWORD" \
    --changelog-file=db.changelog-master.yml \