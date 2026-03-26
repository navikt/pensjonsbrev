#import "casedetails.typ" : casedetails
#import "input.typ": input, languageSettings
#import "closing.typ": closing
#import "content/state.typ": section-start
#import "content/list.typ": 
#import "footer.typ": footer
#import "content/table.typ": letter-table


#let mainTitle(lettertitle) = {
    title[
      #pad(
        text(
          size: 16pt,
          weight: "bold",
          lettertitle,
          tracking: 0.3pt
        ),
      )
    ]
}


#let logo = {
  pad(
    image("NAV_logo.svg", height: 16pt),
    bottom: 32pt
  )
}


#let template(lettertitle: [], doc) = {
  // generelt oppsett for brevet
  set text(
    font:"Source Sans 3",
    size: 11pt,
  )

  show link: it => it.body // disable automatic hyperlinking.
  set par(
    leading: 8.7pt,  // Line spacing within paragraphs
    spacing: 24pt, // Space between paragraphs
  )

  set page(margin: (
      x: 64pt,
      y: 64pt,
      bottom:74pt,
      right:64pt,
    ),
    footer: footer,
    footer-descent: 30% + 4pt,
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