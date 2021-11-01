%! Class = pensjonsbrev_v2
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
    subject=left, % Left alignment of the title
    DIV=16,
    parskip=full, %allways have a vertical space between paragraphs(instead of indentation)
    pagenumber=off,
    twoside,
%    visualize, %DEBUG show boxes
%    draft=true, % DEBUG show overfull boxes, collisions etc.
    addrfield=backgroundimage]{scrlttr2}
%\showfields{head,address,location,refline,foot} %DEBUG


%----------------------------------------
%               Imports
%----------------------------------------
\usepackage{amsmath}
\usepackage{graphicx}   %graphics such as logo
\usepackage{setspace}   %
\usepackage{fontspec}   %declare font presets
\usepackage{xcolor}     %define colors
\usepackage{scrlayer}   % creating custom layers/custom posistioned fields
\usepackage{needspace}
\usepackage{geometry}   % adjusting margins
\usepackage{enumitem,amssymb} % for formChoice list

%! suppress = FileNotFound
\include{params}  % Must be done before pdfx is imported

\usepackage{pdfx}       % create pdf/A compliant pdf's
\setsansfont{Source Sans Pro}
\setmainfont{Source Sans Pro}


%----------------------------------------
%                Global
%----------------------------------------

%fonts
\newfontface{\grayFont}{Source Sans Pro}[Color=888888]
\newfontface{\secondaryFont}[BoldFont={Source Sans Pro Bold}]{Source Sans Pro}
\newfontface{\primaryFont}[BoldFont={Source Sans Pro Bold}]{Source Sans Pro}

