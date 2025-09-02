package no.nav.pensjon.brev.maler.alder.omregning.opptjening.fraser

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.BeloepEndring
import no.nav.pensjon.brev.api.model.maler.alderApi.*
import no.nav.pensjon.brev.api.model.maler.alderApi.BeregnetPensjonPerMaanedGjeldendeSelectors.totalPensjon
import no.nav.pensjon.brev.api.model.maler.alderApi.BeregnetPensjonPerMaanedGjeldendeSelectors.virkFom
import no.nav.pensjon.brev.api.model.maler.alderApi.BeregnetPensjonPerMaanedSelectors.antallBeregningsperioderPensjon
import no.nav.pensjon.brev.api.model.maler.alderApi.BeregnetPensjonPerMaanedVedVirkSelectors.garantipensjonInnvilget
import no.nav.pensjon.brev.api.model.maler.alderApi.BeregnetPensjonPerMaanedVedVirkSelectors.gjenlevenderettAnvendt
import no.nav.pensjon.brev.api.model.maler.alderApi.BeregnetPensjonPerMaanedVedVirkSelectors.minstenivaIndividuellInnvilget
import no.nav.pensjon.brev.api.model.maler.alderApi.BeregnetPensjonPerMaanedVedVirkSelectors.minstenivaPensjonistParInnvilget
import no.nav.pensjon.brev.api.model.maler.alderApi.BeregnetPensjonPerMaanedVedVirkSelectors.pensjonstilleggInnvilget
import no.nav.pensjon.brev.api.model.maler.alderApi.BeregnetPensjonPerMaanedVedVirkSelectors.totalPensjon
import no.nav.pensjon.brev.api.model.maler.alderApi.OpptjeningSelectors.antallAarEndretOpptjening
import no.nav.pensjon.brev.api.model.maler.alderApi.OpptjeningSelectors.endretOpptjeningsAar
import no.nav.pensjon.brev.api.model.maler.alderApi.OpptjeningSelectors.sisteGyldigeOpptjeningsAar
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.common.Constants.ALDERSPENSJON
import no.nav.pensjon.brev.maler.fraser.common.Constants.DIN_PENSJON_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.DITT_NAV
import no.nav.pensjon.brev.maler.fraser.common.Constants.SKATTEETATEN_PENSJONIST_URL
import no.nav.pensjon.brev.maler.vedlegg.vedleggOpplysningerBruktIBeregningenAlder
import no.nav.pensjon.brev.maler.vedlegg.vedleggOpplysningerBruktIBeregningenAlderAP2025
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import java.time.LocalDate

data class AvsnittBeskrivelse(
    val opptjeningType: Expression<OpptjeningType>,
    val opptjening: Expression<Opptjening>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title2 {
            text(
                Language.Bokmal to "Vedtak",
                Language.Nynorsk to "Vedtak",
                Language.English to "Decision",
            )
        }
        showIf(opptjeningType.equalTo(OpptjeningType.TILVEKST)) {
            ifNotNull(opptjening.sisteGyldigeOpptjeningsAar) { sisteGyldigeOpptjeningsAar ->
                paragraph {
                    textExpr(
                        Language.Bokmal to "Skatteoppgjøret for ".expr() + sisteGyldigeOpptjeningsAar.format() + " er klart og den nye opptjeningen er lagt til alderspensjonen din.",
                        Language.Nynorsk to "Skatteoppgjeret for ".expr() + sisteGyldigeOpptjeningsAar.format() + " er klart og den nye oppteninga er lagt til alderspensjonen din.",
                        Language.English to "The final tax settlement for ".expr() + sisteGyldigeOpptjeningsAar.format() + " has been completed and the new pension earnings have been added to your retirement pension.",
                    )
                }
            }
        }
        showIf(opptjeningType.equalTo(OpptjeningType.KORRIGERING)) {
            showIf(opptjening.antallAarEndretOpptjening.equalTo(0)) {
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
                newline()
                list {
                    forEach(opptjening.endretOpptjeningsAar) { aar ->
                        item {
                            textExpr(
                                Language.Bokmal to aar.format(),
                                Language.Nynorsk to aar.format(),
                                Language.English to aar.format()
                            )
                        }
                    }
                }
            }
        }
    }
}

