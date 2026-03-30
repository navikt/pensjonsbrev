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
      block(above: 13pt)[#input.signerendeSaksbehandler]
      block(input.avsenderEnhet, above: 26pt)
    } else if (signertAvSaksbehandler and signertAvAttestant){
      block(
        grid(
          align: left,
          columns: (1fr, 1fr),
          [#input.signerendeAttestant],
          [#input.signerendeSaksbehandler],
        ),
        above: 12pt,
      )
      block(input.avsenderEnhet, above: 26pt)
    } else { // autobrev
      block(above: 8.8pt)[
        #input.avsenderEnhet
        #v(2.5pt)
        #if input.erVedtaksbrev {
          languageSettings.closingautomatisktextvedtaksbrev
        } else {
          languageSettings.closingautomatisktextinfobrev
        }
      ]
    }
  ]
}

#let closing = {
  block(
    closingGreeting,
    breakable: false,
    above: 47.8pt
  )
  block(
    attachments,
    breakable: false,
    above: 40pt
  )
  section-end(1)
}