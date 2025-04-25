package no.nav.pensjon.brev.maler.ufoereBrev.vedlegg

import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.VedleggSlikErUforetrygdenDinBeregnetDto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.VedleggSlikErUforetrygdenDinBeregnetDto.Beregningsmetode
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.VedleggSlikErUforetrygdenDinBeregnetDtoSelectors.BrokSelectors.nevner
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.VedleggSlikErUforetrygdenDinBeregnetDtoSelectors.BrokSelectors.teller
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.VedleggSlikErUforetrygdenDinBeregnetDtoSelectors.TrygdetidSelectors.anvendt
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.VedleggSlikErUforetrygdenDinBeregnetDtoSelectors.TrygdetidSelectors.faktisk
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.VedleggSlikErUforetrygdenDinBeregnetDtoSelectors.TrygdetidSelectors.faktiskEos
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.VedleggSlikErUforetrygdenDinBeregnetDtoSelectors.TrygdetidSelectors.faktiskNordisk
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.VedleggSlikErUforetrygdenDinBeregnetDtoSelectors.TrygdetidSelectors.forholdstall
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.VedleggSlikErUforetrygdenDinBeregnetDtoSelectors.virkningFom
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.VedleggSlikErUforetrygdenDinBeregnetDtoSelectors.grunnbelop
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.VedleggSlikErUforetrygdenDinBeregnetDtoSelectors.uforetidspunkt
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.VedleggSlikErUforetrygdenDinBeregnetDtoSelectors.oifu
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.VedleggSlikErUforetrygdenDinBeregnetDtoSelectors.ifu
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.VedleggSlikErUforetrygdenDinBeregnetDtoSelectors.ieu
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.VedleggSlikErUforetrygdenDinBeregnetDtoSelectors.uforegrad
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.VedleggSlikErUforetrygdenDinBeregnetDtoSelectors.inntektsgrense
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.VedleggSlikErUforetrygdenDinBeregnetDtoSelectors.forventetInntekt
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.VedleggSlikErUforetrygdenDinBeregnetDtoSelectors.kompensasjonsgrad
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.VedleggSlikErUforetrygdenDinBeregnetDtoSelectors.inntektstak
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.VedleggSlikErUforetrygdenDinBeregnetDtoSelectors.sivilstand
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.VedleggSlikErUforetrygdenDinBeregnetDtoSelectors.trygdetid
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.VedleggSlikErUforetrygdenDinBeregnetDtoSelectors.antallBarn
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.VedleggSlikErUforetrygdenDinBeregnetDtoSelectors.fribelopBTSB
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.VedleggSlikErUforetrygdenDinBeregnetDtoSelectors.forventetInntektBT
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.VedleggSlikErUforetrygdenDinBeregnetDtoSelectors.giftLeverAdskilt
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.VedleggSlikErUforetrygdenDinBeregnetDtoSelectors.partnerLeverAdskilt
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.VedleggSlikErUforetrygdenDinBeregnetDtoSelectors.ungUfor
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.VedleggSlikErUforetrygdenDinBeregnetDtoSelectors.yrkesskade
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.VedleggSlikErUforetrygdenDinBeregnetDtoSelectors.beregnetFlyktningsstatus
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.VedleggSlikErUforetrygdenDinBeregnetDtoSelectors.beregningsmetode

import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.VedleggSlikErUforetrygdenDinBeregnetDtoSelectors.YrkesskadeSelectors.yrkesskadegrad
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.VedleggSlikErUforetrygdenDinBeregnetDtoSelectors.YrkesskadeSelectors.inntektForYrkesskade