data class AvsnittEndringPensjon(
    val belopEndring: Expression<BeloepEndring>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(belopEndring.equalTo(BeloepEndring.ENDR_OKT)) {
            paragraph {
                text(
                    Language.Bokmal to "Dette fører til at pensjonen din øker.",
                    Language.Nynorsk to "Dette fører til at pensjonen din aukar.",
                    Language.English to "This leads to an increase in your retirement pension."
                )
            }
        }
        showIf(belopEndring.equalTo(BeloepEndring.ENDR_RED)) {
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
    val beregnetPensjonPerMaanedGjeldende: Expression<BeregnetPensjonPerMaanedGjeldende>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(uforeKombinertMedAlder.not()) {
            paragraph {
                textExpr(
                    Language.Bokmal to "Du får ".expr() + beregnetPensjonPerMaanedGjeldende.totalPensjon.format() + " før skatt fra " + beregnetPensjonPerMaanedGjeldende.virkFom.format() + " i alderspensjon fra folketrygden.",
                    Language.Nynorsk to "Du får ".expr() + beregnetPensjonPerMaanedGjeldende.totalPensjon.format() + " før skatt frå " + beregnetPensjonPerMaanedGjeldende.virkFom.format() + " i alderspensjon frå folketrygda.",
                    Language.English to "You will receive ".expr() + beregnetPensjonPerMaanedGjeldende.totalPensjon.format() + " before tax from " + beregnetPensjonPerMaanedGjeldende.virkFom.format() + " as retirement pension through the National Insurance Scheme.",
                )
            }
        }
        showIf(uforeKombinertMedAlder) {
            paragraph {
                textExpr(
                    Language.Bokmal to "Du får ".expr() + beregnetPensjonPerMaanedGjeldende.totalPensjon.format() + " før skatt fra " + beregnetPensjonPerMaanedGjeldende.virkFom.format() + ". Du får alderspensjon i tillegg til uføretrygden din.",
                    Language.Nynorsk to "Du får ".expr() + beregnetPensjonPerMaanedGjeldende.totalPensjon.format() + " før skatt frå " + beregnetPensjonPerMaanedGjeldende.virkFom.format() + ". Du får alderspensjon ved sida av uføretrygda di.",
                    Language.English to "You will receive ".expr() + beregnetPensjonPerMaanedGjeldende.totalPensjon.format() + " before tax from " + beregnetPensjonPerMaanedGjeldende.virkFom.format() + ". You will receive retirement pension in addition to you disability benefit.",
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
    val beregnetPensjonPerMaanedVedVirk: Expression<BeregnetPensjonPerMaanedVedVirk>,
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
                    Language.Bokmal to "I vedlegget ",
                    Language.Nynorsk to "I vedlegget ",
                    Language.English to "In the appendix ",
                )
                showIf(regelverkType.isOneOf(AlderspensjonRegelverkType.AP2011, AlderspensjonRegelverkType.AP2016)) {
                    namedReference(vedleggOpplysningerBruktIBeregningenAlder)
                }.orShowIf(regelverkType.equalTo(AlderspensjonRegelverkType.AP2025)) {
                    namedReference(vedleggOpplysningerBruktIBeregningenAlderAP2025)
                }
                text(
                    Language.Bokmal to " finner du detaljer om din månedlige pensjon.",
                    Language.Nynorsk to " finn du detaljar om din månadlege pensjon.",
                    Language.English to " you will find more details about your monthly pension.",
                )
            }
        }
    }
}

