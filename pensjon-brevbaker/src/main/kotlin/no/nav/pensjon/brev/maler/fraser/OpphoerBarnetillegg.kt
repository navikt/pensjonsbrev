package no.nav.pensjon.brev.maler.fraser

import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*

object OpphoerBarnetillegg {
    val TBU2290 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
        paragraph {
            text(
                Bokmal to "Vi har vedtatt at barnetillegget i uføretrygden din opphører fra <OnsketVirkningsDato> for barn født <fdato_på_barn_opphør>.",
                Nynorsk to "",
                English to ""
            )
        }
    }

    val TBU1120 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
        paragraph {
            text(
                Bokmal to "Du får <TotalNetto> kroner i uføretrygd per måned før skatt.",
                Nynorsk to "",
                English to ""
            )
        }
    }

    val TBU1121 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
        paragraph {
            text(
                Bokmal to "Du får <TotalNetto> kroner i uføretrygd og barnetillegg per måned før skatt.",
                Nynorsk to "",
                English to ""
            )
        }
    }

    val TBU1122 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
        paragraph {
            text(
                Bokmal to "Du får <TotalNetto> kroner i uføretrygd og gjenlevendetillegg per måned før skatt.",
                Nynorsk to "",
                English to ""
            )
        }
    }

    val TBU1123 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
        paragraph {
            text(
                Bokmal to "Du får <TotalNetto> kroner i uføretrygd, barne- og gjenlevendetillegg per måned før skatt.",
                Nynorsk to "",
                English to ""
            )
        }
    }

    val TBU1253 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
        paragraph {
            text(
                Bokmal to "Du får <TotalNetto> kroner i uføretrygd og ektefelletillegg per måned før skatt.",
                Nynorsk to "",
                English to ""
            )
        }
    }

    val TBU1254 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
        paragraph {
            text(
                Bokmal to "Du får <TotalNetto> kroner i uføretrygd, barne- og ektefelletillegg per måned før skatt.",
                Nynorsk to "",
                English to ""
            )
        }
    }

    val TBU4082 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
        paragraph {
            text(
                Bokmal to "Du får <TotalNetto> kroner i barnetillegg per måned før skatt.",
                Nynorsk to "",
                English to ""
            )
        }
    }

    val TBU4083 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
        paragraph {
            text(
                Bokmal to "Du får <TotalNetto> kroner i barne- og ektefelletillegg per måned før skatt.",
                Nynorsk to "",
                English to ""
            )
        }
    }

    val TBU4084 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
        paragraph {
            text(
                Bokmal to "Du får <TotalNetto> kroner i ektefelletillegg per måned før skatt.",
                Nynorsk to "",
                English to ""
            )
        }
    }

    val TBU2223 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
        paragraph {
            text(
                Bokmal to "Uføretrygden blir fortsatt utbetalt senest den 20. hver måned.",
                Nynorsk to "",
                English to ""
            )
        }
    }

    val TBU1128 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
        paragraph {
            text(
                Bokmal to "I dette brevet forklarer vi hvilke rettigheter og plikter du har. Det er derfor viktig at du leser hele brevet.",
                Nynorsk to "",
                English to ""
            )
        }
    }

    val TBU1092 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
        title1 {
            text(
                Bokmal to "Begrunnelse for vedtaket",
                Nynorsk to "",
                English to ""
            )
        }
    }
    val TBU3920 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
        paragraph {
            text(
                Bokmal to "",
                Nynorsk to "",
                English to ""
            )
        }
    }
}