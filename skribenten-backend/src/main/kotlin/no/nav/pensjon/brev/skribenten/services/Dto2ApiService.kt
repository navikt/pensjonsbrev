package no.nav.pensjon.brev.skribenten.services

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

    suspend fun toApi(brevredigering: Dto.Brevredigering): Api.BrevResponse =
        Api.BrevResponse(
            info = toApi(brevredigering.info),
            redigertBrev = brevredigering.redigertBrev,
            redigertBrevHash = brevredigering.redigertBrevHash,
            saksbehandlerValg = brevredigering.saksbehandlerValg,
            propertyUsage = brevredigering.coverage,
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
            adresselinje3 = adresselinje3
        )

        MottakerType.UTENLANDSK_ADRESSE -> Api.OverstyrtMottaker.UtenlandskAdresse(
            navn = navn!!,
            postnummer = postnummer,
            poststed = poststed,
            adresselinje1 = adresselinje1!!,
            adresselinje2 = adresselinje2,
            adresselinje3 = adresselinje3,
            landkode = landkode!!
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