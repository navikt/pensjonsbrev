package no.nav.pensjon.brev.maler.ufore.lovendringer2026.oktbunnfradrag

import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmOktBunnfradragAutoDto
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktBunnfradragAutoDto.vedtakData
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktBunnfradragData.orienteringOmRettigheterUfoere
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktBunnfradragData.pe
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningvirkningdatofom
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop
import no.nav.pensjon.brev.maler.ufore.vedlegg.vedleggDineRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.namedReference
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object VedtakOmOktBunnfradragInstAuto : AutobrevTemplate<VedtakOmOktBunnfradragAutoDto> {

    override val kode = Pesysbrevkoder.AutoBrev.UT_VEDTAK_OKT_BUNNFRADRAG_INST_2026

    override val template = createTemplate(
        languages = languages(Language.Bokmal, Language.Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - Økning fribeløp med endring",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        val data = vedtakData

        title {
            text(
                bokmal { +"Du kan ha høyere inntekt før vi reduserer uføretrygden din" },
                nynorsk { +"Du kan ha høgare inntekt før vi reduserer uføretrygda di" }
            )
        }
        outline {
            includePhrase(OktBunnfradragInst.Outline(data))

            title1 {
                text(
                    bokmal { + "Opplysninger vi har brukt i beregningen fra " },
                    nynorsk { + "Opplysningar vi har brukt i berekninga frå " },
                )
                ifNotNull(data.pe.vedtaksdata_beregningsdata_beregningufore_beregningvirkningdatofom()) { beregningVirkFom ->
                    text(
                        bokmal { + beregningVirkFom.format() },
                        nynorsk { + beregningVirkFom.format() },
                    )
                }
            }
            paragraph {
                text(
                    bokmal { + " Folketrygdens grunnbeløp (G) benyttet i beregningen er " +
                            data.pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop().format() + "." },
                    nynorsk { + " Folketrygdas grunnbeløp (G) nytta i berekninga er " +
                            data.pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop().format() + "." },
                )
            }
            includePhrase(OpplysningerBruktIBeregningTabell(data.pe))

            title1 {
                text(
                    bokmal { +"Du har rett til å klage" },
                    nynorsk { +"Du har rett til å klage" },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Hvis du mener vedtaket er feil, kan du klage. Fristen for å klage er seks uker fra den datoen vedtaket har kommet fram til deg. Du finner skjema og informasjon på " + "${Constants.KLAGE_URL}."
                    },
                    nynorsk {
                        +"Om du meiner vedtaket er feil, kan du klage. Fristen for å klage er seks veker frå den datoen vedtaket har kome fram til deg. Du finn skjema og informasjon på " + "${Constants.KLAGE_URL}."
                    },
                )
            }
            paragraph {
                text(
                    bokmal { +"I vedlegget " },
                    nynorsk { +"I vedlegget " },
                )
                namedReference(vedleggDineRettigheterOgPlikterUfoere)
                text(
                    bokmal { +" får du vite mer om hvordan du går fram for å klage." },
                    nynorsk { +" får du vite meir om korleis du går fram for å klage." },
                )
            }
            includePhrase(Felles.RettTilInnsyn(vedleggDineRettigheterOgPlikterUfoere))
            includePhrase(Felles.HarDuSpoersmaal.ufoeretrygd)
        }
        includeAttachment(vedleggDineRettigheterOgPlikterUfoere, data.orienteringOmRettigheterUfoere)
    }
}
