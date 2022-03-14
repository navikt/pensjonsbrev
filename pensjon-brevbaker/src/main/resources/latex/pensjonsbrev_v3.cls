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
\newfontface{\grayFont}{Source Sans Pro}[Color=888888]
\newfontface{\primaryFont}[
    BoldFont={Source Sans Pro Bold},
    ItalicFont={Source Sans Pro Italic},
    BoldItalicFont={Source Sans Pro Bold Italic}]{Source Sans Pro}
\newfontface{\secondaryFont}[
    BoldFont={Source Sans Pro Bold},
    ItalicFont={Source Sans Pro Italic},
    BoldItalicFont={Source Sans Pro Bold Italic}]{Source Sans Pro}

% Global font commands
\newcommand{\standardtextsize}{12pt}
\newcommand{\tinytextsize}{8pt}
\newcommand{\tinyText}[1]{{\secondaryFont \fontsize{8pt}{8pt} \selectfont #1}}
\setlength{\parindent}{0em} % indentations before first line in paragraph
\setlength{\parskip}{1em} % space between paragraphs
%itemize / lists spacing
\setlist{nolistsep}
\renewcommand{\baselinestretch}{1.2}

% paragraph formatting
\widowpenalties 1 1000 %penalty score for breaking a paragraph
\sloppy % no word-breaking
\raggedright % no word spacing
\raggedbottom % no line stretching to fit page


% page text area formatting
\setlength{\textwidth}{165 mm} % total text width
% margin adjustments (adjusting relative to original standard article margins):
\setlength{\oddsidemargin}{-5.4 mm}
\setlength{\evensidemargin}{-5.4 mm}
\setlength{\topmargin}{-12 mm}
\setlength{\textheight}{247 mm} % standard text height

\fancypagestyle{normalpage}{
    \renewcommand{\headrulewidth}{0pt}%
    \fancyhf{}%
    \fancyfoot[C]{\letterpagenumber}
}

%----------------------------------------
%                 Logo
%----------------------------------------
\newcommand{\logo}{
    \begin{textblock*}{25mm}(165mm,21mm)
        \hfill\includegraphics[width=25mm,page=1]{nav-logo}
    \end{textblock*}
}

%----------------------------------------
%               Page number
%----------------------------------------
\newcommand{\currentSectionName}{mainletter} %name of the current section of the letter. Used for lastpage label at the end of this section
\newcommand{\letterpagenumber}{ %general page number formatting
    \begin{textblock*}{25mm}(160mm,292mm)
        \tinyText{\hfill\feltsideprefix~\textbf{\thepage}~\feltsideinfix~\textbf{\pageref*{\currentSectionName}}}
    \end{textblock*}
}

%----------------------------------------
%             Case details
%----------------------------------------
\newcommand{\sakspart}{
    {
        \setlength{\tabcolsep}{3.5mm}
        \primaryFont \fontsize{\standardtextsize}{\standardtextsize} \selectfont
        \begin{tabular}{@{}ll}
            \feltnavnprefix & \feltetternavnmottaker \space \feltfornavnmottaker \\
            \feltfoedselsnummerprefix & \feltfoedselsnummer \\
            \feltsaksnummerprefix & \feltsaksnummer
        \end{tabular}
        \\[22pt]
    }
}


\input{content}     %Code related to the letter content, not the page layout
\input{attachment}  %Elements related to the letter content, not the page layout
\input{firstpage}
\input{closing}