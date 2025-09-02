package no.nav.pensjon.brev.maler.alder.endring.sivilstand

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonGarantitilleggDto
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonGarantitilleggDtoSelectors.AlderspensjonVedVirkSelectors.innvilgetFor67
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonGarantitilleggDtoSelectors.AlderspensjonVedVirkSelectors.ufoereKombinertMedAlder
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonGarantitilleggDtoSelectors.AlderspensjonVedVirkSelectors.uttaksgrad
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonGarantitilleggDtoSelectors.BeregnetPensjonPerManedVedVirkSelectors.garantitillegg_safe
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonGarantitilleggDtoSelectors.BeregnetPensjonPerManedVedVirkSelectors.totalPensjon
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonGarantitilleggDtoSelectors.PesysDataSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonGarantitilleggDtoSelectors.PesysDataSelectors.beregnetPensjonPerManedVedVirk
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonGarantitilleggDtoSelectors.PesysDataSelectors.kravVirkDatoFom
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonGarantitilleggDtoSelectors.PesysDataSelectors.maanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonGarantitilleggDtoSelectors.PesysDataSelectors.maanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonGarantitilleggDtoSelectors.PesysDataSelectors.orienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonGarantitilleggDtoSelectors.pesysData
import no.nav.pensjon.brev.maler.alder.endring.sivilstand.fraser.DuFaarAP
import no.nav.pensjon.brev.maler.fraser.alderspensjon.ArbeidsinntektOgAlderspensjon
import no.nav.pensjon.brev.maler.fraser.alderspensjon.InformasjonOmAlderspensjon
import no.nav.pensjon.brev.maler.fraser.alderspensjon.MeldeFraOmEndringer
import no.nav.pensjon.brev.maler.fraser.alderspensjon.UfoereAlder
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlage
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligPensjonFoerSkattAp2025
import no.nav.pensjon.brev.maler.vedlegg.vedleggOrienteringOmRettigheterOgPlikter
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object EndringAvAlderspensjonPgaGarantitillegg :
    RedigerbarTemplate<EndringAvAlderspensjonGarantitilleggDto> {
    override val kode = Pesysbrevkoder.Redigerbar.PE_AP_ENDRING_AV_ALDERSPENSJON_GARANTITILLEGG
    override val kategori = TemplateDescription.Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = Sakstype.pensjon

    override val template =
        createTemplate(
            name = kode.name,
            letterDataType = EndringAvAlderspensjonGarantitilleggDto::class,
            languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
            letterMetadata =
                LetterMetadata(
                    displayTitle = "Vedtak - Endring av alderspensjon pga garantitillegg",
                    isSensitiv = false,
                    distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
                    brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
                ),
        ) {
            val kravVirkDatoFom = pesysData.kravVirkDatoFom.format()
            val garantitillegg =
                pesysData.beregnetPensjonPerManedVedVirk.garantitillegg_safe.ifNull(then = Kroner(0))
            val uforeKombinertMedAlder = pesysData.alderspensjonVedVirk.ufoereKombinertMedAlder
            val totalPensjon = pesysData.beregnetPensjonPerManedVedVirk.totalPensjon.format()
            val uttaksgrad = pesysData.alderspensjonVedVirk.uttaksgrad.ifNull(then = (0))
            val innvilgetFor67 = pesysData.alderspensjonVedVirk.innvilgetFor67

            title {
                text(
                    bokmal { +"Du har fått innvilget garantitillegg fra " + kravVirkDatoFom },
                    nynorsk { +"Du har fått innvilga garantitillegg frå " + kravVirkDatoFom },
                    english {
                        +"You have been granted a guarantee supplement for accumulated pension capital rights from " + kravVirkDatoFom
                    },
                )
            }
            outline {
                includePhrase(Vedtak.Overskrift)

                paragraph {
                    text(
                        bokmal {
                            +"Garantitillegget skal sikre at du får en alderspensjon som tilsvarer den" +
                                " pensjonen du hadde tjent opp før pensjonsreformen i 2010."
                        },
                        nynorsk {
                            +"Garantitillegget skal sikre at du får ein alderspensjon ved 67 år som svarer" +
                                " til den pensjonen du hadde tent opp før pensjonsreforma i 2010."
                        },
                        english {
                            +"The guarantee supplement for accumulated pension capital rights is to ensure" +
                                " that you receive a retirement pension at age 67 that corresponds to the" +
                                " pension you had earned before the pension reform in 2010."
                        },
                    )
                }
                paragraph {
                    text(
                        bokmal {
                            +"Tillegget utbetales sammen med alderspensjonen og kan tidligst utbetales" +
                                " fra måneden etter du fyller 67 år."
                        },
                        nynorsk {
                            +"Tillegget blir betalt ut samen med alderspensjonen og kan tidlegast" +
                                " betalast ut frå månaden etter du fyller 67 år."
                        },
                        english {
                            +"The supplement will be paid in addition to your retirement pension and" +
                                " can at the earliest be paid from the month after you turn 67 years of age."
                        },
                    )
                }
                paragraph {
                    text(
                        bokmal {
                            +"Garantitillegget utgjør ".expr() + garantitillegg.format() + " per måned før skatt fra ".expr() +
                                kravVirkDatoFom +
                                "."
                        },
                        nynorsk {
                            +"Garantitillegget utgjer ".expr() + garantitillegg.format() + " per månad før skatt frå ".expr() +
                                kravVirkDatoFom +
                                "."
                        },
                        english {
                            +"Your monthly guarantee supplement for accumulated pension capital rights will be ".expr() +
                                garantitillegg.format() +
                                " before tax from ".expr() +
                                kravVirkDatoFom +
                                "."
                        },
                    )
                }

                showIf(uforeKombinertMedAlder) {
                    // innvilgelseAPogUTInnledn
                    includePhrase(
                        UfoereAlder.DuFaar(
                            pesysData.beregnetPensjonPerManedVedVirk.totalPensjon,
                            pesysData.kravVirkDatoFom,
                        ),
                    )
                }.orShow {
                    includePhrase(DuFaarAP(kravVirkDatoFom, totalPensjon))
                }

                paragraph {
                    text(
                        bokmal {
                            +"Vedtaket er gjort etter folketrygdloven § 20-20."
                        },
                        nynorsk {
                            +"Vedtaket er gjort etter folketrygdlova § 20-20."
                        },
                        english {
                            +"This decision was made pursuant to the provisions of § 20-20 of the National Insurance Act."
                        },
                    )
                }

                // vedleggBeregnPensjonsOpptjeningOverskrift
                title1 {
                    text(
                        bokmal {
                            +"Pensjonsopptjeningen din"
                        },
                        nynorsk {
                            +"Pensjonsoppteninga di"
                        },
                        english { +"Your accumulated pension capital" },
                    )
                }
                // vedleggBeregnPensjonsOpptjening
                paragraph {
                    text(
                        bokmal {
                            +"I nettjenesten Din pensjon på ${Constants.DIN_PENSJON_URL} kan du få" +
                                " oversikt over pensjonsopptjeningen din for hvert enkelt år." +
                                " Der vil du kunne se hvilke andre typer pensjonsopptjening som er registrert på deg."
                        },
                        nynorsk {
                            +"I nettenesta Din pensjon på ${Constants.DIN_PENSJON_URL} kan du få" +
                                " oversikt over pensjonsoppteninga di for kvart enkelt år." +
                                " Der kan du sjå kva andre typar pensjonsopptening som er registrert på deg."
                        },
                        english {
                            +"Our online service 'Din pensjon' at ${Constants.DIN_PENSJON_URL} provides" +
                                " details on your accumulated rights for each year. Here you will be" +
                                " able to see your other types of pension rights we have registered."
                        },
                    )
                }

                // Arbeidsinntekt og pensjon

                includePhrase(
                    ArbeidsinntektOgAlderspensjon(
                        innvilgetFor67 = innvilgetFor67,
                        uttaksgrad = uttaksgrad.ifNull(then = (0)),
                        uforeKombinertMedAlder = uforeKombinertMedAlder,
                    ),
                )

                includePhrase(InformasjonOmAlderspensjon)
                includePhrase(MeldeFraOmEndringer)
                includePhrase(Felles.RettTilAAKlage(vedlegg = vedleggDineRettigheterOgMulighetTilAaKlage))
                includePhrase(Felles.RettTilInnsyn(vedlegg = vedleggDineRettigheterOgMulighetTilAaKlage))
                includePhrase(Felles.HarDuSpoersmaal.alder)
            }
            includeAttachment(
                vedleggOrienteringOmRettigheterOgPlikter,
                pesysData.orienteringOmRettigheterOgPlikterDto,
            )
            includeAttachmentIfNotNull(
                vedleggMaanedligPensjonFoerSkatt,
                pesysData.maanedligPensjonFoerSkattDto,
            )
            includeAttachmentIfNotNull(
                vedleggMaanedligPensjonFoerSkattAp2025,
                pesysData.maanedligPensjonFoerSkattAP2025Dto,
            )
        }
}
