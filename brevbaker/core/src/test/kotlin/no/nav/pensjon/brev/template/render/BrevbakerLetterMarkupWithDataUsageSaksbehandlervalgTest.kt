package no.nav.pensjon.brev.template.render

import no.nav.brev.brevbaker.BrevbakerLetterMarkup
import no.nav.brev.brevbaker.FellesFactory
import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.SaksbehandlervalgEksempelBrev
import no.nav.brev.brevbaker.SaksbehandlervalgTestDto
import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.template.Language
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class BrevbakerLetterMarkupWithDataUsageSaksbehandlervalgTest {

    private val letter = LetterTestImpl(
        template = SaksbehandlervalgEksempelBrev.template,
        argument = SaksbehandlervalgTestDto(
            saksbehandlerValg = lagSaksbehandlervalg(
                "bool" to true,
                "intUtenDefault" to 42,
                "tekstUtenDefault" to "hei",
                "enumUtenDefault" to "ALTERNATIV_EN",
            )
        ),
        language = Language.Bokmal,
        felles = FellesFactory.felles,
    )

    @Test
    fun `letterDataUsage inneholder saksbehandlerValg-feltet fra DTO-en og de faktiske saksbehandlervalgene`() {
        val result = BrevbakerLetterMarkup.renderLetterMarkupWithDataUsage(letter)

        val saksbehandlervalgType = SaksbehandlervalgIDSL::class.qualifiedName!!

        assertThat(result.letterDataUsage.map { it.typeName to it.propertyName })
            .containsExactlyInAnyOrder(
                SaksbehandlervalgTestDto::class.qualifiedName!! to "saksbehandlerValg",
                saksbehandlervalgType to "bool",
                saksbehandlervalgType to "intUtenDefault",
                saksbehandlervalgType to "tekstUtenDefault",
                saksbehandlervalgType to "enumUtenDefault",
            )
    }
}
