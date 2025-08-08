package no.nav.pensjon.brev.maler.alder.omregning.opptjening.fraser

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.maler.alderApi.BeregnetPensjonPerMaaned
import no.nav.pensjon.brev.api.model.maler.alderApi.BeregnetPensjonPerMaanedSelectors.antallBeregningsperioderPensjon
import no.nav.pensjon.brev.api.model.maler.alderApi.BeregnetPensjonPerMaanedSelectors.garantipensjonInnvilget
import no.nav.pensjon.brev.api.model.maler.alderApi.BeregnetPensjonPerMaanedSelectors.gjenlevenderettAnvendt
import no.nav.pensjon.brev.api.model.maler.alderApi.BeregnetPensjonPerMaanedSelectors.minstenivaIndividuellInnvilget
import no.nav.pensjon.brev.api.model.maler.alderApi.BeregnetPensjonPerMaanedSelectors.minstenivaPensjonistParInnvilget
import no.nav.pensjon.brev.api.model.maler.alderApi.BeregnetPensjonPerMaanedSelectors.pensjonstilleggInnvilget
import no.nav.pensjon.brev.api.model.maler.alderApi.BeregnetPensjonPerMaanedSelectors.totalPensjon
import no.nav.pensjon.brev.api.model.maler.alderApi.BeregnetPensjonPerMaanedSelectors.virkFom
import no.nav.pensjon.brev.api.model.maler.alderApi.Opptjening
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

data class AvsnittBeskrivelse(
    val opptjening: Expression<Opptjening>,
    val sisteGyldigeOpptjeningsAar: Expression<Int>,
    val antallAarEndretOpptjening: Expression<Int>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(opptjening.equalTo(Opptjening.TILVEKST)) {
            paragraph {
                textExpr(
                    Language.Bokmal to "Skatteoppgjøret for ".expr() + sisteGyldigeOpptjeningsAar.format() + " er klart og den nye opptjeningen er lagt til alderspensjonen din.",
                    Language.Nynorsk to "Skatteoppgjeret for ".expr() + sisteGyldigeOpptjeningsAar.format() + " er klart og den nye oppteninga er lagt til alderspensjonen din.",
                    Language.English to "The final tax settlement for ".expr() + sisteGyldigeOpptjeningsAar.format() + " has been completed and the new pension earnings have been added to your retirement pension.",
                )
            }
        }
        showIf(opptjening.equalTo(Opptjening.KORRIGERING)) {
            showIf(antallAarEndretOpptjening.equalTo(0)) {
                paragraph {
                    text(
                        Language.Bokmal to "Pensjonsopptjeningen er endret.",
                        Language.Nynorsk to "Pensjonsopptjeningen er endret.",
                        Language.English to "The pension earnings have been changed."
                    )
                }
            }
            paragraph {
                text(
                    Language.Bokmal to "Pensjonsopptjeningen din er endret for: ",
                    Language.Nynorsk to "Pensjonsoppteninga di er endra for: ",
                    Language.English to "Your pension earnings have been changed for the following income year(-s): "
                )
            }
            // todo list years..
        }
    }
}

data class AvsnittEndringPensjon(
    val belopEndring: Expression<String>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(belopEndring.equalTo("ENDR_OKT")) {
            paragraph {
                text(
                    Language.Bokmal to "Dette fører til at pensjonen din øker.",
                    Language.Nynorsk to "Dette fører til at pensjonen din aukar.",
                    Language.English to "This leads to an increase in your retirement pension."
                )
            }
        }
        showIf(belopEndring.equalTo("ENDR_RED")) {
            paragraph {
                text(
                    Language.Bokmal to "Dette fører til at pensjonen din blir redusert.",
                    Language.Nynorsk to "Dette fører til at pensjonen din blir redusert.",
                    Language.English to "This leads to a reduction in your retirement pension."
                )
            }
        }
    }
}

