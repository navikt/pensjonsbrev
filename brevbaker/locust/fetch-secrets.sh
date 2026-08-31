#!/bin/bash

KUBE_CLUSTER="dev-gcp"

jq --version || (
  echo "ERROR: You need to install the jq CLI tool on your machine: https://stedolan.github.io/jq/" && exit 1
) || exit 1
which nais || (
  echo "ERROR: You need to install the nais CLI tool on your machine: https://doc.nais.io/operate/how-to/naisdevice/nais-cli/" && exit 1
) || exit 1

team_name="$(nais status -ojson | jq -r '.[].team.name | select(contains("pensjonsbrev"))')"
if [ -z "$team_name" ]; then
  echo "ERROR: Could not find a team matching 'pensjonsbrev' via 'nais status'. Make sure you are logged in with 'nais login' and have access to the team." && exit 1
fi

mkdir -p secrets

nais secret get azure-locust -t pensjonsbrev --environment $KUBE_CLUSTER --with-values --reason "local development" --output json | jq '.data | from_entries' > secrets/azuread.json
echo "All secrets are fetched and stored in the \"secrets\" folder."
