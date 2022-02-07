%! Class = pensjonsbrev_v3
%! Author = Håkon Heggholmen
%! Date = 31.01.2022

%----------------------------------------
%            Document class
%----------------------------------------

%! suppress = PackageNameDoesNotMatchFileName
\ProvidesPackage{pensjonsbrev_v3}[2022/01/31 Team pensjonsbrev's Document Class]

\LoadClass[a4paper]{article}

%----------------------------------------
%               Imports
%----------------------------------------
\usepackage{graphicx}   %graphics such as logo
\usepackage{fontspec}   %declare font presets
\usepackage[paper=a4paper,
%    driver=xetex,
%    includeheadfoot,
%    showframe %debug margin lines and other geometry
    ]{geometry}
\usepackage{lastpage}   % last page counter
\usepackage{fancyhdr}   % custom page styles
% tables and colors
\usepackage[rgb]{xcolor}
\selectcolormodel{natural}
\usepackage{ninecolors}
\selectcolormodel{rgb}
\usepackage{tabularray} % better tables(tblr, longtblr)
%\usepackage{afterpage}
%\usepackage{everypage}   % run something on the beginning of the next page. Used to restore geometry
\usepackage{enumitem,amssymb} % for formChoice list

\usepackage{placeins} % Float barrier to make elements between barrier not float.
\usepackage[absolute
    %, overlay
%    ,showboxes %show boxes around textblocks for debugging
    ]{textpos} %first page absolute posistioned elements



%! suppress = FileNotFound
\input{params}  % Must be done before pdfx is imported

\usepackage{pdfx}       % create pdf/A compliant pdf's

%----------------------------------------
%                Global
%----------------------------------------
%fonts
\setsansfont{Source Sans Pro}
\setmainfont{Source Sans Pro}
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


% paragraph formatting
\widowpenalties 1 1000 %penalty score for breaking a paragraph
\sloppy % no word-breaking
\raggedright % no word spacing
\raggedbottom % no line stretching to fit page


% page formatting

%\afterpage{\globaldefs=1\newgeometry{margin=0mm}\globaldefs=0}
\newgeometry{left=2cm, right=2.5cm, top=2.5cm, bottom=2.5cm}
\newgeometry{left=2cm, right=2.5cm, top=102.3mm, bottom=4cm} % First page geometry
\AddToHookNext{shipout/before}{\globaldefs=1\newgeometry{left=2cm, right=2.5cm, top=2.5cm, bottom=2.5cm}\globaldefs=0}
%\AddToHookNext{shipout/firstpage}{\geometry{left=2cm, right=2.5cm, top=2.5cm, bottom=2.5cm} % First page geometry}

%\newgeometry{left=2cm, right=2.5cm, top=2.5cm, bottom=2.5cm} % First page geometry
%\AddEverypageHook{
%    \ifnum\pageref*{LastPage}=\pageref*{LastPage} test
%    \else test
%    \fi
%}
%\AtBeginPage{
%    \globaldefs=1
%    \newgeometry{left=2cm, right=2.5cm, top=2.5cm, bottom=2.5cm} % normal page geometry
%    \globaldefs=0

%}
%\newcommand{\Shipout}{\shipout}
%\renewcommand{\shipout}{test \Shipout}
%\newgeometry{left=2cm, right=2.5cm, top=102.3mm, bottom=4cm} % First page geometry
%\savegeometry{firstpage}

%\savegeometry{normalpage}

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
\newcommand{\letterpagenumber}{ %general page number formatting
    \begin{textblock*}{25mm}(177mm,292mm)
        \tinyText{\feltsideprefix~\textbf{\thepage}~\feltsideinfix~\textbf{\pageref*{LastPage}}}
    \end{textblock*}
}

\input{content}     %Code related to the letter content, not the page layout
\input{attachment}  %Elements related to the letter content, not the page layout
\input{firstpage}
\input{closing}