#!/bin/bash

KUBE_CLUSTER="dev-gcp"

jq --version || (
  echo "ERROR: You need to install the jq CLI tool on your machine: https://stedolan.github.io/jq/" && exit 1
) || exit 1
which base64 || (
  echo "ERROR: You need to install the base64 tool on your machine. (brew install base64 on macOS)" && exit 1
) || exit 1

team_name="$(nais status -ojson | jq -r '.[].team.Name | select(contains("pensjonsbrev"))')"
if [ -z "$team_name" ]; then
  echo "ERROR: Could not find a team matching 'pensjonsbrev' via 'nais status'. Make sure you are logged in with 'nais login' and have access to the team." && exit 1
fi

mkdir -p secrets

nais secret get azure-locust --environment $KUBE_CLUSTER --with-values --reason "local development" --output json | jq '.data | from_entries' > secrets/azuread.json
echo "All secrets are fetched and stored in the \"secrets\" folder."
