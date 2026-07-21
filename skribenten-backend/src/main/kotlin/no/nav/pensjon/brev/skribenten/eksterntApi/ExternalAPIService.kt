package no.nav.pensjon.brev.skribenten.eksterntApi

import no.nav.pensjon.brev.skribenten.ExternalApiConfig
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.skribenten.SkribentenConfig
import no.nav.pensjon.brev.skribenten.brevredigering.application.OpprettBrevService
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.HentBrevForAlleSakerHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.HentBrevForAlleSakerService
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.OpprettBrevHandler
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.brevredigering.domain.MottakerType
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.asSuccess
import no.nav.pensjon.brev.skribenten.fagsystem.BrevmalService
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.SaksId
import no.nav.pensjon.brev.skribenten.routes.toLanguageCode
import no.nav.pensjon.brev.skribenten.services.toApi
import org.slf4j.LoggerFactory

class ExternalAPIService(
    config: ExternalApiConfig,
    private val hentBrevForAlleSaker: HentBrevForAlleSakerService,
    private val brevmalService: BrevmalService,
    private val opprettBrevHandler: OpprettBrevService,
) {

    @Suppress("unused") // Brukes av ktor-di
    constructor(config: SkribentenConfig, hentBrevForAlleSaker: HentBrevForAlleSakerService, brevmalService: BrevmalService, opprettBrevHandler: OpprettBrevHandler) :
            this(config.services.externalApi, hentBrevForAlleSaker, brevmalService, opprettBrevHandler)

    private val skribentenWebUrl = config.skribentenWebUrl

    companion object {
        private val logger = LoggerFactory.getLogger(ExternalAPIService::class.java)
    }

    suspend fun hentAlleBrevForSaker(saksIder: Set<SaksId>): List<ExternalAPI.BrevInfo> {
        val alleBrev = hentBrevForAlleSaker(HentBrevForAlleSakerHandler.Request(saksIder))?.asSuccess()?.value ?: return emptyList()
        val maler = alleBrev.map { it.brevkode }.toSet().associateWith { brevmalService.getRedigerbarTemplate(it) }

        return alleBrev.mapNotNull { it.toExternal(maler[it.brevkode]) }
    }

    private fun Dto.BrevInfo.aapneBrevUrl() = "$skribentenWebUrl/aapne/brev/${id.id}"

    private fun Dto.BrevInfo.toExternal(template: TemplateDescription.Redigerbar?) =
        if (template != null) {
            ExternalAPI.BrevInfo(
                url = aapneBrevUrl(),
                id = id,
                saksId = saksId,
                vedtaksId = vedtaksId,
                journalpostId = journalpostId,
                brevkode = brevkode,
                tittel = template.metadata.displayTitle,
                brevtype = template.metadata.brevtype,
                avsenderEnhetsId = avsenderEnhetId,
                spraak = spraak.toApi(),
                opprettetAv = opprettetAv,
                sistRedigertAv = sistredigertAv,
                redigeresAv = redigeresAv,
                opprettet = opprettet,
                sistRedigert = sistredigert,
                overstyrtMottaker = mottaker?.toExternal(),
                status = when (status) {
                    Dto.BrevStatus.KLADD -> ExternalAPI.BrevStatus.KLADD
                    Dto.BrevStatus.ATTESTERING -> ExternalAPI.BrevStatus.ATTESTERING
                    Dto.BrevStatus.KLAR -> ExternalAPI.BrevStatus.KLAR
                    Dto.BrevStatus.ARKIVERT -> ExternalAPI.BrevStatus.ARKIVERT
                },
            )
        } else {
            logger.warn("Fant ikke brevmal med brevkode $brevkode for brev med id $id")
            null
        }

    private fun Dto.Mottaker.toExternal(): ExternalAPI.OverstyrtMottaker = when (type) {
        MottakerType.SAMHANDLER -> ExternalAPI.OverstyrtMottaker.Samhandler(tssId!!)
        MottakerType.NORSK_ADRESSE -> ExternalAPI.OverstyrtMottaker.NorskAdresse(
            navn = navn!!,
            postnummer = postnummer!!,
            poststed = poststed!!,
            adresselinje1 = adresselinje1,
            adresselinje2 = adresselinje2,
            adresselinje3 = adresselinje3
        )

        MottakerType.UTENLANDSK_ADRESSE -> ExternalAPI.OverstyrtMottaker.UtenlandskAdresse(
            navn = navn!!,
            adresselinje1 = adresselinje1!!,
            adresselinje2 = adresselinje2,
            adresselinje3 = adresselinje3,
            landkode = landkode!!
        )
    }

    suspend fun opprettBrev(request: ExternalAPI.OpprettBrevRequest): Outcome<Dto.Brevredigering, BrevredigeringError> =
        opprettBrevHandler(
            OpprettBrevHandler.Request(
                saksId = request.saksId,
                vedtaksId = request.vedtaksId,
                brevkode = request.brevkode,
                spraak = request.spraak.toLanguageCode(),
                avsenderEnhetsId = request.avsenderEnhetsId,
                saksbehandlerValg = Api.GeneriskBrevdata().also { data -> request.saksbehandlerValg?.forEach { (k, v) -> data[k] = v } },
                reserverForRedigering = request.reserverForRedigering ?: true
            )
        )
}