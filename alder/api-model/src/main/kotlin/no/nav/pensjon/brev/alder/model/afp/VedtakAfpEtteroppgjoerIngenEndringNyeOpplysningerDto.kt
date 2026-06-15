package no.nav.pensjon.brev.alder.model.afp

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year

data class VedtakAfpEtteroppgjoerIngenEndringNyeOpplysningerDto(
    override val saksbehandlerValg: EmptySaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<EmptySaksbehandlerValg, VedtakAfpEtteroppgjoerIngenEndringNyeOpplysningerDto.PesysData> {

    data class PesysData(
        val oppgjoersAar: Year,
        val inntektFoerUttak: Kroner,
        val medlemAvApotekerordningen: Boolean,
        val toleranseBeloep: Kroner,
        val scenario: Scenario,
    ) : FagsystemBrevdata

    /*
     * Tre gjensidig utelukkende scenarier forklarer hvorfor — modellert som
     * [Scenario] (skill-step 7: Exstream-betingelsene avledes hos kalleren slik
     * at malen tar imot en ferdig diskriminator). Original-betingelsene var:
     *   * `HEL_AFP_ALL_INNTEKT_FOER_UTTAK` — `UtbetaltAFP = fullAFP AND PGI = IFU`
     *   * `HEL_AFP_INNTEKT_INNEN_TOLERANSE` — `UtbetaltAFP = fullAFP AND PGI != IFU
     *      AND PGI <= IFU + 15 000`
     *   * `GRADERT_AFP_INNTEKT_SVARER_TIL_FORVENTET` — `0 < UtbetaltAFP < fullAFP
     *      AND PGI <= IFU + FPIberegnet + 15 000`
     */
    enum class Scenario {
        HEL_AFP_ALL_INNTEKT_FOER_UTTAK, HEL_AFP_INNTEKT_INNEN_TOLERANSE, GRADERT_AFP_INNTEKT_SVARER_TIL_FORVENTET,
    }
}
