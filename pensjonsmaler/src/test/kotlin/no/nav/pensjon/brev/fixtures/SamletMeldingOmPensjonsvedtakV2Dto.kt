package no.nav.pensjon.brev.fixtures

import no.nav.brev.Landkode
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.P1RedigerbarDto
import no.nav.pensjon.brev.api.model.maler.P1RedigerbarDto.*
import no.nav.pensjon.brev.api.model.maler.P1RedigerbarDto.AvslaattPensjon
import no.nav.pensjon.brev.api.model.maler.SamletMeldingOmPensjonsvedtakV2Dto
import no.nav.pensjon.brevbaker.api.model.Telefonnummer
import java.time.LocalDate
import java.time.Month


fun createSamletMeldingOmPensjonsvedtakV2Dto() =
    SamletMeldingOmPensjonsvedtakV2Dto(
        saksbehandlerValg = EmptySaksbehandlerValg,
        pesysData = SamletMeldingOmPensjonsvedtakV2Dto.PesysData(
            sakstype = Sakstype.ALDER,
        ),
        p1Vedlegg = createP1VedleggDto()
    )

private val ADRESSE_EKSEMPEL = "Lillevik Torgvei 1\n4321\nLillevik Østre\nDanmark"

fun createP1VedleggDto() = P1RedigerbarDto(
    innehaver = P1Person(
        fornavn = "Peder",
        etternavn = "Ås",
        etternavnVedFoedsel = "Aas",
        foedselsdato = null,
        adresselinje = "Lillevik Torg",
        poststed = Poststed("Lillevik"),
        postnummer = Postnummer("4321"),
        landkode = Landkode("NO")
    ),
    forsikrede = P1Person(
        fornavn = "Lars",
        etternavn = "Holm",
        etternavnVedFoedsel = "Kirkerud",
        foedselsdato = LocalDate.of(1990, Month.MARCH, 1),
        adresselinje = "Storgata 1",
        poststed = Poststed("Lillevik vestre"),
        postnummer = Postnummer("4320"),
        landkode = Landkode("NO"),
    ),
    sakstype = Sakstype.ALDER,
    innvilgedePensjoner = (0..<6).map { innvilgetPensjon },
    avslaattePensjoner = (0..<6).map { avslaattPensjon },
    utfyllendeInstitusjon = UtfyllendeInstitusjon(
        navn = "NFP",
        adresselinje = "Lilleviksgrenda",
        poststed = Poststed("Lillevik"),
        postnummer = Postnummer("4322"),
        landkode = Landkode("NO"),
        institusjonsID = "NFPL1",
        faksnummer = "12134412",
        telefonnummer = Telefonnummer("+123 45678901"),
        epost = Epost("lars.holm@tøys.nfp.no"),
        dato = LocalDate.now(),
    )
)

private val nay =
    Institusjon(
        institusjonsnavn = "NAY",
        pin = "1234",
        saksnummer = "1234",
        vedtaksdato = "1.Januar 2020",
        land = "Norge"
    )


private val innvilgetPensjon = InnvilgetPensjon(
    institusjon = nay,
    pensjonstype = Pensjonstype.Etterlatte,
    vurderingsperiode = "en måned",
    datoFoersteUtbetaling = "TODO()",
    utbetalt = "1000 Kroner fra en dato",
    grunnlagInnvilget = GrunnlagInnvilget.IHenholdTilNasjonalLovgivning,
    reduksjonsgrunnlag = null,
    adresseNyVurdering = ADRESSE_EKSEMPEL,
)


private val avslaattPensjon = AvslaattPensjon(
    institusjon = nay,
    pensjonstype = Pensjonstype.Etterlatte,
    avslagsbegrunnelse = Avslagsbegrunnelse.OpptjeningsperiodePaaMindreEnnEttAar,
    vurderingsperiode = "en måned",
    adresseNyVurdering = ADRESSE_EKSEMPEL,
)


