#import "state.typ": withSpacing

#let bulletlist(..elements) = {
  set list(indent: 6pt, body-indent: 6pt)
  let listContent = elements.pos().map((a) => {
    [- #a]
  }).join()
  withSpacing("list", listContent)
}