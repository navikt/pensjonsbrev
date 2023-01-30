package no.nav.pensjon.brev.maler.fraser

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import java.time.LocalDate


object OpphoerBarnetillegg {

    // TBU4085
    data class HjemmelForBarnetilleggIUfoeretrygden(
        val harBarnetilleggFellesbarn: Expression<Boolean>,
        val harBarnetilleggSaerkullsbarn: Expression<Boolean>
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                showIf(harBarnetilleggFellesbarn or harBarnetilleggSaerkullsbarn) {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-15, 12-16 og 22-12",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-15, 12-16 og 22-12",
                        English to "The decision has been made pursuant to Section 12-15, 12-16 and 22-12 of the Norwegian National Insurance Act."
                    )
                }.orShow {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-15, og 22-12.",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-15, og 22-12.",
                        English to "The decision has been made pursuant to Section 12-15, and 22-12 of the Norwegian National Insurance Act."
                    )
                }
            }
        }
    }


    // TBU4086
    data class OensketVirkningsDatoForEndring(
        val oensketVirkningsDato: Expression<LocalDate>,
        val harBarnetilleggFellesbarn: Expression<Boolean>,
        val harBarnetilleggSaerkullsbarn: Expression<Boolean>,
        val harOpphoertBarnetilleggForFlereBarn: Expression<Boolean>,

        ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                val dato = oensketVirkningsDato.format()
                showIf(harBarnetilleggFellesbarn or harBarnetilleggSaerkullsbarn) {
                    textExpr(
                        Bokmal to "Barnetillegget ditt er blitt endret fra ".expr() + dato +
                                ". Dette er måneden etter at " +
                                ifElse(harOpphoertBarnetilleggForFlereBarn, "barna", "barnet") +
                                " har fylt 18 år. Dette kaller vi virkningstidspunktet.".expr(),
                        Nynorsk to "Barnetillegget ditt er endra frå ".expr() + dato +
                                ". Dette er månaden etter at " +
                                ifElse(harOpphoertBarnetilleggForFlereBarn, "barna", "barnet") +
                                " har fylt 18 år. Dette kallar vi verknadstidspunktet.".expr(),
                        English to "Your child supplement has been changed from ".expr() + dato + ". This is the month after the "
                                + ifElse(harOpphoertBarnetilleggForFlereBarn, "children","child") +
                                " has have turned 18. This is called the effective date.".expr()
                    )
                } orShow {
                    textExpr(
                        Bokmal to "Barnetillegget ditt har opphørt fra ".expr() + dato +
                                ". Dette er måneden etter at " +
                                ifElse(harOpphoertBarnetilleggForFlereBarn, "barna", "barnet") +
                                " har fylt 18 år. Dette kaller vi virkningstidspunktet.".expr(),
                        Nynorsk to "Barnetillegget ditt er stansa frå ".expr() + dato +
                                ". Dette er månaden etter at " +
                                ifElse(harOpphoertBarnetilleggForFlereBarn, "barna", "barnet") +
                                " har fylt 18 år. Dette kallar vi verknadstidspunktet.".expr(),
                        English to "Your child supplement has been discontinued from ".expr() + dato +
                                ". This is the month after the " +
                                ifElse(harOpphoertBarnetilleggForFlereBarn, "children","child") +
                                " has have turned 18. This is called the effective date.".expr()
                    )
                }
            }
        }
    }
}



