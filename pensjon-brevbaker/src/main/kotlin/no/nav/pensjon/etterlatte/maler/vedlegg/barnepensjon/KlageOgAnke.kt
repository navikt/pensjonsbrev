package no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants
import no.nav.pensjon.etterlatte.maler.fraser.common.Felles

@TemplateModelHelpers
val klageOgAnke = createAttachment(
    title = newText(
        Bokmal to "Informasjon om klage og anke",
        Nynorsk to "",
        English to ""
    ),
    includeSakspart = false,
) {
    includePhrase(Felles.VeiledningFraNavForvaltningsloven11)
    includePhrase(Felles.HjelpFraAndreForvaltningsloven12)

    title2 {
        text(
            Bokmal to "Klage på vedtaket - folketrygdloven § 21-12",
            Nynorsk to "",
            English to ""
        )
    }
    paragraph {
        text(
            Bokmal to "Du kan klage på vedtaket innen seks uker fra du mottok det. Kontoret som har " +
                    "fattet vedtaket vil da vurdere saken din på nytt.",
            Nynorsk to "",
            English to ""
        )
    }
    paragraph {
        text(
            Bokmal to "Hvis du ikke får gjennomslag for klagen din, blir den sendt videre til NAV " +
                    "Klageinstans for ny vurdering og avgjørelse.",
            Nynorsk to "",
            English to ""
        )
    }
    paragraph {
        text(
            Bokmal to "Dersom du heller ikke får gjennomslag hos klageinstansen, kan du anke saken " +
                    "inn for Trygderetten.",
            Nynorsk to "",
            English to ""
        )
    }
    paragraph {
        text(
            Bokmal to "Klagen må være skriftlig og inneholde navn, fødselsnummer og adresse.  " +
                    "Bruk gjerne skjemaet som du finner på ${Constants.KLAGE_URL}. Trenger du hjelp, er du velkommen " +
                    "til å ringe oss på telefon 55 55 33 34.",
            Nynorsk to "",
            English to ""
        )
    }
    paragraph {
        text(
            Bokmal to "Du må skrive",
            Nynorsk to "",
            English to ""
        )
        list {
            item {
                text(
                    Bokmal to "hvilket vedtak du klager på",
                    Nynorsk to "",
                    English to ""
                )
            }
            item {
                text(
                    Bokmal to "hvilken endring i vedtaket du ber om",
                    Nynorsk to "",
                    English to ""
                )
            }
        }
    }
    paragraph {
        text(
            Bokmal to "Du bør også",
            Nynorsk to "",
            English to ""
        )
        list {
            item {
                text(
                    Bokmal to "skrive hvorfor du mener vedtaket er feil",
                    Nynorsk to "",
                    English to ""
                )
            }
            item {
                text(
                    Bokmal to "nevne erklæringer og andre dokumenter som du legger ved klagen",
                    Nynorsk to "",
                    English to ""
                )
            }
        }
    }
    paragraph {
        text(
            Bokmal to "Husk å undertegne klagen, ellers må vi sende den i retur til deg.",
            Nynorsk to "",
            English to ""
        )
    }
    paragraph {
        text(
            Bokmal to "Får du medhold, kan du få dekket vesentlige utgifter som har vært nødvendige " +
                    "for å få endret vedtaket. Du kan ha krav på fri rettshjelp etter rettshjelploven. " +
                    "Informasjon om denne ordningen kan du få hos fylkesmannen, advokater eller NAV.",
            Nynorsk to "",
            English to ""
        )
    }
    paragraph {
        text(
            Bokmal to "Du kan lese om saksomkostninger i forvaltningsloven § 36",
            Nynorsk to "",
            English to ""
        )
    }
}