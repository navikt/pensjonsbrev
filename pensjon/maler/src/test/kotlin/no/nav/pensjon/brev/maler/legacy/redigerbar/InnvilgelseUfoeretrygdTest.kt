package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.AvslagUfoeretrygdDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdDto
import no.nav.pensjon.brev.fixtures.redigerbar.createInnvilgelseUfoeretrygdDto
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class InnvilgelseUfoeretrygdTest {

    @Test
    fun testPdf() {
        LetterTestImpl(
            InnvilgelseUforetrygd.template,
            Fixtures.create<InnvilgelseUfoeretrygdDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestPDF("UT_INNVILGELSE_UFOERTRYGD")
    }

    @Test
    fun testHtml() {
        LetterTestImpl(
            InnvilgelseUforetrygd.template,
            Fixtures.create<InnvilgelseUfoeretrygdDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestHtml("UT_INNVILGELSE_UFOERTRYGD")
    }

    @Test
    fun `testHtml - EØS minsteytelse med prorata`() {
        val dto = createInnvilgelseUfoeretrygdDto().let { dto ->
            val pe = dto.pesysData.pe
            val vedtaksdata = pe.vedtaksbrev.vedtaksdata!!
            val beregningsdata = vedtaksdata.beregningsdata!!
            val beregningufore = beregningsdata.beregningufore!!
            val uforetrygdberegning = beregningufore.uforetrygdberegning!!
            val grunnlag = pe.vedtaksbrev.grunnlag

            dto.copy(
                pesysData = dto.pesysData.copy(
                    pe = pe.copy(
                        pebrevkode = "PE_UT_04_102",
                        vedtaksbrev = pe.vedtaksbrev.copy(
                            vedtaksdata = vedtaksdata.copy(
                                beregningsdata = beregningsdata.copy(
                                    beregningufore = beregningufore.copy(
                                        uforetrygdberegning = uforetrygdberegning.copy(
                                            mottarminsteytelse = true,
                                            beregningsmetode = "eos",
                                        )
                                    )
                                ),
                            ),
                            grunnlag = grunnlag.copy(
                                persongrunnlagsliste = grunnlag.persongrunnlagsliste?.map { pg ->
                                    pg.copy(personbostedsland = "swe")
                                }
                            )
                        )
                    )
                )
            )
        }

        LetterTestImpl(
            InnvilgelseUforetrygd.template,
            dto,
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestHtml("UT_INNVILGELSE_UFOERTRYGD_EOS_MINSTEYTELSE")
    }
}