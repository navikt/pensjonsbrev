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
import no.nav.pensjon.brev.template.dsl.text

data class InnvilgetYtelseEPS(
    val sivilstand: Expression<MetaforceSivilstand>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    val sivilstandBestemtStorBokstav = sivilstand.bestemtForm(storBokstav = true)
    val sivilstandBestemtLitenBokstav = sivilstand.bestemtForm(storBokstav = false)

    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        // innvilgetYtelseEPS
        paragraph {
            text(
                bokmal { + sivilstandBestemtStorBokstav + " din har fått innvilget egen pensjon eller uføretrygd." },
                nynorsk { + sivilstandBestemtStorBokstav + " din har fått innvilga eigen pensjon eller eiga uføretrygd." },
                english { + "Your " + sivilstandBestemtLitenBokstav + " has been granted a pension or disability benefit." },
            )
        }
    }
}
