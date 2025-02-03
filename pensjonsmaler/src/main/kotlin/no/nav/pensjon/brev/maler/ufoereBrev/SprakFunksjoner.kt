package no.nav.pensjon.brev.maler.ufoereBrev

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.dsl.expression.*

fun barnetEllerBarnaBestemt(antallBarn: Expression<Int>): Expression<String> {
    val erEngelsk = Expression.FromScope.Language.equalTo(English.expr())

    return ifElse(
        antallBarn.equalTo(1),
        ifElse(erEngelsk, "child", "barnet"),
        ifElse(erEngelsk, "children", "barna"),
    )
}

fun epsTypeUbestemt(borMed: Expression<String>): Expression<String> {
    val erEngelsk = Expression.FromScope.Language.equalTo(English.expr())
    val erBokmal = Expression.FromScope.Language.equalTo(Bokmal.expr())

    val samboer = ifElse(
        erBokmal,
        "samboer".expr(),
        ifElse(erEngelsk, "cohabitant".expr(), "sambuar".expr())
    )

    val ektefelle = ifElse(
        erEngelsk,
        "spouse".expr(),
        "ektefelle".expr()
    )

    val partner = "partner".expr()

    return ifElse(
        borMed.equalTo("samboer"),
        samboer,
        ifElse(borMed.equalTo("ektefelle"), ektefelle, partner)
    )
}