#!/bin/bash
#Compares the files in brevbaker/test_visual/pdf and pdf_original
# pdf_original needs to be a copy of the pdfs before template changes.
folder=../../../../build/test_visual
original_files=$folder/pdf_original
compare_to_folder=$folder/pdf
output_folder=$folder/out
mkdir -p $folder/out
for absolutefilename in $original_files/*; do
      filename=$(basename "$absolutefilename")
      magick compare -density 200 -compose multiply $original_files/$filename $compare_to_folder/$filename $output_folder
done