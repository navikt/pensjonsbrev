package no.nav.pensjon.brev.skribenten.services

import com.typesafe.config.Config
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import no.nav.pensjon.brev.skribenten.auth.AzureADOnBehalfOfAuthorizedHttpClient
import no.nav.pensjon.brev.skribenten.auth.AzureADService
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.BestillBrevRequestDto
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.BestillBrevResponseDto
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.FinnSamhandlerResponseDto
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.FinnSamhandlerResponseDto.Success.Samhandler
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.HentSamhandlerResponseDto
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.RedigerDokumentResponseDto
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.SamhandlerTypeCode
import javax.xml.datatype.XMLGregorianCalendar

class TjenestebussIntegrasjonService(config: Config, authService: AzureADService) {

    private val tjenestebussIntegrasjonUrl = config.getString("url")
    private val tjenestebussIntegrasjonScope = config.getString("scope")

    val tjenestebussIntegrasjonClient =
        AzureADOnBehalfOfAuthorizedHttpClient(tjenestebussIntegrasjonScope, authService) {
            defaultRequest {
                url(tjenestebussIntegrasjonUrl)
            }
            install(ContentNegotiation) {
                jackson()
            }
        }

    suspend fun finnSamhandler(
        call: ApplicationCall,
        samhandlerType: SamhandlerTypeCode,
        navn: String
    ): ServiceResult<FinnSamhandlerResponseDto.Success, FinnSamhandlerResponseDto.Failure> =
        tjenestebussIntegrasjonClient.post(call, "/finnSamhandler") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(
                Domain.FinnSamhandlerRequestDto(
                    navn = navn,
                    samhandlerType = Domain.SamhandlerTypeCode.valueOf(samhandlerType.name)
                )
            )
        }.toServiceResult<Domain.FinnSamhandlerResponseDto, Domain.FinnSamhandlerErrorResponse>()
            .map {
                FinnSamhandlerResponseDto.Success(samhandlere = it.samhandlere.map { s ->
                    Samhandler(
                        navn = s.navn,
                        samhandlerType = s.samhandlerType,
                        offentligId = s.offentligId,
                        idType = s.idType
                    )
                })
            }.catch { error -> FinnSamhandlerResponseDto.Failure(message = error.message, type = error.type) }

    suspend fun hentSamhandler(
        call: ApplicationCall,
        idTSSEkstern: String,
        hentDetaljert: Boolean,
    ): ServiceResult<HentSamhandlerResponseDto.Success, HentSamhandlerResponseDto.Failure> =
        tjenestebussIntegrasjonClient.post(call, "/hentSamhandler") {
            Domain.HentSamhandlerRequestDto(
                idTSSEkstern = idTSSEkstern,
                hentDetaljert = hentDetaljert
            )
        }.toServiceResult<Domain.HentSamhandlerResponseDto.Success, Domain.HentSamhandlerResponseDto.Failure>()
            .map {
                HentSamhandlerResponseDto.Success(
                    samhandler = HentSamhandlerResponseDto.Success.Samhandler(
                        navn = it.samhandler.navn,
                        samhandlerType = it.samhandler.samhandlerType,
                        offentligId = it.samhandler.offentligId,
                        idType = it.samhandler.idType
                    )
                )
            }.catch { error ->
                HentSamhandlerResponseDto.Failure(message = error.message, type = error.type)
            }

    suspend fun bestillBrev(
        call: ApplicationCall,
        bestillBrevRequestDto: BestillBrevRequestDto
    ): ServiceResult<BestillBrevResponseDto.Success, BestillBrevResponseDto.Failure> =
        tjenestebussIntegrasjonClient.post(call, "/bestillbrev") {
            Domain.BestillBrevRequestDto(
                brevKode = bestillBrevRequestDto.brevKode,
                brevGruppe = bestillBrevRequestDto.brevGruppe,
                isRedigerbart = bestillBrevRequestDto.isRedigerbart,
                sprakkode = bestillBrevRequestDto.sprakkode,
                sakskontekstDto = Domain.SakskontekstDto(
                    journalenhet = bestillBrevRequestDto.sakskontekstDto.journalenhet,
                    gjelder = bestillBrevRequestDto.sakskontekstDto.gjelder,
                    dokumenttype = bestillBrevRequestDto.sakskontekstDto.dokumenttype,
                    dokumentdato = bestillBrevRequestDto.sakskontekstDto.dokumentdato,
                    fagsystem = bestillBrevRequestDto.sakskontekstDto.fagsystem,
                    fagomradekode = bestillBrevRequestDto.sakskontekstDto.fagomradekode,
                    innhold = bestillBrevRequestDto.sakskontekstDto.innhold,
                    kategori = bestillBrevRequestDto.sakskontekstDto.kategori,
                    saksid = bestillBrevRequestDto.sakskontekstDto.saksid,
                    saksbehandlernavn = bestillBrevRequestDto.sakskontekstDto.saksbehandlernavn,
                    saksbehandlerId = bestillBrevRequestDto.sakskontekstDto.saksbehandlerId,
                    sensitivitet = bestillBrevRequestDto.sakskontekstDto.sensitivitet
                )
            )
        }.toServiceResult<Domain.BestillBrevResponseDto.Success, Domain.BestillBrevResponseDto.Failure>()
            .map {
                BestillBrevResponseDto.Success(
                    journalpostId = it.journalpostId
                )
            }.catch { error ->
                BestillBrevResponseDto.Failure(message = error.message, type = error.type)
            }

    suspend fun redigerDokument(
        call: ApplicationCall,
        journalpostId: String,
        dokumentId: String,
    ): ServiceResult<RedigerDokumentResponseDto.Success, RedigerDokumentResponseDto.Failure> =
        tjenestebussIntegrasjonClient.post(call, "/hentSamhandler") {
            Domain.RedigerDokumentRequestDto(journalpostId = journalpostId, dokumentId = dokumentId)
        }.toServiceResult<Domain.RedigerDokumentResponseDto.Success, Domain.RedigerDokumentResponseDto.Failure>()
            .map {
                RedigerDokumentResponseDto.Success(
                    metaforceURI = it.metaforceURI
                )
            }.catch { error ->
                RedigerDokumentResponseDto.Failure(message = error.message, type = error.type)
            }


    private object Domain {

        class RedigerDokumentRequestDto(
            val journalpostId: String,
            val dokumentId: String,
        )

        sealed class RedigerDokumentResponseDto {
            data class Success(val metaforceURI: String) : RedigerDokumentResponseDto()
            data class Failure(
                val message: String?,
                val type: String?,
            ) : RedigerDokumentResponseDto() {
            }
        }

        sealed class BestillBrevResponseDto {
            data class Success(val journalpostId: String) : BestillBrevResponseDto()
            data class Failure(
                val message: String,
                val type: String,
            ) : BestillBrevResponseDto()
        }

        data class BestillBrevRequestDto(
            val brevKode: String,
            val brevGruppe: String,
            val isRedigerbart: Boolean,
            val sprakkode: String,
            val sakskontekstDto: SakskontekstDto,
        )


        data class SakskontekstDto(
            val journalenhet: String,
            val gjelder: String,
            val dokumenttype: String,
            val dokumentdato: XMLGregorianCalendar,
            val fagsystem: String,
            val fagomradekode: String,
            val innhold: String,
            val kategori: String,
            val saksid: String,
            val saksbehandlernavn: String,
            val saksbehandlerId: String,
            val sensitivitet: String
        )

        sealed class HentSamhandlerResponseDto {
            data class Success(val samhandler: Samhandler) {
                data class Samhandler(
                    val navn: String,
                    val samhandlerType: String,
                    val offentligId: String,
                    val idType: String
                ) : HentSamhandlerResponseDto()
            }

            data class Failure(
                val message: String,
                val type: String,
            ) : HentSamhandlerResponseDto()
        }

        class HentSamhandlerRequestDto(
            val idTSSEkstern: String,
            val hentDetaljert: Boolean,
        )

        data class FinnSamhandlerResponseDto(val samhandlere: List<Samhandler>) {
            data class Samhandler(
                val navn: String,
                val samhandlerType: String,
                val offentligId: String,
                val idType: String
            )
        }

        data class FinnSamhandlerErrorResponse(
            val message: String,
            val type: String,
        )

        class FinnSamhandlerRequestDto(
            val navn: String,
            val samhandlerType: SamhandlerTypeCode,
        )


        enum class SamhandlerTypeCode {
            AA,    // Ambulansearbeider
            ADVO,  // Advokat / Jurist
            AFPO,  // AFP-ordning
            AK,    // Arbeidskontor
            AMI,   // Arbeidsmarkedsinstitutt
            AN,    // Ankenemnda for sykepenger
            AP,    // Audiopedagog
            APOP,  // Apotek - privat
            APOS,  // Apotek - sykehus
            ART,   // Arbeidstilsynet
            ASEN,  // Arbeidslivssenter
            AT,    // Apotektekniker
            ATU,   // Attfringsutvalg
            AU,    // Audiograf
            BAND,  // Bandasjist
            BB,    // Begravelsesbyr
            BBE,   // Bobestyrer
            BEH,   // Behandler
            BF,    // Bidragsfogden
            BH,    // Barnehage
            BHT,   // Bedriftshelsetjeneste
            BI,    // Bioingenir
            BU,    // Bilutvalget
            EOS,   // ES-Land
            ET,    // Ergoterapeut
            FA,    // Farmasyt
            FB,    // Fiskehelsebiolog
            FFU,   // Folketrygdkontoret for utenlandssaker
            FKL,   // Fellesordningen LO/NHO
            FL,    // Fylkeslegen
            FO,    // Fotterapeut
            FS,    // Forsikringsselskap
            FT,    // Fysioterapeut
            FTK,   // Fylkestrygdekontor
            FULL,  // Fullmektig
            GVS,   // Grunn- og videregende skole
            HE,    // Helsesekretr
            HF,    // Helsefagarbeider
            HFO,   // Helseforetak
            HJS,   // Hjemmesykepleien
            HMS,   // Hjelpemiddelsentral
            HP,    // Hjelpepleier
            HPRD,  // HPR-dummy
            HS,    // Helsesster
            HSTA,  // Helsestasjon
            HSV,   // Hovedstevnevitnet
            HTF,   // Helsetjeneste forvaltning
            HU,    // Hyskole/universitet
            INST,  // Institusjon
            IS,    // Innkrevingssentralen
            JO,    // Jordmor
            KA,    // Klage- og Ankeenhet
            KE,    // Klinisk ernringsfysiolog
            KEMN,  // Kemner
            KI,    // Kiropraktor
            KOMM,  // Kommune
            KON,   // Konvensjonsland
            KRED,  // Kreditor
            KRU,   // Klageinstans reiseutgift
            KYND,  // Sakkyndig
            LABO,  // Laboratorium - offentlig
            LABP,  // Laboratorium - privat
            LARO,  // Lab/rntgen - privat
            LBS,   // Landsdekkende bilsenter
            LE,    // Lege
            LEBE,  // Bedriftslege
            LEFA,  // Fastlege
            LEKO,  // Kommunelege
            LERA,  // Rdgivende lege
            LFK,   // Legemiddel fagkontor
            LHK,   // Leverandr helsetrygdkort
            LK,    // Ligningskontor
            LM,    // Leverandr Motorkjrety
            LOGO,  // Logoped
            LRB,   // Lsreregister Brnnysund
            LS,    // Legesenter
            MT,    // Manuellterapeut
            NB,    // Namsmannen
            OA,    // Omsorgsarbeider
            OI,    // Ortopediingenir
            OM,    // Ombygger bil
            OP,    // Optiker
            OR,    // Ortoptist
            ORVE,  // Ortopedisk verksted
            PE,    // Perfusjonist
            PF,    // Provisorfarmasyt
            POLI,  // Poliklinikk
            PPT,   // Psykologisk pedagogisk tjeneste
            PS,    // Psykolog
            RA,    // Radiograf
            REGO,  // Regional oppgjrsenhet
            RF,    // Reseptarfarmasyt
            RFK,   // Rogaland fylkestrygdekontor
            RHFO,  // Regionalt helseforetak
            RKK,   // Regionklagekontor
            ROL,   // Rdgivende overlege
            RONO,  // Rntgeninstitutt - offentlig
            RONP,  // Rntgeninstitutt - privat
            RT,    // Rdgivende tannlege
            RTK,   // Regionstrygdekontor
            RTV,   // Rikstrygdeverket
            SAD,   // Samleadressat
            SFK,   // Sentralfylkestrygdekontor
            SK,    // Sosialkontor
            SP,    // Sykepleier
            SR,    // Skifteretten
            STKO,  // Stttekontakt
            SUD,   // Statens utlendingsdirektorat
            SYKH,  // Sykehus
            TEPE,  // Tjenestepensjonsordningar
            TH,    // Tannhelsesekretr
            TI,    // Takseringsinstanser
            TK,    // Trygdekontor
            TL,    // Tannlege
            TLO,   // Tannlege offentlig
            TOLK,  // Tolk
            TP,    // Tannpleier
            TR,    // Trygderetten
            TRAN,  // Transportr
            TT,    // Tanntekniker
            UTL,   // Utenlandske samhandler
            VE,    // Veterinr
            VIRT,  // Virtuelt trygdekontor
            VP,    // Vernepleier
            X,     // Diverse
            YH,    // Yrkeshygieniker
            YM,    // Yrkesmedisiner
        }
    }
}
