package no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon

import no.nav.pensjon.brev.template.Expression
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
import no.nav.pensjon.etterlatte.maler.fraser.common.kontakttelefonPensjon
import no.nav.pensjon.etterlatte.maler.fraser.common.postadresse

@TemplateModelHelpers
val klageOgAnkeUtland = createAttachment(
    title = newText(
        Bokmal to "Informasjon om klage og anke",
        Nynorsk to "Informasjon om klage og anke",
        English to ""
    ),
    includeSakspart = false,
) {
    veiledning()
    hjelpFraAndre()
    klagePaaVedtaket()
    hvordanSendeKlage(true.expr())
    hvaMaaKlagenInneholde(true.expr())
    duKanFaaDekketUtgifter()
}

@TemplateModelHelpers
val klageOgAnkeNasjonal = createAttachment(
    title = newText(
        Bokmal to "Informasjon om klage og anke",
        Nynorsk to "",
        English to ""
    ),
    includeSakspart = false,
) {
    veiledning()
    hjelpFraAndre()
    klagePaaVedtaket()
    hvordanSendeKlage(false.expr())
    hvaMaaKlagenInneholde(false.expr())
    duKanFaaDekketUtgifter()
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, Any>.duKanFaaDekketUtgifter() {
    title2 {
        text(
            Bokmal to "Du kan få dekket utgifter",
            Nynorsk to "Du kan få dekt utgifter",
            English to ""
        )
    }
    paragraph {
        text(
            Bokmal to "Får du medhold, kan du få dekket vesentlige utgifter som har vært nødvendige " +
                    "for å få endret vedtaket. Du kan ha krav på fri rettshjelp etter rettshjelploven. " +
                    "Informasjon om denne ordningen kan du få hos statsforvalteren, advokater eller NAV.",
            Nynorsk to "Dersom du får medhald, kan du få dekt vesentlege utgifter som har vore nødvendige " +
                    "for å få endra vedtaket. Du kan ha krav på fri rettshjelp etter rettshjelplova. " +
                    "Statsforvaltaren, ein advokat eller NAV kan gi deg meir informasjon om denne ordninga.",
            English to ""
        )
    }
    paragraph {
        text(
            Bokmal to "Du kan lese om saksomkostninger i forvaltningsloven § 36.",
            Nynorsk to "Sakskostnader er beskrive nærmare i forvaltingslova § 36.",
            English to ""
        )
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, Any>.hvaMaaKlagenInneholde(
    bosattUtland: Expression<Boolean>
) {
    title2 {
        text(
            Bokmal to "Hva må klagen inneholde?",
            Nynorsk to "Kva klaga skal innehalde",
            English to ""
        )
    }
    paragraph {
        text(
            Bokmal to "Du må skrive",
            Nynorsk to "Du må opplyse",
            English to ""
        )
        list {
            item {
                text(
                    Bokmal to "hvilket vedtak du klager på",
                    Nynorsk to "kva vedtak du klagar på",
                    English to ""
                )
            }
            item {
                text(
                    Bokmal to "hvilken endring i vedtaket du ber om",
                    Nynorsk to "kva endring du ønskjer i vedtaket",
                    English to ""
                )
            }
        }
    }
    paragraph {
        text(
            Bokmal to "Du bør også",
            Nynorsk to "I tillegg bør du",
            English to ""
        )
        list {
            item {
                text(
                    Bokmal to "skrive hvorfor du mener vedtaket er feil",
                    Nynorsk to "grunngi kvifor du meiner vedtaket er feil",
                    English to ""
                )
            }
            item {
                text(
                    Bokmal to "nevne erklæringer og andre dokumenter som du legger ved klagen",
                    Nynorsk to "nemne erklæringar og andre dokument du legg ved klaga",
                    English to ""
                )
            }
        }
    }
    paragraph {
        text(
            Bokmal to "NAV kan hjelpe deg med å skrive ned klagen. Trenger du hjelp, er du velkommen til å " +
                    "ringe oss på telefon ",
            Nynorsk to " NAV kan hjelpe deg med å skrive ned klaga. Viss du treng hjelp, " +
                    "må du gjerne ringje oss på telefon ",
            English to ""
        )
        kontakttelefonPensjon(bosattUtland)
        text(
            Bokmal to ".",
            Nynorsk to ".",
            English to ""
        )
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, Any>.hvordanSendeKlage(
    bosattUtland: Expression<Boolean>
) {
    title2 {
        text(
            Bokmal to "Hvordan sende inn klage?",
            Nynorsk to "Slik sender du inn ei klage",
            English to ""
        )
    }
    paragraph {
        text(
            Bokmal to "Klagen må være skriftlig og inneholde ditt navn, fødselsnummer og adresse. " +
                    "Du kan benytte skjemaet som du finner på ${Constants.KLAGE_URL}. Klagen kan sendes via " +
                    "innlogging på nettsiden vår, ${Constants.NAV_URL}, eller sendes til oss i posten til ",
            Nynorsk to "Du må setje fram klaga skriftleg og oppgi namn, fødselsnummer og adresse. " +
                    "Bruk gjerne skjemaet du finn på ${Constants.KLAGE_URL}. " +
                    "Du kan logge på og sende klaga via nettsida vår, ${Constants.NAV_URL}, " +
                    "eller du kan sende ho per post til ",
            English to ""
        )
    }
    postadresse(bosattUtland)
    paragraph {
        text(
            Bokmal to "Hvis du velger å sende klagen " +
                    "per post, må du huske å undertegne den. Hvis klagen ikke er undertegnet, vil vi dessverre " +
                    "måtte returnere den til deg.",
            Nynorsk to "Dersom du vel å sende klaga per post, må du hugse å signere henne. " +
                    "Dersom klaga ikkje er signert, vil du diverre få henne i retur.",
            English to ""
        )
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, Any>.klagePaaVedtaket() {
    title2 {
        text(
            Bokmal to "Klage på vedtaket - folketrygdloven § 21-12",
            Nynorsk to "Klage på vedtaket – folketrygdlova § 21-12",
            English to ""
        )
    }
    paragraph {
        text(
            Bokmal to "Du kan klage på vedtaket innen seks uker fra du mottok det. Kontoret som har " +
                    "fattet vedtaket vil da vurdere saken din på nytt. Hvis du ikke får gjennomslag for klagen din, " +
                    "blir den sendt videre til NAV Klageinstans for ny vurdering og avgjørelse. Dersom du heller " +
                    "ikke får gjennomslag hos klageinstansen, kan du anke saken inn for Trygderetten.",
            Nynorsk to "Du kan klage på vedtaket innan seks veker frå du får det. Kontoret som " +
                    "fatta vedtaket, vil då vurdere saka di på nytt. Dersom du ikkje får gjennomslag for klaga di, " +
                    "blir ho send vidare til NAV Klageinstans for ny vurdering og avgjerd. " +
                    "Dersom du ikkje får gjennomslag hos klageinstansen heller, kan du anke saka inn for Trygderetten.",
            English to ""
        )
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, Any>.hjelpFraAndre() {
    title2 {
        text(
            Bokmal to "Hjelp fra andre - forvaltningsloven § 12",
            Nynorsk to "Hjelp frå andre – forvaltingslova § 12",
            English to ""
        )
    }
    paragraph {
        text(
            Bokmal to "Du kan be om hjelp fra andre under hele saksbehandlingen, for eksempel av advokat, " +
                    "rettshjelper, en organisasjon du er medlem av eller en annen myndig person. Hvis den som " +
                    "hjelper deg ikke er advokat, må du gi denne personen en skriftlig fullmakt. Bruk gjerne " +
                    "skjemaet du finner på ${Constants.FULLMAKT_URL}.",
            Nynorsk to "Du har under heile saksbehandlinga høve til å be om hjelp frå til dømes advokat, " +
                    "rettshjelpar, organisasjonar du er medlem av, eller andre myndige personar. Dersom personen som " +
                    "hjelper deg, ikkje er advokat, må du gi vedkomande ei skriftleg fullmakt. Bruk gjerne " +
                    "skjemaet du finn på ${Constants.FULLMAKT_URL}.",
            English to ""
        )
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, Any>.veiledning() {
    title2 {
        text(
            Bokmal to "Veiledning fra NAV - forvaltningsloven § 11",
            Nynorsk to "Rettleiing frå NAV – forvaltingslova § 11",
            English to ""
        )
    }
    paragraph {
        text(
            Bokmal to "Vi har plikt til å veilede deg om dine rettigheter og plikter i saken din, både før, " +
                    "under og etter saksbehandlingen. Dersom du har spørsmål eller er usikker på noe, vil vi gjøre " +
                    "vårt beste for å hjelpe deg.",
            Nynorsk to "Vi pliktar å rettleie deg om rettane og pliktene du har i saka, både før, " +
                    "under og etter saksbehandlinga. Dersom du har spørsmål eller er usikker på noko, vil vi gjere " +
                    "vårt beste for å hjelpe deg.",
            English to ""
        )
    }
}