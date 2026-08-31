#import "casedetails.typ" : casedetails
#import "footer.typ": footer
#import "content/state.typ": section-start
#import "content/header.typ": logo, mainTitle
#import "content/pagesetup.typ": pageSetup


#let template(lettertitle: [], input: (:), languageSettings: (:), doc) = pageSetup(
  title: lettertitle,
  footer: footer(input, languageSettings),
  {
    section-start(1)

    // første side
    logo
    casedetails(input, languageSettings)
    mainTitle(lettertitle)

    // innholdet i brevet
    doc
  },
)
