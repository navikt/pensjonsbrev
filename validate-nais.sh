#!/usr/bin/env bash

# Validerer nais-manifestene våre ved å rendre hver nais.yaml med hver av vars-filene som ligger
# ved siden av den.
#
# Vars-filer skilles fra manifester på innhold, ikke på filnavn: et Kubernetes-manifest har alltid
# et `kind:`-felt på toppnivå, mens en vars-fil bare er en flat samling variabler. Det betyr at
# nye manifesttyper i .nais-katalogene - PrometheusRule, ApiToken, Topic, hva det måtte være -
# holdes utenfor automatisk, uten at noen må huske å oppdatere en unntaksliste her.
#
# Filnavn duger ikke som skille: vars-filene heter dev.yaml og prod.yaml i de fleste appene, men
# bff-dev.yaml og bff-prod.yaml i skribenten-web og brevoppskrift-web. En liste over kjente navn
# ville derfor stille sluttet å validere de appene den dagen noen la til en ny variant.
#
# Alarmreglene (kind: PrometheusRule) valideres av validate-alerts.sh i stedet, siden
# nais validate kun kjenner Application-manifester.

tabs 4

success=0

# Et Kubernetes-manifest har `kind:` på toppnivå. Vi krever at det står først på linja, slik at vi
# ikke forveksler det med et felt som tilfeldigvis heter kind lenger inne i strukturen.
function erManifest() {
    grep -q "^kind:" "$1"
}

function validateApp() {
    local appYaml="$1"
    local name=$(dirname "$appYaml")
    echo "${name##./}:"

    local kandidater=$(find "$name" -maxdepth 1 -type f \( -iname \*.yaml -o -iname \*.yml \))

    for env in $kandidater ; do
      if erManifest "$env" ; then
        continue
      fi

      local result
      local status
      local printResult
      result=$(nais -v validate --vars-file "$env" --var image=placeholder "$appYaml" 2>&1)
      status=$?

      if [ $status -gt 0 ] || echo $result | grep -q "Missing template variable" ; then
        printResult=1
        success=1
      else
        printResult=0
      fi
      echo "$(basename "$env") $status (0 er OK)"
      if [ $printResult -gt 0 ] ; then
        echo "$result" | sed 's/^/        /'
      fi
    done
    echo ""
    return $success
}

apps=$(find . -type f -name nais.yaml -not -path "*/node_modules/*")

for app in $apps ; do
  validateApp $app
done

exit $success
