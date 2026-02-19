package no.nav.pensjon.brev.api.model.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.KravInitiertAv
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenEndretUttaksgradDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brevbaker.api.model.DisplayText
import no.nav.pensjon.brevbaker.api.model.BrevWrappers.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevWrappers.Percent
import java.time.LocalDate

data class VedtakEndringAvUttaksgradDto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<VedtakEndringAvUttaksgradDto.SaksbehandlerValg, VedtakEndringAvUttaksgradDto.PesysData> {
    data class SaksbehandlerValg(
        @DisplayText("Hvis etterbetaling")
        val etterbetaling: Boolean?,
    ) : SaksbehandlerValgBrevdata

    data class PesysData(
        val orienteringOmRettigheterOgPlikterDto: OrienteringOmRettigheterOgPlikterDto?,
        val maanedligPensjonFoerSkattDto: MaanedligPensjonFoerSkattDto?,
        val krav: Krav,
        val alderspensjonVedVirk: AlderspensjonVedVirk,
        val beregnetPensjonPerManed: BeregnetPensjonPerManed,
        val maanedligPensjonFoerSkattAP2025Dto: MaanedligPensjonFoerSkattAP2025Dto?,
        val opplysningerBruktIBeregningenEndretUttaksgradDto: OpplysningerBruktIBeregningenEndretUttaksgradDto?
    ) : FagsystemBrevdata

    data class Krav(
        val kravInitiertAv: KravInitiertAv,
        val virkDatoFom: LocalDate,
    )

    data class AlderspensjonVedVirk(
        val uttaksgrad: Percent,
        val uforeKombinertMedAlder: Boolean,
        val totalPensjon: Kroner,
        val privatAFPErBrukt: Boolean,
        val regelverkType: AlderspensjonRegelverkType,
        val opphortEktefelletillegg: Boolean,
        val opphortBarnetillegg: Boolean,
    )

    data class BeregnetPensjonPerManed(
        val antallBeregningsperioderPensjon: Int
    )

}