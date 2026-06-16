package no.nav.pensjon.brev.alder.model.afp

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.BrevbakerType

/**
 * Redigerbart vedtak — AFP etteroppgjør (offentlig sektor / SPK), ingen endring
 * (andre avvik enn toleransebeløp).
 *
 * Konvertert fra Exstream-malen `PE_AF_04_110`. Auto-varianten av samme situasjon
 * er [VedtakAfpEtteroppgjoerIngenEndringAutoDto] (`PE_AF_04_102`).
 */
data class VedtakAfpEtteroppgjoerIngenEndringAndreAvvikDto(
    override val saksbehandlerValg: EmptySaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<EmptySaksbehandlerValg, VedtakAfpEtteroppgjoerIngenEndringAndreAvvikDto.PesysData> {

    data class PesysData(
        // PE_Vedtaksdata_Oppgjorsar
        val oppgjoersAar: BrevbakerType.Year,
        // PE_Grunnlag_Persongrunnlag_AFPEOGrunnlag_PGI
        val pgi: BrevbakerType.Kroner,
        val scenario: Scenario,
    ) : FagsystemBrevdata

    /**
     * Scenariovariant av forklaringen som styrer hvilken showIf-blokk som vises i den
     * redigerbare malen. Brevet utleder scenarioet fra Eksstream-feltene før det
     * sendes til brevbaker.
     */
    enum class Scenario {
        HEL_AFP_HELE_AARET_INNTEKT_FOER_UTTAK,
        HEL_AFP_HELE_AARET_INGEN_INNTEKT,
        IKKE_AFP_FULL_INNTEKT,
        HEL_AFP_DELER_AV_AARET,
    }
}