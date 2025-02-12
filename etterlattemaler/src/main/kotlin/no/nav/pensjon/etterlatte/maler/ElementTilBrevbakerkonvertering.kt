package no.nav.pensjon.etterlatte.maler

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.ListScope
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.etterlatte.maler.ElementSelectors.children
import no.nav.pensjon.etterlatte.maler.ElementSelectors.type
import no.nav.pensjon.etterlatte.maler.InnerElementSelectors.children
import no.nav.pensjon.etterlatte.maler.InnerElementSelectors.text
import no.nav.pensjon.etterlatte.maler.InnerElementSelectors.type

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
                    // TODO vi har muligens lagret ned brev med dette formatet allerede så må støtte det videre
                    showIf(inner.type.equalTo(ElementType.BULLETED_LIST)) {
                        list {
                            ifNotNull(inner.children) { i ->
                                lagPunktlisteGammeltFormat(i)
                            }
                        }
                    }.orShow {
                        ifNotNull(inner.text) {
                            textExpr(
                                Bokmal to it,
                                Nynorsk to it,
                                English to it,
                            )
                        }
                    }
                }
            }
        }.orShowIf(element.type.equalTo(ElementType.BULLETED_LIST)) {
            paragraph {
                list {
                    lagPunktliste(element.children)
                }
            }
        }
    }
}

private fun <D : BrevDTO> ListScope<LangBokmalNynorskEnglish, D>.lagPunktlisteGammeltFormat(
    items: Expression<List<InnerElement>>
) {
    forEach(items) { listItem ->
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

private fun <D : BrevDTO> ListScope<LangBokmalNynorskEnglish, D>.lagPunktliste(
    items: Expression<List<InnerElement>>
) {
    forEach(items) { listItem ->
        item {
            ifNotNull(listItem.children) { children ->
                forEach(children) { paragraph ->
                    ifNotNull(paragraph.text) { text ->
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
