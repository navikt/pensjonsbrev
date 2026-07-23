package no.nav.pensjon.brev.skribenten.brevredigering.domain

import no.nav.brev.BrevLandmodell
import no.nav.pensjon.brev.skribenten.db.P1DataTable
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Sakstype
import no.nav.pensjon.brevbaker.api.model.BrevbakerType
import no.nav.pensjon.brevbaker.api.model.PDFVedleggData
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.Entity
import org.jetbrains.exposed.v1.dao.EntityClass
import java.time.LocalDate


class P1Data(brevredigeringId: EntityID<BrevId>) : Entity<BrevId>(brevredigeringId) {
    var p1data by P1DataTable.p1data
    companion object : EntityClass<BrevId, P1Data>(P1DataTable)
}

data class P1RedigerbarDto(
    val innehaver: P1Person,
    val forsikrede: P1Person,
    val sakstype: Sakstype,
    val innvilgedePensjoner: List<InnvilgetPensjon>,
    val avslaattePensjoner: List<AvslaattPensjon>,
    val utfyllendeInstitusjon: UtfyllendeInstitusjon, // I praksis Nav eller Nav-enheten
) : PDFVedleggData {

    data class P1Person(
        val fornavn: String?,
        val etternavn: String?,
        val etternavnVedFoedsel: String?,
        val foedselsdato: LocalDate?,
        val adresselinje: String?,
        val poststed: Poststed?,
        val postnummer: Postnummer?,
        val landkode: BrevLandmodell.Landkode?,
    )

    data class InnvilgetPensjon(
        val institusjon: Institusjon?, // 3.1
        val pensjonstype: Pensjonstype?, //3.2
        val datoFoersteUtbetaling: LocalDate?, //3.3
        val utbetalt: String?, // 3.4
        val grunnlagInnvilget: GrunnlagInnvilget?, // 3.5
        val reduksjonsgrunnlag: Reduksjonsgrunnlag?, // 3.6
        val vurderingsperiode: String?, // 3.7
        val adresseNyVurdering: String?, // 3.8
    )

    data class AvslaattPensjon(
        val institusjon: Institusjon?, // 4.1
        val pensjonstype: Pensjonstype?, // 4.2
        val avslagsbegrunnelse: Avslagsbegrunnelse?, // 4.3
        val vurderingsperiode: String?, // 4.4
        val adresseNyVurdering: String?, // 4.5
    )

    enum class Pensjonstype(val nummer: Int) {
        Alder(1),
        Ufoere(2),
        Etterlatte(3)
    }

    enum class GrunnlagInnvilget(val nummer: Int) {
        IHenholdTilNasjonalLovgivning(4),
        ProRata(5),
        MindreEnnEttAar(6)
    }

    enum class Reduksjonsgrunnlag(val nummer: Int) {
        PaaGrunnAvAndreYtelserEllerAnnenInntekt(7),
        PaaGrunnAvOverlappendeGodskrevnePerioder(8)
    }

    enum class Avslagsbegrunnelse(val nummer: Int) {
        IngenOpptjeningsperioder(4),
        OpptjeningsperiodePaaMindreEnnEttAar(5),
        KravTilKvalifiseringsperiodeEllerAndreKvalifiseringskravErIkkeOppfylt(
            6
        ),
        VilkaarOmUfoerhetErIkkeOppfylt(7),
        InntektstakErOverskredet(8),
        PensjonsalderErIkkeNaadd(9),
        AndreAarsaker(10)
    }

    data class Institusjon(
        val institusjonsnavn: String?,
        val pin: String?,
        val saksnummer: String?,
        val vedtaksdato: String? = null,
        val datoForVedtak: LocalDate?,
        val land: String?,
    )

    data class UtfyllendeInstitusjon(
        val navn: String,
        val adresselinje: String,
        val poststed: Poststed,
        val postnummer: Postnummer,
        val landkode: BrevLandmodell.Landkode,
        val institusjonsID: String?,
        val faksnummer: String?,
        val telefonnummer: BrevbakerType.Telefonnummer?,
        val epost: Epost?,
        val dato: LocalDate? = null, // skal slettes
    )

    @JvmInline
    value class Postnummer(val value: String) {
        init {
            require(value.length < 30) { "Postnumre er jo ikke kjempelange. Denne er ${value.length} lang." }
        }
    }

    @JvmInline
    value class Poststed(val value: String) {
        init {
            require(value.length < 300) { "Poststed er ikke kjempelange. Denne er ${value.length} lang." }
        }
    }

    @JvmInline
    value class Epost(val value: String) {
        init {
            val parts = value.split("@", limit = 2)
            require(parts.size == 2) { "Epost må inneholde @" }
            val localPart = parts[0]
            val domain = parts[1]
            require(localPart.isNotBlank()) { "Epost må ha verdi før æ" }
            require(domain.isNotBlank() && domain.contains(".")) { "Epost må ha verdi etter @ og ha ." }
            require(domain.substringBefore(".").isNotBlank()) { "Epost må ha verdi før ." }
            require(domain.substringAfterLast(".").isNotBlank()) { "Epost må ha verdi etter ." }
        }
    }
}