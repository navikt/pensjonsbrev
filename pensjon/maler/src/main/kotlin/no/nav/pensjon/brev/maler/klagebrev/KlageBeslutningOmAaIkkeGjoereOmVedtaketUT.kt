package no.nav.pensjon.brev.maler.klagebrev

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.model.Brevkategori
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.saksbehandlervalg
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object KlageBeslutningOmAaIkkeGjoereOmVedtaketUT : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    override val featureToggle = FeatureToggles.brevmalKlageBeslutningOmAaIkkeGjoereOmVedtaketUT.toggle

    override val kode = Pesysbrevkoder.Redigerbar.UT_KLAGE_BESLUTNING_AA_IKKE_GJOERE_OM_VEDTAKET
    override val kategori = Brevkategori.KLAGE_OG_ANKE
    override val brevkontekst = TemplateDescription.Brevkontekst.ALLE
    override val sakstyper = setOf(Sakstype.UFOREP)

    override val template = createTemplate(
        languages = languages(Language.Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Klage - beslutning om å ikke gjøre om vedtaket",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        )
    ) {
        val gyldig = saksbehandlervalg("gyldig", "Vedtak er gyldig").bool()

        title {
            text(bokmal {
                +"Beslutning om at vedtak "
                +fritekst("dato") + " ikke gjøres om - vedtaket " })
            showIf(gyldig) {
                text(bokmal { +"er gyldig og " })
            }
            text(bokmal { +"vil fortsatt gjelde for deg" })
        }

        outline {

            paragraph {
                text(bokmal {
                    +"Nav har besluttet at vedtak "
                    +fritekst("dato") + " ikke gjøres om. "
                    +"Vedtaket "
                })
                showIf(gyldig) {
                    text(bokmal {
                        +"er gyldig og "
                    })
                }
                text(bokmal {+"vil fortsatt gjelde for deg. "
                    +"Beslutningen er gjort etter forvaltningsloven § 35."
                })
            }

            title1 { text(bokmal { +"Bakgrunn for saken" }) }
            paragraph {
                text(bokmal {
                    +fritekst(
                        "Skriv en kort beskrivelse av hvorfor resultatet i den tidligere saken ble som det ble. "
                                + "Beskriv hva som har skjedd i saken. "
                                + "Vis til hva som er gjort i eventuelle tidligere omgjøringsvurderinger."
                    )
                })
            }
            paragraph {
                text(bokmal {
                    +"I vedtak "
                    +fritekst("dato") + " avgjorde vi kravet ditt om "
                    +fritekst("fyll inn") + "."
                })
            }

            title1 { text(bokmal { +"I kravet ditt har du lagt vekt på:" }) }
            paragraph {
                text(bokmal {
                    +fritekst(
                        "List opp det eller de viktigste argumentene i kravet. "
                                + "Bruk gjerne punkter med ett argument for hvert punkt."
                    )
                })
                list {
                    item { text(bokmal { +fritekst("argument 1") }) }
                    item { text(bokmal { +fritekst("argument 2") }) }
                    item { text(bokmal { +fritekst("osv.") }) }
                }
            }

            title1 { text(bokmal { +"I vurderingen vår har vi lagt vekt på:" }) }
            paragraph {
                text(bokmal {
                    +fritekst(
                        "List opp de dokumentene som er mest relevante for vurderingen av saken (legeerklæringer, inntektsopplysninger osv.) "
                                + "Bruk gjerne punkter."
                    )
                })
                list {
                    item { text(bokmal { +fritekst("dokument 1") }) }
                    item { text(bokmal { +fritekst("dokument 2") }) }
                    item { text(bokmal { +fritekst("osv.") }) }
                }
            }

            title1 { text(bokmal { +"Vurdering" }) }
            paragraph {
                text(bokmal {
                    +fritekst("De vanligste grunnene til at et vedtak kan gjøres om:")
                })
                list {
                    item { text(bokmal { +fritekst("Saksbehandlingsfeil") }) }
                    item { text(bokmal { +fritekst("Vi har tolket regelverket feil eller vurderingen i vedtaket er feil") }) }
                    item { text(bokmal { +fritekst("Vedtaket skyldes feil eller manglende opplysninger.") }) }
                }
            }
            paragraph { text(bokmal { +fritekst("Ta med det som er relevant for saken.") }) }
            paragraph {
                text(bokmal {
                    +"I kravet ditt om omgjøring skriver du at vedtaket er feil fordi "
                    +fritekst("fyll inn") + ". "
                    +"Vi har kommet til at dine argumenter ikke kan føre frem fordi "
                    +fritekst("fyll inn") + "."
                })
            }
            paragraph {
                text(bokmal {
                    +fritekst(
                        "Gi en kort og konkret begrunnelse for hvorfor vi ikke valgte å omgjøre vedtaket. "
                                + "Ta utgangspunkt i hvert av argumentene til klager og begrunn konkret."
                    )
                })
            }

            title1 { text(bokmal { +"Konklusjon" }) }
            paragraph {
                text(bokmal {
                    +"Vi har vurdert saken og kommet fram til at vedtaket ikke gjøres om. Vedtaket vil fortsatt gjelde for deg."
                })
            }

            title1 { text(bokmal { +"Aktuelt regelverk" }) }
            paragraph {
                text(bokmal {
                    +"Beslutningen er gjort etter forvaltningsloven § 35 og Nav sitt rundskriv til forvaltningsloven. "
                    +"Dette regelverket finner du på lovdata.no."
                })
            }

            title1 { text(bokmal { +"Du kan ikke klage" }) }
            paragraph {
                text(bokmal {
                    +"Denne avgjørelsen er ikke et vedtak om individuelle rettigheter eller plikter (enkeltvedtak), men en prosessuell beslutning. "
                    +"Du kan derfor ikke klage eller anke beslutningen, jf. forvaltningsloven §§ 2 og 28 og lov om anke til trygderetten § 2. "
                })
            }
            paragraph {
                text(bokmal {
                    +"Hvis du er uenig i beslutningen, kan du ta saken til de alminnelige domstolene. "
                    +"Dette gjelder ikke det opprinnelige vedtaket om "
                    +fritekst("fyll inn") + ", men bare hvis du mener det har skjedd feil i vurderingen eller saksbehandlingen etter forvaltningsloven § 35."
                })
            }
            paragraph {
                text(bokmal {
                    +"Du kan også klage til Sivilombudet. "
                    +"Sivilombudet kan ikke selv gjøre om beslutningen eller pålegge myndighetene å gjøre noe, "
                    +"men i de fleste tilfeller retter myndighetene seg etter ombudets uttalelser."
                })
            }

            includePhrase(Felles.RettTilInnsynRedigerbarebrev)

            title1 { text(bokmal { +"Fri rettshjelp" }) }
            paragraph { text(bokmal { +"Du kan få informasjon om fri rettshjelp hos Statsforvalteren eller advokat." }) }

            includePhrase(Felles.HarDuSpoersmaal.ufoeretrygd)
        }
    }
}
