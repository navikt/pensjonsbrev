package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerAvslutning
import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerInnhold
import no.nav.pensjon.brev.alder.maler.vedlegg.vedleggFolketrygden
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingAutoDto
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingAutoDto.Periode
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingAutoDtoSelectors.avvik
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingAutoDtoSelectors.formyebetalt
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingAutoDtoSelectors.fradragberegnetai
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingAutoDtoSelectors.fullafp
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingAutoDtoSelectors.ieo
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingAutoDtoSelectors.ifu
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingAutoDtoSelectors.iiap
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingAutoDtoSelectors.korrigertafp
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingAutoDtoSelectors.oppgjoersAar
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingAutoDtoSelectors.periode
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingAutoDtoSelectors.pgi
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingAutoDtoSelectors.tpiberegnet
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingAutoDtoSelectors.utbetaltafp
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

/**
 * Vedtak — AFP etteroppgjør med tilbakekreving (autobrev).
 *
 * Konvertert fra Exstream-malen `PE_AF_04_107`. Brevet sendes etter et
 * AFP-etteroppgjør (offentlig sektor / Statens pensjonskasse) når bruker
 * ikke har lagt fram nye dokumenterte opplysninger og avviket mellom
 * forventet og faktisk pensjonsgivende inntekt overstiger toleransebeløpet,
 * slik at det blir tilbakekreving av for mye utbetalt AFP. Forklaringen til
 * brukeren har fire periodevarianter avhengig av når AFP er tatt ut / opphørt
 * — se [VedtakAfpEtteroppgjoerTilbakekrevingAutoDto.Periode].
 *
 * Konverterte avvik fra kilden (Step 7 i convert-exstream-letter-skill):
 *  - De fire `showIf`-blokkene for periodevarianter ble i originalen uttrykt
 *    som overlappende rådata-booleans over uttaksdato/opphorsdato/IFU/IEO.
 *    Logikken er løftet ut av malen til [Periode]-diskriminatoren.
 *  - Den hardkodede teksten "som i 2024 var 15 000 kroner" er erstattet med
 *    "i {oppgjørsår} var 15 000 kroner" (samme tilpasning som i
 *    [VedtakAfpEtteroppgjoerEoFase2Auto] / `_ToleransebeloepAuto`).
 *  - "Vennlig hilsen" + avsenderenhet er fjernet — brevbaker-rammeverket setter
 *    signaturen selv via fellesAuto.
 *  - «Ny pensjonsberegning» og «AFP som er betalt ut for mye» er i originalen
 *    formatert som ligninger (A − B = C). En ekte `table` med tom header og
 *    bare to kolonner ble for smal og lite leselig; gjengis derfor som tre
 *    linjer i én paragraf (newline mellom linjene).
 *  - Avslutningen (Dine plikter / klage / innsyn / spørsmål) gjenbrukes som
 *    sub-fraser fra [AfpEtteroppgjoerAvslutning]. Mindre ordlydsavvik fra
 *    Exstream-originalen ("6 uker" → "seks uker", manglende komma etter
 *    "nav.no" i bokmål under «Har du spørsmål?») harmoniseres mot
 *    fellesfrasen. Klagevedlegget [vedleggFolketrygden] legges ved brevet
 *    (samme som AFP-vedtaksbrevene for offentlig sektor), og den ekstra
 *    paragrafen «I vedlegget får du vite mer om hvordan du går fram.» fra
 *    originalen beholdes rett etter klage-frasen og refererer da til
 *    klagevedlegget.
 *  - Hjemmelshenvisningen (lov om AFP for SPK § 3 d) avviker i nynorsk med
 *    ett komma fra fellesfrasen [AfpEtteroppgjoerInnhold.VedtaksgrunnlagAfpSpk]
 *    og er derfor inlinet med kommentar.
 */
@TemplateModelHelpers
object VedtakAfpEtteroppgjoerTilbakekrevingAuto : AutobrevTemplate<VedtakAfpEtteroppgjoerTilbakekrevingAutoDto> {

