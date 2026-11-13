package no.nav.pensjon.brev.maler.klagebrev

import no.nav.pensjon.brev.api.model.Sakstype.*
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkontekst
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder.Redigerbar.*
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgEnum
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.klagebrev.tekstNAY.DuHarRettTilInnsynKlage
import no.nav.pensjon.brev.maler.klagebrev.tekstNAY.DuKanKlagePaaVedtaketKlage
import no.nav.pensjon.brev.model.Brevkategori.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.saksbehandlervalg
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.*
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK


@TemplateModelHelpers
object KlageVedtakAvvisningAvKlageUT : RedigerbarTemplate<EmptyRedigerbarBrevdata> {
    override val featureToggle = FeatureToggles.brevmalKlageVedtakAvvisningAvKlageUT.toggle

    override val kode = UT_KLAGE_VEDTAK_AVVISNING_AV_KLAGE
    override val kategori = KLAGE_OG_ANKE
    override val brevkontekst = Brevkontekst.VEDTAK
    override val sakstyper = setOf(UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak om avvisning av klage",
            distribusjonstype = VEDTAK,
            brevtype = VEDTAKSBREV,
        )
    ) {
        val avvisningsgrunn = saksbehandlervalg("avvisningsgrunn", "Velg avvisningsgrunn:").enum<Avvisningsgrunn>()

        title { text(bokmal { +"Vedtak om avvisning av klage" }) }

        outline {
            ifNotNull(avvisningsgrunn) {
                paragraph {
                    text(bokmal {
                        +"Klagen din av "
                        +fritekst("dato") + " er avvist fordi klagen "
                    })
                    showIf(avvisningsgrunn.isOneOf(Avvisningsgrunn.FOR_SENT_FREMSATT)) {
                        text(bokmal { +"er for sent fremsatt." })
                    }.orShowIf(avvisningsgrunn.isOneOf(Avvisningsgrunn.IKKE_OPPFYLT_FORMKRAV)) {
                        text(bokmal { +"ikke oppfyller formkravene." })
                    }.orShowIf(avvisningsgrunn.isOneOf(Avvisningsgrunn.MANGLENDE_FULLMAKT)) {
                        text(bokmal { +"er sendt inn av fullmektig uten gyldig fullmakt." })
                    }.orShowIf(avvisningsgrunn.isOneOf(Avvisningsgrunn.IKKE_OPPFYLT_OMGJOERINGSKRAV)) {
                        text(bokmal { +"ikke oppfyller omgjøringskravene." })
                    }

                }

                title1 { text(bokmal { +"Vurdering og konklusjon" }) }

                showIf(avvisningsgrunn.isOneOf(Avvisningsgrunn.FOR_SENT_FREMSATT)) {
                    paragraph {
                        text(bokmal {
                            +"Vi fattet den "
                            +fritekst("dato") + " vedtak om "
                            +fritekst("kort om innholdet i vedtaket") + "."
                        })
                    }
                    paragraph {
                        text(bokmal {
                            +"Du ble i vedtaket orientert om klageadgang og klagefrist. "
                            +"Klagefristen ble satt til 6 uker. "
                            +"Du har klaget "
                            +fritekst("antall dager/måneder") + " etter fristen og derfor klaget for sent."
                        })
                    }
                    paragraph {
                        text(bokmal {
                            +fritekst(
                                "Veiledning: Dette avsnittet under kan brukes dersom klager er reservert i KRR og har fått vedtaksbrev i fysisk post, "
                                        + "og/eller klagen er mottatt i posten og innscannet. "
                                        + "Legg inn alternativ 7 eller 8 uker ift hvordan vedtaket er mottatt av bruker, eller klagen er sendt til oss. "
                                        + "For eksempel: vedtaket er sendt elektronisk, men klagen er mottatt per post = frist opptil 7 uker. "
                                        + "Dersom både vedtak og klage er sendt via post, så er fristen opptil 8 uker: se forklaring i rundskrivet. "
                                        + "Husk å vurdere særlige grunner for både digitale og analoge saker."
                            )
                        })
                    }
                    paragraph {
                        text(bokmal {
                            +"Ifølge Nav sine retningslinjer kan det legges til postgang på opptil en uke dersom du er reservert mot elektroniske meldinger, "
                            +"og/eller en uke postgang dersom du har sendt inn klage per post. "
                            +"Din klagefrist er derfor "
                            +fritekst("antall") + " uker. "
                            +fritekst("Det er da tatt hensyn til postgang og andre omstendigheter som kan forklare en oversittelse av 6 ukers fristen.")
                        })
                    }
                }.orShowIf(avvisningsgrunn.isOneOf(Avvisningsgrunn.IKKE_OPPFYLT_FORMKRAV)) {
                    paragraph {
                        text(bokmal {
                            +"Klagen skal være skriftlig og undertegnet av klager eller fullmektig. "
                            +"Klagen skal nevne vedtaket som det klages over, og hvilken endring som ønskes i vedtaket.  "
                        })
                    }
                    paragraph {
                        text(bokmal {
                            +"Vi fattet den "
                            +fritekst("dato") + " vedtak om "
                            +fritekst("kort om innholdet i vedtaket") + ". Den "
                            +fritekst("dato") + " mottok vi en klage over dette vedtaket."
                        })
                    }
                    paragraph {
                        text(bokmal {
                            +"Skriv kort hva klager har skrevet og hvorfor dette ikke fyller formkravene."
                        })
                    }
                    paragraph {
                        text(bokmal {
                            +"Den " + fritekst("dato") + " sendte vi deg et brev hvor vi opplyste om manglene ved klagen. "
                            +"Du fikk frist til å rette manglene innen "
                            +fritekst("dato") + ". Vi har ikke mottatt tilbakemelding på dette. "
                        })
                    }
                    paragraph {
                        text(bokmal {
                            +"Vi vurderer at klagen ikke oppfyller formkravene for klage. "
                        })
                    }
                }.orShowIf(avvisningsgrunn.isOneOf(Avvisningsgrunn.MANGLENDE_FULLMAKT)) {
                    paragraph {
                        text(bokmal {
                            +"En fullmektig som ikke er advokat, må fremlegge skriftlig fullmakt. Klagen skal være undertegnet av klageren eller fullmektig."
                        })
                    }
                    paragraph {
                        text(bokmal {
                            +"Den " + fritekst("dato") + " sendte vi deg et brev hvor vi opplyste om manglene ved klagen. "
                            +"Du fikk frist til å rette manglene innen "
                            +fritekst("dato") + ". Vi har ikke mottatt tilbakemelding på dette. "
                        })
                    }
                }.orShowIf(avvisningsgrunn.isOneOf(Avvisningsgrunn.IKKE_OPPFYLT_OMGJOERINGSKRAV)) {
                    paragraph {
                        text(bokmal {
                            +"Vi viser til vår beslutning av "
                            +fritekst("dato") + " der det fremgår følgende:"
                        })
                    }
                    paragraph {
                        text(bokmal {
                            +fritekst("Du kan ikke klage .....")
                        })
                    }
                    paragraph {
                        text(bokmal {
                            +"Denne avgjørelsen er ikke et vedtak om individuelle rettigheter eller plikter (enkeltvedtak), men en prosessuell beslutning. "
                            +"Du kan derfor ikke klage eller anke beslutningen, jf. forvaltningsloven §§ 2 og 28 og lov om anke til trygderetten § 2."
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
                }

                paragraph {
                    text(bokmal {
                        +"Vilkårene for å behandle klagen er derfor ikke oppfylt og klagen avvises."
                    })
                }
                paragraph {
                    text(bokmal {
                        +"Vedtaket er gjort etter forvaltningsloven "
                    })
                    showIf(avvisningsgrunn.isOneOf(Avvisningsgrunn.FOR_SENT_FREMSATT)) {
                        text(bokmal { +"§ 33." })
                    }.orShowIf(avvisningsgrunn.isOneOf(Avvisningsgrunn.IKKE_OPPFYLT_FORMKRAV)) {
                        text(bokmal { +"§§ 32 og 33." })
                    }.orShowIf(avvisningsgrunn.isOneOf(Avvisningsgrunn.MANGLENDE_FULLMAKT)) {
                        text(bokmal { +"§ 33." })
                    }.orShowIf(avvisningsgrunn.isOneOf(Avvisningsgrunn.IKKE_OPPFYLT_OMGJOERINGSKRAV)) {
                        text(bokmal { +"§§ 2 og 28." })
                    }
                }

                includePhrase(DuKanKlagePaaVedtaketKlage)
                includePhrase(DuHarRettTilInnsynKlage)
                includePhrase(Felles.HarDuSpoersmaal.ufoeretrygd)
            }
        }
    }

    enum class Avvisningsgrunn(override val displayText: String, override val rekkefoelge: Int) : SaksbehandlerValgEnum {
        FOR_SENT_FREMSATT("Klagen er for sent fremsatt", 1),
        IKKE_OPPFYLT_FORMKRAV("Klagen oppfyller ikke formkravene", 2),
        MANGLENDE_FULLMAKT("Klagen er sendt inn av fullmektig uten gyldig fullmakt", 3),
        IKKE_OPPFYLT_OMGJOERINGSKRAV("Klagen oppfyller ikke omgjøringskravene", 4),
    }
}

