package no.nav.pensjon.brev.brevoppskrift.openapi

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.openapi.*
import no.nav.pensjon.brev.brevoppskrift.api.Problem
import no.nav.pensjon.brev.brevoppskrift.draftObjectMapper
import kotlin.reflect.typeOf

fun OpenApiDoc.withCommonResponses(): OpenApiDoc {
    val problemContentType = ContentType.Application.ProblemJson
    val responses = listOf(
        HttpStatusCode.BadRequest, HttpStatusCode.Unauthorized, HttpStatusCode.Forbidden,
        HttpStatusCode.NotFound, HttpStatusCode.Gone, HttpStatusCode.Conflict,
        HttpStatusCode.PayloadTooLarge, HttpStatusCode.UnsupportedMediaType,
        HttpStatusCode.UnprocessableEntity, HttpStatusCode.TooManyRequests,
        HttpStatusCode.InternalServerError, HttpStatusCode.ServiceUnavailable,
    ).associate { status ->
        status.value to ReferenceOr.Value(Response(
            description = "",
            content = mapOf(problemContentType to MediaType(schema = ReferenceOr.schema("Problem"))),
        ))
    }
    fun Operation.documented(): Operation = copy(
        responses = (this.responses ?: Responses()).copy(responses = responses + this.responses?.responses.orEmpty()),
        requestBody = when (val body = requestBody) {
            is ReferenceOr.Value -> ReferenceOr.Value(body.value.copy(required = true))
            else -> body
        },
    )
    return copy(
        components = (components ?: Components()).copy(
            schemas = components?.schemas.orEmpty() + (
                "Problem" to JacksonSchemaInference(draftObjectMapper).buildSchema(typeOf<Problem>())
            ),
        ),
        paths = paths.mapValues { entry ->
            when (val item = entry.value) {
                is ReferenceOr.Value -> ReferenceOr.Value(item.value.copy(
                    get = item.value.get?.documented(),
                    post = item.value.post?.documented(),
                    put = item.value.put?.documented(),
                    delete = item.value.delete?.documented(),
                    patch = item.value.patch?.documented(),
                    head = item.value.head?.documented(),
                    options = item.value.options?.documented(),
                    trace = item.value.trace?.documented(),
                ))
                else -> item
            }
        },
    )
}
