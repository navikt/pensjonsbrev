package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.jackson.jackson
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevkode
import no.nav.pensjon.brevbaker.api.model.Felles
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.TemplateModelSpecification


open class FakeNavansattService(
    val harTilgangTilEnhet: Map<Pair<String, String>, Boolean> = emptyMap(),
    val navansatte: Map<String, String> = emptyMap(),
) : NavansattService {
    override suspend fun harTilgangTilEnhet(ansattId: String, enhetsId: String) =
        ServiceResult.Ok(harTilgangTilEnhet.getOrDefault(Pair(ansattId, enhetsId), false))

    override suspend fun hentNavansatt(ansattId: String): Navansatt? = navansatte[ansattId]?.let {
        Navansatt(
            emptyList(),
            it,
            it.split(' ').first(),
            it.split(' ').last(),
        )
    }
}

open class FakeNorg2Service(val enheter: Map<String, NavEnhet> = mapOf()) : Norg2Service {
    override suspend fun getEnhet(enhetId: String) = enheter[enhetId]
}

open class FakeSamhandlerService(val navn: Map<String, String> = mapOf()) : SamhandlerService {
    override suspend fun hentSamhandlerNavn(idTSSEkstern: String) = navn[idTSSEkstern]
}

open class FakeBrevmetadataService(
    val eblanketter: List<BrevdataDto> = listOf(),
    val brevmaler: List<BrevdataDto> = listOf(),
    val maler: Map<String, BrevdataDto> = mapOf(),
) : BrevmetadataService {
    override suspend fun getBrevmalerForSakstype(sakstype: Sakstype) = brevmaler

    override suspend fun getEblanketter() = eblanketter

    override suspend fun getMal(brevkode: String) = maler[brevkode] ?: TODO("Not implemented")

}

open class FakeBrevbakerService(
    val maler: List<TemplateDescription.Redigerbar> = listOf(),
    val redigerbareMaler: Map<RedigerbarBrevkode, TemplateDescription.Redigerbar> = mapOf(),
) : BrevbakerService {
    override suspend fun getModelSpecification(brevkode: Brevkode.Redigerbart): ServiceResult<TemplateModelSpecification> {
        TODO("Not yet implemented")
    }

    override suspend fun renderMarkup(
        brevkode: Brevkode.Redigerbart,
        spraak: LanguageCode,
        brevdata: RedigerbarBrevdata<*, *>,
        felles: Felles,
    ): ServiceResult<LetterMarkup> {
        TODO("Not yet implemented")
    }

    override suspend fun renderPdf(
        brevkode: Brevkode.Redigerbart,
        spraak: LanguageCode,
        brevdata: RedigerbarBrevdata<*, *>,
        felles: Felles,
        redigertBrev: LetterMarkup,
    ): ServiceResult<LetterResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getTemplates() = ServiceResult.Ok(maler)

    override suspend fun getRedigerbarTemplate(brevkode: Brevkode.Redigerbart) = redigerbareMaler[brevkode]

}

private val objectMapper = jacksonObjectMapper()

fun <T> settOppHttpClient(body: T): HttpClient =
    HttpClient(MockEngine {
        respond(
            content = objectMapper.writeValueAsString(body),
            status = HttpStatusCode.OK,
            headers = headersOf("Content-Type", "application/json")
        )

    }) {
        install(ContentNegotiation) {
            jackson {
                disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            }
        }
    }
