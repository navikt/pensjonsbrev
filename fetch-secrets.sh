#!/usr/bin/env bash

 (cd skribenten-backend && ./fetch-secrets.sh)
 (cd tjenestebuss-integrasjon && ./fetch-secrets.sh)
 (cd skribenten-web/bff && python3 setup_local_azure_secrets.py)