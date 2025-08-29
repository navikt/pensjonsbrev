package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.AP2011
import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.AP2016
import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.AP2025
import no.nav.pensjon.brev.api.model.BeloepEndring
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDtoSelectors.AlderspensjonVedVirkSelectors.fullUttaksgrad
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDtoSelectors.AlderspensjonVedVirkSelectors.regelverkType
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDtoSelectors.AlderspensjonVedVirkSelectors.totalPensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDtoSelectors.AlderspensjonVedVirkSelectors.uforeKombinertMedAlder
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDtoSelectors.BehandlingKontekstSelectors.konteksttypeErKorrigeringopptjening
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDtoSelectors.KravSelectors.arsakErEndretOpptjening
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDtoSelectors.KravSelectors.erForstegangsbehandling
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDtoSelectors.KravSelectors.virkDatoFom
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDtoSelectors.PesysDataSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDtoSelectors.PesysDataSelectors.behandlingKontekst
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDtoSelectors.PesysDataSelectors.krav
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDtoSelectors.PesysDataSelectors.maanedligPensjonFoerSkatt
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDtoSelectors.PesysDataSelectors.maanedligPensjonFoerSkattAP2025
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDtoSelectors.PesysDataSelectors.opplysningerBruktIBeregningenAlder
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDtoSelectors.PesysDataSelectors.opplysningerBruktIBeregningenAlderAP2025
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDtoSelectors.PesysDataSelectors.orienteringOmRettigheterOgPlikter
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDtoSelectors.PesysDataSelectors.ytelseskomponentInformasjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDtoSelectors.YtelseskomponentInformasjonSelectors.belopEndring
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDtoSelectors.pesysData
import no.nav.pensjon.brev.maler.fraser.alderspensjon.ArbeidsinntektOgAlderspensjonKort
import no.nav.pensjon.brev.maler.fraser.alderspensjon.BeregnaPaaNytt
import no.nav.pensjon.brev.maler.fraser.alderspensjon.InformasjonOmAlderspensjon
import no.nav.pensjon.brev.maler.fraser.alderspensjon.UfoereAlder
import no.nav.pensjon.brev.maler.fraser.alderspensjon.VedtakAlderspensjon
import no.nav.pensjon.brev.maler.fraser.common.Constants.DITT_NAV
import no.nav.pensjon.brev.maler.fraser.common.Constants.UTBETALINGER_URL
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligPensjonFoerSkattAp2025
import no.nav.pensjon.brev.maler.vedlegg.vedleggOpplysningerBruktIBeregningenAlder
import no.nav.pensjon.brev.maler.vedlegg.vedleggOpplysningerBruktIBeregningenAlderAP2025
import no.nav.pensjon.brev.maler.vedlegg.vedleggOrienteringOmRettigheterOgPlikter
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
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.notNull
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brev.template.namedReference
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

