package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.AP2011
import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.AP2016
import no.nav.pensjon.brev.api.model.EksportForbudKode.FLYKT_ALDER
import no.nav.pensjon.brev.api.model.EksportForbudKode.UFOR25_ALDER
import no.nav.pensjon.brev.api.model.InformasjonOmMedlemskap
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakStansAlderspensjonFlyttingMellomLandDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakStansAlderspensjonFlyttingMellomLandDtoSelectors.PesysDataSelectors.brukersBostedsland
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakStansAlderspensjonFlyttingMellomLandDtoSelectors.PesysDataSelectors.dineRettigheterOgMulighetTilAaKlage
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakStansAlderspensjonFlyttingMellomLandDtoSelectors.PesysDataSelectors.eksportForbudKodeAvdoed_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakStansAlderspensjonFlyttingMellomLandDtoSelectors.PesysDataSelectors.eksportForbudKode_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakStansAlderspensjonFlyttingMellomLandDtoSelectors.PesysDataSelectors.garantipensjonInnvilget
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakStansAlderspensjonFlyttingMellomLandDtoSelectors.PesysDataSelectors.harAvdoed
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakStansAlderspensjonFlyttingMellomLandDtoSelectors.PesysDataSelectors.informasjonOmMedlemskap
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakStansAlderspensjonFlyttingMellomLandDtoSelectors.PesysDataSelectors.kravVirkDatoFom
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakStansAlderspensjonFlyttingMellomLandDtoSelectors.PesysDataSelectors.minst20AarTrygdetidKap20Avdoed
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakStansAlderspensjonFlyttingMellomLandDtoSelectors.PesysDataSelectors.minst20ArTrygdetid
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakStansAlderspensjonFlyttingMellomLandDtoSelectors.PesysDataSelectors.regelverkType
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakStansAlderspensjonFlyttingMellomLandDtoSelectors.SaksbehandlerValgSelectors.feilutbetaling
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakStansAlderspensjonFlyttingMellomLandDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakStansAlderspensjonFlyttingMellomLandDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.fraser.alderspensjon.FeilutbetalingAP
import no.nav.pensjon.brev.maler.fraser.alderspensjon.Skatteplikt
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlage
import no.nav.pensjon.brev.maler.vedlegg.vedleggInformasjonOmMedlemskapOgHelserettigheterEOES
import no.nav.pensjon.brev.maler.vedlegg.vedleggInformasjonOmMedlemskapOgHelserettigheterUtenforEOES
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.isNull
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

//MF_000128 / AP_STANS_FLYTT_MAN

@TemplateModelHelpers
object VedtakStansAlderspensjonFlyttingMellomLand : RedigerbarTemplate<VedtakStansAlderspensjonFlyttingMellomLandDto> {

    override val featureToggle = FeatureToggles.vedtakStansFlyttingMellomLand.toggle

