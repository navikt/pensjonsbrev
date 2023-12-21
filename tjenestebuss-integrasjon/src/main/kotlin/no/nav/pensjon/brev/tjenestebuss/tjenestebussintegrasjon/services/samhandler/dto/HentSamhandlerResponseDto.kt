package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.samhandler.dto

import no.nav.lib.pen.psakpselv.fault.FaultPenBase

sealed class HentSamhandlerResponseDto {
    data class Success(
        val navn: String,
        val samhandlerType: String,
        val offentligId: String,
        val idType: String) : HentSamhandlerResponseDto()
    data class Failure(
        val failureType: FailureType,
        val message: String,
        val source: String,
        val type: String,
        val cause: String,
    ) : HentSamhandlerResponseDto() {
        constructor(failureType: FailureType, faultPenBase: FaultPenBase): this(
            failureType,
            faultPenBase.errorMessage,
            faultPenBase.errorSource,
            faultPenBase.errorType,
            faultPenBase.rootCause,
        )
        enum class FailureType {
            GENERISK,
            IKKE_FUNNET,
        }
    }
}