package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.LetterMetadataEtterlatte
import no.nav.pensjon.etterlatte.maler.RedigerbartUtfallBrevDTO
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadAktivitetspliktFraser
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadFellesFraser
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt.OmstillingsstoenadAktivitetspliktInformasjon6mndInnholdDTOSelectors.nasjonalEllerUtland
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt.OmstillingsstoenadAktivitetspliktInformasjon6mndInnholdDTOSelectors.redusertEtterInntekt

data class OmstillingsstoenadAktivitetspliktInformasjon6mndInnholdDTO(
    val redusertEtterInntekt: Boolean,
    val nasjonalEllerUtland: NasjonalEllerUtland,
) : RedigerbartUtfallBrevDTO

@TemplateModelHelpers
object OmstillingsstoenadAktivitetspliktInformasjon6mndInnhold : EtterlatteTemplate<OmstillingsstoenadAktivitetspliktInformasjon6mndInnholdDTO> {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.AKTIVITETSPLIKT_INFORMASJON_6MND_INNHOLD

    override val template =
        createTemplate(
            name = kode.name,
            letterDataType = OmstillingsstoenadAktivitetspliktInformasjon6mndInnholdDTO::class,
            languages = languages(Bokmal, Nynorsk, English),
            letterMetadata =
                LetterMetadataEtterlatte(
                    displayTitle = "Informasjon om omstillingsstønaden din",
                    isSensitiv = false,
                    distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
                    brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
                ),
        ) {
            title {
                text(
                    Bokmal to "Informasjon om omstillingsstønaden din",
                    Nynorsk to "Informasjon om omstillingsstønad",
                    English to "Information about adjustment allowance",
                )
            }

            outline {
                paragraph {
                    text(
                        Bokmal to "Du mottar omstillingsstønad.",
                        Nynorsk to "Du mottar omstillingsstønad.",
                        English to "You are receiving an adjustment allowance.",
                    )
                }
                showIf(redusertEtterInntekt) {
                    paragraph {
                        text(
                            Bokmal to "Omstillingsstønaden din er redusert etter en arbeidsinntekt på " +
                                    "<kronebeløp> kroner per år.",
                            Nynorsk to "Omstillingsstønaden din har blitt redusert ut frå ei arbeidsinntekt på " +
                                    "<kronebeløp> kroner per år.",
                            English to "Your adjustment allowance is reduced based on your income from employment " +
                                    "of NOK <kronebeløp> per year.",
                        )
                    }
                }.orShow {
                    paragraph {
                        text(
                            Bokmal to "Omstillingsstønaden din er i dag ikke redusert etter arbeidsinntekt " +
                                    "eller annen inntekt som er likestilt med arbeidsinntekt.",
                            Nynorsk to "Omstillingsstønaden din er i dag ikkje redusert ut frå arbeidsinntekt " +
                                    "eller anna inntekt som er likestilt med arbeidsinntekt.",
                            English to "Your current adjustment allowance is not reduced based on income from " +
                                    "employment or other income that is similar to an income from employment.",
                        )
                    }
                }

                title2 {
                    text(
                        Bokmal to "Omstillingsstønaden skal reduseres etter inntekten din",
                        Nynorsk to "Omstillingsstønaden skal reduserast ut frå inntekta di",
                        English to "The adjustment allowance will be reduced based on your income",
                    )
                }

                paragraph {
                    text(
                        Bokmal to "Stønaden skal reduseres med 45 prosent av arbeidsinntekten din og inntekt " +
                                "som er likestilt med arbeidsinntekt, som er over halvparten av grunnbeløpet i folketrygden (G). " +
                                "Stønaden blir redusert ut fra det du oppgir som forventet inntekt for gjeldende år.",
                        Nynorsk to "Stønaden skal reduserast med 45 prosent av arbeidsinntekta di og inntekt " +
                                "som er likestilt med arbeidsinntekt, som er over halvparten av grunnbeløpet i folketrygda (G). " +
                                "Stønaden blir redusert ut frå det du oppgir som forventa inntekt for gjeldande år.",
                        English to "The allowance will be reduced by 45 percent of your income from employment " +
                                "or other income that is equivalent to income from employment, if this is more than " +
                                "half of the basic amount in the national insurance (G). The allowance will be reduced " +
                                "based on what you declare as anticipated income for the current year.",
                    )
                }

                paragraph {
                    text(
                        Bokmal to "Du opplyste FYLL INN OM INNTEKT LAGT TIL GRUNN, F.EKS. i søknaden at du har en forventet inntekt på X kroner i år.",
                        Nynorsk to "Du opplyste FYLL INN OM INNTEKT LAGT TIL GRUNN, F.EKS. i søknaden at du har en forventet inntekt på X kroner i år.",
                        English to "You stated that FYLL INN OM INNTEKT LAGT TIL GRUNN, F.EKS. i søknaden at du har en forventet inntekt på X kroner i år.",
                    )
                }

                includePhrase(OmstillingsstoenadAktivitetspliktFraser.FellesInfoOmInntektsendring(redusertEtterInntekt))
                includePhrase(OmstillingsstoenadAktivitetspliktFraser.TrengerDuHjelpTilAaFaaNyJobb)
                includePhrase(OmstillingsstoenadAktivitetspliktFraser.HarDuHelseutfordringer)
                includePhrase(OmstillingsstoenadAktivitetspliktFraser.DuMaaMeldeFraOmEndringer(nasjonalEllerUtland))
                includePhrase(OmstillingsstoenadFellesFraser.HarDuSpoersmaal)
            }
        }
}
