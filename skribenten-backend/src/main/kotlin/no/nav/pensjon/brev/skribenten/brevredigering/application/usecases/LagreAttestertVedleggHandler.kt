package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.brevredigering.application.RedigerbareVedleggService
import no.nav.pensjon.brev.skribenten.brevredigering.domain.AttesterBrevPolicy
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.brevredigering.domain.RedigerBrevPolicy
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.SaksId
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.VedleggId
import org.jetbrains.exposed.v1.jdbc.Database

class LagreAttestertVedleggHandler(
    private val attesterBrevPolicy: AttesterBrevPolicy,
    private val redigerBrevPolicy: RedigerBrevPolicy,
    private val redigerbareVedleggService: RedigerbareVedleggService,
    reserverBrevHandler: ReserverBrevHandler,
    database: Database,
) : ReservertBrevHandler<LagreAttestertVedleggHandler.Request, Edit.Attachment>(database, reserverBrevHandler) {

    data class Request(
        override val brevId: BrevId,
        override val saksId: SaksId,
        val vedleggId: VedleggId,
        val redigertVedlegg: Edit.Attachment,
        val frigiReservasjon: Boolean = false,
    ) : BrevredigeringRequest

    override suspend fun execute(request: Request): Outcome<Edit.Attachment, BrevredigeringError>? {
        val brev = BrevredigeringEntity.findByIdAndSaksId(request.brevId, request.saksId) ?: return null

        val principal = PrincipalInContext.require()
        attesterBrevPolicy.kanAttestere(brev, principal).onError { return failure(it) }
        redigerBrevPolicy.kanRedigere(brev, principal).onError { return failure(it) }

        val resultat = redigerbareVedleggService.lagre(brev, request.vedleggId, request.redigertVedlegg, mergeMotMal = false)

        if (request.frigiReservasjon) {
            brev.frigiReservasjon()
        }

        return resultat
    }
}
