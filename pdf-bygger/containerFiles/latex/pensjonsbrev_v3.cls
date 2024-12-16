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
%\usepackage{layout} % page layout debug package
\usepackage{enumitem,amssymb} % for formChoice list
\usepackage[absolute
%    ,showboxes %show boxes around textblocks for debugging
    ]{textpos} %first page absolute posistioned elements
%\usepackage{showframe} %show frames of page geometry
%! suppress = FileNotFound
\input{params}  % Must be done before pdfx is imported

\usepackage[a-3b]{pdfx}       % create pdf/A compliant pdf's

%----------------------------------------
%                Global
%----------------------------------------


%fonts

\setsansfont{Source Sans 3}
\setmainfont{Source Sans 3}
\defaultfontfeatures{LetterSpace=1.5}
\addfontfeature{VerticalPosition = Inferior }
\newfontface{\primaryFont}[
    Color=262626,
    BoldFont={Source Sans 3 Bold},
    ItalicFont={Source Sans 3 Italic},
    BoldItalicFont={Source Sans 3 Bold Italic}]{Source Sans 3}

% Global font commands
\newcommand{\standardtextsize}{11.45pt}
\newcommand{\standardtextspacing}{18.5pt}
\newcommand{\titletextsize}{17pt}

\newcommand{\titleonetextsize}{15.5pt}
\newcommand{\titleonetextspacing}{20pt}

\newcommand{\titletwotextsize}{13pt}
\newcommand{\titletwotextspacing}{20pt}

%paragraph spacing
\setlength{\parindent}{0em} % indentations before first line in paragraph
\setlength{\parskip}{1em} % space between paragraphs

%page numbering
\newcommand{\pagenumbertext}[1]{{\primaryFont \fontsize{10pt}{10pt} \selectfont #1}}

%itemize / lists spacing
\newlength{\listtopsep}
\setlength{\listtopsep}{0pt} % separation between top of list and content
% removes whitespace between items, removes parskip whitespace over and under the list
% adds custom space to margin and after list
\setlist[itemize]{noitemsep, before=\vspace{-5pt}, topsep=\dimexpr(-\parskip), leftmargin=15pt, after=\vspace{10pt}}


% paragraph formatting
\sloppy % no word-breaking
\raggedright % no word spacing
\raggedbottom % no line stretching to fit page
\frenchspacing % no double space after ended sentence

\interlinepenalty=1700 % penalty added between each line of a paragraph.
\clubpenalty=9000 % penalty for a broken page, with a single line of a paragraph remaining on the bottom of the preceding page.
\widowpenalty=9000 % penalty for a broken page, with a single line of a paragraph (called "widow") remaining on the top of the succeeding page.

\@itempenalty=115 % penalty for breaking up an itemlist


% page text area formatting
% DO NOT REPLACE WITH GEOMETRY PACKAGE. Geometry will use a hook for end of page, which is unstable and might cause page numbering bugs.
\newlength{\lettermargin}
\setlength{\lettermargin}{20mm} % top left and right margin.
\newlength{\lettermarginbottom}
\setlength{\lettermarginbottom}{25mm} % bottom margin.
\newlength{\lettermargincalc}
\setlength{\lettermargincalc}{\dimexpr(\lettermargin-1in)} % subtracting standard margin of one inch.

\setlength{\textwidth}{\dimexpr(\paperwidth-\lettermargin*2)} % total text width calculated from margins and paper width
\setlength{\headsep}{\lettermargincalc}
\setlength{\headheight}{0mm}
\setlength{\footskip}{0mm}
\setlength{\hoffset}{0mm}
\setlength{\oddsidemargin}{\lettermargincalc}
\setlength{\evensidemargin}{\lettermargincalc}
\setlength{\topmargin}{0mm}
\setlength{\textheight}{\dimexpr(\paperheight-\lettermargin-\lettermarginbottom)} % standard text height

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
\newlength{\pagenumbertoppadding}
\setlength{\pagenumbertoppadding}{11mm} % space between bottom of page content and page number
\newlength{\pagenumberyposcalc} % calculate page number position to not collide with content.
\setlength{\pagenumberyposcalc}{\dimexpr(\paperheight-\lettermarginbottom+\pagenumbertoppadding)} % bottom margin.
\newcommand{\currentSectionName}{mainletter} %name of the current section of the letter. Used for lastpage label at the end of this section
\newcommand{\letterpagenumber}{ %general page number formatting
    \begin{textblock*}{\textwidth}(\lettermargin,\pagenumberyposcalc)
        \pagenumbertext{\feltsidesaksnummerprefix\feltsaksnummer\hfill\feltsideprefix~\thepage~\feltsideinfix~\pageref*{\currentSectionName}}
    \end{textblock*}
}

%----------------------------------------
%             Case details
%----------------------------------------
\NewTblrEnviron{saksinfotable}
\SetTblrInner[saksinfotable]{colspec={l@{\hskip 8mm}X}}
\SetTblrInner[saksinfotable]{leftsep=0pt}
\SetTblrInner[saksinfotable]{rightsep=0pt}
\SetTblrInner[saksinfotable]{rowsep=0pt}
\SetTblrOuter[saksinfotable]{postsep=0pt}
\SetTblrOuter[saksinfotable]{footsep=0pt}

\newcommand{\sakspart}{
    \setlength{\tabcolsep}{12pt}
    \primaryFont \fontsize{\standardtextsize}{19.5pt} \selectfont
    \saksinfomottaker
    \\[1em]
}


\input{content}     %Code related to the letter content, not the page layout
\input{attachment}  %Elements related to the letter content, not the page layout
\input{firstpage}
\input{closing}