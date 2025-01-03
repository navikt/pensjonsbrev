# Oppdatering av LaTeX
docker build -t debian_latex:latest -f ./pdf-bygger/latex.Dockerfile . && docker-compose build --no-cache pdf-bygger && docker-compose down && docker-compose up -d --build
# Visuell diff
## magick
magick -density 150 ~/IdeaProjects/pensjonsbrev/pensjon-brevbaker/build/design/1.pdf ~/IdeaProjects/pensjonsbrev/pensjon-brevbaker/build/test_pdf/DESIGN_REFERENCE_LETTER_BOKMAL.pdf -evaluate-sequence Xor ~/IdeaProjects/pensjonsbrev/pensjon-brevbaker/build/output.pdf

## diffpdf
