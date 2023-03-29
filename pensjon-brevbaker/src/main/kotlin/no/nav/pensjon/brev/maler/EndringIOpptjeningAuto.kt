package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDto
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDtoSelectors.endringIOpptjening
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDtoSelectors.endringIOpptjeningBeregning
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDtoSelectors.endringIOpptjeningBoolean
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDtoSelectors.fellesbarnTillegg
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDtoSelectors.harEktefelletilleggInnvilget
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDtoSelectors.harGjenlevendetilleggInnvilget
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDtoSelectors.harYrkesskadeOppfylt
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDtoSelectors.maanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDtoSelectors.opplysningerBruktIBeregningUT
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDtoSelectors.orienteringOmRettigheterUfoere
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDtoSelectors.saerkullsbarnTillegg
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDtoSelectors.sivilstand
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDtoSelectors.virkningsDato
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningBeregningSelectors.nettoAkkumulertePlussNettoRestAar_safe
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningBeregningSelectors.oppjustertInntektFoerUfoere80prosent_safe
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningBooleanSelectors.brukerBorInorge
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningBooleanSelectors.harBeloepOekt
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningBooleanSelectors.harBeloepRedusert
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningBooleanSelectors.harDelvisUfoeregrad
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningBooleanSelectors.harFullUfoeregrad
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningBooleanSelectors.harFullUtbetalingsgrad
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningBooleanSelectors.harInntektEtterUfoere
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningBooleanSelectors.harUfoeretrygdUtbetalt
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningBooleanSelectors.harUtbetalingsgrad
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.beloepsgrense
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.forventetInntekt
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.grunnbeloep
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.inntektsgrense
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.inntektsgrenseNesteAar
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.inntektstak
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.kompensasjonsgrad
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.nettoAkkumulerteBeloepUtbetalt
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.nettoUfoeretrygdUtbetaltPerMaaned
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.oppjustertInntektEtterUfoere
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.ufoeregrad
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.utbetalingsgrad
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningSelectors.utbetaltPerMaaned
import no.nav.pensjon.brev.api.model.maler.FellesbarnTilleggSelectors.harFellesbarnInnvilget
import no.nav.pensjon.brev.api.model.maler.FellesbarnTilleggSelectors.harFellesbarnInnvilget_safe
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
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text

@TemplateModelHelpers
object EndringIOpptjeningAuto : AutobrevTemplate<EndringIOpptjeningAutoDto> {

    override val kode: Brevkode.AutoBrev = Brevkode.AutoBrev.UT_ENDRING_OPPTJENING_AUTO

