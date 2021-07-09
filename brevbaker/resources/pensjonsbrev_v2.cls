%! Class = CLASS_NAME
%! Author = Håkon Heggholmen
%! Date = 29.06.2021


%----------------------------------------
%            Document class
%----------------------------------------

\NeedsTeXFormat{LaTeX2e}
%! suppress = PackageNameDoesNotMatchFileName
\ProvidesPackage{pensjonsbrev_v2}[Team pensjonsbrev's Document Class]
\LoadClass[version=last,
    foldmarks=false,
    subject=left,
    DIV=16,
    parskip=full,
%    visualize, %DEBUG show boxes
%    draft=true, % DEBUG show overfull boxes, collisions etc.
    addrfield=backgroundimage]{scrlttr2}
%\showfields{head,address,location,refline,foot} %DEBUG



%----------------------------------------
%               Imports
%----------------------------------------
\usepackage{amsmath}
\usepackage{graphicx}
\usepackage{setspace}
\usepackage{fontspec}
\usepackage{xcolor}
\setsansfont{Arial}
\setmainfont{Times New Roman}
\usepackage{babel} %TODO bytt avhengig av språk og målform
\include{params}


%----------------------------------------
%                Global
%----------------------------------------

%fonts
\newfontface{\grayArialFont}{Arial}[Color=888888]
\newfontface{\arialFont}[BoldFont={Arial Bold}]{Arial}
\newfontface{\timesNewRomanFont}[BoldFont={Times New Roman Bold}]{Times New Roman}

% Global font commands
\newcommand{\standardtextsize}{12pt}
\newcommand{\tinytextsize}{8pt}
\newcommand{\tinyText}[1]{{\arialFont \fontsize{8pt}{8pt} \selectfont #1}}
\setparsizes{0pt}{1em}{0pt plus 1fil}

% page numbering
\pagenumbering{arabic}


%----------------------------------------
%                 Logo
%----------------------------------------
\setkomavar{location}{\input{nav-logo.pdf_tex}}
\setplength{locheight}{17mm}
\setplength{locwidth}{26mm}
\setplength{lochpos}{19mm} % distance from right
\setplength{locvpos}{21mm} % distance from top
\newcommand{\svgwidth}{25mm} % Nav-logo width


%----------------------------------------
%         heading / return address
%----------------------------------------
\setplength{firstheadhpos}{19mm} %distance from left
\setplength{firstheadvpos}{13mm} %distance from top
\setplength{firstheadwidth}{100mm}
\setkomavar{fromname}[returadresse]{
    \newline
    \parskip=0pt
    \baselineskip=5pt
    \tinyText{Returadresse: NAV Familie- og pensjonsytelser Steinkjer}\newline
    \tinyText{Postboks 6600 Etterstad, 0607 OSLO}
}


%----------------------------------------
%                  Date
%----------------------------------------
\setplength{toaddrheight}{100pt}
\setplength{refwidth}{50mm}
\KOMAoption{refline}{dateleft}
\setplength{refhpos}{159mm} %distance from left
\setplength{refvpos}{84mm} %distance from top
\setkomavar{date}{\tinyText{Dato:}\hspace{1pt}6. mai 2021}


%----------------------------------------
%              To address
%----------------------------------------
\setplength{toaddrhpos}{12mm}
\setplength{toaddrvpos}{36mm}
\newcommand{\brevparameter}{
    \arialFont \fontsize{12pt}{12pt} \selectfont
    \feltfornavnmottaker \space \feltetternavnmottaker \newline
    \feltgatenavnmottaker \space \felthusnummermottaker  \newline
    \feltpostnummermottaker \space \feltpoststedmottaker}


%----------------------------------------
%           Title and opening
%----------------------------------------

%Title font
\newfontface{\titleFont}[BoldFont={Times New Roman Bold}]{Times New Roman}
\newcommand{\titleText}[1]{\newline \titleFont \fontsize{16pt}{16pt} \selectfont \textbf{#1}}

\setplength{refaftervskip}{10mm} %vertical distance between title and reference field (containing date)

\newlength{\vskipafteropening} % distance between opening and first section title
\setlength{\vskipafteropening}{\dimexpr(-\baselineskip-3pt)} %removing the baseline skip and 5 pts

%opening prefix font
\newcommand{\tinyGrayText}[1]{{\grayArialFont \fontsize{8pt}{8pt} \selectfont #1}}


\setplength{subjectaftervskip}{2mm}
\newcommand{\tittel}[1]{ %command for creating title in the document
    \setkomavar{subject}{
        \titleText{#1}
    }\opening{ %case and person information after title
        \fontsize{12pt}{14pt} \selectfont
        \tinyGrayText{NAVs saksnummer:} 25305007 \\
        \tinyGrayText{Navn:} \feltetternavnmottaker \space \feltfornavnmottaker \\
        \tinyGrayText{Fødselsnummer:} \feltfoedselsnummer \\
        \vspace*{\vskipafteropening} %vertical space before letter content
    }
}


%----------------------------------------
%               Content
%----------------------------------------
% distance between opening and letter content in pt's
\newif\ifaddspaceover
\addspaceoverfalse

\newlength{\vskipbeforesection} % distance between sections
\setlength{\vskipbeforesection}{10pt}

\newlength{\vskipaftersectiontitle} \setlength{\vskipaftersectiontitle}{5pt}
\newcommand{\lettersection}[2]{

    % Add space between sections
    \ifaddspaceover %If not in the first section, add space above.
        \vspace*{\vskipbeforesection}
    \fi
    \addspaceovertrue %toggle on so that all sections following the first one gets space over the title.

    \par\textbf{#1} %title
    \vspace*{\vskipaftersectiontitle}
    \newline
    #2 %section content
}

\textwidth=160mm %max width of title, opening and content section(latex may override a little to make text nicer)


%----------------------------------------
%               Footer
%----------------------------------------
\setplength{firstfootvpos}{254.5mm} %absolute vertical posistion from top of page
\setlength{\footskip}{5pt} % space between letter content and footer
\newcommand{\footerText}[1]{{\arialFont \fontsize{8pt}{8pt} \selectfont #1}} %footer font command

%footer content
\setkomavar{firstfoot}{%
    \newline\noindent\rule{\textwidth}{0.16mm}\vspace*{3.7mm} % footer top line
    \footerText{
        \textbf{NAV Familie-og pensjonsytelser Steinkjer}
        \newline Postadresse: NAV Familie-og pensjonsytelser Steinkjer // Postboks 6600 Etterstad // 0607 Oslo
        \newline Telefon: 55553334
        \newline nav.no
    }
}



