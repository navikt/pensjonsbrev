package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.samhandler

import com.typesafe.config.Config
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.samhandler.HentSamhandlerAdresseResponseDto.FailureType.GENERISK
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.samhandler.HentSamhandlerAdresseResponseDto.FailureType.NOT_FOUND
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.samhandler.HentSamhandlerAdresseResponseDto.SamhandlerPostadresse
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.soap.TjenestebussService
import no.nav.virksomhet.tjenester.samhandler.meldinger.v2.HentSamhandlerPrioritertAdresseRequest
import no.nav.virksomhet.tjenester.samhandler.v2.binding.HentSamhandlerPrioritertAdresseSamhandlerIkkeFunnet
import org.slf4j.LoggerFactory

class SamhandlerService(config: Config) : TjenestebussService() {
    private val samhandlerClient = SamhandlerClient(config, callIdHandler).client()
    private val logger = LoggerFactory.getLogger(SamhandlerService::class.java)
    fun hentSamhandlerPostadresse(tssEksternId: String): HentSamhandlerAdresseResponseDto =
        try {
            samhandlerClient.hentSamhandlerPrioritertAdresse(
                HentSamhandlerPrioritertAdresseRequest().apply {
                    this.ident = tssEksternId
                    this.identKode = "TSS_EKSTERN_ID"
                }
            )?.let {
                HentSamhandlerAdresseResponseDto(
                    SamhandlerPostadresse(
                        navn = it.navn.trim(),
                        linje1 = it.postadresse.adresselinje1?.trim(),
                        linje2 = it.postadresse.adresselinje2?.trim(),
                        linje3 = it.postadresse.adresselinje3?.trim(),
                        postnr = it.postadresse.postnr?.trim(),
                        poststed = it.postadresse.poststed?.trim(),
                        land = it.postadresse.land.kode?.trim(),
                    )
                )
            } ?: HentSamhandlerAdresseResponseDto(NOT_FOUND)
        } catch (nf: HentSamhandlerPrioritertAdresseSamhandlerIkkeFunnet) {
            HentSamhandlerAdresseResponseDto(NOT_FOUND)
        } catch (e: Exception) {
            logger.error("Feil ved henting av samhandler mot samhandlerv2. ${e.message}")
            HentSamhandlerAdresseResponseDto(GENERISK)
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