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

mkdir -p secrets
temp_dir="$(mktemp -d secrets/.azuread.XXXXXX)"
trap 'rm -f -- "$temp_dir/azuread.json" "$temp_dir/azuread.env"; rmdir -- "$temp_dir"' EXIT

nais secret get azure-brevoppskrift-backend-local --team pensjonsbrev --environment dev-gcp --with-values --reason "local development" --output json |
  jq -e '.data | from_entries | select(length > 0)' > "$temp_dir/azuread.json"
jq -r 'to_entries[] | "\(.key)=\(.value)"' "$temp_dir/azuread.json" > "$temp_dir/azuread.env"

mv -- "$temp_dir/azuread.json" secrets/azuread.json
mv -- "$temp_dir/azuread.env" secrets/azuread.env
echo "Azure configuration saved to brevoppskrift-backend/secrets/azuread.json and azuread.env."
