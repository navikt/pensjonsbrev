package no.nav.pensjon.brev.template

import org.junit.jupiter.api.Assertions
import java.nio.charset.Charset
import java.util.*

fun <Param : Any> Letter<Param>.assertRenderedLetterDoesNotContainAnyOf(vararg searchText: String): Letter<Param> {
    val letterString = this.render().base64EncodedFiles()["letter.tex"]
    searchText.forEach {
        Assertions.assertFalse(
            Base64.getDecoder().decode(letterString).toString(Charset.defaultCharset()).contains(it),
            """Letter should not contain "$it""""
        )
    }
    return this
}

fun <Param : Any> Letter<Param>.assertRenderedLetterContainsAllOf(vararg searchText: String): Letter<Param> {
    val letterString = this.render().base64EncodedFiles()["letter.tex"]
    searchText.forEach {
        Assertions.assertTrue(
            Base64.getDecoder().decode(letterString).toString(Charset.defaultCharset()).contains(it),
            """Letter should contain "$it""""
        )
    }
    return this
}