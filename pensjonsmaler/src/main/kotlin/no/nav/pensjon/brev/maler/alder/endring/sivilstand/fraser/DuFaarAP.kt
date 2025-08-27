package no.nav.pensjon.brev.maler.alder.endring.sivilstand.fraser

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.textExpr

data class DuFaarAP(
    val kravVirkDatoFom: Expression<String>,
    val totalPensjon: Expression<String>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            textExpr(
                Language.Bokmal to
                    "Du får ".expr() + totalPensjon + " hver måned før skatt fra ".expr() +
                    kravVirkDatoFom + " i alderspensjon fra folketrygden.",
                Language.Nynorsk to
                    "Du får ".expr() + totalPensjon + " kvar månad før skatt frå ".expr() +
                    kravVirkDatoFom + " i alderspensjon frå folketrygda.",
                Language.English to
                    "You will receive ".expr() + totalPensjon + " every month before tax from ".expr() +
                    kravVirkDatoFom + " as retirement pension from the National Insurance Scheme.",
            )
        }
    }
}
