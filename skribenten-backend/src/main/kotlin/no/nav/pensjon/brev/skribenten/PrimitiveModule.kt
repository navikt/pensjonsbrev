package no.nav.pensjon.brev.skribenten

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.node.IntNode
import com.fasterxml.jackson.databind.node.TextNode
import no.nav.pensjon.brevbaker.api.model.Bruker
import no.nav.pensjon.brevbaker.api.model.Days
import no.nav.pensjon.brevbaker.api.model.DaysWrapper
import no.nav.pensjon.brevbaker.api.model.Foedselsnummer
import no.nav.pensjon.brevbaker.api.model.FoedselsnummerWrapper
import no.nav.pensjon.brevbaker.api.model.IntWrapper
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.KronerWrapper
import no.nav.pensjon.brevbaker.api.model.Months
import no.nav.pensjon.brevbaker.api.model.MonthsWrapper
import no.nav.pensjon.brevbaker.api.model.NavEnhet
import no.nav.pensjon.brevbaker.api.model.Percent
import no.nav.pensjon.brevbaker.api.model.PercentWrapper
import no.nav.pensjon.brevbaker.api.model.Telefonnummer
import no.nav.pensjon.brevbaker.api.model.TelefonnummerWrapper
import no.nav.pensjon.brevbaker.api.model.Year
import no.nav.pensjon.brevbaker.api.model.YearWrapper
import org.slf4j.LoggerFactory

// TODO: Vi bør kunne rydde bort denne igjen etter at pesys er oppdatert
object PrimitiveModule : SimpleModule() {
    private fun readResolve(): Any = PrimitiveModule

    private val logger = LoggerFactory.getLogger(this::class.java)

    init {
        addDeserializer(NavEnhet::class.java, navEnhetDeserializer())
        addDeserializer(Bruker::class.java, brukerDeserializer())
        addDeserializer(Year::class.java, yearDeserializer())
        addDeserializer(Months::class.java, monthsDeserializer())
        addDeserializer(Days::class.java, daysDeserializer())
        addDeserializer(Percent::class.java, percentDeserializer())
        addDeserializer(Telefonnummer::class.java, telefonnummerDeserializer())
        addDeserializer(Foedselsnummer::class.java, foedselsnummerDeserializer())
        addDeserializer(Kroner::class.java, kronerDeserializer())
    }

    private inline fun <reified T: IntWrapper> unwrap(p: JsonParser, node: JsonNode?): Int = p.codec.treeToValue(node, T::class.java).value
        .also { log(T::class.java) }

    private fun yearDeserializer() = object : StdDeserializer<Year>(Year::class.java) {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext) =
            when (val node = p.codec.readTree<JsonNode>(p)) {
                is IntNode -> Year(p.codec.treeToValue(node, Int::class.java))
                is TextNode -> Year(p.codec.treeToValue(node, String::class.java).toInt())
                else -> Year(unwrap<YearWrapper>(p, node))
            }
    }

    private fun monthsDeserializer() = object : StdDeserializer<Months>(Months::class.java) {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext) =
            when (val node = p.codec.readTree<JsonNode>(p)) {
                is IntNode -> Months(p.codec.treeToValue(node, Int::class.java))
                is TextNode -> Months(p.codec.treeToValue(node, String::class.java).toInt())
                else -> Months(unwrap<MonthsWrapper>(p, node))
            }
    }

    private fun daysDeserializer() = object : StdDeserializer<Days>(Days::class.java) {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext) =
            when (val node = p.codec.readTree<JsonNode>(p)) {
                is IntNode -> Days(p.codec.treeToValue(node, Int::class.java))
                is TextNode -> Days(p.codec.treeToValue(node, String::class.java).toInt())
                else -> Days(unwrap<DaysWrapper>(p, node))
            }
    }


    private fun percentDeserializer() = object : StdDeserializer<Percent>(Percent::class.java) {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext) =
            when (val node = p.codec.readTree<JsonNode>(p)) {
                is IntNode -> Percent(p.codec.treeToValue(node, Int::class.java))
                is TextNode -> Percent(p.codec.treeToValue(node, String::class.java).toInt())
                else -> Percent(unwrap<PercentWrapper>(p, node))
            }
    }

    private fun telefonnummerDeserializer() = object : StdDeserializer<Telefonnummer>(Telefonnummer::class.java) {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext) =
            tolkTelefonnummer(p, p.codec.readTree(p))
    }

    private fun tolkTelefonnummer(p: JsonParser, node: JsonNode?) =
        when (node) {
            is IntNode -> Telefonnummer(p.codec.treeToValue(node, Int::class.java).toString())
            is TextNode -> Telefonnummer(p.codec.treeToValue(node, String::class.java))
            else -> Telefonnummer(p.codec.treeToValue(node, TelefonnummerWrapper::class.java).value).also {
                log(
                    TelefonnummerWrapper::class.java
                )
            }
        }

    private fun navEnhetDeserializer() = object : StdDeserializer<NavEnhet>(NavEnhet::class.java) {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext): NavEnhet? {
            val tree = p.codec.readTree<JsonNode>(p)
            return NavEnhet(
                nettside = tree["nettside"].asText(),
                navn = tree["navn"].asText(),
                telefonnummer = tolkTelefonnummer(p, tree["telefonnummer"])
            )
        }
    }

    private fun brukerDeserializer() = object : StdDeserializer<Bruker>(Bruker::class.java) {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Bruker? {
            val tree = p.codec.readTree<JsonNode>(p)
            return Bruker(
                foedselsnummer = tolkFoedselsnummer(p, tree["foedselsnummer"]),
                fornavn = tree["fornavn"].asText(),
                mellomnavn = tree["mellomnavn"]?.takeIf { !it.isNull }?.asText(),
                etternavn = tree["etternavn"].asText(),
            )
        }
    }

    private fun foedselsnummerDeserializer() = object : StdDeserializer<Foedselsnummer>(Foedselsnummer::class.java) {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext) = tolkFoedselsnummer(p, p.codec.readTree(p))
    }

    private fun tolkFoedselsnummer(p: JsonParser, readTree: JsonNode?): Foedselsnummer = when (val node = readTree) {
        is IntNode -> Foedselsnummer(p.codec.treeToValue(node, Int::class.java).toString())
        is TextNode -> Foedselsnummer(p.codec.treeToValue(node, String::class.java))
        else -> Foedselsnummer(p.codec.treeToValue(node, FoedselsnummerWrapper::class.java).value).also {
            log(
                FoedselsnummerWrapper::class.java
            )
        }
    }

    private fun kronerDeserializer() = object : StdDeserializer<Kroner>(Kroner::class.java) {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext) =
            when (val node = p.codec.readTree<JsonNode>(p)) {
                is IntNode -> Kroner(p.codec.treeToValue(node, Int::class.java))
                is TextNode -> Kroner(p.codec.treeToValue(node, String::class.java).toInt())
                else -> Kroner(unwrap<KronerWrapper>(p, node))
            }
    }

    private fun log(clazz: Class<*>) {
        logger.warn("Mottok $clazz i innpakka versjon")
    }
}