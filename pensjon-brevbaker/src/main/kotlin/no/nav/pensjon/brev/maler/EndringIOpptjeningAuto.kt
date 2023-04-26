package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.maler.AvkortningsinformasjonSelectors.forventetInntekt
import no.nav.pensjon.brev.api.model.maler.AvkortningsinformasjonSelectors.inntektsgrense
import no.nav.pensjon.brev.api.model.maler.AvkortningsinformasjonSelectors.utbetalingsgrad
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDto
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDtoSelectors.brukerBorInorge
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDtoSelectors.fellesbarn1
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDtoSelectors.grunnbeloep
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDtoSelectors.harEktefelletilleggInnvilget
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDtoSelectors.harGjenlevendetilleggInnvilget
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDtoSelectors.harOektUtbetaling
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDtoSelectors.harRedusertUtbetaling
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDtoSelectors.harYrkesskadeOppfylt
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDtoSelectors.maanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDtoSelectors.opplysningerBruktIBeregningUT
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDtoSelectors.orienteringOmRettigheterUfoere
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDtoSelectors.saerkullsbarn1
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDtoSelectors.sivilstand
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDtoSelectors.ufoeregrad
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDtoSelectors.ufoeretrygdOrdinaer1
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDtoSelectors.ufoertrygdUtbetalt
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDtoSelectors.utbetaltPerMaaned
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDtoSelectors.virkningsDato
import no.nav.pensjon.brev.api.model.maler.Fellesbarn1Selectors.harFellesbarnInnvilget
import no.nav.pensjon.brev.api.model.maler.Fellesbarn1Selectors.harFellesbarnInnvilget_safe
import no.nav.pensjon.brev.api.model.maler.Saerkullsbarn1Selectors.harSaerkullsbarnInnvilget
import no.nav.pensjon.brev.api.model.maler.Saerkullsbarn1Selectors.harSaerkullsbarnInnvilget_safe
import no.nav.pensjon.brev.api.model.maler.UfoeretrygdOrdinaer1Selectors.avkortningsinformasjon
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
                    harOektUtbetaling = harOektUtbetaling,
                    harRedusertUtbetaling = harRedusertUtbetaling,
                    virkningsDato = virkningsDato,
                )
            )

            includePhrase(
                Ufoeretrygd.Beloep(
                    ektefelle = harEktefelletilleggInnvilget.notNull(),
                    fellesbarn = fellesbarn1.harFellesbarnInnvilget_safe.notNull(),
                    gjenlevende = harGjenlevendetilleggInnvilget.notNull(),
                    perMaaned = utbetaltPerMaaned,
                    saerkullsbarn = saerkullsbarn1.harSaerkullsbarnInnvilget_safe.notNull(),
                    ufoeretrygd = ufoertrygdUtbetalt.greaterThan(0),
                )
            )

            showIf(utbetaltPerMaaned.greaterThan(0)) {
                includePhrase(Ufoeretrygd.UtbetalingsdatoUfoeretrygd)
            }

            includePhrase(Ufoeretrygd.ViktigAALeseHeleBrevet)
            includePhrase(Vedtak.BegrunnelseOverskrift)
            includePhrase(EndringIOpptjening.UfoerInntektListe)

            includePhrase(
                EndringIOpptjening.EndringIOpptjeningTilUfoeretrygd(
                    harOektUtbetaling = harOektUtbetaling,
                    harRedusertUtbetaling = harRedusertUtbetaling,
                    virkningsDato = virkningsDato,
                )
            )

            includePhrase(Ufoeretrygd.HenvisningTilVedleggOpplysningerOmBeregningenUfoer)

            includePhrase(
                HjemlerFolketrygdloven.Folketrygdloven(
                    harEktefelletilleggInnvilget = harEktefelletilleggInnvilget.notNull(),
                    harFellesbarntilleggInnvilget = fellesbarn1.harFellesbarnInnvilget_safe.notNull(),
                    harGjenlevendetilleggInnvilget = harGjenlevendetilleggInnvilget.notNull(),
                    harYrkesskadeOppfylt = harYrkesskadeOppfylt.notNull(),
                    harSaerkullsbarntilleggInnvilget = saerkullsbarn1.harSaerkullsbarnInnvilget_safe.notNull(),
                )
            )

            includePhrase(
                KombinereUfoeretrygdMedInntekt.KombinereUfoeretrygdOgInntektOverskrift(
                    avkortningsinformasjon = ufoeretrygdOrdinaer1.avkortningsinformasjon,
                    ufoeregrad = ufoeregrad
                )
            )

            includePhrase(
                KombinereUfoeretrygdMedInntekt.InntektVedSidenAvUfoeretrygd(
                    avkortningsinformasjon = ufoeretrygdOrdinaer1.avkortningsinformasjon,
                    ufoeregrad = ufoeregrad
                )
            )

            includePhrase(
                KombinereUfoeretrygdMedInntekt.Inntektsgrense(
                    avkortningsinformasjon = ufoeretrygdOrdinaer1.avkortningsinformasjon,
                    grunnbeloep = grunnbeloep,
                    ufoeregrad = ufoeregrad
                )
            )

            includePhrase(
                KombinereUfoeretrygdMedInntekt.InntektsgrenseLagtTilGrunn(
                    avkortningsinformasjon = ufoeretrygdOrdinaer1.avkortningsinformasjon,
                    grunnbeloep = grunnbeloep,
                    ufoeregrad = ufoeregrad
                )
            )

            includePhrase(
                KombinereUfoeretrygdMedInntekt.Kompensasjonsgrad(avkortningsinformasjon = ufoeretrygdOrdinaer1.avkortningsinformasjon)
            )

            includePhrase(
                KombinereUfoeretrygdMedInntekt.OekeUfoereUtbetalingForRestenAvKalenderAaret(
                    avkortningsinformasjon = ufoeretrygdOrdinaer1.avkortningsinformasjon,
                    harOektUtbetaling = harOektUtbetaling,
                    harRedusertUtbetaling = harRedusertUtbetaling,
                    ufoeregrad = ufoeregrad
                )
            )

            includePhrase(
                KombinereUfoeretrygdMedInntekt.ReduksjonAvInntektUfoere(
                    avkortningsinformasjon = ufoeretrygdOrdinaer1.avkortningsinformasjon,
                    ufoeregrad = ufoeregrad,
                    ufoeretrygdOrdinaer1 = ufoeretrygdOrdinaer1
                )
            )

            includePhrase(
                KombinereUfoeretrygdMedInntekt.BeholderUfoeregraden(ufoeregrad = ufoeregrad)
            )

            showIf(
                ufoeretrygdOrdinaer1.avkortningsinformasjon.utbetalingsgrad.format().equalTo(ufoeregrad.format())
            )
            {
                includePhrase(Ufoeretrygd.MeldeFraOmEventuellInntektOverskrift)
                includePhrase(Ufoeretrygd.MeldeFraOmEventuellInntekt)
            }

            includePhrase(
                KombinereUfoeretrygdMedInntekt.MeldeFraOmEndringerIInntekten(
                    avkortningsinformasjon = ufoeretrygdOrdinaer1.avkortningsinformasjon,
                    ufoeregrad = ufoeregrad
                )
            )

            ifNotNull(
                fellesbarn1, saerkullsbarn1
            ) { fellesbarnTillegg, saerkullsbarnTillegg ->
                showIf(fellesbarnTillegg.harFellesbarnInnvilget or saerkullsbarnTillegg.harSaerkullsbarnInnvilget) {
                    includePhrase(
                        BarnetilleggEndringIOpptjening(
                            barnetilleggFellesbarn = fellesbarnTillegg,
                            barnetilleggSaerkullsbarn = saerkullsbarnTillegg,
                            grunnbeloep = grunnbeloep,
                            sivilstand = sivilstand
                        )
                    )
                }
            }

            includePhrase(
                Gjenlevendetillegg.HarGjenlevendetillegg(
                    forventetInntekt = ufoeretrygdOrdinaer1.avkortningsinformasjon.forventetInntekt,
                    harGjenlevendetilleggInnvilget = harGjenlevendetilleggInnvilget.notNull(),
                    inntektsgrense = ufoeretrygdOrdinaer1.avkortningsinformasjon.inntektsgrense,
                )
            )

            includePhrase(
                EndringIOpptjening.EtterbetalingAvUfoeretrygd(
                    avkortningsinformasjon = ufoeretrygdOrdinaer1.avkortningsinformasjon,
                    harOektUtbetaling = harOektUtbetaling,
                    ufoeregrad = ufoeregrad,
                    virkningsDato = virkningsDato

                )
            )
            includePhrase(
                EndringIOpptjening.TilbakekrevingAvUfoeretrygd(
                    harOektUtbetaling = harOektUtbetaling
                )
            )
            includePhrase(Ufoeretrygd.MeldeFraOmEndringer)
            includePhrase(Ufoeretrygd.RettTilAAKlage)
            includePhrase(Felles.RettTilInnsynPesys)
            includePhrase(Ufoeretrygd.SjekkUtbetalingene)
            includePhrase(Ufoeretrygd.Skattekort)

            includePhrase(
                Ufoeretrygd.SkattForDegSomBorIUtlandet(
                    brukerBorInorge = this.brukerBorInorge
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
                skalViseSlikBeregnerViGjenlevendetilleggHarNyttTillegg = true,
                skalViseSlikBeregnerViGjenlevendetillegg = true,
                skalViseForDegSomMottarEktefelletillegg = true,
                skalViseEtteroppgjoerAvUfoeretrygdOgBarnetillegg = true,
            ), opplysningerBruktIBeregningUT
        )

        includeAttachment(vedleggDineRettigheterOgPlikterUfoere, orienteringOmRettigheterUfoere)

    }

}