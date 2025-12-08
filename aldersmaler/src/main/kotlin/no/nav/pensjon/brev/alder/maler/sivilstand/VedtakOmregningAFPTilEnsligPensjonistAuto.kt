package no.nav.pensjon.brev.alder.maler.sivilstand

import no.nav.pensjon.brev.alder.maler.felles.Constants.DITT_NAV
import no.nav.pensjon.brev.alder.maler.felles.Constants.NAV_URL
import no.nav.pensjon.brev.alder.maler.felles.HarDuSpoersmaalAlder
import no.nav.pensjon.brev.alder.maler.felles.MeldeFraOmEndringer
import no.nav.pensjon.brev.alder.maler.felles.RettTilAAKlage
import no.nav.pensjon.brev.alder.maler.felles.RettTilInnsyn
import no.nav.pensjon.brev.alder.maler.felles.Vedtak
import no.nav.pensjon.brev.alder.maler.vedlegg.vedleggMaanedligPensjonFoerSkattAFPOffentlig
import no.nav.pensjon.brev.alder.maler.vedlegg.vedleggOrienteringOmRettigheterOgPlikter
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.SivilstandAvdoed
import no.nav.pensjon.brev.alder.model.sivilstand.VedtakOmregningAFPTilEnsligPensjonistAutoDto
import no.nav.pensjon.brev.alder.model.sivilstand.VedtakOmregningAFPTilEnsligPensjonistAutoDtoSelectors.AvdoedSelectors.navn
import no.nav.pensjon.brev.alder.model.sivilstand.VedtakOmregningAFPTilEnsligPensjonistAutoDtoSelectors.AvdoedSelectors.sivilstand
import no.nav.pensjon.brev.alder.model.sivilstand.VedtakOmregningAFPTilEnsligPensjonistAutoDtoSelectors.BeregnetPensjonPerManedVedVirkSelectors.minstenivaaIndividuelt
import no.nav.pensjon.brev.alder.model.sivilstand.VedtakOmregningAFPTilEnsligPensjonistAutoDtoSelectors.BeregnetPensjonPerManedVedVirkSelectors.saertillegg
import no.nav.pensjon.brev.alder.model.sivilstand.VedtakOmregningAFPTilEnsligPensjonistAutoDtoSelectors.BeregnetPensjonPerManedVedVirkSelectors.totalPensjon
import no.nav.pensjon.brev.alder.model.sivilstand.VedtakOmregningAFPTilEnsligPensjonistAutoDtoSelectors.antallBeregningsperioder
import no.nav.pensjon.brev.alder.model.sivilstand.VedtakOmregningAFPTilEnsligPensjonistAutoDtoSelectors.avdoed
import no.nav.pensjon.brev.alder.model.sivilstand.VedtakOmregningAFPTilEnsligPensjonistAutoDtoSelectors.beregnetPensjonPerManedVedVirk
import no.nav.pensjon.brev.alder.model.sivilstand.VedtakOmregningAFPTilEnsligPensjonistAutoDtoSelectors.erEndret
import no.nav.pensjon.brev.alder.model.sivilstand.VedtakOmregningAFPTilEnsligPensjonistAutoDtoSelectors.etterbetaling
import no.nav.pensjon.brev.alder.model.sivilstand.VedtakOmregningAFPTilEnsligPensjonistAutoDtoSelectors.harBarnUnder18
import no.nav.pensjon.brev.alder.model.sivilstand.VedtakOmregningAFPTilEnsligPensjonistAutoDtoSelectors.kravVirkDatoFom
import no.nav.pensjon.brev.alder.model.sivilstand.VedtakOmregningAFPTilEnsligPensjonistAutoDtoSelectors.maanedligPensjonFoerSkattAFPOffentligDto
import no.nav.pensjon.brev.alder.model.sivilstand.VedtakOmregningAFPTilEnsligPensjonistAutoDtoSelectors.orienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object VedtakOmregningAFPTilEnsligPensjonistAuto :
    AutobrevTemplate<VedtakOmregningAFPTilEnsligPensjonistAutoDto> {
    override val kode = Aldersbrevkoder.AutoBrev.VEDTAK_OMREGNING_AFP_TIL_ENSLIG_PENSJONIST_AUTO

    override val template =
        createTemplate(
            languages = languages(Language.Bokmal, Language.Nynorsk),
            letterMetadata =
                LetterMetadata(
                    displayTitle = "Vedtak - omregning av AFP til enslig pensjonist",
                    distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
                    brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
                ),
        ) {
            title {
                text(
                    bokmal { +"Vi har regnet om den avtalefestede pensjonen din" },
                    nynorsk { +"Vi har rekna om den avtalefesta pensjonen din" },
                )
            }
            outline {
                includePhrase(Vedtak.Overskrift)

                paragraph {
                    showIf(
                        (erEndret and antallBeregningsperioder.equalTo(1))
                            or (antallBeregningsperioder.greaterThan(1)),
                    ) {
                        text(
                            bokmal {
                                +"Vi har mottatt melding om at " + avdoed.navn +
                                    " er død, og vi har regnet om din avtalefestede pensjon (AFP) fra Statens pensjonskasse fra " +
                                    kravVirkDatoFom.format() +
                                    " fordi du har blitt enslig pensjonist. Vi vil også informere deg om rettigheter i folketrygden du kan ha etter avdøde."
                            },
                            nynorsk {
                                +"Vi har mottatt melding om at " + avdoed.navn +
                                    " er død, og vi har rekna om avtalefesta pensjonen (AFP) din frå Statens Pensjonskasse frå " +
                                    kravVirkDatoFom.format() +
                                    " fordi du har blitt einsleg pensjonist. Vi vil også informere deg om kva rettar i folketrygda du kan ha etter avdøde."
                            },
                        )
                    }.orShowIf(!erEndret and antallBeregningsperioder.equalTo(1)) {
                        text(
                            bokmal {
                                +"Vi har mottatt melding om at " + avdoed.navn +
                                    " er død. Din avtalefestede pensjon (AFP) fra Statens pensjonskasse endres ikke, men vi vil informere deg om rettigheter i folketrygden du kan ha etter avdøde."
                            },
                            nynorsk {
                                +"Vi har mottatt melding om at " + avdoed.navn +
                                    " er død. Din avtalefesta pensjon frå Statens pensjonskasse blir ikkje endra, men vi vil informera deg om kva rettar i folketrygda du kan ha etter avdøde."
                            },
                        )
                    }
                }

                showIf(beregnetPensjonPerManedVedVirk.totalPensjon.greaterThan(0)) {
                    paragraph {
                        showIf(antallBeregningsperioder.greaterThan(0)) {
                            text(
                                bokmal {
                                    +"Du får " + beregnetPensjonPerManedVedVirk.totalPensjon.format() + " i AFP hver måned før skatt."
                                },
                                nynorsk {
                                    +"Du får " + beregnetPensjonPerManedVedVirk.totalPensjon.format() +
                                        "  i AFP kvar månad før skatt."
                                },
                            )
                        }

                        showIf(antallBeregningsperioder.greaterThan(1)) {
                            text(
                                bokmal { +" Du kan lese mer om andre beregningsperioder i vedlegget." },
                                nynorsk { +" Du kan lese meir om andre utrekningsperiodar i vedlegget." },
                            )
                        }
                    }
                }

                showIf(beregnetPensjonPerManedVedVirk.totalPensjon.equalTo(0)) {
                    paragraph {
                        showIf(antallBeregningsperioder.greaterThan(0)) {
                            text(
                                bokmal { +"Du får ikke utbetalt AFP på grunn av høy inntekt." },
                                nynorsk { +"Du får ikkje utbetalt AFP på grunn av høg inntekt." },
                            )
                        }

                        showIf(antallBeregningsperioder.greaterThan(1)) {
                            text(
                                bokmal { +" Du kan lese mer om andre beregningsperioder i vedlegget." },
                                nynorsk { +" Du kan lese meir om andre utrekningsperiodar i vedlegget." },
                            )
                        }
                    }
                }

                paragraph {
                    showIf(beregnetPensjonPerManedVedVirk.saertillegg) {
                        showIf(beregnetPensjonPerManedVedVirk.minstenivaaIndividuelt) {
                            text(
                                bokmal {
                                    +"Vedtaket er gjort etter lov om AFP for medlemmer av Statens pensjonskasse § 3, jf. folketrygdloven §§ 3-2 og 3-3 og forskrift om omregning av AFP § 3, jf. folketrygdloven § 19-8."
                                },
                                nynorsk {
                                    +"Vedtaket er gjort etter lov om AFP for medlemmer av Statens pensjonskasse § 3, jf. folketrygdlova §§ 3-2 og 3-3 og forskrift om omrekning av AFP § 3, jf. folketrygdlova § 19-8."
                                },
                            )
                        }.orShow {
                            text(
                                bokmal {
                                    +"Vedtaket er gjort etter lov om AFP for medlemmer av Statens pensjonskasse § 3, jf. folketrygdloven §§ 3-2 og 3-3."
                                },
                                nynorsk {
                                    +"Vedtaket er gjort etter lov om AFP for medlemmer av Statens pensjonskasse § 3, jf. folketrygdlova §§ 3-2 og 3-3."
                                },
                            )
                        }
                    }.orShow {
                        showIf(beregnetPensjonPerManedVedVirk.minstenivaaIndividuelt) {
                            text(
                                bokmal {
                                    +"Vedtaket er gjort etter lov om AFP for medlemmer av Statens pensjonskasse § 3, jf. folketrygdloven § 3-2 og forskrift om omregning av AFP § 3, jf. folketrygdloven § 19-8."
                                },
                                nynorsk {
                                    +"Vedtaket er gjort etter lov om AFP for medlemmer av Statens pensjonskasse § 3, jf. folketrygdlova § 3-2 og forskrift om omrekning av AFP § 3, jf. folketrygdlova § 19-8."
                                },
                            )
                        }.orShow {
                            text(
                                bokmal {
                                    +"Vedtaket er gjort etter lov om AFP for medlemmer av Statens pensjonskasse § 3, jf. folketrygdloven § 3-2."
                                },
                                nynorsk {
                                    +"Vedtaket er gjort etter lov om AFP for medlemmer av Statens pensjonskasse § 3, jf. folketrygdlova § 3-2."
                                },
                            )
                        }
                    }
                }

                ifNotNull(avdoed.sivilstand) { sivilstand ->
                    showIf(
                        sivilstand.isOneOf(
                            SivilstandAvdoed.SAMBOER1_5,
                            SivilstandAvdoed.PARTNER,
                            SivilstandAvdoed.GIFT,
                        ),
                    ) {

                        title2 {
                            text(
                                bokmal { +"Andre pensjonsordninger" },
                                nynorsk { +"Andre pensjonsordningar" },
                            )
                        }

                        paragraph {
                            text(
                                bokmal {
                                    +"Dersom avdøde hadde en privat eller offentlig pensjonsordning og du har spørsmål om dette, kan du kontakte avdødes arbeidsgiver. Du kan også ta kontakt med pensjonsordningen eller forsikringsselskapet."
                                },
                                nynorsk {
                                    +"Dersom avdøde hadde ei privat eller offentleg pensjonsordning og du har spørsmål om dette, kan du kontakte arbeidsgivaren til den avdøde. Du kan også ta kontakt med pensjonsordninga eller forsikringsselskapet."
                                },
                            )
                        }

                        title2 {
                            text(
                                bokmal { +"Rettigheter hvis avdøde har bodd eller arbeidet i utlandet" },
                                nynorsk { +"Rettar når avdøde har budd eller arbeidd i utlandet" },
                            )
                        }

                        paragraph {
                            text(
                                bokmal {
                                    +"Hvis avdøde har bodd eller arbeidet i utlandet kan dette få betydning for dine rettigheter etter avdøde. Norge har trygdesamarbeid med en rekke land gjennom EØS-avtalen og andre avtaler. Derfor kan du også ha rett til pensjon fra andre land. Vi kan hjelpe deg med søknad til land Norge har trygdeavtale med."
                                },
                                nynorsk {
                                    +"Dersom avdøde har budd eller arbeidd i utlandet kan dette få noko å seie for dine rettar etter avdøde. Noreg har trygdesamarbeid med ei rekkje land gjennom EØS-avtalen og andre avtalar. Derfor kan du også ha rett til pensjon frå andre land. Vi kan hjelpe deg med søknad til land Noreg har trygdeavtale med."
                                },
                            )
                        }
                    }
                }

                showIf(harBarnUnder18) {
                    title2 {
                        text(
                            bokmal { +"For deg som har barn under 18 år" },
                            nynorsk { +"For deg som har barn under 18 år" },
                        )
                    }

                    paragraph {
                        text(
                            bokmal {
                                +"Forsørger du barn under 18 år, kan du ha rett til utvidet barnetrygd. I tillegg kan barn ha rett til barnepensjon. Du finner søknadskjema og mer informasjon om dette på $NAV_URL"
                            },
                            nynorsk {
                                +"Forsørgjer du barn under 18 år, kan du ha rett til utvida barnetrygd. I tillegg kan barn ha rett til barnepensjon. Du finn søknadsskjema og meir informasjon om dette på $NAV_URL"
                            },
                        )
                    }
                }

                showIf(etterbetaling) {
                    includePhrase(Vedtak.Etterbetaling(virkDatoFom = kravVirkDatoFom))
                }

                includePhrase(MeldeFraOmEndringer)
                includePhrase(RettTilAAKlage)
                includePhrase(RettTilInnsyn(vedlegg = vedleggOrienteringOmRettigheterOgPlikter))

                title2 {
                    text(
                        bokmal { +"Sjekk utbetalingene dine" },
                        nynorsk { +"Sjekk utbetalingane dine" },
                    )
                }

                paragraph {
                    text(
                        bokmal {
                            +"Du får pensjonen utbetalt den 20. hver måned, eller senest siste virkedag før denne datoen. Du kan se alle utbetalinger du har mottatt på $DITT_NAV. Her kan du også endre kontonummeret ditt."
                        },
                        nynorsk {
                            +"Du får pensjon utbetalt den 20. kvar månad, eller seinast siste yrkedag før denne datoen. Du kan sjå alle utbetalingar du har fått på $DITT_NAV. Her kan du også endre kontonummeret ditt."
                        },
                    )
                }

                includePhrase(HarDuSpoersmaalAlder)
            }
            includeAttachment(
                vedleggOrienteringOmRettigheterOgPlikter,
                orienteringOmRettigheterOgPlikterDto,
            )
            includeAttachment(
                vedleggMaanedligPensjonFoerSkattAFPOffentlig,
                maanedligPensjonFoerSkattAFPOffentligDto,
            )
        }
}
