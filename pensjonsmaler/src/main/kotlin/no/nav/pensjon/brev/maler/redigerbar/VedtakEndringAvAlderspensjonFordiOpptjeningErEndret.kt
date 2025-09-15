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
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDtoSelectors.PesysDataSelectors.etterbetaling
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
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.expression.lessThan
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.notNull
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.namedReference
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import java.time.LocalDate
import java.time.Month

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
                    text(
                        bokmal { + "Pensjonsopptjeningen for " + fritekst + " er endret." },
                        nynorsk { + "Pensjonsoppteninga for " + fritekst + " er endra." },
                        english { + "Your pension earnings for the income year(s) " + fritekst + " has(have) been changed." }
                    )
                }

                showIf(pesysData.ytelseskomponentInformasjon.belopEndring.equalTo(BeloepEndring.UENDRET)) {
                    // ingenEndringPensjon_001
                    paragraph {
                        text(
                            bokmal { + "Dette får ingen betydning for pensjonen din." },
                            nynorsk { + "Dette får ingen følgjer for pensjonen din." },
                            english { + "This does not affect your pension." }
                        )
                    }
                }.orShowIf(pesysData.ytelseskomponentInformasjon.belopEndring.equalTo(BeloepEndring.ENDR_OKT)) {
                    //  nyBeregningAPØkning_001
                    paragraph {
                        text(
                            bokmal { + "Dette fører til at pensjonen din øker." },
                            nynorsk { + "Dette fører til at pensjonen din aukar." },
                            english { + "This leads to an increase in your retirement pension." }
                        )
                    }
                }.orShowIf(pesysData.ytelseskomponentInformasjon.belopEndring.equalTo(BeloepEndring.ENDR_RED)) {
                    // nyBeregningAPReduksjon_001
                    paragraph {
                        text(
                            bokmal { + "Dette fører til at pensjonen din blir redusert." },
                            nynorsk { + "Dette fører til at pensjonen din blir redusert." },
                            english { + "This leads to a reduction in your retirement pension." }
                        )
                    }
                }

                showIf(not(pesysData.alderspensjonVedVirk.uforeKombinertMedAlder)) {
                    // innvilgelseAPInnledn_001
                    paragraph {
                        text(
                            bokmal { + "Du får " + pesysData.alderspensjonVedVirk.totalPensjon.format() + " hver måned før skatt fra " + pesysData.krav.virkDatoFom.format() + " i alderspensjon fra folketrygden." },
                            nynorsk { + "Du får " + pesysData.alderspensjonVedVirk.totalPensjon.format() + " kvar månad før skatt frå " + pesysData.krav.virkDatoFom.format() + " i alderspensjon frå folketrygden." },
                            english { + "You will receive " + pesysData.alderspensjonVedVirk.totalPensjon.format() + " every month before tax from " + pesysData.krav.virkDatoFom.format() + " as retirement pension from the National Insurance Scheme." },
                        )
                    }
                }.orShow {
                    includePhrase(UfoereAlder.DuFaar(pesysData.alderspensjonVedVirk.totalPensjon, pesysData.krav.virkDatoFom))
                }

                // utbetalingsInfoMndUtbet_001
                paragraph {
                    text(
                        bokmal { + "Hvis du har andre pensjonsytelser som for eksempel AFP eller tjenestepensjon, blir de utbetalt i tillegg til alderspensjonen. Alderspensjonen din utbetales innen den 20. hver måned. Du finner oversikt over utbetalingene dine på $UTBETALINGER_URL." },
                        nynorsk { + "Dersom du har andre pensjonsytingar som for eksempel AFP eller tenestepensjon, kjem slik utbetaling i tillegg til alderspensjonen. Alderspensjonen din blir betalt ut innan den 20. i kvar månad. Du finn meir informasjon om utbetalingane dine på $UTBETALINGER_URL." },
                        english { + "If you have occupational pensions from other schemes, this will be paid in addition to your retirement pension. Your pension will be paid at the latest on the 20th of each month. See the more detailed information on what you will receive at $UTBETALINGER_URL." }
                    )
                }
            }

            // endretOpptjeningBegrunn_004
            includePhrase(Vedtak.BegrunnelseOverskrift)
            paragraph {
                text(
                    bokmal { + "Pensjonsopptjeningen din er endret fordi:" },
                    nynorsk { + "Pensjonsoppteninga di er endra fordi:" },
                    english { + "Your pension earnings have been changed because:" }
                )
                list {
                    item {
                        text(
                            bokmal { + "Skatteetaten har endret skatteoppgjøret ditt." },
                            nynorsk { + "Skatteetaten har endra skatteoppgjeret ditt." },
                            english { + "The Norwegian Tax Administration has amended one or several tax returns." }
                        )
                    }
                    item {
                        text(
                            bokmal { + "Skatteetaten har endret den pensjonsgivende inntekten din." },
                            nynorsk { + "Skatteetaten har endra den pensjonsgivande inntekta di." },
                            english { + "The Norwegian Tax Administration has amended your pensionable income." }
                        )
                    }
                    item {
                        val fritekst = fritekst("dette året / disse årene")
                        text(
                            bokmal { + "Du har fått medregnet inntekten din for " + fritekst + "." },
                            nynorsk { + "Du har fått rekna med inntekta di for " + fritekst + "." },
                            english { + "Your pensionable income for has been added to your pension reserves for " + fritekst + "." },
                        )
                    }
                    item {
                        text(
                            bokmal { + "Du har fått omsorgsopptjening fordi du har hatt omsorg for små barn eller pleietrengende personer." },
                            nynorsk { + "Du har fått omsorgsopptening fordi du har hatt omsorg for små barn eller pleietrengande personer." },
                            english { + "You have been granted pension rights for unpaid care work. (Care for sick, disabled or elderly people, or care for children under the age of six years.)" }
                        )
                    }
                    item {
                        val fritekst = fritekst("dette året / disse årene")
                        text(
                            bokmal { + "Du har fått lagt til trygdetid for " + fritekst + "." },
                            nynorsk { + "Du har fått lagt til trygdetid for " + fritekst + "." },
                            english { + "You have been granted national insurance coverage for " + fritekst + "." },
                        )
                    }
                    item {
                        text(
                            bokmal { + "Dagpengene eller uføretrygden din er endret." },
                            nynorsk { + "Dagpengane eller uføretrygda di er endra." },
                            english { + "Your unemployment benefit or disability benefit has been changed." }
                        )
                    }
                }

                showIf(pesysData.opplysningerBruktIBeregningenAlder.notNull() or pesysData.opplysningerBruktIBeregningenAlderAP2025.notNull()) {
                    text(
                        bokmal { + "Du kan finne mer informasjon i vedlegget " },
                        nynorsk { + "Du kan finne meir informasjon i vedlegget " },
                        english { + "You will find more information in the appendix " }
                    )
                    showIf(pesysData.alderspensjonVedVirk.regelverkType.isOneOf(AP2011, AP2016)) {
                        namedReference(vedleggOpplysningerBruktIBeregningenAlder)
                    }.orShowIf(pesysData.alderspensjonVedVirk.regelverkType.equalTo(AP2025) and pesysData.opplysningerBruktIBeregningenAlderAP2025.notNull()) {
                        namedReference(vedleggOpplysningerBruktIBeregningenAlderAP2025)
                    }
                    text(bokmal { + "." }, nynorsk { + "." }, english { + "." })
                }
            }

            showIf(pesysData.alderspensjonVedVirk.regelverkType.equalTo(AP2011)) {
                // hjemmelAP2011Opptjening_001
                paragraph {
                    text(
                        bokmal { + "Vedtaket er gjort etter folketrygdloven § 19-13." },
                        nynorsk { + "Vedtaket er gjort etter folketrygdlova § 19-13." },
                        english { + "This decision was made pursuant to the provisions of § 19-13 of the National Insurance Act." }
                    )
                }
            }.orShowIf(pesysData.alderspensjonVedVirk.regelverkType.equalTo(AP2016)) {
                showIf(pesysData.krav.arsakErEndretOpptjening) {
                    // hjemmelAP2016Opptjening_001
                    paragraph {
                        text(
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 19-13, 19-15, 20-17 og 20-19." },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 19-13, 19-15, 20-17 og 20-19." },
                            english { + "This decision was made pursuant to the provisions of §§ 19-13, 19-15, 20-17 and 20-19 of the National Insurance Act." }
                        )
                    }
                }
            }.orShowIf(pesysData.alderspensjonVedVirk.regelverkType.equalTo(AP2025)) {
                showIf(not(pesysData.behandlingKontekst.konteksttypeErKorrigeringopptjening)) {
                    showIf(pesysData.krav.erForstegangsbehandling) {
                        // AP2025TidligUttakHjemmel_001
                        paragraph {
                            text(
                                bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 20-2, 20-3, 20-9 til 20-15, 22-12 og 22-13." },
                                nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 20-2, 20-3, 20-9 til 20-15, 22-12 og 22-13." },
                                english { + "This decision was made pursuant to the provisions of §§ 20-2, 20-3, 20-9 to 20-15, 22-12 and 22-13 of the National Insurance Act." }
                            )
                        }
                    }.orShow {
                        // hjemmelAP2025Opptjening_001
                        paragraph {
                            text(
                                bokmal { + "Vedtaket er gjort etter folketrygdloven § 20-17." },
                                nynorsk { + "Vedtaket er gjort etter folketrygdlova § 20-17." },
                                english { + "This decision was made pursuant to the provision of § 20-7 of the National Insurance Act." }
                            )
                        }
                    }
                }
            }

            //  skattAPendring_001
            showIf(pesysData.ytelseskomponentInformasjon.belopEndring.isOneOf(BeloepEndring.ENDR_OKT, BeloepEndring.ENDR_RED)) {
                includePhrase(VedtakAlderspensjon.EndringKanHaBetydningForSkatt)
            }

            showIf(pesysData.etterbetaling) {
                // etterbetalingAP_002
                title1 {
                    text(
                        bokmal { +"Etterbetaling" },
                        nynorsk { +"Etterbetaling" },
                        english { +"Retroactive payment" }
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Du får etterbetalt pensjon fra " + pesysData.krav.virkDatoFom.format() + ". Etterbetalingen vil vanligvis bli utbetalt i løpet av syv virkedager. Vi kan trekke fra skatt og ytelser du har fått fra for eksempel Nav eller tjenestepensjonsordninger. Derfor kan etterbetalingen din bli forsinket. Tjenestepensjonsordninger har ni ukers frist på å kreve trekk i etterbetalingen. Du kan sjekke eventuelle fradrag i utbetalingsmeldingen på $DITT_NAV" },
                        nynorsk { +"Du får etterbetalt pensjon frå " + pesysData.krav.virkDatoFom.format() + ". Etterbetalinga blir vanlegvis betalt ut i løpet av sju yrkedagar. Vi kan trekke frå skatt og ytingar du har fått frå for eksempel Nav eller tenestepensjonsordningar. Derfor kan etterbetalinga di bli forsinka. Tenestepensjonsordninga har ni veker frist på å krevje trekk i etterbetalinga. Du kan sjekke eventuelle frådrag i utbetalingsmeldinga på $DITT_NAV." },
                        english { +"You will receive retroactive pension payments from " + pesysData.krav.virkDatoFom.format() + ". The retroactive payments will normally be made in the course of seven working days. We can make deductions for tax and benefits you have received, for example, from Nav or occupational pension schemes. Therefore, your retroactive payment may be delayed. Occupational pension schemes have a deadline of nine weeks to demand a deduction from the retroactive payments. You can check if there are any deductions from the payment notice at $DITT_NAV." }
                    )
                }
                showIf(pesysData.krav.virkDatoFom.lessThan(LocalDate.of(LocalDate.now().year, Month.JANUARY, 1))) {
                    paragraph {
                        text(
                            bokmal { +"Hvis etterbetalingen gjelder tidligere år, trekker vi skatt etter skatteetatens standardsatser." },
                            nynorsk { +"Dersom etterbetalinga gjeld tidlegare år, vil vi trekkje skatt etter standardsatsane til skatteetaten." },
                            english { +"If the retroactive payment refers to earlier years, we will deduct tax at the Tax Administration's standard rates." }
                        )
                    }
                }
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
                //  nyOpptjeningGradertAP_001
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

            // TODO: Det kjens som dette burde vera standardtekst i felles
            //  meldEndringerPesys_001
            title1 {
                text(
                    bokmal { + "Du må melde fra om endringer" },
                    nynorsk { + "Du må melde frå om endringar" },
                    english { + "You must notify Nav if anything changes" }
                )
            }
            paragraph {
                text(
                    bokmal { + "Hvis du får endringer i familiesituasjon, planlegger opphold i utlandet, eller ektefellen eller samboeren din får endringer i inntekten, kan det ha betydning for beløpet du får utbetalt fra Nav. I slike tilfeller må du derfor straks melde fra til oss. I vedlegget ser du hvilke endringer du må si fra om." },
                    nynorsk { + "Dersom du får endringar i familiesituasjonen, planlegg opphald i utlandet, eller ektefellen, partnaren eller sambuaren din får endringar i inntekta, kan det få noko å seie for beløpet du får utbetalt frå Nav. I slike tilfelle må du derfor straks melde frå til oss. I vedlegget ser du kva endringar du må seie frå om." },
                    english { + "If there are changes in your family situation or you are planning a long-term stay abroad, or there are changes in the income of your spouse or co-habiting partner, these might affect the payments you receive from Nav. In such cases, you must notify Nav immediately. The appendix specifies which changes you are obligated to notify us of." }
                )
            }
            paragraph {
                text(
                    bokmal { + "Hvis du har fått utbetalt for mye fordi du ikke har gitt oss beskjed, må du vanligvis betale tilbake pengene. Du er selv ansvarlig for å holde deg orientert om bevegelser på kontoen din, og du må melde fra om eventuelle feil til Nav." },
                    nynorsk { + "Dersom du har fått utbetalt for mykje fordi du ikkje har gitt oss beskjed, må du vanlegvis betale tilbake pengane. Du er sjølv ansvarleg for å halde deg orientert om rørsler på kontoen din, og du må melde frå om eventuelle feil til Nav." },
                    english { + "If your payments have been too high as a result of you failing to notify us of a change, the incorrect payment must normally be repaid. It is your responsibility to keep yourself informed of movements in your account, and you are obligated to report any and all errors to Nav." }
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