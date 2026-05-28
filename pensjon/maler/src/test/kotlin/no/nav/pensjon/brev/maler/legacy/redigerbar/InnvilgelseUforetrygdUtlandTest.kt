package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdUtlandDto
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class InnvilgelseUforetrygdUtlandTest {

    @Test
    fun testHtml() {
        LetterTestImpl(
            InnvilgelseUforetrygdUtland.template,
            Fixtures.create<InnvilgelseUfoeretrygdUtlandDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestHtml("UT_INNVILGELSE_UFOERETRYGD_UTLAND")
    }
}
