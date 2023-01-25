package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarnVedvirkSelectors.erRedusertMotInntekt_safe
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.virkningsDato
import no.nav.pensjon.brev.api.model.maler.EndringOpptjeningAutoDto
import no.nav.pensjon.brev.api.model.maler.EndringOpptjeningAutoDtoSelectors.barnetilleggFellesbarn
import no.nav.pensjon.brev.api.model.maler.EndringOpptjeningAutoDtoSelectors.barnetilleggSaerkullsbarn
import no.nav.pensjon.brev.api.model.maler.EndringOpptjeningAutoDtoSelectors.endringIOpptjening
import no.nav.pensjon.brev.api.model.maler.EndringOpptjeningAutoDtoSelectors.ufoeretrygd
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.beloepsgrense
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.forventetInntekt
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.grunnbeloep
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.harBeloepOekt
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.harBeloepRedusert
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.harDelvisUfoeregrad
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.harFullUfoeregrad
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.harInntektEtterUfoere
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.inntektsgrense
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.inntektsgrenseNesteAar
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.inntektstak
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.kompensasjonsgrad
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.nettoAkkumulerteBeloepUtbetalt
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.nettoAkkumulertePlussNettoRestAar
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.nettoUfoeretrygdUtbetaltPerMaaned
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.oppjustertInntektEtterUfoere
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.oppjustertInntektFoerUfoere80prosent
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.ufoeregrad
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.utbetalingsgrad
import no.nav.pensjon.brev.api.model.maler.EndringOpptjeningAutoDtoSelectors.maanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.api.model.maler.EndringOpptjeningAutoDtoSelectors.opplysningerBruktIBeregningUT
import no.nav.pensjon.brev.api.model.maler.EndringOpptjeningAutoDtoSelectors.orienteringOmRettigheterUfoere
import no.nav.pensjon.brev.api.model.maler.InntektFoerUfoerhetVedVirkSelectors.erSannsynligEndret
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.barnetilleggSaerkullsbarnVedVirk
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.inntektFoerUfoerhetVedVirk
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.maanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.opplysningerBruktIBeregningUT
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDtoSelectors.orienteringOmRettigheterOgPlikter
import no.nav.pensjon.brev.api.model.maler.UfoeretrygdSelectors.brukerBorInorge
import no.nav.pensjon.brev.api.model.maler.UfoeretrygdSelectors.harEktefelletilleggInnvilget
import no.nav.pensjon.brev.api.model.maler.UfoeretrygdSelectors.harFellesbarnInnvilget
import no.nav.pensjon.brev.api.model.maler.UfoeretrygdSelectors.harGjenlevendetilleggInnvilget
import no.nav.pensjon.brev.api.model.maler.UfoeretrygdSelectors.harSaerkullsbarnInnvilget
import no.nav.pensjon.brev.api.model.maler.UfoeretrygdSelectors.harUtbetalingsgrad
import no.nav.pensjon.brev.api.model.maler.UfoeretrygdSelectors.harYrkesskadeGradUtbetaling
import no.nav.pensjon.brev.api.model.maler.UfoeretrygdSelectors.utbetaltPerMaaned
import no.nav.pensjon.brev.maler.fraser.EndringOpptjening
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.ufoer.HjemlerFolketrygdloven
import no.nav.pensjon.brev.maler.fraser.ufoer.Ufoeretrygd
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.maler.fraser.ufoer.Gjenlevendetillegg
import no.nav.pensjon.brev.maler.fraser.ufoer.KombinereUfoeretrygdMedInntekt
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.maler.vedlegg.vedleggOpplysningerBruktIBeregningUT
import no.nav.pensjon.brev.maler.vedlegg.vedleggOrienteringOmRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.VedtaksbrevTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
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
                    harBeloepOekt = endringIOpptjening.harBeloepOekt,
                    harBeloepRedusert = endringIOpptjening.harBeloepRedusert,
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
                    harBeloepOekt = endringIOpptjening.harBeloepOekt,
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
                    ufoeregrad = endringIOpptjening.ufoeregrad,
                    utbetalingsgrad = endringIOpptjening.utbetalingsgrad,
                )
            )

            includePhrase(
                KombinereUfoeretrygdMedInntekt.InntektVedSidenAvUfoeretrygd(
                    ufoeregrad = endringIOpptjening.ufoeregrad,
                    utbetalingsgrad = endringIOpptjening.utbetalingsgrad,
                )
            )

            includePhrase(
                KombinereUfoeretrygdMedInntekt.Inntektsgrense(
                    beloepsgrense = endringIOpptjening.beloepsgrense,
                    grunnbeloep = endringIOpptjening.grunnbeloep,
                    harFullUfoeregrad = endringIOpptjening.harFullUfoeregrad,
                    harInntektEtterUfoere = endringIOpptjening.harInntektEtterUfoere,
                    inntektsgrense = endringIOpptjening.inntektsgrense,
                    inntektsgrenseNesteAar = endringIOpptjening.inntektsgrenseNesteAar,
                    ufoeregrad = endringIOpptjening.ufoeregrad,
                )
            )

            includePhrase(
                KombinereUfoeretrygdMedInntekt.InntektsgrenseLagtTilGrunn(
                    inntektsgrense = endringIOpptjening.inntektsgrense,
                    inntektsgrenseNesteAar = endringIOpptjening.inntektsgrenseNesteAar,
                    beloepsgrense = endringIOpptjening.beloepsgrense,
                    oppjustertInntektEtterUfoere = endringIOpptjening.oppjustertInntektEtterUfoere,
                    grunnbeloep = endringIOpptjening.grunnbeloep,
                    ufoeregrad = endringIOpptjening.ufoeregrad,
                    harInntektEtterUfoere = endringIOpptjening.harInntektEtterUfoere,
                    harFullUfoeregrad = endringIOpptjening.harFullUfoeregrad,
                    harDelvisUfoeregrad = endringIOpptjening.harDelvisUfoeregrad,
                )
            )

            includePhrase(
                KombinereUfoeretrygdMedInntekt.Kompensasjonsgrad(
                    inntektsgrense = endringIOpptjening.inntektsgrense,
                    inntektsgrenseNesteAar = endringIOpptjening.inntektsgrenseNesteAar,
                    inntektstak = endringIOpptjening.inntektstak,
                    kompensasjonsgrad = endringIOpptjening.kompensasjonsgrad,
                )
            )
            includePhrase(
                KombinereUfoeretrygdMedInntekt.OekeUfoereUtbetalingForRestenAvKalenderAaret(
                    forventetInntekt = endringIOpptjening.forventetInntekt,
                    harBeloepOekt = endringIOpptjening.harBeloepOekt,
                    harBeloepRedusert = endringIOpptjening.harBeloepRedusert,
                    inntektsgrense = endringIOpptjening.inntektsgrense,
                    inntektstak = endringIOpptjening.inntektstak,
                    oppjustertInntektFoerUfoere80prosent = endringIOpptjening.oppjustertInntektFoerUfoere80prosent,
                    ufoeregrad = endringIOpptjening.ufoeregrad,
                    utbetalingsgrad = endringIOpptjening.utbetalingsgrad,
                )
            )

            includePhrase(
                KombinereUfoeretrygdMedInntekt.ReduksjonAvInntektUfoere(
                    inntektsgrense = endringIOpptjening.inntektsgrense,
                    nettoAkkumulerteBeloepUtbetalt = endringIOpptjening.nettoAkkumulerteBeloepUtbetalt,
                    nettoAkkumulertePlussNettoRestAar = endringIOpptjening.nettoAkkumulertePlussNettoRestAar,
                    nettoUfoeretrygdUtbetaltPerMaaned = endringIOpptjening.nettoUfoeretrygdUtbetaltPerMaaned,
                    oppjustertInntektFoerUfoere80prosent = endringIOpptjening.oppjustertInntektFoerUfoere80prosent,
                    ufoeregrad = endringIOpptjening.ufoeregrad,
                    utbetalingsgrad = endringIOpptjening.utbetalingsgrad,
                )
            )

            includePhrase(
                KombinereUfoeretrygdMedInntekt.BeholderUfoeregraden(
                    ufoeregrad = endringIOpptjening.ufoeregrad
                )
            )

            val utbetalingsgrad = endringIOpptjening.utbetalingsgrad.format()
            val ufoeregrad = endringIOpptjening.ufoeregrad.format()
            showIf(utbetalingsgrad.equalTo(ufoeregrad)) {
                includePhrase(Ufoeretrygd.MeldeFraOmEventuellInntekt)
            }

            includePhrase(
                KombinereUfoeretrygdMedInntekt.MeldeFraOmEndringerIInntekten(
                    forventetInntekt = endringIOpptjening.forventetInntekt,
                    inntektsgrense = endringIOpptjening.inntektsgrense,
                    inntektstak = endringIOpptjening.inntektstak,
                    oppjustertInntektFoerUfoere80prosent = endringIOpptjening.oppjustertInntektFoerUfoere80prosent,
                    ufoeregrad = endringIOpptjening.ufoeregrad,
                    utbetalingsgrad = endringIOpptjening.utbetalingsgrad,
                )
            )

            includePhrase(
                Gjenlevendetillegg.HarGjenlevendetillegg(
                    forventetInntekt = endringIOpptjening.forventetInntekt,
                    harGjenlevendetilleggInnvilget = ufoeretrygd.harGjenlevendetilleggInnvilget,
                    inntektsgrense = endringIOpptjening.inntektsgrense,
                )
            )

            includePhrase(
                EndringOpptjening.EtterbetalingAvUfoeretrygd(
                    harBeloepOekt = endringIOpptjening.harBeloepOekt,
                    ufoeregrad = endringIOpptjening.ufoeregrad,
                    utbetalingsgrad = endringIOpptjening.utbetalingsgrad,
                    virkningsDato = endringIOpptjening.virkningsDato

                )
            )
            includePhrase(
                EndringOpptjening.TilbakekrevingAvUfoeretrygd(
                    harBeloepRedusert = endringIOpptjening.harBeloepRedusert
                )
            )
            includePhrase(Ufoeretrygd.MeldeFraOmEndringer)
            includePhrase(Ufoeretrygd.RettTilAAKlage)
            includePhrase(Felles.RettTilKlagePesys_001)
            includePhrase(Felles.RettTilInnsynPesys_001)
            includePhrase(Ufoeretrygd.SjekkUtbetalingene)
            includePhrase(Ufoeretrygd.Skattekort)

            includePhrase(
                Ufoeretrygd.SkattForDegSomBorIUtlandet(
                    brukerBorInorge = ufoeretrygd.brukerBorInorge
                )
            )
        }

        includeAttachment(vedleggMaanedligUfoeretrygdFoerSkatt, maanedligUfoeretrygdFoerSkatt)
        includeAttachment(vedleggOpplysningerBruktIBeregningUT, opplysningerBruktIBeregningUT)
        includeAttachment(vedleggOrienteringOmRettigheterOgPlikterUfoere, orienteringOmRettigheterUfoere)

    }
}