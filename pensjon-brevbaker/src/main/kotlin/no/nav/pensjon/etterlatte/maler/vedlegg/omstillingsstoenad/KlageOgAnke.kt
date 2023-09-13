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
val klageOgAnke = createAttachment(
    title = newText(
        Bokmal to "Informasjon om klage og anke ",
        Nynorsk to "",
        English to "",
    ),
    includeSakspart = false,
) {
    veiledningFraNav()
    includePhrase(Felles.HjelpFraAndreForvaltningsloven12)
    klagePaaVedtaketFolketrygdloven2112()
    hvordanSendeInnKlage()
    hvaMaaKlagenInneholde()
    tilbakekreving()
    duKanFaaDekketUtgifter()

}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, Any>.veiledningFraNav() {
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
                    "under og etter saksbehandlingen. Dersom du har spørsmål eller er usikker på noe, vil vi " +
                    "gjøre vårt beste for å hjelpe deg. ",
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
            Bokmal to "Du kan klage på vedtaket innen seks uker fra du mottok det. Kontoret som har fattet " +
                    "vedtaket vil da vurdere saken din på nytt. Hvis du ikke får gjennomslag for klagen din, " +
                    "blir den sendt videre til NAV Klageinstans for ny vurdering og avgjørelse. Dersom du heller " +
                    "ikke får gjennomslag hos klageinstansen, kan du anke saken inn for Trygderetten.",
            Nynorsk to "",
            English to "",
        )
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, Any>.hvordanSendeInnKlage() {
    title2 {
        text(
            Bokmal to "Hvordan sende inn klage?",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "Klagen må være skriftlig og inneholde ditt navn, fødselsnummer og adresse. " +
                    "Du kan benytte skjemaet som du finner på ${Constants.KLAGE_URL}. Klagen kan sendes via " +
                    "innlogging på nettsiden vår, ${Constants.NAV_URL}, eller sendes til oss i posten til NAV " +
                    "${Constants.POSTADRESSE}. Hvis du velger å sende klagen " +
                    "per post, må du huske å undertegne den. Hvis klagen ikke er undertegnet, vil vi dessverre " +
                    "måtte returnere den til deg.",
            Nynorsk to "",
            English to "",
        )
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, Any>.hvaMaaKlagenInneholde() {
    title2 {
        text(
            Bokmal to "Hva må klagen inneholde?",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "Du må skrive",
            Nynorsk to "",
            English to "",
        )
        list {
            item {
                text(
                    Bokmal to "hvilket vedtak du klager på ",
                    Nynorsk to "",
                    English to "",
                )
            }
            item {
                text(
                    Bokmal to "hvilken endring i vedtaket du ber om",
                    Nynorsk to "",
                    English to "",
                )
            }
                    }
        text(
            Bokmal to "Du bør også",
            Nynorsk to "",
            English to "",
        )
        list {
            item {
                text(
                    Bokmal to "skrive hvorfor du mener vedtaket er feil",
                    Nynorsk to "",
                    English to "",
                )
            }
            item {
                text(
                    Bokmal to "nevne erklæringer og andre dokumenter som du legger ved klagen",
                    Nynorsk to "",
                    English to "",
                )
            }
        }
        text(
            Bokmal to "NAV kan hjelpe deg med å skrive ned klagen. Trenger du hjelp, er du velkommen til " +
                    "å ringe oss på telefon ${Constants.KONTAKTTELEFON_PENSJON}.",
            Nynorsk to "",
            English to "",
        )
    }
}


private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, Any>.tilbakekreving() {
    title2 {
        text(
            Bokmal to "Tilbakekreving",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "Du må som hovedregel begynne å betale tilbake når du får fakturaen, selv om du klager " +
                    "på vedtaket. Dette framgår av forvaltningsloven § 42 med tilhørende rundskriv.",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "NAV kan av eget tiltak bestemme at tilbakekrevingen skal utsettes til klagen er " +
                    "behandlet, for eksempel hvis vi ser at det er sannsynlig at det påklagede vedtaket blir omgjort. " +
                    "Du kan også søke om utsettelse av tilbakebetaling til klagen er behandlet. Vi gjør deg " +
                    "oppmerksom på at det ikke gis utsettelse bare av økonomiske grunner.",
            Nynorsk to "",
            English to "",
        )
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, Any>.duKanFaaDekketUtgifter() {
    title2 {
        text(
            Bokmal to "Du kan få dekket utgifter",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "Får du medhold, kan du få dekket vesentlige utgifter som har vært nødvendige for å " +
                    "få endret vedtaket. Du kan ha krav på fri rettshjelp etter rettshjelploven. Informasjon om " +
                    "denne ordningen kan du få hos statsforvalter, advokater eller NAV.",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "Du kan lese om saksomkostninger i forvaltningsloven § 36",
            Nynorsk to "",
            English to "",
        )
    }
}