import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.LocalizedFormatter.CurrencyFormat
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
val createVedleggSlikErUforetrygdenDinBeregnet
    = createAttachment<LangBokmalNynorsk, VedleggSlikErUforetrygdenDinBeregnetDto> (
        title = newText(
            Bokmal to "Slik er uføretrygden din beregnet ",
            Nynorsk to "",
        ),
        includeSakspart = false,
    ) {

        paragraph {
            list {
                item {
                    textExpr(
                        Bokmal to "Opplysninger vi har brukt i beregningen fra ".expr() + virkningFom.format() + ". ",
                        Nynorsk to "".expr(),
                    )
                }
                item {
                    textExpr(
                        Bokmal to "Folketrygdens grunnbeløp (G) benyttet i beregningen er ".expr() + grunnbelop.format() + " kroner. ",
                        Nynorsk to "".expr(),
                    )
                }
                item {
                    text(
                        Bokmal to "Alle beløp vi har brukt i dette vedlegget er før skatt (brutto). ",
                        Nynorsk to "",
                    )
                }
            }
        }

        paragraph {
            table(header = {
                column(1) {
                    text(
                        Bokmal to "Opplysninger",
                        Nynorsk to "",
                    )
                }
                column(1) {}
            }) {
                row {
                    cell {
                        text(
                            Bokmal to "Uføretidspunkt ",
                            Nynorsk to "",
                        )
                    }
                    cell {
                        textExpr(
                            Bokmal to uforetidspunkt.format(),
                            Nynorsk to "".expr(),
                        )
                    }
                }
                row {
                    cell {
                        text(
                            Bokmal to "Beregningsgrunnlag (din oppjusterte inntekt før uføretidspunktet)",
                            Nynorsk to "",
                        )
                    }
                    cell {
                        textExpr(
                            Bokmal to oifu.format(),
                            Nynorsk to "".expr(),
                        )
                    }
                }
                row {
                    cell {
                        text(
                            Bokmal to "Inntekt før uførhet ",
                            Nynorsk to "",
                        )
                    }
                    cell {
                        textExpr(
                            Bokmal to ifu.format(),
                            Nynorsk to "".expr(),
                        )
                    }
                }
                row {
                    cell {
                        text(
                            Bokmal to "Inntekt etter uførhet ",
                            Nynorsk to "",
                        )
                    }
                    cell {
                        textExpr(
                            Bokmal to ieu.format(),
                            Nynorsk to "".expr(),
                        )
                    }
                }
                row {
                    cell {
                        text(
                            Bokmal to "Uføregrad ",
                            Nynorsk to "",
                        )
                    }
                    cell {
                        textExpr(
                            Bokmal to uforegrad.format(),
                            Nynorsk to "".expr(),
                        )
                    }
                }
                row {
                    cell {
                        text(
                            Bokmal to "Inntektsgrense ",
                            Nynorsk to "",
                        )
                    }
                    cell {
                        textExpr(
                            Bokmal to inntektsgrense.format(),
                            Nynorsk to "".expr(),
                        )
                    }
                }
                row {
                    cell {
                        text(
                            Bokmal to "Forventet inntekt ",
                            Nynorsk to "",
                        )
                    }
                    cell {
                        textExpr(
                            Bokmal to forventetInntekt.format(),
                            Nynorsk to "".expr(),
                        )
                    }
                }
                row {
                    cell {
                        text(
                            Bokmal to "Kompensasjonsgrad ",
                            Nynorsk to "",
                        )
                    }
                    cell {
                        textExpr(
                            Bokmal to kompensasjonsgrad.format(),
                            Nynorsk to "".expr(),
                        )
                    }
                }
                row {
                    cell {
                        text(
                            Bokmal to "Inntekt som medfører at uføretrygden ikke blir utbetalt ",
                            Nynorsk to "",
                        )
                    }
                    cell {
                        textExpr(
                            Bokmal to inntektstak.format(),
                            Nynorsk to "".expr(),
                        )
                    }
                }
                row {
                    cell {
                        text(
                            Bokmal to "Inntekt som medfører at uføretrygden ikke blir utbetalt ",
                            Nynorsk to "",
                        )
                    }
                    cell {
                        textExpr(
                            Bokmal to inntektstak.format(),
                            Nynorsk to "".expr(),
                        )
                    }
                }
                row {
                    cell {
                        text(
                            Bokmal to "Sivilstatus lagt til grunn ved beregning ",
                            Nynorsk to "",
                        )
                    }
                    cell {
                        textExpr(
                            Bokmal to sivilstand,
                            Nynorsk to "".expr(),
                        )
                    }
                }
                showIf(giftLeverAdskilt) {
                    row {
                        cell {
                            text(
                                Bokmal to "Du eller ektefellen er registrert med annet bosted, eller er på institusjon ",
                                Nynorsk to "",
                            )
                        }
                        cell {
                            text(
                                Bokmal to "Ja",
                                Nynorsk to "",
                            )
                        }
                    }
                }
                showIf(partnerLeverAdskilt) {
                    row {
                        cell {
                            text(
                                Bokmal to "Du eller partneren er registrert med annet bosted, eller er på institusjon ",
                                Nynorsk to "",
                            )
                        }
                        cell {
                            text(
                                Bokmal to "Ja",
                                Nynorsk to "",
                            )
                        }
                    }
                }
                showIf(ungUfor) {
                    row {
                        cell {
                            text(
                                Bokmal to "Ung ufør ",
                                Nynorsk to "",
                            )
                        }
                        cell {
                            text(
                                Bokmal to "Ja",
                                Nynorsk to "",
                            )
                        }
                    }
                }
                ifNotNull(yrkesskade) {
                    row {
                        cell {
                            text(
                                Bokmal to "Yrkesskadegrad ",
                                Nynorsk to "",
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to it.yrkesskadegrad.format(),
                                Nynorsk to "".expr(),
                            )
                        }
                    }
                    row {
                        cell {
                            text(
                                Bokmal to "Årlig arbeidsinntekt for yrkesskaden ",
                                Nynorsk to "",
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to it.inntektForYrkesskade.format(),
                                Nynorsk to "".expr(),
                            )
                        }
                    }
                }

                showIf(beregnetFlyktningsstatus) {
                    row {
                        cell {
                            text(
                                Bokmal to "Du er innvilget flyktningstatus fra UDI ",
                                Nynorsk to "",
                            )
                        }
                        cell {
                            text(
                                Bokmal to "Ja",
                                Nynorsk to "",
                            )
                        }
                    }
                }
                row {
                    cell {
                        showIf(beregningsmetode.equalTo(Beregningsmetode.FOLKETRYGD)) {
                            text(
                                Bokmal to "Trygdetid (maksimalt 40 år) ",
                                Nynorsk to "",
                            )
                        }.orShowIf(
                            beregningsmetode.equalTo(Beregningsmetode.EOS) or beregningsmetode.equalTo(
                                Beregningsmetode.NORDISK
                            )
                        ) {
                            text(
                                Bokmal to "Teoretisk trygdetid i Norge og andre EØS-land som er brukt i beregningen (maksimalt 40 år)",
                                Nynorsk to "",
                            )
                        }.orShow {
                            text(
                                Bokmal to "Teoretisk trygdetid i Norge og andre avtaleland som er brukt i beregningen (maksimalt 40 år",
                                Nynorsk to "",
                            )
                        }
                    }
                    cell {
                        textExpr(
                            Bokmal to trygdetid.anvendt.format(),
                            Nynorsk to "".expr(),
                        )
                    }
                }
                showIf(beregningsmetode.notEqualTo(Beregningsmetode.FOLKETRYGD)) {
                    row {
                        cell {
                            text(
                                Bokmal to "Faktisk trygdetid i Norge",
                                Nynorsk to "",
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to trygdetid.faktisk.format(),
                                Nynorsk to "".expr(),
                            )
                        }
                    }

                }
                showIf(beregningsmetode.equalTo(Beregningsmetode.EOS)) {
                    row {
                        cell {
                            text(
                                Bokmal to "Faktisk trygdetid i Norge",
                                Nynorsk to "",
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to trygdetid.faktisk.format(),
                                Nynorsk to "".expr(),
                            )
                        }
                    }

                }
                ifNotNull(trygdetid.faktiskEos) { faktiskTtEos ->
                    showIf(beregningsmetode.equalTo(Beregningsmetode.EOS)) {
                        row {
                            cell {
                                text(
                                    Bokmal to "Faktisk trygdetid i andre EØS-land",
                                    Nynorsk to "",
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to faktiskTtEos.format(),
                                    Nynorsk to "".expr(),
                                )
                            }
                        }

                    }
                    showIf(beregningsmetode.equalTo(Beregningsmetode.EOS)) {
                        row {
                            cell {
                                text(
                                    Bokmal to "Faktisk trygdetid i Norge og EØS-land (maksimalt 40 år)",
                                    Nynorsk to "",
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to faktiskTtEos.plus(trygdetid.faktisk).format(),
                                    Nynorsk to "".expr(),
                                )
                            }
                        }

                    }
                }
                showIf(beregningsmetode.equalTo(Beregningsmetode.EOS)) {
                    row {
                        cell {
                            text(
                                Bokmal to "Forholdstallet brukt i beregning av trygdetid",
                                Nynorsk to "",
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to trygdetid.forholdstall.teller.format() +" / " +  trygdetid.forholdstall.nevner.format(),
                                Nynorsk to "".expr(),
                            )
                        }
                    }
                }
                ifNotNull(trygdetid.faktiskNordisk) { nordiskTt ->
                    showIf(beregningsmetode.equalTo(Beregningsmetode.NORDISK)) {
                        row {
                            cell {
                                text(
                                    Bokmal to "Faktisk trygdetid i annet nordisk land som brukes i beregning av framtidig trygdetid",
                                    Nynorsk to "",
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to nordiskTt.format(),
                                    Nynorsk to "".expr(),
                                )
                            }
                        }
                    }
                }
                showIf((beregningsmetode.equalTo(Beregningsmetode.FOLKETRYGD) or beregningsmetode.equalTo(Beregningsmetode.NORDISK)) and trygdetid.faktisk.lessThan(40)) {
                    row {
                        cell {
                            text(
                                Bokmal to "Norsk fremtidig trygdetid ",
                                Nynorsk to "",
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to trygdetid.faktisk.format(),
                                Nynorsk to "".expr(),
                            )
                        }
                    }
                }
                showIf(beregningsmetode.equalTo(Beregningsmetode.NORDISK)) {
                    row {
                        cell {
                            text(
                                Bokmal to "Forholdstallet brukt i reduksjon av norsk framtidig trygdetid",
                                Nynorsk to "",
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to trygdetid.forholdstallNordisk.teller.format() +" / " +  trygdetid.forholdstall.nevner.format(),
                                Nynorsk to "".expr(),
                            )
                        }
                    }
                }


                row {
                    cell {
                        text(
                            Bokmal to "",
                            Nynorsk to "",
                        )
                    }
                    cell {
                        textExpr(
                            Bokmal to ,
                            Nynorsk to "".expr(),
                        )
                    }
                }
                row {
                    cell {
                        text(
                            Bokmal to "",
                            Nynorsk to "",
                        )
                    }
                    cell {
                        textExpr(
                            Bokmal to "".expr(),
                            Nynorsk to "".expr(),
                        )
                    }
                }
                row {
                    cell {
                        text(
                            Bokmal to "",
                            Nynorsk to "",
                        )
                    }
                    cell {
                        textExpr(
                            Bokmal to ,
                            Nynorsk to "".expr(),
                        )
                    }
                }

            }
        }

    }
