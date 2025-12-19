package no.nav.pensjon.brev.skribenten.services

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import no.nav.pensjon.brev.skribenten.db.MottakerType
import no.nav.pensjon.brev.skribenten.domain.BrevedigeringError
import no.nav.pensjon.brev.skribenten.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.domain.RedigerBrevPolicy
import no.nav.pensjon.brev.skribenten.domain.Reservasjon
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.Api.BrevStatus
import no.nav.pensjon.brev.skribenten.model.Api.NavAnsatt
import no.nav.pensjon.brev.skribenten.model.NavIdent
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brev.skribenten.usecase.Result

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
            propertyUsage = brevredigering.propertyUsage,
            valgteVedlegg = brevredigering.valgteVedlegg,
        )

    suspend fun toApi(info: Dto.BrevInfo): Api.BrevInfo {
        val template = brevbakerService.getRedigerbarTemplate(info.brevkode)
            ?: throw BrevredigeringException.BrevmalFinnesIkke("Fant ikke mal for brevkode i brevbaker: ${info.brevkode}")

        return Api.BrevInfo(
            id = info.id,
            saksId = info.saksId,
            opprettetAv = hentNavAnsatt(info.opprettetAv),
            opprettet = info.opprettet,
            sistredigertAv = hentNavAnsatt(info.sistredigertAv),
            sistredigert = info.sistredigert,
            brevkode = info.brevkode,
            brevtittel = template.metadata.displayTitle,
            brevtype = template.metadata.brevtype,
            status = when (info.status) {
                Dto.BrevStatus.KLADD ->
                    if (info.redigeresAv != null) {
                        BrevStatus.UnderRedigering(hentNavAnsatt(info.redigeresAv))
                    } else {
                        BrevStatus.Kladd
                    }

                Dto.BrevStatus.ATTESTERING -> BrevStatus.Attestering
                Dto.BrevStatus.KLAR -> BrevStatus.Klar(info.attestertAv?.let { hentNavAnsatt(it) })
                Dto.BrevStatus.ARKIVERT -> BrevStatus.Arkivert
            },
            distribusjonstype = info.distribusjonstype,
            mottaker = info.mottaker?.toApi(),
            avsenderEnhet = info.avsenderEnhetId?.let { norg2Service.getEnhet(it) },
            spraak = info.spraak.toApi(),
            journalpostId = info.journalpostId,
            vedtaksId = info.vedtaksId,
        )
    }

    suspend fun toApi(reservasjon: Reservasjon): Api.ReservasjonResponse = with(reservasjon) {
        Api.ReservasjonResponse(
            vellykket = vellykket,
            reservertAv = NavAnsatt(reservertAv, navansattService.hentNavansatt(reservertAv.id)!!.navn),
            timestamp = timestamp,
            expiresIn = expiresIn,
            redigertBrevHash = redigertBrevHash,
        )
    }

    suspend fun respond(call: ApplicationCall, result: Result<Dto.Brevredigering, BrevedigeringError>?) {
        when (result) {
            is Result.Success -> call.respond(toApi(result.value))
            is Result.Failure -> when (result.error) {
                is BrevreservasjonPolicy.ReservertAvAnnen ->
                    call.respond(HttpStatusCode.Locked, toApi(result.error.eksisterende))
                is RedigerBrevPolicy.KanIkkeRedigere.ArkivertBrev ->
                    call.respond(HttpStatusCode.Conflict, "Brev er arkivert med journalpostId: ${result.error.journalpostId}")
                is RedigerBrevPolicy.KanIkkeRedigere.IkkeReservert ->
                    call.respond(HttpStatusCode.Conflict, "Brev er ikke reservert for redigering av deg")
                is RedigerBrevPolicy.KanIkkeRedigere.LaastBrev ->
                    call.respond(HttpStatusCode.Locked, "Brev er låst for redigering")
            }
            null -> call.respond(HttpStatusCode.NotFound, "Fant ikke brev")
        }
    }

    private suspend fun Dto.Mottaker.toApi(): Api.OverstyrtMottaker = when (type) {
        MottakerType.SAMHANDLER -> Api.OverstyrtMottaker.Samhandler(
            tssId = tssId!!,
            navn = samhandlerService.hentSamhandlerNavn(tssId)
        )

        MottakerType.NORSK_ADRESSE -> Api.OverstyrtMottaker.NorskAdresse(
            navn = navn!!,
            postnummer = postnummer!!,
            poststed = poststed!!,
            adresselinje1 = adresselinje1,
            adresselinje2 = adresselinje2,
            adresselinje3 = adresselinje3,
            manueltAdressertTil = manueltAdressertTil
        )

        MottakerType.UTENLANDSK_ADRESSE -> Api.OverstyrtMottaker.UtenlandskAdresse(
            navn = navn!!,
            adresselinje1 = adresselinje1!!,
            adresselinje2 = adresselinje2,
            adresselinje3 = adresselinje3,
            landkode = landkode!!,
            manueltAdressertTil = manueltAdressertTil
        )
    }

    suspend fun hentNavAnsatt(navIdent: NavIdent): NavAnsatt =
        NavAnsatt(navIdent, navansattService.hentNavansatt(navIdent.id)?.navn)

}

fun LanguageCode.toApi() = when (this) {
    LanguageCode.BOKMAL -> SpraakKode.NB
    LanguageCode.NYNORSK -> SpraakKode.NN
    LanguageCode.ENGLISH -> SpraakKode.EN
}