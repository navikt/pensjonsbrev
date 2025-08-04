package no.nav.pensjon.brev.pdfbygger

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.node.IntNode
import com.fasterxml.jackson.databind.node.TextNode
import no.nav.pensjon.brevbaker.api.model.Days
import no.nav.pensjon.brevbaker.api.model.DaysWrapper
import no.nav.pensjon.brevbaker.api.model.Foedselsnummer
import no.nav.pensjon.brevbaker.api.model.IntWrapper
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.Months
import no.nav.pensjon.brevbaker.api.model.MonthsWrapper
import no.nav.pensjon.brevbaker.api.model.Percent
import no.nav.pensjon.brevbaker.api.model.PercentWrapper
import no.nav.pensjon.brevbaker.api.model.Telefonnummer
import no.nav.pensjon.brevbaker.api.model.Year
import no.nav.pensjon.brevbaker.api.model.YearWrapper
import org.slf4j.LoggerFactory

// TODO: Vi bør kunne rydde bort denne igjen etter at pesys er oppdatert
object PrimitiveModule : SimpleModule() {
    private fun readResolve(): Any = PrimitiveModule

    private val logger = LoggerFactory.getLogger(this::class.java)

    init {
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
            when (val node = p.codec.readTree<JsonNode>(p)) {
                is IntNode -> Telefonnummer(p.codec.treeToValue(node, Int::class.java).toString())
                is TextNode -> Telefonnummer(p.codec.treeToValue(node, String::class.java))
//                else -> Telefonnummer(p.codec.treeToValue(node, TelefonnummerWrapper::class.java).value).also { log(
//                    TelefonnummerWrapper::class.java) }
//                else -> ctxt.findRootValueDeserializer(ctxt.constructType(Telefonnummer::class.java)).deserialize(p, ctxt) as Telefonnummer
                else -> Telefonnummer(p.codec.treeToValue(node, Map::class.java)["value"].toString())
            }
    }

    private fun foedselsnummerDeserializer() = object : StdDeserializer<Foedselsnummer>(Foedselsnummer::class.java) {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext) =
            when (val node = p.codec.readTree<JsonNode>(p)) {
                is IntNode -> Foedselsnummer(p.codec.treeToValue(node, Int::class.java).toString())
                is TextNode -> Foedselsnummer(p.codec.treeToValue(node, String::class.java))
                else -> Foedselsnummer(p.codec.treeToValue(node, Map::class.java)["value"].toString())
            }
    }

    private fun kronerDeserializer() = object : StdDeserializer<Kroner>(Kroner::class.java) {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext) =
            when (val node = p.codec.readTree<JsonNode>(p)) {
                is IntNode -> Kroner(p.codec.treeToValue(node, Int::class.java))
                is TextNode -> Kroner(p.codec.treeToValue(node, String::class.java).toInt())
                else -> Kroner(p.codec.treeToValue(node, Map::class.java)["value"].toString().toInt())
            }
    }

    private fun log(clazz: Class<*>) {
        logger.warn("Mottok $clazz i innpakka versjon")
    }
}