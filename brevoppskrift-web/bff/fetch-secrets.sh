#!/usr/bin/env bash
set -euo pipefail
umask 077

for tool in nais jq; do
  command -v "$tool" >/dev/null || {
    echo "ERROR: Install $tool before fetching secrets." >&2
    exit 1
  }
done

cd -- "$(dirname -- "${BASH_SOURCE[0]}")"

# Use the local registration until Entra ID is enabled for the dev BFF.
secret_name="azure-brevoppskrift-web-local"

temp_file="$(mktemp .env.XXXXXX)"
trap 'rm -f -- "$temp_file"' EXIT

nais secret get "$secret_name" --team pensjonsbrev --environment dev-gcp --with-values --reason "local development" --output json |
  jq -er '.data | from_entries | select(length > 0) | to_entries[] | "\(.key)=\(.value)"' > "$temp_file"

mv -- "$temp_file" .env
echo "Local BFF Azure configuration saved to brevoppskrift-web/bff/.env."
