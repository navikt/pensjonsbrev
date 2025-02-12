package no.nav.pensjon.brev.maler.vedlegg

import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.createVedleggTestTemplate
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.languages
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.INTEGRATION_TEST)
class PraktiskInformasjonEtteroppgjoerTest {

    @Test
    fun testVedlegg() {
        val template = createVedleggTestTemplate(
            vedleggPraktiskInformasjonEtteroppgjoerUfoeretrygd,
            Unit.expr(),
            languages(Bokmal)
        )
        Letter(
            template,
            Unit,
            Bokmal,
            Fixtures.fellesAuto
        ).renderTestPDF("PraktiskInformasjonEtteroppgjoer")
    }
}