package no.nav.pensjon.brev.maler.alder.endring.sivilstand.fraser

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

data class DuFaarAP(
    val kravVirkDatoFom: Expression<LocalDate>,
    val totalPensjon: Expression<Kroner>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                bokmal { + 
                    "Du får " + totalPensjon.format() + " hver måned før skatt fra " +
                    kravVirkDatoFom.format() + " i alderspensjon fra folketrygden." },
                nynorsk { + 
                    "Du får " + totalPensjon.format() + " kvar månad før skatt frå " +
                    kravVirkDatoFom.format() + " i alderspensjon frå folketrygda." },
                english { + 
                    "You will receive " + totalPensjon.format() + " every month before tax from " +
                    kravVirkDatoFom.format() + " as retirement pension from the National Insurance Scheme." },
            )
        }
    }
}
