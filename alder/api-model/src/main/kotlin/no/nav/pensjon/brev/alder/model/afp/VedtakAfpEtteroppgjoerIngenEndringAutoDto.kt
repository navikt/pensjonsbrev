package no.nav.pensjon.brev.alder.model.afp

import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner

/**
 * Vedtak — ingen endring (andre avvik) — AFP etteroppgjør (autobrev).
 *
 * Konvertert fra Exstream-malen `PE_AF_04_102`. Brevet sendes når vi har
 * gjennomført etteroppgjør for AFP (offentlig sektor / Statens pensjonskasse)
 * og kommet til at pensjonsberegningen ikke skal endres. Det finnes fire
 * gjensidig utelukkende scenarier som forklarer hvorfor — modellert som
 * [Scenario] (skill-step 7: Exstream-betingelsene avledes hos kalleren slik
 * at malen tar imot en ferdig diskriminator).
 *
 * Field comments carry the original `PE_…` source path so the mapping team kan
 * grep `pe_xml_mappinger(in).csv` for å finne tilsvarende PESYS-XML-node.
 */
data class VedtakAfpEtteroppgjoerIngenEndringAutoDto(
    // PE_Vedtaksdata_Oppgjorsar
    // (rtv-brev brev Vedtaksdata Oppgjorsar)
    val oppgjoersAar: Int,

    // PE_Grunnlag_Persongrunnlag_AFPEOGrunnlag_PGI
    // (rtv-brev brev Grunnlag Persongrunnlagsliste Persongrunnlag AFPEOGrunnlag PGI)
    // Pensjonsgivende inntekt fra Skatteetaten. Vises i brevteksten kun for
    // scenariene HEL_AFP_HELE_AARET_INNTEKT_FOER_UTTAK og IKKE_AFP_FULL_INNTEKT,
    // men holdes alltid med i Dto-en for enkelhet.
    val pgi: Kroner,

    val scenario: Scenario,
) : AutobrevData {

    /**
     * Hvilket av de fire gjensidig utelukkende scenariene som beskriver
     * hvorfor pensjonsberegningen ikke endres. Eksstream uttrykte disse som
     * fire `showIf`-blokker over rådata; her er logikken løftet ut av malen.
     */
    enum class Scenario {
        // PGI < IFU AND IIAP=0 AND FPIberegnet=0 AND UtbetaltAFP=fullAFP
        // AND AFP_Uttaksdato >= Dato0102
        HEL_AFP_HELE_AARET_INNTEKT_FOER_UTTAK,

        // AFP_Uttaksdato < Dato0102 AND (Opphorsdato >= Dato3112 OR Opphorsdato tom)
        // AND UtbetaltAFP = fullAFP AND PGI = 0
        HEL_AFP_HELE_AARET_INGEN_INNTEKT,

        // UtbetaltAFP = 0 AND PGI >= TPIberegnet
        IKKE_AFP_FULL_INNTEKT,

        // AFP_Uttaksdato < Dato0102 AND Opphorsdato < Dato3112
        // AND PGI <= 15 100 AND UtbetaltAFP = fullAFP
        HEL_AFP_DELER_AV_AARET,
    }
}
