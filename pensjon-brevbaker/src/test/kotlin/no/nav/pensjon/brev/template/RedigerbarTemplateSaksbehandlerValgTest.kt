package no.nav.pensjon.brev.template

import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.isEmpty
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brev.maler.ProductionTemplates
import org.junit.jupiter.api.Test
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.declaredMemberProperties
import kotlin.test.assertFailsWith

// TODO: Må flyttes til brevbaker-api-model-common når vi er klar til å håndheve nullable felter i saksbehandlerValg.
interface SaksbehandlerValgValidator {
    fun isAllowedNonNullable(propertyName: String): Boolean
}

data class ValgMedOverstyrtValidator(val tillattNonNullable: String) : SaksbehandlerValgBrevdata {
    companion object : SaksbehandlerValgValidator {
        override fun isAllowedNonNullable(propertyName: String) = propertyName == "tillattNonNullable"
    }
}

data class ValgUtenOverstyrtValidator(val ikkeTillattNonNullable: String) : SaksbehandlerValgBrevdata

class RedigerbarTemplateSaksbehandlerValgTest {

    @Test
    fun `kan overstyre validator til aa tillate non-nullable`() {
        validateSaksbehandlerValg(ValgMedOverstyrtValidator::class)
    }

    @Test
    fun `standardvalidator krever at alle felter er nullable`() {
        assertThat(validateSaksbehandlerValg(ValgUtenOverstyrtValidator::class), isEmpty.not())
    }

    @Test
    fun `alle redigerbare maler skal ha saksbehandlerValg hvor alle felter er nullable`() {
        val saksbehandlerValgName = RedigerbarBrevdata<*, *>::saksbehandlerValg.name

        val nonNullableTreff = ProductionTemplates.hentRedigerbareMaler().map { mal ->
            val name = mal.kode.kode()

            val saksbehandlerValgProperty =
                mal.template.letterDataType.declaredMemberProperties.first { it.name == saksbehandlerValgName }

            val saksbehandlerValgClass = saksbehandlerValgProperty.returnType.classifier as? KClass<*>
                ?: error("Could not get KClass for saksbehandlerValg of template $name")

            name to validateSaksbehandlerValg(saksbehandlerValgClass)
        }.filter { it.second.isNotEmpty() }

        assertThat(nonNullableTreff, isEmpty) {
            """
            | Alle felter i saksbehandlerValg i alle redigerbare maler må være nullable for å tillate delvis oppdatering.
            | Følgende maler har felter som ikke er nullable:
            |${nonNullableTreff.joinToString("\n") { (malnavn, felter) ->
                "   - $malnavn: ${felter.joinToString(", ")}"
              }}
                
            | Om dette er ønsket oppførsel, implementer et companion object i saksbehandlerValg-klassen som implementerer SaksbehandlerValgValidator.
            | 
            """.trimMargin("|")
        }
    }

    private fun validateSaksbehandlerValg(saksbehandlervalg: KClass<*>): Set<String> {
        val validator = saksbehandlervalg.companionObjectInstance as? SaksbehandlerValgValidator
            ?: DefaultSaksbehandlerValgValidator

        return saksbehandlervalg.constructors.flatMap { constructor ->
            constructor.parameters.filter { parameter ->
                validator.isAllowedNonNullable(parameter.name!!) || !parameter.type.isMarkedNullable
            }
        }.map { it.name!! }.toSet()
    }

    private object DefaultSaksbehandlerValgValidator : SaksbehandlerValgValidator {
        override fun isAllowedNonNullable(propertyName: String) = false
    }
}