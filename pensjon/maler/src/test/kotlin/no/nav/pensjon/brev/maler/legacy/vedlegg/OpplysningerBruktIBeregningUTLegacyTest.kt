package no.nav.pensjon.brev.maler.legacy.vedlegg

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.createVedleggTestTemplate
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata
import no.nav.pensjon.brev.fixtures.createPEgruppe10
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.languages
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class OpplysningerBruktIBeregningUTLegacyTest {

    @Test
    fun testVedlegg() {
        val template = createVedleggTestTemplate(
            vedleggOpplysningerBruktIBeregningUTLegacy,
            createPEgruppe10().expr(),
            languages(Language.Bokmal, Language.Nynorsk, Language.English),
        )
        LetterTestImpl(
            template,
            EmptyAutobrevdata,
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestPDF("OpplysningerBruktIBeregningUfoereLegacy")
    }

    @Test
    fun testVedleggMedInntektsgrenseOgAvkortning() {
        val basis = createPEgruppe10()
        val vedtaksdata = basis.vedtaksbrev.vedtaksdata!!
        val vilkarsvedtaklist = vedtaksdata.vilkarsvedtaklist!!
        val medInntektsdetaljer = basis.copy(
            pebrevkode = "PE_UT_04_101",
            vedtaksbrev = basis.vedtaksbrev.copy(
                vedtaksdata = vedtaksdata.copy(
                    kravhode = vedtaksdata.kravhode!!.copy(
                        kravarsaktype = "foerstegangsbehandling"
                    ),
                    vilkarsvedtaklist = vilkarsvedtaklist.copy(
                        vilkarsvedtak = vilkarsvedtaklist.vilkarsvedtak.map {
                            it.copy(
                                beregningsvilkar = it.beregningsvilkar!!.copy(
                                    ifubegrunnelse = "stdbegr_12_8_2_1"
                                )
                            )
                        }
                    )
                )
            )
        )
        val template = createVedleggTestTemplate(
            vedleggOpplysningerBruktIBeregningUTLegacy,
            medInntektsdetaljer.expr(),
            languages(Language.Bokmal, Language.Nynorsk),
        )
        LetterTestImpl(
            template,
            EmptyAutobrevdata,
            Language.Nynorsk,
            Fixtures.fellesAuto
        ).renderTestPDF("OpplysningerBruktIBeregningUfoereLegacyInntektsgrense2")
    }
}