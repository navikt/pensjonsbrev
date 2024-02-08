package no.nav.pensjon.etterlatte.maler.fraser.barnepensjon

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

object BarnepensjonForeldreloesFraser {

    data class FoersteDel(
        val virkningstidspunkt: Expression<LocalDate>,
        val sistePeriodeFom: Expression<LocalDate>,
        val sistePeriodeBeloep: Expression<Kroner>,
        val bareEnPeriode: Expression<Boolean>,
        val enEllerFlerePerioderMedFlereBeloep: Expression<Boolean>,
        val ingenUtbetaling: Expression<Boolean>,
    ): OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            val formatertVirkningsdato = virkningstidspunkt.format()
            val formatertBeloep = sistePeriodeBeloep.format()
            val formatertFom = sistePeriodeFom.format()

            paragraph {
                textExpr(
                    Language.Bokmal to "Du er innvilget barnepensjon fra $formatertVirkningsdato} fordi begge foreldrene dine er registrert død.".expr() + ifElse (bareEnPeriode, "Du får $formatertBeloep kroner hver måned før skatt.", "") ,
                    Language.Nynorsk to "".expr(),
                    Language.English to "".expr(),
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Se hvordan vi har beregnet pensjonen din i vedlegget “Beregning av barnepensjon”.",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }
            showIf(enEllerFlerePerioderMedFlereBeloep) {
                paragraph {
                    text(
                        Language.Bokmal to "Du får $formatertBeloep kroner hver måned før skatt fra $formatertFom. Se beløp for tidligere perioder og hvordan vi har beregnet pensjonen i vedlegg “Beregning av barnepensjon”.",
                        Language.Nynorsk to "",
                        Language.English to "",
                    )
                }
            }
            showIf(ingenUtbetaling) {
                paragraph {
                    text(
                        Language.Bokmal to "Du får ikke utbetalt barnepensjon fordi den er redusert utfra det du mottar i uføretrygd fra NAV.",
                        Language.Nynorsk to "",
                        Language.English to "",
                    )
                }
            }
            paragraph {
                text(
                    Language.Bokmal to "Se hvordan vi har beregnet pensjonen din i vedlegget “Beregning av barnepensjon”.",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }
        }
    }
}
