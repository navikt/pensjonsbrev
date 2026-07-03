package no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser

import no.nav.pensjon.brev.ufore.api.model.vedlegg.Beregningsmetode
import no.nav.pensjon.brev.ufore.api.model.vedlegg.SivilstatusVisning
import no.nav.pensjon.brev.ufore.api.model.vedlegg.OpplysningerBruktIBeregningUTLegacyDto.Avkortning
import no.nav.pensjon.brev.ufore.api.model.vedlegg.OpplysningerBruktIBeregningUTLegacyDto.Barnetillegg
import no.nav.pensjon.brev.ufore.api.model.vedlegg.OpplysningerBruktIBeregningUTLegacyDto.Beregning
import no.nav.pensjon.brev.ufore.api.model.vedlegg.OpplysningerBruktIBeregningUTLegacyDto.InntektFoerUfoere
import no.nav.pensjon.brev.ufore.api.model.vedlegg.OpplysningerBruktIBeregningUTLegacyDto.Person
import no.nav.pensjon.brev.ufore.api.model.vedlegg.OpplysningerBruktIBeregningUTLegacyDto.Trygdetid
import no.nav.pensjon.brev.ufore.api.model.vedlegg.OpplysningerBruktIBeregningUTLegacyDto.Visningsflagg
import no.nav.pensjon.brev.ufore.api.model.vedlegg.OpplysningerBruktIBeregningUTLegacyDto.Yrkesskade
import no.nav.pensjon.brev.ufore.api.model.vedlegg.OpplysningerBruktIBeregningUTLegacyDto.Ytelsesgrunnlag
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.avkortning.*
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.barnetillegg.*
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.beregning.*
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.inntektFoerUfoere.*
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.person.*
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.trygdetid.*
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.visningsflagg.*
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.yrkesskade.*
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.ytelsesgrunnlag.*
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text

/**
 * Portert fra legacy TBU010V. Den store opplysningstabellen for brukerens egen beregning.
 * Brevkode-/kravårsak-/sivilstand-/dato-betingelsene fra legacy er trukket ut til ferdig
 * beregnede [Visningsflagg] og til [Person.sivilstatusVisning]/[Beregning.beregningsmetode].
 * Rader som er null i DTO-en utelates via ifNotNull.
 */
