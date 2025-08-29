package no.nav.pensjon.brev.maler.alder.endring.sivilstand.fraser

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text

data class DuFaarAP(
    val kravVirkDatoFom: Expression<String>,
    val totalPensjon: Expression<String>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                bokmal { + 
                    "Du får " + totalPensjon + " hver måned før skatt fra " +
                    kravVirkDatoFom + " i alderspensjon fra folketrygden." },
                nynorsk { + 
                    "Du får " + totalPensjon + " kvar månad før skatt frå " +
                    kravVirkDatoFom + " i alderspensjon frå folketrygda." },
                english { + 
                    "You will receive " + totalPensjon + " every month before tax from " +
                    kravVirkDatoFom + " as retirement pension from the National Insurance Scheme." },
            )
        }
    }
}
