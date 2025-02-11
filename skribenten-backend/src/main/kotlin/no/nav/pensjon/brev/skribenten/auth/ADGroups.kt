package no.nav.pensjon.brev.skribenten.auth

import com.typesafe.config.Config

@JvmInline
value class ADGroup(val id: String)

object ADGroups {
    private lateinit var _pensjonUtland: String
    private lateinit var _pensjonSaksbehandler: String
    private lateinit var _fortroligAdresse: String
    private lateinit var _strengtFortroligAdresse: String
    private lateinit var _strengtFortroligUtland: String
    private lateinit var _attestant: String

    val pensjonUtland: ADGroup
        get() = ADGroup(_pensjonUtland)
    val pensjonSaksbehandler: ADGroup
        get() = ADGroup(_pensjonSaksbehandler)
    val fortroligAdresse: ADGroup
        get() = ADGroup(_fortroligAdresse)
    val strengtFortroligAdresse: ADGroup
        get() = ADGroup(_strengtFortroligAdresse)
    val strengtFortroligUtland: ADGroup
        get() = ADGroup(_strengtFortroligUtland)
    val attestant: ADGroup
        get() = ADGroup(_attestant)

    fun init(groupsConfig: Config) {
        _pensjonUtland = groupsConfig.getString("pensjonUtland")
        _pensjonSaksbehandler = groupsConfig.getString("pensjonSaksbehandler")
        _fortroligAdresse = groupsConfig.getString("fortroligAdresse")
        _strengtFortroligAdresse = groupsConfig.getString("strengtFortroligAdresse")
        _strengtFortroligUtland = groupsConfig.getString("strengtFortroligUtland")
        _attestant = groupsConfig.getString("attestant")
    }
}
