package brev.vedlegg

import brev.vedlegg.maanedligPensjonFoerSkattAFP.TabellDinMaanedligAFP
import brev.vedlegg.maanedligPensjonFoerSkattAFP.TabellMaanedligPensjonAFP
import no.nav.pensjon.brev.model.alder.BeloepEndring
import no.nav.pensjon.brev.model.alder.aldersovergang.OpptjeningType
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattAFPDto
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattAFPDtoSelectors.AFPPrivatBeregingListeSelectors.antallBeregningsperioder
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattAFPDtoSelectors.AFPPrivatBeregningSelectors.afpLivsvarigNetto
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattAFPDtoSelectors.AFPPrivatBeregningSelectors.komptilleggNetto
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattAFPDtoSelectors.AFPPrivatBeregningSelectors.kronetilleggNetto
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattAFPDtoSelectors.KravSelectors.virkDatoFom
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattAFPDtoSelectors.afpPrivatBeregningGjeldende
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattAFPDtoSelectors.afpPrivatBeregningListe
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattAFPDtoSelectors.belopEndring
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattAFPDtoSelectors.krav
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattAFPDtoSelectors.opptjeningType
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.Kroner

// V00008 i metaforce
@TemplateModelHelpers
val vedleggMaanedligPensjonFoerSkattAFP =
    createAttachment<LangBokmalNynorskEnglish, MaanedligPensjonFoerSkattAFPDto>(
        title =
            newText(
                Bokmal to "Dette får du i AFP før skatt",
                Nynorsk to "Dette får du i AFP før skatt",
                English to "This is your monthly AFP before tax",
            ),
        outline = {

            includePhrase(
                TabellMaanedligPensjonAFP(
                    opptjeningType = opptjeningType,
                    beloepEndring = belopEndring,
                    afpPrivatBeregning = afpPrivatBeregningListe,
                    afpPrivatBeregningGjeldende = afpPrivatBeregningGjeldende,
                ),
            )

            title1 {
                text(
                    bokmal { +"Slik er pensjonen din satt sammen" },
                    nynorsk { +"Slik er pensjonen din sett saman" },
                    english { +"This is how your pension is calculated" },
                )
            }

            val afpLivsvarigNetto = afpPrivatBeregningGjeldende.afpLivsvarigNetto.ifNull(Kroner(0))

            showIf(afpLivsvarigNetto.greaterThan(0)) {
                paragraph {
                    text(
                        bokmal {
                            +"Den livsvarige delen av AFP beregnes ut fra opptjeningen du har hatt til og med det året du fylte 61."
                        },
                        nynorsk {
                            +"Den livsvarige delen av AFP blir berekna ut frå oppteninga du har hatt til og med det året du fylte 61."
                        },
                        english {
                            +"The life-long part of AFP is calculated based on your accumulated pension rights up to and including the calendar year you turn 61."
                        },
                    )
                }
            }

            val kronetilleggNetto = afpPrivatBeregningGjeldende.kronetilleggNetto.ifNull(Kroner(0))

            showIf(kronetilleggNetto.greaterThan(0)) {
                paragraph {
                    text(
                        bokmal {
                            +"AFP kronetillegg utbetales til og med den måneden du fyller 67 år."
                        },
                        nynorsk {
                            +"AFP kronetillegg blir betalt ut til og med den månaden du fyller 67 år."
                        },
                        english {
                            +"The AFP flat increase will be paid up to and including the month you turn 67."
                        },
                    )
                }
            }

            val komptilleggNetto = afpPrivatBeregningGjeldende.komptilleggNetto.ifNull(Kroner(0))

            showIf(komptilleggNetto.greaterThan(0)) {
                paragraph {
                    text(
                        bokmal {
                            +"Du får AFP kompensasjonstillegg fordi du er født før 1963."
                        },
                        nynorsk {
                            +"Du får AFP kompensasjonstillegg fordi du er fødd før 1963."
                        },
                        english {
                            +"You are receiving the AFP compensatory allowance because you are born before 1963."
                        },
                    )
                }
            }

            showIf(afpPrivatBeregningListe.antallBeregningsperioder.greaterThan(1)) {
                showIf(
                    (
                        opptjeningType.equalTo(OpptjeningType.KORRIGERING)
                            and belopEndring.notEqualTo(BeloepEndring.ENDR_RED)
                    )

                        or

                        (opptjeningType.equalTo(OpptjeningType.TILVEKST)),
                ) {
                    title1 {
                        text(
                            bokmal { +"Oversikt over pensjonen fra " + krav.virkDatoFom.format() },
                            nynorsk { +"Oversikt over pensjonen frå " + krav.virkDatoFom.format() },
                            english { +"Pension specifications as of " + krav.virkDatoFom.format() },
                        )
                    }

                    includePhrase(TabellDinMaanedligAFP(afpPrivatBeregningListe))
                }
            }
        },
    )
