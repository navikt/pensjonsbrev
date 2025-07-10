package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.BeloepEndring.ENDR_OKT
import no.nav.pensjon.brev.api.model.BeloepEndring.ENDR_RED
import no.nav.pensjon.brev.api.model.BeloepEndring.UENDRET
import no.nav.pensjon.brev.api.model.KravInitiertAv.BRUKER
import no.nav.pensjon.brev.api.model.KravInitiertAv.NAV
import no.nav.pensjon.brev.api.model.KravInitiertAv.VERGE
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPaaGjenlevenderettIAlderspensjonDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPaaGjenlevenderettIAlderspensjonDtoSelectors.PesysDataSelectors.AlderspensjonVedVirkSelectors.totalPensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPaaGjenlevenderettIAlderspensjonDtoSelectors.PesysDataSelectors.AlderspensjonVedVirkSelectors.uttaksgrad
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPaaGjenlevenderettIAlderspensjonDtoSelectors.PesysDataSelectors.AvdoedSelectors.redusertTrygdetidAvtaleland
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPaaGjenlevenderettIAlderspensjonDtoSelectors.PesysDataSelectors.AvdoedSelectors.redusertTrygdetidEOS
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPaaGjenlevenderettIAlderspensjonDtoSelectors.PesysDataSelectors.AvdoedSelectors.redusertTrygdetidNorge
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPaaGjenlevenderettIAlderspensjonDtoSelectors.PesysDataSelectors.AvtalelandSelectors.erEOSLand
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPaaGjenlevenderettIAlderspensjonDtoSelectors.PesysDataSelectors.AvtalelandSelectors.navn
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPaaGjenlevenderettIAlderspensjonDtoSelectors.PesysDataSelectors.BeregnetPensjonPerManedSelectors.antallBeregningsperioderPensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPaaGjenlevenderettIAlderspensjonDtoSelectors.PesysDataSelectors.KravSelectors.kravInitiertAv
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPaaGjenlevenderettIAlderspensjonDtoSelectors.PesysDataSelectors.YtelseskomponentInformasjonSelectors.beloepEndring
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPaaGjenlevenderettIAlderspensjonDtoSelectors.PesysDataSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPaaGjenlevenderettIAlderspensjonDtoSelectors.PesysDataSelectors.avdoed
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPaaGjenlevenderettIAlderspensjonDtoSelectors.PesysDataSelectors.avtaleland
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPaaGjenlevenderettIAlderspensjonDtoSelectors.PesysDataSelectors.beregnetPensjonPerManed
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPaaGjenlevenderettIAlderspensjonDtoSelectors.PesysDataSelectors.dineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPaaGjenlevenderettIAlderspensjonDtoSelectors.PesysDataSelectors.krav
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPaaGjenlevenderettIAlderspensjonDtoSelectors.PesysDataSelectors.maanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPaaGjenlevenderettIAlderspensjonDtoSelectors.PesysDataSelectors.maanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPaaGjenlevenderettIAlderspensjonDtoSelectors.PesysDataSelectors.ytelseskomponentInformasjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPaaGjenlevenderettIAlderspensjonDtoSelectors.SaksbehandlerValgSelectors.avdoedNavn
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPaaGjenlevenderettIAlderspensjonDtoSelectors.SaksbehandlerValgSelectors.samboerUtenFellesBarn
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPaaGjenlevenderettIAlderspensjonDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPaaGjenlevenderettIAlderspensjonDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.maler.fraser.alderspensjon.DuFaarHverMaaned
import no.nav.pensjon.brev.maler.fraser.alderspensjon.FlereBeregningsperioder
import no.nav.pensjon.brev.maler.fraser.alderspensjon.TrygdetidTittel
import no.nav.pensjon.brev.maler.fraser.alderspensjon.Utbetalingsinformasjon
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlage
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligPensjonFoerSkattAp2025
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object AvslagPaaGjenlevenderettIAlderspensjon : RedigerbarTemplate<AvslagPaaGjenlevenderettIAlderspensjonDto> {
    override val kategori = TemplateDescription.Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.ALDER)
    override val kode = Pesysbrevkoder.Redigerbar.PE_AP_AVSLAG_GJENLEVENDERETT

    override val template = createTemplate(
        name = kode.name,
        letterDataType = AvslagPaaGjenlevenderettIAlderspensjonDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "avslag på gjenlevenderett i alderspensjon",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        val initiertAvBrukerEllerVerge = pesysData.krav.kravInitiertAv.isOneOf(BRUKER, VERGE)
        val initiertAvNav = pesysData.krav.kravInitiertAv.equalTo(NAV)

        title {
            // avslagGjRettAPTittel_001
            showIf(
                pesysData.alderspensjonVedVirk.totalPensjon.greaterThan(0) and initiertAvBrukerEllerVerge
            ) {
                text(
                    Bokmal to "Vi har avslått søknaden din om gjenlevenderett i alderspensjonen",
                    Nynorsk to "Vi har avslått søknaden din om attlevanderett i alderspensjonen",
                    English to "We have declined your application for survivor's rights in your retirement pension"
                )
            }.orShowIf(
                // avslagGjRettAPTittel_002
                pesysData.alderspensjonVedVirk.totalPensjon.greaterThan(0) and initiertAvNav
            ) {
                text(
                    Bokmal to "Vi har vurdert om du har pensjonsrettigheter etter avdøde",
                    Nynorsk to "Vi har vurdert om du har pensjonsrettar etter avdøde",
                    English to "We have assessed whether you have survivor’s rights in your retirement pension"
                )
            }.orShowIf(pesysData.alderspensjonVedVirk.totalPensjon.equalTo(0)) {
                // avslagAPGjRettTittel_001
                text(
                    Bokmal to "Vi har avslått søknaden din om alderspensjon med gjenlevenderett",
                    Nynorsk to "Vi har avslått søknaden din om alderspensjon med attlevanderett",
                    English to "We have declined your application for  retirement pension with survivor’s rights"
                )
            }
        }
        outline {
            includePhrase(Vedtak.Overskrift)
            showIf(initiertAvNav) {
                // avslagGjRettAPAvdod_001
                paragraph {
                    textExpr(
                        Bokmal to "Vi har fått beskjed om at ".expr() + saksbehandlerValg.avdoedNavn + " døde " + fritekst("dato") + ".",
                        Nynorsk to "Vi har fått beskjed om at ".expr() + saksbehandlerValg.avdoedNavn + " døydde " + fritekst("dato") + ".",
                        English to "We have received notice that ".expr() + saksbehandlerValg.avdoedNavn + " died " + fritekst(
                            "dato"
                        ) + "."
                    )
                }
            }

            // TODO: Her kjem alle Under x års medlemstid-blokkene, som har returnverdi 1 i doksys
            // Må finne ut av korleis vi handterer dei
            // Heilt fram til og med Under 20 år-blokkene

            showIf(initiertAvBrukerEllerVerge) {
                // avslagGJRettAPGiftUnder5aarSøknad_001
                paragraph {
                    text(
                        Bokmal to "For å ha rettigheter som gift må du og avdøde ha vært gift i minst fem år eller ha felles barn.",
                        Nynorsk to "For å ha rettar som gift må du og avdøde ha vore gifte i minst fem år eller ha felles barn.",
                        English to "To have rights as a married person, you and the deceased must have been married for at least five years or have joint children."
                    )
                }
                paragraph {
                    textExpr(
                        Bokmal to "Du og ".expr() + saksbehandlerValg.avdoedNavn + " har ikke vært gift i minst fem år. Ekteskapet ble inngått " + fritekst(
                            "dato"
                        ) + " og ektefellen din døde " + fritekst("dato") + ". Dere har heller ikke felles barn. Derfor har vi avslått søknaden din.",
                        Nynorsk to "Du og ".expr() + saksbehandlerValg.avdoedNavn + " har ikkje vore gifte i minst fem år. Ekteskapet blei inngått " + fritekst(
                            "dato"
                        ) + ", og ektefellen din døydde " + fritekst("dato") + ". De har heller ikkje felles barn. Derfor har vi avslått søknaden din.",
                        English to "You and ".expr() + saksbehandlerValg.avdoedNavn + " have not been married for at least five years. Your marriage took place on " + fritekst(
                            "dato"
                        ) + " and your spouse died on " + fritekst("dato") + ". You also have no joint children. We have declined your application for this reason."
                    )
                }
            }.orShowIf(initiertAvNav) {
                // avslagGjRettAPVilkårGift5aarNav_001
                paragraph {
                    text(
                        Bokmal to "For å ha rettigheter som gift må du og avdøde ha vært gift i minst fem år eller ha felles barn.",
                        Nynorsk to "For å ha rettar som gift må du og avdøde ha vore gifte i minst fem år eller ha felles barn.",
                        English to "To have rights as a married person, you and the deceased must have been married for at least five years or have joint children."
                    )
                }
                paragraph {
                    textExpr(
                        Bokmal to "Du og ".expr() + saksbehandlerValg.avdoedNavn + " har ikke vært gift i minst fem år. Ekteskapet ble inngått " + fritekst(
                            "dato"
                        ) + " og ektefellen din døde " + fritekst("dato") + ". Dere har heller ikke felles barn. Derfor har du ikke rettigheter etter avdøde.",
                        Nynorsk to "Du og ".expr() + saksbehandlerValg.avdoedNavn + " har ikkje vore gifte i minst fem år. Ekteskapet blei inngått " + fritekst(
                            "dato"
                        ) + ", og ektefellen din døydde " + fritekst("dato") + ". De har heller ikkje felles barn. Derfor har du ikkje rettar etter avdøde.",
                        English to "You and ".expr() + saksbehandlerValg.avdoedNavn + " have not been married for at least five years. Your marriage took place on " + fritekst(
                            "dato"
                        ) + " and your spouse died on " + fritekst("dato") + ". You also have no joint children. You have no survivor’s rights in your retirement pension for this reason."
                    )
                }
            }

            showIf(saksbehandlerValg.samboerUtenFellesBarn) {
                // avslagGjRettAPVilkårSamboMFB_001 / avslagGjRettAPVilkårSamboMFBNav_001
                paragraph {
                    text(
                        Bokmal to "For å ha rettigheter som samboer må du og avdøde tidligere ha vært gift i minst fem år eller ha/hatt felles barn.",
                        Nynorsk to "For å ha rettar som sambuar må du og avdøde tidlegare ha vore gifte i minst fem år eller ha/hatt felles barn.",
                        English to "To have rights as a cohabiting partner, you and the deceased must have been previously married for at least five years or have/had joint children."
                    )
                }

                paragraph {
                    textExpr(
                        Bokmal to "Du og ".expr() + saksbehandlerValg.avdoedNavn + " har ikke tidligere vært gift i minst fem år. Dere var heller ikke samboere med felles barn.",
                        Nynorsk to "Du og ".expr() + saksbehandlerValg.avdoedNavn + " har ikkje tidlegare vore gifte i minst fem år. De var heller ikkje sambuarar med felles barn.",
                        English to "You and ".expr() + saksbehandlerValg.avdoedNavn + " have not been previously married for at least five years. You were also not cohabiting partners with joint children."
                    )
                    showIf(initiertAvBrukerEllerVerge) {
                        text(
                            Bokmal to "Derfor har vi avslått søknaden din.",
                            Nynorsk to "Derfor har vi avslått søknaden din.",
                            English to "We have declined your application for this reason."
                        )
                    }.orShowIf(initiertAvNav) {
                        text(
                            Bokmal to "Derfor har du ikke rettigheter etter avdøde.",
                            Nynorsk to "Derfor har du ikkje rettar etter avdøde.",
                            English to "You have no survivor’s rights in your retirement pension for this reason."
                        )
                    }
                }
            }
            // omregnetEnsligAP_002
            paragraph {
                text(
                    Bokmal to "Vi har regnet om pensjonen din fordi du har blitt enslig pensjonist. Dette er gjort etter folketrygdloven § 3-2.",
                    Nynorsk to "Vi har rekna om pensjonen din fordi du har blitt einsleg pensjonist. Dette er gjort etter folketrygdlova § 3-2.",
                    English to "We have recalculated your pension because you have become a single pensioner. This decision was made pursuant to the provisions of § 3-2 of the National Insurance Act."
                )
            }

            showIf(pesysData.ytelseskomponentInformasjon.beloepEndring.equalTo(UENDRET) and pesysData.alderspensjonVedVirk.totalPensjon.greaterThan(0)) {
                // ingenEndringBelop_002
                paragraph {
                    text(
                        Bokmal to "Dette får derfor ingen betydning for utbetalingen din.",
                        Nynorsk to "Dette får derfor ingen følgjer for utbetalinga di.",
                        English to "Therefore, this does not affect the amount you will receive."
                    )
                }
            }.orShowIf(pesysData.ytelseskomponentInformasjon.beloepEndring.equalTo(ENDR_OKT)) {
                // nyBeregningAPØkning_001
                paragraph {
                    text(
                        Bokmal to "Dette fører til at pensjonen din øker.",
                        Nynorsk to "Dette fører til at pensjonen din aukar.",
                        English to "This leads to an increase in your retirement pension."
                    )
                }
            }
            includePhrase(DuFaarHverMaaned(pesysData.alderspensjonVedVirk.totalPensjon))

            showIf(pesysData.beregnetPensjonPerManed.antallBeregningsperioderPensjon.greaterThan(0)
                and pesysData.alderspensjonVedVirk.uttaksgrad.greaterThan(0)) {
                includePhrase(Utbetalingsinformasjon)
            }

            includePhrase(FlereBeregningsperioder(pesysData.beregnetPensjonPerManed.antallBeregningsperioderPensjon, pesysData.alderspensjonVedVirk.totalPensjon))

            // avslagGjRettAPHjemmel_001
            paragraph {
                text(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 17-2, 17-11, 19-3, 19-16 og 22-12.",
                    Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 17-2, 17-11, 19-3, 19-16 og 22-12.",
                    English to "This decision was made pursuant to the provisions of §§ 17-2, 17-11, 19-3, 19-16 and 22-12 of the National Insurance Act."
                )
            }

            ifNotNull(pesysData.avtaleland) { land ->
                showIf(land.erEOSLand) {
                    // avslagGjRettAPHjemmelEOS_001
                    paragraph {
                        text(
                            Bokmal to "Vedtaket er også gjort etter EØS-avtalens forordning 883/2004 artikkel 6 og 57.",
                            Nynorsk to "Vedtaket er også gjort etter EØS-avtalens forordning 883/2004 artikkel 6 og 57.",
                            English to "This decision was also made pursuant to the provisions of Articles 6 and 57 of the Regulation (EC) no. 883/2004."
                        )
                    }
                }.orShow {
                    // avslagGjRettAPHjemmelAvtale_001
                    paragraph {
                        textExpr(
                            Bokmal to "Vedtaket er også gjort etter artikkel ".expr() + fritekst("Legg inn aktuelle artikler om sammenlegging og eksport") + " i trygdeavtalen med ",
                            Nynorsk to "Vedtaket er også gjort etter artikkel".expr() + fritekst("Legg inn aktuelle artikler om sammenlegging og eksport") + " i trygdeavtalen med ",
                            English to "This decision was also made pursuant to the provisions of Article ".expr() + fritekst("Legg inn aktuelle artikler om sammenlegging og eksport") + " in the social security agreement with "
                        )
                        ifNotNull(land.navn) { navn ->
                            textExpr(
                                Bokmal to navn + ".",
                                Nynorsk to navn + ".",
                                English to navn + "."
                            )
                        }
                    }
                }
            }

            showIf(pesysData.avdoed.redusertTrygdetidNorge or pesysData.avdoed.redusertTrygdetidEOS or pesysData.avdoed.redusertTrygdetidAvtaleland) {
                // norskTTAvdodInfoAvslag_001
                includePhrase(TrygdetidTittel)
                paragraph {
                    text(
                        Bokmal to "Trygdetid er perioder med medlemskap i folketrygden. Som hovedregel er dette bo- eller arbeidsperioder i Norge.",
                        Nynorsk to "Trygdetid er periodar med medlemskap i folketrygda. Som hovudregel er dette bu- eller arbeidsperiodar i Noreg.",
                        English to "The period of national insurance coverage is periods as a member of the National Insurance Scheme. As a general rule, these are periods registered as living or working in Norway."
                    )
                }

                showIf(pesysData.avdoed.redusertTrygdetidNorge) {
                    // avslagUnder1aarTTAvdod_001
                    paragraph {
                        textExpr(
                            Bokmal to "Våre opplysninger viser at ".expr() + saksbehandlerValg.avdoedNavn + " " + fritekst("har bodd eller arbeidet i Norge i angi antall dager/ måneder / ikke har bodd eller arbeidet i Norge") + ".",
                            Nynorsk to "Ifølgje våre opplysningar har ".expr() + saksbehandlerValg.avdoedNavn + " " + fritekst(" budd eller arbeidd i Noreg i angi antall dagar/ månader / ikkje budd eller arbeidd i Noreg") + ".",
                            English to fritekst("We have registered that / We have no record of") + " " + saksbehandlerValg.avdoedNavn + " " + fritekst(" has been living or working in Norway for angi antall days/ months /  living or working in Norway.") + ".",
                        )
                    }
                }
                showIf(pesysData.avdoed.redusertTrygdetidEOS) {
                    // avslagUnder3aarTTAvdodEOS_001
                    paragraph {
                        textExpr(
                            Bokmal to "Vi har fått opplyst at ".expr() + saksbehandlerValg.avdoedNavn + " har " + fritekst("angi antall") + " måneder opptjeningstid i annet EØS-land. Den samlede trygdetiden i Norge og annet EØS-land er " + fritekst("angi samlet trygdetid i Norge og EØS-land") + ".",
                            Nynorsk to "Vi har fått opplyst at ".expr() + saksbehandlerValg.avdoedNavn + " har " + fritekst("angi antall") + " månader oppteningstid i anna EØS-land. Den samla trygdetiden i Norge og anna EØS-land er " + fritekst("angi samlet trygdetid i Norge og EØS-land") + ".",
                            English to "We have been informed that ".expr() + saksbehandlerValg.avdoedNavn + " has " + fritekst("angi antall") + " months of national insurance coverage in an other EEA country. The total national insurance coverage in Norway and an other EEA country is " + fritekst("angi samlet trygdetid i Norge og EØS-land") + "."
                        )
                    }
                }

                showIf(pesysData.avdoed.redusertTrygdetidAvtaleland) {
                    // avslagUnder3aarTTAvdodAvtale_001
                    paragraph {
                        textExpr(
                            Bokmal to "Vi har fått opplyst at ".expr() + saksbehandlerValg.avdoedNavn + " har " + fritekst("angi antall") + " måneder opptjeningstid i annet avtaleland. Den samlede trygdetiden i Norge og annet avtaleland er " + fritekst("angi samlet trygdetid i Norge og avtaleland") + ".",
                            Nynorsk to "Vi har fått opplyst at ".expr() + saksbehandlerValg.avdoedNavn + " har " + fritekst("angi antall") + " månader oppteningstid i anna avtaleland. Den samla trygdetiden i Norge og anna avtaleland er " + fritekst("angi samlet trygdetid i Norge og avtaleland") + ".",
                            English to "We have been informed that ".expr() + saksbehandlerValg.avdoedNavn + " has " + fritekst("angi antall") + " months of national insurance coverage in an other signatory country. The total national insurance coverage in Norway and an other signatory country is " + fritekst("angi samlet trygdetid i Norge og avtaleland") + "."
                        )
                    }
                }

            }

            showIf(pesysData.ytelseskomponentInformasjon.beloepEndring.isOneOf(ENDR_OKT, ENDR_RED)) {
                includePhrase(Vedtak.EndringKanHaBetydningForSkatt)
            }

            includePhrase(Felles.RettTilAAKlage(vedleggDineRettigheterOgMulighetTilAaKlage))
            includePhrase(Felles.RettTilInnsyn(vedleggDineRettigheterOgMulighetTilAaKlage))
            includePhrase(Felles.HarDuSpoersmaal.alder)
        }
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlage, pesysData.dineRettigheterOgMulighetTilAaKlageDto)
        includeAttachmentIfNotNull(vedleggMaanedligPensjonFoerSkatt, pesysData.maanedligPensjonFoerSkattDto)
        includeAttachmentIfNotNull(vedleggMaanedligPensjonFoerSkattAp2025, pesysData.maanedligPensjonFoerSkattAP2025Dto)
    }
}