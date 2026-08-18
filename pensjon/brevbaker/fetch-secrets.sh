#!/bin/bash

KUBE_CLUSTER="dev-gcp"

jq --version || (
  echo "ERROR: You need to install the jq CLI tool on your machine: https://stedolan.github.io/jq/" && exit 1
) || exit 1
which base64 || (
  echo "ERROR: You need to install the base64 tool on your machine. (brew install base64 on macOS)" && exit 1
) || exit 1

kubectl --context ${KUBE_CLUSTER} -n pensjonsbrev auth can-i get secrets --request-timeout=2s > /dev/null 2>&1 || (
  echo "ERROR: Could not access secrets in namespace pensjonsbrev on cluster ${KUBE_CLUSTER}. Make sure you are connected to naisdevice and have run 'kubectl config use-context ${KUBE_CLUSTER}'." && exit 1
) || exit 1

function getSecret() {
  local secret_name="$1"
  local output_name="$2"

  echo ""
  kubectl --context $KUBE_CLUSTER -n pensjonsbrev get secret "${secret_name}" -o json | jq '.data | map_values(@base64d)' > secrets/"${output_name}".json

  echo "Creating ${output_name}.env file from ${output_name}.json..."
  jq -r 'to_entries|map("\(.key)=\(.value|tostring)")|.[]' secrets/"${output_name}".json > secrets/"${output_name}".env
  echo "${output_name}.env file created in the \"secrets\" folder."
}

mkdir -p secrets

# AzureAD
secret_name="$(kubectl --context $KUBE_CLUSTER -n pensjonsbrev get azureapp pensjon-brevbaker -o=jsonpath='{.spec.secretName}')"
getSecret "$secret_name" azuread

# Unleash ApiToken
getSecret pensjon-brevbaker-unleash-api-token unleash