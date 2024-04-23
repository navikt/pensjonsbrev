package no.nav.pensjon.brev.skribenten.auth

import no.nav.pensjon.brev.skribenten.services.NAVEnhet
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AuthorizeAnsattSakTilgangKtTest {

    @Test
    fun `should return true if one of the penSakEnheter ids matches one of the navAnsattEnheter ids`() {
        val navAnsattEnheter = listOf(NAVEnhet("1", "Enhet1"), NAVEnhet("2", "Enhet2"))
        val penSakEnheter = listOf("2", "3")
        assertTrue(harTilgangTilSakSinEnhet(navAnsattEnheter, penSakEnheter))
    }

    @Test
    fun `should return false if none of the penSakEnheter ids matches navAnsattEnheter ids`() {
        val navAnsattEnheter = listOf(NAVEnhet("1", "Enhet1"), NAVEnhet("2", "Enhet2"))
        val penSakEnheter = listOf("3", "4")
        assertFalse(harTilgangTilSakSinEnhet(navAnsattEnheter, penSakEnheter))
    }
}
