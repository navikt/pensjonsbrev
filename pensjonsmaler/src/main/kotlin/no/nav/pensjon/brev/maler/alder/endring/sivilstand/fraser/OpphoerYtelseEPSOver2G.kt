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

data class OpphoerYtelseEPSOver2G(
    val sivilstand: Expression<MetaforceSivilstand>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    val sivilstandBestemtStorBokstav = sivilstand.bestemtForm(storBokstav = true)
    val sivilstandBestemtLitenBokstav = sivilstand.bestemtForm(storBokstav = false)

    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        // opphorYtelseEPSOver2G
        paragraph {
            textExpr(
                Language.Bokmal to
                    sivilstandBestemtStorBokstav +
                    " din mottar ikke lenger egen pensjon eller uføretrygd, men har fortsatt en inntekt større enn to ganger grunnbeløpet.",
                Language.Nynorsk to
                    sivilstandBestemtStorBokstav +
                    " din får ikkje lenger eigen pensjon eller eiga uføretrygd, men har framleis ei inntekt som er større enn to gonger grunnbeløpet.",
                Language.English to
                    "Your ".expr() + sivilstandBestemtLitenBokstav +
                    " no longer receives a pension or disability benefit, but still has an annual income that exceeds twice the national insurance basic amount.",
            )
        }
    }
}
