package no.nav.pensjon.brev.skribenten.auth

import com.typesafe.config.Config

@JvmInline
value class ADGroup(val id: String)

object ADGroups {
    private lateinit var _pensjonUtland: String
    private lateinit var _fortroligAdresse: String
    private lateinit var _strengtFortroligAdresse: String
    private lateinit var _pensjonSaksbehandler: String
    private lateinit var _attestant: String
    private lateinit var _veileder: String
    private lateinit var _okonomi: String
    private lateinit var _brukerhjelpA: String

    val pensjonUtland: ADGroup
        get() = ADGroup(_pensjonUtland)
    val fortroligAdresse: ADGroup
        get() = ADGroup(_fortroligAdresse)
    val strengtFortroligAdresse: ADGroup
        get() = ADGroup(_strengtFortroligAdresse)
    val pensjonSaksbehandler: ADGroup
        get() = ADGroup(_pensjonSaksbehandler)
    val attestant: ADGroup
        get() = ADGroup(_attestant)
    val veileder: ADGroup
        get() = ADGroup(_veileder)
    val okonomi: ADGroup
        get() = ADGroup(_okonomi)
    val brukerhjelpA: ADGroup
        get() = ADGroup(_brukerhjelpA)

    val alleBrukergrupper: Set<ADGroup>
        get() = setOf(pensjonSaksbehandler, attestant, veileder, okonomi, brukerhjelpA)

    fun init(groupsConfig: Config) {
        _pensjonUtland = groupsConfig.getString("pensjonUtland")
        _fortroligAdresse = groupsConfig.getString("fortroligAdresse")
        _strengtFortroligAdresse = groupsConfig.getString("strengtFortroligAdresse")
        _pensjonSaksbehandler = groupsConfig.getString("pensjonSaksbehandler")
        _attestant = groupsConfig.getString("attestant")
        _veileder = groupsConfig.getString("veileder")
        _okonomi = groupsConfig.getString("okonomi")
        _brukerhjelpA = groupsConfig.getString("brukerhjelpA")
    }
}