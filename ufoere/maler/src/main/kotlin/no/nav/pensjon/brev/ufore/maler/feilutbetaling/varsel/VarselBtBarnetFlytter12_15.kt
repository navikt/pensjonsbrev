package no.nav.pensjon.brev.ufore.maler.feilutbetaling.varsel

import no.nav.pensjon.brev.ufore.api.model.maler.Sakstype.UFOREP
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
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.feilutbetaling.FeilutbetalingSpesifikkVarselDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.feilutbetaling.FeilutbetalingSpesifikkVarselDtoSelectors.pesysData
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.feilutbetaling.VarselFeilutbetalingPesysDataSelectors.feilutbetaltBrutto
import no.nav.pensjon.brev.ufore.maler.Brevkategori.FEILUTBETALING
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
    override val brevkontekst = VEDTAK
    override val sakstyper = setOf(UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Varsel feilutbetaling barnet flytter §12-15",
            distribusjonstype = VIKTIG,
            brevtype = INFORMASJONSBREV
        )
    ) {
        val dato = fritekst("dato")
        val kilde = fritekst("kilde")
        val hendelse = fritekst("handling")
        val navn = fritekst("navn")
        val bruttoFeilutbetalt = pesysData.feilutbetaltBrutto.format(CurrencyFormat)

        title {
            text(
                bokmal { +"Vi vurderer om du må betale tilbake uføretrygd " },
                nynorsk { + "Vi vurderer om du må betale tilbake uføretrygd " }
                )
        }
        outline {
            paragraph {
                text(bokmal { + "Vi vurderer at du kan ha fått utbetalt for mye i uføretrygd fra og med " + dato + " til og med " + dato +
                        ". Grunnen til det, er at vi har fått opplysninger om at du ikke lenger forsørger " + navn + ". " },
                    nynorsk { + "Vi vurderer at du kan ha fått utbetalt for mykje i uføretrygd frå og med " + dato + " til og med " + dato +
                            ". Grunnen til det er at vi har fått opplysningar om at du ikkje lenger forsørgjer " + navn + ". "}
                    )
            }
            paragraph {
                text(
                    bokmal { + "Uføretrygden og barnetillegget ditt påvirkes av om du forsørger barn eller ikke. I vedtaket av " + dato +
                            ", informerte vi deg om at endringen i forsørgelse av barn, kan ha ført til feilutbetalinger tilbake i tid. "},
                    nynorsk { + "Uføretrygda og barnetillegget ditt blir påverka av om du forsørgjer barn eller ikkje. I vedtaket av " + dato +
                            " informerte vi deg om at endringa i forsørging av barn kan ha ført til feilutbetalingar tilbake i tid. "}
                    )
            }

            includePhrase(FeilutbetalingFraser.FeilOpplysninger)

            title1 {
                text(
                bokmal { + "Derfor mener vi du har fått utbetalt for mye " },
                    nynorsk { + "Derfor meiner vi du har fått utbetalt for mykje "}
                    )
            }
            paragraph {
                text(
                    bokmal { + "Du har mottatt barnetillegg i uføretrygden for barn født " + dato + ". " },
                    nynorsk { + "Du har mottatt barnetillegg i uføretrygda for barn fødd " + dato + ". "}
                    )
            }
            paragraph {
                text(
                    bokmal { + "Vi har den " + dato + " har mottatt opplysninger fra " + kilde + " om at barnet " + hendelse + ". " },
                    nynorsk { + "Vi har den " + dato + " mottatt opplysningar frå " + kilde + " om at barnet " + hendelse + ". " }
                )
            }
            paragraph {
                text(
                    bokmal { + "Hvis du mener at dette er feil, må du dokumentere at du fortsatt forsørger " + navn + ". " },
                    nynorsk { + "Dersom du meiner at dette er feil, må du dokumentere at du framleis forsørgjer " + navn + ". "}
                    )
            }
            paragraph {
                text(
                    bokmal { + "Forsørgelse kan dokumenteres på flere måter: " },
                    nynorsk { + "Forsørging kan dokumenterast på fleire måtar: "}
                    )
                list {
                    item {
                        text(
                            bokmal { + "barnet bor hos deg " },
                            nynorsk { + "barnet bur hos deg " }
                        )
                    }
                    item {
                        text(
                            bokmal { + "skriftlig bidragsavtale eller bidrag fastsatt av Nav " },
                            nynorsk { + "skriftleg bidragsavtale eller bidrag fastsett av Nav " }
                        )
                    }
                    item {
                        text(
                            bokmal { + "avtale om \"vanlig samværsrett\" etter barnelova § 43 " },
                            nynorsk { + "avtale om \"vanleg samværsrett\" etter barnelova § 43 " }
                        )
                    }
                    item {
                        text(
                            bokmal { + "avtale om delt bosted etter barnelova § 36 " },
                            nynorsk { + "avtale om delt bustad etter barnelova § 36 " }
                        )
                    }
                    item {
                        text(
                            bokmal { + "dokumentasjon på at du betaler jevnlig utgifter til forsørgelse av barnet " },
                            nynorsk { + "dokumentasjon på at du betaler jamleg utgifter til forsørging av barnet "}
                        )
                    }
                }
            }
            paragraph {
                text(
                    bokmal { + "I vårt vedtak av " + dato + " er barnetillegget for barn født " + dato +
                            " derfor opphørt fra " + dato + ". Du får en lavere utbetaling av uføretrygd fra måneden etter at " + hendelse +
                            ". Dette står i folketrygdloven § 22-12 femte ledd. "},
                    nynorsk { + "I vårt vedtak av " + dato + " er barnetillegget for barn fødd " + dato +
                            " derfor opphøyrt frå " + dato + ". Du får ei lågare utbetaling av uføretrygd frå månaden etter at " + hendelse +
                            ". Dette står i folketrygdlova § 22-12 femte ledd. " }
                )
            }
            paragraph {
                text(
                    bokmal { + "Du fikk utbetalt uføretrygd og barnetillegg i perioden " + dato + " til " + dato +
                            " , og her har det oppstått en feilutbetaling på " + bruttoFeilutbetalt +
                            " kroner. Utbetalingen din skulle vært endret fra måneden etter at " + hendelse + ". "},
                    nynorsk { + "Du fekk utbetalt uføretrygd og barnetillegg i perioden " + dato + " til " + dato +
                            ", og her har det oppstått ei feilutbetaling på " + bruttoFeilutbetalt + " kroner. " +
                            "Utbetalinga di skulle vore endra frå månaden etter at " + hendelse + ". "}
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



