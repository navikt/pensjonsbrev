package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDto
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDtoSelectors.endringIOpptjening
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDtoSelectors.fellesbarnTillegg
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDtoSelectors.maanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDtoSelectors.opplysningerBruktIBeregningUT
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDtoSelectors.orienteringOmRettigheterUfoere
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDtoSelectors.saerkullsbarnTillegg
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDtoSelectors.sivilstand
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.virkningsDato
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.beloepsgrense
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.brukerBorInorge
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.forventetInntekt
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.grunnbeloep
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.harBeloepOekt
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.harBeloepRedusert
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.harDelvisUfoeregrad
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.harEktefelletilleggInnvilget
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.harFullUfoeregrad
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.harFullUtbetalingsgrad
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.harGjenlevendetilleggInnvilget
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.harInntektEtterUfoere
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.harUtbetalingsgrad
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.harYrkesskadeGradUtbetaling
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
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.utbetaltPerMaaned
import no.nav.pensjon.brev.api.model.maler.FellesbarnTilleggSelectors.harFellesbarnInnvilget
import no.nav.pensjon.brev.api.model.maler.FellesbarnTilleggSelectors.harFellesbarnInnvilget_safe
import no.nav.pensjon.brev.api.model.maler.OpphoerBarnetilleggAutoDtoSelectors.barnetilleggFellesbarn
import no.nav.pensjon.brev.api.model.maler.OpphoerBarnetilleggAutoDtoSelectors.barnetilleggSaerkullsbarn
import no.nav.pensjon.brev.api.model.maler.SaerkullsbarnTilleggSelectors.harSaerkullsbarnInnvilget
import no.nav.pensjon.brev.api.model.maler.SaerkullsbarnTilleggSelectors.harSaerkullsbarnInnvilget_safe
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.ufoer.HjemlerFolketrygdloven
import no.nav.pensjon.brev.maler.fraser.ufoer.Ufoeretrygd
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.maler.fraser.ufoer.Gjenlevendetillegg
import no.nav.pensjon.brev.maler.fraser.ufoer.endringIOpptjening.BarnetilleggEndringIOpptjening
import no.nav.pensjon.brev.maler.fraser.ufoer.endringIOpptjening.EndringIOpptjening
import no.nav.pensjon.brev.maler.fraser.ufoer.endringIOpptjening.KombinereUfoeretrygdMedInntekt
import no.nav.pensjon.brev.maler.vedlegg.createVedleggOpplysningerBruktIBeregningUT
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.VedtaksbrevTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text

@TemplateModelHelpers
object EndringIOpptjeningAuto : VedtaksbrevTemplate<EndringIOpptjeningAutoDto> {

    override val kode: Brevkode.Vedtak = Brevkode.Vedtak.UT_ENDRING_OPPTJENING_AUTO

