package no.nav.pensjon.brev.mal

import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.validation.DuplicateBlockIdentity
import no.nav.pensjon.brev.template.autobrevMal
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.redigerbarMal
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

/**
 * Denne testen er plassert utenfor pakken `no.nav.pensjon.brev.template` for å unngå at linjen i test-malen som har
 * valideringsfeil blir suppressed i unntaket.
 */
class BlockIdentityValidationTest {

    @Test
    fun `kan ikke ha to like blokker i outline`() {
        assertThrows<DuplicateBlockIdentity> {
            redigerbarMal {
                title { text(bokmal { +"tittel" }) }
                outline {
                    title1 { text(bokmal { +"hei" }) }
                    title1 { text(bokmal { +"hei" }) }
                }
            }
        }
    }

    @Test
    fun `kan ikke ha en lik blokk i showIf som i outline-root`() {
        assertThrows<DuplicateBlockIdentity> {
            redigerbarMal {
                title { text(bokmal { +"tittel" }) }
                outline {
                    title1 { text(bokmal { +"hei" }) }
                    showIf(true.expr()) {
                        title1 { text(bokmal { +"hei" }) }
                    }
                }
            }
        }
    }

    @Test
    fun `kan ikke ha like blokker showIf`() {
        assertThrows<DuplicateBlockIdentity> {
            redigerbarMal {
                title { text(bokmal { +"tittel" }) }
                outline {
                    title1 { text(bokmal { +"forskjellig" }) }
                    showIf(true.expr()) {
                        title1 { text(bokmal { +"hei" }) }
                        title1 { text(bokmal { +"hei" }) }
                    }
                }
            }
        }
    }

    @Test
    fun `kan ikke ha en lik blokk i orShow som i outline-root`() {
        assertThrows<DuplicateBlockIdentity> {
            redigerbarMal {
                title { text(bokmal { +"tittel" }) }
                outline {
                    title1 { text(bokmal { +"hei" }) }
                    showIf(true.expr()) {
                        title1 { text(bokmal { +"forskjellig" }) }
                    } orShow {
                        title1 { text(bokmal { +"hei" }) }
                    }
                }
            }
        }
    }

    @Test
    fun `kan ikke ha en like blokker i orShow`() {
        assertThrows<DuplicateBlockIdentity> {
            redigerbarMal {
                title { text(bokmal { +"tittel" }) }
                outline {
                    title1 { text(bokmal { +"forskjellig" }) }
                    showIf(true.expr()) {
                        title1 { text(bokmal { +"forskjellig 2" }) }
                    } orShow {
                        title1 { text(bokmal { +"hei" }) }
                        title1 { text(bokmal { +"hei" }) }
                    }
                }
            }
        }
    }

    @Test
    fun `kan ha lik blokk i tilhoerende showIf og orShow`() {
        assertDoesNotThrow {
            redigerbarMal {
                title { text(bokmal { +"tittel" }) }
                outline {
                    showIf(true.expr()) {
                        title1 { text(bokmal { +"hei" }) }
                    } orShow {
                        title1 { text(bokmal { +"hei" }) }
                    }
                }
            }
        }
    }

    @Test
    fun `kan ha lik blokk i tilhoerende showIf og orShowIf`() {
        assertDoesNotThrow {
            redigerbarMal {
                title { text(bokmal { +"tittel" }) }
                outline {
                    showIf(true.expr()) {
                        title1 { text(bokmal { +"hei" }) }
                    }.orShowIf(true.expr()) {
                        title1 { text(bokmal { +"hei" }) }
                    }
                }
            }
        }
    }

    @Test
    fun `kan ha lik blokk i tilhoerende ifNotNull og orIfNotNull`() {
        assertDoesNotThrow {
            redigerbarMal {
                title { text(bokmal { +"tittel" }) }
                outline {
                    ifNotNull(true.expr()) {
                        title1 { text(bokmal { +"hei" }) }
                    }.orIfNotNull(true.expr()) {
                        title1 { text(bokmal { +"hei" }) }
                    }
                }
            }
        }
    }

    @Test
    fun `kan ha lik blokk i tilhoerende ifNotNull-2arg og orIfNotNull`() {
        assertDoesNotThrow {
            redigerbarMal {
                title { text(bokmal { +"tittel" }) }
                outline {
                    ifNotNull(true.expr(), false.expr()) { _, _ ->
                        title1 { text(bokmal { +"hei" }) }
                    }.orIfNotNull(true.expr()) {
                        title1 { text(bokmal { +"hei" }) }
                    }
                }
            }
        }
    }

    @Test
    fun `kan ha lik blokk i tilhoerende showIf og orShow men ikke etter`() {
        assertThrows<DuplicateBlockIdentity> {
            redigerbarMal {
                title { text(bokmal { +"tittel" }) }
                outline {
                    showIf(true.expr()) {
                        title1 { text(bokmal { +"hei" }) }
                    } orShow {
                        title1 { text(bokmal { +"hei" }) }
                    }
                    title1 { text(bokmal { +"hei" }) }
                }
            }
        }
    }

    @Test
    fun `kan ikke ha lik blokk i forEach som i outline-root`() {
        assertThrows<DuplicateBlockIdentity> {
            redigerbarMal {
                title { text(bokmal { +"tittel" }) }
                outline {
                    title1 { text(bokmal { +"hei" }) }

                    forEach(emptyList<Int>().expr()) {
                        title1 { text(bokmal { +"hei" }) }
                    }
                }
            }
        }
    }

    @Test
    fun `kan ikke ha like blokker i forEach`() {
        assertThrows<DuplicateBlockIdentity> {
            redigerbarMal {
                title { text(bokmal { +"tittel" }) }
                outline {
                    title1 { text(bokmal { +"forskjellig" }) }

                    forEach(emptyList<Int>().expr()) {
                        title1 { text(bokmal { +"hei" }) }
                        title1 { text(bokmal { +"hei" }) }
                    }
                }
            }
        }
    }

    @Test
    fun `kan ha like blokker i autobrev`() {
        assertDoesNotThrow {
            autobrevMal {
                title { text(bokmal { +"tittel" }) }
                outline {
                    title1 { text(bokmal { +"hei" }) }
                    title1 { text(bokmal { +"hei" }) }
                }
            }
        }
    }

    @Test
    fun `blokker i fraser valideres`() {
        val frase = object : OutlinePhrase<LangBokmal>() {
            override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
                paragraph { text(bokmal { +"hei" }) }
            }
        }

        assertThrows<DuplicateBlockIdentity> {
            redigerbarMal {
                title { text(bokmal { +"tittel" }) }
                outline {
                    paragraph { text(bokmal { +"hei" }) }
                    includePhrase(frase)
                }
            }
        }
    }
}