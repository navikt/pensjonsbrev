package no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon

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
val dineRettigheterOgPlikter = createAttachment(
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
    klagePaaVedtaketFolketrygdloven2112()
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, Any>.meldFraOmEndringer() {
    title2 {
        text(
            Bokmal to "Meld fra om endringer",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "Du må melde fra med en gang det skjer viktige endringer i barnets liv, som",
            Nynorsk to "",
            English to "",
        )
        list {
            item {
                text(
                    Bokmal to "endringer av familie- eller omsorgsforhold",
                    Nynorsk to "",
                    English to "",
                )
            }
            item {
                text(
                    Bokmal to "flytting eller opphold i et annet land over tid",
                    Nynorsk to "",
                    English to "",
                )
            }
            item {
                text(
                    Bokmal to "varig opphold i institusjon",
                    Nynorsk to "",
                    English to "",
                )
            }
        }
        text(
            Bokmal to "Du er ansvarlig for å holde deg orientert om bevegelser på kontoen for utbetaling av barnepensjon, " +
                "og du må straks melde fra om eventuelle feil til NAV. Er det utbetalt for mye barnepensjon fordi " +
                "NAV ikke har fått beskjed, må pengene vanligvis betales tilbake.",
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
            Bokmal to "Vi har plikt til å veilede deg om dine rettigheter og plikter i saken din, både før, " +
                "under og etter saksbehandlingen. Dersom du har spørsmål eller er usikker på noe, vil vi gjøre " +
                "vårt beste for å hjelpe deg.",
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
            Bokmal to "Du har som hovedregel rett til å se dokumentene i saken din. Hvis du ønsker innsyn, kan du kontakte oss på telefon eller per post.",
            Nynorsk to "",
            English to "",
        )
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, Any>.klagePaaVedtaketFolketrygdloven2112() {
    title2 {
        text(
            Bokmal to "Klage på vedtaket - folketrygdloven § 21-12",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "Du kan klage på vedtaket innen seks uker fra du mottok det. Kontoret som har " +
                "fattet vedtaket vil da vurdere saken din på nytt.",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "Klagen må være skriftlig og inneholde navn, fødselsnummer og adresse. " +
                    "Bruk gjerne skjemaet som du finner på ${Constants.KLAGE_URL}. " +
                    "Trenger du hjelp, er du velkommen til å ringe oss på telefon ${Constants.KONTAKTTELEFON_PENSJON}.",
            Nynorsk to "",
            English to "",
        )
    }
}
