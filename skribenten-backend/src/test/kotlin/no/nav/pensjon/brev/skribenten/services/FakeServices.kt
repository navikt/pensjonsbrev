package no.nav.pensjon.brev.skribenten.services

class FakeSafService(val journalpost: Pair<String, List<String>>?) : SafService {
    override suspend fun waitForJournalpostStatusUnderArbeid(journalpostId: String) = JournalpostLoadingResult.READY
    override suspend fun getFirstDocumentInJournal(journalpostId: String): ServiceResult.Ok<SafService.HentDokumenterResponse> =
        journalpost?.let {
            SafService.HentDokumenterResponse.Journalpost(
                it.first, it.second.map { dok ->
                    SafService.HentDokumenterResponse.Dokument(dok)
                }
            )
        }?.let {
            ServiceResult.Ok(
                SafService.HentDokumenterResponse(
                    SafService.HentDokumenterResponse.Journalposter(
                        it
                    ), null
                )
            )
        } ?: TODO("Not implemented")

    override suspend fun hentPdfForJournalpostId(journalpostId: String) = TODO("Not yet implemented")
}