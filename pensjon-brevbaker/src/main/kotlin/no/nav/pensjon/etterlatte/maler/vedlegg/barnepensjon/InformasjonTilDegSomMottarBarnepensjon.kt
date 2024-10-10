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
    skattetrekkPaaBarnepensjonNasjonal()
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
    skattetrekkPaaBarnepensjonUtland()
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
            Bokmal to "Du kan sjekke og endre kontonummeret som er registrert på deg ved å logge inn på ${Constants.NAV_URL}. Hvis du ikke kan melde fra digitalt, kan du melde om endringer via post. Du må da legge ved kopi av gyldig legitimasjon, eller vise gyldig legitimasjon ved personlig fremmøte på et NAV-kontor.",
            Nynorsk to "Du kan sjekke og endre kontonummeret som er registrert på deg ved å logge inn på ${Constants.NAV_URL}. Dersom du ikkje kan melde frå digitalt, kan du melde om endringar per post. Du må då leggje ved kopi av gyldig legitimasjon, eller vise gyldig legitimasjon ved personleg frammøte på eit NAV-kontor. ",
            English to "You can check and change the bank account number that is registered in our system by logging in to our website: ${Constants.NAV_URL}. If you are unable to notify us digitally, you can report any changes using conventional mail. Please remember to include a copy of a valid identification document or coming to the NAV office in person and bringing proof of identity.",
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

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, Any>.skattetrekkPaaBarnepensjonNasjonal() {
    title2 {
        text(
            Bokmal to "Skattetrekk på barnepensjon",
            Nynorsk to "Skattetrekk på barnepensjon",
            English to "Tax deductions on children's pensions",
        )
    }
    paragraph {
        text(
            Bokmal to "Barnepensjon er skattepliktig, men vi trekker ikke skatt uten at du har gitt beskjed om det. Du kan legge til et frivillig skattetrekk som en prosentandel av pensjonen eller som et fast beløp. Dette sikrer at skatten blir riktig og gir mindre risiko for restskatt. Har du spørsmål om størrelsen på skattetrekk på barnepensjonen må du ta kontakt med Skatteetaten.",
            Nynorsk to "Barnepensjon er skattepliktig, men vi trekkjer ikkje skatt av beløpet utan at det er avtalt. Du kan leggje til eit frivillig skattetrekk anten som prosentdel av pensjonen eller som fast beløp. Dette sikrar at skatten blir rett, og gir mindre risiko for restskatt. Ta kontakt med Skatteetaten dersom du har spørsmål om kor stort skattetrekket vil vere.",
            English to "A children’s pension is taxable, but we do not deduct tax from the amount unless we have agreed with you to do so. You can add a voluntary tax deduction as a percentage of your pension or as a fixed amount. This ensures that your tax payment is correct, and it minimises the risk of back taxes. If you have questions about the amount of tax deduction on your children's pension, you must contact the Norwegian Tax Administration.",
        )
    }
    paragraph {
        text(
            Bokmal to "Har du allerede meldt fra om frivillig skattetrekk, videreføres dette i årene fremover til du melder fra om endring.",
            Nynorsk to "Viss du allereie har meldt frå om frivillig skattetrekk, blir dette vidareført i åra framover inntil du melder frå om endring.",
            English to "If you have already notified us about the voluntary tax withholding, this will continue in future years until you report a change.",
        )
    }
    paragraph {
        text(
            Bokmal to "Du kan lese mer om skattetrekk på ${Constants.BP_SKATTETREKK}.",
            Nynorsk to "Du kan lese meir om skattetrekk på ${Constants.BP_SKATTETREKK}.",
            English to "Read more about tax deductions at ${Constants.BP_SKATTETREKK}.",
        )
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, Any>.skattetrekkPaaBarnepensjonUtland() {
    title2 {
        text(
            Bokmal to "Skattetrekk på barnepensjon",
            Nynorsk to "Skattetrekk på barnepensjon",
            English to "Tax deductions on children's pensions",
        )
    }
    paragraph {
        text(
            Bokmal to "Barnepensjon er skattepliktig, men vi trekker ikke skatt uten at du har gitt beskjed om det. Skatteetaten svarer på spørsmål om skatt på pensjon for deg som ikke er skattemessig bosatt i Norge. Les mer om skatt på ${Constants.SKATTETREKK_KILDESKATT_URL}.",
            Nynorsk to "Barnepensjon er skattepliktig, men vi trekkjer ikkje skatt utan at du har gitt beskjed om det. Skatteetaten svarer på spørsmål om skatt på pensjon for deg som ikkje er skattemessig busett i Noreg. Les meir om skatt på ${Constants.SKATTETREKK_KILDESKATT_URL}.",
            English to "Children’s pension is taxable; however, we do not deduct tax if you do not notify us to do so. The Tax Administration will respond to any queries regarding tax on pensions for those who are resident in Norway for tax purposes. Read more about tax at:  ${Constants.SKATTETREKK_KILDESKATT_URL}.",
        )
    }
}