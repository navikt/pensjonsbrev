package no.nav.pensjon.brev.brevoppskrift.auth

import kotlinx.serialization.Serializable

@JvmInline
value class NavIdent(val value: String) {
    override fun toString(): String = value
}

@JvmInline
value class AuthorName(val value: String) {
    override fun toString(): String = value
}

@Serializable
@JvmInline
value class EntraAudience(val value: String)

@Serializable
@JvmInline
value class EntraClientId(val value: String)

@Serializable
@JvmInline
value class EntraGroupId(val value: String)