    override val kode = Aldersbrevkoder.AutoBrev.PE_AFP_ETTEROPPGJOER_TILBAKEKREVING_AUTO

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - AFP etteroppgjør med tilbakekreving",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                bokmal { +"Avtalefestet pensjon (AFP) - vedtak i etteroppgjør for " + oppgjoersAar.format() },
                nynorsk { +"Avtalefesta pensjon (AFP) - vedtak i etteroppgjer for " + oppgjoersAar.format() },
            )
        }

        outline {
            paragraph {
                text(
                    bokmal {
                        +"Vi viser til vårt forhåndsvarsel om etteroppgjør av avtalefestet pensjon (AFP) for " +
                            oppgjoersAar.format() + ". Resultatet av etteroppgjøret viser at du har fått " +
                            formyebetalt.format() + " for mye utbetalt. Dette er hele det feilutbetalte " +
                            "beløpet. I beløpet som kreves tilbake vil innbetalt skatt bli trukket fra."
                    },
                    nynorsk {
                        +"Vi viser til førehandsvarselet vårt om etteroppgjer av avtalefesta pensjon (AFP) for " +
                            oppgjoersAar.format() + ". Resultatet av etteroppgjeret viser at du har fått " +
                            formyebetalt.format() + " for mykje utbetalt. Dette er heile det feilutbetalte " +
                            "beløpet. I beløpet som Nav krev tilbake vil innbetalt skatt bli trekt frå."
                    },
                )
            }

            // Inlinet hjemmelshenvisning. Bokmål er identisk med
            // [AfpEtteroppgjoerInnhold.VedtaksgrunnlagAfpSpk], men nynorsk i
            // originalen har et ekstra komma før "og tilhøyrande forskrift"
            // som fellesfrasen normaliserer bort. Holdes inlinet med samme
            // ordlyd som Exstream-kilden.
            paragraph {
                text(
                    bokmal {
                        +"Vedtaket er gjort etter lov om AFP for medlemmer av Statens pensjonskasse § 3 " +
                            "bokstav d, og tilhørende forskrift om kombinasjon av avtalefestet pensjon for " +
                            "medlemmer av Statens pensjonskasse og arbeidsinntekt (pensjonsgivende inntekt)."
                    },
                    nynorsk {
                        +"Vedtaket er gjort etter lov om AFP for medlemmer av Statens pensjonskasse § 3 " +
                            "bokstav d, og tilhøyrande forskrift om kombinasjon av avtalefesta pensjon for " +
                            "medlemmer av Statens pensjonskasse og arbeidsinntekt (pensjonsgivende inntekt)."
                    },
                )
            }

            includePhrase(AfpEtteroppgjoerInnhold.InntektenDinIAarTittel(oppgjoersAar))

            showIf(periode.equalTo(Periode.HEL_AFP_HELE_AARET)) {
                paragraph {
                    text(
                        bokmal {
                            +"Du har ikke lagt fram nye dokumenterte opplysninger som viser at din " +
                                "pensjonsgivende inntekt på " + pgi.format() + " helt eller delvis " +
                                "stammer fra tidligere arbeid før uttak av AFP, eller fra frivillig eller " +
                                "beordret tjeneste i helsevesenet, skole eller barnehage i forbindelse med " +
                                "covid-19-pandemien. Du har heller ikke lagt fram nye opplysninger om " +
                                "inntekt gitt som pensjonistavlønning i forbindelse med arbeid med " +
                                "fordrevne fra Ukraina. I samsvar med den tidligere beregningen har vi " +
                                "derfor lagt til grunn at hele denne inntekten skal gi avkorting av AFP."
                        },
                        nynorsk {
                            +"Du har ikkje lagt fram nye dokumenterte opplysningar som viser at den " +
                                "pensjonsgivande inntekta di på " + pgi.format() + " heilt eller delvis " +
                                "stammar frå tidlegare arbeid før uttak av AFP, eller frå frivillig eller " +
                                "beordra teneste i helsesektoren, skule eller barnehage i samband med " +
                                "covid-19-pandemien. Du har heller ikkje lagt fram nye opplysningar om " +
                                "inntekt frå arbeid med fordrivne frå Ukraina. I samsvar med den tidlegare " +
                                "berekninga har vi derfor lagt til grunn at denne inntekta skal gi " +
                                "avkorting av AFP."
                        },
                    )
                }
            }

            showIf(periode.equalTo(Periode.UTTAK_I_AARET)) {
                paragraph {
                    text(
                        bokmal {
                            +"Du har ikke lagt fram nye dokumenterte opplysninger om arbeidsinntekten din " +
                                "før du tok ut AFP. Du har heller ikke lagt frem opplysninger om eventuell " +
                                "inntekt fra frivillig eller beordret tjeneste i helsevesenet, skole eller " +
                                "barnehage i forbindelse med covid-19-pandemien. Du har heller ikke lagt " +
                                "fram nye opplysninger om inntekt gitt som pensjonistavlønning i " +
                                "forbindelse med arbeid med fordrevne fra Ukraina. Arbeidsinntekten som " +
                                "skal gi avkorting av AFP er derfor satt til " + ifu.format() + " i samsvar " +
                                "med den tidligere beregningen. Dette beløpet skal holdes utenfor " +
                                "etteroppgjøret for " + oppgjoersAar.format() + "."
                        },
                        nynorsk {
                            +"Du har ikkje lagt fram nye dokumenterte opplysningar om arbeidsinntekta di " +
                                "før du tok ut AFP. Du har heller ikkje lagt fram opplysningar om " +
                                "eventuell inntekt frå frivillig eller beordra teneste i helsesektoren, " +
                                "skule eller barnehage i samband med covid-19-pandemien. Du har heller " +
                                "ikkje lagt fram nye opplysningar om inntekt frå arbeid med fordrivne frå " +
                                "Ukraina. Arbeidsinntekta som skal gi avkorting av AFP er derfor sett til " +
                                ifu.format() + " i samsvar med den tidlegare berekninga. Dette beløpet " +
                                "skal haldast utanfor etteroppgjeret for " + oppgjoersAar.format() + "."
                        },
                    )
                }
                paragraph {
                    text(
                        bokmal {
                            +"Den faktiske arbeidsinntekten din i den perioden du har mottatt AFP er satt " +
                                "til " + iiap.format() + ". Dette beløpet utgjør differansen mellom din " +
                                "pensjonsgivende inntekt for " + oppgjoersAar.format() + " på " +
                                pgi.format() + " og arbeidsinntekten din før uttak av AFP på " +
                                ifu.format() + "."
                        },
                        nynorsk {
                            +"Den faktiske arbeidsinntekta di i den perioden du har fått AFP er sett til " +
                                iiap.format() + ". Dette beløpet utgjer differansen mellom den " +
                                "pensjonsgivande inntekta di for " + oppgjoersAar.format() + " på " +
                                pgi.format() + " og arbeidsinntekta di før uttak av AFP på " +
                                ifu.format() + "."
                        },
                    )
                }
            }

            showIf(periode.equalTo(Periode.UTTAK_OG_OPPHOER_I_AARET)) {
                paragraph {
                    text(
                        bokmal {
                            +"Du har ikke lagt fram nye dokumenterte opplysninger om arbeidsinntekten din " +
                                "før du tok ut AFP eller om arbeidsinntekten din etter opphør av AFP. Du " +
                                "har heller ikke lagt frem opplysninger om eventuell inntekt fra frivillig " +
                                "eller beordret tjeneste i helsevesenet, skole eller barnehage i " +
                                "forbindelse med covid-19-pandemien. Du har heller ikke lagt fram nye " +
                                "opplysninger om inntekt gitt som pensjonistavlønning i forbindelse med " +
                                "arbeid med fordrevne fra Ukraina. Arbeidsinntekten din for perioden før " +
                                "uttak av AFP er derfor satt til " + ifu.format() + ". Arbeidsinntekten " +
                                "din for perioden etter opphør av AFP er satt til " + ieo.format() + ". " +
                                "Disse beløpene skal holdes utenfor etteroppgjøret for " +
                                oppgjoersAar.format() + " i samsvar med den tidligere beregningen."
                        },
                        nynorsk {
                            +"Du har ikkje lagt fram nye dokumenterte opplysningar om arbeidsinntekta di " +
                                "før du tok ut AFP eller om arbeidsinntekta di etter at AFP tok slutt. Du " +
                                "har heller ikkje lagt fram opplysningar om eventuell inntekt frå " +
                                "frivillig eller beordra teneste i helsesektoren, skule eller barnehage i " +
                                "samband med covid-19-pandemien. Du har heller ikkje lagt fram nye " +
                                "opplysningar om inntekt frå arbeid med fordrivne frå Ukraina. " +
                                "Arbeidsinntekta di for perioden før uttak av AFP er sett til " +
                                ifu.format() + ". Arbeidsinntekta di for perioden etter opphør av AFP er " +
                                "sett til " + ieo.format() + ". Desse beløpa skal haldast utanfor " +
                                "etteroppgjeret for " + oppgjoersAar.format() + " i samsvar med den " +
                                "tidlegare berekninga."
                        },
                    )
                }
                paragraph {
                    text(
                        bokmal {
                            +"Den faktiske arbeidsinntekten i den perioden du har mottatt AFP, er " +
                                iiap.format() + ". Dette beløpet utgjør differansen mellom din " +
                                "pensjonsgivende inntekt for " + oppgjoersAar.format() + " på " +
                                pgi.format() + " og summen av arbeidsinntektene før uttak av AFP på " +
                                ifu.format() + " og etter opphør av AFP på " + ieo.format() + "."
                        },
                        nynorsk {
                            +"Den faktiske arbeidsinntekta i den perioden du har fått AFP, er " +
                                iiap.format() + ". Dette beløpet utgjer differansen mellom den " +
                                "pensjonsgivande inntekta di for " + oppgjoersAar.format() + " på " +
                                pgi.format() + " og summen av arbeidsinntektene før uttak av AFP på " +
                                ifu.format() + " og etter at AFP tok slutt på " + ieo.format() + "."
                        },
                    )
                }
            }

            showIf(periode.equalTo(Periode.OPPHOER_I_AARET)) {
                paragraph {
                    text(
                        bokmal {
                            +"Du har ikke lagt fram nye opplysninger om inntektsforholdene dine. Du har " +
                                "heller ikke lagt frem opplysninger om eventuell inntekt fra frivillig " +
                                "eller beordret tjeneste i helsevesenet, skole eller barnehage i " +
                                "forbindelse med covid-19-pandemien. Du har heller ikke lagt fram nye " +
                                "opplysninger om inntekt gitt som pensjonistavlønning i forbindelse med " +
                                "arbeid med fordrevne fra Ukraina. Arbeidsinntekten din etter opphør av " +
                                "AFP, er derfor satt til " + ieo.format() + " i samsvar med den tidligere " +
                                "beregningen. Dette beløpet skal holdes utenfor etteroppgjøret for " +
                                oppgjoersAar.format() + "."
                        },
                        nynorsk {
                            +"Du har ikkje lagt fram nye opplysningar om inntektsforholda dine. Du har " +
                                "heller ikkje lagt fram opplysningar om eventuell inntekt frå frivillig " +
                                "eller beordra teneste i helsesektoren, skule eller barnehage i samband " +
                                "med covid-19-pandemien. Du har heller ikkje lagt fram nye opplysningar " +
                                "om inntekt frå arbeid med fordrivne frå Ukraina. Arbeidsinntekta di " +
                                "etter at AFP tok slutt, er derfor sett til " + ieo.format() + " i " +
                                "samsvar med den tidlegare berekninga. Dette beløpet skal haldast utanfor " +
                                "etteroppgjeret for " + oppgjoersAar.format() + "."
                        },
                    )
                }
                paragraph {
                    text(
                        bokmal {
                            +"Den faktiske arbeidsinntekten i den perioden du har mottatt AFP er satt til " +
                                iiap.format() + ". Dette beløpet utgjør differansen mellom din " +
                                "pensjonsgivende inntekt for " + oppgjoersAar.format() + " på " +
                                pgi.format() + " og arbeidsinntekten din etter opphør av AFP på " +
                                ieo.format() + "."
                        },
                        nynorsk {
                            +"Den faktiske arbeidsinntekta i den perioden du har fått AFP er sett til " +
                                iiap.format() + ". Dette beløpet utgjer differansen mellom den " +
                                "pensjonsgivande inntekta di for " + oppgjoersAar.format() + " på " +
                                pgi.format() + " og arbeidsinntekta di etter at AFP tok slutt på " +
                                ieo.format() + "."
                        },
                    )
                }
            }

            paragraph {
                text(
                    bokmal {
                        +"Den arbeidsinntekten du har hatt i perioden med AFP er " + avvik.format() +
                            " høyere enn den forventede arbeidsinntekten som ble lagt til grunn ved " +
                            "utbetalingen av pensjonen din i det aktuelle tidsrommet. Fordi dette er mer " +
                            "enn toleransebeløpet som i " + oppgjoersAar.format() + " var 15 000 kroner, " +
                            "er pensjonen din beregnet på ny og avregnet mot den pensjonen du allerede " +
                            "har fått utbetalt i perioden."
                    },
                    nynorsk {
                        +"Den arbeidsinntekta du har hatt i perioden med AFP, er " + avvik.format() +
                            " høgare enn den forventa arbeidsinntekta som blei lagd til grunn ved " +
                            "utbetalinga av pensjonen din i det aktuelle tidsrommet. Fordi dette er meir " +
                            "enn toleransebeløpet som i " + oppgjoersAar.format() + " var 15 000 kroner, " +
                            "er pensjonen din berekna på nytt og avrekna mot den pensjonen du allereie " +
                            "har fått utbetalt i perioden."
                    },
                )
            }

            // «Ny pensjonsberegning» — i originalen formatert som en ligning
            // (A − B = C). Med kun to kolonner (etikett + beløp) ble en ekte
            // table med tom header for smal og lite leselig; gjengis derfor
            // som tre linjer i én paragraf (newline mellom linjene).
            title1 {
                text(
                    bokmal { +"Ny pensjonsberegning" },
                    nynorsk { +"Ny pensjonsberekning" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Full AFP (uten fradrag for inntekt): " + fullafp.format() },
                    nynorsk { +"Full AFP (utan frådrag for inntekt): " + fullafp.format() },
                )
                newline()
                text(
                    bokmal { +"− Nytt beregnet inntektsfradrag: " + fradragberegnetai.format() },
                    nynorsk { +"− Nytt berekna inntektsfrådrag: " + fradragberegnetai.format() },
                )
                newline()
                text(
                    bokmal { +"= AFP etter fradrag for den nye inntekten: " + korrigertafp.format() },
                    nynorsk { +"= AFP etter frådrag for den nye inntekta: " + korrigertafp.format() },
                )
            }

            paragraph {
                text(
                    bokmal {
                        +"Inntektsfradraget i AFP for den nye inntekten på " + fradragberegnetai.format() +
                            " beregnes slik: " + iiap.format() + " (ny beregnet inntekt) / " +
                            tpiberegnet.format() + " (tidligere arbeidsinntekt*) x " + fullafp.format() +
                            " (full AFP). *Tidligere arbeidsinntekt er beregnet ut fra inntekten din i " +
                            "årene før du tok ut AFP."
                    },
                    nynorsk {
                        +"Inntektsfrådraget i AFP for den nye inntekta på " + fradragberegnetai.format() +
                            " blir berekna slik: " + iiap.format() + " (ny berekna inntekt) / " +
                            tpiberegnet.format() + " (tidlegare arbeidsinntekt*) x " + fullafp.format() +
                            " (full AFP). *Tidlegare arbeidsinntekt er berekna ut frå inntekta di i åra " +
                            "før du tok ut AFP."
                    },
                )
            }

            // «AFP som er betalt ut for mye» — samme A − B = C-form som
            // «Ny pensjonsberegning»; gjengis som tre linjer i én paragraf.
            title1 {
                text(
                    bokmal { +"AFP som er betalt ut for mye" },
                    nynorsk { +"AFP som er betalt ut for mykje" },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Dette beløpet er differansen mellom tidligere utbetalt AFP og AFP etter fradrag " +
                            "for den nye inntekten."
                    },
                    nynorsk {
                        +"Dette beløpet er differansen mellom tidlegare utbetalt AFP og AFP etter frådrag " +
                            "for den nye inntekta."
                    },
                )
            }
            paragraph {
                text(
                    bokmal { +"Tidligere utbetalt AFP: " + utbetaltafp.format() },
                    nynorsk { +"Tidlegare utbetalt AFP: " + utbetaltafp.format() },
                )
                newline()
                text(
                    bokmal { +"− AFP fratrukket nytt beregnet inntektsfradrag: " + korrigertafp.format() },
                    nynorsk { +"− AFP fråtrekt nytt berekna inntektsfrådrag: " + korrigertafp.format() },
                )
                newline()
                text(
                    bokmal { +"= For mye utbetalt AFP: " + formyebetalt.format() },
                    nynorsk { +"= For mykje utbetalt AFP: " + formyebetalt.format() },
                )
            }

            title1 {
                text(
                    bokmal { +"Tilbakebetaling" },
                    nynorsk { +"Tilbakebetaling" },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Nav sender kravet til Skatteetaten for videre oppfølging. Dette skjer først når " +
                            "klagefristen på 6 uker er gått ut og vanligvis innen 12 uker."
                    },
                    nynorsk {
                        +"Nav sender kravet til Skatteetaten for vidare oppfølging. Dette skjer først når " +
                            "klagefristen på 6 veker er gått ut og vanlegvis innan 12 veker."
                    },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Du vil motta et eget brev med betalingsinformasjon. Beløpet skal som hovedregel " +
                            "betales tilbake i løpet av 12 kalendermåneder, og vil trekkes i den månedlige " +
                            "utbetalingen din av AFP eller alderspensjon fra folketrygden."
                    },
                    nynorsk {
                        +"Du vil få eit eige brev frå Skatteetaten med betalingsinformasjon. Beløpet skal " +
                            "som hovudregel betalast tilbake i løpet av 12 kalendermånadar, og vil " +
                            "trekkast i den månadlege utbetalinga di av AFP eller alderspensjon frå " +
                            "folketrygda."
                    },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Etter du har mottatt brevet fra Skatteetaten kan du ta kontakt med dem for å " +
                            "endre trekkbeløpet."
                    },
                    nynorsk {
                        +"Etter du har fått brevet frå Skatteetaten kan du ta kontakt med dei for å endre " +
                            "trekkbeløpet."
                    },
                )
            }

            title1 {
                text(
                    bokmal { +"Skatteoppgjør" },
                    nynorsk { +"Skatteoppgjer" },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Nav rapporterer endringen til Skatteetaten. De vil foreta en korrigering av " +
                            "skatteoppgjøret ditt for " + oppgjoersAar.format() + " basert på denne " +
                            "endringen av utbetalt AFP."
                    },
                    nynorsk {
                        +"Nav rapporterer endringa til Skatteetaten. Dei vil gjere ei korrigering av " +
                            "skatteoppgjeret ditt for " + oppgjoersAar.format() + " basert på denne " +
                            "endringa."
                    },
                )
            }

            // Avslutning — Dine plikter / klage / innsyn / Har du spørsmål.
            // Vi komponerer fra sub-frasene i AfpEtteroppgjoerAvslutning der
            // ordlyden er identisk med PE_AF_04_102/106, og inliner klage- og
            // spørsmålsseksjonen som har små avvik (jf. docstring øverst).
            includePhrase(AfpEtteroppgjoerAvslutning.DinePlikter)

            // Klage- og spørsmålsseksjonene gjenbruker fellesfrasene fra
            // [AfpEtteroppgjoerAvslutning]. Mindre ordlydsavvik fra
            // Exstream-originalen ("6 uker" → "seks uker" i bokmål/nynorsk,
            // og manglende komma etter "nav.no" i bokmål under "Har du
            // spørsmål?") harmoniseres mot fellesfrasen.
            includePhrase(AfpEtteroppgjoerAvslutning.DuHarRettTilAaKlageSeksUker)
            paragraph {
                text(
                    bokmal { +"I vedlegget får du vite mer om hvordan du går fram." },
                    nynorsk { +"I vedlegget får du vite meir om korleis du går fram." },
                )
            }

            includePhrase(AfpEtteroppgjoerAvslutning.DuHarRettTilInnsyn)
            includePhrase(AfpEtteroppgjoerAvslutning.HarDuSporsmal)
        }
        includeAttachment(vedleggFolketrygden)
    }
}
