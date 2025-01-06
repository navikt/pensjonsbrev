#!/bin/bash
# Will update the LaTeX files while running pdf-builder. Useful for iterating on changes.
docker exec -u 0 -it pensjonsbrev_pdf-bygger_1 rm -rf /app/pensjonsbrev_latex && docker cp ./pdf-bygger/containerFiles/latex pensjonsbrev_pdf-bygger_1:/app/pensjonsbrev_latex/