package no.nav.pensjon.brev.converters

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.deser.std.StringDeserializer
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.node.IntNode
import com.fasterxml.jackson.databind.node.TextNode
import no.nav.pensjon.brevbaker.api.model.Days
import no.nav.pensjon.brevbaker.api.model.DaysWrapper
import no.nav.pensjon.brevbaker.api.model.IntWrapper
import no.nav.pensjon.brevbaker.api.model.Months
import no.nav.pensjon.brevbaker.api.model.MonthsWrapper
import no.nav.pensjon.brevbaker.api.model.Percent
import no.nav.pensjon.brevbaker.api.model.PercentWrapper
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
        addDeserializer(String::class.java, stringDeser())
    }

    private val standardStringDeserializer = StringDeserializer()

    // TODO: Denne kan vi fjerne når både Gjenny og Pesys bruker ny nok versjon av biblioteket, så dei sender
    // fødselsnummer, telefonnumer og kroner som flat tekst og ikkje som innpakka objekt
    private fun stringDeser() = object : StdDeserializer<String>(String::class.java) {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext, ): String? {
            try {
                return standardStringDeserializer.deserialize(p, ctxt)
            } catch (e: MismatchedInputException) {
                val node = p.codec.readTree<JsonNode>(p)
                return when (p.parsingContext.currentName) {
                    "telefonnummer" -> p.codec.treeToValue(node, Map::class.java)["value"].toString()
                    "foedselsnummer" -> p.codec.treeToValue(node, Map::class.java)["value"].toString()
                    "kroner" -> p.codec.treeToValue(node, Map::class.java)["value"].toString()
                    else -> throw e
                }
            }
        }
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

    private fun log(clazz: Class<*>) {
        logger.warn("Mottok $clazz i innpakka versjon")
    }
}