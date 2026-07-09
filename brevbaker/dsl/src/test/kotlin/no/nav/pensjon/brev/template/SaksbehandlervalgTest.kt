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
            "bool", "boolMedDefault",
            "intUtenDefault", "intMedDefault",
            "tekstUtenDefault", "tekstMedDefault",
            "enumUtenDefault", "enumMedDefault",
        )
    }

    @Test
    fun `genererer opp malen`() {

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
    fun `bool med default gir default naar ikke satt`() {
        with(TemplateRootScope<LangBokmal, SaksbehandlervalgTestDto>()) {
            val boolMedDefault = saksbehandlervalg("boolMedDefault", "Boolsk valg med default").bool(default = true)
            assertThat(boolMedDefault.eval(expressionScope(lagSaksbehandlervalg()))).isTrue()
        }
    }

    @Test
    fun `bool med default gir satt verdi naar satt`() {
        with(TemplateRootScope<LangBokmal, SaksbehandlervalgTestDto>()) {
            val boolMedDefault = saksbehandlervalg("boolMedDefault", "Boolsk valg med default").bool(default = true)
            assertThat(boolMedDefault.eval(expressionScope(lagSaksbehandlervalg("boolMedDefault" to false)))).isFalse()
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
    fun `int med default gir default naar ikke satt`() {
        with(TemplateRootScope<LangBokmal, SaksbehandlervalgTestDto>()) {
            val intMedDefault = saksbehandlervalg("intMedDefault", "Tall med default").int(default = 42)
            assertThat(intMedDefault.eval(expressionScope(lagSaksbehandlervalg()))).isEqualTo(42)
        }
    }

    @Test
    fun `int med default gir satt verdi naar satt`() {
        with(TemplateRootScope<LangBokmal, SaksbehandlervalgTestDto>()) {
            val intMedDefault = saksbehandlervalg("intMedDefault", "Tall med default").int(default = 42)
            assertThat(intMedDefault.eval(expressionScope(lagSaksbehandlervalg("intMedDefault" to 7)))).isEqualTo(7)
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
    fun `tekst med default gir default naar ikke satt`() {
        with(TemplateRootScope<LangBokmal, SaksbehandlervalgTestDto>()) {
            val tekstMedDefault = saksbehandlervalg("tekstMedDefault", "Tekst med default").text(default = "standardtekst")
            assertThat(tekstMedDefault.eval(expressionScope(lagSaksbehandlervalg()))).isEqualTo("standardtekst")
        }
    }

    @Test
    fun `tekst med default gir satt verdi naar satt`() {
        with(TemplateRootScope<LangBokmal, SaksbehandlervalgTestDto>()) {
            val tekstMedDefault = saksbehandlervalg("tekstMedDefault", "Tekst med default").text(default = "standardtekst")
            assertThat(tekstMedDefault.eval(expressionScope(lagSaksbehandlervalg("tekstMedDefault" to "annet")))).isEqualTo("annet")
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

    @Test
    fun `enum med default gir default naar ikke satt`() {
        with(TemplateRootScope<LangBokmal, SaksbehandlervalgTestDto>()) {
            val enumMedDefault = saksbehandlervalg("enumMedDefault", "Enum med default").enum(default = TestValgEnum.ALTERNATIV_EN)
            assertThat(enumMedDefault.eval(expressionScope(lagSaksbehandlervalg()))).isEqualTo(TestValgEnum.ALTERNATIV_EN)
        }
    }

    @Test
    fun `enum med default gir satt verdi naar satt`() {
        with(TemplateRootScope<LangBokmal, SaksbehandlervalgTestDto>()) {
            val enumMedDefault = saksbehandlervalg("enumMedDefault", "Enum med default").enum(default = TestValgEnum.ALTERNATIV_EN)
            assertThat(
                enumMedDefault.eval(expressionScope(lagSaksbehandlervalg("enumMedDefault" to TestValgEnum.ALTERNATIV_TO.name)))
            ).isEqualTo(TestValgEnum.ALTERNATIV_TO)
        }
    }
}
