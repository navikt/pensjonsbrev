package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.*
import no.nav.pensjon.brev.api.model.EksportForbudKode.FLYKT_ALDER
import no.nav.pensjon.brev.api.model.EksportForbudKode.UFOR25_ALDER
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.dineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakStansAlderspensjonFlyttingMellomLandDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakStansAlderspensjonFlyttingMellomLandDtoSelectors.PesysDataSelectors.brukersBostedsland
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakStansAlderspensjonFlyttingMellomLandDtoSelectors.PesysDataSelectors.eksportForbudKode
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakStansAlderspensjonFlyttingMellomLandDtoSelectors.PesysDataSelectors.garantipensjonInnvilget
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakStansAlderspensjonFlyttingMellomLandDtoSelectors.PesysDataSelectors.harAvdod
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakStansAlderspensjonFlyttingMellomLandDtoSelectors.PesysDataSelectors.harEksportForbud
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakStansAlderspensjonFlyttingMellomLandDtoSelectors.PesysDataSelectors.kravVirkDatoFom
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakStansAlderspensjonFlyttingMellomLandDtoSelectors.PesysDataSelectors.minst20ArTrygdetid
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakStansAlderspensjonFlyttingMellomLandDtoSelectors.PesysDataSelectors.regelverkType
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakStansAlderspensjonFlyttingMellomLandDtoSelectors.SaksbehandlerValgSelectors.feilutbetaling
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakStansAlderspensjonFlyttingMellomLandDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakStansAlderspensjonFlyttingMellomLandDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.maler.fraser.alderspensjon.FeilutbetalingAP
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlage
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object VedtakStansAlderspensjonFlyttingMellomLand : RedigerbarTemplate<VedtakStansAlderspensjonFlyttingMellomLandDto> {

    override val kode = Pesysbrevkoder.Redigerbar.PE_AP_STANS_FLYTTING_MELLOM_LAND
    override val kategori = TemplateDescription.Brevkategori.VEDTAK_FLYTTE_MELLOM_LAND
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.ALDER)

    override val template = createTemplate(
        name = kode.name,
        letterDataType = VedtakStansAlderspensjonFlyttingMellomLandDto::class,
        languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - stans av alderspensjon ved flytting mellom land",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        val kravVirkDatoFom = pesysData.kravVirkDatoFom
        val brukersBostedsland = pesysData.brukersBostedsland
        val eksportForbudKode = pesysData.eksportForbudKode
        val regelverkType = pesysData.regelverkType
        val minst20ArTrygdetid = pesysData.minst20ArTrygdetid
        val harEksportForbud = pesysData.harEksportForbud
        val harAvdod = pesysData.harAvdod
        val garantipensjonInnvilget = pesysData.garantipensjonInnvilget

        title {
            textExpr(
                Bokmal to "Vi stanser alderspensjonen din fra ".expr() + kravVirkDatoFom.format(),
                Nynorsk to "Vi stansar alderspensjonen din frå ".expr() + kravVirkDatoFom.format(),
                English to "We are stopping your retirement pension from ".expr() + kravVirkDatoFom.format(),
            )
        }
        outline {
            includePhrase(Vedtak.Overskrift)

            // flyttingAPstans
            paragraph {
                textExpr(
                    Bokmal to "Vi har fått melding om at du har flyttet til ".expr() + brukersBostedsland,
                    Nynorsk to "Vi har fått melding om at du har flytta ti ".expr() + brukersBostedsland,
                    English to "We have received notice that you have moved to ".expr() + brukersBostedsland
                )
            }
            showIf(eksportForbudKode.isOneOf(UFOR25_ALDER)) {
                // eksportUngUforStans
                paragraph {
                    text(
                        Bokmal to "Når du flytter til utlandet har du ikke lenger rett til pensjon etter reglene for unge uføre. "
                                + "Derfor stanser vi utbetalingen av alderspensjonen din.",
                        Nynorsk to "Når du flyttar til utlandet har du ikkje lenger rett til alderspensjon etter reglane for unge uføre. "
                                + "Derfor stansar vi utbetalinga av alderspensjonen din.",
                        English to "When you move abroad, you are no longer eligible for retirement pension calculated in accordance with the regulations for young people with disabilities, "
                                + "you have to live in Norway. We are therefore stopping your retirement pension.",
                    )
                }
            }.orShowIf(eksportForbudKode.isOneOf(FLYKT_ALDER)) {
                // eksportFlyktningStans
                paragraph {
                    text(
                        Bokmal to "Når du flytter til et land utenfor EØS-området har du ikke lenger rett til alderspensjon etter reglene for flyktninger. "
                                + "Derfor stanser vi utbetalingen av alderspensjonen din.",
                        Nynorsk to "Når du flyttar til eit land utanfor EØS-området har du ikkje lenger rett til alderspensjon etter reglane for flyktningar. "
                                + "Derfor stansar vi utbetalinga av alderspensjonen din.",
                        English to "When you move to a country outside the EEA region, you are no longer eligible for retirement pension calculated in accordance with the regulations for refugees. "
                                + "We are therefore stopping your retirement pension.",
                    )
                }
            }
            showIf(regelverkType.isOneOf(AP2011, AP2016) and not(harEksportForbud) and not(minst20ArTrygdetid)) {
                showIf(harAvdod) {
                    // eksportAP2016Under20aarStansAvdod, eksportAP2011Under20aarStansAvdod,
                    paragraph {
                        textExpr(
                            Bokmal to "Verken du eller avdøde har vært medlem i folketrygden i minst 20 år".expr()
                                    + ifElse(
                                regelverkType.isOneOf(AP2016),
                                ifTrue = ", rett til tilleggspensjon eller ha tjent opp inntektspensjon. ",
                                ifFalse = " eller har rett til tilleggspensjon. "
                            )
                                    + "Da har du ikke rett til å få utbetalt alderspensjonen din når du flytter til dette landet. "
                                    + "Derfor stanser vi utbetalingen av alderspensjonen din.",
                            Nynorsk to "Verken du eller avdøde har vært medlem i folketrygda i minst 20 år".expr()
                                    + ifElse(
                                regelverkType.isOneOf(AP2016),
                                ifTrue = ", rett til tilleggspensjon eller inntektspensjon",
                                ifFalse = " eller har rett til tilleggspensjon. "
                            )
                                    + "Da har du ikkje rett til å få utbetalt alderspensjon når du flyttar til dette landet. "
                                    + "Derfor stansar vi utbetalinga av alderspensjonen din.",
                            English to "Neither you nor the deceased have been a member of the Norwegian National Insurance Scheme for at least 20 years".expr()
                                    + ifElse(
                                regelverkType.isOneOf(AP2016),
                                ifTrue = ", are entitled to a supplementary pension or income-based pension. ",
                                ifFalse = " or are entitled to a supplementary pension. "
                            )
                                    + "Then you are not eligible for your retirement pension when you move to this country. "
                                    + "We are therefore stopping your retirement pension."
                        )
                    }
                }.orShow {
                    // eksportAP2016Under20aarStans, eksportAP2011Under20aarStans
                    paragraph {
                        textExpr(
                            Bokmal to "For å få utbetalt alderspensjonen din når du flytter til dette landet må du enten ha vært medlem i folketrygden i minst 20 år".expr() + ifElse(
                                regelverkType.isOneOf(AP2016),
                                ifTrue = ", ha rett til tilleggspensjon eller ha tjent opp inntektspensjon.",
                                ifFalse = " eller ha rett til tilleggspensjon. "
                            )
                                    + "Det har du ikke, og derfor stanser vi utbetalingen av alderspensjonen din.",
                            Nynorsk to "For få utbetalt alderspensjonen din når du flyttar til dette landet må du anten ha vore medlem i folketrygda i minst 20 år".expr() + ifElse(
                                regelverkType.isOneOf(AP2016),
                                ifTrue = ", ha rett til tilleggspensjon eller ha tent opp inntektspensjon. ",
                                ifFalse = " eller ha rett til tilleggspensjon. "
                            )
                                    + "Det har du ikkje, og derfor stansar vi utbetalinga av alderspensjonen din.",
                            English to "To be eligible for your retirement pension when you move to this country you must have been a member of the Norwegian National Insurance Scheme for at least 20 years".expr() + ifElse(
                                regelverkType.isOneOf(AP2016),
                                ifTrue = ", be entitled to a supplementary pension or have had a pensionable income.",
                                ifFalse = " or be entitled to a supplementary pension. "
                            )
                                    + "You do not meet any of these requirements, therefore we are stopping your retirement pension."
                        )
                    }
                }

            }

            showIf(garantipensjonInnvilget) {
                // flyttingAPGarantipensjonHjemmel
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 19-3, 20-10 og 22-12.",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 19-3, 20-10 og 22-12.",
                        English to "This decision was made pursuant to the provisions of §§ 19-3, 20-10 and 22-12 of the National Insurance Act.",
                    )
                }
            }.orShow {
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 19-3 og 22-12.",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 19-3 og 22-12.",
                        English to "This decision was made pursuant to the provisions of §§ 19-3 and 22-12 of the National Insurance Act.",
                    )
                }
            }

            // flytteAPInfo
            paragraph {
                text(
                    Bokmal to "Du må selv gi oss beskjed dersom du flytter tilbake til Norge eller til et annet land. "
                            + "Da vil vi vurdere retten din til alderspensjon på nytt.",
                    Nynorsk to "Du må sjølv gi oss beskjed dersom du flyttar tilbake til Noreg eller til eit anna land. "
                            + "Da vil vi vurdere retten din til alderspensjon på nytt.",
                    English to "You must give us notice if you move back to Norway or to another country. "
                            + "We will then review your right to retirement pension",
                )
            }

            showIf(saksbehandlerValg.feilutbetaling) { includePhrase(FeilutbetalingAP) }

            // skattAPstans
            title1 {
                text(
                    Bokmal to "Stans av alderspensjon kan ha betydning for skatten",
                    Nynorsk to "Stans av alderspensjon kan ha betyding for skatt",
                    English to "Stoppage of retirement pension may affect how much tax you pay",
                )
            }
            paragraph {
                text(
                    Bokmal to "Questions about tax liability to Norway after moving abroad must be directed to the Tax Administration. "
                            + "You must clarify questions about tax liability to your country of residence with the local tax authorities.",
                    Nynorsk to "Spørsmål om skatteplikt til Noreg etter flytting til utlandet må du rette til skatteetaten. "
                            + "Du må sjølv avklare spørsmål om skatteplikt til det landet du bur i, med skatteorgana der.",
                    English to "Questions about tax liability to Norway after moving abroad must be directed to the Tax Administration. "
                            + "You must clarify questions about tax liability to your country of residence with the local tax authorities.",
                )
            }

            includePhrase(Felles.RettTilAAKlage(vedlegg = vedleggDineRettigheterOgMulighetTilAaKlage))
            includePhrase(Felles.RettTilInnsyn(vedlegg = vedleggDineRettigheterOgMulighetTilAaKlage))
            includePhrase(Felles.HarDuSpoersmaal.alder)
        }
        includeAttachment(
            vedleggDineRettigheterOgMulighetTilAaKlage,
            pesysData.dineRettigheterOgMulighetTilAaKlageDto
        )
    }
}
