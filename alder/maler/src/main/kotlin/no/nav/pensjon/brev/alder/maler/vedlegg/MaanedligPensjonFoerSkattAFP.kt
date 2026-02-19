package no.nav.pensjon.brev.alder.maler.vedlegg

import no.nav.pensjon.brev.alder.maler.vedlegg.maanedligPensjonFoerSkattAFP.TabellDinMaanedligAFP
import no.nav.pensjon.brev.alder.maler.vedlegg.maanedligPensjonFoerSkattAFP.TabellMaanedligPensjonAFP
import no.nav.pensjon.brev.alder.model.BeloepEndring
import no.nav.pensjon.brev.alder.model.aldersovergang.OpptjeningType
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPDto
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPDtoSelectors.AFPPrivatBeregningListeSelectors.antallBeregningsperioder
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPDtoSelectors.AFPPrivatBeregningSelectors.afpLivsvarigNetto
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPDtoSelectors.AFPPrivatBeregningSelectors.komptilleggNetto
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPDtoSelectors.AFPPrivatBeregningSelectors.kronetilleggNetto
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPDtoSelectors.KravSelectors.virkDatoFom
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPDtoSelectors.afpPrivatBeregningGjeldende
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPDtoSelectors.afpPrivatBeregningListe
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPDtoSelectors.belopEndring
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPDtoSelectors.krav
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPDtoSelectors.opptjeningType
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.BrevWrappers.Kroner

// V00008 i metaforce
@TemplateModelHelpers
val vedleggMaanedligPensjonFoerSkattAFP =
    createAttachment<LangBokmalNynorskEnglish, MaanedligPensjonFoerSkattAFPDto>(
        title = {
            text(
                bokmal { +"Dette får du i AFP før skatt" },
                nynorsk { +"Dette får du i AFP før skatt" },
                english { +"This is your monthly AFP before tax" },
            )
        },
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
