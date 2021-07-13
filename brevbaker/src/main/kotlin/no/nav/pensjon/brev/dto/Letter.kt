package no.nav.pensjon.brev.dto

data class Letter(
    val standardFields: StandardFields,
    val letterTemplate: LetterTemplate,
    val fields: List<Field<*>> = ArrayList()
)

data class StandardFields(
    val returAdresse: String,
    val postnummer: String,
    val poststed: String,
    val land: String,
    val mottakerNavn: String,
    val verge: String,
    val adresseLinje1: String,
    val adresseLinje2: String,
    val adresseLinje3: String,
    val dokumentDato: String,
    val saksnummer: String,
    val sakspartNavn: String,
    val sakspartId: String,
    val kontakTelefonnummer: String,
)

/**
 *
 *TODO the letter template will need to grow in complexity to accommodate different content such as:
 *      - tables, last page content, display logic depending on field values
 */
class LetterTemplate(val template: String)

abstract class Field<T>(
    internal val value: T,
    /**
     * Unique field identifier
     * used to:
     *  - map to the correct Field implementation
     *  - build a map of required fields when requesting a letter
     *  - inserting fields into the letter template
     */
    val ID: String
) {
    // Used to verify incoming data for fields
    abstract fun verify(): Boolean

    //Every Field needs to override the toString function to ensure proper formatting.
    abstract override fun toString(): String

}
