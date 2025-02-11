package no.nav.pensjon.brev.pdfbygger

import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

object Fixtures {
    val felles = no.nav.brev.brevbaker.Fixtures.felles

    val fellesAuto = no.nav.brev.brevbaker.Fixtures.fellesAuto
}

internal inline fun <reified LetterData : Any> outlineTestTemplate(noinline function: OutlineOnlyScope<LangBokmal, LetterData>.() -> Unit) =
    createTemplate(
        name = "test",
        letterDataType = LetterData::class,
        languages = languages(Bokmal),
        letterMetadata = testLetterMetadata,
    ) {
        title.add(bokmalTittel)
        outline(function)
    }

internal val bokmalTittel = newText(Bokmal to "test brev")
internal val nynorskTittel = newText(Language.Nynorsk to "test brev")
internal val testLetterMetadata =
    LetterMetadata(
        displayTitle = "En fin display tittel",
        isSensitiv = false,
        distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
        brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
    )
