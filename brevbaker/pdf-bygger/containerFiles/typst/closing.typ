#import "input.typ": input, languageSettings
#import "content/state.typ": section-end

#let attachments = {
  if(input.attachments != none and input.attachments.len() > 0){
    text(size: 12pt, weight: "bold", tracking: 0.2pt)[#languageSettings.closingvedleggprefix]
    set list(indent: 6.5pt, body-indent: 5pt)
    block(
      input.attachments.map((a)=>{
        [- #a]
      }).join(),
      above: 12.8pt
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
        #block(
          if input.erVedtaksbrev {
            languageSettings.closingautomatisktextvedtaksbrev
          } else {
            languageSettings.closingautomatisktextinfobrev
          }, above: 22pt
        )
      ]
    }
  ]
}

#let closing = {
  block(
    closingGreeting,
    breakable: false,
    above: 44pt
  )
  block(
    attachments,
    breakable: false,
    above: 44pt
  )
  section-end(1)
}