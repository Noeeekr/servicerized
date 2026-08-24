#!/bin/sh

# SECTION VARIABLES

# Customizable variables
ENVIRONMENT="dev"

DATABASE_PORT="5432"
DATABASE_HOST="localhost"
DATABASE_NAME="servicerized-main-db"
DATABASE_USER_NAME="servicerized-main-user"
DATABASE_USER_PASSWORD="servicerized-main-user"

# Static variables
ARGS="$@"
FLAG_PREFIX="--"

SCRIPTS_DIR="$(cd "$(dirname "$0")" && pwd -P)"
SECRETS_DIR="$(cd "$SCRIPTS_DIR/../resources/secrets" && pwd -P )"

DATABASE_NAME_FLAGNAME="dbname"
DATABASE_HOST_FLAGNAME="dbhost"
DATABASE_PORT_FLAGNAME="dbport"
DATABASE_USER_NAME_FLAGNAME="dbuser"
DATABASE_USER_PASSWORD_FLAGNAME="dbpasswd"

ENVIRONMENT_FLAGNAME="env"

# SECTION VARIABLES END
# SECTION FUNCTIONS

## Function get_flag() checks command args for flag value
## Arg 1: The name of the flag
## Returns the next arg after the flag or "" if no next arg is found
get_flag() {
    FLAG="$FLAG_PREFIX$1"
    EQUAL=0

    for ARG in $ARGS; do
        if [ "$EQUAL" = "1" ]; then 
            echo "$ARG"
            return 0
        fi

        if is_equal "$FLAG" "$ARG"; then
            EQUAL=1
        fi
    done

    echo ""
    return 1
}

## Function is_equal() checks if two arguments are equal
## Arg 1: any string
## Arg 2: any string
## Return code 0 when equal and code 1 when diff
is_equal() {
    if [ "$1" = "$2" ]; then
        return 0
    fi
    return 1
}

## Function prompt_value() sets the value of the specified variable
## Arg 1: The description of the variable
## Arg 2: A default value
## Returns default value when user inputs empty or the user custom string
prompt_value() {
    VARIABLE="$2"
    read -p "$1: (default: $2)" VARIABLE
    if [ "$VARIABLE" = "" ]; then
        echo "$2" 
    else
        echo "$VARIABLE"
    fi
}

## Function parse() attempts getting value from flag, if not possible doesn't change the original value
## Arg 1: The original value
## Arg 2: The flag name
replace_with_flag_value() {
    FINAL_VALUE="$1"
    DESCRIPTION="$3"
    FLAG_NAME="$2"

    FLAG_VALUE="$(get_flag "$FLAG_NAME")"

    if [ "$DESCRIPTION" = "" ]; then
        DESCRIPTION=$2
    fi

    if [ "$FLAG_VALUE" = "" ]; then
        FINAL_VALUE="$(prompt_value "$DESCRIPTION" "$FINAL_VALUE")" 
    else 
        FINAL_VALUE="$FLAG_VALUE"
    fi

    echo "$FINAL_VALUE"
    return 0
}

## Function create_secret_file() creates a file and writes the content to it
## Arg 1: File name
## Arg 2: File content
create_secret_file() {
    mkdir -p "$SECRETS_DIR"
    touch "$SECRETS_DIR/$1"
    echo "$2" > "$SECRETS_DIR/$1"
}

## Function log_file_creation() logs information about created files
## Arg 1: The path of the created file
log_file_creation() {
    echo "+ $1 at $2"
}

must_approve_overview() {
    echo "Secrets folder: $SECRETS_DIR"

    PROCEED=""
    read -p "Proceed? (y) " PROCEED
    PROCEED="$(echo "$PROCEED" | cut -c 1)"

    if [ "$PROCEED" != "y" ]; then
        echo "Aborted"
        exit 0 
    fi
}

## Function log_section() logs the section name in a consistent format
## Arg 1: Section name 
log_section() {
    echo "\n[$1]\n"
}

get_flyway_file_content() {
    echo "flyway.url=jdbc:postgresql://$DATABASE_HOST:$DATABASE_PORT/$DATABASE_NAME"
    echo "flyway.password=$DATABASE_USER_PASSWORD"
    echo "flyway.driver=org.postgresql.Driver"
    echo "flyway.user=$DATABASE_USER_NAME"
}

get_docker_file_content() {
    echo "POSTGRES_PASSWORD=$DATABASE_USER_PASSWORD"
    echo "POSTGRES_PORT=$DATABASE_PORT"
    echo "POSTGRES_USER=$DATABASE_USER_NAME"
    echo "POSTGRES_DB=$DATABASE_NAME"
}

# SECTION FUNCTIONS END
# SECTION MAIN

log_section "Overview"

must_approve_overview

log_section "Configuration parameters"

ENVIRONMENT="$(replace_with_flag_value "$ENVIRONMENT" "$ENVIRONMENT_FLAGNAME" "Environment")"

echo ""

DATABASE_NAME="$(replace_with_flag_value "$DATABASE_NAME" "$DATABASE_NAME_FLAGNAME" "Database name")"
DATABASE_HOST="$(replace_with_flag_value "$DATABASE_HOST" "$DATABASE_HOST_FLAGNAME" "Database host")"
DATABASE_PORT="$(replace_with_flag_value "$DATABASE_PORT" "$DATABASE_PORT_FLAGNAME" "Database port")"
DATABASE_USER_NAME="$(replace_with_flag_value "$DATABASE_USER_NAME" "$DATABASE_USER_NAME_FLAGNAME" "Database user name")"
DATABASE_USER_NAME="$(replace_with_flag_value "$DATABASE_USER_NAME" "$DATABASE_USER_NAME_FLAGNAME" "Database user password")"

log_section "Creating configuration files"

## Create flyway file

FILE_NAME="flyway.$ENVIRONMENT.conf"
FILE_CONTENT=$(get_flyway_file_content)

log_file_creation "$FILE_NAME" "$SECRETS_DIR"
create_secret_file "$FILE_NAME" "$FILE_CONTENT"

FILE_NAME="compose.$ENVIRONMENT.env"
FILE_CONTENT=$(get_docker_file_content)

log_file_creation "$FILE_NAME" "$SECRETS_DIR"
create_secret_file "$FILE_NAME" "$FILE_CONTENT"

exit 0 

# SECTION MAIN END