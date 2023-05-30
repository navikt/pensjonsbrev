package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.createVedleggTestTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.PDF_BYGGER)
class OrienteringOmRettigheterUfoereTest {

    @Test
    fun testVedlegg() {
        val template = createVedleggTestTemplate(
            vedleggDineRettigheterOgPlikterUfoere,
            Fixtures.create(OrienteringOmRettigheterUfoereDto::class).expr()
        )
        Letter(
            template,
            Unit,
            Nynorsk,
            Fixtures.fellesAuto
        ).renderTestPDF("OrienteringOmRettigheterUfoere")

    }
}