    override val kode = Pesysbrevkoder.Redigerbar.PE_AP_STANS_FLYTTING_MELLOM_LAND
    override val kategori = TemplateDescription.Brevkategori.VEDTAK_FLYTTE_MELLOM_LAND
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.ALDER)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - stans av alderspensjon ved flytting mellom land",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        val garantipensjonInnvilget = pesysData.garantipensjonInnvilget
        val kravVirkDatoFom = pesysData.kravVirkDatoFom
        val minst20ArTrygdetid = pesysData.minst20ArTrygdetid
        val regelverkType = pesysData.regelverkType

        title {
            text(
                bokmal { + "Vi stanser alderspensjonen din fra " + kravVirkDatoFom.format() },
                nynorsk { + "Vi stansar alderspensjonen din frå " + kravVirkDatoFom.format() },
                english { + "We are stopping your retirement pension from " + kravVirkDatoFom.format() },
            )
        }
        outline {
            includePhrase(Vedtak.Overskrift)

            // flyttingAPstans
            paragraph {
                text(
                    bokmal { + "Vi har fått melding om at du har flyttet til " },
                    nynorsk { + "Vi har fått melding om at du har flytta til " },
                    english { + "We have received notice that you have moved to " }
                )
                eval(pesysData.brukersBostedsland.ifNull(fritekst("BOSTEDSLAND")))
                text(
                    bokmal { + "." },
                    nynorsk { + "." },
                    english { + "." },
                )
            }
            ifNotNull(
                pesysData.eksportForbudKode_safe,
                pesysData.eksportForbudKodeAvdoed_safe
            ) { eksportForbudKode, eksportForbudKodeAvdoed ->
                showIf(eksportForbudKode.equalTo(UFOR25_ALDER)) {
                    // eksportUngUforStans
                    paragraph {
                        text(
                            bokmal { + "Når du flytter til utlandet har du ikke lenger rett til pensjon etter reglene for unge uføre. "
                                    + "Derfor stanser vi utbetalingen av alderspensjonen din." },
                            nynorsk { + "Når du flyttar til utlandet har du ikkje lenger rett til alderspensjon etter reglane for unge uføre. "
                                    + "Derfor stansar vi utbetalinga av alderspensjonen din." },
                            english { + "When you move abroad, you are no longer eligible for retirement pension calculated in accordance with the regulations for young people with disabilities, "
                                    + "you have to live in Norway. We are therefore stopping your retirement pension." },
                        )
                    }
                }.orShowIf(eksportForbudKode.equalTo(FLYKT_ALDER) or eksportForbudKodeAvdoed.equalTo(FLYKT_ALDER)) {
                    // eksportFlyktningStans
                    paragraph {
                        text(
                            bokmal { + "Når du flytter til et land utenfor EØS-området har du ikke lenger rett til alderspensjon etter reglene for flyktninger. "
                                    + "Derfor stanser vi utbetalingen av alderspensjonen din." },
                            nynorsk { + "Når du flyttar til eit land utanfor EØS-området har du ikkje lenger rett til alderspensjon etter reglane for flyktningar. "
                                    + "Derfor stansar vi utbetalinga av alderspensjonen din." },
                            english { + "When you move to a country outside the EEA region, you are no longer eligible for retirement pension calculated in accordance with the regulations for refugees. "
                                    + "We are therefore stopping your retirement pension." },
                        )
                    }
                }
            }

            showIf(regelverkType.isOneOf(AP2011, AP2016) and not(minst20ArTrygdetid) and pesysData.eksportForbudKode_safe.isNull() and pesysData.minst20AarTrygdetidKap20Avdoed) {
                    // eksportAP2016Under20aarStans, eksportAP2011Under20aarStans
                    paragraph {
                        text(
                            bokmal { + "For å få utbetalt alderspensjonen din når du flytter til dette landet må du enten ha vært medlem i folketrygden i minst 20 år" },
                            nynorsk { + "For få utbetalt alderspensjonen din når du flyttar til dette landet må du anten ha vore medlem i folketrygda i minst 20 år" },
                            english { + "To be eligible for your retirement pension when you move to this country you must have been a member of the Norwegian National Insurance Scheme for at least 20 years" }
                        )
                        showIf(regelverkType.equalTo(AP2016)) {
                            text(
                                bokmal { + ", ha rett til tilleggspensjon eller ha tjent opp inntektspensjon." },
                                nynorsk { + ", ha rett til tilleggspensjon eller ha tent opp inntektspensjon. " },
                                english { + ", be entitled to a supplementary pension or have had a pensionable income. " }
                            )
                        }.orShow {
                            text(
                                bokmal { + " eller ha rett til tilleggspensjon. " },
                                nynorsk { + " eller ha rett til tilleggspensjon. " },
                                english { + " or be entitled to a supplementary pension. " }
                            )
                        }
                        text(
                            bokmal { + "Det har du ikke, og derfor stanser vi utbetalingen av alderspensjonen din." },
                            nynorsk { + "Det har du ikkje, og derfor stansar vi utbetalinga av alderspensjonen din." },
                            english { + "You do not meet any of these requirements, therefore we are stopping your retirement pension." }
                        )
                }
            }
            showIf(regelverkType.isOneOf(AP2011, AP2016) and pesysData.eksportForbudKodeAvdoed_safe.isNull() and pesysData.harAvdoed and not(pesysData.minst20AarTrygdetidKap20Avdoed)) {
                // eksportAP2016Under20aarStansAvdod, eksportAP2011Under20aarStansAvdod,
                paragraph {
                    text(
                        bokmal { + "Verken du eller avdøde har vært medlem i folketrygden i minst 20 år" },
                        nynorsk { + "Verken du eller avdøde har vore medlem i folketrygda i minst 20 år" },
                        english { + "Neither you nor the deceased have been a member of the Norwegian National Insurance Scheme for at least 20 years" }
                    )
                    showIf(regelverkType.equalTo(AP2016)) {
                        text(
                            bokmal { + ", rett til tilleggspensjon eller ha tjent opp inntektspensjon. " },
                            nynorsk { + ", rett til tilleggspensjon eller inntektspensjon. " },
                            english { + ", are entitled to a supplementary pension or income-based pension. " }
                        )
                    }.orShow {
                        text(
                            bokmal { + " eller har rett til tilleggspensjon. " },
                            nynorsk { + " eller har rett til tilleggspensjon. " },
                            english { + " or are entitled to a supplementary pension. " }
                        )
                    }
                    text(
                        bokmal { + "Da har du ikke rett til å få utbetalt alderspensjonen din når du flytter til dette landet. Derfor stanser vi utbetalingen av alderspensjonen din." },
                        nynorsk { + "Da har du ikkje rett til å få utbetalt alderspensjon når du flyttar til dette landet. Derfor stansar vi utbetalinga av alderspensjonen din." },
                        english { + "Then you are not eligible for your retirement pension when you move to this country. We are therefore stopping your retirement pension." }
                    )
                }
            }

            showIf(garantipensjonInnvilget) {
                // flyttingAPGarantipensjonHjemmel
                paragraph {
                    text(
                        bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 19-3, 20-10 og 22-12." },
                        nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 19-3, 20-10 og 22-12." },
                        english { + "This decision was made pursuant to the provisions of §§ 19-3, 20-10 and 22-12 of the National Insurance Act." },
                    )
                }
            }.orShow {
                // flyttingAPHjemmel
                paragraph {
                    text(
                        bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 19-3 og 22-12." },
                        nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 19-3 og 22-12." },
                        english { + "This decision was made pursuant to the provisions of §§ 19-3 and 22-12 of the National Insurance Act." },
                    )
                }
            }

            // flytteAPInfo
            paragraph {
                text(
                    bokmal { + "Du må selv gi oss beskjed dersom du flytter tilbake til Norge eller til et annet land. "
                            + "Da vil vi vurdere retten din til alderspensjon på nytt." },
                    nynorsk { + "Du må sjølv gi oss beskjed dersom du flyttar tilbake til Noreg eller til eit anna land. "
                            + "Da vil vi vurdere retten din til alderspensjon på nytt." },
                    english { + "You must give us notice if you move back to Norway or to another country. "
                            + "We will then review your right to retirement pension." },
                )
            }

            showIf(saksbehandlerValg.feilutbetaling) { includePhrase(FeilutbetalingAP) }

            // skattAPstans
            title1 {
                text(
                    bokmal { + "Stans av alderspensjon kan ha betydning for skatten" },
                    nynorsk { + "Stans av alderspensjon kan ha betyding for skatt" },
                    english { + "Stoppage of retirement pension may affect how much tax you pay" },
                )
            }
            includePhrase(Skatteplikt)

            includePhrase(Felles.RettTilAAKlage(vedlegg = vedleggDineRettigheterOgMulighetTilAaKlage))
            includePhrase(Felles.RettTilInnsyn(vedlegg = vedleggDineRettigheterOgMulighetTilAaKlage))
            includePhrase(Felles.HarDuSpoersmaal.alder)
        }
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlage, pesysData.dineRettigheterOgMulighetTilAaKlage)
        includeAttachment(vedleggInformasjonOmMedlemskapOgHelserettigheterEOES, pesysData.informasjonOmMedlemskap.equalTo(InformasjonOmMedlemskap.EOES))
        includeAttachment(vedleggInformasjonOmMedlemskapOgHelserettigheterUtenforEOES, pesysData.informasjonOmMedlemskap.equalTo(InformasjonOmMedlemskap.UTENFOR_EOES))
    }
}
