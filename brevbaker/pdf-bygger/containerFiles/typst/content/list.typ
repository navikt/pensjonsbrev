#import "state.typ": withSpacing

#let bulletlist(..elements) = {
  set list(indent: 6.5pt, body-indent: 5pt)
  let listContent = elements.pos().map((a) => {
    [- #a]
  }).join()
  withSpacing("list", listContent)
}

#let numberedlist(..elements) = {
  set enum(indent: 6.5pt, body-indent: 5pt)
  let listContent = elements.pos().map((a) => {
    [+ #a]
  }).join()
  withSpacing("list", listContent)
}
