package no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr

import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

data class BunnfradragVedAldersovergangNesteArDto(
    val vedtakVirkFom: LocalDate,
    val gradertUforetrygd: Boolean,
    val nettoUforetrygd: Kroner,
    val nettoBarnetillegg: Kroner?,
    val nettoGjenlevendetillegg: Kroner?,
    val btfbEndret: Boolean,
    val btsbEndret: Boolean,
    val nettoTotal: Kroner,
    val bunnfradrag: Kroner,
    val fribelop: Kroner,
    val inntektEtterUforhet: Kroner,
    val datoFyller67: LocalDate,
    val datoForNormertPensjonsalder: LocalDate,
    val hjemler: Set<String>,

    val btfb: BarnetilleggFellesbarn?,
    val btsb: BarnetilleggSaerkullsbarn?,

    val pe: PEgruppe10,
    val orienteringOmRettigheterUfoere: OrienteringOmRettigheterUfoereDto
) : AutobrevData {
    data class BarnetilleggFellesbarn(
        val netto: Kroner,
        val inntektBruker: Kroner,
        val inntektAnnenForelder: Kroner,
    )

    data class BarnetilleggSaerkullsbarn(
        val netto: Kroner,
        val inntektBruktIAvkortning: Kroner,
    )
}

data class BunnfradragVedAldersovergangNesteArAutoDto(
    val data: BunnfradragVedAldersovergangNesteArDto
) : AutobrevData

data class BunnfradragVedAldersovergangNesteArRedigerbarDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<BunnfradragVedAldersovergangNesteArRedigerbarDto.PesysData> {
    data class PesysData(
        val data: BunnfradragVedAldersovergangNesteArDto,
    ) : FagsystemBrevdata
}

