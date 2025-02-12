package no.nav.pensjon.brev.skribenten.services

import no.nav.pensjon.brev.skribenten.db.MottakerType
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.Api.BrevStatus
import no.nav.pensjon.brev.skribenten.model.Api.NavAnsatt
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.NavIdent
import no.nav.pensjon.brevbaker.api.model.LanguageCode

class Dto2ApiService(
    private val brevbakerService: BrevbakerService,
    private val navansattService: NavansattService,
    private val norg2Service: Norg2Service,
    private val samhandlerService: SamhandlerService,
) {
    suspend fun toApi(brevredigering: Dto.Brevredigering): Api.BrevResponse =
        Api.BrevResponse(
            info = toApi(brevredigering.info),
            redigertBrev = brevredigering.redigertBrev,
            redigertBrevHash = brevredigering.redigertBrevHash,
            saksbehandlerValg = brevredigering.saksbehandlerValg,
        )

    suspend fun toApi(info: Dto.BrevInfo): Api.BrevInfo {
        val template = brevbakerService.getRedigerbarTemplate(info.brevkode)

        return Api.BrevInfo(
            id = info.id,
            opprettetAv = hentNavAnsatt(info.opprettetAv),
            opprettet = info.opprettet,
            sistredigertAv = hentNavAnsatt(info.sistredigertAv),
            sistredigert = info.sistredigert,
            brevkode = info.brevkode,
            brevtittel = template?.metadata?.displayTitle ?: info.brevkode.kode(),
            status =
                when {
                    info.journalpostId != null -> BrevStatus.Arkivert
                    info.laastForRedigering -> BrevStatus.Klar
                    info.redigeresAv != null -> BrevStatus.UnderRedigering(hentNavAnsatt(info.redigeresAv))
                    else -> BrevStatus.Kladd
                },
            distribusjonstype = info.distribusjonstype,
            mottaker = info.mottaker?.toApi(),
            avsenderEnhet = info.avsenderEnhetId?.let { norg2Service.getEnhet(it) },
            spraak = info.spraak.toApi(),
            journalpostId = info.journalpostId,
        )
    }

    private fun LanguageCode.toApi() =
        when (this) {
            LanguageCode.BOKMAL -> SpraakKode.NB
            LanguageCode.NYNORSK -> SpraakKode.NN
            LanguageCode.ENGLISH -> SpraakKode.EN
        }

    private suspend fun Dto.Mottaker.toApi(): Api.OverstyrtMottaker =
        when (type) {
            MottakerType.SAMHANDLER -> Api.OverstyrtMottaker.Samhandler(tssId!!, samhandlerService.hentSamhandlerNavn(tssId))
            MottakerType.NORSK_ADRESSE -> Api.OverstyrtMottaker.NorskAdresse(navn!!, postnummer!!, poststed!!, adresselinje1, adresselinje2, adresselinje3)
            MottakerType.UTENLANDSK_ADRESSE ->
                Api.OverstyrtMottaker.UtenlandskAdresse(
                    navn!!,
                    postnummer,
                    poststed,
                    adresselinje1!!,
                    adresselinje2,
                    adresselinje3,
                    landkode!!,
                )
        }

    suspend fun hentNavAnsatt(navIdent: NavIdent): NavAnsatt =
        NavAnsatt(navIdent, navansattService.hentNavansatt(navIdent.id)?.navn)
}
