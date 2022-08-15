%! Class = pensjonsbrev_v3
%! Author = Håkon Heggholmen
%! Date = 31.01.2022

%----------------------------------------
%            Document class
%----------------------------------------

%! suppress = PackageNameDoesNotMatchFileName
\ProvidesPackage{pensjonsbrev_v3}[2022/01/31 Team pensjonsbrev's Document Class]
\NeedsTeXFormat{LaTeX2e}
\LoadClass[a4paper]{article}

%----------------------------------------
%               Imports
%----------------------------------------
\usepackage{graphicx}   %graphics such as logo
\usepackage{fontspec}   %declare font presets
\usepackage{fancyhdr}   % custom page styles
% tables and colors
\usepackage{xcolor}
\usepackage{ninecolors}
\usepackage{tabularray} % better tables(tblr, longtblr)
\usepackage{enumitem,amssymb} % for formChoice list
\usepackage{placeins} % Float barrier to make elements between barrier not float.
\usepackage[absolute
%    ,showboxes %show boxes around textblocks for debugging
    ]{textpos} %first page absolute posistioned elements
%\usepackage{showframe} %show frames of page geometry
%! suppress = FileNotFound
\input{params}  % Must be done before pdfx is imported

\usepackage{pdfx}       % create pdf/A compliant pdf's

%----------------------------------------
%                Global
%----------------------------------------
%fonts
\setsansfont{Source Sans Pro}
\setmainfont{Source Sans Pro}
\addfontfeature{VerticalPosition = Inferior }
\newfontface{\primaryFont}[
    Color=262626, % TODO blir det mye kostnader på for SITS å printe nesten svart istedenfor svart?
    BoldFont={Source Sans Pro Bold},
    ItalicFont={Source Sans Pro Italic},
    BoldItalicFont={Source Sans Pro Bold Italic}]{Source Sans Pro}

% Global font commands
\newcommand{\standardtextsize}{11.45pt}
\newcommand{\standardtextspacing}{18.5pt}
\newcommand{\titletextsize}{15.3pt}
\newcommand{\titletwotextsize}{13.4pt}
\newcommand{\pagenumbertext}[1]{{\primaryFont \fontsize{10pt}{10pt} \selectfont #1}}
\setlength{\parindent}{0em} % indentations before first line in paragraph
\setlength{\parskip}{1em} % space between paragraphs

%itemize / lists spacing
\newlength{\listtopsep}
\setlength{\listtopsep}{10pt} % separation between top of list and content
\newlength{\listtopsepcalc}
\setlength{\listtopsepcalc}{\dimexpr(\listtopsep-\parskip)} %parskip is subtracted because it is added to the topsep by default
\setlist[itemize]{noitemsep, topsep=\listtopsepcalc, leftmargin=15pt}


% paragraph formatting
\widowpenalties 1 10000 %penalty score for breaking a paragraph
\sloppy % no word-breaking
\raggedright % no word spacing
\raggedbottom % no line stretching to fit page


%% page text area formatting
\newlength{\lettermargin}
\setlength{\lettermargin}{20mm}
\usepackage[
%    showframe, %used to show and debug margins
    top=\lettermargin,
    left=\lettermargin,
    right=\lettermargin,
    bottom=25mm
]{geometry}

\fancypagestyle{normalpage}{
    \renewcommand{\headrulewidth}{0pt}%
    \fancyhf{}%
    \fancyfoot[C]{\letterpagenumber}
}

%----------------------------------------
%                 Logo
%----------------------------------------
\newcommand{\logo}{
    \begin{textblock*}{24.1mm}(16.7mm,17.7mm)
        \hfill\includegraphics[width=24.1mm,page=1]{nav-logo}
    \end{textblock*}
}

%----------------------------------------
%               Page number
%----------------------------------------
\newcommand{\currentSectionName}{mainletter} %name of the current section of the letter. Used for lastpage label at the end of this section
\newcommand{\letterpagenumber}{ %general page number formatting
    \begin{textblock*}{\textwidth}(\lettermargin,280mm)
        \pagenumbertext{\feltsaksnummerprefix~\feltsaksnummer\hfill\feltsideprefix~\thepage~\feltsideinfix~\pageref*{\currentSectionName}}
    \end{textblock*}
}

%----------------------------------------
%             Case details
%----------------------------------------
\newcommand{\sakspart}{
    {
        \setlength{\tabcolsep}{3.9mm}
        \primaryFont \fontsize{\standardtextsize}{19.5pt} \selectfont
        \begin{tabular}{@{}ll}
            \feltnavnprefix & \feltetternavnbruker \space \feltfornavnbruker \\
            \feltfoedselsnummerprefix & \feltfoedselsnummerbruker \\
            \feltsaksnummerprefix & \feltsaksnummer
        \end{tabular}
        \\[28pt]
    }
}


\input{content}     %Code related to the letter content, not the page layout
\input{attachment}  %Elements related to the letter content, not the page layout
\input{firstpage}
\input{closing}