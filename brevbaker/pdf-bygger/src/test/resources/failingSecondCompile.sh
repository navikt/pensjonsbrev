outputFile="${1%.*}.pdf"

if [ ! -f "$outputFile" ] ; then
  echo "kompilerer $1"
  echo "kompilerer $1" >> "$outputFile"
else
  echo "feilet $1" 1>&2
  exit 1
fi