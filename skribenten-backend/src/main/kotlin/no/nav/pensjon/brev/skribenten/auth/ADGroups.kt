package no.nav.pensjon.brev.skribenten.auth

import com.typesafe.config.Config
import kotlin.reflect.KProperty

@JvmInline
value class ADGroup(val id: String)

object ADGroups {
    var pensjonUtland: ADGroup by LateinitStringBackingField()
        private set
    var fortroligAdresse: ADGroup by LateinitStringBackingField()
        private set
    var strengtFortroligAdresse: ADGroup by LateinitStringBackingField()
        private set
    var pensjonSaksbehandler: ADGroup by LateinitStringBackingField()
        private set
    var attestant: ADGroup by LateinitStringBackingField()
        private set
    var veileder: ADGroup by LateinitStringBackingField()
        private set
    var okonomi: ADGroup by LateinitStringBackingField()
        private set
    var brukerhjelpA: ADGroup by LateinitStringBackingField()
        private set
    var klagebehandler: ADGroup by LateinitStringBackingField()
        private set

    // Liste av AD-grupper som skal ha tilgang til skribenten (det er altså nok å være i en av disse gruppene for å få tilgang)
    val alleBrukergrupper: Set<ADGroup>
        get() = setOf(pensjonSaksbehandler, attestant, veileder, okonomi, brukerhjelpA, klagebehandler)

    fun init(groupsConfig: Config) {
        pensjonUtland = ADGroup(groupsConfig.getString("pensjonUtland"))
        fortroligAdresse = ADGroup(groupsConfig.getString("fortroligAdresse"))
        strengtFortroligAdresse = ADGroup(groupsConfig.getString("strengtFortroligAdresse"))
        pensjonSaksbehandler = ADGroup(groupsConfig.getString("pensjonSaksbehandler"))
        attestant = ADGroup(groupsConfig.getString("attestant"))
        veileder = ADGroup(groupsConfig.getString("veileder"))
        okonomi = ADGroup(groupsConfig.getString("okonomi"))
        brukerhjelpA = ADGroup(groupsConfig.getString("brukerhjelpA"))
        klagebehandler = ADGroup(groupsConfig.getString("klagebehandler"))
    }
}

private class LateinitStringBackingField() {
    private lateinit var value: String

    operator fun getValue(thisRef: ADGroups, property: KProperty<*>): ADGroup {
        return ADGroup(value)
    }
    operator fun setValue(thisRef: ADGroups, property: KProperty<*>, value: ADGroup) {
        this.value = value.id
    }
}