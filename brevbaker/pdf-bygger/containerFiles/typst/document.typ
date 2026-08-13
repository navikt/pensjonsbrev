// Rot-templaten for det generiske "document"-konseptet.
//
// Til forskjell fra `template.typ` (brev) har et dokument verken hilsen, signatur eller vedlegg, og
// konsumenten velger selv hvilke topp-elementer som vises. Side- og tekstoppsettet er felles med
// brev (`content/pagesetup.typ`), og innholdet rendres med de samme komponentene under `content/`.

#import "casedetails.typ": casedetails, documentDate
#import "footer.typ": footer
#import "content/state.typ": section-start, section-end
#import "content/header.typ": logo, mainTitle
#import "content/pagesetup.typ": pageSetup

// Avstand mellom topp-elementene. Det første synlige elementet får ingen ekstra avstand, slik at
// dokumentet ikke får et hull på toppen når et element er skjult.
#let elementSpacing = 48pt

#let documentTemplate(
  title: "",
  showTitle: true,
  showLogo: true,
  showCaseDetails: false,
  showDocumentDate: false,
  showFooter: false,
  input: (:),
  languageSettings: (:),
  doc,
) = pageSetup(
  title: title,
  footer: if showFooter { footer(input, languageSettings) } else { none },
  {
    section-start(1)

    // Datoen vises i saksinformasjonen når begge er med, ellers alene.
    let elements = ()
    if showLogo { elements.push("logo") }
    if showCaseDetails { elements.push("casedetails") }
    if showDocumentDate and not showCaseDetails { elements.push("date") }
    if showTitle { elements.push("title") }

    for (index, element) in elements.enumerate() {
      let above = if index == 0 { 0pt } else { elementSpacing }
      if element == "logo" {
        block(logo, above: above, below: 0pt)
      } else if element == "casedetails" {
        casedetails(input, languageSettings, above: above)
      } else if element == "date" {
        documentDate(input.dokumentDato, above: above)
      } else if element == "title" {
        mainTitle(title, above: above)
      }
    }

    doc

    section-end(1)
  },
)
