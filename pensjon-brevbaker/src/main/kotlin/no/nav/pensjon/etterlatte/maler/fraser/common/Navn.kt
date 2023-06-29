package no.nav.pensjon.etterlatte.maler.fraser.common

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.dsl.ParagraphOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.etterlatte.maler.Navn
import no.nav.pensjon.etterlatte.maler.NavnSelectors.etternavn
import no.nav.pensjon.etterlatte.maler.NavnSelectors.fornavn
import no.nav.pensjon.etterlatte.maler.NavnSelectors.mellomnavn

fun <L : Language> ParagraphOnlyScope<LanguageSupport.Single<L>, Unit>.formaterNavn(
    spraak: L,
    navn: Expression<Navn>,
) {
    textExpr(spraak to navn.fornavn + " ")
    ifNotNull(navn.mellomnavn) { mellomnavn -> textExpr(spraak to mellomnavn + " ") }
    textExpr(spraak to navn.etternavn)
}
