package no.nav.pensjon.brev.alder.model.afpprivat

import no.nav.pensjon.brev.api.model.maler.AutobrevData
import java.time.LocalDate

/**
 * Auto-vedtak — avslag på AFP i privat sektor.
 *
 * Konvertert fra Exstream-malen `PE_AF_04_116` (autobrev-variant av den
 * redigerbare malen `PE_AF_04_112` / [AvslagAfpPrivatDto]). Brevet gjenbruker
 * [AvslagAfpPrivatDto.Begrunnelse]-enumet fra den redigerbare versjonen og legger til en
 * sluttparagraf som forklarer at vedtaket er produsert automatisk.
 */
data class AvslagAfpPrivatAutoDto(
    // PE_Vedtaksbrev_Vedtaksdata_Kravhode_KravMottatDato
    val kravMottattDato: LocalDate,
    // PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Begrunnelse[1]
    val begrunnelse: AvslagAfpPrivatDto.Begrunnelse,
) : AutobrevData
