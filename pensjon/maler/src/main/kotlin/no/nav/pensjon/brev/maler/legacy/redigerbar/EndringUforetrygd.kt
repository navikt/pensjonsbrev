package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EndringUfoeretrygdDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EndringUfoeretrygdDtoSelectors.OpphoersbegrunnelseSelectors.barn_flyttet_ikke_avt_land
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EndringUfoeretrygdDtoSelectors.OpphoersbegrunnelseSelectors.barn_opph_ikke_avt_land
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EndringUfoeretrygdDtoSelectors.OpphoersbegrunnelseSelectors.bruker_flyttet_ikke_avt_land
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EndringUfoeretrygdDtoSelectors.OpphoersbegrunnelseSelectors.eps_flyttet_ikke_avt_land
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EndringUfoeretrygdDtoSelectors.OpphoersbegrunnelseSelectors.eps_opph_ikke_avt_land
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EndringUfoeretrygdDtoSelectors.PesysDataSelectors.annen_forld_rett_bt
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EndringUfoeretrygdDtoSelectors.PesysDataSelectors.antallBarnOpphor
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EndringUfoeretrygdDtoSelectors.PesysDataSelectors.bt_innt_over_1g
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EndringUfoeretrygdDtoSelectors.PesysDataSelectors.bt_over_18
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EndringUfoeretrygdDtoSelectors.PesysDataSelectors.hjemler
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EndringUfoeretrygdDtoSelectors.PesysDataSelectors.maanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EndringUfoeretrygdDtoSelectors.PesysDataSelectors.mindre_ett_ar_bt_flt
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EndringUfoeretrygdDtoSelectors.PesysDataSelectors.oifuVedVirkningstidspunkt
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EndringUfoeretrygdDtoSelectors.PesysDataSelectors.opphoersbegrunnelse
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EndringUfoeretrygdDtoSelectors.PesysDataSelectors.opphortBarnetillegg
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EndringUfoeretrygdDtoSelectors.PesysDataSelectors.opphortEktefelletillegg
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EndringUfoeretrygdDtoSelectors.PesysDataSelectors.opphortGjenlevendetillegg
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EndringUfoeretrygdDtoSelectors.PesysDataSelectors.pe
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EndringUfoeretrygdDtoSelectors.pesysData
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.ufoer.Ufoeretrygd
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.maler.legacy.fraser.*
import no.nav.pensjon.brev.maler.legacy.vedlegg.vedleggOpplysningerBruktIBeregningUTLegacy
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgPlikterUfore
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.model.Brevkategori
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.namedReference
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import java.time.LocalDate

@TemplateModelHelpers
object EndringUforetrygd : RedigerbarTemplate<EndringUfoeretrygdDto> {

    override val featureToggle = FeatureToggles.brevmalUtInnvilgelse.toggle

