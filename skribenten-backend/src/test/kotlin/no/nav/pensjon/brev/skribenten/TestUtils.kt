package no.nav.pensjon.brev.skribenten

import com.typesafe.config.ConfigValueFactory
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevkode
import no.nav.pensjon.brev.skribenten.auth.*
import no.nav.pensjon.brev.skribenten.model.NavIdent
import org.assertj.core.api.AbstractAssert
import java.time.LocalDate
import java.util.function.Consumer

inline fun <reified T> AbstractAssert<*, *>.isInstanceOfSatisfying(block: Consumer<T>) =
    isInstanceOfSatisfying(T::class.java, block)!!

data class MockPrincipal(override val navIdent: NavIdent, override val fullName: String, val groups: Set<ADGroup> = emptySet()) : UserPrincipal {
    override val accessToken: UserAccessToken
        get() = throw NotImplementedError("Not implemented in mock class")

    override fun isInGroup(groupId: ADGroup) = groups.contains(groupId)

    override fun getOnBehalfOfToken(scope: String): TokenResponse.OnBehalfOfToken? {
        throw NotImplementedError("Not implemented in mock class")
    }

    override fun setOnBehalfOfToken(scope: String, token: TokenResponse.OnBehalfOfToken) {
        throw NotImplementedError("Not implemented in mock class")
    }

}

fun initADGroups() {
    ADGroups.init(
        ConfigValueFactory.fromMap(
            mapOf(
                "pensjonUtland" to "ad gruppe id for Pensjon_Utland",
                "fortroligAdresse" to "ad gruppe id for Fortrolig_Adresse",
                "strengtFortroligAdresse" to "ad gruppe id for Strengt_Fortrolig_Adresse",
                "pensjonSaksbehandler" to "ad gruppe id for PENSJON_SAKSBEHANDLER",
                "attestant" to "ad gruppe id for Attestant",
                "veileder" to "ad gruppe id for veileder",
                "okonomi" to "ad gruppe id for okonomi",
                "brukerhjelpA" to "ad gruppe id for brukerhjelpA",
            )
        ).toConfig()
    )
}

object Testbrevkoder {
    val TESTBREV = RedigerbarBrevkode("TESTBREV")
    val INFORMASJONSBREV = RedigerbarBrevkode("INFORMASJONSBREV")
    val VEDTAKSBREV = RedigerbarBrevkode("VEDTAKSBREV")
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