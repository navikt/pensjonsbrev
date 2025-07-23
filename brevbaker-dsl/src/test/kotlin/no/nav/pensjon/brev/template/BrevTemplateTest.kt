package no.nav.pensjon.brev.template

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import no.nav.brev.brevbaker.FellesFactory
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.template.BrevTemplateTest.EksempelBrev.fritekstIfNull
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import org.junit.jupiter.api.Test

private enum class RedigerbarBrevkode : Brevkode.Redigerbart {
    TESTBREV;

    override fun kode() = name
}

private class BrevTemplateTest {
    private val testExpressionScope = ExpressionScope(EmptyBrevdata, FellesFactory.felles, Language.Bokmal)

    private object EksempelBrev : RedigerbarTemplate<EmptyRedigerbarBrevdata> {
        override val kategori = TemplateDescription.Brevkategori.INNHENTE_OPPLYSNINGER
        override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
        override val sakstyper = Sakstype.all
        override val kode = RedigerbarBrevkode.TESTBREV
        override val template: LetterTemplate<*, EmptyRedigerbarBrevdata> =
            createTemplate(
                name = "test",
                letterDataType = EmptyRedigerbarBrevdata::class,
                languages = languages(Language.Bokmal),
                letterMetadata = LetterMetadata(
                    displayTitle = "testBrev",
                    isSensitiv = false,
                    distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
                    brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
                )
            ) {
                title { text(Language.Bokmal to "Test tittel") }

            }

    }

    @Test
    fun `fritekstOrNull gir fritekst om null`() {
        with(EksempelBrev.template) {
            val text = "fritekst"
            assertThat(null.expr<String?>().fritekstIfNull(text)
                .eval(testExpressionScope), equalTo(text))
        }
    }

    @Test
    fun `gir ikke fritekst om verdi er satt`() {
        with(EksempelBrev.template) {
            val text = "ikkeFriTekst"
            assertThat(text.expr<String?>().fritekstIfNull("bla")
                .eval(testExpressionScope), equalTo(text))
        }
    }

}