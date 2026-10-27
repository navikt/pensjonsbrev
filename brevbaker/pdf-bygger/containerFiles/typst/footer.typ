#import "content/state.typ": sectionCounter


#let footer(saksnummer, languageSettings) = context {
  let currentPageNumber = counter(page).get().first()

  let currentSection = sectionCounter.at(here()).first()

  let currentSectionEnd = query(label("section-end-" + str(currentSection)))

  let currentSectionEndLocation = currentSectionEnd.first().location()
  let pageNumberAtEndOfSection = counter(page).at(currentSectionEndLocation).first()

  set text(9pt)
  set align(right)
  if(currentPageNumber <= pageNumberAtEndOfSection) {
    if saksnummer != none {
      [#languageSettings.saksnummerprefix #saksnummer #h(1fr)
      #languageSettings.sideprefix #currentPageNumber #languageSettings.sideinfix #pageNumberAtEndOfSection]
    } else {
      [#languageSettings.sideprefix #currentPageNumber #languageSettings.sideinfix #pageNumberAtEndOfSection]
    }
  }
}
