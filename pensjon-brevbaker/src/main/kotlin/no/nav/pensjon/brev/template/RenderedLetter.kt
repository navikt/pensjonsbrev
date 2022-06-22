package no.nav.pensjon.brev.template

import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.util.*

interface RenderedLetter {
    fun base64EncodedFiles(): Map<String, String>
}

class RenderedLatexLetter : RenderedLetter {
    private val files: MutableMap<String, ByteArrayOutputStream> = mutableMapOf()

    companion object {
        val base64Encoder: Base64.Encoder = Base64.getEncoder()
    }

    override fun base64EncodedFiles(): Map<String, String> {
        return files.mapValues { base64Encoder.encodeToString(it.value.toByteArray()) }
    }

    fun newFile(name: String): OutputStream =
        ByteArrayOutputStream()
            .also { files[name] = it }

    fun addFiles(newFiles: Map<String, ByteArray>) {
        files.putAll(newFiles.mapValues { pair ->
            ByteArrayOutputStream().also {
                pair.value.inputStream().transferTo(it)
            }
        })
    }
}