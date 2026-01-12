package no.nav.pensjon.brev.alder.maler.sivilstand.fraser

import no.nav.pensjon.brev.alder.maler.felles.bestemtForm
import no.nav.pensjon.brev.alder.model.MetaforceSivilstand
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

data class EndringYtelseEPS(
    val sivilstand: Expression<MetaforceSivilstand>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    val sivilstandBestemtLitenBokstav = sivilstand.bestemtForm(storBokstav = false)

    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        // endringYtelseEPS
        paragraph {
            text(
                bokmal { + 
                    "Pensjonen eller uføretrygden til " + sivilstandBestemtLitenBokstav + " din er endret." },
                nynorsk { + "Pensjonen eller uføretrygda til " + sivilstandBestemtLitenBokstav + " din er endra." },
                english { + 
                    "Your " + sivilstandBestemtLitenBokstav + "'s pension or disability benefit has been changed." },
            )
        }
    }
}
