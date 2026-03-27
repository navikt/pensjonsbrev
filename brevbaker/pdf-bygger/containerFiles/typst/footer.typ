#import "input.typ": languageSettings, input
#import "content/state.typ": sectionCounter

#let shouldShowFooter = {
  context {
    return true
  }
}

#let footer = context {
  let currentPageNumber = counter(page).get().first()

  let currentSection = sectionCounter.at(here()).first()
  
  let currentSectionEnd = query(label("section-end-" + str(currentSection)))

  let currentSectionEndLocation = currentSectionEnd.first().location()
  let pageNumberAtEndOfSection = counter(page).at(currentSectionEndLocation).first()

  set text(9pt)
  set align(right)
  if(currentPageNumber <= pageNumberAtEndOfSection) {
    [#languageSettings.saksnummerprefix #input.saksnummer #h(1fr)
    #languageSettings.sideprefix #currentPageNumber #languageSettings.sideinfix #pageNumberAtEndOfSection]
  }
}


