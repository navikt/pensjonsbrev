package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.template.BrevTemplateTest.EksempelBrev.fritekst
import no.nav.pensjon.brev.template.dsl.TemplateRootScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

private enum class RedigerbarBrevkode : Brevkode.Redigerbart {
    TESTBREV_REDIGERBART;

    override fun kode() = name
}

private class BrevTemplateTest {
    private val testExpressionScope = ExpressionScope(EmptyAutobrevdata, FellesFactory.felles, Language.Bokmal)

    private object EksempelBrev : RedigerbarTemplate<EmptyRedigerbarBrevdata> {
        override val kategori = TemplateDescription.Brevkategori.INNHENTE_OPPLYSNINGER
        override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
        override val sakstyper = Sakstype.all
        override val kode = RedigerbarBrevkode.TESTBREV_REDIGERBART
        override val template =
            createTemplate(
                languages = languages(Language.Bokmal),
                letterMetadata = LetterMetadata(
                    displayTitle = "testBrev",
                    distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
                    brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
                )
            ) {
                title { text(bokmal { +"Test tittel" }) }

            }

    }

    @Test
    fun `kan bruke fritekst som ifNull`() {
        with(EksempelBrev.template) {
            with(TemplateRootScope<LangBokmal, EmptyRedigerbarBrevdata>()) {
                val text = "fritekst"
                assertThat(
                    null.expr<String?>().ifNull(fritekst(text))
                        .eval(testExpressionScope)).isEqualTo(text)
            }
        }
    }

    @Test
    fun `kan ikke ha fritekst uten tekst`() {
        with(EksempelBrev.template) {
            with(TemplateRootScope<LangBokmal, EmptyRedigerbarBrevdata>()) {
                    assertThrows<IllegalArgumentException> { fritekst("       ") }
            }
        }
    }


    @Test
    fun `kan ha fritekst med mellomrom foerst og sist`() {
        with(EksempelBrev.template) {
            with(TemplateRootScope<LangBokmal, EmptyRedigerbarBrevdata>()) {
                assertDoesNotThrow{ fritekst(" hei ") }
            }
        }
    }

    @Test
    fun `gir ikke fritekst om verdi er satt`() {
        with(EksempelBrev.template) {
            with(TemplateRootScope<LangBokmal, EmptyRedigerbarBrevdata>()) {
                val text = "ikkeFriTekst"
                assertThat(
                    text.expr<String?>().ifNull(fritekst("bla"))
                        .eval(testExpressionScope)).isEqualTo(text)
            }
        }
    }

}