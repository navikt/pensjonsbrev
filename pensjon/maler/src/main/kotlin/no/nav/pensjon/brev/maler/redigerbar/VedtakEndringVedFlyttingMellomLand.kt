package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.BeloepEndring
import no.nav.pensjon.brev.api.model.EksportForbudKode.DOD26_ALDER
import no.nav.pensjon.brev.api.model.EksportForbudKode.FLYKT_ALDER
import no.nav.pensjon.brev.api.model.EksportForbudKode.UFOR25_ALDER
import no.nav.pensjon.brev.api.model.InformasjonOmMedlemskap
import no.nav.pensjon.brev.api.model.KravArsakType
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDto.AarsakTilAtPensjonenOeker
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDto.Opphoersbegrunnelse.BRUKER_FLYTTET_IKKE_AVT_LAND
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.AlderspensjonVedVirkSelectors.erEksportberegnet
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.AlderspensjonVedVirkSelectors.fullUttaksgrad
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.AlderspensjonVedVirkSelectors.garantipensjonInnvilget
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.AlderspensjonVedVirkSelectors.gjenlevenderettAnvendt
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.AlderspensjonVedVirkSelectors.minstenivaaIndividuellInnvilget
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.AlderspensjonVedVirkSelectors.minstenivaaPensjonistParInnvilget
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.AlderspensjonVedVirkSelectors.pensjonstilleggInnvilget
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.AlderspensjonVedVirkSelectors.totalPensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.AlderspensjonVedVirkSelectors.uforeKombinertMedAlder
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.BeregnetPensjonPerMaanedVedVirkSelectors.grunnpensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.BrukerSelectors.borIAvtaleland
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.BrukerSelectors.borIEOES
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.BrukerSelectors.faktiskBostedsland
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.InngangOgEksportVurderingAvdoedSelectors.minst20ArBotidKap19
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.InngangOgEksportVurderingAvdoedSelectors.minst20ArTrygdetidKap20
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.InngangOgEksportVurderingSelectors.eksportForbudKode
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.InngangOgEksportVurderingSelectors.eksportTrygdeavtaleAvtaleland
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.InngangOgEksportVurderingSelectors.eksportTrygdeavtaleEOES
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.InngangOgEksportVurderingSelectors.minst20AarTrygdetid
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.KravSelectors.aarsak
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.KravSelectors.virkDatoFom
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.OpphoersbegrunnelseVedVirkSelectors.begrunnelseBT
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.OpphoersbegrunnelseVedVirkSelectors.begrunnelseET
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.YtelseskomponentInformasjonSelectors.beloepEndring
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.beregnetpensjonPerMaanedVedVirk
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.bruker
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.erEtterbetaling1Maaned
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.informasjonOmMedlemskap
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.inngangOgEksportVurdering
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.inngangOgEksportVurderingAvdoed
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.krav
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.maanedligPensjonFoerSkatt
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.maanedligPensjonFoerSkattAP2025
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.opphoersbegrunnelseVedVirk
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.opplysningerBruktIBeregningen
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.opplysningerBruktIBeregningenAlderAP2025Dto
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.opplysningerOmAvdoedBruktIBeregning
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.orienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.PesysDataSelectors.ytelseskomponentInformasjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.SaksbehandlerValgSelectors.aarsakTilAtPensjonenOeker
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.SaksbehandlerValgSelectors.endringIPensjonen
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.SaksbehandlerValgSelectors.etterbetaling
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.SaksbehandlerValgSelectors.reduksjonTilbakeITid
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.maler.fraser.alderspensjon.ArbeidsinntektOgAlderspensjonKort
import no.nav.pensjon.brev.maler.fraser.alderspensjon.BeregnaPaaNytt
import no.nav.pensjon.brev.maler.fraser.alderspensjon.FeilutbetalingAP
import no.nav.pensjon.brev.maler.fraser.alderspensjon.InformasjonOmAlderspensjon
import no.nav.pensjon.brev.maler.fraser.alderspensjon.MeldFraOmEndringer2
import no.nav.pensjon.brev.maler.fraser.alderspensjon.UfoereAlder
import no.nav.pensjon.brev.maler.fraser.alderspensjon.Utbetalingsinformasjon
import no.nav.pensjon.brev.maler.fraser.alderspensjon.VedtakAlderspensjon
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.maler.vedlegg.vedleggInformasjonOmMedlemskapOgHelserettigheterEOES
import no.nav.pensjon.brev.maler.vedlegg.vedleggInformasjonOmMedlemskapOgHelserettigheterUtenforEOES
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligPensjonFoerSkattAp2025
import no.nav.pensjon.brev.maler.vedlegg.vedleggOpplysningerBruktIBeregningenAlder
import no.nav.pensjon.brev.maler.vedlegg.vedleggOpplysningerBruktIBeregningenAlderAP2025
import no.nav.pensjon.brev.maler.vedlegg.vedleggOpplysningerOmAvdoedBruktIBeregning
import no.nav.pensjon.brev.maler.vedlegg.vedleggOrienteringOmRettigheterOgPlikter
import no.nav.pensjon.brev.model.Brevkategori
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.isNull
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.expression.safe
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
//000117 i Doksys
object VedtakEndringVedFlyttingMellomLand : RedigerbarTemplate<VedtakEndringVedFlyttingMellomLandDto> {

