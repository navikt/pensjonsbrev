package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.BeloepEndring
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdataMedSaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattAlderspensjonDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

data class VedtakEndringAvAlderspensjonInstitusjonsoppholdDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: PesysData
) : RedigerbarBrevdataMedSaksbehandlerValg<VedtakEndringAvAlderspensjonInstitusjonsoppholdDto.PesysData> {

    data class PesysData(
        val beregnetPensjonPerManedVedVirk: BeregnetPensjonPerManedVedVirk,
        val krav: Krav,
        val institusjonsoppholdVedVirk: InstitusjonsoppholdVedVirk,
        val alderspensjonVedVirk: AlderspensjonVedVirk,
        val beloepEndring: BeloepEndring,
        val orienteringOmRettigheterOgPlikterDto: OrienteringOmRettigheterOgPlikterDto,
        val maanedligPensjonFoerSkattAlderspensjonDto: MaanedligPensjonFoerSkattAlderspensjonDto?
    ) : FagsystemBrevdata {

        data class BeregnetPensjonPerManedVedVirk(
            val totalPensjon: Kroner,
            val antallBeregningsperioderPensjon: Int
        )

        data class Krav(
            val virkDatoFom: LocalDate
        )

        data class InstitusjonsoppholdVedVirk(
            val helseinstitusjon: Boolean,
            val fengsel: Boolean
        )

        data class AlderspensjonVedVirk(
            val totalPensjon: Kroner,
            val uforeKombinertMedAlder: Boolean,
            val regelverkType: AlderspensjonRegelverkType,
        )
    }
}