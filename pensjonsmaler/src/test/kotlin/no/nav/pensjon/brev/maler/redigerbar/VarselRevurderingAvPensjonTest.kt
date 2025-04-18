package no.nav.pensjon.brev.maler.redigerbar

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.redigerbar.VarselRevurderingAvPensjonDto
import no.nav.pensjon.brev.api.toLanguage
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brevbaker.api.model.LanguageCode.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class VarselRevurderingAvPensjonTest {

    private val data = VarselRevurderingAvPensjonDto(
        saksbehandlerValg = VarselRevurderingAvPensjonDto.SaksbehandlerValg(
            tittelValg = VarselRevurderingAvPensjonDto.SaksbehandlerValg.TittelValg.RevurderingAvRett
        ),
        pesysData = VarselRevurderingAvPensjonDto.PesysData(sakstype = Sakstype.FAM_PL)
        )

    @Test
    fun `med revurdering av rett`() {
        writeAllLanguages(
            "revurdering av rett",
            data.copy(saksbehandlerValg = data.saksbehandlerValg.copy(tittelValg = VarselRevurderingAvPensjonDto.SaksbehandlerValg.TittelValg.RevurderingAvRett))
        )
    }

    @Test
    fun `med revurdering av reduksjon`() {
        writeAllLanguages(
            "revurdering reduksjon",
            data.copy(saksbehandlerValg = data.saksbehandlerValg.copy(tittelValg = VarselRevurderingAvPensjonDto.SaksbehandlerValg.TittelValg.RevurderingReduksjon))
        )
    }

    private fun writeAllLanguages(testNavn: String, data: VarselRevurderingAvPensjonDto) {
        listOf(BOKMAL, NYNORSK, ENGLISH).forEach { lang ->
            LetterTestImpl(
                VarselRevurderingAvPensjon.template,
                data,
                lang.toLanguage(),
                Fixtures.felles
            ).renderTestPDF("000130-$testNavn-${lang.name}")
        }
    }

    @Test
    fun testHtml() {
        LetterTestImpl(
            VarselRevurderingAvPensjon.template,
            Fixtures.create(),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestHtml(VarselRevurderingAvPensjon.kode.name)
    }

}