    override val template = createTemplate(
        name = kode.name,
        letterDataType = EndringIOpptjeningAutoDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - ny beregning av UT pga endring i opptjening",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
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
                    harBeloepOekt = endringIOpptjeningBoolean.harBeloepOekt,
                    harBeloepRedusert = endringIOpptjeningBoolean.harBeloepRedusert,
                    virkningsDato = virkningsDato,
                )
            )

            includePhrase(
                Ufoeretrygd.Beloep(
                    ektefelle = harEktefelletilleggInnvilget.notNull(),
                    fellesbarn = fellesbarnTillegg.harFellesbarnInnvilget_safe.notNull(),
                    gjenlevende = harGjenlevendetilleggInnvilget.notNull(),
                    perMaaned = endringIOpptjening.utbetaltPerMaaned,
                    saerkullsbarn = saerkullsbarnTillegg.harSaerkullsbarnInnvilget_safe.notNull(),
                    ufoeretrygd = endringIOpptjeningBoolean.harUtbetalingsgrad,
                )
            )

            includePhrase(
                Ufoeretrygd.UtbetalingsdatoUfoeretrygd(harUfoeretrygdUtbetalt = endringIOpptjeningBoolean.harUfoeretrygdUtbetalt)
            )

            includePhrase(Ufoeretrygd.ViktigAALeseHeleBrevet)
            includePhrase(Vedtak.BegrunnelseOverskrift)
            includePhrase(EndringIOpptjening.UfoerInntektListe)

            includePhrase(
                EndringIOpptjening.EndringIOpptjeningTilUfoeretrygd(
                    harBeloepOekt = endringIOpptjeningBoolean.harBeloepOekt,
                    harBeloepRedusert = endringIOpptjeningBoolean.harBeloepRedusert,
                    virkningsDato = virkningsDato,
                )
            )

            includePhrase(Ufoeretrygd.HenvisningTilVedleggOpplysningerOmBeregningenUfoer)

            includePhrase(
                HjemlerFolketrygdloven.Folketrygdloven(
                    harEktefelletilleggInnvilget = harEktefelletilleggInnvilget.notNull(),
                    harFellesbarntilleggInnvilget = fellesbarnTillegg.harFellesbarnInnvilget_safe.notNull(),
                    harGjenlevendetilleggInnvilget = harGjenlevendetilleggInnvilget.notNull(),
                    harYrkesskadeOppfylt = harYrkesskadeOppfylt.notNull(),
                    harSaerkullsbarntilleggInnvilget = saerkullsbarnTillegg.harSaerkullsbarnInnvilget_safe.notNull(),
                )
            )

            includePhrase(
                KombinereUfoeretrygdMedInntekt.KombinereUfoeretrygdOgInntektOverskrift(
                    harDelvisUfoeregrad = endringIOpptjeningBoolean.harDelvisUfoeregrad,
                    harFullUfoeregrad = endringIOpptjeningBoolean.harFullUfoeregrad,
                    harFullUtbetalingsgrad = endringIOpptjeningBoolean.harFullUtbetalingsgrad,
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
                    harFullUfoeregrad = endringIOpptjeningBoolean.harFullUfoeregrad,
                    harInntektEtterUfoere = endringIOpptjeningBoolean.harInntektEtterUfoere,
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
                    harInntektEtterUfoere = endringIOpptjeningBoolean.harInntektEtterUfoere,
                    harFullUfoeregrad = endringIOpptjeningBoolean.harFullUfoeregrad,
                    harDelvisUfoeregrad = endringIOpptjeningBoolean.harDelvisUfoeregrad,
                )
            )
            ifNotNull(
                endringIOpptjeningBeregning.oppjustertInntektFoerUfoere80prosent_safe,
                endringIOpptjeningBeregning.nettoAkkumulertePlussNettoRestAar_safe
            ) { oppjustertInntekt, nettoAkkumulerte ->
                includePhrase(
                    KombinereUfoeretrygdMedInntekt.Kompensasjonsgrad(
                        inntektsgrense = endringIOpptjening.inntektsgrense,
                        inntektsgrenseNesteAar = endringIOpptjening.inntektsgrenseNesteAar,
                        kompensasjonsgrad = endringIOpptjening.kompensasjonsgrad,
                        oppjustertInntektFoerUfoere80prosent = oppjustertInntekt,
                    )
                )

                includePhrase(
                    KombinereUfoeretrygdMedInntekt.OekeUfoereUtbetalingForRestenAvKalenderAaret(
                        forventetInntekt = endringIOpptjening.forventetInntekt,
                        harBeloepOekt = endringIOpptjeningBoolean.harBeloepOekt,
                        harBeloepRedusert = endringIOpptjeningBoolean.harBeloepRedusert,
                        inntektsgrense = endringIOpptjening.inntektsgrense,
                        inntektstak = endringIOpptjening.inntektstak,
                        oppjustertInntektFoerUfoere80prosent = oppjustertInntekt,
                        ufoeregrad = endringIOpptjening.ufoeregrad,
                        utbetalingsgrad = endringIOpptjening.utbetalingsgrad,
                    )
                )

                includePhrase(
                    KombinereUfoeretrygdMedInntekt.ReduksjonAvInntektUfoere(
                        inntektsgrense = endringIOpptjening.inntektsgrense,
                        nettoAkkumulerteBeloepUtbetalt = endringIOpptjening.nettoAkkumulerteBeloepUtbetalt,
                        nettoAkkumulertePlussNettoRestAar = nettoAkkumulerte,
                        nettoUfoeretrygdUtbetaltPerMaaned = endringIOpptjening.nettoUfoeretrygdUtbetaltPerMaaned,
                        oppjustertInntektFoerUfoere80prosent = oppjustertInntekt,
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
                        oppjustertInntektFoerUfoere80prosent = oppjustertInntekt,
                        ufoeregrad = endringIOpptjening.ufoeregrad,
                        utbetalingsgrad = endringIOpptjening.utbetalingsgrad,
                    )
                )
            }
            ifNotNull(
                fellesbarnTillegg, saerkullsbarnTillegg
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
                    harGjenlevendetilleggInnvilget = harGjenlevendetilleggInnvilget.notNull(),
                    inntektsgrense = endringIOpptjening.inntektsgrense,
                )
            )

            includePhrase(
                EndringIOpptjening.EtterbetalingAvUfoeretrygd(
                    harBeloepOekt = endringIOpptjeningBoolean.harBeloepOekt,
                    ufoeregrad = endringIOpptjening.ufoeregrad,
                    utbetalingsgrad = endringIOpptjening.utbetalingsgrad,
                    virkningsDato = virkningsDato

                )
            )
            includePhrase(
                EndringIOpptjening.TilbakekrevingAvUfoeretrygd(
                    harBeloepRedusert = endringIOpptjeningBoolean.harBeloepRedusert
                )
            )
            includePhrase(Ufoeretrygd.MeldeFraOmEndringer)
            includePhrase(Ufoeretrygd.RettTilAAKlage)
            includePhrase(Felles.RettTilInnsynPesys)
            includePhrase(Ufoeretrygd.SjekkUtbetalingene)
            includePhrase(Ufoeretrygd.Skattekort)

            includePhrase(
                Ufoeretrygd.SkattForDegSomBorIUtlandet(
                    brukerBorInorge = this.endringIOpptjeningBoolean.brukerBorInorge
                )
            )
        }


        includeAttachment(vedleggMaanedligUfoeretrygdFoerSkatt, maanedligUfoeretrygdFoerSkatt)

        includeAttachment(
            createVedleggOpplysningerBruktIBeregningUT(
                skalViseMinsteytelse = false,
                skalViseBarnetillegg = true,
                skalViseGjenlevendetillegg = false
                ), opplysningerBruktIBeregningUT
        )

        includeAttachment(vedleggDineRettigheterOgPlikterUfoere, orienteringOmRettigheterUfoere)

    }

}