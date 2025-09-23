package no.nav.pensjon.brev.maler.alder.endring.sivilstand

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.*
import no.nav.pensjon.brev.api.model.BeloepEndring
import no.nav.pensjon.brev.api.model.MetaforceSivilstand
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandSaerskiltSatsDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandSaerskiltSatsDtoSelectors.AlderspensjonVedVirkSelectors.innvilgetFor67
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandSaerskiltSatsDtoSelectors.AlderspensjonVedVirkSelectors.minstenivaaIndividuellInnvilget
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandSaerskiltSatsDtoSelectors.AlderspensjonVedVirkSelectors.saertilleggInnvilget
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandSaerskiltSatsDtoSelectors.AlderspensjonVedVirkSelectors.ufoereKombinertMedAlder
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandSaerskiltSatsDtoSelectors.AlderspensjonVedVirkSelectors.uttaksgrad
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandSaerskiltSatsDtoSelectors.BeregnetPensjonPerManedVedVirkSelectors.grunnbelop
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandSaerskiltSatsDtoSelectors.BeregnetPensjonPerManedVedVirkSelectors.grunnpensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandSaerskiltSatsDtoSelectors.BeregnetPensjonPerManedVedVirkSelectors.totalPensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandSaerskiltSatsDtoSelectors.PesysDataSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandSaerskiltSatsDtoSelectors.PesysDataSelectors.beloepEndring
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandSaerskiltSatsDtoSelectors.PesysDataSelectors.beregnetPensjonPerManedVedVirk
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandSaerskiltSatsDtoSelectors.PesysDataSelectors.kravVirkDatoFom
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandSaerskiltSatsDtoSelectors.PesysDataSelectors.maanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandSaerskiltSatsDtoSelectors.PesysDataSelectors.maanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandSaerskiltSatsDtoSelectors.PesysDataSelectors.orienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandSaerskiltSatsDtoSelectors.PesysDataSelectors.regelverkType
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandSaerskiltSatsDtoSelectors.PesysDataSelectors.saerskiltSatsErBrukt
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandSaerskiltSatsDtoSelectors.PesysDataSelectors.sivilstand
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandSaerskiltSatsDtoSelectors.PesysDataSelectors.vedtakEtterbetaling
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandSaerskiltSatsDtoSelectors.SaksbehandlerValgSelectors.aarligKontrollEPS
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandSaerskiltSatsDtoSelectors.SaksbehandlerValgSelectors.epsAvkallPaaEgenAlderspenspensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandSaerskiltSatsDtoSelectors.SaksbehandlerValgSelectors.epsAvkallPaaEgenUfoeretrygd
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandSaerskiltSatsDtoSelectors.SaksbehandlerValgSelectors.epsHarInntektOver1G
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandSaerskiltSatsDtoSelectors.SaksbehandlerValgSelectors.epsHarRettTilFullAlderspensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandSaerskiltSatsDtoSelectors.SaksbehandlerValgSelectors.epsIkkeFylt62Aar
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandSaerskiltSatsDtoSelectors.SaksbehandlerValgSelectors.epsIkkeRettTilFullAlderspensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandSaerskiltSatsDtoSelectors.SaksbehandlerValgSelectors.epsTarUtAlderspensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandSaerskiltSatsDtoSelectors.SaksbehandlerValgSelectors.epsTarUtAlderspensjonIStatligSektor
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandSaerskiltSatsDtoSelectors.SaksbehandlerValgSelectors.epsTarUtUfoeretrygd
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandSaerskiltSatsDtoSelectors.SaksbehandlerValgSelectors.feilutbetaling
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandSaerskiltSatsDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandSaerskiltSatsDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.alder.endring.sivilstand.fraser.BetydningForUtbetaling
import no.nav.pensjon.brev.maler.alder.endring.sivilstand.fraser.DuFaarAP
import no.nav.pensjon.brev.maler.alder.endring.sivilstand.fraser.OmregningGarantiPen
import no.nav.pensjon.brev.maler.alder.endring.sivilstand.fraser.SivilstandSamboerHjemler
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
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

// MF_000102 Vedtaksbrevet dekker alle regelverkstypene.

