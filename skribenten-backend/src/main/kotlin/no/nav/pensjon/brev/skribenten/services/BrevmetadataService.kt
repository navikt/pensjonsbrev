package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.databind.DeserializationFeature
import com.typesafe.config.Config
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import no.nav.pensjon.brev.skribenten.services.BrevdataDto.BrevkontekstCode.*
import org.slf4j.LoggerFactory

class BrevmetadataService(config: Config) {
    private val brevmetadataUrl = config.getString("url")
    private val logger = LoggerFactory.getLogger(BrevmetadataService::class.java)
    private val httpClient = HttpClient(CIO) {
        defaultRequest {
            url(brevmetadataUrl)
        }
        install(ContentNegotiation) {
            jackson {
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            }
        }
    }

    suspend fun getRedigerbareBrev(sakstype: PenService.SakType, includeVedtak: Boolean): List<LetterMetadata> {
        val httpResponse = httpClient.get("/api/brevdata/brevdataForSaktype/$sakstype?includeXsd=false") {
            contentType(ContentType.Application.Json)
        }

        if (httpResponse.status.isSuccess()) {
            return httpResponse.body<List<BrevdataDto>>()
                .filter { it.redigerbart }
                .filter { filterForKontekst(it, includeVedtak) }
                .map { it.mapToMetadata() }
        } else {
            logger.error("Feil ved henting av brevmetadata. Status: ${httpResponse.status} Message: ${httpResponse.bodyAsText()}")
            return emptyList()
        }
    }

    private fun filterForKontekst(brevdataDto: BrevdataDto, includeVedtak: Boolean): Boolean =
        when(brevdataDto.brevkontekst){
            ALLTID -> true
            SAK -> !includeVedtak
            VEDTAK -> includeVedtak
            null -> false
        }

    private fun BrevdataDto.mapToMetadata() =
        LetterMetadata(
            name = dekode,     // TODO handle missing fields in front-end instead.
            id = brevkodeIBrevsystem,
            spraak = sprak ?: emptyList(),
            brevsystem = when (brevsystem) {
                BrevdataDto.BrevSystem.DOKSYS -> BrevSystem.DOKSYS
                BrevdataDto.BrevSystem.GAMMEL -> BrevSystem.EXSTREAM
            },
            brevkategoriCode = this.brevkategori,
            dokumentkategoriCode = this.dokumentkategori
        )


    suspend fun getEblanketter(): List<LetterMetadata> {
        return httpClient.get("/api/brevdata/allBrev?includeXsd=false") {
            contentType(ContentType.Application.Json)
        }.body<List<BrevdataDto>>()
            .filter { it.dokumentkategori == BrevdataDto.DokumentkategoriCode.E_BLANKETT }
            .map { it.mapToMetadata() }
    }

