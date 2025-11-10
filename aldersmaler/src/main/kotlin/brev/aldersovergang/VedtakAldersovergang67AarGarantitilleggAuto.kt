package brev.aldersovergang

import brev.felles.Constants
import brev.felles.HarDuSpoersmaalAlder
import brev.felles.MeldeFraOmEndringer
import brev.felles.RettTilAAKlage
import brev.felles.RettTilInnsyn
import brev.vedlegg.vedleggMaanedligPensjonFoerSkatt
import brev.vedlegg.vedleggMaanedligPensjonFoerSkattAp2025
import brev.vedlegg.vedleggOrienteringOmRettigheterOgPlikter
import no.nav.pensjon.brev.model.alder.Aldersbrevkoder
import no.nav.pensjon.brev.model.alder.aldersovergang.AlderspensjonVedVirkSelectors.totalPensjon
import no.nav.pensjon.brev.model.alder.aldersovergang.AlderspensjonVedVirkSelectors.uttaksgrad
import no.nav.pensjon.brev.model.alder.aldersovergang.BeregnetPensjonPerManedVedVirkSelectors.garantitillegg
import no.nav.pensjon.brev.model.alder.aldersovergang.VedtakAldersovergang67AarGarantitilleggAutoDto
import no.nav.pensjon.brev.model.alder.aldersovergang.VedtakAldersovergang67AarGarantitilleggAutoDtoSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.model.alder.aldersovergang.VedtakAldersovergang67AarGarantitilleggAutoDtoSelectors.beregnetPensjonPerManedVedVirk
import no.nav.pensjon.brev.model.alder.aldersovergang.VedtakAldersovergang67AarGarantitilleggAutoDtoSelectors.maanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.model.alder.aldersovergang.VedtakAldersovergang67AarGarantitilleggAutoDtoSelectors.maanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.model.alder.aldersovergang.VedtakAldersovergang67AarGarantitilleggAutoDtoSelectors.orienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brev.model.alder.aldersovergang.VedtakAldersovergang67AarGarantitilleggAutoDtoSelectors.virkFom
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

// MF_000195
@TemplateModelHelpers
object VedtakAldersovergang67AarGarantitilleggAuto : AutobrevTemplate<VedtakAldersovergang67AarGarantitilleggAutoDto> {
    override val kode = Aldersbrevkoder.AutoBrev.VEDTAK_ALDERSOVERGANG_67_AAR_GARANTITILLEGG_AUTO

