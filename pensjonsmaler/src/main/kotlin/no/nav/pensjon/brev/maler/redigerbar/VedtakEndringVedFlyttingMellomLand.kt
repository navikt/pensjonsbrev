package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.BeloepEndring
import no.nav.pensjon.brev.api.model.EksportForbudKode
import no.nav.pensjon.brev.api.model.EksportForbudKode.FLYKT_ALDER
import no.nav.pensjon.brev.api.model.EksportForbudKode.UFOR25_ALDER
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDto.Aarsak.INNVANDRET
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDto.Aarsak.UTVANDRET
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDto.Opphoersbegrunnelse.BRUKER_FLYTTET_IKKE_AVT_LAND
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.AlderspensjonVedVirkSelectors.erEksportberegnet
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.AlderspensjonVedVirkSelectors.garantipensjonInnvilget
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.AlderspensjonVedVirkSelectors.gjenlevenderettAnvendt
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.AlderspensjonVedVirkSelectors.minstenivaaIndividuellInnvilget
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.AlderspensjonVedVirkSelectors.minstenivaaPensjonistParInnvilget
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.AlderspensjonVedVirkSelectors.pensjonstilleggInnvilget
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.AlderspensjonVedVirkSelectors.totalPensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.AlderspensjonVedVirkSelectors.uforeKombinertMedAlder
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.AlderspensjonVedVirkSelectors.uttaksgrad
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.BeregnetPensjonPerMaanedVedVirkSelectors.grunnnpensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.BrukerSelectors.borIAvtaleland
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.BrukerSelectors.borIEOES
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.BrukerSelectors.faktiskBostedsland
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.BrukerSelectors.faktiskBostedsland_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.InngangOgEksportVurderingAvdoedSelectors.eksportForbudKode_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.InngangOgEksportVurderingAvdoedSelectors.minst20ArBotidKap19_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.InngangOgEksportVurderingAvdoedSelectors.minst20ArTrygdetidKap20_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.InngangOgEksportVurderingSelectors.eksportForbudKode
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.InngangOgEksportVurderingSelectors.eksportTrygdeavtaleAvtaleland
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.InngangOgEksportVurderingSelectors.eksportTrygdeavtaleEOES
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.InngangOgEksportVurderingSelectors.minst20AarTrygdetid
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.KravSelectors.aarsak
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.KravSelectors.virkDatoFom
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.OpphoersbegrunnelseVedVirkSelectors.begrunnelseBT_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.OpphoersbegrunnelseVedVirkSelectors.begrunnelseET_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.VedtakSelectors.erEtterbetaling1Maaned
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.YtelseskomponentInformasjonSelectors.beloepEndring_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.beregnetpensjonPerMaanedVedVirk
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.bruker
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.inngangOgEksportVurdering
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.inngangOgEksportVurderingAvdoed
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.inngangOgEksportVurderingAvdoed_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.krav
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.opphoersbegrunnelseVedVirk_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.vedtak
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.ytelseskomponentInformasjon_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.SaksbehandlerValgSelectors.endringIPensjonen
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.SaksbehandlerValgSelectors.etterbetaling
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.SaksbehandlerValgSelectors.innvandret
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.SaksbehandlerValgSelectors.reduksjonTilbakeITid
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.maler.fraser.alderspensjon.FeilutbetalingAP
import no.nav.pensjon.brev.maler.fraser.alderspensjon.InformasjonOmAlderspensjon
import no.nav.pensjon.brev.maler.fraser.alderspensjon.UfoereAlder
import no.nav.pensjon.brev.maler.fraser.alderspensjon.Utbetalingsinformasjon
import no.nav.pensjon.brev.maler.fraser.alderspensjon.VedtakAlderspensjon
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.isNull
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object VedtakEndringVedFlyttingMellomLand : RedigerbarTemplate<VedtakEndringVedFlyttingMellomLandDto> {
    override val kategori = TemplateDescription.Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.ALDER)
    override val kode = Pesysbrevkoder.Redigerbar.PE_AP_ENDRING_FLYTTING_MELLOM_LAND

    val aarsakUtvandret = UTVANDRET
    override val template = createTemplate(
        name = kode.name,
        letterDataType = VedtakEndringVedFlyttingMellomLandDto::class,
        languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - endring ved flytting mellom land",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        title {
            // nyBeregningAPTittel_001
            textExpr(
                Bokmal to "Vi har beregnet alderspensjonen din på nytt fra ".expr() + pesysData.krav.virkDatoFom.format(),
                Nynorsk to "Vi har berekna alderspensjonen din på nytt frå ".expr() + pesysData.krav.virkDatoFom.format(),
                English to "We have recalculated your retirement pension from ".expr() + pesysData.krav.virkDatoFom.format()
            )
        }

        outline {
            val bostedsland = pesysData.bruker.faktiskBostedsland_safe.ifNull(fritekst("bostedsland"))

            includePhrase(Vedtak.Overskrift)

            ifNotNull(pesysData.bruker.faktiskBostedsland) {
                // flyttingAPmeld_001
                paragraph {
                    textExpr(
                        Bokmal to "Vi har fått melding om at du har flyttet til ".expr() + it + ".",
                        Nynorsk to "Vi har fått melding om at du har flytta til ".expr() + it + ".",
                        English to "We have received notice that you have moved to ".expr() + it + "."
                    )
                }
            }.orShow {
                // flyttingAPNorge_001
                paragraph {
                    text(
                        Bokmal to "Du har flyttet til Norge.",
                        Nynorsk to "Du har flytta til Noreg.",
                        English to "You have moved to Norway."
                    )
                }
            }

            val aarsakUtvandret = pesysData.krav.aarsak.equalTo(aarsakUtvandret)
            showIf(
                aarsakUtvandret and pesysData.alderspensjonVedVirk.erEksportberegnet.equalTo(true)
            ) {
                showIf(pesysData.inngangOgEksportVurdering.eksportForbudKode.equalTo(UFOR25_ALDER)) {
                    // eksportUngUfor_001
                    paragraph {
                        text(
                            Bokmal to "For å ha rett til alderspensjon etter reglene for unge uføre må du være bosatt i Norge. Du har likevel fortsatt rett til pensjon etter din egen opptjening.",
                            Nynorsk to "For å ha rett til alderspensjon etter reglane for unge uføre må du bu i Noreg. Du har likevel framleis rett til pensjon etter di eiga opptening.",
                            English to "To be eligible for retirement pension calculated in accordance with the regulations for young people with disabilities, you have to live in Norway. However, you are still eligible for a pension calculated on the basis of your own accumulated pension rights."
                        )
                    }
                }.orShowIf(pesysData.inngangOgEksportVurdering.eksportForbudKode.equalTo(EksportForbudKode.DOD26_ALDER)) {
                    // eksportUngUforAvdod_001
                    paragraph {
                        text(
                            Bokmal to "Når du flytter til utlandet har du ikke lenger rett til alderspensjon etter reglene som gjelder når avdøde var yngre enn 26 år ved dødsfallet. Du har fortsatt rett til alderspensjon etter din egen og avdødes opptjening.",
                            Nynorsk to "Når du flyttar til utlandet har du ikkje lenger rett til alderspensjon etter reglane som gjeldar når avdøde var yngre enn 26 år ved dødsfallet. Du har framleis rett til pensjon etter di eiga og avdøde si opptening.",
                            English to "When you move abroad, you are no longer eligible for retirement pension calculated in accordance with the regulations as the deceased was younger than 26 years old at the time of death. However, you are still eligible for a pension calculated on the basis of your own and the deceased’s accumulated pension rights."
                        )
                    }
                }
                    .orShowIf(pesysData.inngangOgEksportVurdering.eksportForbudKode.equalTo(FLYKT_ALDER)) {
                        // eksportFlyktning_001
                        paragraph {
                            text(
                                Bokmal to "Når du flytter til et land utenfor EØS-området har du ikke lenger rett til alderspensjon etter reglene for flyktninger. Du har fortsatt rett til pensjon etter din egen opptjening.",
                                Nynorsk to "Når du flyttar til eit land utanfor EØS-området har du ikkje lenger rett til alderspensjon etter reglane for flyktningar. Du har framleis rett til pensjon etter di eiga opptening.",
                                English to "When you move to a country outside the EEA region, you are no longer eligible for retirement pension determined on the basis of the regulations for refugees. However, you are still eligible for a pension calculated on the basis of your own accumulated pension rights."
                            )
                        }
                    }

                showIf(pesysData.inngangOgEksportVurderingAvdoed_safe.eksportForbudKode_safe.equalTo(FLYKT_ALDER)) {}
                // eksportFlyktningAvdod_001
                paragraph {
                    text(
                        Bokmal to "Når du flytter til et land utenfor EØS-området har du ikke lenger rett til alderspensjon etter reglene for flyktninger. Du har fortsatt rett til alderspensjon etter din egen og avdødes opptjening.",
                        Nynorsk to "Når du flyttar til eit land utanfor EØS-området har du ikkje lenger rett til alderspensjon etter reglane for flyktningar. Du har framleis rett til pensjon etter di eiga og avdøde si opptening.",
                        English to "When you move to a country outside the EEA region, you are no longer eligible for retirement pension determined on the basis of the regulations for refugees. However, you are still eligible for a pension calculated on the basis of your own and the deceased’s accumulated pension rights."
                    )
                }

                showIf(
                    pesysData.inngangOgEksportVurdering.eksportForbudKode.isNull() and
                            not(pesysData.inngangOgEksportVurdering.minst20AarTrygdetid) and
                            pesysData.inngangOgEksportVurderingAvdoed.minst20ArTrygdetidKap20_safe.equalTo(true)
                            and pesysData.inngangOgEksportVurderingAvdoed.minst20ArBotidKap19_safe.equalTo(true)
                ) {
                    // eksportAPunder20aar_001
                    paragraph {
                        textExpr(
                            Bokmal to "For å ha rett til full alderspensjon når du bor i ".expr() + bostedsland + ", må du ha vært medlem i folketrygden i minst 20 år. Du har mindre enn 20 års medlemstid og har derfor ikke rett til full pensjon.",
                            Nynorsk to "For å ha rett til full alderspensjon når du bur i ".expr() + bostedsland + ", må du ha vore medlem i folketrygda i minst 20 år. Du har mindre enn 20 års medlemstid og har derfor ikkje rett til full pensjon.",
                            English to "To be eligible for a full retirement pension while living in ".expr() + bostedsland + ", you must have been a member of the National Insurance scheme earning pension rights for at least 20 years. You have been a member for less than 20 years, and are therefore not eligible for a full pension."
                        )
                    }

                    paragraph {
                        text(
                            Bokmal to "I vedleggene finner du mer detaljerte opplysninger.",
                            Nynorsk to "I vedlegga finn du meir detaljerte opplysningar.",
                            English to "There is more detailed information in the attachments."
                        )
                    }
                }.orShowIf(
                    pesysData.inngangOgEksportVurderingAvdoed_safe.eksportForbudKode_safe.isNull() and
                            pesysData.inngangOgEksportVurdering.minst20AarTrygdetid and
                            (pesysData.inngangOgEksportVurderingAvdoed_safe.minst20ArTrygdetidKap20_safe.equalTo(false) or
                                    (pesysData.inngangOgEksportVurderingAvdoed_safe.minst20ArBotidKap19_safe.equalTo(
                                        false
                                    )))
                ) {
                    // eksportAPUnder20aarAvdod_001
                    paragraph {
                        text(
                            Bokmal to "Verken du eller avdøde har vore medlem i folketrygda i minst 20 år. Da har du ikke rett til å få utbetalt hele alderspensjonen din når du flytter til dette landet.",
                            Nynorsk to "Verken du eller avdøde har vært medlem i folketrygden i minst 20 år. Da har du ikkje rett til å få utbetalt heila alderspensjon din når du flyttar til dette landet.",
                            English to "Neither you or the deceased have been a member of the Norwegian National Insurance Scheme for at least 20 years. Therefore, you are not eligible for your full retirement pension when you move to this country."
                        )
                    }
                }
            }

            showIf(
                aarsakUtvandret and
                        pesysData.opphoersbegrunnelseVedVirk_safe.begrunnelseET_safe.equalTo(
                            BRUKER_FLYTTET_IKKE_AVT_LAND
                        ) and
                        not(
                            pesysData.opphoersbegrunnelseVedVirk_safe.begrunnelseBT_safe.equalTo(
                                BRUKER_FLYTTET_IKKE_AVT_LAND
                            )
                        )
            ) {
                // eksportAPET_001
                paragraph {
                    text(
                        Bokmal to "Når du flytter til dette landet har du ikke lenger rett til ektefelletillegg i alderspensjonen. Derfor har vi opphørt ektefelletillegget ditt.",
                        Nynorsk to "Når du flyttar til dette landet har du ikkje lenger rett til ektefelletillegg i alderspensjonen. Derfor har vi opphørt ektefelletillegget ditt.",
                        English to "When you move to this country, you are no longer eligible for a spouse supplement. Therefore, we have stopped your supplement."
                    )
                }
            }.orShowIf(
                aarsakUtvandret and
                        not(
                            pesysData.opphoersbegrunnelseVedVirk_safe.begrunnelseET_safe.equalTo(
                                BRUKER_FLYTTET_IKKE_AVT_LAND
                            )
                        ) and
                        pesysData.opphoersbegrunnelseVedVirk_safe.begrunnelseBT_safe.equalTo(
                            BRUKER_FLYTTET_IKKE_AVT_LAND
                        )
            ) {
                // eksportAPBT_001
                paragraph {
                    text(
                        Bokmal to "Når du flytter til dette landet har du ikke lenger rett til barnetillegg i alderspensjonen din. Derfor har vi opphørt barnetillegget ditt.",
                        Nynorsk to "Når du flyttar til dette landet har du ikkje lenger rett til barnetillegg i alderspensjonen din. Derfor har vi opphørt barnetillegget ditt.",
                        English to "When you move to this country, you are no longer eligible for a child supplement. Therefore, we have stopped your supplement."
                    )
                }
            }.orShowIf(
                aarsakUtvandret and
                        pesysData.opphoersbegrunnelseVedVirk_safe.begrunnelseET_safe.equalTo(
                            BRUKER_FLYTTET_IKKE_AVT_LAND
                        ) and
                        pesysData.opphoersbegrunnelseVedVirk_safe.begrunnelseBT_safe.equalTo(
                            BRUKER_FLYTTET_IKKE_AVT_LAND
                        )
            ) {
                // eksportAPETBT_001
                paragraph {
                    text(
                        Bokmal to "Når du flytter til dette landet har du ikke lenger rett til ektefelle- og barnetillegg i alderspensjonen. Derfor har vi opphørt ektefelle- og barnetillegget ditt.",
                        Nynorsk to "Når du flyttar til dette landet har du ikkje lenger rett til ektefelle- og barnetillegg i alderspensjonen din. Derfor har vi opphørt ektefelle- og barnetillegget ditt.",
                        English to "When you move to this country, you are no longer eligible for spouse and child supplement. Therefore, we have stopped your supplement."
                    )
                }
            }


            // TODO Saksbehandlervalg under data-styring. Kan føre til at valg ikke har noen effekt.
            showIf(
                pesysData.krav.aarsak.equalTo(INNVANDRET) and pesysData.ytelseskomponentInformasjon_safe.beloepEndring_safe.equalTo(
                    BeloepEndring.ENDR_OKT
                ) and saksbehandlerValg.innvandret
            ) {
                // importAPRedusTT_001
                paragraph {
                    text(
                        Bokmal to "Når du bor i Norge har du rett til å få utbetalt hele alderspensjonen din igjen. Derfor har vi beregnet pensjonen din på nytt.",
                        Nynorsk to "Når du bur i Noreg har du rett til å få utbetalt heile alderspensjonen din igjen. Derfor har vi berekna pensjonen din på nytt.",
                        English to "When you live in Norway you are eligible for your full retirement pension. We have therefore recalculated your pension."
                    )
                }

                // importAPUngUfor_001
                paragraph {
                    text(
                        Bokmal to "Når du bor i Norge har du rett til alderspensjon etter reglene for unge uføre igjen. Derfor har vi beregnet pensjonen din på nytt.",
                        Nynorsk to "Når du bur i Noreg har du rett til alderspensjon etter reglane for unge uføre igjen. Derfor har vi berekna pensjonen din på nytt.",
                        English to "When you live in Norway you are eligible for retirement pension calculated in accordance with the regulations for young people with disabilities. We have therefore recalculated your pension."
                    )
                }

                // importAPflyktninger_001
                paragraph {
                    text(
                        Bokmal to "Når du bor i Norge eller et EØS-land har du rett til alderspensjon etter reglene for flyktninger igjen. Derfor har vi beregnet pensjonen din på nytt.",
                        Nynorsk to "Når du bur i Noreg eller eit EØS-land har du rett til alderspensjon etter reglane for flyktningar igjen. Derfor har vi berekna pensjonen din på nytt.",
                        English to "When you live in Norway or an EEA country you are eligible for retirement pension determined on the basis of the regulations for refugees. We have therefore recalculated your pension."
                    )
                }
            }

            val eksportForbudKode = pesysData.inngangOgEksportVurdering.eksportForbudKode
            val eksportForbudKodeAvdoed = pesysData.inngangOgEksportVurderingAvdoed_safe.eksportForbudKode_safe
            showIf(
                aarsakUtvandret and
                        not(
                            pesysData.opphoersbegrunnelseVedVirk_safe.begrunnelseBT_safe.equalTo(
                                BRUKER_FLYTTET_IKKE_AVT_LAND
                            )
                        ) and
                        not(
                            pesysData.opphoersbegrunnelseVedVirk_safe.begrunnelseET_safe.equalTo(
                                BRUKER_FLYTTET_IKKE_AVT_LAND
                            )
                        ) and
                        (eksportForbudKode.equalTo(FLYKT_ALDER) or eksportForbudKode.equalTo(UFOR25_ALDER)
                                or eksportForbudKodeAvdoed.equalTo(FLYKT_ALDER) or eksportForbudKodeAvdoed.equalTo(
                            EksportForbudKode.DOD26_ALDER
                        ))
            ) {
                // omregningAP_001
                paragraph {
                    text(
                        Bokmal to "Derfor har vi beregnet pensjonen din på nytt.",
                        Nynorsk to "Derfor har vi berekna pensjonen din på nytt.",
                        English to "We have therefore recalculated your pension."
                    )
                }
            }

            showIf(
                aarsakUtvandret and
                        pesysData.ytelseskomponentInformasjon_safe.beloepEndring_safe.notEqualTo(BeloepEndring.UENDRET) and
                        pesysData.inngangOgEksportVurdering.eksportForbudKode.isNull() and
                        not(
                            pesysData.opphoersbegrunnelseVedVirk_safe.begrunnelseBT_safe.equalTo(
                                BRUKER_FLYTTET_IKKE_AVT_LAND
                            )
                        ) and
                        not(
                            pesysData.opphoersbegrunnelseVedVirk_safe.begrunnelseET_safe.equalTo(
                                BRUKER_FLYTTET_IKKE_AVT_LAND
                            )
                        )
            ) {
                showIf(
                    pesysData.beregnetpensjonPerMaanedVedVirk.grunnnpensjon.greaterThan(0) and
                            not(pesysData.alderspensjonVedVirk.garantipensjonInnvilget) and
                            not(pesysData.alderspensjonVedVirk.pensjonstilleggInnvilget) and
                            not(pesysData.alderspensjonVedVirk.minstenivaaIndividuellInnvilget) and
                            not(pesysData.alderspensjonVedVirk.minstenivaaPensjonistParInnvilget)
                ) {
                    // omregningGP_001
                    paragraph {
                        text(
                            Bokmal to "Derfor har vi beregnet grunnpensjonen din på nytt.",
                            Nynorsk to "Derfor har vi berekna grunnpensjonen din på nytt.",
                            English to "We have therefore recalculated your basic pension."
                        )
                    }
                }

                showIf(
                    pesysData.ytelseskomponentInformasjon_safe.beloepEndring_safe.notEqualTo(BeloepEndring.UENDRET)
                            and pesysData.beregnetpensjonPerMaanedVedVirk.grunnnpensjon.greaterThan(0)
                ) {
                    paragraph {
                        text(
                            Bokmal to "Derfor har vi beregnet grunnpensjonen",
                            Nynorsk to "Derfor har vi berekna grunnpensjonen",
                            English to "We have therefore recalculated your basic pension"
                        )
                        showIf(
                            not(pesysData.alderspensjonVedVirk.garantipensjonInnvilget) and
                                    pesysData.alderspensjonVedVirk.pensjonstilleggInnvilget and
                                    not(pesysData.alderspensjonVedVirk.minstenivaaIndividuellInnvilget) and
                                    not(pesysData.alderspensjonVedVirk.minstenivaaPensjonistParInnvilget)
                        ) {
                            // omregningGP_PenT_001
                            text(
                                Bokmal to " og pensjonstillegget ditt",
                                Nynorsk to " og pensjonstillegget ditt",
                                English to " and pension supplement"
                            )
                        }
                            .orShowIf(
                                not(pesysData.alderspensjonVedVirk.garantipensjonInnvilget) and
                                        not(pesysData.alderspensjonVedVirk.pensjonstilleggInnvilget) and
                                        (pesysData.alderspensjonVedVirk.minstenivaaIndividuellInnvilget or pesysData.alderspensjonVedVirk.minstenivaaPensjonistParInnvilget)
                            ) {
                                // omregningGP_MNT_001
                                text(
                                    Bokmal to " og minstenivåtillegget ditt",
                                    Nynorsk to " og minstenivåtillegget ditt",
                                    English to " and minimum pension supplement"
                                )
                            }
                            .orShowIf(
                                not(pesysData.alderspensjonVedVirk.garantipensjonInnvilget) and
                                        pesysData.alderspensjonVedVirk.pensjonstilleggInnvilget and
                                        (pesysData.alderspensjonVedVirk.minstenivaaIndividuellInnvilget or pesysData.alderspensjonVedVirk.minstenivaaPensjonistParInnvilget)
                            ) {
                                // omregningGP_PenT_MNT_001
                                text(
                                    Bokmal to ", pensjonstillegget og minstenivåtillegget ditt",
                                    Nynorsk to ", pensjonstillegget og minstenivåtillegget ditt",
                                    English to ", supplementary pension and minimum pension supplement"
                                )
                            }
                            .orShowIf(
                                pesysData.alderspensjonVedVirk.garantipensjonInnvilget and
                                        not(pesysData.alderspensjonVedVirk.pensjonstilleggInnvilget) and
                                        not(pesysData.alderspensjonVedVirk.minstenivaaIndividuellInnvilget) and
                                        not(pesysData.alderspensjonVedVirk.minstenivaaPensjonistParInnvilget)
                            ) {
                                // omregningGP_GarantiPen_001
                                text(
                                    Bokmal to " og garantipensjonen din",
                                    Nynorsk to " og garantipensjonen din",
                                    English to " and guaranteed pension"
                                )
                            }
                            .orShowIf(
                                pesysData.alderspensjonVedVirk.garantipensjonInnvilget and
                                        pesysData.alderspensjonVedVirk.pensjonstilleggInnvilget and
                                        not(pesysData.alderspensjonVedVirk.minstenivaaIndividuellInnvilget) and
                                        not(pesysData.alderspensjonVedVirk.minstenivaaPensjonistParInnvilget)
                            ) {
                                // omregningGP_PenT_GarantiPen_001
                                text(
                                    Bokmal to ", pensjonstillegget og garantipensjonen din",
                                    Nynorsk to ", pensjonstillegget og garantipensjonen din",
                                    English to ", supplementary pension and guaranteed pension"
                                )
                            }
                            .orShowIf(
                                pesysData.alderspensjonVedVirk.garantipensjonInnvilget and
                                        not(pesysData.alderspensjonVedVirk.pensjonstilleggInnvilget) and
                                        (pesysData.alderspensjonVedVirk.minstenivaaIndividuellInnvilget or pesysData.alderspensjonVedVirk.minstenivaaPensjonistParInnvilget)
                            ) {
                                // omregningGP_GarantiPen_MNT_001
                                text(
                                    Bokmal to ", garantipensjonen og minstenivåtillegget ditt",
                                    Nynorsk to ", garantipensjonen og minstenivåtillegget ditt",
                                    English to ", guaranteed pension and minimum pension supplement"
                                )
                            }
                            .orShowIf(
                                pesysData.alderspensjonVedVirk.garantipensjonInnvilget and
                                        pesysData.alderspensjonVedVirk.pensjonstilleggInnvilget and
                                        (pesysData.alderspensjonVedVirk.minstenivaaIndividuellInnvilget or pesysData.alderspensjonVedVirk.minstenivaaPensjonistParInnvilget)
                            ) {
                                // omregningGP_PenT_GarantiPen_MNT_001
                                text(
                                    Bokmal to ", pensjonstillegget, garantipensjonen og minstenivåtillegget ditt",
                                    Nynorsk to ", pensjonstillegget, garantipensjonen og minstenivåtillegget ditt",
                                    English to ", supplementary pension, guaranteed pension and minimum pension supplement"
                                )
                            }
                        text(
                            Bokmal to " på nytt.",
                            Nynorsk to " på nytt.",
                            English to "."
                        )
                    }
                }
            }

            showIf(pesysData.ytelseskomponentInformasjon_safe.beloepEndring_safe.equalTo(BeloepEndring.UENDRET)) {
                // ingenEndringPensjon_001
                paragraph {
                    text(
                        Bokmal to "Dette får ingen betydning for pensjonen din.",
                        Nynorsk to "Dette får ingen følgjer for pensjonen din.",
                        English to "This does not affect your pension."
                    )
                }
            }.orShowIf(pesysData.ytelseskomponentInformasjon_safe.beloepEndring_safe.equalTo(BeloepEndring.ENDR_RED)) {
                // nyBeregningAPReduksjon_001
                paragraph {
                    text(
                        Bokmal to "Dette fører til at pensjonen din blir redusert.",
                        Nynorsk to "Dette fører til at pensjonen din blir redusert.",
                        English to "This leads to a reduction in your retirement pension."
                    )
                }
            }.orShowIf(pesysData.ytelseskomponentInformasjon_safe.beloepEndring_safe.equalTo(BeloepEndring.ENDR_OKT)) {
                // nyBeregningAPØkning_001
                paragraph {
                    text(
                        Bokmal to "Dette fører til at pensjonen din øker.",
                        Nynorsk to "Dette fører til at pensjonen din aukar.",
                        English to "This leads to an increase in your retirement pension."
                    )
                }
            }

            showIf(not(pesysData.alderspensjonVedVirk.uforeKombinertMedAlder)) {
                // innvilgelseAPInnledn_001
                paragraph {
                    textExpr(
                        Bokmal to "Du får ".expr() + pesysData.alderspensjonVedVirk.totalPensjon.format() + " kroner hver måned før skatt fra " + pesysData.krav.virkDatoFom.format() + " i alderspensjon fra folketrygden.",
                        Nynorsk to "Du får ".expr() + pesysData.alderspensjonVedVirk.totalPensjon.format() + " kroner kvar månad før skatt frå " + pesysData.krav.virkDatoFom.format() + " i alderspensjon frå folketrygda.",
                        English to "You will receive NOK ".expr() + pesysData.alderspensjonVedVirk.totalPensjon.format() + " every month before tax from " + pesysData.krav.virkDatoFom.format() + " as retirement pension from the National Insurance Scheme."
                    )
                }
            }.orShow {
                // innvilgelseAPogUTInnledn_001
                paragraph {
                    textExpr(
                        Bokmal to "Du får ".expr() + pesysData.alderspensjonVedVirk.totalPensjon.format() + " kroner hver måned før skatt fra " + pesysData.krav.virkDatoFom.format() + ". Du får alderspensjon fra folketrygden i tillegg til uføretrygden din.",
                        Nynorsk to "Du får ".expr() + pesysData.alderspensjonVedVirk.totalPensjon.format() + " kroner kvar månad før skatt frå " + pesysData.krav.virkDatoFom.format() + ". Du får alderspensjon frå folketrygda ved sida av uføretrygda di.",
                        English to "You will receive NOK ".expr() + pesysData.alderspensjonVedVirk.totalPensjon.format() + " every month before tax from " + pesysData.krav.virkDatoFom.format() + ". You will receive retirement pension through the National Insurance Scheme in addition to your disability benefit."
                    )
                }
            }

            includePhrase(Utbetalingsinformasjon)

            showIf(pesysData.inngangOgEksportVurdering.eksportTrygdeavtaleEOES and pesysData.bruker.borIEOES) {
                // hvisFlyttetBosattEØS_001
                paragraph {
                    textExpr(
                        Bokmal to "Vi forutsetter at du bor i ".expr() + bostedsland + ". Hvis du skal flytte til et land utenfor EØS-området, må du kontakte oss slik at vi kan vurdere om du fortsatt har rett til alderspensjon.",
                        Nynorsk to "Vi føreset at du bur i ".expr() + bostedsland + ". Dersom du skal flytte til eit land utanfor EØS-området, må du kontakte oss slik at vi kan vurdere om du framleis har rett til alderspensjon.",
                        English to "We presume that you live in ".expr() + bostedsland + ". If you are moving to a country outside the EEA region, it is important that you contact Nav. We will then reassess your eligibility for retirement pension."
                    )
                }
            }

            showIf(pesysData.inngangOgEksportVurdering.eksportTrygdeavtaleAvtaleland and not(pesysData.bruker.borIEOES) and pesysData.bruker.borIAvtaleland) {
                // hvisFlyttetBosattAvtaleland_001
                paragraph {
                    textExpr(
                        Bokmal to "Vi forutsetter at du bor i ".expr() + bostedsland + ". Hvis du skal flytte til et annet land, må du kontakte oss slik at vi kan vurdere om du fortsatt har rett til alderspensjon.",
                        Nynorsk to "Vi føreset at du bur i ".expr() + bostedsland + ". Dersom du skal flytte til eit anna land, må du kontakte oss slik at vi kan vurdere om du framleis har rett til alderspensjon. ",
                        English to "We presume that you live in ".expr() + bostedsland + ". If you are moving to another country, it is important that you contact Nav. We will then reassess your eligibility for retirement pension."
                    )
                }
            }

            paragraph {
                text(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 19-3",
                    Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 19-3",
                    English to "This decision was made pursuant to the provisions of §§ 19-3"
                )
                showIf(not(pesysData.alderspensjonVedVirk.garantipensjonInnvilget) and not(pesysData.alderspensjonVedVirk.gjenlevenderettAnvendt)) {
                    // flyttingAPHjemmel_001
                    text(
                        Bokmal to " og 22-12.",
                        Nynorsk to " og 22-12.",
                        English to " and 22-12 of the National Insurance Act."
                    )
                }.orShowIf(pesysData.alderspensjonVedVirk.garantipensjonInnvilget and not(pesysData.alderspensjonVedVirk.gjenlevenderettAnvendt)) {
                    // flyttingAPGarantipensjonHjemmel_001
                    text(
                        Bokmal to ", 20-10 og 22-12.",
                        Nynorsk to ", 20-10 og 22-12.",
                        English to ", 20-10 and 22-12 of the National Insurance Act."
                    )
                }
                    .orShowIf(not(pesysData.alderspensjonVedVirk.garantipensjonInnvilget) and pesysData.alderspensjonVedVirk.gjenlevenderettAnvendt) {
                        //  flyttingAPGjenlevendeHjemmel_001
                        text(
                            Bokmal to ", 19-16 jamfør 17-4 og 22-12.",
                            Nynorsk to ", 19-16 jamfør 17-4 og 22-12.",
                            English to ", 19-16 confer 17-4 and 22-12 of the National Insurance Act."
                        )
                    }
                    .orShowIf(pesysData.alderspensjonVedVirk.garantipensjonInnvilget and pesysData.alderspensjonVedVirk.gjenlevenderettAnvendt) {
                        // flyttingAP2016GjenlevendeGarantipensjonHjemmel_001
                        text(
                            Bokmal to ", 19-16 jamfør 17-4, 20-10, 20-19a og 22-12.",
                            Nynorsk to ", 19-16 jamfør 17-4, 20-10, 20-19a og 22-12.",
                            English to ", 19-16 confer 17-4, 20-10, 20-19a and 22-12 of the National Insurance Act."
                        )
                    }
            }

            showIf(pesysData.inngangOgEksportVurdering.eksportTrygdeavtaleEOES and pesysData.bruker.borIEOES) {
                // euArt7Hjemmel_001
                paragraph {
                    text(
                        Bokmal to "Vedtaket er også gjort etter EØS-avtalens regler i forordning 883/2004, artikkel 7.",
                        Nynorsk to "Vedtaket er også gjort etter EØS-avtalens reglar i forordning 883/2004, artikkel 7.",
                        English to "This decision was also made pursuant to the provisions of Article 7 of Regulation (EC) 883/2004."
                    )
                }
            }

            showIf(pesysData.inngangOgEksportVurdering.eksportTrygdeavtaleAvtaleland and not(pesysData.bruker.borIEOES) and pesysData.bruker.borIAvtaleland) {
                // avtaleEksportHjemmel_001
                paragraph {
                    textExpr(
                        Bokmal to "Vedtaket er også gjort etter reglene i trygdeavtalen med ".expr() + bostedsland + ", artikkel " + fritekst(
                            "legg inn aktuell artikkel om eksport"
                        ) + ".",
                        Nynorsk to "Vedtaket er også gjort etter reglane i trygdeavtalen med ".expr() + bostedsland + ", artikkel " + fritekst(
                            "legg inn aktuell artikkel om eksport"
                        ) + ".",
                        English to "This decision was also made pursuant to the provisions of the Social Security Agreement with ".expr() + bostedsland + ", Article " + fritekst(
                            "legg inn aktuell artikkel om eksport"
                        ) + ".",

                        )
                }
            }

            showIf(
                pesysData.krav.aarsak.equalTo(UTVANDRET)
                        and pesysData.opphoersbegrunnelseVedVirk_safe.begrunnelseET_safe.equalTo(
                    BRUKER_FLYTTET_IKKE_AVT_LAND
                )
                        and pesysData.opphoersbegrunnelseVedVirk_safe.begrunnelseBT_safe.notEqualTo(
                    BRUKER_FLYTTET_IKKE_AVT_LAND
                )
            ) {
                // flyttingETAPHjemmel_001
                paragraph {
                    text(
                        Bokmal to "Ektefelletillegget er behandlet etter § 3-24 i folketrygdloven.",
                        Nynorsk to "Ektefelletillegget er behandla etter § 3-24 i folketrygdlova.",
                        English to "The spouse supplement has been processed pursuant to the provisions of § 3-24 of the National Insurance Act."
                    )
                }
            }

            showIf(
                pesysData.krav.aarsak.equalTo(UTVANDRET)
                        and pesysData.opphoersbegrunnelseVedVirk_safe.begrunnelseET_safe.notEqualTo(
                    BRUKER_FLYTTET_IKKE_AVT_LAND
                )
                        and pesysData.opphoersbegrunnelseVedVirk_safe.begrunnelseBT_safe.equalTo(
                    BRUKER_FLYTTET_IKKE_AVT_LAND
                )
            ) {
                // flyttingBTAPHjemmel_001
                paragraph {
                    text(
                        Bokmal to "Barnetillegget er behandlet etter § 3-25 i folketrygdloven.",
                        Nynorsk to "Barnetillegget er behandla etter § 3-25 i folketrygdlova.",
                        English to "The child supplement has been processed pursuant to the provisions of § 3-25 of the National Insurance Act."
                    )
                }
            }

            showIf(
                pesysData.krav.aarsak.equalTo(UTVANDRET)
                        and pesysData.opphoersbegrunnelseVedVirk_safe.begrunnelseET_safe.equalTo(
                    BRUKER_FLYTTET_IKKE_AVT_LAND
                )
                        and pesysData.opphoersbegrunnelseVedVirk_safe.begrunnelseBT_safe.equalTo(
                    BRUKER_FLYTTET_IKKE_AVT_LAND
                )
            ) {
                // flyttingETBTAPHjemmel_001
                paragraph {
                    text(
                        Bokmal to "Ektefelle- og barnetillegget er behandlet etter §§ 3-24 og 3-25 i folketrygdloven.",
                        Nynorsk to "Ektefelle- og barnetillegget er behandla etter §§ 3-24 og 3-25 i folketrygdlova.",
                        English to "The spouse and child supplement has been processed pursuant to the provisions of §§ 3-24 and 3-25 of the National Insurance Act."
                    )
                }
            }

            showIf(saksbehandlerValg.reduksjonTilbakeITid) {
                includePhrase(FeilutbetalingAP)
            }

            showIf(saksbehandlerValg.endringIPensjonen) {
                includePhrase(VedtakAlderspensjon.EndringKanHaBetydningForSkatt)
            }

            // TODO Saksbehandlervalg under data-styring. Kan føre til at valg ikke har noen effekt.
            showIf(
                pesysData.ytelseskomponentInformasjon_safe.beloepEndring_safe.equalTo(BeloepEndring.ENDR_OKT)
                        and pesysData.vedtak.erEtterbetaling1Maaned
                        and saksbehandlerValg.etterbetaling
            ) {
                // etterbetalingAP_002
                includePhrase(Vedtak.Etterbetaling(pesysData.krav.virkDatoFom))
            }

            includePhrase(VedtakAlderspensjon.ArbeidsinntektOgAlderspensjon)

            showIf(pesysData.alderspensjonVedVirk.uttaksgrad.equalTo(100)) {
                // nyOpptjeningHelAP_001
                paragraph {
                    text(
                        Bokmal to "Hvis du har 100 prosent alderspensjon, gjelder økningen fra 1. januar året etter at skatteoppgjøret ditt er ferdig.",
                        Nynorsk to "Dersom du har 100 prosent alderspensjon, gjeld auken frå 1. januar året etter at skatteoppgjeret ditt er ferdig.",
                        English to "If you are receiving a full (100 percent) retirement pension, the increase will come into effect from 1 January the year after your final tax settlement has been completed."
                    )
                }
            }.orShow {
                // nyOpptjeningGradertAP_001
                paragraph {
                    text(
                        Bokmal to "Hvis du har lavere enn 100 prosent alderspensjon, blir økningen lagt til hvis du søker om endret grad eller ny beregning av den graden du har nå.",
                        Nynorsk to "Dersom du har lågare enn 100 prosent alderspensjon, blir auken lagd til dersom du søkjer om endra grad eller ny berekning av den graden du har no.",
                        English to "If you are receiving retirement pension at a reduced rate (lower than 100 percent), the increase will come into effect if you apply to have the rate changed or have your current rate recalculated."
                    )
                }
            }

            includePhrase(UfoereAlder.UfoereKombinertMedAlder(pesysData.alderspensjonVedVirk.uforeKombinertMedAlder))

            includePhrase(InformasjonOmAlderspensjon)
        }
    }
}