package no.nav.pensjon.brev.alder.maler.avslag.uttak

import no.nav.pensjon.brev.alder.maler.felles.Constants.SUPPLERENDE_STOENAD_URL
import no.nav.pensjon.brev.alder.maler.felles.HarDuSpoersmaalAlder
import no.nav.pensjon.brev.alder.maler.felles.RettTilAAKlage
import no.nav.pensjon.brev.alder.maler.felles.RettTilInnsyn
import no.nav.pensjon.brev.alder.maler.felles.Vedtak
import no.nav.pensjon.brev.alder.maler.fraser.vedlegg.opplysningerbruktiberegningenalder.OpplysningerBruktIBeregningenTrygdetidTabeller
import no.nav.pensjon.brev.alder.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlage
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.alder.model.avslag.AvslagUnder5AarTrygdetidAutoDto
import no.nav.pensjon.brev.alder.model.avslag.AvslagUnder5AarTrygdetidAutoDtoSelectors.borINorge
import no.nav.pensjon.brev.alder.model.avslag.AvslagUnder5AarTrygdetidAutoDtoSelectors.dineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.alder.model.avslag.AvslagUnder5AarTrygdetidAutoDtoSelectors.regelverkType
import no.nav.pensjon.brev.alder.model.avslag.AvslagUnder5AarTrygdetidAutoDtoSelectors.trygdeperioderNorge
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.isNotEmpty
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object AvslagUnder5AartrygdetidAuto : AutobrevTemplate<AvslagUnder5AarTrygdetidAutoDto> {
    override val kode = Aldersbrevkoder.AutoBrev.PE_AP_AVSLAG_UNDER_5_AAR_TRYGDETID_AUTO

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - avslag på søknad om alderspensjon",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        val erAp2011 = regelverkType.equalTo(AlderspensjonRegelverkType.AP2011)
        val erAp2016 = regelverkType.equalTo(AlderspensjonRegelverkType.AP2016)

        title {
            text(
                bokmal { +"Nav har avslått søknaden din om alderspensjon" },
                nynorsk { +"Nav har avslått søknaden din om alderspensjon" },
                english { +"Nav has declined your application for retirement pension" },
            )
        }
        outline {
            title2 {
                text(
                    bokmal { + "Vedtak" },
                    nynorsk { + "Vedtak" },
                    english { + "Decision" }
                )
            }
            paragraph {
                text(
                    bokmal { +"For å ha rett til alderspensjon må du ha minst fem års trygdetid" },
                    nynorsk { +"For å ha rett til alderspensjon må du ha minst fem års trygdetid" },
                    english { +"To be eligible for retirement pension, you must have at least five years of national insurance coverage" },
                )

                showIf(erAp2016) {
                    text(
                        bokmal { +", eller ha tjent opp inntektspensjon" },
                        nynorsk { +", eller ha tent opp inntektspensjon" },
                        english { +", or have had a pensionable income" },
                    )
                }
                text(
                    bokmal { +". Det har du ikke, og derfor har vi avslått søknaden din." },
                    nynorsk { +". Det har du ikkje, og derfor har vi avslått søknaden din." },
                    english { +". You do not meet this requirement, therefore we have declined your application." },
                )
            }

            showIf(erAp2011) {
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven § 19-2." },
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova § 19-2." },
                        english { +"This decision was made pursuant to the provisions of § 19-2 of the National Insurance Act." }
                    )
                }
            }.orShow {
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 19-2, 20-5 til 20-8 og 20-10." },
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 19-2, 20-5 til 20-8 og 20-10." },
                        english { +"This decision was made pursuant to the provisions of §§ 19-2, 20-5 to 20-8 and 20-10 of the National Insurance Act." },
                    )
                }
            }

            showIf(trygdeperioderNorge.isNotEmpty()) {
                title2 {
                    includePhrase(Vedtak.TrygdetidText)
                }

                paragraph {
                    text(
                        bokmal { +"Trygdetid er perioder du har vært medlem i folketrygden. Som hovedregel er dette perioder du har bodd eller arbeidet i Norge. Trygdetid har betydning for beregning av pensjonen din. Full trygdetid er 40 år." },
                        nynorsk { +"Trygdetid er periodar du har vore medlem i folketrygda. Som hovudregel er dette periodar du har budd eller arbeidd i Noreg. Trygdetid har betydning for berekninga av pensjonen din. Full trygdetid er 40 år." },
                        english { +"The period of national insurance coverage is the periods in which you have been a member of the Norwegian National Insurance Scheme. As a general rule, these are periods when you have been registered as living or working in Norway. The period of national insurance coverage affects the calculation of your pension. The full insurance period is 40 years." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Tabellen nedenfor viser perioder vi har brukt for å fastsette din norske trygdetid." },
                        nynorsk { +"Tabellen nedanfor viser periodar vi har brukt for å fastsetje den norske trygdetida di." },
                        english { +"The table below shows the time periods used to establish your Norwegian national insurance coverage." },
                    )
                }

                includePhrase(OpplysningerBruktIBeregningenTrygdetidTabeller.NorskTrygdetid(trygdeperioderNorge))
            }


            showIf(borINorge) {
                title1 {
                    text(
                        bokmal { +"Supplerende stønad" },
                        nynorsk { +"Supplerande stønad" },
                        english { +"Supplementary benefit" }
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Hvis du har kort botid i Norge når du fyller 67 år, kan du søke om supplerende stønad. Du kan lese mer om supplerende stønad på vår nettside $SUPPLERENDE_STOENAD_URL." },
                        nynorsk { +"Dersom du har kort butid i Noreg når du fyller 67 år, kan du søke om supplerande stønad. Du kan lese meir om supplerande stønad på vår nettside $SUPPLERENDE_STOENAD_URL." },
                        english { +"If you have only lived a short period in Norway before reaching 67 years of age, you can apply for supplementary benefit. You can read more about supplementary benefit at our website $SUPPLERENDE_STOENAD_URL." },
                    )
                }
            }

            includePhrase(RettTilAAKlage)
            includePhrase(RettTilInnsyn(vedleggDineRettigheterOgMulighetTilAaKlage))
            includePhrase(HarDuSpoersmaalAlder)
        }
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlage, dineRettigheterOgMulighetTilAaKlageDto)
    }
}
