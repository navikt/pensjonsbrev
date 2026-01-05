package no.nav.pensjon.brev.ufore.maler.feilutbetaling.varsel

import no.nav.pensjon.brev.api.model.Sakstype.UFOREP
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkategori.FEILUTBETALING
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkontekst.VEDTAK
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.LocalizedFormatter
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_FEILUTBETALING_VARSEL_SIVILSTAND
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.feilutbetaling.FeilutbetalingSpesifikkVarselDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.feilutbetaling.FeilutbetalingSpesifikkVarselDtoSelectors.pesysData
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.feilutbetaling.VarselFeilutbetalingPesysDataSelectors.feilutbetaltBrutto
import no.nav.pensjon.brev.ufore.maler.FeatureToggles
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.INFORMASJONSBREV
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VIKTIG

@TemplateModelHelpers
object VarselFeilutbetalingSivilstand12_13_2: RedigerbarTemplate<FeilutbetalingSpesifikkVarselDto> {
    override val featureToggle = FeatureToggles.feilutbetalingNy.toggle

    override val kode = UT_FEILUTBETALING_VARSEL_SIVILSTAND
    override val kategori = FEILUTBETALING
    override val brevkontekst = VEDTAK
    override val sakstyper = setOf(UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Varsel feilutbetaling sivilstand §12-13 andre ledd",
            distribusjonstype = VIKTIG,
            brevtype = INFORMASJONSBREV
        )
    ) {
        val dato = fritekst("dato")
        val kilde = fritekst("kilde")
        val handling = fritekst("handling")
        val prosent = fritekst("prosent")
        val bruttoFeilutbetalt = pesysData.feilutbetaltBrutto.format(LocalizedFormatter.CurrencyFormat)

        title {
            text(
                bokmal { + "Vi vurderer om du må betale tilbake uføretrygd " },
                nynorsk { + "Vi vurderer om du må betale tilbake uføretrygd "}
            )
        }
        outline {
            paragraph {
                text(
                    bokmal { + "Vi vurderer at du kan ha fått utbetalt for mye i uføretrygd fra og med " +  dato + " til og med " +  dato + ". " +
                            "Grunnen til det, er at vi har fått opplysninger om at du har fått en samboer eller ektefelle." },
                    nynorsk { + "Vi vurderer at du kan ha fått utbetalt for mykje i uføretrygd frå og med " +  dato + " til og med " +  dato + ". Grunnen til det er at vi har fått opplysningar om at du har fått ein sambuar eller ektefelle. " }
                )
            }
            paragraph {
                text(
                    bokmal { + "Din sivilstand påvirker beregningen av din uføretrygd, og er årsaken til at vi vurderer at du har fått utbetalt for mye. " +
                            "I vedtaket av " +  dato + ", informerte vi deg om at endringen i din sivilstand kan ha ført til feilutbetalinger tilbake i tid. " },
                    nynorsk { + "Sivilstanden din påverkar berekninga av uføretrygda di, og er årsaka til at vi vurderer at du har fått utbetalt for mykje. " +
                            "I vedtaket av " +  dato + " informerte vi deg om at endringa i sivilstanden din kan ha ført til feilutbetalingar tilbake i tid. " }
                )
            }
            includePhrase(FeilutbetalingFraser.FeilOpplysninger)
            title1 {
                text(
                    bokmal { + "Derfor mener vi du har fått utbetalt for mye " },
                    nynorsk { + "Derfor meiner vi du har fått utbetalt for mykje " }
                )
            }
            paragraph {
                text(
                    bokmal { + "Vi har den " +  dato + " mottatt opplysninger fra " +  kilde +
                            " om at du " +  handling + " den " +  dato + ". " },
                    nynorsk { + "Vi har den " +  dato + " mottatt opplysningar frå " +  kilde +
                            " om at du " +  handling + " den " +  dato + ". " }
                )
            }
            paragraph {
                text(
                    bokmal { + "Den " + dato + " fikk du vedtak om innvilgelse av " + prosent + " prosent uføretrygd. I søknaden dato fylte du ut at du var enslig. " },
                    nynorsk { + "" +  dato + " fekk du vedtak om innvilging av " + prosent + " prosent uføretrygd. I søknaden dato fylte du ut at du var einsleg. " }
                )
            }
            paragraph {
                text(
                    bokmal { + "Fordi du får uføretrygd beregnet etter reglene for minsteytelse, påvirker din sivilstand beregningen av din uføretrygd. " },
                    nynorsk { + "Fordi du får uføretrygd berekna etter reglane for minsteyting, påverkar sivilstanden din berekninga av uføretrygda di. "}
                )
            }
            paragraph {
                text(
                    bokmal { + "Du får en lavere utbetaling av uføretrygd fra måneden etter at sivilstanden din ble endret. Dette står i folketrygdloven § 22-12 femte ledd. " },
                    nynorsk { + "Du får ei lågare utbetaling av uføretrygd frå månaden etter at sivilstanden din vart endra. Dette står i folketrygdlova § 22-12 femte ledd. "}
                )
            }
            paragraph {
                text(
                    bokmal { + "Du fikk utbetalt uføretrygd som enslig i perioden " +  dato + " til " +
                            dato + " , og her har det oppstått en feilutbetaling på " + bruttoFeilutbetalt  + ". " +
                            " kroner. Utbetalingen din skulle vært endret fra måneden etter at du " +  handling + ". " },
                    nynorsk { + "Du fekk utbetalt uføretrygd som einsleg i perioden " +  dato + " til " +
                            dato + ", og her har det oppstått ei feilutbetaling på " + bruttoFeilutbetalt  + ". " +
                            " kroner. Utbetalinga di skulle vore endra frå månaden etter at du " +  handling + ". "}
                )
            }
            paragraph {
                text(
                    bokmal { + fritekst("Fritekst")},
                    nynorsk { + fritekst("Fritekst")}
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



