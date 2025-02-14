#!/usr/bin/env bash

tabs 4

function validateApp() {
    local app="$1"
    local name=$(dirname "$app")
    echo "$(basename "$name"):"

    local envs=$(find "$app" -type f \( -iname \*.yaml -o -iname \*.yml \) -not -iname \*azure\* -not -iname nais.yaml -not -iname kafka-hpa.yaml -not -iname unleash\*)

    for env in $envs ; do
      local result
      local status
      result=$(nais validate --vars "$env" --var image=placeholder "$app/nais.yaml" 2>&1)
      status=$?
      if [ $status -eq 0 ] ; then
        echo -e -n "\e[0;32m ✓ \e[00m"
      else
        echo -e -n "\e[1;31m ❌\e[00m"
      fi
      echo -e "\t $(basename "$env")"
      if [ $status -gt 0 ] ; then
        echo "$result" | sed 's/^/        /'
      fi
    done
    echo ""
}

apps=$(find . -type d -name .nais)

for app in $apps ; do
  validateApp $app
done