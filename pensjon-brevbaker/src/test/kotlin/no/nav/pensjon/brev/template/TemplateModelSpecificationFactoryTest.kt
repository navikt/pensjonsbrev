@file:Suppress("ConvertSecondaryConstructorToPrimary")

package no.nav.pensjon.brev.template

import com.natpryce.hamkrest.*
import com.natpryce.hamkrest.assertion.assertThat
import no.nav.pensjon.brevbaker.api.model.DisplayText
import no.nav.pensjon.brevbaker.api.model.TemplateModelSpecification.FieldType
import no.nav.pensjon.brevbaker.api.model.TemplateModelSpecification.FieldType.Scalar.Kind
import org.junit.jupiter.api.*
import java.time.LocalDate

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

    private val spec = TemplateModelSpecificationFactory(AModel::class).build()
    private val aModelSpec = spec.types[AModel::class.qualifiedName!!]!!

    @Test
    fun `letterModelTypeName matches input class`() {
        assertThat(spec.letterModelTypeName, equalTo(AModel::class.qualifiedName))
    }

    @Test
    fun `has an entry for all fields`() {
        assertThat(aModelSpec.keys, Set<String>::containsAll, listOf("enBool", "tekst", "tall", "strenger", "doubles", "etTall", "etDesimal", "dato", "sub"))
    }

    @Test
    fun `string is a scalar value with type`() {
        assertThat(aModelSpec["tekst"], equalTo(FieldType.Scalar(false, Kind.STRING)))
    }

    @Test
    fun `handles display text`() {
        assertThat(aModelSpec["dato"], equalTo(FieldType.Scalar(false, Kind.DATE, "viktig dato")))
    }

    @Test
    fun `int is a scalar value with type`() {
        assertThat(aModelSpec["etTall"], equalTo(FieldType.Scalar(false, Kind.NUMBER)))
    }

    @Test
    fun `double is a scalar value with type`() {
        assertThat(aModelSpec["etDesimal"], equalTo(FieldType.Scalar(false, Kind.DOUBLE)))
    }

    @Test
    fun `boolean is a scalar value with boolean as type`() {
        assertThat(aModelSpec["enBool"], equalTo(FieldType.Scalar(false, Kind.BOOLEAN)))
    }

    @Test
    fun `date is a scalar value with type`() {
        assertThat(aModelSpec["dato"], equalTo(FieldType.Scalar(false, Kind.DATE, "viktig dato")))
    }

    @Test
    fun `list fields have Array type with scalars as items`() {
        assertThat(aModelSpec["tall"], equalTo(FieldType.Array(false, FieldType.Scalar(false, Kind.NUMBER))))
        assertThat(aModelSpec["strenger"], equalTo(FieldType.Array(false, FieldType.Scalar(false, Kind.STRING))))
        assertThat(aModelSpec["doubles"], equalTo(FieldType.Array(false, FieldType.Scalar(false, Kind.DOUBLE))))
    }

    @Test
    fun `object fields have Object type and name can be looked up in specification types`() {
        assertThat(aModelSpec["sub"], equalTo(FieldType.Object(false, AModel.SubModel::class.qualifiedName!!)))
        assertThat(
            spec.types[AModel.SubModel::class.qualifiedName!!]!!, equalTo(
                mapOf(
                    "navn" to FieldType.Scalar(false, Kind.STRING),
                    "alder" to FieldType.Scalar(false, Kind.NUMBER),
                )
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
        assertThrows<TemplateModelSpecificationError> { TemplateModelSpecificationFactory(NoPrimaryConstructor::class).build() }
    }

    class NotADataClass(val navn: String, @Suppress("unused") val alder: Int)

    @Test
    fun `fails for non data class`() {
        assertThrows<TemplateModelSpecificationError> { TemplateModelSpecificationFactory(NotADataClass::class).build() }
    }

    data class WithEnumeration(val navn: String, val anEnum: AnEnum) {
        @Suppress("unused")
        enum class AnEnum { FLAG1, FLAG2 }
    }

    @Test
    fun `enum fields have Enum type with all enum-values`() {
        val spec = TemplateModelSpecificationFactory(WithEnumeration::class).build().types[WithEnumeration::class.qualifiedName!!]!!
        assertThat(spec["anEnum"], equalTo(FieldType.Enum(false, WithEnumeration.AnEnum.entries.map { it.name }.toSet())))
    }

    data class WithValueClass(val navn: String, val aValueClass: TheValue) {
        @JvmInline
        value class TheValue(val value: Int)
    }

    @Test
    fun `value class fields have Object type that can be looked up in specifiaction types`() {
        val spec = TemplateModelSpecificationFactory(WithValueClass::class).build()
        val withValueClassSpec = spec.types[WithValueClass::class.qualifiedName!!]!!
        assertThat(withValueClassSpec["aValueClass"], equalTo(FieldType.Object(false, WithValueClass.TheValue::class.qualifiedName!!)))
        assertThat(spec.types[WithValueClass.TheValue::class.qualifiedName!!]!!, equalTo(mapOf("value" to FieldType.Scalar(false, Kind.NUMBER))))
    }

    data class WithNullable(val scalar: String?, val objekt: AModel.SubModel?, val listNullable: List<String>?, val listWithNullable: List<String?>, val list: List<String?>?)

    @Test
    fun `nullable fields are mapped correctly`() {
        val spec = TemplateModelSpecificationFactory(WithNullable::class).build()

        assertThat(
            spec.types[WithNullable::class.qualifiedName!!]!!,
            equalTo(
                mapOf(
                    "scalar" to FieldType.Scalar(true, Kind.STRING),
                    "objekt" to FieldType.Object(true, AModel.SubModel::class.qualifiedName!!),
                    "listNullable" to FieldType.Array(true, FieldType.Scalar(false, Kind.STRING)),
                    "listWithNullable" to FieldType.Array(false, FieldType.Scalar(true, Kind.STRING)),
                    "list" to FieldType.Array(true, FieldType.Scalar(true, Kind.STRING))
                )
            )
        )

        assertThat(
            spec.types[AModel.SubModel::class.qualifiedName!!]!!,
            equalTo(mapOf("navn" to FieldType.Scalar(false, Kind.STRING), "alder" to FieldType.Scalar(false, Kind.NUMBER)))
        )
    }

    data class Recursive(val next: Recursive?)
    data class SubRecursive(val next: Child) {
        data class Child(val next: Grandchild)
        data class Grandchild(val next: SubRecursive)
    }

    @Test
    fun `recursive model structures causes failure`() {
        assertThrows<TemplateModelSpecificationError>("Should not support self-recursive types") {
            TemplateModelSpecificationFactory(Recursive::class).build()
        }
        assertThrows<TemplateModelSpecificationError>("Should not support circular-recursive types") {
            TemplateModelSpecificationFactory(SubRecursive::class).build()
        }
    }

}