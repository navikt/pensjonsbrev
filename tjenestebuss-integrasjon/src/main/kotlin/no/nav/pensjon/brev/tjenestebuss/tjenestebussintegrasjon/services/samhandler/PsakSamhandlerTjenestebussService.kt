package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.samhandler

import no.nav.inf.psak.samhandler.FinnSamhandlerFaultPenGeneriskMsg
import no.nav.inf.psak.samhandler.HentSamhandlerFaultPenGeneriskMsg
import no.nav.inf.psak.samhandler.HentSamhandlerFaultPenSamhandlerIkkeFunnetMsg
import no.nav.inf.psak.samhandler.PSAKSamhandler
import no.nav.lib.pen.psakpselv.asbo.samhandler.ASBOPenFinnSamhandlerRequest
import no.nav.lib.pen.psakpselv.asbo.samhandler.ASBOPenHentSamhandlerRequest
import no.nav.lib.pen.psakpselv.asbo.samhandler.ASBOPenSamhandler
import no.nav.lib.pen.psakpselv.fault.FaultPenBase
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.*
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.samhandler.dto.*
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.soap.TjenestebussService
import org.slf4j.LoggerFactory

class PsakSamhandlerTjenestebussService(clientFactory: PsakSamhandlerClientFactory) :
    TjenestebussService<PSAKSamhandler>(clientFactory) {
    private val logger = LoggerFactory.getLogger(this::class.java)


    fun hentSamhandler(requestDto: HentSamhandlerRequestDto): HentSamhandlerResponseDto {
        try {
            val response = client.hentSamhandler(ASBOPenHentSamhandlerRequest().apply {
                idTSSEkstern = requestDto.idTSSEkstern
                hentDetaljert = requestDto.hentDetaljert
            })
            logger.info("Henter samhandler for TSS id: ${requestDto.idTSSEkstern}")
            return HentSamhandlerResponseDto(
                HentSamhandlerResponseDto.Success(
                    navn = response.navn,
                    samhandlerType = response.samhandlerType,
                    offentligId = response.offentligId,
                    idType = response.idType
                )
            )
        } catch (ex: HentSamhandlerFaultPenGeneriskMsg) {
            logger.error(
                "En feil oppstod under henting av samhandler med TSS id: ${requestDto.idTSSEkstern} ",
                ex.faultInfo.prettyPrint()
            )
            return HentSamhandlerResponseDto(HentSamhandlerResponseDto.FailureType.GENERISK)
        } catch (ex: HentSamhandlerFaultPenSamhandlerIkkeFunnetMsg) {
            ex.faultInfo.prettyPrint()
            logger.error(
                "Kunne ikke finne samhandler med TSS id: ${requestDto.idTSSEkstern}",
                ex.faultInfo.prettyPrint()
            )
            return HentSamhandlerResponseDto(HentSamhandlerResponseDto.FailureType.IKKE_FUNNET)
        }
    }

    fun finnSamhandler(requestDto: FinnSamhandlerRequestDto): FinnSamhandlerResponseDto {
        if (requestDto.direkteOppslag != null) {
            return finnSamhandlerVedDirekteOppslag(requestDto.samhandlerType, requestDto.direkteOppslag)
        }

        if (requestDto.organisasjonsnavn != null) {
            return finnSamhandlerVedOrganisasjonsnavn(requestDto.samhandlerType, requestDto.organisasjonsnavn)
        }

        if (requestDto.personnavn != null) {
            return finnSamhandlerVedPersonnavn(requestDto.samhandlerType, requestDto.personnavn)
        }

        //request klassen validerer at kun 1 eksisterer
        throw IllegalArgumentException("Forventet at en av direkteOppslag, organisasjonsnavn eller personnavn er satt")
    }


    private fun finnSamhandlerVedDirekteOppslag(
        samhandlerType: SamhandlerTypeCode,
        direkteOppslag: DirekteOppslag,
    ): FinnSamhandlerResponseDto {
        try {
            val samhandlerResponse = client.finnSamhandler(ASBOPenFinnSamhandlerRequest().apply {
                this.offentligId = direkteOppslag.id
                this.idType = direkteOppslag.identtype.name
                this.samhandlerType = samhandlerType.name
            })
            return FinnSamhandlerResponseDto(samhandlere = samhandlerResponse.samhandlere.flatMap { samhandler ->
                samhandler.toSamhandler()
            }.distinctBy { it.idTSSEkstern })
        } catch (ex: FinnSamhandlerFaultPenGeneriskMsg) {
            logger.error(
                "En feil oppstod under kall til finnSamhandler(direkte oppslag) med id: ${direkteOppslag.id}, identtype: ${direkteOppslag.identtype}, samhandlerType: $samhandlerType",
                ex.faultInfo.prettyPrint()
            )
            return FinnSamhandlerResponseDto("Feil ved henting av samhandler")
        }
    }


    private fun finnSamhandlerVedOrganisasjonsnavn(
        samhandlerType: SamhandlerTypeCode,
        organisasjonsnavn: Organisasjonsnavn,
    ): FinnSamhandlerResponseDto {
        try {
            val samhandlerResponse = client.finnSamhandler(ASBOPenFinnSamhandlerRequest().apply {
                this.navn = organisasjonsnavn.navn
                this.samhandlerType = samhandlerType.name
            })
            return FinnSamhandlerResponseDto(
                samhandlere = samhandlerResponse.samhandlere
                    .filtrerPåInnlandUtland(organisasjonsnavn.innlandUtland)
                    .flatMap { samhandler -> samhandler.toSamhandler() }
                    .distinctBy { it.idTSSEkstern }
            )
        } catch (ex: FinnSamhandlerFaultPenGeneriskMsg) {
            logger.error(
                "En feil oppstod under kall til finnSamhandler(organisasjonsnavn) med navn: ${organisasjonsnavn.navn}, innUtland: ${organisasjonsnavn.innlandUtland}, samhandlerType: $samhandlerType",
                ex.faultInfo.prettyPrint()
            )
            return FinnSamhandlerResponseDto("Feil ved henting av samhandler")
        }
    }

    private fun finnSamhandlerVedPersonnavn(
        samhandlerType: SamhandlerTypeCode,
        personnavn: Personnavn
    ): FinnSamhandlerResponseDto {
        try {
            val samhandlerResponse = client.finnSamhandler(ASBOPenFinnSamhandlerRequest().apply {
                //TODO: sjekk om det er riktig å sette navn på denne måten - selv pesys har en fornavn/etternavn split, men frontenden deres sender kanskje requesten som 1 string
                this.navn = "${personnavn.fornavn} ${personnavn.etternavn}"
                this.samhandlerType = samhandlerType.name
            })
            return FinnSamhandlerResponseDto(samhandlere = samhandlerResponse.samhandlere.flatMap { samhandler ->
                samhandler.toSamhandler()
            }.distinctBy { it.idTSSEkstern }
                .filter {
                    it.idType == Identtype.FNR
                }
            )
        } catch (ex: FinnSamhandlerFaultPenGeneriskMsg) {
            logger.error(
                "En feil oppstod under kall til finnSamhandler(personnavn) med navn: samhandlerType: $samhandlerType",
                ex.faultInfo.prettyPrint()
            )

            return FinnSamhandlerResponseDto("Feil ved henting av samhandler")
        }
    }


    override fun sendPing(): Boolean {
        hentSamhandler(HentSamhandlerRequestDto("123", false))
        return true
    }

    override val name = "PsakSamhandlerTjenestebuss"

}

