package no.nav.pensjon.etterlatte.maler.fraser.common

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.ParagraphOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.etterlatte.maler.Navn
import no.nav.pensjon.etterlatte.maler.NavnSelectors.etternavn
import no.nav.pensjon.etterlatte.maler.NavnSelectors.fornavn
import no.nav.pensjon.etterlatte.maler.NavnSelectors.mellomnavn

fun ParagraphOnlyScope<LangBokmalNynorskEnglish, Unit>.formaterNavn(navn: Expression<Navn>) {
    textExpr(
        Language.Bokmal to navn.fornavn + " ",
        Language.Nynorsk to navn.fornavn + " ",
        Language.English to navn.fornavn + " ",
    )
    ifNotNull(navn.mellomnavn) { mellomnavn ->
        textExpr(
            Language.Bokmal to mellomnavn + " ",
            Language.Nynorsk to mellomnavn + " ",
            Language.English to mellomnavn + " ",
        )
    }
    textExpr(
        Language.Bokmal to navn.etternavn + " ",
        Language.Nynorsk to navn.etternavn + " ",
        Language.English to navn.etternavn + " ",
    )
}
