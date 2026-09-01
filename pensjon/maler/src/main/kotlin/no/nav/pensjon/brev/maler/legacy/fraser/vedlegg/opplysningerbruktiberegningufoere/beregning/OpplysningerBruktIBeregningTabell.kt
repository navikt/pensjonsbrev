package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.beregning

import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.selectors.pEgruppe10.*
import no.nav.pensjon.brev.api.model.maler.legacy.personsak.selectors.personSak.*
import no.nav.pensjon.brev.maler.fraser.common.BroekText
import no.nav.pensjon.brev.maler.fraser.common.Ja
import no.nav.pensjon.brev.maler.fraser.ufoer.erUforetidspunktMaanedEtterFoedsel
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import java.time.LocalDate

data class OpplysningerBruktIBeregningTabell(val pe: Expression<PEgruppe10>) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        paragraph {
            table(
                header = {
                    column(4) {
                        text(
                            bokmal { + "Opplysning" },
                            nynorsk { + "Opplysning" },
                        )
                    }
                    column(columnSpan = 1, alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT) {}
                }
            ) {
                ifNotNull(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforetidspunkt()){ uforetidspunkt ->
                    val foedselsdato = pe.personsak.foedselsdato
                    val erMndEtterFoedsel = erUforetidspunktMaanedEtterFoedsel(uforetidspunkt, foedselsdato)
                    val visUforetidspunkt = ifElse(erMndEtterFoedsel, foedselsdato.formatMonthYear(), uforetidspunkt.format())

                    row {
                        cell {
                            text(
                                bokmal { + "Uføretidspunkt" },
                                nynorsk { + "Uføretidspunkt" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + visUforetidspunkt },
                                nynorsk { + visUforetidspunkt },
                            )
                        }

                    }
                }

                showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagordinar_beregningsgrunnlagordinerarsbelop().notEqualTo(0))){
                    row {
                        cell {
                            text(
                                bokmal { + "Beregningsgrunnlag" },
                                nynorsk { + "Berekningsgrunnlag" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagordinar_beregningsgrunnlagordinerarsbelop()
                                    .format(false) + " kr" },
                                nynorsk { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagordinar_beregningsgrunnlagordinerarsbelop()
                                    .format(false) + " kr" },
                            )
                        }

                    }
                }

                showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadearsbelop().greaterThan(0))){
                    row {
                        cell {
                            text(
                                bokmal { + "Beregningsgrunnlag yrkesskade" },
                                nynorsk { + "Berekningsgrunnlag yrkesskade" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadearsbelop()
                                    .format(false) + " kr" },
                                nynorsk { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadearsbelop()
                                    .format(false) + " kr" },
                            )
                        }

                    }
                }

                showIf((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt().greaterThan(0))){
                    row {
                        cell {
                            text(
                                bokmal { + "Inntekt før uførhet" },
                                nynorsk { + "Inntekt før uførleik" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt().format(false) + " kr" },
                                nynorsk { + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt().format(false) + " kr" },
                            )
                        }

                    }
                }

                showIf((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieuinntekt().greaterThan(0))){
                    row {
                        cell {
                            text(
                                bokmal { + "Inntekt etter uførhet" },
                                nynorsk { + "Inntekt etter uførleik" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieuinntekt().format(false) + " kr" },
                                nynorsk { + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieuinntekt().format(false) + " kr" },
                            )
                        }

                    }
                }

                row {
                    cell {
                        text(
                            bokmal { + "Uføregrad" },
                            nynorsk { + "Uføregrad" },
                        )
                    }
                    cell {
                        text(
                            bokmal { + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().format() + " %" },
                            nynorsk { + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().format() + " %" },
                        )
                    }

                }

                showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense().greaterThan(0))){
                    row {
                        cell {
                            text(
                                bokmal { + "Inntektsgrense" },
                                nynorsk { + "Inntektsgrense" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.ut_inntektsgrense_faktisk().format(false) + " kr" },
                                nynorsk { + pe.ut_inntektsgrense_faktisk().format(false) + " kr" },
                            )
                        }

                    }
                }

                showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_forventetinntekt().greaterThan(0))){
                    row {
                        cell {
                            text(
                                bokmal { + "Forventet inntekt" },
                                nynorsk { + "Forventa inntekt" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_forventetinntekt()
                                    .format(false) + " kr" },
                                nynorsk { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_forventetinntekt()
                                    .format(false) + " kr" },
                            )
                        }

                    }
                }

                showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_kompensasjonsgrad().greaterThan(0.0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense().lessThan(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektstak()))){
                    row {
                        cell {
                            text(
                                bokmal { + "Reduksjonsprosent" },
                                nynorsk { + "Reduksjonsprosent" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_kompensasjonsgrad()
                                    .format() + " %" },
                                nynorsk { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_kompensasjonsgrad()
                                    .format() + " %" },
                            )
                        }

                    }
                }

                row {
                    cell {
                        text(
                            bokmal { + "Inntekt som medfører at uføretrygden ikke blir utbetalt" },
                            nynorsk { + "Inntekt som fører til at uføretrygda ikkje blir utbetalt" },
                        )
                    }
                    cell {
                        showIf(
                            (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense()
                                .lessThan(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektstak()))
                        ) {
                            text(
                                bokmal { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektstak().format(false) + " kr" },
                                nynorsk { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektstak().format(false) + " kr" },
                            )
                        }

                        showIf(
                            (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense()
                                .greaterThanOrEqual(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektstak()))
                        ) {
                            text(
                                bokmal { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense().format(false) + " kr" },
                                nynorsk { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense().format(false) + " kr" },
                            )
                        }
                    }

                }

                showIf(((pe.pebrevkode().equalTo("PE_UT_04_300") or pe.pebrevkode().equalTo("PE_UT_14_300")) or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_sats().notEqualTo(0.0) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget().not()))){
                    row {
                        cell {
                            text(
                                bokmal { + "Sivilstatus lagt til grunn ved beregningen" },
                                nynorsk { + "Sivilstatus lagd til grunn ved berekninga" },
                            )
                        }

                        cell {
                            showIf(
                                ((pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("gift men lever adskilt") and pe.vedtaksdata_beregningsdata_beregning_beregningbrukersivilstand().equalTo("gift")) or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed ektefelle") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("separert bor med ektefelle"))
                            ) {
                                text(
                                    bokmal { + "Gift" },
                                    nynorsk { + "Gift" },
                                )
                            }

                            showIf(
                                (pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed registrert partner") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("registrert partner men lever adskilt") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("separert bormed partner"))
                            ) {
                                text(
                                    bokmal { + "Partner" },
                                    nynorsk { + "Partnar" },
                                )
                            }

                            showIf(
                                (pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("separert bormed 3-2") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed 3-2") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("gift ektefelle bormed 3-2") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("registrert partner bormed 3-2") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("separert partner bormed 3-2"))
                            ) {
                                text(
                                    bokmal { + "Samboer (jf. folketrygdloven § 12-13)" },
                                    nynorsk { + "Sambuar (jf. folketrygdlova § 12-13)" },
                                )
                            }

                            showIf(pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed 1-5")) {
                                text(
                                    bokmal { + "Samboer (jf. folketrygdloven § 1-5)" },
                                    nynorsk { + "Sambuar (jf. folketrygdlova § 1-5)" },
                                )
                            }

                            showIf(pe.vedtaksdata_beregningsdata_beregning_beregningbrukersivilstand().equalTo("enke")) {
                                text(
                                    bokmal { + "Enke/Enkemann " },
                                    nynorsk { + "Enkje/Enkjemann " },
                                )
                            }

                            showIf(
                                (pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("enslig") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("enslig separert") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("enslig separert partner") or (pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("gift men lever adskilt") and pe.vedtaksdata_beregningsdata_beregning_beregningbrukersivilstand().equalTo("sepr")))
                            ) {
                                text(
                                    bokmal { + "Enslig" },
                                    nynorsk { + "Einsleg" },
                                )
                            }
                        }
                    }
                }

                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("registrert partner men lever adskilt".expr()))){
                    row {
                        cell {
                            text(
                                bokmal { + "Du eller partneren er registrert med annet bosted, eller er på institusjon" },
                                nynorsk { + "Du eller partnaren er registrert med annan bustad, eller er på institusjon" },
                            )
                        }
                        cell {
                            includePhrase(Ja)
                        }
                    }
                }

                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("gift men lever adskilt") and pe.vedtaksdata_beregningsdata_beregning_beregningbrukersivilstand().equalTo("gift"))){
                    row {
                        cell {
                            text(
                                bokmal { + "Du eller ektefellen er registrert med annet bosted, eller er på institusjon" },
                                nynorsk { + "Du eller ektefellen er registrert med annan bustad, eller er på institusjon" },
                            )
                        }
                        cell {
                            includePhrase(Ja)
                        }
                    }
                }

                showIf(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_unguforresultat().equalTo("oppfylt")){
                    row {
                        cell {
                            text(
                                bokmal { + "Ung ufør" },
                                nynorsk { + "Ung ufør" },
                            )
                        }
                        cell {
                            includePhrase(Ja)
                        }
                    }
                }

                showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().notEqualTo(0))) {
                    row {
                        cell {
                            text(
                                bokmal { + "Yrkesskadegrad" },
                                nynorsk { + "Yrkesskadegrad" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().format() + " %" },
                                nynorsk { + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().format() + " %" },
                            )
                        }
                    }
                }

                showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().notEqualTo(0))){
                    ifNotNull(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_skadetidspunkt()) { skadetidspunkt ->
                        row {
                            cell {
                                text(
                                    bokmal { + "Skadetidspunktet for yrkesskaden" },
                                    nynorsk { + "Skadetidspunktet for yrkesskaden" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { + skadetidspunkt.format() },
                                    nynorsk { + skadetidspunkt.format() },
                                )
                            }
                        }
                    }
                }

                showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().notEqualTo(0))) {
                    row {
                        cell {
                            text(
                                bokmal { + "Årlig arbeidsinntekt på skadetidspunktet" },
                                nynorsk { + "Årleg arbeidsinntekt på skadetidspunktet" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format(false) + " kr" },
                                nynorsk { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format(false) + " kr" },
                            )
                        }
                    }
                }

                showIf(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("folketrygd")){
                    row {
                        cell {
                            text(
                                bokmal { + "Trygdetid (maksimalt 40 år)" },
                                nynorsk { + "Trygdetid (maksimalt 40 år)" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid().format() + " år" },
                                nynorsk { + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid().format() + " år" },
                            )
                        }
                    }
                }

                showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("eos") or pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("nordisk"))){
                    row {
                        cell {
                            text(
                                bokmal { + "Teoretisk trygdetid i Norge og andre EØS-land som er brukt i beregningen (maksimalt 40 år)" },
                                nynorsk { + "Teoretisk trygdetid i Noreg og andre EØS-land som er brukt i berekninga (maksimalt 40 år)" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid().format() + " år" },
                                nynorsk { + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid().format() + " år" },
                            )
                        }
                    }
                }

                showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().notEqualTo("eos") and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().notEqualTo("nordisk") and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().notEqualTo("folketrygd"))){
                    row {
                        cell {
                            text(
                                bokmal { + "Teoretisk trygdetid i Norge og andre avtaleland som er brukt i beregningen (maksimalt 40 år)" },
                                nynorsk { + "Teoretisk trygdetid i Noreg og andre avtaleland som er brukt i berekninga (maksimalt 40 år)" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid().format() + " år" },
                                nynorsk { + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid().format() + " år" },
                            )
                        }
                    }
                }

                showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().notEqualTo("folketrygd"))){
                    row {
                        cell {
                            text(
                                bokmal { + "Faktisk trygdetid i Norge" },
                                nynorsk { + "Faktisk trygdetid i Noreg" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_fattnorge().format() + " måneder" },
                                nynorsk { + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_fattnorge().format() + " månader" },
                            )
                        }
                    }
                }

                showIf(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("eos")){
                    row {
                        cell {
                            text(
                                bokmal { + "Faktisk trygdetid i andre EØS-land" },
                                nynorsk { + "Faktisk trygdetid i andre EØS-land" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_fatteos().format() + " måneder" },
                                nynorsk { + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_fatteos().format() + " månader" },
                            )
                        }
                    }
                }

                showIf(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("eos")){
                    row {
                        cell {
                            text(
                                bokmal { + "Faktisk trygdetid i Norge og EØS-land (maksimalt 40 år)" },
                                nynorsk { + "Faktisk trygdetid i Noreg og EØS-land (maksimalt 40 år)" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.ut_sum_fattnorge_fatteos().format() + " måneder" },
                                nynorsk { + pe.ut_sum_fattnorge_fatteos().format() + " månader" },
                            )
                        }
                    }
                }

                showIf(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("eos")){
                    row {
                        cell {
                            text(
                                bokmal { + "Forholdstallet brukt i beregning av trygdetid" },
                                nynorsk { + "Forholdstalet brukt ved berekning av trygdetid" },
                            )
                        }
                        cell {
                            includePhrase(
                                BroekText(
                                    pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_tttellereos(),
                                    pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_ttnevnereos()
                                )
                            )
                        }
                    }
                }

                showIf(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("nordisk")){
                    row {
                        cell {
                            text(
                                bokmal { + "Faktisk trygdetid i annet nordisk land som brukes i beregning av framtidig trygdetid" },
                                nynorsk { + "Faktisk trygdetid i anna nordisk land som blir brukt ved berekning av framtidig trygdetid" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_ttnordisk().format() + " måneder" },
                                nynorsk { + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_ttnordisk().format() + " månader" },
                            )
                        }
                    }
                }

                showIf(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_framtidigttnorsk().lessThan(480)
                        and (pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("folketrygd") or pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("nordisk"))) {
                    row {
                        cell {
                            text(
                                bokmal { + "Norsk framtidig trygdetid" },
                                nynorsk { + "Norsk framtidig trygdetid" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_framtidigttnorsk().format() + " måneder" },
                                nynorsk { + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_framtidigttnorsk().format() + " månader" },
                            )
                        }
                    }
                }

                showIf(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("nordisk")){
                    row {
                        cell {
                            text(
                                bokmal { + "Forholdstallet brukt i reduksjon av norsk framtidig trygdetid" },
                                nynorsk { + "Forholdstalet brukt ved reduksjon av norsk framtidig trygdetid" },
                            )
                        }
                        cell {
                            includePhrase(
                                BroekText(
                                    pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_tttellernordisk(),
                                    pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_ttnevnernordisk()
                                )
                            )
                        }
                    }
                }

                showIf(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("nordisk")){
                    row {
                        cell {
                            text(
                                bokmal { + "Samlet trygdetid brukt i beregning av uføretrygd etter reduksjon av framtidig trygdetid" },
                                nynorsk { + "Samla trygdetid brukt ved berekning av uføretrygd etter reduksjon av framtidig trygdetid" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.ut_sum_fattnorge_fatt_a10_netto().format() + " måneder" },
                                nynorsk { + pe.ut_sum_fattnorge_fatt_a10_netto().format() + " månader" },
                            )
                        }
                    }
                }

                showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().notEqualTo("folketrygd") and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().notEqualTo("eos") and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().notEqualTo("nordisk"))){
                    row {
                        cell {
                            text(
                                bokmal { + "Faktisk trygdetid i annet avtaleland " },
                                nynorsk { + "Faktisk trygdetid i anna avtaleland" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ttutlandtrygdeavtaleliste_ttutlandtrygdeavtale_fattbilateral().format() + " måneder" },
                                nynorsk { + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ttutlandtrygdeavtaleliste_ttutlandtrygdeavtale_fattbilateral().format() + " månader" },
                            )
                        }
                    }
                }

                showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().notEqualTo("folketrygd") and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().notEqualTo("eos") and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().notEqualTo("nordisk"))){
                    row {
                        cell {
                            text(
                                bokmal { + "Faktisk trygdetid i Norge og avtaleland (maksimalt 40 år)" },
                                nynorsk { + "Faktisk trygdetid i Noreg og avtaleland (maksimalt 40 år)" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.ut_sum_fattnorge_fattbilateral().format() + " måneder" },
                                nynorsk { + pe.ut_sum_fattnorge_fattbilateral().format() + " månader" },
                            )
                        }
                    }
                }

                showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().notEqualTo("folketrygd") and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().notEqualTo("eos") and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().notEqualTo("nordisk"))){
                    row {
                        cell {
                            text(
                                bokmal { + "Forholdstallet brukt i beregning av uføretrygd" },
                                nynorsk { + "Forholdstalet brukt i berekning av uføretrygd" },
                            )
                        }
                        cell {
                            includePhrase(
                                BroekText(
                                    pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_proratabrokteller(),
                                    pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_proratabroknevner()
                                )
                            )
                        }
                    }
                }

                showIf((pe.vedtaksdata_kravhode_kravgjelder().equalTo("f_bh_bo_utl") and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().lessThan(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()))){
                    row {
                        cell {
                            text(
                                bokmal { + "År med inntekt over folketrygdens grunnbeløp før uføretidspunktet" },
                                nynorsk { + "År med inntekt over grunnbeløpet i folketrygda før uføretidspunktet" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagordinar_antallarover1g()
                                    .format() + " år" },
                                nynorsk { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagordinar_antallarover1g()
                                    .format() + " år" },
                            )
                        }
                    }
                }

                showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagordinar_antallarinntektiavtaleland().notEqualTo(0))){
                    row {
                        cell {
                            text(
                                bokmal { + "År med inntekt i utlandet brukt i beregningen" },
                                nynorsk { + "År med inntekt i utlandet" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagordinar_antallarinntektiavtaleland()
                                    .format() + " år" },
                                nynorsk { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagordinar_antallarinntektiavtaleland()
                                    .format() + " år" },
                            )
                        }
                    }
                }

                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget())){
                    row {
                        cell {
                            text(
                                bokmal { + "Totalt antall barn du har barnetillegg for" },
                                nynorsk { + "Totalt tal barn du har barnetillegg for" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.ut_antallbarnserkullogfelles().format() },
                                nynorsk { + pe.ut_antallbarnserkullogfelles().format() },
                            )
                        }
                    }
                }

                showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_gradertoppjustertifu().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_andelytelseavoifu().greaterThan(95.0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningvirkningdatofom().ifNull(LocalDate.of(2000,1,1)).greaterThanOrEqual(LocalDate.of(2016,1,1))){
                    row {
                        cell {
                            text(
                                bokmal { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_prosentsatsoifufortak().format() + " % av inntekt før uførhet (justert for endringer i grunnbeløpet)" },
                                nynorsk { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_prosentsatsoifufortak().format() + " % av inntekt før uførleik (justert for endringar i grunnbeløpet)" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_gradertoppjustertifu().format(false) + " kr" },
                                nynorsk { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_gradertoppjustertifu().format(false) + " kr" },
                            )
                        }
                    }
                }

                showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()){
                    row {
                        cell {
                            text(
                                bokmal { + "Fribeløp for særkullsbarn" },
                                nynorsk { + "Fribeløp for særkullsbarn" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop()
                                    .format(false) + " kr" },
                                nynorsk { + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop()
                                    .format(false) + " kr" },
                            )
                        }
                    }
                }

                showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()){
                    row {
                        cell {
                            text(
                                bokmal { + "Fribeløp for fellesbarn" },
                                nynorsk { + "Fribeløp for fellesbarn" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop()
                                    .format(false) + " kr" },
                                nynorsk { + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop()
                                    .format(false) + " kr" },
                            )
                        }
                    }
                }

                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget())){
                    row {
                        cell {
                            text(
                                bokmal { + "Samlet inntekt som er brukt i fastsettelse av barnetillegg" },
                                nynorsk { + "Inntekt for deg som er brukt i berekning av barnetillegg" },
                            )
                        }

                        cell {
                            showIf(not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget())) {
                                text(
                                    bokmal { + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().format(false) },
                                    nynorsk { + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().format(false) },
                                )
                            }

                            showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) {
                                text(
                                    bokmal { + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinntektbruktiavkortning().format(false) },
                                    nynorsk { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbrukersinntekttilavkortning().format(false) },
                                )
                            }
                            text(
                                bokmal { + " kr" },
                                nynorsk { + " kr" },
                            )
                        }
                    }
                }

                showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()){
                    row {
                        cell {
                            text(
                                bokmal { + "Samlet inntekt til annen forelder som er brukt i fastsettelse av barnetillegg" },
                                nynorsk { + "Inntekt til annan forelder som er brukt i berekning av barnetillegg" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbinntektannenforelder().format(false) + " kr" },
                                nynorsk { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbinntektannenforelder().format(false) + " kr" },
                            )
                        }
                    }
                }

                showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbelopfratrukketannenforeldersinntekt().greaterThan(0)){
                    row {
                        cell {
                            text(
                                bokmal { + "Beløp som er trukket fra annen forelders inntekt (inntil 1G)" },
                                nynorsk { + "Beløp som er trekt frå inntekta til ein annan forelder (inntil 1G)" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbelopfratrukketannenforeldersinntekt()
                                    .format(false) + " kr" },
                                nynorsk { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbelopfratrukketannenforeldersinntekt()
                                    .format(false) + " kr" },
                            )
                        }
                    }
                }

                showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and pe.vedtaksdata_beregningsdata_beregningufore_beregningvirkningdatofom().ifNull(LocalDate.of(2000,1,1)).greaterThanOrEqual(LocalDate.of(2016,1,1).expr())){
                    row {
                        cell {
                            text(
                                bokmal { + "Samlet inntekt for deg som gjør at barnetillegget ikke blir utbetalt" },
                                nynorsk { + "Samla inntekt som gjer at barnetillegget ikkje blir utbetalt" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektstak()
                                    .format(false) + " kr" },
                                nynorsk { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektstak()
                                    .format(false) + " kr" },
                            )
                        }
                    }
                }

                showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregningufore_beregningvirkningdatofom().ifNull(LocalDate.of(2000,1,1)).greaterThanOrEqual(LocalDate.of(2016,1,1))){
                    row {
                        cell {
                            text(
                                bokmal { + "Samlet inntekt for deg og annen forelder som gjør at barnetillegget ikke blir utbetalt" },
                                nynorsk { + "Samla inntekt for deg og annan forelder som gjer at barnetillegget ikkje blir utbetalt" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektstak()
                                    .format(false) + " kr" },
                                nynorsk { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektstak()
                                    .format(false) + " kr" },
                            )
                        }
                    }
                }
            }
        }
    }
}