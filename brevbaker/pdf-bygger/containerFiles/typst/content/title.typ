#import "state.typ": updateElementType, lastElementType

#let letterTitle(size, weight, tracking, title) = {
  show heading : set text(size: size, weight: "bold", tracking: 0.2pt)
  context { 
    if(lastElementType.get() != "title") {
      block(title, above: 26pt, sticky: true)
    } else {
      block(title, sticky: true)
    }
  }
  updateElementType("title")
}

#let title1(iTitle) = { letterTitle(13pt, "bold",  0.2pt)[= #iTitle]}
#let title2(iTitle) = { letterTitle(12pt, "bold",  0.2pt)[== #iTitle]}
#let title3(iTitle) = { letterTitle(11pt, "bold",  0.2pt)[=== #iTitle]}
