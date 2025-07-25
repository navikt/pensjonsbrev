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
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPaaGjenlevenderettIAlderspensjonDtoSelectors.PesysDataSelectors.BrukerSelectors.faktiskBostedsland_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPaaGjenlevenderettIAlderspensjonDtoSelectors.PesysDataSelectors.KravSelectors.kravInitiertAv
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPaaGjenlevenderettIAlderspensjonDtoSelectors.PesysDataSelectors.YtelseskomponentInformasjonSelectors.beloepEndring
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPaaGjenlevenderettIAlderspensjonDtoSelectors.PesysDataSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPaaGjenlevenderettIAlderspensjonDtoSelectors.PesysDataSelectors.avdoed
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPaaGjenlevenderettIAlderspensjonDtoSelectors.PesysDataSelectors.avtaleland
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPaaGjenlevenderettIAlderspensjonDtoSelectors.PesysDataSelectors.beregnetPensjonPerMaaned
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPaaGjenlevenderettIAlderspensjonDtoSelectors.PesysDataSelectors.bruker
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPaaGjenlevenderettIAlderspensjonDtoSelectors.PesysDataSelectors.dineRettigheterOgMulighetTilAaKlage
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPaaGjenlevenderettIAlderspensjonDtoSelectors.PesysDataSelectors.krav
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPaaGjenlevenderettIAlderspensjonDtoSelectors.PesysDataSelectors.maanedligPensjonFoerSkatt
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPaaGjenlevenderettIAlderspensjonDtoSelectors.PesysDataSelectors.maanedligPensjonFoerSkattAP2025
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPaaGjenlevenderettIAlderspensjonDtoSelectors.PesysDataSelectors.ytelseskomponentInformasjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPaaGjenlevenderettIAlderspensjonDtoSelectors.SaksbehandlerValgSelectors.avdoedNavn
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPaaGjenlevenderettIAlderspensjonDtoSelectors.SaksbehandlerValgSelectors.samboerUtenFellesBarn
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPaaGjenlevenderettIAlderspensjonDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagPaaGjenlevenderettIAlderspensjonDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.maler.fraser.alderspensjon.DerforHar
import no.nav.pensjon.brev.maler.fraser.alderspensjon.DuFaarHverMaaned
import no.nav.pensjon.brev.maler.fraser.alderspensjon.FlereBeregningsperioder
import no.nav.pensjon.brev.maler.fraser.alderspensjon.TrygdetidTittel
import no.nav.pensjon.brev.maler.fraser.alderspensjon.Utbetalingsinformasjon
import no.nav.pensjon.brev.maler.fraser.alderspensjon.VedtakAlderspensjon
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlage
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligPensjonFoerSkattAp2025
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.PlainTextOnlyPhrase
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.PlainTextOnlyScope
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

