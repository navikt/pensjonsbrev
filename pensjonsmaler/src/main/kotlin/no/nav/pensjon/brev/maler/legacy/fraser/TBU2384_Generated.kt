package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_kravhode_kravmottatdato
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text

data class TBU2384_Generated(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        //[TBU2384, TBU2384NN, TBU2384EN]

        paragraph {
            text (
                bokmal { + "Vi har avslått søknaden din om uføretrygd som vi mottok " + pe.vedtaksdata_kravhode_kravmottatdato().format() + "." },
                nynorsk { + "Vi har avslått søknaden din om uføretrygd som vi fekk " + pe.vedtaksdata_kravhode_kravmottatdato().format() + "." },
                english { + "We have denied your application for disability benefit, which we received on " + pe.vedtaksdata_kravhode_kravmottatdato().format() + "." },
            )
        }
    }
}
