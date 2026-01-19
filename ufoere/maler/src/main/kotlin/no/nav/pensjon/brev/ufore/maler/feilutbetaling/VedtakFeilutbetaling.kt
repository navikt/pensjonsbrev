package no.nav.pensjon.brev.ufore.maler.feilutbetaling

import no.nav.pensjon.brev.ufore.maler.FeatureToggles
import no.nav.pensjon.brev.ufore.api.model.maler.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brev.ufore.maler.vedlegg.oversiktOverFeilutbetalinger
import no.nav.pensjon.brev.ufore.maler.vedlegg.oversiktOverFeilutbetalingerPaRadform
import no.nav.pensjon.brev.ufore.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.LocalizedFormatter.CurrencyFormat
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.isNull
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.expression.notNull
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.namedReference
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_VEDTAK_FEILUTBETALING
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.OversiktOverFeilutbetalingPEDtoSelectors.feilutbetalingPerArListe
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.PesysDataSelectors.feilutbetaltTotalBelop
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.PesysDataSelectors.oversiktOverFeilutbetalingPEDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.PesysDataSelectors.resultatAvVurderingenForTotalBelop
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.PesysDataSelectors.sluttPeriodeForTilbakekreving
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.PesysDataSelectors.startPeriodeForTilbakekreving
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.PesysDataSelectors.sumTilInnkrevingTotalBelop
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.TilbakekrevingResultat
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.VedtakFeilutbetalingUforeDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.VedtakFeilutbetalingUforeDtoSelectors.SaksbehandlervalgSelectors.reduksjonForeldelse
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.VedtakFeilutbetalingUforeDtoSelectors.SaksbehandlervalgSelectors.sivilstandEndret
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.VedtakFeilutbetalingUforeDtoSelectors.pesysData
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.VedtakFeilutbetalingUforeDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.ufore.maler.feilutbetaling.varsel.FeilutbetalingFraser
import no.nav.pensjon.brev.ufore.maler.fraser.Constants
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object VedtakFeilutbetaling : RedigerbarTemplate<VedtakFeilutbetalingUforeDto> {

    override val featureToggle = FeatureToggles.feilutbetaling.toggle

    override val kode = UT_VEDTAK_FEILUTBETALING
    override val kategori = TemplateDescription.Brevkategori.FEILUTBETALING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal, Language.Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - tilbakekreving av feilutbetalt beløp",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        val feilutbetaltTotalBeloep = pesysData.feilutbetaltTotalBelop.format(CurrencyFormat)
        val resultatAvVurderingenForTotalBeloep = pesysData.resultatAvVurderingenForTotalBelop
        val sluttPeriodeForTilbakekreving = pesysData.sluttPeriodeForTilbakekreving
        val startPeriodeForTilbakekreving = pesysData.startPeriodeForTilbakekreving
        val sumTilInnkrevingTotalBeloep = pesysData.sumTilInnkrevingTotalBelop.format(CurrencyFormat)
        val dato = fritekst("dato")

        title {
            text(
                bokmal { + "Du må betale tilbake uføretrygd"},
                nynorsk { + "Du må betale tilbake uføretrygd "}
            )
        }
        outline {
            paragraph {
                text(
                    bokmal { + "Du har fått utbetalt for mye uføretrygd fra " + dato + " til " + dato +
                            ". Det feilutbetalte beløpet er " + sumTilInnkrevingTotalBeloep + " kroner uten skatt. " },
                    nynorsk { + "Du har fått utbetalt for mykje uføretrygd frå " + dato + " til " + dato +
                            ". Det feilutbetalte " + sumTilInnkrevingTotalBeloep + " er beløp kroner utan skatt. "})
            }
            paragraph {
                showIf(resultatAvVurderingenForTotalBeloep equalTo TilbakekrevingResultat.FULL_TILBAKEKREV) {
                    text(
                        bokmal { +"Vi har kommet fram til at du skal betale tilbake hele beløpet. " },
                        nynorsk { +"Vi har kome fram til at du skal betale tilbake heile beløpet. " }
                    )
                }.orShow {
                    text (
                        bokmal { + "Vi har kommet fram til at du skal betale tilbake deler av beløpet. Beløpet du skal betale tilbake er beløp kroner. " },
                        nynorsk { + "Vi har kome fram til at du skal betale tilbake delar av beløpet. Beløpet du skal betale tilbake er beløp kroner. " }
                    )
                }
            }
            paragraph {
                text (
                    bokmal { + "I vedlegget " },
                    nynorsk { + "I vedlegget " }
                )
                text (
                    bokmal { + "Oversikt over feilutbetalinger" },
                    nynorsk { + "Oversikt over feilutbetalinger" }
                )
                text (
                    bokmal { + " finner du en oversikt over periodene med feilutbetalinger og beløpet du må betale tilbake." },
                    nynorsk { + " finn du ei oversikt over periodane med feilutbetalingar og beløpet du må betale tilbake. " }
                )
            }

            title1 {
                text (
                    bokmal { + "Derfor må du betale tilbake " },
                    nynorsk { + "Derfor må du betale tilbake " }
                )
            }
            paragraph {
                showIf(resultatAvVurderingenForTotalBeloep equalTo TilbakekrevingResultat.FULL_TILBAKEKREV) {
                    text(
                        bokmal {
                            +"Fordi situasjonen din har endret seg og vi ikke har fått beskjed, har denne endringen ført til at du har fått utbetalt for mye. " +
                                    "Derfor har vi vurdert at vi skal kreve tilbake hele det feilutbetalte beløpet. "
                        },
                        nynorsk {
                            +"Fordi situasjonen din har endra seg og vi ikkje har fått beskjed, har denne endringa ført til at du har fått utbetalt for mykje. " +
                                    "Derfor har vi vurdert at vi skal krevje tilbake heile det feilutbetalte beløpet. "
                        }
                    )
                }.orShow {
                    text(
                        bokmal {
                            +"Fordi situasjonen din har endret seg og vi ikke har fått beskjed, har denne endringen ført til at du har fått utbetalt for mye. " +
                                    "Derfor har vi vurdert at vi skal kreve tilbake deler av det feilutbetalte beløpet. "
                        },
                        nynorsk {
                            +"Fordi situasjonen din har endra seg og vi ikkje har fått beskjed, har denne endringa ført til at du har fått utbetalt for mykje. " +
                                    "Derfor har vi vurdert at vi skal krevje tilbake delar av det feilutbetalte beløpet. "
                        }
                    )
                }
            }
            paragraph {
                text (
                    bokmal { + "I vedtaksbrevet vi sendte deg, har vi opplyst om at X påvirker beregningen av uføretrygden din. " +
                            "I vedtaket står det også at du må gi oss beskjed om endringer i din situasjon. " +
                            "Vi vurder derfor at du har fått informasjon om regelverket for beregning av uføretrygden din, og dine rettigheter og plikter. " },
                    nynorsk { + "I vedtaksbrevet vi sende deg, har vi opplyst om at X påverkar berekninga av uføretrygda di. " +
                            "I vedtaket står det òg at du må gi oss beskjed om endringar i situasjonen din. " +
                            "Vi vurderer derfor at du har fått informasjon om regelverket for berekning av uføretrygd, og dine rettar og plikter. " }
                )
            }
            paragraph {
                text(
                    bokmal { + fritekst("Din vurdering om bruker har gitt beskjed") },
                    nynorsk { + fritekst("Di vurdering om brukar har gitt beskjed ") }
                )
            }
            paragraph {
                text (
                    bokmal { + "Vi vurderer at vilkårene for tilbakekreving er oppfylt etter folketrygdloven § 22-15 første ledd. " },
                    nynorsk { + "Vi vurderer at vilkåra for tilbakekrevjing er oppfylt etter folketrygdlova § 22-15 første ledd. " }
                )
            }

            includePhrase(FeilutbetalingFraser.KriterierTilbakekreving)
            includePhrase(FeilutbetalingFraser.KriterierIngenTilbakekreving)

            title1 {
                text (
                    bokmal { + "I disse tilfellene kan vi ikke kreve tilbake feilutbetalt beløp " },
                    nynorsk { + "Derfor kan vi ikkje krevje tilbake feilutbetalt beløp " }
                )
            }
            paragraph {
                text (
                    bokmal { + "Folketrygdloven § 22-15 fjerde ledd sier at vi som hovedregel skal kreve tilbake feilutbetalte beløpet, " +
                            "med mindre det finnes særlige grunner til å la være. Det betyr at vi bare kan gjøre unntak hvis det foreligger sterke grunner til ikke å kreve tilbake. " },
                    nynorsk { + "Folketrygdlova § 22-15 fjerde ledd seier at vi som hovudregel skal krevje tilbake det feilutbetalte beløpet, " +
                            "dersom det ikkje finst særlege grunnar til å la vere. Det betyr at vi berre kan gjere unntak dersom det ligg føre sterke grunnar til å ikkje krevje tilbake. " }
                )
            }
            showIf(resultatAvVurderingenForTotalBeloep equalTo TilbakekrevingResultat.FULL_TILBAKEKREV) {
                paragraph {
                    text(
                        bokmal {
                            +"Vi har vurdert om det finnes særlige grunner til å ikke kreve tilbake hele eller deler av det feilutbetalte beløpet. " +
                                    "Vi har vurdert at unntaksregelen om særlige grunner ikke kan brukes, og vi krever derfor tilbake hele beløpet. "
                        },
                        nynorsk {
                            +"Vi har vurdert om det finst særlege grunnar til å ikkje krevje tilbake heile eller delar av det feilutbetalte beløpet. " +
                                    "Vi har vurdert at unntaksregelen om særlege grunnar ikkje kan brukast, og vi krev derfor tilbake heile beløpet. \n"
                        }
                    )
                }
                paragraph {
                    text (
                        bokmal { + "Feilutbetalt beløp kreves tilbake i sin helhet, jf. folketrygdloven § 22-15 fjerde ledd. " },
                        nynorsk { + "Feilutbetalt beløp krevjast tilbake i sin heilskap, jf. folketrygdlova § 22-15 fjerde ledd." }
                    )
                }
            }.orShow{
                paragraph {
                    text (
                        bokmal { + "Vi har vurdert om det finnes særlige grunner til å ikke kreve tilbake hele eller deler av det feilutbetalte beløpet. " +
                                "Vi har vurdert at [beskriv kort grunnlaget, f.eks. «mottaker har ikke hatt mulighet til å oppdage feilen», «svært lang tid har gått», " +
                                "«særlige sosiale eller økonomiske forhold»]. På denne bakgrunn settes kravet ned / frafalles helt." },
                        nynorsk { + "Vi har vurdert om det finst særlege grunnar til å ikkje krevje tilbake heile eller delar av det feilutbetalte beløpet. " +
                                "Vi har vurdert at [beskriv kort grunnlaget, t.d. «mottakar har ikkje hatt moglegheit til å oppdage feilen», «svært lang tid har gått», " +
                                "«særlege sosiale eller økonomiske forhold»]. På denne bakgrunnen blir kravet sett ned / fråfalle heilt." }
                    )
                }
                paragraph {
                    text (
                        bokmal { + "Feilutbetalt beløp kreves tilbake i sin helhet, jf. folketrygdloven § 22-15 fjerde ledd. " },
                        nynorsk { + "Feilutbetalt beløp krevjast delvis tilbake, jf. folketrygdlova § 22-15 fjerde ledd." }
                    )
                }

            }

            paragraph {
                text (
                    bokmal { + "Vedtaket er gjort etter folketrygdloven § 22-15. " },
                    nynorsk { + "Vedtaket er gjort etter folketrygdlova § 22-15. " }
                )
            }

            title1 {
                text (
                    bokmal { + "Beregning " },
                    nynorsk { + "Berekning " }
                )
            }
            paragraph {
                text (
                    bokmal { + "Beløpet som er feilutbetalt i perioden fra " + dato + " til " + dato + " er " + feilutbetaltTotalBeloep + " kroner (inkludert skatt). " +
                            "Du skal bare betale tilbake " + sumTilInnkrevingTotalBeloep + " kroner (uten skatt). Du finner fullstendig beregning i vedlegget " +
                            "«Oversikt over feilutbetalinger». " },
                    nynorsk { + "Beløpet som er feilutbetalt i perioden frå " + dato + " til " + dato + " er " + feilutbetaltTotalBeloep + " kroner (inkludert skatt). " +
                            "Du skal berre betale tilbake " + sumTilInnkrevingTotalBeloep + " kroner (utan skatt). Du finn fullstendig berekning i vedlegget " +
                            "«Oversikt over feilutbetalingar». " }
                )
            }

            title1 {
                text (
                    bokmal { + "Slik påvirker dette skatteoppgjøret ditt " },
                    nynorsk { + "Slik påverkar dette skatteoppgjeret ditt " }
                )
            }
            paragraph {
                text (
                    bokmal { + "Vi rapporterer tilbakebetalingen din til Skatteetaten. De vil korrigere skatteoppgjøret ditt ut fra denne endringen." },
                    nynorsk { + "Vi rapporterer tilbakebetalinga di til Skatteetaten. Dei vil korrigere skatteoppgjeret ditt ut frå denne endringa. " }
                )
            }
            paragraph {
                text (
                    bokmal { + "Du skal betale tilbake det feilutbetalte beløpet til Skatteetaten (innkrevingsmyndigheten). " +
                            "De vil sende deg en faktura på beløpet du skal betale tilbake. Har du spørsmål om nedbetalingsplan eller annet i forbindelse med tilbakebetalingen, " +
                            "må du ta kontakt med Skatteetaten. " },
                    nynorsk { + "Du skal betale tilbake det feilutbetalte beløpet til Skatteetaten (innkrevingsmyndigheita). " +
                            "Dei vil sende deg ein faktura på beløpet du skal betale tilbake. Har du spørsmål om nedbetalingsplan eller anna i samband med tilbakebetalinga, " +
                            "må du ta kontakt med Skatteetaten. " }
                )
            }
            title1 {
                text (
                    bokmal { + "Du har rett til å klage " },
                    nynorsk { + "Du har rett til å klage " }
                )
            }
            paragraph {
                text (
                    bokmal { + "Dersom du meiner at vi har gitt deg feil eller mangelfull informasjon, eller du har hatt ein alvorleg helsetilstand, " +
                            "ber vi deg om å gi oss dokumentasjon på dette i klagen din. " },
                    nynorsk { + "Dersom du meiner at vi har gitt deg feil eller mangelfull informasjon, eller du har hatt ein alvorleg helsetilstand, " +
                            "ber vi deg om å gi oss dokumentasjon på dette i klagen din. " }
                )
            }
            paragraph {
                text (
                    bokmal { + "Hvis du mener vedtaket er feil, kan du klage innen 6 uker fra den datoen vedtaket har kommet fram til deg. " +
                            "Dette følger av folketrygdloven § 21-12. Du finner skjema og informasjon på ${Constants.KLAGE_URL}.  " },
                    nynorsk { + "Dersom du meiner vedtaket er feil, kan du klage innan 6 veker frå den datoen vedtaket har kome fram til deg. " +
                            "Dette følgjer av folketrygdlova § 21-12. Du finn skjema og informasjon på ${Constants.KLAGE_URL}. " }
                )
            }
            paragraph {
                text (
                    bokmal { + "Nav kan veilede deg på telefon om hvordan du sender en klage. Nav-kontoret ditt kan også hjelpe deg med å skrive en klage. " +
                            "Kontakt oss på telefon ${Constants.NAV_KONTAKTSENTER_TELEFON_UFORE} hvis du trenger hjelp.  " },
                    nynorsk { + "Nav kan rettleie deg på telefon om korleis du sender ein klage. Nav-kontoret ditt kan òg hjelpe deg med å skrive ein klage. " +
                            "Kontakt oss på telefon ${Constants.NAV_KONTAKTSENTER_TELEFON_UFORE} dersom du treng hjelp. " }
                )
            }
            paragraph {
                text (
                    bokmal { + "Hvis du sender klage i posten, må du signere klagen.  " },
                    nynorsk { + "Dersom du sender klage i posten, må du signere klagen. " }
                )
            }
            paragraph {
                text (
                    bokmal { + "Mer informasjon om klagerettigheter finner du på ${Constants.KLAGERETTIGHETER_URL}. " },
                    nynorsk { + "Meir informasjon om klagerettar finn du på ${Constants.KLAGERETTIGHETER_URL}. " }
                )
            }
            includePhrase(Felles.RettTilInnsynRefVedlegg)
            includePhrase(Felles.HarDuSporsmal)
        }

        includeAttachment(oversiktOverFeilutbetalingerPaRadform, pesysData.oversiktOverFeilutbetalingPEDto, pesysData.oversiktOverFeilutbetalingPEDto.feilutbetalingPerArListe.notNull())
        includeAttachment(oversiktOverFeilutbetalinger, pesysData.oversiktOverFeilutbetalingPEDto, pesysData.oversiktOverFeilutbetalingPEDto.feilutbetalingPerArListe.isNull())
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk)
    }
}