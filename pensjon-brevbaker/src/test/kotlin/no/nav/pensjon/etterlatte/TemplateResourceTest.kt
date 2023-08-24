package no.nav.pensjon.etterlatte

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.render.PensjonHTMLRenderer
import no.nav.pensjon.brev.writeTestHTML
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class TemplateResourceTest {

    @ParameterizedTest(name = "{index} => template={0}, etterlatteBrevKode={1}, fixtures={2}")
    @MethodSource("alleMalene")
    fun <T : Any> testHtml(
        template: LetterTemplate<LanguageSupport.Triple<Language.Bokmal, Language.Nynorsk, Language.English>, T>,
        etterlatteBrevKode: EtterlatteBrevKode,
        fixtures: T,
    ) {
        Letter(
            template,
            fixtures,
            Language.Bokmal,
            Fixtures.felles,
        ).let { PensjonHTMLRenderer.render(it) }
            .also { writeTestHTML(etterlatteBrevKode.name, it) }
    }

    companion object {
        @JvmStatic
        fun alleMalene() =
            prodAutobrevTemplates.map {
                Arguments.of(
                    it.template,
                    it.kode,
                    Fixtures.create(it.template.letterDataType),
                )
            }
    }
}
