package no.nav.pensjon.brev.skribenten.services

import io.ktor.http.HttpStatusCode

/**
 * Signaliserer server feil i tjenestelag. Typisk 5xx-status.
 */
open class ServiceException(
    override val message: String,
    cause: Throwable? = null,
    val status: HttpStatusCode = HttpStatusCode.InternalServerError
) : Exception(message, cause)