data class AvsnittUtbetalingPerMaaned(
    val uforeKombinertMedAlder: Expression<Boolean>,
    val beregnetPensjonPerMaanedGjeldende: Expression<BeregnetPensjonPerMaaned>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(uforeKombinertMedAlder.not()) {
            paragraph {
                textExpr(
                    Language.Bokmal to "Du får ".expr() + beregnetPensjonPerMaanedGjeldende.totalPensjon.format() + " kroner før skatt fra " + beregnetPensjonPerMaanedGjeldende.virkFom.format() + " i alderspensjon fra folketrygden.",
                    Language.Nynorsk to "Du får ".expr() + beregnetPensjonPerMaanedGjeldende.totalPensjon.format() + " kroner før skatt frå " + beregnetPensjonPerMaanedGjeldende.virkFom.format() + " i alderspensjon frå folketrygda.",
                    Language.English to "You will receive NOK ".expr() + beregnetPensjonPerMaanedGjeldende.totalPensjon.format() + " before tax from " + beregnetPensjonPerMaanedGjeldende.virkFom.format() + " as retirement pension through the National Insurance Scheme.",
                )
            }
        }
        showIf(uforeKombinertMedAlder) {
            paragraph {
                textExpr(
                    Language.Bokmal to "Du får ".expr() + beregnetPensjonPerMaanedGjeldende.totalPensjon.format() + " kroner før skatt fra " + beregnetPensjonPerMaanedGjeldende.virkFom.format() + ". Du får alderspensjon i tillegg til uføretrygden din.",
                    Language.Nynorsk to "Du får ".expr() + beregnetPensjonPerMaanedGjeldende.totalPensjon.format() + " kroner før skatt frå " + beregnetPensjonPerMaanedGjeldende.virkFom.format() + ". Du får alderspensjon ved sida av uføretrygda di.",
                    Language.English to "You will receive NOK ".expr() + beregnetPensjonPerMaanedGjeldende.totalPensjon.format() + " before tax from " + beregnetPensjonPerMaanedGjeldende.virkFom.format() + ". You will receive retirement pension in addition to you disability benefit.",
                )
            }
        }

        paragraph {
            text(
                Language.Bokmal to "Hvis du har andre pensjonsytelser som for eksempel AFP eller tjenestepensjon, blir de utbetalt i tillegg til alderspensjonen. Alderspensjonen din utbetales innen den 20. hver måned. Du finner oversikt over utbetalingene dine på ${Constants.UTBETALINGER_URL}.",
                Language.Nynorsk to "Dersom du har andre pensjonsytingar som for eksempel AFP eller tenestepensjon, kjem slik utbetaling i tillegg til alderspensjonen. Alderspensjonen din blir betalt ut innan den 20. i kvar månad. Du finn meir informasjon om utbetalingane dine på ${Constants.UTBETALINGER_URL}.",
                Language.English to "If you have occupational pensions from other schemes, this will be paid in addition to your retirement pension. Your pension will be paid at the latest on the 20th of each month. See the more detailed information on what you will receive at ${Constants.UTBETALINGER_URL}."
            )
        }
    }
}

data class AvsnittFlereBeregningsperioder(
    val beregnetPensjonPerMaaned: Expression<BeregnetPensjonPerMaaned>,
    val beregnetPensjonPerMaanedVedVirk: Expression<BeregnetPensjonPerMaaned>,
    val regelverkType: Expression<AlderspensjonRegelverkType>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(
            beregnetPensjonPerMaaned.antallBeregningsperioderPensjon.greaterThan(1) and beregnetPensjonPerMaanedVedVirk.totalPensjon.greaterThan(
                0
            )
        ) {
            paragraph {
                text(
                    Language.Bokmal to "I vedlegget \"",
                    Language.Nynorsk to "I vedlegget \"",
                    Language.English to "In the appendix \"",
                )
                showIf(
                    regelverkType.equalTo(AlderspensjonRegelverkType.AP2011) or regelverkType.equalTo(
                        AlderspensjonRegelverkType.AP2016
                    )
                ) {
                    text(
                        Language.Bokmal to "Opplysninger brukt i beregningen",
                        Language.Nynorsk to "Opplysningar brukte i berekninga",
                        Language.English to "Information about your calculation",
                    )
                }
                showIf(regelverkType.equalTo(AlderspensjonRegelverkType.AP2025)) {
                    text(
                        Language.Bokmal to "Slik har vi beregnet pensjonen din",
                        Language.Nynorsk to "Slik har vi berekna pensjonen din",
                        Language.English to "This is how we have calculated your pension",
                    )
                }
                text(
                    Language.Bokmal to "\" finner du detaljer om din månedlige pensjon.",
                    Language.Nynorsk to "\" finn du detaljar om din månadlege pensjon.",
                    Language.English to "\" you will find more details about your monthly pension.",
                )
            }
        }
    }
}

