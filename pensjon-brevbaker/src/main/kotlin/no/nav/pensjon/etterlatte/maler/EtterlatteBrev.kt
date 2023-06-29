package no.nav.pensjon.etterlatte.maler

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonValue
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import no.nav.pensjon.etterlatte.*
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
    val avkortingsinfo: Avkortingsinfo,
    val avdoed: Avdoed
)

data class BarnepensjonInnvilgelseDTO(
    val utbetalingsinfo: Utbetalingsinfo,
    val avkortingsinfo: Avkortingsinfo? = null,
    val avdoed: Avdoed
)

data class Avkortingsinfo(
    val grunnbeloep: Kroner,
    val inntekt: Kroner,
    val virkningsdato: LocalDate,
    val beregningsperioder: List<AvkortetBeregningsperiode>
)

enum class EndringIUtbetaling {
    OEKES, REDUSERES, SAMME
}

enum class BarnepensjonSoeskenjusteringGrunn(val endring: EndringIUtbetaling) {
    NYTT_SOESKEN(EndringIUtbetaling.REDUSERES),
    SOESKEN_DOER(EndringIUtbetaling.OEKES),
    SOESKEN_INN_INSTITUSJON_INGEN_ENDRING(EndringIUtbetaling.SAMME),
    SOESKEN_INN_INSTITUSJON_ENDRING(EndringIUtbetaling.OEKES),
    SOESKEN_UT_INSTITUSJON(EndringIUtbetaling.REDUSERES),
    FORPLEID_ETTER_BARNEVERNSLOVEN(EndringIUtbetaling.OEKES),
    SOESKEN_BLIR_ADOPTERT(EndringIUtbetaling.OEKES)
}

data class BarnepensjonRevurderingSoeskenjusteringDTO(
    val utbetalingsinfo: Utbetalingsinfo,
    val grunnForJustering: BarnepensjonSoeskenjusteringGrunn
)

data class BarnepensjonRevurderingAdopsjonDTO(
    val virkningsdato: LocalDate,
    val adoptertAv: Navn
)

data class BarnepensjonRevurderingOmgjoeringAvFarskapDTO(
    val virkningsdato: LocalDate,
    val naavaerendeFar: Navn,
    var forrigeFar: Navn,
    val forrigeVirkningsdato: LocalDate
)

data class Navn(val fornavn: String, val mellomnavn: String? = null, val etternavn: String)

data class AvkortetBeregningsperiode(
    val datoFOM: LocalDate,
    val datoTOM: LocalDate?,
    val inntekt: Kroner,
    val utbetaltBeloep: Kroner
)
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

data class Avdoed(
    val navn: String,
    val doedsdato: LocalDate,
)