data class AvsnittHjemmel(
    val opptjeningType: Expression<OpptjeningType>,
    val regelverkType: Expression<AlderspensjonRegelverkType>,
    val beregnetPensjonPerMaanedVedVirk: Expression<BeregnetPensjonPerMaanedVedVirk>,
    val erFoerstegangsbehandling: Expression<Boolean>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(regelverkType.equalTo(AlderspensjonRegelverkType.AP2011)) {
            showIf(opptjeningType.equalTo(OpptjeningType.KORRIGERING)) {
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
            showIf(opptjeningType.equalTo(OpptjeningType.KORRIGERING)) {
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
                        Language.Bokmal to ", 20-17",
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
            showIf(opptjeningType.equalTo(OpptjeningType.KORRIGERING)) {
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
                        Language.Bokmal to ", 20-17",
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
    val opptjeningType: Expression<OpptjeningType>,
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
            showIf(opptjeningType.equalTo(OpptjeningType.TILVEKST)) {
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
                    }
                    item {
                        text(
                            Language.Bokmal to "Du har fått medregnet omsorgsopptjening for dette året fordi du har hatt omsorg for små barn eller pleietrengende personer.",
                            Language.Nynorsk to "Du har fått rekna med omsorgsopptening for dette året fordi du har hatt omsorg for små barn eller pleietrengande personar.",
                            Language.English to "You have been granted pension rights for unpaid care work. (Care for sick, disabled or elderly people, or care for children under the age of six years.)"
                        )
                    }
                    item {
                        text(
                            Language.Bokmal to "Du har fått lagt til trygdetid for dette året.",
                            Language.Nynorsk to "Du har fått lagt til trygdetid for dette året.",
                            Language.English to "You have been granted national insurance coverage for this year."
                        )
                    }
                }
            }

            text(
                Language.Bokmal to "Du kan finne mer informasjon i vedlegget ",
                Language.Nynorsk to "Du kan finne meir informasjon i vedlegget ",
                Language.English to "You will find more information in the appendix "
            )
            showIf(
                regelverkType.isOneOf(AlderspensjonRegelverkType.AP2011, AlderspensjonRegelverkType.AP2016)) {
                namedReference(vedleggOpplysningerBruktIBeregningenAlder)
            }
            showIf(regelverkType.equalTo(AlderspensjonRegelverkType.AP2025)) {
                namedReference(vedleggOpplysningerBruktIBeregningenAlderAP2025)
            }
            text(
                Language.Bokmal to ".",
                Language.Nynorsk to ".",
                Language.English to ".",
            )
        }
        showIf(opptjeningType.equalTo(OpptjeningType.KORRIGERING) and antallAarEndretOpptjening.greaterThan(0)) {
            paragraph {
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
                    }
                    item {
                        text(
                            Language.Bokmal to "Skatteetaten har endret din pensjonsgivende inntekt.",
                            Language.Nynorsk to "Skatteetaten har endra den pensjonsgivande inntekta di.",
                            Language.English to "The Norwegian Tax Administration has amended your pensionable income"
                        )
                    }
                    item {
                        text(
                            Language.Bokmal to "Du har fått ny eller endret omsorgsopptjening.",
                            Language.Nynorsk to "Du har fått ny eller endra omsorgsopptening.",
                            Language.English to "You have been granted pension rights for unpaid care work, or this pension rights have been changed. (Care for sick, disabled or elderly people, or care for children under the age of six years.)"
                        )

                    }
                }
            }
        }

        showIf(opptjeningType.equalTo(OpptjeningType.KORRIGERING) and antallAarEndretOpptjening.equalTo(0)) {
            paragraph {
                text(
                    Language.Bokmal to "Pensjonsopptjeningen til den avdøde er endret.",
                    Language.Nynorsk to "Pensjonsoppteninga til den avdøde er endra.",
                    Language.English to "The accumulated rights earned by the deceased have been changed."
                )
            }
        }
    }
}


data class AvsnittEtterbetaling(
    val virkFom: Expression<LocalDate>,
    val opptjeningType: Expression<OpptjeningType>,
    val beloepEndring: Expression<BeloepEndring>,
    val antallAarEndretOpptjening: Expression<Int>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(
            opptjeningType.equalTo(OpptjeningType.KORRIGERING) and
                    beloepEndring.equalTo(BeloepEndring.ENDR_OKT) and
                    antallAarEndretOpptjening.greaterThan(0) and
                    virkFom.greaterThan(LocalDate.now())
        ) {
            title2 {
                text(
                    Language.Bokmal to "Etterbetaling",
                    Language.Nynorsk to "Etterbetaling",
                    Language.English to "Retroactive payment"
                )
            }
            paragraph {
                textExpr(
                    Language.Bokmal to "Du får etterbetalt pensjon fra ".expr() + virkFom.format() + ". Etterbetalingen vil vanligvis bli utbetalt i løpet av syv virkedager. Du kan sjekke eventuelle fradrag i utbetalingsmeldingen på $DITT_NAV.",
                    Language.Nynorsk to "Du får etterbetalt pensjon frå ".expr() + virkFom.format() + ". Etterbetalinga blir vanlegvis betalt ut i løpet av sju yrkedagar. Du kan sjekke eventuelle frådrag i utbetalingsmeldinga på $DITT_NAV.",
                    Language.English to "You will receive retroactive pension payments from ".expr() + virkFom.format() + ". The retroactive payments will normally be made in the course of seven working days. You can check if there are any deductions from the payment notice at $DITT_NAV."
                )
            }

            paragraph {
                text(
                    Language.Bokmal to "Hvis etterbetalingen gjelder tidligere år, trekker vi skatt etter skatteetatens standardsatser.",
                    Language.Nynorsk to "Dersom etterbetalinga gjeld tidlegare år, vil vi trekkje skatt etter standardsatsane til skatteetaten.",
                    Language.English to "If the retroactive payment refers to earlier years, we will deduct tax at the Tax Administration's standard rates."
                )
            }
        }
    }
}

