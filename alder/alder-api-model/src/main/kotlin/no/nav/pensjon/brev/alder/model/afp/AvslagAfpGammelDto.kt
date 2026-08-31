package no.nav.pensjon.brev.alder.model.afp

import no.nav.pensjon.brev.api.model.maler.BrevdataMedSaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import java.time.LocalDate

/**
 * Vedtak — avslag på avtalefestet pensjon (AFP) etter den gamle AFP-ordningen
 * (lov av 23. desember 1988 om statstilskott til ordninger for avtalefestet pensjon — "tilskottsloven").
 *
 * Konvertert fra Exstream-malen `PE_AF_04_010`.
 *
 * Selve hjemmelshenvisningen ("paragraf …") er fritekst i originalen og må
 * fylles ut av saksbehandler ved hjelp av [no.nav.pensjon.brev.template.dsl.fritekst].
 */
data class AvslagAfpGammelDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: PesysData,
) : BrevdataMedSaksbehandlerValg<AvslagAfpGammelDto.PesysData> {

    data class PesysData(
        // PE_Kravdata_Kravhode_KravMotattDato
        val kravMottattDato: LocalDate,
    ) : FagsystemBrevdata
}