data class AvsnittHjemmel(
    val opptjening: Expression<Opptjening>,
    val regelverkType: Expression<AlderspensjonRegelverkType>,
    val beregnetPensjonPerMaanedVedVirk: Expression<BeregnetPensjonPerMaaned>,
    val erFoerstegangsbehandling: Expression<Boolean>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(regelverkType.equalTo(AlderspensjonRegelverkType.AP2011)) {
            showIf(opptjening.equalTo(Opptjening.KORRIGERING)) {
                paragraph {
                    text(
                        Language.Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 3-15",
                        Language.Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 3-15",
                        Language.English to "This decision was made pursuant to the provisions of §§ 3-15"
                    )
                    showIf(beregnetPensjonPerMaanedVedVirk.pensjonstilleggInnvilget and beregnetPensjonPerMaanedVedVirk.minstenivaPensjonistParInnvilget) {
                        text(
                            Language.Bokmal to ", 19-8, 19-9",
                            Language.Nynorsk to ", 19-8, 19-9",
                            Language.English to ", 19-8, 19-9"
                        )
                    }
                    text(
                        Language.Bokmal to ", 19-13",
                        Language.Nynorsk to ", 19-13",
                        Language.English to ", 19-13"
                    )
                    showIf(beregnetPensjonPerMaanedVedVirk.minstenivaIndividuellInnvilget) {
                        text(
                            Language.Bokmal to ", 19-14",
                            Language.Nynorsk to ", 19-14",
                            Language.English to ", 19-14"
                        )
                    }
                    showIf(beregnetPensjonPerMaanedVedVirk.gjenlevenderettAnvendt) {
                        text(
                            Language.Bokmal to ", 19-16",
                            Language.Nynorsk to ", 19-16",
                            Language.English to ", 19-16"
                        )
                    }
                    text(
                        Language.Bokmal to " og 22-12.",
                        Language.Nynorsk to " og 22-12.",
                        Language.English to " and 22-12 of the National Insurance Act."
                    )
                }
            }.orShow {
                paragraph {
                    text(
                        Language.Bokmal to "Vedtaket er gjort etter folketrygdloven § 19-13.",
                        Language.Nynorsk to "Vedtaket er gjort etter folketrygdlova § 19-13.",
                        Language.English to "This decision was made pursuant to the provisions of § 19-13 of the National Insurance Act."
                    )
                }
            }
        }

        showIf(regelverkType.equalTo(AlderspensjonRegelverkType.AP2016)) {
            showIf(opptjening.equalTo(Opptjening.KORRIGERING)) {
                paragraph {
                    text(
                        Language.Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 3-15",
                        Language.Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 3-15",
                        Language.English to "This decision was made pursuant to the provisions of §§ 3-15"
                    )
                    showIf(beregnetPensjonPerMaanedVedVirk.pensjonstilleggInnvilget and beregnetPensjonPerMaanedVedVirk.minstenivaPensjonistParInnvilget) {
                        text(
                            Language.Bokmal to ", 19-8, 19-9",
                            Language.Nynorsk to ", 19-8, 19-9",
                            Language.English to ", 19-8, 19-9"
                        )
                    }
                    text(
                        Language.Bokmal to ", 19-13",
                        Language.Nynorsk to ", 19-13",
                        Language.English to ", 19-13"
                    )
                    showIf(beregnetPensjonPerMaanedVedVirk.minstenivaIndividuellInnvilget) {
                        text(
                            Language.Bokmal to ", 19-14",
                            Language.Nynorsk to ", 19-14",
                            Language.English to ", 19-14"
                        )
                    }
                    showIf(beregnetPensjonPerMaanedVedVirk.gjenlevenderettAnvendt) {
                        text(
                            Language.Bokmal to ", 19-16",
                            Language.Nynorsk to ", 19-16",
                            Language.English to ", 19-16"
                        )
                    }
                    showIf(beregnetPensjonPerMaanedVedVirk.garantipensjonInnvilget) {
                        text(
                            Language.Bokmal to ", 20-9",
                            Language.Nynorsk to ", 20-9",
                            Language.English to ", 20-9"
                        )
                    }
                    text(
                        Language.Bokmal to ", 22-17",
                        Language.Nynorsk to ", 20-17",
                        Language.English to ", 20-17"
                    )
                    showIf(beregnetPensjonPerMaanedVedVirk.minstenivaIndividuellInnvilget) {
                        text(
                            Language.Bokmal to ", 20-18",
                            Language.Nynorsk to ", 20-18",
                            Language.English to ", 20-18"
                        )
                    }
                    showIf(beregnetPensjonPerMaanedVedVirk.gjenlevenderettAnvendt) {
                        text(
                            Language.Bokmal to ", 20-19a",
                            Language.Nynorsk to ", 20-19a",
                            Language.English to ", 20-19a"
                        )
                    }
                    text(
                        Language.Bokmal to " og 22-12.",
                        Language.Nynorsk to " og 22-12.",
                        Language.English to " and 22-12 of the National Insurance Act."
                    )
                }
            }.orShow {
                paragraph {
                    text(
                        Language.Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 19-13, 19-15, 20-17 og 20-19.",
                        Language.Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 19-13, 19-15, 20-17 og 20-19.",
                        Language.English to "This decision was made pursuant to the provisions of §§ 19-13, 19-15, 20-17 and 20-19 of the National Insurance Act."
                    )
                }
            }
        }

        showIf(regelverkType.equalTo(AlderspensjonRegelverkType.AP2025)) {
            showIf(opptjening.equalTo(Opptjening.KORRIGERING)) {
                paragraph {
                    text(
                        Language.Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 3-15",
                        Language.Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 3-15",
                        Language.English to "This decision was made pursuant to the provisions of §§ 3-15"
                    )
                    showIf(beregnetPensjonPerMaanedVedVirk.gjenlevenderettAnvendt) {
                        text(
                            Language.Bokmal to ", 19-16",
                            Language.Nynorsk to ", 19-16",
                            Language.English to ", 19-16"
                        )
                    }
                    showIf(beregnetPensjonPerMaanedVedVirk.garantipensjonInnvilget) {
                        text(
                            Language.Bokmal to ", 20-9",
                            Language.Nynorsk to ", 20-9",
                            Language.English to ", 20-9"
                        )
                    }
                    text(
                        Language.Bokmal to ", 22-17",
                        Language.Nynorsk to ", 20-17",
                        Language.English to ", 20-17"
                    )
                    showIf(beregnetPensjonPerMaanedVedVirk.minstenivaIndividuellInnvilget) {
                        text(
                            Language.Bokmal to ", 20-18",
                            Language.Nynorsk to ", 20-18",
                            Language.English to ", 20-18"
                        )
                    }
                    text(
                        Language.Bokmal to " og 22-12.",
                        Language.Nynorsk to " og 22-12.",
                        Language.English to " and 22-12 of the National Insurance Act."
                    )
                }
            }.orShow {
                paragraph {
                    showIf(erFoerstegangsbehandling) {
                        text(
                            Language.Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 20-2, 20-3, 20-9 til 20-15, 22-12 og 22-13.",
                            Language.Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 20-2, 20-3, 20-9 til 20-15, 22-12 og 22-13.",
                            Language.English to "This decision was made pursuant to the provisions of §§ 20-2, 20-3, 20-9 to 20-15, 22-12 and 22-13 of the National Insurance Act."
                        )
                    }.orShow {
                        text(
                            Language.Bokmal to "Vedtaket er gjort etter folketrygdloven § 20-17.",
                            Language.Nynorsk to "Vedtaket er gjort etter folketrygdlova § 20-17.",
                            Language.English to "This decision was made pursuant to the provision of § 20-17 of the National Insurance Act."
                        )
                    }
                }
            }
        }
    }
}

