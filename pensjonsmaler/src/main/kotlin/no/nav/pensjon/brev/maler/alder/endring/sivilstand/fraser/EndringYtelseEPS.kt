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

data class EndringYtelseEPS(
    val sivilstand: Expression<MetaforceSivilstand>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    val sivilstandBestemtLitenBokstav = sivilstand.bestemtForm(storBokstav = false)

    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        // endringYtelseEPS
        paragraph {
            textExpr(
                Language.Bokmal to
                    "Pensjonen eller uføretrygden til ".expr() + sivilstandBestemtLitenBokstav + " din er endret.",
                Language.Nynorsk to "Pensjonen eller uføretrygda til ".expr() + sivilstandBestemtLitenBokstav + " din er endra.",
                Language.English to
                    "Your ".expr() + sivilstandBestemtLitenBokstav + "'s pension or disability benefit has been changed.",
            )
        }
    }
}
