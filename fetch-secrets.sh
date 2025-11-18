#!/usr/bin/env bash

gcloud auth print-access-token >& /dev/null || (
  read -p "Inlogging i GCP er utløpt. Vil du autentisere på nytt? (J/n) " -n 1 -r -s
  echo
  if [[ $REPLY == "" || $REPLY =~ ^[YyjJ]$ ]]; then
    gcloud auth login --update-adc
  else
    echo -e "${red}Du må ha en gyldig innlogging i GCP. Du kan logge inn med 'gcloud auth login', avslutter${endcolor}"
    exit 1
  fi
) || exit 1

 (cd skribenten-backend && ./fetch-secrets.sh)
 (cd tjenestebuss-integrasjon && ./fetch-secrets.sh)
 (cd skribenten-web/bff && ./fetch-secrets.sh)
 (cd pensjon-brevbaker && ./fetch-secrets.sh)