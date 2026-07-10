@file:Suppress("ConvertSecondaryConstructorToPrimary")

package no.nav.pensjon.brev.template

import no.nav.brev.InternKonstruktoer
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgEnum
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgVerdi
import no.nav.pensjon.brevbaker.api.model.DisplayText
import no.nav.pensjon.brevbaker.api.model.TemplateModelSpecification.FieldType
import no.nav.pensjon.brevbaker.api.model.TemplateModelSpecification.FieldType.Scalar.Kind
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import java.time.LocalDate

@OptIn(BrevbakerDSLInternal::class)
class TemplateModelSpecificationFactoryTest {
    data class AModel(
        val tekst: String,
        val etTall: Int,
        val etDesimal: Double,
        val enBool: Boolean,
        @DisplayText("viktig dato")
        val dato: LocalDate,
        val tall: List<Int>,
        val strenger: List<String>,
        val doubles: List<Double>,
        val sub: SubModel,
    ) {
        data class SubModel(val navn: String, val alder: Int)
    }

    private val spec = TemplateModelSpecificationFactory(AModel::class).build(emptyMap())
    private val aModelSpec = spec.types[AModel::class.qualifiedName!!]!!

    @Test
    fun `letterModelTypeName matches input class`() {
        assertThat(spec.letterModelTypeName).isEqualTo(AModel::class.qualifiedName)
    }

    @Test
    fun `has an entry for all fields`() {
        assertThat(aModelSpec.keys).containsAll(listOf("enBool", "tekst", "tall", "strenger", "doubles", "etTall", "etDesimal", "dato", "sub"))
    }

    @Test
    fun `string is a scalar value with type`() {
        assertThat(aModelSpec["tekst"]).isEqualTo(FieldType.Scalar(false, Kind.STRING))
    }

    @Test
    fun `handles display text`() {
        assertThat(aModelSpec["dato"]).isEqualTo(FieldType.Scalar(false, Kind.DATE, "viktig dato"))
    }

    @Test
    fun `int is a scalar value with type`() {
        assertThat(aModelSpec["etTall"]).isEqualTo(FieldType.Scalar(false, Kind.NUMBER))
    }

    @Test
    fun `double is a scalar value with type`() {
        assertThat(aModelSpec["etDesimal"]).isEqualTo(FieldType.Scalar(false, Kind.DOUBLE))
    }

    @Test
    fun `boolean is a scalar value with boolean as type`() {
        assertThat(aModelSpec["enBool"]).isEqualTo(FieldType.Scalar(false, Kind.BOOLEAN))
    }

    @Test
    fun `date is a scalar value with type`() {
        assertThat(aModelSpec["dato"]).isEqualTo(FieldType.Scalar(false, Kind.DATE, "viktig dato"))
    }

    @Test
    fun `list fields have Array type with scalars as items`() {
        assertThat(aModelSpec["tall"]).isEqualTo(FieldType.Array(false, FieldType.Scalar(false, Kind.NUMBER)))
        assertThat(aModelSpec["strenger"]).isEqualTo(FieldType.Array(false, FieldType.Scalar(false, Kind.STRING)))
        assertThat(aModelSpec["doubles"]).isEqualTo(FieldType.Array(false, FieldType.Scalar(false, Kind.DOUBLE)))
    }

    @Test
    fun `object fields have Object type and name can be looked up in specification types`() {
        assertThat(aModelSpec["sub"]).isEqualTo(FieldType.Object(false, AModel.SubModel::class.qualifiedName!!))
        assertThat(
            spec.types[AModel.SubModel::class.qualifiedName!!]!!).isEqualTo(
                mapOf(
                    "navn" to FieldType.Scalar(false, Kind.STRING),
                    "alder" to FieldType.Scalar(false, Kind.NUMBER),
                )
            )
    }

    class NoPrimaryConstructor {
        @Suppress("unused")
        constructor(x: String) {
            this.x = x
        }

        var x: String
    }

    @Test
    fun `fails for Model classes without primary constructor`() {
        assertThrows<TemplateModelSpecificationError> { TemplateModelSpecificationFactory(NoPrimaryConstructor::class).build(emptyMap()) }
    }

    class NotADataClass(val navn: String, @Suppress("unused") val alder: Int)

    @Test
    fun `fails for non data class`() {
        assertThrows<TemplateModelSpecificationError> { TemplateModelSpecificationFactory(NotADataClass::class).build(emptyMap()) }
    }

    data class WithEnumeration(val navn: String, val anEnum: AnEnum) {
        @Suppress("unused")
        enum class AnEnum {
            @DisplayText("Flag 1") FLAG1, @DisplayText("Flag 2") FLAG2
        }
    }

    @Test
    fun `enum fields have Enum type with all enum-values`() {
        val spec = TemplateModelSpecificationFactory(WithEnumeration::class).build(emptyMap()).types[WithEnumeration::class.qualifiedName!!]!!
        assertThat(spec["anEnum"]).isEqualTo(FieldType.Enum(false, setOf(FieldType.EnumEntry("FLAG1", "Flag 1"), FieldType.EnumEntry("FLAG2", "Flag 2"))))
    }

    data class WithValueClass(val navn: String, val aValueClass: TheValue) {
        @JvmInline
        value class TheValue(val value: Int)
    }

