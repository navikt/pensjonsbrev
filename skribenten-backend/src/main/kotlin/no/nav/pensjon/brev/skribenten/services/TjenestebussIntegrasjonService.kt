package no.nav.pensjon.brev.skribenten.services

import com.typesafe.config.Config
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import no.nav.pensjon.brev.skribenten.auth.AzureADOnBehalfOfAuthorizedHttpClient
import no.nav.pensjon.brev.skribenten.auth.AzureADService
import no.nav.pensjon.brev.skribenten.routes.OrderLetterRequest
import no.nav.pensjon.brev.skribenten.routes.getCurrentGregorianTime
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.*
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.FinnSamhandlerResponseDto.Success.Samhandler
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

    suspend fun bestillExtreamBrev(
        call: ApplicationCall,
        request: OrderLetterRequest,
        navIdent: String,
        metadata: BrevdataDto
    ): ServiceResult<BestillBrevResponseDto.Success, BestillBrevResponseDto.Failure> {

        //TODO better error handling.
        // TODO access controls for e-blanketter
        val isEblankett = metadata.dokumentkategori == BrevdataDto.DokumentkategoriCode.E_BLANKETT
        val bestillBrevRequestDto = BestillBrevRequestDto(
            brevKode = request.brevkode, // ID på brev
            brevGruppe = metadata.brevgruppe
                ?: throw BadRequestException("Fant ikke brevgruppe gitt extream brev :${request.brevkode}"),
            isRedigerbart = metadata.redigerbart,
            sprakkode = request.spraak.toString(),
            sakskontekstDto = SakskontekstDto(
                journalenhet = "0001",                              // NAV org enhet nr som skriver brevet. Kommer med i signatur.
                //    private String decideJournalEnhet(NAVEnhet enhetToSet, BrevmenyForm form) {
                //        if (form.getValgtAvsenderEnhet().equals(enhetToSet.getEnhetsId())) {
                //            return enhetToSet.getEnhetsId();
                //        } else {
                //            return form.getSak().getEierTilgang().getTilgangGittTil();
                //        }
                gjelder = request.gjelderPid,                       // Hvem gjelder brevet? Kan være ulik fra mottaker om det er verge.
                dokumenttype = metadata.dokType,                    // Inngående, utgående, notat
                dokumentdato = getCurrentGregorianTime(),           // nåværende dato. TODO Skal dette komme fra parameter kanskje?
                fagsystem = "PEN",                                  // TODO skal dette være noe annet enn PEN (pesys?)
                fagomradekode = "PEN",                              // Fagområde pensjon uansett hva det faktisk er. Finnes det UFO?
                innhold = metadata.dekode,                          // Visningsnavn
                kategori = metadata.dokumentkategori.toString(),    // Kategori for dokumentet
                kravtype = null, // TODO sett. Brukes for notater
                land = request.landkode.takeIf { isEblankett },
                mottaker = request.mottakerText.takeIf { isEblankett }, // Fritekst felt for SED/Eblankett mottaker til journalpost
                saksid = request.sakId.toString(),// sakid
                saksbehandlernavn = "saksbehandler saksbehandlerson", // TODO hent navn fra nav ansatt service? eller claim kanskje?
                saksbehandlerId = navIdent,
            )
        )

        return tjenestebussIntegrasjonClient.post(call, "/bestillbrev") {
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
