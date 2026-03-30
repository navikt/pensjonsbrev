#import "state.typ": withSpacing

#let formChoice(prompt, ..choices) = {
  let formContent = [
    #prompt
    #block(inset: (left: 1pt))[
      #for choice in choices.pos() [
        ☐#h(5pt)#choice #linebreak()
      ]
    ]
  ]
  withSpacing("form", formContent)
}

#let formText(prompt, dots) = {
  let formContent = [#prompt #dots]
  withSpacing("form", formContent)
}

