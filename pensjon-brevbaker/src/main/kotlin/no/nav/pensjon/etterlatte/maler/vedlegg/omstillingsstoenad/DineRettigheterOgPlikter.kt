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
        Nynorsk to "",
        English to "",
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
            Bokmal to "Plikt til å opplyse om endringer - folketrygdloven § 21-3",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "Du må melde fra om endringer som",
            Nynorsk to "",
            English to "",
        )
        list {
            item {
                text(
                    Bokmal to "arbeidsinntekten din endrer seg",
                    Nynorsk to "",
                    English to "",
                )
            }
            item {
                text(
                    Bokmal to "arbeidssituasjonen din endrer seg",
                    Nynorsk to "",
                    English to "",
                )
            }
            item {
                text(
                    Bokmal to "du får innvilget andre stønader fra NAV",
                    Nynorsk to "",
                    English to "",
                )
            }
            item {
                text(
                    Bokmal to "du ikke lenger er arbeidssøker",
                    Nynorsk to "",
                    English to "",
                )
            }
            item {
                text(
                    Bokmal to "du endrer, avbryter eller reduserer omfanget av utdanningen din",
                    Nynorsk to "",
                    English to "",
                )
            }
            item {
                text(
                    Bokmal to "du gifter deg eller inngår partnerskap",
                    Nynorsk to "",
                    English to "",
                )
            }
            item {
                text(
                    Bokmal to "du blir samboer med en du har felles barn med eller tidligere har vært gift med",
                    Nynorsk to "",
                    English to "",
                )
            }
            item {
                text(
                    Bokmal to "du skal oppholde deg utenfor Norge i en periode på mer enn seks måneder eller " +
                            "skal flytte til et annet land",
                    Nynorsk to "",
                    English to "",
                )
            }
            item {
                text(
                    Bokmal to "du får varig opphold i institusjon",
                    Nynorsk to "",
                    English to "",
                )
            }
        }
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, Any>.feilutbetaling() {
    title2 {
        text(
            Bokmal to "Feilutbetaling av stønad folketrygdloven § 22-15 og § 22-16",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "Du er ansvarlig for å holde deg orientert om bevegelser på kontoen for utbetaling av " +
                    "omstillingsstønad og du må straks melde fra om du oppdager feil. Hvis du ikke melder fra om " +
                    "endringer og får utbetalt for mye stønad, kan stønad som er utbetalt feil kreves tilbake.",
            Nynorsk to "",
            English to "",
        )
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, Any>.straffeansvar() {
    title2 {
        text(
            Bokmal to "Straffeansvar – folketrygdloven § 25-12",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "Hvis du med vilje gir feil opplysninger eller ikke gir oss nødvendige opplysninger, " +
                    "kan det medføre straffeansvar.",
            Nynorsk to "",
            English to "",
        )
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, Any>.veiledningFraNavForvaltningsloven11() {
    title2 {
        text(
            Bokmal to "Veiledning fra NAV - forvaltningsloven § 11",
            Nynorsk to "Rettleiing frå NAV – forvaltingslova § 11",
            English to "Guidance from NAV – Section 11 of the Public Administration Act",
        )
    }
    paragraph {
        text(
            Bokmal to "Vi har plikt til å veilede deg om dine rettigheter og plikter i saken din, både før, " +
                    "under og etter saksbehandlingen. Dersom du har spørsmål eller er usikker på noe, vil vi gjøre " +
                    "vårt beste for å hjelpe deg.",
            Nynorsk to "Vi pliktar å rettleie deg om rettane og pliktene du har i saka, både før, " +
                    "under og etter saksbehandlinga. Dersom du har spørsmål eller er usikker på noko, " +
                    "vil vi gjere vårt beste for å hjelpe deg.",
            English to "We have a duty to advise you of your rights and obligations in your case – before, " +
                    "during and after the case has been processed. " +
                    "If you have any questions or are unsure about anything, we will do our best to help you.",
        )
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, Any>.duHarRettTilInnsynISakenDin() {
    title2 {
        text(
            Bokmal to "Du har rett til innsyn i saken din - forvaltningsloven § 18 ",
            Nynorsk to "Du har rett til innsyn i saka di – forvaltingslova § 18",
            English to "You have the right to access the documents in your case – Section 18 of the " +
                    "Public Administration Act",
        )
    }
    paragraph {
        text(
            Bokmal to "Du har som hovedregel rett til å se dokumentene i saken din. Hvis du ønsker innsyn, " +
                    "kan du kontakte oss på telefon eller per post.",
            Nynorsk to "Du har som hovudregel rett til å sjå dokumenta i saka di. Kontakt oss på telefon " +
                    "eller per post dersom du ønskjer innsyn.",
            English to "As a general rule, you have the right to see the documents in your case. If " +
                    "you want access, you can contact us by phone or mail.",
        )
    }
}
