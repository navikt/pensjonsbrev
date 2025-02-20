package no.nav.pensjon.brev.maler.fraser.alderspensjon

import no.nav.pensjon.brev.api.model.maler.alderApi.NormertPensjonsalder
import no.nav.pensjon.brev.api.model.maler.alderApi.NormertPensjonsalderSelectors.aar
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.TextOnlyPhrase
import no.nav.pensjon.brev.template.dsl.TextOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.textExpr

class NormertPensjonsalderFormatering(
    val normertPensjonsalder: Expression<NormertPensjonsalder>
) : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {

        textExpr(
            Bokmal to normertPensjonsalder.aar.format(),
            Nynorsk to normertPensjonsalder.aar.format(),
            English to normertPensjonsalder.aar.format()
        )

    }

}