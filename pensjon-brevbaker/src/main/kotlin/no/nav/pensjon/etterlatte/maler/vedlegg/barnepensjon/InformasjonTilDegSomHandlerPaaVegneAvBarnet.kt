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
        Nynorsk to "Informasjon til deg som handlar på vegner av barnet",
        English to "Information for those acting on behalf of the child",
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
            Bokmal to "Frem til barn fyller 18 år, er det verge som ivaretar barnets interesser. " +
                    "Barns verge er foreldre eller andre personer oppnevnt av Statsforvalteren.",
            Nynorsk to "Fram til barnet fyller 18 år, er det verja som varetek interessene til barnet. " +
                    "Verja til barnet vil vere foreldra eller andre personar som Statsforvaltaren har utnemnt.",
            English to "The interests of children are attended to by a guardian until a child reaches the age of 18. " +
                    "A child's guardian is a parent(s) or another person appointed by the County Governor.",
        )
    }
    paragraph {
        text(
            Bokmal to "Du kan ikke logge deg inn på våre nettsider på vegne av barnet. " +
                    "Skal du sende oss noe må du bruke adressen ${Constants.POSTADRESSE}.",
            Nynorsk to "Du kan ikkje logge deg inn på nettsidene våre på vegner av barnet. " +
                    "Dersom du skal sende oss noko, må du gjere det til adressa ${Constants.POSTADRESSE}.",
            English to "You may not log on to our website on behalf of your child. " +
                    "If you want to send us something, you must use our mailing address: ${Constants.POSTADRESSE}",
        )
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, Any>.endringAvKontonummer() {
    title2 {
        text(
            Bokmal to "Skal du endre kontonummer?",
            Nynorsk to "Skal du endre kontonummer?",
            English to "Are you going to change your bank account number?",
        )
    }
    paragraph {
        text(
            Bokmal to "Forelder kan sende melding via ${Constants.SKRIVTILOSS_URL} eller sende skjema for melding om nytt " +
                    "kontonummer i posten. Du må da legge ved kopi av gyldig legitimasjon.",
            Nynorsk to "Som forelder kan du sende melding via ${Constants.SKRIVTILOSS_URL} " +
                    "eller sende skjema for melding om nytt kontonummer i posten. Legg då ved ein kopi av gyldig legitimasjon.",
            English to "Parent can notify us via ${Constants.Engelsk.SKRIVTILOSS_URL}, " +
                    "or send the Notification of New Account Number Form by conventional mail. You must then enclose a copy of a valid proof of identity.",
        )
    }
    paragraph {
        text(
            Bokmal to "Oppnevnt verge må melde om endring via post. Du må legge ved kopi av egen legitimasjon og vergefullmakt.",
            Nynorsk to "Dersom du er oppnemnd verje, må du melde frå om endring via post. Legg då ved ein kopi av legitimasjonen din og verjefullmakta.",
            English to "The appointed guardian must report the change by mail. You must enclose a copy of your own proof of identity and the power of guardianship.",
        )
    }
    paragraph {
        text(
            Bokmal to "Du finner mer informasjon og lenke til riktig skjema på ${Constants.KONTONUMMER_URL}.",
            Nynorsk to "Du finn meir informasjon og lenkje til rett skjema på ${Constants.KONTONUMMER_URL}.",
            English to "You can find more information and a link to the correct form online: ${Constants.Engelsk.KONTONUMMER_URL}.",
        )
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, Any>.skattetrekkPaaBarnepensjon() {
    title2 {
        text(
            Bokmal to "Skattetrekk på barnepensjon",
            Nynorsk to "Skattetrekk på barnepensjon",
            English to "Tax deductions on children's pensions",
        )
    }
    paragraph {
        text(
            Bokmal to "Barnepensjon er skattepliktig, men vi trekker ikke skatt uten å få " +
                    "beskjed om dette. Hvis du opplyste om ønsket skattetrekk i søknaden, har vi registrert " +
                    "dette for i år. Du må selv sjekke om dette skattetrekket overføres ved årsskiftet. " +
                    "Du kan lese mer om frivillig skattetrekk på ${Constants.SKATTETREKK_PENGESTOETTE_URL}.",
            Nynorsk to "Barnepensjon er skattepliktig, men vi trekkjer ikkje skatt utan at vi har fått beskjed om det. " +
                    "Dersom du opplyste om ønskt skattetrekk i søknaden, har vi registrert dette for i år. " +
                    "Du må sjølv sjekke om dette skattetrekket blir overført ved årsskiftet. " +
                    "Du kan lese meir om frivillig skattetrekk på ${Constants.SKATTETREKK_PENGESTOETTE_URL}.",
            English to "Children's pensions are taxable, but we do not deduct tax without being notified. " +
                    "If you stated the desired tax deduction in the application, we will have registered it for this year. " +
                    "You must check whether this tax deduction will be continued until the end of the year. " +
                    "You can read more about voluntary tax withholding online: ${Constants.SKATTETREKK_PENGESTOETTE_URL}.",
        )
    }
}
