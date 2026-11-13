package no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr

import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.FribelopUtvikling
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.Fribelopsperiode
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

data class OktBunnfradragNesteArDto(
    val vedtakVirkFom: LocalDate,
    val gradertUforetrygd: Boolean,
    val nettoUforetrygd: Kroner,
    val nettoBarnetillegg: Kroner?,
    val nettoGjenlevendetillegg: Kroner?,
    val endringUforetrygd: Kroner,
    val endringBarnetillegg: Kroner,
    val endringGjenlevendetillegg: Kroner,
    val bunnfradrag: Kroner,
    val inntektEtterUforhet: Kroner,
    val fribelop: Kroner,
    val grunnbelop: Kroner,
    val datoForNormertPensjonsalder: LocalDate,
    val sokerMottarApIlaAret: Boolean,
    val opphorsdato: LocalDate?,
    val fribelopsperioder: List<Fribelopsperiode>,
    val fribelopUtvikling: FribelopUtvikling,
    val periodisertBarnetillegg: Boolean,
    val hjemler: Set<String>,
    val pe: PEgruppe10,
    val orienteringOmRettigheterUfoere: OrienteringOmRettigheterUfoereDto
    )

data class OktBunnfradragNesteArAutoDto(
    val data: OktBunnfradragNesteArDto
) : AutobrevData

data class OktBunnfradragNesteArRedigerbarDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<OktBunnfradragNesteArRedigerbarDto.PesysData> {
    data class PesysData(
        val data: OktBunnfradragNesteArDto,
    ) : FagsystemBrevdata
}
