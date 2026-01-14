package no.nav.pensjon.brev.alder.maler.vedlegg

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.createVedleggTestTemplate
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.pensjon.brev.alder.Fixtures
import no.nav.pensjon.brev.alder.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata
import no.nav.pensjon.brev.alder.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.languages
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class DineRettigheterOgMulighetTilAaKlageTest {

    @ParameterizedTest(name = "{index} => template={0}, etterlatteBrevKode={1}, fixtures={2}, spraak={3}")
    @MethodSource("sakstyperOgSpraak")
    fun `lag vedlegg`(sakstype: Sakstype, spraak: Language) {
        val template = createVedleggTestTemplate(
            vedleggDineRettigheterOgMulighetTilAaKlage,
            DineRettigheterOgMulighetTilAaKlageDto(sakstype = sakstype, true).expr(),
            languages(Language.Bokmal, Language.Nynorsk, Language.English),
        )
        LetterTestImpl(
            template,
            EmptyAutobrevdata,
            spraak,
            Fixtures.fellesAuto
        ).renderTestHtml(this::class.simpleName + "_${sakstype}_${spraak::class.simpleName}")
    }

    companion object {
        @JvmStatic
        fun sakstyperOgSpraak() = listOf(Language.Nynorsk, Language.Bokmal, Language.English)
            .flatMap { spraak ->
                listOf(Sakstype.ALDER, Sakstype.UFOREP, Sakstype.BARNEP).map {
                    Arguments.of(
                        it,
                        spraak
                    )
                }
            }
    }
}

fun createDineRettigheterOgMulighetTilAaKlageDto() = DineRettigheterOgMulighetTilAaKlageDto(
    sakstype = Sakstype.ALDER,
    brukerUnder18Aar = null
)