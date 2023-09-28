package no.nav.pensjon.etterlatte.maler.fraser.common

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.StringExpression
import no.nav.pensjon.brev.template.dsl.ParagraphOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.etterlatte.maler.Navn
import no.nav.pensjon.etterlatte.maler.NavnSelectors.etternavn
import no.nav.pensjon.etterlatte.maler.NavnSelectors.fornavn
import no.nav.pensjon.etterlatte.maler.NavnSelectors.mellomnavn

fun ParagraphOnlyScope<LangBokmalNynorskEnglish, Unit>.formaterNavn(navn: Expression<Navn>) {
    allLanguages(navn.fornavn + " ")
    ifNotNull(navn.mellomnavn) { mellomnavn -> allLanguages(mellomnavn + " ") }
    allLanguages(navn.etternavn)
}

private fun ParagraphOnlyScope<LangBokmalNynorskEnglish, Unit>.allLanguages(tekst: StringExpression) {
    textExpr(Bokmal to tekst, Nynorsk to tekst, English to tekst)
}