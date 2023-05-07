package no.nav.pensjon.brev.maler.vedlegg

import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.PDF_BUILDER_URL
import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.createVedleggTestTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.render.PensjonLatexRenderer
import no.nav.pensjon.brev.writeTestPDF
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.PDF_BYGGER)
class OpplysningerBruktIBeregningUTTest {

    @Test
    fun testVedlegg() {
        val template = createVedleggTestTemplate(
            createVedleggOpplysningerBruktIBeregningUT(
                skalViseAvdoed = true,
                skalViseBarnetillegg = true,
                skalViseDinUtbetalingFoerSkatt = true,
                skalViseEtteroppgjoerAvUfoeretrygdOgBarnetillegg = true,
                skalViseForDegSomMottarEktefelletillegg = true,
                skalViseInntektEtterUfoer = true,
                skalViseInntektFoerUfoer = true,
                skalViseKompensasjonsgrad = true,
                skalViseMinsteytelse = true,
                skalViseReduksjonAvUfoeretrygden = true,
                skalViseSlikBeregnerViGjenlevendetillegg = true,
                skalViseSlikBeregnerViGjenlevendetilleggHarNyttTillegg = true,
                skalViseSlikBeregnerViUfoeretrygdenDin = true,
                skalViseTabellInntekteneBruktIBeregningen = true,
                skalViseTrygdetidenDin = true,
                skalViseUtbetalingAvUTNaarInntektEndres = true,
                skalViseYrkesskadeEllerYrkessykdom = true
            ),
            Fixtures.create(OpplysningerBruktIBeregningUTDto::class).expr()
        )
        Letter(
            template,
            Unit,
            Language.Bokmal,
            Fixtures.fellesAuto
        )
            .let { PensjonLatexRenderer.render(it) }
            .let { runBlocking { LaTeXCompilerService(PDF_BUILDER_URL).producePDF(it, "test").base64PDF } }
            .also { writeTestPDF("OpplysningerBruktIBeregningUfoere", it) }

    }
}