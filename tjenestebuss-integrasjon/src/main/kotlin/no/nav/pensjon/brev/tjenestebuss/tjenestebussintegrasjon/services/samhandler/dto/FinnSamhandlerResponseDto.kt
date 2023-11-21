package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.samhandler.dto

import no.nav.lib.pen.psakpselv.fault.FaultPenBase

sealed class FinnSamhandlerResponseDto {
    data class Success(val samhandlere: List<Samhandler>): FinnSamhandlerResponseDto() {
        data class Samhandler(
            val navn: String,
            val samhandlerType: String,
            val offentligId: String,
            val idType: String) : FinnSamhandlerResponseDto()
    }
    data class Failure(
        val message: String,
        val source: String,
        val type: String,
        val cause: String,
    ) : FinnSamhandlerResponseDto() {
        constructor(faultPenBase: FaultPenBase): this(
            faultPenBase.errorMessage,
            faultPenBase.errorSource,
            faultPenBase.errorType,
            faultPenBase.rootCause,
        )
    }
}