package no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto

import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.Test

class FinnSamhandlerRequestDtoTest {

    @Test
    fun `Forventer at kun 1 oppslagstype er utfylt`() {
        assertThrows<IllegalArgumentException> {
            FinnSamhandlerRequestDto(
                samhandlerType = SamhandlerTypeCode.AA,
                direkteOppslag = DirekteOppslag("identtype", "id"),
                organisasjonsnavn = Organisasjonsnavn("innUtland", "navn"),
                personnavn = Personnavn("fornavn", "etternavn")
            )
        }
        assertThrows<IllegalArgumentException> {
            FinnSamhandlerRequestDto(
                samhandlerType = SamhandlerTypeCode.AA,
                direkteOppslag = null,
                organisasjonsnavn = null,
                personnavn = null
            )
        }
        assertDoesNotThrow {
            FinnSamhandlerRequestDto(
                samhandlerType = SamhandlerTypeCode.AA,
                direkteOppslag = DirekteOppslag("identtype", "id"),
                organisasjonsnavn = null,
                personnavn = null
            )
        }
    }
}