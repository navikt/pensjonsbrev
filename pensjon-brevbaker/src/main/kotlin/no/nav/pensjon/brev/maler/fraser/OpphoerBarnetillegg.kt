package no.nav.pensjon.brev.maler.fraser

import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import java.util.Date

data class BarnetilleggUtbetaltDto(
    val barnetilleggFellesbarnUtbetaltNetto: Kroner,
    val barnetilleggSaerkullsbarnUtbetaltNetto: Kroner
)

data class OpphoerBarnetilleggDto(
    val fdatoPaaBarnetilleggOpphoert: Number
)

object OpphoerBarnetillegg {

    val TBU2290 = OutlinePhrase<LangBokmalNynorskEnglish, Date> {onsketVirkningsDato ->
        paragraph {
            val fdatoPaaBarnetilleggOpphoert = it.select(OpphoerBarnetilleggDto::fdatoPaaBarnetilleggOpphoert)
            text(
                Bokmal to "Vi har vedtatt at barnetillegget i uføretrygden din opphører fra " .expr() + onsketVirkningsDato.format() + " for barn født " .expr() + fdatoPaaBarnetilleggOpphoert.format() +".",
                Nynorsk to "Vi har stansa barnetillegget i uføretrygda di frå <OnsketVirkningsDato> for barn fødd <fdato_på_barn_opphør>.",
                English to "The child supplement in your disability benefit has been discontinued, effective as of <OnsketVirkningsDato>, for child born <fdato_på_barn_opphør>."
            )
        }
    }

    val TBU1120 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
      val barnetilleggFellesbarnUtbetaltNetto = arg.sel
        paragraph {
            text(
                Bokmal to "Du får <TotalNetto> kroner i uføretrygd per måned før skatt.",
                Nynorsk to "Du får <TotalNetto> kroner i uføretrygd per månad før skatt.",
                English to "Your monthly disability benefit payment will be NOK <TotalNetto> before tax."
            )
        }
    }

    val TBU1121 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
        paragraph {
            text(
                Bokmal to "Du får <TotalNetto> kroner i uføretrygd og barnetillegg per måned før skatt.",
                Nynorsk to "Du får <TotalNetto> kroner i uføretrygd og barnetillegg per månad før skatt.",
                English to "Your monthly disability benefit and child supplement payment will be NOK <TotalNetto> before tax."
            )
        }
    }

    val TBU1122 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
        paragraph {
            text(
                Bokmal to "Du får <TotalNetto> kroner i uføretrygd og gjenlevendetillegg per måned før skatt.",
                Nynorsk to "Du får <TotalNetto> kroner i uføretrygd og attlevandetillegg per månad før skatt.",
                English to "Your monthly disability benefit and survivor's supplement payment will be NOK <TotalNetto> before tax."
            )
        }
    }

    val TBU1123 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
        paragraph {
            text(
                Bokmal to "Du får <TotalNetto> kroner i uføretrygd, barne- og gjenlevendetillegg per måned før skatt.",
                Nynorsk to "Du får <TotalNetto> kroner i uføretrygd, barne- og attlevandetillegg per månad før skatt.",
                English to "Your monthly disability benefit, child supplement and survivor's supplement payment will be NOK <TotalNetto> before tax."
            )
        }
    }

    val TBU1253 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
        paragraph {
            text(
                Bokmal to "Du får <TotalNetto> kroner i uføretrygd og ektefelletillegg per måned før skatt.",
                Nynorsk to "Du får <TotalNetto> kroner i uføretrygd og ektefelletillegg per månad før skatt.",
                English to "Your monthly disability benefit and spouse supplement payment will be NOK <TotalNetto> before tax."
            )
        }
    }

    val TBU1254 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
        paragraph {
            text(
                Bokmal to "Du får <TotalNetto> kroner i uføretrygd, barne- og ektefelletillegg per måned før skatt.",
                Nynorsk to "Du får <TotalNetto> kroner i uføretrygd, barne- og ektefelletillegg per månad før skatt.",
                English to "Your monthly disability benefit, child supplement and spouse supplement payment will be NOK <TotalNetto> before tax."
            )
        }
    }

    val TBU4082 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
        paragraph {
            text(
                Bokmal to "Du får <TotalNetto> kroner i barnetillegg per måned før skatt.",
                Nynorsk to "Du får <TotalNetto> kroner i barnetillegg per månad før skatt.",
                English to "Your monthly child supplement payment will be NOK <TotalNetto> before tax."
            )
        }
    }

    val TBU4083 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
        paragraph {
            text(
                Bokmal to "Du får <TotalNetto> kroner i barne- og ektefelletillegg per måned før skatt.",
                Nynorsk to "Du får <TotalNetto> kroner i barne- og ektefelletillegg per månad før skatt.",
                English to "Your monthly child supplement and spouse supplement  payment will be NOK <TotalNetto> before tax."
            )
        }
    }

    val TBU4084 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
        paragraph {
            text(
                Bokmal to "Du får <TotalNetto> kroner i ektefelletillegg per måned før skatt.",
                Nynorsk to "Du får <TotalNetto> kroner i ektefelletillegg per månad før skatt.",
                English to "Your monthly spouse supplement payment will be NOK <TotalNetto> before tax."
            )
        }
    }

    val TBU2223 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
        paragraph {
            text(
                Bokmal to "Uføretrygden blir fortsatt utbetalt senest den 20. hver måned.",
                Nynorsk to "Uføretrygda blir framleis utbetalt seinast den 20. i kvar månad.",
                English to "Your disability benefit will still be paid no later than the 20th of every month."
            )
        }
    }

    val TBU1128 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
        paragraph {
            text(
                Bokmal to "I dette brevet forklarer vi hvilke rettigheter og plikter du har. Det er derfor viktig at du leser hele brevet.",
                Nynorsk to "I dette brevet forklarer vi kva rettar og plikter du har. Det er derfor viktig at du les heile brevet.",
                English to "In this letter we will explain your rights and obligations. Therefore, it is important that you read the whole letter."
            )
        }
    }

    val TBU1092 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
        title1 {
            text(
                Bokmal to "Begrunnelse for vedtaket",
                Nynorsk to "Grunngiving for vedtaket",
                English to "Grounds for the decision"
            )
        }
    }
    val TBU3920 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
        paragraph {
            text(
                Bokmal to "",
                Nynorsk to "For å ha rett til barnetillegg må du forsørgje barn under 18 år. Vi har stansa barnetillegget i uføretrygda fordi barnetbarna har fylt 18 år.",
                English to "To be eligible for child supplement, you must support children under 18 years of age. The child supplement in your disability benefit has been discontinued because your  childchildren has(have) turned 18 years of age."
            )
        }
    }
}