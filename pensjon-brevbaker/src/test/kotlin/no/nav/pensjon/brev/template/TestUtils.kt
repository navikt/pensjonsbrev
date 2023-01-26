package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.render.PensjonLatexRenderer
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
        Assertions.assertTrue(letterString.contains(it), """Letter should contain "$it"""")
    }
    return this
}

private fun <Param : Any> Letter<Param>.renderLetterAndAttachments(): String {
    val letterString = PensjonLatexRenderer.render(this).base64EncodedFiles().filterKeys {
        it.startsWith("attachment") || it == "letter.tex"
    }.map { Base64.getDecoder().decode(it.value).toString(Charset.defaultCharset()) }.joinToString("\n")
    return letterString
}

fun <AttachmentData : Any> createVedleggTestTemplate(
    template: AttachmentTemplate<LangBokmalNynorskEnglish, AttachmentData>,
    attachmentData: Expression<AttachmentData>
) = createTemplate(
    name = "test-template",
    letterDataType = Unit::class,
    languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
    letterMetadata = LetterMetadata(
        "test mal",
        isSensitiv = false,
        distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
    ),
) {
    title {
        text(
            Language.Bokmal to "Tittel",
            Language.Nynorsk to "Tittel",
            Language.English to "Title"
        )
    }

    outline {}

    includeAttachment(template,attachmentData)
}
