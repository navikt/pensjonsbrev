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
\setsansfont{Arial}
\setmainfont{Times New Roman}
%! suppress = FileNotFound
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
\newcommand{\tinyArialText}[1]{{\arialFont \fontsize{8pt}{8pt} \selectfont #1}}
\setparsizes{0pt}{1em}{0pt plus 1fil}

%itemize / lists spacing
\usepackage{enumitem}
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
    \tinyArialText{\feltreturadresseenhetprefix \space \feltnavenhet}\newline
    \tinyArialText{\feltreturadresse \space \feltreturadressepostnrsted}
}


%----------------------------------------
%                  Date
%----------------------------------------
\setplength{toaddrheight}{150pt}
\setplength{refwidth}{50mm}
\KOMAoption{refline}{dateleft}
\setplength{refhpos}{159mm} %distance from left
\setplength{refvpos}{84mm} %distance from top
\setkomavar{date}{\tinyArialText{\feltdatoprefix}\hspace{1pt}\feltdato}


%----------------------------------------
%              To address
%----------------------------------------
\setplength{toaddrhpos}{12mm}
\setplength{toaddrvpos}{36mm}
\newcommand{\brevparameter}{
    \arialFont \fontsize{12pt}{12pt} \selectfont
    \feltfornavnmottaker \space \feltetternavnmottaker \newline
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
\newfontface{\titleFont}[BoldFont={Times New Roman Bold}]{Times New Roman}
\newcommand{\titleText}[1]{\textbf{\titleFont \fontsize{16pt}{16pt} \selectfont \newline #1}}

\setplength{refaftervskip}{10mm} %vertical distance between title and reference field (containing date)

\newlength{\vskipafteropening} % distance between opening and first section title
\setlength{\vskipafteropening}{\dimexpr(6pt)} %removing the baseline skip and 5 pts

%opening prefix font
\newcommand{\tinyGrayText}[1]{{\grayArialFont \fontsize{8pt}{8pt} \selectfont #1}}

\newcommand{\sakspart}{
    \fontsize{12pt}{14pt} \selectfont
    \tinyGrayText{\feltsaksnummerprefix} \feltsaksnummer \\
    \tinyGrayText{\feltnavnprefix} \feltetternavnmottaker \space \feltfornavnmottaker \\
    \tinyGrayText{\feltfoedselsnummerprefix} \feltfoedselsnummer
    \par
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
    \newline
}

\newcommand{\paragraph}[1]{
    \par
    \samepage %do not break up paragraphs across pages
    #1
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
\newcommand{\footerText}[1]{{\arialFont \fontsize{8pt}{8pt} \selectfont #1}} %footer font command
%page number
\renewcommand*\pagemark{\usekomafont{pagenumber}{\tinyArialText{\feltsideprefix~\textbf{\thepage}~\feltsideinfix~\textbf{\letterlastpage}}}}

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
    \vspace*{8pt}
    \feltclosingautomatisktext
}
\renewcommand{\closing}{
    \par
    \begin{minipage}{\textwidth}
        \vspace{5pt}
        \textbf{\feltclosingspoersmaal}
        \vspace*{\vskipaftersectiontitle}
        \newline
        \paragraph{\feltclosingkontaktoss}
        \vspace*{42pt}
        \par
        \feltclosinggreeting
        \par
        \closingbehandlet
        \par
        \vspace{50pt}
        \feltclosingvedlegg
    \end{minipage}
}

%----------------------------------------
%       Attachment environment
%----------------------------------------
\newcounter{attachments}

\newcommand{\startvedlegg}[1]{
    % make sure that the attachment starts on an even page (for printing)
    \clearpage{\cleardoublepage}

    % setup page counter
    \setcounter{page}{1}
    \stepcounter{attachments}

    % renew last page for attachments
    \renewcommand*\pagemark{\usekomafont{pagenumber}{\tinyArialText{\feltsideprefix~\textbf{\thepage}~\feltsideinfix~\textbf{\pageref{attachment\arabic{attachments}}}}}}
    \hfill\includegraphics[width=25mm,page=1]{nav-logo.pdf}
    \par
    \titleText{#1} \par
}

\newcommand{\sluttvedlegg}{
    \label{attachment\arabic{attachments}}
}