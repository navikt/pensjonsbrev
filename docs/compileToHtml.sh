#!/bin/bash
mkdir compile && find . -name '*.adoc' -exec cp {} compile \; && (cd compile && asciidoctor ./*.adoc)
