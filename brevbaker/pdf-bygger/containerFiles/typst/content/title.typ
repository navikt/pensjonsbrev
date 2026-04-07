#import "state.typ": withSpacing, lastHeadingLevel

// Font sizes for each heading depth level (depth 2, 3, 4)
#let titleSizes = (14pt, 12pt, 11pt)

// Render a heading at the correct consecutive level for PDF/UA-1 compliance.
// The actual heading depth is clamped to at most lastHeadingLevel + 1
#let letterTitle(intendedLevel, title) = {
  context {
    let actualLevel = calc.min(intendedLevel, lastHeadingLevel.get() + 1)
    let size = titleSizes.at(actualLevel - 2)
    show heading: set text(size: size, weight: "bold", tracking: 0.2pt)
    show heading: set block(above: 0pt, below: 0pt)
    lastHeadingLevel.update(actualLevel)
    withSpacing("title", heading(depth: actualLevel, title))
  }
}

#let title1(iTitle) = { letterTitle(2)[#iTitle]}
#let title2(iTitle) = { letterTitle(3)[#iTitle]}
#let title3(iTitle) = { letterTitle(4)[#iTitle]}
