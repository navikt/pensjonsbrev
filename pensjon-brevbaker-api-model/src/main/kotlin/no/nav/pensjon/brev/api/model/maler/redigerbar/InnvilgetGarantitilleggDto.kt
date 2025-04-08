package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkstype
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterAlderDto
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

@Suppress("unused")
data class InnvilgetGarantitilleggDto(
    override val pesysData: PesysData, override val saksbehandlerValg: EmptyBrevdata
) : RedigerbarBrevdata<EmptyBrevdata, InnvilgetGarantitilleggDto.PesysData> {

    data class PesysData(
        val garantitillegg: Kroner,
        val kravAarsak: String,
        val kravVirkDatoFom: LocalDate,
        val minstenivaaIndividuellInnvilget: Boolean,
        val regelverkstype: AlderspensjonRegelverkstype,
        val saerskiltSatsErBrukt: Boolean,
        val totalPensjon: Kroner,
        val ufoereKombinertMedAlder: Boolean,
        val maanedligPensjonFoerSkattDto: MaanedligPensjonFoerSkattDto,
        val orienteringOmRettigheterAlderDto: OrienteringOmRettigheterAlderDto
    ) : BrevbakerBrevdata
}