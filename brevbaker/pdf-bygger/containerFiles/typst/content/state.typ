#import "spacing.typ": getSpacing

#let lastElementType = state("lastElementType", "start")

// Set to true to show visible borders around content blocks for debugging
// DO NOT SET TO TRUE IN PROD
#let debug = false

#let updateElementType(elementType) = {
  lastElementType.update(elementType)
}

// Helper to wrap content with appropriate spacing based on element transition
#let withSpacing(elementType, content) = {
  context {
    let prevType = lastElementType.get()
    let space = getSpacing(prevType, elementType)
    let sticky = elementType == "title"
    let stroke = if debug { 0.5pt + red } else { none }
    block(above: space, sticky: sticky, below: 0pt, stroke: stroke)[
      #if debug {
        place(top + left, dx: -2pt, dy: -10pt,
          text(size: 6pt, fill: blue)[#prevType → #elementType space: #space sticky:#sticky]
        )
      }
      #content
    ]
  }
  updateElementType(elementType)
}

#let sectionCounter = counter("section-counter")

#let section-start(sectionNumber) = {
  sectionCounter.step()
  counter(page).update(1)
  lastElementType.update("start")
  [#metadata("section-start")#label("section-start-" + str(sectionNumber))]
}

#let section-end(sectionNumber) = {
  [#metadata("section-end")#label("section-end-" + str(sectionNumber))]
}
