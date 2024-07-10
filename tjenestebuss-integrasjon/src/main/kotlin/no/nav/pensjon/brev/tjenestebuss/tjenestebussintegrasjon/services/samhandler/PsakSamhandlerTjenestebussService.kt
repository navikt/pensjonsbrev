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
        return when(requestDto){
            is FinnSamhandlerRequestDto.DirekteOppslag -> finnSamhandlerVedDirekteOppslag(requestDto)
            is FinnSamhandlerRequestDto.Organisasjonsnavn -> finnSamhandlerVedOrganisasjonsnavn(requestDto)
            is FinnSamhandlerRequestDto.Personnavn -> finnSamhandlerVedPersonnavn(requestDto)
        }
    }


    private fun finnSamhandlerVedDirekteOppslag(
        oppslag: FinnSamhandlerRequestDto.DirekteOppslag
    ): FinnSamhandlerResponseDto {
        try {
            val samhandlerResponse = client.finnSamhandler(ASBOPenFinnSamhandlerRequest().apply {
                this.offentligId = oppslag.id
                this.idType = oppslag.identtype.name
                this.samhandlerType = oppslag.samhandlerType.name
            })
            return FinnSamhandlerResponseDto(samhandlere = samhandlerResponse.samhandlere.flatMap { samhandler ->
                samhandler.toSamhandler()
            }.distinctBy { it.idTSSEkstern })
        } catch (ex: FinnSamhandlerFaultPenGeneriskMsg) {
            logger.error(
                "En feil oppstod under kall til finnSamhandler(direkte oppslag) med id: ${oppslag.id}, identtype: ${oppslag.identtype}, samhandlerType: ${oppslag.samhandlerType}",
                ex.faultInfo.prettyPrint()
            )
            return FinnSamhandlerResponseDto("Feil ved henting av samhandler")
        }
    }


    private fun finnSamhandlerVedOrganisasjonsnavn(
        oppslag: FinnSamhandlerRequestDto.Organisasjonsnavn
    ): FinnSamhandlerResponseDto {
        try {
            val samhandlerResponse = client.finnSamhandler(ASBOPenFinnSamhandlerRequest().apply {
                this.navn = oppslag.navn
                this.samhandlerType = oppslag.samhandlerType.name
            })
            return FinnSamhandlerResponseDto(
                samhandlere = samhandlerResponse.samhandlere
                    .filtrerPåInnlandUtland(oppslag.innlandUtland)
                    .flatMap { samhandler -> samhandler.toSamhandler() }
                    .distinctBy { it.idTSSEkstern }
            )
        } catch (ex: FinnSamhandlerFaultPenGeneriskMsg) {
            logger.error(
                "En feil oppstod under kall til finnSamhandler(organisasjonsnavn) med navn: ${oppslag.navn}, innlandUtland: ${oppslag.innlandUtland}, samhandlerType: ${oppslag.samhandlerType}",
                ex.faultInfo.prettyPrint()
            )
            return FinnSamhandlerResponseDto("Feil ved henting av samhandler")
        }
    }

    private fun finnSamhandlerVedPersonnavn(
        oppslag: FinnSamhandlerRequestDto.Personnavn
    ): FinnSamhandlerResponseDto {
        try {
            val samhandlerResponse = client.finnSamhandler(ASBOPenFinnSamhandlerRequest().apply {
                //etter en del testing og feiling, ser det ut som at dette er formatet som er forventet..
                this.navn = "${oppslag.etternavn} ${oppslag.fornavn}"
                this.samhandlerType = oppslag.samhandlerType.name
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
                "En feil oppstod under kall til finnSamhandler(personnavn), med navn: ${oppslag.fornavn} ${oppslag.etternavn}, samhandlerType: ${oppslag.samhandlerType}",
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
            //logikken skal gjenspeile det som er i pesys
            //https://github.com/navikt/pesys/blob/main/pen-app/src/main/java/no/nav/pensjon/pen_app/psak2/samhandler/sokesamhandler/SokeSamhandlerController.kt#L42
            when (landFilter) {
                InnlandUtland.INNLAND -> avdeling.uAdresse.land == "NOR" || avdeling.aAdresse.land == "NOR" || (avdeling.uAdresse.land == null && avdeling.aAdresse.land == null)
                InnlandUtland.UTLAND -> avdeling.uAdresse.land != "NOR" || avdeling.aAdresse.land != "NOR"
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



