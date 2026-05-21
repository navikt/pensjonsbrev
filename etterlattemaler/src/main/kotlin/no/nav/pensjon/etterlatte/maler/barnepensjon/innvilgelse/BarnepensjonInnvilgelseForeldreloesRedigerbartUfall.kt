package no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.Delmal
import no.nav.pensjon.etterlatte.maler.ForskjelligAvdoedPeriode
import no.nav.pensjon.etterlatte.maler.RedigerbartUtfallBrevDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonForeldreloesRedigerbarDTOSelectors.data
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonForeldreloesRedigerbarDataSelectors.erEtterbetaling
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonForeldreloesRedigerbarDataSelectors.erGjenoppretting
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonForeldreloesRedigerbarDataSelectors.erSluttbehandling
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonForeldreloesRedigerbarDataSelectors.flerePerioder
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonForeldreloesRedigerbarDataSelectors.forskjelligAvdoedPeriode
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonForeldreloesRedigerbarDataSelectors.harUtbetaling
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonForeldreloesRedigerbarDataSelectors.sisteBeregningsperiodeBeloep
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonForeldreloesRedigerbarDataSelectors.sisteBeregningsperiodeDatoFom
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonForeldreloesRedigerbarDataSelectors.vedtattIPesys
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonForeldreloesRedigerbarDataSelectors.virkningsdato
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.BarnepensjonForeldreloesFraser
import java.time.LocalDate

data class BarnepensjonForeldreloesRedigerbarData(
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
)

data class BarnepensjonForeldreloesRedigerbarDTO(
    override val data: BarnepensjonForeldreloesRedigerbarData,
) : RedigerbartUtfallBrevDTO

@TemplateModelHelpers
object BarnepensjonInnvilgelseForeldreloesRedigerbartUfall :
    EtterlatteTemplate<BarnepensjonForeldreloesRedigerbarDTO>, Delmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_INNVILGELSE_UTFALL_FORELDRELOES
    override val template = createTemplate(
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - innvilgelse - Foreldreløs",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(bokmal { +"" }, nynorsk { +"" }, english { +"" })
        }
        outline {
            includePhrase(
                BarnepensjonForeldreloesFraser.Vedtak(
                    virkningstidspunkt = data.virkningsdato,
                    sistePeriodeBeloep = data.sisteBeregningsperiodeBeloep,
                    sistePeriodeFom = data.sisteBeregningsperiodeDatoFom,
                    flerePerioder = data.flerePerioder,
                    harUtbetaling = data.harUtbetaling,
                    vedtattIPesys = data.vedtattIPesys,
                    erGjenoppretting = data.erGjenoppretting,
                    forskjelligAvdoedPeriode = data.forskjelligAvdoedPeriode,
                    erSluttbehandling = data.erSluttbehandling,
                )
            )
            includePhrase(
                BarnepensjonForeldreloesFraser.BegrunnelseForVedtaketRedigerbart(data.erEtterbetaling, data.vedtattIPesys),
            )
        }
    }
}
