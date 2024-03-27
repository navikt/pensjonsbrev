package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.informasjon.Aktivitetsgrad
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.informasjon.OmstillingsstoenadAktivitetspliktVarselbrevInnholdDTO


fun createOmstillingsstoenadAktivitetspliktVarselbrevInnholdDTO() =
    OmstillingsstoenadAktivitetspliktVarselbrevInnholdDTO(
        aktivitetsgrad = Aktivitetsgrad.IKKE_I_AKTIVITET
    )