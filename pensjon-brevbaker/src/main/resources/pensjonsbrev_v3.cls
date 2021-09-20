\NeedsTeXFormat{LaTeX2e}
%! suppress = PackageNameDoesNotMatchFileName
\ProvidesPackage{pensjonsbrev_v3}[2021/09/10 Team pensjonsbrev's Document Class]

\renewcommand{\normalsize}{\fontsize{10}{12}\selectfont}

%\LoadClass[a4paper]{article}

%----------------------------------------
%               Imports
%----------------------------------------
%\usepackage{amsmath}
\usepackage{graphicx}   %graphics such as logo
%\usepackage{setspace}   %
\usepackage{fontspec}   %declare font presets
\usepackage{xcolor}     %define colors
%\usepackage{needspace}
\usepackage[absolute
%,showboxes
]{textpos}
\usepackage{afterpage}
\usepackage[a4paper]{geometry}   % adjusting margins
\usepackage{lastpage}
%\usepackage{fancyhdr}
\setsansfont{Arial}
\setmainfont{Times New Roman}
\include{params}

%----------------------------------------
%                Global
%----------------------------------------

% length vars (in mm)
\newcommand{\marginleft}{20}

%textpos config
\setlength{\TPHorizModule}{1mm}
\setlength{\TPVertModule}{1mm}
\setlength{\parindent}{0mm}
\setlength{\parskip}{1em}

%fonts
\newfontface{\grayArialFont}{Arial}[Color=888888]
\newfontface{\arialFont}[BoldFont={Arial Bold}]{Arial}
\newfontface{\primaryfont}[BoldFont={Times New Roman Bold}]{Times New Roman}

\newcommand{\standardtextsize}{12pt}
\newcommand{\tinytextsize}{8pt}
\newcommand{\tinyArialText}[1]{{\arialFont \fontsize{8pt}{8pt} \selectfont #1}}
\newcommand{\tinyGrayText}[1]{{\grayArialFont \fontsize{8pt}{8pt} \selectfont #1}}

% geometry
\geometry{a4paper, twoside
, left=2cm, right=2.5cm, top=2cm, bottom=3.5cm
, showframe
}

% page numbering
\pagenumbering{Arabic}
\setcounter{page}{1}
\renewcommand{\thepage}{\arabic{page}}
\newcommand{\lastpage}{\pageref{LastPage}}
\newcommand\pagemark{\tinyArialText{\feltsideprefix~\textbf{\thepage}~\feltsideinfix~\textbf{\lastpage}}}

%----------------------------------------
%                Letterhead
%----------------------------------------

\newcommand{\letterheadRecipient}{
    \begin{textblock}{150}(\marginleft,50)
        \arialFont \fontsize{12pt}{12pt} \selectfont
        \baselineskip=14pt
        \feltfornavnmottaker \space \feltetternavnmottaker \newline
        \feltmottakeradresselinjeen  \newline
        \feltpostnummermottaker \space \feltpoststedmottaker
    \end{textblock}
}

\newcommand{\letterheadReturnaddress}{
    \begin{textblock}{140}(\marginleft,19)
        \tinyArialText{\feltreturadresseenhetprefix \space \feltnavenhet}
        \baselineskip=4pt
        \newline
        \tinyArialText{\feltreturadresse \space \feltreturadressepostnrsted}
    \end{textblock}
}

\newcommand{\letterheadLogo}{
    \begin{textblock}{26}(164,21)
        \hfill\includegraphics[width=25mm,page=1]{nav-logo.pdf}
    \end{textblock}
}

\newcommand{\letterheadDate}{
    \begin{textblock}{50}(149,82)
        \tinyArialText{\feltdatoprefix}\hspace{2pt}\feltdato
    \end{textblock}
}

\newcommand{\letterhead}{
    \letterheadReturnaddress
    \letterheadLogo
    \letterheadRecipient
    \letterheadDate
    \vspace*{73mm}
}

%----------------------------------------
%                Footers
%----------------------------------------
\newcommand{\letterfirstfooter}{
    \begin{textblock}{184}(13,258)
        \rule{\textwidth}{0.16mm}\vspace*{3.7mm} % footer top line
        \arialFont \fontsize{8pt}{8pt} \selectfont
        \textbf{\feltnavenhet}
        \newline \feltpostadresseprefix \space \feltnavenhet \space//\space \feltpostadresse \space//\space \feltpostadressepostnrsted
        \newline \feltnavenhettlfprefix \space \feltnavenhettlf
        \newline \feltnavenhetnettside
%        \newline \hspace*{\fill}\pagemark
    \end{textblock}
}

%----------------------------------------
%                Element commands
%----------------------------------------
\newcommand{\pagetitle}[1]{
    \textbf{\primaryfont \fontsize{16pt}{16pt} \selectfont \newline #1}
     \par
}

\newcommand{\paragraph}[1]{
    %\samepage %do not break up paragraphs across pages
    #1\par
}

\newcommand{\sakspart}{
    \fontsize{12pt}{14pt} \selectfont
    \tinyGrayText{\feltsaksnummerprefix} \feltsaksnummer \\
    \tinyGrayText{\feltnavnprefix} \feltetternavnmottaker \space \feltfornavnmottaker \\
    \tinyGrayText{\feltfoedselsnummerprefix} \feltfoedselsnummer
    \par
    \vspace{2mm}
}

%----------------------------------------
%                Primary environments
%----------------------------------------

\newenvironment{pensjonsbrev}{
    % margins of letter first page
    \newgeometry{left=2cm, right=2.5cm, top=2cm, bottom=9cm}
    \afterpage{\newgeometry{left=2cm, right=2.5cm, top=2cm, bottom=3.5cm}}


    % activate default font
    \primaryfont \fontsize{12pt}{12pt} \selectfont
    \letterhead
    \letterfirstfooter
}
{

}

\newenvironment{pensjonsvedlegg}{}{}

%\setlength{\footskip}{4cm}
%\fancypagestyle{plain}{
%    \fancyhead{}
%    \fancyfoot{}
%    \renewcommand{\headrulewidth}{0pt}
%    \renewcommand{\footrulewidth}{0pt}
%    \fancyfoot[R]{\pagemark}
%}
%\pagestyle{plain}

\newcommand{\startletter}{
    \setcounter{page}{1}

    % margins of letter first page
    \newgeometry{left=2cm, right=2.5cm, top=2cm, bottom=5cm}
    \afterpage{\newgeometry{left=2cm, right=2.5cm, top=2cm, bottom=2cm}}
    \afterpage{\restoregeometry}

    % activate default font
    \primaryfont \fontsize{12pt}{12pt} \selectfont
    \letterhead
    \letterfirstfooter
}


% eksempelbruk

%    private fun renderLetterV3(letter: Letter, printWriter: PrintWriter): Unit =
%        with(printWriter) {
%            println("""\documentclass[12pt]{pensjonsbrev_v3}""")
%            println("""\begin{document}""")
%//            println("""    \begin{pensjonsbrev}{}""")
%            println("""\startletter""")
%            println("""    \pagetitle{${letter.template.title.text(letter.language)}}""")
%            println("""    \sakspart""")
%            contents(letter, printWriter)
%//            println("""    \end{pensjonsbrev}""")
%//            letter.template.attachments.forEachIndexed { index, _ -> println("""\input{attachment_$index}""") }
%            println("""\end{document}""")
%        }