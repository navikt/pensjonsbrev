#import "state.typ": withSpacing

// Font sizes for each heading depth level (depth 2, 3, 4)
#let titleSizes = (14pt, 12pt, 11pt)

#let letterTitle(level, title) = {
  let size = titleSizes.at(level - 2)
  show heading: set text(size: size, weight: "bold", tracking: 0.2pt)
  show heading: set block(above: 0pt, below: 0pt)
  withSpacing("title", heading(depth: level, title))
}

#let title1(iTitle) = { letterTitle(2)[#iTitle]}
#let title2(iTitle) = { letterTitle(3)[#iTitle]}
#let title3(iTitle) = { letterTitle(4)[#iTitle]}
