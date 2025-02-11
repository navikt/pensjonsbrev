package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.varsel.OmstillingsstoenadVarselAktivitetspliktDTO

fun createOmstillingsstoenadVarselAktivitetspliktDTO() =
    OmstillingsstoenadVarselAktivitetspliktDTO(
        innhold = emptyList(),
        bosattUtland = true,
    )
