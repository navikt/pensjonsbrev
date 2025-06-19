package no.nav.pensjon.brev.skribenten.services

import com.typesafe.config.Config
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.skribenten.db.MottakerType
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.ExternalAPI
import org.slf4j.LoggerFactory

class ExternalAPIService(
    config: Config,
    private val brevredigeringService: BrevredigeringService,
    private val brevbakerService: BrevbakerService
) {
    private val skribentenWebUrl = config.getString("skribentenWebUrl")

    companion object {
        private val logger = LoggerFactory.getLogger(ExternalAPIService::class.java)
    }

    suspend fun hentAlleBrevForSaker(saksIder: Set<Long>): List<ExternalAPI.BrevInfo> {
        val alleBrev = brevredigeringService.hentBrevForAlleSaker(saksIder)
        val maler = alleBrev.map { it.brevkode }.toSet().associateWith { brevbakerService.getRedigerbarTemplate(it) }

        return alleBrev.mapNotNull { it.toExternal(maler[it.brevkode]) }
    }

    private fun Dto.BrevInfo.aapneBrevUrl() = "$skribentenWebUrl/aapne/brev/$id"

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
            postnummer = postnummer,
            poststed = poststed,
            adresselinje1 = adresselinje1!!,
            adresselinje2 = adresselinje2,
            adresselinje3 = adresselinje3,
            landkode = landkode!!
        )
    }
}