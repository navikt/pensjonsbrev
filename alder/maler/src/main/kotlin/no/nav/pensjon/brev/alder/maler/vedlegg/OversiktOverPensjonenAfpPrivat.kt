package no.nav.pensjon.brev.alder.maler.vedlegg

import no.nav.pensjon.brev.alder.maler.felles.KronerText
import no.nav.pensjon.brev.alder.model.vedlegg.OversiktOverPensjonenAfpPrivatDto
import no.nav.pensjon.brev.alder.model.vedlegg.OversiktOverPensjonenAfpPrivatDto.AfpBeregning
import no.nav.pensjon.brev.alder.model.vedlegg.OversiktOverPensjonenAfpPrivatDto.Periode
import no.nav.pensjon.brev.alder.model.vedlegg.OversiktOverPensjonenAfpPrivatDto.SistePeriode
import no.nav.pensjon.brev.alder.model.vedlegg.selectors.oversiktOverPensjonenAfpPrivatDto.periode.beregning as periodeBeregning
import no.nav.pensjon.brev.alder.model.vedlegg.selectors.oversiktOverPensjonenAfpPrivatDto.periode.virkDatoFom as periodeVirkDatoFom
import no.nav.pensjon.brev.alder.model.vedlegg.selectors.oversiktOverPensjonenAfpPrivatDto.periode.virkDatoTom as periodeVirkDatoTom
import no.nav.pensjon.brev.alder.model.vedlegg.selectors.oversiktOverPensjonenAfpPrivatDto.sistePeriode.beregning as sisteBeregning
import no.nav.pensjon.brev.alder.model.vedlegg.selectors.oversiktOverPensjonenAfpPrivatDto.sistePeriode.virkDatoFom as sisteVirkDatoFom
import no.nav.pensjon.brev.alder.model.vedlegg.selectors.oversiktOverPensjonenAfpPrivatDto.afpBeregning.*
import no.nav.pensjon.brev.alder.model.vedlegg.selectors.oversiktOverPensjonenAfpPrivatDto.*
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.text

// PE_AF_oversikt_over_pensjonen_RTF — AFP i privat sektor. Vises kun når antall
// beregningsperioder > 1. Bryter ned AFP-en per periode i komponentene
// livsvarig / kronetillegg / kompensasjonstillegg.
@TemplateModelHelpers
val vedleggOversiktOverPensjonenAfpPrivat =
    createAttachment<LangBokmalNynorskEnglish, OversiktOverPensjonenAfpPrivatDto>(
        title = {
            text(
                bokmal { +"Oversikt over pensjonens størrelse fra " + virkningFom.format() },
                nynorsk { +"Oversikt over storleiken på pensjonen frå " + virkningFom.format() },
                english { +"Summary of size of pension from " + virkningFom.format() },
            )
        },
        includeSakspart = false,
        outline = {
            paragraph {
                text(
                    bokmal {
                        +"Hvis det har vært endringer i noen av opplysningene som ligger til grunn for " +
                            "beregningen eller pensjonen har vært regulert i perioden, kan dette føre til en " +
                            "endring i størrelsen på pensjonen."
                    },
                    nynorsk {
                        +"Dersom det har vore endringar i nokre av opplysningane som ligg til grunn for " +
                            "berekninga eller pensjonen er regulert i perioden, kan det føre til ei endring i " +
                            "storleiken på pensjonen som blir utbetalt."
                    },
                    english {
                        +"If there have been any changes in the details on which the calculations is based, " +
                            "or if there has been an annual adjustment of the pension during the period, this " +
                            "may affect the size of the pension."
                    },
                )
            }

            forEach(perioder) { p ->
                includePhrase(AfpForPeriode(p))
            }

            includePhrase(AfpForSistePeriode(sistePeriode))
        },
    )

private data class AfpForPeriode(
    val periode: Expression<Periode>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                bokmal {
                    +"Din AFP per måned fra " + periode.periodeVirkDatoFom.format() +
                        " til " + periode.periodeVirkDatoTom.format() + ":"
                },
                nynorsk {
                    +"AFP-en din per månad frå " + periode.periodeVirkDatoFom.format() +
                        " til " + periode.periodeVirkDatoTom.format() + ":"
                },
                english {
                    +"Your monthly contractual pension from " + periode.periodeVirkDatoFom.format() +
                        " to " + periode.periodeVirkDatoTom.format() + ":"
                },
            )
        }
        includePhrase(AfpBeloepTabell(periode.periodeBeregning))
    }
}

private data class AfpForSistePeriode(
    val periode: Expression<SistePeriode>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                bokmal { +"Din AFP per måned fra " + periode.sisteVirkDatoFom.format() + ":" },
                nynorsk { +"AFP-en din per månad frå " + periode.sisteVirkDatoFom.format() + ":" },
                english {
                    +"Your monthly contractual pension from " + periode.sisteVirkDatoFom.format() + ":"
                },
            )
        }
        includePhrase(AfpBeloepTabell(periode.sisteBeregning))
    }
}

private data class AfpBeloepTabell(
    val beregning: Expression<AfpBeregning>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            table(
                header = {
                    column {
                        text(bokmal { +"" }, nynorsk { +"" }, english { +"" })
                    }
                    column(alignment = RIGHT) {
                        text(
                            bokmal { +"Beløp per måned" },
                            nynorsk { +"Beløp per månad" },
                            english { +"Amount per month" },
                        )
                    }
                },
            ) {
                ifNotNull(beregning.livsvarigBrutto) {
                    row {
                        cell {
                            text(
                                bokmal { +"AFP livsvarig del" },
                                nynorsk { +"AFP livsvarig del" },
                                english { +"Contractual pension, lifelong amount" },
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }
                ifNotNull(beregning.kronetilleggBrutto) {
                    row {
                        cell {
                            text(
                                bokmal { +"AFP kronetillegg" },
                                nynorsk { +"AFP-kronetillegg" },
                                english { +"Contractual pension, NOK supplement" },
                            )
                        }
                        cell { includePhrase(KronerText(it)) }
                    }
                }
                ifNotNull(beregning.kompensasjonstilleggBrutto) {
                    row {
                        cell {
                            text(
                                bokmal { +"AFP kompensasjonstillegg (skattefritt)" },
                                nynorsk { +"AFP-kompensasjonstillegg (skattefritt)" },
                                english { +"Contractual pension, compensation supplement (tax-free)" },
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
                            english { +"Total contractual pension before tax" },
                            BOLD,
                        )
                    }
                    cell { includePhrase(KronerText(beregning.totalPensjon, BOLD)) }
                }
            }
        }
    }
}
