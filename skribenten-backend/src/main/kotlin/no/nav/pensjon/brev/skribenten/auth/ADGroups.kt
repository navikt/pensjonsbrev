package no.nav.pensjon.brev.skribenten.auth

import kotlinx.serialization.Serializable
import no.nav.pensjon.brev.skribenten.GroupsConfig

@Serializable
@JvmInline
value class ADGroup(val id: String) {
    override fun toString() = id
}

interface SkribentenADGroups {
    val pensjonUtland: ADGroup
    val fortroligAdresse: ADGroup
    val strengtFortroligAdresse: ADGroup
    val pensjonSaksbehandler: ADGroup
    val attestant: ADGroup
    val veileder: ADGroup
    val okonomi: ADGroup
    val brukerhjelpA: ADGroup
    val klagebehandler: ADGroup
}

object ADGroups : SkribentenADGroups {
    lateinit var config: GroupsConfig
        private set

    override val pensjonUtland by lazy { config.pensjonUtland }
    override val fortroligAdresse by lazy { config.fortroligAdresse }
    override val strengtFortroligAdresse by lazy { config.strengtFortroligAdresse }
    override val pensjonSaksbehandler by lazy { config.pensjonSaksbehandler }
    override val attestant by lazy { config.attestant }
    override val veileder by lazy { config.veileder }
    override val okonomi by lazy { config.okonomi }
    override val brukerhjelpA by lazy { config.brukerhjelpA }
    override val klagebehandler by lazy { config.klagebehandler }

    // Liste av AD-grupper som skal ha tilgang til skribenten (det er altså nok å være i en av disse gruppene for å få tilgang)
    val alleBrukergrupper: Set<ADGroup>
        get() = setOf(pensjonSaksbehandler, attestant, veileder, okonomi, brukerhjelpA, klagebehandler)

    fun init(groupsConfig: GroupsConfig) {
        config = groupsConfig
    }
}