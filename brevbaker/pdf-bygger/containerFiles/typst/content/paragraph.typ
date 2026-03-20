#import "state.typ": updateElementType, lastElementType

#let paragraph(content) = {
  context {
    content
    parbreak()
  }
  updateElementType("paragraph")
}