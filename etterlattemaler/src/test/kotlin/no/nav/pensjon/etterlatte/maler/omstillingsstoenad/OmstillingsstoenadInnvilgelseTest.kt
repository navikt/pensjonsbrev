package no.nav.pensjon.etterlatte.maler.omstillingsstoenad

import io.mockk.mockk
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelseDTO
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class OmstillingsstoenadInnvilgelseTest {

    @Test
    fun `lavEllerIngenInntekt brukes som default hvis omsRettUtenTidsbegrensning ikke er satt`() {
        val dto = OmstillingsstoenadInnvilgelseDTO(
            innhold = mockk(),
            beregning = mockk(),
            innvilgetMindreEnnFireMndEtterDoedsfall = false,
            lavEllerIngenInntekt = true,
            harUtbetaling = false,
            etterbetaling = mockk(),
            tidligereFamiliepleier = false,
            bosattUtland = false,
            erSluttbehandling = false,
            datoVedtakOmgjoering = null,
        )

        assertEquals(true, dto.omsRettUtenTidsbegrensning)
    }

    @Test
    fun `omsRettUtenTidsbegrensning overstyrer lavEllerIngenInntekt`() {
        val dto = OmstillingsstoenadInnvilgelseDTO(
            innhold = mockk(),
            beregning = mockk(),
            innvilgetMindreEnnFireMndEtterDoedsfall = false,
            lavEllerIngenInntekt = true,
            harUtbetaling = false,
            etterbetaling = mockk(),
            omsRettUtenTidsbegrensning = false,
            tidligereFamiliepleier = false,
            bosattUtland = false,
            erSluttbehandling = false,
            datoVedtakOmgjoering = null
        )

        assertEquals(false, dto.omsRettUtenTidsbegrensning)
    }
    
}