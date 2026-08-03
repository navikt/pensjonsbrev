package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.maler.Brevkategori
import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpOffentligSektorInnhold
import no.nav.pensjon.brev.alder.maler.brev.FeatureToggles
import no.nav.pensjon.brev.alder.maler.felles.HarDuSpoersmaal
import no.nav.pensjon.brev.alder.maler.felles.Vedtak
import no.nav.pensjon.brev.alder.maler.vedlegg.vedleggFolketrygden
import no.nav.pensjon.brev.alder.maler.vedlegg.vedleggInformasjonOmAfp
import no.nav.pensjon.brev.alder.maler.vedlegg.vedleggOpplysningerOmBeregningenAfp
import no.nav.pensjon.brev.alder.maler.vedlegg.vedleggOversiktOverPensjonenAfp
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.Sakstype
import no.nav.pensjon.brev.alder.model.afp.selectors.afpOffentligSektor.beregning.*
import no.nav.pensjon.brev.alder.model.afp.selectors.afpOffentligSektor.ektefelletillegg.*
import no.nav.pensjon.brev.alder.model.afp.VedtakEndringAfpOffentligSektorDto
import no.nav.pensjon.brev.alder.model.afp.selectors.vedtakEndringAfpOffentligSektorDto.pesysData.*
import no.nav.pensjon.brev.alder.model.afp.selectors.vedtakEndringAfpOffentligSektorDto.*
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.TemplateDescription.ISakstype
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.notNull
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

/**
 * Vedtak — melding om endring av avtalefestet pensjon (AFP) i offentlig sektor.
 *
 * Konvertert fra Exstream-malen `PE_AF_04_020`. Brevet er strukturelt svært likt
 * innvilgelsesvarianten [InnvilgelseAvAfpOffentligSektor] (PE_AF_04_001), men
 * mangler vedlegget «Hvordan pensjonen er beregnet» og introduksjonen er en
 * endringsmelding i stedet for en innvilgelse.
 *
 * Endrings-spesifikke fritekst-felter («økes/reduseres», «årsak til endring»,
 * «tidspunkt for neste utbetaling») er kommentert ut med TODO og må re-evalueres
 * når brevet tas i bruk i Skribenten.
 */
@TemplateModelHelpers
object VedtakEndringAfpOffentligSektor : RedigerbarTemplate<VedtakEndringAfpOffentligSektorDto> {

    override val kode = Aldersbrevkoder.Redigerbar.PE_AF_VEDTAK_ENDRING_OFFENTLIG

    override val featureToggle = FeatureToggles.vedtakEndringAfpOffentligSektor.toggle

    override val kategori = Brevkategori.VEDTAK_ENDRING_OG_REVURDERING

    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.VEDTAK

