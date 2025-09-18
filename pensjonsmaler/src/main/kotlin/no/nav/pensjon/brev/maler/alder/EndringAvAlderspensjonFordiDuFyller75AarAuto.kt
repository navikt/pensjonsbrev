package no.nav.pensjon.brev.maler.alder

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.*
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonFordiDuFyller75AarAutoDto
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonFordiDuFyller75AarAutoDtoSelectors.harFlereBeregningsperioder
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonFordiDuFyller75AarAutoDtoSelectors.kravVirkDatoFom
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonFordiDuFyller75AarAutoDtoSelectors.regelverkType
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvAlderspensjonFordiDuFyller75AarAutoDtoSelectors.totalPensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.maanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.maler.fraser.alderspensjon.DuFaarHverMaaned
import no.nav.pensjon.brev.maler.fraser.alderspensjon.MeldFraOmEndringer2
import no.nav.pensjon.brev.maler.fraser.alderspensjon.Utbetalingsinformasjon
import no.nav.pensjon.brev.maler.fraser.alderspensjon.VedtakAlderspensjon.EndringKanHaBetydningForSkatt
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.maler.vedlegg.maanedligPensjonFoerSkattAlderspensjon
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlage
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.includeAttachment
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype


// MF_000118 : AP_INNV_AO_75_AUTO / AO_AP_GRAD_AP_75
@TemplateModelHelpers
object EndringAvAlderspensjonFordiDuFyller75AarAuto :
    AutobrevTemplate<EndringAvAlderspensjonFordiDuFyller75AarAutoDto> {
    override val kode = Pesysbrevkoder.AutoBrev.PE_AP_ENDRING_FORDI_DU_FYLLER_75_AAR_AUTO

    override val template =
        createTemplate(
            languages = languages(Bokmal, Nynorsk, English),
            letterMetadata =
                LetterMetadata(
                    displayTitle = "Informasjon til deg som snart fyller 67 år",
                    isSensitiv = false,
                    distribusjonstype = Distribusjonstype.VIKTIG,
                    brevtype = Brevtype.VEDTAKSBREV
                ),

            ) {
            title {
                text(
                    bokmal { +"Vi har økt alderspensjonen til 100 prosent fra ".expr() + kravVirkDatoFom.format() },
                    nynorsk { +"Vi har auka alderspensjonen til 100 prosent frå ".expr() + kravVirkDatoFom.format() },
                    english { +"We have increased your retirement pension to 100 percent from ".expr() + kravVirkDatoFom.format() }
                )
            }
            outline {
                includePhrase(Vedtak.Overskrift)
                includePhrase(DuFaarHverMaaned(totalPensjon))
                includePhrase(Utbetalingsinformasjon)

                showIf(harFlereBeregningsperioder and totalPensjon.greaterThan(0)) {
                    includePhrase(Felles.FlereBeregningsperioder)
                }

                // begrunnAP75
                paragraph {
                    text(
                        bokmal { +"Du vil ikke tjene på å vente med å ta ut hele pensjonen etter at du har blitt 75 år. Derfor har vi regnet om pensjonen din." },
                        nynorsk { +"Du vil ikkje tene på å vente med å ta ut heile pensjonen etter at du har blitt 75 år. Derfor har vi regna om pensjonen din." },
                        english { +"After turning 75 years, postponing withdrawal of your retirement pension will not result in a higher pension amount. Therefore, we have recalculated your pension." }
                    )
                }

                showIf(regelverkType.isOneOf(AP2011)) {
                    // endrUtaksgradAP2011
                    paragraph {
                        text(
                            bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 19-10, 19-12 og 22-12." },
                            nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 19-10, 19-12 og 22-12." },
                            english { +"This decision was made pursuant to the provisions of §§ 19-10, 19-12 and 22-12 of the National Insurance Act." }
                        )
                    }
                }.orShow {
                    // endrUtaksgradAP2016
                    paragraph {
                        text(
                            bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 19-10, 19-12, 19-15, 20-14, 20-16, 20-19 og 22-12." },
                            nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 19-10, 19-12, 19-15, 20-14, 20-16, 20-19 og 22-12." },
                            english { +"This decision was made pursuant to the provisions of §§ 19-10, 19-12, 19-15, 20-14, 20-16, 20-19 and 22-12 of the National Insurance Act." }
                        )
                    }
                }

                // skattAPendring
                includePhrase(EndringKanHaBetydningForSkatt)

                includePhrase(MeldFraOmEndringer2)
                includePhrase(Felles.RettTilAAKlage(vedleggDineRettigheterOgMulighetTilAaKlage))
                includePhrase(Felles.RettTilInnsyn(vedleggDineRettigheterOgMulighetTilAaKlage))
                includePhrase(Felles.HarDuSpoersmaal.alder)
                    }
            includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlage, DineRettigheterOgMulighetTilAaKlageDto)
            includeAttachment(vedleggMaanedligPensjonFoerSkatt, maanedligPensjonFoerSkattAlderspensjon)
                }
            }
        }
}