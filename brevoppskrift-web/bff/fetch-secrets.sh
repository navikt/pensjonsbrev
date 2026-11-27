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

temp_file="$(mktemp .env.XXXXXX)"
trap 'rm -f -- "$temp_file"' EXIT

secret_name="$(
  nais app env brevoppskrift-web --team pensjonsbrev --environment dev-gcp --verbose --output json |
    jq -er '[.[] | select(.source.kind == "SECRET") | .source.name | select(startswith("azure-brevoppskrift-web"))] | unique |
      if length == 1 then .[0] else error("Expected one Azure secret for brevoppskrift-web; deploy the dev application first.") end'
)"

nais secret get "$secret_name" --team pensjonsbrev --environment dev-gcp --with-values --reason "local development" --output json |
  jq -er '.data | from_entries | select(length > 0) | to_entries[] | "\(.key)=\(.value)"' > "$temp_file"

mv -- "$temp_file" .env
echo "Local BFF Azure configuration saved to brevoppskrift-web/bff/.env."
