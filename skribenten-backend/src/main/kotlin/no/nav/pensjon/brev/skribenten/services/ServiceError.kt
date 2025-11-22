package no.nav.pensjon.brev.skribenten.services

/**
 * Signaliserer server feil i tjenestelag. Typisk 5xx-status.
 */
open class ServiceError(override val message: String, cause: Throwable? = null) : Exception(message, cause)