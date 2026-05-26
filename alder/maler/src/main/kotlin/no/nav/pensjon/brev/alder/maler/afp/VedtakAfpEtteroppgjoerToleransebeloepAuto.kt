package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerAvslutning
import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerForklaringer
import no.nav.pensjon.brev.alder.maler.felles.HarDuSpoersmaal
import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerInnhold
import no.nav.pensjon.brev.alder.maler.felles.Constants.AFP_ETTEROPPGJOER_URL
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerToleransebeloepAutoDto
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerToleransebeloepAutoDto.Periode
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerToleransebeloepAutoDtoSelectors.avvik
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerToleransebeloepAutoDtoSelectors.fpiberegnet
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerToleransebeloepAutoDtoSelectors.ieo
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerToleransebeloepAutoDtoSelectors.ifu
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerToleransebeloepAutoDtoSelectors.iiap
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerToleransebeloepAutoDtoSelectors.oppgjoersAar
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerToleransebeloepAutoDtoSelectors.opphorsdato
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerToleransebeloepAutoDtoSelectors.periode
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerToleransebeloepAutoDtoSelectors.pgi
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerToleransebeloepAutoDtoSelectors.uttaksdato
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.notNull
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

/**
 * Vedtak — ingen endring (innenfor toleransebeløpet) — AFP etteroppgjør (autobrev).
 *
 * Konvertert fra Exstream-malen `PE_AF_04_100`. Brevet sendes etter et
 * AFP-etteroppgjør (offentlig sektor / Statens pensjonskasse) når forskjellen
 * mellom forventet og faktisk pensjonsgivende inntekt ikke overstiger
 * toleransebeløpet, og pensjonsberegningen derfor ikke
 * skal endres.
 */
@TemplateModelHelpers
object VedtakAfpEtteroppgjoerToleransebeloepAuto : AutobrevTemplate<VedtakAfpEtteroppgjoerToleransebeloepAutoDto> {

    override val kode = Aldersbrevkoder.AutoBrev.PE_AFP_ETTEROPPGJOER_TOLERANSEBELOP_AUTO

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - ingen endring (innenfor toleransebeløp) - AFP etteroppgjør",
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
            includePhrase(AfpEtteroppgjoerInnhold.EtteroppgjoerIntro)
            includePhrase(AfpEtteroppgjoerInnhold.IkkeFunnetGrunnlagForAaEndre(oppgjoersAar))

            paragraph {
                text(
                    bokmal {
                        +"Vi gjør oppmerksom på at innrapporterte inntektsopplysninger fra Skatteetaten " +
                            "ikke skiller mellom hvor stor del av inntekten din som er opptjent før og " +
                            "etter at du tok ut AFP. Nav kan heller ikke se om noen av inntektene stammer " +
                            "fra arbeid i forbindelse med covid-19 eller arbeid med fordrevne fra Ukraina. " +
                            "Noen slike inntekter kan unntas fra inntektsavkortingen."
                    },
                    nynorsk {
                        +"Vi gjer merksam på at innrapporterte inntektsopplysningar frå Skatteetaten ikkje " +
                            "skil mellom kor stor del av inntekta di som er opptent før og etter at du tok " +
                            "ut AFP. Nav kan heller ikkje sjå om nokre av inntektene stammar frå arbeid i " +
                            "samband med covid-19 eller arbeid med fordrivne frå Ukraina. Enkelte slike " +
                            "inntekter kan haldast utanfor inntektsavkortingen."
                    },
                )
            }

            paragraph {
                text(
                    bokmal {
                        +"Hvis du har hatt lavere pensjonsgivende inntekt den tiden du har hatt AFP enn " +
                            "det våre beregninger viser, kan du ha rett til høyere AFP."
                    },
                    nynorsk {
                        +"Dersom du har hatt lågare pensjonsgivande inntekt i perioden med rett til AFP " +
                            "enn det berekningane våre viser, kan du ha rett til høgare AFP."
                    },
                )
            }

            includePhrase(AfpEtteroppgjoerInnhold.VedtaksgrunnlagAfpSpk)

