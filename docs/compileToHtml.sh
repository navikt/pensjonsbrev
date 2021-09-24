#!/bin/bash
sudo apt-get install asciidoctor
mkdir compile && find . -name '*.adoc' -exec cp {} compile \; && (cd compile && asciidoctor ./*.adoc)
