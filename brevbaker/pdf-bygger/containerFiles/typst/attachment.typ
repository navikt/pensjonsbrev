#import "content/state.typ": section-start, section-end
#import "template.typ": logo, mainTitle
#import "casedetails.typ" : casedetails

#let startAttachment(title, input, languageSettings, sectionNumber: int, showCaseDetails: bool) = {
  pagebreak(to: "odd", weak: false)
  section-start(sectionNumber)

  logo
  if showCaseDetails {
    casedetails(input, languageSettings)
  }

  mainTitle(title)
}

#let endAttachment(sectionNumber: int) = {
  section-end(sectionNumber)
}