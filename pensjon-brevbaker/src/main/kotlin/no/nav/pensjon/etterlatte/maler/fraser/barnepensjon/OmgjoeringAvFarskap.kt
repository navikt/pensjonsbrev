package no.nav.pensjon.etterlatte.maler.fraser.barnepensjon

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
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
        val naaevaerendeFar: Expression<Navn>,
        val forrigeFar: Expression<Navn>,
        val opprinneligInnvilgelsesdato: Expression<LocalDate>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Language.Bokmal to "Begrunnelse for vedtaket",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }
            paragraph {
                textExpr(
                    Language.Bokmal to
                        "Vi viser til vedtak av ".expr() + opprinneligInnvilgelsesdato.format() + ". " +
                        "Vi har omgjort dette vedtaket fordi vi har fått informasjon om at det er fastslått at ",
                    Language.Nynorsk to "".expr(),
                    Language.English to "".expr(),
                )
                formaterNavn(naaevaerendeFar)
                text(
                    Language.Bokmal to " er din far og at dette gjelder fra du ble født. ",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
                formaterNavn(naaevaerendeFar)
                text(
                    Language.Bokmal to " er ikke død.",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to
                        "Du er ikke lenger gjenlevende barn etter ",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
                formaterNavn(forrigeFar)
                text(
                    Language.Bokmal to
                        ". Begge foreldrene dine lever, og du har derfor ikke rett til barnepensjon. Som en følge av dette er tidligere vedtak om innvilget barnepensjon omgjort.",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }
        }
    }
}
