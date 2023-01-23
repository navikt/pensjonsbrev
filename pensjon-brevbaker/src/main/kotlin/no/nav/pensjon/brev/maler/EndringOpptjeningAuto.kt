package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.ufoerBeloepOekt
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.ufoerBeloepRedusert
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.virkningsDato
import no.nav.pensjon.brev.api.model.maler.EndringOpptjeningAutoDto
import no.nav.pensjon.brev.api.model.maler.EndringOpptjeningAutoDtoSelectors.barnetilleggFellesbarn
import no.nav.pensjon.brev.api.model.maler.EndringOpptjeningAutoDtoSelectors.barnetilleggSaerkullsbarn
import no.nav.pensjon.brev.api.model.maler.EndringOpptjeningAutoDtoSelectors.endringIOpptjening
import no.nav.pensjon.brev.api.model.maler.EndringOpptjeningAutoDtoSelectors.kombinereUfoeretrygdMedInntekt
import no.nav.pensjon.brev.api.model.maler.EndringOpptjeningAutoDtoSelectors.ufoeretrygd
import no.nav.pensjon.brev.api.model.maler.KombinereUfoeretrygdMedInntektSelectors.beloepsgrense
import no.nav.pensjon.brev.api.model.maler.KombinereUfoeretrygdMedInntektSelectors.forventetInntekt
import no.nav.pensjon.brev.api.model.maler.KombinereUfoeretrygdMedInntektSelectors.grunnbeloep
import no.nav.pensjon.brev.api.model.maler.KombinereUfoeretrygdMedInntektSelectors.harBeloepOekt
import no.nav.pensjon.brev.api.model.maler.KombinereUfoeretrygdMedInntektSelectors.harBeloepRedusert
import no.nav.pensjon.brev.api.model.maler.KombinereUfoeretrygdMedInntektSelectors.harDelvisUfoeregrad
import no.nav.pensjon.brev.api.model.maler.KombinereUfoeretrygdMedInntektSelectors.harFullUfoeregrad
import no.nav.pensjon.brev.api.model.maler.KombinereUfoeretrygdMedInntektSelectors.harInntektEtterUfoere
import no.nav.pensjon.brev.api.model.maler.KombinereUfoeretrygdMedInntektSelectors.inntektsgrense
import no.nav.pensjon.brev.api.model.maler.KombinereUfoeretrygdMedInntektSelectors.inntektsgrenseNesteAar
import no.nav.pensjon.brev.api.model.maler.KombinereUfoeretrygdMedInntektSelectors.inntektstak
import no.nav.pensjon.brev.api.model.maler.KombinereUfoeretrygdMedInntektSelectors.kompensasjonsgrad
import no.nav.pensjon.brev.api.model.maler.KombinereUfoeretrygdMedInntektSelectors.nettoAkkumulerteBeloepUtbetalt
import no.nav.pensjon.brev.api.model.maler.KombinereUfoeretrygdMedInntektSelectors.nettoAkkumulertePlussNettoRestAar
import no.nav.pensjon.brev.api.model.maler.KombinereUfoeretrygdMedInntektSelectors.nettoUfoeretrygdUtbetaltPerMaaned
import no.nav.pensjon.brev.api.model.maler.KombinereUfoeretrygdMedInntektSelectors.oppjustertInntektEtterUfoere
import no.nav.pensjon.brev.api.model.maler.KombinereUfoeretrygdMedInntektSelectors.oppjustertInntektFoerUfoere80prosent
import no.nav.pensjon.brev.api.model.maler.KombinereUfoeretrygdMedInntektSelectors.ufoeregrad
import no.nav.pensjon.brev.api.model.maler.KombinereUfoeretrygdMedInntektSelectors.utbetalingsgrad
import no.nav.pensjon.brev.api.model.maler.UfoeretrygdSelectors.harEktefelletilleggInnvilget
import no.nav.pensjon.brev.api.model.maler.UfoeretrygdSelectors.harFellesbarnInnvilget
import no.nav.pensjon.brev.api.model.maler.UfoeretrygdSelectors.harGjenlevendetilleggInnvilget
import no.nav.pensjon.brev.api.model.maler.UfoeretrygdSelectors.harSaerkullsbarnInnvilget
import no.nav.pensjon.brev.api.model.maler.UfoeretrygdSelectors.harUtbetalingsgrad
import no.nav.pensjon.brev.api.model.maler.UfoeretrygdSelectors.harYrkesskadeGradUtbetaling
import no.nav.pensjon.brev.api.model.maler.UfoeretrygdSelectors.utbetaltPerMaaned
import no.nav.pensjon.brev.maler.fraser.EndringOpptjening
import no.nav.pensjon.brev.maler.fraser.ufoer.HjemlerFolketrygdloven
import no.nav.pensjon.brev.maler.fraser.ufoer.Ufoeretrygd
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.maler.fraser.ufoer.KombinereUfoeretrygdMedInntekt
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.VedtaksbrevTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.notNull
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text

