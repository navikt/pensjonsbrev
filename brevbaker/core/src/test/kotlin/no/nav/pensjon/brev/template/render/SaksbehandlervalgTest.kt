package no.nav.pensjon.brev.template.render

import no.nav.brev.brevbaker.FellesFactory
import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.SaksbehandlervalgEksempelBrev
import no.nav.brev.brevbaker.SaksbehandlervalgTestDto
import no.nav.brev.brevbaker.TestValgEnum
import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Test

class SaksbehandlervalgTest {
    @Test
    fun `kan rendre saksbehandlervalg - med null-verdier der mulig`() {
        LetterTestImpl(
            SaksbehandlervalgEksempelBrev.template,
            lagSaksbehandlervalgene(true),
            Language.Bokmal,
            FellesFactory.felles
        ).renderTestHtml(SaksbehandlervalgEksempelBrev.kode.kode() + "-null")
    }
    @Test
    fun `kan rendre saksbehandlervalg - med verdier`() {
        LetterTestImpl(
            SaksbehandlervalgEksempelBrev.template,
            lagSaksbehandlervalgene(false),
            Language.Bokmal,
            FellesFactory.felles
        ).renderTestHtml(SaksbehandlervalgEksempelBrev.kode.kode() + "-verdier")
    }

    private fun lagSaksbehandlervalgene(nullable: Boolean): SaksbehandlervalgTestDto = SaksbehandlervalgTestDto(
        saksbehandlerValg = lagSaksbehandlervalg(
            "bool" to if (nullable) null else true,
            "boolMedDefault" to if (nullable) null else false,
            "intUtenDefault" to if (nullable) null else 1,
            "intMedDefault" to if (nullable) null else -1,
            "tekstUtenDefault" to if (nullable) null else "hei",
            "tekstMedDefault" to if (nullable) null else "hallo",
            "enumUtenDefault" to if (nullable) null else TestValgEnum.ALTERNATIV_EN,
            "enumMedDefault" to if (nullable) null else TestValgEnum.ALTERNATIV_TO,
        )
    )
}