    override val template = createTemplate(
        name = kode.name,
        letterDataType = EndringIOpptjeningAutoDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - ny beregning av UT pga endring i opptjening",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
        )
    ) {
        title {
            text(
                Bokmal to "NAV har beregnet uføretrygden din på nytt",
                Nynorsk to "NAV har berekna uføretrygda di på nytt",
                English to "NAV has recalculated your disability benefit"
            )
        }
        outline {
            includePhrase(EndringIOpptjening.OpplysningerFraSkatteetaten)

            includePhrase(
                EndringIOpptjening.BetydningForUfoeretrygden(
                    harBeloepOekt = endringIOpptjening.harBeloepOekt,
                    harBeloepRedusert = endringIOpptjening.harBeloepRedusert,
                    virkningsDato = endringIOpptjening.virkningsDato,
                )
            )

            includePhrase(
                Ufoeretrygd.Beloep(
                    ektefelle = endringIOpptjening.harEktefelletilleggInnvilget.notNull(),
                    fellesbarn = fellesbarnTillegg.harFellesbarnInnvilget_safe.notNull(),
                    gjenlevende = endringIOpptjening.harGjenlevendetilleggInnvilget.notNull(),
                    perMaaned = endringIOpptjening.utbetaltPerMaaned,
                    saerkullsbarn = saerkullsbarnTillegg.harSaerkullsbarnInnvilget_safe.notNull(),
                    ufoeretrygd = endringIOpptjening.harUtbetalingsgrad,
                )
            )

            includePhrase(Ufoeretrygd.UtbetalingsdatoUfoeretrygd)
            includePhrase(Ufoeretrygd.ViktigAALeseHeleBrevet)
            includePhrase(Vedtak.BegrunnelseOverskrift)
            includePhrase(EndringIOpptjening.UfoerInntektListe)

            includePhrase(
                EndringIOpptjening.EndringIOpptjeningTilUfoeretrygd(
                    harBeloepOekt = endringIOpptjening.harBeloepOekt,
                    harBeloepRedusert = endringIOpptjening.harBeloepRedusert,
                    virkningsDato = endringIOpptjening.virkningsDato,
                )
            )

            includePhrase(Ufoeretrygd.HenvisningTilVedleggOpplysningerOmBeregningenUfoer)

            includePhrase(
                HjemlerFolketrygdloven.Folketrygdloven(
                    harEktefelletilleggInnvilget = endringIOpptjening.harEktefelletilleggInnvilget.notNull(),
                    harFellesbarntilleggInnvilget = fellesbarnTillegg.harFellesbarnInnvilget_safe.notNull(),
                    harGjenlevendetilleggInnvilget = endringIOpptjening.harGjenlevendetilleggInnvilget.notNull(),
                    harYrkesskadegradUtbetaling = endringIOpptjening.harYrkesskadeGradUtbetaling.notNull(),
                    harSaerkullsbarntilleggInnvilget = saerkullsbarnTillegg.harSaerkullsbarnInnvilget_safe.notNull(),
                )
            )

            includePhrase(
                KombinereUfoeretrygdMedInntekt.KombinereUfoeretrygdOgInntektOverskrift(
                    harDelvisUfoeregrad = endringIOpptjening.harDelvisUfoeregrad,
                    harFullUfoeregrad = endringIOpptjening.harFullUfoeregrad,
                    harFullUtbetalingsgrad = endringIOpptjening.harFullUtbetalingsgrad,
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
                    kompensasjonsgrad = endringIOpptjening.kompensasjonsgrad,
                    oppjustertInntektFoerUfoere80prosent = endringIOpptjening.oppjustertInntektFoerUfoere80prosent,
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

            showIf(endringIOpptjening.utbetalingsgrad.format().equalTo(endringIOpptjening.ufoeregrad.format()))
            {
                includePhrase(Ufoeretrygd.MeldeFraOmEventuellInntektOverskrift)
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
            ifNotNull(fellesbarnTillegg, saerkullsbarnTillegg
            ) { fellesbarnTillegg, saerkullsbarnTillegg ->
                showIf(fellesbarnTillegg.harFellesbarnInnvilget or saerkullsbarnTillegg.harSaerkullsbarnInnvilget) {
                    includePhrase(
                        BarnetilleggEndringIOpptjening(
                            barnetilleggFellesbarn = fellesbarnTillegg,
                            barnetilleggSaerkullsbarn = saerkullsbarnTillegg,
                            grunnbeloep = endringIOpptjening.grunnbeloep,
                            sivilstand = sivilstand
                        )
                    )
                }
            }

            includePhrase(
                Gjenlevendetillegg.HarGjenlevendetillegg(
                    forventetInntekt = endringIOpptjening.forventetInntekt,
                    harGjenlevendetilleggInnvilget = endringIOpptjening.harGjenlevendetilleggInnvilget.notNull(),
                    inntektsgrense = endringIOpptjening.inntektsgrense,
                )
            )

            includePhrase(
                EndringIOpptjening.EtterbetalingAvUfoeretrygd(
                    harBeloepOekt = endringIOpptjening.harBeloepOekt,
                    ufoeregrad = endringIOpptjening.ufoeregrad,
                    utbetalingsgrad = endringIOpptjening.utbetalingsgrad,
                    virkningsDato = endringIOpptjening.virkningsDato

                )
            )
            includePhrase(
                EndringIOpptjening.TilbakekrevingAvUfoeretrygd(
                    harBeloepRedusert = endringIOpptjening.harBeloepRedusert
                )
            )
            includePhrase(Ufoeretrygd.MeldeFraOmEndringer)
            includePhrase(Ufoeretrygd.RettTilAAKlage)
            includePhrase(Felles.RettTilInnsynPesys)
            includePhrase(Ufoeretrygd.SjekkUtbetalingene)
            includePhrase(Ufoeretrygd.Skattekort)

            includePhrase(
                Ufoeretrygd.SkattForDegSomBorIUtlandet(
                    brukerBorInorge = endringIOpptjening.brukerBorInorge
                )
            )
        }


        includeAttachment(vedleggMaanedligUfoeretrygdFoerSkatt, maanedligUfoeretrygdFoerSkatt)

        includeAttachment(
            createVedleggOpplysningerBruktIBeregningUT(
                skalViseMinsteytelse = false,
                skalViseBarnetillegg = true,
            ), opplysningerBruktIBeregningUT
        )

        includeAttachment(vedleggDineRettigheterOgPlikterUfoere, orienteringOmRettigheterUfoere)

    }

}