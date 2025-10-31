package brev.vedlegg.maanedligPensjonFoerSkattAFP

import brev.felles.KronerText
import no.nav.pensjon.brev.model.alder.BeloepEndring
import no.nav.pensjon.brev.model.alder.aldersovergang.OpptjeningType
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattAFPDto
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattAFPDtoSelectors.AFPPrivatBeregingListeSelectors.afpPrivatBeregingListe
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattAFPDtoSelectors.AFPPrivatBeregingListeSelectors.antallBeregningsperioder
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattAFPDtoSelectors.AFPPrivatBeregningSelectors.afpLivsvarigNetto
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattAFPDtoSelectors.AFPPrivatBeregningSelectors.datoFom
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattAFPDtoSelectors.AFPPrivatBeregningSelectors.datoTil
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattAFPDtoSelectors.AFPPrivatBeregningSelectors.komptilleggNetto
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattAFPDtoSelectors.AFPPrivatBeregningSelectors.kronetilleggNetto
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattAFPDtoSelectors.AFPPrivatBeregningSelectors.totalPensjon
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.PlainTextOnlyPhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.PlainTextOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.text
import java.time.LocalDate

data class TabellMaanedligPensjonAFP(
    val opptjeningType: Expression<OpptjeningType>,
    val beloepEndring: Expression<BeloepEndring>,
    val afpPrivatBeregning: Expression<MaanedligPensjonFoerSkattAFPDto.AFPPrivatBeregingListe>,
    val afpPrivatBeregningGjeldende: Expression<MaanedligPensjonFoerSkattAFPDto.AFPPrivatBeregning>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            includePhrase(
                TabellOverskrift(
                    datoFom = afpPrivatBeregningGjeldende.datoFom,
                    beloepEndring = beloepEndring,
                    opptjeningType = opptjeningType,
                    antallPerioder = afpPrivatBeregning.antallBeregningsperioder,
                ),
            )
        }

        includePhrase(TabellInnhold(afpPrivatBeregningGjeldende))
    }
}

data class TabellDinMaanedligAFP(
    val afpPrivatBeregning: Expression<MaanedligPensjonFoerSkattAFPDto.AFPPrivatBeregingListe>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        forEach(afpPrivatBeregning.afpPrivatBeregingListe) { beregning ->

            title2 {
                includePhrase(
                    TabellDetaljOverskrift(
                        datoFom = beregning.datoFom,
                        datoTil = beregning.datoTil,
                    ),
                )
            }

            includePhrase(TabellInnhold(beregning))
        }
    }
}

private data class TabellOverskrift(
    val datoFom: Expression<LocalDate>,
    val beloepEndring: Expression<BeloepEndring>,
    val opptjeningType: Expression<OpptjeningType>,
    val antallPerioder: Expression<Int>,
) : PlainTextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun PlainTextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(
            (
                opptjeningType.equalTo(OpptjeningType.KORRIGERING) and
                    beloepEndring.notEqualTo(
                        BeloepEndring.ENDR_RED,
                    )
            ) or opptjeningType.equalTo(OpptjeningType.TILVEKST),
        ) {
            text(
                bokmal { +"Din månedlige AFP" },
                nynorsk { +"Din månadlege AFP" },
                english { +"Your monthly AFP" },
            )
        }.orShowIf(
            opptjeningType.equalTo(OpptjeningType.KORRIGERING) and
                beloepEndring.equalTo(
                    BeloepEndring.ENDR_RED,
                ) and antallPerioder.greaterThan(0),
        ) {
            text(
                bokmal { +"Din månedlige AFP fra " + datoFom.format() },
                nynorsk { +"Din månadlege AFP frå " + datoFom.format() },
                english { +"Your monthly AFP from " + datoFom.format() },
            )
        }
    }
}

private object PensjonPerMaaned : PlainTextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun PlainTextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        text(
            bokmal { +"Pensjon per måned" },
            nynorsk { +"Pensjon per månad" },
            english { +"Pension per month" },
        )
    }
}

private data class TabellDetaljOverskrift(
    val datoFom: Expression<LocalDate>,
    val datoTil: Expression<LocalDate?>,
) : PlainTextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun PlainTextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        text(
            bokmal { +"Din månedlige AFP fra " + datoFom.format() },
            nynorsk { +"Din månadlege AFP frå " + datoFom.format() },
            english { +"Your monthly AFP from " + datoFom.format() },
        )

        ifNotNull(datoTil) {
            text(
                bokmal { +" til " + it.format() },
                nynorsk { +" til " + it.format() },
                english { +" to " + it.format() },
            )
        }
    }
}

data class TabellInnhold(
    val afpPrivatBeregning: Expression<MaanedligPensjonFoerSkattAFPDto.AFPPrivatBeregning>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            table(header = {
                column(columnSpan = 3) {
                    text(bokmal { +"" }, nynorsk { +"" }, english { +"" })
                }
                column(alignment = RIGHT, columnSpan = 1) {
                    includePhrase(PensjonPerMaaned)
                }
            }) {
                ifNotNull(afpPrivatBeregning.afpLivsvarigNetto) {
                    row {
                        cell {
                            text(
                                bokmal { +"AFP Livsvarig del" },
                                nynorsk { +"AFP Livsvarig del" },
                                english { +"AFP lifelong part" },
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }

                ifNotNull(afpPrivatBeregning.kronetilleggNetto) {
                    row {
                        cell {
                            text(
                                bokmal { +"AFP kronetillegg" },
                                nynorsk { +"AFP kronetillegg" },
                                english { +"AFP flat increase" },
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }

                ifNotNull(afpPrivatBeregning.komptilleggNetto) {
                    row {
                        cell {
                            text(
                                bokmal { +"AFP kompensasjonstillegg (skattefritt)" },
                                nynorsk { +"AFP kompensasjonstillegg (skattefritt)" },
                                english { +"AFP compensatory allowance (tax-free)" },
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }

                row {
                    cell {
                        text(
                            bokmal { +"Sum AFP før skatt" },
                            nynorsk { +"Sum AFP før skatt" },
                            english { +"Total AFP before tax" },
                            FontType.BOLD,
                        )
                    }
                    cell {
                        includePhrase(
                            KronerText(
                                afpPrivatBeregning.totalPensjon,
                                FontType.BOLD,
                            ),
                        )
                    }
                }
            }
        }
    }
}
