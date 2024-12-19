package no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
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
        Bokmal to "Informasjon til deg som mottar barnepensjon",
        Nynorsk to "Informasjon til deg som får barnepensjon",
        English to "Information to recipients of children’s pensions",
    ),
    includeSakspart = false,
) {
    informasjon()
    postadresse(utland = false.expr())
    endringAvKontonummerNasjonal()
}

@TemplateModelHelpers
val informasjonTilDegSomMottarBarnepensjonUtland = createAttachment(
    title = newText(
        Bokmal to "Informasjon til deg som mottar barnepensjon",
        Nynorsk to "Informasjon til deg som får barnepensjon",
        English to "Information to recipients of children’s pensions",
    ),
    includeSakspart = false,
) {
    informasjon()
    postadresse(utland = true.expr())
    endringAvKontonummerUtland()
    utbetalingAvBarnepensjonUtland()
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, Any>.informasjon() {
    paragraph {
        text(
            Bokmal to "Du kan logge deg inn på våre nettsider for å se utbetalinger, brev eller lignende. Du kan også chatte eller sende melding via ${Constants.SKRIVTILOSS_URL}. Har du ikke BankID eller annen innloggingsmulighet til vår hjemmeside nav.no, kan du kontakte oss på telefon. Skal du sende oss noe per post må du bruke adressen",
            Nynorsk to "Du kan logge deg inn på nettsidene våre for å sjå utbetalingar, brev eller liknande. Du kan også chatte eller sende melding via ${Constants.SKRIVTILOSS_URL}. Har du ikkje BankID eller andre moglegheiter til å logge på heimesida vår nav.no, kan du kontakte oss på telefon. Skal du sende oss noko per post, bruker du adressa",
            English to "You can log in to our website to see your payments, letters or similar. You can also chat with us or send a message online: ${Constants.Engelsk.SKRIVTILOSS_URL}. Please contact us by phone if you do not use BankID or another log in option. To send us any documents by conventional mail, please use this",
        )
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, Any>.endringAvKontonummerNasjonal() {
    title2 {
        text(
            Bokmal to "Skal du endre kontonummer?",
            Nynorsk to "Skal du endre kontonummer?",
            English to "Are you going to change your bank account number?",
        )
    }
    paragraph {
        text(
            Bokmal to "Du kan sjekke og endre kontonummeret som er registrert på deg ved å logge inn på ${Constants.NAV_URL}. Hvis du ikke kan melde fra digitalt, kan du melde om endringer via post. Du må da legge ved kopi av gyldig legitimasjon, eller vise gyldig legitimasjon ved personlig fremmøte på et Nav-kontor.",
            Nynorsk to "Du kan sjekke og endre kontonummeret som er registrert på deg ved å logge inn på ${Constants.NAV_URL}. Dersom du ikkje kan melde frå digitalt, kan du melde om endringar per post. Du må då leggje ved kopi av gyldig legitimasjon, eller vise gyldig legitimasjon ved personleg frammøte på eit Nav-kontor. ",
            English to "You can check and change the bank account number that is registered in our system by logging in to our website: ${Constants.NAV_URL}. If you are unable to notify us digitally, you can report any changes using conventional mail. Please remember to include a copy of a valid identification document or coming to the Nav office in person and bringing proof of identity.",
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

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, Any>.endringAvKontonummerUtland() {
    title2 {
        text(
            Bokmal to "Skal du endre kontonummer?",
            Nynorsk to "Skal du endre kontonummer?",
            English to "Are you going to change your bank account number?",
        )
    }
    paragraph {
        text(
            Bokmal to "Hvis du logger på ${Constants.NAV_URL} med BankID, Buypass eller Comfides, kan du endre kontonummer i \"Personopplysninger\" på www.nav.no. Hvis du ikke kan melde fra digitalt, kan du melde om endringer via post.",
            Nynorsk to "Dersom du loggar på ${Constants.NAV_URL} med BankID, Buypass eller Comfides, kan du endre kontonummer i \"Personopplysningar\" på www.nav.no. Dersom du ikkje kan melde frå digitalt, kan du melde om endringar via post. ",
            English to "If you log in to ${Constants.NAV_URL} using BankID, Buypass or Comfides, you can send us a message within the site ${Constants.SKRIVTILOSS_URL}. If you are unable to notify us digitally, you can report any changes using conventional mail.",
        )
    }
    paragraph {
        text(
            Bokmal to "Du finner skjema på ${Constants.Utland.ENDRE_KONTONUMMER_SKJEMA_URL}. Husk underskrift på skjemaet og legg ved kopi av gyldig legitimasjon.",
            Nynorsk to "Du finn skjema på ${Constants.Utland.ENDRE_KONTONUMMER_SKJEMA_URL}. Hugs underskrift på skjemaet og legg ved kopi av gyldig legitimasjon.",
            English to "You will find the form here: ${Constants.Utland.ENDRE_KONTONUMMER_SKJEMA_URL}. Remember to sign the form and doctor enclose a copy of your identification. ",
        )
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, Any>.utbetalingAvBarnepensjonUtland() {
    title2 {
        text(
            Bokmal to "Utbetaling av barnepensjon",
            Nynorsk to "Utbetaling av barnepensjon",
            English to "Payment of the children's pension",
        )
    }
    paragraph {
        text(
            Bokmal to "Når kontonummer er i en utenlandsk bank, må du være oppmerksom på at det er et gebyr for hver utbetaling. Det kan også ta litt lenger tid før pengene er på kontoen din. Du finner mer informasjon om utbetaling på ${Constants.Utland.UTBETALING_INFO}.",
            Nynorsk to "Når kontonummer er i ein utanlandsk bank, må du vere merksam på at det blir trekt eit gebyr for kvar utbetaling. Det kan også ta litt lenger tid før pengane er på kontoen din. Du finn meir informasjon om utbetaling på ${Constants.Utland.UTBETALING_INFO}.",
            English to "If the bank account is held by a foreign bank, be aware that a minor fee may be charged for each payment. It will also take a little more time before the money reaches your account. More information on payments is available online: ${Constants.Engelsk.UTBETALING_INFO}.",
        )
    }
}
