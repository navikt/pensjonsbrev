package no.nav.pensjon.brev.skribenten.services.brev

import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.skribenten.domain.MottakerType
import no.nav.pensjon.brev.skribenten.domain.Brevredigering
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.SaksId
import no.nav.pensjon.brev.skribenten.model.VedtaksId
import no.nav.pensjon.brev.skribenten.services.BrevdataResponse
import no.nav.pensjon.brev.skribenten.services.EnhetId
import no.nav.pensjon.brev.skribenten.services.PenService
import no.nav.pensjon.brev.skribenten.services.SamhandlerService
import no.nav.pensjon.brevbaker.api.model.SignerendeSaksbehandlere

class BrevdataService(private val penService: PenService, private val samhandlerService: SamhandlerService) {

    suspend fun hentBrevdata(
        saksId: SaksId,
        vedtaksId: VedtaksId?,
        brevkode: Brevkode.Redigerbart,
        avsenderEnhetsId: EnhetId,
        mottaker: Dto.Mottaker?,
        signatur: SignerendeSaksbehandlere
    ): BrevdataResponse.Data {
        val pesysData = penService.hentPesysBrevdata(
            saksId = saksId,
            vedtaksId = vedtaksId,
            brevkode = brevkode,
            avsenderEnhetsId = avsenderEnhetsId,
        )

        return pesysData.copy(
            felles = pesysData.felles
                .medSignerendeSaksbehandlere(signatur)
                .medAnnenMottakerNavn(mottaker?.annenMottakerNavn())
        )
    }

    suspend fun hentBrevdata(brev: Brevredigering): BrevdataResponse.Data =
        hentBrevdata(
            saksId = brev.saksId,
            vedtaksId = brev.vedtaksId,
            brevkode = brev.brevkode,
            avsenderEnhetsId = brev.avsenderEnhetId,
            mottaker = brev.mottaker?.toDto(),
            signatur = SignerendeSaksbehandlere(
                // Redigerbare brev skal alltid ha saksbehandlers signatur, derfor non-null assertion her.
                saksbehandler = brev.redigertBrev.signatur.saksbehandlerNavn!!,
                attesterendeSaksbehandler = brev.redigertBrev.signatur.attesterendeSaksbehandlerNavn,
            ),
        )

    suspend fun hentAnnenMottakerNavn(mottaker: Dto.Mottaker): String? =
        mottaker.annenMottakerNavn()

    private suspend fun Dto.Mottaker.annenMottakerNavn(): String? =
        when (type) {
            MottakerType.SAMHANDLER -> tssId?.let { samhandlerService.hentSamhandlerNavn(it) }
            MottakerType.NORSK_ADRESSE, MottakerType.UTENLANDSK_ADRESSE ->
                if (manueltAdressertTil == Dto.Mottaker.ManueltAdressertTil.ANNEN) navn else null
        }
}