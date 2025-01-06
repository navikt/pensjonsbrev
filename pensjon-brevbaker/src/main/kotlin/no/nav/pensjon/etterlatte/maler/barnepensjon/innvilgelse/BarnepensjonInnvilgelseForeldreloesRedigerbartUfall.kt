package no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.Delmal
import no.nav.pensjon.etterlatte.maler.ForskjelligAvdoedPeriode
import no.nav.pensjon.etterlatte.maler.RedigerbartUtfallBrevDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonForeldreloesRedigerbarDTOSelectors.erEtterbetaling
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonForeldreloesRedigerbarDTOSelectors.erGjenoppretting
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonForeldreloesRedigerbarDTOSelectors.erSluttbehandling
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonForeldreloesRedigerbarDTOSelectors.flerePerioder
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonForeldreloesRedigerbarDTOSelectors.forskjelligAvdoedPeriode
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonForeldreloesRedigerbarDTOSelectors.harUtbetaling
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonForeldreloesRedigerbarDTOSelectors.sisteBeregningsperiodeBeloep
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonForeldreloesRedigerbarDTOSelectors.sisteBeregningsperiodeDatoFom
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonForeldreloesRedigerbarDTOSelectors.vedtattIPesys
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonForeldreloesRedigerbarDTOSelectors.virkningsdato
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.BarnepensjonForeldreloesFraser
import java.time.LocalDate

data class BarnepensjonForeldreloesRedigerbarDTO(
    val virkningsdato: LocalDate,
    val sisteBeregningsperiodeBeloep: Kroner,
    val sisteBeregningsperiodeDatoFom: LocalDate,
    val erEtterbetaling: Boolean,
    val flerePerioder: Boolean,
    val harUtbetaling: Boolean,
    val erGjenoppretting: Boolean,
    val vedtattIPesys: Boolean,
    val forskjelligAvdoedPeriode: ForskjelligAvdoedPeriode? = null,
    val erSluttbehandling: Boolean = false
) : RedigerbartUtfallBrevDTO

@TemplateModelHelpers
object BarnepensjonInnvilgelseForeldreloesRedigerbartUfall :
    EtterlatteTemplate<BarnepensjonForeldreloesRedigerbarDTO>, Delmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BP_INNVILGELSE_UTFALL_FORELDRELOES
    override val template = createTemplate(
        name = kode.name,
        letterDataType = BarnepensjonForeldreloesRedigerbarDTO::class,
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - innvilgelse - Foreldreløs",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(Language.Bokmal to "", Language.Nynorsk to "", Language.English to "")
        }
        outline {
            includePhrase(
                BarnepensjonForeldreloesFraser.Vedtak(
                    virkningstidspunkt = virkningsdato,
                    sistePeriodeBeloep = sisteBeregningsperiodeBeloep,
                    sistePeriodeFom = sisteBeregningsperiodeDatoFom,
                    flerePerioder = flerePerioder,
                    harUtbetaling = harUtbetaling,
                    vedtattIPesys = vedtattIPesys,
                    erGjenoppretting = erGjenoppretting,
                    forskjelligAvdoedPeriode = forskjelligAvdoedPeriode,
                    erSluttbehandling = erSluttbehandling,
                )
            )
            includePhrase(
                BarnepensjonForeldreloesFraser.BegrunnelseForVedtaketRedigerbart(erEtterbetaling, vedtattIPesys),
            )
        }
    }
}
