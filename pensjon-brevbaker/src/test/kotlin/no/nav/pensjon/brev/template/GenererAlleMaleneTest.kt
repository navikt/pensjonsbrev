package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.maler.ProductionTemplates
import no.nav.pensjon.brev.maler.example.EksempelbrevRedigerbart
import no.nav.pensjon.brev.maler.example.LetterExample
import no.nav.pensjon.brev.renderTestHtml
import no.nav.pensjon.brev.renderTestPDF
import no.nav.pensjon.brev.settOppFakeUnleash
import org.junit.jupiter.api.Tag
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class GenererAlleMaleneTest {

    private val filterForPDF = listOf<String>("67")

    @Tag(TestTags.MANUAL_TEST)
    @ParameterizedTest(name = "{1}, {3}")
    @MethodSource("alleMalene")
    fun <T : Any> testPdf(
        template: LetterTemplate<LanguageSupport, T, *>,
        brevkode: Brevkode<*>,
        fixtures: T,
        spraak: Language,
    ) {
        if (filterForPDF.isNotEmpty()) {
            if (!filterForPDF.any { template.name.lowercase().contains(it.lowercase()) }) {
                return
            }
        }
        if (!template.language.supports(spraak)) {
            println("Mal ${template.name} fins ikke på språk $spraak, tester ikke denne")
            return
        }
        val letter = Letter(template, fixtures, spraak, Fixtures.felles)

        letter.renderTestPDF(filnavn(brevkode, spraak))
    }

    @ParameterizedTest(name = "{1}, {3}")
    @MethodSource("alleMalene")
    fun <T : Any> testHtml(
        template: LetterTemplate<LanguageSupport, T, *>,
        brevkode: Brevkode<*>,
        fixtures: T,
        spraak: Language,
    ) {
        if (!template.language.supports(spraak)) {
            println("Mal ${template.name} fins ikke på språk $spraak, tester ikke denne")
            return
        }
        Letter(
            template,
            fixtures,
            spraak,
            Fixtures.felles,
        ).renderTestHtml(filnavn(brevkode, spraak))
    }

    private fun filnavn(brevkode: Brevkode<*>, spraak: Language) =
        "${brevkode.kode()}_${spraak.javaClass.simpleName}"

    companion object {
        @JvmStatic
        fun alleMalene(): List<Arguments> {
            settOppFakeUnleash()
            return listOf(Language.Nynorsk, Language.Bokmal, Language.English).flatMap { spraak ->
                ProductionTemplates.hentAutobrevmaler().map {
                    Arguments.of(
                        it.template,
                        it.kode,
                        Fixtures.create(it.template.letterDataType),
                        spraak,
                    )
                } + ProductionTemplates.hentRedigerbareMaler().map {
                    Arguments.of(
                        it.template,
                        it.kode,
                        Fixtures.create(it.template.letterDataType),
                        spraak,
                    )
                } + LetterExample.let {
                    Arguments.of(
                        it.template,
                        it.kode,
                        Fixtures.create(it.template.letterDataType),
                        spraak,
                    )
                } + EksempelbrevRedigerbart.let {
                    Arguments.of(
                        it.template,
                        it.kode,
                        Fixtures.create(it.template.letterDataType),
                        spraak,
                    )
                }
            }
        }
    }
}