#!/bin/bash
#Compares the files in brevbaker/test_visual/pdf and pdf_original
# pdf_original needs to be a copy of the pdfs before template changes.
folder=../../../build/test_visual
original_files=$folder/original
compare_to_folder=$folder/pdf
mogrify_folder=$folder/mogrify
output_folder=$folder/out
mkdir -p $folder/out
mkdir -p $mogrify_folder
magick mogrify -path $mogrify_folder -format png -background white -alpha remove -alpha off -density 200 -quality 85 $compare_to_folder/*.pdf
for absolutefilename in $original_files/*.png; do
      filename=$(basename "$absolutefilename")
      echo -e "\n-------------------------------"
      echo comparing $filename
      magick compare -metric MAE -density 150 -compose multiply $original_files/$filename $mogrify_folder/$filename $output_folder/$filename
      echo -e "\n-------------------------------"
done