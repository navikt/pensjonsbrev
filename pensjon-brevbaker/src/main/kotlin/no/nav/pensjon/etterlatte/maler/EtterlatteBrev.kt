package no.nav.pensjon.etterlatte.maler

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonValue
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

data class ManueltBrevDTO(
    val innhold: List<Element> = emptyList()
) {
    data class Element(
        val type: ElementType,
        val children: List<InnerElement> = emptyList()
    )

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class InnerElement(
        val type: ElementType? = null,
        val text: String? = null,
        val children: List<InnerElement>? = null,
    )

    enum class ElementType(@JsonValue val value: String) {
        HEADING_TWO("heading-two"),
        HEADING_THREE("heading-three"),
        PARAGRAPH("paragraph"),
        BULLETED_LIST("bulleted-list"),
        LIST_ITEM("list-item")
    }
}

data class OMSInnvilgelseDTO(
    val utbetalingsinfo: Utbetalingsinfo,
    val avdoed: Avdoed
) {

    data class Utbetalingsinfo(
        val beloep: Kroner,
        val inntekt: Kroner,
        val virkningsdato: LocalDate,
        val grunnbeloep: Kroner,
        val beregningsperioder: List<Beregningsperiode>,
    )

    data class Beregningsperiode(
        val datoFOM: LocalDate,
        val datoTOM: LocalDate?,
        val inntekt: Kroner,
        var utbetaltBeloep: Kroner,
    )
}

data class BarnepensjonInnvilgelseDTO(
    val utbetalingsinfo: Utbetalingsinfo,
    val avdoed: Avdoed
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
}

data class Avdoed(
    val navn: String,
    val doedsdato: LocalDate,
)
