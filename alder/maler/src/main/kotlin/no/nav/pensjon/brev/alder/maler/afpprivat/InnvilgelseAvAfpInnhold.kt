package no.nav.pensjon.brev.alder.maler.afpprivat

import no.nav.pensjon.brev.alder.maler.afpprivat.fraser.AfpPrivatFraser
import no.nav.pensjon.brev.alder.maler.felles.Constants
import no.nav.pensjon.brev.alder.maler.felles.KronerText
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.BrevbakerType
import java.time.LocalDate

/**
 * Felles innhold for innvilgelse av AFP i privat sektor.
 *
 * Brukes av både [InnvilgelseAvAfpAuto] (autobrev, PE_AF_04_115) og
 * den redigerbare malen `InnvilgelseAvAfp` (PE_AF_04_111). Inneholder hele
 * brevkroppen, men ikke avsluttende `Har du spørsmål?`-blokk — den
 * inkluderes separat i hver mal.
 */
data class InnvilgelseAvAfpInnhold(
    val kravMottattDato: Expression<LocalDate>,
    val virkningFom: Expression<LocalDate>,
    val totalPensjon: Expression<BrevbakerType.Kroner>,
    val livsvarigBrutto: Expression<BrevbakerType.Kroner?>,
    val kronetilleggBrutto: Expression<BrevbakerType.Kroner?>,
    val kompensasjonstilleggBrutto: Expression<BrevbakerType.Kroner?>,
    val brukerUnder70Aar: Expression<Boolean>,
    val bosattINorge: Expression<Boolean>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                bokmal {
                    +"Nav viser til søknaden din om AFP i privat sektor mottatt " +
                        kravMottattDato.format() +
                        ". Du har valgt å ta ut AFP fra " + virkningFom.format() +
                        ". Du får " + totalPensjon.format() +
                        " hver måned før skatt."
                },
                nynorsk {
                    +"Nav viser til søknaden din om AFP i privat sektor motteken " +
                        kravMottattDato.format() +
                        ". Du har valt å ta ut AFP frå " + virkningFom.format() +
                        ". Du får " + totalPensjon.format() +
                        " kvar månad før skatt."
                },
                english {
                    +"Nav has granted your application which we received on " +
                        kravMottattDato.format() +
                        " for contractual pension in the private sector. You have decided to draw " +
                        "contractual pension starting on " + virkningFom.format() +
                        ". You will receive " + totalPensjon.format() + " per month before tax."
                },
            )
        }

        paragraph {
            text(
                bokmal {
                    +"AFP i privat sektor gis etter bestemmelsene i lov om statstilskott til arbeidstakere som tar " +
                        "ut avtalefestet pensjon i privat sektor (AFP-tilskottsloven). Fellesordningen for AFP har " +
                        "funnet at du oppfyller de avtalemessige vilkårene for rett til AFP. Nav har avgjort andre " +
                        "spørsmål om retten til pensjon, blant annet beregningen. Beregningsreglene står i " +
                        "paragrafene 6 til 11 i AFP-tilskottsloven."
                },
                nynorsk {
                    +"AFP i privat sektor blir gitt etter føresegnene i lov om statstilskot til arbeidstakarar som " +
                        "tek ut avtalefesta pensjon i privat sektor (AFP-tilskotslova). Fellesordninga for AFP har " +
                        "funne at du oppfyller dei avtalemessige vilkåra for rett til AFP. Nav har avgjort andre " +
                        "spørsmål om retten til pensjon, mellom anna berekninga. Berekningsreglane står i " +
                        "paragrafane 6 til 11 i AFP-tilskotslova."
                },
                english {
                    +"Private sector contractual pension is granted pursuant to the act relating to state subsidies " +
                        "to employees who draw an early retirement pension in the private sector (the Early " +
                        "Retirement Pension Subsidy Act - AFP-tilskottsloven). The Common Scheme for Contractual " +
                        "Pension has ascertained that you meet the contractual terms for the right to AFP. Nav has " +
                        "made a decision on other issues regarding the right to pension, including the calculation. " +
                        "The calculation rules are listed in sections 6 to 11 of the Early Retirement Pension " +
                        "Subsidy Act."
                },
            )
        }

        // Tabell: "Beløp per måned" — restaurert fra de flate text(...)-blokkene i konverteren
        // (Step 5 i convert-exstream-letter). Konvertert til ekte table med betinget rad per komponent.
        paragraph {
            text(
                bokmal { +"Din AFP per måned blir slik:" },
                nynorsk { +"AFP-en din per månad blir slik:" },
                english { +"Your monthly contractual pension will be:" },
            )
            table(
                header = {
                    column {
                        text(
                            bokmal { +"Beløp per måned" },
                            nynorsk { +"Beløp per månad" },
                            english { +"Amount per month" },
                        )
                    }
                    column(alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT) {
                        text(bokmal { +"" }, nynorsk { +"" }, english { +"" })
                    }
                },
            ) {
                ifNotNull(livsvarigBrutto) { brutto ->
                    row {
                        cell {
                            text(
                                bokmal { +"AFP livsvarig del" },
                                nynorsk { +"AFP livsvarig del" },
                                english { +"Contractual pension, lifelong amount" },
                            )
                        }
                        cell { includePhrase(KronerText(brutto)) }
                    }
                }
                ifNotNull(kronetilleggBrutto) { brutto ->
                    row {
                        cell {
                            text(
                                bokmal { +"AFP kronetillegg" },
                                nynorsk { +"AFP-kronetillegg" },
                                english { +"Contractual pension, NOK supplement" },
                            )
                        }
                        cell { includePhrase(KronerText(brutto)) }
                    }
                }
                ifNotNull(kompensasjonstilleggBrutto) { brutto ->
                    row {
                        cell {
                            text(
                                bokmal { +"AFP kompensasjonstillegg (skattefritt)" },
                                nynorsk { +"AFP-kompensasjonstillegg (skattefritt)" },
                                english { +"Contractual pension, compensation supplement (tax-free)" },
                            )
                        }
                        cell { includePhrase(KronerText(brutto)) }
                    }
                }
                row {
                    cell {
                        text(
                            bokmal { +"Sum AFP før skatt" },
                            nynorsk { +"Sum AFP før skatt" },
                            english { +"Total contractual pension before tax" },
                            Element.OutlineContent.ParagraphContent.Text.FontType.BOLD,
                        )
                    }
                    cell { includePhrase(
                        KronerText(
                            totalPensjon,
                            Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
                        )
                    ) }
                }
            }
        }

        // Levealdersjustering — felles tekst for alle.
        includePhrase(AfpPrivatFraser.Levealdersjustering)

        // Per-komponent forklaring (én paragraph per innvilget komponent — null = ikke innvilget).
        includePhrase(AfpPrivatFraser.KomponentLivsvarig(livsvarigBrutto))
        includePhrase(AfpPrivatFraser.KomponentKronetillegg(kronetilleggBrutto))
        includePhrase(AfpPrivatFraser.KomponentKompensasjonstillegg(kompensasjonstilleggBrutto))

        // AFP og alderspensjon fra folketrygden.
        includePhrase(AfpPrivatFraser.AfpOgAlderspensjon)

        // Opptjening etter 61 år (kun for brukere under 70 år).
        showIf(brukerUnder70Aar) {
            includePhrase(AfpPrivatFraser.OpptjeningEtter61Aar)
        }

        includePhrase(AfpPrivatFraser.ArbeidUtenReduksjon)

        // Utbetaling — overskrift + tekst.
        includePhrase(AfpPrivatFraser.MaanedligUtbetaling)

        // Skattepliktig — bosatt i Norge.
        showIf(bosattINorge) {
            includePhrase(AfpPrivatFraser.SkattINorge(kompensasjonstilleggBrutto))
        }

        // Skattepliktig — bosatt i utlandet.
        showIf(not(bosattINorge)) {
            includePhrase(AfpPrivatFraser.SkattIUtlandet(kompensasjonstilleggBrutto))
        }

        includePhrase(AfpPrivatFraser.DinPensjonSkattetrekk)

        // Dine rettigheter — innsyn.
        title1 {
            text(
                bokmal { +"Dine rettigheter" },
                nynorsk { +"Dine rettar" },
                english { +"Your rights" },
            )
        }
        includePhrase(AfpPrivatFraser.InnsynForvaltningsloven18)

        // Klagerett.
        includePhrase(AfpPrivatFraser.KlagerettFolketrygdloven2112)
    }
}