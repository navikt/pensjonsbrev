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
import no.nav.pensjon.etterlatte.maler.Navn
import no.nav.pensjon.etterlatte.maler.fraser.common.formaterNavn
import java.time.LocalDate

object OmgjoeringAvFarskap {

    data class BegrunnelseForVedtaket(
        val vedtaksdato: Expression<LocalDate>,
        val virkningsdato: Expression<LocalDate>,
        val naaevaerendeFar: Expression<Navn>,
        val forrigeFar: Expression<Navn>,
        val innvilgelsesdato: Expression<LocalDate>,
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
                    Language.Bokmal to
                        "Vi viser til vedtak av ".expr() + vedtaksdato.format() + ". " +
                        "Vi har omgjort dette vedtaket fordi vi har fått informasjon om at det er fastslått at ",
                )
                formaterNavn(Language.Bokmal, naaevaerendeFar)
                text(
                    Language.Bokmal to " er din far og at dette gjelder fra du ble født. ",
                )
                formaterNavn(Language.Bokmal, naaevaerendeFar)
                textExpr(
                    Language.Bokmal to " er ikke død. Barnepensjonen din opphører fra ".expr() + formatertVirkningsdato + ".",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to
                        "Du er ikke lenger gjenlevende barn etter ",
                )
                formaterNavn(Language.Bokmal, forrigeFar)
                textExpr(
                    Language.Bokmal to
                        ". Begge foreldrene dine lever, og du har derfor ikke rett til barnepensjon. Som en følge av dette er tidligere vedtak om innvilget barnepensjon av ".expr() +
                        innvilgelsesdato.format() +" ugyldig.",
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
