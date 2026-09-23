package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt.Aktivitetsgrad
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt.NasjonalEllerUtland
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt.OmstillingsstoenadAktivitetspliktInformasjon10mndInnholdDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt.OmstillingsstoenadAktivitetspliktInformasjon10mndInnholdData

fun createOmstillingsstoenadAktivitetspliktInformasjon10mndInnholdDTO(
    nasjonalEllerUtland: NasjonalEllerUtland = NasjonalEllerUtland.NASJONAL,
) =
    OmstillingsstoenadAktivitetspliktInformasjon10mndInnholdDTO(
        data = OmstillingsstoenadAktivitetspliktInformasjon10mndInnholdData(
            aktivitetsgrad = Aktivitetsgrad.OVER_50_PROSENT,
            utbetaling = true,
            redusertEtterInntekt = true,
            nasjonalEllerUtland = nasjonalEllerUtland,
            halvtGrunnbeloep = Kroner(130160 / 2),
        )
    )