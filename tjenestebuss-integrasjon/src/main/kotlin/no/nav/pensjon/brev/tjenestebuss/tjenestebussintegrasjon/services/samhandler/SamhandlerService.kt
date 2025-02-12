package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.samhandler

import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.samhandler.HentSamhandlerAdresseResponseDto.FailureType.GENERISK
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.samhandler.HentSamhandlerAdresseResponseDto.FailureType.NOT_FOUND
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.samhandler.HentSamhandlerAdresseResponseDto.SamhandlerPostadresse
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.soap.TjenestebussService
import no.nav.virksomhet.tjenester.samhandler.meldinger.v2.HentSamhandlerPrioritertAdresseRequest
import no.nav.virksomhet.tjenester.samhandler.v2.binding.HentSamhandlerPrioritertAdresseSamhandlerIkkeFunnet
import no.nav.virksomhet.tjenester.samhandler.v2.binding.Samhandler
import org.slf4j.LoggerFactory

class SamhandlerService(clientFactory: SamhandlerClientFactory) : TjenestebussService<Samhandler>(clientFactory) {
    private val logger = LoggerFactory.getLogger(SamhandlerService::class.java)

    fun hentSamhandlerPostadresse(tssEksternId: String): HentSamhandlerAdresseResponseDto =
        try {
            client.hentSamhandlerPrioritertAdresse(
                HentSamhandlerPrioritertAdresseRequest().apply {
                    this.ident = tssEksternId
                    this.identKode = "TSS_EKSTERN_ID"
                },
            )?.let {
                it.postadresse?.let { adresse ->
                    HentSamhandlerAdresseResponseDto(
                        SamhandlerPostadresse(
                            navn = it.navn.trim(),
                            linje1 = adresse.adresselinje1?.trim(),
                            linje2 = adresse.adresselinje2?.trim(),
                            linje3 = adresse.adresselinje3?.trim(),
                            postnr = adresse.postnr?.trim(),
                            poststed = adresse.poststed?.trim(),
                            land = adresse.land?.kode?.trim(),
                        ),
                    )
                }
            } ?: HentSamhandlerAdresseResponseDto(NOT_FOUND)
        } catch (nf: HentSamhandlerPrioritertAdresseSamhandlerIkkeFunnet) {
            HentSamhandlerAdresseResponseDto(NOT_FOUND)
        } catch (e: Exception) {
            logger.error("Feil ved henting av samhandler mot samhandlerv2. ${e.message}")
            HentSamhandlerAdresseResponseDto(GENERISK)
        }

    override val name = "SamhandlerV2"

    override fun sendPing(): Boolean {
        hentSamhandlerPostadresse("123")
        return true
    }
}

data class HentSamhandlerAdresseResponseDto(
    val adresse: SamhandlerPostadresse?,
    val failureType: FailureType?,
) {
    constructor(adresse: SamhandlerPostadresse) : this(adresse, null)
    constructor(failureType: FailureType) : this(null, failureType)

    data class SamhandlerPostadresse(
        val navn: String,
        val linje1: String?,
        val linje2: String?,
        val linje3: String?,
        val postnr: String?,
        val poststed: String?,
        val land: String?,
    )

    enum class FailureType {
        NOT_FOUND,
        GENERISK,
    }
}
