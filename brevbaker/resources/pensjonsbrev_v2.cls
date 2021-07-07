%! Class = CLASS_NAME
%! Author = Håkon Heggholmen
%! Date = 29.06.2021

\NeedsTeXFormat{LaTeX2e}
%! suppress = PackageNameDoesNotMatchFileName
\ProvidesPackage{pensjonsbrev_v2}[Team pensjonsbrev's Document Class]
\LoadClass[version=last,
    foldmarks=false,
    subject=left,
    parskip=never,
    DIV=15,
    draft=true, % TODO remove before production. Used for debugging
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
\newfontface{\timesNewRomanFont}{Times New Roman}
\newfontface{\titleFont}[BoldFont={Times New Roman Bold}]{Times New Roman}


%heading / return address
\setplength{firstheadhpos}{20mm}
\setplength{firstheadvpos}{13mm}
\setplength{firstheadwidth}{100mm}
\setkomavar{fromname}[returadresse]{
    \newline
    \parskip=0pt
    \baselineskip=5pt
    \tinyText{Returadresse: NAV Familie- og pensjonsytelser Steinkjer}\newline
    \tinyText{Postboks 6600 Etterstad, 0607 OSLO}
}
%\setkomavar{fromname}[returadresse]{\onehalfspacing \parskip=0pt \tinyText{ Returadresse: NAV Familie- og pensjonsytelser Steinkjer}}
%\setkomavar{fromaddress}[returadresse ]{\onehalfspacing \parskip=0pt \tinyText{ Postboks 6600 Etterstad, 0607 OSLO}}

% Global variables


% Font commands
\newcommand{\standardtextsize}{12pt}
\newcommand{\tinytextsize}{8pt}
\newcommand{\tinyGrayText}[1]{{\grayArialFont \fontsize{8pt}{8pt} \selectfont #1}}
\newcommand{\tinyText}[1]{{\arialFont \fontsize{8pt}{8pt} \selectfont #1}}
\newcommand{\titleText}[1]{
    \newline \titleFont \fontsize{16pt}{16pt} \selectfont \parindent=0pt \textbf{#1 }}

% page numbering
\pagenumbering{arabic}

% general distances
\setlength{\parindent}{0em}
\setlength{\parskip}{1em}

% to address
\setplength{toaddrheight}{170pt}

% logo
\setkomavar{location}{\input{nav-logo.pdf_tex}}
\setplength{locheight}{26mm}
\setplength{locwidth}{26mm}
\setplength{lochpos}{19mm} % distance from right
\setplength{locvpos}{21mm} % distance from top
\newcommand{\svgwidth}{25mm} % Nav-logo width

% Date
\setplength{refwidth}{100mm}
\setplength{refhpos}{88mm} %distance from left
\setplength{refvpos}{83.7mm} %distance from top
\setkomavar{date}{\tinyText{Dato:}\hspace{1pt
}6. mai 2021}
%\KOMAoption{refline}{dateleft}

% To address
\setplength{toaddrhpos}{12mm}
\setplength{toaddrvpos}{23.5mm}
\newcommand{\brevparameter}{
    \arialFont \fontsize{12pt}{14pt} \selectfont
    \feltfornavnmottaker \space \feltetternavnmottaker \newline
    \feltgatenavnmottaker \space \felthusnummermottaker  \newline
    \feltpostnummermottaker \space \feltpoststedmottaker}
%\setkomavar{toaddress}[description ]{
%    \arialFont \fontsize{12pt}{8pt} \selectfont
%    \feltgatenavnmottaker \space \felthusnummermottaker
%    \\ \feltpoststedmottaker, \feltpostnummermottaker }

%Content (block starting with letter title)
\setplength{refaftervskip}{10mm} %vertical distance from reference field (containing date)
    %Title
    \setplength{subjectaftervskip}{2mm}
    \newcommand{\tittel}[1]{ %command for creating title in the document
        \setkomavar{subject}{
            \titleText{#1}
        }\opening{ %case and person information after title
            \tinyGrayText{NAVs saksnummer:} 25305007 \\
            \tinyGrayText{Navn:} \feltetternavnmottaker \space \feltfornavnmottaker \\
            \tinyGrayText{Fødselsnummer:} \feltfoedselsnummer \\
        }
    }





\newcommand{\infofield}[1]{{\color{gray}\footnotesize #1}}
\newcommand{\brevseksjon}[1]{\par \textbf{#1} \newline}
\newcommand{\hentVedlegg}[1]{\par \input{vedlegg/#1}}
