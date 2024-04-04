package no.nav.pensjon.brev.template

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.containsSubstring
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.render.PensjonLatexRenderer
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import org.junit.jupiter.api.Assertions
import java.nio.charset.Charset
import java.util.*

fun <Param : Any> Letter<Param>.assertRenderedLetterDoesNotContainAnyOf(vararg searchText: String): Letter<Param> {
    val letterString = renderLetterAndAttachments()
    searchText.forEach {
        Assertions.assertFalse(letterString.contains(it), """Letter should not contain "$it"""")
    }
    return this
}

fun <Param : Any> Letter<Param>.assertRenderedLetterContainsAllOf(vararg searchText: String): Letter<Param> {
    val letterString = renderLetterAndAttachments()
    searchText.forEach {
        assertThat(letterString, containsSubstring(it))
    }
    return this
}

private fun <Param : Any> Letter<Param>.renderLetterAndAttachments(): String {
    val letterString = PensjonLatexRenderer.render(this).base64EncodedFiles().filterKeys {
        it.startsWith("attachment") || it == "letter.tex"
    }.map { Base64.getDecoder().decode(it.value).toString(Charset.defaultCharset()) }.joinToString("\n")
    return letterString
}


fun <AttachmentData : Any, Lang: LanguageSupport> createVedleggTestTemplate(
    template: AttachmentTemplate<Lang, AttachmentData>,
    attachmentData: Expression<AttachmentData>,
    languages: Lang,
) = createTemplate(
    name = "test-template",
    letterDataType = Unit::class,
    languages = languages,
    letterMetadata = LetterMetadata(
        "test mal",
        isSensitiv = false,
        distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
        brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
    ),
) {
    title {
        eval("Tittel".expr())
    }

    outline {}

    includeAttachment(template,attachmentData)
}
