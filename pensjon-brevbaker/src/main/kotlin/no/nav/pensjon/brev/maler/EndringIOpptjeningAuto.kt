package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.maler.AvkortningsinformasjonSelectors.forventetInntekt
import no.nav.pensjon.brev.api.model.maler.AvkortningsinformasjonSelectors.harUtbetalingsgrad
import no.nav.pensjon.brev.api.model.maler.AvkortningsinformasjonSelectors.inntektsgrense
import no.nav.pensjon.brev.api.model.maler.AvkortningsinformasjonSelectors.utbetalingsgrad
import no.nav.pensjon.brev.api.model.maler.BeregningUfoereEndringIOpptjeningSelectors.grunnbeloep
import no.nav.pensjon.brev.api.model.maler.BeregningUfoereEndringIOpptjeningSelectors.harBeloepOekt
import no.nav.pensjon.brev.api.model.maler.BeregningUfoereEndringIOpptjeningSelectors.harBeloepRedusert
import no.nav.pensjon.brev.api.model.maler.BeregningUfoereEndringIOpptjeningSelectors.harUfoeretrygdUtbetalt
import no.nav.pensjon.brev.api.model.maler.BeregningUfoereEndringIOpptjeningSelectors.ufoeregrad
import no.nav.pensjon.brev.api.model.maler.BeregningUfoereEndringIOpptjeningSelectors.utbetaltPerMaaned
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDto
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDtoSelectors.avkortningsinformasjon
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDtoSelectors.beregningUfoereEndringIOpptjening
import no.nav.pensjon.brev.api.model.maler.EndringIOpptjeningAutoDtoSelectors.brukerBorInorge
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
                    harBeloepOekt = beregningUfoereEndringIOpptjening.harBeloepOekt,
                    harBeloepRedusert = beregningUfoereEndringIOpptjening.harBeloepRedusert,
                    virkningsDato = virkningsDato,
                )
            )

            includePhrase(
                Ufoeretrygd.Beloep(
                    ektefelle = harEktefelletilleggInnvilget.notNull(),
                    fellesbarn = fellesbarnTillegg.harFellesbarnInnvilget_safe.notNull(),
                    gjenlevende = harGjenlevendetilleggInnvilget.notNull(),
                    perMaaned = beregningUfoereEndringIOpptjening.utbetaltPerMaaned,
                    saerkullsbarn = saerkullsbarnTillegg.harSaerkullsbarnInnvilget_safe.notNull(),
                    ufoeretrygd = avkortningsinformasjon.harUtbetalingsgrad,
                )
            )

            includePhrase(
                Ufoeretrygd.UtbetalingsdatoUfoeretrygd(harUfoeretrygdUtbetalt = beregningUfoereEndringIOpptjening.harUfoeretrygdUtbetalt)
            )

            includePhrase(Ufoeretrygd.ViktigAALeseHeleBrevet)
            includePhrase(Vedtak.BegrunnelseOverskrift)
            includePhrase(EndringIOpptjening.UfoerInntektListe)

            includePhrase(
                EndringIOpptjening.EndringIOpptjeningTilUfoeretrygd(
                    harBeloepOekt = beregningUfoereEndringIOpptjening.harBeloepOekt,
                    harBeloepRedusert = beregningUfoereEndringIOpptjening.harBeloepRedusert,
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
                    avkortningsinformasjon = avkortningsinformasjon,
                    beregningUfoereEndringIOpptjening = beregningUfoereEndringIOpptjening
                )
            )

            includePhrase(
                KombinereUfoeretrygdMedInntekt.InntektVedSidenAvUfoeretrygd(
                    avkortningsinformasjon = avkortningsinformasjon,
                    beregningUfoereEndringIOpptjening = beregningUfoereEndringIOpptjening
                )
            )

            includePhrase(
                KombinereUfoeretrygdMedInntekt.Inntektsgrense(
                    avkortningsinformasjon = avkortningsinformasjon,
                    beregningUfoereEndringIOpptjening = beregningUfoereEndringIOpptjening
                )
            )

            includePhrase(
                KombinereUfoeretrygdMedInntekt.InntektsgrenseLagtTilGrunn(
                    avkortningsinformasjon = avkortningsinformasjon,
                    beregningUfoereEndringIOpptjening = beregningUfoereEndringIOpptjening
                )
            )

            includePhrase(
                KombinereUfoeretrygdMedInntekt.Kompensasjonsgrad(
                    avkortningsinformasjon = avkortningsinformasjon,
                    beregningUfoereEndringIOpptjening = beregningUfoereEndringIOpptjening
                )
            )

            includePhrase(
                KombinereUfoeretrygdMedInntekt.OekeUfoereUtbetalingForRestenAvKalenderAaret(
                    avkortningsinformasjon = avkortningsinformasjon,
                    beregningUfoereEndringIOpptjening = beregningUfoereEndringIOpptjening
                )
            )

            includePhrase(
                KombinereUfoeretrygdMedInntekt.ReduksjonAvInntektUfoere(
                    avkortningsinformasjon = avkortningsinformasjon,
                    beregningUfoereEndringIOpptjening = beregningUfoereEndringIOpptjening
                )
            )

            includePhrase(
                KombinereUfoeretrygdMedInntekt.BeholderUfoeregraden(
                    ufoeregrad = beregningUfoereEndringIOpptjening.ufoeregrad
                )
            )

            showIf(
                avkortningsinformasjon.utbetalingsgrad.format()
                    .equalTo(beregningUfoereEndringIOpptjening.ufoeregrad.format())
            )
            {
                includePhrase(Ufoeretrygd.MeldeFraOmEventuellInntektOverskrift)
                includePhrase(Ufoeretrygd.MeldeFraOmEventuellInntekt)
            }

            includePhrase(
                KombinereUfoeretrygdMedInntekt.MeldeFraOmEndringerIInntekten(
                    avkortningsinformasjon = avkortningsinformasjon,
                    beregningUfoereEndringIOpptjening = beregningUfoereEndringIOpptjening
                )
            )

        ifNotNull(
            fellesbarnTillegg, saerkullsbarnTillegg
        ) { fellesbarnTillegg, saerkullsbarnTillegg ->
            showIf(fellesbarnTillegg.harFellesbarnInnvilget or saerkullsbarnTillegg.harSaerkullsbarnInnvilget) {
                includePhrase(
                    BarnetilleggEndringIOpptjening(
                        barnetilleggFellesbarn = fellesbarnTillegg,
                        barnetilleggSaerkullsbarn = saerkullsbarnTillegg,
                        grunnbeloep = beregningUfoereEndringIOpptjening.grunnbeloep,
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
                harBeloepOekt = beregningUfoereEndringIOpptjening.harBeloepOekt,
                ufoeregrad = beregningUfoereEndringIOpptjening.ufoeregrad,
                utbetalingsgrad = avkortningsinformasjon.utbetalingsgrad,
                virkningsDato = virkningsDato

            )
        )
        includePhrase(
            EndringIOpptjening.TilbakekrevingAvUfoeretrygd(
                harBeloepRedusert = beregningUfoereEndringIOpptjening.harBeloepRedusert
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
    skalViseSlikBeregnerViGjenlevendetillegg = true,
    skalViseForDegSomMottarEktefelletillegg = true,
    skalViseEtteroppgjoerAvUfoeretrygdOgBarnetillegg = true,
    ), opplysningerBruktIBeregningUT
    )

    includeAttachment(vedleggDineRettigheterOgPlikterUfoere, orienteringOmRettigheterUfoere)

}

}