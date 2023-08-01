package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.api.model.vedlegg.PraktiskInformasjonEtteroppgjoerDto
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.createVedleggTestTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.INTEGRATION_TEST)
class PraktiskInformasjonEtteroppgjoerTest {

    @Test
    fun testVedlegg() {
        val template = createVedleggTestTemplate(
            vedleggPraktiskInformasjonEtteroppgjoer,
            Fixtures.create(PraktiskInformasjonEtteroppgjoerDto::class).expr()
        )
        Letter(
            template,
            Unit,
            Bokmal,
            Fixtures.fellesAuto
        ).renderTestPDF("PraktiskInformasjonEtteroppgjoer")
    }
}