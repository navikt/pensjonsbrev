package no.nav.pensjon.brev.skribenten.services

import io.ktor.server.application.*
import no.nav.pensjon.brev.skribenten.db.MottakerType
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.Api.BrevStatus
import no.nav.pensjon.brev.skribenten.model.Api.NavAnsatt
import no.nav.pensjon.brev.skribenten.model.NavIdent
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brevbaker.api.model.LanguageCode

class Dto2ApiService(
    private val brevbakerService: BrevbakerService,
    private val navansattService: NavansattService,
    private val norg2Service: Norg2Service,
    private val samhandlerService: SamhandlerService,
) {

    suspend fun toApi(call: ApplicationCall, brevredigering: Dto.Brevredigering): Api.BrevResponse =
        Api.BrevResponse(
            info = toApi(call, brevredigering.info),
            redigertBrev = brevredigering.redigertBrev,
            redigertBrevHash = brevredigering.redigertBrevHash,
            saksbehandlerValg = brevredigering.saksbehandlerValg,
        )

    suspend fun toApi(call: ApplicationCall, info: Dto.BrevInfo): Api.BrevInfo {
        val template = brevbakerService.getRedigerbarTemplate(call, info.brevkode)

        return Api.BrevInfo(
            id = info.id,
            opprettetAv = hentNavAnsatt(call, info.opprettetAv),
            opprettet = info.opprettet,
            sistredigertAv = hentNavAnsatt(call, info.sistredigertAv),
            sistredigert = info.sistredigert,
            brevkode = info.brevkode,
            brevtittel = template?.metadata?.displayTitle ?: info.brevkode.name,
            status = when {
                info.laastForRedigering -> BrevStatus.Klar
                info.redigeresAv != null -> BrevStatus.UnderRedigering(hentNavAnsatt(call, info.redigeresAv))
                else -> BrevStatus.Kladd
            },
            distribusjonstype = info.distribusjonstype,
            mottaker = info.mottaker?.toApi(call),
            avsenderEnhet = info.avsenderEnhetId?.let { norg2Service.getEnhet(call, it) },
            spraak = info.spraak.toApi()
        )
    }

    private fun LanguageCode.toApi() = when (this) {
        LanguageCode.BOKMAL -> SpraakKode.NB
        LanguageCode.NYNORSK -> SpraakKode.NN
        LanguageCode.ENGLISH -> SpraakKode.EN
    }

    private suspend fun Dto.Mottaker.toApi(call: ApplicationCall): Api.OverstyrtMottaker = when (type) {
        MottakerType.SAMHANDLER -> Api.OverstyrtMottaker.Samhandler(tssId!!, samhandlerService.hentSamhandlerNavn(call, tssId))
        MottakerType.NORSK_ADRESSE -> Api.OverstyrtMottaker.NorskAdresse(navn!!, postnummer!!, poststed!!, adresselinje1, adresselinje2, adresselinje3)
        MottakerType.UTENLANDSK_ADRESSE -> Api.OverstyrtMottaker.UtenlandskAdresse(
            navn!!,
            postnummer,
            poststed,
            adresselinje1!!,
            adresselinje2,
            adresselinje3,
            landkode!!
        )
    }

    private suspend fun hentNavAnsatt(call: ApplicationCall, navIdent: NavIdent): NavAnsatt =
        NavAnsatt(navIdent, navansattService.hentNavansatt(call, navIdent.id)?.navn)

}