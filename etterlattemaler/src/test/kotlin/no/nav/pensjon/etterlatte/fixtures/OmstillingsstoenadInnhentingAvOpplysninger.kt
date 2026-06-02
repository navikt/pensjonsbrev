package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.informasjon.OmstillingsstoenadInnhentingAvOpplysningerDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.informasjon.OmstillingsstoenadInnhentingAvOpplysningerData

fun createOmstillingsstoenadInnhentingAvOpplysningerDTO() = OmstillingsstoenadInnhentingAvOpplysningerDTO(
    data = OmstillingsstoenadInnhentingAvOpplysningerData(borIUtlandet = true)
)
