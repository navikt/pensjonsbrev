#import "state.typ": withSpacing

#let bulletlist(..elements) = {
  set list(indent: 6.5pt, body-indent: 5pt)
  withSpacing("list", list(..elements.pos()))
}

#let numberedlist(..elements) = {
  set enum(indent: 6.5pt, body-indent: 5pt)
  withSpacing("list", enum(..elements.pos()))
}
