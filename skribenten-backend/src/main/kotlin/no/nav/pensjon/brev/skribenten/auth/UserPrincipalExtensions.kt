package no.nav.pensjon.brev.skribenten.auth

import no.nav.pensjon.brev.skribenten.services.NavansattService

/**
 * Henter signaturnavn for principal fra NavansattService.
 * Fallbacker til principal.fullName hvis navansatt ikke finnes.
 */
suspend fun UserPrincipal.hentSignatur(navansattService: NavansattService): String =
    navansattService.hentNavansatt(navIdent.id)?.fulltNavn ?: fullName

