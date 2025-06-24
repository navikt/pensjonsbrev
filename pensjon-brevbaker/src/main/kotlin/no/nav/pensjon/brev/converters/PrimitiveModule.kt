package no.nav.pensjon.brev.converters

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
import no.nav.pensjon.brevbaker.api.model.FoedselsnummerWrapper
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.KronerWrapper
import no.nav.pensjon.brevbaker.api.model.Months
import no.nav.pensjon.brevbaker.api.model.MonthsWrapper
import no.nav.pensjon.brevbaker.api.model.Percent
import no.nav.pensjon.brevbaker.api.model.PercentWrapper
import no.nav.pensjon.brevbaker.api.model.Telefonnummer
import no.nav.pensjon.brevbaker.api.model.TelefonnummerWrapper
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
        addDeserializer(Telefonnummer::class.java, telefonnummerDeserializer())
        addDeserializer(Foedselsnummer::class.java, foedselsnummerDeserializer())
        addDeserializer(Kroner::class.java, kronerDeserializer())
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

    private fun telefonnummerDeserializer() = object : StdDeserializer<Telefonnummer>(Telefonnummer::class.java) {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext) =
            when (val node = p.codec.readTree<JsonNode>(p)) {
                is IntNode -> Telefonnummer(p.codec.treeToValue(node, Int::class.java).toString())
                is TextNode -> Telefonnummer(p.codec.treeToValue(node, String::class.java))
                else -> Telefonnummer(p.codec.treeToValue(node, TelefonnummerWrapper::class.java).value)
            }
    }

    private fun foedselsnummerDeserializer() = object : StdDeserializer<Foedselsnummer>(Foedselsnummer::class.java) {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext) =
            when (val node = p.codec.readTree<JsonNode>(p)) {
                is IntNode -> Foedselsnummer(p.codec.treeToValue(node, Int::class.java).toString())
                is TextNode -> Foedselsnummer(p.codec.treeToValue(node, String::class.java))
                else -> Foedselsnummer(p.codec.treeToValue(node, FoedselsnummerWrapper::class.java).value)
            }
    }

    private fun kronerDeserializer() = object : StdDeserializer<Kroner>(Kroner::class.java) {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext) =
            when (val node = p.codec.readTree<JsonNode>(p)) {
                is IntNode -> Kroner(p.codec.treeToValue(node, Int::class.java))
                is TextNode -> Kroner(p.codec.treeToValue(node, String::class.java).toInt())
                else -> Kroner(p.codec.treeToValue(node, KronerWrapper::class.java).value)
            }
    }
}