package no.nav.pensjon.brev.maler.vedlegg

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.createVedleggTestTemplate
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattAlderspensjonDto
import no.nav.pensjon.brev.fixtures.createAlderspensjonPerManed
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.languages
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.LocalDate
import java.time.Month

class MaanedligPensjonFoerSkattAlderspensjonTest {

    @ParameterizedTest
    @MethodSource("sakstyperOgSpraak")
    fun `test vedlegg maanedligPensjonFoerSkattAlderspensjon`(
        regelverkType: AlderspensjonRegelverkType,
        spraak: Language,
    ) {
        val template = createVedleggTestTemplate(
            maanedligPensjonFoerSkattAlderspensjon,
            createMaanedligPensjonFoerSkattAlderspensjonDto(regelverkType).expr(),
            languages(Language.Bokmal, Language.English),
        )
        LetterTestImpl(
            template,
            Unit,
            spraak,
            Fixtures.fellesAuto
        ).renderTestHtml(this::class.simpleName + "_${regelverkType.name}_${spraak::class.simpleName}")
    }

    companion object {
        @JvmStatic
        fun sakstyperOgSpraak() = listOf(Language.Bokmal, Language.Nynorsk, Language.English)
            .flatMap { spraak ->
                AlderspensjonRegelverkType.entries.map { regelverk ->
                    Arguments.of(
                        regelverk,
                        spraak
                    )
                }
            }
    }
}

fun createMaanedligPensjonFoerSkattAlderspensjonDto(regelverkType: AlderspensjonRegelverkType = AlderspensjonRegelverkType.AP2016) =
    MaanedligPensjonFoerSkattAlderspensjonDto(
        krav = MaanedligPensjonFoerSkattAlderspensjonDto.Krav(
            virkDatoFom = LocalDate.of(2022, Month.MARCH, 1)
        ),
        alderspensjonGjeldende = MaanedligPensjonFoerSkattAlderspensjonDto.AlderspensjonGjeldende(
            regelverkType = regelverkType
        ),
        alderspensjonPerManed = listOf(createAlderspensjonPerManed(), createAlderspensjonPerManed(), createAlderspensjonPerManed()),
    )