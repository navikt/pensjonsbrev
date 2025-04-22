package no.nav.pensjon.brev.maler.vedlegg

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.createVedleggTestTemplate
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.vedlegg.InformasjonOmMedlemskapOgHelserettigheterDto
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.languages
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class InformasjonOmMedlemskapOgHelserettigheterTest {

    @ParameterizedTest
    @MethodSource("sakstyperOgSpraak")
    fun `test vedlegg vedleggInformasjonOmMedlemskapOgHelserettigheter`(sakstype: Sakstype, spraak: Language, erEOSLand: Boolean) {
        val template = createVedleggTestTemplate(
            vedleggInformasjonOmMedlemskapOgHelserettigheter,
            InformasjonOmMedlemskapOgHelserettigheterDto(erEOSLand).expr(),
            languages(Language.Bokmal, Language.English),
        )
        LetterTestImpl(
            template,
            Unit,
            spraak,
            Fixtures.fellesAuto
        ).renderTestHtml(this::class.simpleName + "_${sakstype}_${spraak::class.simpleName}_${erEOSLand}")
    }

    companion object {
        @JvmStatic
        fun sakstyperOgSpraak() = listOf(Language.Bokmal, Language.English)
            .flatMap { spraak -> listOf(true, false).map { Arguments.of(Sakstype.ALDER, spraak, it) } }
    }
}