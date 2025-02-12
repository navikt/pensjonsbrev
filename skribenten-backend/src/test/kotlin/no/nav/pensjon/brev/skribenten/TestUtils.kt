package no.nav.pensjon.brev.skribenten

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.skribenten.auth.*
import no.nav.pensjon.brev.skribenten.model.NavIdent
import org.assertj.core.api.AbstractAssert
import java.time.LocalDate
import java.util.function.Consumer

inline fun <reified T> AbstractAssert<*, *>.isInstanceOfSatisfying(block: Consumer<T>) =
    isInstanceOfSatisfying(T::class.java, block)!!

inline fun <reified T> AbstractAssert<*, *>.isInstanceOf() =
    isInstanceOf(T::class.java)!!

data class MockPrincipal(override val navIdent: NavIdent, override val fullName: String, val groups: MutableSet<ADGroup> = mutableSetOf()) : UserPrincipal {
    override val accessToken: UserAccessToken
        get() = throw NotImplementedError("Not implemented in mock class")

    override fun isInGroup(groupId: ADGroup) = groups.contains(groupId)

    override fun getOnBehalfOfToken(scope: String): TokenResponse.OnBehalfOfToken? {
        throw NotImplementedError("Not implemented in mock class")
    }

    override fun setOnBehalfOfToken(
        scope: String,
        token: TokenResponse.OnBehalfOfToken,
    ) {
        throw NotImplementedError("Not implemented in mock class")
    }
}

enum class Testbrevkoder : Brevkode.Redigerbart {
    TESTBREV,
    ;

    override fun kode() = name
}

data class EksempelRedigerbartDto(
    override val saksbehandlerValg: EmptyBrevdata,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<EmptyBrevdata, EksempelRedigerbartDto.PesysData> {
    data class PesysData(
        val pensjonInnvilget: Boolean,
        val datoInnvilget: LocalDate,
        val navneliste: List<String>,
        val datoAvslaatt: LocalDate?,
        val pensjonBeloep: Int?,
    ) : BrevbakerBrevdata
}
