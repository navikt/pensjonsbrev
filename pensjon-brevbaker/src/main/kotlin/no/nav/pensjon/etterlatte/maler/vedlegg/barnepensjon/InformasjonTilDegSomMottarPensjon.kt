package no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon

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
import no.nav.pensjon.etterlatte.maler.vedlegg.endringAvKontonummerUtland
import no.nav.pensjon.etterlatte.maler.vedlegg.skattetrekkPaaBarnepensjon
import no.nav.pensjon.etterlatte.maler.vedlegg.skattetrekkPaaBarnepensjonUtland
import no.nav.pensjon.etterlatte.maler.vedlegg.utbetalingUtland

@TemplateModelHelpers
val informasjonTilDegSomMottarPensjon = createAttachment(
    title = newText(
        Language.Bokmal to "Informasjon til deg som mottar pensjon",
        Language.Nynorsk to "Informasjon til deg som mottar pensjon",
        Language.English to "Informasjon til deg som mottar pensjon",
    ),
    includeSakspart = false,
) {
    informasjon()
    postadresse(false.expr())
    endringAvKontonummerSelvMottaker()
    skattetrekkPaaBarnepensjon()
}


@TemplateModelHelpers
val utlandInformasjonTilDegSomMottarPensjon = createAttachment(
    title = newText(
        Language.Bokmal to "Informasjon til deg som handler på vegne av barnet",
        Language.Nynorsk to "Informasjon til deg som handlar på vegner av barnet",
        Language.English to "Information for those acting on behalf of the child",
    ),
    includeSakspart = false,
) {
    informasjon()
    postadresse(true.expr())
    utbetalingUtland()
    endringAvKontonummerUtland(false.expr())
    skattetrekkPaaBarnepensjonUtland()
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Language.Bokmal, Language.Nynorsk, Language.English>, Any>.endringAvKontonummerSelvMottaker() {
    title2 {
        text(
            Language.Bokmal to "Skal du endre kontonummer?",
            Language.Nynorsk to "Skal du endre kontonummer?",
            Language.English to "Are you going to change your bank account number?",
        )
    }
    paragraph {
        text(
            Language.Bokmal to "Du kan sjekke og endre kontonummeret som er registrert på deg ved å logge inn på nav.no. Hvis du ikke kan melde fra digitalt, kan du melde om endringer via post. Du må da legge ved kopi av gyldig legitimasjon, eller vise gyldig legitimasjon ved personlig fremmøte på et NAV-kontor. Du finner mer informasjon og lenke til riktig skjema på ${Constants.KONTONUMMER_URL}",
            Language.Nynorsk to "Du kan sjekke og endre kontonummeret som er registrert på deg ved å logge inn på nav.no. Hvis du ikke kan melde fra digitalt, kan du melde om endringer via post. Du må da legge ved kopi av gyldig legitimasjon, eller vise gyldig legitimasjon ved personlig fremmøte på et NAV-kontor. Du finner mer informasjon og lenke til riktig skjema på ${Constants.KONTONUMMER_URL}",
            Language.English to "Du kan sjekke og endre kontonummeret som er registrert på deg ved å logge inn på nav.no. Hvis du ikke kan melde fra digitalt, kan du melde om endringer via post. Du må da legge ved kopi av gyldig legitimasjon, eller vise gyldig legitimasjon ved personlig fremmøte på et NAV-kontor. Du finner mer informasjon og lenke til riktig skjema på ${Constants.KONTONUMMER_URL}",
            )
    }
}


private fun OutlineOnlyScope<LanguageSupport.Triple<Language.Bokmal, Language.Nynorsk, Language.English>, Any>.informasjon() {
    paragraph {
        text(
            Language.Bokmal to "Du kan logge deg inn på våre nettsider for å se utbetalinger, brev osv. Du kan også chatte eller sende melding via nav.no/skrivtiloss. Har du ikke BankID eller annen innloggingsmulighet til vår hjemmeside nav.no, kan du kontakte oss på telefon." +
                    "Skal du sende oss noe per post må du bruke adressen",
            Language.Nynorsk to "Du kan logge deg inn på våre nettsider for å se utbetalinger, brev osv. Du kan også chatte eller sende melding via nav.no/skrivtiloss. Har du ikke BankID eller annen innloggingsmulighet til vår hjemmeside nav.no, kan du kontakte oss på telefon." +
                    "Skal du sende oss noe per post må du bruke adressen",
            Language.English to "Du kan logge deg inn på våre nettsider for å se utbetalinger, brev osv. Du kan også chatte eller sende melding via nav.no/skrivtiloss. Har du ikke BankID eller annen innloggingsmulighet til vår hjemmeside nav.no, kan du kontakte oss på telefon." +
                    "Skal du sende oss noe per post må du bruke adressen",
        )
    }
}