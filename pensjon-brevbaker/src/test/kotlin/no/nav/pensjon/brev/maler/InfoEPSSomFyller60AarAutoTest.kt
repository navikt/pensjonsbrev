package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.api.model.maler.InfoTilDegSomHarEPSSomFyller60AarAutoDto
import no.nav.pensjon.brev.template.*
import org.junit.jupiter.api.*

@Tag(TestTags.MANUAL_TEST)
class InfoEPSSomFyller60AarAutoTest {

    @Test
    fun testPdfNB() {
        Letter(
            InfoTilDegSomHarEPSSomFyller60AarAuto.template,
            Fixtures.create<InfoTilDegSomHarEPSSomFyller60AarAutoDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestPDF("INFO_EPS_SOM_FYLLER_60_AAR_AUTO_BOKMAL")
    }

    @Test
    fun testPdfNN() {
        Letter(
            InfoTilDegSomHarEPSSomFyller60AarAuto.template,
            Fixtures.create<InfoTilDegSomHarEPSSomFyller60AarAutoDto>(),
            Language.Nynorsk,
            Fixtures.fellesAuto
        ).renderTestPDF("INFO_EPS_SOM_FYLLER_60_AAR_AUTO_NYNORSK")
    }

    @Test
    fun testPdfEN() {
        Letter(
            InfoTilDegSomHarEPSSomFyller60AarAuto.template,
            Fixtures.create<InfoTilDegSomHarEPSSomFyller60AarAutoDto>(),
            Language.English,
            Fixtures.fellesAuto
        ).renderTestPDF("INFO_EPS_SOM_FYLLER_60_AAR_AUTO_ENGLISH")
    }


    @Test
    fun testHtml() {
        Letter(
            InfoTilDegSomHarEPSSomFyller60AarAuto.template,
            Fixtures.create<InfoTilDegSomHarEPSSomFyller60AarAutoDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderTestHtml("INFO_EPS_SOM_FYLLER_60_AAR_AUTO_BOKMAL")
    }
}