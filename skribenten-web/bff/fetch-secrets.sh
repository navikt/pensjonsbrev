#!/bin/bash

KUBE_CLUSTER="dev-gcp"

jq --version || (
  echo "ERROR: You need to install the jq CLI tool on your machine: https://stedolan.github.io/jq/" && exit 1
) || exit 1
which base64 || (
  echo "ERROR: You need to install the base64 tool on your machine. (brew install base64 on macOS)" && exit 1
) || exit 1

mkdir -p secrets

# AzureAD
secret_name="$(kubectl --context $KUBE_CLUSTER -n pensjonsbrev get azureapp skribenten-web -o=jsonpath='{.spec.secretName}')"
kubectl --context $KUBE_CLUSTER -n pensjonsbrev get secret ${secret_name} -o json | jq '.data | map_values(@base64d)' | jq -r 'to_entries|map("\(.key)=\(.value|tostring)")|.[]' > .env
echo ".env file created."