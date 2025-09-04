package no.nav.pensjon.brev.maler.alder.endring.sivilstand

import no.nav.pensjon.brev.api.model.KravArsakType
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.AlderspensjonVedVirkSelectors.garantipensjonInnvilget
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.AlderspensjonVedVirkSelectors.innvilgetFor67
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.AlderspensjonVedVirkSelectors.minstenivaaIndividuellInnvilget
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.AlderspensjonVedVirkSelectors.minstenivaaPensjonsistParInnvilget
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.AlderspensjonVedVirkSelectors.pensjonstilleggInnvilget
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.AlderspensjonVedVirkSelectors.saertilleggInnvilget
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.AlderspensjonVedVirkSelectors.ufoereKombinertMedAlder
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.AlderspensjonVedVirkSelectors.uttaksgrad
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.BeregnetPensjonPerManedVedVirkSelectors.grunnbelop
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.BeregnetPensjonPerManedVedVirkSelectors.grunnpensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.BeregnetPensjonPerManedVedVirkSelectors.totalPensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.beregnetPensjonPerManedVedVirk
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.kravVirkDatoFom
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.maanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.maanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.orienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.regelverkType
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.saerskiltSatsErBrukt
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.sivilstand
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.vedtakEtterbetaling
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.aarligKontrollEPS
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.beloepEndring
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.endringPensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.epsAvkallPaaEgenAlderspenspensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.epsAvkallPaaEgenUfoeretrygd
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.epsHarInntektOver1G
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.epsHarRettTilFullAlderspensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.epsIkkeFylt62Aar
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.epsIkkeRettTilFullAlderspensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.epsTarUtAlderspensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.epsTarUtAlderspensjonIStatligSektor
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.epsTarUtUfoeretrygd
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.etterbetaling
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.feilutbetaling
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.maler.alder.endring.sivilstand.fraser.BetydningForUtbetaling
import no.nav.pensjon.brev.maler.alder.endring.sivilstand.fraser.DuFaarAP
import no.nav.pensjon.brev.maler.alder.endring.sivilstand.fraser.OmregningGarantiPen
import no.nav.pensjon.brev.maler.alder.endring.sivilstand.fraser.SivilstandHjemler
import no.nav.pensjon.brev.maler.fraser.alderspensjon.*
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlage
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligPensjonFoerSkattAp2025
import no.nav.pensjon.brev.maler.vedlegg.vedleggOrienteringOmRettigheterOgPlikter
import no.nav.pensjon.brev.model.bestemtForm
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

// MF_000102 Vedtaksbrevet dekker alle regelverkstypene.

@TemplateModelHelpers
object EndringAvAlderspensjonSivilstandSaerskiltSats : RedigerbarTemplate<EndringAvAlderspensjonSivilstandDto> {
    override val kode = Pesysbrevkoder.Redigerbar.PE_AP_ENDRING_AV_ALDERSPENSJON_SIVILSTAND
    override val kategori = TemplateDescription.Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = Sakstype.pensjon

