#!/usr/bin/env bash

tabs 4

success=0

function validateApp() {
    local appYaml="$1"
    local name=$(dirname "$appYaml")
    echo "${name##./}:"

    local envs=$(find "$name" -maxdepth 1 -type f \( -iname \*.yaml -o -iname \*.yml \) -not -iname \*azure\* -not -iname nais.yaml -not -iname unleash\* -not -iname hpa.yaml -not -iname \*topic\*)

    for env in $envs ; do
      local result
      local status
      local printResult
      result=$(nais -v validate --vars-file "$env" --var image=placeholder "$appYaml" 2>&1)
      status=$?

      if [ $status -gt 0 ] || echo $result | grep -q "Missing template variable for" ; then
        echo -e -n "\e[1;31m ❌\e[00m"
        printResult=1
        success=1
      else
        echo -e -n "\e[0;32m ✓ \e[00m"
        printResult=0
      fi
      echo -e "\t $(basename "$env")"
      if [ $printResult -gt 0 ] ; then
        echo "$result" | sed 's/^/        /'
      fi
    done
    echo ""
    return $success
}

apps=$(find . -type f -name nais.yaml)

for app in $apps ; do
  validateApp $app
done

exit $success