            // Melding om endringer av inntekten + dokumentasjon + covid-19 + Ukraina (alltid).
            // Identisk innhold med PE_AF_04_102 er trukket ut til delte fraser; bare paragrafer
            // med ordlydsforskjeller er inlinet og markert med TODO for faglig gjennomgang.
            //
            // Seksjon 1: Melding om endringer av inntekten.
            includePhrase(AfpEtteroppgjoerInnhold.MeldingOmEndringerInnledning)

            // Inlinet: «Inntekt som skal holdes utenfor etteroppgjøret» — covid-19-punktet
            // i listen avviker fra PE_AF_04_102.
            paragraph {
                text(
                    bokmal { +"Inntekt som skal holdes utenfor etteroppgjøret:" },
                    nynorsk { +"Inntekt som skal haldast utanfor etteroppgjeret:" },
                )
                list {
                    item {
                        text(
                            bokmal { +"Feriepenger og lønn som er opptjent før første uttak av AFP." },
                            nynorsk { +"Feriepengar og lønn som er opptente før første uttaket av AFP." },
                        )
                    }
                    item {
                        text(
                            bokmal {
                                +"Honorar, royalty, bonus eller andre inntekter som stammer fra arbeid " +
                                    "eller virksomhet før første uttak av AFP."
                            },
                            nynorsk {
                                +"Honorar, royalty, bonus eller andre inntekter som stammar frå arbeid " +
                                    "eller verksemd før det første uttaket av AFP."
                            },
                        )
                    }
                    item {
                        text(
                            bokmal {
                                +"Etterbetaling av trygdeytelser som gjelder for tidsrom før AFP ble " +
                                    "tatt ut."
                            },
                            nynorsk {
                                +"Etterbetaling av trygdeytingar som gjeld for tidsrom før AFP vart " +
                                    "tatt ut."
                            },
                        )
                    }
                    // TODO ordlydsavvik vs PE_AF_04_102: 04_102 har "Feriepenger som stammer fra arbeid med covid-19." / "...covid-19-pandemien.". Avklar med fag om ordlyden skal harmoniseres.
                    item {
                        text(
                            bokmal {
                                +"Feriepenger som stammer fra enkelte typer arbeid i forbindelse med " +
                                    "covid-19."
                            },
                            nynorsk {
                                +"Feriepengar som skriv seg frå enkelte typar arbeid i samband med covid-19."
                            },
                        )
                    }
                    item {
                        text(
                            bokmal {
                                +"Inntekt som stammer fra arbeid i forbindelse med fordrevne fra Ukraina."
                            },
                            nynorsk {
                                +"Inntekt som skriv seg frå arbeid i samband med fordrivne frå Ukraina."
                            },
                        )
                    }
                }
                text(
                    bokmal {
                        +"Se mer informasjon om arbeid i forbindelse med covid-19 og fordrevne fra " +
                            "Ukraina nedenfor."
                    },
                    nynorsk {
                        +"Sjå meir informasjon om arbeid i samband med covid-19 og fordrivne frå " +
                            "Ukraina nedanfor."
                    },
                )
            }

            includePhrase(AfpEtteroppgjoerInnhold.AnnenInntektInntektsproevd)

            includePhrase(AfpEtteroppgjoerInnhold.DokumenterInntekterUtenforAvkorting)

            includePhrase(AfpEtteroppgjoerInnhold.SkjemaForDokumentasjon)

            // Seksjon 2: Spesielt om inntekter opptjent i forbindelse med covid-19.
            includePhrase(AfpEtteroppgjoerInnhold.SpesieltOmCovidInntekterInnledning)