// Mal 152 i metaforce
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
            displayTitle = "Vedtak - avslag på gjenlevenderett i alderspensjon",
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
                    val dato = fritekst("dato")
                    textExpr(
                        Bokmal to "Vi har fått beskjed om at ".expr() + saksbehandlerValg.avdoedNavn + " døde " + dato + ".",
                        Nynorsk to "Vi har fått beskjed om at ".expr() + saksbehandlerValg.avdoedNavn + " døydde " + dato + ".",
                        English to "We have received notice that ".expr() + saksbehandlerValg.avdoedNavn + " died " + dato + "."
                    )
                }
            }

            // avslagGjRettAPUnder1aarTTSøknad_001
            paragraph {
                text(
                    Bokmal to "For at du skal ha rett til alderspensjon med gjenlevenderett må avdøde ha bodd eller arbeidet i Norge i minst ett år.",
                    Nynorsk to "For at du skal ha rett til alderspensjon med attlevanderett, må avdøde ha budd eller arbeidd i Noreg i minst eitt år.",
                    English to "To be entitled to a retirement pension with survivor’s rights, the deceased must have lived or worked in Norway for at least one year."
                )
            }
            paragraph {
                textExpr(
                    Bokmal to "Ifølge opplysningene våre har ".expr() + saksbehandlerValg.avdoedNavn + " bodd eller arbeidet i Norge i " + fritekst(
                        "angi antall dager/måneder"
                    ) + ".",
                    Nynorsk to "Ifølgje opplysningane våre har ".expr() + saksbehandlerValg.avdoedNavn + " budd eller arbeidd i Noreg i " + fritekst(
                        "angi antall dagar/månader"
                    ) + ".",
                    English to "According to our information, ".expr() + saksbehandlerValg.avdoedNavn + " has lived or worked in Norway for " + fritekst(
                        "angi antall dager/måneder"
                    ) + "."
                )
                textExpr(
                    Bokmal to " I følge opplysningene våre har ".expr() + saksbehandlerValg.avdoedNavn + " aldri bodd eller arbeidet i Norge. ",
                    Nynorsk to " Ifølgje opplysningane våre har ".expr() + saksbehandlerValg.avdoedNavn + " aldri budd eller arbeidd i Noreg. ",
                    English to " According to our information, ".expr() + saksbehandlerValg.avdoedNavn + " has never lived or worked in Norway. "
                )

                includePhrase(DerforHar(initiertAvBrukerEllerVerge = initiertAvBrukerEllerVerge, initiertAvNav = initiertAvNav))
            }

            // avslagGjRettAPUnder3aar_001
            showIf(pesysData.avdoed.redusertTrygdetidNorge) {
                paragraph {
                    textExpr(
                        Bokmal to "For at du skal ha rett til alderspensjon med gjenlevenderett må avdøde ha vært medlem i folketrygden eller ha mottatt pensjon eller uføretrygd de siste ".expr() + fritekst(
                            "tre / fem"
                        ) + " årene fram til dødsfallet.",
                        Nynorsk to "For at du skal ha rett til alderspensjon med attlevanderett, må avdøde ha vore medlem i folketrygda eller ha mottatt pensjon eller uføretrygd dei siste ".expr() + fritekst(
                            "tre / fem"
                        ) + " åra fram til dødsfallet.",
                        English to "To be entitled to survivor’s rights in your retirement pension, the deceased must have been a member of the National Insurance Scheme or have received a pension or disability benefit for the last ".expr() + fritekst(
                            "three / five"
                        ) + " years prior to his or her death."
                    )
                }
            }

            // avslagGjRettAPUnder3aarEOS_001
            showIf(pesysData.avdoed.redusertTrygdetidEOS) {
                Under3Eller5Aar(EOSLand)
            }

            // avslagAPGjRettUnder3aarAvtale_001
            showIf(pesysData.avdoed.redusertTrygdetidAvtaleland) {
                Under3Eller5Aar(Avtaleland)
            }

            // avslagGjRettAPUnder20aar_001
            paragraph {
                text(
                    Bokmal to "For at du skal ha rett til å få utbetalt alderspensjon med gjenlevenderett når du bor i ",
                    Nynorsk to "For at du skal ha rett til å få utbetalt alderspensjon med attlevanderett når du bur i ",
                    English to "To be eligible for your retirement pension with survivor`s rights when you live in "
                )
                eval(pesysData.bruker.faktiskBostedsland_safe.ifNull(fritekst("BOSTEDSLAND")))
                text(
                    Bokmal to ", må avdøde ha hatt 20 års botid i Norge eller rett til tilleggspensjon.",
                    Nynorsk to ", må avdøde ha hatt 20 års butid i Noreg eller ha rett til tilleggspensjon.",
                    English to ", the deceased must have 20 years of residence in Norway or be entitled to a supplementary pension."
                )
            }
            paragraph {
                showIf(initiertAvBrukerEllerVerge) {
                    textExpr(
                        Bokmal to "Dette har ikke ".expr() + saksbehandlerValg.avdoedNavn + ". Derfor har vi avslått søknaden din.",
                        Nynorsk to "Dette har ikkje ".expr() + saksbehandlerValg.avdoedNavn + ". Derfor har vi avslått søknaden din.",
                        English to "Neither of these applies to ".expr() + saksbehandlerValg.avdoedNavn + ". We have declined your application for this reason."
                    )
                }.orShowIf(initiertAvNav) {
                    textExpr(
                        Bokmal to "Dette har ikke ".expr() + saksbehandlerValg.avdoedNavn + ". Derfor har du ikke rett til alderspensjon med gjenlevenderett.",
                        Nynorsk to "Dette har ikkje ".expr() + saksbehandlerValg.avdoedNavn + ". Derfor har du ikkje rett til alderspensjon med attlevanderett.",
                        English to "Neither of these applies to ".expr() + saksbehandlerValg.avdoedNavn + ". You are not entitled to survivor’s rights in your retirement pension for this reason."
                    )
                }
            }

            // avslagGJRettAPGiftUnder5aarSøknad_001 / avslagGjRettAPVilkårGift5aarNav_001
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
                    ) + " og ektefellen din døde " + fritekst("dato") + ". Dere har heller ikke felles barn. ",
                    Nynorsk to "Du og ".expr() + saksbehandlerValg.avdoedNavn + " har ikkje vore gifte i minst fem år. Ekteskapet blei inngått " + fritekst(
                        "dato"
                    ) + ", og ektefellen din døydde " + fritekst("dato") + ". De har heller ikkje felles barn. ",
                    English to "You and ".expr() + saksbehandlerValg.avdoedNavn + " have not been married for at least five years. Your marriage took place on " + fritekst(
                        "dato"
                    ) + " and your spouse died on " + fritekst("dato") + ". You also have no joint children. "
                )
                includePhrase(DerforHar(initiertAvBrukerEllerVerge = initiertAvBrukerEllerVerge, initiertAvNav = initiertAvNav))
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
                        Bokmal to "Du og ".expr() + saksbehandlerValg.avdoedNavn + " har ikke tidligere vært gift i minst fem år. Dere var heller ikke samboere med felles barn. ",
                        Nynorsk to "Du og ".expr() + saksbehandlerValg.avdoedNavn + " har ikkje tidlegare vore gifte i minst fem år. De var heller ikkje sambuarar med felles barn. ",
                        English to "You and ".expr() + saksbehandlerValg.avdoedNavn + " have not been previously married for at least five years. You were also not cohabiting partners with joint children. "
                    )
                    includePhrase(DerforHar(initiertAvBrukerEllerVerge = initiertAvBrukerEllerVerge, initiertAvNav = initiertAvNav))
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

            showIf(
                pesysData.ytelseskomponentInformasjon.beloepEndring.equalTo(UENDRET) and pesysData.alderspensjonVedVirk.totalPensjon.greaterThan(
                    0
                )
            ) {
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

            showIf(
                pesysData.beregnetPensjonPerMaaned.antallBeregningsperioderPensjon.greaterThan(0)
                        and pesysData.alderspensjonVedVirk.uttaksgrad.greaterThan(0)
            ) {
                includePhrase(Utbetalingsinformasjon)
            }

            includePhrase(
                FlereBeregningsperioder(
                    pesysData.beregnetPensjonPerMaaned.antallBeregningsperioderPensjon,
                    pesysData.alderspensjonVedVirk.totalPensjon
                )
            )

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
                            Nynorsk to "Vedtaket er også gjort etter artikkel ".expr() + fritekst("Legg inn aktuelle artikler om sammenlegging og eksport") + " i trygdeavtalen med ",
                            English to "This decision was also made pursuant to the provisions of Article ".expr() + fritekst(
                                "Legg inn aktuelle artikler om sammenlegging og eksport"
                            ) + " in the social security agreement with "
                        )
                        ifNotNull(land.navn) { eval(it + ".") }
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
                            Bokmal to "Våre opplysninger viser at ".expr() + saksbehandlerValg.avdoedNavn + " " + fritekst(
                                "har bodd eller arbeidet i Norge i angi antall dager/ måneder / ikke har bodd eller arbeidet i Norge"
                            ) + ".",
                            Nynorsk to "Ifølgje våre opplysningar har ".expr() + saksbehandlerValg.avdoedNavn + " " + fritekst(
                                " budd eller arbeidd i Noreg i angi antall dagar/ månader / ikkje budd eller arbeidd i Noreg"
                            ) + ".",
                            English to fritekst("We have registered that / We have no record of") + " " + saksbehandlerValg.avdoedNavn + " " + fritekst(
                                " has been living or working in Norway for angi antall days/ months /  living or working in Norway."
                            ) + ".",
                        )
                    }
                }
                showIf(pesysData.avdoed.redusertTrygdetidEOS) {
                    // avslagUnder3aarTTAvdodEOS_001
                    paragraph {
                        textExpr(
                            Bokmal to "Vi har fått opplyst at ".expr() + saksbehandlerValg.avdoedNavn + " har " + fritekst(
                                "angi antall"
                            ) + " måneder opptjeningstid i annet EØS-land. Den samlede trygdetiden i Norge og annet EØS-land er " + fritekst(
                                "angi samlet trygdetid i Norge og EØS-land"
                            ) + ".",
                            Nynorsk to "Vi har fått opplyst at ".expr() + saksbehandlerValg.avdoedNavn + " har " + fritekst(
                                "angi antall"
                            ) + " månader oppteningstid i anna EØS-land. Den samla trygdetiden i Norge og anna EØS-land er " + fritekst(
                                "angi samlet trygdetid i Norge og EØS-land"
                            ) + ".",
                            English to "We have been informed that ".expr() + saksbehandlerValg.avdoedNavn + " has " + fritekst(
                                "angi antall"
                            ) + " months of national insurance coverage in an other EEA country. The total national insurance coverage in Norway and an other EEA country is " + fritekst(
                                "angi samlet trygdetid i Norge og EØS-land"
                            ) + "."
                        )
                    }
                }

                showIf(pesysData.avdoed.redusertTrygdetidAvtaleland) {
                    // avslagUnder3aarTTAvdodAvtale_001
                    paragraph {
                        textExpr(
                            Bokmal to "Vi har fått opplyst at ".expr() + saksbehandlerValg.avdoedNavn + " har " + fritekst(
                                "angi antall"
                            ) + " måneder opptjeningstid i annet avtaleland. Den samlede trygdetiden i Norge og annet avtaleland er " + fritekst(
                                "angi samlet trygdetid i Norge og avtaleland"
                            ) + ".",
                            Nynorsk to "Vi har fått opplyst at ".expr() + saksbehandlerValg.avdoedNavn + " har " + fritekst(
                                "angi antall"
                            ) + " månader oppteningstid i anna avtaleland. Den samla trygdetiden i Norge og anna avtaleland er " + fritekst(
                                "angi samlet trygdetid i Norge og avtaleland"
                            ) + ".",
                            English to "We have been informed that ".expr() + saksbehandlerValg.avdoedNavn + " has " + fritekst(
                                "angi antall"
                            ) + " months of national insurance coverage in an other signatory country. The total national insurance coverage in Norway and an other signatory country is " + fritekst(
                                "angi samlet trygdetid i Norge og avtaleland"
                            ) + "."
                        )
                    }
                }

            }

            showIf(pesysData.ytelseskomponentInformasjon.beloepEndring.isOneOf(ENDR_OKT, ENDR_RED)) {
                includePhrase(VedtakAlderspensjon.EndringKanHaBetydningForSkatt)
            }

            includePhrase(Felles.RettTilAAKlage(vedleggDineRettigheterOgMulighetTilAaKlage))
            includePhrase(Felles.RettTilInnsyn(vedleggDineRettigheterOgMulighetTilAaKlage))
            includePhrase(Felles.HarDuSpoersmaal.alder)
        }
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlage, pesysData.dineRettigheterOgMulighetTilAaKlage)
        includeAttachmentIfNotNull(vedleggMaanedligPensjonFoerSkatt, pesysData.maanedligPensjonFoerSkatt)
        includeAttachmentIfNotNull(vedleggMaanedligPensjonFoerSkattAp2025, pesysData.maanedligPensjonFoerSkattAP2025)
    }

    private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, AvslagPaaGjenlevenderettIAlderspensjonDto>.Under3Eller5Aar(
        land: PlainTextOnlyPhrase<LangBokmalNynorskEnglish>,
    ) {
        paragraph {
            text(
                Bokmal to "For at du skal ha rett til alderspensjon med gjenlevenderett må avdøde",
                Nynorsk to "For at du skal ha rett til alderspensjon med attlevanderett må avdøde",
                English to "To be entitled to survivor’s rights in your retirement pension, the deceased must have"
            )
            list {
                item {
                    textExpr(
                        Bokmal to "ha vært medlem i folketrygden de siste ".expr() + fritekst("tre / fem") + " årene fram til dødsfallet eller",
                        Nynorsk to "ha vore medlem i folketrygda dei siste ".expr() + fritekst("tre / fem") + " åra fram til dødsfallet eller",
                        English to "been a member of the National Insurance Scheme in the last ".expr() + fritekst(
                            "three / five"
                        ) + " years prior to his or her death or"
                    )
                }
                item {
                    textExpr(
                        Bokmal to "ha mottatt pensjon eller uføretrygd de siste ".expr() + fritekst("tre / fem") + " årene fram til dødsfallet eller",
                        Nynorsk to "ha mottatt pensjon eller uføretrygd dei siste ".expr() + fritekst("tre / fem") + " åra fram til dødsfallet eller",
                        English to "received a pension or disability benefit in the last ".expr() + fritekst("three / five") + " years prior to his or her death or"
                    )
                }
                item {
                    text(
                        Bokmal to "ha hatt pensjonsopptjening både i Norge og andre ",
                        Nynorsk to "ha hatt pensjonsopptening både i Noreg og andre ",
                        English to "had accrued pension rights both in Norway and other "
                    )
                    includePhrase(land)
                    textExpr(
                        Bokmal to " i de siste ".expr() + fritekst("tre / fem") + " årene fram til dødsfallet.",
                        Nynorsk to " i dei siste ".expr() + fritekst("tre / fem") + " åra fram til dødsfallet.",
                        English to " in the last ".expr() + fritekst("three / five") + " years prior to his or her death."
                    )
                }
            }
        }
    }

    private object EOSLand : PlainTextOnlyPhrase<LangBokmalNynorskEnglish>() {
        override fun PlainTextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            text(
                Bokmal to "EØS-land",
                Nynorsk to "EØS-land",
                English to "EEA member states"
            )
        }
    }

    private object Avtaleland : PlainTextOnlyPhrase<LangBokmalNynorskEnglish>() {
        override fun PlainTextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            text(
                Bokmal to "avtaleland",
                Nynorsk to "avtaleland",
                English to "contracting states"
            )
        }
    }
}