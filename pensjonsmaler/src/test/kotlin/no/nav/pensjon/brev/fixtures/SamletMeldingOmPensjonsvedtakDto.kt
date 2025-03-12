package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.Landkode
import no.nav.pensjon.brev.api.model.maler.P1Person
import no.nav.pensjon.brev.api.model.maler.SamletMeldingOmPensjonsvedtakDto

fun createSamletMeldingOmPensjonsvedtakDto() =
    SamletMeldingOmPensjonsvedtakDto(
        holder = P1Person(
            fornavn = "Peder",
            etternavn = "Ås",
            etternavnVedFoedsel = "Aas",
            adresselinje = "Lillevik Torg",
            poststed = "Lillevik",
            postnummer = "4321",
            landkode = Landkode(landkode = "NO"),
        ),
        insuredPerson = P1Person(
            fornavn = "Lars",
            etternavn = "Holm",
            etternavnVedFoedsel = "Kirkerud",
            adresselinje = "Storgata 1",
            poststed = "Lillevik vestre",
            postnummer = "4320",
            landkode = Landkode(landkode = "NO"),
        ),
        sakstype = Sakstype.ALDER,
        innvilgedePensjoner = listOf(),
    )