    override val template =
        createTemplate(
            languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
            letterMetadata =
                LetterMetadata(
                    displayTitle = "Vedtak - omregning av alderspensjon pga rett til garantitillegg",
                    isSensitiv = false,
                    distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
                    brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
                ),
        ) {
            title {
                text(
                    bokmal { +"Du har fått innvilget garantitillegg fra " + virkFom.format() },
                    nynorsk { +"Du har fått innvilga garantitillegg frå " + virkFom.format() },
                    english { +"You have been granted a guarantee supplement for accumulated pension capital rights from " + virkFom.format() },
                )
            }

            outline {
                title2 {
                    text(
                        bokmal { +"Vedtak" },
                        nynorsk { +"Vedtak" },
                        english { +"Decision" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Garantitillegget skal sikre at du får en alderspensjon som tilsvarer den pensjonen du hadde tjent opp før pensjonsreformen i 2010." },
                        nynorsk { +"Garantitillegget skal sikre at du får ein alderspensjon ved 67 år som svarer til den pensjonen du hadde tent opp før pensjonsreforma i 2010." },
                        english { +"The guarantee supplement for accumulated pension capital rights is to ensure that you receive a retirement pension at age 67 that corresponds to the pension you had earned before the pension reform in 2010." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Tillegget utbetales sammen med alderspensjonen og kan tidligst utbetales fra måneden etter du fyller 67 år." },
                        nynorsk { +"Tillegget blir betalt ut samen med alderspensjonen og kan tidlegast betalast ut frå månaden etter du fyller 67 år." },
                        english { +"The supplement will be paid in addition to your retirement pension and can at the earliest be paid from the month after you turn 67 years of age." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Garantitillegget utgjør " + beregnetPensjonPerManedVedVirk.garantitillegg.format() + " per måned før skatt fra " + virkFom.format() + "." },
                        nynorsk { +"Garantitillegget utgjer " + beregnetPensjonPerManedVedVirk.garantitillegg.format() + " per månad før skatt frå " + virkFom.format() + "." },
                        english { +"Your monthly guarantee supplement for accumulated pension capital rights will be " + beregnetPensjonPerManedVedVirk.garantitillegg.format() + " before tax from " + virkFom.format() + "." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Du får " + alderspensjonVedVirk.totalPensjon.format() + " hver måned før skatt fra " + virkFom.format() + " i alderspensjon fra folketrygden." },
                        nynorsk { +"Du får " + alderspensjonVedVirk.totalPensjon.format() + " kvar månad før skatt frå " + virkFom.format() + " i alderspensjon frå folketrygda." },
                        english { +"You will receive " + alderspensjonVedVirk.totalPensjon.format() + " every month before tax from " + virkFom.format() + " as retirement pension from the National Insurance Scheme." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Hvis du har andre pensjonsytelser som for eksempel AFP eller tjenestepensjon, blir de utbetalt i tillegg til alderspensjonen. Alderspensjonen din utbetales innen den 20. hver måned. Du finner oversikt over utbetalingene dine på nav.no/utbetalinger." },
                        nynorsk { +"Dersom du har andre pensjonsytingar som for eksempel AFP eller tenestepensjon, kjem slik utbetaling i tillegg til alderspensjonen. Alderspensjonen din blir betalt ut innan den 20. i kvar månad. Du finn meir informasjon om utbetalingane dine på nav.no/utbetalinger." },
                        english { +"If you have occupational pensions from other schemes, this will be paid in addition to your retirement pension. Your pension will be paid at the latest on the 20th of each month. See the more detailed information on what you will receive at nav.no/utbetalingsinformasjon." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven § 20-20." },
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova § 20-20." },
                        english { +"This decision was made pursuant to the provisions of § 20-20 of the National Insurance Act." },
                    )
                }

                title2 {
                    text(
                        bokmal { +"Arbeidsinntekt og alderspensjon" },
                        nynorsk { +"Arbeidsinntekt og alderspensjon" },
                        english { +"Earned income and retirement pension" },
                    )
                }

                paragraph {
                    text(
                        bokmal { +"Du kan arbeide så mye du vil uten at alderspensjonen din blir redusert. Det kan føre til at pensjonen din øker." },
                        nynorsk { +"Du kan arbeide så mykje du vil utan at alderspensjonen din blir redusert. Det kan føre til at pensjonen din aukar." },
                        english { +"You can work as much as you want without your retirement pension being reduced. This may lead to an increase in your pension." },
                    )
                }
                showIf(alderspensjonVedVirk.uttaksgrad.equalTo(100)) {
                    paragraph {
                        text(
                            bokmal { +"Hvis du har 100 prosent alderspensjon, gjelder økningen fra 1. januar året etter at skatteoppgjøret ditt er ferdig." },
                            nynorsk { +"Dersom du har 100 prosent alderspensjon, gjeld auken frå 1. januar året etter at skatteoppgjeret ditt er ferdig." },
                            english { +"If you are receiving a full (100 percent) retirement pension, the increase will come into effect from 1 January the year after your final tax settlement has been completed." },
                        )
                    }
                }.orShow {
                    paragraph {
                        text(
                            bokmal { +"Hvis du har lavere enn 100 prosent alderspensjon, blir økningen lagt til hvis du søker om endret grad eller ny beregning av den graden du har nå." },
                            nynorsk { +"Dersom du har lågare enn 100 prosent alderspensjon, blir auken lagd til dersom du søkjer om endra grad eller ny berekning av den graden du har no." },
                            english { +"If you are receiving retirement pension at a reduced rate (lower than 100 percent), the increase will come into effect if you apply to have the rate changed or have your current rate recalculated." },
                        )
                    }
                }

                title2 {
                    text(
                        bokmal { +"Hvor kan du få vite mer om alderspensjonen din?" },
                        nynorsk { +"Kvar kan du få vite meir om alderspensjonen din?" },
                        english { +"Where can you find out more about your retirement pension?" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Du finner mer informasjon om hvordan alderspensjon er satt sammen og oversikter over grunnbeløp og aktuelle satser på ${Constants.ALDERSPENSJON_URL}." },
                        nynorsk { +"Du finn meir informasjon om korleis alderspensjonen er sett saman, og oversikter over grunnbeløp og aktuelle satsar på ${Constants.ALDERSPENSJON_URL}." },
                        english { +"There is more information on how retirement pension is calculated, with overviews of basic amounts and relevant rates, at ${Constants.ALDERSPENSJON_URL}." },
                    )
                }

                title2 {
                    text(
                        bokmal { +"Pensjonsopptjeningen din" },
                        nynorsk { +"Pensjonsoppteninga di" },
                        english { +"Your accumulated pension capital" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"I nettjenesten Din pensjon på ${Constants.DIN_PENSJON_URL} kan du få oversikt over pensjonsopptjeningen din for hvert enkelt år. Der vil du kunne se hvilke andre typer pensjonsopptjening som er registrert på deg." },
                        nynorsk { +"I nettenesta Din pensjon på ${Constants.DIN_PENSJON_URL} kan du få oversikt over pensjonsoppteninga di for kvart enkelt år. Der kan du sjå kva andre typar pensjonsopptening som er registrert på deg." },
                        english { +"Our online service «Din pensjon» at ${Constants.DIN_PENSJON_URL} provides details on your accumulated rights for each year. Here you will be able to see your other types of pension rights we have registered." },
                    )
                }
                includePhrase(MeldeFraOmEndringer)
                includePhrase(RettTilAAKlage)
                includePhrase(RettTilInnsyn(vedlegg = vedleggOrienteringOmRettigheterOgPlikter))
                includePhrase(HarDuSpoersmaalAlder)
            }

            includeAttachment(vedleggOrienteringOmRettigheterOgPlikter, orienteringOmRettigheterOgPlikterDto)
            includeAttachmentIfNotNull(vedleggMaanedligPensjonFoerSkatt, maanedligPensjonFoerSkattDto)
            includeAttachmentIfNotNull(vedleggMaanedligPensjonFoerSkattAp2025, maanedligPensjonFoerSkattAP2025Dto)
        }
}