#import "state.typ": withSpacing

#let formChoice(prompt, ..choices) = {
  let formContent = [
    #prompt
    #block(inset: (left: 1em))[
      #for choice in choices.pos() [
        #box(stroke: 0.5pt, width: 0.8em, height: 0.8em) #choice #linebreak()
      ]
    ]
  ]
  withSpacing("form", formContent)
}

#let formText(prompt, dots) = {
  let formContent = [#prompt #dots]
  withSpacing("form", formContent)
}

