package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.FeatureToggles
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sakstype.*
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkategori.VARSEL
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkontekst.ALLE
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.maler.fraser.Constants
import no.nav.pensjon.brev.maler.fraser.Felles
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_VARSEL_FEILUTBETALING
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.INFORMASJONSBREV
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VIKTIG

@TemplateModelHelpers
object UforeVarselFeilutbetaling : RedigerbarTemplate<EmptyRedigerbarBrevdata> {
    override val featureToggle = FeatureToggles.feilutbetaling.toggle

    override val kode = UT_VARSEL_FEILUTBETALING
    override val kategori: TemplateDescription.Brevkategori = VARSEL
    override val brevkontekst: TemplateDescription.Brevkontekst = ALLE
    override val sakstyper: Set<Sakstype> = setOf(UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Varsel - tilbakekreving av feilutbetalt beløp",
            isSensitiv = false,
            distribusjonstype = VIKTIG,
            brevtype = INFORMASJONSBREV
        )
    ) {
        title {
            text(bokmal { +"Vi vurderer om du må betale tilbake uføretrygd" })
        }
        outline {
            paragraph {
                val dato = fritekst("dato")
                text(
                    bokmal {
                        +"Vi viser til vedtaket vårt " +fritekst("dato") + ". Du har fått " + fritekst("beløp")
                        +" kroner for mye utbetalt i uføretrygd fra "
                        +dato + " til og med " + dato + "."
                    },
                )
            }

            paragraph {
                text(
                    bokmal { +"Hvis du har opplysninger vi bør vite om når vi vurderer om du skal betale tilbake beløpet, ber vi om at du uttaler deg. Det må du gjøre innen 14 dager etter at du har fått dette varselet." },
                )
            }

            paragraph {
                text(
                    bokmal { +"Dette er bare et varsel om at vi vurderer å kreve tilbake det feilutbetalte beløpet. Du får et vedtak når saken er ferdig behandlet." },
                )
            }

            paragraph {
                text(
                    bokmal { +"Hvis vi vedtar at du må betale tilbake hele eller deler av det feilutbetalte beløpet, trekker vi fra skatten på beløpet vi krever tilbake." },
                )
            }
            title1 {
                text(
                    bokmal { +"Dette har skjedd" },
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
                )
            }
            paragraph {
                text(
                    bokmal { +"For å avgjøre om vi kan kreve tilbake, vurderer vi blant annet" },
                )
            }
            paragraph {
                list {
                    item {
                        text(
                            bokmal { +"om du forsto eller burde forstått at beløpet du fikk utbetalt var feil" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"hvor mye du har fått feilutbetalt" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"hvor lang tid det har gått siden utbetalingen" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"om Nav helt eller delvis er skyld i feilutbetalingen" },
                        )
                    }
                }
                text(
                    bokmal { +"Selv om det er Nav som er skyld i feilutbetalingen, kan vi kreve at du betaler tilbake hele eller deler av beløpet." },
                )
            }
            paragraph {
                text(
                    bokmal { +"Hvis du forsto eller burde forstått at du fikk utbetalt feil beløp, skal Nav kreve tilbake hele beløpet." },
                )
            }

            paragraph {
                text(
                    bokmal { +"Dette går fram av folketrygdloven § 22-15." },
                )
            }

            title1 {
                text(
                    bokmal { +"Slik uttaler du deg" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Du har rett til å uttale deg før vi tar den endelige avgjørelsen om tilbakebetaling. Du kan skrive til oss på ${Constants.KONTAKT_URL} eller ringe oss på telefon ${Constants.NAV_KONTAKTSENTER_TELEFON_UFORE}." },
                )
            }

            title1 {
                text(
                    bokmal { +"Hva skjer videre i saken din" },
                )
            }

            paragraph {
                text(
                    bokmal { +"Vi vil vurdere saken og sende deg et vedtak. Hvis du må betale hele eller deler av beløpet, vil du få beskjed om hvordan du betaler tilbake i vedtaket. Nav kan gjøre trekk i framtidige utbetalinger for å kreve inn beløpet." },
                )
            }

            includePhrase(Felles.RettTilInnsyn)
            includePhrase(Felles.HarDuSporsmal)
        }
    }

}
