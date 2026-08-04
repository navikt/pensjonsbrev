#!/usr/bin/env bash

# Validerer alarmreglene våre med promtool.
#
# promtool forstår ikke PrometheusRule-manifestet i seg selv, kun innholdet i spec:. Skriptet
# pakker derfor ut spec: til en midlertidig fil før validering. Det som faktisk sjekkes er at
# PromQL-uttrykkene parser, at for-periodene er gyldige varigheter, og at Go-templatene i
# annotasjonene (`{{ $labels.pod }}`, `{{ $value }}`) er syntaktisk gyldige.
#
# Merk at promtool ikke kan vite om metrikkene finnes eller om labelnavnene stemmer - det avdekkes
# først når reglene kjører mot Mimir.
#
# promtool hentes fra PATH, eller fra PROMTOOL hvis den er satt.

set -uo pipefail

PROMTOOL="${PROMTOOL:-promtool}"

if ! command -v "$PROMTOOL" > /dev/null 2>&1 ; then
    echo "Fant ikke promtool. Sett PROMTOOL=/sti/til/promtool, eller legg den på PATH."
    exit 1
fi

if ! python3 -c "import yaml" > /dev/null 2>&1 ; then
    echo "Mangler pyyaml. Installer med: pip install pyyaml"
    exit 1
fi

success=0

# Leter etter alle PrometheusRule-manifester framfor å liste dem opp, slik at en ny alarmfil
# valideres automatisk uten at noen må huske å oppdatere dette skriptet.
files=$(grep -rl --include=\*.yaml --include=\*.yml "kind: PrometheusRule" . \
    --exclude-dir=node_modules --exclude-dir=build --exclude-dir=.git)

if [ -z "$files" ] ; then
    echo "Fant ingen PrometheusRule-manifester."
    exit 1
fi

for file in $files ; do
    navn="${file#./}"
    spec=$(mktemp --suffix=.yaml)

    # Feilmeldingen fanges og skrives ut selv, slik at en ugyldig fil gir én lesbar linje i stedet
    # for en Python-traceback.
    uttrekk=$(python3 -c "
import sys, yaml
try:
    with open(sys.argv[1]) as f:
        doc = yaml.safe_load(f)
    with open(sys.argv[2], 'w') as f:
        yaml.safe_dump(doc['spec'], f, allow_unicode=True, sort_keys=False)
except yaml.YAMLError as e:
    sys.exit('ugyldig YAML: ' + str(e).replace('\n', ' '))
except KeyError:
    sys.exit('mangler spec:')
" "$file" "$spec" 2>&1)
    status=$?

    if [ $status -gt 0 ] ; then
        echo "${navn}: $status (0 er OK)"
        echo "$uttrekk" | sed 's/^/        /'
        success=1
        rm -f "$spec"
        continue
    fi

    result=$("$PROMTOOL" check rules "$spec" 2>&1)
    status=$?
    rm -f "$spec"

    antall=$(grep -c "^\s*- alert:" "$file")
    echo "${navn}: $status (0 er OK), $antall regler"
    if [ $status -gt 0 ] ; then
        echo "$result" | sed 's/^/        /'
        success=1
    fi
done

exit $success
