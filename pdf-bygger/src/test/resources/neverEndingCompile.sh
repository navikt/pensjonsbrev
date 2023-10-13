outputFile=${1%.*}.pdf

while true ; do
    echo "kompilerer $1" >> "$outputFile"
    sleep 1
done