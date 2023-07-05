package no.nav.pensjon.etterlatte.maler

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.etterlatte.maler.ManueltBrevDTOSelectors.ElementSelectors.children
import no.nav.pensjon.etterlatte.maler.ManueltBrevDTOSelectors.ElementSelectors.type
import no.nav.pensjon.etterlatte.maler.ManueltBrevDTOSelectors.InnerElementSelectors.children
import no.nav.pensjon.etterlatte.maler.ManueltBrevDTOSelectors.InnerElementSelectors.text
import no.nav.pensjon.etterlatte.maler.ManueltBrevDTOSelectors.innhold

fun <T : Language> OutlineOnlyScope<LanguageSupport.Single<T>, ManueltBrevDTO>.konverterElementerTilBrevbakerformat(
    spraak: T,
) {
    forEach(innhold) { element ->
        showIf(element.type.equalTo(ManueltBrevDTO.ElementType.HEADING_TWO)) {
            forEach(element.children) { inner ->
                title1 {
                    ifNotNull(inner.text) {
                        textExpr(spraak to it)
                    }
                }
            }
        }.orShowIf(element.type.equalTo(ManueltBrevDTO.ElementType.HEADING_THREE)) {
            forEach(element.children) { inner ->
                title2 {
                    ifNotNull(inner.text) {
                        textExpr(spraak to it)
                    }
                }
            }
        }.orShowIf(element.type.equalTo(ManueltBrevDTO.ElementType.PARAGRAPH)) {
            paragraph {
                forEach(element.children) { inner ->
                    ifNotNull(inner.text) {
                        textExpr(spraak to it)
                    }
                }
            }
        }.orShowIf(element.type.equalTo(ManueltBrevDTO.ElementType.BULLETED_LIST)) {
            paragraph {
                list {
                    forEach(element.children) { inner ->
                        item {
                            ifNotNull(inner.children) {
                                forEach(it) { inner2 ->
                                    ifNotNull(inner2.text) { text ->
                                        textExpr(spraak to text)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
