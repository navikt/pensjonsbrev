package no.nav.pensjon.etterlatte.maler

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brevbaker.api.model.*
import no.nav.pensjon.etterlatte.*
import no.nav.pensjon.etterlatte.maler.EtterlatteBrevDtoSelectors.navn
import java.time.LocalDate

data class EtterlatteBrevDto(val navn: String)

data class BarnepensjonVedtakDTO(
    val utbetalingsinfo: Utbetalingsinfo,
    val avdoed: AvdoedEYB,
    val avsender: Avsender,
    val mottaker: Mottaker,
    val attestant: Attestant? = null,
) {

    data class Utbetalingsinfo(
        val antallBarn: Int,
        val beloep: Kroner,
        val soeskenjustering: Boolean,
        val virkningsdato: LocalDate,
        val beregningsperioder: List<Beregningsperiode>,
    )

    data class Beregningsperiode(
        val datoFOM: LocalDate,
        val datoTOM: LocalDate?,
        val grunnbeloep: Kroner,
        val antallBarn: Int,
        var utbetaltBeloep: Kroner,
    )

    data class AvdoedEYB(
        val navn: String,
        val doedsdato: LocalDate,
    )

    data class Avsender(
        val kontor: String,
        val adresse: String,
        val postnummer: String,
        val telefonnummer: Telefonnummer,
        val saksbehandler: String,
    )

    data class Mottaker(
        val navn: String,
        val adresse: String,
        val postnummer: String,
        val poststed: String,
        val land: String,
    )

    data class Attestant(
        val navn: String,
        val kontor: String,
    )

}

@TemplateModelHelpers
object EtterlatteBrev: EtterlatteTemplate<EtterlatteBrevDto> {
    override val kode = EtterlatteBrevKode.A_LETTER

    override val template = createTemplate(
        name = kode.name,
        letterDataType =  EtterlatteBrevDto::class,
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata("Et etterlatte brev", false, LetterMetadata.Distribusjonstype.VEDTAK, LetterMetadata.Brevtype.VEDTAKSBREV),
    ) {
        title { text(Bokmal to "Et eksempel brev") }

        outline {
            paragraph {
                textExpr(Bokmal to "Dette er et eksempelbrev til ".expr() + navn + ".")
            }
        }
    }
}