private fun FaultPenBase.prettyPrint() =
    """
    | errorSource: $errorSource
    | errorType: $errorType
    | rootCause: $rootCause
    | errorMessage: $errorMessage
    """.trimMargin()

private fun Array<ASBOPenSamhandler>.filtrerPåInnlandUtland(landFilter: InnlandUtland): List<ASBOPenSamhandler> =
    this.filter {
        it.avdelinger.any { avdeling ->
            when (landFilter) {
                //TODO - sjekk hvordan vi burde håndtere hvis samhandler ikke er registert i norge eller utlandet
                InnlandUtland.INNLAND -> avdeling.uAdresse.land == "NOR" || avdeling.aAdresse.land == "NOR" || (avdeling.uAdresse.land == null && avdeling.aAdresse.land == null)
                //TODO - sjekk hvordan vi burde håndtere hvis samhandler ikke er registert i norge eller utlandet
                InnlandUtland.UTLAND -> avdeling.uAdresse.land != "NOR" || avdeling.aAdresse.land != "NOR" || (avdeling.uAdresse.land == null && avdeling.aAdresse.land == null)
                InnlandUtland.ALLE -> true
            }
        }
    }

private fun ASBOPenSamhandler.toSamhandler(): List<FinnSamhandlerResponseDto.Samhandler> =
    this.avdelinger.map { avdeling ->
        FinnSamhandlerResponseDto.Samhandler(
            navn = avdeling.avdelingNavn.takeIf { !it.isNullOrBlank() } ?: this.navn,
            samhandlerType = this.samhandlerType,
            offentligId = this.offentligId,
            idType = Identtype.valueOf(this.idType),
            idTSSEkstern = avdeling.idTSSEkstern,
        )
    }



