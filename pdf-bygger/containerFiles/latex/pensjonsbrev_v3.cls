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
%\usepackage{titlesec}
\usepackage{ninecolors}
\usepackage{tabularray} % better tables(tblr, longtblr)
%\usepackage{layout} % page layout debug package
\usepackage{enumitem,amssymb} % for formChoice list
\usepackage[absolute
%    ,showboxes %show boxes around textblocks for debugging
]{textpos} %first page absolute posistioned elements
%\usepackage{showframe} %show frames of page geometry
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
\newcommand{\standardtextsize}{11pt}        % size
\newcommand{\standardtextspacing}{18.5pt}   % space between normal lines

% main title
\newcommand{\titletextsize}{16pt}           % size
\newcommand{\titletextspacing}{26pt}        % space under

%title one

\newcommand{\titleonetextsize}{13pt}        % size
\newcommand{\titleonetextspacing}{10pt}     % space under

%title two
\newcommand{\titletwotextsize}{12pt}        % size
\newcommand{\titletwotextspacing}{10pt}     % space under
%\newcommand{\titleone}{\primaryFont \fontsize{\titleonetextsize}{\titleonetextspacing} \selectfont}

%paragraph spacing
\setlength{\parindent}{0em}                 % indentations before first line in paragraph
\setlength{\parskip}{1em}                   % space between paragraphs

%page numbering
\newcommand{\pagenumbertext}[1]{{\primaryFont \fontsize{10pt}{10pt} \selectfont #1}}

%itemize / lists spacing
\newlength{\listtopsep}
\setlength{\listtopsep}{0pt} % separation between top of list and content
% removes whitespace between items, removes parskip whitespace over and under the list
% adds custom space to margin and after list
\setlist[itemize]{noitemsep, before=\vspace{-5pt}, topsep=\dimexpr(-\parskip), leftmargin=15pt, after=\vspace{10pt}}


% paragraph formatting
\sloppy                                     % no word-breaking
\raggedright                                % no word spacing
\raggedbottom                               % no line stretching to fit page
\frenchspacing                              % no double space after ended sentence

% Page break penalties changing this section requires extensive testing, so it's not recommended.
\interlinepenalty=1700                      % penalty added between each line of a paragraph.
\clubpenalty=9000                           % penalty for a broken page, with a single line of a paragraph remaining on the bottom of the preceding page.
\widowpenalty=9000                          % penalty for a broken page, with a single line of a paragraph (called "widow") remaining on the top of the succeeding page.

\@itempenalty=115                           % penalty for breaking up an itemlist


% page text area formatting
% DO NOT REPLACE WITH GEOMETRY PACKAGE. Geometry will use a hook for end of page, which is unstable and might cause page numbering bugs.
\newlength{\lettermargin}
\setlength{\lettermargin}{64pt}                               % top left and right margin.
\newlength{\lettermarginbottom}
\setlength{\lettermarginbottom}{74pt}                         % bottom margin.
\newlength{\lettermargincalc}
\setlength{\lettermargincalc}{\dimexpr(\lettermargin-1in)}    % subtracting standard margin of one inch.

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
    \includegraphics[height=16pt,page=1]{nav-logo}
}

%----------------------------------------
%               Page number
%----------------------------------------
\newlength{\pagenumbertoppadding}
\setlength{\pagenumbertoppadding}{16pt} % space between bottom of page content and page number
\newlength{\pagenumberyposcalc} % calculate page number position to not collide with content.
\setlength{\pagenumberyposcalc}{\dimexpr(\paperheight-\lettermarginbottom+\pagenumbertoppadding)} % bottom margin.
\newcommand{\currentSectionName}{mainletter}                                                      % name of the current section of the letter. Used for lastpage label at the end of this section
\newcommand{\letterpagenumber}{                                                                   % general page number formatting
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
    \primaryFont \fontsize{\standardtextsize}{\standardtextspacing} \selectfont
    \saksinfomottaker
}


\input{content}     % Code related to the letter content, not the page layout
\input{attachment}  % Elements related to the letter content, not the page layout
\input{firstpage}
\input{closing}