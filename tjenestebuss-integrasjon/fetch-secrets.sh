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

mkdir -p secrets/sts

vault kv get -field username serviceuser/dev/srvpensjonsbrev-esb > secrets/sts
vault kv get -field password serviceuser/dev/srvpensjonsbrev-esb > secrets/sts

