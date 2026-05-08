package no.nav.pensjon.etterlatte.maler.andre

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.Fixtures
import no.nav.pensjon.etterlatte.fixtures.createOmstillingsstoenadBeregningRedigerbartVedlegg
import no.nav.pensjon.etterlatte.maler.ManueltBrevMedTittelDTO
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningRedigerbartVedlegg
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.redigerbar.OmstillingsstoenadVedleggBeregningRedigerbartUtfall
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.INTEGRATION_TEST)
internal class OmstillingsstoenadVedleggBeregningRedigerbartUtfallTest {

    @Test
    fun testHtml() {
        val argument: OmstillingsstoenadBeregningRedigerbartVedlegg = createOmstillingsstoenadBeregningRedigerbartVedlegg()

        val letter = LetterTestImpl(
            OmstillingsstoenadVedleggBeregningRedigerbartUtfall.template,
            argument,
            Language.Bokmal,
            Fixtures.felles
        )
        letter.renderTestHtml(EtterlatteBrevKode.OMSTILLINGSSTOENAD_VEDLEGG_BEREGNING_UTFALL.name)
    }

    @Test
    fun testHtmlMedBeregningsdata() {
        val argument: OmstillingsstoenadBeregningRedigerbartVedlegg = createOmstillingsstoenadBeregningRedigerbartVedlegg()

        val letter = LetterTestImpl(
            OmstillingsstoenadVedleggBeregningRedigerbartUtfall.template,
            argument,
            Language.Bokmal,
            Fixtures.felles
        )
        letter.renderTestHtml(EtterlatteBrevKode.OMSTILLINGSSTOENAD_VEDLEGG_BEREGNING_UTFALL.name)
    }

}