package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*

data class TBU1210_Generated(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        // [TBU1210NN, TBU1210, TBU1210EN]

        paragraph {
            textExpr(
                Bokmal to "Blir uføretrygden din redusert på grunn av inntekt beholder du likevel uføregraden din på ".expr() + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().format() + " prosent. Du får utbetalt hele uføretrygden igjen dersom du tjener mindre enn inntektsgrensen din.",
                Nynorsk to "Blir uføretrygda di redusert på grunn av inntekt beheld du likevel uføregraden din på ".expr() + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().format() + " prosent. Du får utbetalt heile uføretrygda att dersom du tener mindre enn inntektsgrensa di.",
                English to "If your disability benefit is reduced due to income, you will still maintain the level of disability you have been granted. If you earn less than the income limit, ".expr() + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().format() + " percent disability benefit will be reinstated.",
            )
        }
    }
}