// 119 i doksys
@TemplateModelHelpers
object VedtakEndringAvAlderspensjonFordiOpptjeningErEndret : RedigerbarTemplate<VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDto> {
    override val kategori = TemplateDescription.Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper: Set<Sakstype> = setOf(Sakstype.ALDER)
    override val kode = Pesysbrevkoder.Redigerbar.PE_AP_ENDRING_PGA_OPPTJENING
    override val template = createTemplate(
        name = kode.name,
        letterDataType = VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - endring av alderspensjon fordi opptjening er endret",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            includePhrase(BeregnaPaaNytt(pesysData.krav.virkDatoFom))
        }
        outline {
            includePhrase(Vedtak.Overskrift)

            showIf(pesysData.krav.arsakErEndretOpptjening) {
                paragraph {
                    val fritekst = fritekst("år")
                    textExpr(
                        Bokmal to "Pensjonsopptjeningen for ".expr() + fritekst + " er endret.",
                        Nynorsk to "Pensjonsoppteninga for ".expr() + fritekst + " er endra.",
                        English to "Your pension earnings for the income year(s) ".expr() + fritekst + " has(have) been changed."
                    )
                }

                showIf(pesysData.ytelseskomponentInformasjon.belopEndring.equalTo(BeloepEndring.UENDRET)) {
                    // ingenEndringPensjon_001
                    paragraph {
                        text(
                            Bokmal to "Dette får ingen betydning for pensjonen din.",
                            Nynorsk to "Dette får ingen følgjer for pensjonen din.",
                            English to "This does not affect your pension."
                        )
                    }
                }.orShowIf(pesysData.ytelseskomponentInformasjon.belopEndring.equalTo(BeloepEndring.ENDR_OKT)) {
                    //  nyBeregningAPØkning_001
                    paragraph {
                        text(
                            Bokmal to "Dette fører til at pensjonen din øker.",
                            Nynorsk to "Dette fører til at pensjonen din aukar.",
                            English to "This leads to an increase in your retirement pension."
                        )
                    }
                }.orShowIf(pesysData.ytelseskomponentInformasjon.belopEndring.equalTo(BeloepEndring.ENDR_RED)) {
                    // nyBeregningAPReduksjon_001
                    paragraph {
                        text(
                            Bokmal to "Dette fører til at pensjonen din blir redusert.",
                            Nynorsk to "Dette fører til at pensjonen din blir redusert.",
                            English to "This leads to a reduction in your retirement pension."
                        )
                    }
                }

                showIf(not(pesysData.alderspensjonVedVirk.uforeKombinertMedAlder)) {
                    // innvilgelseAPInnledn_001
                    paragraph {
                        textExpr(
                            Bokmal to "Du får ".expr() + pesysData.alderspensjonVedVirk.totalPensjon.format() + " hver måned før skatt fra " + pesysData.krav.virkDatoFom.format() + " i alderspensjon fra folketrygden.",
                            Nynorsk to "Du får ".expr() + pesysData.alderspensjonVedVirk.totalPensjon.format() + " kvar månad før skatt frå " + pesysData.krav.virkDatoFom.format() + " i alderspensjon frå folketrygden.",
                            English to "You will receive ".expr() + pesysData.alderspensjonVedVirk.totalPensjon.format() + " every month before tax from " + pesysData.krav.virkDatoFom.format() + " as retirement pension from the National Insurance Scheme.",
                        )
                    }
                }.orShow {
                    includePhrase(UfoereAlder.DuFaar(pesysData.alderspensjonVedVirk.totalPensjon, pesysData.krav.virkDatoFom))
                }

                // utbetalingsInfoMndUtbet_001
                paragraph {
                    text(
                        Bokmal to "Hvis du har andre pensjonsytelser som for eksempel AFP eller tjenestepensjon, blir de utbetalt i tillegg til alderspensjonen. Alderspensjonen din utbetales innen den 20. hver måned. Du finner oversikt over utbetalingene dine på $UTBETALINGER_URL.",
                        Nynorsk to "Dersom du har andre pensjonsytingar som for eksempel AFP eller tenestepensjon, kjem slik utbetaling i tillegg til alderspensjonen. Alderspensjonen din blir betalt ut innan den 20. i kvar månad. Du finn meir informasjon om utbetalingane dine på $UTBETALINGER_URL.",
                        English to "If you have occupational pensions from other schemes, this will be paid in addition to your retirement pension. Your pension will be paid at the latest on the 20th of each month. See the more detailed information on what you will receive at $UTBETALINGER_URL."
                    )
                }
            }

            // endretOpptjeningBegrunn_004
            includePhrase(Vedtak.BegrunnelseOverskrift)
            paragraph {
                text(
                    Bokmal to "Pensjonsopptjeningen din er endret fordi:",
                    Nynorsk to "Pensjonsoppteninga di er endra fordi:",
                    English to "Your pension earnings have been changed because:"
                )
                list {
                    item {
                        text(
                            Bokmal to "Skatteetaten har endret skatteoppgjøret ditt.",
                            Nynorsk to "Skatteetaten har endra skatteoppgjeret ditt.",
                            English to "The Norwegian Tax Administration has amended one or several tax returns."
                        )
                    }
                    item {
                        text(
                            Bokmal to "Skatteetaten har endret den pensjonsgivende inntekten din.",
                            Nynorsk to "Skatteetaten har endra den pensjonsgivande inntekta di.",
                            English to "The Norwegian Tax Administration has amended your pensionable income."
                        )
                    }
                    item {
                        val fritekst = fritekst("dette året / disse årene")
                        textExpr(
                            Bokmal to "Du har fått medregnet inntekten din for ".expr() + fritekst + ".",
                            Nynorsk to "Du har fått rekna med inntekta di for ".expr() + fritekst + ".",
                            English to "Your pensionable income for has been added to your pension reserves for ".expr() + fritekst + ".",
                        )
                    }
                    item {
                        text(
                            Bokmal to "Du har fått omsorgsopptjening fordi du har hatt omsorg for små barn eller pleietrengende personer.",
                            Nynorsk to "Du har fått omsorgsopptening fordi du har hatt omsorg for små barn eller pleietrengande personer.",
                            English to "You have been granted pension rights for unpaid care work. (Care for sick, disabled or elderly people, or care for children under the age of six years.)"
                        )
                    }
                    item {
                        val fritekst = fritekst("dette året / disse årene")
                        textExpr(
                            Bokmal to "Du har fått lagt til trygdetid for ".expr() + fritekst + ".",
                            Nynorsk to "Du har fått lagt til trygdetid for ".expr() + fritekst + ".",
                            English to "You have been granted national insurance coverage for ".expr() + fritekst + ".",
                        )
                    }
                    item {
                        text(
                            Bokmal to "Dagpengene eller uføretrygden din er endret.",
                            Nynorsk to "Dagpengane eller uføretrygda di er endra.",
                            English to "Your unemployment benefit or disability benefit has been changed."
                        )
                    }
                }

                showIf(pesysData.opplysningerBruktIBeregningenAlder.notNull() or pesysData.opplysningerBruktIBeregningenAlderAP2025.notNull()) {
                    text(
                        Bokmal to "Du kan finne mer informasjon i vedlegget ",
                        Nynorsk to "Du kan finne meir informasjon i vedlegget ",
                        English to "You will find more information in the appendix "
                    )
                    showIf(pesysData.alderspensjonVedVirk.regelverkType.isOneOf(AP2011, AP2016)) {
                        namedReference(vedleggOpplysningerBruktIBeregningenAlder)
                    }.orShowIf(pesysData.alderspensjonVedVirk.regelverkType.equalTo(AP2025) and pesysData.opplysningerBruktIBeregningenAlderAP2025.notNull()) {
                        namedReference(vedleggOpplysningerBruktIBeregningenAlderAP2025)
                    }
                    text(Bokmal to ".", Nynorsk to ".", English to ".")
                }
            }

            showIf(pesysData.alderspensjonVedVirk.regelverkType.equalTo(AP2011)) {
                // hjemmelAP2011Opptjening_001
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven § 19-13.",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova § 19-13.",
                        English to "This decision was made pursuant to the provisions of § 19-13 of the National Insurance Act."
                    )
                }
            }.orShowIf(pesysData.alderspensjonVedVirk.regelverkType.equalTo(AP2016)) {
                showIf(pesysData.krav.arsakErEndretOpptjening) {
                    // hjemmelAP2016Opptjening_001
                    paragraph {
                        text(
                            Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 19-13, 19-15, 20-17 og 20-19.",
                            Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 19-13, 19-15, 20-17 og 20-19.",
                            English to "This decision was made pursuant to the provisions of §§ 19-13, 19-15, 20-17 and 20-19 of the National Insurance Act."
                        )
                    }
                }
            }.orShowIf(pesysData.alderspensjonVedVirk.regelverkType.equalTo(AP2025)) {
                showIf(not(pesysData.behandlingKontekst.konteksttypeErKorrigeringopptjening)) {
                    showIf(pesysData.krav.erForstegangsbehandling) {
                        // AP2025TidligUttakHjemmel_001
                        paragraph {
                            text(
                                Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 20-2, 20-3, 20-9 til 20-15, 22-12 og 22-13.",
                                Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 20-2, 20-3, 20-9 til 20-15, 22-12 og 22-13.",
                                English to "This decision was made pursuant to the provisions of §§ 20-2, 20-3, 20-9 to 20-15, 22-12 and 22-13 of the National Insurance Act."
                            )
                        }
                    }.orShow {
                        // hjemmelAP2025Opptjening_001
                        paragraph {
                            text(
                                Bokmal to "Vedtaket er gjort etter folketrygdloven § 20-17.",
                                Nynorsk to "Vedtaket er gjort etter folketrygdlova § 20-17.",
                                English to "This decision was made pursuant to the provision of § 20-7 of the National Insurance Act."
                            )
                        }
                    }
                }
            }

            //  skattAPendring_001
            includePhrase(VedtakAlderspensjon.EndringKanHaBetydningForSkatt)

            // etterbetalingAP_002
            title1 {
                text(
                    Bokmal to "Etterbetaling",
                    Nynorsk to "Etterbetaling",
                    English to "Retroactive payment"
                )
            }
            paragraph {
                textExpr(
                    Bokmal to "Du får etterbetalt pensjon fra ".expr() + pesysData.krav.virkDatoFom.format() + ". Etterbetalingen vil vanligvis bli utbetalt i løpet av syv virkedager. Vi kan trekke fra skatt og ytelser du har fått fra for eksempel Nav eller tjenestepensjonsordninger. Derfor kan etterbetalingen din bli forsinket. Tjenestepensjonsordninger har ni ukers frist på å kreve trekk i etterbetalingen. Du kan sjekke eventuelle fradrag i utbetalingsmeldingen på $DITT_NAV",
                    Nynorsk to "Du får etterbetalt pensjon frå ".expr() + pesysData.krav.virkDatoFom.format() + ". Etterbetalinga blir vanlegvis betalt ut i løpet av sju yrkedagar. Vi kan trekke frå skatt og ytingar du har fått frå for eksempel Nav eller tenestepensjonsordningar. Derfor kan etterbetalinga di bli forsinka. Tenestepensjonsordninga har ni veker frist på å krevje trekk i etterbetalinga. Du kan sjekke eventuelle frådrag i utbetalingsmeldinga på $DITT_NAV.",
                    English to "You will receive retroactive pension payments from ".expr() + pesysData.krav.virkDatoFom.format() + ". The retroactive payments will normally be made in the course of seven working days. We can make deductions for tax and benefits you have received, for example, from Nav or occupational pension schemes. Therefore, your retroactive payment may be delayed. Occupational pension schemes have a deadline of nine weeks to demand a deduction from the retroactive payments. You can check if there are any deductions from the payment notice at $DITT_NAV."
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis etterbetalingen gjelder tidligere år, trekker vi skatt etter skatteetatens standardsatser.",
                    Nynorsk to "Dersom etterbetalinga gjeld tidlegare år, vil vi trekkje skatt etter standardsatsane til skatteetaten.",
                    English to "If the retroactive payment refers to earlier years, we will deduct tax at the Tax Administration's standard rates."
                )
            }

            includePhrase(ArbeidsinntektOgAlderspensjonKort)

            showIf(pesysData.alderspensjonVedVirk.fullUttaksgrad) {
                // nyOpptjeningHelAP_001
                paragraph {
                    text(
                        Bokmal to "Hvis du har 100 prosent alderspensjon, gjelder økningen fra 1. januar året etter at skatteoppgjøret ditt er ferdig.",
                        Nynorsk to "Dersom du har 100 prosent alderspensjon, gjeld auken frå 1. januar året etter at skatteoppgjeret ditt er ferdig.",
                        English to "If you are receiving a full (100 percent) retirement pension, the increase will come into effect from 1 January the year after your final tax settlement has been completed."
                    )
                }
            }.orShow {
                //  nyOpptjeningGradertAP_001
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

            // TODO: Det kjens som dette burde vera standardtekst i felles
            //  meldEndringerPesys_001
            title1 {
                text(
                    Bokmal to "Du må melde fra om endringer",
                    Nynorsk to "Du må melde frå om endringar",
                    English to "You must notify Nav if anything changes"
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du får endringer i familiesituasjon, planlegger opphold i utlandet, eller ektefellen eller samboeren din får endringer i inntekten, kan det ha betydning for beløpet du får utbetalt fra Nav. I slike tilfeller må du derfor straks melde fra til oss. I vedlegget ser du hvilke endringer du må si fra om.",
                    Nynorsk to "Dersom du får endringar i familiesituasjonen, planlegg opphald i utlandet, eller ektefellen, partnaren eller sambuaren din får endringar i inntekta, kan det få noko å seie for beløpet du får utbetalt frå Nav. I slike tilfelle må du derfor straks melde frå til oss. I vedlegget ser du kva endringar du må seie frå om.",
                    English to "If there are changes in your family situation or you are planning a long-term stay abroad, or there are changes in the income of your spouse or co-habiting partner, these might affect the payments you receive from Nav. In such cases, you must notify Nav immediately. The appendix specifies which changes you are obligated to notify us of."
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du har fått utbetalt for mye fordi du ikke har gitt oss beskjed, må du vanligvis betale tilbake pengene. Du er selv ansvarlig for å holde deg orientert om bevegelser på kontoen din, og du må melde fra om eventuelle feil til Nav.",
                    Nynorsk to "Dersom du har fått utbetalt for mykje fordi du ikkje har gitt oss beskjed, må du vanlegvis betale tilbake pengane. Du er sjølv ansvarleg for å halde deg orientert om rørsler på kontoen din, og du må melde frå om eventuelle feil til Nav.",
                    English to "If your payments have been too high as a result of you failing to notify us of a change, the incorrect payment must normally be repaid. It is your responsibility to keep yourself informed of movements in your account, and you are obligated to report any and all errors to Nav."
                )
            }

            includePhrase(Felles.RettTilAAKlage(vedleggOrienteringOmRettigheterOgPlikter))
            includePhrase(Felles.RettTilInnsyn(vedleggOrienteringOmRettigheterOgPlikter))
            includePhrase(Felles.HarDuSpoersmaal.alder)
        }

        includeAttachment(vedleggOrienteringOmRettigheterOgPlikter, pesysData.orienteringOmRettigheterOgPlikter)
        includeAttachmentIfNotNull(vedleggMaanedligPensjonFoerSkatt, pesysData.maanedligPensjonFoerSkatt)
        includeAttachmentIfNotNull(vedleggMaanedligPensjonFoerSkattAp2025, pesysData.maanedligPensjonFoerSkattAP2025)
        includeAttachmentIfNotNull(vedleggOpplysningerBruktIBeregningenAlder, pesysData.opplysningerBruktIBeregningenAlder)
        includeAttachmentIfNotNull(vedleggOpplysningerBruktIBeregningenAlderAP2025, pesysData.opplysningerBruktIBeregningenAlderAP2025)
    }
}