data class AvsnittSkattApEndring(
    val borINorge: Expression<Boolean>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(borINorge) {
            title2 {
                text(
                    Language.Bokmal to "Endring av alderspensjon kan ha betydning for skatt",
                    Language.Nynorsk to "Endring av alderspensjon kan ha betyding for skatt",
                    Language.English to "The change in your retirement pension may affect how much tax you pay"
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Du bør kontrollere om skattekortet ditt er riktig når alderspensjonen blir endret. Dette kan du gjøre selv på $SKATTEETATEN_PENSJONIST_URL. Der får du også mer informasjon om skattekort for pensjonister.",
                    Language.Nynorsk to "Du bør kontrollere om skattekortet ditt er riktig når alderspensjonen blir endra. Dette kan du gjere sjølv på $SKATTEETATEN_PENSJONIST_URL. Der får du også meir informasjon om skattekort for pensjonistar.",
                    Language.English to "When your retirement pension has been changed, you should check if your tax deduction card is correctly calculated. You can change your tax card by logging on to $SKATTEETATEN_PENSJONIST_URL. There you will find more information regarding tax deduction card for pensioners."
                )
            }

            paragraph {
                text(
                    Language.Bokmal to "På $DIN_PENSJON_URL får du vite hva du betaler i skatt. Her kan du også legge inn ekstra skattetrekk om du ønsker det. Dersom du endrer skattetrekket vil dette gjelde fra måneden etter at vi har fått beskjed.",
                    Language.Nynorsk to "På $DIN_PENSJON_URL får du vite kva du betaler i skatt. Her kan du også leggje inn tilleggsskatt om du ønskjer det. Dersom du endrar skattetrekket, vil dette gjelde frå månaden etter at vi har fått beskjed.",
                    Language.English to "At $DIN_PENSJON_URL you can see how much tax you are paying. Here you can also add surtax, if you want. If you change your income tax rate, this will be applied from the month after we have been notified of the change."
                )
            }
        }
    }
}

data class AvsnittArbeidsinntekt(
    val uttaksgrad: Expression<Int>,
    val uforeKombinertMedAlder: Expression<Boolean>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title2 {
            text(
                Language.Bokmal to "Arbeidsinntekt og alderspensjon",
                Language.Nynorsk to "Arbeidsinntekt og alderspensjon",
                Language.English to "Earned income and retirement pension"
            )
        }
        paragraph {
            text(
                Language.Bokmal to "Du kan arbeide så mye du vil uten at alderspensjonen din blir redusert. Det kan føre til at pensjonen din øker.",
                Language.Nynorsk to "Du kan arbeide så mykje du vil utan at alderspensjonen din blir redusert. Det kan føre til at pensjonen din aukar.",
                Language.English to "You can work as much as you want without your retirement pension being reduced. This may lead to an increase in your pension."
            )
        }

        showIf(uttaksgrad.equalTo(100)) {
            paragraph {
                text(
                    Language.Bokmal to "Hvis du har 100 prosent alderspensjon, gjelder økningen fra 1. januar året etter at skatteoppgjøret ditt er ferdig.",
                    Language.Nynorsk to "Dersom du har 100 prosent alderspensjon, gjeld auken frå 1. januar året etter at skatteoppgjeret ditt er ferdig.",
                    Language.English to "If you are receiving a full (100 percent) retirement pension, the increase will come into effect from 1 January the year after your final tax settlement has been completed."
                )
            }
        }

        showIf(uttaksgrad.lessThan(100)) {
            paragraph {
                text(
                    Language.Bokmal to "Hvis du har lavere enn 100 prosent alderspensjon, blir økningen lagt til hvis du søker om endret grad eller ny beregning av den graden du har nå.",
                    Language.Nynorsk to "Dersom du har lågare enn 100 prosent alderspensjon, blir auken lagd til dersom du søkjer om endra grad eller ny berekning av den graden du har no.",
                    Language.English to "If you are receiving retirement pension at a reduced rate (lower than 100 percent), the increase will come into effect if you apply to have the rate changed or have your current rate recalculated."
                )
            }
        }

        showIf(uforeKombinertMedAlder) {
            paragraph {
                text(
                    Language.Bokmal to "Uføretrygden din kan fortsatt bli redusert på grunn av inntekt. Du finner informasjon om inntektsgrensen i vedtak om uføretrygd.",
                    Language.Nynorsk to "Uføretrygda di kan framleis bli redusert på grunn av inntekt. Du finn informasjon om inntektsgrensa i vedtak om uføretrygd.",
                    Language.English to "Your disability benefit may still be reduced as a result of income. You can find information on the income limit in the decision on disability benefit."
                )
            }
        }
    }
}

