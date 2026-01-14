package no.nav.pensjon.brev.ufore.maler.feilutbetaling.varsel

import no.nav.pensjon.brev.ufore.maler.Sakstype.UFOREP
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkategori.FEILUTBETALING
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkontekst.VEDTAK
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.LocalizedFormatter.CurrencyFormat
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_FEILUTBETALING_VARSEL_BARN_UTLAND
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.feilutbetaling.FeilutbetalingSpesifikkVarselDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.feilutbetaling.FeilutbetalingSpesifikkVarselDtoSelectors.pesysData
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.feilutbetaling.VarselFeilutbetalingPesysDataSelectors.feilutbetaltBrutto
import no.nav.pensjon.brev.ufore.maler.FeatureToggles
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.INFORMASJONSBREV
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VIKTIG

@TemplateModelHelpers
object VarselBtBarnUtland12_15: RedigerbarTemplate<FeilutbetalingSpesifikkVarselDto> {
    override val featureToggle = FeatureToggles.feilutbetalingNy.toggle

    override val kode = UT_FEILUTBETALING_VARSEL_BARN_UTLAND
    override val kategori = FEILUTBETALING
    override val brevkontekst = VEDTAK
    override val sakstyper = setOf(UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Varsel feilutbetaling barn bor i utlandet §12-15",
            distribusjonstype = VIKTIG,
            brevtype = INFORMASJONSBREV
        )
    ) {
        val dato = fritekst("dato")
        val land = fritekst("land")
        val hendelse = fritekst("handling")
        val bruttoFeilutbetalt = pesysData.feilutbetaltBrutto.format(CurrencyFormat)

        title {
            text(
                bokmal { +"Vi vurderer om du må betale tilbake uføretrygd " },
                nynorsk { + "Vi vurderer om du må betale tilbake uføretrygd "}
            )
        }
        outline {
            paragraph {
                text( bokmal { + "Vi vurderer at du kan ha fått utbetalt for mye i uføretrygd fra og med " + dato +
                        " til og med " + dato + " . Grunnen til det, er at vi har fått opplysninger om at du ikke lenger bor i et EØS-land. " },
                    nynorsk { + "Vi vurderer at du kan ha fått utbetalt for mykje i uføretrygd frå og med " + dato +
                            " til og med " + dato + ". Grunnen til det er at vi har fått opplysningar om at du ikkje lenger bur i eit EØS-land. "}
                )
            }
            paragraph {
                text( bokmal { + "Uføretrygden og barnetillegget ditt påvirkes av om du bor i EØS-land eller ikke. I vedtaket av " +
                        dato + ", informerte vi deg om at endringen i bostedsland, kan ha ført til feilutbetalinger tilbake i tid.  " },
                    nynorsk { + "Uføretrygda og barnetillegget ditt blir påverka av om du bur i EØS-land eller ikkje. I vedtaket av " +
                             dato + " informerte vi deg om at endringa i bustadsland kan ha ført til feilutbetalingar tilbake i tid. "}
                )
            }
            includePhrase(FeilutbetalingFraser.FeilOpplysninger)
            title1 {
                text(
                    bokmal { +"Derfor mener vi du har fått utbetalt for mye " },
                    nynorsk { + "Derfor meiner vi du har fått utbetalt for mykje "}
                )
            }
            paragraph {
                text(
                    bokmal { +"Du har fått barnetillegg i uføretrygden for barn født " + dato + ". " },
                    nynorsk { + "Du har fått barnetillegg i uføretrygda for barn fødd " + dato + ". "}
                )
            }
            paragraph {
                text(
                    bokmal { + "For å ha rett til barnetillegg fra 1. juli 2020 må: " },
                    nynorsk { + "For å ha rett til barnetillegg frå 1. juli 2020 må: "}
                )
                list {
                    item {
                        text(
                            bokmal { + "du bo i Norge, innenfor EØS-området eller i et annet land Norge har trygdeavtale med" },
                            nynorsk { + "du bu i Noreg, innanfor EØS-området eller i eit anna land Noreg har trygdeavtale med "}
                        )
                    }
                    item {
                        text(
                            bokmal { + "barnet ditt være bosatt og oppholde seg i Norge, innenfor EØS-området eller et annet land Norge har trygdeavtale med " },
                            nynorsk { + "barnet ditt vere busett og opphalde seg i Noreg, innanfor EØS-området eller i eit anna land Noreg har trygdeavtale med "}
                        )
                    }
                }
            }
            paragraph {
                text(
                    bokmal { + "Du har bodd i " + land + " og har derfor ikke rett til barnetillegg fra " + dato + "." },
                    nynorsk { + "Du har budd i " + land + " og har derfor ikkje rett til barnetillegg frå " + dato + ". "}
                )
            }
            paragraph {
                text(
                    bokmal { + "Du fikk utbetalt uføretrygd og barnetillegg i perioden " + dato + " til " + dato +
                            ", og her har det oppstått en feilutbetaling på " + bruttoFeilutbetalt + " kroner.  " },
                    nynorsk { + "Du fekk utbetalt uføretrygd og barnetillegg i perioden " + dato + " til " + dato +
                            ", og her har det oppstått ei feilutbetaling på " + bruttoFeilutbetalt + " kroner. "}
                )
            }
            paragraph {
                text(
                    bokmal { + "Utbetalingen din skulle vært endret ved utgangen av måneden etter at " + hendelse +
                            ". Barnetillegg stanses ved utgangen av den måneden retten til ytelsen faller bort. Dette følger av folketrygdloven § 22-12 sjette ledd. " },
                    nynorsk { + "Utbetalinga di skulle vore endra ved utgangen av månaden etter at " + hendelse +
                            ". Barnetillegg blir stoppa ved utgangen av den månaden retten til ytinga fell bort. Dette følgjer av folketrygdlova § 22-12 sjette ledd. "}
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



