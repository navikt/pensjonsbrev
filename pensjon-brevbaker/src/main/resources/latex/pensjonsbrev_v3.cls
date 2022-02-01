%! Class = pensjonsbrev_v2
%! Author = Håkon Heggholmen
%! Date = 31.01.2022

%----------------------------------------
%            Document class
%----------------------------------------

%! suppress = PackageNameDoesNotMatchFileName
\ProvidesPackage{pensjonsbrev_v3}[2022/01/31 Team pensjonsbrev's Document Class]

\LoadClass{article}



%----------------------------------------
%               Imports
%----------------------------------------
%\usepackage{amsmath}
\usepackage{graphicx}   %graphics such as logo
\usepackage{fontspec}   %declare font presets
\usepackage[paper=a4paper]{geometry}

\usepackage{lastpage}   % last page counter
\usepackage{fancyhdr}   % custom page styles
\usepackage{afterpage}   % run something on the beginning of the next page. Used to restore geometry
\usepackage{enumitem,amssymb} % for formChoice list
\usepackage{placeins} % Float barrier to make elements between barrier not float.
\usepackage[absolute
%    ,showboxes %show boxes around textblocks for debugging
    ]{textpos} %first page absolute posistioned elements

% tables and colors
\usepackage[rgb]{xcolor}
\selectcolormodel{natural}
\usepackage{ninecolors}
\selectcolormodel{rgb}
\usepackage{tabularray} % better tables(tblr, longtblr)

%! suppress = FileNotFound
\input{params}  % Must be done before pdfx is imported

\usepackage{pdfx}       % create pdf/A compliant pdf's
\setsansfont{Source Sans Pro}
\setmainfont{Source Sans Pro}


%----------------------------------------
%                Global
%----------------------------------------

%fonts
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
%\setparsizes{0pt}{1em}{0pt plus 1fil}

%itemize / lists spacing
\setlist{nolistsep}


% paragraph formatting
\widowpenalties 1 1000 %penalty score for breaking a paragraph
\sloppy % no word-breaking
\raggedright % no word spacing
\raggedbottom

%----------------------------------------
%               Content
%----------------------------------------


% space between sections
\newlength{\vskipbeforesection}
\setlength{\vskipbeforesection}{12pt}


%Letter section title
\newcommand{\lettersectiontitle}[1]{
    \vspace{6pt} \textbf{#1} \\*[6pt]
}

\newcommand{\templateparagraph}[1] {#1 \par}

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

%----------------------------------------
%               Footer
%----------------------------------------
\newcommand{\letterpagenumber}{ %general page number formatting
    \tinyText{
        \feltsideprefix~\textbf{\thepage}~\feltsideinfix~\textbf{
            \pageref*{LastPage}
        }
    }
}

%\newplength{secondfootvpos}
%\setplength{secondfootvpos}{290.5mm} %absolute vertical posistion from top of first page

% footer from page 2 onward
% \DeclareNewLayer[ %create a format similar to the first page
%     align=tl,
%     hoffset=\dimexpr.5\paperwidth-.5\useplength{firstfootwidth}\relax,
%     voffset=\useplength{secondfootvpos},
%     width=\useplength{firstfootwidth},
%     foreground,
%     contents={\parbox{\layerwidth}{
%         \hspace*{\fill}\pagemark %right align
%     }}
% ]{secondfoot.fg}
% \DeclarePageStyleByLayers{secondfooter}{secondfoot.fg}
% \pagestyle{secondfooter}

\input{attachment}
\input{firstpage}
\input{closing}