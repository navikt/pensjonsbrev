#!/bin/bash

KUBE_CLUSTER="dev-gcp"

if [ "${BASH_VERSINFO:-0}" -lt 4 ]; then
    echo "Du har for gammel versjon av bash. Vennligst installer versjon 4 eller høyere"

    if [[ $OSTYPE == 'darwin'* ]]; then
        echo
        echo "På Mac kan du kjøre: ${white}brew install bash${endcolor}"
    fi

    exit 1
fi

if command -v nais >& /dev/null; then
  DISCONNECT_STATUS=$(nais device status | grep -c Disconnected)

  if [ $DISCONNECT_STATUS -eq 1 ]; then
    read -p "Du er ikke koblet til med naisdevice. Vil du koble til? (j/n) " -n 1 -r
    echo
    if [[ $REPLY =~ ^[YyjJ]$ ]]; then
      nais device connect
    else
      echo -e "${red}Du må være koblet til med naisdevice, avslutter${endcolor}"
      exit 1
    fi
  fi
fi

jq --version >& /dev/null || (
  echo "ERROR: You need to install the jq CLI tool on your machine: https://stedolan.github.io/jq/" && exit 1
) || exit 1
base64 --help >& /dev/null || (
  echo "ERROR: You need to install the base64 tool on your machine. (brew install base64 on macOS)" && exit 1
) || exit 1
which kubectl >& /dev/null || (
  echo "ERROR: You need to install and configure kubectl (see: https://confluence.adeo.no/x/UzjYF)" && exit 1
) || exit 1

declare -A kubernetes_context_namespace_secrets
declare -A kubernetes_secret_array
function fetch_kubernetes_secret {
    local context=$1
    local namespace=$2
    local secret=$3
    local path=$4
    local writefile=$5
    local name=$6
    local context_namespace_secrets_key
    local context_namespace_secrets_value
    local secret_name
    local secret_response

    context_namespace_secrets_key="$context:$namespace"

    if [ -v kubernetes_context_namespace_secrets["$context_namespace_secrets_key"] ]; then
        context_namespace_secrets_value=${kubernetes_context_namespace_secrets["$context_namespace_secrets_key"]}
    else
        context_namespace_secrets_value=$(kubectl --context="$context" -n "$namespace" get secrets)
        kubernetes_context_namespace_secrets["$context_namespace_secrets_key"]=$context_namespace_secrets_value
    fi

    secret_name=$(echo "$context_namespace_secrets_value" | grep "$secret" | tail -1 | awk '{print $1}')

    if [ -v kubernetes_secret_array["$secret_name"] ]; then
        secret_response=${kubernetes_secret_array["$secret_name"]}
    else
        secret_response=$(kubectl --context="$context" -n "$namespace" get secret "$secret_name" -o json)
        kubernetes_secret_array["$secret_name"]=$secret_response
    fi

    if [ "$writefile" == true ]; then
        echo "$secret_response" | jq -j ".data[\"$name\"]" | base64 --decode > secrets/$name
    else
        secret=$(echo "$secret_response" | jq -j ".data[\"$name\"]" | base64 --decode)
        echo "$name=$secret" >> secrets/$path.env
    fi
}

function fetch_kubernetes_secret_array {
    local type=$1
    local context=$2
    local namespace=$3
    local secret=$4
    local path=$5
    local writefile=$6
    local A=("$@")

    echo -n -e "\t- $type"

      if [ "$writefile" == false ]; then
          rm -f secrets/$path.env
      fi

    for i in "${A[@]:6}"
    do
        fetch_kubernetes_secret "$context" "$namespace" "$secret" "$path" "$writefile" "$i"
    done


    spinIndex=0
    spinStarted=false
    echo -e "${bold}${white}✔${endcolor}${normal}"
}

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

echo -e "${bold}Henter secrets fra Kubernetes${normal}"

fetch_kubernetes_secret_array "Kafka" "nais-dev" "pensjonsbrev" "aiven-pensjon-brevbaker" "kafka" false \
    "KAFKA_BROKERS" \
    "KAFKA_CREDSTORE_PASSWORD" \
    "KAFKA_SCHEMA_REGISTRY" \
    "KAFKA_SCHEMA_REGISTRY_USER" \
    "KAFKA_SCHEMA_REGISTRY_PASSWORD"

fetch_kubernetes_secret_array "Kafka" "nais-dev" "pensjonsbrev" "aiven-pensjon-brevbaker" "kafka" true \
  "client.truststore.jks"\
  "client.keystore.p12"

echo "All secrets are fetched and stored in the \"secrets\" folder."