@TemplateModelHelpers
object EndringOpptjeningAuto : VedtaksbrevTemplate<EndringOpptjeningAutoDto> {

    override val kode: Brevkode.Vedtak = Brevkode.Vedtak.UT_ENDRING_OPPTJENING_AUTO

    override val template = createTemplate(
        name = kode.name,
        letterDataType = EndringOpptjeningAutoDto::class,
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - ny beregning av UT pga endring i opptjening",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
        )
    ) {
        val harBarnetilleggFellesbarn = barnetilleggFellesbarn.notNull()
        val harBarnetilleggSaerkullsbarn = barnetilleggSaerkullsbarn.notNull()
        title {
            text(
                Bokmal to "NAV har beregnet uføretrygden din på nytt",
                Nynorsk to "NAV har berekne uføretrygden di på nytt",
                English to "NAV has recalculated your disability benefit"
            )
        }
        outline {
            includePhrase(EndringOpptjening.OpplysningerFraSkatteetaten)

            includePhrase(
                EndringOpptjening.BetydningForUfoeretrygden(
                    ufoerBeloepOekt = endringIOpptjening.ufoerBeloepOekt,
                    ufoerBeloepRedusert = endringIOpptjening.ufoerBeloepRedusert,
                    virkningsDato = endringIOpptjening.virkningsDato,
                )
            )

            includePhrase(
                Ufoeretrygd.Beloep(
                    ektefelle = ufoeretrygd.harEktefelletilleggInnvilget,
                    fellesbarn = ufoeretrygd.harFellesbarnInnvilget,
                    gjenlevende = ufoeretrygd.harGjenlevendetilleggInnvilget,
                    perMaaned = ufoeretrygd.utbetaltPerMaaned,
                    saerkullsbarn = ufoeretrygd.harSaerkullsbarnInnvilget,
                    ufoeretrygd = ufoeretrygd.harUtbetalingsgrad,
                )
            )

            includePhrase(Ufoeretrygd.UtbetalingsdatoUfoeretrygd)
            includePhrase(Vedtak.ViktigAaLeseHeleBrevet)
            includePhrase(Vedtak.BegrunnelseOverskrift)
            includePhrase(EndringOpptjening.UfoerInntektListe)

            includePhrase(
                EndringOpptjening.EndringIOpptjeningTilUfoeretrygd(
                    ufoerBeloepOekt = endringIOpptjening.ufoerBeloepOekt,
                    ufoerBeloepRedusert = endringIOpptjening.ufoerBeloepRedusert,
                    virkningsDato = endringIOpptjening.virkningsDato,
                )
            )

            includePhrase(Ufoeretrygd.HenvisningTilVedleggOpplysningerOmBeregningenUfoer)

            includePhrase(
                HjemlerFolketrygdloven.Folketrygdloven(
                    harEktefelletilleggInnvilget = ufoeretrygd.harEktefelletilleggInnvilget,
                    harFellesbarntilleggInnvilget = ufoeretrygd.harFellesbarnInnvilget,
                    harGjenlevendetilleggInnvilget = ufoeretrygd.harGjenlevendetilleggInnvilget,
                    harYrkesskadegradUtbetaling = ufoeretrygd.harYrkesskadeGradUtbetaling,
                    harSaerkullsbarntilleggInnvilget = ufoeretrygd.harSaerkullsbarnInnvilget,
                )
            )

            includePhrase(
                KombinereUfoeretrygdMedInntekt.KombinereUfoeretrygdOgInntektOverskrift(
                    ufoeregrad = kombinereUfoeretrygdMedInntekt.ufoeregrad,
                    utbetalingsgrad = kombinereUfoeretrygdMedInntekt.utbetalingsgrad,
                )
            )

            includePhrase(
                KombinereUfoeretrygdMedInntekt.InntektVedSidenAvUfoeretrygd(
                    ufoeregrad = kombinereUfoeretrygdMedInntekt.ufoeregrad,
                    utbetalingsgrad = kombinereUfoeretrygdMedInntekt.utbetalingsgrad,
                )
            )

            includePhrase(
                KombinereUfoeretrygdMedInntekt.Inntektsgrense(
                    beloepsgrense = kombinereUfoeretrygdMedInntekt.beloepsgrense,
                    grunnbeloep = kombinereUfoeretrygdMedInntekt.grunnbeloep,
                    harFullUfoeregrad = kombinereUfoeretrygdMedInntekt.harFullUfoeregrad,
                    harInntektEtterUfoere = kombinereUfoeretrygdMedInntekt.harInntektEtterUfoere,
                    inntektsgrense = kombinereUfoeretrygdMedInntekt.inntektsgrense,
                    inntektsgrenseNesteAar = kombinereUfoeretrygdMedInntekt.inntektsgrenseNesteAar,
                    ufoeregrad = kombinereUfoeretrygdMedInntekt.ufoeregrad,
                )
            )

            includePhrase(
                KombinereUfoeretrygdMedInntekt.InntektsgrenseLagtTilGrunn(
                    inntektsgrense = kombinereUfoeretrygdMedInntekt.inntektsgrense,
                    inntektsgrenseNesteAar = kombinereUfoeretrygdMedInntekt.inntektsgrenseNesteAar,
                    beloepsgrense = kombinereUfoeretrygdMedInntekt.beloepsgrense,
                    oppjustertInntektEtterUfoere = kombinereUfoeretrygdMedInntekt.oppjustertInntektEtterUfoere,
                    grunnbeloep = kombinereUfoeretrygdMedInntekt.grunnbeloep,
                    ufoeregrad = kombinereUfoeretrygdMedInntekt.ufoeregrad,
                    harInntektEtterUfoere = kombinereUfoeretrygdMedInntekt.harInntektEtterUfoere,
                    harFullUfoeregrad = kombinereUfoeretrygdMedInntekt.harFullUfoeregrad,
                    harDelvisUfoeregrad = kombinereUfoeretrygdMedInntekt.harDelvisUfoeregrad,
                )
            )

            includePhrase(
                KombinereUfoeretrygdMedInntekt.Kompensasjonsgrad(
                    inntektsgrense = kombinereUfoeretrygdMedInntekt.inntektsgrense,
                    inntektsgrenseNesteAar = kombinereUfoeretrygdMedInntekt.inntektsgrenseNesteAar,
                    inntektstak = kombinereUfoeretrygdMedInntekt.inntektstak,
                    kompensasjonsgrad = kombinereUfoeretrygdMedInntekt.kompensasjonsgrad,
                )
            )
            includePhrase(
                KombinereUfoeretrygdMedInntekt.OekeUfoereUtbetalingForRestenAvKalenderAaret(
                    forventetInntekt = kombinereUfoeretrygdMedInntekt.forventetInntekt,
                    harBeloepOekt = kombinereUfoeretrygdMedInntekt.harBeloepOekt,
                    harBeloepRedusert = kombinereUfoeretrygdMedInntekt.harBeloepRedusert,
                    inntektsgrense = kombinereUfoeretrygdMedInntekt.inntektsgrense,
                    inntektstak = kombinereUfoeretrygdMedInntekt.inntektstak,
                    oppjustertInntektFoerUfoere80prosent = kombinereUfoeretrygdMedInntekt.oppjustertInntektFoerUfoere80prosent,
                    ufoeregrad = kombinereUfoeretrygdMedInntekt.ufoeregrad,
                    utbetalingsgrad = kombinereUfoeretrygdMedInntekt.utbetalingsgrad,
                )
            )

            includePhrase(
                KombinereUfoeretrygdMedInntekt.ReduksjonAvInntektUfoere(
                    inntektsgrense = kombinereUfoeretrygdMedInntekt.inntektsgrense,
                    nettoAkkumulerteBeloepUtbetalt = kombinereUfoeretrygdMedInntekt.nettoAkkumulerteBeloepUtbetalt,
                    nettoAkkumulertePlussNettoRestAar = kombinereUfoeretrygdMedInntekt.nettoAkkumulertePlussNettoRestAar,
                    nettoUfoeretrygdUtbetaltPerMaaned = kombinereUfoeretrygdMedInntekt.nettoUfoeretrygdUtbetaltPerMaaned,
                    oppjustertInntektFoerUfoere80prosent = kombinereUfoeretrygdMedInntekt.oppjustertInntektFoerUfoere80prosent,
                    ufoeregrad = kombinereUfoeretrygdMedInntekt.ufoeregrad,
                    utbetalingsgrad = kombinereUfoeretrygdMedInntekt.utbetalingsgrad,
                )
            )

            includePhrase(
                KombinereUfoeretrygdMedInntekt.BeholderUfoeregraden(
                    ufoeregrad = kombinereUfoeretrygdMedInntekt.ufoeregrad
                )
            )

            val utbetalingsgrad = kombinereUfoeretrygdMedInntekt.utbetalingsgrad.format()
            val ufoeregrad = kombinereUfoeretrygdMedInntekt.ufoeregrad.format()
            showIf(utbetalingsgrad.equalTo(ufoeregrad) {
                includePhrase(Ufoeretrygd.MeldeFraOmEventuellInntektOverskrift)
                includePhrase(Ufoeretrygd.MeldeFraOmEventuellInntekt)
            }
        }
    }
}