package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.maler.Brevkategori
import no.nav.pensjon.brev.alder.maler.felles.Constants.NAV_URL
import no.nav.pensjon.brev.alder.maler.felles.HarDuSpoersmaal
import no.nav.pensjon.brev.alder.maler.felles.KronerText
import no.nav.pensjon.brev.alder.maler.vedlegg.vedleggHvordanPensjonenErBeregnetAfpOffentlig
import no.nav.pensjon.brev.alder.maler.vedlegg.vedleggOpplysningerOmBeregningenAfp
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.Sakstype
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDto
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDto.YtelsesKomponent
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDto.SivilstandGruppe
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDtoSelectors.pesysData
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDtoSelectors.PesysDataSelectors.afpPensjonsgrad
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDtoSelectors.PesysDataSelectors.beregning
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDtoSelectors.PesysDataSelectors.beregningVirkDatoFom
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDtoSelectors.PesysDataSelectors.etterbetaling
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDtoSelectors.PesysDataSelectors.flerePerioder
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDtoSelectors.PesysDataSelectors.framtidigArligInntekt
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDtoSelectors.PesysDataSelectors.grunnbeloep
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDtoSelectors.PesysDataSelectors.opplysningerOmBeregningen
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDtoSelectors.PesysDataSelectors.hvordanPensjonenErBeregnet
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDtoSelectors.PesysDataSelectors.kravMottattDato
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDtoSelectors.PesysDataSelectors.sivilstand
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDtoSelectors.PesysDataSelectors.tidligereArbeidsinntekt
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDtoSelectors.PesysDataSelectors.virkningFom
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDtoSelectors.BeregningSelectors.afpTillegg
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDtoSelectors.BeregningSelectors.barnetilleggFelles
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDtoSelectors.BeregningSelectors.barnetilleggSerkull
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDtoSelectors.BeregningSelectors.brutto
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDtoSelectors.BeregningSelectors.ektefelletillegg
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDtoSelectors.BeregningSelectors.familietillegg
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDtoSelectors.BeregningSelectors.fasteUtgifterInstitusjon
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDtoSelectors.BeregningSelectors.grunnpensjon
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDtoSelectors.BeregningSelectors.minstenivaatilleggIndividuelt
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDtoSelectors.BeregningSelectors.netto
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDtoSelectors.BeregningSelectors.saertillegg
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDtoSelectors.BeregningSelectors.tilleggspensjon
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDtoSelectors.EktefelletilleggSelectors.brutto as ektefelletilleggBrutto
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDtoSelectors.EktefelletilleggSelectors.inntektBruktIAvkortning
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDtoSelectors.EktefelletilleggSelectors.netto as ektefelletilleggNetto
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDtoSelectors.YtelsesKomponentSelectors.brutto as ytelsesKomponentBrutto
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpOffentligSektorDtoSelectors.YtelsesKomponentSelectors.netto as ytelsesKomponentNetto
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.TemplateDescription.ISakstype
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.TableScope
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.lessThanOrEqual
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.isNull
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.expression.notNull
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
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
            ifNotNull(pesysData.beregning.ektefelletillegg) { et ->
                showIf(et.ektefelletilleggNetto.equalTo(Kroner(0))) {
                    paragraph {
                        text(
                            bokmal { +"Ektefelletillegget vil ikke komme til utbetaling fordi din samlede inntekt er for høy." },
                            nynorsk { +"Ektefelletillegget vil ikkje komme til utbetaling fordi den samla inntekta di er for høg." },
                        )
                    }
                }
            }

            // Beregningstabell — to varianter avhengig av om det er fradrag for inntekt.
            paragraph {
                text(
                    bokmal {
                        +"Den månedlige pensjonen fra " + pesysData.beregningVirkDatoFom.format() +
                            " er slik. Folketrygdens grunnbeløp (G) benyttet i beregningen er " +
                            pesysData.grunnbeloep.format() +
                            ". Framtidig årlig inntekt benyttet i beregningen er " +
                            pesysData.framtidigArligInntekt.format() + "."
                    },
                    nynorsk {
                        +"Den månadlege pensjonen frå " + pesysData.beregningVirkDatoFom.format() +
                            " er slik. Grunnbeløpet (G) i folketrygda nytta i berekninga er " +
                            pesysData.grunnbeloep.format() +
                            ". Forventa årleg inntekt nytta i berekninga er " +
                            pesysData.framtidigArligInntekt.format() + "."
                    },
                )
            }

            showIf(pesysData.beregning.brutto.notEqualTo(pesysData.beregning.netto)) {
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
                        cell { includePhrase(KronerText(pesysData.beregning.grunnpensjon.ytelsesKomponentBrutto)) }
                        cell { includePhrase(KronerText(pesysData.beregning.grunnpensjon.ytelsesKomponentNetto)) }
                    }
                    komponentRow(pesysData.beregning.tilleggspensjon, "Tilleggspensjon", "Tilleggspensjon")
                    komponentRow(pesysData.beregning.saertillegg, "Særtillegg", "Særtillegg")
                    komponentRow(pesysData.beregning.minstenivaatilleggIndividuelt, "Minstenivåtillegg individuelt", "Minstenivåtillegg individuelt")
                    komponentRow(pesysData.beregning.afpTillegg, "AFP-tillegg", "AFP-tillegg")
                    ifNotNull(pesysData.beregning.ektefelletillegg) { et ->
                        row {
                            cell { text(bokmal { +"Ektefelletillegg" }, nynorsk { +"Ektefelletillegg" }) }
                            cell { includePhrase(KronerText(et.ektefelletilleggBrutto)) }
                            cell { includePhrase(KronerText(et.ektefelletilleggNetto)) }
                        }
                    }
                    komponentRow(pesysData.beregning.fasteUtgifterInstitusjon, "Faste utgifter ved institusjonsopphold", "Faste utgifter ved institusjonsopphald")
                    komponentRow(pesysData.beregning.familietillegg, "Familietillegg", "Familietillegg")
                    row {
                        cell { text(bokmal { +"Sum pensjon før skatt" }, nynorsk { +"Sum pensjon før skatt" }, BOLD) }
                        cell { includePhrase(KronerText(pesysData.beregning.brutto, BOLD)) }
                        cell { includePhrase(KronerText(pesysData.beregning.netto, BOLD)) }
                    }
                }
                }
            }

            // Variant uten brutto-kolonne (brutto = netto, ingen inntektsavkortning).
            showIf(pesysData.beregning.brutto.equalTo(pesysData.beregning.netto)) {
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
                        cell { includePhrase(KronerText(pesysData.beregning.grunnpensjon.ytelsesKomponentNetto)) }
                    }
                    komponentNettoRow(pesysData.beregning.tilleggspensjon, "Tilleggspensjon", "Tilleggspensjon")
                    komponentNettoRow(pesysData.beregning.saertillegg, "Særtillegg", "Særtillegg")
                    komponentNettoRow(pesysData.beregning.minstenivaatilleggIndividuelt, "Minstenivåtillegg individuelt", "Minstenivåtillegg individuelt")
                    komponentNettoRow(pesysData.beregning.afpTillegg, "AFP-tillegg", "AFP-tillegg")
                    ifNotNull(pesysData.beregning.ektefelletillegg) { et ->
                        row {
                            cell { text(bokmal { +"Ektefelletillegg" }, nynorsk { +"Ektefelletillegg" }) }
                            cell { includePhrase(KronerText(et.ektefelletilleggNetto)) }
                        }
                    }
                    komponentNettoRow(pesysData.beregning.fasteUtgifterInstitusjon, "Faste utgifter ved institusjonsopphold", "Faste utgifter ved institusjonsopphald")
                    komponentNettoRow(pesysData.beregning.familietillegg, "Familietillegg", "Familietillegg")
                    row {
                        cell { text(bokmal { +"Sum pensjon før skatt" }, nynorsk { +"Sum pensjon før skatt" }, BOLD) }
                        cell { includePhrase(KronerText(pesysData.beregning.netto, BOLD)) }
                    }
                }
                }
            }

            // Innholdsliste.
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
                        pesysData.beregning.ektefelletillegg.notNull()
                            .and(pesysData.beregning.barnetilleggSerkull.not().or(pesysData.beregning.barnetilleggFelles.not()))
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
                    item {
                        text(
                            bokmal { +"hvordan pensjonen din er beregnet" },
                            nynorsk { +"korleis pensjonen din er berekna" },
                        )
                    }
                    showIf(pesysData.flerePerioder) {
                        item {
                            text(
                                bokmal { +"oversikt over pensjonens størrelse fra " + pesysData.virkningFom.format() },
                                nynorsk { +"kor stor pensjonen er (oversikt) frå " + pesysData.virkningFom.format() },
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

            // Begrunnelse for vedtaket.
            title2 {
                text(
                    bokmal { +"Begrunnelse for vedtaket" },
                    nynorsk { +"Grunngiving for vedtaket" },
                )
            }
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
            title2 {
                text(
                    bokmal { +"Utbetaling og skatt" },
                    nynorsk { +"Utbetaling og skatt" },
                )
            }
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
            paragraph {
                text(
                    bokmal {
                        +"Skattereglene for pensjonsinntekt er ikke de samme som for arbeidsinntekt. Derfor bør du vurdere å søke om " +
                            "nytt skattekort når du starter uttak av pensjon. Endring av skattekort gjøres enklest på Skatteetatens " +
                            "nettsider www.skatteetaten.no. Har du spørsmål kan du ringe Skatteetaten på telefon 800 80 000. Du trenger " +
                            "ikke levere skattekortet til Nav fordi skatteopplysningene dine sendes elektronisk fra Skatteetaten."
                    },
                    nynorsk {
                        +"Skattereglane for pensjonsinntekt er ikkje dei same som for arbeidsinntekt. Derfor bør du vurdere å søkje om " +
                            "nytt skattekort når du begynner å ta ut pensjon. Skattekortendringar gjer du enklast på nettsidene til " +
                            "skatteetaten, www.skatteetaten.no. Har du spørsmål, kan du ringje skatteetaten på tlf. 800 80 000. Du treng " +
                            "ikkje levere skattekortet til Nav ettersom skatteopplysningane dine blir sende elektronisk frå skatteetaten."
                    },
                )
            }

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
                paragraph {
                    text(
                        bokmal {
                            +"Ved etterbetalinger som gjelder tidligere år, vil Nav trekke 30 prosent som en standardsats. Dersom du " +
                                "krever at skatteetaten forut for utbetalingen reberegner skatten for de tidligere årene, så må du gi " +
                                "beskjed om dette til Nav innen sju dager etter dato for dette brevet."
                        },
                        nynorsk {
                            +"Ved etterbetalingar som gjeld tidlegare år, vil Nav trekkje 30 prosent som ein standardsats. Dersom du " +
                                "krev at skatteetaten før utbetalinga bereknar om att skatten for dei tidlegare åra, må du gi beskjed " +
                                "om det til Nav innan sju dagar etter datoen for dette brevet."
                        },
                    )
                }
            }

            // Hvordan AFP beregnes.
            title2 {
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
                        +"Din tidligere arbeidsinntekt er fastsatt til " + pesysData.tidligereArbeidsinntekt.format() +
                            ". Den tidligere arbeidsinntekten er et beregnet beløp, basert på de tre beste inntektsårene dine " +
                            "av de fem siste årene, forut for året før du går ut i pensjon. Denne beregnede inntekten blir justert i " +
                            "forhold til økninger i folketrygdens grunnbeløp (G)."
                    },
                    nynorsk {
                        +"Den tidlegare arbeidsinntekta di er fastsett til " + pesysData.tidligereArbeidsinntekt.format() +
                            ". Den tidlegare arbeidsinntekta er eit berekna beløp basert på dei tre beste inntektsåra dine av " +
                            "dei fem siste åra før året før du går ut i pensjon. Denne berekna inntekta blir justert i forhold til " +
                            "auke i grunnbeløpet (G) i folketrygda."
                    },
                )
            }
            showIf(pesysData.framtidigArligInntekt.lessThanOrEqual(15_000)) {
                paragraph {
                    text(
                        bokmal {
                            +"Du har oppgitt at din antatte framtidige arbeidsinntekt ikke vil overstige toleransebeløpet på 15 000 " +
                                "kroner per år. AFP er derfor beregnet uten fradrag for arbeidsinntekt. I den framtidige arbeidsinntekten " +
                                "skal både lønnsinntekt, feriepenger, fordel av forsikringspremier betalt av arbeidsgiver, personinntekt " +
                                "fra næring, honorar og lignende regnes med."
                        },
                        nynorsk {
                            +"Du har oppgitt at den forventa framtidige arbeidsinntekta di ikkje kjem til å overstige toleransebeløpet " +
                                "på 15 000 kroner per år. AFP er derfor berekna utan frådrag for arbeidsinntekt. I den framtidige " +
                                "arbeidsinntekta skal både lønnsinntekt, feriepengar, fordel av forsikringspremiar betalte av " +
                                "arbeidsgivar, personinntekt frå næring, honorar og liknande reknast med."
                        },
                    )
                }
            }
            showIf(pesysData.framtidigArligInntekt.greaterThan(15_000)) {
                paragraph {
                    text(
                        bokmal {
                            +"Pensjonen din er redusert etter antatt årlig framtidig arbeidsinntekt på " +
                                pesysData.framtidigArligInntekt.format() + "."
                        },
                        nynorsk {
                            +"Pensjonen din er redusert etter ei forventa årleg framtidig arbeidsinntekt på " +
                                pesysData.framtidigArligInntekt.format() + "."
                        },
                    )
                }
            }

            // Ektefelletillegg og inntekt.
            ifNotNull(pesysData.beregning.ektefelletillegg) { et ->
                title2 {
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
                                et.inntektBruktIAvkortning.format() +
                                ". I denne inntekten tar vi hensyn til pensjonen din, antatt framtidig arbeidsinntekt, og " +
                                "pensjon/ytelser fra andre. Dette innebærer at alle inntektsendringer kan få betydning for størrelsen " +
                                "på ektefelletillegget."
                        },
                        nynorsk {
                            +"Ektefelletillegget blir inntektsprøvd mot den samla inntekta di på " +
                                et.inntektBruktIAvkortning.format() +
                                ". I denne inntekta tek vi omsyn til pensjonen din, forventa framtidig arbeidsinntekt og " +
                                "pensjon/ytingar frå andre. Det inneber at alle inntektsendringar kan vere avgjerande for storleiken " +
                                "på ektefelletillegget."
                        },
                    )
                }
            }

            // Dine plikter.
            title2 {
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
            showIf(pesysData.beregning.ektefelletillegg.isNull()) {
                paragraph {
                    text(
                        bokmal {
                            +"Det vil si når arbeidsinntekten din endrer seg med mer enn 15 000 kroner pr. år i forhold til den " +
                                "inntekten som pensjonen er beregnet etter."
                        },
                        nynorsk {
                            +"Det vil seie når arbeidsinntekta di endrar seg med meir enn 15 000 kroner per år i forhold til den " +
                                "inntekta som pensjonen er berekna etter."
                        },
                    )
                }
            }
            showIf(pesysData.beregning.ektefelletillegg.notNull()) {
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
                    showIf(pesysData.sivilstand.equalTo(SivilstandGruppe.ENSLIG)) {
                        item {
                            text(
                                bokmal { +"du gifter deg, inngår partnerskap eller samboerskap" },
                                nynorsk { +"du gifter deg eller inngår partnarskap eller sambuarskap" },
                            )
                        }
                    }
                    showIf(pesysData.sivilstand.equalTo(SivilstandGruppe.BOR_MED_EKTEFELLE)) {
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
                    showIf(pesysData.sivilstand.equalTo(SivilstandGruppe.SEPARERT_FRA_EKTEFELLE)) {
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
                    showIf(pesysData.sivilstand.equalTo(SivilstandGruppe.SEPARERT_FRA_PARTNER)) {
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
                    showIf(pesysData.sivilstand.equalTo(SivilstandGruppe.BOR_MED_PARTNER)) {
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
                    showIf(pesysData.sivilstand.equalTo(SivilstandGruppe.BOR_MED_SAMBOER)) {
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

            // Dine rettigheter.
            title2 {
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
            paragraph {
                text(
                    bokmal {
                        +"Hvis du mener at vedtaket ikke er i samsvar med det du har søkt om, kan du klage på vedtaket. Fristen for å " +
                            "klage er seks uker fra du mottar dette brevet."
                    },
                    nynorsk {
                        +"Dersom du meiner at vedtaket ikkje er i samsvar med det du har søkt om, kan du klage. Fristen for å klage er " +
                            "seks veker frå du får dette brevet."
                    },
                )
            }

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
    }
}

private fun TableScope<LangBokmalNynorsk, InnvilgelseAvAfpOffentligSektorDto>.komponentRow(
    ytelsesKomponent: Expression<YtelsesKomponent?>,
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

private fun TableScope<LangBokmalNynorsk, InnvilgelseAvAfpOffentligSektorDto>.komponentNettoRow(
    ytelsesKomponent: Expression<YtelsesKomponent?>,
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
