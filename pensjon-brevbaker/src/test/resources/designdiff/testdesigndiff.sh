#!/bin/bash
# this script requires imagemagick to function
diffpdf -a ./design/6.pdf ../../../../build/test_design_diff/design_test_page_6.pdf
#folder=../../../../build/test_design_diff
#for absolutefilename in $folder/pdf_original/*; do
      #filename=$(basename "$absolutefilename")
      #mkdir -p $folder/out
      #magick compare -density 200 -compose multiply $folder/pdf_original/$filename $folder/pdf/$filename $folder/out/$filename
#done