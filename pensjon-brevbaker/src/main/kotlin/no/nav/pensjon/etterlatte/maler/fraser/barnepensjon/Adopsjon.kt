package no.nav.pensjon.etterlatte.maler.fraser.barnepensjon

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import java.time.LocalDate

object Adopsjon {

    data class BegrunnelseForVedtaket(
        val virkningsdato: Expression<LocalDate>,
        val navn: Expression<String>,
    ) : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            title2 {
                text(
                    Language.Bokmal to "Begrunnelse for vedtaket",
                )
            }
            val formatertVirkningsdato = virkningsdato.format()
            paragraph {
                textExpr(
                    Language.Bokmal to "Barnepensjonen din opphører fra".expr() + formatertVirkningsdato + ".",
                )
            }
            paragraph {
                textExpr(
                    Language.Bokmal to "Vi viser til informasjon fra deg/verge om at du er adoptert av".expr() + navn + " fra " + formatertVirkningsdato + ".",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Retten til barnepensjon faller bort dersom barnet blir adoptert av et ektepar, " +
                        "eller dersom en ektefelle adopterer den andre ektefellens barn. " +
                        "Barnepensjonen faller bort fra og med måneden etter at adopsjonen er vedtatt.",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Vedtaket er gjort etter bestemmelsene i folketrygdloven § 18-7 og § 22-12.",
                )
            }
        }
    }
}
