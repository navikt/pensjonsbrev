package no.nav.pensjon.etterlatte

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.outlineTestTemplate
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.ElementType
import no.nav.pensjon.etterlatte.maler.InnerElement
import no.nav.pensjon.etterlatte.maler.ManueltBrevDTO
import no.nav.pensjon.etterlatte.maler.ManueltBrevDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import org.junit.jupiter.api.Test

class ElementTilBrevbakerkonvertererTest {

    @Test
    fun `viser list-item utenfor liste som ny punktliste`() {
        val kulepunktUtenforListe = Element(
            type = ElementType.LIST_ITEM,
            children = listOf(
                InnerElement(
                    type = ElementType.PARAGRAPH,
                    text = "dette er et kulepunkt"
                )
            )
        )
        val liste = Element(
            type = ElementType.BULLETED_LIST,
            children = listOf(
                InnerElement(
                    type = ElementType.LIST_ITEM,
                    children = listOf(
                        InnerElement(
                            type = ElementType.PARAGRAPH,
                            text = "kulepunkt 1 i liste"
                        )
                    ),
                ),
                InnerElement(
                    type = ElementType.LIST_ITEM,
                    children = listOf(
                        InnerElement(
                            type = ElementType.PARAGRAPH,
                            text = "kulepunkt 2 i liste"
                        )
                    )
                )
            )
        )


        LetterTestImpl(outlineTestTemplate<ManueltBrevDTO> { konverterElementerTilBrevbakerformat(innhold) }, ManueltBrevDTO(innhold = listOf(kulepunktUtenforListe, liste)), Language.Bokmal, Fixtures.felles).renderTestHtml("kulepunkt")
    }
}