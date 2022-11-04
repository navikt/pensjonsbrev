FROM debian
USER root
LABEL org.opencontainers.image.source="https://github.com/navikt/pensjonsbrev/latexinstallation"
LABEL org.opencontainers.image.description="XeLaTeX installation with required packages for NAVs letter template"
LABEL org.opencontainers.image.title="XeLaTeX"
ENV DEBIAN_FRONTEND="noninteractive" TZ="Europe/London"
ENV PATH="${PATH}:/app/tex/bin/x86_64-linux/"

#Download and install tlmgr (texlive package manager)
RUN apt -y --allow-releaseinfo-change -o Acquire::Check-Valid-Until=false update
RUN apt -y install tzdata perl-tk wget
RUN wget https://mirror.ctan.org/systems/texlive/tlnet/install-tl-unx.tar.gz
RUN tar -xf install-tl-unx.tar.gz
RUN mv ./install-tl*/ install-tl
RUN chown -R root install-tl
RUN ./install-tl/install-tl --no-interaction -s f -portable -texdir /app/tex -texuserdir /app/texlocal

#Install xetex and required packages from uib using texlive package manager
RUN tlmgr option repository https://mirror.ctan.org/systems/texlive/tlnet/
RUN tlmgr install xetex
RUN tlmgr install collection-latex fontspec ninecolors xcolor tabularray nowidow enumitem textpos pdfx xmpincl
