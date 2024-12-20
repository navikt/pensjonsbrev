%! Class = pensjonsbrev_v4
%! Author = Håkon Heggholmen
%! Date = 20.12.2024 %TODO

%----------------------------------------
%            Document class
%----------------------------------------

%! suppress = PackageNameDoesNotMatchFileName
                                  %TODO
\ProvidesPackage{pensjonsbrev_v4}[2024/12/20 Team pensjonsbrev's Document Class]
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
\input{params}  % Must be done before pdfx is imported
\usepackage[a-3b]{pdfx}       % create pdf/A compliant pdf's

%----------------------------------------
%                Global
%----------------------------------------

%fonts
\setsansfont{Source Sans 3}
\setmainfont{Source Sans 3}
\addfontfeature{VerticalPosition = Inferior }
\newfontface{\primaryFont}[
    Color=000000,
    BoldFont={Source Sans 3 Bold},
    ItalicFont={Source Sans 3 Italic},
    BoldItalicFont={Source Sans 3 Bold Italic}]{Source Sans 3}

% Global font commands
\newcommand{\standardtextsize}{11pt}
\newcommand{\standardtextspacing}{16pt}
\newcommand{\standardtext}{\primaryFont \fontsize{\standardtextsize}{\standardtextspacing} \mdseries \selectfont}

% main title
\newcommand{\titletextsize}{16pt}
\newcommand{\titletextspacing}{20pt}
\newcommand{\lettertitle}{\primaryFont \fontsize{\titletextsize}{\titletextspacing} \bfseries \selectfont}

% title 1
\newcommand{\titleone}{\primaryFont \fontsize{\titleonetextsize}{\titleonetextspacing} \bfseries \selectfont}
\newcommand{\titleonetextsize}{13pt}
\newcommand{\titleonetextspacing}{10pt}

% title 2
\newcommand{\titletwo}{\primaryFont \fontsize{\titletwotextsize}{\titletwotextspacing} \bfseries \selectfont}
\newcommand{\titletwotextsize}{12pt}
\newcommand{\titletwotextspacing}{10pt}

% paragraph spacing
\setlength{\parindent}{0em}                 % indentations before first line in paragraph
\setlength{\parskip}{\standardtextsize}     % space between paragraphs

% general paragraph formatting
\sloppy                                     % no word-breaking
\raggedright                                % no word spacing
\raggedbottom                               % no line stretching to fit page
\frenchspacing                              % no double space after ended sentence

% Page break penalties changing this section requires extensive testing after changes.
\interlinepenalty=1700                      % penalty added between each line of a paragraph.
\clubpenalty=9000                           % penalty for a broken page, with a single line of a paragraph remaining on the bottom of the preceding page.
\widowpenalty=9000                          % penalty for a broken page, with a single line of a paragraph (called "widow") remaining on the top of the succeeding page.
\@itempenalty=1000                          % penalty for breaking up an itemlist


%----------------------------------------
%             Margins
%----------------------------------------
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

%----------------------------------------
%                 Logo
%----------------------------------------
\newcommand{\logo}{
    \includegraphics[height=16pt,page=1]{nav-logo}
}

% order matters
\input{pagenumber} % needs to load before attachment
\input{saksinfotable} % needs to load before attachment
\input{content/content}
\input{attachment}
\input{firstpage}
\input{closing}