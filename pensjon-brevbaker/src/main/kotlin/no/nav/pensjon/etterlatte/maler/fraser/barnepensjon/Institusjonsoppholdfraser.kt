package no.nav.pensjon.etterlatte.maler.fraser.barnepensjon

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

object Institusjonsoppholdfraser {
    object Innvilgelse : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Innlagt på institusjon – ingen reduksjon pga dokumenterte utgifter",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "Du er innlagt i helseinstitusjon og barnepensjonen skal etter hovedregelen reduseres som følge av dette. Du har dokumentert nødvendige utgifter til bolig og barnepensjonen din vil derfor ikke være redusert når du er innlagt i institusjon.",
                    Nynorsk to "",
                    English to "",
                )
            }
        }
    }

    data class Lover(val erEtterbetaling: Expression<Boolean>) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            showIf(erEtterbetaling) {
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter bestemmelsene om barnepensjon i folketrygdloven §§ 18-2, 18-3, 18-4, 18-5, 18-8 og § 22-12.",
                        Nynorsk to "",
                        English to "",
                    )
                }
            }.orShow {
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter bestemmelsene om barnepensjon i folketrygdloven §§ 18-2, 18-3, 18-4, 18-5, 18-8 § 22-12 og § 22-13.",
                        Nynorsk to "",
                        English to "",
                    )
                }
            }
        }
    }

    object LoverEndring : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    Bokmal to "Vedtaket er gjort etter bestemmelsene om barnepensjon i folketrygdloven § 18-8 og § 22-12.",
                    Nynorsk to "",
                    English to "",
                )
            }
        }
    }

    data class HarDokumentertUtgiftBarnepensjonBlirRedusertMedMindreEnn90Prosent(
        val virkningsdato: Expression<LocalDate>,
        val prosent: Expression<Int>,
        val kronebeloep: Expression<Kroner>,
    ) :
        OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            val formatertVirkningsdato = virkningsdato.format()
            paragraph {
                text(
                    Bokmal to "Innlagt – har dokumenterte utgift – barnepensjon blir redusert med mindre enn 90% ",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                textExpr(
                    Bokmal to "Barnepensjonen din endres fra ".expr() + formatertVirkningsdato + " fordi du er blitt innlagt i helseinstitusjon. " +
                        "Du har dokumentert nødvendige utgifter til bolig og barnepensjonen din vil bli redusert til " + prosent.format() + " av grunnbeløpet (G). Du får " + kronebeloep.format() + " kroner hver måned.",
                    Nynorsk to "".expr(),
                    English to "".expr(),
                )
            }
            includePhrase(LoverEndring)
            includePhrase(Barnepensjon.SlikHarViBeregnetPensjonenDinTittel)
            paragraph {
                text(
                    Bokmal to "Når du blir innlagt i institusjon skal barnepensjonen som hovedregel utbetales med 10 prosent av grunnbeløpet (G). Siden du har dokumentert nødvendige utgifter til bolig vil pensjonen din utbetales med <mer enn 10 prosent> av grunnbeløpet.",
                    Nynorsk to "",
                    English to "",
                )
            }
        }
    }
}
