package no.nav.pensjon.brev.alder.maler.sivilstand.fraser

import no.nav.pensjon.brev.alder.maler.felles.bestemtForm
import no.nav.pensjon.brev.alder.model.MetaforceSivilstand
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

data class OpphoerYtelseEPS(
    val sivilstand: Expression<MetaforceSivilstand>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    val sivilstandBestemtStorBokstav = sivilstand.bestemtForm(storBokstav = true)
    val sivilstandBestemtLitenBokstav = sivilstand.bestemtForm(storBokstav = false)

    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        // opphorYtelseEPS
        paragraph {
            text(
                bokmal { + sivilstandBestemtStorBokstav + " din mottar ikke lenger egen pensjon eller uføretrygd." },
                nynorsk { + sivilstandBestemtStorBokstav + " din får ikkje lenger eigen pensjon eller eiga uføretrygd." },
                english { + 
                    "Your " + sivilstandBestemtLitenBokstav + " no longer receives a pension or disability benefit." },
            )
        }
    }
}
