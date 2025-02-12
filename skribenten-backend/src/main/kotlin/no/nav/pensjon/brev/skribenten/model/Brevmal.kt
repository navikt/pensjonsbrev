package no.nav.pensjon.brev.skribenten.model

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sakstype.*
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkategori
import no.nav.pensjon.brev.skribenten.services.BrevdataDto
import no.nav.pensjon.brev.skribenten.services.SpraakKode
import no.nav.pensjon.brevbaker.api.model.LanguageCode

enum class BrevSystem { EXSTREAM, DOKSYS, BREVBAKER }

interface LetterMetadata {
    val brevkode: String
    val brevkontekst: TemplateDescription.Brevkontekst?
    val isForSakskontekst: Boolean get() = brevkontekst in setOf(TemplateDescription.Brevkontekst.SAK, TemplateDescription.Brevkontekst.ALLE)
    val isForVedtakskontekst: Boolean get() = brevkontekst in setOf(TemplateDescription.Brevkontekst.VEDTAK, TemplateDescription.Brevkontekst.ALLE)
    val isRedigerbart: Boolean

    fun isForSakstype(sakstype: Sakstype): Boolean

    /**
     * Svarer på om brevmalen er relevant for angitt sakstype når regelverket brukt i saken
     * er som angitt av forGammeltRegelverk.
     *
     * Om forGammeltRegelverk er null så betyr det at man ikke har hentet fra Pesys,
     * dette er kun fordi det per nå bare er relevant for brevmaler for ALDER.
     */
    fun isRelevantRegelverk(
        sakstype: Sakstype,
        forGammeltRegelverk: Boolean?,
    ): Boolean

    fun toApi(): Api.Brevmal

    /**
     * Brevmetadata om brevmaler fra pensjon-brevmetadata (Exstream/Doksys)
     */
    data class Legacy(val data: BrevdataDto, private val hasSakstype: Sakstype) : LetterMetadata {
        override val brevkode: String get() = data.brevkodeIBrevsystem
        override val brevkontekst: TemplateDescription.Brevkontekst?
            get() =
                when (data.brevkontekst) {
                    BrevdataDto.BrevkontekstCode.ALLTID -> TemplateDescription.Brevkontekst.ALLE
                    BrevdataDto.BrevkontekstCode.SAK -> TemplateDescription.Brevkontekst.SAK
                    BrevdataDto.BrevkontekstCode.VEDTAK -> TemplateDescription.Brevkontekst.VEDTAK
                    null -> null
                }
        override val isRedigerbart: Boolean get() = data.redigerbart

        override fun isForSakstype(sakstype: Sakstype) = sakstype == hasSakstype

        override fun isRelevantRegelverk(
            sakstype: Sakstype,
            forGammeltRegelverk: Boolean?,
        ): Boolean =
            when (sakstype) {
                ALDER ->
                    if (forGammeltRegelverk == true) {
                        data.brevregeltype?.gjelderGammeltRegelverk() ?: true
                    } else {
                        data.brevregeltype?.gjelderNyttRegelverk() ?: true
                    }

                UFOREP -> data.brevregeltype?.gjelderGammeltRegelverk() ?: true
                BARNEP, AFP, AFP_PRIVAT, FAM_PL, GAM_YRK, GENRL, GJENLEV, GRBL, KRIGSP, OMSORG -> true
            }

        override fun toApi(): Api.Brevmal =
            with(data) {
                Api.Brevmal(
                    name = dekode,
                    id = brevkodeIBrevsystem,
                    spraak = sprak ?: emptyList(),
                    brevsystem =
                        when (brevsystem) {
                            BrevdataDto.BrevSystem.DOKSYS -> BrevSystem.DOKSYS
                            BrevdataDto.BrevSystem.GAMMEL -> BrevSystem.EXSTREAM
                        },
                    brevkategori = BrevmalOverstyring.kategori[brevkodeIBrevsystem]?.toKategoriTekst() ?: this.brevkategori?.toKategoriTekst(),
                    dokumentkategoriCode = this.dokumentkategori,
                    redigerbart = redigerbart,
                    redigerbarBrevtittel = isRedigerbarBrevtittel(),
                )
            }

