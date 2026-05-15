package no.nav.pensjon.brev.alder.model.afp

import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year

/**
 * Vedtak — AFP etteroppgjør EO fase 2 (autobrev).
 *
 * Konvertert fra Exstream-malen `PE_AF_04_106`. Brevet sendes når Nav har
 * gjennomført etteroppgjør for AFP (offentlig sektor / Statens pensjonskasse)
 * basert på nye opplysninger fra bruker om inntekt før AFP-uttak, og
 * konkludert med at det ikke skal tilbakekreves noe.
 *
 * Tre gjensidig utelukkende scenarier forklarer hvorfor — modellert som
 * [Scenario] (skill-step 7: Exstream-betingelsene avledes hos kalleren slik
 * at malen tar imot en ferdig diskriminator). Original-betingelsene var:
 *   * `HEL_AFP_ALL_INNTEKT_FOER_UTTAK` — `UtbetaltAFP = fullAFP AND PGI = IFU`
 *   * `HEL_AFP_INNTEKT_INNEN_TOLERANSE` — `UtbetaltAFP = fullAFP AND PGI != IFU
 *      AND PGI <= IFU + 15 000`
 *   * `GRADERT_AFP_INNTEKT_SVARER_TIL_FORVENTET` — `0 < UtbetaltAFP < fullAFP
 *      AND PGI <= IFU + FPIberegnet + 15 000`
 */
data class VedtakAfpEtteroppgjoerEoFase2AutoDto(
    // PE_Vedtaksdata_Oppgjorsar
    val oppgjoersAar: Year,

    // PE_Grunnlag_Persongrunnlag_AFPEOGrunnlag_IFU
    // Inntekt opptjent før uttak av AFP. Vises kun i tekstvariantene for
    // scenariene [Scenario.HEL_AFP_INNTEKT_INNEN_TOLERANSE] og
    // [Scenario.GRADERT_AFP_INNTEKT_SVARER_TIL_FORVENTET], men holdes alltid
    // med i Dto-en for enkelhet.
    val ifu: Kroner,

    val scenario: Scenario,
) : AutobrevData {

    enum class Scenario {
        HEL_AFP_ALL_INNTEKT_FOER_UTTAK,
        HEL_AFP_INNTEKT_INNEN_TOLERANSE,
        GRADERT_AFP_INNTEKT_SVARER_TIL_FORVENTET,
    }
}
