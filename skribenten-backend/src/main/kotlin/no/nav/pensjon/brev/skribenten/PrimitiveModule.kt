package no.nav.pensjon.brev.skribenten

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.node.IntNode
import com.fasterxml.jackson.databind.node.TextNode
import no.nav.pensjon.brevbaker.api.model.Days
import no.nav.pensjon.brevbaker.api.model.DaysWrapper
import no.nav.pensjon.brevbaker.api.model.Months
import no.nav.pensjon.brevbaker.api.model.MonthsWrapper
import no.nav.pensjon.brevbaker.api.model.Percent
import no.nav.pensjon.brevbaker.api.model.PercentWrapper
import no.nav.pensjon.brevbaker.api.model.Year
import no.nav.pensjon.brevbaker.api.model.YearWrapper

// TODO: Vi bør kunne rydde bort denne igjen etter at pesys er oppdatert
object PrimitiveModule : SimpleModule() {
    private fun readResolve(): Any = PrimitiveModule

    init {
        addDeserializer(Year::class.java, yearDeserializer())
        addDeserializer(Months::class.java, monthsDeserializer())
        addDeserializer(Days::class.java, daysDeserializer())
        addDeserializer(Percent::class.java, percentDeserializer())
    }

    private fun yearDeserializer() = object : StdDeserializer<Year>(Year::class.java) {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext) =
            when (val node = p.codec.readTree<JsonNode>(p)) {
                is IntNode -> Year(p.codec.treeToValue(node, Int::class.java))
                is TextNode -> Year(p.codec.treeToValue(node, String::class.java).toInt())
                else -> Year(p.codec.treeToValue(node, YearWrapper::class.java).value)
            }
    }

    private fun monthsDeserializer() = object : StdDeserializer<Months>(Months::class.java) {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext) =
            when (val node = p.codec.readTree<JsonNode>(p)) {
                is IntNode -> Months(p.codec.treeToValue(node, Int::class.java))
                is TextNode -> Months(p.codec.treeToValue(node, String::class.java).toInt())
                else -> Months(p.codec.treeToValue(node, MonthsWrapper::class.java).value)
            }
    }

    private fun daysDeserializer() = object : StdDeserializer<Days>(Days::class.java) {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext) =
            when (val node = p.codec.readTree<JsonNode>(p)) {
                is IntNode -> Days(p.codec.treeToValue(node, Int::class.java))
                is TextNode -> Days(p.codec.treeToValue(node, String::class.java).toInt())
                else -> Days(p.codec.treeToValue(node, DaysWrapper::class.java).value)
            }
    }


    private fun percentDeserializer() = object : StdDeserializer<Percent>(Percent::class.java) {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext) =
            when (val node = p.codec.readTree<JsonNode>(p)) {
                is IntNode -> Percent(p.codec.treeToValue(node, Int::class.java))
                is TextNode -> Percent(p.codec.treeToValue(node, String::class.java).toInt())
                else -> Percent(p.codec.treeToValue(node, PercentWrapper::class.java).value)
            }
    }
}