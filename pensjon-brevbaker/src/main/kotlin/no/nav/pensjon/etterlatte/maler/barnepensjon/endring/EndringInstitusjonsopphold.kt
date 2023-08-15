package no.nav.pensjon.etterlatte.maler.barnepensjon.endring

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.Delmal
import no.nav.pensjon.etterlatte.maler.Utbetalingsinfo
import no.nav.pensjon.etterlatte.maler.barnepensjon.endring.BarnepensjonEndringInstitusjonsoppholdDTOSelectors.innlagtdato
import no.nav.pensjon.etterlatte.maler.barnepensjon.endring.BarnepensjonEndringInstitusjonsoppholdDTOSelectors.kronebeloep
import no.nav.pensjon.etterlatte.maler.barnepensjon.endring.BarnepensjonEndringInstitusjonsoppholdDTOSelectors.prosent
import no.nav.pensjon.etterlatte.maler.barnepensjon.endring.BarnepensjonEndringInstitusjonsoppholdDTOSelectors.utskrevetdato
import no.nav.pensjon.etterlatte.maler.barnepensjon.endring.BarnepensjonEndringInstitusjonsoppholdDTOSelectors.virkningsdato
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.Institusjonsoppholdfraser
import no.nav.pensjon.etterlatte.maler.fraser.common.Vedtak
import java.time.LocalDate

data class BarnepensjonEndringInstitusjonsoppholdDTO(
    val utbetalingsinfo: Utbetalingsinfo,
    val erEtterbetalingMerEnnTreMaaneder: Boolean,
    val virkningsdato: LocalDate,
    val prosent: Int?,
    val kronebeloep: Kroner,
    val innlagtdato: LocalDate?,
    val utskrevetdato: LocalDate?,
)

@TemplateModelHelpers
object EndringInstitusjonsopphold : EtterlatteTemplate<BarnepensjonEndringInstitusjonsoppholdDTO>, Delmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_REVURDERING_INSTITUSJONSOPPHOLD

    override val template = createTemplate(
        name = UtAvFengselsopphold.kode.name,
        letterDataType = BarnepensjonEndringInstitusjonsoppholdDTO::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - institusjonsopphold",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                Bokmal to "Vi har endret barnepensjonen din",
                Nynorsk to "",
                English to "",
            )
        }
        outline {
            includePhrase(Vedtak.BegrunnelseForVedtaket)
            ifNotNull(prosent) {
                includePhrase(
                    Institusjonsoppholdfraser.HarDokumentertUtgiftBarnepensjonBlirRedusertMedMindreEnn90Prosent(
                        virkningsdato,
                        it,
                        kronebeloep,
                    ),
                )
            }
            includePhrase(
                Institusjonsoppholdfraser.InnlagtVanligSats(
                    virkningsdato,
                    kronebeloep,
                ),
            )
            ifNotNull(innlagtdato) {
                includePhrase(
                    Institusjonsoppholdfraser.InnlagtPaaNyttInnen3Maaneder(
                        it,
                        virkningsdato,
                        kronebeloep,
                    ),
                )
            }
            ifNotNull(innlagtdato) {
                includePhrase(
                    Institusjonsoppholdfraser.HarDokumentertUtgiftIngenReduksjonVanligUtbetaling(
                        it,
                        virkningsdato,
                        kronebeloep,
                    ),
                )
            }
            includePhrase(Institusjonsoppholdfraser.UtskrevetVanligSats(virkningsdato, kronebeloep))
            ifNotNull(utskrevetdato) {
                includePhrase(
                    Institusjonsoppholdfraser.UtskrevetHarDokumenterteUtgiftIngenReduksjonHarVaertVanligUtbetaling(
                        it,
                        virkningsdato,
                        kronebeloep,
                    ),
                )
            }
        }
    }
}
