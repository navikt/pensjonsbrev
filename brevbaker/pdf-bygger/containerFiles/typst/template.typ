#import "casedetails.typ" : casedetails
#import "footer.typ": footer
#import "content/state.typ": section-start


#let mainTitle(lettertitle) = {
  show heading: set text(size: 17pt, weight: "bold", tracking: 0.32pt)
  show heading: set block(above: 48pt, below: 0pt)
  [= #lettertitle]
}


#let logo = {
  image("NAV_logo.svg", height: 16pt, alt: "Nav logo")
}


#let template(lettertitle: [], input: (:), languageSettings: (:), doc) = {
  // generelt oppsett for brevet
  set text(
    font: "Source Sans 3",
    fallback: true, // Falls back to Noto fonts for unsupported glyphs
    size: 11pt,
  )

  set par(
    leading: 8.7pt,  // Line spacing within paragraphs
    spacing: 24pt, // Space between paragraphs
  )

  set page(margin: (
      x: 64pt,
      y: 64pt,
      bottom: 74pt,
    ),
    footer: footer(input, languageSettings),
    footer-descent: 30% + 4pt,
  )
  set document(
    title: lettertitle,
  )

  section-start(1)

  // første side
  logo
  casedetails(input, languageSettings)
  mainTitle(lettertitle)

  // innholdet i brevet
  doc
}