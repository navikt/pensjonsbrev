package no.nav.pensjon.brev.ufore.maler.feilutbetaling.varsel

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LocalizedFormatter
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.VarselFeilutbetalingUforeDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.VarselFeilutbetalingUforeDtoSelectors.SaksbehandlervalgSelectors.rentetillegg
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.VarselFeilutbetalingUforeDtoSelectors.pesysData
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.VarselFeilutbetalingUforeDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.feilutbetaling.VarselFeilutbetalingPesysDataSelectors.feilutbetaltBrutto
import no.nav.pensjon.brev.ufore.maler.FeatureToggles
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object VarselFeilutbetaling : RedigerbarTemplate<VarselFeilutbetalingUforeDto> {
    override val featureToggle = FeatureToggles.feilutbetaling.toggle

    override val kode = Ufoerebrevkoder.Redigerbar.UT_VARSEL_FEILUTBETALING
    override val kategori: TemplateDescription.Brevkategori = TemplateDescription.Brevkategori.FEILUTBETALING
    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.ALLE
    override val sakstyper: Set<Sakstype> = setOf(Sakstype.UFOREP)

    override val template = createTemplate(
        languages = languages(Language.Bokmal, Language.Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Varsel - tilbakekreving av feilutbetalt beløp",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        )
    ) {
        val dato = fritekst("dato")
        val bruttoFeilutbetalt = pesysData.feilutbetaltBrutto.format(LocalizedFormatter.CurrencyFormat)


        title {
            text(
                bokmal { +"Vi vurderer om du må betale tilbake uføretrygd" },
                nynorsk { + "Vi vurderer om du må betale tilbake uføretrygd "}
            )
        }
        outline {
            paragraph {
                text(
                    bokmal {+"Vi viser til vedtaket vårt " + dato + ". Du har fått " + bruttoFeilutbetalt + " kroner for mye utbetalt i uføretrygd fra " + dato + " til og med " + dato + "."},
                    nynorsk { + "Vi viser til vedtaket vårt " + dato + ". Du har fått " + bruttoFeilutbetalt + " kroner for mykje utbetalt i uføretrygd frå " + dato + " til og med " + dato + ". "}
                )
            }

            paragraph {
                text(
                    bokmal { +"Hvis du har opplysninger vi bør vite om når vi vurderer om du skal betale tilbake beløpet, " +
                            "ber vi om at du uttaler deg. Det må du gjøre innen 14 dager etter at du har fått dette varselet." },
                    nynorsk { + "Dersom du har opplysningar vi bør vite om når vi vurderer om du skal betale tilbake beløpet, " +
                            "ber vi om at du uttaler deg. Det må du gjere innan 14 dagar etter at du har fått dette varselet. "}
                )
            }

            paragraph {
                text(
                    bokmal { +"Dette er bare et varsel om at vi vurderer å kreve tilbake det feilutbetalte beløpet. Du får et vedtak når saken er ferdig behandlet." },
                    nynorsk { + "Dette er berre eit varsel om at vi vurderer å krevje tilbake det feilutbetalte beløpet. Du får eit vedtak når saka er ferdig behandla. "},
                )
            }

            paragraph {
                text(
                    bokmal { +"Hvis vi vedtar at du må betale tilbake hele eller deler av det feilutbetalte beløpet, trekker vi fra skatten på beløpet vi krever tilbake." },
                    nynorsk { + "Dersom vi vedtek at du må betale tilbake heile eller delar av det feilutbetalte beløpet, trekkjer vi frå skatten på beløpet vi krev tilbake. "}
                )
            }
            title1 {
                text(
                    bokmal { +"Dette har skjedd" },
                    nynorsk { + "Dette har skjedd "}
                )
            }
            paragraph {
                eval(
                    fritekst(
                        "beskriv hva som har skjedd i saken/ årsaken til feilutbetalingen. " +
                                "Gjør kort rede for årsaken til feilutbetalingen. " +
                                "Er årsaken den samme for hele perioden? " +
                                "Er det vi som har gjort en feil eller er det mottakeren? " +
                                "Har mottakeren gitt feilaktige/mangelfulle opplysninger? " +
                                "Har mottakeren latt være å melde fra om endringer av betydning? " +
                                "Har Nav fulgt opp melding fra mottakeren uten unødig opphold? " +
                                "Har Nav hatt tilgang til opplysningene fra andre kilder enn mottakeren?"
                    )
                )
            }

            title1 {
                text(
                    bokmal { +"Dette legger vi vekt på i vurderingen vår" },
                    nynorsk { + "Dette legg vi vekt på i vurderinga vår "}
                )
            }
            paragraph {
                text(
                    bokmal { +"For å avgjøre om vi kan kreve tilbake, vurderer vi blant annet" },
                    nynorsk { + "For å avgjere om vi kan krevje tilbake, vurderer vi mellom anna: "}
                )
            }
            paragraph {
                list {
                    item {
                        text(
                            bokmal { +"om du forsto eller burde forstått at beløpet du fikk utbetalt var feil" },
                            nynorsk { + "om du forstod eller burde forstått at beløpet du fekk utbetalt var feil "}
                        )
                    }
                    item {
                        text(
                            bokmal { +"om du har gitt riktig informasjon til Nav" },
                            nynorsk { + "om du har gitt rett informasjon til Nav "}
                        )
                    }
                    item {
                        text(
                            bokmal { +"om du har gitt all nødvendig informasjon til Nav i rett tid" },
                            nynorsk { + "om du har gitt all nødvendig informasjon til Nav i rett tid "}
                        )
                    }
                }
                text(
                    bokmal { +"Selv om det er Nav som er skyld i feilutbetalingen, kan vi kreve at du betaler tilbake hele eller deler av beløpet." },
                    nynorsk { + "Sjølv om det er Nav som er skuld i feilutbetalinga, kan vi krevje at du betaler tilbake heile eller delar av beløpet. "}
                )
            }

            paragraph {
                text(
                    bokmal { +"Dette går fram av folketrygdloven § 22-15." },
                    nynorsk { + "Dette går fram av folketrygdlova § 22-15. "}
                )
            }

            showIf(saksbehandlerValg.rentetillegg) {
                title1 {
                    text(
                        bokmal { + "Rentetillegg " },
                        nynorsk { + "Rentetillegg " },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Hvis du bevisst har gitt oss feil eller mangelfull informasjon eller opptrådt grovt uaktsomt, " +
                                "kan vi beregne et rentetillegg på ti prosent av beløpet vi krever tilbakebetalt. Dette går fram av folketrygdloven § 22-17a." },
                        nynorsk { + "Dersom du medvite har gitt oss feil eller mangelfull informasjon eller opptrådt grovt aktlaust, " +
                                "kan vi berekne eit rentetillegg på ti prosent av beløpet vi krev tilbakebetalt. Dette går fram av folketrygdlova § 22-17a.  " },
                    )
                }
            }

            includePhrase(FeilutbetalingFraser.SlikUttalerDuDeg)
            includePhrase(FeilutbetalingFraser.HvaSkjerVidere)
            includePhrase(Felles.RettTilInnsyn)
            includePhrase(Felles.HarDuSporsmal)
        }
    }

}