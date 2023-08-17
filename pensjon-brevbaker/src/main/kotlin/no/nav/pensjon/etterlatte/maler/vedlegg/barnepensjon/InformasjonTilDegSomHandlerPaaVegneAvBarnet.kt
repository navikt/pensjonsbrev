package no.nav.pensjon.etterlatte.maler.vedlegg

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants

@TemplateModelHelpers
val informasjonTilDegSomHandlerPaaVegneAvBarnet = createAttachment(
    title = newText(
        Bokmal to "Informasjon til deg som handler på vegne av barnet",
        Nynorsk to "",
        English to "",
    ),
    includeSakspart = false,
) {
    informasjon()
    endringAvKontonummer()
    skattetrekkPaaBarnepensjon()
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, Any>.informasjon() {
    paragraph {
        text(
            Bokmal to "Frem til barn fyller 18 år, er det verge som ivaretar barnets interesser. Barns verge er foreldre eller andre personer oppnevnt av Statsforvalteren.",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "Du kan ikke logge deg inn på våre nettsider på vegne av barnet. Skal du sende oss noe må du bruke adressen ${Constants.POSTADRESSE}.",
            Nynorsk to "",
            English to "",
        )
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, Any>.endringAvKontonummer() {
    title2 {
        text(
            Bokmal to "Endring av kontonummer",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "Ved endring av kontonummer for utbetaling av barnepensjon til barn under 18 år, " +
                "kan du som forelder sende melding via ${Constants.SKRIVTILOSS_URL} eller sende skjema for melding om nytt " +
                "kontonummer per post. Du må da legge ved kopi av gyldig legitimasjon.",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "Oppnevnt verge må melde om endring via post. Det må legges ved kopi av egen legitimasjon og vergefullmakt.",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "Du finner mer informasjon og lenke til riktig skjema på ${Constants.KONTONUMMER_URL}.",
            Nynorsk to "",
            English to "",
        )
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, Any>.skattetrekkPaaBarnepensjon() {
    title2 {
        text(
            Bokmal to "Skattetrekk på barnepensjon",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "Barnepensjon er skattepliktig, men vi trekker ikke skatt uten å få " +
                "beskjed om dette. Hvis du opplyste om ønsket skattetrekk i søknaden, har vi registrert " +
                "dette for i år. Du må selv sjekke om dette skattetrekket overføres ved årsskiftet. " +
                "Du kan lese mer om frivillig skattetrekk på ${Constants.SKATTETREKK_PENGESTOETTE_URL}.",
            Nynorsk to "",
            English to "",
        )
    }
}
