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
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants
import no.nav.pensjon.etterlatte.maler.fraser.common.Felles

@TemplateModelHelpers
val dineRettigheterOgPlikterOMS = createAttachment(
    title = newText(
        Bokmal to "Dine rettigheter og plikter",
        Nynorsk to "",
        English to "",
    ),
    includeSakspart = false,
) {
    meldFraOmEndringer()
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
                    Bokmal to "du gifter deg, inngår partnerskap eller samboerskap",
                    Nynorsk to "",
                    English to "",
                )
            }
            item {
                text(
                    Bokmal to "gifter deg eller blir samboer med en du tidligere har vært gift med",
                    Nynorsk to "",
                    English to "",
                )
            }
            item {
                text(
                    Bokmal to "får felles barn med ny samboer",
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
                    Bokmal to "får varig opphold i institusjon",
                    Nynorsk to "",
                    English to "",
                )
            }
                    }
        text(
            Bokmal to "Du er ansvarlig for å holde deg orientert om bevegelser på kontoen for utbetaling av " +
                    "omstillingsstønad og du må straks melde fra om du oppdager feil. Hvis du ikke melder fra om " +
                    "endringer og får utbetalt for mye stønad, kan stønad som er utbetalt feil kreves tilbake.",
            Nynorsk to "",
            English to "",
        )
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, Any>.veiledningFraNavForvaltningsloven11() {
    title2 {
        text(
            Bokmal to "Veiledning fra NAV - forvaltningsloven § 11",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "Vi har plikt til å veilede deg om dine rettigheter og plikter i saken din, " +
                    "både før, under og etter saksbehandlingen. Dersom du har spørsmål eller er usikker på noe, " +
                    "vil vi gjøre vårt beste for å hjelpe deg.",
            Nynorsk to "",
            English to "",
        )
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, Any>.duHarRettTilInnsynISakenDin() {
    title2 {
        text(
            Bokmal to "Du har rett til innsyn i saken din - forvaltningsloven § 18 ",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "Du har som hovedregel rett til å se dokumentene i saken din. Hvis du ønsker innsyn, " +
                    "så du kontakte oss på telefon eller per post.",
            Nynorsk to "",
            English to "",
        )
    }
}