    override val kategori = Brevkategori.VEDTAK_FLYTTE_MELLOM_LAND
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.ALDER)
    override val kode = Pesysbrevkoder.Redigerbar.PE_AP_ENDRING_FLYTTING_MELLOM_LAND
    override val template = createTemplate(
        languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - endring av alderspensjon ved flytting mellom land",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        val virkDatoFom = pesysData.krav.virkDatoFom.format()
        title {
            includePhrase(BeregnaPaaNytt(pesysData.krav.virkDatoFom))
        }

        outline {
            val bostedsland = pesysData.safe { bruker.faktiskBostedsland }.ifNull(fritekst("bostedsland"))
            val garantipensjonInnvilget = pesysData.alderspensjonVedVirk.garantipensjonInnvilget
            val pensjonstilleggInnvilget = pesysData.alderspensjonVedVirk.pensjonstilleggInnvilget
            val minstenivaaIndividuellInnvilget = pesysData.alderspensjonVedVirk.minstenivaaIndividuellInnvilget
            val minstenivaaPensjonistParInnvilget = pesysData.alderspensjonVedVirk.minstenivaaPensjonistParInnvilget
            val gjenlevenderettAnvendt = pesysData.alderspensjonVedVirk.gjenlevenderettAnvendt

            val totalPensjon = pesysData.alderspensjonVedVirk.totalPensjon.format()
            val aarsakUtvandret = pesysData.krav.aarsak.equalTo(KravArsakType.UTVANDRET)

            val beloepUendret =
                pesysData.safe { ytelseskomponentInformasjon }.safe { beloepEndring }.equalTo(BeloepEndring.UENDRET)
            val beloepOekning =
                pesysData.safe { ytelseskomponentInformasjon }.safe { beloepEndring }.equalTo(BeloepEndring.ENDR_OKT)
            val beloepRedusert =
                pesysData.safe { ytelseskomponentInformasjon }.safe { beloepEndring }.equalTo(BeloepEndring.ENDR_RED)

            val eksportForbudKode = pesysData.inngangOgEksportVurdering.eksportForbudKode
            val eksportForbudKodeAvdoed = pesysData.safe { inngangOgEksportVurderingAvdoed }.safe { eksportForbudKode }

            includePhrase(Vedtak.Overskrift)

            ifNotNull(pesysData.bruker.faktiskBostedsland) {
                // flyttingAPmeld_001
                paragraph {
                    text(
                        bokmal { + "Vi har fått melding om at du har flyttet til " + it + "." },
                        nynorsk { + "Vi har fått melding om at du har flytta til " + it + "." },
                        english { + "We have received notice that you have moved to " + it + "." }
                    )
                }
            }.orShow {
                // flyttingAPNorge_001
                paragraph {
                    text(
                        bokmal { + "Du har flyttet til Norge." },
                        nynorsk { + "Du har flytta til Noreg." },
                        english { + "You have moved to Norway." }
                    )
                }
            }

            showIf(aarsakUtvandret and pesysData.alderspensjonVedVirk.erEksportberegnet) {
                showIf(eksportForbudKode.equalTo(UFOR25_ALDER)) {
                    // eksportUngUfor_001
                    paragraph {
                        text(
                            bokmal { + "For å ha rett til alderspensjon etter reglene for unge uføre må du være bosatt i Norge. Du har likevel fortsatt rett til pensjon etter din egen opptjening." },
                            nynorsk { + "For å ha rett til alderspensjon etter reglane for unge uføre må du bu i Noreg. Du har likevel framleis rett til pensjon etter di eiga opptening." },
                            english { + "To be eligible for retirement pension calculated in accordance with the regulations for young people with disabilities, you have to live in Norway. However, you are still eligible for a pension calculated on the basis of your own accumulated pension rights." }
                        )
                    }
                }

                showIf(eksportForbudKodeAvdoed.equalTo(DOD26_ALDER)) {
                    // eksportUngUforAvdod_001
                    paragraph {
                        text(
                            bokmal { + "Når du flytter til utlandet har du ikke lenger rett til alderspensjon etter reglene som gjelder når avdøde var yngre enn 26 år ved dødsfallet. Du har fortsatt rett til alderspensjon etter din egen og avdødes opptjening." },
                            nynorsk { + "Når du flyttar til utlandet har du ikkje lenger rett til alderspensjon etter reglane som gjeldar når avdøde var yngre enn 26 år ved dødsfallet. Du har framleis rett til pensjon etter di eiga og avdøde si opptening." },
                            english { + "When you move abroad, you are no longer eligible for retirement pension calculated in accordance with the regulations as the deceased was younger than 26 years old at the time of death. However, you are still eligible for a pension calculated on the basis of your own and the deceased’s accumulated pension rights." }
                        )
                    }
                }

                showIf(eksportForbudKode.equalTo(FLYKT_ALDER)) {
                    // eksportFlyktning_001
                    paragraph {
                        text(
                            bokmal { + "Når du flytter til et land utenfor EØS-området har du ikke lenger rett til alderspensjon etter reglene for flyktninger. Du har fortsatt rett til pensjon etter din egen opptjening." },
                            nynorsk { + "Når du flyttar til eit land utanfor EØS-området har du ikkje lenger rett til alderspensjon etter reglane for flyktningar. Du har framleis rett til pensjon etter di eiga opptening." },
                            english { + "When you move to a country outside the EEA region, you are no longer eligible for retirement pension determined on the basis of the regulations for refugees. However, you are still eligible for a pension calculated on the basis of your own accumulated pension rights." }
                        )
                    }
                }

                showIf(eksportForbudKodeAvdoed.equalTo(FLYKT_ALDER)) {
                    // eksportFlyktningAvdod_001
                    paragraph {
                        text(
                            bokmal { + "Når du flytter til et land utenfor EØS-området har du ikke lenger rett til alderspensjon etter reglene for flyktninger. Du har fortsatt rett til alderspensjon etter din egen og avdødes opptjening." },
                            nynorsk { + "Når du flyttar til eit land utanfor EØS-området har du ikkje lenger rett til alderspensjon etter reglane for flyktningar. Du har framleis rett til pensjon etter di eiga og avdøde si opptening." },
                            english { + "When you move to a country outside the EEA region, you are no longer eligible for retirement pension determined on the basis of the regulations for refugees. However, you are still eligible for a pension calculated on the basis of your own and the deceased’s accumulated pension rights." }
                        )
                    }
                }

                val minst20AarTrygdetid = pesysData.inngangOgEksportVurdering.minst20AarTrygdetid
                val minst20AarTrygdetidAvdoed =
                    pesysData.inngangOgEksportVurderingAvdoed.safe { minst20ArTrygdetidKap20 }
                val minst20AarBotidAvdoed =
                    pesysData.inngangOgEksportVurderingAvdoed.safe {minst20ArBotidKap19 }
                showIf(
                    eksportForbudKode.isNull() and
                            not(minst20AarTrygdetid) and
                            minst20AarTrygdetidAvdoed.ifNull(true)
                            and minst20AarBotidAvdoed.ifNull(true)
                ) {
                    // eksportAPunder20aar_001
                    paragraph {
                        text(
                            bokmal { + "For å ha rett til full alderspensjon når du bor i " + bostedsland + ", må du ha vært medlem i folketrygden i minst 20 år. Du har mindre enn 20 års medlemstid og har derfor ikke rett til full pensjon." },
                            nynorsk { + "For å ha rett til full alderspensjon når du bur i " + bostedsland + ", må du ha vore medlem i folketrygda i minst 20 år. Du har mindre enn 20 års medlemstid og har derfor ikkje rett til full pensjon." },
                            english { + "To be eligible for a full retirement pension while living in " + bostedsland + ", you must have been a member of the National Insurance scheme earning pension rights for at least 20 years. You have been a member for less than 20 years, and are therefore not eligible for a full pension." }
                        )
                    }

                    paragraph {
                        text(
                            bokmal { + "I vedleggene finner du mer detaljerte opplysninger." },
                            nynorsk { + "I vedlegga finn du meir detaljerte opplysningar." },
                            english { + "There is more detailed information in the attachments." }
                        )
                    }
                }.orShowIf(
                    eksportForbudKodeAvdoed.isNull() and
                            minst20AarTrygdetid and
                            (not(minst20AarTrygdetidAvdoed.ifNull(true)) or not(minst20AarBotidAvdoed.ifNull(true)))
                ) {
                    // eksportAPUnder20aarAvdod_001
                    paragraph {
                        text(
                            bokmal { + "Verken du eller avdøde har vært medlem i folketrygden i minst 20 år. Da har du ikke rett til å få utbetalt hele alderspensjonen din når du flytter til dette landet." },
                            nynorsk { + "Verken du eller avdøde har vore medlem i folketrygda i minst 20 år. Da har du ikkje rett til å få utbetalt heile alderspensjonen din når du flyttar til dette landet." },
                            english { + "Neither you or the deceased have been a member of the Norwegian National Insurance Scheme for at least 20 years. Therefore, you are not eligible for your full retirement pension when you move to this country." }
                        )
                    }
                }
            }

            val begrunnelseETErBrukerFlyttetIkkeAvtLand =
                pesysData.safe { opphoersbegrunnelseVedVirk }.safe { begrunnelseET }.equalTo(
                    BRUKER_FLYTTET_IKKE_AVT_LAND
                )
            val begrunnelseBTErBrukerFlyttetIkkeAvtLand =
                pesysData.safe { opphoersbegrunnelseVedVirk }.safe { begrunnelseBT }.equalTo(
                    BRUKER_FLYTTET_IKKE_AVT_LAND
                )
            showIf(
                aarsakUtvandret and begrunnelseETErBrukerFlyttetIkkeAvtLand and not(
                    begrunnelseBTErBrukerFlyttetIkkeAvtLand
                )
            ) {
                // eksportAPET_001
                paragraph {
                    text(
                        bokmal { + "Når du flytter til dette landet har du ikke lenger rett til ektefelletillegg i alderspensjonen. Derfor har vi opphørt ektefelletillegget ditt." },
                        nynorsk { + "Når du flyttar til dette landet har du ikkje lenger rett til ektefelletillegg i alderspensjonen. Derfor har vi opphørt ektefelletillegget ditt." },
                        english { + "When you move to this country, you are no longer eligible for a spouse supplement. Therefore, we have stopped your supplement." }
                    )
                }
            }.orShowIf(
                aarsakUtvandret and not(begrunnelseETErBrukerFlyttetIkkeAvtLand) and begrunnelseBTErBrukerFlyttetIkkeAvtLand
            ) {
                // eksportAPBT_001
                paragraph {
                    text(
                        bokmal { + "Når du flytter til dette landet har du ikke lenger rett til barnetillegg i alderspensjonen din. Derfor har vi opphørt barnetillegget ditt." },
                        nynorsk { + "Når du flyttar til dette landet har du ikkje lenger rett til barnetillegg i alderspensjonen din. Derfor har vi opphørt barnetillegget ditt." },
                        english { + "When you move to this country, you are no longer eligible for a child supplement. Therefore, we have stopped your supplement." }
                    )
                }
            }.orShowIf(
                aarsakUtvandret and begrunnelseETErBrukerFlyttetIkkeAvtLand and begrunnelseBTErBrukerFlyttetIkkeAvtLand
            ) {
                // eksportAPETBT_001
                paragraph {
                    text(
                        bokmal { + "Når du flytter til dette landet har du ikke lenger rett til ektefelle- og barnetillegg i alderspensjonen. Derfor har vi opphørt ektefelle- og barnetillegget ditt." },
                        nynorsk { + "Når du flyttar til dette landet har du ikkje lenger rett til ektefelle- og barnetillegg i alderspensjonen din. Derfor har vi opphørt ektefelle- og barnetillegget ditt." },
                        english { + "When you move to this country, you are no longer eligible for spouse and child supplement. Therefore, we have stopped your supplement." }
                    )
                }
            }

            showIf(
                pesysData.krav.aarsak.equalTo(KravArsakType.INNVANDRET) and beloepOekning
            ) {
                // TODO Saksbehandlervalg under data-styring. Kan føre til at valg ikke har noen effekt.
                showIf(saksbehandlerValg.aarsakTilAtPensjonenOeker.equalTo(AarsakTilAtPensjonenOeker.EKSPORTBEREGNING_MED_REDUSERT_TRYGDETID)) {
                    // importAPRedusTT_001
                    paragraph {
                        text(
                            bokmal { + "Når du bor i Norge har du rett til å få utbetalt hele alderspensjonen din igjen. Derfor har vi beregnet pensjonen din på nytt." },
                            nynorsk { + "Når du bur i Noreg har du rett til å få utbetalt heile alderspensjonen din igjen. Derfor har vi berekna pensjonen din på nytt." },
                            english { + "When you live in Norway you are eligible for your full retirement pension. We have therefore recalculated your pension." }
                        )
                    }
                }.orShowIf(saksbehandlerValg.aarsakTilAtPensjonenOeker.equalTo(AarsakTilAtPensjonenOeker.EKSPORTFORBUD_UNG_UFOER)) {
                    // importAPUngUfor_001
                    paragraph {
                        text(
                            bokmal { + "Når du bor i Norge har du rett til alderspensjon etter reglene for unge uføre igjen. Derfor har vi beregnet pensjonen din på nytt." },
                            nynorsk { + "Når du bur i Noreg har du rett til alderspensjon etter reglane for unge uføre igjen. Derfor har vi berekna pensjonen din på nytt." },
                            english { + "When you live in Norway you are eligible for retirement pension calculated in accordance with the regulations for young people with disabilities. We have therefore recalculated your pension." }
                        )
                    }
                }
                    .orShowIf(saksbehandlerValg.aarsakTilAtPensjonenOeker.equalTo(AarsakTilAtPensjonenOeker.EKSPORTFORBUD_FLYKTNING)) {
                        // importAPflyktninger_001
                        paragraph {
                            text(
                                bokmal { + "Når du bor i Norge eller et EØS-land har du rett til alderspensjon etter reglene for flyktninger igjen. Derfor har vi beregnet pensjonen din på nytt." },
                                nynorsk { + "Når du bur i Noreg eller eit EØS-land har du rett til alderspensjon etter reglane for flyktningar igjen. Derfor har vi berekna pensjonen din på nytt." },
                                english { + "When you live in Norway or an EEA country you are eligible for retirement pension determined on the basis of the regulations for refugees. We have therefore recalculated your pension." }
                            )
                        }
                    }
            }

            showIf(
                aarsakUtvandret and
                        not(begrunnelseBTErBrukerFlyttetIkkeAvtLand) and
                        not(begrunnelseETErBrukerFlyttetIkkeAvtLand) and
                        (
                                eksportForbudKode.equalTo(UFOR25_ALDER)
                                        or eksportForbudKode.equalTo(FLYKT_ALDER)
                                        or eksportForbudKodeAvdoed.equalTo(UFOR25_ALDER)
                                        or eksportForbudKodeAvdoed.equalTo(FLYKT_ALDER)
                                        or eksportForbudKodeAvdoed.equalTo(DOD26_ALDER)
                                )
            ) {
                // omregningAP_001
                paragraph {
                    text(
                        bokmal { + "Derfor har vi beregnet pensjonen din på nytt." },
                        nynorsk { + "Derfor har vi berekna pensjonen din på nytt." },
                        english { + "We have therefore recalculated your pension." }
                    )
                }
            }

            showIf(pesysData.safe { beregnetpensjonPerMaanedVedVirk.grunnpensjon }.ifNull(Kroner(0)).greaterThan(0) and
                    aarsakUtvandret and
                    not(beloepUendret) and
                    eksportForbudKode.isNull() and
                    not(begrunnelseBTErBrukerFlyttetIkkeAvtLand) and
                    not(begrunnelseETErBrukerFlyttetIkkeAvtLand)
            ) {
                paragraph {
                    text(
                        bokmal { + "Derfor har vi beregnet grunnpensjonen" },
                        nynorsk { + "Derfor har vi berekna grunnpensjonen" },
                        english { + "We have therefore recalculated your basic pension" }
                    )
                    showIf(
                        not(garantipensjonInnvilget) and
                                pensjonstilleggInnvilget and
                                not(minstenivaaIndividuellInnvilget) and
                                not(minstenivaaPensjonistParInnvilget)
                    ) {
                        // omregningGP_PenT_001
                        text(
                            bokmal { + " og pensjonstillegget ditt" },
                            nynorsk { + " og pensjonstillegget ditt" },
                            english { + " and pension supplement" }
                        )
                    }.orShowIf(
                        not(garantipensjonInnvilget) and
                                not(pensjonstilleggInnvilget) and
                                (minstenivaaIndividuellInnvilget or minstenivaaPensjonistParInnvilget)
                    ) {
                        // omregningGP_MNT_001
                        text(
                            bokmal { + " og minstenivåtillegget ditt" },
                            nynorsk { + " og minstenivåtillegget ditt" },
                            english { + " and minimum pension supplement" }
                        )
                    }.orShowIf(
                        not(garantipensjonInnvilget) and
                                pensjonstilleggInnvilget and
                                (minstenivaaIndividuellInnvilget or minstenivaaPensjonistParInnvilget)
                    ) {
                        // omregningGP_PenT_MNT_001
                        text(
                            bokmal { + ", pensjonstillegget og minstenivåtillegget ditt" },
                            nynorsk { + ", pensjonstillegget og minstenivåtillegget ditt" },
                            english { + ", supplementary pension and minimum pension supplement" }
                        )
                    }.orShowIf(
                        garantipensjonInnvilget and
                                not(pensjonstilleggInnvilget) and
                                not(minstenivaaIndividuellInnvilget) and
                                not(minstenivaaPensjonistParInnvilget)
                    ) {
                        // omregningGP_GarantiPen_001
                        text(
                            bokmal { + " og garantipensjonen din" },
                            nynorsk { + " og garantipensjonen din" },
                            english { + " and guaranteed pension" }
                        )
                    }.orShowIf(
                        garantipensjonInnvilget and
                                pensjonstilleggInnvilget and
                                not(minstenivaaIndividuellInnvilget) and
                                not(minstenivaaPensjonistParInnvilget)
                    ) {
                        // omregningGP_PenT_GarantiPen_001
                        text(
                            bokmal { + ", pensjonstillegget og garantipensjonen din" },
                            nynorsk { + ", pensjonstillegget og garantipensjonen din" },
                            english { + ", supplementary pension and guaranteed pension" }
                        )
                    }.orShowIf(
                        garantipensjonInnvilget and
                                not(pensjonstilleggInnvilget) and
                                (minstenivaaIndividuellInnvilget or minstenivaaPensjonistParInnvilget)
                    ) {
                        // omregningGP_GarantiPen_MNT_001
                        text(
                            bokmal { + ", garantipensjonen og minstenivåtillegget ditt" },
                            nynorsk { + ", garantipensjonen og minstenivåtillegget ditt" },
                            english { + ", guaranteed pension and minimum pension supplement" }
                        )
                    }.orShowIf(
                        garantipensjonInnvilget and
                                pensjonstilleggInnvilget and
                                (minstenivaaIndividuellInnvilget or minstenivaaPensjonistParInnvilget)
                    ) {
                        // omregningGP_PenT_GarantiPen_MNT_001
                        text(
                            bokmal { + ", pensjonstillegget, garantipensjonen og minstenivåtillegget ditt" },
                            nynorsk { + ", pensjonstillegget, garantipensjonen og minstenivåtillegget ditt" },
                            english { + ", supplementary pension, guaranteed pension and minimum pension supplement" }
                        )
                    }
                    text(
                        bokmal { + " på nytt." },
                        nynorsk { + " på nytt." },
                        english { + "." }
                    )
                }
            }

            showIf(beloepUendret) {
                // ingenEndringPensjon_001
                paragraph {
                    text(
                        bokmal { + "Dette får ingen betydning for pensjonen din." },
                        nynorsk { + "Dette får ingen følgjer for pensjonen din." },
                        english { + "This does not affect your pension." }
                    )
                }
            }.orShowIf(beloepRedusert) {
                // nyBeregningAPReduksjon_001
                paragraph {
                    text(
                        bokmal { + "Dette fører til at pensjonen din blir redusert." },
                        nynorsk { + "Dette fører til at pensjonen din blir redusert." },
                        english { + "This leads to a reduction in your retirement pension." }
                    )
                }
            }.orShowIf(beloepOekning) {
                // nyBeregningAPØkning_001
                paragraph {
                    text(
                        bokmal { + "Dette fører til at pensjonen din øker." },
                        nynorsk { + "Dette fører til at pensjonen din aukar." },
                        english { + "This leads to an increase in your retirement pension." }
                    )
                }
            }
            showIf(pesysData.alderspensjonVedVirk.uforeKombinertMedAlder) {
                // innvilgelseAPogUTInnledn_001
                paragraph {
                    text(
                        bokmal { + "Du får " + totalPensjon + " hver måned før skatt fra " + virkDatoFom + ". Du får alderspensjon fra folketrygden i tillegg til uføretrygden din." },
                        nynorsk { + "Du får " + totalPensjon + " kvar månad før skatt frå " + virkDatoFom + ". Du får alderspensjon frå folketrygda ved sida av uføretrygda di." },
                        english { + "You will receive " + totalPensjon + " every month before tax from " + virkDatoFom + ". You will receive retirement pension through the National Insurance Scheme in addition to your disability benefit." }
                    )
                }
            }.orShow {
                // innvilgelseAPInnledn_001
                paragraph {
                    text(
                        bokmal { + "Du får " + totalPensjon + " hver måned før skatt fra " + virkDatoFom + " i alderspensjon fra folketrygden." },
                        nynorsk { + "Du får " + totalPensjon + " kvar månad før skatt frå " + virkDatoFom + " i alderspensjon frå folketrygda." },
                        english { + "You will receive " + totalPensjon + " every month before tax from " + virkDatoFom + " as retirement pension from the National Insurance Scheme." }
                    )
                }
            }

            includePhrase(Utbetalingsinformasjon)

            showIf(pesysData.inngangOgEksportVurdering.eksportTrygdeavtaleEOES and pesysData.bruker.borIEOES) {
                // hvisFlyttetBosattEØS_001
                paragraph {
                    text(
                        bokmal { + "Vi forutsetter at du bor i " + bostedsland + ". Hvis du skal flytte til et land utenfor EØS-området, må du kontakte oss slik at vi kan vurdere om du fortsatt har rett til alderspensjon." },
                        nynorsk { + "Vi føreset at du bur i " + bostedsland + ". Dersom du skal flytte til eit land utanfor EØS-området, må du kontakte oss slik at vi kan vurdere om du framleis har rett til alderspensjon." },
                        english { + "We presume that you live in " + bostedsland + ". If you are moving to a country outside the EEA region, it is important that you contact Nav. We will then reassess your eligibility for retirement pension." }
                    )
                }
            }

            showIf(pesysData.inngangOgEksportVurdering.eksportTrygdeavtaleAvtaleland and not(pesysData.bruker.borIEOES) and pesysData.bruker.borIAvtaleland) {
                // hvisFlyttetBosattAvtaleland_001
                paragraph {
                    text(
                        bokmal { + "Vi forutsetter at du bor i " + bostedsland + ". Hvis du skal flytte til et annet land, må du kontakte oss slik at vi kan vurdere om du fortsatt har rett til alderspensjon." },
                        nynorsk { + "Vi føreset at du bur i " + bostedsland + ". Dersom du skal flytte til eit anna land, må du kontakte oss slik at vi kan vurdere om du framleis har rett til alderspensjon. " },
                        english { + "We presume that you live in " + bostedsland + ". If you are moving to another country, it is important that you contact Nav. We will then reassess your eligibility for retirement pension." }
                    )
                }
            }

            paragraph {
                text(
                    bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 19-3" },
                    nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 19-3" },
                    english { + "This decision was made pursuant to the provisions of §§ 19-3" }
                )
                showIf(garantipensjonInnvilget and not(gjenlevenderettAnvendt)) {
                    // flyttingAPGarantipensjonHjemmel_001
                    text(
                        bokmal { + ", 20-10" },
                        nynorsk { + ", 20-10" },
                        english { + ", 20-10" }
                    )
                }.orShowIf(not(garantipensjonInnvilget) and gjenlevenderettAnvendt) {
                    //  flyttingAPGjenlevendeHjemmel_001
                    text(
                        bokmal { + ", 19-16 jamfør 17-4" },
                        nynorsk { + ", 19-16 jamfør 17-4" },
                        english { + ", 19-16 confer 17-4" }
                    )
                }.orShowIf(garantipensjonInnvilget and gjenlevenderettAnvendt) {
                    // flyttingAP2016GjenlevendeGarantipensjonHjemmel_001
                    text(
                        bokmal { + ", 19-16 jamfør 17-4, 20-10, 20-19a" },
                        nynorsk { + ", 19-16 jamfør 17-4, 20-10, 20-19a" },
                        english { + ", 19-16 confer 17-4, 20-10, 20-19a" }
                    )
                }
                text(
                    bokmal { + " og 22-12." },
                    nynorsk { + " og 22-12." },
                    english { + " and 22-12 of the National Insurance Act." }
                )
            }

            showIf(pesysData.inngangOgEksportVurdering.eksportTrygdeavtaleEOES and pesysData.bruker.borIEOES) {
                // euArt7Hjemmel_001
                paragraph {
                    text(
                        bokmal { + "Vedtaket er også gjort etter EØS-avtalens regler i forordning 883/2004, artikkel 7." },
                        nynorsk { + "Vedtaket er også gjort etter EØS-avtalens reglar i forordning 883/2004, artikkel 7." },
                        english { + "This decision was also made pursuant to the provisions of Article 7 of Regulation (EC) 883/2004." }
                    )
                }
            }.orShowIf(pesysData.inngangOgEksportVurdering.eksportTrygdeavtaleAvtaleland and not(pesysData.bruker.borIEOES) and pesysData.bruker.borIAvtaleland) {
                // avtaleEksportHjemmel_001
                paragraph {
                    val fritekst = fritekst("legg inn aktuell artikkel om eksport")
                    text(
                        bokmal { + "Vedtaket er også gjort etter reglene i trygdeavtalen med " + bostedsland + ", artikkel " + fritekst + "." },
                        nynorsk { + "Vedtaket er også gjort etter reglane i trygdeavtalen med " + bostedsland + ", artikkel " + fritekst + "." },
                        english { + "This decision was also made pursuant to the provisions of the Social Security Agreement with " + bostedsland + ", Article " + fritekst + "." },

                        )
                }
            }

            showIf(pesysData.krav.aarsak.equalTo(KravArsakType.UTVANDRET)) {
                showIf(begrunnelseETErBrukerFlyttetIkkeAvtLand and not(begrunnelseBTErBrukerFlyttetIkkeAvtLand)) {
                    // flyttingETAPHjemmel_001
                    paragraph {
                        text(
                            bokmal { + "Ektefelletillegget er behandlet etter § 3-24 i folketrygdloven." },
                            nynorsk { + "Ektefelletillegget er behandla etter § 3-24 i folketrygdlova." },
                            english { + "The spouse supplement has been processed pursuant to the provisions of § 3-24 of the National Insurance Act." }
                        )
                    }
                }.orShowIf(not(begrunnelseETErBrukerFlyttetIkkeAvtLand) and begrunnelseBTErBrukerFlyttetIkkeAvtLand) {
                    // flyttingBTAPHjemmel_001
                    paragraph {
                        text(
                            bokmal { + "Barnetillegget er behandlet etter § 3-25 i folketrygdloven." },
                            nynorsk { + "Barnetillegget er behandla etter § 3-25 i folketrygdlova." },
                            english { + "The child supplement has been processed pursuant to the provisions of § 3-25 of the National Insurance Act." }
                        )
                    }
                }.orShowIf(begrunnelseETErBrukerFlyttetIkkeAvtLand and begrunnelseBTErBrukerFlyttetIkkeAvtLand) {
                    // flyttingETBTAPHjemmel_001
                    paragraph {
                        text(
                            bokmal { + "Ektefelle- og barnetillegget er behandlet etter §§ 3-24 og 3-25 i folketrygdloven." },
                            nynorsk { + "Ektefelle- og barnetillegget er behandla etter §§ 3-24 og 3-25 i folketrygdlova." },
                            english { + "The spouse and child supplement has been processed pursuant to the provisions of §§ 3-24 and 3-25 of the National Insurance Act." }
                        )
                    }
                }
            }

            showIf(saksbehandlerValg.reduksjonTilbakeITid) {
                includePhrase(FeilutbetalingAP)
            }

            showIf(saksbehandlerValg.endringIPensjonen) {
                includePhrase(VedtakAlderspensjon.EndringKanHaBetydningForSkatt)
            }

            showIf(beloepOekning and pesysData.erEtterbetaling1Maaned and saksbehandlerValg.etterbetaling) {
                // etterbetalingAP_002
                includePhrase(Vedtak.Etterbetaling(pesysData.krav.virkDatoFom))
            }

            includePhrase(ArbeidsinntektOgAlderspensjonKort)

            showIf(pesysData.alderspensjonVedVirk.fullUttaksgrad) {
                // nyOpptjeningHelAP_001
                paragraph {
                    text(
                        bokmal { + "Hvis du har 100 prosent alderspensjon, gjelder økningen fra 1. januar året etter at skatteoppgjøret ditt er ferdig." },
                        nynorsk { + "Dersom du har 100 prosent alderspensjon, gjeld auken frå 1. januar året etter at skatteoppgjeret ditt er ferdig." },
                        english { + "If you are receiving a full (100 percent) retirement pension, the increase will come into effect from 1 January the year after your final tax settlement has been completed." }
                    )
                }
            }.orShow {
                // nyOpptjeningGradertAP_001
                paragraph {
                    text(
                        bokmal { + "Hvis du har lavere enn 100 prosent alderspensjon, blir økningen lagt til hvis du søker om endret grad eller ny beregning av den graden du har nå." },
                        nynorsk { + "Dersom du har lågare enn 100 prosent alderspensjon, blir auken lagd til dersom du søkjer om endra grad eller ny berekning av den graden du har no." },
                        english { + "If you are receiving retirement pension at a reduced rate (lower than 100 percent), the increase will come into effect if you apply to have the rate changed or have your current rate recalculated." }
                    )
                }
            }

            includePhrase(UfoereAlder.UfoereKombinertMedAlder(pesysData.alderspensjonVedVirk.uforeKombinertMedAlder))

            includePhrase(InformasjonOmAlderspensjon)

            includePhrase(MeldFraOmEndringer2)
            includePhrase(Felles.RettTilAAKlage)
            includePhrase(Felles.RettTilInnsyn(vedleggOrienteringOmRettigheterOgPlikter))
            includePhrase(Felles.HarDuSpoersmaal(Constants.PENSJON_URL, Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON))

        }
        includeAttachment(vedleggOrienteringOmRettigheterOgPlikter, pesysData.orienteringOmRettigheterOgPlikterDto)
        includeAttachmentIfNotNull(vedleggMaanedligPensjonFoerSkatt, pesysData.maanedligPensjonFoerSkatt)
        includeAttachmentIfNotNull(vedleggMaanedligPensjonFoerSkattAp2025, pesysData.maanedligPensjonFoerSkattAP2025)
        includeAttachmentIfNotNull(vedleggOpplysningerBruktIBeregningenAlder, pesysData.opplysningerBruktIBeregningen)
        includeAttachmentIfNotNull(vedleggOpplysningerBruktIBeregningenAlderAP2025, pesysData.opplysningerBruktIBeregningenAlderAP2025Dto)
        includeAttachmentIfNotNull(vedleggOpplysningerOmAvdoedBruktIBeregning, pesysData.opplysningerOmAvdoedBruktIBeregning)
        includeAttachment(vedleggInformasjonOmMedlemskapOgHelserettigheterEOES, pesysData.informasjonOmMedlemskap.equalTo(InformasjonOmMedlemskap.EOES))
        includeAttachment(vedleggInformasjonOmMedlemskapOgHelserettigheterUtenforEOES, pesysData.informasjonOmMedlemskap.equalTo(InformasjonOmMedlemskap.UTENFOR_EOES))
    }
}