    @Test
    fun `value class fields have Scalar type`() {
        val spec = TemplateModelSpecificationFactory(WithValueClass::class).build(emptyMap())
        val withValueClassSpec = spec.types[WithValueClass::class.qualifiedName!!]!!
        assertThat(withValueClassSpec["aValueClass"]).isEqualTo(FieldType.Scalar(false, Kind.NUMBER))
        assertThat(spec.types[WithValueClass::class.qualifiedName!!]!!).isEqualTo(mapOf("navn" to FieldType.Scalar(false, Kind.STRING), "aValueClass" to FieldType.Scalar(false, Kind.NUMBER)))
    }

    data class WithNullable(val scalar: String?, val objekt: AModel.SubModel?, val listNullable: List<String>?, val listWithNullable: List<String?>, val list: List<String?>?)

    @Test
    fun `nullable fields are mapped correctly`() {
        val spec = TemplateModelSpecificationFactory(WithNullable::class).build(emptyMap())

        assertThat(
            spec.types[WithNullable::class.qualifiedName!!]!!)
            .isEqualTo(
                mapOf(
                    "scalar" to FieldType.Scalar(true, Kind.STRING),
                    "objekt" to FieldType.Object(true, AModel.SubModel::class.qualifiedName!!),
                    "listNullable" to FieldType.Array(true, FieldType.Scalar(false, Kind.STRING)),
                    "listWithNullable" to FieldType.Array(false, FieldType.Scalar(true, Kind.STRING)),
                    "list" to FieldType.Array(true, FieldType.Scalar(true, Kind.STRING))
                )
            )

        assertThat(
            spec.types[AModel.SubModel::class.qualifiedName!!]!!)
            .isEqualTo(mapOf("navn" to FieldType.Scalar(false, Kind.STRING), "alder" to FieldType.Scalar(false, Kind.NUMBER)))
    }

    data class Recursive(val next: Recursive?)
    data class SubRecursive(val next: Child) {
        data class Child(val next: Grandchild)
        data class Grandchild(val next: SubRecursive)
    }

    @Test
    fun `recursive model structures causes failure`() {
        assertThrows<TemplateModelSpecificationError>("Should not support self-recursive types") {
            TemplateModelSpecificationFactory(Recursive::class).build(emptyMap())
        }
        assertThrows<TemplateModelSpecificationError>("Should not support circular-recursive types") {
            TemplateModelSpecificationFactory(SubRecursive::class).build(emptyMap())
        }
    }

    data class WithSaksbehandlervalg(val navn: String, val valg: SaksbehandlervalgIDSL)

    enum class TestEnum(override val displayText: String) : SaksbehandlerValgEnum {
        ALTERNATIV_EN("Alternativ en"),
        ALTERNATIV_TO("Alternativ to"),
    }

    @OptIn(InternKonstruktoer::class)
    private val saksbehandlervalg: Map<String, SaksbehandlervalgVerdi<*>> = mapOf(
        "boolValg" to SaksbehandlervalgVerdi.Bool("boolValg", "Bool display"),
        "intValg" to SaksbehandlervalgVerdi.Integer("intValg", "Int display"),
        "textValg" to SaksbehandlervalgVerdi.Text("textValg", "Text display"),
        "enumValg" to SaksbehandlervalgVerdi.Enum("enumValg", "Enum display", TestEnum::class),
    )

    @Test
    fun `SaksbehandlervalgIDSL field gets Object type pointing to its own type spec`() {
        val spec = TemplateModelSpecificationFactory(WithSaksbehandlervalg::class).build(saksbehandlervalg)
        val withValgSpec = spec.types[WithSaksbehandlervalg::class.qualifiedName!!]!!

        assertThat(withValgSpec["valg"]).isEqualTo(FieldType.Object(false, SaksbehandlervalgIDSL::class.qualifiedName!!))
    }

    @Test
    fun `SaksbehandlervalgIDSL type spec has one field per registered saksbehandlervalg`() {
        val spec = TemplateModelSpecificationFactory(WithSaksbehandlervalg::class).build(saksbehandlervalg)
        val valgSpec = spec.types[SaksbehandlervalgIDSL::class.qualifiedName!!]!!

        assertThat(valgSpec.keys).containsExactlyInAnyOrder("boolValg", "intValg", "textValg", "enumValg")
    }

    @Test
    fun `SaksbehandlervalgVerdi types are mapped to correct FieldType`() {
        val spec = TemplateModelSpecificationFactory(WithSaksbehandlervalg::class).build(saksbehandlervalg)
        val valgSpec = spec.types[SaksbehandlervalgIDSL::class.qualifiedName!!]!!

        assertThat(valgSpec["boolValg"]).isEqualTo(FieldType.Scalar(true, Kind.BOOLEAN, "Bool display"))
        assertThat(valgSpec["intValg"]).isEqualTo(FieldType.Scalar(true, Kind.NUMBER, "Int display"))
        assertThat(valgSpec["textValg"]).isEqualTo(FieldType.Scalar(true, Kind.STRING, "Text display"))
        assertThat(valgSpec["enumValg"]).isEqualTo(
            FieldType.Enum(
                true,
                setOf(FieldType.EnumEntry("ALTERNATIV_EN", "Alternativ en"), FieldType.EnumEntry("ALTERNATIV_TO", "Alternativ to")),
            )
        )
    }

    @Test
    fun `fails when SaksbehandlervalgIDSL is present but no saksbehandlervalg map is provided`() {
        assertThrows<IllegalArgumentException> {
            TemplateModelSpecificationFactory(WithSaksbehandlervalg::class).build(null)
        }
    }

    @Test
    fun `model without SaksbehandlervalgIDSL fields does not require a saksbehandlervalg map`() {
        assertThat(TemplateModelSpecificationFactory(AModel::class).build(null)).isEqualTo(spec)
    }

}