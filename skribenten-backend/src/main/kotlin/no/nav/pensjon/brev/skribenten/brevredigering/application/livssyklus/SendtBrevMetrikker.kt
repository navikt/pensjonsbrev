package no.nav.pensjon.brev.skribenten.brevredigering.application.livssyklus

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import io.ktor.callid.KtorCallIdContextElement
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import no.nav.pensjon.brev.skribenten.Metrics
import no.nav.pensjon.brev.skribenten.auth.currentPrincipalContext
import no.nav.pensjon.brev.skribenten.brevredigering.domain.Brevredigering
import no.nav.pensjon.brev.skribenten.brevredigering.domain.MottakerType
import no.nav.pensjon.brev.skribenten.model.Distribusjon
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.routes.samhandler.dto.HentSamhandlerResponseDto
import no.nav.pensjon.brev.skribenten.services.EnhetId
import no.nav.pensjon.brev.skribenten.services.SamhandlerService
import org.slf4j.LoggerFactory
import java.nio.ByteBuffer
import java.security.MessageDigest
import kotlin.coroutines.EmptyCoroutineContext

private val logger = LoggerFactory.getLogger(SendtBrevMetrikker::class.java)

class SendtBrevMetrikker(
    private val samhandlerService: SamhandlerService,
    private val registry: MeterRegistry = Metrics.registry,
    private val scope: CoroutineScope = CoroutineScope(
        SupervisorJob() + Dispatchers.IO + CoroutineExceptionHandler { _, e -> logger.warn("Feil ved telling av sendt brev", e) }
    ),
) {

    data class SendtBrevMaaling(
        val mottakerType: MottakerType?,
        val tssId: String?,
        val manueltAdressertTil: Dto.Mottaker.ManueltAdressertTil?,
        val distribusjonstype: Distribusjon,
        val avsenderEnhet: EnhetId,
    ) {
        constructor(brev: Brevredigering) : this(
            mottakerType = brev.mottaker?.type,
            tssId = brev.mottaker?.tssId,
            manueltAdressertTil = brev.mottaker?.manueltAdressertTil,
            distribusjonstype = brev.distribusjonstype,
            avsenderEnhet = brev.avsenderEnhetId,
        )
    }

    // Måledataene leses her, ikke i den asynkrone jobben: entiteten slettes rett etter sending.
    suspend fun tellSendtBrev(brev: Brevredigering): Job = tellSendtBrev(SendtBrevMaaling(brev))

    internal suspend fun tellSendtBrev(maaling: SendtBrevMaaling): Job {
        val kontekst = currentPrincipalContext() +
            (currentCoroutineContext()[KtorCallIdContextElement] ?: EmptyCoroutineContext)

        return scope.launch(kontekst) { tell(maaling) }
    }

    private suspend fun tell(maaling: SendtBrevMaaling) {
        val samhandler = maaling.tssId?.let { hentSamhandler(it) }

        Counter.builder(metricName)
            .description("Antall brev sendt fra Skribenten, fordelt på hvem brevet ble sendt til.")
            .tag("mottaker", maaling.mottakerType?.name ?: BRUKER)
            .tag("samhandler_type", samhandler?.map { it?.samhandlerType }?.getOrUkjent() ?: IKKE_RELEVANT)
            .tag("id_type", samhandler?.map { it?.idType()?.name }?.getOrUkjent() ?: IKKE_RELEVANT)
            .tag("adressert_til", maaling.manueltAdressertTil?.name ?: IKKE_RELEVANT)
            .tag("distribusjon", maaling.distribusjonstype.name)
            .tag("avsender_enhet", maaling.avsenderEnhet.value)
            .register(registry)
            .increment()

        val offentligId = samhandler?.getOrNull()?.takeIf { it.idType() == SamhandlerIdType.ORG }?.offentligId
        if (!offentligId.isNullOrBlank()) {
            tellOrganisasjon(offentligId)
        }
    }

    // Tom labelverdi leses som fraværende i Prometheus, og serien ville da falt ut av `sum by (samhandler_type)`.
    private fun Result<String?>.getOrUkjent(): String =
        this.map { it?.takeIf(String::isNotBlank) ?: UKJENT }.getOrDefault(UKJENT)

    private fun HentSamhandlerResponseDto.Success.idType(): SamhandlerIdType =
        when (idType.takeIf(String::isNotBlank)) {
            null -> SamhandlerIdType.UKJENT
            "FNR" -> SamhandlerIdType.FNR
            "ORG", "ORGNR" -> SamhandlerIdType.ORG
            else -> SamhandlerIdType.ANNEN
        }

    // Bøtta er et anslag på spredningen mellom organisasjoner, ikke en identifikator. Se sendtbrevmetrikk.adoc.
    private fun tellOrganisasjon(orgNr: String) {
        Counter.builder(orgMetricName)
            .description(
                "Brev sendt til samhandlere identifisert med organisasjonsnummer, fordelt på en hash-bøtte." +
                    " Bøtta er et anslag på spredningen mellom organisasjoner, ikke en identifikator."
            )
            .tag("org_bucket", orgBoette(orgNr).toString())
            .register(registry)
            .increment()
    }

    // Brevet er allerede sendt, så en feil her skal kun gi en mindre presis metrikk.
    private suspend fun hentSamhandler(tssId: String): Result<HentSamhandlerResponseDto.Success?> =
        runCatching { samhandlerService.hentSamhandler(tssId).success }
            .onFailure { e ->
                currentCoroutineContext().ensureActive()
                logger.warn("Klarte ikke å hente samhandler for metrikk", e)
            }

    // Navnene går rett ut som labelverdien id_type: omdøping brekker eksisterende spørringer og dashbord.
    enum class SamhandlerIdType { UKJENT, FNR, ORG, ANNEN }

    companion object {
        const val metricName = "skribenten_brev_sendt"
        const val orgMetricName = "skribenten_brev_sendt_samhandler_org"
        const val BRUKER = "BRUKER"
        const val IKKE_RELEVANT = "IKKE_RELEVANT"
        const val UKJENT = "UKJENT"

        // Frosset: endres tallet eller hashfunksjonen, flyttes alle organisasjonsnumre til nye bøtter og historikken blir verdiløs.
        const val ANTALL_ORG_BOETTER = 128

        // SHA-256 og ikke String.hashCode(): sistnevnte er svakt fordelt for numeriske strenger av lik lengde, altså nøyaktig det et orgnr er.
        internal fun orgBoette(orgNr: String): Int {
            val hash = MessageDigest.getInstance("SHA-256").digest(orgNr.toByteArray(Charsets.UTF_8))
            // ByteBuffer.getInt er fortegnsbesatt, og % på et negativt tall gir negativt bøttenummer.
            val usignert = ByteBuffer.wrap(hash).int.toLong() and 0xFFFFFFFFL
            return (usignert % ANTALL_ORG_BOETTER).toInt()
        }
    }
}
