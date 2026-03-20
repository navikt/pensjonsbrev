#import "input.typ": input, languageSettings
#import "content/state.typ": section-end

#let attachments = {
  if(input.attachments != none){
    text(size: 11pt, weight: "bold", tracking: 0.2pt)[#languageSettings.closingvedleggprefix]
    set list(body-indent: 15pt)
    pad(
      input.attachments.map((a)=>{
        [- #a]
      }).join(),
      bottom: 10pt
    )
  }
}

#let closingGreeting = {
  [
    #languageSettings.closinggreeting
    #linebreak()
    #if (input.signerendeSaksbehandler != none and input.signerendeAttestant == none) {
      [#input.signerendeSaksbehandler]
    } else {
      grid(
        align: left,
        columns: (1fr, 1fr),
        [#input.signerendeAttestant],
        [#input.signerendeSaksbehandler],
      )
    }
    #linebreak()
    #input.avsenderEnhet
  ]
  let erAutobrev = input.signerendeSaksbehandler == none and input.signerendeAttestant == none
  
  if(erAutobrev) {
    if input.erVedtaksbrev {
      [#languageSettings.closingautomatisktextvedtaksbrev]
    } else {
      [#languageSettings.closingautomatisktextinfobrev]
    }
  }
}

#let closing = {
    block(closingGreeting,
    breakable: false,
    above: 32pt
  )
  block(
    attachments,
    breakable: false,
    above: 40pt
  )
  section-end(1)
}