#!/bin/bash

KUBE_CLUSTER="dev-gcp"

function checkKubectl() {
  echo "Verify kubectl, may take some time..."
  output="$(kubectl --context $KUBE_CLUSTER version 2>&1)"
  status=$?

  if [ $status -gt 0 ] ; then
    if echo "$output" | grep -q "command not found" ; then
      echo "ERROR: You need to install kubectl: $output"
      echo "Howto: https://doc.nais.io/basics/access/"
      return $status

    elif echo "$output" | grep -q "Unable to connect to the server" ; then
      error_msg="$(echo "$output" | grep "Unable to connect to the server" | cut -d':' -f2)"
      echo "ERROR: Cannot connect to kubernetes cluster $KUBE_CLUSTER: $error_msg"
      echo "Have you remembered to connect naisdevice? (see https://doc.nais.io/basics/access/)"
      return 1

    elif echo "$output" | grep -q "error: You must be logged in" ; then
      echo "ERROR: Not logged in to the cluster. Use 'gcloud auth login' (see https://doc.nais.io/basics/access/)."
      return 1

    else
      echo "WARN: Got unknown error from kubectl, but will attempt to fetch secrets anyway."
      return 0

    fi
  elif echo "$output" | grep -q "Client Version" && echo "$output" | grep -q "Server Version" ; then
    echo "kubectl: OK "
    return 0
  else
    echo "WARN: Got unexpected output from 'kubectl version', but will attempt to fetch secrets anyway."
    return 0
  fi
}

checkKubectl || exit 1
jq --version || (
  echo "ERROR: You need to install the jq CLI tool on your machine: https://stedolan.github.io/jq/" && exit 1
) || exit 1
which base64 || (
  echo "ERROR: You need to install the base64 tool on your machine. (brew install base64 on macOS)" && exit 1
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

#kafka
#secret and AivenApplication created with:
#nais aiven create -p nav-dev -e 999999 -s aiven-pensjon-brevbaker-q2-lokal kafka pensjon-brevbaker-lokal pensjonsbrev
nais aiven tidy
nais aiven get kafka aiven-pensjon-brevbaker-q2-lokal pensjonsbrev
rm -rf ./secrets/kafka
mv /tmp/aiven-secret* ./secrets/kafka

# Erstatt paths i filer for å kunne mounte fila riktig i pdf-bygger containeren. Laget for å få riktig path i container
find ./secrets/kafka/* -type f -exec sed -i 's/\/tmp\/aiven-secret-.*\//\/secrets\//g' {} \;

#rett opp i kcat conf (brukes kun utenfor container)
sed -i 's/\/secrets\//\.\//g' ./secrets/kafka/kcat.conf

echo -e "Kafka secrets hentet til ./secrets/kafka"