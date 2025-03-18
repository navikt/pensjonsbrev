package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.Sakstype
import java.time.LocalDate

data class SamletMeldingOmPensjonsvedtakDto(
    val holder: P1Person,
    val insuredPerson: P1Person,
    val sakstype: Sakstype,
    val innvilgedePensjoner: List<InnvilgetPensjon>,
    val avslaattePensjoner: List<AvslaattPensjon>,
    val institution: Institution,
) : BrevbakerBrevdata

data class P1Person(
    val fornavn: String,
    val etternavn: String,
    val etternavnVedFoedsel: String?,
    val foedselsdato: String?,
    val adresselinje: String,
    val poststed: String,
    val postnummer: String,
    val landkode: String,
) {
    init {
        require(landkode.length == 2)
    }
}

data class InnvilgetPensjon(
    val institusjon: String,
    val type: Sakstype,
    val datoFoersteUtbetaling: String,
    val bruttobeloep: String, //TODO type ordentleg?
    val grunnlagInnvilget: GrunnlagInnvilget,
    val reduksjonsgrunnlag: Reduksjonsgrunnlag?,
    val vurderingsperiode: String,
    val adresseNyVurdering: Adresse,
)

data class AvslaattPensjon(
    val institusjon: String,
    val type: Sakstype,
    val avslagsbegrunnelse: Avslagsbegrunnelse,
    val vurderingsperiode: String,
    val adresseNyVurdering: Adresse,
)

enum class GrunnlagInnvilget(val nummer: Int) {
    NationalLegislation(4),
    ProRata(5),
    LessThanOneYear(6)
}

enum class Reduksjonsgrunnlag(val nummer: Int) {
    InViewOfAnotherBenefitOrIncome(7),
    InViewOfOverlappingOfCreditedPeriods(8)
}

enum class Avslagsbegrunnelse(val nummer: Int) {
    NoInsurancePeriods(4),
    InsurancePeriodsLessThanOneYear(5),
    QualifyingPeriodNotCompletedOrEligibilityRequirementsNotMet(6),
    NoPartialDisabilityOrInvalidityWasFound(7),
    IncomeCeilingIsExceeded(8),
    PensionAgeNotYetReached(9),
    OtherReasons(10)
}

data class Adresse(
    val adresselinje1: String,
    val adresselinje2: String?,
    val adresselinje3: String?,
    val landkode: String,
    val postnummer: String,
    val poststed: String,
) {
    init {
        require(landkode.length == 2)
    }
}

data class Institution(
    val name: String,
    val street: String,
    val town: String,
    val postcode: String,
    val countryCode: String,
    val institutionID: String,
    val officeFax: String?,
    val officePhone: String?,
    val email: String,
    val date: LocalDate,
    val signature: String,
)