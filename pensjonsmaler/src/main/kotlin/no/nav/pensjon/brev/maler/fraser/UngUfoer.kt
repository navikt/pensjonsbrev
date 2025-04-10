package no.nav.pensjon.brev.maler.fraser

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import java.time.LocalDate

object UngUfoer {
    /**
     * TBU3008, TBU3009, TBU3010, endrMYUngUfoer20Aar_001
     */

    data class EndringMinsteYtelseUngUfoerVed20aar(val minsteytelseVedVirkSats: Expression<Double>) :
        OutlinePhrase<LangBokmalNynorsk>() {

        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(
                    Language.Bokmal to "Du har tidligere fått innvilget uføretrygd som ung ufør. Fra og med den måneden du fyller 20 år har du rett til høyere minsteytelse.",
                    Language.Nynorsk to "Du har tidlegare innvilga rett som ung ufør i uføretrygda di. Frå og med månaden du fyller 20 år har du rett til høgare minsteyting.",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Sivilstanden din avgjør hva du kan få i minsteytelse som ung ufør:",
                    Language.Nynorsk to "Sivilstanden din avgjer kva du kan få i minsteyting som ung ufør:",
                )
                list {
                    item {
                        text(
                            Language.Bokmal to "Er du enslig, er minste årlige uføretrygd 2,91 ganger folketrygdens grunnbeløp.",
                            Language.Nynorsk to "Er du einsleg, er minste årlege uføretrygd 2,91 gangar grunnbeløpet i folketrygda."
                        )
                    }
                    item {
                        text(
                            Language.Bokmal to "Lever du sammen med en ektefelle eller samboer, er minste årlige ytelse 2,66 ganger folketrygdens grunnbeløp.",
                            Language.Nynorsk to "Lever du saman med ein ektefelle eller sambuar, er minste årlege yting 2,66 gangar grunnbeløpet i folketrygda."
                        )
                    }
                }
            }

            paragraph {
                val satsFormatert = minsteytelseVedVirkSats.formatTwoDecimals()
                textExpr(
                    Language.Bokmal to "Du får derfor en årlig ytelse som utgjør ".expr()
                            + satsFormatert + " ganger grunnbeløpet.",

                    Language.Nynorsk to "Du får derfor ei årleg yting som utgjer ".expr()
                            + satsFormatert + " gangar grunnbeløpet.",
                )
            }
        }
    }

    /**
     * TBU3007
     */
    data class UngUfoer20aar(val kravVirkningFraOgMed: Expression<LocalDate>) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() =
            paragraph {
                val formatertDato = kravVirkningFraOgMed.format()
                textExpr(
                    Language.Bokmal to "Vi har økt uføretrygden din fra ".expr() + formatertDato + " fordi du fyller 20 år. Du vil nå få utbetalt uføretrygd med rettighet som ung ufør.",
                    Language.Nynorsk to "Vi har auka uføretrygda di frå ".expr() + formatertDato + " fordi du fyller 20 år. Du får no utbetalt uføretrygd med rett som ung ufør.",
                )
            }
    }
}