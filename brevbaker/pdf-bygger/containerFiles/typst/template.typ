#import "casedetails.typ" : casedetails
#import "input.typ": input, languageSettings
#import "content/state.typ": section-start
#import "footer.typ": footer


#let mainTitle(lettertitle) = {
  show heading: set text(size: 16pt, weight: "bold", tracking: 0.32pt)
  show heading: set block(above: 53pt, below: 0pt)
  [= #lettertitle]
}


#let logo = {
  image("NAV_logo.svg", height: 16pt, alt: "Nav logo")
}


#let template(lettertitle: [], doc) = {
  // generelt oppsett for brevet
  set text(
    font: "Source Sans 3",
    fallback: true, // Falls back to Noto fonts for unsupported glyphs (fonts.conf restricts to Source Sans 3 + Noto only)
    size: 11pt,
  )

  show link: it => it.body // disable automatic hyperlinking.
  set par(
    leading: 8.7pt,  // Line spacing within paragraphs
    spacing: 24pt, // Space between paragraphs
  )

  set page(margin: (
      x: 64pt,
      y: 64pt,
      bottom: 74pt,
    ),
    footer: footer,
    footer-descent: 30% + 4pt,
  )
  set document(
    title: lettertitle,
  )

  section-start(1)

  // første side
  logo
  casedetails
  mainTitle(lettertitle)

  // innholdet i brevet
  doc
}