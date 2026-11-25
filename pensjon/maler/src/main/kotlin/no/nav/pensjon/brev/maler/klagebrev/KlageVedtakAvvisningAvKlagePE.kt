package no.nav.pensjon.brev.maler.klagebrev

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sakstype.Companion.pensjon
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkontekst
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder.Redigerbar.*
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.model.Brevkategori.KLAGE_OG_ANKE
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.saksbehandlervalg
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.*
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.*

@TemplateModelHelpers
object KlageVedtakAvvisningAvKlagePE : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    override val featureToggle = FeatureToggles.brevmalKlageVedtakAvvisningAvKlagePE.toggle

    override val kode = PE_KLAGE_VEDTAK_AVVISNING_AV_KLAGE
    override val kategori = KLAGE_OG_ANKE
    override val brevkontekst = Brevkontekst.VEDTAK
    override val sakstyper: Set<Sakstype> = pensjon

    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - avvisning av klage",
            distribusjonstype = VEDTAK,
            brevtype = VEDTAKSBREV,
        )
    ) {
        val forSentFramsattKlage = saksbehandlervalg("forSentFramsattKlage", "Ved for sent framsatt klage").bool()
        val manglendeUnderskrift = saksbehandlervalg("manglendeUnderskrift", "Ved muntlige klage / manglende underskrift").bool()
        val manglendeRettsligKlageinteresse = saksbehandlervalg("manglendeRettsligKlageinteresse", "Ved manglende rettslig klageinteresse").bool()
        val ikkePartISaken = saksbehandlervalg("ikkePartISaken", "Hvis klageren ikke er part i saken").bool()
        val manglendeFullmakt = saksbehandlervalg("manglendeFullmakt", "Ved manglende fullmakt").bool()

        title { text(bokmal { +"Vi har avvist klagen din" }) }

        outline {

            paragraph {
                text(bokmal {
                    +"Klagen av "
                    +fritekst("dato") + " over Nav familie- og pensjonsytelser sitt vedtak av " + fritekst("dato") + " avvises fordi "
                    +fritekst("påpek manglene i begrunnelsen (f.eks. for sent fremsatt, manglende fullmakt)")
                })
            }

            title1 { text(bokmal { +"Rettslig grunnlag" }) }
            paragraph {
                text(bokmal {
                    +"Det organet som har fattet vedtaket som er påklaget skal avvise saken hvis vilkårene for å behandle den ikke foreligger. "
                    +"Dette framgår av folketrygdloven § 21-1, jf. forvaltningsloven § 33 andre ledd."
                })
            }

            title1 { text(bokmal { +"Vurdering og konklusjon" }) }
            paragraph {
                text(bokmal {
                    +"Nav familie- og pensjonsytelser fattet den "
                    +fritekst("dato") + " vedtak om "
                    +fritekst("kort om innholdet i vedtaket") + ". "
                })
            }
            paragraph {
                text(bokmal {
                    +"Den "
                    +fritekst("dato") + " mottok vi klagen over dette vedtaket."
                })
            }

            showIf(forSentFramsattKlage.equalTo(true)) {
                paragraph {
                    text(bokmal {
                        +"I vedtaket ble du orientert om klageadgang og klagefrist. Klagen din oversitter klagefristen med "
                        +fritekst("antall dager/måneder") + " og er framsatt for sent."
                    })
                }
                paragraph {
                    text(bokmal {
                        +"Dersom det foreligger særlige grunner til at klagefristen er oversittet, "
                        +"eller det er særlige grunner til at klagen skal bli prøvd, "
                        +"kan det unntaksvis ses bort fra oversittelsen. "
                        +"Dette følger av forvaltningsloven § 31 første ledd."
                    })
                }
                paragraph {
                    text(bokmal {
                        +"Trygderettspraksis viser at det skal svært mye til før det ses bort fra oversittelse av klagefristen. "
                        +"Bakgrunnen er at klagefristen i trygdesaker er vesentlig lengre enn ankefristen i sivile saker for domstolene og dobbelt så lang som klagefristen i andre deler av forvaltningen. "
                        +"Det er derfor lagt opp til at bestemmelsen skal forstås bokstavelig. "

                    })
                }
                paragraph {
                    text(bokmal {
                        +fritekst("(Eksempel: Brukeren har ikke påberopt seg særlige grunner. Tilpass teksten og drøftelsen nedenfor slik at de passer den konkrete saken.)")
                    })
                }
                paragraph {
                    text(bokmal {
                        +"Du har ikke påberopt deg særlige grunner til at klagen er fremsatt for sent. "
                        +"Etter en gjennomgang av saken kan vi heller ikke se at det foreligger forhold ved vedtakets innhold som kan utgjøre en særlig grunn til å behandle saken på nytt til tross for at klagefristen er oversittet."
                    })
                }
            }

            showIf(manglendeUnderskrift.equalTo(true)) {
                paragraph {
                    text(bokmal {
                        +"Det følger av forvaltningsloven § 32 første ledd bokstav a og b at klagen må være skriftlig og undertegnet, "
                        +"eller være påført en kvalifisert elektronisk signatur, "
                        +"jf. lov om elektroniske tillitstjenester § 1 og europaparlaments- og rådsforordning (EU) nr. 910/2014 "
                        +"om elektronisk identifikasjon og tillitstjenester for elektroniske transaksjoner i det indre marked (eIDAS-forordningen) artikkel 25 nr. 2."
                    })
                }
                paragraph {
                    text(bokmal {
                        +"Den "
                        +fritekst("dato") + " sendte vi deg et brev hvor vi opplyste om manglene ved klagen. "
                        +"Fristen for å rette disse manglene ble satt til "
                        +fritekst("dato") + ". Vi har ikke mottatt tilbakemelding på dette."
                    })
                }
            }

            showIf(manglendeRettsligKlageinteresse.equalTo(true)) {
                paragraph {
                    text(bokmal {
                        +"Etter forvaltningsloven § 28 første ledd må klageren ha rettslig klageinteresse. "
                        +"I dette ligger det at klageren må ha et reelt behov for å få realitetsbehandlet saken sin. "
                        +"Hvis en realitetsbehandling av klagen ikke vil gi noen bedre rettighet for klageren enn det vedkommende allerede har oppnådd, kan ikke parten sies å ha rettslig klageinteresse."
                    })
                }
                paragraph {
                    text(bokmal {
                        +fritekst("Oppgi relevant faktum som viser at klageren ikke har rettslig klageinteresse")
                    })
                }
                paragraph {
                    text(bokmal { +"Etter dette kan du ikke sies å ha rettslig klageinteresse." })
                }
            }

            showIf(ikkePartISaken.equalTo(true)) {
                paragraph {
                    text(bokmal {
                        +"Etter forvaltningsloven § 28 første ledd kan et enkeltvedtak påklages av part i saken eller annen med rettslig klageinteresse. "
                        +"Saken gjelder klage over vedtak om "
                        +fritekst("ytelse") + " datert "
                        +fritekst("dato") + "."
                    })
                }
                paragraph {
                    text(bokmal {
                        +fritekst("Oppgi relevant faktum som viser at klageren ikke er part i saken")
                    })
                }
                paragraph {
                    text(bokmal { +"Du anses derfor ikke som part i denne saken." })
                }
            }

            showIf(manglendeFullmakt.equalTo(true)) {
                paragraph {
                    text(bokmal {
                        +"Det følger av forvaltningsloven § 12 siste ledd at en fullmektig som ikke er advokat må fremlegge skriftlig fullmakt."
                    })
                }
                paragraph {
                    text(bokmal {
                        +fritekst("Oppgi relevant faktum som viser at fullmakten mangler")
                    })
                }
                paragraph {
                    text(bokmal { +"Vilkårene for å behandle klagen er dermed ikke oppfylt og klagen avvises, jf. forvaltningsloven § 33 andre ledd." })
                }
            }

            title1 { text(bokmal { +"Informasjon om klageadgang " }) }
            paragraph {
                text(bokmal {
                    +"Du kan klage på vedtaket. "
                    +"Fristen for å klage er seks uker fra du mottar dette brevet. Klagen skal sendes til:"
                })
            }
            paragraph {
                text(bokmal {
                    +"Nav familie- og pensjonsytelser"
                })
                newline()
                text(bokmal {
                    +"Postboks 6600 Etterstad, 0607 OSLO"
                })
            }

            includePhrase(Felles.RettTilInnsynRedigerbarebrev)
            includePhrase(Felles.HarDuSpoersmaal.alder)
        }
    }
}

