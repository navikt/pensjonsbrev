package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.fraser.common.SakType
import no.nav.pensjon.etterlatte.maler.klage.KlageOversendelseBrukerDTO
import java.time.LocalDate

fun createKlageOversendelseBrukerDTO() = KlageOversendelseBrukerDTO(
    sakType = SakType.OMSTILLINGSSTOENAD,
    klageDato = LocalDate.now().minusWeeks(3),
    vedtakDato = LocalDate.now().minusMonths(2),
    innstillingTekst = """
        En innstillingstekst
        
        Med flere linjer
        
        og noe annet
        """.trimIndent(),
    under18Aar = false,
    harVerge = false,
    bosattIUtlandet = false
)