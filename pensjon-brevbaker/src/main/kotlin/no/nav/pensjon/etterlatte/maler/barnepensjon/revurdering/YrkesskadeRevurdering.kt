package no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.Utbetalingsinfo
import no.nav.pensjon.etterlatte.maler.UtbetalingsinfoSelectors.antallBarn
import no.nav.pensjon.etterlatte.maler.UtbetalingsinfoSelectors.beloep
import no.nav.pensjon.etterlatte.maler.UtbetalingsinfoSelectors.beregningsperioder
import no.nav.pensjon.etterlatte.maler.UtbetalingsinfoSelectors.soeskenjustering
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingYrkesskadeDTOSelectors.utbetalingsinfo
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingYrkesskadeDTOSelectors.virkningsdato
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingYrkesskadeDTOSelectors.yrkesskadeErDokumentert
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.Barnepensjon
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.RevurderingYrkesskadeFraser
import no.nav.pensjon.etterlatte.maler.fraser.common.Vedtak
import java.time.LocalDate

data class BarnepensjonRevurderingYrkesskadeDTO(
    val utbetalingsinfo: Utbetalingsinfo,
    val yrkesskadeErDokumentert: Boolean,
    val virkningsdato: LocalDate,
)

@TemplateModelHelpers
object YrkesskadeRevurdering : EtterlatteTemplate<BarnepensjonRevurderingYrkesskadeDTO> {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_REVURDERING_YRKESSKADE

    override val template = createTemplate(
        name = kode.name,
        letterDataType = BarnepensjonRevurderingYrkesskadeDTO::class,
        languages = languages(Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - yrkesskade",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            showIf(yrkesskadeErDokumentert) {
                text(
                    Bokmal to "Vi har endret barnepensjonen din",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }.orShow {
                text(
                    Bokmal to "Vi har vurdert barnepensjonen din",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }
        }
        outline {
            includePhrase(Vedtak.BegrunnelseForVedtaket)
            includePhrase(RevurderingYrkesskadeFraser.Begrunnelse(yrkesskadeErDokumentert, virkningsdato, utbetalingsinfo.beloep))
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