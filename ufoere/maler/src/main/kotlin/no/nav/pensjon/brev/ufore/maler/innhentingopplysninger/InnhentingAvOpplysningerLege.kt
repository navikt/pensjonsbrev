package no.nav.pensjon.brev.ufore.maler.innhentingopplysninger

import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.saksbehandlervalg
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.AlltidValgbareVedlegg.SKJEMA_FOR_BANKOPPLYSNINGER
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_INNHENTING_AV_OPPLYSNINGER_LEGE
import no.nav.pensjon.brev.ufore.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.ufore.api.model.maler.Sakstype
import no.nav.pensjon.brev.ufore.maler.Brevkategori
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VIKTIG

@TemplateModelHelpers
object InnhentingAvOpplysningerLege : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    override val kode = UT_INNHENTING_AV_OPPLYSNINGER_LEGE
    override val kategori = Brevkategori.INNHENTE_OPPLYSNINGER
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper = setOf(Sakstype.UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Innhenting av opplysninger - Lege",
            distribusjonstype = VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        ),
    )
    {
        val helsetilstand = saksbehandlervalg("helsetilstand","Helsetilstand").bool()
        val behandling = saksbehandlervalg("behandling","Behandling").bool()
        val yrkesskadesykdom = saksbehandlervalg("yrkesskadesykdom","Yrkesskade eller -sykdom").bool()
        val ungufor = saksbehandlervalg("ungufor","Ung ufør").bool()

        title {
            text (bokmal { + "Nav ber om tilleggsopplysninger" })
        }
        outline {
            paragraph {
                text(bokmal { + "I forbindelse med behandling av søknad om uføretrygd ber vi om mer utfyllende opplysninger. "})
            }

            showIf(helsetilstand) {
                title1 {
                    text (bokmal { + "Helsetilstand" })
                }
                paragraph {
                    text (
                        bokmal { + "Nav skal gjøre en vurdering i forhold til om brukers sykdom/skade er hovedårsak til varig nedsatt inntektsevne med minst halvparten. I den forbindelse ber vi om tilbakemelding på følgende: " },
                    )
                    numberedList {
                        item {
                            text(bokmal { +"Beskriv hvordan brukers sykdomsbilde påvirker arbeidsevnen." })
                        }
                        item {
                            text(bokmal { +"Fra hvilket tidspunkt anser du at brukers arbeidsevne har vært varig nedsatt med minst halvparten på grunn av sykdom/skade?" })
                        }
                        item {
                            text(bokmal { +"Beskriv utviklingen i brukers sykdomsbilde, fra sykdommen oppstod og frem til i dag." })
                        }
                        item {
                            text(bokmal { +"Beskriv hvordan brukers funksjonsevne antas å bli på sikt." })
                        }
                    }
                }
            }

            showIf(behandling) {
                title1 {
                    text(bokmal { +"Behandling" })
                }
                paragraph {
                    text(
                        bokmal { +"Nav skal gjøre en vurdering i forhold til om bruker har forsøkt all hensiktsmessig behandling for å bedre sin arbeidsevne. I den forbindelse ber vi om tilbakemelding på følgende:" },
                    )
                    numberedList {
                        item {
                            text(bokmal { +"Hvilken behandling eller utredning har bruker gjennomført og hvilke resultater har behandlingen/utredningen gitt?" })
                        }
                        item {
                            text(bokmal { +"Er bruker henvist til ny behandling/utredning?" })
                        }
                        item {
                            text(bokmal { +"Vil videre behandling føre til bedring av brukers arbeidsevne?" })
                        }
                        item {
                            text(bokmal { +"Vil annen behandling kunne føre til økt arbeidsevne?" })
                        }
                    }
                }
            }

            showIf(yrkesskadesykdom) {
                title1 {
                    text(bokmal { +"Yrkesskade eller -sykdom" })
                }
                paragraph {
                    text(
                        bokmal { +"Bruker er registrert med godkjent yrkesskade/yrkessykdom. Vi ønsker i den forbindelse å få opplyst i hvilken betydning denne yrkesskaden/yrkessykdommen har for brukers inntektsevne." },
                    )
                }
            }

            showIf(ungufor) {
                title1 {
                    text(bokmal { +"Ung ufør" })
                }
                paragraph {
                    text(
                        bokmal { +"Bruker skal vurderes etter bestemmelsene i folketrygdloven om vedkommende fyller vilkåret om rettighet som ung ufør. I den forbindelse trenger vi noen opplysninger fra deg." },
                    )
                    numberedList {
                        item {
                            text(bokmal { +"Når var bruker første gang i kontakt med lege for utredning/behandling for " + fritekst("diagnose" + "?") })
                        }
                        item {
                            text(bokmal { +"Fra hvilket tidspunkt har bruker vært varig minst 50 prosent arbeidsufør?" })
                        }
                        item {
                            text(bokmal { +"Mener du brukers sykdom var å anse som alvorlig og varig før bruker fylte 26 år?" })
                        }
                    }
                }
            }

            paragraph {
                text(
                    bokmal { +"Vi ber om at opplysningene sendes til Nav innen " + fritekst("dato") + ". " },
                )
            }
            paragraph {
                text(
                    bokmal { +"Legeerklæring og regning (NAV-blankett NAV 05-04.05) med oppgitt " + fritekst("takstnr.") + " sendes til Nav. Bruker skal ikke betale noen egenandel for erklæringen." }
                )
            }

            paragraph {
                text (
                    bokmal { + "På forhånd takk for godt samarbeid." },
                )
            }
        }
    }
}
