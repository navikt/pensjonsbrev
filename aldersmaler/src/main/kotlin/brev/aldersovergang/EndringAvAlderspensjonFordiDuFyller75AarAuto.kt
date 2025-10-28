package brev.aldersovergang

import brev.felles.DuFaarHverMaaned
import brev.felles.HarDuSpoersmaal
import brev.felles.MeldFraOmEndringer2
import brev.felles.RettTilAAKlage
import brev.felles.RettTilInnsyn
import brev.felles.Utbetalingsinformasjon
import brev.felles.Vedtak
import brev.felles.VedtakAlderspensjon
import brev.vedlegg.opplysningerbruktiberegningen.vedleggOpplysningerBruktIBeregningenAlder
import brev.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlage
import brev.vedlegg.vedleggMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.model.alder.Aldersbrevkoder
import no.nav.pensjon.brev.model.alder.AlderspensjonRegelverkType
import no.nav.pensjon.brev.model.alder.aldersovergang.EndringAvAlderspensjonFordiDuFyller75AarAutoDto
import no.nav.pensjon.brev.model.alder.aldersovergang.EndringAvAlderspensjonFordiDuFyller75AarAutoDtoSelectors.dineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.model.alder.aldersovergang.EndringAvAlderspensjonFordiDuFyller75AarAutoDtoSelectors.kravVirkDatoFom
import no.nav.pensjon.brev.model.alder.aldersovergang.EndringAvAlderspensjonFordiDuFyller75AarAutoDtoSelectors.maanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.model.alder.aldersovergang.EndringAvAlderspensjonFordiDuFyller75AarAutoDtoSelectors.opplysningerBruktIBeregningenAlder
import no.nav.pensjon.brev.model.alder.aldersovergang.EndringAvAlderspensjonFordiDuFyller75AarAutoDtoSelectors.regelverkType
import no.nav.pensjon.brev.model.alder.aldersovergang.EndringAvAlderspensjonFordiDuFyller75AarAutoDtoSelectors.totalPensjon
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.notNull
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata


/* MF_000118 : AP_INNV_AO_75_AUTO / AO_AP_GRAD_AP_75
Det er omregning til 100 prosent alderspensjon for de som har gradert pensjon med regelverk AP2011 eller AP2016.
 */
@TemplateModelHelpers
object EndringAvAlderspensjonFordiDuFyller75AarAuto :
    AutobrevTemplate<EndringAvAlderspensjonFordiDuFyller75AarAutoDto> {
    override val kode = Aldersbrevkoder.AutoBrev.PE_AP_ENDRING_FORDI_DU_FYLLER_75_AAR_AUTO

    override val template =
        createTemplate(
            languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
            letterMetadata =
                LetterMetadata(
                    displayTitle = "Vedtak - endring av alderspensjon fordi du fyller 75 år",
                    isSensitiv = false,
                    distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
                    brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
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

                showIf(maanedligPensjonFoerSkattDto.notNull()) {
                    paragraph {
                        text(
                            bokmal { + "Du kan lese mer om andre beregningsperioder i vedlegget." },
                            nynorsk { + "Du kan lese meir om andre berekningsperiodar i vedlegget." },
                            english { + "There is more information about other calculation periods in the attachment." },
                        )
                    }
                }

                // begrunnAP75
                paragraph {
                    text(
                        bokmal { +"Du vil ikke tjene på å vente med å ta ut hele pensjonen etter at du har blitt 75 år. Derfor har vi regnet om pensjonen din." },
                        nynorsk { +"Du vil ikkje tene på å vente med å ta ut heile pensjonen etter at du har blitt 75 år. Derfor har vi regna om pensjonen din." },
                        english { +"After turning 75 years, postponing withdrawal of your retirement pension will not result in a higher pension amount. Therefore, we have recalculated your pension." }
                    )
                }

                showIf(regelverkType.equalTo(AlderspensjonRegelverkType.AP2011)) {
                    // endrUtaksgradAP2011
                    paragraph {
                        text(
                            bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 19-10, 19-12 og 22-12." },
                            nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 19-10, 19-12 og 22-12." },
                            english { +"This decision was made pursuant to the provisions of §§ 19-10, 19-12 and 22-12 of the National Insurance Act." }
                        )
                    }
                }.orShowIf(regelverkType.equalTo(AlderspensjonRegelverkType.AP2016)) {
                    // endrUtaksgradAP2016
                    paragraph {
                        text(
                            bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 19-10, 19-12, 19-15, 20-14, 20-16, 20-19 og 22-12." },
                            nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 19-10, 19-12, 19-15, 20-14, 20-16, 20-19 og 22-12." },
                            english { +"This decision was made pursuant to the provisions of §§ 19-10, 19-12, 19-15, 20-14, 20-16, 20-19 and 22-12 of the National Insurance Act." }
                        )
                    }
                }

                includePhrase(VedtakAlderspensjon.EndringKanHaBetydningForSkatt)
                includePhrase(MeldFraOmEndringer2)
                includePhrase(RettTilAAKlage(vedleggDineRettigheterOgMulighetTilAaKlage))
                includePhrase(RettTilInnsyn(vedleggDineRettigheterOgMulighetTilAaKlage))
                includePhrase(HarDuSpoersmaal.alder)
            }
            includeAttachment(
                vedleggDineRettigheterOgMulighetTilAaKlage,
                dineRettigheterOgMulighetTilAaKlageDto
            )
            includeAttachmentIfNotNull(vedleggMaanedligPensjonFoerSkatt, maanedligPensjonFoerSkattDto)
            includeAttachmentIfNotNull(vedleggOpplysningerBruktIBeregningenAlder, opplysningerBruktIBeregningenAlder)
        }
}