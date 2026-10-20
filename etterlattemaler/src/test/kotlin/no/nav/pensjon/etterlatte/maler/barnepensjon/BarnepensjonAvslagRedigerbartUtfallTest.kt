package no.nav.pensjon.etterlatte.maler.barnepensjon

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.Fixtures
import no.nav.pensjon.etterlatte.fixtures.createBarnepensjonAvslagRedigerbartUtfallDTO
import no.nav.pensjon.etterlatte.fixtures.createBarnepensjonAvslagRedigerbartUtfallDTOUtenDoedsdato
import no.nav.pensjon.etterlatte.maler.barnepensjon.avslag.BarnepensjonAvslagRedigerbartUtfall
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.INTEGRATION_TEST)
internal class BarnepensjonAvslagRedigerbartUtfallTest {

    @Test
    fun testMedDoedsdatoHtml() {
        val dto = createBarnepensjonAvslagRedigerbartUtfallDTO()

        LetterTestImpl(
            BarnepensjonAvslagRedigerbartUtfall.template,
            dto,
            Language.Bokmal,
            Fixtures.felles,
        ).renderTestHtml("${EtterlatteBrevKode.BARNEPENSJON_AVSLAG_UTFALL}")
    }

    @Test
    fun testMedDoedsdatoPdf() {
        val dto = createBarnepensjonAvslagRedigerbartUtfallDTO()

        LetterTestImpl(
            BarnepensjonAvslagRedigerbartUtfall.template,
            dto,
            Language.Bokmal,
            Fixtures.felles,
        ).renderTestPDF("${EtterlatteBrevKode.BARNEPENSJON_AVSLAG_UTFALL}")
    }

    @Test
    fun testOrdinaerAvslagMedDoedsdatoHtml() {
        val dto = createBarnepensjonAvslagRedigerbartUtfallDTO(erSluttbehandling = false)

        LetterTestImpl(
            BarnepensjonAvslagRedigerbartUtfall.template,
            dto,
            Language.Bokmal,
            Fixtures.felles,
        ).renderTestHtml("${EtterlatteBrevKode.BARNEPENSJON_AVSLAG}_ordinaer_med_doedsdato")
    }

    @Test
    fun testOrdinaerAvslagUtenDoedsdatoHtml() {
        val dto = createBarnepensjonAvslagRedigerbartUtfallDTOUtenDoedsdato(erSluttbehandling = false)

        LetterTestImpl(
            BarnepensjonAvslagRedigerbartUtfall.template,
            dto,
            Language.Bokmal,
            Fixtures.felles,
        ).renderTestHtml("${EtterlatteBrevKode.BARNEPENSJON_AVSLAG}_ordinaer_uten_doedsdato")
    }

    @Test
    fun testOrdinaerAvslagUtenDoedsdatoPdf() {
        val dto = createBarnepensjonAvslagRedigerbartUtfallDTOUtenDoedsdato(erSluttbehandling = false)

        LetterTestImpl(
            BarnepensjonAvslagRedigerbartUtfall.template,
            dto,
            Language.Bokmal,
            Fixtures.felles,
        ).renderTestPDF("${EtterlatteBrevKode.BARNEPENSJON_AVSLAG}_ordinaer_uten_doedsdato")
    }

    @Test
    fun testSluttbehandlingUtenDoedsdatoHtml() {
        val dto = createBarnepensjonAvslagRedigerbartUtfallDTOUtenDoedsdato(erSluttbehandling = true)

        LetterTestImpl(
            BarnepensjonAvslagRedigerbartUtfall.template,
            dto,
            Language.Bokmal,
            Fixtures.felles,
        ).renderTestHtml("${EtterlatteBrevKode.BARNEPENSJON_AVSLAG}_sluttbehandling_uten_doedsdato")
    }

    @Test
    fun testSluttbehandlingUtenDoedsdatoPdf() {
        val dto = createBarnepensjonAvslagRedigerbartUtfallDTOUtenDoedsdato(erSluttbehandling = true)

        LetterTestImpl(
            BarnepensjonAvslagRedigerbartUtfall.template,
            dto,
            Language.Bokmal,
            Fixtures.felles,
        ).renderTestPDF("${EtterlatteBrevKode.BARNEPENSJON_AVSLAG}_sluttbehandling_uten_doedsdato")
    }

}
