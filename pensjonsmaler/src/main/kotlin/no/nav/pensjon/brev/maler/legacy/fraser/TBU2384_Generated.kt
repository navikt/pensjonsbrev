package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_kravhode_kravmottatdato
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.textExpr

data class TBU2384_Generated(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        // [TBU2384, TBU2384NN, TBU2384EN]

        paragraph {
            textExpr(
                Bokmal to "Vi har avslått søknaden din om uføretrygd som vi mottok ".expr() + pe.vedtaksdata_kravhode_kravmottatdato().format() + ".",
                Nynorsk to "Vi har avslått søknaden din om uføretrygd som vi fekk ".expr() + pe.vedtaksdata_kravhode_kravmottatdato().format() + ".",
                English to "We have denied your application for disability benefit, which we received on ".expr() + pe.vedtaksdata_kravhode_kravmottatdato().format() + ".",
            )
        }
    }
}