    override val sakstyper: Set<ISakstype> = setOf(Sakstype.AFP)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - endring av AFP (offentlig sektor)",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                bokmal { +"Avtalefestet pensjon (AFP) - melding om endring" },
                nynorsk { +"Avtalefesta pensjon (AFP) - melding om endring" },
            )
        }

        outline {
            // Innledning. «Økes/reduseres» og «årsak til endring» kommer fra
            // Exstream-fritekst og må fylles inn av saksbehandler i Skribenten.
            paragraph {
                text(
                    bokmal {
                        +"Nav har vedtatt at din AFP skal " + fritekst("økes/reduseres") +
                            " fra " + pesysData.virkningFom.format() + " fordi " +
                            fritekst("årsak til endring") + ". Du får AFP etter pensjonsgrad på " +
                            pesysData.afpPensjonsgrad.format() + " prosent fra " +
                            pesysData.virkningFom.format() + "."
                    },
                    nynorsk {
                        +"Nav har vedteke at din AFP skal " + fritekst("aukast/reduserast") +
                            " frå " + pesysData.virkningFom.format() + " fordi " +
                            fritekst("årsak til endringa") + ". Du får AFP etter ein pensjonsgrad på " +
                            pesysData.afpPensjonsgrad.format() + " prosent frå " +
                            pesysData.virkningFom.format() + "."
                    },
                )
            }

            // Ektefelletillegg innvilget men netto = 0 -> ikke til utbetaling.
            includePhrase(AfpOffentligSektorInnhold.EktefelletilleggIkkeUtbetaltOpplysning(pesysData.beregning.ektefelletillegg))

            // Beregningstabell — to varianter avhengig av om det er fradrag for inntekt.
            includePhrase(
                AfpOffentligSektorInnhold.BeregningsTabellAfpOffentlig(
                    beregning = pesysData.beregning,
                    beregningVirkDatoFom = pesysData.beregningVirkDatoFom,
                    grunnbeloep = pesysData.grunnbeloep,
                    framtidigArligInntekt = pesysData.framtidigArligInntekt,
                )
            )

            // Innholdsliste.
            includePhrase(
                AfpOffentligSektorInnhold.Innholdsliste(
                    beregning = pesysData.beregning,
                    flerePerioder = pesysData.flerePerioder,
                    virkningFom = pesysData.virkningFom,
                    inkluderHvordanPensjonenItem = false,
                )
            )

            includePhrase(Vedtak.BegrunnelseOverskrift)

            paragraph {
                text(
                    bokmal { +fritekst("Årsak til endring") },
                    nynorsk { +fritekst("Årsak til endringa") },
                )
            }
            showIf(pesysData.beregning.ektefelletillegg.notNull()) {
                includePhrase(AfpOffentligSektorInnhold.EktefelletilleggOpplysning)
            }

            // Utbetaling og skatt.
            includePhrase(AfpOffentligSektorInnhold.UtbetalingOgSkattTittel)
            // «Tidspunkt for neste utbetaling» fylles inn av saksbehandler.
            paragraph {
                text(
                    bokmal { +"AFP blir utbetalt med nytt beløp fra " + fritekst("tidspunkt for neste utbetaling") + "." },
                    nynorsk { +"AFP blir utbetalt med nytt beløp frå " + fritekst("tidspunkt for neste utbetaling") + "." },
                )
            }
            includePhrase(AfpOffentligSektorInnhold.UtbetalingsdagerOpplysning)

            // Etterbetaling / feilutbetaling.
            showIf(pesysData.etterbetaling) {
                paragraph {
                    text(
                        bokmal {
                            +"Du får etterbetalt pensjon fra " + pesysData.virkningFom.format() +
                                ". Etterbetalingen vil vanligvis bli utbetalt i løpet av sju virkedager. Det kan bli beregnet " +
                                "fradrag i etterbetalingen for skatt, ytelser du har mottatt fra Nav, eller andre som for eksempel " +
                                "tjenestepensjonsordninger. Hvis skattekontor eller andre ordninger har slike krav i etterbetalingen " +
                                "kan utbetalingen bli noe forsinket. Slike fradrag vil gå fram av utbetalingsmeldingen."
                        },
                        nynorsk {
                            +"Du får etterbetalt pensjon frå " + pesysData.virkningFom.format() +
                                ". Etterbetalinga blir vanlegvis utbetalt i løpet av sju arbeidsdagar. Det kan bli berekna frådrag i " +
                                "etterbetalinga for skatt eller ytingar du har fått frå Nav, eller andre, som til dømes " +
                                "tenestepensjonsordningar. Dersom likningskontor eller andre ordningar har slike krav i etterbetalinga, " +
                                "kan utbetalinga bli noko forseinka. Slike frådrag vil gå fram av utbetalingsmeldinga."
                        },
                    )
                }
                includePhrase(AfpOffentligSektorInnhold.EtterbetalingStandardSats)
            }

            // Hvordan AFP beregnes.
            includePhrase(
                AfpOffentligSektorInnhold.HvordanAfpBeregnes(
                    tidligereArbeidsinntekt = pesysData.tidligereArbeidsinntekt,
                    framtidigArligInntekt = pesysData.framtidigArligInntekt,
                    toleranseBeloep = pesysData.toleranseBeloep,
                ),
            )

            // Ektefelletillegg og inntekt.
            ifNotNull(pesysData.beregning.ektefelletillegg) { et ->
                includePhrase(AfpOffentligSektorInnhold.EktefelletilleggOgInntekt(et.inntektBruktIAvkortning))
            }

            // Dine plikter.
            includePhrase(
                AfpOffentligSektorInnhold.DinePlikterAfpOffentlig(
                    sivilstand = pesysData.sivilstand,
                    harEktefelletillegg = pesysData.beregning.ektefelletillegg.notNull(),
                    toleranseBeloep = pesysData.toleranseBeloep,
                ),
            )

            // Dine rettigheter.
            includePhrase(AfpOffentligSektorInnhold.DineRettigheterInnsyn)
            paragraph {
                text(
                    bokmal {
                        +"Hvis du mener at vedtaket ikke er riktig, kan du klage på vedtaket. Fristen for å klage er seks uker " +
                            "fra du mottar dette brevet."
                    },
                    nynorsk {
                        +"Dersom du meiner at vedtaket ikkje er rett, kan du klage på det. Fristen for å klage er seks veker frå " +
                            "du får dette brevet."
                    },
                )
            }

            includePhrase(HarDuSpoersmaal.alder)
        }
        includeAttachment(
            vedleggOpplysningerOmBeregningenAfp,
            pesysData.opplysningerOmBeregningen,
        )
        includeAttachmentIfNotNull(
            vedleggOversiktOverPensjonenAfp,
            pesysData.oversiktOverPensjonen,
        )
        includeAttachment(vedleggInformasjonOmAfp)
        includeAttachment(vedleggFolketrygden)
    }
}
