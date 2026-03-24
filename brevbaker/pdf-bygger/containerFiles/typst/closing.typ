#import "input.typ": input, languageSettings
#import "content/state.typ": section-end

#let attachments = {
  if(input.attachments != none and input.attachments.len() > 0){
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
  let signertAvSaksbehandler = input.signerendeSaksbehandler != none
  let signertAvAttestant = input.signerendeAttestant != none
  [
    #languageSettings.closinggreeting
    #if (signertAvSaksbehandler and not(signertAvAttestant)) {
      [#input.signerendeSaksbehandler]
    } else if (signertAvSaksbehandler and signertAvAttestant){
      grid(
        align: left,
        columns: (1fr, 1fr),
        [#input.signerendeAttestant],
        [#input.signerendeSaksbehandler],
      )
    } else { // autobrev
      linebreak()
    }
    #input.avsenderEnhet
  ]
  let erAutobrev = not (signertAvAttestant) and not (signertAvSaksbehandler)

  if(erAutobrev) {
    v(2pt)
    if input.erVedtaksbrev {
      [#languageSettings.closingautomatisktextvedtaksbrev]
    } else {
      [#languageSettings.closingautomatisktextinfobrev]
    }
  }
}

#let closing = {
  block(
    closingGreeting,
    breakable: false,
    above: 48pt
  )
  block(
    attachments,
    breakable: false,
    above: 40pt
  )
  section-end(1)
}