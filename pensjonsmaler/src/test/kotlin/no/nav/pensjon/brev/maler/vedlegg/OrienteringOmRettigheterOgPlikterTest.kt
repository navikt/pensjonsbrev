package no.nav.pensjon.brev.maler.vedlegg

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.createVedleggTestTemplate
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.Institusjon
import no.nav.pensjon.brev.api.model.MetaforceSivilstand
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sakstype.AFP
import no.nav.pensjon.brev.api.model.Sakstype.ALDER
import no.nav.pensjon.brev.api.model.Sakstype.BARNEP
import no.nav.pensjon.brev.api.model.Sakstype.UFOREP
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.languages
import org.junit.jupiter.api.Tag
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@Tag(TestTags.MANUAL_TEST)
class OrienteringOmRettigheterOgPlikterTest {

    @ParameterizedTest(name = "{index} => template={0}, etterlatteBrevKode={1}, fixtures={2}, spraak={3}")
    @MethodSource("sakstyperOgSpraak")
    fun `lag vedlegg`(sakstype: Sakstype, spraak: Language) {
        val template = createVedleggTestTemplate(
            vedleggOrienteringOmRettigheterOgPlikter,
            createOrienteringOmRettigheterOgPlikterDto(
                sakstype = sakstype
            ).expr(),
            languages(Language.Bokmal, Language.Nynorsk, Language.English),
        )
        LetterTestImpl(
            template,
            Unit,
            spraak,
            Fixtures.fellesAuto
        ).renderTestHtml(this::class.simpleName + "_${sakstype}_${spraak::class.simpleName}")
    }

    companion object {
        @JvmStatic
        fun sakstyperOgSpraak() = listOf(Language.Nynorsk, Language.Bokmal, Language.English)
            .flatMap { spraak -> listOf(ALDER, UFOREP, BARNEP, AFP).map { Arguments.of(it, spraak) } }
    }
}

fun createOrienteringOmRettigheterOgPlikterDto(sakstype: Sakstype = ALDER) = OrienteringOmRettigheterOgPlikterDto(
    sakstype = sakstype,
    brukerBorINorge = true,
    institusjonsoppholdGjeldende = Institusjon.INGEN,
    sivilstand = MetaforceSivilstand.GIFT,
    borSammenMedBruker = true,
    epsPaInstitusjon = false,
    epsOppholdSykehjem = null,
    harBarnetillegg = false,
    brukerUnder18Aar = false,
)