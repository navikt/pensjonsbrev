package no.nav.pensjon.brev.alder.model.afp

import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year

data class VedtakAfpEtteroppgjoerIngenEndringNyeOpplysningerAutoDto(
    val oppgjoersAar: Year,
    val inntektFoerUttak: Kroner,
    val medlemAvApotekerordningen: Boolean,
    val toleranseBeloep: Kroner,
    val scenario: Scenario,
) : AutobrevData {
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
