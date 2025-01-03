#!/bin/bash
folder=../../../../build/test_visual
for absolutefilename in $folder/pdf_original/*; do
      filename=$(basename "$absolutefilename")
      mkdir -p $folder/out
      magick compare -density 200 -compose multiply $folder/pdf_original/$filename $folder/pdf/$filename $folder/out/$filename
done