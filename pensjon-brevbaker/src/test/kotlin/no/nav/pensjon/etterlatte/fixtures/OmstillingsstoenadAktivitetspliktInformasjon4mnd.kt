package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt.Aktivitetsgrad
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt.OmstillingsstoenadAktivitetspliktInformasjon4mndInnholdDTO


fun createOmstillingsstoenadAktivitetspliktInformasjon4mndInnholdDTO() =
    OmstillingsstoenadAktivitetspliktInformasjon4mndInnholdDTO(
        aktivitetsgrad = Aktivitetsgrad.IKKE_I_AKTIVITET
    )