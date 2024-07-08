package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon


import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.samhandler.dto.Identtype
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.samhandler.dto.InnlandUtland
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.samhandler.dto.SamhandlerTypeCode
import org.junit.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class FinnSamhandlerRequestDtoTest {
    @Test
    fun `Forventer at kun 1 oppslagstype er utfylt`() {
        assertThrows<IllegalArgumentException> {
            FinnSamhandlerRequestDto(
                samhandlerType = SamhandlerTypeCode.AA,
                direkteOppslag = DirekteOppslag(Identtype.FNR, "id"),
                organisasjonsnavn = Organisasjonsnavn(InnlandUtland.INNLAND, "navn"),
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
                direkteOppslag = DirekteOppslag(Identtype.FNR, "id"),
                organisasjonsnavn = null,
                personnavn = null
            )
        }
    }
}