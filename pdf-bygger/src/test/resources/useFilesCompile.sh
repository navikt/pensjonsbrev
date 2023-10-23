outputFile="${1%.*}.pdf"

cat "f1.txt" > "$outputFile"
echo "" >> "$outputFile"
cat "f2.txt" >> "$outputFile"