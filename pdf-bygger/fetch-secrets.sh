#!/usr/bin/env bash

bold=$(tput bold)
normal=$(tput sgr0)
white="[97;1m"
yellow="[33;1m"
endcolor="[0m"

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

set -e
set -o pipefail

spin[0]="⠻"
spin[1]="⠽"
spin[2]="⠾"
spin[3]="⠷"
spin[4]="⠯"
spin[5]="⠟"

spinIndex=0
spinStarted=false
spinElements=6

declare -A kubernetes_context_namespace_secrets
declare -A kubernetes_secret_array

function doSpin {
    if "$spinStarted"; then
        echo -ne "\b${spin[spinIndex]}"
    else
        echo -ne "${spin[spinIndex]}"
    fi
    spinStarted=true
    spinIndex=$(((spinIndex+1) % $spinElements))
}

function fetch_kubernetes_secret {
    local context=$1
    local namespace=$2
    local secret=$3
    local path=$4
    local name=$5
    local context_namespace_secrets_key
    local context_namespace_secrets_value
    local secret_name
    local secret_response

    context_namespace_secrets_key="$context:$namespace"

    if [ -v kubernetes_context_namespace_secrets["$context_namespace_secrets_key"] ]; then
        context_namespace_secrets_value=${kubernetes_context_namespace_secrets["$context_namespace_secrets_key"]}
    else
        echo "kubectl --context="$context" -n "$namespace" get secrets"
        context_namespace_secrets_value=$(kubectl --context="$context" -n "$namespace" get secrets)
        kubernetes_context_namespace_secrets["$context_namespace_secrets_key"]=$context_namespace_secrets_value
    fi

    secret_name=$(echo "$context_namespace_secrets_value" | grep "$secret" | tail -1 | awk '{print $1}')

    if [ -v kubernetes_secret_array["$secret_name"] ]; then
        secret_response=${kubernetes_secret_array["$secret_name"]}
    else
        echo "kubectl --context="$context" -n "$namespace" get secret "$secret_name" -o json"
        secret_response=$(kubectl --context="$context" -n "$namespace" get secret "$secret_name" -o json)
        kubernetes_secret_array["$secret_name"]=$secret_response
    fi

    echo "$secret_response" | jq -j ".data[\"$name\"]" | base64 --decode > secrets/$env/$path/$name

    echo -e "${bold}${white}✔${endcolor}${normal}"
}

function fetch_kubernetes_secret_array {
    local type=$1
    local context=$2
    local namespace=$3
    local secret=$4
    local path=$5
    local A=("$@")

    echo -n -e "\t- $type \n"

    mkdir -p "secrets/$env/$path"

    for i in "${A[@]:5}"
    do
        fetch_kubernetes_secret "$context" "$namespace" "$secret" "$path" "$i"
    done


    spinIndex=0
    spinStarted=false
    echo -e "\b${bold}${white}✔${endcolor}${normal}"
}

echo -e "${bold}Henter secrets fra Kubernetes${normal}"

fetch_kubernetes_secret_array "Kafka" "nais-dev" "pensjonsbrev" "aiven-pensjonsbrev-pdf-bygger" "kafka" \
    "KAFKA_BROKERS" \
    "KAFKA_CREDSTORE_PASSWORD" \
    "KAFKA_SCHEMA_REGISTRY" \
    "KAFKA_SCHEMA_REGISTRY_USER" \
    "KAFKA_SCHEMA_REGISTRY_PASSWORD"


echo -e "\b${bold}${white}✔${endcolor}${normal}"
