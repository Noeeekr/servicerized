#!/bin/sh

# SECTION BEGIN : STATIC VARIABLES

IDENTITY_SERVICE_ENV_FILE_PATH="./services/identity/src/main/resources/secrets/compose.dev.env"
POSTGRES_SERVICE_ENV_FILE_PATH="./services/pg-database/src/main/resources/secrets/compose.dev.env"

IDENTITY_SERVICE_SELECTION_ID="identity"
POSTGRES_SERVICE_SELECTION_ID="postgres"

SELECTION_IDS="$IDENTITY_SERVICE_SELECTION_ID $POSTGRES_SERVICE_SELECTION_ID"

# SECTION END : STATIC VARIABLES
# SECTION BEGIN : DYNAMIC VARIABLES

# SECTION END : DYNAMIC VARIABLES
# SECTION BEGIN : FUNCTIONS

prompt_available_services() {
	# Prints available values
	
	FORMATTED_STRING="AVAILABLE VALUES:"
	AVAILABLE_VALUES="$1"
	for VALUE in $AVAILABLE_VALUES; do
		FORMATTED_STRING="$FORMATTED_STRING\n- $VALUE"
	done
	echo "$FORMATTED_STRING"
}
# Function read_match() prompts user for a string input that must match uniquely one of the values providen.
# The input may not match equally, it only needs to match enough for it to be mapped to a single value.
# It returns a string with the value the user selected. It exits the program if user input doesn't match any value.
# Arg 1: A list of values that can be matched.
read_match() {
	
	# Prompts user for value	
	CHOOSEN_VALUE=""
	read -p "Choose a service: " CHOOSEN_VALUE

	# Exits if no input
	if [ "$CHOOSEN_VALUE" = "" ]; then
		exit 0
	fi

	# Check if matches a single available service
	STR_LEN="${#str}"
	MATCH_FOUND=""
	for VALUE in $AVAILABLE_VALUES; do
		# Skip values that doesn't match
		CHECK_VALUE="$(printf "%s.${STR_LEN}" "$VALUE")"
		if [ "$CHECK_VALUE" != "$CHOOSEN_VALUE" ]; then
			continue
		fi
		# If match is already set and there is another value exit
		if [ "$MATCH_FOUND" != "" ]; then
			echo "Ambigous value found, it matches more than one option. Provide a more specific value next time."
			exit 0
		fi
		# Sets the first match
		MATCH_FOUND="$VALUE"
	done

	if [ "$MATCH_FOUND" = "" ]; then
		echo "Unknown option. Please provide a option that matches agaisn't a available option."
		exit 0
	fi

	# Return chosen value
	echo "$MATCH_FOUND"
}

# get_env_file_path_by_id checks if the id providen is equal to any env file path id. If so, returns the path.
# Arg 1: The wanted env file path id.
get_env_file_path_by_id() {
	SERVICE_ID="$1"
	if [ "$SERVICE_ID" = "$IDENTITY_SERVICE_SELECTION_ID" ]; then
		echo "$IDENTITY_SERVICE_ENV_FILE_PATH"
	else
		echo "$POSTGRES_SERVICE_ENV_FILE_PATH"
	fi
}

# SECTION END : FUNCTIONS
echo "What service you want to use"
prompt_available_services "$SELECTION_IDS"

CHOOSEN_VALUE="$(read_match)"
CHOOSEN_ENV_FILE_PATH=$(get_env_file_path_by_id $CHOOSEN_VALUE)

UID=$(id -u) docker compose --env-file="$CHOOSEN_ENV_FILE_PATH" up -d dev-service
