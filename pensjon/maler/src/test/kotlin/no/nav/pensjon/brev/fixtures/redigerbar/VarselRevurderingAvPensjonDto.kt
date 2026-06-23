package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.brev.brevbaker.tilSaksbehandlervalgverdiEnum
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.redigerbar.VarselRevurderingAvPensjonDto

fun createVarselRevurderingAvPensjonDto() = VarselRevurderingAvPensjonDto(
    saksbehandlerValg = lagSaksbehandlervalg("tittelValg" to VarselRevurderingAvPensjonDto.SaksbehandlerValg.TittelValg.RevurderingAvRett.tilSaksbehandlervalgverdiEnum("Tittelvalg")),
    pesysData = VarselRevurderingAvPensjonDto.PesysData(sakstype = Sakstype.ALDER),
)