    suspend fun getMal(brevkode: String): BrevdataDto {
        return httpClient.get("/api/brevdata/brevForBrevkode/${brevkode}") {
            contentType(ContentType.Application.Json)
        }.body<BrevdataDto>()
    }

//    fun execute(request: HentBrevMenyRequest): HentBrevMenyResponse {
//        validateRequest(request)
//
//        val sak: Sak = fetchSakFromDb(request)
//        var brevdataList: List<Brevdata> = brevdataService.hentBrevdataForSaktype(sak.getSakType().getCode(), false)
//        if (!org.apache.commons.lang3.BooleanUtils.isTrue(request.isVisFlere())) {
//            if (org.apache.commons.lang3.BooleanUtils.isTrue(request.isErVeileder())) {
//                brevdataList = filterBrevdataOnVeileder(brevdataList)
//            } else {
//                if (request.getKravId() != null) {
//                    val krav: KravHode = fetchKravFromDb(request)
//
//                    brevdataList = filterBrevdataOnKravGjelder(brevdataList, krav)
//                    brevdataList = filterBrevdataOnUtland(brevdataList, krav, sak)
//                }
//
//                brevdataList = if (request.getVedtakId() == null) {
//                    filterBrevdataOnSakKontekst(brevdataList)
//                } else {
//                    filterBrevdataOnVedtakKontekst(brevdataList)
//                }
//            }
//        } else {
//            if (request.getVedtakId() == null) {
//                brevdataList = filterBrevdataOnSakKontekst(brevdataList)
//            }
//        }
//        val response: HentBrevMenyResponse = HentBrevMenyResponse()
//        response.setBrevdata(brevdataList)
//        return response
//    }
//
//    private fun filterBrevdataOnVeileder(brevdataList: List<Brevdata>): List<Brevdata> {
//        return brevdataList.stream().filter(Predicate<Brevdata> { brevdata: Brevdata ->
//            org.apache.commons.lang3.BooleanUtils.isTrue((brevdata as Brev).getSynligForVeileder())
//        }).collect(Collectors.toList<Any>())
//    }
//
//    private fun filterBrevdataOnKravGjelder(brevdataList: List<Brevdata>, kravHode: KravHode): List<Brevdata> {
//        val filteredBrevdatList: MutableList<Brevdata> = ArrayList<Brevdata>()
//        for (brevdata in brevdataList) {
//            val brev: Brev = brevdata as Brev
//            if (brev.getBrevkravtype() != null &&
//                (brev.getBrevkravtype().equals(BrevKravTypeCode.ALLE) ||
//                        kravHode.getKravGjelder().getCodeAsString().equals(brev.getBrevkravtype().toString()))
//            ) {
//                filteredBrevdatList.add(brev)
//            }
//        }
//        return filteredBrevdatList
//    }
//
//    private fun filterBrevdataOnUtland(brevdataList: List<Brevdata>, kravHode: KravHode, sak: Sak): List<Brevdata> {
//        val utlandsStatus: BrevUtlandCode = if (sak.getSakType().isCodeEqualTo(SakTypeCode.GJENLEV) && org.apache.commons.lang3.BooleanUtils.isTrue(
//                kravHode.getBoddArbeidUtlandAvdod()
//            )
//        ) {
//            BrevUtlandCode.UTLAND
//        } else if (sak.getSakType().isCodeEqualTo(SakTypeCode.BARNEP)
//            && (org.apache.commons.lang3.BooleanUtils.isTrue(kravHode.getBoddArbeidUtlandFar()) || org.apache.commons.lang3.BooleanUtils.isTrue(
//                kravHode.getBoddArbeidUtlandMor()
//            ))
//        ) {
//            BrevUtlandCode.UTLAND
//        } else if (org.apache.commons.lang3.BooleanUtils.isTrue(kravHode.getBoddArbeidUtland())) {
//            BrevUtlandCode.UTLAND
//        } else {
//            BrevUtlandCode.NASJONALT
//        }
//
//        return brevdataList.stream().filter(
//            Predicate<Brevdata> { brevdata: Brevdata ->
//                ((brevdata as Brev).getUtland() != null && ((brevdata as Brev).getUtland()
//                    .equals(BrevUtlandCode.ALLTID) ||
//                        utlandsStatus.equals((brevdata as Brev).getUtland())
//                        )
//                        )
//            }
//        ).collect(Collectors.toList<Any>())
//    }
//
//    private fun filterBrevdataOnSakKontekst(brevdataList: List<Brevdata>): List<Brevdata> {
//        return brevdataList.stream().filter(Predicate<Brevdata> { brevdata: Brevdata ->
//            brevdataHas
//            Brevkontekst(
//                brevdata,
//                BrevKontekstCode.SAK
//            )
//        }).collect(Collectors.toList<Any>())
//    }
//
//    private fun filterBrevdataOnVedtakKontekst(brevdataList: List<Brevdata>): List<Brevdata> {
//        return brevdataList.stream().filter(Predicate<Brevdata> { brevdata: Brevdata ->
//            brevdataHasBrevkontekst(
//                brevdata,
//                BrevKontekstCode.VEDTAK
//            )
//        }).collect(Collectors.toList<Any>())
//    }
//
//    private fun brevdataHasBrevkontekst(brevdata: Brevdata, brevKontekstCode: BrevKontekstCode): Boolean {
//        var result = false
//        if (BrevKontekstCode.SAK.equals(brevKontekstCode)) {
//            if ((brevdata as Brev).getBrevkontekst() != null
//                && EnumUtils.isEnumNameOfValue(
//                    (brevdata as Brev).getBrevkontekst().toString(), BrevKontekstCode.SAK,
//                    BrevKontekstCode.ALLTID
//                )
//            ) {
//                result = true
//            }
//        } else if (BrevKontekstCode.VEDTAK.equals(brevKontekstCode)) {
//            if ((brevdata as Brev).getBrevkontekst() != null
//                && EnumUtils.isEnumNameOfValue(
//                    (brevdata as Brev).getBrevkontekst().toString(), BrevKontekstCode.VEDTAK,
//                    BrevKontekstCode.ALLTID
//                )
//            ) {
//                result = true
//            }
//        }
//        return result
//    }
}

data class BrevdataDto(
    val redigerbart: Boolean,
    val dekode: String,
    val brevkategori: BrevkategoriCode?,
    val dokType: DokumentType,
    val sprak: List<SpraakKode>?,
    val visIPselv: Boolean?,
    val utland: String?,
    val brevregeltype: String?,
    val brevkravtype: String?,
    val brevkontekst: BrevkontekstCode?,
    val dokumentkategori: DokumentkategoriCode,
    val synligForVeileder: Boolean?,
    val prioritet: Int?,
    val brevkodeIBrevsystem: String,
    val brevsystem: BrevSystem,
    val brevgruppe: String?,
) {
    enum class DokumentkategoriCode { B, E_BLANKETT, IB, SED, VB }
    enum class BrevkategoriCode { BREV_MED_SKJEMA, INFORMASJON, INNHENTE_OPPL, NOTAT, OVRIG, VARSEL, VEDTAK }
    enum class BrevSystem { DOKSYS, GAMMEL /*EXSTREAM*/ , }
    enum class BrevkontekstCode { ALLTID, SAK, VEDTAK }

    enum class DokumentType {
        I, //Inngende dokument
        N, //Notat
        U, //Utgende dokument
    }
}


enum class SpraakKode {
    EN, // Engelsk
    NB, // Bokmaal
    NN, // Nynorsk
    FR, // Fransk
    SE, // Nord-samisk
}

enum class BrevSystem { EXSTREAM, DOKSYS, BREVBAKER }

data class LetterMetadata(
    val name: String,
    val id: String,
    val brevsystem: BrevSystem,
    val spraak: List<SpraakKode>, // Enkelte brev er egentlig bare bokmål, men har null i metadata.
    val brevkategoriCode: BrevdataDto.BrevkategoriCode?,
    val dokumentkategoriCode: BrevdataDto.DokumentkategoriCode?,
)

