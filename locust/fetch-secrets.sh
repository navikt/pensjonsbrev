#!/bin/bash

KUBE_CLUSTER="dev-gcp"

jq --version || (
  echo "ERROR: You need to install the jq CLI tool on your machine: https://stedolan.github.io/jq/" && exit 1
) || exit 1
which base64 || (
  echo "ERROR: You need to install the base64 tool on your machine. (brew install base64 on macOS)" && exit 1
) || exit 1

mkdir -p secrets
kubectl --context $KUBE_CLUSTER -n pensjonsbrev get secret azure-locust -o json | jq '.data | map_values(@base64d)' > secrets/azuread.json
echo "All secrets are fetched and stored in the \"secrets\" folder."
