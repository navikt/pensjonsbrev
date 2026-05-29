package no.nav.pensjon.brev.alder.model.afpprivat

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import java.time.LocalDate

/**
 * Vedtak — avslag på AFP i privat sektor (redigerbar).
 *
 * Konvertert fra Exstream-malen `PE_AF_04_112`.
 */
data class AvslagAfpPrivatDto(
    override val saksbehandlerValg: EmptySaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<EmptySaksbehandlerValg, AvslagAfpPrivatDto.PesysData> {

    /**
     * Begrunnelsene tilsvarer rådata-strenger i originalen
     * (`PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse`):
     * `under_62`, `bruker_avslag_ap`, `bruker_ikke_ap`,
     * `bruker_up_e_62`, `bruker_ut_e_62`, `bruker_lop_up`.
     * Hjemmelshenvisningen følger gruppen:
     *  - § 5 første ledd bokstav a — [UNDER_62]
     *  - § 5 første ledd bokstav b — [BRUKER_AVSLAG_AP], [BRUKER_IKKE_AP]
     *  - § 8 første ledd            — [BRUKER_UP_E_62], [BRUKER_UT_E_62], [BRUKER_LOP_UP]
     */
    enum class Begrunnelse {
        UNDER_62,
        BRUKER_AVSLAG_AP,
        BRUKER_IKKE_AP,
        BRUKER_UP_E_62,
        BRUKER_UT_E_62,
        BRUKER_LOP_UP,
    }

    data class PesysData(
        // PE_Vedtaksdata_Kravhode_KravMottatDato
        val kravMottattDato: LocalDate,
        // PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse[1]
        val begrunnelse: Begrunnelse,
    ) : FagsystemBrevdata
}