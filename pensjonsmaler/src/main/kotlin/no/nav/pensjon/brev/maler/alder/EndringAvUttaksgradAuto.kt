package no.nav.pensjon.brev.maler.alder

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.*
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvUttaksgradAutoDto
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvUttaksgradAutoDtoSelectors.AlderspensjonVedVirkSelectors.privatAFPerBrukt
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvUttaksgradAutoDtoSelectors.AlderspensjonVedVirkSelectors.skjermingstilleggInnvilget
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvUttaksgradAutoDtoSelectors.AlderspensjonVedVirkSelectors.totalPensjon
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvUttaksgradAutoDtoSelectors.AlderspensjonVedVirkSelectors.ufoereKombinertMedAlder
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvUttaksgradAutoDtoSelectors.AlderspensjonVedVirkSelectors.uttaksgrad
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvUttaksgradAutoDtoSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvUttaksgradAutoDtoSelectors.harFlereBeregningsperioder
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvUttaksgradAutoDtoSelectors.kravVirkDatoFom
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvUttaksgradAutoDtoSelectors.maanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvUttaksgradAutoDtoSelectors.maanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvUttaksgradAutoDtoSelectors.opplysningerBruktIBeregningenEndretUttaksgradDto
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvUttaksgradAutoDtoSelectors.orienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvUttaksgradAutoDtoSelectors.regelverkType
import no.nav.pensjon.brev.maler.fraser.alderspensjon.AfpPrivatErBrukt
import no.nav.pensjon.brev.maler.fraser.alderspensjon.ArbeidsinntektOgAlderspensjonKort
import no.nav.pensjon.brev.maler.fraser.alderspensjon.MeldFraOmEndringer2
import no.nav.pensjon.brev.maler.fraser.alderspensjon.UfoereAlder
import no.nav.pensjon.brev.maler.fraser.alderspensjon.Utbetalingsinformasjon
import no.nav.pensjon.brev.maler.fraser.alderspensjon.VedtakAlderspensjon
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligPensjonFoerSkattAp2025
import no.nav.pensjon.brev.maler.vedlegg.vedleggOpplysningerBruktIBeregningenEndretUttaksgrad
import no.nav.pensjon.brev.maler.vedlegg.vedleggOrienteringOmRettigheterOgPlikter
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.lessThan
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.VEDTAKSBREV

/* MF_000099 / AP_ENDR_GRAD_AUTO: Brevet produseres ved innvilget søknad om endring av uttaksgrad i selvbetjeningsløsningen.
Malen har 2 resultater:
Endring i uttaksgrad -> når bruker endrer uttaksgrad til en uttaksgrad større enn null
Stans av alderspensjon -> når bruker endrer uttaksgrad til null */

// opphorETBegrunn, opphorBTBegrunn og opphorBTETBegrunn er fjernet fra malen

