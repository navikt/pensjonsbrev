package no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr

import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

data class InntektOgAldersovergangNesteArDto(
    val vedtakVirkFom: LocalDate,
    val gradertUforetrygd: Boolean,
    val nettoUforetrygd: Kroner,
    val nettoBarnetillegg: Kroner?,
    val nettoGjenlevendetillegg: Kroner?,
    val nettoTotal: Kroner,
    val bunnfradrag: Kroner,
    val fribelop: Kroner,
    val inntektEtterUforhet: Kroner,
    val datoFyller67: LocalDate,
    val datoForNormertPensjonsalder: LocalDate,
    val btfb: BarnetilleggFellesbarn?,
    val btsb: BarnetilleggSerkullsbarn?,
    val hjemler: Set<String>,

    val pe: PEgruppe10,
    val orienteringOmRettigheterUfoere: OrienteringOmRettigheterUfoereDto
) : AutobrevData {
    data class BarnetilleggFellesbarn(
        val netto: Kroner,
        val inntektBruker: Kroner,
        val inntektAnnenForelder: Kroner,
        val erEndret: Boolean,
    )

    data class BarnetilleggSerkullsbarn(
        val netto: Kroner,
        val inntektBruktIAvkortning: Kroner,
        val erEndret: Boolean,
    )
}

data class InntektOgAldersovergangNesteArAutoDto(
    val data: InntektOgAldersovergangNesteArDto
) : AutobrevData

data class InntektOgAldersovergangNesteArRedigerbarDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<InntektOgAldersovergangNesteArRedigerbarDto.PesysData> {
    data class PesysData(
        val data: InntektOgAldersovergangNesteArDto,
    ) : FagsystemBrevdata
}

