package no.nav.pensjon.brev.ufore.maler.feilutbetaling.varsel

import no.nav.pensjon.brev.api.model.Sakstype.UFOREP
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkategori.FEILUTBETALING
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkontekst.ALLE
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.LocalizedFormatter
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_FEILUTBETALING_VARSEL_SONING
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.feilutbetaling.FeilutbetalingSpesifikkVarselDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.feilutbetaling.FeilutbetalingSpesifikkVarselDtoSelectors.pesysData
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.feilutbetaling.VarselFeilutbetalingPesysDataSelectors.feilutbetaltBrutto
import no.nav.pensjon.brev.ufore.maler.FeatureToggles
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.INFORMASJONSBREV
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VIKTIG

@TemplateModelHelpers
object VarselSoning12_20: RedigerbarTemplate<FeilutbetalingSpesifikkVarselDto> {
    override val featureToggle = FeatureToggles.feilutbetalingNy.toggle

    override val kode = UT_FEILUTBETALING_VARSEL_SONING
    override val kategori = FEILUTBETALING
    override val brevkontekst = ALLE
    override val sakstyper = setOf(UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Varsel feilutbetaling soning §12-20",
            distribusjonstype = VIKTIG,
            brevtype = INFORMASJONSBREV
        )
    ) {

        val dato = fritekst("dato")
        val kilde = fritekst("kilde")
        val bruttoFeilutbetalt = pesysData.feilutbetaltBrutto.format(LocalizedFormatter.CurrencyFormat)

        title {
            text(
                bokmal { + "Vi vurderer om du må betale tilbake uføretrygd " },
                nynorsk { + "Vi vurderer om du må betale tilbake uføretrygd " }
            )
        }
        outline {
            paragraph {
                text(
                    bokmal { +"Vi vurderer at du kan ha fått utbetalt for mye i uføretrygd fra og med " + dato + " til og med " +
                        dato + " . Grunnen til det, er at vi har fått opplysninger om at du har gjennomført straff i perioden " + dato + " til " + dato + ". " },
                    nynorsk { + "Vi vurderer at du kan ha fått utbetalt for mykje i uføretrygd frå og med " + dato + " til og med " + dato +
                            ". Grunnen til det er at vi har fått opplysningar om at du har gjennomført straff i perioden " + dato + " til " + dato + ". "})
            }
            paragraph {
                text(
                    bokmal { + "Uføretrygden påvirkes av om du gjennomfører straff. Dette gjelder også for gjennomføring av straff i utlandet. " },
                    nynorsk { + "Uføretrygda blir påverka av om du gjennomfører straff. Dette gjeld òg for gjennomføring av straff i utlandet. "}
                    )
            }
            paragraph {
                text(
                    bokmal { + "I vedtaket av " + dato + ", informerte vi deg om at gjennomføring av straff kan ha ført til at du har fått utbetalt for mye uføretrygd tilbake i tid. " },
                    nynorsk { + "I vedtaket av " + dato + " informerte vi deg om at gjennomføring av straff kan ha ført til at du har fått utbetalt for mykje uføretrygd tilbake i tid. "}
                )
            }
            paragraph {
                text(
                    bokmal { + "Hvis du forsørger barn, skal du likevel få utbetalt 50 prosent av uføretrygden din. " },
                    nynorsk { + "Dersom du forsørgjer barn, skal du likevel få utbetalt 50 prosent av uføretrygda di. "}
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
                    bokmal { + "Vi har den " + dato + " fått opplysninger fra " + kilde + " om at du har gjennomført varetekt, straff eller særreaksjon fra " + dato + ". " + fritekst("Eventuelt til dato/i dag") },
                    nynorsk { + "Vi har den " + dato + " fått opplysningar frå " + kilde + " om at du har gjennomført varetekt, straff eller særreaksjon frå " + dato + ". " + fritekst("Eventuelt til dato/i dag ")}
                    )
            }
            paragraph {
                text(
                    bokmal { + "Vi stanser utbetaling fra og med andre måned etter at den som får uføretrygd settes i varetekt eller soner straff. " +
                        "Dette gjelder også ved tvungent psykisk helsevern, tvungen omsorg eller forvaring (særreaksjon) i anstalt under kriminalomsorgen eller tilsvarende anstalt i utlandet. " },
                    nynorsk { + "Vi stansar utbetaling frå og med den andre månaden etter at den som får uføretrygd blir sett i varetekt eller sonar straff. " +
                            "Dette gjeld òg ved tvungent psykisk helsevern, tvungen omsorg eller forvaring (særreaksjon) i anstalt under kriminalomsorga eller tilsvarande anstalt i utlandet. "}
                )
            }
            paragraph {
                text(
                    bokmal { + "Du fikk utbetalt uføretrygd i perioden " + dato + " til " + dato +
                        " samtidig som du var under kriminalomsorgen. Utbetalingen din skulle vært lavere i denne perioden, og derfor har det skjedd en feilutbetaling på " + bruttoFeilutbetalt + " kroner. " },
                    nynorsk { + "Du fekk utbetalt uføretrygd i perioden " + dato + " til " + dato +
                            " samtidig som du var under kriminalomsorga. Utbetalinga di skulle vore lågare i denne perioden, og derfor har det skjedd ei feilutbetaling på " + bruttoFeilutbetalt + " kroner. "}
                )
            }

            paragraph {
                text(
                    bokmal { + "Du kan lese mer om stans i utbetaling av uføretrygd og soning av straff og særreaksjoner i folketrygdloven § 12-20. " },
                    nynorsk { + "Du kan lese meir om stans i utbetaling av uføretrygd og soning av straff og særreaksjonar i folketrygdlova § 12-20. "}
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
