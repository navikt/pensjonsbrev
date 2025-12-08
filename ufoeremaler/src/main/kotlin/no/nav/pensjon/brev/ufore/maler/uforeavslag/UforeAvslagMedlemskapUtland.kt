package no.nav.pensjon.brev.ufore.maler.uforeavslag

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.expression.notNull
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_AVSLAG_MEDLEMSKAP_UTLAND
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagUtlandDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagUtlandDtoSelectors.SaksbehandlervalgSelectors.visBrukerIkkeOmfattesAvPersonkretsTrygdeforordning
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagUtlandDtoSelectors.SaksbehandlervalgSelectors.visInnvilgetPensjonEOSLand
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagUtlandDtoSelectors.SaksbehandlervalgSelectors.visTekstVedArtikkel57Avslag
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagUtlandDtoSelectors.SaksbehandlervalgSelectors.visVedtakFraAndreLand
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagUtlandDtoSelectors.TrygdetidSelectors.fomDato
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagUtlandDtoSelectors.TrygdetidSelectors.land
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagUtlandDtoSelectors.TrygdetidSelectors.tomDato
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagUtlandDtoSelectors.UforeAvslagPendataSelectors.artikkel
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagUtlandDtoSelectors.UforeAvslagPendataSelectors.avtaletype
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagUtlandDtoSelectors.UforeAvslagPendataSelectors.eosNordisk
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagUtlandDtoSelectors.UforeAvslagPendataSelectors.kravGjelder
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagUtlandDtoSelectors.UforeAvslagPendataSelectors.kravMottattDato
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagUtlandDtoSelectors.UforeAvslagPendataSelectors.trygdetidListe
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagUtlandDtoSelectors.pesysData
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagUtlandDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.ufore.maler.FeatureToggles
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brev.ufore.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK

@TemplateModelHelpers
object UforeAvslagMedlemskapUtland : RedigerbarTemplate<UforeAvslagUtlandDto> {

    override val featureToggle = FeatureToggles.avslagMedlemskapUtland.toggle

