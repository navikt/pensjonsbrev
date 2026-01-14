package no.nav.pensjon.brev.ufore.maler.feilutbetaling

import no.nav.pensjon.brev.ufore.maler.FeatureToggles
import no.nav.pensjon.brev.ufore.maler.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brev.ufore.maler.vedlegg.oversiktOverFeilutbetalinger
import no.nav.pensjon.brev.ufore.maler.vedlegg.oversiktOverFeilutbetalingerPaRadform
import no.nav.pensjon.brev.ufore.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.LocalizedFormatter.CurrencyFormat
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
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
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object VedtakFeilutbetaling : RedigerbarTemplate<VedtakFeilutbetalingUforeDto> {

    override val featureToggle = FeatureToggles.feilutbetaling.toggle

    override val kode = UT_VEDTAK_FEILUTBETALING
    override val kategori = TemplateDescription.Brevkategori.FEILUTBETALING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - tilbakekreving av feilutbetalt beløp",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        val feilutbetaltTotalBeloep = pesysData.feilutbetaltTotalBelop
        val resultatAvVurderingenForTotalBeloep = pesysData.resultatAvVurderingenForTotalBelop
        val sluttPeriodeForTilbakekreving = pesysData.sluttPeriodeForTilbakekreving
        val startPeriodeForTilbakekreving = pesysData.startPeriodeForTilbakekreving
        val sumTilInnkrevingTotalBeloep = pesysData.sumTilInnkrevingTotalBelop

        title {
            text(bokmal { + "Du må betale tilbake uføretrygd"})
        }
        outline {
            paragraph {
                text(bokmal { + "Vi viser til forhåndsvarselet vårt " + fritekst("dato") + "." })
            }
            paragraph {
                text(
                    bokmal { + "Du har fått for mye uføretrygd"
                            + " utbetalt fra " + startPeriodeForTilbakekreving.format() + " til " + sluttPeriodeForTilbakekreving.format()
                            + ". Dette er " + feilutbetaltTotalBeloep.format(CurrencyFormat) + " kroner inkludert skatt." },

                )
            }
            showIf(resultatAvVurderingenForTotalBeloep.isOneOf(TilbakekrevingResultat.FULL_TILBAKEKREV)) {
                paragraph {
                    text(bokmal { + "Vi har kommet fram til at du skal betale tilbake hele beløpet. Det vil si " +
                            sumTilInnkrevingTotalBeloep.format(CurrencyFormat) + " kroner etter at skatten er trukket fra." }
                    )
                }
            }
            showIf(resultatAvVurderingenForTotalBeloep.isOneOf(TilbakekrevingResultat.DELVIS_TILBAKEKREV)) {
                paragraph {
                    text(
                        bokmal { + "Vi har kommet fram til at du skal betale tilbake deler av beløpet. Det vil si " +
                                sumTilInnkrevingTotalBeloep.format(CurrencyFormat) + " kroner etter at skatten er trukket fra." },
                    )
                }
            }
            paragraph {
                text(bokmal { + "Beløpet som er trukket i skatt vil Nav få tilbake av Skatteetaten." }
                )
            }
            paragraph {
                text(bokmal { + "Vedtaket er gjort etter folketrygdloven § 22-15." })
            }
            paragraph {
                text(bokmal { + "I vedlegget " })
                namedReference(oversiktOverFeilutbetalinger)
                text(bokmal { + " finner du en oversikt over periodene med feilutbetalinger og beløpet du må betale tilbake." })
            }

            title1 {
                text(bokmal { + "Derfor må du betale tilbake" })
            }
            paragraph {
                text(bokmal { + fritekst("Her kan du skive inn hvorfor det foreligger en feilutbetaling") } )
            }

            title1 {
                text(bokmal { + "Tilbakekreving av feilutbetalt uføretrygd" })
            }
            paragraph {
                text(bokmal { + "Nav kan kreve tilbake feilutbetalt stønad, dersom mottakeren forsto eller burde ha forstått at de ikke hadde rett til utbetalingen. " +
                        "Dette går fram av folketrygdloven § 22-15 første ledd, første punktum." }
                )
            }
            paragraph {
                text(bokmal { + fritekst("Her skal du forklare hvilken informasjon bruker har fått") } )
            }
            paragraph {
                text(bokmal { + "Ved vurderingen av om og når mottakeren burde forstått at utbetalingen han eller hun har mottatt skyldtes en feil, " +
                        "er det avgjørende hvor åpenbar eller synlig feilen er. " +
                        "Sentralt i denne vurderingen er hvilken informasjon mottakeren har fått for eksempel i vedtak eller i samtaler med Navs organer, " +
                        "og kvaliteten på denne informasjonen."}
                )
            }
            paragraph {
                text(bokmal { + fritekst("Fritekst for din vurdering om bruker har gitt beskjed") } )
            }

            showIf(saksbehandlerValg.sivilstandEndret) {
                paragraph { text (bokmal {
                    + "I tidligere vedtak kommer det klart frem at uføretrygden din er beregnet ut fra sivilstatus som enslig. " +
                            "På bakgrunn av den informasjonen du har fått finner vi at du er tilstrekkelig orientert om regelverket, " +
                            "samt dine rettigheter og plikter som stønadsmottaker. " + fritekst("Her kan du skrive inn din begrunnelse")
                }) }
            }

            paragraph {
                text(bokmal {
                    + "Vilkårene for tilbakekreving etter folketrygdloven § 22-15 første ledd " + fritekst("Vurdering om hvorvidt § 22-15 første ledd er oppfylt")
                })
            }

            title1 {
                text(bokmal { + "Hel eller delvis tilbakekreving " })
            }
            paragraph {
                text(bokmal {
                    + "Av folketrygdloven § 22-15 fjerde ledd følger det at det skal settes frem krav om tilbakebetaling” med mindre særlige grunner taler mot det. "
                    + "Videre at kravet kan settes til en del av det feilutbetalte beløpet. Ordlyden” skal” viser at hovedregel er at det skal settes frem krav. " +
                            "Skylddeling er dermed å regne som et unntak. Videre er” særlige grunner” et sterkt uttrykk, og det skal dermed gode grunner til for å sette ned kravet."
                })
            }
            paragraph {text(bokmal {+ fritekst("Fritekst for å begrunne hvorfor vi ikke skylddeler")})}
            paragraph {
                text(bokmal { + "Vi finner ikke at særlige grunner tilsier at tilbakekreving ikke skal foretas. " })
            }
            paragraph {
                text(bokmal { + "Vedtaket er gjort etter folketrygdloven § 22-15 " + fritekst("Henvisning") })
            }
            paragraph {
                text(bokmal {+ "Feilutbetalt beløp kreves " + fritekst("tilbake | delvis tilbake | kreves ikke tilbake") + " jf. folketrygdloven § 22-15 fjerde ledd. "})
            }

            showIf(saksbehandlerValg.reduksjonForeldelse) {
                title1 {
                    text(bokmal { + "Reduksjon på grunn av foreldelse" })
                }
                paragraph { text (bokmal {
                    + "Etter foreldelsesloven § 2, jf. § 3 foreldes et pengekrav som hovedregel etter tre år. " +
                            "Fristen begynner å løpe fra det tidspunktet feilutbetalingen ble foretatt. " +
                            "Nav finner ikke grunnlag for å anvende unntaksregler i foreldelsesloven som forlenger den vanlige fristen. "
                }) }
                paragraph { text (bokmal {
                    + "For mye utbetalt i perioden " + fritekst("periode") + " er derfor bortfalt som foreldet og kreves ikke tilbakebetalt. "
                }) }
            }

            title1 {
                text(bokmal { + "Beregning" })
            }
            paragraph {
                text(bokmal { + "Feilutbetalt beløp i perioden " + fritekst("periode") + " utgjør brutto " + fritekst("beløp" + " kroner (inkludert skatt). ") +
                        "Det gjøres fradrag for skattetrekk med " + fritekst("beløp") + " kroner, og du skal tilbakebetale " + fritekst("beløp") + " kroner. " })
            }
            title1 {
                text(bokmal { + "Betydning for skatteoppgjøret" })
            }
            paragraph {
                text(bokmal { + "Vi rapporterer endringen til Skatteetaten. De vil korrigere skatteoppgjøret ditt ut fra denne endringen." })
            }
            paragraph {
                text(bokmal { + "Kravet om tilbakebetaling av for mye utbetalt ytelse er overført til Skatteetaten. Du vil motta en faktura på hele beløpet som kreves tilbake. Spørsmål om nedbetalingsplan og andre forhold knyttet til innbetaling rettes til Skatteetaten. " })
            }

            includePhrase(Felles.RettTilAKlageKort)
            includePhrase(Felles.RettTilInnsynRefVedlegg)
            includePhrase(Felles.HarDuSporsmal)
        }

        includeAttachment(oversiktOverFeilutbetalingerPaRadform, pesysData.oversiktOverFeilutbetalingPEDto, pesysData.oversiktOverFeilutbetalingPEDto.feilutbetalingPerArListe.notNull())
        includeAttachment(oversiktOverFeilutbetalinger, pesysData.oversiktOverFeilutbetalingPEDto, pesysData.oversiktOverFeilutbetalingPEDto.feilutbetalingPerArListe.isNull())
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk)
    }
}