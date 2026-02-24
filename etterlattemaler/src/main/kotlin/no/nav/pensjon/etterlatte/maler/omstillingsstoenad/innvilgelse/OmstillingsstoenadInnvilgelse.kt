package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.FerdigstillingBrevDTO
import no.nav.pensjon.etterlatte.maler.Hovedmal
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregning
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadEtterbetaling
import no.nav.pensjon.etterlatte.maler.fraser.common.Felles
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadFellesFraser
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadInnvilgelseFraser
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelseDTOSelectors.data
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelseDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelseDataSelectors.beregning
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelseDataSelectors.datoVedtakOmgjoering
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelseDataSelectors.etterbetaling
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelseDataSelectors.harUtbetaling
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelseDataSelectors.informasjonOmOmstillingsstoenadData
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelseDataSelectors.innvilgetMindreEnnFireMndEtterDoedsfall
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelseDataSelectors.omsRettUtenTidsbegrensning
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelseDataSelectors.tidligereFamiliepleier
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.InformasjonOmOmstillingsstoenadData
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.beregningAvOmstillingsstoenad
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.dineRettigheterOgPlikter
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.informasjonOmOmstillingsstoenad
import java.time.LocalDate

data class OmstillingsstoenadInnvilgelseData(
    val innvilgetMindreEnnFireMndEtterDoedsfall: Boolean,
    val lavEllerIngenInntekt: Boolean?, // TODO: skal fases ut
    val omsRettUtenTidsbegrensning: Boolean = lavEllerIngenInntekt ?: false, // TODO: overtar for lavEllerIngenInntekt
    val etterbetaling: OmstillingsstoenadEtterbetaling?,
    val tidligereFamiliepleier: Boolean = false,
    val bosattUtland: Boolean = false,
    val harUtbetaling: Boolean,
    val beregning: OmstillingsstoenadBeregning,
    val erSluttbehandling: Boolean = false,
    val datoVedtakOmgjoering: LocalDate? = null,
) {
    val informasjonOmOmstillingsstoenadData = InformasjonOmOmstillingsstoenadData(tidligereFamiliepleier, bosattUtland)
}

data class OmstillingsstoenadInnvilgelseDTO(
    override val innhold: List<Element>,
    val data: OmstillingsstoenadInnvilgelseData,
) : FerdigstillingBrevDTO

@TemplateModelHelpers
object OmstillingsstoenadInnvilgelse : EtterlatteTemplate<OmstillingsstoenadInnvilgelseDTO>, Hovedmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMSTILLINGSSTOENAD_INNVILGELSE

    override val template =
        createTemplate(
            languages = languages(Bokmal, Nynorsk, English),
            letterMetadata =
                LetterMetadata(
                    displayTitle = "Vedtak - Innvilget omstillingsstønad",
                    distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
                    brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
                ),
        ) {

            title {
                ifNotNull(data.datoVedtakOmgjoering) {
                    text(
                        bokmal { +"Vi har omgjort vedtaket om omstillingsstønad av " + it.format() },
                        nynorsk { +"Vi har gjort om vedtaket om omstillingsstønad av " + it.format() },
                        english { +"We have reversed our decision regarding the adjustment allowance on " + it.format() },
                    )
                }.orShow {
                    text(
                        bokmal { +"Vi har innvilget søknaden din om omstillingsstønad" },
                        nynorsk { +"Vi har innvilga søknaden din om omstillingsstønad" },
                        english { +"We have granted your application for adjustment allowance" },
                    )
                }
            }

            outline {
                konverterElementerTilBrevbakerformat(innhold)

                showIf(data.harUtbetaling) {
                    includePhrase(OmstillingsstoenadInnvilgelseFraser.UtbetalingMedEtterbetaling(data.etterbetaling))
                }
                includePhrase(OmstillingsstoenadInnvilgelseFraser.HvaErOmstillingsstoenad(data.tidligereFamiliepleier))
                includePhrase(
                    OmstillingsstoenadFellesFraser.HvorLengerKanDuFaaOmstillingsstoenad(
                        data.beregning,
                        data.omsRettUtenTidsbegrensning,
                        data.tidligereFamiliepleier,
                    ),
                )
                showIf(data.omsRettUtenTidsbegrensning.not()) {
                    includePhrase(
                        OmstillingsstoenadInnvilgelseFraser.Aktivitetsplikt(
                            data.innvilgetMindreEnnFireMndEtterDoedsfall,
                            data.tidligereFamiliepleier,
                        ),
                    )
                }
                includePhrase(OmstillingsstoenadFellesFraser.Inntektsendring)
                includePhrase(OmstillingsstoenadFellesFraser.Etteroppgjoer)
                includePhrase(OmstillingsstoenadFellesFraser.MeldFraOmEndringer)
                includePhrase(Felles.DuHarRettTilAaKlage)
                includePhrase(OmstillingsstoenadFellesFraser.HarDuSpoersmaal)
            }
            includeAttachment(
                beregningAvOmstillingsstoenad(tidligereFamiliepleier = true, innvilgelsesaar = true),
                data.beregning,
                data.tidligereFamiliepleier,
            )
            includeAttachment(
                beregningAvOmstillingsstoenad(tidligereFamiliepleier = false, innvilgelsesaar = true),
                data.beregning,
                data.tidligereFamiliepleier.not(),
            )

            includeAttachment(informasjonOmOmstillingsstoenad(),
                data.informasjonOmOmstillingsstoenadData)

            includeAttachment(dineRettigheterOgPlikter)
        }
}
