package no.nav.pensjon.brev.routing

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStarted
import io.ktor.util.AttributeKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import no.nav.brev.InterneDataklasser
import no.nav.brev.brevbaker.markup.LetterMarkup
import no.nav.brev.brevbaker.markup.LetterMarkupWithDataUsage
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupWithDataUsageImpl
import org.slf4j.LoggerFactory
import kotlin.time.TimeSource

@OptIn(InterneDataklasser::class)
internal class Oppvarming(private val jsonMapper: ObjectMapper) {
    private val logger = LoggerFactory.getLogger(Oppvarming::class.java)

    @Volatile
    var ferdig: Boolean = false
        private set

    internal val feiledeSteg: MutableSet<String> = mutableSetOf()

    fun varmOpp() {
        val start = TimeSource.Monotonic.markNow()
        try {
            maal("brevdata") { jsonMapper.readValue<BrevbakerBrevdata>("{}") }
            maal("markup") {
                listOf(
                    LetterMarkupImpl::class.java,
                    LetterMarkupWithDataUsageImpl::class.java,
                    LetterMarkup::class.java,
                    LetterMarkupWithDataUsage::class.java,
                ).forEach {
                    jsonMapper.readerFor(it)
                    jsonMapper.writerFor(it)
                }
            }
            logger.info("Oppvarming ferdig på ${start.elapsedNow().inWholeMilliseconds} ms")
        } finally {
            ferdig = true
        }
    }

    private fun maal(navn: String, steg: () -> Unit) {
        val start = TimeSource.Monotonic.markNow()
        try {
            steg()
            logger.info("Oppvarming av $navn ferdig på ${start.elapsedNow().inWholeMilliseconds} ms")
        } catch (e: Exception) {
            feiledeSteg.add(navn)
            logger.warn("Oppvarming av $navn feilet etter ${start.elapsedNow().inWholeMilliseconds} ms", e)
        }
    }
}

internal val OppvarmingKey = AttributeKey<Oppvarming>("Oppvarming")

internal fun Application.startOppvarming(jsonMapper: ObjectMapper) {
    val oppvarming = Oppvarming(jsonMapper)
    attributes.put(OppvarmingKey, oppvarming)
    monitor.subscribe(ApplicationStarted) { it.launch(Dispatchers.Default) { oppvarming.varmOpp() } }
}
