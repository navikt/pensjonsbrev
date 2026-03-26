#import "state.typ": withSpacing

#let letterTitle(size, weight, tracking, title) = {
  show heading: set text(size: size, weight: "bold", tracking: 0.2pt)
  show heading: set block(above: 0pt, below: 0pt)
  withSpacing("title", title)
}

#let title1(iTitle) = { letterTitle(13pt, "bold",  0.2pt)[= #iTitle]}
#let title2(iTitle) = { letterTitle(12pt, "bold",  0.2pt)[== #iTitle]}
#let title3(iTitle) = { letterTitle(11pt, "bold",  0.2pt)[=== #iTitle]}
