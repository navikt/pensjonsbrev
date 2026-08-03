package no.nav.pensjon.brev.alder.maler.afp.fraser

import no.nav.pensjon.brev.alder.maler.felles.Constants.NAV_URL
import no.nav.pensjon.brev.alder.maler.felles.Constants.SKATTEETATEN_URL
import no.nav.pensjon.brev.alder.maler.felles.Constants.SKATT_KONTAKT_TLF
import no.nav.pensjon.brev.alder.maler.felles.KronerText
import no.nav.pensjon.brev.alder.model.afp.AfpOffentligSektor
import no.nav.pensjon.brev.alder.model.afp.selectors.afpOffentligSektor.beregning.*
import no.nav.pensjon.brev.alder.model.afp.selectors.afpOffentligSektor.ektefelletillegg.brutto as ektefelletilleggBrutto
import no.nav.pensjon.brev.alder.model.afp.selectors.afpOffentligSektor.ektefelletillegg.netto as ektefelletilleggNetto
import no.nav.pensjon.brev.alder.model.afp.selectors.afpOffentligSektor.ytelsesKomponent.brutto as ytelsesKomponentBrutto
import no.nav.pensjon.brev.alder.model.afp.selectors.afpOffentligSektor.ytelsesKomponent.netto as ytelsesKomponentNetto
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.TableScope
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.lessThanOrEqual
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.expression.notNull
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

/**
 * Fellesinnhold som deles mellom AFP offentlig sektor-brevene
 *   * `PE_AF_04_001` — [no.nav.pensjon.brev.alder.maler.afp.InnvilgelseAvAfpOffentligSektor]
 *   * `PE_AF_04_020` — [no.nav.pensjon.brev.alder.maler.afp.VedtakEndringAfpOffentligSektor]
 *
 * Hver phrase tilsvarer en byte-identisk seksjon i de to brevene; brev-spesifikke
 * fritekst-paragrafer og lov-/begrunnelsestekster er holdt utenfor og lever
 * fortsatt i selve malfilen.
 */

object AfpOffentligSektorInnhold {