    override val kode = Pesysbrevkoder.Redigerbar.UT_ENDRING_UFOERETRYGD
    override val kategori = Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - endring av uføretrygd",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            text(
                bokmal { +"Nav har endret uføretrygden din" },
                nynorsk { +"Nav har endra uføretrygda di" },
            )
        }
        outline {
            val pe = pesysData.pe

            val kravarsak = pe.vedtaksdata_kravhode_kravarsaktype()
            val onsketvirkningsdato = pe.vedtaksdata_kravhode_onsketvirkningsdato().ifNull(LocalDate.now())

            val uforetidspunkt = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforetidspunkt().ifNull(LocalDate.now())
            val skadetidspunkt = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_skadetidspunkt().ifNull(LocalDate.now())
            val virkningstidspunktBegrunnelse = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningbegrunnelse()
            val virkningbegrunnelseStdbegr_22_12_1_5 = virkningstidspunktBegrunnelse.equalTo("stdbegr_22_12_1_5")
            val uforegradFraBeregning = pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()
            val uforegradFraVilkar = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforegrad()
            val yrkesskadegradFraBeregning = pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad()
            val yrkesskadegradFraVilkar = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad()

            val utbetalingsgrad = pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad()
            val mottarMinsteytelse = pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse()
            val belopsgrense = pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_belopsgrense()
            val grunnbelop = pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop()

            val ektefelletilleggInnvilget = pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()
            val gjenlevendetilleggInnvilget = pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()

            val ifuBegrunnelse = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()
            val ieuBegrunnelse = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieubegrunnelse()
            val ieuInntekt = pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieuinntekt()
            val oifuKroner = pesysData.oifuVedVirkningstidspunkt.ifNull(Kroner(0))
            val oifuMerEnnIfu = oifuKroner.greaterThan(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt())

            val barnetilleggSerkullInnvilget = pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()
            val barnetilleggFellesInnvilget = pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()
            val barnetilleggInnvilget = barnetilleggSerkullInnvilget or barnetilleggFellesInnvilget
            val btSerkullNetto = pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto()
            val btSerkullNetto0 = btSerkullNetto.equalTo(0)
            val btFellesNetto = pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto()
            val btFellesNetto0 = btFellesNetto.equalTo(0)
            val btFellesJusteringsbelopPerAr = pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar()
            val btFellesJusteringsbelopPerAr0 = btFellesJusteringsbelopPerAr.equalTo(0)
            val btSerkullJusteringsbelopPerAr = pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar()
            val btSerkullJusteringsbelopPerAr0 = btSerkullJusteringsbelopPerAr.equalTo(0)
            val btFellesFradrag = pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbfradrag()
            val btFellesFradrag0 = btFellesFradrag.equalTo(0)
            val btSerkullFradrag = pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbfradrag()
            val btSerkullFradrag0 = btSerkullFradrag.equalTo(0)

            val instoppholdtype = pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype()
            val instoppholdanvendt = pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instopphanvendt()
            val fasteUtgifterInstopphold = pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphfasteutgifterperiodeliste_instopphfasteutgifterperiode_fasteutgifter()

            val txtBarnetBarnaOpphor = if (pesysData.antallBarnOpphor.greaterThan(1).equals(true)) "barna" else "barnet"
            val txtBarnetBarnaOpphorForsorgaForsorgde = if (pesysData.antallBarnOpphor.greaterThan(1).equals(true)) "barna forsørgde" else "barnet forsørga"
            val txtBarnetBarnaOpphorDittDine = if (pesysData.antallBarnOpphor.greaterThan(1).equals(true)) "barna dine" else "barnet ditt"
            val txtOgEllerEktefelle = if (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget().equals(true)) " og/eller ektefelle" else ""

            showIf(gjenlevendetilleggInnvilget.not() and
                        pe.vedtaksdata_kravhode_kravgjelder().isNotAnyOf("sok_uu", "sok_ys") and
                        kravarsak.isNotAnyOf("endring_ifu", "endret_inntekt", "barn_endret_inntekt", "eps_endret_inntekt", "begge_for_end_inn", "soknad_bt", "instopphold", "omgj_etter_klage", "omgj_etter_anke")) {
                paragraph {
                    text(
                        bokmal { +"Vi har endret uføretrygden din fra " + onsketvirkningsdato.format() + "." },
                        nynorsk { +"Vi har endra uføretrygda di frå " + onsketvirkningsdato.format() + "." }
                    )
                }
            }

            showIf(barnetilleggInnvilget and kravarsak.equalTo("soknad_bt")) {
                paragraph {
                    text(
                        bokmal { +"Vi har innvilget søknaden din om barnetillegg som vi mottok " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Vi har endret uføretrygden din fra " + onsketvirkningsdato.format() + "." },
                        nynorsk { +"Vi har innvilga søknaden din om barnetillegg som vi fekk " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Vi har endra uføretrygda di frå " + onsketvirkningsdato.format() + "." },
                    )

                    showIf(
                        (barnetilleggFellesInnvilget and (((barnetilleggSerkullInnvilget and btSerkullNetto0) and (barnetilleggFellesInnvilget and btFellesNetto0)) or (barnetilleggSerkullInnvilget and btSerkullNetto
                            .equalTo(0) and not(barnetilleggFellesInnvilget)) or (barnetilleggFellesInnvilget and btFellesNetto0 and not(barnetilleggSerkullInnvilget))))
                    ) {
                        text(
                            bokmal { +" Tillegget blir ikke utbetalt fordi inntekten til deg og din " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + " er over grensen for å få utbetalt barnetillegg." },
                            nynorsk { +" Tillegget blir ikkje utbetalt, fordi inntekta til deg og " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall() + " din er over grensa for å få utbetalt barnetillegg." },
                        )
                    }

                    showIf(
                        ((barnetilleggSerkullInnvilget and not(barnetilleggFellesInnvilget)) and (((barnetilleggSerkullInnvilget and btSerkullNetto0) and (barnetilleggFellesInnvilget and btFellesNetto0)) or (barnetilleggSerkullInnvilget and btSerkullNetto
                            .equalTo(0) and not(barnetilleggFellesInnvilget)) or (barnetilleggFellesInnvilget and btFellesNetto0 and not(barnetilleggSerkullInnvilget))))
                    ) {
                        text(
                            bokmal { +" Tillegget blir ikke utbetalt fordi inntekten din er over grensen for å få utbetalt barnetillegg." },
                            nynorsk { +"Tillegget blir ikkje utbetalt fordi inntekta di er over grensa for å få utbetalt barnetillegg." }
                        )
                    }
                }
            }

            showIf(kravarsak.isOneOf("omgj_etter_klage", "omgj_etter_anke")) {
                paragraph {
                    text(
                        bokmal { +"Vi har endret uføretrygden din fra " + onsketvirkningsdato.format() + " fordi du har fått medhold i klagen din." },
                        nynorsk { +"Vi har endra uføretrygda di frå " + onsketvirkningsdato.format() + " fordi du har fått medhald i klaga di." },
                    )
                }
            }

            showIf((barnetilleggInnvilget and kravarsak.isOneOf("endret_inntekt", "barn_endret_inntekt", "eps_endret_inntekt", "begge_for_end_inn"))) {
                paragraph {
                    text(
                        bokmal { +"Vi har endret barnetillegget i uføretrygden din fordi du har meldt fra om inntektsendring." },
                        nynorsk { +"Vi har endra barnetillegget i uføretrygda di fordi du har meldt frå om inntektsendring." },
                    )
                }
            }

            showIf(pesysData.opphortBarnetillegg) {
                paragraph {
                    text(
                        bokmal { +"Vi har vedtatt at barnetillegget i uføretrygden din opphører fra " + onsketvirkningsdato.format() + " for barn født " + fritekst("fødselsdato barnet/barna") + "." },
                        nynorsk { +"Vi har stansa barnetillegget i uføretrygda di frå " + onsketvirkningsdato.format() + " for barn fødd " + fritekst("fødselsdato barnet/barna") + "." },
                    )
                }
            }

            showIf(pesysData.opphortEktefelletillegg) {
                paragraph {
                    text(
                        bokmal { +"Vi har opphørt ektefelletillegget i uføretrygden din fra " + onsketvirkningsdato.format() + "." },
                        nynorsk { +"Vi har stansa ektefelletillegget i uføretrygda di frå " + onsketvirkningsdato.format() + "." },
                    )
                }
            }

            showIf(pe.vedtaksdata_kravhode_kravgjelder().equalTo("sok_uu")) {
                paragraph {
                    text(
                        bokmal { +"Vi har innvilget søknaden din om rettighet som ung ufør som vi mottok " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Vi har endret uføretrygden din fra " + onsketvirkningsdato.format() + "." },
                        nynorsk { +"Vi har innvilga søknaden din om å få rettar som ung ufør som vi fekk " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Vi har endra uføretrygda di frå " + onsketvirkningsdato.format() + "." },
                    )
                }
            }

            showIf((yrkesskadegradFraVilkar.equalTo(uforegradFraVilkar) and pe.vedtaksdata_kravhode_kravgjelder().equalTo("sok_ys"))) {
                paragraph {
                    text(
                        bokmal { +"Vi har innvilget søknaden din om uføretrygd etter særbestemmelsene for yrkesskade eller yrkessykdom som vi mottok " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Vi har kommet fram til at hele uførheten din skyldes en godkjent yrkesskade eller yrkessykdom, og uføretrygden din er endret fra " + onsketvirkningsdato.format() + "." },
                        nynorsk { +"Vi har innvilga søknaden din om uføretrygd etter særreglane for yrkesskade eller yrkessjukdom som vi fekk " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Vi har kome fram til at heile uførleiken din kjem av ein godkjend yrkesskade eller yrkessjukdom, og uføretrygda di er endra frå " + onsketvirkningsdato.format() + "." },
                    )
                }
            }

            showIf((yrkesskadegradFraVilkar.lessThan(uforegradFraBeregning) and pe.vedtaksdata_kravhode_kravgjelder().equalTo("sok_ys"))) {
                paragraph {
                    text(
                        bokmal { +"Vi har innvilget søknaden din om uføretrygd etter særbestemmelsene for yrkesskade eller yrkessykdom som vi mottok " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Vi har kommet fram til at " + yrkesskadegradFraBeregning.format() + " prosent av uførheten din skyldes en godkjent yrkesskade eller yrkessykdom, og uføretrygden din er endret fra " + onsketvirkningsdato.format() + "." },
                        nynorsk { +"Vi har innvilga søknaden din om uføretrygd etter særreglane for yrkesskade eller yrkessjukdom som vi fekk " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Vi har kome fram til at " + yrkesskadegradFraBeregning.format() + " prosent av uførleiken din kjem av ein godkjend yrkesskade eller yrkessjukdom, og uføretrygda di er endra frå " + onsketvirkningsdato.format() + "." },
                    )
                }
            }

            showIf(kravarsak.equalTo("endring_ifu")) {
                paragraph {
                    text(
                        bokmal { +"Vi har innvilget søknaden din om endring av inntektsgrense. " },
                        nynorsk { +"Vi har innvilga søknaden din om endring av inntektsgrense. " },
                    )
                    text(
                        bokmal { +"Den nye inntektsgrensen din har økt til " + pe.ut_inntektsgrense_faktisk().format() + " kroner fra " + onsketvirkningsdato.format() + "." },
                        nynorsk { +"Den nye inntektsgrensa di har auka til " + pe.ut_inntektsgrense_faktisk().format() + " kroner frå " + onsketvirkningsdato.format() + "." },
                    )
                }
            }

            showIf(gjenlevendetilleggInnvilget and kravarsak.isNotAnyOf("soknad_bt", "instopphold")) {
                paragraph {
                    text(
                        bokmal { +"Vi har innvilget deg gjenlevenderettigheter i uføretrygden din. Vi har endret uføretrygden din fra " + onsketvirkningsdato.format() + "." },
                        nynorsk { +"Vi har innvilga deg attlevanderettar i uføretrygda di. Vi har endra uføretrygda di frå " + onsketvirkningsdato.format() + "." },
                    )
                }
            }

            showIf(pesysData.opphortGjenlevendetillegg) {
                paragraph {
                    text(
                        bokmal { +"Vi har opphørt gjenlevendetillegget i uføretrygden din fra " + onsketvirkningsdato.format() + "." },
                        nynorsk { +"Vi har stansa attlevandetillegget i uføretrygda di frå " + onsketvirkningsdato.format() + "." },
                    )
                }
            }

            showIf((kravarsak.equalTo("instopphold") and instoppholdanvendt and instoppholdtype.equalTo("reduksjon_hs"))) {
                paragraph {
                    text(
                        bokmal { +"Vi har endret uføretrygden din fra " + onsketvirkningsdato.format() + " fordi du ble innlagt på institusjon." },
                        nynorsk { +"Vi har endra uføretrygda di frå " + onsketvirkningsdato.format() + " fordi du ble innlagd på institusjon." },
                    )
                }
            }

            showIf((not(instoppholdanvendt) and instoppholdtype.equalTo("reduksjon_hs"))) {
                paragraph {
                    text(
                        bokmal { +"Vi har mottatt opplysninger om at du er innlagt på institusjon. Dette får ikke betydning for uføretrygden din, og du vil få utbetalt det samme som før." },
                        nynorsk { +"Vi har fått opplysningar om at du er innlagd på institusjon. Dette får ikkje noko å seie for uføretrygda di, og du får utbetalt det same som før." },
                    )

                    showIf((instoppholdtype.equalTo("reduksjon_hs") and not(instoppholdanvendt) and pe.ut_forsorgeransvar_ingen_er_false() and kravarsak.equalTo("instopphold") and barnetilleggFellesInnvilget and btFellesNetto0)) {
                        text(
                            bokmal { +" Fra og med måneden etter at du ble innlagt på institusjon vil du bli vurdert med sivilstand som enslig. Det betyr at barnetillegget kun vil bli beregnet ut fra den samlede inntekten din fra samme tidspunkt." },
                            nynorsk { +" Frå og med månaden etter at du blei innlagd på institusjon vil du bli vurdert med ein sivilstand som einsleg. Det betyr at barnetillegget berre blir berekna ut frå den samla inntekta di frå same tidspunkt. " },
                        )
                    }
                }
            }

            showIf((kravarsak.equalTo("instopphold") and not(instoppholdanvendt) and instoppholdtype.equalTo("reduksjon_fo"))) {
                paragraph {
                    text(
                        bokmal { +"Vi har mottatt opplysninger om at du er under straffegjennomføring. Dette får ikke betydning for uføretrygden din, og du vil få utbetalt det samme som før." },
                        nynorsk { +"Vi har fått opplysningar om at du er under straffegjennomføring. Dette får ikkje noko å seie for uføretrygda di, og du får utbetalt det same som før." },
                    )
                    //TODO masse forenklinger i uttrykk i instopphold, som feks showif under vs showif over
                    showIf((instoppholdtype.equalTo("reduksjon_fo") and not(instoppholdanvendt) and pe.ut_forsorgeransvar_ingen_er_false() and kravarsak.equalTo("instopphold") and barnetilleggInnvilget)) {
                        text(
                            bokmal { +" Fra og med måneden etter at du er under straffegjennomføring vil du bli vurdert med sivilstand som enslig. Det betyr at barnetillegget kun vil bli beregnet ut fra den samlede inntekten din fra samme tidspunkt." },
                            nynorsk { +" Frå og med månaden etter at du er under straffegjennomføringa vil du bli vurdert med ein sivilstand som einsleg. Det betyr at barnetillegget berre blir berekna ut frå den samla inntekta di frå same tidspunkt." },
                        )
                    }
                }
            }

            showIf((kravarsak.equalTo("instopphold") and instoppholdanvendt and instoppholdtype.equalTo("reduksjon_fo"))) {
                paragraph {
                    text(
                        bokmal { +"Vi har endret utbetalingen av uføretrygden din fra " + onsketvirkningsdato.format() + " fordi du er under straffegjennomføring." },
                        nynorsk { +"Vi har stansa utbetalinga av uføretrygda di frå " + onsketvirkningsdato.format() + " fordi du er under straffegjennomføring." },
                    )
                }
            }

            showIf((kravarsak.equalTo("instopphold") and instoppholdtype.equalTo(""))) {
                paragraph {
                    text(
                        bokmal { +"Vi har gjenopptatt utbetaling av uføretrygden din fra " + onsketvirkningsdato.format() + "." },
                        nynorsk { +"Vi har teke til att med å utbetale uføretrygda di frå " + onsketvirkningsdato.format() + "." },
                    )
                }
            }

            showIf((not(ektefelletilleggInnvilget) and not(barnetilleggInnvilget) and not(gjenlevendetilleggInnvilget) and utbetalingsgrad.equalTo(uforegradFraBeregning) and not(instoppholdanvendt))) {
                paragraph {
                    text(
                        bokmal { +"Du får " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " i uføretrygd per måned før skatt." },
                        nynorsk { +"Du får " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " i uføretrygd per månad før skatt." },
                    )
                }
            }

            showIf((barnetilleggInnvilget and not(ektefelletilleggInnvilget) and not(gjenlevendetilleggInnvilget) and utbetalingsgrad.equalTo(uforegradFraBeregning) and not(instoppholdanvendt))) {
                paragraph {
                    text(
                        bokmal { +"Du får " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " i uføretrygd og barnetillegg per måned før skatt." },
                        nynorsk { +"Du får " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " i uføretrygd og barnetillegg per månad før skatt." },
                    )
                }
            }

            showIf((not(barnetilleggInnvilget) and gjenlevendetilleggInnvilget and not(ektefelletilleggInnvilget) and utbetalingsgrad.equalTo(uforegradFraBeregning) and not(instoppholdanvendt))) {
                paragraph {
                    text(
                        bokmal { +"Du får " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " i uføretrygd og gjenlevendetillegg per måned før skatt." },
                        nynorsk { +"Du får " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " i uføretrygd og attlevandetillegg per månad før skatt." },
                    )
                }
            }

            showIf((barnetilleggInnvilget and gjenlevendetilleggInnvilget and not(ektefelletilleggInnvilget) and utbetalingsgrad.equalTo(uforegradFraBeregning) and not(instoppholdanvendt))) {
                paragraph {
                    text(
                        bokmal { +"Du får " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " i uføretrygd, barne- og gjenlevendetillegg per måned før skatt." },
                        nynorsk { +"Du får " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " i uføretrygd, barne- og attlevandetillegg per månad før skatt." },
                    )
                }
            }

            showIf((ektefelletilleggInnvilget and not(gjenlevendetilleggInnvilget) and not(barnetilleggInnvilget) and utbetalingsgrad.equalTo(uforegradFraBeregning) and not(instoppholdanvendt) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etnetto().greaterThan(0))) {
                paragraph {
                    text(
                        bokmal { +"Du får " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " i uføretrygd og ektefelletillegg per måned før skatt." },
                        nynorsk { +"Du får " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " i uføretrygd og ektefelletillegg per månad før skatt." },
                    )
                }
            }

            showIf((ektefelletilleggInnvilget and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etnetto().greaterThan(0) and ((barnetilleggSerkullInnvilget and btSerkullNetto.greaterThan(0)) or (barnetilleggFellesInnvilget and btFellesNetto.greaterThan(0))) and not(gjenlevendetilleggInnvilget) and not(instoppholdanvendt))) {
                paragraph {
                    text(
                        bokmal { +"Du får " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " i uføretrygd, barne- og ektefelletillegg per måned før skatt." },
                        nynorsk { +"Du får " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " i uføretrygd, barne- og ektefelletillegg per månad før skatt." },
                    )
                }
            }

            showIf((utbetalingsgrad.lessThan(uforegradFraBeregning) and not(barnetilleggInnvilget) and not(gjenlevendetilleggInnvilget) and not(ektefelletilleggInnvilget) and not(instoppholdanvendt))) {
                paragraph {
                    text(
                        bokmal { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningufore_total().format() + " kroner i uføretrygd per måned før skatt. Fordi du har inntekt ved siden av uføretrygden, vil utbetalingen din bli redusert til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner per måned før skatt." },
                        nynorsk { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningufore_total().format() + " kroner i uføretrygd per månad før skatt. Fordi du har inntekt ved sida av uføretrygda, blir utbetalinga di redusert til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner per månad før skatt." },
                    )
                }
            }

            showIf((utbetalingsgrad.lessThan(uforegradFraBeregning) and (barnetilleggInnvilget) and not(gjenlevendetilleggInnvilget) and not(ektefelletilleggInnvilget) and not(instoppholdanvendt))) {
                paragraph {
                    text(
                        bokmal { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningufore_total().format() + " kroner i uføretrygd og barnetillegg per måned før skatt. Fordi du har inntekt ved siden av uføretrygden, vil utbetalingen din bli redusert til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner per måned før skatt." },
                        nynorsk { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningufore_total().format() + " kroner i uføretrygd og barnetillegg per månad før skatt. Fordi du har inntekt ved sida av uføretrygda, blir utbetalinga di redusert til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner per månad før skatt." },
                    )
                }
            }

            showIf((utbetalingsgrad.lessThan(uforegradFraBeregning) and not(barnetilleggInnvilget) and gjenlevendetilleggInnvilget and not(ektefelletilleggInnvilget) and not(instoppholdanvendt))) {
                paragraph {
                    text(
                        bokmal { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningufore_total().format() + " kroner i uføretrygd og gjenlevendetillegg per måned før skatt. Fordi du har inntekt ved siden av uføretrygden, vil utbetalingen din bli redusert til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner per måned før skatt." },
                        nynorsk { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningufore_total().format() + " kroner i uføretrygd og attlevandetillegg per månad før skatt. Fordi du har inntekt ved sida av uføretrygda, blir utbetalinga di redusert til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner per månad før skatt." },
                    )
                }
            }

            showIf((utbetalingsgrad.lessThan(uforegradFraBeregning) and (not(barnetilleggSerkullInnvilget) or not(barnetilleggFellesInnvilget)) and not(gjenlevendetilleggInnvilget) and ektefelletilleggInnvilget and not(instoppholdanvendt))) {
                paragraph {
                    text(
                        bokmal { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningufore_total().format() + " kroner i uføretrygd og ektefelletillegg per måned før skatt. Fordi du har inntekt ved siden av uføretrygden, vil utbetalingen din bli redusert til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner per måned før skatt." },
                        nynorsk { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningufore_total().format() + " kroner i uføretrygd og ektefelletillegg per månad før skatt. Fordi du har inntekt ved sida av uføretrygda, blir utbetalinga di redusert til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner per månad før skatt." },
                    )
                }
            }

            showIf((utbetalingsgrad.lessThan(uforegradFraBeregning) and (barnetilleggInnvilget) and gjenlevendetilleggInnvilget and not(ektefelletilleggInnvilget) and not(instoppholdanvendt))) {
                paragraph {
                    text(
                        bokmal { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningufore_total().format() + " kroner i uføretrygd, barne- og gjenlevendetillegg per måned før skatt. Fordi du har inntekt ved siden av uføretrygden, vil utbetalingen din bli redusert til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner per måned før skatt." },
                        nynorsk { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningufore_total().format() + " kroner i uføretrygd, barne- og attlevandetillegg per månad før skatt. Fordi du har inntekt ved sida av uføretrygda, blir utbetalinga di redusert til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner per månad før skatt." },
                    )
                }
            }

            showIf((instoppholdtype.equalTo("reduksjon_fo") and pe.ut_forsorgeransvar_ingen_er_true() and instoppholdanvendt)) {
                paragraph {
                    text(
                        bokmal { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto().format() + " kroner i uføretrygd, men den kommer ikke til utbetaling fordi du er under straffegjennomføring." },
                        nynorsk { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto().format() + " kroner i uføretrygd, men ho blir ikkje betalt ut fordi du er under straffegjennomføring." },
                    )
                }
            }

            showIf((instoppholdtype.equalTo("reduksjon_hs") and instoppholdanvendt)) {
                paragraph {
                    text(
                        bokmal { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto().format() + " kroner i uføretrygd, men utbetalingen er redusert fordi du er innlagt på institusjon. I denne perioden vil du få utbetalt " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner." },
                        nynorsk { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto().format() + " kroner i uføretrygd, men utbetalinga er redusert fordi du er innlagd på institusjon. I denne perioden får du betalt ut " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner." },
                    )
                }
            }

            showIf((instoppholdtype.equalTo("reduksjon_fo") and pe.ut_forsorgeransvar_ingen_er_false() and instoppholdanvendt)) {
                paragraph {
                    text(
                        bokmal { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto().format() + " kroner i uføretrygd, men utbetalingen er redusert fordi du er under straffegjennomføring. I denne perioden vil du få utbetalt " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner." },
                        nynorsk { +"Du har rett til å få " + pe.vedtaksdata_beregningsdata_beregningsresultattilrevurderingtotalnetto().format() + " kroner i uføretrygd, men utbetalinga er redusert fordi du er under straffegjennomføring. I denne perioden får du betalt ut " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner." },
                    )
                }
            }

            showIf((((pe.grunnlag_persongrunnlagsliste_personbostedsland()).equalTo("nor") or (pe.grunnlag_persongrunnlagsliste_personbostedsland()).equalTo("")) and (pe.vedtaksdata_beregningsdata_beregningufore_belopokt() or pe.vedtaksdata_beregningsdata_beregningufore_belopredusert()))) {
                paragraph {
                    text(
                        bokmal { +"Uføretrygden blir utbetalt senest den 20. hver måned. Du får din første utbetaling med nytt beløp i " + fritekst("måned og år") + "." },
                        nynorsk { +"Uføretrygda blir utbetalt seinast den 20. i kvar månad. Du får den første utbetalinga di med nytt beløp i " + fritekst("måned og år") + "." },
                    )
                }
            }

            showIf(((pe.grunnlag_persongrunnlagsliste_personbostedsland()).notEqualTo("nor") and (pe.grunnlag_persongrunnlagsliste_personbostedsland()).notEqualTo(""))) {
                paragraph {
                    text(
                        bokmal { +"Uføretrygden blir utbetalt senest den 20. hver måned. Mottar du uføretrygden på en utenlandsk bankkonto kan utbetalingen bli forsinket. Du får din første utbetaling i " + fritekst("måned og år") + "." },
                        nynorsk { +"Uføretrygda blir utbetalt seinast den 20. i kvar månad. Får du uføretrygda på ein utanlandsk bankkonto, kan utbetalinga bli forseinka. Du får den første utbetalinga di i " + fritekst("måned og år") + "." },
                    )
                }
            }

            showIf(((not(pe.vedtaksdata_beregningsdata_beregningufore_belopokt()) and not(pe.vedtaksdata_beregningsdata_beregningufore_belopredusert())) and ((pe.grunnlag_persongrunnlagsliste_personbostedsland()).equalTo("nor") or (pe.grunnlag_persongrunnlagsliste_personbostedsland()).equalTo("")))) {
                includePhrase(TBU2223_Generated)
            }
            paragraph {
                text(
                    bokmal { +"I dette brevet forklarer vi hvilke rettigheter og plikter du har. Det er derfor viktig at du leser hele brevet." },
                    nynorsk { +"I dette brevet forklarer vi kva rettar og plikter du har. Det er derfor viktig at du les heile brevet." },
                )
            }

            title1 {
                text(
                    bokmal { +"Begrunnelse for vedtaket" },
                    nynorsk { +"Grunngiving for vedtaket" },
                )
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse = true AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold" AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_EktefelleGarantiTillegg_EGTinnvilget = false) THEN      INCLUDE ENDIF
            // TODO: Finner ingen mapping av dette for exstream brev, tar en antagelse på at EktefelleGarantiTillegg_EGTinnvilget alltid vil være false
            showIf((mottarMinsteytelse and kravarsak.isNotAnyOf("soknad_bt", "instopphold"))) {

                paragraph {
                    text(
                        bokmal { +"Du har uføretrygd med minsteytelse. Størrelsen på minsteytelsen er avhengig av sivilstanden din." },
                        nynorsk { +"Du har uføretrygd med minsteyting. Storleiken på minsteytinga er avhengig av sivilstanden din." },
                    )
                }
            }

            showIf((mottarMinsteytelse and kravarsak.equalTo("sivilstandsendring") and (pe.vedtaksdata_beregningsdata_beregningufore_belopokt() or pe.vedtaksdata_beregningsdata_beregningufore_belopredusert()))) {

                paragraph {
                    text(
                        bokmal { +"Vi har mottatt opplysninger om at sivilstanden din har blitt endret. Utbetalingen din endres derfor til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_sats().format() + " ganger folketrygdens grunnbeløp." },
                        nynorsk { +"Vi har fått opplysningar om at sivilstanden din har blitt endra. Utbetalinga av uføretrygda di blir derfor endra til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_sats().format() + " gonger grunnbeløpet i folketrygda." },
                    )
                }
            }

            showIf(((not(mottarMinsteytelse) or (not(pe.vedtaksdata_beregningsdata_beregningufore_belopokt()) and not(pe.vedtaksdata_beregningsdata_beregningufore_belopredusert()))) and kravarsak.equalTo("sivilstandsendring"))) {

                paragraph {
                    text(
                        bokmal { +"Vi har mottatt opplysninger om at sivilstanden din har blitt endret. Dette får ikke betydning for uføretrygden din, og du vil få utbetalt det samme som før." },
                        nynorsk { +"Vi har fått opplysningar om at sivilstanden din har blitt endra. Dette får ikkje noko å seie for uføretrygda di, og du får utbetalt det same som før." },
                    )
                }
            }

            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed 1-5") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed 3-2") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed_ektefelle") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed_registrert_partner")) and pe.vedtaksdata_beregningsdata_beregningufore_belopredusert() and not(mottarMinsteytelse) and kravarsak.equalTo("sivilstandsendring"))) {

                paragraph {
                    text(
                        bokmal { +"Vi har mottatt opplysninger om at du " + fritekst("sivilstandsendring") + ". Du har minsteytelse i uføretrygden din. Den endrede sivilstanden din medfører nå at du får uføretrygd på grunnlag av egen opptjening." },
                        nynorsk { +"Vi har fått opplysningar om at du " + fritekst("sivilstandsendring") + ". Du har minsteyting i uføretrygda di. Den endra sivilstanden din fører no til at du får uføretrygd på grunnlag av di eiga opptening." },
                    )
                }
            }

            showIf((pe.vedtaksdata_kravhode_kravgjelder().equalTo("sok_uu") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_unguforresultat()).equalTo("oppfylt"))) {

                paragraph {
                    text(
                        bokmal { +"For å innvilges rettighet som ung ufør, må du være alvorlig og varig syk før du fylte 26 år. Uføretidspunktet ditt er fastsatt til " + uforetidspunkt.format() + ". Du er innvilget rettighet som ung ufør. Det betyr at uføretrygden din vil bli beregnet etter regler som gjelder ung ufør." },
                        nynorsk { +"For at du skal få innvilga rett som ung ufør, må du vere alvorleg og varig sjuk før du fylte 26 år. Uføretidspunktet ditt er sett til " + uforetidspunkt.format() + ". Du er innvilga rett som ung ufør. Det betyr at uføretrygda di blir berekna etter reglar som gjeld ung ufør." },
                    )
                }
            }

            showIf(((yrkesskadegradFraVilkar).equalTo(uforegradFraBeregning) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadebest() and kravarsak.isNotAnyOf("soknad_bt", "instopphold"))) {

                paragraph {
                    text(
                        bokmal { +"Du er innvilget uføretrygd etter regler for yrkesskade eller yrkessykdom. Vi har ut fra sakens opplysninger vurdert at hele din nedsatte inntektsevne skyldes den godkjente yrkesskaden eller yrkessykdommen." },
                        nynorsk { +"Du er innvilga uføretrygd etter reglar for yrkesskade eller yrkessjukdom. Vi har ut frå opplysningane i saka vurdert at heile den nedsette inntektsevna di kjem av den godkjende yrkesskaden eller yrkessjukdommen." },
                    )

                    showIf(not(pe.vedtaksbrev_vedtaksdata_kravhode_brukerkonvertertup())) {
                        text(
                            bokmal { +" Dette betyr at uføretrygden din vil bli beregnet etter særbestemmelser som gir deg en høyere uføretrygd." },
                            nynorsk { +" Dette betyr at uføretrygda di blir berekna etter særreglar, og gir deg ei høgare uføretrygd." },
                        )
                    }
                }
                paragraph {
                    text(
                        bokmal { +"Skadetidspunktet ditt har vi fastsatt til " + skadetidspunkt.format() + ". Din årlige inntekt på dette tidspunktet er fastsatt til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format() + " kroner." },
                        nynorsk { +"Skadetidspunktet ditt har vi fastsett til " + skadetidspunkt.format() + ". Den årlege inntekta di på dette tidspunktet er fastsett til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format() + " kroner." },
                    )
                }

                showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat()).equalTo("oppfylt") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ivsbegrunnelse()).equalTo("stdbegr_12_17_3_2"))) {
                    paragraph {
                        text(
                            bokmal { +"Skadetidspunktet ditt har vi fastsatt til " + skadetidspunkt.format() + ". Din årlige inntekt på dette tidspunktet er fastsatt til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format() + " kroner. Vi har justert denne inntekten opp fordi den skal tilsvare inntekten du ville hatt som frisk på skadetidspunktet." },
                            nynorsk { +"Skadetidspunktet ditt har vi fastsett til " + skadetidspunkt.format() + ". Den årlege inntekta på dette tidspunktet er fastsett til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format() + " kroner. Vi har justert denne inntekta opp fordi den skal svare til inntekta du ville hatt som frisk på skadetidspunktet." },
                        )
                    }
                }
            }

            showIf(((yrkesskadegradFraVilkar).equalTo(uforegradFraBeregning) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadebest()) and kravarsak.isNotAnyOf("soknad_bt", "instopphold"))) {
                paragraph {
                    text(
                        bokmal { +"Du er innvilget uføretrygd etter regler for yrkesskade eller yrkessykdom. Vi har ut fra sakens opplysninger vurdert at hele din nedsatte inntektsevne skyldes den godkjente yrkesskaden eller yrkessykdommen." },
                        nynorsk { +"Du er innvilga uføretrygd etter reglar for yrkesskade eller yrkessjukdom. Vi har ut frå opplysningane i saka vurdert at heile den nedsette inntektsevna di kjem av den godkjende yrkesskaden eller yrkessjukdommen." },
                    )

                    showIf(not(pe.vedtaksbrev_vedtaksdata_kravhode_brukerkonvertertup())) {
                        text(
                            bokmal { +" Dette betyr at uføretrygden din vil bli beregnet etter særbestemmelser, dersom dette er til fordel for deg." },
                            nynorsk { +" Dette betyr at uføretrygda di blir berekna etter særreglar dersom dette er til fordel for deg." },
                        )
                    }
                }
                paragraph {
                    text(
                        bokmal { +"Skadetidspunktet ditt har vi fastsatt til " + skadetidspunkt.format() + ". Din årlige inntekt på dette tidspunktet er fastsatt til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format() + " kroner." },
                        nynorsk { +"Skadetidspunktet ditt har vi fastsett til " + skadetidspunkt.format() + ". Den årlege inntekta di på dette tidspunktet er fastsett til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format() + " kroner." },
                    )
                }

                showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat()).equalTo("oppfylt") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ivsbegrunnelse()).equalTo("stdbegr_12_17_3_2"))) {
                    paragraph {
                        text(
                            bokmal { +"Skadetidspunktet ditt har vi fastsatt til " + skadetidspunkt.format() + ". Din årlige inntekt på dette tidspunktet er fastsatt til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format() + " kroner. Vi har justert denne inntekten opp fordi den skal tilsvare inntekten du ville hatt som frisk på skadetidspunktet." },
                            nynorsk { +"Skadetidspunktet ditt har vi fastsett til " + skadetidspunkt.format() + ". Den årlege inntekta på dette tidspunktet er fastsett til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format() + " kroner. Vi har justert denne inntekta opp fordi den skal svare til inntekta du ville hatt som frisk på skadetidspunktet." },
                        )
                    }
                }

                showIf(not(pe.vedtaksbrev_vedtaksdata_kravhode_brukerkonvertertup())) {
                    paragraph {
                        text(
                            bokmal { +"Inntekten din på skadetidspunktet er lavere enn beregningsgrunnlaget ditt, og uføretrygden din vil derfor ikke bli beregnet etter særbestemmelser for yrkesskade eller yrkessykdom." },
                            nynorsk { +"Inntekta di på skadetidspunktet er lågare enn berekningsgrunnlaget ditt, og uføretrygda di blir derfor ikkje berekna etter særreglar for yrkesskade eller yrkessjukdom." },
                        )
                    }
                }
            }

            showIf(((yrkesskadegradFraVilkar).lessThan(uforegradFraBeregning) and (yrkesskadegradFraVilkar).greaterThan(0) and kravarsak.isNotAnyOf("soknad_bt", "instopphold"))) {
                paragraph {
                    text(
                        bokmal { +"Du er innvilget uføretrygd etter regler for yrkesskade eller yrkessykdom. Vi har ut fra sakens opplysninger vurdert om yrkesskaden eller yrkessykdommen din er årsak til uførheten din." },
                        nynorsk { +"Du er innvilga uføretrygd etter reglar for yrkesskade eller yrkessjukdom. Vi har ut frå opplysningane i saka vurdert om yrkesskaden eller yrkessjukdommen din er årsaka til uførleiken din." },
                    )
                }

                paragraph {
                    text(
                        bokmal { +"Vi har kommet fram til at " + yrkesskadegradFraBeregning.format() + " prosent av uførheten din skyldes godkjent yrkesskade eller yrkessykdom. " + fritekst("konkret begrunnelse") + "" },
                        nynorsk { +"Vi har kome fram til at " + yrkesskadegradFraBeregning.format() + " prosent av uførleiken din kjem av godkjend yrkesskade eller yrkessjukdom. " + fritekst("konkret begrunnelse") + "." },
                    )
                }

                showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadebest() and not(pe.vedtaksbrev_vedtaksdata_kravhode_brukerkonvertertup()))) {
                    paragraph {
                        text(
                            bokmal { +"Derfor vil denne delen av uføretrygden din bli beregnet etter særbestemmelser som gir deg en høyere uføretrygd." },
                            nynorsk { +"Derfor vil denne delen av uføretrygda di bli rekna ut etter særreglar som gjer deg ei høgare uføretrygd." },
                        )
                    }
                }
            }

            showIf(((yrkesskadegradFraVilkar).lessThan(uforegradFraBeregning) and (yrkesskadegradFraVilkar).greaterThan(0) and kravarsak.isNotAnyOf("soknad_bt", "instopphold"))) {
                showIf((not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadebest()) and not(pe.vedtaksbrev_vedtaksdata_kravhode_brukerkonvertertup()))) {
                    paragraph {
                        text(
                            bokmal { +"Denne delen av uføretrygden din vil bli beregnet etter særbestemmelser, dersom dette er til fordel for deg." },
                            nynorsk { +"Denne delen av uføretrygda di blir rekna ut etter særreglar dersom det er til fordel for deg." },
                        )
                    }
                }

                showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskadebegrunnelse()).equalTo("stdbegr_12_17_3_1"))) {
                    paragraph {
                        text(
                            bokmal { +"Skadetidspunktet ditt har vi fastsatt til " + skadetidspunkt.format() + ". Din årlige inntekt på dette tidspunktet er fastsatt til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format() + " kroner." },
                            nynorsk { +"Skadetidspunktet ditt har vi fastsett til " + skadetidspunkt.format() + ". Den årlege inntekta di på dette tidspunktet er fastsett til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format() + " kroner." },
                        )
                    }
                }

                showIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat()).equalTo("oppfylt") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ivsbegrunnelse()).equalTo("stdbegr_12_17_3_2"))) {
                    paragraph {
                        text(
                            bokmal { +"Skadetidspunktet ditt har vi fastsatt til " + skadetidspunkt.format() + ". Din årlige inntekt på dette tidspunktet er fastsatt til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format() + " kroner. Vi har justert denne inntekten opp fordi den skal tilsvare inntekten du ville hatt som frisk på skadetidspunktet." },
                            nynorsk { +"Skadetidspunktet ditt har vi fastsett til " + skadetidspunkt.format() + ". Den årlege inntekta på dette tidspunktet er fastsett til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format() + " kroner. Vi har justert denne inntekta opp fordi den skal svare til inntekta du ville hatt som frisk på skadetidspunktet." },
                        )
                    }
                }

                showIf((not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadebest()) and not(pe.vedtaksbrev_vedtaksdata_kravhode_brukerkonvertertup()))) {
                    paragraph {
                        text(
                            bokmal { +"Inntekten din på skadetidspunktet er lavere enn beregningsgrunnlaget ditt, og uføretrygden din vil derfor ikke bli beregnet etter særbestemmelser for yrkesskade eller yrkessykdom." },
                            nynorsk { +"Inntekta di på skadetidspunktet er lågare enn berekningsgrunnlaget ditt, og uføretrygda di blir derfor ikkje berekna etter særreglar for yrkesskade eller yrkessjukdom." },
                        )
                    }
                }
            }

            showIf((kravarsak.equalTo("endring_ifu") and uforegradFraBeregning.greaterThan(0) and uforegradFraBeregning.lessThan(100) and (ifuBegrunnelse).equalTo("stdbegr_12_8_2_9"))) {
                showIf((kravarsak.equalTo("endring_ifu") and uforegradFraBeregning.greaterThan(0) and uforegradFraBeregning.lessThan(100) and (ifuBegrunnelse).equalTo("stdbegr_12_8_2_9"))) {
                    paragraph {
                        text(
                            bokmal { +"Inntekten i stillingen din har økt og du får derfor en høyere inntektsgrense. Dette gjør vi for at du skal få riktig utbetaling av uføretrygd." },
                            nynorsk { +"Inntekta i stillinga di har auka, og du får derfor ei høgare inntektsgrense. Dette gjer vi for at du skal få riktig utbetaling av uføretrygd." },
                        )
                    }
                }
                paragraph {
                    text(
                        bokmal { +"Du kan lese mer om dette i vedlegget " },
                        nynorsk { +"Du kan lese meir om dette i vedlegget " },
                    )
                    namedReference(vedleggOpplysningerBruktIBeregningUTLegacy)
                    text(bokmal { +"." }, nynorsk { +"." })
                }

                showIf((barnetilleggInnvilget and pe.ut_virkningstidpunktstorreenn01012016())) {
                    paragraph {
                        text(
                            bokmal { +"Fordi inntekten din har økt, blir størrelsen på barnetillegget ditt endret." },
                            nynorsk { +"Fordi inntekta di har auka, blir storleiken på barnetillegget ditt endra." },
                        )
                    }
                }
            }

            showIf((gjenlevendetilleggInnvilget and kravarsak.isNotAnyOf("soknad_bt", "instopphold"))) {
                paragraph {
                    text(
                        bokmal { +"Du er innvilget gjenlevendetillegg i uføretrygden fordi du er gjenlevende ektefelle/samboer. Tillegget er beregnet etter ditt eget og den avdødes beregningsgrunnlag. " },
                        nynorsk { +"Du er innvilga attlevandetillegg i uføretrygda fordi du er attlevande ektefelle/sambuar. Tillegget er berekna etter ditt eiga og den avdøde sitt brekningsgrunnlag. " },
                    )

                    showIf((mottarMinsteytelse and gjenlevendetilleggInnvilget)) {
                        text(
                            bokmal { +"Siden sivilstanden din har endret seg har dette også betydning for størrelsen på uføretrygden din. " },
                            nynorsk { +"Sidan sivilstanden din har endra seg har dette også noko å seie for storleiken på uføretrygda di. " },
                        )
                    }

                    showIf((mottarMinsteytelse or (gjenlevendetilleggInnvilget and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtnetto().greaterThan(0)))) {
                        text(
                            bokmal { +"Det betyr at uføretrygden din har økt. " },
                            nynorsk { +"Det betyr at uføretrygda di har auka. " },
                        )
                    }

                    showIf(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_nyttgjenlevendetillegg()) {
                        text(
                            bokmal { +"Tillegget er tidsbegrenset til fem år fra virkningstidspunktet. " },
                            nynorsk { +"Tillegget er tidsbegrensa til fem år frå verknadstidspunktet. " },
                        )
                    }
                }
            }

            showIf((gjenlevendetilleggInnvilget and kravarsak.isNotAnyOf("soknad_bt", "instopphold"))) {
                paragraph {
                    text(
                        bokmal { +"Avdøde må også ha vært medlem i folketrygden, eller mottatt pensjon fra folketrygden, de siste " + fritekst("tre/fem årene") + " før dødsfallet." },
                        nynorsk { +"Avdøde må også ha vore medlem i folketrygda, eller fått pensjon frå folketrygda, dei siste " + fritekst("tre/fem åra") + " før dødsfallet." },
                    )
                }
            }

            showIf((kravarsak.equalTo("sivilstandsendring") and pe.vedtaksdata_beregningsdata_beregning_beregningbenyttetsivilstand().equalTo("gift") and pesysData.opphortGjenlevendetillegg)) {
                paragraph {
                    text(
                        bokmal { +"Du er tidligere innvilget gjenlevendetillegg i uføretrygden din. Gjenlevendetillegget ditt opphører fra " + onsketvirkningsdato.format() + " fordi du har giftet deg." },
                        nynorsk { +"Du er tidlegare innvilga attlevandetillegg i uføretrygda di. Attlevandetillegget ditt tek slutt frå " + onsketvirkningsdato.format() + " fordi du har gifta deg." },
                    )
                }
            }

            showIf((kravarsak.equalTo("sivilstandsendring") and (pe.vedtaksdata_beregningsdata_beregning_beregningbrukersivilstand().equalTo("samboer1_5") or pe.vedtaksdata_beregningsdata_beregning_beregningbrukersivilstand().equalTo("samboer3_2") and pe.vedtaksdata_beregningsdata_beregning_beregningbrukersivilstand().notEqualTo("gift")) and pesysData.opphortGjenlevendetillegg)) {
                paragraph {
                    text(
                        bokmal { +"Du er tidligere innvilget gjenlevendetillegg i uføretrygden din. Gjenlevendetillegget ditt opphører fra " + onsketvirkningsdato.format() + " fordi du er i et samboerforhold, og dere har felles barn." },
                        nynorsk { +"Du er tidlegare innvilga attlevandetillegg i uføretrygda di. Attlevandetillegget ditt tek slutt frå " + onsketvirkningsdato.format() + " fordi du er i eit sambuarforhold og de har felles barn." },
                    )
                }
            }

            showIf(pesysData.opphortEktefelletillegg and kravarsak.isNotAnyOf("sivilstandsendring", "soknad_bt", "instopphold")) {
                paragraph {
                    showIf(not(pesysData.opphoersbegrunnelse.bruker_flyttet_ikke_avt_land or pesysData.opphoersbegrunnelse.eps_flyttet_ikke_avt_land or pesysData.opphoersbegrunnelse.eps_opph_ikke_avt_land)) {
                        text(
                            bokmal { +"Ektefelletillegg beholdes bare ut den perioden som vedtaket gjelder for. Vi har derfor opphørt ektefelletillegget." },
                            nynorsk { +"Ektefelletillegget beheld du bare ut perioden vedtaket gjeld for. Vi har derfor stansa ektefelletillegget ditt. " },
                        )
                    }

                    showIf(pesysData.opphoersbegrunnelse.bruker_flyttet_ikke_avt_land) {
                        text(
                            bokmal { +"Ifølge våre opplysninger er du bosatt i " + fritekst("bostedsland") + ". Derfor har du ikke lenger rett til ektefelletillegg." },
                            nynorsk { +"Ifølgje våre opplysningar er du busett i " + fritekst("bostedsland") + ". Da har du ikkje lenger rett til ektefelletillegg." },
                        )
                    }

                    showIf((pesysData.opphoersbegrunnelse.eps_flyttet_ikke_avt_land or pesysData.opphoersbegrunnelse.barn_flyttet_ikke_avt_land or pesysData.opphoersbegrunnelse.eps_opph_ikke_avt_land)) {
                        text(
                            bokmal { +"Ifølge våre opplysninger er " + fritekst("ektefellen/partneren/samboeren din") + " bosatt i " + fritekst("bostedsland") + ". Derfor har du ikke lenger rett til ektefelletillegg." },
                            nynorsk { +"Ifølgje våre opplysningar er " + fritekst("ektefelle/partner/sambuar") + " busett i " + fritekst("bostedsland") + ". Da har du ikkje lenger rett til ektefelletillegg. " },
                        )
                    }

                    showIf((pesysData.opphoersbegrunnelse.bruker_flyttet_ikke_avt_land or pesysData.opphoersbegrunnelse.eps_flyttet_ikke_avt_land or pesysData.opphoersbegrunnelse.eps_opph_ikke_avt_land)) {
                        text(
                            bokmal { +"For å ha rett til ektefelletillegg fra 1. juli 2020 må du og ektefellen/samboeren din enten bo i Norge, innenfor EØS-området eller i et annet land Norge har trygdeavtale med. " },
                            nynorsk { +"For å ha rett til ektefelletillegg frå 1. juli 2020 må du og ektefellen/sambuaren din enten bu i Noreg, innanfor EØS-området eller i eit anna land Noreg har trygdeavtale med. " },
                        )
                    }
                }
            }

            showIf((kravarsak.equalTo("sivilstandsendring") and (pe.vedtaksdata_beregningsdata_beregning_beregningbenyttetsivilstand().equalTo("skil") or pe.vedtaksdata_beregningsdata_beregning_beregningbenyttetsivilstand().equalTo("skpa")) and pesysData.opphortEktefelletillegg)) {
                paragraph {
                    text(
                        bokmal { +"Vi har mottatt opplysninger om at du har blitt skilt. Vi har derfor opphørt ektefelletillegget." },
                        nynorsk { +"Vi har fått opplysningar om at du har blitt skilt. Vi har derfor stansa ektefelletillegget ditt." },
                    )
                }
            }

            showIf((kravarsak.equalTo("sivilstandsendring") and pe.vedtaksdata_beregningsdata_beregning_beregningbenyttetsivilstand().equalTo("enke") and pesysData.opphortEktefelletillegg)) {
                paragraph {
                    text(
                        bokmal { +"Vi har mottatt opplysninger om at du har blitt enke/enkemann. Vi har derfor opphørt ektefelletillegget." },
                        nynorsk { +"Vi har fått opplysningar om at du har blitt enkje/enkjemann. Vi har derfor stansa ektefelletillegget ditt." },
                    )
                }
            }

            //TODO: kan vi slå sammen noe her med avslag bt?
            showIf(pesysData.opphortBarnetillegg) {
                paragraph {
                    text(
                        bokmal { +"Vi har opphørt barnetillegget i uføretrygden din for barn født " + fritekst("fødselsdato barnet/barna") + "." },
                        nynorsk { +"Vi har stansa barnetillegget i uføretrygda for barn fødd " + fritekst("fødselsdato barnet/barna") + "." },
                    )
                }

                showIf(pesysData.bt_over_18) {
                    paragraph {
                        text(
                            bokmal { +"For å ha rett til barnetillegg må du forsørge barn under 18 år. Vi har vedtatt at barnetillegget i uføretrygden opphører fordi " + txtBarnetBarnaOpphor + " har fylt 18 år. " },
                            nynorsk { +"For å ha rett til barnetillegg må du forsørgje barn under 18 år. Vi har stansa barnetillegget i uføretrygda fordi " + txtBarnetBarnaOpphor + " har fylt 18 år." },
                        )
                    }
                }

                showIf(pesysData.annen_forld_rett_bt) {
                    paragraph {
                        text(
                            bokmal { +fritekst("slett det som ikke er aktuelt") },
                            nynorsk { +fritekst("slett det som ikke er aktuelt") },
                        )
                    }
                }

                showIf(pesysData.annen_forld_rett_bt) {
                    paragraph {
                        text(
                            bokmal { +"Når " + txtBarnetBarnaOpphor + " blir forsørget av begge foreldrene og begge mottar uføretrygd, skal barnetillegget gis til den som får det høyeste tillegget. Den andre forelderen har rett til et høyere barnetillegg enn det du vil få. Vi har derfor opphørt barnetillegget i uføretrygden din. " },
                            nynorsk { +"Når " + txtBarnetBarnaOpphor + " blir " + txtBarnetBarnaOpphorForsorgaForsorgde + " av begge foreldra og begge får uføretrygd, blir barnetillegget gitt til den som får det høgaste tillegget. Den andre forelderen har rett til eit høgare barnetillegg enn det du vil få. Vi har derfor stansa barnetillegget i uføretrygda di." },
                        )
                    }
                }

                showIf(pesysData.annen_forld_rett_bt) {
                    paragraph {
                        text(
                            bokmal { +"Når " + txtBarnetBarnaOpphor + " blir forsørget av foreldre som ikke bor sammen, blir barnetillegget gitt til den som har samme folkeregistrerte adresse som " + txtBarnetBarnaOpphor + ". Du bor ikke på samme folkeregistrerte adresse som " + txtBarnetBarnaOpphor + ". Vi har derfor opphørt barnetillegget i uføretrygden din." },
                            nynorsk { +"Når " + txtBarnetBarnaOpphor + " blir " + txtBarnetBarnaOpphorForsorgaForsorgde + " av foreldre som ikkje bur saman, blir barnetillegget gitt til den som har same folkeregistrerte adresse som " + txtBarnetBarnaOpphor + ". Du bur ikkje på same folkeregistrerte adresse som " + txtBarnetBarnaOpphor + ". Vi har derfor stansa barnetillegget i uføretrygda di." },
                        )
                    }
                }

                showIf(pesysData.mindre_ett_ar_bt_flt) {
                    paragraph {
                        text(
                            bokmal { +"Det er mulig å flytte barnetillegget fra den ene til den andre forelderen. Det må imidlertid ha gått et år siden barnetillegget ble overført. I ditt tilfelle har det gått ett år og barnetillegget er overført til den andre forelderen. Vi har derfor opphørt barnetillegget i uføretrygden din." },
                            nynorsk { +"Det er mogleg å flytte barnetillegget frå den eine til den andre forelderen. Det må i det minste ha gått eit år sidan barnetillegget blei overført. I ditt tilfelle har det gått eit år og barnetillegget er overført til den andre forelderen. Vi har derfor stansa barnetillegget i uføretrygda di." },
                        )
                    }
                }

                showIf(pesysData.bt_innt_over_1g) {
                    paragraph {
                        text(
                            bokmal { +"Når " + txtBarnetBarnaOpphor + " har inntekt over folketrygdens grunnbeløp på " + grunnbelop.format() + " kroner, har du ikke rett til barnetillegg. " },
                            nynorsk { +"Når " + txtBarnetBarnaOpphor + " har inntekt over grunnbeløpet i folketrygda på " + grunnbelop.format() + " kroner, har du ikkje rett til barnetillegg." },
                        )
                        text(
                            bokmal { +"Det er opplyst at " + txtBarnetBarnaOpphor + " " + txtBarnetBarnaOpphorDittDine + " har inntekt over " + grunnbelop.format() + " kroner. Vi har derfor opphørt barnetillegget i uføretrygden din." },
                            nynorsk { +"Det er opplyst at " + txtBarnetBarnaOpphor + " " + txtBarnetBarnaOpphorDittDine + " har inntekt over " + grunnbelop.format() + " kroner. Vi har derfor stansa barnetillegget i uføretrygda di." },
                        )
                    }
                }
            }

            showIf(pesysData.opphortBarnetillegg and (pesysData.opphoersbegrunnelse.bruker_flyttet_ikke_avt_land or pesysData.opphoersbegrunnelse.barn_flyttet_ikke_avt_land or pesysData.opphoersbegrunnelse.barn_opph_ikke_avt_land)) {
                showIf(pesysData.opphoersbegrunnelse.barn_flyttet_ikke_avt_land or pesysData.opphoersbegrunnelse.barn_opph_ikke_avt_land) {
                    paragraph {
                        text(
                            bokmal { +"Ifølge våre opplysninger er " + txtBarnetBarnaOpphor + " bosatt i " + fritekst("bostedsland") + ".  Derfor har du ikke lenger rett til barnetillegg" },
                            nynorsk { +"Ifølgje våre opplysningar er " + txtBarnetBarnaOpphor + " busett i " + fritekst("bostedsland") + ". Derfor har du ikkje lenger rett til barnetillegg." },
                        )
                    }
                }

                showIf(pesysData.opphoersbegrunnelse.bruker_flyttet_ikke_avt_land) {
                    paragraph {
                        text(
                            bokmal { +"Ifølge våre opplysninger er du bosatt i " + fritekst("bostedsland") + ". Derfor har du ikke lenger rett til barnetillegg." },
                            nynorsk { +"Ifølgje våre opplysningar er du busett i " + fritekst("bostedsland") + ". Da har du ikkje lenger rett til barnetillegg." },
                        )
                    }
                }

                paragraph {
                    text(
                        bokmal { +"For å ha rett til barnetillegg fra 1. juli 2020 må du enten bo i Norge, innenfor EØS-området eller i et annet land Norge har trygdeavtale med " },
                        nynorsk { +"For å ha rett til barnetillegg frå 1. juli 2020 må du enten bu i Noreg, innanfor EØS-området eller i eit anna land Noreg har trygdeavtale med " },
                    )
                    //TODO: teksten under er for barn, trenger vi noe logikk her?
                    text(
                        bokmal { +"må også barnet være bosatt og oppholde seg i Norge, innenfor EØS-området eller et annet land Norge har trygdeavtale med " },
                        nynorsk { +"må også barn vere busett og opphalde seg i Noreg, innanfor EØS-området eller eit anna land Noreg har trygdeavtale med " },
                    )
                    text(
                        bokmal { +"Dette går frem av folketrygdloven § 12-15 som gjelder fra 1.juli 2020." },
                        nynorsk { +"Dette går fram av folketrygdlova § 12-15 som gjeld frå 1. juli 2020" },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopOkt = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopRedusert = false AND PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat(1) = "false" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "sivilstandsendring") THEN      INCLUDE ENDIF
            // showIf((not(pe.vedtaksdata_beregningsdata_beregningufore_belopokt()) and not(pe.vedtaksdata_beregningsdata_beregningufore_belopredusert()) and FUNKSJON_PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat(1).equalTo("false") and kravarsak.notEqualTo("sivilstandsendring"))){
            // TODO: dette ser aldri ut til å ha blitt vist, pga resultat = "false"-sjekk. Skal det være med?
            showIf((not(pe.vedtaksdata_beregningsdata_beregningufore_belopokt()) and not(pe.vedtaksdata_beregningsdata_beregningufore_belopredusert()) and kravarsak.notEqualTo("sivilstandsendring"))) {
                paragraph {
                    text(
                        bokmal { +"Dette får ikke betydning for uføretrygden din, og du vil få utbetalt det samme som før." },
                        nynorsk { +"Dette får ikkje noko å seie for uføretrygda di, og du får utbetalt det same som før." },
                    )
                }
            }

            showIf(not(pesysData.opphortBarnetillegg)) {
                paragraph {
                    showIf(barnetilleggInnvilget) {
                        text(
                            bokmal { +"Barnetillegg kan gis så lenge du forsørger barn. Det gis som et tillegg til uføretrygden din og opphører når barnet fyller 18 år. " },
                            nynorsk { +"Barnetillegg kan bli gitt så lenge du forsørgjer barn. Det blir gitt som eit tillegg til uføretrygda di og blir stansa når barnet ditt fyller 18 år. " },
                        )
                        showIf(kravarsak.equalTo("soknad_bt")) {
                            text(
                                bokmal { +"Du er innvilget barnetillegg fordi du forsørger barn." },
                                nynorsk { +"Du er innvilga barnetillegg fordi du forsørgjer barn." },
                            )
                        }
                    }
                }
            }

            showIf((not(barnetilleggFellesInnvilget) and barnetilleggSerkullInnvilget and (kravarsak.isOneOf("endret_inntekt", "barn_endret_inntekt", "annen_for_end_in", "begge_for_end_in")))) {
                paragraph {
                    text(
                        bokmal { +"Vi har mottatt opplysninger om at inntekten din er endret. Barnetillegget er derfor beregnet på nytt." },
                        nynorsk { +"Vi har fått opplysningar om at inntekta di er endra. Barnetillegg er derfor berekna på nytt." },
                    )
                }
            }

            showIf((barnetilleggFellesInnvilget and kravarsak.isOneOf("endret_inntekt", "barn_endret_inntekt", "annen_for_end_in", "begge_for_end_in", "eps_endret_inntekt"))) {
                paragraph {
                    text(
                        bokmal { +"Vi har mottatt opplysninger om at inntekten deres er endret. Barnetillegget er derfor beregnet på nytt." },
                        nynorsk { +"Vi har fått opplysningar om at inntekta dykkar er endra. Barnetillegg er derfor berekna på nytt." },
                    )
                }
            }

            showIf(kravarsak.equalTo("omgj_etter_klage")) {
                paragraph {
                    text(
                        bokmal { +"Søknaden din er innvilget etter klage og vi anser klagen som ferdig behandlet. Dersom du ønsker å opprettholde klagen, må du gi tilbakemelding til Nav innen 3 uker." },
                        nynorsk { +"Søknaden din er innvilga etter klage, og vi ser det slik at klaga er ferdig behandla. Dersom du ønskjer å halde fast på klaga, må du melde dette tilbake til Nav innan 3 veker." },
                    )
                }
            }

            showIf((instoppholdtype.equalTo("reduksjon_hs") and kravarsak.notEqualTo("instopphold"))) {
                title1 {
                    text(
                        bokmal { +"Utbetaling av uføretrygd for deg som er innlagt på institusjon" },
                        nynorsk { +"Utbetaling av uføretrygd når du er innlagd på institusjon" },
                    )
                }
            }

            showIf((instoppholdtype.equalTo("reduksjon_hs") and (instoppholdanvendt or pe.ut_forsorgeransvar_ingen_er_false() or fasteUtgifterInstopphold.notEqualTo(Kroner(0))))) {
                paragraph {
                    text(
                        bokmal { +"Vi reduserer ikke utbetalingen av uføretrygden din verken i innleggelsesmåneden eller de tre påfølgende månedene når du er innlagt på institusjon. Uføretrygden din blir deretter redusert og skal under oppholdet utgjøre 14 prosent av uføretrygden inntil institusjonsoppholdet avsluttes. Totalt sett skal utbetalingen likevel utgjøre minst 45 prosent av folketrygdens grunnbeløp, før skatt. " },
                        nynorsk { +"Vi reduserer ikkje utbetalinga av uføretrygda di verken i innleggingsmånaden eller dei tre påfølgjande månadene når du er innlagd på institusjon. Uføretrygda di blir deretter redusert og skal under opphaldet utgjere 14 prosent av uføretrygda inntil institusjonsopphaldet blir avslutta. Totalt sett skal utbetalinga likevel utgjere minst 45 prosent av grunnbeløpet i folketrygda før skatt. " },
                    )

                    showIf(ektefelletilleggInnvilget) {
                        text(
                            bokmal { +"Dersom du mottar ektefelletillegg vil dette tillegget også bli redusert. " },
                            nynorsk { +"Dersom du får ektefelletillegg vil dette tillegget også bli redusert. " },
                        )
                    }

                    showIf(gjenlevendetilleggInnvilget) {
                        text(
                            bokmal { +"Dersom du mottar gjenlevendetillegg vil dette tillegget også bli redusert." },
                            nynorsk { +"Dersom du får attlevandetillegg vil dette tillegget også bli redusert." },
                        )
                    }
                }
            }

            showIf((instoppholdtype.equalTo("reduksjon_hs") and (instoppholdanvendt or pe.ut_forsorgeransvar_ingen_er_false() or fasteUtgifterInstopphold.notEqualTo(Kroner(0))))) {
                paragraph {
                    text(
                        bokmal { +"Dersom du har faste og nødvendige utgifter til bolig, kan vi vurdere om uføretrygden din kan reduseres mindre. Du må sende inn dokumentasjon på dine utgifter til Nav. Forsørger du barn" + txtOgEllerEktefelle + " under innleggelsen på institusjonen, vil vi ikke redusere uføretrygden din." },
                        nynorsk { +"Dersom du har faste og nødvendige utgifter til bustad, vil vi vurdere en lågare reduksjon av uføretrygda di. Du må sende inn dokumentasjon på utgiftene dine til Nav. Viss du forsørgjer barn" + txtOgEllerEktefelle + " mens du er lagd inn på institusjonen, reduserer vi ikkje uføretrygda di." },
                    )
                }
            }

            showIf((not(instoppholdanvendt) and pe.ut_forsorgeransvar_ingen_er_true() and fasteUtgifterInstopphold.equalTo(0) and instoppholdtype.equalTo("reduksjon_hs"))) {
                paragraph {
                    text(
                        bokmal { +"Uføretrygden din er lavere enn 45 prosent av folketrygdens grunnbeløp. Du vil derfor ikke få redusert utbetaling av uføretrygden din når du er innlagt på institusjon." },
                        nynorsk { +"Uføretrygda di er lågare enn 45 prosent av grunnbeløpet i folketrygda. Du får derfor ikkje redusert utbetaling av uføretrygda di når du er innlagd på institusjon." },
                    )
                }
            }

            showIf((pe.ut_forsorgeransvar_ingen_er_false() and instoppholdtype.equalTo("reduksjon_hs") and fasteUtgifterInstopphold.equalTo(0))) {
                paragraph {
                    text(
                        bokmal { +"Du forsørger barn" + txtOgEllerEktefelle + " under oppholdet ditt i institusjon. Vi har derfor kommet fram til at utbetalingen din ikke skal reduseres." },
                        nynorsk { +"Du forsørgjer barn " + txtOgEllerEktefelle + " under opphaldet ditt på institusjon. Vi har derfor kome fram til at utbetalinga di derfor ikkje skal reduserast." },
                    )
                }
            }

            showIf((not(instoppholdanvendt) and fasteUtgifterInstopphold.notEqualTo(0) and instoppholdtype.equalTo("reduksjon_hs") and pe.ut_forsorgeransvar_ingen_er_true())) {
                paragraph {
                    text(
                        bokmal { +"Du har dokumentert at du har faste og nødvendige utgifter til bolig under oppholdet ditt på institusjon. Vi har derfor kommet fram til at utbetalingen din ikke skal reduseres." },
                        nynorsk { +"Du har dokumentert at du har faste og nødvendige utgifter til bustad under opphaldet ditt på institusjon. Vi har kome fram til at utbetalinga di derfor ikkje skal reduserast." },
                    )
                }
            }

            showIf((fasteUtgifterInstopphold.notEqualTo(0) and pe.ut_forsorgeransvar_ingen_er_false() and instoppholdtype.equalTo("reduksjon_hs"))) {
                paragraph {
                    text(
                        bokmal { +"Du har dokumentert at du har faste og nødvendige utgifter til bolig, og du forsørger barn" + txtOgEllerEktefelle + " under oppholdet ditt i institusjon. Vi har derfor kommet fram til at utbetalingen din ikke skal reduseres." },
                        nynorsk { +"Du har dokumentert at du har faste og nødvendige utgifter til bustad, og du forsørgjer barn" + txtOgEllerEktefelle + " under opphaldet ditt på institusjon. Vi har derfor kome fram til at utbetalinga di ikkje skal reduserast." },
                    )
                }
            }

            showIf((instoppholdanvendt and fasteUtgifterInstopphold.notEqualTo(0) and instoppholdtype.equalTo("reduksjon_hs"))) {
                paragraph {
                    text(
                        bokmal { +"Du har dokumentert at du har faste og nødvendige utgifter til bolig under oppholdet ditt på institusjon. Vi har derfor kommet fram til at utbetalingen din skal reduseres til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner." },
                        nynorsk { +"Du har dokumentert at du har faste og nødvendige utgifter til bustad under opphaldet ditt på institusjon. Vi har kome fram til at utbetalinga di skal reduserast til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner." },
                    )
                }
            }

            showIf((instoppholdanvendt and instoppholdtype.equalTo("reduksjon_hs") and fasteUtgifterInstopphold.equalTo(0) and pe.ut_forsorgeransvar_ingen_er_true())) {
                paragraph {
                    text(
                        bokmal { +"Du forsørger ikke barn" + txtOgEllerEktefelle + ", og det er ikke dokumentert at du har faste og nødvendige utgifter til bolig under oppholdet ditt på institusjon. Vi har derfor kommet fram til at uføretrygden din skal reduseres til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner." },
                        nynorsk { +"Du forsørgjer ikkje barn " + txtOgEllerEktefelle + ", og det er ikkje dokumentert at du har faste og nødvendige utgifter til bustad under opphaldet ditt på institusjon. Vi har derfor kome fram til at uføretrygda di skal reduserast til " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " kroner." },
                    )
                }
            }

            showIf((instoppholdtype.equalTo("reduksjon_fo") and kravarsak.notEqualTo("instopphold"))) {
                paragraph {
                    text(
                        bokmal { +"Utbetaling av uføretrygd for deg som er under straffegjennomføring" },
                        nynorsk { +"Utbetaling av uføretrygd når du er under straffegjennomføring" },
                    )
                }
            }

            showIf(instoppholdtype.equalTo("reduksjon_fo")) {
                paragraph {
                    text(
                        bokmal { +"Uføretrygden din er redusert fordi du er under straffegjennomføring." },
                        nynorsk { +"Uføretrygda di er redusert fordi du er under straffegjennomføring." },
                    )

                    showIf(((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphreduksjonsperiodeliste_instopphreduksjonsperiode_forsorgeransvar()))) {
                        text(
                            bokmal { +" " },
                            nynorsk { +" " },
                        )
                    }

                    showIf((pe.ut_forsorgeransvar_siste_er_true())) {
                        text(
                            bokmal { +"Da du forsørger barn" },
                            nynorsk { +" Da du forsørgjer barn" },
                        )
                    }

                    showIf(((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_instopphreduksjonsperiodeliste_instopphreduksjonsperiode_forsorgeransvar()) and ektefelletilleggInnvilget)) {
                        text(
                            bokmal { +" og/eller ektefelle" },
                            nynorsk { +" og/eller ektefelle" },
                        )
                    }

                    showIf((pe.ut_forsorgeransvar_siste_er_true())) {
                        text(
                            bokmal { +", vil utbetaling av uføretrygden din reduseres med 50 prosent. " },
                            nynorsk { +", vil utbetalinga av uføretrygda di reduserast med 50 prosent. " },
                        )
                    }
                    text(
                        bokmal { +"Utbetalingen din er redusert fra andre måned etter at straffegjennomføring tok til. Når straffegjennomføring er avsluttet, vil vi ikke lenger redusere uføretrygden din. " },
                        nynorsk { +" Utbetalinga di er redusert frå den andre månaden etter at straffegjennomføringa tok til. Når straffegjennomføringa er avslutta, vil vi ikkje lenger redusere uføretrygda di. " },
                    )

                    showIf(ektefelletilleggInnvilget) {
                        text(
                            bokmal { +"Dersom du mottar ektefelletillegg vil dette tillegget også bli redusert. " },
                            nynorsk { +"Dersom du får ektefelletillegg vil dette tillegget også bli redusert. " },
                        )
                    }

                    showIf(gjenlevendetilleggInnvilget) {
                        text(
                            bokmal { +"Dersom du mottar gjenlevendetillegg vil dette tillegget også bli redusert." },
                            nynorsk { +"Dersom du får attlevandetillegg vil dette tillegget også bli redusert." },
                        )
                    }
                }
            }

            showIf((kravarsak.equalTo("instopphold") and instoppholdtype.equalTo(""))) {
                paragraph {
                    text(
                        bokmal { +"Du er ikke lenger " + fritekst("innlagt på institusjon/under straffegjennomføring") + ", og du får derfor tilbake utbetalingen av uføretrygden." },
                        nynorsk { +"Du er ikkje lenger " + fritekst("innlagt på institusjon/under straffegjennomføring") + ", og du får derfor tilbake utbetalinga av uføretrygda." },
                    )
                }
            }

            showIf(pesysData.opphortEktefelletillegg) {
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter forskrift om overgangsregler ved innføringen av uføretrygd § 8." },
                        nynorsk { +"Vedtaket er gjort etter forskrift om overgangsreglar ved innføringa av uføretrygd § 8." },
                    )
                }
            }

            paragraph {
                showIf(kravarsak.notEqualTo("endring_ifu")) {
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven " + pesysData.hjemler.format(HjemmelFormatter(true)) +"."},
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova " + pesysData.hjemler.format(HjemmelFormatter(true)) +"."},
                    )
                }.orShow {
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven " + pesysData.hjemler.format(HjemmelFormatter(false)) +" og forskrift om uføretrygd fra folketrygden § 2-3."},
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova " + pesysData.hjemler.format(HjemmelFormatter(false)) +" og forskrift om uføretrygd frå folketrygda § 2-3."},
                    )
                }
            }

            title1 {
                text(
                    bokmal { +"Dette er virkningstidspunktet ditt" },
                    nynorsk { +"Dette er verknadstidspunktet ditt" },
                )
            }

            showIf(((virkningstidspunktBegrunnelse).equalTo(""))) {
                paragraph {
                    text(
                        bokmal { +"Uføretrygden din er endret fra " + onsketvirkningsdato.format() + ". Dette kaller vi virkningstidspunktet. " + fritekst("Konkret begrunnelse for fastsatt virkningstidspunkt") + "." },
                        nynorsk { +"Uføretrygda di er endra frå " + onsketvirkningsdato.format() + ". Dette kallar vi verknadstidspunktet. " + fritekst("Konkret begrunnelse for fastsatt virkningstidspunkt") + "." },
                    )
                }
            }.orShowIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevneresultat()).equalTo("oppfylt") and (virkningstidspunktBegrunnelse).equalTo("stdbegr_22_12_1_1"))) {
                paragraph {
                    text(
                        bokmal { +"Du har fått innvilget uføretrygd fra " + onsketvirkningsdato.format() + ". Dette kaller vi virkningstidspunktet. Fram til dette vil du få arbeidsavklaringspenger." },
                        nynorsk { +"Du har fått innvilga uføretrygd frå " + onsketvirkningsdato.format() + ". Dette kallar vi verknadstidspunktet. Fram til dette kjem du til å få arbeidsavklaringspengar." },
                    )
                }
            }.orShowIf(((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevneresultat()).equalTo("oppfylt") and (virkningstidspunktBegrunnelse).equalTo("stdbegr_22_12_1_2"))) {
                paragraph {
                    text(
                        bokmal { +"Du har fått innvilget uføretrygd fra " + onsketvirkningsdato.format() + ". Dette kaller vi virkningstidspunktet. Arbeidsavklaringspengene utbetales fram til " + fritekst("dato for opphør") + " og uføretrygd utbetales for de gjenstående dagene i " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_mndvirkningstidpunkt().format() + "." },
                        nynorsk { +"Du har fått innvilga uføretrygd frå " + onsketvirkningsdato.format() + ". Dette kallar vi verknadstidspunktet. Arbeidsavklaringspengane blir betalte ut fram til " + fritekst("Dato for opphør") + ", og uføretrygd blir betalt ut for dei dagane som er att i " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_mndvirkningstidpunkt().format() + "." },
                    )
                }
            }.orShowIf(((virkningstidspunktBegrunnelse).equalTo("stdbegr_22_12_1_3"))) {
                paragraph {
                    text(
                        bokmal { +"Du har fått innvilget uføretrygd fra " + onsketvirkningsdato.format() + ". Dette kaller vi virkningstidspunktet. Du vil få sykepenger fram til " + fritekst("dato for opphør") + ". I denne måneden får du utbetalt den delen av sykepengene som overstiger uføretrygden." },
                        nynorsk { +"Du har fått innvilga uføretrygd frå " + onsketvirkningsdato.format() + ". Dette kallar vi verknadstidspunktet. Du får sjukepengar fram til " + fritekst("Dato for opphør") + ". I denne månaden får du utbetalt den delen av sjukepengane som overstig uføretrygda." },
                    )
                }
            }.orShowIf(((virkningstidspunktBegrunnelse).equalTo("stdbegr_22_12_1_4"))) {
                paragraph {
                    text(
                        bokmal { +"Du har fått innvilget uføretrygd fra " + onsketvirkningsdato.format() + ". Dette kaller vi virkningstidspunktet. For å ha rett til uføretrygd må du ha fylt 18 år. Du får derfor uføretrygd fra måneden etter at du fyller 18 år." },
                        nynorsk { +"Du har fått innvilga uføretrygd frå " + onsketvirkningsdato.format() + ". Dette kallar vi verknadstidspunktet. For å ha rett til uføretrygd må du ha fylt 18 år. Du får derfor uføretrygd frå månaden etter at du fyljer 18 år." },
                    )
                }
            }.orShowIf(virkningbegrunnelseStdbegr_22_12_1_5) {
                paragraph {
                    text(
                        bokmal { +"Du har fått innvilget uføretrygd fra " + onsketvirkningsdato.format() + ". Dette kaller vi virkningstidspunktet. Vi mottok søknaden din " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Dersom vilkårene for rett til uføretrygd var oppfylt før dette, kan uføretrygden innvilges opptil tre måneder før denne datoen. <FRITEKST>" },
                        nynorsk { +"Du har fått innvilga uføretrygd frå " + onsketvirkningsdato.format() + ". Dette kallar vi verknadstidspunktet. Vi fekk søknaden din " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Dersom vilkåra for rett til uføretrygd var oppfylte før dette, kan vi innvilge uføretrygd opptil tre månader før denne datoen. <FRITEKST>" },
                    )
                }
            }.orShowIf(((virkningstidspunktBegrunnelse).equalTo("stdbegr_22_12_1_12"))) {
                paragraph {
                    text(
                        bokmal { +"Uføretrygden din er endret fra " + onsketvirkningsdato.format() + ". Dette kaller vi virkningstidspunktet. Du vil derfor få ny utbetaling fra og med måneden etter den måneden vilkårene er oppfylt." },
                        nynorsk { +"Uføretrygda di er endra frå " + onsketvirkningsdato.format() + ". Dette kallar vi verknadstidspunktet. Du vil derfor få ny utbetaling frå og med månaden etter den månaden vilkåra er oppfylde." },
                    )
                }
            }.orShowIf(((virkningstidspunktBegrunnelse).equalTo("stdbegr_22_12_1_11"))) {
                paragraph {
                    text(
                        bokmal { +"Uføretrygden din er endret fra " + onsketvirkningsdato.format() + ". Dette kaller vi virkningstidspunktet. Du vil derfor få ny utbetaling fra og med måneden vilkårene er oppfylt. " },
                        nynorsk { +"Uføretrygda di er endra frå " + onsketvirkningsdato.format() + ". Dette kallar vi verknadstidspunktet. Du vil derfor få ny utbetaling frå og med månaden vilkåra er oppfylde." },
                    )
                }
            }.orShowIf(((virkningstidspunktBegrunnelse).equalTo("stdbegr_22_12_1_14"))) {
                paragraph {
                    text(
                        bokmal { +"Virkningstidspunkt for opphør er satt til måneden vilkårene ikke lenger er oppfylt." },
                        nynorsk { +"Verknadstidspunktet for stans er satt til månaden vilkåra ikkje lenger er oppfylde." },
                    )
                }
            }

            showIf((pe.vedtaksdata_kravhode_kravgjelder().notEqualTo("sok_ys"))) {
                showIf(((ieuBegrunnelse).notEqualTo("") or (ifuBegrunnelse).notEqualTo(""))) {
                    title1 {
                        text(
                            bokmal { +"Slik har vi fastsatt uføregraden din" },
                            nynorsk { +"Slik har vi fastsett uføregraden din" },
                        )
                    }
                    paragraph {
                        text(
                            bokmal { +"Vi har sammenliknet inntektsmulighetene dine før og etter at du ble ufør, og vurdert hvor mye inntektsevnen din er varig nedsatt." },
                            nynorsk { +"Vi har samanlikna inntektsmoglegheitene dine før og etter at du blei ufør, og vurdert kor mykje inntektsevna di er varig nedsett." },
                        )
                    }
                }

                showIf(((ifuBegrunnelse).equalTo("stdbegr_12_8_2_1") and (ieuBegrunnelse).notEqualTo("stdbegr_12_8_1_3"))) {
                    paragraph {
                        text(
                            bokmal { +"Inntekten din før du ble ufør er fastsatt til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt().format() + " kroner. " + fritekst("begrunnelse for fastsatt IFU") + "." },
                            nynorsk { +"Inntekta di før du blei ufør er fastsett til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt().format() + " kroner. " + fritekst("Begrunnelse for fastsatt IFU") + "." },
                        )
                        showIf(oifuMerEnnIfu) {
                            text(
                                bokmal { +" Oppjustert til virkningstidspunktet tilsvarer dette en inntekt på " + oifuKroner.format() + " kroner." },
                                nynorsk { +" Oppjustert til verknadstidspunktet svarer dette til ei inntekt på " + oifuKroner.format() + " kroner." },
                            )
                        }
                    }
                }

                showIf(((ifuBegrunnelse).equalTo("stdbegr_12_8_2_3") and (ieuBegrunnelse).notEqualTo("stdbegr_12_8_1_3"))) {
                    paragraph {
                        text(
                            bokmal { +"Du hadde begrenset yrkesaktivitet og inntekt før du ble ufør. Inntekten din før du ble ufør er fastsatt til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt().format() + " kroner. Dette er for å garantere deg et minstenivå på inntekt før uførhet. Dette minstenivået skal tilsvare 3,3 ganger folketrygdens grunnbeløp." },
                            nynorsk { +"Du hadde avgrensa yrkesaktivitet og inntekt før du blei ufør. Inntekta di før du blei ufør, er fastsett til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt().format() + " kroner. Dette er for å garantere deg eit minstenivå på inntekt før uførleik. Dette minstenivået skal svare til 3,3 gonger grunnbeløpet." },
                        )
                        showIf(oifuMerEnnIfu) {
                            text(
                                bokmal { +" Oppjustert til virkningstidspunktet tilsvarer dette en inntekt på " + oifuKroner.format() + " kroner." },
                                nynorsk { +" Oppjustert til verknadstidspunktet svarer dette til ei inntekt på " + oifuKroner.format() + " kroner." },
                            )
                        }
                    }
                }

                showIf(((ifuBegrunnelse).equalTo("stdbegr_12_8_2_5") and (ifuBegrunnelse).notEqualTo("stdbegr_12_8_1_3"))) {
                    paragraph {
                        text(
                            bokmal { +"Du hadde begrenset yrkesaktivitet og inntekt før du ble ufør. Inntekten din før du ble ufør er fastsatt til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt().format() + " kroner. Dette er for å garantere deg et minstenivå på inntekt før uførhet. Dette minstenivået skal tilsvare 3,5 ganger folketrygdens grunnbeløp." },
                            nynorsk { +"Du hadde avgrensa yrkesaktivitet og inntekt før du blei ufør. Inntekta di før du blei ufør, er fastsett til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt().format() + " kroner. Dette er for å garantere deg eit minstenivå på inntekt før uførleik. Dette minstenivået skal svare til 3,5 gonger grunnbeløpet." },
                        )
                        showIf(oifuMerEnnIfu) {
                            text(
                                bokmal { +" Oppjustert til virkningstidspunktet tilsvarer dette en inntekt på " + oifuKroner.format() + " kroner." },
                                nynorsk { +" Oppjustert til verknadstidspunktet svarer dette til ei inntekt på " + oifuKroner.format() + " kroner." },
                            )
                        }
                    }
                }

                showIf((((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_unguforresultat()).equalTo("oppfylt")) and ((ifuBegrunnelse).equalTo("stdbegr_12_8_2_4")) and ((ieuBegrunnelse).notEqualTo("stdbegr_12_8_1_3")))) {
                    paragraph {
                        text(
                            bokmal { +"Inntekten din før du ble ufør er fastsatt til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt().format() + " kroner. Vi har innvilget deg rettighet som ung ufør, og inntekten din før du ble ufør skal derfor tilsvare minst 4,5 ganger grunnbeløpet." },
                            nynorsk { +"Inntekta di før du blei ufør, er fastsett til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt().format() + " kroner. Vi har innvilga deg rettar som ung ufør, og inntekta di før du blei ufør skal derfor svare til minst 4,5 gonger grunnbeløpet." },
                        )
                        showIf(oifuMerEnnIfu) {
                            text(
                                bokmal { +" Oppjustert til virkningstidspunktet tilsvarer dette en inntekt på " + oifuKroner.format() + " kroner." },
                                nynorsk { +" Oppjustert til verknadstidspunktet svarer dette til ei inntekt på " + oifuKroner.format() + " kroner." },
                            )
                        }
                    }
                }

                showIf((((ifuBegrunnelse).notEqualTo("") or (ieuBegrunnelse).notEqualTo("")) and uforegradFraBeregning.equalTo(100) and (ieuInntekt).equalTo(0) and (ieuBegrunnelse).notEqualTo("stdbegr_12_8_1_3") and (ifuBegrunnelse).notEqualTo("stdbegr_12_8_2_2") and (ifuBegrunnelse).notEqualTo("stdbegr_12_8_2_9"))) {
                    paragraph {
                        text(
                            bokmal { +"Du har ikke inntekt i dag, og vi har derfor fastsatt uføregraden din til " + uforegradFraBeregning.format() + " prosent." },
                            nynorsk { +"Du har ikkje inntekt i dag, og vi har derfor fastsett uføregraden din til " + uforegradFraBeregning.format() + " prosent." },
                        )
                    }
                }

                showIf((((ifuBegrunnelse).notEqualTo("") or (ieuBegrunnelse).notEqualTo("")) and ((uforegradFraBeregning.greaterThan(0) and uforegradFraBeregning.lessThan(100)) or (uforegradFraBeregning.equalTo(100) and (ieuInntekt).greaterThan(0))) and (ieuBegrunnelse).notEqualTo("stdbegr_12_8_1_3") and (ifuBegrunnelse).notEqualTo("stdbegr_12_8_2_2") and (ifuBegrunnelse).notEqualTo("stdbegr_12_8_2_9"))) {
                    paragraph {
                        text(
                            bokmal { +"Du har en inntekt på " + ieuInntekt.format() + " kroner, og vi har derfor fastsatt uføregraden din til " + uforegradFraBeregning.format() + " prosent." },
                            nynorsk { +"Du har ei inntekt på " + ieuInntekt.format() + " kroner, og vi har derfor fastsett uføregraden din til " + uforegradFraBeregning.format() + " prosent." },
                        )
                    }
                }

                showIf(((ifuBegrunnelse).equalTo("stdbegr_12_8_2_9") or (ifuBegrunnelse).equalTo("stdbegr_12_8_2_2"))) {
                    paragraph {
                        text(
                            bokmal { +"Vi har fastsatt inntekten din før du ble ufør til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt().format() + " kroner. Inntekten din før du ble ufør er fastsatt ut fra stillingsandelen din, og forventet inntekt på " + ieuInntekt.format() + " kroner. Inntekten din etter at du ble ufør er derfor fastsatt til " + ieuInntekt.format() + " kroner og uføregraden din blir " + uforegradFraBeregning.format() + " prosent." },
                            nynorsk { +"Vi har fastsett inntekta di før du blei ufør til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt().format() + " kroner. Inntekta di før du blei ufør, er fastsett ut frå stillingsdelen din og forventa inntekt på " + ieuInntekt.format() + " kroner. Inntekta di etter at du blei ufør, er derfor fastsett til " + ieuInntekt.format() + " kroner, og uføregraden din blir " + uforegradFraBeregning.format() + " prosent." },
                        )
                    }
                }
            }

            showIf((uforegradFraBeregning.equalTo(100) and utbetalingsgrad.equalTo(100) and kravarsak.isNotAnyOf("soknad_bt", "instopphold"))) {
                title1 {
                    text(
                        bokmal { +"Skal du kombinere uføretrygd og inntekt?" },
                        nynorsk { +"Skal du kombinere uføretrygd og inntekt?" },
                    )
                }
            }

            showIf((((uforegradFraBeregning.lessThan(100) and uforegradFraBeregning.greaterThan(0)) or (utbetalingsgrad.lessThan(uforegradFraBeregning))) and kravarsak.isNotAnyOf("soknad_bt", "instopphold"))) {
                title1 {
                    text(
                        bokmal { +"For deg som kombinerer uføretrygd og inntekt" },
                        nynorsk { +"For deg som kombinerer uføretrygd og inntekt" },
                    )
                }
            }

            showIf((utbetalingsgrad.equalTo(uforegradFraBeregning) and kravarsak.isNotAnyOf("soknad_bt", "instopphold"))) {
                paragraph {
                    text(
                        bokmal { +"Du har mulighet til å ha inntekt ved siden av uføretrygden din. Det lønner seg å jobbe, fordi inntekt og uføretrygd alltid vil være høyere enn uføretrygd alene." },
                        nynorsk { +"Det er mogleg for deg å ha inntekt ved sida av uføretrygda di. Det lønner seg å jobbe fordi inntekt og uføretrygd alltid vil vere høgare enn uføretrygd åleine." },
                    )
                }
            }

            showIf((utbetalingsgrad.lessThan(uforegradFraBeregning) and kravarsak.isNotAnyOf("soknad_bt", "instopphold"))) {
                paragraph {
                    text(
                        bokmal { +"Utbetalingen av uføretrygden din er redusert fordi du har inntekt utover inntektsgrensen. Det lønner seg likevel å jobbe, fordi inntekt og uføretrygd alltid vil være høyere enn uføretrygd alene." },
                        nynorsk { +"Utbetalinga av uføretrygda di er redusert fordi du har inntekt utover inntektsgrensa. Det lønner seg å jobbe, fordi inntekt og uføretrygd alltid er høgare enn uføretrygd åleine." },
                    )
                }
            }
            //TODO: en del felles nedover som not bt and not inst
            showIf((uforegradFraBeregning.equalTo(100) and kravarsak.isNotAnyOf("soknad_bt", "instopphold"))) {
                showIf((belopsgrense.notEqualTo(grunnbelop) and belopsgrense.notEqualTo(60000) and ieuInntekt.equalTo(0))) {
                    paragraph {
                        text(
                            bokmal { +"Du kan ha en årlig inntekt på 40 prosent av folketrygdens grunnbeløp, uten at uføretrygden din blir redusert. I dag er dette " + pe.ut_inntektsgrense_faktisk().format() + ". Dette er inntektsgrensen din." },
                            nynorsk { +"Du kan ha ei årleg inntekt på 40 prosent av grunnbeløpet i folketrygda utan at uføretrygda di blir redusert. I dag er dette " + pe.ut_inntektsgrense_faktisk().format() + ". Dette er inntektsgrensa di." },
                        )
                    }
                }

                showIf(belopsgrense.equalTo(60000)) {
                    paragraph {
                        text(
                            bokmal { +"Du kan ha en årlig inntekt på 60 000 kroner uten at uføretrygden din blir redusert. Dette er inntektsgrensen din." },
                            nynorsk { +"Du kan ha ei årleg inntekt på 60 000 kroner utan at uføretrygda di blir redusert. Dette er inntektsgrensa di." },
                        )
                    }
                }
            }

            showIf((grunnbelop.equalTo(belopsgrense) and kravarsak.isNotAnyOf("soknad_bt", "instopphold"))) {
                paragraph {
                    text(
                        bokmal { +"Du kan ha en årlig inntekt på folketrygdens grunnbeløp fordi du er i varig tilrettelagt arbeid, uten at uføretrygden din blir redusert. I dag er dette " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense().format() + ". Dette er inntektsgrensen din." },
                        nynorsk { +"Du kan ha ei årleg inntekt på grunnbeløpet i folketrygda mens du er i varig tilrettelagt arbeid utan at uføretrygda di blir redusert. I dag er dette " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense().format() + ". Dette er inntektsgrensa di." },
                    )
                }
            }

            showIf((belopsgrense.notEqualTo(60000) and belopsgrense.notEqualTo(grunnbelop) and (uforegradFraBeregning.lessThan(100) and uforegradFraBeregning.greaterThan(0)) or ((ieuInntekt).greaterThan(0) and uforegradFraBeregning.equalTo(100)) and kravarsak.isNotAnyOf("soknad_bt", "instopphold"))) {
                paragraph {
                    text(
                        bokmal { +"Vi har lagt til grunn at du framover skal ha en inntekt på " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_oieu().format() + " per år. Du kan i tillegg ha en årlig inntekt på 40 prosent av folketrygdens grunnbeløp, uten at uføretrygden din blir redusert. Inntektsgrensen din blir derfor " + pe.ut_inntektsgrense_faktisk().format() + "." },
                        nynorsk { +"Vi har lagt til grunn at du framover skal ha ei inntekt på " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_oieu().format() + " per år. Du kan i tillegg ha ei årleg inntekt på 40 prosent av grunnbeløpet i folketrygda utan at uføretrygda di blir redusert. Inntektsgrensa di blir derfor " + pe.ut_inntektsgrense_faktisk().format() + "." },
                    )
                }
            }

            showIf((belopsgrense.equalTo(60000) and uforegradFraBeregning.greaterThan(0) and uforegradFraBeregning.lessThan(100) and kravarsak.isNotAnyOf("soknad_bt", "instopphold"))) {
                paragraph {
                    text(
                        bokmal { +"Vi har lagt til grunn at du framover skal ha en inntekt på " + pe.ut_inntektsgrense_faktisk_minus_60000().format() + " per år. Du kan i tillegg ha en årlig inntekt på 60 000 kroner, uten at uføretrygden din blir redusert. Inntektsgrensen din blir derfor " + pe.ut_inntektsgrense_faktisk().format() + "." },
                        nynorsk { +"Vi har lagt til grunn at du framover skal ha ei inntekt på " + pe.ut_inntektsgrense_faktisk_minus_60000().format() + " per år. Du kan i tillegg ha ei årleg inntekt på 60 000 kroner utan at uføretrygda di blir redusert. Inntektsgrensa di blir derfor " + pe.ut_inntektsgrense_faktisk().format() + "." },
                    )
                }
            }

            showIf((kravarsak.isNotAnyOf("soknad_bt", "instopphold"))) {
                paragraph {
                    text(
                        bokmal { +"Vi bruker en fastsatt prosentandel når vi justerer uføretrygden din ut fra inntekt. Denne prosentandelen kaller vi kompensasjonsgrad. " },
                        nynorsk { +"Vi bruker ein fastsett prosentdel når vi justerer uføretrygda di ut frå inntekt. Denne prosentdelen kallar vi kompensasjonsgrad. " },
                    )
                    text(
                        bokmal { +"For deg utgjør kompensasjonsgraden " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_kompensasjonsgrad().format() + " prosent. Det er bare den delen av inntekten din som overstiger " + pe.ut_inntektsgrense_faktisk().format() + " kroner, som vi justerer uføretrygden din ut fra. Det betyr at et beløp som tilsvarer " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_kompensasjonsgrad().format() + " prosent av den inntekten du har over " + pe.ut_inntektsgrense_faktisk().format() + " kroner trekkes fra uføretrygden din. " },
                        nynorsk { +"For deg utgjer kompensasjonsgraden " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_kompensasjonsgrad().format() + " prosent. Det er berre den delen av inntekta di som overstig " + pe.ut_inntektsgrense_faktisk().format() + " kroner, som vi justerer uføretrygda di ut frå. Det betyr at eit beløp som svarer til " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_kompensasjonsgrad().format() + " prosent av inntekta du har over " + pe.ut_inntektsgrense_faktisk().format() + " kroner blir trekt frå uføretrygda di. " },
                    )

                    showIf(gjenlevendetilleggInnvilget) {
                        text(
                            bokmal { +"Tjener du mer enn inntektsgrensen din, reduserer vi gjenlevendetillegget ditt med samme prosent som vi reduserer uføretrygden din med." },
                            nynorsk { +"Tener du meir enn inntektsgrensa di, reduserer vi attlevandetillegget ditt med same prosent som vi reduserer uføretrygda di med." },
                        )
                    }
                }
            }

            showIf(
                (utbetalingsgrad.lessThan(uforegradFraBeregning)
                        and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_forventetinntekt().greaterThan(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense())
                        and kravarsak.isNotAnyOf("soknad_bt", "instopphold"))
            ) {
                includePhrase(TBU2361_Generated(pe))
            }

            showIf((utbetalingsgrad.lessThan(uforegradFraBeregning) and pe.vedtaksdata_beregningsdata_beregningufore_belopredusert() and kravarsak.isNotAnyOf("soknad_bt", "instopphold"))) {
                includePhrase(TBU2362_Generated)
            }

            showIf((utbetalingsgrad.lessThan(uforegradFraBeregning) and pe.vedtaksdata_beregningsdata_beregningufore_belopokt() and kravarsak.isNotAnyOf("soknad_bt", "instopphold"))) {
                includePhrase(TBU2363_Generated)
            }

            showIf((utbetalingsgrad.lessThan(uforegradFraBeregning) and kravarsak.isNotAnyOf("soknad_bt", "instopphold"))) {
                paragraph {
                    text(
                        bokmal { +"Ut fra den årlige inntekten din vil uføretrygden utgjøre " + pe.ut_nettoakk_pluss_nettorestar().format() + " kroner." },
                        nynorsk { +"På bakgrunn av den innmelde inntekta di utgjer uføretrygda di " + pe.ut_nettoakk_pluss_nettorestar().format() + " kroner." },
                    )

                    showIf((not(FUNKSJON_FF_CheckIfFirstDayAndMonthOfYear(pe.vedtaksdata_virkningfom())))) {
                        text(
                            bokmal { +" Hittil i år har du fått utbetalt " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_nettoakk().format() + " kroner." },
                            nynorsk { +" Hittil i år har du fått utbetalt " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_nettoakk().format() + " kroner." },
                        )
                    }
                    text(
                        bokmal { +" Du har derfor rett til en utbetaling av uføretrygd på " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_netto().format() + " kroner per måned for resten av året." },
                        nynorsk { +" Du har derfor rett til ei utbetaling av uføretrygd på " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_netto().format() + " kroner per månad for resten av kalenderåret." },
                    )
                }
            }

            showIf((kravarsak.isNotAnyOf("soknad_bt", "instopphold"))) {
                paragraph {
                    text(
                        bokmal { +"Blir uføretrygden din redusert på grunn av inntekt beholder du likevel uføregraden din på " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().format() + " prosent. Du får utbetalt hele uføretrygden igjen dersom du tjener mindre enn inntektsgrensen din." },
                        nynorsk { +"Blir uføretrygda di redusert på grunn av inntekt beheld du likevel uføregraden din på " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().format() + " prosent. Du får utbetalt heile uføretrygda att dersom du tener mindre enn inntektsgrensa di." },
                    )
                }
            }

            showIf((utbetalingsgrad.equalTo(uforegradFraBeregning) and kravarsak.isNotAnyOf("soknad_bt", "instopphold"))) {
                title1 {
                    text(
                        bokmal { +"Du må melde fra om eventuell inntekt" },
                        nynorsk { +"Du må melde frå om eventuell inntekt" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Dersom du er i jobb eller har planer om å jobbe, må du melde fra om eventuelle endringer i inntekten din. Det er viktig at du melder fra så tidlig som mulig, slik at du får riktig utbetaling av uføretrygd. Dette kan du gjøre under menyvalget " + quoted("uføretrygd") + " når du logger deg inn på $NAV_URL. Her kan du legge inn hvor mye du forventer å tjene i løpet av året. Du vil da kunne se hvor mye du vil få utbetalt i uføretrygd ved siden av inntekten din." },
                        nynorsk { +"Dersom du er i jobb eller har planar om å jobbe, må du melde frå om eventuelle endringar i inntekta di. Det er viktig at du melder frå så tidleg som råd, slik at du får rett utbetaling av uføretrygd. Dette kan du gjere under menyvalet " + quoted("uføretrygd") + " når du logger deg inn på $NAV_URL. Her kan du leggje inn kor mykje du forventar å tene i løpet av året. Du vil då kunne sjå kor mykje du kjem til å få betalt ut i uføretrygd ved sida av inntekta di." },
                    )
                }
            }

            showIf((utbetalingsgrad.lessThan(uforegradFraBeregning) and kravarsak.isNotAnyOf("soknad_bt", "instopphold"))) {
                title1 {
                    text(
                        bokmal { +"Du må melde fra om endringer i inntekten" },
                        nynorsk { +"Du må melde frå om endringar i inntekta" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Du kan melde fra om inntektsendringer under menyvalget " + quoted("uføretrygd") + " når du logger deg inn på $NAV_URL. Her kan du legge inn endringer i den forventede årlige inntekten, og se hva dette betyr for utbetalingen av uføretrygden din. For at du skal få en jevn utbetaling av uføretrygden, er det viktig at du melder fra om inntektsendringer så tidlig som mulig." },
                        nynorsk { +"Du kan melde frå om inntektsendringar under menyvalget " + quoted("uføretrygd") + " når du logger deg inn på $NAV_URL. Her kan du leggje inn endringar i den forventa årlege inntekta og sjå kva dette har å seie for utbetalinga av uføretrygda di. For at du skal få ei jamn utbetaling av uføretrygda er det viktig at du melder frå om inntektsendringar så tidleg som mogleg." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Vi gjør oppmerksom på at det ikke utbetales uføretrygd når inntekten din utgjør mer enn 80 prosent av inntekten du hadde før du ble ufør, det vil si " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektstak().format() + " per år. Inntekten er justert opp til dagens verdi." },
                        nynorsk { +"Vi gjer merksam på at det ikkje blir utbetalt uføretrygd når inntekta di utgjer meir enn 80 prosent av inntekta du hadde før du blei ufør, det vil seie " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektstak().format() + " per år. Inntekta er justert opp til dagens verdi." },
                    )
                }
            }

            showIf((utbetalingsgrad.lessThan(uforegradFraBeregning) and kravarsak.isNotAnyOf("soknad_bt", "instopphold"))) {
                includePhrase(TBU2280_Generated(pe))
            }

            showIf((barnetilleggInnvilget and pe.vedtaksdata_kravhode_sokerbt())) {
                title1 {
                    text(
                        bokmal { +"Slik påvirker inntekt barnetillegget ditt " },
                        nynorsk { +"Slik verkar inntekt inn på barnetillegget ditt" },
                    )
                }
            }

            //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) AND PE_Vedtaksdata_Kravhode_SokerBT = true AND ((PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB) OR (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)) AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoForReduksjonBT > PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoEtterReduksjonBT AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoEtterReduksjonBT <> 0 ) THEN      INCLUDE ENDIF
            showIf(
                (barnetilleggInnvilget and pe.vedtaksdata_kravhode_sokerbt() and ((pe.vedtaksdata_beregningsdata_beregningufore_belopsendring_barnetilleggfellesyk_belopgammelbtfb().notEqualTo(pe.vedtaksdata_beregningsdata_beregningufore_belopsendring_barnetilleggfellesyk_belopnybtfb())) or (pe.vedtaksdata_beregningsdata_beregningufore_belopsendring_barnetilleggserkullyk_belopgammelbtsb().notEqualTo(pe.vedtaksdata_beregningsdata_beregningufore_belopsendring_barnetilleggserkullyk_belopnybtsb()))) and kravarsak.notEqualTo("soknad_bt") and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoforreduksjonbt()
                    .greaterThan(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt()) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt().notEqualTo(0))
            ) {
                paragraph {
                    text(
                        bokmal { +"Barnetillegget i uføretrygden din er endret fordi " + fritekst("årsak til endring") + "." },
                        nynorsk { +"Barnetillegget i uføretrygda di er endra fordi " + fritekst("årsak til endring") + "." },
                    )
                }
            }

            showIf(
                not(
                    (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoforreduksjonbt().greaterThan(0)
                            and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt().equalTo(0))
                )
            ) {

                showIf(((not(barnetilleggFellesInnvilget) and barnetilleggSerkullInnvilget))) {
                    includePhrase(TBU2338_Generated(pe))
                }

                showIf(barnetilleggFellesInnvilget) {
                    includePhrase(TBU2339_Generated(pe))
                }

                showIf(barnetilleggInnvilget) {
                    paragraph {
                        text(
                            bokmal { +"Endringer i inntekten din" },
                            nynorsk { +"Endringar i inntekta di" },
                        )

                        showIf(barnetilleggFellesInnvilget) {
                            text(
                                bokmal { +" og til ektefellen, partneren eller samboeren din " },
                                nynorsk { +" og til ektefella, partnaren eller sambuaren din " },
                            )
                        }

                        text(
                            bokmal { +"kan ha betydning for barnetillegget ditt. Du kan enkelt melde fra om inntektsendringer under menyvalget «uføretrygd» på $NAV_URL." },
                            nynorsk { +"kan ha betydning for barnetillegget ditt. Du kan enkelt melde frå om inntektsendringar under menyvalet «uføretrygd» på $NAV_URL." },
                        )
                    }
                }

                showIf(barnetilleggFellesInnvilget) {
                    paragraph {
                        text(
                            bokmal { +"Inntekten din er " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbrukersinntekttilavkortning().format() + " kroner og inntekten til ektefellen, partneren eller samboeren din er " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbinntektannenforelder().format() + " kroner. " },
                            nynorsk { +"Inntekta di er " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbrukersinntekttilavkortning().format() + " kroner, og inntekta til ektefella, partnaren eller sambuaren din er " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbinntektannenforelder().format() + " kroner. " },
                        )

                        showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbelopfratrukketannenforeldersinntekt().greaterThan(0))) {
                            text(
                                bokmal { +"Folketrygdens grunnbeløp på inntil " + grunnbelop.format() + " kroner er holdt utenfor inntekten til ektefellen, partneren eller samboeren din. " },
                                nynorsk { +"Grunnbeløpet i folketrygda på inntil " + grunnbelop.format() + " kroner er halde utanfor inntekta til ektefella, partnaren eller sambuaren din. " }
                            )
                        }

                        showIf(((not(barnetilleggSerkullInnvilget) and btFellesJusteringsbelopPerAr0 and not(btFellesNetto0)))) {
                            text(
                                bokmal { +"Til sammen er inntektene " + pe.ut_btfb_inntekt_hoyere_lavere() + " enn fribeløpet ditt på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop().format() + " kroner. Barnetillegget ditt er derfor " },
                                nynorsk { +"Til saman er inntektene " + pe.ut_btfb_inntekt_hoyere_lavere() + " enn fribeløpet ditt på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop().format() + " kroner. Barnetillegget ditt er derfor " },
                            )
                        }

                        showIf(((not(barnetilleggSerkullInnvilget) and btFellesJusteringsbelopPerAr0 and not(btFellesNetto0) and btFellesFradrag0))) {
                            text(
                                bokmal { +"ikke " },
                                nynorsk { +"ikkje " },
                            )
                        }

                        showIf(((not(barnetilleggSerkullInnvilget) and btFellesJusteringsbelopPerAr0 and not(btFellesNetto0)))) {
                            text(
                                bokmal { +"redusert. " },
                                nynorsk { +"redusert. " },
                            )
                        }

                        showIf(((btFellesJusteringsbelopPerAr.notEqualTo(0) and not(barnetilleggSerkullInnvilget)))) {
                            text(
                                bokmal { +"Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget. " },
                                nynorsk { +"Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget. " },
                            )
                        }

                        showIf(((btFellesJusteringsbelopPerAr.notEqualTo(0) and not(barnetilleggSerkullInnvilget) and btFellesNetto0))) {
                            text(
                                bokmal { +"Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året. " },
                                nynorsk { +"Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året. " },
                            )
                        }
                    }
                }

                showIf((barnetilleggSerkullInnvilget and not(barnetilleggFellesInnvilget))) {
                    paragraph {
                        showIf(((btSerkullNetto.greaterThan(0) and btSerkullJusteringsbelopPerAr0 and pe.ut_btsbinnvilget_ikke_btfbinnvilget()))) {
                            text(
                                bokmal { +"Inntekten din på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().format() + " kroner er " + pe.ut_btsb_inntekt_hoyere_lavere() + " enn fribeløpet ditt på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop().format() + " kroner. Barnetillegget ditt er derfor " },
                                nynorsk { +"Inntekta di på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().format() + " kroner er " + pe.ut_btsb_inntekt_hoyere_lavere() + " enn fribeløpet ditt på " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop().format() + " kroner. Barnetillegget ditt er derfor " },
                            )
                        }

                        showIf(((btSerkullNetto.greaterThan(0) and btSerkullJusteringsbelopPerAr0 and pe.ut_btsbinnvilget_ikke_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().lessThanOrEqual(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop())))) {
                            text(
                                bokmal { +"ikke " },
                                nynorsk { +"ikkje " },
                            )
                        }

                        showIf(((btSerkullNetto.greaterThan(0) and btSerkullJusteringsbelopPerAr0 and pe.ut_btsbinnvilget_ikke_btfbinnvilget()))) {
                            text(
                                bokmal { +"redusert ut fra inntekt. " },
                                nynorsk { +"redusert ut frå inntekt. " },
                            )
                        }

                        showIf(((btSerkullNetto0 and btSerkullJusteringsbelopPerAr0 and pe.ut_btsbinnvilget_ikke_btfbinnvilget()))) {
                            text(
                                bokmal { +"Inntekten din er " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().format() + " kroner. " },
                                nynorsk { +"Inntekta di er " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().format() + " kroner. " },
                            )
                        }

                        showIf(((pe.ut_btsbinnvilget_og_justbeloppa()))) {
                            text(
                                bokmal { +"Inntekten din er " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().format() + " kroner. Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget. " },
                                nynorsk { +"Inntekta di er " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().format() + " kroner. Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget. " },
                            )

                            showIf((btSerkullNetto0)) {
                                text(
                                    bokmal { +"Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året." },
                                    nynorsk { +"Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året." },
                                )
                            }
                        }
                    }
                }

                //TODO: tror masse kan slås sammen her
                showIf((pe.ut_tbu1286_del1() or pe.ut_tbu1286_del2() or pe.ut_tbu1286_del3())) {
                    paragraph {

                        showIf((pe.ut_tbu1286_del1() and btSerkullNetto.notEqualTo(0) and btSerkullJusteringsbelopPerAr0)) {
                            text(
                                bokmal { +"Inntekten din er " + pe.ut_btsb_inntekt_hoyere_lavere() + " enn " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop().format() + " kroner, som er fribeløpet for barnetillegget til " + pe.ut_barnet_barna_serkull() + " som ikke bor sammen med begge foreldrene. " },
                                nynorsk { +"Inntekta di er " + pe.ut_btsb_inntekt_hoyere_lavere() + " enn " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop().format() + " kroner, som er fribeløpet for barnetillegget til " + pe.ut_barnet_barna_serkull() + " som ikkje bur saman med begge foreldra. " },
                            )
                        }

                        showIf((pe.ut_tbu1286_del1() and btSerkullNetto.notEqualTo(0) and ((btSerkullFradrag.greaterThan(0) and btFellesFradrag0) or (btSerkullFradrag0 and btFellesFradrag.greaterThan(0))) and btSerkullJusteringsbelopPerAr0)) {
                            text(
                                bokmal { +"Dette barnetillegget er derfor " + pe.ut_ikke() + "redusert ut fra inntekt. " },
                                nynorsk { +"Dette barnetillegget er derfor " + pe.ut_ikke() + "redusert ut frå inntekt. " },
                            )
                        }

                        showIf((pe.ut_tbu1286_del1() and not(btFellesNetto0) and btFellesJusteringsbelopPerAr0)) {
                            text(
                                bokmal { +"Til sammen er " },
                                nynorsk { +"Til saman er " },
                            )
                        }

                        showIf((pe.ut_tbu1286_del1() and not(btFellesNetto0) and ((btSerkullFradrag0 and btFellesFradrag0) or (btSerkullFradrag.greaterThan(0) and btFellesFradrag.greaterThan(0))) and btFellesJusteringsbelopPerAr0)) {
                            text(
                                bokmal { +"også " },
                                nynorsk { +"også " },
                            )
                        }

                        showIf((pe.ut_tbu1286_del1() and not(btFellesNetto0) and btFellesJusteringsbelopPerAr0)) {
                            text(
                                bokmal { +"inntektene til deg og ektefellen, partneren eller samboeren din " + pe.ut_btfb_inntekt_hoyere_lavere() + " enn " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop().format() + " kroner, som er fribeløpet for barnetillegget til " + pe.ut_barnet_barna_felles() + " som bor med begge sine foreldre. " },
                                nynorsk { +"inntektene til deg og ektefella, partnaren eller sambuaren din " + pe.ut_btfb_inntekt_hoyere_lavere() + " enn " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop().format() + " kroner, som er fribeløpet for barnetillegget til " + pe.ut_barnet_barna_felles() + " som bur saman med begge foreldra sine. " },
                            )
                        }

                        showIf((pe.ut_tbu1286_del1() and ((btSerkullFradrag.greaterThan(0) and btFellesFradrag0) or (btSerkullFradrag0 and btFellesFradrag.greaterThan(0))) and not(btFellesNetto0) and btFellesJusteringsbelopPerAr0)) {
                            text(
                                bokmal { +"Dette barnetillegget er derfor " },
                                nynorsk { +"Dette barnetillegget er derfor " },
                            )
                        }

                        showIf((pe.ut_tbu1286_del1() and btFellesNetto.equalTo(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbbrutto()) and ((btSerkullFradrag.greaterThan(0) and btFellesFradrag0) or (btSerkullFradrag0 and btFellesFradrag.greaterThan(0))) and not(btFellesNetto0) and btFellesJusteringsbelopPerAr0)) {
                            text(
                                bokmal { +"ikke " },
                                nynorsk { +"ikkje " },
                            )
                        }

                        showIf((pe.ut_tbu1286_del1() and ((btSerkullFradrag.greaterThan(0) and btFellesFradrag0) or (btSerkullFradrag0 and btFellesFradrag.greaterThan(0))) and not(btFellesNetto0) and btFellesJusteringsbelopPerAr0)) {
                            text(
                                bokmal { +"redusert ut fra inntekt. " },
                                nynorsk { +"redusert ut frå inntekt. " },
                            )
                        }

                        showIf(
                            (pe.ut_tbu1286_del1() and not(btFellesNetto0) and btSerkullNetto.notEqualTo(0) and ((btSerkullFradrag0 and btFellesFradrag0) or (btSerkullFradrag.greaterThan(0) and btFellesFradrag.greaterThan(0))) and btSerkullJusteringsbelopPerAr
                                .equalTo(0) and btFellesJusteringsbelopPerAr0)
                        ) {
                            text(
                                bokmal { +"Barnetilleggene er derfor" },
                                nynorsk { +"Desse barnetillegga er derfor" },
                            )
                        }

                        showIf(
                            (pe.ut_tbu1286_del1() and (btFellesNetto.equalTo(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbbrutto()) and btSerkullNetto.equalTo(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbbrutto())) and (not(btFellesNetto0) and btSerkullNetto.notEqualTo(0)) and ((btSerkullFradrag0 and btFellesFradrag
                                .equalTo(0)) or (btSerkullFradrag.greaterThan(0) and btFellesFradrag.greaterThan(0))) and btSerkullJusteringsbelopPerAr0 and btFellesJusteringsbelopPerAr0)
                        ) {
                            text(
                                bokmal { +" ikke" },
                                nynorsk { +" ikkje" },
                            )
                        }

                        showIf(
                            (pe.ut_tbu1286_del1() and not(btFellesNetto0) and btSerkullNetto.notEqualTo(0) and ((btSerkullFradrag0 and btFellesFradrag0) or (btSerkullFradrag.greaterThan(0) and btFellesFradrag.greaterThan(0))) and btSerkullJusteringsbelopPerAr
                                .equalTo(0) and btFellesJusteringsbelopPerAr0)
                        ) {
                            text(
                                bokmal { +" redusert ut fra inntekt. " },
                                nynorsk { +" redusert ut frå inntekt. " },
                            )
                        }

                        showIf(((pe.ut_tbu1286_del2()))) {
                            text(
                                bokmal { +"Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget for " + pe.ut_barnet_barna_felles() + " som bor med begge sine foreldre. " },
                                nynorsk { +"Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget for " + pe.ut_barnet_barna_felles() + " som bur saman med begge foreldra sine. " },
                            )
                        }

                        showIf(((pe.ut_tbu1286_del2() and btFellesNetto0))) {
                            text(
                                bokmal { +"Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året. " },
                                nynorsk { +"Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året. " },
                            )
                        }

                        showIf(((pe.ut_tbu1286_del3()))) {
                            text(
                                bokmal { +"Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget for " + pe.ut_barnet_barna_serkull() + " som ikke bor sammen med begge foreldrene. " },
                                nynorsk { +"Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget for " + pe.ut_barnet_barna_serkull() + " som ikkje bur saman med begge foreldra. " },
                            )
                        }

                        showIf(((pe.ut_tbu1286_del3() and btSerkullNetto0))) {
                            text(
                                bokmal { +"Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året. " },
                                nynorsk { +"Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året. " },
                            )
                        }
                    }
                }

                showIf((barnetilleggSerkullInnvilget and btSerkullNetto0 and btSerkullJusteringsbelopPerAr0 and (not(btFellesNetto0) or not(barnetilleggFellesInnvilget)))) {
                    paragraph {
                        text(
                            bokmal { +"Barnetillegget " },
                            nynorsk { +"Barnetillegget " },
                        )

                        showIf(barnetilleggFellesInnvilget) {
                            text(
                                bokmal { +"for " + pe.ut_barnet_barna_serkull() + " som ikke bor sammen med begge foreldrene, " },
                                nynorsk { +"for " + pe.ut_barnet_barna_serkull() + " som ikkje bur saman med begge foreldra sine, " },
                            )
                        }
                        text(
                            bokmal { +"blir ikke utbetalt fordi du " },
                            nynorsk { +"blir ikkje utbetalt fordi du " },
                        )

                        showIf(barnetilleggFellesInnvilget) {
                            text(
                                bokmal { +"alene " },
                                nynorsk { +"åleine " },
                            )
                        }
                        text(
                            bokmal { +"har en samlet inntekt som er høyere enn " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektstak().format() + " kroner. Inntekten din er over grensen for å få utbetalt barnetillegg." },
                            nynorsk { +"har ei samla inntekt som er høgare enn " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektstak().format() + " kroner. Inntekta di er over grensa for å få utbetalt barnetillegg. " },
                        )
                    }
                }

                showIf((barnetilleggFellesInnvilget and btFellesNetto0 and btFellesJusteringsbelopPerAr0 and (btSerkullNetto.notEqualTo(0) or not(barnetilleggSerkullInnvilget)))) {
                    paragraph {
                        text(
                            bokmal { +"Barnetillegget" },
                            nynorsk { +"Barnetillegget " },
                        )

                        showIf(barnetilleggSerkullInnvilget) {
                            text(
                                bokmal { +" for " + pe.ut_barnet_barna_felles() + " som bor med begge sine foreldre," },
                                nynorsk { +"for " + pe.ut_barnet_barna_felles() + " som bur saman med begge foreldra sine, " },
                            )
                        }
                        text(
                            bokmal { +" blir ikke utbetalt fordi dere har en samlet inntekt som er høyere enn " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektstak().format() + " kroner. De samlede inntektene er over grensen for å få utbetalt barnetillegg." },
                            nynorsk { +"blir ikkje utbetalt fordi dei har ei samla inntekt som er høgare enn " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektstak().format() + " kroner. Dei samla inntektene er over grensa for å få utbetalt barnetillegg." },
                        )
                    }
                }

                showIf(((barnetilleggSerkullInnvilget and btSerkullNetto0 and btSerkullJusteringsbelopPerAr0) and (barnetilleggFellesInnvilget and btFellesNetto0 and btFellesJusteringsbelopPerAr0))) {
                    paragraph {
                        text(
                            bokmal { +"Barnetillegget for " + pe.ut_barnet_barna_felles() + " som bor med begge sine foreldre, blir ikke utbetalt fordi de samlede inntektene er høyere enn " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektstak().format() + ". Barnetillegget for " + pe.ut_barnet_barna_serkull() + " som ikke bor sammen med begge foreldrene, blir heller ikke utbetalt fordi inntekten din alene er høyere enn " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektstak().format() + ". Inntektene er over grensen for å få utbetalt barnetillegg." },
                            nynorsk { +"Barnetillegget for " + pe.ut_barnet_barna_felles() + " som bur saman med begge foreldra sine, blir ikkje utbetalt fordi dei samla inntektene er høgare enn " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektstak().format() + ". Barnetillegget for " + pe.ut_barnet_barna_serkull() + " som ikkje bur saman med begge foreldra, blir heller ikkje utbetalt fordi inntekta di åleine er høgare enn " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektstak().format() + ". Inntektene er over grensa for å få utbetalt barnetillegg." },
                        )
                    }
                }

                showIf(barnetilleggInnvilget) {
                    paragraph {
                        text(
                            bokmal { +"Du kan lese mer om beregningen av barnetillegg i vedlegget " },
                            nynorsk { +"Du kan lese meir om berekninga av barnetillegg i vedlegget " },
                        )
                        namedReference(vedleggOpplysningerBruktIBeregningUTLegacy)
                        text(bokmal { +"." }, nynorsk { +"." })
                    }
                }
            }

            showIf(barnetilleggInnvilget) {

                showIf((pe.ut_kravlinjekode_vedtakresultat_forekomst_bt_innv().greaterThan(0))) {
                    includePhrase(TBU5005_Generated)
                    paragraph {
                        text(
                            bokmal { +"Hvis du planlegger å flytte eller oppholde deg i et annet land, må du kontakte oss slik at vi kan ta stilling til om du fortsatt har rett til barnetillegg. Dette gjelder også hvis barnet du forsørger skal oppholde seg i et annet land." },
                            nynorsk { +"Om du planlegg å flytte eller opphalde deg i eit anna land må du kontakte oss slik at vi kan ta stilling til om du fortsatt har rett til barnetillegg. Dette gjeld også om barnet du forsørgjer skal opphalde seg i eit anna land. " },
                        )
                    }
                }
            }

            showIf((ektefelletilleggInnvilget and kravarsak.isNotAnyOf("soknad_bt", "instopphold"))) {
                title1 {
                    text(
                        bokmal { +"For deg som mottar ektefelletillegg" },
                        nynorsk { +"For deg som får ektefelletillegg" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Du mottar ektefelletillegg i uføretrygden din. Dette tillegget blir ikke endret som følge av endring i uføretrygden din." },
                        nynorsk { +"Du får ektefelletillegg i uføretrygda di. Dette tillegget blir ikkje endra som følgje av endring i uføretrygda di." },
                    )
                    text(
                        bokmal { +"For å ha rett til ektefelletillegg fra 1. juli 2020 " },
                        nynorsk { +"For å ha rett til ektefelletillegg frå 1. juli 2020 " },
                    )
                    //TODO: fiks må du må også i de neste to textene
                    text(
                        bokmal { +"må du enten bo i Norge, innenfor EØS-området eller i et annet land Norge har trygdeavtale med" },
                        nynorsk { +"må du enten bu i Noreg, innanfor EØS-området eller i eit anna land Noreg har trygdeavtale med" },
                    )
                    text(
                        bokmal { +"må også ektefellen/samboeren din være bosatt og oppholde seg i Norge, innenfor EØS-området eller et annet land Norge har trygdeavtale med" },
                        nynorsk { +"må også ektefellen/sambuaren din vere busett og opphalde seg i Noreg, innanfor EØS-området eller eit anna land Noreg har trygdeavtale med" },
                    )
                    text(
                        bokmal { +"Hvis du planlegger å flytte eller oppholde deg i et annet land, må du kontakte oss slik at vi kan ta stilling til om du fortsatt har rett til ektefelletillegg. Dette gjelder også hvis ektefellen/samboeren din du forsørger skal oppholde seg i et annet land." },
                        nynorsk { +"Om du planlegg å flytte eller opphalde deg i eit anna land må du kontakte oss slik at vi kan ta stilling til om du fortsatt har rett til ektefelletillegg. Dette gjeld også om ektefellen/sambuaren du forsørgjer skal opphalde seg i eit anna land." },
                    )
                }
            }

            showIf((pe.vedtaksdata_harLopendealderspensjon() and kravarsak.isNotAnyOf("soknad_bt", "instopphold"))) {
                includePhrase(Ufoeretrygd.KombinereUforetrygdAldersPensjon)
            }

            showIf((pe.vedtaksdata_beregningsdata_beregningufore_belopokt() and utbetalingsgrad.equalTo(uforegradFraBeregning) and onsketvirkningsdato.lessThan(pe.vedtakfattetdato_minus_1mnd()))) {
                title1 {
                    text(
                        bokmal { +"Etterbetaling av uføretrygd" },
                        nynorsk { +"Etterbetaling av uføretrygd" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Du får etterbetalt uføretrygd fra " + onsketvirkningsdato.format() + ". Beløpet blir vanligvis utbetalt i løpet av sju virkedager. Det kan bli beregnet fradrag i etterbetalingen for skatt og ytelser du har mottatt fra Nav eller andre, som for eksempel tjenestepensjonsordninger. I disse tilfellene kan etterbetalingen bli forsinket med inntil ni uker. Fradrag i etterbetalingen vil gå fram av utbetalingsmeldingen." },
                        nynorsk { +"Du får etterbetalt uføretrygd frå " + onsketvirkningsdato.format() + ". Beløpet blir vanlegvis utbetalt innan sju vyrkedagar. Det kan bli rekna ut frådrag i etterbetalinga for skatt og ytingar du har fått frå Nav eller andre, som til dømes tenestepensjonsordningar. I desse tilfella kan etterbetalinga bli forseinka med inntil ni veker. Frådrag i etterbetalinga kjem fram av utbetalingsmeldinga." },
                    )
                }
            }

            showIf((pe.vedtaksdata_beregningsdata_beregningantallperioder().greaterThan(1) and pe.vedtaksdata_beregningsdata_beregningufore_belopredusert() and utbetalingsgrad.equalTo(uforegradFraBeregning) and onsketvirkningsdato.lessThan(pe.vedtakfattetdato_minus_1mnd()))) {
                title1 {
                    text(
                        bokmal { +"Tilbakekreving av uføretrygd" },
                        nynorsk { +"Tilbakekrevjing av uføretrygd" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Fordi uføretrygden din er redusert tilbake i tid, betyr dette at du har fått utbetalt for mye i uføretrygd. Du får eget brev med varsel om eventuell tilbakekreving av det feilutbetalte beløpet." },
                        nynorsk { +"Fordi uføretrygda di er redusert tilbake i tid, betyr dette at du har fått utbetalt for mykje i uføretrygd. Du får eit eiga brev med varsel om ei eventuell tilbakekrevjing av det feilutbetalte beløpet." },
                    )
                }
            }

            includePhrase(Ufoeretrygd.MeldeFraOmEndringer)
            includePhrase(Felles.RettTilAAKlage)
            includePhrase(Felles.RettTilInnsyn(vedleggDineRettigheterOgPlikterUfoere))
            includePhrase(Ufoeretrygd.SjekkUtbetalingene)
            includePhrase(Ufoeretrygd.Skattekort)
            includePhrase(Ufoeretrygd.SkattForDegSomBorIUtlandet(pe.grunnlag_persongrunnlagsliste_personbostedsland().equalTo("nor") or pe.grunnlag_persongrunnlagsliste_personbostedsland().equalTo("")))
            includePhrase(Felles.HarDuSpoersmaal.ufoeretrygd)
        }

        includeAttachmentIfNotNull(vedleggMaanedligUfoeretrygdFoerSkatt, pesysData.maanedligUfoeretrygdFoerSkatt)
        includeAttachment(vedleggOpplysningerBruktIBeregningUTLegacy, pesysData.pe, pesysData.pe.inkluderopplysningerbruktiberegningen())
        includeAttachment(vedleggDineRettigheterOgPlikterUfore)
    }
}