class AvsnittLesMerOmAlderspensjon : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title2 {
            text(
                Language.Bokmal to "Hvor kan du få vite mer om alderspensjonen din?",
                Language.Nynorsk to "Kvar kan du få vite meir om alderspensjonen din?",
                Language.English to "Where can you find out more about your retirement pension?"
            )
        }

        paragraph {
            text(
                Language.Bokmal to "Du finner mer informasjon om hvordan alderspensjon er satt sammen og oversikter over grunnbeløp og aktuelle satser på $ALDERSPENSJON. " +
                        "Informasjon om utbetalingene dine finner du på $DITT_NAV. Her kan du også endre kontonummeret ditt.",
                Language.Nynorsk to "Du finn meir informasjon om korleis alderspensjonen er sett saman, og oversikter over grunnbeløp og aktuelle satsar på $ALDERSPENSJON. " +
                        "Informasjon om utbetalingane dine finn du på $DITT_NAV. Her kan du også endre kontonummeret ditt.",
                Language.English to "There is more information on how retirement pension is calculated, with overviews of basic amounts and relevant rates, at $$ALDERSPENSJON. " +
                        "You can find more detailed information on what you will receive at $DITT_NAV. Here you can also change your bank account number."
            )
        }
    }
}

class AvsnittMeldFraOmEndringer : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title2 {
            text(
                Language.Bokmal to "Du må melde fra om endringer",
                Language.Nynorsk to "Du må melde frå om endringar",
                Language.English to "You must notify Nav if anything changes "
            )
        }

        paragraph {
            text(
                Language.Bokmal to "Hvis du får endringer i familiesituasjon, planlegger opphold i utlandet, eller ektefellen eller samboeren din får endringer i inntekten, kan det ha betydning for beløpet du får utbetalt fra Nav. I slike tilfeller må du derfor straks melde fra til oss. I vedlegget ser du hvilke endringer du må si fra om.",
                Language.Nynorsk to "Dersom du får endringar i familiesituasjonen, planlegg opphald i utlandet, eller ektefellen, partnaren eller sambuaren din får endringar i inntekta, kan det få noko å seie for beløpet du får utbetalt frå Nav. I slike tilfelle må du derfor straks melde frå til oss. I vedlegget ser du kva endringar du må seie frå om.",
                Language.English to "If there are changes in your family situation or you are planning a long-term stay abroad, or there are changes in the income of your spouse or co-habiting partner, these might affect the payments you receive from Nav. In such cases, you must notify Nav immediately. The appendix specifies which changes you are obligated to notify us of."
            )
        }

        paragraph {
            text(
                Language.Bokmal to "Hvis du har fått utbetalt for mye fordi du ikke har gitt oss beskjed, må du vanligvis betale tilbake pengene. Du er selv ansvarlig for å holde deg orientert om bevegelser på kontoen din, og du må melde fra om eventuelle feil til Nav.",
                Language.Nynorsk to "Dersom du har fått utbetalt for mykje fordi du ikkje har gitt oss beskjed, må du vanlegvis betale tilbake pengane. Du er sjølv ansvarleg for å halde deg orientert om rørsler på kontoen din, og du må melde frå om eventuelle feil til Nav.",
                Language.English to "If your payments have been too high as a result of you failing to notify us of a change, the incorrect payment must normally be repaid. It is your responsibility to keep yourself informed of movements in your account, and you are obligated to report any and all errors to Nav."
            )
        }
    }
}