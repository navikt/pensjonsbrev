package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.etterlatte.maler.FeilutbetalingType
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingRedigerbartUtfallDTO

fun createOmstillingsstoenadRevurderingRedigerbartUtfallDTO() =
    OmstillingsstoenadRevurderingRedigerbartUtfallDTO(
        erEtterbetaling = true,
        harUtbetaling = true,
        feilutbetaling = FeilutbetalingType.FEILUTBETALING_UTEN_VARSEL
    )