@TemplateModelHelpers
object EndringAvAlderspensjonSivilstandSaerskiltSats :
    RedigerbarTemplate<EndringAvAlderspensjonSivilstandSaerskiltSatsDto> {

    override val featureToggle = FeatureToggles.endringAvAlderspensjonSivilstandVurderSaerskiltSats.toggle

    override val kode = Pesysbrevkoder.Redigerbar.PE_AP_ENDRING_AV_ALDERSPENSJON_SAERSKILT_SATS
    override val kategori = TemplateDescription.Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = Sakstype.pensjon

    override val template = createTemplate(
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - endring av alderspensjon (særskilt sats)",
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
        val minstenivaaIndividuellInnvilget = pesysData.alderspensjonVedVirk.minstenivaaIndividuellInnvilget
        val saerskiltSatsErBrukt = pesysData.saerskiltSatsErBrukt
        val saertilleggInnvilget = pesysData.alderspensjonVedVirk.saertilleggInnvilget
        val uforeKombinertMedAlder = pesysData.alderspensjonVedVirk.ufoereKombinertMedAlder
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

            // TODO skal alle disse saksbehandlervalgene være radioknapper?
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
                        bokmal { +sivilstandBestemtStorBokstav + " din som du forsørger har ikke rett til full alderspensjon fra folketrygden og har inntekt lavere enn grunnbeløpet " + grunnbelop.format() + "." },
                        nynorsk { +sivilstandBestemtStorBokstav + " din som du forsørgjer har ikkje rett til full alderspensjon frå folketrygda og har inntekt lågare enn grunnbeløpet " + grunnbelop.format() + "." },
                        english { +"Your " + sivilstandBestemtLitenBokstav + " you support does not have rights to full retirement pension through the National Insurance Act and has income lower than the basic amount which is " + grunnbelop.format() + "." },
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
                            +"Your retirement pension is not recalculated according to a special rate " + "because your " + sivilstandBestemtLitenBokstav + " receives contractual retirement pension from the public sector."
                        },
                    )
                }
            }
            showIf(saksbehandlerValg.epsTarUtUfoeretrygd) {
                // SaerSatsIkkeBruktEpsMottarUT
                paragraph {
                    text(
                        bokmal { +"Du får ikke beregnet alderspensjonen din med særskilt sats fordi " + sivilstandBestemtLitenBokstav + " din mottar uføretrygd fra folketrygden." },
                        nynorsk { +"Du får ikkje berekna alderspensjonen din med særskilt sats fordi " + sivilstandBestemtLitenBokstav + " din mottar uføretrygd frå folketrygda." },
                        english { +"Your retirement pension is not recalculated according to a special rate because your " + sivilstandBestemtLitenBokstav + " receives disability benefits through the National Insurance Act." },
                    )
                }
            }

            showIf(saerskiltSatsErBrukt) {
                paragraph {
                    text(
                        bokmal { +"Derfor har vi beregnet " },
                        nynorsk { +"Derfor har vi berekna " },
                        english { +"We have therefore recalculated your " },
                    )
                    showIf(regelverkType.isOneOf(AP1967)) {
                        text(
                            bokmal { +"særtillegget" },
                            nynorsk { +"særtillegget" },
                            english { +"special supplement" },
                        )
                    }.orShowIf(regelverkType.isOneOf(AP2011, AP2016)) {
                        text(
                            bokmal { +"pensjonstillegget" },
                            nynorsk { +"pensjonstillegget" },
                            english { +"basic pension" },
                        )
                    }
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
                includePhrase(BetydningForUtbetaling(regelverkType, pesysData.beloepEndring))
            }

            showIf(saksbehandlerValg.aarligKontrollEPS) {
                // SaerSatsInfoAarligKontrollEps
                paragraph {
                    text(
                        bokmal {
                            +"Fram til " + sivilstandBestemtLitenBokstav + " din fyller 67 år, har vi en årlig kontroll om " + sivilstandBestemtLitenBokstav + " din har rett til full alderpensjon. Du får nytt vedtak hvis dette fører til at alderspensjonen din blir omregnet."
                        },
                        nynorsk {
                            +"Fram til " + sivilstandBestemtLitenBokstav + " din fyller 67 år, har vi ein årleg kontroll av " + sivilstandBestemtLitenBokstav + " si rett til full alderpensjon. Du får nytt vedtak hvis dette fører til at alderspensjonen din blir omrekna."
                        },
                        english {
                            +"Until your " + sivilstandBestemtLitenBokstav + " turns 67 years of age, we have an annual control of their rights to a full retirement pension. " + "You will receive a new decision if this results in your retirement pension being recalculated."
                        },
                    )
                }
            }

            showIf(uforeKombinertMedAlder) {
                // innvilgelseAPogUTInnledn
                includePhrase(
                    UfoereAlder.DuFaar(
                        pesysData.beregnetPensjonPerManedVedVirk.totalPensjon,
                        pesysData.kravVirkDatoFom
                    )
                )
            }.orShow {
                includePhrase(
                    DuFaarAP(
                        pesysData.kravVirkDatoFom,
                        pesysData.beregnetPensjonPerManedVedVirk.totalPensjon
                    )
                )
            }

            includePhrase(Utbetalingsinformasjon)

            showIf(saerskiltSatsErBrukt) {
                paragraph {
                    text(
                        bokmal { + "Vedtaket er gjort etter folketrygdloven §§ " },
                        nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ " },
                        english { + "This decision was made pursuant to the provisions of §§ " },
                    )
                    showIf(regelverkType.isOneOf(AP1967)) {
                        includePhrase(SivilstandSamboerHjemler(sivilstand))
                        showIf(saertilleggInnvilget) {
                            text(
                                bokmal { + ", 3-3" },
                                nynorsk { + ", 3-3" },
                                english { + ", 3-3" },
                            )
                        }
                        text(
                            bokmal { + ", 19-8 og 22-12." },
                            nynorsk { + ", 19-8 og 22-12." },
                            english { + ", 19-8 and 22-12 of the National Insurance Act." },
                        )
                    }.orShowIf(regelverkType.isOneOf(AP2011, AP2016)) {
                        showIf(sivilstand.isOneOf(MetaforceSivilstand.SAMBOER_1_5)) {
                            text(
                                bokmal { + "1-5, " },
                                nynorsk { + "1-5, " },
                                english { + "1-5, " },
                            )
                        }
                        text(
                            bokmal { + "19-8, 19-9 og 22-12." },
                            nynorsk { + "19-8, 19-9 og 22-12." },
                            english { + "19-8, 19-9 and 22-12 of the National Insurance Act." },
                        )
                    }
                }
            }

            // Selectable - Hvis reduksjon tilbake i tid - feilutbetalingAP
            showIf(saksbehandlerValg.feilutbetaling) {
                includePhrase(FeilutbetalingAP)
            }

            // Hvis endring i pensjonen (Selectable) - skattAPendring
            showIf(pesysData.beloepEndring.isOneOf(BeloepEndring.ENDR_OKT, BeloepEndring.ENDR_RED)) {
                includePhrase(VedtakAlderspensjon.EndringKanHaBetydningForSkatt)
            }

            // Hvis etterbetaling (Selectable) - etterbetalingAP_002
            showIf(vedtakEtterbetaling) {
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
