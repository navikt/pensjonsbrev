package no.nav.pensjon.brev.routing

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brev.api.model.BestillRedigertBrevRequest
import no.nav.pensjon.brev.api.model.BestillRedigertBrevRequestV2
import no.nav.pensjon.brev.api.model.maler.BestillBrevRequest
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupWithDataUsageImpl
import no.nav.pensjon.brevbaker.api.model.RedigerbareVedleggTitler
import org.slf4j.LoggerFactory
import java.lang.reflect.Modifier
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.memberProperties
import kotlin.time.measureTime
import no.nav.brev.brevbaker.markup.LetterMarkup as MarkupLetterMarkup
import no.nav.brev.brevbaker.markup.LetterMarkupWithDataUsage as MarkupLetterMarkupWithDataUsage

private val logger = LoggerFactory.getLogger("no.nav.pensjon.brev.routing.Oppvarming")

@OptIn(InterneDataklasser::class)
internal fun ObjectMapper.varmOppApiTyper() {
    val varighet = measureTime {
        listOf(
            jacksonTypeRef<BestillBrevRequest<Brevkode.Automatisk>>(),
            jacksonTypeRef<BestillBrevRequest<Brevkode.Redigerbart>>(),
            jacksonTypeRef<BestillRedigertBrevRequest<Brevkode.Redigerbart>>(),
            jacksonTypeRef<BestillRedigertBrevRequestV2<Brevkode.Redigerbart>>(),
        ).forEach { canDeserialize(typeFactory.constructType(it)) }

        konkreteKlasser(
            LetterMarkupImpl::class,
            LetterMarkupWithDataUsageImpl::class,
            RedigerbareVedleggTitler::class,
            MarkupLetterMarkup::class,
            MarkupLetterMarkupWithDataUsage::class,
        ).forEach { canSerialize(it) }

        listOf(
            ArrayList<Any>(), buildList { add(1) }, emptyList<Any>(), listOf(1), listOf(1, 2),
            LinkedHashSet<Any>(), buildSet { add(1) }, emptySet<Any>(), setOf(1), HashSet<Any>(),
            LinkedHashMap<Any, Any>(), buildMap { put(1, 1) }, emptyMap<Any, Any>(), mapOf(1 to 1), HashMap<Any, Any>(),
        ).forEach { canSerialize(it.javaClass) }
    }
    logger.info("Oppvarming av API-typer ferdig på ${varighet.inWholeMilliseconds} ms")
}

private fun konkreteKlasser(vararg rot: KClass<*>): Set<Class<*>> {
    val besokt = mutableSetOf<KClass<*>>()
    val ko = ArrayDeque(rot.toList())
    while (ko.isNotEmpty()) {
        val klasse = ko.removeFirst()
        if (!klasse.java.name.startsWith("no.nav.") || !besokt.add(klasse)) continue
        ko.addAll(klasse.nestedClasses)
        ko.addAll(klasse.sealedSubclasses)
        klasse.memberProperties.forEach { ko.addAll(klasserI(it.returnType)) }
    }
    return besokt.map { it.java }.filterTo(mutableSetOf()) { !it.isInterface && !Modifier.isAbstract(it.modifiers) }
}

private fun klasserI(type: KType): List<KClass<*>> =
    listOfNotNull(type.classifier as? KClass<*>) + type.arguments.mapNotNull { it.type }.flatMap { klasserI(it) }
