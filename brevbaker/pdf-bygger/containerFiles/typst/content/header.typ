// Felles topp-elementer som deles av brev-, vedleggs- og dokument-rotene.

#let mainTitle(title, above: 48pt) = {
  show heading: set text(size: 17pt, weight: "bold", tracking: 0.32pt)
  show heading: set block(above: above, below: 0pt)
  [= #title]
}

#let logo = {
  image("../NAV_logo.svg", height: 16pt, alt: "Nav logo")
}
