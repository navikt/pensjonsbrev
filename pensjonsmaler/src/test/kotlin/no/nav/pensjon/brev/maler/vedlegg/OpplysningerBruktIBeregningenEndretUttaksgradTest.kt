package no.nav.pensjon.brev.maler.vedlegg

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.createVedleggTestTemplate
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.Beregningsmetode
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenEndretUttaksgradDto
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.Percent
import no.nav.pensjon.brevbaker.api.model.Year
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.LocalDate

class OpplysningerBruktIBeregningenEndretUttaksgradTest {

    @ParameterizedTest(name = "{index} => template={0}, etterlatteBrevKode={1}, fixtures={2}, spraak={3}")
    @MethodSource("sakstyperOgSpraak")
    fun `lag vedlegg`(regelverk: AlderspensjonRegelverkType, spraak: Language) {
        val template = createVedleggTestTemplate(
            vedleggOpplysningerBruktIBeregningenEndretUttaksgrad,
            createOpplysningerBruktIBeregningenEndretUttaksgradDto(regelverk).expr(),
            languages(Language.Bokmal, Language.Nynorsk, Language.English),
        )
        LetterTestImpl(
            template,
            Unit,
            spraak,
            Fixtures.fellesAuto
        ).renderTestHtml(this::class.simpleName + "_${regelverk}_${spraak::class.simpleName}")
    }

    companion object {
        @JvmStatic
        fun sakstyperOgSpraak() = listOf(Language.Nynorsk, Language.Bokmal, Language.English)
            .flatMap { spraak ->
                AlderspensjonRegelverkType.entries.map {
                    Arguments.of(
                        it,
                        spraak
                    )
                }
            }
    }
}

fun createOpplysningerBruktIBeregningenEndretUttaksgradDto(alderspensjonRegelverkType: AlderspensjonRegelverkType = AlderspensjonRegelverkType.AP2025) =
    OpplysningerBruktIBeregningenEndretUttaksgradDto(
        alderspensjonVedVirk = OpplysningerBruktIBeregningenEndretUttaksgradDto.AlderspensjonVedVirk(
            uttaksgrad = Percent(80),
            regelverkType = alderspensjonRegelverkType,
            andelKap19 = 40,
            andelKap20 = 60
        ),
        oppfrisketOpptjeningVedVirk = OpplysningerBruktIBeregningenEndretUttaksgradDto.OppfrisketOpptjeningVedVirk(
            sisteGyldigeOpptjeningsAr = Year(2022),
            poenggivendeInntektSisteGyldigeOpptjeningsAr = Kroner(987),
            poengtallSisteGyldigeOpptjeningsAr = 4.0,
            opptjeningTilfortKap20 = Kroner(234)
        ),
        bruker = OpplysningerBruktIBeregningenEndretUttaksgradDto.Bruker(
            fodselsdato = LocalDate.now()
        ),
        krav = OpplysningerBruktIBeregningenEndretUttaksgradDto.Krav(
            virkDatoFom = LocalDate.now()
        ),
        trygdetidsdetaljerKap19VedVirk = OpplysningerBruktIBeregningenEndretUttaksgradDto.TrygdetidsdetaljerKap19VedVirk(
            anvendtTT = 40,
            beregningsmetode = Beregningsmetode.FOLKETRYGD
        ),
        beregningKap19VedVirk = OpplysningerBruktIBeregningenEndretUttaksgradDto.BeregningKap19VedVirk(
            sluttpoengtall = 10.0,
            poengAr = 50,
            poengArf92 = 1,
            poengAre91 = 3,
            forholdstallLevealder = 2.5
        ),
        endretUttaksgradVedVirk = OpplysningerBruktIBeregningenEndretUttaksgradDto.EndretUttaksgradVedVirk(
            restGrunnpensjon = Kroner(299),
            restTilleggspensjon = Kroner(50),
            pensjonsbeholdning = Kroner(1000),
            garantipensjonsBeholdning = Kroner(10)
        ),
        trygdetidsdetaljerKap20VedVirk = OpplysningerBruktIBeregningenEndretUttaksgradDto.TrygdetidsdetaljerKap20VedVirk(
            anvendtTT = 40
        ),
        beregningKap20VedVirk = OpplysningerBruktIBeregningenEndretUttaksgradDto.BeregningKap20VedVirk(
            delingstallLevealder = 1.0
        )
    )