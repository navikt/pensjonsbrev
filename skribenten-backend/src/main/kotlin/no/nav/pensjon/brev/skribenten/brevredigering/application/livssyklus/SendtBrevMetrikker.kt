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
import no.nav.pensjon.brev.skribenten.services.EnhetId
import no.nav.pensjon.brev.skribenten.services.SamhandlerService
import org.slf4j.LoggerFactory
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
        Counter.builder(metricName)
            .description("Antall brev sendt fra Skribenten, fordelt på hvem brevet ble sendt til.")
            .tag("mottaker", maaling.mottakerType?.name ?: BRUKER)
            .tag("samhandler_type", samhandlerTypeTag(maaling))
            .tag("adressert_til", maaling.manueltAdressertTil?.name ?: IKKE_RELEVANT)
            .tag("distribusjon", maaling.distribusjonstype.name)
            .tag("avsender_enhet", maaling.avsenderEnhet.value)
            .register(registry)
            .increment()
    }

    private suspend fun samhandlerTypeTag(maaling: SendtBrevMaaling): String =
        if (maaling.mottakerType == MottakerType.SAMHANDLER) samhandlerType(maaling.tssId) ?: UKJENT else IKKE_RELEVANT

    // Brevet er allerede sendt, så en feil her skal kun gi en mindre presis metrikk.
    private suspend fun samhandlerType(tssId: String?): String? =
        tssId?.let {
            try {
                samhandlerService.hentSamhandlerType(it)
            } catch (e: Exception) {
                currentCoroutineContext().ensureActive()
                logger.warn("Klarte ikke å hente samhandlertype for metrikk, teller som $UKJENT", e)
                null
            }
        }

    companion object {
        const val metricName = "skribenten_brev_sendt"
        const val BRUKER = "BRUKER"
        const val IKKE_RELEVANT = "IKKE_RELEVANT"
        const val UKJENT = "UKJENT"
    }
}
