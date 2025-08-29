package no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.etterlatte.maler.fraser.common.Felles

@TemplateModelHelpers
val dineRettigheterOgPlikter = createAttachment(
    title = newText(
        Bokmal to "Dine rettigheter og plikter",
        Nynorsk to "Dine rettar og plikter",
        English to "Your rights and obligations",
    ),
    includeSakspart = false,
) {
    meldFraOmEndringer()
    feilutbetaling()
    straffeansvar()
    veiledningFraNavForvaltningsloven11()
    includePhrase(Felles.HjelpFraAndreForvaltningsloven12)
    duHarRettTilInnsynISakenDin()
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, Any>.meldFraOmEndringer() {
    title2 {
        text(
            bokmal { +"Plikt til å opplyse om endringer - folketrygdloven § 21-3" },
            nynorsk { +"Plikt til å opplyse om endringar – folketrygdlova § 21-3" },
            english { +"Duty to notify Nav of any changes – National Insurance Act Section 21-3" },
        )
    }
    paragraph {
        text(
            bokmal { +"Du må melde fra om endringer som" },
            nynorsk { +"Du må melde frå dersom" },
            english { +"You must notify Nav of any changes in your circumstances, for example if:" },
        )
        list {
            item {
                text(
                    bokmal { +"arbeidsinntekten din endrer seg" },
                    nynorsk { +"arbeidsinntekta di endrar seg" },
                    english { +"Your work income changes" },
                )
            }
            item {
                text(
                    bokmal { +"arbeidssituasjonen din endrer seg" },
                    nynorsk { +"arbeidssituasjonen din endrar seg" },
                    english { +"Your work situation changes" },
                )
            }
            item {
                text(
                    bokmal { +"du får innvilget andre stønader fra Nav" },
                    nynorsk { +"du får innvilga andre stønader frå Nav" },
                    english { +"You are granted any other benefits by Nav " },
                )
            }
            item {
                text(
                    bokmal { +"du ikke lenger er arbeidssøker" },
                    nynorsk { +"du ikkje lenger er arbeidssøkjar" },
                    english { +"You are no longer seeking work" },
                )
            }
            item {
                text(
                    bokmal { +"du endrer, avbryter eller reduserer omfanget av utdanningen din" },
                    nynorsk { +"du endrar, avbryt eller reduserer omfanget av utdanninga di" },
                    english { +"You change, discontinue or reduce the scope of your education" },
                )
            }
            item {
                text(
                    bokmal { +"du gifter deg eller inngår partnerskap" },
                    nynorsk { +"du giftar deg eller inngår partnarskap" },
                    english { +"You are getting married or initiating cohabitation" },
                )
            }
            item {
                text(
                    bokmal { +"du blir samboer med en du har felles barn med eller tidligere har vært gift med" },
                    nynorsk { +"du blir sambuar med nokon du har barn med eller tidlegare har vore gift med" },
                    english { +"You become a cohabiting partner with someone you have common children with " +
                            "or were previously married to" },
                )
            }
            item {
                text(
                    bokmal { +"du får felles barn med ny samboer" },
                    nynorsk { +"du får barn med ny sambuar" },
                    english { +"You are expecting a child with a new cohabiting partner" },
                )
            }
            item {
                text(
                    bokmal { +"du skal oppholde deg utenfor Norge i en periode på mer enn seks måneder eller " +
                            "skal flytte til et annet land" },
                    nynorsk { +"du skal opphalde deg utanfor Noreg i meir enn seks månader, eller du skal " +
                            "flytte til eit anna land" },
                    english { +"You stay outside of Norway for more than 6 months or move to another country" },
                )
            }
            item {
                text(
                    bokmal { +"du får varig opphold i institusjon" },
                    nynorsk { +"du får varig opphald på ein institusjon" },
                    english { +"You reside permanently in an institution" },
                )
            }
        }
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, Any>.feilutbetaling() {
    title2 {
        text(
            bokmal { +"Feilutbetaling av stønad folketrygdloven § 22-15 og § 22-16" },
            nynorsk { +"Feilutbetaling av stønad etter folketrygdlova § 22-15 og § 22-16" },
            english { +"Incorrect payment of allowance – National Insurance Act Sections 22-15 and 22-16" },
        )
    }
    paragraph {
        text(
            bokmal { +"Du er ansvarlig for å holde deg orientert om bevegelser på kontoen for utbetaling av " +
                    "omstillingsstønad og du må straks melde fra om du oppdager feil. Hvis du ikke melder fra om " +
                    "endringer og får utbetalt for mye stønad, kan stønad som er utbetalt feil kreves tilbake." },
            nynorsk { +"Du er ansvarleg for å følgje med på kor mykje omstillingsstønad som kjem inn på " +
                    "kontoen din, og må melde frå med ein gong dersom du oppdagar feil. Dersom du får utbetalt for " +
                    "mykje stønad fordi du ikkje har meldt frå om endringar, kan vi krevje at du betaler tilbake " +
                    "det du ikkje hadde rett på." },
            english { +"You are responsible for keeping yourself informed of transactions in your account " +
                    "concerning payment of adjustment allowance and you must notify us if you discover any errors. " +
                    "If you do not notify us of any changes and thereby receive too much in allowance, you can be " +
                    "required to repay the overpaid amount." },
        )
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, Any>.straffeansvar() {
    title2 {
        text(
            bokmal { +"Straffeansvar – folketrygdloven § 25-12" },
            nynorsk { +"Straffeansvar – folketrygdlova § 25-12" },
            english { +"Criminal liability– National Insurance Act Section 25-12" },
        )
    }
    paragraph {
        text(
            bokmal { +"Hvis du med vilje gir feil opplysninger eller ikke gir oss nødvendige opplysninger, " +
                    "kan det medføre straffeansvar." },
            nynorsk { +"Det kan medføre straffeansvar dersom du med vilje gir feil opplysningar eller held " +
                    "tilbake opplysningar vi treng." },
            english { +"If you knowingly provide incorrect information or fail to provide us with the " +
                    "required information, this can lead to criminal liability. " },
        )
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, Any>.veiledningFraNavForvaltningsloven11() {
    title2 {
        text(
            bokmal { +"Veiledning fra Nav - forvaltningsloven § 11" },
            nynorsk { +"Rettleiing frå Nav – forvaltingslova § 11" },
            english { +"Guidance from Nav – Section 11 of the Public Administration Act" },
        )
    }
    paragraph {
        text(
            bokmal { +"Vi har plikt til å veilede deg om dine rettigheter og plikter i saken din, både før, " +
                    "under og etter saksbehandlingen. Dersom du har spørsmål eller er usikker på noe, vil vi gjøre " +
                    "vårt beste for å hjelpe deg." },
            nynorsk { +"Vi pliktar å rettleie deg om rettane og pliktene du har i saka, både før, " +
                    "under og etter saksbehandlinga. Dersom du har spørsmål eller er usikker på noko, " +
                    "vil vi gjere vårt beste for å hjelpe deg." },
            english { +"We have a duty to advise you of your rights and obligations in your case – before, " +
                    "during and after the case has been processed. " +
                    "If you have any questions or are unsure about anything, we will do our best to help you." },
        )
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, Any>.duHarRettTilInnsynISakenDin() {
    title2 {
        text(
            bokmal { +"Du har rett til innsyn i saken din - forvaltningsloven § 18 " },
            nynorsk { +"Du har rett til innsyn i saka di – forvaltingslova § 18" },
            english { +"You have the right to access the documents in your case – Section 18 of the " +
                    "Public Administration Act" },
        )
    }
    paragraph {
        text(
            bokmal { +"Du har som hovedregel rett til å se dokumentene i saken din. Hvis du ønsker innsyn, " +
                    "kan du kontakte oss på telefon eller per post." },
            nynorsk { +"Du har som hovudregel rett til å sjå dokumenta i saka di. Kontakt oss på telefon " +
                    "eller per post dersom du ønskjer innsyn." },
            english { +"As a general rule, you have the right to see the documents in your case. If " +
                    "you want access, you can contact us by phone or mail." },
        )
    }
}