@TemplateModelHelpers
object EndringAvUttaksgradAuto : AutobrevTemplate<EndringAvUttaksgradAutoDto> {
    override val kode = Pesysbrevkoder.AutoBrev.PE_AP_ENDRING_UTTAKSGRAD_AUTO

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - endring av uttaksgrad (auto)",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = VEDTAKSBREV
        ),
        ) {
            title {
                showIf(alderspensjonVedVirk.uttaksgrad.greaterThan(0)) {
                    text(
                        bokmal { +"Vi har innvilget søknaden din om ".expr() + alderspensjonVedVirk.uttaksgrad.format() + " prosent alderspensjon" },
                        nynorsk { +"Vi har innvilga søknaden din om ".expr() + alderspensjonVedVirk.uttaksgrad.format() + " prosent alderspensjon" },
                        english { +"We have granted your application for ".expr() + alderspensjonVedVirk.uttaksgrad.format() + " percent retirement pension" }
                    )
                }.orShow {
                    text(
                        bokmal { +"Vi stanser utbetalingen av alderspensjonen din" },
                        nynorsk { +"Vi stansar utbetalinga av alderspensjonen din" },
                        english { +"We are stopping your retirement pension" }
                    )
                }
            }
            outline {
                includePhrase(Vedtak.Overskrift)

                showIf(alderspensjonVedVirk.uttaksgrad.greaterThan(0)) {
                    paragraph {
                        text(
                            bokmal { +"Du får ".expr() + alderspensjonVedVirk.totalPensjon.format() + " hver måned før skatt fra ".expr() + kravVirkDatoFom.format() },
                            nynorsk { +"Du får ".expr() + alderspensjonVedVirk.totalPensjon.format() + " kvar månad før skatt frå ".expr() + kravVirkDatoFom.format() },
                            english { +"You will receive ".expr() + alderspensjonVedVirk.totalPensjon.format() + " every month before tax from ".expr() + kravVirkDatoFom.format() }
                        )
                        showIf(alderspensjonVedVirk.ufoereKombinertMedAlder) {
                            // innvilgelseAPogUTInnledn -> Hvis løpende uføretrygd
                            text(
                                bokmal { +". Du får alderspensjon fra folketrygden i tillegg til uføretrygden din." },
                                nynorsk { +". Du får alderspensjon frå folketrygda ved sida av uføretrygda di." },
                                english { +". You will receive retirement pension through the National Insurance Scheme in addition to your disability benefit." }
                            )
                        }.orShow {
                            // innvilgelseAPInnledn -> Ingen løpende uføretrygd
                            text(
                                bokmal { +" i alderspensjon fra folketrygden." },
                                nynorsk { +" i alderspensjon frå folketrygda." },
                                english { +" as retirement pension from the National Insurance Scheme." }
                            )
                        }
                    }

                    // innvilgelseAPogAFPPrivat
                    showIf(alderspensjonVedVirk.privatAFPerBrukt) {
                        includePhrase(AfpPrivatErBrukt(uttaksgrad = alderspensjonVedVirk.uttaksgrad))
                    }

                    // utbetalingsInfoMndUtbet
                    includePhrase(Utbetalingsinformasjon)

                    // flereBeregningsperioderVedlegg
                    showIf(harFlereBeregningsperioder and alderspensjonVedVirk.totalPensjon.greaterThan(0)) {
                        includePhrase(Felles.FlereBeregningsperioder)
                    }
                }.orShow {
                    // stansAPInnledn
                    paragraph {
                        text(
                            bokmal { +"Vi viser til søknaden din, og stanser utbetalingen av alderspensjonen fra ".expr() + kravVirkDatoFom.format() + "." },
                            nynorsk { +"Vi viser til søknaden din, og stansar utbetalinga av alderspensjonen frå ".expr() + kravVirkDatoFom.format() + "." },
                            english { +"This is in reference to your application. We are stopping payment of your retirement pension from ".expr() + kravVirkDatoFom.format() + "." }
                        )
                    }
                    showIf(alderspensjonVedVirk.skjermingstilleggInnvilget) {
                        // fortsattSkjermingstillegg
                        paragraph {
                            text(
                                bokmal { +"Du får fortsatt utbetalt skjermingstillegget til uføre. Vedtaket er gjort etter folketrygdloven §§ 19-9a, 19-10 og 19-12." },
                                nynorsk { +"Du får fortsatt utbetalt skjermingstillegget til uføre. Vedtaket er gjort etter folketrygdlova §§ 19-9a, 19-10 og 19-12." },
                                english { +"You will still receive the supplement for disabled people. This decision was made pursuant to the provisions of §§ 19-9a, 19-10 and 19-12 of the National Insurance Act." }
                            )
                        }
                    }
                }

                showIf(regelverkType.equalTo(AP2011) and not(alderspensjonVedVirk.skjermingstilleggInnvilget)) {
                    // endrUtaksgradAP2011
                    paragraph {
                        text(
                            bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 19-10, 19-12 og 22-12." },
                            nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 19-10, 19-12 og 22-12." },
                            english { +"This decision was made pursuant to the provisions of §§ 19-10, 19-12 and 22-12 of the National Insurance Act." }
                        )
                    }
                }.orShowIf(regelverkType.equalTo(AP2016)) {
                    // endrUtaksgradAP2016
                    paragraph {
                        text(
                            bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 19-10, 19-12, 19-15, 20-14, 20-16, 20-19 og 22-12." },
                            nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 19-10, 19-12, 19-15, 20-14, 20-16, 20-19 og 22-12." },
                            english { +"This decision was made pursuant to the provisions of §§ 19-10, 19-12, 19-15, 20-14, 20-16, 20-19 and 22-12 of the National Insurance Act." }
                        )
                    }
                }.orShowIf(regelverkType.equalTo(AP2025)) {
                    // endrUtaksgradAP2025Soknad
                    paragraph {
                        text(
                            bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 20-14, 20-16 og 22-13." },
                            nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 20-14, 20-16 og 22-13." },
                            english { +"This decision was made pursuant to the provisions of §§ 20-14, 20-16 and 22-13 of the National Insurance Act." }
                        )
                    }
                }

                showIf(alderspensjonVedVirk.uttaksgrad.lessThan(100)) {
                    val uttaksgradStoerreEnn0 = alderspensjonVedVirk.uttaksgrad.greaterThan(0)
                    // gradsendrAPSoknadInfo, nySoknadAPInfo
                    paragraph {
                        text(
                            bokmal {
                                +"Du må sende oss en ny søknad når du ønsker å ta ut "
                                +ifElse(
                                    uttaksgradStoerreEnn0,
                                    ifTrue = "mer alderspensjon",
                                    ifFalse = "alderspensjon"
                                ) + ". En eventuell endring kan tidligst skje måneden etter at vi har mottatt søknaden."
                            },
                            nynorsk {
                                +"Du må sende oss ein ny søknad når du ønskjer å ta ut "
                                +ifElse(
                                    uttaksgradStoerreEnn0,
                                    ifTrue = "meir alderspensjon",
                                    ifFalse = "alderspensjon"
                                ) + ". Ei eventuell endring kan tidlegast skje månaden etter at vi har mottatt søknaden."
                            },
                            english {
                                +"You have to submit an application when you want to "
                                +ifElse(
                                    uttaksgradStoerreEnn0,
                                    ifTrue = "increase",
                                    ifFalse = "start drawing"
                                ) + " your retirement pension. Any change will be implemented at the earliest the month after we have received the application."
                            }
                        )
                    }
                }

                showIf(alderspensjonVedVirk.uttaksgrad.greaterThan(0)) {
                    // skattAPendring
                    includePhrase(VedtakAlderspensjon.EndringKanHaBetydningForSkatt)

                    // arbinntektAP
                    includePhrase(ArbeidsinntektOgAlderspensjonKort)

                    showIf(alderspensjonVedVirk.uttaksgrad.equalTo(100)) {
                        // nyOpptjeningHelAP_001
                        paragraph {
                            text(
                                bokmal { +"Hvis du har 100 prosent alderspensjon, gjelder økningen fra 1. januar året etter at skatteoppgjøret ditt er ferdig." },
                                nynorsk { +"Dersom du har 100 prosent alderspensjon, gjeld auken frå 1. januar året etter at skatteoppgjeret ditt er ferdig." },
                                english { +"If you are receiving a full (100 percent) retirement pension, the increase will come into effect from 1 January the year after your final tax settlement has been completed." }
                            )
                        }
                    }.orShow {
                        // nyOpptjeningGradertAP_001
                        paragraph {
                            text(
                                bokmal { +"Hvis du har lavere enn 100 prosent alderspensjon, blir økningen lagt til hvis du søker om endret grad eller ny beregning av den graden du har nå." },
                                nynorsk { +"Dersom du har lågare enn 100 prosent alderspensjon, blir auken lagd til dersom du søkjer om endra grad eller ny berekning av den graden du har no." },
                                english { +"If you are receiving retirement pension at a reduced rate (lower than 100 percent), the increase will come into effect if you apply to have the rate changed or have your current rate recalculated." }
                            )
                        }
                    }
                    includePhrase(UfoereAlder.UfoereKombinertMedAlder(alderspensjonVedVirk.ufoereKombinertMedAlder))
                    includePhrase(MeldFraOmEndringer2)
                }

                includePhrase(Felles.RettTilAAKlage)
                includePhrase(Felles.RettTilInnsyn(vedleggOrienteringOmRettigheterOgPlikter))
                includePhrase(Felles.HarDuSpoersmaal.alder)
            }
            includeAttachmentIfNotNull(vedleggOrienteringOmRettigheterOgPlikter, orienteringOmRettigheterOgPlikterDto)  // V00002
            includeAttachmentIfNotNull(vedleggMaanedligPensjonFoerSkatt, maanedligPensjonFoerSkattDto)  // V00003
            includeAttachmentIfNotNull(vedleggMaanedligPensjonFoerSkattAp2025, maanedligPensjonFoerSkattAP2025Dto)  // V00010
            includeAttachmentIfNotNull(vedleggOpplysningerBruktIBeregningenEndretUttaksgrad, opplysningerBruktIBeregningenEndretUttaksgradDto)  // V00005
        }
}