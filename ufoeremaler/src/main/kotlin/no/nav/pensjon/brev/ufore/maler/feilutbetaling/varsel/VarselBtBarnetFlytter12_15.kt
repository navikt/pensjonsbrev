package no.nav.pensjon.brev.ufore.maler.feilutbetaling.varsel

import no.nav.pensjon.brev.api.model.Sakstype.UFOREP
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkategori.FEILUTBETALING
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkontekst.ALLE
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.LocalizedFormatter
import no.nav.pensjon.brev.template.LocalizedFormatter.CurrencyFormat
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.feilutbetaling.FeilutbetalingSpesifikkVarselDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.feilutbetaling.FeilutbetalingSpesifikkVarselDtoSelectors.pesysData
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.feilutbetaling.VarselFeilutbetalingPesysDataSelectors.feilutbetaltBrutto
import no.nav.pensjon.brev.ufore.maler.FeatureToggles
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.INFORMASJONSBREV
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VIKTIG

@TemplateModelHelpers
object VarselBtBarnetFlytter12_15: RedigerbarTemplate<FeilutbetalingSpesifikkVarselDto> {
    override val featureToggle = FeatureToggles.feilutbetalingNy.toggle

    override val kode = Ufoerebrevkoder.Redigerbar.UT_FEILUTBETALING_VARSEL_BARN_FLYTTER
    override val kategori = FEILUTBETALING
    override val brevkontekst = ALLE
    override val sakstyper = setOf(UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Varsel feilutbetaling barnet flytter §12-15",
            distribusjonstype = VIKTIG,
            brevtype = INFORMASJONSBREV
        )
    ) {
        title {
            text(
                bokmal { +"Vi vurderer om du må betale tilbake uføretrygd " })
        }
        outline {
            paragraph {
                text(bokmal { + "Vi vurderer at du kan ha fått utbetalt for mye i uføretrygd fra og med " + fritekst("dato") + " til og med " + fritekst("dato") +
                        " . Grunnen til det, er at vi har fått opplysninger om at du ikke lenger forsørger " + fritekst("navn") + ". " })
            }
            paragraph {
                text(
                    bokmal { + "Uføretrygden og barnetillegget ditt påvirkes av om du forsørger barn eller ikke. I vedtaket av " + fritekst("dato") +
                            ", informerte vi deg om at endringen i forsørgelse av barn, kan ha ført til feilutbetalinger tilbake i tid. "})
            }

            includePhrase(FeilutbetalingFraser.FeilOpplysninger)

            title1 {
                text(
                bokmal { + "Derfor mener vi du har fått utbetalt for mye " } )
            }
            paragraph {
                text(
                    bokmal { + "Du har mottatt barnetillegg i uføretrygden for barn født " + fritekst("dato") + ". " }
                )
            }
            paragraph {
                text(
                    bokmal { + "Vi har den " + fritekst("dato") + " har mottatt opplysninger fra " + fritekst("kilde") +
                            " om at barnet " + fritekst("hendelse") + ". " }
                )
            }
            paragraph {
                text(
                    bokmal { + "Hvis du mener at dette er feil, må du dokumentere at du fortsatt forsørger " + fritekst("navn") + ". " }
                )
            }
            paragraph {
                text(
                    bokmal { + "Forsørgelse kan dokumenteres på flere måter: " }
                )
                list {
                    item {
                        text(
                            bokmal { + "barnet bor hos deg " }
                        )
                    }
                    item {
                        text(
                            bokmal { + "skriftlig bidragsavtale eller bidrag fastsatt av Nav " }
                        )
                    }
                    item {
                        text(
                            bokmal { + "avtale om \"vanlig samværsrett\" etter barnelova § 43 " }
                        )
                    }
                    item {
                        text(
                            bokmal { + "avtale om delt bosted etter barnelova § 36 " }
                        )
                    }
                    item {
                        text(
                            bokmal { + "dokumentasjon på at du betaler jevnlig utgifter til forsørgelse av barnet " }
                        )
                    }
                }
            }
            paragraph {
                text(
                    bokmal { + "I vårt vedtak av " + fritekst("dato") + " er barnetillegget for barn født " + fritekst("dato") +
                            " derfor opphørt fra " + fritekst("dato") + ". Du får en lavere utbetaling av uføretrygd fra måneden etter at " + fritekst("hendelse") +
                            ". Dette står i folketrygdloven § 22-12 femte ledd. "}
                )
            }
            paragraph {
                text(
                    bokmal { + "Du fikk utbetalt uføretrygd og barnetillegg i perioden " + fritekst("dato") + " til " + fritekst("dato") +
                            " , og her har det oppstått en feilutbetaling på " + pesysData.feilutbetaltBrutto.format(CurrencyFormat) +
                            " kroner. Utbetalingen din skulle vært endret fra måneden etter at " + fritekst("hendelse") + ". "}
                )
            }
            includePhrase(FeilutbetalingFraser.KriterierTilbakekreving)
            includePhrase(FeilutbetalingFraser.KriterierIngenTilbakekreving)
            includePhrase(FeilutbetalingFraser.SlikUttalerDuDeg)
            includePhrase(FeilutbetalingFraser.HvaSkjerVidere)
            includePhrase(Felles.RettTilInnsyn)
            includePhrase(Felles.HarDuSporsmal)
        }
    }
}



