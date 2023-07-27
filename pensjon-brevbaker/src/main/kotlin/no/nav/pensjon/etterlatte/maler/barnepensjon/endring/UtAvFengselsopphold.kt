package no.nav.pensjon.etterlatte.maler.barnepensjon.endring

import no.nav.pensjon.brev.template.Expression
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
import no.nav.pensjon.etterlatte.maler.Utbetalingsinfo
import no.nav.pensjon.etterlatte.maler.UtbetalingsinfoSelectors.antallBarn
import no.nav.pensjon.etterlatte.maler.UtbetalingsinfoSelectors.beregningsperioder
import no.nav.pensjon.etterlatte.maler.UtbetalingsinfoSelectors.soeskenjustering
import no.nav.pensjon.etterlatte.maler.barnepensjon.endring.BarnepensjonUtAvFengselsoppholdDTOSelectors.erEtterbetalingMerEnnTreeMaaneder
import no.nav.pensjon.etterlatte.maler.barnepensjon.endring.BarnepensjonUtAvFengselsoppholdDTOSelectors.utbetalingsinfo
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.Barnepensjon
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.Lover
import no.nav.pensjon.etterlatte.maler.fraser.common.Vedtak
import java.time.LocalDate

data class BarnepensjonUtAvFengselsoppholdDTO(
    val utbetalingsinfo: Utbetalingsinfo,
    val erEtterbetalingMerEnnTreeMaaneder: Boolean,
    val virkningsdato: LocalDate,
    val fraDato: LocalDate,
    val tilDato: LocalDate,
)

@TemplateModelHelpers
object UtAvFengselsopphold : EtterlatteTemplate<BarnepensjonUtAvFengselsoppholdDTO> {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_REVURDERING_UT_AV_FENGSELSOPPHOLD

    override val template = createTemplate(
        name = kode.name,
        letterDataType = BarnepensjonUtAvFengselsoppholdDTO::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - ut av fengselsopphold",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                Bokmal to "Vi har vurdert/endret barnepensjonen din",
                Nynorsk to "",
                English to "",
            )
        }
        outline {
            includePhrase(Vedtak.BegrunnelseForVedtaket)
            includePhrase(Barnepensjon.FyllInnHer)
            includePhrase(
                Lover.MuligEtterbetaling(
                    paragraf = Expression.Literal("FYLL INN HER"),
                    erEtterbetaling = erEtterbetalingMerEnnTreeMaaneder,
                ),
            )
            includePhrase(
                Barnepensjon.SlikHarViBeregnetPensjonenDin(
                    utbetalingsinfo.beregningsperioder,
                    utbetalingsinfo.soeskenjustering,
                    utbetalingsinfo.antallBarn,
                ),
            )
        }
    }
}
