package no.nav.pensjon.brev.skribenten.fagsystem

import no.nav.pensjon.brev.skribenten.fagsystem.pesys.LegacyBrevService
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.PenClient
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.Pen
import no.nav.pensjon.brev.skribenten.model.Pen.SendRedigerbartBrevRequest
import no.nav.pensjon.brev.skribenten.model.SaksId
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Pid

class BrevService(private val penClient: PenClient, private val legacyBrevService: LegacyBrevService) {

    suspend fun sendbrev(sendRedigerbartBrevRequest: SendRedigerbartBrevRequest, distribuer: Boolean): Pen.BestillBrevResponse =
        penClient.sendbrev(sendRedigerbartBrevRequest, distribuer)

    suspend fun bestillOgRedigerExstreamBrev(gjelderPid: Pid, request: Api.BestillExstreamBrevRequest, saksId: SaksId): Api.BestillOgRedigerBrevResponse =
        legacyBrevService.bestillOgRedigerExstreamBrev(gjelderPid, request, saksId)

     suspend fun bestillOgRedigerEblankett(gjelderPid: Pid, request: Api.BestillEblankettRequest, saksId: SaksId): Api.BestillOgRedigerBrevResponse =
         legacyBrevService.bestillOgRedigerEblankett(gjelderPid, request, saksId)

}