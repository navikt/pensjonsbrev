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
        Language.Nynorsk to "Informasjon til deg som får barnepensjon",
        Language.English to "Information to recipients of children’s pensions",
    ),
    includeSakspart = false,
) {
    informasjon()
    postadresse(utland = false.expr())
    endringAvKontonummerSelvMottaker()
    skattetrekkPaaBarnepensjon()
}

@TemplateModelHelpers
val utlandInformasjonTilDegSomMottarPensjon = createAttachment(
    title = newText(
        Language.Bokmal to "Informasjon til deg som mottar pensjon",
        Language.Nynorsk to "Informasjon til deg som får barnepensjon",
        Language.English to "Information to recipients of children’s pensions",
    ),
    includeSakspart = false,
) {
    informasjon()
    postadresse(utland = true.expr())
    utbetalingUtland()
    endringAvKontonummerUtland(erUnder18Aar = false.expr())
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
            Language.Bokmal to "Du kan sjekke og endre kontonummeret som er registrert på deg ved å logge inn på nav.no. Hvis du ikke kan melde fra digitalt, kan du melde om endringer via post. Du må da legge ved kopi av gyldig legitimasjon, eller vise gyldig legitimasjon ved personlig fremmøte på et NAV-kontor. Du finner mer informasjon og lenke til riktig skjema på ${Constants.KONTONUMMER_URL}.",
            Language.Nynorsk to "Du kan sjekke og endre kontonummeret du er registrert med, ved å logge inn på nav.no. Dersom du ikkje får til å melde frå digitalt, kan du melde om endringar via post. Du må då leggje ved kopi av gyldig legitimasjon, eller vise fram gyldig legitimasjon ved personleg frammøte på eit NAV-kontor. Du finn meir informasjon og lenkje til rett skjema på ${Constants.KONTONUMMER_URL}.",
            Language.English to "You can check and change the bank account number that is registered in our system by logging in to our website: nav.no. If you are unable to notify us digitally, you can report any changes using conventional mail. Please remember to include a copy of a valid identification document or coming to the NAV office in person and bringing proof of identity. You can find more information and a link to the correct form online: ${Constants.KONTONUMMER_URL}.",
            )
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Language.Bokmal, Language.Nynorsk, Language.English>, Any>.informasjon() {
    paragraph {
        text(
            Language.Bokmal to "Du kan logge deg inn på våre nettsider for å se utbetalinger, brev osv. Du kan også chatte eller sende melding via nav.no/skrivtiloss. Har du ikke BankID eller annen innloggingsmulighet til vår hjemmeside nav.no, kan du kontakte oss på telefon.",
            Language.Nynorsk to "Du kan logge deg inn på nettsidene våre for å sjå utbetalingar, brev osv. Du kan også chatte med oss eller sende melding via nav.no/skrivtiloss. Dersom du ikkje har BankID eller andre moglegheiter til å logge på heimesida vår nav.no, kan du kontakte oss på telefon.",
            Language.English to "You can log in to our website to see your payments, letters etc. You can also chat with us or send a message online: nav.no/skrivtiloss. Please contact us by phone if you do not use BankID or another log in option.",
        )
    }
    paragraph {
        text(
            Language.Bokmal to "Skal du sende oss noe per post må du bruke adressen",
            Language.Nynorsk to "Viss du skal sende oss noko per post, bruker du adressa",
            Language.English to "To send us any documents by conventional mail, please use this mailing address:",
        )
    }
}