    override val template = createTemplate(
        name = kode.name,
        letterDataType = EndringAvAlderspensjonSivilstandDto::class,
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - Endring av alderspensjon (sivilstand)",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        val kravVirkDatoFom = pesysData.kravVirkDatoFom.format()
        val regelverkType = pesysData.regelverkType
        val sivilstand = pesysData.sivilstand
        val sivilstandBestemtStorBokstav = pesysData.sivilstand.bestemtForm(storBokstav = true)
        val sivilstandBestemtLitenBokstav = pesysData.sivilstand.bestemtForm(storBokstav = false)
        val grunnpensjon = pesysData.beregnetPensjonPerManedVedVirk.grunnpensjon.ifNull(then = Kroner(0))
        val garantipensjonInnvilget = pesysData.alderspensjonVedVirk.garantipensjonInnvilget
        val pensjonstilleggInnvilget = pesysData.alderspensjonVedVirk.pensjonstilleggInnvilget
        val minstenivaaIndividuellInnvilget = pesysData.alderspensjonVedVirk.minstenivaaIndividuellInnvilget
        val minstenivaaPensjonistParInnvilget = pesysData.alderspensjonVedVirk.minstenivaaPensjonsistParInnvilget
        val saertilleggInnvilget = pesysData.alderspensjonVedVirk.saertilleggInnvilget
        val saerskiltSatsErBrukt = pesysData.saerskiltSatsErBrukt
        val uforeKombinertMedAlder = pesysData.alderspensjonVedVirk.ufoereKombinertMedAlder
        val totalPensjon = pesysData.beregnetPensjonPerManedVedVirk.totalPensjon.format()
        val vedtakEtterbetaling = pesysData.vedtakEtterbetaling
        val uttaksgrad = pesysData.alderspensjonVedVirk.uttaksgrad.ifNull(then = (0))
        val grunnbelop = pesysData.beregnetPensjonPerManedVedVirk.grunnbelop
        val innvilgetFor67 = pesysData.alderspensjonVedVirk.innvilgetFor67

        title {
            text(
                bokmal { +"Vi har beregnet alderspensjon din på nytt fra " + kravVirkDatoFom },
                nynorsk { +"Vi har berekna alderspensjonen din på nytt frå " + kravVirkDatoFom },
                english { +"We have recalculated your retirement pension from " + kravVirkDatoFom },
            )
        }
        outline {
            includePhrase(Vedtak.Overskrift)
            includePhrase(OmregningGarantiPen(regelverkType))

            showIf(saksbehandlerValg.epsIkkeFylt62Aar) {
                // SaerSatsBruktEpsUnder62
                paragraph {
                    text(
                        bokmal { +sivilstandBestemtStorBokstav + " din du forsørger har en inntekt lavere enn grunnbeløpet " + grunnbelop.format() + "." },
                        nynorsk { +sivilstandBestemtStorBokstav + " din du forsørgjer har ei inntekt lågare enn grunnbeløpet " + grunnbelop.format() + "." },
                        english { +"Your " + sivilstandBestemtLitenBokstav + " you support has an income lower than the basic amount which is " + grunnbelop.format() + "." },
                    )
                }
            }

            showIf(saksbehandlerValg.epsIkkeRettTilFullAlderspensjon) {
                // SaerSatsBruktEpsIkkeRettTilAP
                paragraph {
                    text(
                        bokmal {
                            +sivilstandBestemtStorBokstav + " din som du forsørger har ikke rett til full" + " alderspensjon fra folketrygden og har inntekt lavere enn grunnbeløpet " + grunnbelop.format() + "."
                        },

                        nynorsk {
                            +sivilstandBestemtStorBokstav + " din som du forsørgjer har ikkje rett til full" + " alderspensjon frå folketrygda og har inntekt lågare enn grunnbeløpet " + grunnbelop.format() + "."
                        },

                        english {
                            +"Your " + sivilstandBestemtLitenBokstav + " you support does not have rights to full" + " retirement pension through the National Insurance Act and has income lower than the basic amount which is " + grunnbelop.format() + "."
                        },
                    )
                }
            }
            showIf(saksbehandlerValg.epsAvkallPaaEgenAlderspenspensjon) {

                // SaerSatsBruktEpsGittAvkallAP
                paragraph {
                    text(
                        bokmal { +sivilstandBestemtStorBokstav + " din har gitt avkall på sin alderspensjon fra folketrygden." },
                        nynorsk { +sivilstandBestemtStorBokstav + " din har gitt avkall på alderspensjon sin frå folketrygda." },
                        english { +"Your " + sivilstandBestemtLitenBokstav + " has renounced their retirement pension through the National Insurance Act." },
                    )
                }
            }

            showIf(saksbehandlerValg.epsAvkallPaaEgenUfoeretrygd) {
                // SaerSatsBruktEpsGittAvkallUT
                paragraph {
                    text(
                        bokmal { +sivilstandBestemtStorBokstav + " din har gitt avkall på sin uføretrygd fra folketrygden." },
                        nynorsk { +sivilstandBestemtStorBokstav + " din har gitt avkall på uføretrygda si frå folketrygda." },
                        english { +"Your " + sivilstandBestemtLitenBokstav + " has renounced their disability benefits through the National Insurance Act." },
                    )
                }
            }
            // TODO kan ikke dette data-styres?
            showIf(saksbehandlerValg.epsHarInntektOver1G) {
                // SaerSatsIkkeBruktEpsInntektOver1G, SaerSatsIkkeBruktEpsRettTilFullAP, SaerSatsIkkeBruktEpsMottarAP, SaerSatsIkkeBruktEpsMottarAfp, SaerSatsIkkeBruktEpsMottarUT
                paragraph {
                    text(
                        bokmal {
                            +"Du får ikke beregnet alderspensjonen din med særskilt sats fordi " + sivilstandBestemtLitenBokstav + " din har inntekt høyere enn grunnbeløpet (" + grunnpensjon.format() + ")."
                        },
                        nynorsk {
                            +"Du får ikkje berekna alderspensjonen din med særskilt sats fordi " + sivilstandBestemtLitenBokstav + " din har inntekt høgare enn grunnbeløpet (" + grunnpensjon.format() + ")."
                        },
                        english {
                            +"Your retirement pension is not recalculated according to a special rate because your " + sivilstandBestemtLitenBokstav + " has a higher income than the basic amount which is " + grunnpensjon.format() + "."
                        },
                    )
                }
            }

            showIf(saksbehandlerValg.epsHarRettTilFullAlderspensjon) {
                // SaerSatsIkkeBruktEpsRettTilFullAP
                paragraph {
                    text(
                        bokmal {
                            +"Du får ikke beregnet alderspensjonen din med særskilt sats fordi " + sivilstandBestemtLitenBokstav + " din har rett til full alderspensjon fra folketrygden."
                        },
                        nynorsk {
                            +"Du får ikkje berekna alderspensjonen din med særskilt sats fordi " + sivilstandBestemtLitenBokstav + " din har rett til full alderspensjon frå folketrygda."
                        },
                        english {
                            +"Your retirement pension is not recalculated according to a special rate because your " + sivilstandBestemtLitenBokstav + " has rights to full retirement pension through the National Insurance Act."
                        },
                    )
                }
            }

            showIf(saksbehandlerValg.epsTarUtAlderspensjon) {
                // SaerSatsIkkeBruktEpsMottarAP
                paragraph {
                    text(
                        bokmal {
                            +"Du får ikke beregnet alderspensjonen din med særskilt sats fordi " + sivilstandBestemtLitenBokstav + " din mottar alderspensjon fra folketrygden."
                        },
                        nynorsk {
                            +"Du får ikkje berekna alderspensjonen din med særskilt sats fordi " + sivilstandBestemtLitenBokstav + " din mottar alderspensjon frå folketrygda."
                        },
                        english {
                            +"Your retirement pension is not recalculated according to a special rate because your " + sivilstandBestemtLitenBokstav + " recieves retirement pension through the National Insurance Act."
                        },
                    )
                }
            }

            showIf(saksbehandlerValg.epsTarUtAlderspensjonIStatligSektor) {
                // SaerSatsIkkeBruktEpsMottarAfp
                paragraph {
                    text(
                        bokmal {
                            +"Du får ikke beregnet alderspensjonen din med særskilt sats fordi " + sivilstandBestemtLitenBokstav + " din mottar AFP i statlig sektor."
                        },
                        nynorsk {
                            +"Du får ikkje berekna alderspensjonen din med særskilt sats fordi " + sivilstandBestemtLitenBokstav + " din mottar AFP i statleg sektor."
                        },
                        english {
                            +"Your retirement pension is not recalculated according to a special rate because your " + sivilstandBestemtLitenBokstav + " receives contractual retirement pension from the public sector."
                        },
                    )
                }
            }

            showIf(saksbehandlerValg.epsTarUtUfoeretrygd) {
                // SaerSatsIkkeBruktEpsMottarUT
                paragraph {
                    text(
                        bokmal {
                            +"Du får ikke beregnet alderspensjonen din med særskilt sats fordi " +
                                    sivilstandBestemtLitenBokstav + " din mottar uføretrygd fra folketrygden."
                        },
                        nynorsk {
                            +"Du får ikkje berekna alderspensjonen din med særskilt sats fordi " +
                                    sivilstandBestemtLitenBokstav + " din mottar uføretrygd frå folketrygda."
                        },
                        english {
                            +"Your retirement pension is not recalculated according to a special rate because your " +
                                    sivilstandBestemtLitenBokstav + " receives disability benefits through the National Insurance Act."
                        },
                    )
                }
            }

            showIf(saerskiltSatsErBrukt) {
                paragraph {
                    text(
                        bokmal { +"Derfor har vi beregnet pensjonstillegget" },
                        nynorsk { +"Derfor har vi berekna pensjonstillegget" },
                        english { +"We have therefore recalculated your basic pension" },
                    )

                    showIf(minstenivaaIndividuellInnvilget) {
                        text(
                            bokmal { +" og minstenivåtillegget" },
                            nynorsk { +" og minstenivåtillegget" },
                            english { +" and minimum pension supplement" },
                        )
                    }
                    text(
                        bokmal { +" ditt på nytt med særskilt sats." },
                        nynorsk { +" ditt på nytt med særskilt sats." },
                        english { +" according to a special rate." },
                    )
                }
            }

            showIf(saerskiltSatsErBrukt) {
                includePhrase(BetydningForUtbetaling(regelverkType, saksbehandlerValg.beloepEndring))
            }

            showIf(saksbehandlerValg.aarligKontrollEPS) {
                // SaerSatsInfoAarligKontrollEps
                paragraph {
                    text(
                        bokmal {
                            +"Fram til " + sivilstandBestemtLitenBokstav + " din fyller 67 år, har vi en årlig kontroll om " + sivilstandBestemtLitenBokstav + " din har rett til full alderpensjon." + " Du får nytt vedtak hvis dette fører til at alderspensjonen din blir omregnet."
                        },
                        nynorsk {
                            +"Fram til " + sivilstandBestemtLitenBokstav + " din fyller 67 år, har vi ein årleg kontroll av " + sivilstandBestemtLitenBokstav + " si rett til full alderpensjon." + " Du får nytt vedtak hvis dette fører til at alderspensjonen din blir omrekna."
                        },
                        english {
                            +"Until your " + sivilstandBestemtLitenBokstav + " turns 67 years of age, we have an annual control of their rights to a full retirement pension." + " You will receive a new decision if this results in your retirement pension being recalculated."
                        },
                    )
                }
            }

            showIf(uforeKombinertMedAlder) {
                includePhrase(
                    UfoereAlder.DuFaar(
                        pesysData.beregnetPensjonPerManedVedVirk.totalPensjon, pesysData.kravVirkDatoFom
                    )
                )
            }.orShow {
                includePhrase(DuFaarAP(kravVirkDatoFom, totalPensjon))
            }

            includePhrase(Utbetalingsinformasjon)

            includePhrase(
                SivilstandHjemler(
                    regelverkType = regelverkType,
                    kravArsakType = KravArsakType.VURDER_SERSKILT_SATS.expr(),
                    sivilstand = sivilstand,
                    saertilleggInnvilget = saertilleggInnvilget,
                    pensjonstilleggInnvilget = pensjonstilleggInnvilget,
                    minstenivaaIndividuellInnvilget = minstenivaaIndividuellInnvilget,
                    minstenivaaPensjonistParInnvilget = minstenivaaPensjonistParInnvilget,
                    garantipensjonInnvilget = garantipensjonInnvilget,
                    saerskiltSatsErBrukt = saerskiltSatsErBrukt,
                ),
            )

            // Selectable - Hvis reduksjon tilbake i tid - feilutbetalingAP
            showIf(saksbehandlerValg.feilutbetaling) {
                includePhrase(FeilutbetalingAP)
            }

            // Hvis endring i pensjonen (Selectable) - skattAPendring
            showIf(saksbehandlerValg.endringPensjon) {
                includePhrase(VedtakAlderspensjon.EndringKanHaBetydningForSkatt)
            }

            // Hvis etterbetaling (Selectable) - etterbetalingAP_002
            showIf(saksbehandlerValg.etterbetaling or vedtakEtterbetaling) {
                includePhrase(Vedtak.Etterbetaling(pesysData.kravVirkDatoFom))
            }

            // Arbeidsinntekt og pensjon
            includePhrase(
                ArbeidsinntektOgAlderspensjon(
                    innvilgetFor67 = innvilgetFor67,
                    uttaksgrad = uttaksgrad.ifNull(then = (0)),
                    uforeKombinertMedAlder = uforeKombinertMedAlder,
                ),
            )

            includePhrase(InformasjonOmAlderspensjon)
            includePhrase(MeldeFraOmEndringer)
            includePhrase(Felles.RettTilAAKlage(vedlegg = vedleggDineRettigheterOgMulighetTilAaKlage))
            includePhrase(Felles.RettTilInnsyn(vedlegg = vedleggDineRettigheterOgMulighetTilAaKlage))
            includePhrase(Felles.HarDuSpoersmaal.alder)
        }
        includeAttachment(
            vedleggOrienteringOmRettigheterOgPlikter,
            pesysData.orienteringOmRettigheterOgPlikterDto,
        )
        includeAttachmentIfNotNull(
            vedleggMaanedligPensjonFoerSkatt,
            pesysData.maanedligPensjonFoerSkattDto,
        )
        includeAttachmentIfNotNull(
            vedleggMaanedligPensjonFoerSkattAp2025,
            pesysData.maanedligPensjonFoerSkattAP2025Dto,
        )
    }
}