    override val kode = UT_AVSLAG_MEDLEMSKAP_UTLAND
    override val kategori = TemplateDescription.Brevkategori.FOERSTEGANGSBEHANDLING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)


    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Avslag uføretrygd - 12-2 Utland",
            distribusjonstype = VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        ),
    )
    {
        title {
            text (bokmal { + "Nav har avslått søknaden din om uføretrygd"})
        }
        outline {
            paragraph {
                text(bokmal { +"Vi har avslått din søknad om uføretrygd som vi fikk den " + pesysData.kravMottattDato.format() + "." })
            }
            title1 {
                text(bokmal { +"Derfor får du ikke uføretrygd" })
            }
            paragraph {
                text(bokmal { +"Du var ikke medlem av folketrygden i de fem siste årene før du ble ufør. Du oppfyller heller ingen av unntaksreglene." })
            }
            paragraph {
                text(bokmal { +"For å ha rett til uføretrygd, må du som hovedregel ha vært medlem av folketrygden i de siste fem årene fram til uføretidspunktet. " +
                        "Vi kan gjøre unntak fra hovedregelen dersom: " })
                list {
                    item {
                        text(bokmal { + "uførheten din skyldes en godkjent yrkesskade eller yrkessykdom " })
                    }
                }
            }
            paragraph {
                text(bokmal { +"Eller dersom du har vært medlem av folketrygden i minst ett år umiddelbart før du søker om uføretrygd, og " })
                list {
                    item {
                        text(bokmal { + "ble ufør før du fylte 26 år og da var medlem av trygden eller " })
                    }
                    item {
                        text(bokmal { + "etter fylte 16 år har vært medlem i trygden med unntak av maksimum fem år " })
                    }
                }
            }
            paragraph {
                text(bokmal { +"Eller dersom du var medlem av folketrygden på uføretidspunktet, og: " })
                list {
                    item {
                        text(bokmal { + "har tjent opp rett til minst halvparten av minsteytelsen for uføretrygd. " })
                    }
                }
            }

            paragraph {
                text(bokmal { +"Du flyttet til Norge " + fritekst("siste innflyttingsdato til Norge") + ", og ble da medlem av folketrygden. " })
            }

            paragraph {
                text(bokmal { + fritekst("Individuell vurdering") })
            }

            paragraph {
                text(bokmal { +"Vi har fastsatt uføretidspunktet ditt til " + fritekst("dato") + ". Da ble inntektsevnen din varig nedsatt med minst halvparten. " })
            }

            paragraph {
                text(bokmal { + fritekst("Individuell vurdering") })
            }

            title1 {
                text(bokmal { +"Sammenlegging etter trygdeavtale" })
            }

            paragraph {
                text(bokmal {+ "Fordi du har hatt opphold i land Norge har trygdeavtale med, har Nav innhentet og vurdert opplysninger om din opptjening i " + fritekst("land") + ". " +
                        "Trygdetid i avtaleland kan legges sammen med trygdetid i Norge for å fylle vilkår om medlemskap. " +
                        "Nav må forholde seg til trygdetiden som trygdemyndighetene i andre land har godskrevet etter sin nasjonale lovgivning. "})
            }

            paragraph {
                text(bokmal { + "Dette er din trygdetid:"})
            }

            paragraph {
                table(
                    header = {
                        column {
                            text(bokmal { +"Land" })
                        }
                        column {
                            text(bokmal { +"Fra og med" })
                        }
                        column {
                            text(bokmal { +"Til og med" })
                        }
                    }
                ) {
                    forEach(pesysData.trygdetidListe) { trygdetidrad ->
                        row {
                            cell {
                                text(bokmal { +trygdetidrad.land })
                            }
                            cell {
                                text(bokmal { +trygdetidrad.fomDato.format() })
                            }
                            cell {
                                text(bokmal { +trygdetidrad.tomDato.format() })
                            }
                        }
                    }
                }
            }

            paragraph {
                text(bokmal { + fritekst("Individuell vurdering medlemsskap og lovvalg") })
            }

            showIf(saksbehandlerValg.visBrukerIkkeOmfattesAvPersonkretsTrygdeforordning) {
                paragraph {
                    text(bokmal { + "For at trygdetid i EØS-land kan brukes, er det et krav om at du er statsborger i et EØS-land. " })
                }
                paragraph {
                    text(bokmal { + "Du er statsborger i " + fritekst("land") + ". Dette landet er ikke et EØS-land. Norge har heller ikke inngått en egen trygdeavtale med dette landet. " })
                }
                paragraph {
                    text(bokmal { + "Fordi du ikke er statsborger i et EØS-land, har du ikke trygderettigheter etter EØS-reglene. " })
                }
                paragraph {
                    text(bokmal { + "Vedtaket er gjort etter folketrygdloven kapittel 2 og 12. Vedtaket er også gjort etter EØS-forordning 883/2004 artikkel 2 og artikkel 6, og Nordisk konvensjon artikkel 3 og artikkel 4." })
                }
            }.orShowIf(saksbehandlerValg.visTekstVedArtikkel57Avslag) {
                paragraph {
                    text(bokmal { + "For at trygdetid i annet EØS-land kan brukes, må du ha minst ett års medlemskap i folketrygden før uføretidspunktet, forutsatt at du har vært yrkesaktiv i Norge eller andre EØS-land. " })
                }
                paragraph {
                    text(bokmal { + "Har du ikke vært yrkesaktiv i Norge eller andre EØS-land, må du ha minst tre års medlemskap i folketrygden før uføretidspunktet." })
                }
                paragraph {
                    text(bokmal { + "Du har ikke vært medlem av folketrygden før " + fritekst("uføretidspunktet / før dato") + ", og fyller dermed ikke minstekravet til trygdetid i Norge." })
                }
                paragraph {
                    text(bokmal { + "Du har ikke rett til uføretrygd fra Norge etter EØS-avtalen." })
                }
                paragraph {
                    text(bokmal { + "Vedtaket er gjort etter folketrygdloven kapittel 2 og § 12-2." })
                }
                paragraph {
                    text(bokmal { + "Vedtaket er også gjort etter EØS-forordning 883/2004 artikkel 52 og artikkel 57." })
                }
            }.orShow {
                paragraph {
                    text(bokmal { + "Når vi legger sammen trygdetiden din fra utland og Norge, oppfyller du fortsatt ikke vilkårene om trygdetid." })
                }

                paragraph {
                    text(bokmal { + fritekst("Eventuell konkret individuell begrunnelse") })
                }

                paragraph {
                    text(bokmal { + "Du mangler trygdetid fra " + fritekst("dato") + " til " + fritekst("dato") + " i femårsperioden før uføretidspunktet. " +
                            "Når vi legger sammen all trygdetiden din, har du også mer enn fem år uten medlemskap etter du fylte 16 år." })
                }

                showIf(saksbehandlerValg.visInnvilgetPensjonEOSLand) {
                    paragraph {
                        text(bokmal { + "I vurderingen har vi tatt hensyn til pensjon du har fått innvilget fra EØS-land, og vi har lagt til grunn en årlig utenlandsk pensjon på " + fritekst("beløp") + " kroner. " +
                                "Kan du dokumentere at den utenlandske pensjonsutbetalingen er høyere, gjør vi en ny vurdering." })
                    }
                }

                paragraph {
                    text(bokmal { +"Vedtaket har vi gjort etter folketrygdloven kapittel 2 og § 12-2. "})
                }

                showIf(pesysData.eosNordisk) {
                    paragraph {
                        text(bokmal { +"Vedtaket har vi også gjort etter EØS-forordning 883/2004 artikkel 6 og artikkel 51, og forskrift om beregning av uføretrygd etter EØS-avtalen av 12. februar 2015." })
                    }
                }.orShowIf(pesysData.avtaletype.notNull() and pesysData.artikkel.notNull()) {
                    showIf(pesysData.avtaletype.equalTo("Storbritannia")) {
                        paragraph {
                            text(bokmal { + "Vedtaket har vi også gjort etter konvensjonen mellom Island, Liechtenstein, Norge og Storbritannia artikkel 10 og artikkel 46." })
                        }
                    }.orShow {
                        paragraph {
                            text(bokmal { + "Vedtaket har vi også gjort etter trygdeavtalen med " + pesysData.avtaletype.ifNull("avtaletype") + " artikkel " + pesysData.artikkel.ifNull("X") + "." })
                        }
                    }
                }.orShow {
                    paragraph {
                        text(bokmal { + fritekst("Du har valgt en avtaletype hvor vi ikke har en forhåndsutfylt tekst. Her må du sette inn tekst som passer til saken.") })
                    }
                }
            }

            showIf(saksbehandlerValg.visVedtakFraAndreLand) {
                title1 {
                    text(bokmal { +"Vedtak fra andre land" })
                }

                paragraph {
                    text(bokmal {+"Vi har mottatt melding fra " + fritekst("land") + " om at du har fått " + fritekst("innvilget/avslått") + " " + fritekst("uføreytelse eller alderspensjon") + "."})
                }
                paragraph {
                    text(bokmal { +"Vedtaket er gjort etter landets egen trygdelovgivning. Du kan lese om begrunnelse, rettigheter og plikter i vedtaket som de skal ha sendt til deg." })
                }
                paragraph {
                    text(bokmal { +"Du finner oversikt over alle vedtak i vedlegget «P1 - Samlet melding om pensjonsvedtak»." })
                }
            }

            includePhrase(Felles.RettTilAKlageLang)
            includePhrase(Felles.RettTilInnsynRefVedlegg)
            includePhrase(Felles.HarDuSporsmal)
        }
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk)
    }
}
