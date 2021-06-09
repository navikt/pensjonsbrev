package no.nav.pensjon.brevmaker

import java.lang.IllegalArgumentException

val standardFields = arrayOf(
    FieldType.FORNAVN_MOTTAKER,
    FieldType.ETTERNAVN_MOTTAKER,
    FieldType.GATENAVN_MOTTAKER,
    FieldType.HUSNUMMER_MOTTAKER,
    FieldType.POSTNUMMER_MOTTAKER,
    FieldType.POSTSTED_MOTTAKER,
)

enum class LetterTemplate(val letterId: String, vararg val fields: FieldType) {
    ALDERSPENSJON_TEST("alderspensjon", *standardFields, FieldType.ALDERSPENSJON_MAANEDLIG_BELOEP),
    TESTBREV("annetbrev", *standardFields, FieldType.TESTBREV_OPPGITTPROSENT),
    NYTT_BREV("nyttbrev", *standardFields, FieldType.TESTBREV2_NYTTFELT);
    fun mapFields(fieldData: Map<String, String>): String {
        return fields
            .map { fieldType ->
                fieldData[fieldType.typeKey]?.let { Field(fieldType, it) }
                    ?: throw IllegalArgumentException("""Missing field ${fieldType.typeKey}""")
            }.map { """\newcommand{\felt${it.type.typeKey}}{${it.content}}""" + "\n" }
            .reduce { acc, string -> acc + string }
    }

    companion object{
        fun fromId(id: String): LetterTemplate? {
            values().forEach {
                if(it.letterId == id){
                    return it;
                }
            }
            return null;
        }
    }

}