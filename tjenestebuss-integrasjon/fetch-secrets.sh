#!/bin/bash

KUBE_CLUSTER="dev-fss"

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


export VAULT_ADDR=https://vault.adeo.no
vault --version >& /dev/null || (
  echo "ERROR: You need to install the Vault CLI on your machine: https://www.vaultproject.io/downloads.html" && exit 1
) || exit 1


while true; do
	NAME="$(vault token lookup -format=json | jq '.data.display_name' -r; exit ${PIPESTATUS[0]})"
  ret=${PIPESTATUS[0]}
  if [ $ret -ne 0 ]; then
    echo "Looks like you are not logged in to Vault."

    read -p "Do you want to log in? (y/n) " -n 1 -r
    echo    # (optional) move to a new line
    if [[ $REPLY =~ ^[Yy]$ ]]
    then
      vault login -method=oidc -no-print
    else
      echo "Could not log in to Vault. Aborting."
      exit 1
    fi
  else
    break;
  fi
done

mkdir -p secrets/pensjonsbrev

SAMHANDLERV2_USERNAME=$(vault kv get -field SAMHANDLERV2_USERNAME kv/preprod/fss/pensjonsbrev-tjenestebuss-q2/pensjonsbrev)
SAMHANDLERV2_PASSWORD=$(vault kv get -field SAMHANDLERV2_PASSWORD kv/preprod/fss/pensjonsbrev-tjenestebuss-q2/pensjonsbrev)
jq --null-input \
 --arg samhandlerv2_username "$SAMHANDLERV2_USERNAME" \
 --arg samhandlerv2_password "$SAMHANDLERV2_PASSWORD" \
  '{"PENSJONSBREV_SAMHANDLERV2_USERNAME": $samhandlerv2_username,
    "PENSJONSBREV_SAMHANDLERV2_PASSWORD": $samhandlerv2_password}' > secrets/pensjonsbrev/auth.json
kubectl --context $KUBE_CLUSTER -n pensjonsbrev get secret azure-pensjonsbrev-tjenestebuss-lokal -o json | jq '.data | map_values(@base64d)' > secrets/azuread.json
echo "Creating docker env file from secrets..."
jq -r 'to_entries|map("\(.key)=\(.value|tostring)")|.[]' secrets/azuread.json > secrets/docker.env
jq -r 'to_entries|map("\(.key)=\(.value|tostring)")|.[]' secrets/pensjonsbrev/auth.json >> secrets/docker.env
echo "docker.env file created in the \"secrets\" folder."
echo "All secrets are fetched and stored in the \"secrets\" folder."
