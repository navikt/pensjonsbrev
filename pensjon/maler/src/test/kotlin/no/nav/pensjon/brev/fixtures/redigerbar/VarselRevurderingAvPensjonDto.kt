package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgVerdi
import no.nav.pensjon.brev.api.model.maler.redigerbar.VarselRevurderingAvPensjonDto

fun createVarselRevurderingAvPensjonDto() = VarselRevurderingAvPensjonDto(
    saksbehandlerValg = lagSaksbehandlervalg(),
    pesysData = VarselRevurderingAvPensjonDto.PesysData(sakstype = Sakstype.ALDER),
)

private fun lagSaksbehandlervalg(defaultverdi: SaksbehandlervalgVerdi = SaksbehandlervalgVerdi.Enum(
    VarselRevurderingAvPensjonDto.SaksbehandlerValg.TittelValg.RevurderingAvRett,
    "Tittelvalg",
    VarselRevurderingAvPensjonDto.SaksbehandlerValg.TittelValg::class.java,
)): SaksbehandlervalgIDSL = object : SaksbehandlervalgIDSL {
    override val verdier: Map<String, SaksbehandlervalgVerdi> = emptyMap()

    override fun <T : SaksbehandlervalgVerdi> get(key: String): T {
        return defaultverdi as T
    }
}