% Global font commands
\newcommand{\standardtextsize}{12pt}
\newcommand{\tinytextsize}{8pt}
\newcommand{\tinyText}[1]{{\secondaryFont \fontsize{8pt}{8pt} \selectfont #1}}
\setparsizes{0pt}{1em}{0pt plus 1fil}

%itemize / lists spacing
\setlist{nolistsep}


% margins of the letter content
\geometry{left=2cm, right=2.5cm, top=2cm, bottom=3.5cm}

% paragraph formatting
\sloppy % no word-breaking
\raggedright % no word spacing


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
    \tinyText{\feltreturadresseenhetprefix \space \feltnavenhet}\newline
    \tinyText{\feltreturadresse \space \feltreturadressepostnrsted}
}


%----------------------------------------
%                  Date
%----------------------------------------
\setplength{toaddrheight}{150pt}
\setplength{refwidth}{50mm}
\KOMAoption{refline}{dateleft}
\setplength{refhpos}{159mm} %distance from left
\setplength{refvpos}{84mm} %distance from top
\setkomavar{date}{\tinyText{\feltdatoprefix}\hspace{1pt}\feltdato}


%----------------------------------------
%              To address
%----------------------------------------
\setplength{toaddrhpos}{12mm}
\setplength{toaddrvpos}{36mm}
\newcommand{\brevparameter}{
    \secondaryFont \fontsize{12pt}{12pt} \selectfont
    \feltmottakeradresselineone \newline
    \feltmottakeradresselinetwo \newline
    \feltmottakeradresselinethree \newline
    \feltmottakeradresselinefour \newline
    \feltmottakeradresselinefive
}


%----------------------------------------
%           Title and opening
%----------------------------------------

%Title font
\newfontface{\titleFont}[BoldFont={Source Sans Pro Bold}]{Source Sans Pro}
\newcommand{\titleText}[1]{\textbf{\titleFont \fontsize{16pt}{16pt} \selectfont \newline #1}}

\setplength{refaftervskip}{10mm} %vertical distance between title and reference field (containing date)

\newlength{\vskipafteropening} % distance between opening and first section title
\setlength{\vskipafteropening}{\dimexpr(6pt)} %removing the baseline skip and 5 pts

%opening prefix font
\newcommand{\tinyGrayText}[1]{{\grayFont \fontsize{8pt}{8pt} \selectfont #1}}

\newcommand{\sakspart}{
    \fontsize{12pt}{14pt} \selectfont
    \tinyGrayText{\feltsaksnummerprefix} \feltsaksnummer \\
    \tinyGrayText{\feltnavnprefix} \feltetternavnmottaker \space \feltfornavnmottaker \\
    \tinyGrayText{\feltfoedselsnummerprefix} \feltfoedselsnummer
    \\*
    \vspace*{\vskipafteropening} %vertical space before letter content
}

\setplength{subjectaftervskip}{2mm}
\newcommand{\tittel}[1]{ %command for creating title in the document
    \setkomavar{subject}{
        \titleText{#1}
    }\opening{ %case and person information after title
        \sakspart
    }
}


%----------------------------------------
%               Content
%----------------------------------------


% space between sections
\newif\ifaddspaceover
\addspaceoverfalse
\newlength{\vskipbeforesection}
\setlength{\vskipbeforesection}{12pt}

% section title
\newlength{\vskipaftersectiontitle} \setlength{\vskipaftersectiontitle}{6pt}
\newcommand{\lettersectiontitle}[1]{
% Add space between sections
    \ifaddspaceover %If not in the first section, add space above.
        \vspace*{\vskipbeforesection}
    \fi
    \addspaceovertrue %toggle on so that all sections following the first one gets space over the title.
    \textbf{#1} %title
    \vspace*{\vskipaftersectiontitle}
    \\*
}

\newcommand{\paragraph}[1]{
    #1
    \pagebreak[3] %suggest pagebreak to the LaTeX compiler if it needs to
    \par
}

\newcommand{\formvspace}[1][true]{\vspace{0.5cm}}

\newcommand{\formText}[1]{
    \par #1
}

% formChoice
\newlist{checklist}{itemize}{1}
\setlist[checklist]{label=$\square$}
\newenvironment{formChoice}[1]{
    \par #1
    \begin{checklist}
} {
    \end{checklist}
}

% attachmentList
\newenvironment{attachmentList}{
    \feltclosingvedleggprefix
    \begin{itemize}
} {
    \end{itemize}
}

%----------------------------------------
%               Footer
%----------------------------------------
\setlength{\footskip}{6pt} % space between letter content and footer
\newcommand{\footerText}[1]{{\secondaryFont \fontsize{8pt}{8pt} \selectfont #1}} %footer font command
%page number
\renewcommand*\pagemark{\usekomafont{pagenumber}{\tinyText{\feltsideprefix~\textbf{\thepage}~\feltsideinfix~\textbf{\pageref*{\thisletter.lastpage}}}}}

%first footer page
\setplength{firstfootvpos}{254.5mm} %absolute vertical posistion from top of first page
\setkomavar{firstfoot}{
    \newline\noindent\rule{\textwidth}{0.16mm}\vspace*{3.7mm} % footer top line
    \footerText{
        \textbf{\feltnavenhet}
        \newline \feltpostadresseprefix \space \feltnavenhet \space//\space \feltpostadresse \space//\space \feltpostadressepostnrsted
        \newline \feltnavenhettlfprefix \space \feltnavenhettlf
        \newline \feltnavenhetnettside
        \newline
    }
    \newline\hspace*{\fill}\pagemark
}

% footer from page 2 onward
\DeclareNewLayer[ %create a format similar to the first page
    align=tl,
    hoffset=\dimexpr.5\paperwidth-.5\useplength{firstfootwidth}\relax,
    voffset=\useplength{firstfootvpos}+7em,
    width=\useplength{firstfootwidth},
    foreground,
    contents={\parbox{\layerwidth}{
        \hspace*{\fill}\pagemark %right align
    }}
]{secondfoot.fg}
\DeclarePageStyleByLayers{secondfooter}{secondfoot.fg}
\pagestyle{secondfooter}


%----------------------------------------
%       Closing/last page of letter
%----------------------------------------
\newcommand{\closingsaksbehandlet}{
    \vspace*{60pt}
    \parbox[t]{0.5\linewidth}{\feltclosingsaksbehandlerfirst \\ \feltclosingsaksbehandlersuffix}
    \parbox[t]{0.5\linewidth}{\feltclosingsaksbehandlersecond \\ \feltclosingsaksbehandlersuffix}
    \par
    \vspace*{14pt}
    \feltnavenhet
}

\newcommand{\closingautomatiskbehandlet}{
    \vspace*{4pt}
    \feltnavenhet
    \par
    \vspace*{12pt}
    \feltclosingautomatisktext
}

\newcommand{\closingvedleggspace}{ % Should be used if there is any attachments.
    \par
    \vspace{60pt}
}

\renewcommand{\closing}{
    \par
    \begin{minipage}{\textwidth}
        \vspace{24pt}
        \lettersectiontitle{\feltclosingspoersmaal}
        \paragraph{\feltclosingkontaktoss}
        \par
        \vspace*{42pt}
        \feltclosinggreeting
        \par
        \closingbehandlet
        \feltclosingvedlegg
    \end{minipage}
}

%----------------------------------------
%       Attachment environment
%----------------------------------------

% setup a counter to uniquely identify attachments (used for labeling the last page of attachments)
\newcounter{attachments}

% produces a label for the current attachment
\newcommand{\currentattachmentlastpage}{attachment\arabic{attachments}}
\newcommand{\startvedlegg}[1]{
    % make sure that the attachment starts on an even page (for printing)
    \clearpage{\cleardoublepage}

    % reset the page counter for this attachment
    \setcounter{page}{1}
    % increase the attachment counter to identify this attachment
    \stepcounter{attachments}

    % renew pagemark with pageref to the last page of this attachment
    \renewcommand*\pagemark{\usekomafont{pagenumber}{\tinyText{\feltsideprefix~\textbf{\thepage}~\feltsideinfix~\textbf{\pageref*{\currentattachmentlastpage}}}}}
    \hfill\includegraphics[width=25mm,page=1]{nav-logo.pdf}
    \par
    \titleText{#1} \par
}

\newcommand{\sluttvedlegg}{
    % create a label on the last page of this attachment (used by \pagemark for attachments)
    \label{\currentattachmentlastpage}
}