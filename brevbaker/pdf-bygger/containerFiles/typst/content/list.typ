#import "state.typ": updateElementType, lastElementType

#let bulletlist(..elements) = {
  set list(body-indent: 15pt, )
  block(
    elements.pos().map((a)=>{
      [- #a]
    }).join(),
    below: 10pt,
  )
  updateElementType("list")
}