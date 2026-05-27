package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.maler.felles.Constants
import no.nav.pensjon.brev.alder.maler.felles.HarDuSpoersmaal
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.afp.AvslagAfpPrivatAutoDto
import no.nav.pensjon.brev.alder.model.afp.AvslagAfpPrivatAutoDtoSelectors.begrunnelse
import no.nav.pensjon.brev.alder.model.afp.AvslagAfpPrivatAutoDtoSelectors.kravMottattDato
import no.nav.pensjon.brev.alder.model.afp.AvslagAfpPrivatDto.Begrunnelse
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

/**
 * Auto-vedtak — avslag på AFP i privat sektor.
 *
 * Konvertert fra Exstream-malen `PE_AF_04_116`. Dette er autobrev-varianten
 * av den redigerbare malen [AvslagAfpPrivat] (PE_AF_04_112) og bruker samme
 * [Begrunnelse]-enum. Originalen har kun bokmål og nynorsk.
 */
@TemplateModelHelpers
object AvslagAfpPrivatAuto : AutobrevTemplate<AvslagAfpPrivatAutoDto> {

    override val kode = Aldersbrevkoder.AutoBrev.PE_AFP_PRIVAT_AVSLAG_AUTO

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - avslag på AFP i privat sektor",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                bokmal { +"Søknaden din om avtalefestet pensjon (AFP) i privat sektor er avslått - melding om vedtak" },
                nynorsk { +"Søknaden din om avtalefesta pensjon (AFP) i privat sektor er avslått - melding om vedtak" },
            )
        }

        outline {
            paragraph {
                text(
                    bokmal {
                        +"Nav viser til søknaden din om AFP i privat sektor mottatt " +
                            kravMottattDato.format() + ". Søknaden din er avslått."
                    },
                    nynorsk {
                        +"Nav viser til søknaden din om AFP i privat sektor motteken " +
                            kravMottattDato.format() + ". Søknaden din er avslått."
                    },
                )
            }

            title1 {
                text(
                    bokmal { +"Begrunnelse for vedtaket" },
                    nynorsk { +"Grunngiving for vedtaket" },
                )
            }

            paragraph {
                text(
                    bokmal {
                        +"AFP i privat sektor gis etter bestemmelsene i lov om statstilskott til " +
                            "arbeidstakere som tar ut avtalefestet pensjon i privat sektor " +
                            "(AFP-tilskottsloven). Fellesordningen for AFP har funnet at du oppfyller de " +
                            "avtalemessige vilkårene for rett til AFP. Nav har avgjort andre spørsmål om " +
                            "retten til pensjon."
                    },
                    nynorsk {
                        +"AFP i privat sektor blir gitt etter reglane i lov om statstilskot til " +
                            "arbeidstakarar som tek ut avtalefesta pensjon i privat sektor " +
                            "(AFP-tilskotslova). Fellesordninga for AFP har funne at du oppfyller dei " +
                            "avtalemessige vilkåra for rett til AFP. Nav har avgjort andre spørsmål om " +
                            "retten til pensjon."
                    },
                )
            }

            // Hjemmelshenvisning gruppert per § (samme inndeling som AvslagAfpPrivat).
            showIf(begrunnelse.isOneOf(Begrunnelse.UNDER_62)) {
                paragraph {
                    text(
                        bokmal { +"Dette vedtaket er gjort etter AFP-tilskottsloven paragraf 5 første ledd bokstav a." },
                        nynorsk { +"Dette vedtaket er gjort etter AFP-tilskotslova paragraf 5 første ledd bokstav a." },
                    )
                }
            }

            showIf(begrunnelse.isOneOf(Begrunnelse.BRUKER_AVSLAG_AP, Begrunnelse.BRUKER_IKKE_AP)) {
                paragraph {
                    text(
                        bokmal { +"Dette vedtaket er gjort etter AFP-tilskottsloven paragraf 5 første ledd bokstav b." },
                        nynorsk { +"Dette vedtaket er gjort etter AFP-tilskotslova paragraf 5 første ledd bokstav b." },
                    )
                }
            }

            showIf(begrunnelse.isOneOf(Begrunnelse.BRUKER_UP_E_62, Begrunnelse.BRUKER_LOP_UP, Begrunnelse.BRUKER_UT_E_62)) {
                paragraph {
                    text(
                        bokmal { +"Dette vedtaket er gjort etter AFP-tilskottsloven paragraf 8 første ledd." },
                        nynorsk { +"Dette vedtaket er gjort etter AFP-tilskotslova paragraf 8 første ledd." },
                    )
                }
            }

            showIf(begrunnelse.isOneOf(Begrunnelse.BRUKER_AVSLAG_AP)) {
                paragraph {
                    text(
                        bokmal {
                            +"AFP i privat sektor før fylte 70 år må tas ut sammen med alderspensjon " +
                                "fra folketrygden. Søknaden din om avtalefestet pensjon er avslått " +
                                "fordi du har fått avslag på søknaden din om alderspensjon. Hvis du ved " +
                                "eventuell senere rett til alderspensjon fra folketrygden også ønsker å " +
                                "ta ut AFP i privat sektor, må du sende inn ny søknad om AFP. Vi gjør " +
                                "oppmerksom på at du på uttakstidspunktet for AFP må være reell " +
                                "arbeidstaker i en bedrift som omfattes av AFP-ordningen."
                        },
                        nynorsk {
                            +"AFP i privat sektor før fylte 70 år må takast ut saman med alderspensjon " +
                                "frå folketrygda. Søknaden din om avtalefesta pensjon er avslått fordi " +
                                "du har fått avslag på søknaden din om alderspensjon. Dersom du ved " +
                                "eventuell seinare rett til alderspensjon frå folketrygda òg ønskjer å " +
                                "ta ut AFP i privat sektor, må du sende inn ny søknad om AFP. Vi gjer " +
                                "merksam på at du på uttakstidspunktet for AFP må vere reell " +
                                "arbeidstakar i ei verksemd som er omfatta av AFP-ordninga."
                        },
                    )
                }
            }

            showIf(begrunnelse.isOneOf(Begrunnelse.BRUKER_IKKE_AP)) {
                paragraph {
                    text(
                        bokmal {
                            +"AFP i privat sektor før fylte 70 år må tas ut sammen med alderspensjon " +
                                "fra folketrygden. Søknaden din om avtalefestet pensjon er avslått " +
                                "fordi du verken mottar eller har søkt om å ta ut alderspensjon. Hvis " +
                                "du ved eventuell senere rett til alderspensjon fra folketrygden også " +
                                "ønsker å ta ut AFP i privat sektor, må du sende inn ny søknad om AFP. " +
                                "Vi gjør oppmerksom på at du på uttakstidspunktet for AFP må være reell " +
                                "arbeidstaker i en bedrift som omfattes av AFP-ordningen."
                        },
                        nynorsk {
                            +"AFP i privat sektor før fylte 70 år må takast ut saman med alderspensjon " +
                                "frå folketrygda. Søknaden din om avtalefesta pensjon er avslått fordi " +
                                "du verken får eller har søkt om å ta ut alderspensjon. Dersom du ved " +
                                "eventuell seinare rett til alderspensjon frå folketrygda òg ønskjer å " +
                                "ta ut AFP i privat sektor, må du sende inn ny søknad om AFP. Vi gjer " +
                                "merksam på at du på uttakstidspunktet for AFP må vere reell " +
                                "arbeidstakar i ei verksemd som er omfatta av AFP-ordninga."
                        },
                    )
                }
            }

            showIf(begrunnelse.isOneOf(Begrunnelse.BRUKER_UP_E_62)) {
                paragraph {
                    text(
                        bokmal {
                            +"AFP i privat sektor kan ikke gis til personer som etter fylte 62 år har " +
                                "mottatt uførepensjon fra folketrygden. Søknaden din er avslått fordi " +
                                "du har mottatt uførepensjon etter at du fylte 62 år. Du vil derfor " +
                                "heller ikke senere kunne få rett til AFP."
                        },
                        nynorsk {
                            +"AFP i privat sektor kan ikkje gjevast til personar som etter fylte 62 år " +
                                "har fått uførepensjon frå folketrygda. Søknaden din er avslått fordi " +
                                "du har fått uførepensjon etter at du fylte 62 år. Du vil heller ikkje " +
                                "seinare kunne få rett til AFP i privat sektor."
                        },
                    )
                }
            }

            showIf(begrunnelse.isOneOf(Begrunnelse.BRUKER_UT_E_62)) {
                paragraph {
                    text(
                        bokmal {
                            +"AFP i privat sektor kan ikke gis til personer som etter fylte 62 år har " +
                                "mottatt uføretrygd fra folketrygden. Søknaden din er avslått fordi du " +
                                "har mottatt uføretrygd etter at du fylte 62 år. Som hovedregel vil du " +
                                "heller ikke kunne få rett til AFP senere, selv om du skulle si ifra " +
                                "deg uføretrygden. Det finnes enkelte unntak fra denne regelen. Du " +
                                "finner mer informasjon om dette på " +
                                Constants.AFP_PRIVAT_UFORETRYGD_URL + "."
                        },
                        nynorsk {
                            +"AFP i privat sektor kan ikkje gjevast til personar som etter fylte 62 år " +
                                "har fått uføretrygd frå folketrygda. Søknaden din er avslått fordi du " +
                                "har fått uføretrygd etter at du fylte 62 år. Som hovudregel vil du " +
                                "heller ikkje kunne få rett til AFP seinare, sjølv om du skulle seie " +
                                "ifrå deg uføretrygda. Det fins enkelte unntak frå denne regelen. Du " +
                                "finn meir informasjon om dette på " +
                                Constants.AFP_PRIVAT_UFORETRYGD_URL + "."
                        },
                    )
                }
            }

            showIf(begrunnelse.isOneOf(Begrunnelse.BRUKER_LOP_UP)) {
                paragraph {
                    text(
                        bokmal {
                            +"AFP i privat sektor kan ikke gis til personer som etter fylte 62 år har " +
                                "mottatt uførepensjon fra folketrygden. Søknaden din er avslått fordi " +
                                "du mottar uførepensjon. Du vil derfor heller ikke senere kunne få " +
                                "rett til AFP i privat sektor."
                        },
                        nynorsk {
                            +"AFP i privat sektor kan ikkje gjevast til personar som etter fylte 62 år " +
                                "har fått uførepensjon frå folketrygda. Søknaden din er avslått fordi " +
                                "du får uførepensjon. Du vil derfor heller ikkje seinare kunne få rett " +
                                "til AFP i privat sektor."
                        },
                    )
                }
            }

            showIf(begrunnelse.isOneOf(Begrunnelse.UNDER_62)) {
                paragraph {
                    text(
                        bokmal { +"AFP i privat sektor kan ikke innvilges fordi du ikke har fylt 62 år på virkningstidspunktet." },
                        nynorsk { +"AFP i privat sektor kan ikkje innvilgast fordi du ikkje har fylt 62 år på verknadstidspunktet." },
                    )
                }
            }

            title1 {
                text(
                    bokmal { +"Dine rettigheter" },
                    nynorsk { +"Dine rettar" },
                )
            }

            paragraph {
                text(
                    bokmal { +"Du har som hovedregel rett til å se sakens dokumenter etter bestemmelsene i forvaltningsloven paragraf 18." },
                    nynorsk { +"Du har som hovudregel rett til å sjå saksdokumenta etter reglane i forvaltingslova paragraf 18." },
                )
            }

            paragraph {
                text(
                    bokmal {
                        +"Du kan klage på avslaget etter bestemmelsene i folketrygdloven paragraf 21-12. " +
                            "Fristen for å klage er seks uker fra du mottar dette brevet. Vedlagt finner " +
                            "du en orientering om klage- og ankebehandling."
                    },
                    nynorsk {
                        +"Du kan klage på avslaget etter reglane i folketrygdlova paragraf 21-12. Fristen " +
                            "for å klage er seks veker frå du får dette brevet. Vedlagt finn du ei " +
                            "orientering om klage- og ankebehandling."
                    },
                )
            }

            includePhrase(HarDuSpoersmaal.afpPrivat)

            paragraph {
                text(
                    bokmal { +"Søknaden har blitt automatisk saksbehandlet av vårt fagsystem. Vedtaksbrevet er derfor ikke underskrevet av saksbehandler." },
                    nynorsk { +"Søknaden er blitt automatisk saksbehandla av fagsystemet vårt. Vedtaksbrevet er derfor ikkje underskrive av saksbehandlar." },
                )
            }
        }
    }
}
