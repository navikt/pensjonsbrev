package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.BeloepEndring
import no.nav.pensjon.brevbaker.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brevbaker.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattAlderspensjonDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brevbaker.api.model.DisplayText
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

data class VedtakEndringAvAlderspensjonInstitusjonsoppholdDto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: PesysData
) : RedigerbarBrevdata<VedtakEndringAvAlderspensjonInstitusjonsoppholdDto.SaksbehandlerValg, VedtakEndringAvAlderspensjonInstitusjonsoppholdDto.PesysData> {
    data class SaksbehandlerValg(
        @DisplayText("Alderspensjon under opphold i institusjon")
        val alderspensjonUnderOppholdIInstitusjon: Boolean,
        @DisplayText("Alderspensjon under soning")
        val alderspensjonUnderSoning: Boolean,
        @DisplayText("Alderspensjon ved varetektsfengsling")
        val alderspensjonVedVaretektsfengsling: Boolean,
        @DisplayText("Alderspensjon redusert")
        val alderspensjonRedusert: Boolean,
        @DisplayText("Alderspensjon stanset")
        val alderspensjonStanset: Boolean,
        @DisplayText("Informasjon om sivilstand ved institusjonsopphold")
        val informasjonOmSivilstandVedInstitusjonsopphold: Boolean,
        @DisplayText("Hvis reduksjon tilbake i tid")
        val hvisReduksjonTilbakeITid: Boolean,
        @DisplayText("Hvis etterbetaling")
        val etterbetaling: Boolean?,
    ) : SaksbehandlerValgBrevdata

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