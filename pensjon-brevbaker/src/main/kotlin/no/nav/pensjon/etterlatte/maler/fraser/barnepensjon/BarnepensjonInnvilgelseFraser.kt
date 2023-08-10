package no.nav.pensjon.etterlatte.maler.fraser.barnepensjon

import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object BarnepensjonInnvilgelseFraser {
    object MeldFraOmEndringer : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Language.Bokmal to "Du må melde fra om endringer",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Du har plikt til å melde fra til oss om endringer som har betydning for utbetalingen av barnepensjon, eller retten til å få barnepensjon. I vedlegget «Dine rettigheter og plikter» ser du hvilke endringer du må si fra om.",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }
        }
    }

    object DuHarRettTilAaKlage : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Language.Bokmal to "Du har rett til å klage",
                    Language.Nynorsk to "Du har rett til å klage",
                    Language.English to "TODO",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Hvis du mener vedtaket er feil, kan du klage innen seks uker fra den datoen " +
                        "du mottok vedtaket. Klagen skal være skriftlig. Du finner skjema og informasjon på ${Constants.KLAGE_URL}.",
                    Language.Nynorsk to "TODO nynorsk",
                    Language.English to "TODO engelsk",
                )
            }
        }
    }
}
