#import "casedetails.typ" : casedetails
#import "input.typ": input, languageSettings
#import "closing.typ": closing
#import "content/state.typ": section-start
#import "content/list.typ": 
#import "footer.typ": footer
#import "content/table.typ": next-page-table


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
    image("NAV_logo.svg", height: 16pt),
    bottom: 48pt
  )
}


#let template(lettertitle: [], doc) = {
  // generelt oppsett for brevet
  set text(
    font:"Source Sans 3",
    size: 11pt,
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