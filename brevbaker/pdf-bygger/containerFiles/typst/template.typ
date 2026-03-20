#import "casedetails.typ" : casedetails
#import "input.typ": input, languageSettings
#import "closing.typ": closing
#import "content/state.typ": section-start
#import "content/list.typ": 
#import "content/table.typ": letterTable
#import "footer.typ": footer

#let mainTitle(lettertitle) = {
    title[
      #pad(
        text(
          size: 16pt,
          weight: "bold",
          lettertitle,
        ),
        bottom: 32pt,
      )
    ]
}


#let logo = {
  pad(
    image("nav-logo.pdf", height: 16pt),
    // TODO bytt ut med svg
    bottom: 48pt
  )
}


#let template(lettertitle: [], doc) = {
  // generelt oppsett for brevet
  set text(
    font:"Source Sans Pro",
    size: 11pt,
  )

  set table(
    stroke: none,
    gutter: 0.2em,
    fill: (x, y) => {
      if(y == 0) {
        blue
      } else if calc.even(y) {
        gray
      }
      //if x == 0 or y == 0 { gray }
    },
    inset: (right: 1.5em),
  )

  
  set page(margin: (
      x: 64pt,
      y: 64pt,
      bottom:74pt,
      right:64pt,
    ),
    number-align: right,
    footer: footer,
  )
  set document(
    title: lettertitle,
  )

  section-start(1)

  // første side
  logo
  casedetails
  mainTitle(lettertitle)

  // innholdet i brevet
  doc
}