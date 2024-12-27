%! Class = pensjonsbrev_v4
%! Author = Håkon Heggholmen
%! Date = 20.12.2024 %TODO

%----------------------------------------
%            Document class
%----------------------------------------

\ProvidesPackage{pensjonsbrev_v4}[2024/12/20 Team pensjonsbrev's Document Class]
\NeedsTeXFormat{LaTeX2e}
\LoadClass[a4paper]{article}

%----------------------------------------
%               Imports
%----------------------------------------
\RequirePackage{graphicx}         % graphics such as logo
\RequirePackage{fontspec}         % declare font presets
\RequirePackage{fancyhdr}         % custom page styles
\RequirePackage{tabularray}       % better tables(tblr, longtblr)
\RequirePackage{enumitem}         % for customizing itemlist
\RequirePackage[absolute]{textpos}% first page absolute posistioned elements
\input{params}                    % Must be done before pdfx is imported
\RequirePackage[a-3u]{pdfx}       % create pdf/A compliant pdf's

%----------------------------------------
%                Fonts
%----------------------------------------

%fonts
\setsansfont{Source Sans 3}
\setmainfont{Source Sans 3}
\addfontfeature{VerticalPosition = Inferior }
\newfontface{\primaryFont}[
    Color=000000,
    BoldFont={Source Sans 3 Bold},
    ItalicFont={Source Sans 3 Italic}]{Source Sans 3}


% Global font commands
\newcommand{\standardtextsize}{11pt}
\newcommand{\standardtextspacing}{16pt}
\newcommand{\standardtext}{\primaryFont \fontsize{\standardtextsize}{\standardtextspacing} \mdseries \selectfont}

% main title
\newfontfamily\titlefont[LetterSpace=0.3]{Source Sans 3 Bold}
\newcommand{\titletextsize}{16pt}
\newcommand{\titletextspacing}{20pt}
\newcommand{\lettertitle}{\titlefont \fontsize{\titletextsize}{\titletextspacing} \bfseries \selectfont}

% title 1
\newfontfamily\titleonefont[LetterSpace=0.25]{Source Sans 3 Bold}
\newcommand{\titleone}{\titleonefont \fontsize{\titleonetextsize}{\titleonetextspacing} \bfseries \selectfont}
\newcommand{\titleonetextsize}{13pt}
\newcommand{\titleonetextspacing}{10pt}

% title 2
\newfontfamily\titletwofont[LetterSpace=0.2]{Source Sans 3 Bold}
\newcommand{\titletwo}{\titletwofont \fontsize{\titletwotextsize}{\titletwotextspacing} \bfseries \selectfont}
\newcommand{\titletwotextsize}{12pt}
\newcommand{\titletwotextspacing}{10pt}

% paragraph spacing
\setlength{\parindent}{0em}                 % indentations before first line in paragraph
\setlength{\parskip}{16pt}                  % space between paragraphs

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
    \includegraphics[height=16pt,page=1]{logo/nav-logo}
}

% order matters
% needs to load before attachment and firstpage
\input{pagenumber}
\input{saksinfotable}
\input{content/content} % elements used to render the letter and attachment outline/content
\input{attachment}      % attachment layout
\input{firstpage}       % first page layout
\input{closing}         % content on the end of main letter content