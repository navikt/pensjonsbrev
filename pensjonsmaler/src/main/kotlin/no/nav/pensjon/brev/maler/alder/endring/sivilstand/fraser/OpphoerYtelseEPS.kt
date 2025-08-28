package no.nav.pensjon.brev.maler.alder.endring.sivilstand.fraser

import no.nav.pensjon.brev.api.model.MetaforceSivilstand
import no.nav.pensjon.brev.model.bestemtForm
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.textExpr

data class OpphoerYtelseEPS(
    val sivilstand: Expression<MetaforceSivilstand>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    val sivilstandBestemtStorBokstav = sivilstand.bestemtForm(storBokstav = true)
    val sivilstandBestemtLitenBokstav = sivilstand.bestemtForm(storBokstav = false)

    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        // opphorYtelseEPS
        paragraph {
            textExpr(
                Language.Bokmal to sivilstandBestemtStorBokstav + " din mottar ikke lenger egen pensjon eller uføretrygd.",
                Language.Nynorsk to sivilstandBestemtStorBokstav + " din får ikkje lenger eigen pensjon eller eiga uføretrygd.",
                Language.English to
                    "Your ".expr() + sivilstandBestemtLitenBokstav + " no longer receives a pension or disability benefit.",
            )
        }
    }
}
