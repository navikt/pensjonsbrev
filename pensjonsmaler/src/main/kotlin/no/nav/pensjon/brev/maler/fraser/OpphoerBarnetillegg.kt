package no.nav.pensjon.brev.maler.fraser

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
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
                        bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-15, 12-16 og 22-12" },
                        nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-15, 12-16 og 22-12" },
                        english { + "The decision has been made pursuant to Section 12-15, 12-16 and 22-12 of the Norwegian National Insurance Act." }
                    )
                }.orShow {
                    text(
                        bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 12-15, og 22-12." },
                        nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 12-15, og 22-12." },
                        english { + "The decision has been made pursuant to Section 12-15, and 22-12 of the Norwegian National Insurance Act." }
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
                    text(
                        bokmal { + "Barnetillegget ditt er blitt endret fra " + dato +
                                ". Dette er måneden etter at " +
                                ifElse(harOpphoertBarnetilleggForFlereBarn, "barna", "barnet") +
                                " har fylt 18 år. Dette kaller vi virkningstidspunktet." },
                        nynorsk { + "Barnetillegget ditt er endra frå " + dato +
                                ". Dette er månaden etter at " +
                                ifElse(harOpphoertBarnetilleggForFlereBarn, "barna", "barnet") +
                                " har fylt 18 år. Dette kallar vi verknadstidspunktet." },
                        english { + "Your child supplement has been changed from " + dato + ". This is the month after the "
                                + ifElse(harOpphoertBarnetilleggForFlereBarn, "children","child") +
                                " has have turned 18. This is called the effective date." }
                    )
                } orShow {
                    text(
                        bokmal { + "Barnetillegget ditt har opphørt fra " + dato +
                                ". Dette er måneden etter at " +
                                ifElse(harOpphoertBarnetilleggForFlereBarn, "barna", "barnet") +
                                " har fylt 18 år. Dette kaller vi virkningstidspunktet." },
                        nynorsk { + "Barnetillegget ditt er stansa frå " + dato +
                                ". Dette er månaden etter at " +
                                ifElse(harOpphoertBarnetilleggForFlereBarn, "barna", "barnet") +
                                " har fylt 18 år. Dette kallar vi verknadstidspunktet." },
                        english { + "Your child supplement has been discontinued from " + dato +
                                ". This is the month after the " +
                                ifElse(harOpphoertBarnetilleggForFlereBarn, "children","child") +
                                " has have turned 18. This is called the effective date." }
                    )
                }
            }
        }
    }
}



