package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.Sakstype

data class SamletMeldingOmPensjonsvedtakDto(
    val holder: P1Person,
    val insuredPerson: P1Person,
    val sakstype: Sakstype,
    val innvilgedePensjoner: List<InnvilgetPensjon>,
    val avslaattePensjoner: List<AvslaattPensjon>,
) : BrevbakerBrevdata

data class P1Person(
    val fornavn: String,
    val etternavn: String,
    val etternavnVedFoedsel: String?,
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
    val s: String
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