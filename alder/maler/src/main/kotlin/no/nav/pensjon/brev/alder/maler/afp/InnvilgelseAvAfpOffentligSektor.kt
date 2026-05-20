package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.maler.Brevkategori
import no.nav.pensjon.brev.alder.maler.brev.FeatureToggles
import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpOffentligSektorInnhold
import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpPrivatFraser
import no.nav.pensjon.brev.alder.maler.felles.HarDuSpoersmaal
import no.nav.pensjon.brev.alder.maler.felles.Vedtak
import no.nav.pensjon.brev.alder.maler.vedlegg.*
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.Sakstype
import no.nav.pensjon.brev.alder.model.afp.AfpOffentligSektorSelectors.BeregningSelectors.ektefelletillegg
import no.nav.pensjon.brev.alder.model.afp.AfpOffentligSektorSelectors.EktefelletilleggSelectors.inntektBruktIAvkortning
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDto
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDtoSelectors.PesysDataSelectors.afpPensjonsgrad
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDtoSelectors.PesysDataSelectors.beregning
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDtoSelectors.PesysDataSelectors.beregningVirkDatoFom
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDtoSelectors.PesysDataSelectors.etterbetaling
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDtoSelectors.PesysDataSelectors.flerePerioder
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDtoSelectors.PesysDataSelectors.framtidigArligInntekt
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDtoSelectors.PesysDataSelectors.grunnbeloep
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDtoSelectors.PesysDataSelectors.hvordanPensjonenErBeregnet
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDtoSelectors.PesysDataSelectors.kravMottattDato
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDtoSelectors.PesysDataSelectors.opplysningerOmBeregningen
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDtoSelectors.PesysDataSelectors.oversiktOverPensjonen
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDtoSelectors.PesysDataSelectors.sivilstand
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDtoSelectors.PesysDataSelectors.tidligereArbeidsinntekt
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDtoSelectors.PesysDataSelectors.virkningFom
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.TemplateDescription.ISakstype
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.notNull
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

/**
 * Vedtak — innvilgelse av avtalefestet pensjon (AFP) i offentlig sektor (gammel AFP).
 *
 * Konvertert fra Exstream-malen `PE_AF_04_001`. Privat-sektor varianten av
 * AFP (`PE_AF_04_111` / `PE_AF_04_115`) ligger i [InnvilgelseAvAfp] /
 * [InnvilgelseAvAfpAuto] og bruker felles innhold [InnvilgelseAvAfpInnhold].
 *
 * Brevet er redigerbart og styres av saksbehandler i Skribenten.
 */
@TemplateModelHelpers
object InnvilgelseAvAfpOffentligSektor : RedigerbarTemplate<InnvilgelseAvAfpOffentligSektorDto> {

    override val kode = Aldersbrevkoder.Redigerbar.PE_AF_INNVILGELSE_OFFENTLIG

    override val featureToggle = FeatureToggles.innvilgelseAvAfpOffentligSektor.toggle

    override val kategori = Brevkategori.FOERSTEGANGSBEHANDLING

    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.VEDTAK

