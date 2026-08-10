package no.nav.pensjon.brev.template

import no.nav.brev.brevbaker.SaksbehandlervalgEksempelBrev
import no.nav.brev.brevbaker.SaksbehandlervalgTestDto
import no.nav.brev.brevbaker.TestValgEnum
import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.template.dsl.TemplateRootScope
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

private class SaksbehandlervalgTest {

    @Test
    fun `malen registrerer alle saksbehandlervalg`() {
        assertThat(SaksbehandlervalgEksempelBrev.template.saksbehandlervalg?.keys).containsExactlyInAnyOrder(
            "bool",
            "intUtenDefault",
            "tekstUtenDefault",
            "enumUtenDefault",
        )
    }

    private fun expressionScope(saksbehandlerValg: SaksbehandlervalgIDSL) =
        ExpressionScope(
            SaksbehandlervalgTestDto(saksbehandlerValg = saksbehandlerValg),
            FellesFactory.felles,
            Language.Bokmal,
        )

    @Test
    fun `bool uten satt verdi gir default false`() {
        with(TemplateRootScope<LangBokmal, SaksbehandlervalgTestDto>()) {
            val bool = saksbehandlervalg("bool", "Boolsk valg").bool()
            assertThat(bool.eval(expressionScope(lagSaksbehandlervalg()))).isFalse()
        }
    }

    @Test
    fun `bool med satt verdi gir satt verdi`() {
        with(TemplateRootScope<LangBokmal, SaksbehandlervalgTestDto>()) {
            val bool = saksbehandlervalg("bool", "Boolsk valg").bool()
            assertThat(bool.eval(expressionScope(lagSaksbehandlervalg("bool" to true)))).isTrue()
        }
    }

    @Test
    fun `int uten default er null naar ikke satt`() {
        with(TemplateRootScope<LangBokmal, SaksbehandlervalgTestDto>()) {
            val intUtenDefault = saksbehandlervalg("intUtenDefault", "Tall uten default").int()
            assertThat(intUtenDefault.eval(expressionScope(lagSaksbehandlervalg()))).isNull()
        }
    }

    @Test
    fun `int uten default gir satt verdi`() {
        with(TemplateRootScope<LangBokmal, SaksbehandlervalgTestDto>()) {
            val intUtenDefault = saksbehandlervalg("intUtenDefault", "Tall uten default").int()
            assertThat(intUtenDefault.eval(expressionScope(lagSaksbehandlervalg("intUtenDefault" to 7)))).isEqualTo(7)
        }
    }

    @Test
    fun `tekst uten default er null naar ikke satt`() {
        with(TemplateRootScope<LangBokmal, SaksbehandlervalgTestDto>()) {
            val tekstUtenDefault = saksbehandlervalg("tekstUtenDefault", "Tekst uten default").text()
            assertThat(tekstUtenDefault.eval(expressionScope(lagSaksbehandlervalg()))).isNull()
        }
    }

    @Test
    fun `tekst uten default gir satt verdi`() {
        with(TemplateRootScope<LangBokmal, SaksbehandlervalgTestDto>()) {
            val tekstUtenDefault = saksbehandlervalg("tekstUtenDefault", "Tekst uten default").text()
            assertThat(tekstUtenDefault.eval(expressionScope(lagSaksbehandlervalg("tekstUtenDefault" to "hei")))).isEqualTo("hei")
        }
    }

    @Test
    fun `enum uten default er null naar ikke satt`() {
        with(TemplateRootScope<LangBokmal, SaksbehandlervalgTestDto>()) {
            val enumUtenDefault = saksbehandlervalg("enumUtenDefault", "Enum uten default").enum<TestValgEnum>()
            assertThat(enumUtenDefault.eval(expressionScope(lagSaksbehandlervalg()))).isNull()
        }
    }

    @Test
    fun `enum uten default gir satt verdi`() {
        with(TemplateRootScope<LangBokmal, SaksbehandlervalgTestDto>()) {
            val enumUtenDefault = saksbehandlervalg("enumUtenDefault", "Enum uten default").enum<TestValgEnum>()
            assertThat(
                enumUtenDefault.eval(expressionScope(lagSaksbehandlervalg("enumUtenDefault" to TestValgEnum.ALTERNATIV_TO.name)))
            ).isEqualTo(TestValgEnum.ALTERNATIV_TO)
        }
    }
}
