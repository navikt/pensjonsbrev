%! Class = CLASS_NAME
%! Author = Håkon Heggholmen
%! Date = 29.06.2021

\NeedsTeXFormat{LaTeX2e}
%! suppress = PackageNameDoesNotMatchFileName
\ProvidesPackage{pensjonsbrev_v2}[Team pensjonsbrev's Document Class]
\LoadClass[version=last,
    foldmarks=false,
    DIV=15,
    addrfield=backgroundimage]{scrlttr2}
\include{params}

% Packages
\usepackage{amsmath}
\usepackage{graphicx}
\usepackage{setspace}
\usepackage{fontspec}
\usepackage{xcolor}
\setsansfont{Arial}
\setmainfont{Times New Roman}

%fonts
\newfontface{\grayArialFont}{Arial}[Color=888888]
\newfontface{\arialFont}{Arial}


%heading / return address
\setplength{firstheadhpos}{20mm}
\setplength{firstheadvpos}{13mm}
\setkomavar{fromname}[returadresse]{
    \newline \parskip=0pt \baselineskip=10pt
        \tinyText{Returadresse: NAV Familie- og pensjonsytelser Steinkjer} \\ \tinyText{Postboks 6600 Etterstad, 0607 OSLO}
}
%\setkomavar{fromname}[returadresse]{\onehalfspacing \parskip=0pt \tinyText{ Returadresse: NAV Familie- og pensjonsytelser Steinkjer}}
%\setkomavar{fromaddress}[returadresse ]{\onehalfspacing \parskip=0pt \tinyText{ Postboks 6600 Etterstad, 0607 OSLO}}

% Global variables
\newcommand{\standardtextsize}{12pt}
\newcommand{\tinytextsize}{8pt}

% Font commands
\newcommand{\tinyGrayText}[1]{{\grayArialFont \fontsize{8pt}{8pt} \selectfont #1}}
\newcommand{\tinyText}[1]{{\arialFont \fontsize{8pt}{8pt} \selectfont #1}}

% page numbering
\pagenumbering{arabic}

% general distances
\setlength{\parindent}{0em}
\setlength{\parskip}{1em}

% to address
\setplength{toaddrheight}{170pt}

% logo
\setkomavar{location}{\input{nav-logo.pdf_tex}}
\setplength{locheight}{10mm}
\setplength{lochpos}{35mm} % distance from right
\setplength{locvpos}{21mm} % distance from top
\setplength{locwidth}{10mm}
\newcommand{\svgwidth}{25mm} % Nav-logo width

% Date
\setplength{refhpos}{8pt}
\setkomavar{date}{TESTDATO}

%EXPERIMENTAL
%-----------
\setplength{refhpos}{1mm}
%\setplength{toaddrhpos}{1mm}
%\setplength{firstfoothpos}{1mm}

%-----------

\setkomavar{toaddress}[description ]{
    \uppercase{\feltgatenavnmottaker \space \felthusnummermottaker
    \\ \feltpoststedmottaker, \feltpostnummermottaker }}


\newcommand{\hentVedlegg}[1]{\par \input{vedlegg/#1}}
\newcommand{\tittel}[1]{\setkomavar{subject}{#1}\opening{}}
\newcommand{\infofield}[1]{{\color{gray}\footnotesize #1}}
\newcommand{\brevseksjon}[1]{\par \textbf{#1} \newline}
\newcommand{\brevparameter}{
    \feltfornavnmottaker \space \feltetternavnmottaker \newline
    \uppercase{\feltgatenavnmottaker \space \felthusnummermottaker}  \newline
    \feltpostnummermottaker \space \feltpoststedmottaker}