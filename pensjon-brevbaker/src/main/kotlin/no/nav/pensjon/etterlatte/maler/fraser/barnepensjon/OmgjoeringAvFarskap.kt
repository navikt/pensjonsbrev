package no.nav.pensjon.etterlatte.maler.fraser.barnepensjon

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language
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

object OmgjoeringAvFarskap {

    data class BegrunnelseForVedtaket(
        val naaevaerendeFar: Expression<Navn>,
        val forrigeFar: Expression<Navn>,
        val opprinneligInnvilgelsesdato: Expression<LocalDate>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Begrunnelse for vedtaket",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                textExpr(
                    Bokmal to
                        "Vi viser til vedtak av ".expr() + opprinneligInnvilgelsesdato.format() + ". " +
                        "Vi har omgjort dette vedtaket fordi vi har fått informasjon om at det er fastslått at ",
                    Nynorsk to "".expr(),
                    English to "".expr(),
                )
                formaterNavn(naaevaerendeFar)
                text(
                    Bokmal to " er din far og at dette gjelder fra du ble født. ",
                    Nynorsk to "",
                    English to "",
                )
                formaterNavn(naaevaerendeFar)
                text(
                    Bokmal to " er ikke død.",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to
                        "Du er ikke lenger gjenlevende barn etter ",
                    Nynorsk to "",
                    English to "",
                )
                formaterNavn(forrigeFar)
                text(
                    Bokmal to
                        ". Begge foreldrene dine lever, og du har derfor ikke rett til barnepensjon. Som en følge av dette er tidligere vedtak om innvilget barnepensjon omgjort.",
                    Nynorsk to "",
                    English to "",
                )
            }
        }
    }
}
