package no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.ny

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
import no.nav.pensjon.etterlatte.maler.Avdoed
import no.nav.pensjon.etterlatte.maler.AvdoedSelectors.doedsdato
import no.nav.pensjon.etterlatte.maler.AvdoedSelectors.navn
import no.nav.pensjon.etterlatte.maler.Delmal
import no.nav.pensjon.etterlatte.maler.Utbetalingsinfo
import no.nav.pensjon.etterlatte.maler.UtbetalingsinfoSelectors.beloep
import no.nav.pensjon.etterlatte.maler.UtbetalingsinfoSelectors.virkningsdato
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.ny.BarnepensjonInnvilgelseInstitusjonsoppholdDTOSelectors.avdoed
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.ny.BarnepensjonInnvilgelseInstitusjonsoppholdDTOSelectors.erEtterbetalingMerEnnTreeMaaneder
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.ny.BarnepensjonInnvilgelseInstitusjonsoppholdDTOSelectors.utbetalingsinfo
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.Barnepensjon
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.Institusjonsoppholdfraser
import no.nav.pensjon.etterlatte.maler.fraser.common.Vedtak

data class BarnepensjonInnvilgelseInstitusjonsoppholdDTO(
    val erEtterbetalingMerEnnTreeMaaneder: Boolean,
    val utbetalingsinfo: Utbetalingsinfo,
    val avdoed: Avdoed,
)

@TemplateModelHelpers
object BarnepensjonInnvilgelseInstitusjonsopphold : EtterlatteTemplate<BarnepensjonInnvilgelseInstitusjonsoppholdDTO>, Delmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_INNVILGELSE_INSTITUSJONSOPPHOLD

    override val template = createTemplate(
        name = kode.name,
        letterDataType = BarnepensjonInnvilgelseInstitusjonsoppholdDTO::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - innvilgelse",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                Bokmal to "Vi innvilger barnepensjonen din",
                Nynorsk to "",
                English to "",
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
            includePhrase(
                Institusjonsoppholdfraser.Innvilgelse,
            )

            includePhrase(Institusjonsoppholdfraser.Lover(erEtterbetalingMerEnnTreeMaaneder))
        }
    }
}
