package no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.innvilgelse

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants
import no.nav.pensjon.etterlatte.maler.fraser.common.postadresse

@TemplateModelHelpers
val informasjonTilDegSomMottarBarnepensjonNasjonal = createAttachment(
    title = newText(
        Language.Bokmal to "Informasjon til deg som mottar barnepensjon",
        Language.Nynorsk to "Informasjon til deg som får barnepensjon",
        Language.English to "Information to recipients of children’s pensions",
    ),
    includeSakspart = false,
) {
    informasjon()
    postadresse(utland = false.expr())
    endringAvKontonummerNasjonal()
    skattetrekkPaaBarnepensjonNasjonal()
}

@TemplateModelHelpers
val informasjonTilDegSomMottarBarnepensjonUtland = createAttachment(
    title = newText(
        Language.Bokmal to "Informasjon til deg som mottar barnepensjon",
        Language.Nynorsk to "Informasjon til deg som får barnepensjon",
        Language.English to "Information to recipients of children’s pensions",
    ),
    includeSakspart = false,
) {
    informasjon()
    postadresse(utland = true.expr())
    endringAvKontonummerUtland()
    utbetalingAvBarnepensjonUtland()
    skattetrekkPaaBarnepensjonUtland()
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Language.Bokmal, Language.Nynorsk, Language.English>, Any>.informasjon() {
    paragraph {
        text(
            Language.Bokmal to "Du kan logge deg inn på våre nettsider for å se utbetalinger, brev eller lignende. Du kan også chatte eller sende melding via ${Constants.SKRIVTILOSS_URL}. Har du ikke BankID eller annen innloggingsmulighet til vår hjemmeside nav.no, kan du kontakte oss på telefon. Skal du sende oss noe per post må du bruke adressen",
            Language.Nynorsk to "",
            Language.English to "",
        )
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Language.Bokmal, Language.Nynorsk, Language.English>, Any>.endringAvKontonummerNasjonal() {
    title2 {
        text(
            Language.Bokmal to "Skal du endre kontonummer?",
            Language.Nynorsk to "",
            Language.English to "",
        )
    }
    paragraph {
        text(
            Language.Bokmal to "Du kan sjekke og endre kontonummeret som er registrert på deg ved å logge inn på nav.no. Hvis du ikke kan melde fra digitalt, kan du melde om endringer via post. Du må da legge ved kopi av gyldig legitimasjon, eller vise gyldig legitimasjon ved personlig fremmøte på et NAV-kontor.",
            Language.Nynorsk to "",
            Language.English to "",
            )
    }
    paragraph {
        text(
            Language.Bokmal to "Du finner mer informasjon og lenke til riktig skjema på ${Constants.KONTONUMMER_URL}.",
            Language.Nynorsk to "",
            Language.English to "",
        )
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Language.Bokmal, Language.Nynorsk, Language.English>, Any>.endringAvKontonummerUtland() {
    title2 {
        text(
            Language.Bokmal to "Skal du endre kontonummer?",
            Language.Nynorsk to "",
            Language.English to "",
        )
    }
    paragraph {
        text(
            Language.Bokmal to "Hvis du logger på ${Constants.NAV_URL} med BankID, Buypass eller Comfides, kan du endre kontonummer i \"Personopplysninger\" på www.nav.no. Hvis du ikke kan melde fra digitalt, kan du melde om endringer via post.",
            Language.Nynorsk to "",
            Language.English to "",
        )
    }
    paragraph {
        text(
            Language.Bokmal to "Du finner skjema på ${Constants.Utland.ENDRE_KONTONUMMER_SKJEMA_URL}. Husk underskrift på skjemaet og legg ved kopi av gyldig legitimasjon.",
            Language.Nynorsk to "",
            Language.English to "",
        )
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Language.Bokmal, Language.Nynorsk, Language.English>, Any>.utbetalingAvBarnepensjonUtland() {
    title2 {
        text(
            Language.Bokmal to "Utbetaling av barnepensjon",
            Language.Nynorsk to "Utbetaling av barnepensjon",
            Language.English to "Payment of the children's pension",
        )
    }
    paragraph {
        text(
            Language.Bokmal to "Når kontonummer er i en utenlandsk bank, må du være oppmerksom på at det er et gebyr for hver utbetaling. Det kan også ta litt lenger tid før pengene er på kontoen din. Du finner mer informasjon om utbetaling på ${Constants.Utland.UTBETALING_INFO}.",
            Language.Nynorsk to "",
            Language.English to "",
        )
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Language.Bokmal, Language.Nynorsk, Language.English>, Any>.skattetrekkPaaBarnepensjonNasjonal() {
    title2 {
        text(
            Language.Bokmal to "Skattetrekk på barnepensjon",
            Language.Nynorsk to "Skattetrekk på barnepensjon",
            Language.English to "Tax deductions on children's pensions",
        )
    }
    paragraph {
        text(
            Language.Bokmal to "Barnepensjon er skattepliktig, men vi trekker ikke skatt uten å få beskjed om dette. Hvis du opplyste om ønsket skattetrekk da du søkte barnepensjon, ble dette registrert. Du må selv sjekke om dette skattetrekket blir overført ved hvert årsskifte. Du kan lese mer om frivillig skattetrekk på ${Constants.SKATTETREKK_PENGESTOETTE_URL}.",
            Language.Nynorsk to "",
            Language.English to "",
        )
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Language.Bokmal, Language.Nynorsk, Language.English>, Any>.skattetrekkPaaBarnepensjonUtland() {
    title2 {
        text(
            Language.Bokmal to "Skattetrekk på barnepensjon",
            Language.Nynorsk to "Skattetrekk på barnepensjon",
            Language.English to "Tax deductions on children's pensions",
        )
    }
    paragraph {
        text(
            Language.Bokmal to "Skatteetaten svarer på spørsmål om skatt på pensjon for deg som ikke er skattemessig bosatt i Norge. Les mer om skatt på ${Constants.SKATTETREKK_KILDESKATT_URL}.",
            Language.Nynorsk to "",
            Language.English to "",
        )
    }
}