data class AvsnittBegrunnelseForVedtaket(
    val opptjening: Expression<Opptjening>,
    val antallAarEndretOpptjening: Expression<Int>,
    val regelverkType: Expression<AlderspensjonRegelverkType>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title2 {
            text(
                Language.Bokmal to "Begrunnelse for vedtaket",
                Language.Nynorsk to "Grunngiving for vedtaket",
                Language.English to "Grounds for the decision"
            )
        }

        paragraph {
            showIf(opptjening.equalTo(Opptjening.TILVEKST)) {
                text(
                    Language.Bokmal to "Pensjonsopptjeningen din kan være endret av en eller flere grunner:",
                    Language.Nynorsk to "Pensjonsoppteninga di kan vere endra av ein eller fleire grunnar:",
                    Language.English to "There can be several reasons why your pension earnings have been changed:"
                )
                list {
                    item {
                        text(
                            Language.Bokmal to "Du har fått medregnet inntekten din for dette året.",
                            Language.Nynorsk to "Du har fått rekna med inntekta di for dette året.",
                            Language.English to "Your pensionable income for this year has been added to your pension reserves."
                        )
                        text(
                            Language.Bokmal to "Du har fått medregnet omsorgsopptjening for dette året fordi du har hatt omsorg for små barn eller pleietrengende personer.",
                            Language.Nynorsk to "Du har fått rekna med omsorgsopptening for dette året fordi du har hatt omsorg for små barn eller pleietrengande personar.",
                            Language.English to "You have been granted pension rights for unpaid care work. (Care for sick, disabled or elderly people, or care for children under the age of six years.)"
                        )
                        text(
                            Language.Bokmal to "Du har fått lagt til trygdetid for dette året.",
                            Language.Nynorsk to "Du har fått lagt til trygdetid for dette året.",
                            Language.English to "You have been granted national insurance coverage for this year."
                        )
                    }
                }

                text(
                    Language.Bokmal to "Du kan finne mer informasjon i vedlegget \"",
                    Language.Nynorsk to "Du kan finne meir informasjon i vedlegget \"",
                    Language.English to "You will find more information in the appendix \""
                )
                showIf(
                    regelverkType.equalTo(AlderspensjonRegelverkType.AP2011) or regelverkType.equalTo(
                        AlderspensjonRegelverkType.AP2016
                    )
                ) {
                    text(
                        Language.Bokmal to "Opplysninger brukt i beregningen",
                        Language.Nynorsk to "Opplysningar brukte i berekninga",
                        Language.English to "Information about your calculation",
                    )
                }
                showIf(regelverkType.equalTo(AlderspensjonRegelverkType.AP2025)) {
                    text(
                        Language.Bokmal to "Slik har vi beregnet pensjonen din",
                        Language.Nynorsk to "Slik har vi berekna pensjonen din",
                        Language.English to "This is how we have calculated your pension",
                    )
                }
                text(
                    Language.Bokmal to "\".",
                    Language.Nynorsk to "\".",
                    Language.English to "\".",
                )
            }
            showIf(opptjening.equalTo(Opptjening.KORRIGERING) and antallAarEndretOpptjening.greaterThan(0)) {
                text(
                    Language.Bokmal to "Pensjonsopptjeningen kan være endret av flere grunner:",
                    Language.Nynorsk to "Pensjonsoppteninga kan vere endra av fleire grunnar:",
                    Language.English to "There can be several reasons why your pension earnings have been changed: "
                )
                list {
                    item {
                        text(
                            Language.Bokmal to "Skatteetaten har endret skatteoppgjøret ditt.",
                            Language.Nynorsk to "Skatteetaten har endra skatteoppgjeret.",
                            Language.English to "The Norwegian Tax Administration has amended one or several tax returns."
                        )
                        text(
                            Language.Bokmal to "Skatteetaten har endret din pensjonsgivende inntekt.",
                            Language.Nynorsk to "Skatteetaten har endra den pensjonsgivande inntekta di.",
                            Language.English to "The Norwegian Tax Administration has amended your pensionable income"
                        )
                        text(
                            Language.Bokmal to "Du har fått ny eller endret omsorgsopptjening.",
                            Language.Nynorsk to "Du har fått ny eller endra omsorgsopptening.",
                            Language.English to "You have been granted pension rights for unpaid care work, or this pension rights have been changed. (Care for sick, disabled or elderly people, or care for children under the age of six years.)"
                        )
                    }
                }
            }
            showIf(opptjening.equalTo(Opptjening.KORRIGERING) and antallAarEndretOpptjening.equalTo(0)) {
                text(
                    Language.Bokmal to "Pensjonsopptjeningen til den avdøde er endret.",
                    Language.Nynorsk to "Pensjonsoppteninga til den avdøde er endra.",
                    Language.English to "The accumulated rights earned by the deceased have been changed."
                )
            }
        }
    }
}