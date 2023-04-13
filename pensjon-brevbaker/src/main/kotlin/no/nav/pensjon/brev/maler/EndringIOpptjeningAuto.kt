package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.maler.AvkortningsinformasjonSelectors.beloepsgrense
import no.nav.pensjon.brev.api.model.maler.AvkortningsinformasjonSelectors.brukerBorInorge
import no.nav.pensjon.brev.api.model.maler.AvkortningsinformasjonSelectors.forventetInntekt
import no.nav.pensjon.brev.api.model.maler.AvkortningsinformasjonSelectors.grunnbeloep
import no.nav.pensjon.brev.api.model.maler.AvkortningsinformasjonSelectors.harBeloepOekt
import no.nav.pensjon.brev.api.model.maler.AvkortningsinformasjonSelectors.harBeloepRedusert
import no.nav.pensjon.brev.api.model.maler.AvkortningsinformasjonSelectors.harDelvisUfoeregrad
import no.nav.pensjon.brev.api.model.maler.AvkortningsinformasjonSelectors.harFullUfoeregrad
import no.nav.pensjon.brev.api.model.maler.AvkortningsinformasjonSelectors.harFullUtbetalingsgrad
import no.nav.pensjon.brev.api.model.maler.AvkortningsinformasjonSelectors.harInntektEtterUfoere
import no.nav.pensjon.brev.api.model.maler.AvkortningsinformasjonSelectors.harUfoeretrygdUtbetalt
import no.nav.pensjon.brev.api.model.maler.AvkortningsinformasjonSelectors.harUtbetalingsgrad
import no.nav.pensjon.brev.api.model.maler.AvkortningsinformasjonSelectors.inntektsgrense
import no.nav.pensjon.brev.api.model.maler.AvkortningsinformasjonSelectors.inntektsgrenseNesteAar
import no.nav.pensjon.brev.api.model.maler.AvkortningsinformasjonSelectors.inntektstak
import no.nav.pensjon.brev.api.model.maler.AvkortningsinformasjonSelectors.kompensasjonsgrad
import no.nav.pensjon.brev.api.model.maler.AvkortningsinformasjonSelectors.nettoAkkumulerteBeloepUtbetalt
import no.nav.pensjon.brev.api.model.maler.AvkortningsinformasjonSelectors.nettoAkkumulertePlussNettoRestAar_safe
import no.nav.pensjon.brev.api.model.maler.AvkortningsinformasjonSelectors.nettoUfoeretrygdUtbetaltPerMaaned
import no.nav.pensjon.brev.api.model.maler.AvkortningsinformasjonSelectors.oppjustertInntektEtterUfoere
import no.nav.pensjon.brev.api.model.maler.AvkortningsinformasjonSelectors.oppjustertInntektFoerUfoere80prosent_safe
import no.nav.pensjon.brev.api.model.maler.AvkortningsinformasjonSelectors.ufoeregrad
import no.nav.pensjon.brev.api.model.maler.AvkortningsinformasjonSelectors.utbetalingsgrad
import no.nav.pensjon.brev.api.model.maler.AvkortningsinformasjonSelectors.utbetaltPerMaaned
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDto
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDtoSelectors.avkortningsinformasjon
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
                    harBeloepOekt = avkortningsinformasjon.harBeloepOekt,
                    harBeloepRedusert = avkortningsinformasjon.harBeloepRedusert,
                    virkningsDato = virkningsDato,
                )
            )

            includePhrase(
                Ufoeretrygd.Beloep(
                    ektefelle = harEktefelletilleggInnvilget.notNull(),
                    fellesbarn = fellesbarnTillegg.harFellesbarnInnvilget_safe.notNull(),
                    gjenlevende = harGjenlevendetilleggInnvilget.notNull(),
                    perMaaned = avkortningsinformasjon.utbetaltPerMaaned,
                    saerkullsbarn = saerkullsbarnTillegg.harSaerkullsbarnInnvilget_safe.notNull(),
                    ufoeretrygd = avkortningsinformasjon.harUtbetalingsgrad,
                )
            )

            includePhrase(
                Ufoeretrygd.UtbetalingsdatoUfoeretrygd(harUfoeretrygdUtbetalt = avkortningsinformasjon.harUfoeretrygdUtbetalt)
            )

            includePhrase(Ufoeretrygd.ViktigAALeseHeleBrevet)
            includePhrase(Vedtak.BegrunnelseOverskrift)
            includePhrase(EndringIOpptjening.UfoerInntektListe)

            includePhrase(
                EndringIOpptjening.EndringIOpptjeningTilUfoeretrygd(
                    harBeloepOekt = avkortningsinformasjon.harBeloepOekt,
                    harBeloepRedusert = avkortningsinformasjon.harBeloepRedusert,
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
                    harDelvisUfoeregrad = avkortningsinformasjon.harDelvisUfoeregrad,
                    harFullUfoeregrad = avkortningsinformasjon.harFullUfoeregrad,
                    harFullUtbetalingsgrad = avkortningsinformasjon.harFullUtbetalingsgrad,
                )
            )

            includePhrase(
                KombinereUfoeretrygdMedInntekt.InntektVedSidenAvUfoeretrygd(
                    ufoeregrad = avkortningsinformasjon.ufoeregrad,
                    utbetalingsgrad = avkortningsinformasjon.utbetalingsgrad,
                )
            )

            includePhrase(
                KombinereUfoeretrygdMedInntekt.Inntektsgrense(
                    beloepsgrense = avkortningsinformasjon.beloepsgrense,
                    grunnbeloep = avkortningsinformasjon.grunnbeloep,
                    harFullUfoeregrad = avkortningsinformasjon.harFullUfoeregrad,
                    harInntektEtterUfoere = avkortningsinformasjon.harInntektEtterUfoere,
                    inntektsgrense = avkortningsinformasjon.inntektsgrense,
                    inntektsgrenseNesteAar = avkortningsinformasjon.inntektsgrenseNesteAar,
                    ufoeregrad = avkortningsinformasjon.ufoeregrad,
                )
            )

            includePhrase(
                KombinereUfoeretrygdMedInntekt.InntektsgrenseLagtTilGrunn(
                    inntektsgrense = avkortningsinformasjon.inntektsgrense,
                    inntektsgrenseNesteAar = avkortningsinformasjon.inntektsgrenseNesteAar,
                    beloepsgrense = avkortningsinformasjon.beloepsgrense,
                    oppjustertInntektEtterUfoere = avkortningsinformasjon.oppjustertInntektEtterUfoere,
                    grunnbeloep = avkortningsinformasjon.grunnbeloep,
                    ufoeregrad = avkortningsinformasjon.ufoeregrad,
                    harInntektEtterUfoere = avkortningsinformasjon.harInntektEtterUfoere,
                    harFullUfoeregrad = avkortningsinformasjon.harFullUfoeregrad,
                    harDelvisUfoeregrad = avkortningsinformasjon.harDelvisUfoeregrad,
                )
            )
            ifNotNull(
                avkortningsinformasjon.oppjustertInntektFoerUfoere80prosent_safe,
                avkortningsinformasjon.nettoAkkumulertePlussNettoRestAar_safe
            ) { oppjustertInntekt, nettoAkkumulerte ->
                includePhrase(
                    KombinereUfoeretrygdMedInntekt.Kompensasjonsgrad(
                        inntektsgrense = avkortningsinformasjon.inntektsgrense,
                        inntektsgrenseNesteAar = avkortningsinformasjon.inntektsgrenseNesteAar,
                        kompensasjonsgrad = avkortningsinformasjon.kompensasjonsgrad,
                        oppjustertInntektFoerUfoere80prosent = oppjustertInntekt,
                    )
                )

                includePhrase(
                    KombinereUfoeretrygdMedInntekt.OekeUfoereUtbetalingForRestenAvKalenderAaret(
                        forventetInntekt = avkortningsinformasjon.forventetInntekt,
                        harBeloepOekt = avkortningsinformasjon.harBeloepOekt,
                        harBeloepRedusert = avkortningsinformasjon.harBeloepRedusert,
                        inntektsgrense = avkortningsinformasjon.inntektsgrense,
                        inntektstak = avkortningsinformasjon.inntektstak,
                        oppjustertInntektFoerUfoere80prosent = oppjustertInntekt,
                        ufoeregrad = avkortningsinformasjon.ufoeregrad,
                        utbetalingsgrad = avkortningsinformasjon.utbetalingsgrad,
                    )
                )

                includePhrase(
                    KombinereUfoeretrygdMedInntekt.ReduksjonAvInntektUfoere(
                        inntektsgrense = avkortningsinformasjon.inntektsgrense,
                        nettoAkkumulerteBeloepUtbetalt = avkortningsinformasjon.nettoAkkumulerteBeloepUtbetalt,
                        nettoAkkumulertePlussNettoRestAar = nettoAkkumulerte,
                        nettoUfoeretrygdUtbetaltPerMaaned = avkortningsinformasjon.nettoUfoeretrygdUtbetaltPerMaaned,
                        oppjustertInntektFoerUfoere80prosent = oppjustertInntekt,
                        ufoeregrad = avkortningsinformasjon.ufoeregrad,
                        utbetalingsgrad = avkortningsinformasjon.utbetalingsgrad,
                    )
                )

                includePhrase(
                    KombinereUfoeretrygdMedInntekt.BeholderUfoeregraden(
                        ufoeregrad = avkortningsinformasjon.ufoeregrad
                    )
                )

                showIf(avkortningsinformasjon.utbetalingsgrad.format().equalTo(avkortningsinformasjon.ufoeregrad.format()))
                {
                    includePhrase(Ufoeretrygd.MeldeFraOmEventuellInntektOverskrift)
                    includePhrase(Ufoeretrygd.MeldeFraOmEventuellInntekt)
                }

                includePhrase(
                    KombinereUfoeretrygdMedInntekt.MeldeFraOmEndringerIInntekten(
                        forventetInntekt = avkortningsinformasjon.forventetInntekt,
                        inntektsgrense = avkortningsinformasjon.inntektsgrense,
                        inntektstak = avkortningsinformasjon.inntektstak,
                        oppjustertInntektFoerUfoere80prosent = oppjustertInntekt,
                        ufoeregrad = avkortningsinformasjon.ufoeregrad,
                        utbetalingsgrad = avkortningsinformasjon.utbetalingsgrad,
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
                            grunnbeloep = avkortningsinformasjon.grunnbeloep,
                            sivilstand = sivilstand
                        )
                    )
                }
            }

            includePhrase(
                Gjenlevendetillegg.HarGjenlevendetillegg(
                    forventetInntekt = avkortningsinformasjon.forventetInntekt,
                    harGjenlevendetilleggInnvilget = harGjenlevendetilleggInnvilget.notNull(),
                    inntektsgrense = avkortningsinformasjon.inntektsgrense,
                )
            )

            includePhrase(
                EndringIOpptjening.EtterbetalingAvUfoeretrygd(
                    harBeloepOekt = avkortningsinformasjon.harBeloepOekt,
                    ufoeregrad = avkortningsinformasjon.ufoeregrad,
                    utbetalingsgrad = avkortningsinformasjon.utbetalingsgrad,
                    virkningsDato = virkningsDato

                )
            )
            includePhrase(
                EndringIOpptjening.TilbakekrevingAvUfoeretrygd(
                    harBeloepRedusert = avkortningsinformasjon.harBeloepRedusert
                )
            )
            includePhrase(Ufoeretrygd.MeldeFraOmEndringer)
            includePhrase(Ufoeretrygd.RettTilAAKlage)
            includePhrase(Felles.RettTilInnsynPesys)
            includePhrase(Ufoeretrygd.SjekkUtbetalingene)
            includePhrase(Ufoeretrygd.Skattekort)

            includePhrase(
                Ufoeretrygd.SkattForDegSomBorIUtlandet(
                    brukerBorInorge = this.avkortningsinformasjon.brukerBorInorge
                )
            )
        }


        includeAttachment(vedleggMaanedligUfoeretrygdFoerSkatt, maanedligUfoeretrygdFoerSkatt)

        includeAttachment(
            createVedleggOpplysningerBruktIBeregningUT(
                skalViseMinsteytelse = false,
                skalViseBarnetillegg = true,
                skalViseAvdoed = true,
                skalViseSlikBeregnerViUfoeretrygdenDin = true,
                skalViseTabellInntekteneBruktIBeregningen = true,
                skalViseTabellInntekteneBruktIBeregningenAvdoed = true,
                skalViseSlikBeregnerViGjenlevendetillegg = true,
                skalViseForDegSomMottarEktefelletillegg = true,
                skalViseEtteroppgjoerAvUfoeretrygdOgBarnetillegg = true,
                ), opplysningerBruktIBeregningUT
        )

        includeAttachment(vedleggDineRettigheterOgPlikterUfoere, orienteringOmRettigheterUfoere)

    }

}