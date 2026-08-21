#!/bin/bash

KUBE_CLUSTER="dev-gcp"

jq --version || (
  echo "ERROR: You need to install the jq CLI tool on your machine: https://stedolan.github.io/jq/" && exit 1
) || exit 1
which nais || (
  echo "ERROR: You need to install the nais CLI tool on your machine: https://doc.nais.io/operate/how-to/naisdevice/nais-cli/" && exit 1
) || exit 1

team_name="$(nais status -ojson | jq -r '.[].team.Name | select(contains("pensjonsbrev"))')"
if [ -z "$team_name" ]; then
  echo "ERROR: Could not find a team matching 'pensjonsbrev' via 'nais status'. Make sure you are logged in with 'nais login' and have access to the team." && exit 1
fi

# AzureAD
secret_name="$(nais app env skribenten-web --environment $KUBE_CLUSTER --team pensjonsbrev --verbose --output=json | jq -r '[.[] | select(.source.kind=="SECRET") | .source.name] | unique | .[] | select(startswith("azure-skribenten-web"))')"
nais secret get "${secret_name}" --environment $KUBE_CLUSTER --with-values --reason "local development" --output json | jq '.data | from_entries' | jq -r 'to_entries|map("\(.key)=\(.value|tostring)")|.[]' > .env
echo ".env file created."