package no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.ny

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.Avdoed
import no.nav.pensjon.etterlatte.maler.AvdoedSelectors.doedsdato
import no.nav.pensjon.etterlatte.maler.AvdoedSelectors.navn
import no.nav.pensjon.etterlatte.maler.Delmal
import no.nav.pensjon.etterlatte.maler.Utbetalingsinfo
import no.nav.pensjon.etterlatte.maler.UtbetalingsinfoSelectors.beloep
import no.nav.pensjon.etterlatte.maler.UtbetalingsinfoSelectors.virkningsdato
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.ny.BarnepensjonInnvilgelseEnkelDTOSelectors.avdoed
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.ny.BarnepensjonInnvilgelseEnkelDTOSelectors.erEtterbetalingMerEnnTreMaaneder
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.ny.BarnepensjonInnvilgelseEnkelDTOSelectors.erInstitusjonsopphold
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.ny.BarnepensjonInnvilgelseEnkelDTOSelectors.utbetalingsinfo
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.Barnepensjon
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.Institusjonsoppholdfraser
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.Lover
import no.nav.pensjon.etterlatte.maler.fraser.common.Vedtak

data class BarnepensjonInnvilgelseEnkelDTO(
    val utbetalingsinfo: Utbetalingsinfo,
    val avdoed: Avdoed,
    val erEtterbetalingMerEnnTreMaaneder: Boolean,
    val erInstitusjonsopphold: Boolean,
)

@TemplateModelHelpers
object BarnepensjonInnvilgelseEnkel : EtterlatteTemplate<BarnepensjonInnvilgelseEnkelDTO>, Delmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_INNVILGELSE_ENKEL

    override val template = createTemplate(
        name = kode.name,
        letterDataType = BarnepensjonInnvilgelseEnkelDTO::class,
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - innvilgelse",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                Language.Bokmal to "Vi innvilger barnepensjonen din",
                Language.Nynorsk to "",
                Language.English to "",
            )
        }
        outline {
            includePhrase(Vedtak.BegrunnelseForVedtaket)

            includePhrase(
                Barnepensjon.Foerstegangsbehandlingsvedtak(
                    utbetalingsinfo.virkningsdato,
                    avdoed.navn,
                    avdoed.doedsdato,
                    utbetalingsinfo.beloep,
                ),
            )

            showIf(erInstitusjonsopphold) {
                includePhrase(
                    Institusjonsoppholdfraser.Innvilgelse,
                )
                includePhrase(Institusjonsoppholdfraser.Lover(erEtterbetalingMerEnnTreMaaneder))
            }.orShow {
                includePhrase(
                    Lover.MuligEtterbetaling(
                        paragraf = Expression.Literal("FYLL INN HER"),
                        erEtterbetaling = erEtterbetalingMerEnnTreMaaneder,
                    ),
                )
            }
        }
    }
}