    override val sakstyper: Set<ISakstype> = setOf(Sakstype.AFP)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - innvilgelse av AFP (offentlig sektor)",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                bokmal { +"Avtalefestet pensjon - melding om vedtak" },
                nynorsk { +"Avtalefesta pensjon - melding om vedtak" },
            )
        }

        outline {
            paragraph {
                text(
                    bokmal {
                        +"Nav har innvilget søknaden din av " + pesysData.kravMottattDato.format() +
                            " om avtalefestet pensjon (AFP). Du får AFP fra " + pesysData.virkningFom.format() +
                            " etter pensjonsgrad på " + pesysData.afpPensjonsgrad.format() + " prosent."
                    },
                    nynorsk {
                        +"Nav har godkjent søknaden din av " + pesysData.kravMottattDato.format() +
                            " om avtalefesta pensjon (AFP). Du får AFP frå " + pesysData.virkningFom.format() +
                            " etter ein pensjonsgrad på " + pesysData.afpPensjonsgrad.format() + " prosent."
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
                    inkluderHvordanPensjonenItem = true,
                )
            )

            includePhrase(Vedtak.BegrunnelseOverskrift)

            paragraph {
                text(
                    bokmal { +"AFP gis etter bestemmelsene i lov om avtalefestet pensjon for medlemmer av Statens pensjonskasse med tilhørende forskrifter." },
                    nynorsk { +"AFP vert gitt etter reglane i lov om avtalefestet pensjon for medlemmer av Statens pensjonskasse med tilhøyrande forskrifter." },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Pensjonen er beregnet etter bestemmelsene i folketrygdloven kapittel 3. I tillegg mottar du et AFP-tillegg. " +
                            "I følge lov om avtalefestet pensjon for medlemmer av Statens pensjonskasse paragraf 3 bokstav c, " +
                            "skal full AFP (inkludert AFP-tillegg) ikke overstige 70 prosent av tidligere arbeidsinntekt."
                    },
                    nynorsk {
                        +"Pensjonen er rekna etter reglane i folketrygdlova kapittel 3. Du får også eit AFP-tillegg. " +
                            "Ifølgje lov om avtalefestet pensjon for medlemmer av Statens pensjonskasse paragraf 3 bokstav c, " +
                            "skal samla AFP (inkludert AFP-tillegg) ikkje overstiga 70 prosent av tidlegare arbeidsinntekt."
                    },
                )
            }
            showIf(pesysData.beregning.ektefelletillegg.notNull()) {
                paragraph {
                    // TODO denne frasen er gaanske lik en annen, men mindre tekstlige forskjeller. Kunne de vært samstillt?
                    text(
                        bokmal {
                            +"Ektefelletillegget gis som et tillegg i AFP til den som forsørger ektefelle, partner eller " +
                                "samboer over 60 år. I følge folketrygdloven paragraf 3-24, kan du få ektefelletillegg dersom " +
                                "den du forsørger ikke har egen pensjon eller uføretrygd, og ikke har egen inntekt som overstiger " +
                                "folketrygdens grunnbeløp. Som egen inntekt inngår også kapitalinntekt. Ektefelletillegget vil falle " +
                                "bort når den som blir forsørget får rett til egen hel alderspensjon. Dette gjelder selv om pensjonen " +
                                "ikke blir tatt ut."
                        },
                        nynorsk {
                            +"Ektefelletillegg vert gitt som eit tillegg i AFP til den som forsørgjer ektefelle, partnar eller " +
                                "sambuar over 60 år. I fylgje folketrygdlova paragraf 3-24, kan du få ektefelletillegg dersom den du " +
                                "forsørgjer ikkje har eigen pensjon eller uføretrygd, og ikkje har eiga inntekt som er større enn " +
                                "grunnbeløpet i folketrygda. Som eiga inntekt skal det også reknast med kapitalinntekt. Ein vil ikkje " +
                                "lenger få ektefelletillegget når den som vert forsørgd får rett til eigen heil alderspensjon. Dette " +
                                "gjeld sjølv om pensjonen ikkje vert nytta."
                        },
                    )
                }
            }

            // Utbetaling og skatt.
            includePhrase(AfpOffentligSektorInnhold.UtbetalingOgSkattTittel)
            includePhrase(AfpOffentligSektorInnhold.UtbetalingsdagerOpplysning)
            includePhrase(AfpOffentligSektorInnhold.SkattekortPensjonOpplysning)

            // Etterbetaling.
            showIf(pesysData.etterbetaling) {
                paragraph {
                    text(
                        bokmal {
                            +"Du får etterbetalt pensjon fra " + pesysData.virkningFom.format() +
                                ". Etterbetalingen vil vanligvis bli utbetalt i løpet av sju virkedager. Det kan bli beregnet fradrag " +
                                "i etterbetalingen for skatt, ytelser du har mottatt fra Nav eller andre, som for eksempel " +
                                "tjenestepensjonsordninger. Hvis skattekontor eller andre ordninger har krav i etterbetalingen kan denne " +
                                "bli forsinket. Fradrag i etterbetalingen vil gå fram av utbetalingsmeldingen."
                        },
                        nynorsk {
                            +"Du får etterbetalt pensjon frå " + pesysData.virkningFom.format() +
                                ". Etterbetalinga blir vanlegvis utbetalt i løpet av sju arbeidsdagar. Det kan bli berekna frådrag i " +
                                "etterbetalinga for skatt eller ytingar du har fått frå Nav eller andre, som til dømes " +
                                "tenestepensjonsordningar. Dersom likningskontor eller andre ordningar har krav i etterbetalinga, kan ho " +
                                "bli forseinka. Frådrag i etterbetalinga går fram av utbetalingsmeldinga."
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
                ),
            )

            // Dine rettigheter.
            includePhrase(AfpOffentligSektorInnhold.DineRettigheterInnsyn)
            includePhrase(AfpPrivatFraser.KlagerettFolketrygdloven2112)
            includePhrase(HarDuSpoersmaal.alder)
        }
        includeAttachment(
            vedleggHvordanPensjonenErBeregnetAfpOffentlig,
            pesysData.hvordanPensjonenErBeregnet,
        )
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
