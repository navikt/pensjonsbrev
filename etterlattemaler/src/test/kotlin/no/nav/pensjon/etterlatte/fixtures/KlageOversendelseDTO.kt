package no.nav.pensjon.etterlatte.fixtures

import no.nav.brev.brevbaker.vilkaarligDato
import no.nav.pensjon.etterlatte.maler.fraser.common.SakType
import no.nav.pensjon.etterlatte.maler.klage.KlageOversendelseBrukerDTO

fun createKlageOversendelseBrukerDTO() = KlageOversendelseBrukerDTO(
    sakType = SakType.OMSTILLINGSSTOENAD,
    klageDato = vilkaarligDato.minusWeeks(3),
    vedtakDato = vilkaarligDato.minusMonths(2),
    innstillingTekst = """
        En innstillingstekst
        
        Med flere linjer
        
        og noe annet
        """.trimIndent(),
    under18Aar = false,
    harVerge = false,
    bosattIUtlandet = false
)