        private fun BrevdataDto.BrevkategoriCode.toKategoriTekst() =
            when (this) {
                BrevdataDto.BrevkategoriCode.BREV_MED_SKJEMA -> "Brev med skjema"
                BrevdataDto.BrevkategoriCode.INFORMASJON -> "Informasjon"
                BrevdataDto.BrevkategoriCode.INNHENTE_OPPL -> "Innhente opplysninger"
                BrevdataDto.BrevkategoriCode.NOTAT -> "Notat"
                BrevdataDto.BrevkategoriCode.OVRIG -> "Øvrig"
                BrevdataDto.BrevkategoriCode.VARSEL -> "Varsel"
                BrevdataDto.BrevkategoriCode.VEDTAK -> "Vedtak"
            }
    }

    /**
     * Brevmetadata om brevmaler fra Brevbaker
     */
    data class Brevbaker(val data: TemplateDescription.Redigerbar) : LetterMetadata {
        override fun toApi(): Api.Brevmal =
            with(data) {
                Api.Brevmal(
                    name = metadata.displayTitle,
                    id = name,
                    brevsystem = BrevSystem.BREVBAKER,
                    spraak = this.languages.map { it.toSpraakKode() },
                    brevkategori = kategori.toKategoriTekst(),
                    dokumentkategoriCode = metadata.brevtype.toDokumentkategoriCode(),
                    redigerbart = true,
                    redigerbarBrevtittel = false,
                )
            }

        override val brevkontekst: TemplateDescription.Brevkontekst get() = data.brevkontekst
        override val isRedigerbart: Boolean = true
        override val brevkode: String get() = data.name

        override fun isForSakstype(sakstype: Sakstype) = sakstype in data.sakstyper

        override fun isRelevantRegelverk(
            sakstype: Sakstype,
            forGammeltRegelverk: Boolean?,
        ) = true

        private fun LanguageCode.toSpraakKode(): SpraakKode =
            when (this) {
                LanguageCode.BOKMAL -> SpraakKode.NB
                LanguageCode.NYNORSK -> SpraakKode.NN
                LanguageCode.ENGLISH -> SpraakKode.EN
            }

        private fun no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.toDokumentkategoriCode(): BrevdataDto.DokumentkategoriCode =
            when (this) {
                no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.VEDTAKSBREV -> BrevdataDto.DokumentkategoriCode.VB
                no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.INFORMASJONSBREV -> BrevdataDto.DokumentkategoriCode.IB
            }
    }
}

private fun Brevkategori.toKategoriTekst() =
    when (this) {
        Brevkategori.ETTEROPPGJOER -> "Etteroppgjør"
        Brevkategori.FOERSTEGANGSBEHANDLING -> "Førstegangsbehandling"
        Brevkategori.VEDTAK_ENDRING_OG_REVURDERING -> "Vedtak - endring og revurdering"
        Brevkategori.VEDTAK_FLYTTE_MELLOM_LAND -> "Vedtak - flytte mellom land"
        Brevkategori.SLUTTBEHANDLING -> "Sluttbehandling"
        Brevkategori.INFORMASJONSBREV -> "Informasjonsbrev"
        Brevkategori.VARSEL -> "Varsel"
        Brevkategori.VEDTAK_EKSPORT -> "Vedtak - eksport"
        Brevkategori.OMSORGSOPPTJENING -> "Omsorgsopptjening"
        Brevkategori.UFOEREPENSJON -> "Uførepensjon"
        Brevkategori.INNHENTE_OPPLYSNINGER -> "Innhente opplysninger"
        Brevkategori.LEVEATTEST -> "Leveattest"
        Brevkategori.FEILUTBETALING -> "Feilutbetaling"
        Brevkategori.KLAGE_OG_ANKE -> "Klage og anke"
        Brevkategori.POSTERINGSGRUNNLAG -> "Posteringsgrunnlag"
        Brevkategori.FRITEKSTBREV -> "Fritekstbrev"
    }
