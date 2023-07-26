package no.nav.pensjon.etterlatte.maler.fraser.barnepensjon

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
import no.nav.pensjon.etterlatte.maler.Navn
import no.nav.pensjon.etterlatte.maler.fraser.common.formaterNavn
import java.time.LocalDate

object Adopsjon {

    data class BegrunnelseForVedtaket(
        val virkningsdato: Expression<LocalDate>,
        val adoptertAv1: Expression<Navn>,
        val adoptertAv2: Expression<Navn?>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Begrunnelse for vedtaket",
                    Nynorsk to "",
                    English to "",
                )
            }
            val formatertVirkningsdato = virkningsdato.format()
            paragraph {
                textExpr(
                    Bokmal to "Barnepensjonen din opphører fra ".expr() + formatertVirkningsdato + ".",
                    Nynorsk to "".expr(),
                    English to "".expr(),
                )
            }
            paragraph {
                text(
                    Bokmal to "Vi viser til informasjon fra deg/verge om at du er adoptert av ",
                    Nynorsk to "",
                    English to "",
                )
                formaterNavn(adoptertAv1)
                ifNotNull(adoptertAv2) { text(Bokmal to " og ", Nynorsk to " og ", English to " and ") }
                ifNotNull(adoptertAv2) { ad2 -> formaterNavn(ad2) }
                textExpr(
                    Bokmal to " fra ".expr() + formatertVirkningsdato + ".",
                    Nynorsk to "".expr(),
                    English to "".expr(),
                )
            }
            paragraph {
                text(
                    Bokmal to "Retten til barnepensjon faller bort dersom barnet blir adoptert av et ektepar, " +
                        "eller dersom en ektefelle adopterer den andre ektefellens barn. " +
                        "Barnepensjonen faller bort fra og med måneden etter at adopsjonen er vedtatt.",
                    Nynorsk to "",
                    English to "",
                )
            }
        }
    }
}
