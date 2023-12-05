package no.nav.pensjon.etterlatte.maler

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.etterlatte.maler.ElementSelectors.children
import no.nav.pensjon.etterlatte.maler.ElementSelectors.type
import no.nav.pensjon.etterlatte.maler.InnerElementSelectors.text

fun <D : BrevDTO> OutlineOnlyScope<LangBokmalNynorskEnglish, D>.konverterElementerTilBrevbakerformat(
    innhold: Expression<List<Element>>,
) {
    forEach(innhold) { element ->
        showIf(element.type.equalTo(ElementType.HEADING_TWO)) {
            forEach(element.children) { inner ->
                title1 {
                    ifNotNull(inner.text) {
                        textExpr(
                            Bokmal to it,
                            Nynorsk to it,
                            English to it,
                        )
                    }
                }
            }
        }.orShowIf(element.type.equalTo(ElementType.HEADING_THREE)) {
            forEach(element.children) { inner ->
                title2 {
                    ifNotNull(inner.text) {
                        textExpr(
                            Bokmal to it,
                            Nynorsk to it,
                            English to it,
                        )
                    }
                }
            }
        }.orShowIf(element.type.equalTo(ElementType.PARAGRAPH)) {
            paragraph {
                forEach(element.children) { inner ->
                    ifNotNull(inner.text) {
                        textExpr(
                            Bokmal to it,
                            Nynorsk to it,
                            English to it,
                        )
                    }
                }
            }
        }.orShowIf(element.type.equalTo(ElementType.BULLETED_LIST)) {
            paragraph {
                list {
                    forEach(element.children) { listItem ->
                        item {
                            ifNotNull(listItem.text) { text ->
                                textExpr(
                                    Bokmal to text,
                                    Nynorsk to text,
                                    English to text,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
