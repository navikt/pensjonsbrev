// Rot-templaten for det generiske "document"
//
// Til forskjell fra `template.typ` (brev) har et dokument verken hilsen, signatur eller vedlegg, og
// konsumenten velger selv hvilke topp-elementer som vises. Side- og tekstoppsettet er felles med
// brev (`content/pagesetup.typ`), og innholdet rendres med de samme komponentene under `content/`.

#import "casedetails.typ": casedetails, documentDate
#import "footer.typ": footer
#import "content/state.typ": section-start, section-end
#import "content/header.typ": logo, mainTitle
#import "content/pagesetup.typ": pageSetup

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

    // Settes til `elementSpacing` etter det første synlige elementet.
    let above = 0pt

    if showLogo {
      block(logo, above: above, below: 0pt)
      above = elementSpacing
    }

    if showCaseDetails {
      casedetails(input, languageSettings, above: above)
      above = elementSpacing
    } else if showDocumentDate {
      // Datoen vises i saksinformasjonen når begge er med, ellers alene.
      documentDate(input.dokumentDato, above: above)
      above = elementSpacing
    }

    if showTitle {
      mainTitle(title, above: above)
      above = elementSpacing
    }

    doc

    section-end(1)
  },
)