data class TBU010V(
    val flagg: Expression<Visningsflagg>,
    val beregning: Expression<Beregning>,
    val avkortning: Expression<Avkortning>,
    val ytelsesgrunnlag: Expression<Ytelsesgrunnlag>,
    val inntektFoerUfoere: Expression<InntektFoerUfoere>,
    val trygdetid: Expression<Trygdetid>,
    val person: Expression<Person>,
    val yrkesskade: Expression<Yrkesskade?>,
    val barnetilleggFelles: Expression<Barnetillegg?>,
    val barnetilleggSaerkull: Expression<Barnetillegg?>,
    val antallBarnTotalt: Expression<Int?>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        paragraph {
            table(
                header = {
                    column(4) {
                        text(bokmal { + "Opplysning" }, nynorsk { + "Opplysning" })
                    }
                    column(columnSpan = 1, alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT) {}
                }
            ) {
                // Uføretidspunkt (vises som måned/år når uføretidspunktet er måneden etter fødsel)
                row {
                    cell { text(bokmal { + "Uføretidspunkt" }, nynorsk { + "Uføretidspunkt" }) }
                    cell {
                        showIf(flagg.erMaanedEtterFoedsel) {
                            text(
                                bokmal { + person.foedselsdato.formatMonthYear() },
                                nynorsk { + person.foedselsdato.formatMonthYear() },
                            )
                        }.orShow {
                            text(
                                bokmal { + beregning.ufoeretidspunkt.format() },
                                nynorsk { + beregning.ufoeretidspunkt.format() },
                            )
                        }
                    }
                }

                showIf(flagg.visBeregningsgrunnlag) {
                    row {
                        cell { text(bokmal { + "Beregningsgrunnlag" }, nynorsk { + "Berekningsgrunnlag" }) }
                        cell {
                            text(
                                bokmal { + ytelsesgrunnlag.beregningsgrunnlagOrdinaerAarsbeloep.format(false) + " kr" },
                                nynorsk { + ytelsesgrunnlag.beregningsgrunnlagOrdinaerAarsbeloep.format(false) + " kr" },
                            )
                        }
                    }
                }

                ifNotNull(ytelsesgrunnlag.yrkesskadeAarsbeloep) { aarsbeloep ->
                    row {
                        cell { text(bokmal { + "Beregningsgrunnlag yrkesskade" }, nynorsk { + "Berekningsgrunnlag yrkesskade" }) }
                        cell { text(bokmal { + aarsbeloep.format(false) + " kr" }, nynorsk { + aarsbeloep.format(false) + " kr" }) }
                    }
                }

                showIf(flagg.visInntektFoerUfoere) {
                    row {
                        cell { text(bokmal { + "Inntekt før uførhet" }, nynorsk { + "Inntekt før uførleik" }) }
                        cell {
                            text(
                                bokmal { + inntektFoerUfoere.ifuInntekt.format(false) + " kr" },
                                nynorsk { + inntektFoerUfoere.ifuInntekt.format(false) + " kr" },
                            )
                        }
                    }
                }

                ifNotNull(inntektFoerUfoere.ieuInntekt) { ieu ->
                    row {
                        cell { text(bokmal { + "Inntekt etter uførhet" }, nynorsk { + "Inntekt etter uførleik" }) }
                        cell { text(bokmal { + ieu.format(false) + " kr" }, nynorsk { + ieu.format(false) + " kr" }) }
                    }
                }

                row {
                    cell { text(bokmal { + "Uføregrad" }, nynorsk { + "Uføregrad" }) }
                    cell { text(bokmal { + beregning.ufoeregrad.format() + " %" }, nynorsk { + beregning.ufoeregrad.format() + " %" }) }
                }

                showIf(flagg.visInntektsgrense) {
                    row {
                        cell { text(bokmal { + "Inntektsgrense" }, nynorsk { + "Inntektsgrense" }) }
                        cell {
                            text(
                                bokmal { + avkortning.inntektsgrenseFaktisk.format(false) + " kr" },
                                nynorsk { + avkortning.inntektsgrenseFaktisk.format(false) + " kr" },
                            )
                        }
                    }
                }

                showIf(flagg.visForventetInntekt) {
                    row {
                        cell { text(bokmal { + "Forventet inntekt" }, nynorsk { + "Forventa inntekt" }) }
                        cell {
                            text(
                                bokmal { + avkortning.forventetInntektAar.format(false) + " kr" },
                                nynorsk { + avkortning.forventetInntektAar.format(false) + " kr" },
                            )
                        }
                    }
                }

                showIf(flagg.visReduksjonsprosent) {
                    row {
                        cell { text(bokmal { + "Reduksjonsprosent" }, nynorsk { + "Reduksjonsprosent" }) }
                        cell {
                            text(
                                bokmal { + avkortning.kompensasjonsgrad.format() + " %" },
                                nynorsk { + avkortning.kompensasjonsgrad.format() + " %" },
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
                        showIf(flagg.inntektsgrenseLavereEnnInntektstak) {
                            text(
                                bokmal { + avkortning.inntektstak.format(false) + " kr" },
                                nynorsk { + avkortning.inntektstak.format(false) + " kr" },
                            )
                        }.orShow {
                            text(
                                bokmal { + avkortning.inntektsgrenseAar.format(false) + " kr" },
                                nynorsk { + avkortning.inntektsgrenseAar.format(false) + " kr" },
                            )
                        }
                    }
                }

                showIf(flagg.visSivilstatusRad) {
                    row {
                        cell {
                            text(
                                bokmal { + "Sivilstatus lagt til grunn ved beregningen" },
                                nynorsk { + "Sivilstatus lagd til grunn ved berekninga" },
                            )
                        }
                        cell {
                            ifNotNull(person.sivilstatusVisning) { sivilstatus ->
                                showIf(sivilstatus.equalTo(SivilstatusVisning.GIFT)) {
                                    text(bokmal { + "Gift" }, nynorsk { + "Gift" })
                                }
                                showIf(sivilstatus.equalTo(SivilstatusVisning.PARTNER)) {
                                    text(bokmal { + "Partner" }, nynorsk { + "Partnar" })
                                }
                                showIf(sivilstatus.equalTo(SivilstatusVisning.SAMBOER_12_13)) {
                                    text(
                                        bokmal { + "Samboer (jf. folketrygdloven § 12-13)" },
                                        nynorsk { + "Sambuar (jf. folketrygdlova § 12-13)" },
                                    )
                                }
                                showIf(sivilstatus.equalTo(SivilstatusVisning.SAMBOER_1_5)) {
                                    text(
                                        bokmal { + "Samboer (jf. folketrygdloven § 1-5)" },
                                        nynorsk { + "Sambuar (jf. folketrygdlova § 1-5)" },
                                    )
                                }
                                showIf(sivilstatus.equalTo(SivilstatusVisning.ENKE)) {
                                    text(bokmal { + "Enke/Enkemann " }, nynorsk { + "Enkje/Enkjemann " })
                                }
                                showIf(sivilstatus.equalTo(SivilstatusVisning.ENSLIG)) {
                                    text(bokmal { + "Enslig" }, nynorsk { + "Einsleg" })
                                }
                            }
                        }
                    }
                }

                showIf(flagg.visPartnerAnnetBosted) {
                    row {
                        cell {
                            text(
                                bokmal { + "Du eller partneren er registrert med annet bosted, eller er på institusjon" },
                                nynorsk { + "Du eller partnaren er registrert med annan bustad, eller er på institusjon" },
                            )
                        }
                        cell { text(bokmal { + "Ja" }, nynorsk { + "Ja" }) }
                    }
                }

                showIf(flagg.visEktefelleAnnetBosted) {
                    row {
                        cell {
                            text(
                                bokmal { + "Du eller ektefellen er registrert med annet bosted, eller er på institusjon" },
                                nynorsk { + "Du eller ektefellen er registrert med annan bustad, eller er på institusjon" },
                            )
                        }
                        cell { text(bokmal { + "Ja" }, nynorsk { + "Ja" }) }
                    }
                }

                showIf(flagg.visUngUfoerRad) {
                    row {
                        cell { text(bokmal { + "Ung ufør" }, nynorsk { + "Ung ufør" }) }
                        cell { text(bokmal { + "Ja" }, nynorsk { + "Ja" }) }
                    }
                }

                ifNotNull(beregning.yrkesskadegrad) { yrkesskadegrad ->
                    row {
                        cell { text(bokmal { + "Yrkesskadegrad" }, nynorsk { + "Yrkesskadegrad" }) }
                        cell { text(bokmal { + yrkesskadegrad.format() + " %" }, nynorsk { + yrkesskadegrad.format() + " %" }) }
                    }
                }

                ifNotNull(yrkesskade) { yrkesskade ->
                    row {
                        cell { text(bokmal { + "Skadetidspunktet for yrkesskaden" }, nynorsk { + "Skadetidspunktet for yrkesskaden" }) }
                        cell { text(bokmal { + yrkesskade.skadetidspunkt.format() }, nynorsk { + yrkesskade.skadetidspunkt.format() }) }
                    }
                }

                ifNotNull(ytelsesgrunnlag.inntektVedSkadetidspunkt) { inntekt ->
                    row {
                        cell {
                            text(
                                bokmal { + "Årlig arbeidsinntekt på skadetidspunktet" },
                                nynorsk { + "Årleg arbeidsinntekt på skadetidspunktet" },
                            )
                        }
                        cell { text(bokmal { + inntekt.format(false) + " kr" }, nynorsk { + inntekt.format(false) + " kr" }) }
                    }
                }

                // Trygdetid-rader styrt av beregningsmetode
                showIf(beregning.beregningsmetode.equalTo(Beregningsmetode.FOLKETRYGD)) {
                    row {
                        cell { text(bokmal { + "Trygdetid (maksimalt 40 år)" }, nynorsk { + "Trygdetid (maksimalt 40 år)" }) }
                        cell { text(bokmal { + beregning.anvendtTrygdetid.format() + " år" }, nynorsk { + beregning.anvendtTrygdetid.format() + " år" }) }
                    }
                }

                showIf(beregning.beregningsmetode.equalTo(Beregningsmetode.EOS) or beregning.beregningsmetode.equalTo(Beregningsmetode.NORDISK)) {
                    row {
                        cell {
                            text(
                                bokmal { + "Teoretisk trygdetid i Norge og andre EØS-land som er brukt i beregningen (maksimalt 40 år)" },
                                nynorsk { + "Teoretisk trygdetid i Noreg og andre EØS-land som er brukt i berekninga (maksimalt 40 år)" },
                            )
                        }
                        cell { text(bokmal { + beregning.anvendtTrygdetid.format() + " år" }, nynorsk { + beregning.anvendtTrygdetid.format() + " år" }) }
                    }
                }

                showIf(flagg.beregningsmetodeErBilateral) {
                    row {
                        cell {
                            text(
                                bokmal { + "Teoretisk trygdetid i Norge og andre avtaleland som er brukt i beregningen (maksimalt 40 år)" },
                                nynorsk { + "Teoretisk trygdetid i Noreg og andre avtaleland som er brukt i berekninga (maksimalt 40 år)" },
                            )
                        }
                        cell { text(bokmal { + beregning.anvendtTrygdetid.format() + " år" }, nynorsk { + beregning.anvendtTrygdetid.format() + " år" }) }
                    }
                }

                showIf(not(beregning.beregningsmetode.equalTo(Beregningsmetode.FOLKETRYGD))) {
                    ifNotNull(trygdetid.faktiskTTNorge) { ttNorge ->
                        row {
                            cell { text(bokmal { + "Faktisk trygdetid i Norge" }, nynorsk { + "Faktisk trygdetid i Noreg" }) }
                            cell { text(bokmal { + ttNorge.format() + " måneder" }, nynorsk { + ttNorge.format() + " månader" }) }
                        }
                    }
                }

                showIf(beregning.beregningsmetode.equalTo(Beregningsmetode.EOS)) {
                    ifNotNull(trygdetid.faktiskTTEOS) { ttEOS ->
                        row {
                            cell { text(bokmal { + "Faktisk trygdetid i andre EØS-land" }, nynorsk { + "Faktisk trygdetid i andre EØS-land" }) }
                            cell { text(bokmal { + ttEOS.format() + " måneder" }, nynorsk { + ttEOS.format() + " månader" }) }
                        }
                    }
                    ifNotNull(trygdetid.sumFattNorgeFattEOS) { sum ->
                        row {
                            cell {
                                text(
                                    bokmal { + "Faktisk trygdetid i Norge og EØS-land (maksimalt 40 år)" },
                                    nynorsk { + "Faktisk trygdetid i Noreg og EØS-land (maksimalt 40 år)" },
                                )
                            }
                            cell { text(bokmal { + sum.format() + " måneder" }, nynorsk { + sum.format() + " månader" }) }
                        }
                    }
                    ifNotNull(trygdetid.tellerTTEOS, trygdetid.nevnerTTEOS) { teller, nevner ->
                        row {
                            cell {
                                text(
                                    bokmal { + "Forholdstallet brukt i beregning av trygdetid" },
                                    nynorsk { + "Forholdstalet brukt ved berekning av trygdetid" },
                                )
                            }
                            cell { text(bokmal { + teller.format() + "/" + nevner.format() }, nynorsk { + teller.format() + "/" + nevner.format() }) }
                        }
                    }
                }

                showIf(beregning.beregningsmetode.equalTo(Beregningsmetode.NORDISK)) {
                    ifNotNull(trygdetid.faktiskTTNordisk) { ttNordisk ->
                        row {
                            cell {
                                text(
                                    bokmal { + "Faktisk trygdetid i annet nordisk land som brukes i beregning av framtidig trygdetid" },
                                    nynorsk { + "Faktisk trygdetid i anna nordisk land som blir brukt ved berekning av framtidig trygdetid" },
                                )
                            }
                            cell { text(bokmal { + ttNordisk.format() + " måneder" }, nynorsk { + ttNordisk.format() + " månader" }) }
                        }
                    }
                }

                showIf(flagg.visNorskFramtidigTrygdetid) {
                    ifNotNull(trygdetid.framtidigTTNorsk) { framtidig ->
                        row {
                            cell { text(bokmal { + "Norsk framtidig trygdetid" }, nynorsk { + "Norsk framtidig trygdetid" }) }
                            cell { text(bokmal { + framtidig.format() + " måneder" }, nynorsk { + framtidig.format() + " månader" }) }
                        }
                    }
                }

                showIf(beregning.beregningsmetode.equalTo(Beregningsmetode.NORDISK)) {
                    ifNotNull(trygdetid.tellerTTNordisk, trygdetid.nevnerTTNordisk) { teller, nevner ->
                        row {
                            cell {
                                text(
                                    bokmal { + "Forholdstallet brukt i reduksjon av norsk framtidig trygdetid" },
                                    nynorsk { + "Forholdstalet brukt ved reduksjon av norsk framtidig trygdetid" },
                                )
                            }
                            cell { text(bokmal { + teller.format() + "/" + nevner.format() }, nynorsk { + teller.format() + "/" + nevner.format() }) }
                        }
                    }
                    ifNotNull(trygdetid.sumFattNorgeFattA10Netto) { sum ->
                        row {
                            cell {
                                text(
                                    bokmal { + "Samlet trygdetid brukt i beregning av uføretrygd etter reduksjon av framtidig trygdetid" },
                                    nynorsk { + "Samla trygdetid brukt ved berekning av uføretrygd etter reduksjon av framtidig trygdetid" },
                                )
                            }
                            cell { text(bokmal { + sum.format() + " måneder" }, nynorsk { + sum.format() + " månader" }) }
                        }
                    }
                }

                showIf(flagg.beregningsmetodeErBilateral) {
                    ifNotNull(trygdetid.faktiskTTBilateral) { ttBilateral ->
                        row {
                            cell { text(bokmal { + "Faktisk trygdetid i annet avtaleland " }, nynorsk { + "Faktisk trygdetid i anna avtaleland" }) }
                            cell { text(bokmal { + ttBilateral.format() + " måneder" }, nynorsk { + ttBilateral.format() + " månader" }) }
                        }
                    }
                    ifNotNull(trygdetid.sumFattNorgeFattBilateral) { sum ->
                        row {
                            cell {
                                text(
                                    bokmal { + "Faktisk trygdetid i Norge og avtaleland (maksimalt 40 år)" },
                                    nynorsk { + "Faktisk trygdetid i Noreg og avtaleland (maksimalt 40 år)" },
                                )
                            }
                            cell { text(bokmal { + sum.format() + " måneder" }, nynorsk { + sum.format() + " månader" }) }
                        }
                    }
                    ifNotNull(beregning.proRataBroekTeller, beregning.proRataBroekNevner) { teller, nevner ->
                        row {
                            cell {
                                text(
                                    bokmal { + "Forholdstallet brukt i beregning av uføretrygd" },
                                    nynorsk { + "Forholdstalet brukt i berekning av uføretrygd" },
                                )
                            }
                            cell { text(bokmal { + teller.format() + "/" + nevner.format() }, nynorsk { + teller.format() + "/" + nevner.format() }) }
                        }
                    }
                }

                showIf(flagg.visAarOver1G) {
                    ifNotNull(ytelsesgrunnlag.antallAarOver1G) { antall ->
                        row {
                            cell {
                                text(
                                    bokmal { + "År med inntekt over folketrygdens grunnbeløp før uføretidspunktet" },
                                    nynorsk { + "År med inntekt over grunnbeløpet i folketrygda før uføretidspunktet" },
                                )
                            }
                            cell { text(bokmal { + antall.format() + " år" }, nynorsk { + antall.format() + " år" }) }
                        }
                    }
                }

                ifNotNull(ytelsesgrunnlag.antallAarInntektIAvtaleland) { antall ->
                    row {
                        cell {
                            text(
                                bokmal { + "År med inntekt i utlandet brukt i beregningen" },
                                nynorsk { + "År med inntekt i utlandet" },
                            )
                        }
                        cell { text(bokmal { + antall.format() + " år" }, nynorsk { + antall.format() + " år" }) }
                    }
                }

                showIf(barnetilleggFelles.notNull() or barnetilleggSaerkull.notNull()) {
                    ifNotNull(antallBarnTotalt) { antall ->
                        row {
                            cell {
                                text(
                                    bokmal { + "Totalt antall barn du har barnetillegg for" },
                                    nynorsk { + "Totalt tal barn du har barnetillegg for" },
                                )
                            }
                            cell { text(bokmal { + antall.format() }, nynorsk { + antall.format() }) }
                        }
                    }
                }

                showIf(flagg.visOifuFortak) {
                    ifNotNull(avkortning.prosentsatsOifuForTak, avkortning.gradertOppjustertIFU) { prosent, gradert ->
                        row {
                            cell {
                                text(
                                    bokmal { + prosent.format() + " % av inntekt før uførhet (justert for endringer i grunnbeløpet)" },
                                    nynorsk { + prosent.format() + " % av inntekt før uførleik (justert for endringar i grunnbeløpet)" },
                                )
                            }
                            cell { text(bokmal { + gradert.format(false) + " kr" }, nynorsk { + gradert.format(false) + " kr" }) }
                        }
                    }
                }

                ifNotNull(barnetilleggSaerkull) { saerkull ->
                    showIf(saerkull.innvilget) {
                        ifNotNull(saerkull.fribeloep) { fribeloep ->
                            row {
                                cell { text(bokmal { + "Fribeløp for særkullsbarn" }, nynorsk { + "Fribeløp for særkullsbarn" }) }
                                cell { text(bokmal { + fribeloep.format(false) + " kr" }, nynorsk { + fribeloep.format(false) + " kr" }) }
                            }
                        }
                    }
                }

                ifNotNull(barnetilleggFelles) { felles ->
                    showIf(felles.innvilget) {
                        ifNotNull(felles.fribeloep) { fribeloep ->
                            row {
                                cell { text(bokmal { + "Fribeløp for fellesbarn" }, nynorsk { + "Fribeløp for fellesbarn" }) }
                                cell { text(bokmal { + fribeloep.format(false) + " kr" }, nynorsk { + fribeloep.format(false) + " kr" }) }
                            }
                        }
                    }
                }

                showIf(barnetilleggFelles.notNull() or barnetilleggSaerkull.notNull()) {
                    row {
                        cell {
                            text(
                                bokmal { + "Samlet inntekt som er brukt i fastsettelse av barnetillegg" },
                                nynorsk { + "Inntekt for deg som er brukt i berekning av barnetillegg" },
                            )
                        }
                        cell {
                            showIf(flagg.barnetilleggFellesInnvilget) {
                                ifNotNull(barnetilleggFelles) { felles ->
                                    ifNotNull(felles.brukersInntektTilAvkortning) { inntekt ->
                                        text(bokmal { + inntekt.format(false) + " kr" }, nynorsk { + inntekt.format(false) + " kr" })
                                    }
                                }
                            }.orShow {
                                ifNotNull(barnetilleggSaerkull) { saerkull ->
                                    ifNotNull(saerkull.inntektBruktIAvkortning) { inntekt ->
                                        text(bokmal { + inntekt.format(false) + " kr" }, nynorsk { + inntekt.format(false) + " kr" })
                                    }
                                }
                            }
                        }
                    }
                }

                showIf(barnetilleggFelles.notNull()) {
                    ifNotNull(barnetilleggFelles) { felles ->
                        ifNotNull(felles.inntektAnnenForelder) { inntekt ->
                            row {
                                cell {
                                    text(
                                        bokmal { + "Samlet inntekt til annen forelder som er brukt i fastsettelse av barnetillegg" },
                                        nynorsk { + "Inntekt til annan forelder som er brukt i berekning av barnetillegg" },
                                    )
                                }
                                cell { text(bokmal { + inntekt.format(false) + " kr" }, nynorsk { + inntekt.format(false) + " kr" }) }
                            }
                        }
                    }
                }

                ifNotNull(barnetilleggFelles) { felles ->
                    ifNotNull(felles.beloepFratrukketAnnenForeldersInntekt) { beloep ->
                        row {
                            cell {
                                text(
                                    bokmal { + "Beløp som er trukket fra annen forelders inntekt (inntil 1G)" },
                                    nynorsk { + "Beløp som er trekt frå inntekta til ein annan forelder (inntil 1G)" },
                                )
                            }
                            cell { text(bokmal { + beloep.format(false) + " kr" }, nynorsk { + beloep.format(false) + " kr" }) }
                        }
                    }
                }

                showIf(flagg.visBarnetilleggSaerkullIkkeUtbetalt) {
                    ifNotNull(barnetilleggSaerkull) { saerkull ->
                        ifNotNull(saerkull.inntektstak) { inntektstak ->
                            row {
                                cell {
                                    text(
                                        bokmal { + "Samlet inntekt for deg som gjør at barnetillegget ikke blir utbetalt" },
                                        nynorsk { + "Samla inntekt som gjer at barnetillegget ikkje blir utbetalt" },
                                    )
                                }
                                cell { text(bokmal { + inntektstak.format(false) + " kr" }, nynorsk { + inntektstak.format(false) + " kr" }) }
                            }
                        }
                    }
                }

                showIf(flagg.visBarnetilleggFellesIkkeUtbetalt) {
                    ifNotNull(barnetilleggFelles) { felles ->
                        ifNotNull(felles.inntektstak) { inntektstak ->
                            row {
                                cell {
                                    text(
                                        bokmal { + "Samlet inntekt for deg og annen forelder som gjør at barnetillegget ikke blir utbetalt" },
                                        nynorsk { + "Samla inntekt for deg og annan forelder som gjer at barnetillegget ikkje blir utbetalt" },
                                    )
                                }
                                cell { text(bokmal { + inntektstak.format(false) + " kr" }, nynorsk { + inntektstak.format(false) + " kr" }) }
                            }
                        }
                    }
                }
            }
        }
    }
}