    /**
     * Standard utbetalingsdato-paragraf brukt i AFP-vedtaksbrev.
     * Forteller om utbetaling den 20. hver måned og henviser til oversikt på nav.no.
     */
    object UtbetalingsdagerOpplysning : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +"Pensjonen din blir vanligvis utbetalt den 20. hver måned. Når den 20. er en lørdag eller offentlig fridag " +
                            "blir pensjonen utbetalt senest siste virkedag før den 20. Oversikt over utbetalingsdatoer finner du på " +
                            NAV_URL + "."
                    },
                    nynorsk {
                        +"Pensjonen din blir vanlegvis utbetalt den 20. kvar månad. Når den 20. er ein laurdag eller offentleg fridag, " +
                            "blir pensjonen utbetalt seinast siste yrkedagen før den 20. Oversikt over utbetalingsdatoar finn du på " +
                            NAV_URL + "."
                    },
                )
            }
        }
    }

    /**
     * Standard skattekort-informasjon brukt i pensjonsvedtaksbrev. Forteller at
     * skattereglene for pensjon avviker fra arbeidsinntekt og henviser til
     * Skatteetaten.
     */
    object SkattekortPensjonOpplysning : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +"Skattereglene for pensjonsinntekt er ikke de samme som for arbeidsinntekt. Derfor bør du vurdere å søke om " +
                            "nytt skattekort når du starter uttak av pensjon. Endring av skattekort gjøres enklest på Skatteetatens " +
                            "nettsider $SKATTEETATEN_URL. Har du spørsmål kan du ringe Skatteetaten på telefon $SKATT_KONTAKT_TLF. Du trenger " +
                            "ikke levere skattekortet til Nav fordi skatteopplysningene dine sendes elektronisk fra Skatteetaten."
                    },
                    nynorsk {
                        +"Skattereglane for pensjonsinntekt er ikkje dei same som for arbeidsinntekt. Derfor bør du vurdere å søkje om " +
                            "nytt skattekort når du begynner å ta ut pensjon. Skattekortendringar gjer du enklast på nettsidene til " +
                            "Skatteetaten, $SKATTEETATEN_URL. Har du spørsmål, kan du ringje Skatteetaten på telefon $SKATT_KONTAKT_TLF. Du treng " +
                            "ikkje levere skattekortet til Nav ettersom skatteopplysningane dine blir sende elektronisk frå Skatteetaten."
                    },
                )
            }
        }
    }

    /**
     * Seksjonen «Hvordan AFP beregnes i forhold til tidligere og framtidig inntekt».
     * Inneholder title1 + forklarende paragrafer om beregningsgrunnlaget og to varianter
     * (toleransebeløp/avkortning) avhengig av antatt framtidig inntekt.
     */
    data class HvordanAfpBeregnes(
        val tidligereArbeidsinntekt: Expression<Kroner>,
        val framtidigArligInntekt: Expression<Kroner>,
        val toleranseBeloep: Expression<Kroner>,
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            title1 {
                text(
                    bokmal { +"Hvordan AFP beregnes i forhold til tidligere og framtidig inntekt" },
                    nynorsk { +"Korleis AFP blir berekna i forhold til tidlegare og framtidig inntekt" },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"AFP blir beregnet med utgangspunkt i tidligere og framtidig inntekt. AFP skal utgjøre en like stor prosent " +
                            "av full pensjon som den reduksjonen du får i framtidig arbeidsinntekt. Hvis framtidig arbeidsinntekt " +
                            "utgjør 65 prosent av den beregnede tidligere inntekten, så skal pensjonen tilsvare 35 prosent av full AFP."
                    },
                    nynorsk {
                        +"AFP blir berekna med utgangspunkt i tidlegare og framtidig inntekt. AFP skal utgjere ein like stor prosent " +
                            "av full pensjon som den reduksjonen du får i framtidig arbeidsinntekt. Dersom framtidig arbeidsinntekt " +
                            "utgjer 65 prosent av den berekna tidlegare inntekta, skal pensjonen tilsvare 35 prosent av full AFP."
                    },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Din tidligere arbeidsinntekt er fastsatt til " + tidligereArbeidsinntekt.format() +
                            ". Den tidligere arbeidsinntekten er et beregnet beløp, basert på de tre beste inntektsårene dine " +
                            "av de fem siste årene, forut for året før du går ut i pensjon. Denne beregnede inntekten blir justert i " +
                            "forhold til økninger i folketrygdens grunnbeløp (G)."
                    },
                    nynorsk {
                        +"Den tidlegare arbeidsinntekta di er fastsett til " + tidligereArbeidsinntekt.format() +
                            ". Den tidlegare arbeidsinntekta er eit berekna beløp basert på dei tre beste inntektsåra dine av " +
                            "dei fem siste åra før året før du går ut i pensjon. Denne berekna inntekta blir justert i forhold til " +
                            "auke i grunnbeløpet (G) i folketrygda."
                    },
                )
            }
            showIf(framtidigArligInntekt.lessThanOrEqual(15_000)) {
                paragraph {
                    text(
                        bokmal {
                            +"Du har oppgitt at din antatte framtidige arbeidsinntekt ikke vil overstige toleransebeløpet på " + toleranseBeloep.format() +
                                " per år. AFP er derfor beregnet uten fradrag for arbeidsinntekt. I den framtidige arbeidsinntekten " +
                                "skal både lønnsinntekt, feriepenger, fordel av forsikringspremier betalt av arbeidsgiver, personinntekt " +
                                "fra næring, honorar og lignende regnes med."
                        },
                        nynorsk {
                            +"Du har oppgitt at den forventa framtidige arbeidsinntekta di ikkje kjem til å overstige toleransebeløpet " +
                                "på " + toleranseBeloep.format() + " per år. AFP er derfor berekna utan frådrag for arbeidsinntekt. I den framtidige " +
                                "arbeidsinntekta skal både lønnsinntekt, feriepengar, fordel av forsikringspremiar betalte av " +
                                "arbeidsgivar, personinntekt frå næring, honorar og liknande reknast med."
                        },
                    )
                }
            }
            showIf(framtidigArligInntekt.greaterThan(15_000)) {
                paragraph {
                    text(
                        bokmal {
                            +"Pensjonen din er redusert etter antatt årlig framtidig arbeidsinntekt på " +
                                framtidigArligInntekt.format() + "."
                        },
                        nynorsk {
                            +"Pensjonen din er redusert etter ei forventa årleg framtidig arbeidsinntekt på " +
                                framtidigArligInntekt.format() + "."
                        },
                    )
                }
            }
        }
    }

    /**
     * Generell forklaring av vilkårene for ektefelletillegg i AFP — hvem det gis til,
     * hvilke inntekts-/pensjonsforhold som påvirker retten, og når tillegget faller bort.
     *
     * Kaller wraps i `showIf(pesysData.beregning.ektefelletillegg.notNull()) { includePhrase(...) }`.
     */
    object EktefelletilleggOpplysning : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +"Ektefelletillegget gis som et tillegg i AFP til den som forsørger ektefelle, partner eller " +
                            "samboer over 60 år. I følge folketrygdloven paragraf 3-24, kan du få ektefelletillegg så lenge" +
                            "den du forsørger ikke har egen pensjon eller uføretrygd, og ikke har egen inntekt som overstiger " +
                            "folketrygdens grunnbeløp. Som egen inntekt inngår også kapitalinntekt. Ektefelletillegget vil falle " +
                            "bort når den som blir forsørget får rett til egen hel alderspensjon. Dette gjelder selv om pensjonen " +
                            "ikke blir tatt ut."
                    },
                    nynorsk {
                        +"Ektefelletillegg blir gitt som eit tillegg i AFP til den som forsørgjer ektefelle, partnar eller " +
                            "sambuar over 60 år. Ifølgje folketrygdlova paragraf 3-24 kan du få ektefelletillegg dersom " +
                            "den du forsørgjer, ikkje har eigen pensjon eller eiga inntekt, inkludert kapitalinntekt, som " +
                            "overstig grunnbeløpet i folketrygda. Ektefelletillegget vil falle bort når den som blir " +
                            "forsørgt får rett til eigen heil alderspensjon, sjølv om pensjonen ikkje blir tatt ut."
                    },
                )
            }
        }
    }

    /**
     * Seksjonen «Ektefelletillegg og inntekt». Forklarer at AFP og ektefelletillegg
     * inntektsprøves forskjellig og refererer til den inntekten som er brukt i avkortninga.
     *
     * Kaller wraps i `ifNotNull(pesysData.beregning.ektefelletillegg) { et -> includePhrase(...) }`.
     */
    data class EktefelletilleggOgInntekt(
        val inntektBruktIAvkortning: Expression<Kroner>,
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            title1 {
                text(
                    bokmal { +"Ektefelletillegg og inntekt" },
                    nynorsk { +"Ektefelletillegg og inntekt" },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Det er viktig at du er oppmerksom på at AFP og ektefelletillegg inntektsprøves forskjellig."
                    },
                    nynorsk {
                        +"Det er viktig at du er merksam på at AFP og ektefelletillegg blir inntektsprøvde på forskjellige måtar."
                    },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Ektefelletillegget inntektsprøves mot din samlede inntekt på " +
                            inntektBruktIAvkortning.format() +
                            ". I denne inntekten tar vi hensyn til pensjonen din, antatt framtidig arbeidsinntekt, og " +
                            "pensjon/ytelser fra andre. Dette innebærer at alle inntektsendringer kan få betydning for størrelsen " +
                            "på ektefelletillegget."
                    },
                    nynorsk {
                        +"Ektefelletillegget blir inntektsprøvd mot den samla inntekta di på " +
                            inntektBruktIAvkortning.format() +
                            ". I denne inntekta tek vi omsyn til pensjonen din, forventa framtidig arbeidsinntekt og " +
                            "pensjon/ytingar frå andre. Det inneber at alle inntektsendringar kan vere avgjerande for storleiken " +
                            "på ektefelletillegget."
                    },
                )
            }
        }
    }

    /**
     * Hele «Dine plikter»-seksjonen: meldeplikt, sivilstand-spesifikke endringer, og
     * varsel om at urettmessig utbetaling kan kreves tilbake.
     *
     * Listen «Du må også melde fra hvis» varierer med sivilstand (samme grupperinger
     * som i Exstream-malen), og den første «det vil si når»-paragrafen avhenger av
     * om ektefelletillegg er innvilget.
     */
    data class DinePlikterAfpOffentlig(
        val sivilstand: Expression<AfpOffentligSektor.Sivilstand>,
        val harEktefelletillegg: Expression<Boolean>,
        val toleranseBeloep: Expression<Kroner>,
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            title1 {
                text(
                    bokmal { +"Dine plikter" },
                    nynorsk { +"Dine plikter" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Du har plikt til å melde fra til Nav om endringer som har betydning for størrelsen på pensjonen din." },
                    nynorsk { +"Du pliktar å melde frå til Nav om endringar som er avgjerande for storleiken på pensjonen din." },
                )
            }
            showIf(harEktefelletillegg.not()) {
                paragraph {
                    text(
                        bokmal {
                            +"Det vil si når arbeidsinntekten din endrer seg med mer enn " + toleranseBeloep.format() + " per år i forhold til den " +
                                "inntekten som pensjonen er beregnet etter."
                        },
                        nynorsk {
                            +"Det vil seie når arbeidsinntekta di endrar seg med meir enn " + toleranseBeloep.format() + " per år i forhold til den " +
                                "inntekta som pensjonen er berekna etter."
                        },
                    )
                }
            }
            showIf(harEktefelletillegg) {
                paragraph {
                    text(
                        bokmal { +"Det vil si når arbeidsinntekten din endrer seg, eller når du får endringer i" },
                        nynorsk { +"Det vil seie når arbeidsinntekta di endrar seg eller du får endringar i" },
                    )
                    list {
                        item {
                            text(
                                bokmal { +"tjenestepensjon fra offentlige eller private ordninger" },
                                nynorsk { +"tenestepensjon frå offentlege eller private ordningar" },
                            )
                        }
                        item {
                            text(
                                bokmal { +"individuelle pensjonsordninger, livrente og gavepensjon" },
                                nynorsk { +"individuelle pensjonsordningar, livrente og gåvepensjon" },
                            )
                        }
                        item {
                            text(
                                bokmal { +"andre ytelser fra Nav" },
                                nynorsk { +"andre ytingar frå Nav" },
                            )
                        }
                        item {
                            text(
                                bokmal { +"ytelser og pensjon fra andre land" },
                                nynorsk { +"ytingar og pensjon frå andre land" },
                            )
                        }
                    }
                }
            }

            paragraph {
                text(
                    bokmal { +"Du må også melde fra hvis" },
                    nynorsk { +"Du må òg melde frå dersom" },
                )
                list {
                    showIf(sivilstand.equalTo(AfpOffentligSektor.Sivilstand.ENSLIG)) {
                        item {
                            text(
                                bokmal { +"du gifter deg, inngår partnerskap eller samboerskap" },
                                nynorsk { +"du gifter deg eller inngår partnarskap eller sambuarskap" },
                            )
                        }
                    }
                    showIf(sivilstand.equalTo(AfpOffentligSektor.Sivilstand.BOR_MED_EKTEFELLE)) {
                        item {
                            text(
                                bokmal {
                                    +"ektefellens inntektsforhold endrer seg. Dette gjelder både endringer i arbeidsinntekt, " +
                                        "pensjonsinntekter og kapitalinntekt"
                                },
                                nynorsk {
                                    +"ektefellens inntektsforhold endrar seg (gjeld både endringar i arbeidsinntekt, " +
                                        "pensjonsinntekter og kapitalinntekt)"
                                },
                            )
                        }
                        item {
                            text(
                                bokmal { +"du og ektefellen flytter fra hverandre eller blir skilt" },
                                nynorsk { +"du og ektefellen flytter frå kvarandre eller blir skilde" },
                            )
                        }
                        item {
                            text(
                                bokmal { +"en av dere får et varig opphold i institusjon" },
                                nynorsk { +"ein av dykk får eit varig opphald i institusjon" },
                            )
                        }
                        item {
                            text(
                                bokmal { +"ektefellen din dør" },
                                nynorsk { +"ektefellen din døyr" },
                            )
                        }
                    }
                    showIf(sivilstand.equalTo(AfpOffentligSektor.Sivilstand.SEPARERT_FRA_EKTEFELLE)) {
                        item {
                            text(
                                bokmal { +"du og ektefellen flytter sammen igjen" },
                                nynorsk { +"du og ektefellen flytter saman igjen" },
                            )
                        }
                        item {
                            text(
                                bokmal { +"du blir skilt" },
                                nynorsk { +"du blir skild" },
                            )
                        }
                        item {
                            text(
                                bokmal { +"ektefellen din dør" },
                                nynorsk { +"ektefellen din døyr" },
                            )
                        }
                    }
                    showIf(sivilstand.equalTo(AfpOffentligSektor.Sivilstand.SEPARERT_FRA_PARTNER)) {
                        item {
                            text(
                                bokmal { +"du og partneren flytter sammen igjen" },
                                nynorsk { +"du og partnaren flytter saman igjen" },
                            )
                        }
                        item {
                            text(
                                bokmal { +"du blir skilt" },
                                nynorsk { +"du blir skild" },
                            )
                        }
                        item {
                            text(
                                bokmal { +"partneren din dør" },
                                nynorsk { +"partnaren din døyr" },
                            )
                        }
                    }
                    showIf(sivilstand.equalTo(AfpOffentligSektor.Sivilstand.BOR_MED_PARTNER)) {
                        item {
                            text(
                                bokmal {
                                    +"partnerens inntektsforhold endrer seg. Dette gjelder både endringer i arbeidsinntekt, " +
                                        "pensjonsinntekter og kapitalinntekt"
                                },
                                nynorsk {
                                    +"partnarens inntektsforhold endrar seg (gjeld både endringar i arbeidsinntekt, " +
                                        "pensjonsinntekter og kapitalinntekt)"
                                },
                            )
                        }
                        item {
                            text(
                                bokmal { +"du og partneren flytter fra hverandre eller blir skilt" },
                                nynorsk { +"du og partnaren flytter frå kvarandre eller blir skilde" },
                            )
                        }
                        item {
                            text(
                                bokmal { +"en av dere får et varig opphold i institusjon" },
                                nynorsk { +"ein av dykk får eit varig opphald i institusjon" },
                            )
                        }
                        item {
                            text(
                                bokmal { +"partneren din dør" },
                                nynorsk { +"partnaren din døyr" },
                            )
                        }
                    }
                    showIf(sivilstand.equalTo(AfpOffentligSektor.Sivilstand.BOR_MED_SAMBOER)) {
                        item {
                            text(
                                bokmal {
                                    +"samboerens inntektsforhold endrer seg. Dette gjelder både endringer i arbeidsinntekt, " +
                                        "pensjonsinntekter og kapitalinntekt"
                                },
                                nynorsk {
                                    +"sambuarens inntektsforhold endrar seg (gjeld både endringar i arbeidsinntekt, " +
                                        "pensjonsinntekter og kapitalinntekt)"
                                },
                            )
                        }
                        item {
                            text(
                                bokmal { +"du gifter deg eller inngår partnerskap" },
                                nynorsk { +"du gifter deg eller inngår partnarskap" },
                            )
                        }
                        item {
                            text(
                                bokmal { +"du får barn med samboeren" },
                                nynorsk { +"du får barn med sambuaren din" },
                            )
                        }
                        item {
                            text(
                                bokmal { +"en av dere får et varig opphold i institusjon" },
                                nynorsk { +"ein av dykk får eit varig opphald i institusjon" },
                            )
                        }
                        item {
                            text(
                                bokmal { +"du og samboeren din flytter fra hverandre" },
                                nynorsk { +"du og sambuaren din flytter frå kvarandre" },
                            )
                        }
                        item {
                            text(
                                bokmal { +"samboeren din dør" },
                                nynorsk { +"sambuaren din døyr" },
                            )
                        }
                    }
                    item {
                        text(
                            bokmal { +"du skal flytte innen Norge" },
                            nynorsk { +"du skal flytte innanfor Noreg" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"du skal oppholde deg utenfor Norge i en lengre periode eller skal flytte til et annet land" },
                            nynorsk { +"du skal opphalde deg utanfor Noreg i ein lengre periode eller skal flytte til eit anna land" },
                        )
                    }
                }
            }
            paragraph {
                text(
                    bokmal { +"Hvis du får utbetalt for mye pensjon fordi du ikke har meldt fra om endringer, kan vi kreve tilbake det som er for mye utbetalt." },
                    nynorsk { +"Dersom du får utbetalt for mykje pensjon fordi du ikkje har meldt frå om endringar, kan vi krevje tilbake det som er for mykje utbetalt." },
                )
            }
        }
    }

    /**
     * Innledning til «Dine rettigheter»-seksjonen: title1 + innsynsrett-paragraf.
     *
     * Den avsluttende «Hvis du mener vedtaket…»-klage-paragrafen er ikke inkludert
     * fordi ordlyden varierer mellom innvilgelse og endring; den emitteres av
     * kalleren etter `includePhrase(DineRettigheterInnsyn)`.
     */
    object DineRettigheterInnsyn : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            title1 {
                text(
                    bokmal { +"Dine rettigheter" },
                    nynorsk { +"Dine rettar" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Du har etter forvaltningsloven paragraf 18 som hovedregel rett til å se sakens dokumenter." },
                    nynorsk { +"Du har etter forvaltningslova paragraf 18 som hovudregel rett til å sjå saksdokumenta." },
                )
            }
        }
    }

    /**
     * Beregningstabell for AFP offentlig sektor-brevene. Viser månedlig pensjon
     * fordelt på ytelseskomponenter med to varianter:
     *
     *  * Brutto + Netto-kolonner når `beregning.brutto != beregning.netto` (inntektsavkortning).
     *  * Kun Netto-kolonne når `beregning.brutto == beregning.netto`.
     *
     * Innledningsparagrafen («Den månedlige pensjonen fra …») er inkludert i phrasen.
     */
    data class BeregningsTabellAfpOffentlig(
        val beregning: Expression<AfpOffentligSektor.Beregning>,
        val beregningVirkDatoFom: Expression<LocalDate>,
        val grunnbeloep: Expression<Kroner>,
        val framtidigArligInntekt: Expression<Kroner>,
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +"Den månedlige pensjonen fra " + beregningVirkDatoFom.format() +
                            " er slik. Folketrygdens grunnbeløp (G) benyttet i beregningen er " +
                            grunnbeloep.format() +
                            ". Framtidig årlig inntekt benyttet i beregningen er " +
                            framtidigArligInntekt.format() + "."
                    },
                    nynorsk {
                        +"Den månadlege pensjonen frå " + beregningVirkDatoFom.format() +
                            " er slik. Grunnbeløpet (G) i folketrygda nytta i berekninga er " +
                            grunnbeloep.format() +
                            ". Forventa årleg inntekt nytta i berekninga er " +
                            framtidigArligInntekt.format() + "."
                    },
                )
            }

            showIf(beregning.brutto.notEqualTo(beregning.netto)) {
                paragraph {
                    table(
                        header = {
                            column {
                                text(bokmal { +"" }, nynorsk { +"" })
                            }
                            column(alignment = ColumnAlignment.RIGHT) {
                                text(
                                    bokmal { +"Pensjon per måned før fradrag for inntekten" },
                                    nynorsk { +"Pensjon per månad før frådrag for inntekt" },
                                )
                            }
                            column(alignment = ColumnAlignment.RIGHT) {
                                text(
                                    bokmal { +"Pensjon per måned etter fradrag for inntekten" },
                                    nynorsk { +"Pensjon per månad etter frådrag for inntekt" },
                                )
                            }
                        },
                    ) {
                        row {
                            cell { text(bokmal { +"Grunnpensjon" }, nynorsk { +"Grunnpensjon" }) }
                            cell { includePhrase(KronerText(beregning.grunnpensjon.ytelsesKomponentBrutto)) }
                            cell { includePhrase(KronerText(beregning.grunnpensjon.ytelsesKomponentNetto)) }
                        }
                        komponentRow(beregning.tilleggspensjon, "Tilleggspensjon", "Tilleggspensjon")
                        komponentRow(beregning.saertillegg, "Særtillegg", "Særtillegg")
                        komponentRow(beregning.minstenivaatilleggIndividuelt, "Minstenivåtillegg individuelt", "Minstenivåtillegg individuelt")
                        komponentRow(beregning.afpTillegg, "AFP-tillegg", "AFP-tillegg")
                        ifNotNull(beregning.ektefelletillegg) { et ->
                            row {
                                cell { text(bokmal { +"Ektefelletillegg" }, nynorsk { +"Ektefelletillegg" }) }
                                cell { includePhrase(KronerText(et.ektefelletilleggBrutto)) }
                                cell { includePhrase(KronerText(et.ektefelletilleggNetto)) }
                            }
                        }
                        komponentRow(beregning.fasteUtgifterInstitusjon, "Faste utgifter ved institusjonsopphold", "Faste utgifter ved institusjonsopphald")
                        komponentRow(beregning.familietillegg, "Familietillegg", "Familietillegg")
                        row {
                            cell { text(bokmal { +"Sum pensjon før skatt" }, nynorsk { +"Sum pensjon før skatt" }, BOLD) }
                            cell { includePhrase(KronerText(beregning.brutto, BOLD)) }
                            cell { includePhrase(KronerText(beregning.netto, BOLD)) }
                        }
                    }
                }
            }

            showIf(beregning.brutto.equalTo(beregning.netto)) {
                paragraph {
                    table(
                        header = {
                            column {
                                text(bokmal { +"" }, nynorsk { +"" })
                            }
                            column(alignment = ColumnAlignment.RIGHT) {
                                text(bokmal { +"Pensjon per måned" }, nynorsk { +"Pensjon per månad" })
                            }
                        },
                    ) {
                        row {
                            cell { text(bokmal { +"Grunnpensjon" }, nynorsk { +"Grunnpensjon" }) }
                            cell { includePhrase(KronerText(beregning.grunnpensjon.ytelsesKomponentNetto)) }
                        }
                        komponentNettoRow(beregning.tilleggspensjon, "Tilleggspensjon", "Tilleggspensjon")
                        komponentNettoRow(beregning.saertillegg, "Særtillegg", "Særtillegg")
                        komponentNettoRow(beregning.minstenivaatilleggIndividuelt, "Minstenivåtillegg individuelt", "Minstenivåtillegg individuelt")
                        komponentNettoRow(beregning.afpTillegg, "AFP-tillegg", "AFP-tillegg")
                        ifNotNull(beregning.ektefelletillegg) { et ->
                            row {
                                cell { text(bokmal { +"Ektefelletillegg" }, nynorsk { +"Ektefelletillegg" }) }
                                cell { includePhrase(KronerText(et.ektefelletilleggNetto)) }
                            }
                        }
                        komponentNettoRow(beregning.fasteUtgifterInstitusjon, "Faste utgifter ved institusjonsopphold", "Faste utgifter ved institusjonsopphald")
                        komponentNettoRow(beregning.familietillegg, "Familietillegg", "Familietillegg")
                        row {
                            cell { text(bokmal { +"Sum pensjon før skatt" }, nynorsk { +"Sum pensjon før skatt" }, BOLD) }
                            cell { includePhrase(KronerText(beregning.netto, BOLD)) }
                        }
                    }
                }
            }
        }
    }

    /**
     * Innholdsliste-paragrafen «Dette brevet inneholder informasjon om …».
     *
     * Listen er identisk mellom 04_001 og 04_020 med ett unntak: 04_001 viser et
     * ekstra punkt «hvordan pensjonen din er beregnet» fordi brevet har vedlegget
     * «Hvordan pensjonen er beregnet». Styres via [inkluderHvordanPensjonenItem].
     */
    data class Innholdsliste(
        val beregning: Expression<AfpOffentligSektor.Beregning>,
        val flerePerioder: Expression<Boolean>,
        val virkningFom: Expression<LocalDate>,
        val inkluderHvordanPensjonenItem: Boolean,
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(
                    bokmal { +"Dette brevet inneholder informasjon om" },
                    nynorsk { +"Dette brevet inneheld informasjon om" },
                )
                list {
                    item {
                        text(
                            bokmal { +"begrunnelse for vedtaket" },
                            nynorsk { +"grunngiving for vedtaket" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"utbetaling og skatt" },
                            nynorsk { +"utbetaling og skatt" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"hvordan AFP beregnes i forhold til tidligere og framtidig arbeidsinntekt" },
                            nynorsk { +"korleis AFP blir berekna i forhold til tidlegare og framtidig arbeidsinntekt" },
                        )
                    }
                    showIf(
                        beregning.ektefelletillegg.notNull()
                            .and(beregning.barnetilleggSerkull.not().or(beregning.barnetilleggFelles.not()))
                    ) {
                        item {
                            text(
                                bokmal { +"ektefelletillegg og inntekt" },
                                nynorsk { +"ektefelletillegg og inntekt" },
                            )
                        }
                    }
                    item {
                        text(
                            bokmal { +"dine plikter og rettigheter" },
                            nynorsk { +"dine plikter og rettar" },
                        )
                    }
                    if (inkluderHvordanPensjonenItem) {
                        item {
                            text(
                                bokmal { +"hvordan pensjonen din er beregnet" },
                                nynorsk { +"korleis pensjonen din er berekna" },
                            )
                        }
                    }
                    showIf(flerePerioder) {
                        item {
                            text(
                                bokmal { +"oversikt over pensjonens størrelse fra " + virkningFom.format() },
                                nynorsk { +"kor stor pensjonen er (oversikt) frå " + virkningFom.format() },
                            )
                        }
                    }
                    item {
                        text(
                            bokmal { +"vedlegg som gir kort informasjon om etteroppgjør" },
                            nynorsk { +"etteroppgjer (vedlegg)" },
                        )
                    }
                }
            }
        }
    }

    /**
     * Title1 «Utbetaling og skatt» – brukt som overskrift for seksjonen om
     * utbetalingsdager, skatt og etterbetaling i AFP-vedtaksbrev.
     */
    object UtbetalingOgSkattTittel : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            title1 {
                text(
                    bokmal { +"Utbetaling og skatt" },
                    nynorsk { +"Utbetaling og skatt" },
                )
            }
        }
    }

    /**
     * Opplyser om at ektefelletillegget er innvilget, men ikke kommer til
     * utbetaling fordi netto ektefelletillegg er 0 (samlet inntekt for høy).
     * Emitterer ingenting hvis det ikke finnes ektefelletillegg eller hvis
     * netto er ulik 0.
     */
    data class EktefelletilleggIkkeUtbetaltOpplysning(
        val ektefelletillegg: Expression<AfpOffentligSektor.Ektefelletillegg?>,
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            ifNotNull(ektefelletillegg) { et ->
                showIf(et.ektefelletilleggNetto.equalTo(Kroner(0))) {
                    paragraph {
                        text(
                            bokmal { +"Ektefelletillegget vil ikke komme til utbetaling fordi din samlede inntekt er for høy." },
                            nynorsk { +"Ektefelletillegget vil ikkje komme til utbetaling fordi den samla inntekta di er for høg." },
                        )
                    }
                }
            }
        }
    }

    /**
     * Standardsats-paragrafen brukt i etterbetalings-seksjonen i AFP-vedtaksbrev.
     * Tekst er byte-identisk mellom 04_001 og 04_020. Den foregående
     * «Du får etterbetalt …»-paragrafen avviker mellom brevene og emitteres
     * fortsatt inline i hvert brev.
     */
    object EtterbetalingStandardSats : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +"Ved etterbetalinger som gjelder tidligere år, vil Nav trekke 30 prosent som en standardsats. Dersom du " +
                            "krever at Skatteetaten forut for utbetalingen reberegner skatten for de tidligere årene, så må du gi " +
                            "beskjed om dette til Nav innen sju dager etter dato for dette brevet."
                    },
                    nynorsk {
                        +"Ved etterbetalingar som gjeld tidlegare år, vil Nav trekkje 30 prosent som ein standardsats. Dersom du " +
                            "krev at Skatteetaten før utbetalinga bereknar om att skatten for dei tidlegare åra, må du gi beskjed " +
                            "om det til Nav innan sju dagar etter datoen for dette brevet."
                    },
                )
            }
        }
    }
}

private fun TableScope<LangBokmalNynorsk, Unit>.komponentRow(
    ytelsesKomponent: Expression<AfpOffentligSektor.YtelsesKomponent?>,
    bokmalLabel: String,
    nynorskLabel: String,
) {
    ifNotNull(ytelsesKomponent) { k ->
        row {
            cell { text(bokmal { +bokmalLabel }, nynorsk { +nynorskLabel }) }
            cell { includePhrase(KronerText(k.ytelsesKomponentBrutto)) }
            cell { includePhrase(KronerText(k.ytelsesKomponentNetto)) }
        }
    }
}

private fun TableScope<LangBokmalNynorsk, Unit>.komponentNettoRow(
    ytelsesKomponent: Expression<AfpOffentligSektor.YtelsesKomponent?>,
    bokmalLabel: String,
    nynorskLabel: String,
) {
    ifNotNull(ytelsesKomponent) { k ->
        row {
            cell { text(bokmal { +bokmalLabel }, nynorsk { +nynorskLabel }) }
            cell { includePhrase(KronerText(k.ytelsesKomponentNetto)) }
        }
    }
}
