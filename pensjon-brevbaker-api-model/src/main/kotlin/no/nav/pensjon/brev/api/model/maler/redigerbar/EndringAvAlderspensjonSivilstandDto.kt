package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.KravArsakType
import no.nav.pensjon.brev.api.model.MetaforceSivilstand
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterAlderDto
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

@Suppress("unused")
data class EndringAvAlderspensjonSivilstandDto(
    override val pesysData: PesysData,
    override val saksbehandlerValg: SaksbehandlerValg,
) : RedigerbarBrevdata<EndringAvAlderspensjonSivilstandDto.SaksbehandlerValg, EndringAvAlderspensjonSivilstandDto.PesysData> {

    data class SaksbehandlerValg(
        val samboer15: MetaforceSivilstand,
    ) : BrevbakerBrevdata

    data class PesysData(
        val brukersSivilstand: MetaforceSivilstand,
        val garantitillegg: Kroner?,
        val kravAarsak: KravArsakType,
        val kravVirkDatoFom: LocalDate,
        val minstenivaaIndividuellInnvilget: Boolean,
        val regelverkType: AlderspensjonRegelverkType,
        val saerskiltSatsErBrukt: Boolean,
        val totalPensjon: Kroner,
        val ufoereKombinertMedAlder: Boolean,
        val epsVedVirk: EpsVedVirk,
        val maanedligPensjonFoerSkattDto: MaanedligPensjonFoerSkattDto,
        val orienteringOmRettigheterAlderDto: OrienteringOmRettigheterAlderDto
    ) : BrevbakerBrevdata

    data class EpsVedVirk(
        val harInntektOver2G: Boolean,
        val mottarPensjon: Boolean,
        val borSammenMedBruker: Boolean,
        val mottarOmstillingsstonad: Boolean,
    )
}