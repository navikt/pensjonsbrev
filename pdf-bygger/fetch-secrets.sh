#!/usr/bin/env bash
# secret ble laget med:
#nais aiven create -p nav-dev -e 100 -s aiven-pensjon-pdf-bygger-async-q2-local kafka pensjon-pdf-bygger-async-q2 pensjonsbrev

nais aiven tidy
nais aiven get kafka aiven-pensjon-pdf-bygger-async-q2-local pensjonsbrev
rm -rf ./secrets
mv /tmp/aiven-secret* ./secrets

# Erstatt paths i filer for å kunne mounte fila riktig i pdf-bygger containeren. Laget for å få riktig path i container
find ./secrets/* -type f -exec sed -i 's/\/tmp\/aiven-secret-.*\//\/secrets\//g' {} \;

#rett opp i kcat conf (brukes kun utenfor container)
sed -i 's/\/secrets\//\.\//g' ./secrets/kcat.conf

echo -e "Kafka secrets hentet til ./secrets"