package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattAlderspensjonDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

data class VedtakEndringAlderspensjonInstitusjonsoppholdDto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: PesysData
) : RedigerbarBrevdata<VedtakEndringAlderspensjonInstitusjonsoppholdDto.SaksbehandlerValg, VedtakEndringAlderspensjonInstitusjonsoppholdDto.PesysData> {
    data class SaksbehandlerValg(
        val alderspensjonUnderOppholdIInstitusjon: Boolean,
        val alderspensjonUnderSoning: Boolean,
        val alderspensjonVedVaretektsfengsling: Boolean,
        val alderspensjonRedusert: Boolean,
        val alderspensjonStanset: Boolean,
        val informasjonOmSivilstandVedInstitusjonsopphold: Boolean,
        val hvisReduksjonTilbakeITid: Boolean,
        val hvisEtterbetaling: Boolean,
        val hvisEndringIPensjon: Boolean
    ) : BrevbakerBrevdata

    data class PesysData(
        val beregnetPensjonPerManedVedVirk: BeregnetPensjonPerManedVedVirk,
        val krav: Krav,
        val institusjonsoppholdVedVirk: InstitusjonsoppholdVedVirk,
        val alderspensjonVedVirk: AlderspensjonVedVirk,
        val orienteringOmRettigheterOgPlikterDto: OrienteringOmRettigheterOgPlikterDto,
        val maanedligPensjonFoerSkattAlderspensjonDto: MaanedligPensjonFoerSkattAlderspensjonDto
    ) : BrevbakerBrevdata {

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