            // Inlinet: covid-19-dokumentasjonslisten — avviker fra PE_AF_04_102.
            // TODO dokumentasjonslisten avviker fra PE_AF_04_102: 04_102 har 4 punkter (med "om arbeidet var beordret eller frivillig") og bokmål-ordlyd "i hvilken tidsperiode(-r) dette gjelder". Avklar med fag om listen skal harmoniseres.
            paragraph {
                text(
                    bokmal {
                        +"For at Nav skal kunne holde slike inntekter utenfor avkorting, må du sende " +
                            "oss dokumentasjon. Se mer på $AFP_ETTEROPPGJOER_URL om hvordan du sender " +
                            "dokumentasjon. Vi trenger bekreftelse fra arbeidsgiveren din om følgende:"
                    },
                    nynorsk {
                        +"For at Nav skal kunne halde slike inntekter utanfor avkorting, må du sende " +
                            "oss dokumentasjon. Sjå meir på $AFP_ETTEROPPGJOER_URL. Vi treng stadfesting " +
                            "frå arbeidsgivaren din om følgjande:"
                    },
                )
                list {
                    item {
                        text(
                            bokmal { +"hvor mye du har hatt i inntekter fra slikt ekstra arbeid" },
                            nynorsk { +"kor mykje du har hatt i feriepengar frå slikt ekstra arbeid" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"hvilken tidsperiode(-r) feriepengene er opptjent" },
                            nynorsk { +"i kva for tidsperiode (-periodar) feriepengane er tent opp" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"om utbetalingen er gjort etter særskilt sats for pensjonistavlønning" },
                            nynorsk { +"om utbetalinga er gjort etter særskilt sats for pensjonistavlønning" },
                        )
                    }
                }
            }

            // Seksjon 3: Spesielt om unntak ... fordrevne fra Ukraina.
            includePhrase(AfpEtteroppgjoerInnhold.SpesieltOmUkrainaUnntak)
            includePhrase(AfpEtteroppgjoerInnhold.InntektenDinIAarTittel(oppgjoersAar))

            includePhrase(AfpEtteroppgjoerInnhold.SamletPgiOpplysning(pgi = pgi, oppgjoersAar = oppgjoersAar))

            // Periode-diskriminert fordeling av PGI på periodene med/uten AFP.
            // Delt med PE_AF_04_101 (etterbetaling). Se phrase for detaljer.
            includePhrase(
                AfpEtteroppgjoerInnhold.IfuIeoFordelingPerPeriode(
                    erHelAfpHeleAaret = periode.equalTo(Periode.HEL_AFP_HELE_AARET),
                    erUttakIAaret = periode.equalTo(Periode.UTTAK_I_AARET),
                    erOpphoerIAaret = periode.equalTo(Periode.OPPHOER_I_AARET),
                    erUttakOgOpphoerIAaret = periode.equalTo(Periode.UTTAK_OG_OPPHOER_I_AARET),
                    uttaksdato = uttaksdato,
                    opphorsdato = opphorsdato,
                    oppgjoersAar = oppgjoersAar,
                    ifu = ifu,
                    ieo = ieo,
                    iiap = iiap,
                ),
            )

            paragraph {
                text(
                    bokmal {
                        +"Ved beregningen av pensjonen din for " + oppgjoersAar.format() + " la vi til grunn " +
                            "at du ville ha en forventet arbeidsinntekt på " + fpiberegnet.format() + ". " +
                            "Forskjellen mellom den tidligere benyttede arbeidsinntekten og den " +
                            "arbeidsinntekten du etter vår beregning har hatt i perioden, utgjør " +
                            avvik.format() + ". Denne forskjellen er ikke større enn toleransebeløpet som " +
                            "i 2024 var på 15 000 kroner."
                    },
                    nynorsk {
                        +"Ved berekninga av pensjonen din for " + oppgjoersAar.format() + " la vi til grunn " +
                            "at du ville ha ei forventa arbeidsinntekt på " + fpiberegnet.format() + ". " +
                            "Forskjellen mellom den tidlegare nytta arbeidsinntekta og den arbeidsinntekta " +
                            "du etter vår berekning har hatt i perioden, utgjer " + avvik.format() + ". " +
                            "Denne forskjellen er ikkje større enn toleransebeløpet som i 2024 var på " +
                            "15 000 kroner."
                    },
                )
            }

            includePhrase(AfpEtteroppgjoerInnhold.PensjonsberegningenBlirIkkeEndret(oppgjoersAar))

            includePhrase(AfpEtteroppgjoerAvslutning.DinePlikter)

            includePhrase(AfpEtteroppgjoerAvslutning.DuHarRettTilAaKlageMedDokumentasjonsfrist)

            includePhrase(AfpEtteroppgjoerAvslutning.DuHarRettTilInnsyn)
            includePhrase(HarDuSpoersmaal.afpEtteroppgjoer)
        }
    }
}
