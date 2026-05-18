package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.varsel.OmstillingsstoenadVarselAktivitetspliktDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.varsel.OmstillingsstoenadVarselAktivitetspliktData


fun createOmstillingsstoenadVarselAktivitetspliktDTO() = OmstillingsstoenadVarselAktivitetspliktDTO(
    innhold = emptyList(),
    data = OmstillingsstoenadVarselAktivitetspliktData(bosattUtland = true),
)