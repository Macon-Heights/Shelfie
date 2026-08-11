package me.alexandervortex.shelfie.data.parser.epub

import me.alexandervortex.shelfie.base.ext.normalizeEmptyLines
import me.alexandervortex.shelfie.base.ext.normalizeEmptyTextUI
import me.alexandervortex.shelfie.base.ext.splitPartsBySentences
import me.alexandervortex.shelfie.data.mapper.ElementMapper
import me.alexandervortex.shelfie.ui.component.new.ImageUIModel
import me.alexandervortex.shelfie.ui.model.BookUIModel
import me.alexandervortex.shelfie.ui.model.ElementUIModel
import me.alexandervortex.shelfie.features.screens.addbook.mvi.PreviewScreenUIModel
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import java.io.File
import java.io.InputStream
import java.util.zip.ZipFile
import javax.inject.Inject

class EpubParser
@Inject constructor(
    private val elementMapper: ElementMapper,
) {

    fun getPreview(inputStream: InputStream): PreviewScreenUIModel {
        val tempFile = File.createTempFile("epub_preview", ".epub")
        return try {
            inputStream.use { input ->
                tempFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            parse(id = "preview", file = tempFile, scrollOffset = 0, scrollIndex = 0).titleInfo
        } catch (_: Exception) {
            PreviewScreenUIModel(null, null, null, null, null, null, null, emptyList())
        } finally {
            if (tempFile.exists()) tempFile.delete()
        }
    }

    fun parse(
        id: String,
        file: File,
        scrollOffset: Int,
        scrollIndex: Int,
    ): BookUIModel {
        ZipFile(file).use { zip ->
            val opfPath = getOpfPath(zip) ?: throw Exception("OPF not found")
            val opfDoc = zip.getInputStream(zip.getEntry(opfPath)).use {
                Jsoup.parse(it, "UTF-8", "", Parser.xmlParser())
            }

            val metadata = opfDoc.selectFirst("metadata")
            val title = metadata?.selectFirst("dc|title")?.text()
                ?: metadata?.selectFirst("title")?.text()
            val author = metadata?.selectFirst("dc|creator")?.text()
                ?: metadata?.selectFirst("creator")?.text()
            
            val manifest = opfDoc.select("manifest > item").associate {
                it.attr("id") to it.attr("href")
            }
            
            val spine = opfDoc.select("spine > itemref").mapNotNull {
                manifest[it.attr("idref")]
            }

            val binaries = mutableMapOf<String, ByteArray>()
            manifest.values.forEach { path ->
                if (path.endsWith(".jpg", true) || path.endsWith(".png", true) || path.endsWith(".jpeg", true)) {
                    val fullPath = resolvePath(opfPath, path)
                    zip.getEntry(fullPath)?.let { entry ->
                        val data = zip.getInputStream(entry).use { it.readBytes() }
                        binaries[path] = data
                        binaries[path.substringAfterLast("/")] = data
                    }
                }
            }

            // Get cover image specifically
            val coverId = metadata?.selectFirst("meta[name=cover]")?.attr("content")
            val coverPath = coverId?.let { manifest[it] } 
                ?: manifest.values.find { it.contains("cover", true) && (it.endsWith(".jpg", true) || it.endsWith(".png", true)) }
            
            val coverImage = ImageUIModel(coverPath?.let { binaries[it] ?: binaries[it.substringAfterLast("/")] })

            val elements = mutableListOf<ElementUIModel>()
            spine.forEach { path ->
                val fullPath = resolvePath(opfPath, path)
                zip.getEntry(fullPath)?.let { entry ->
                    val doc = zip.getInputStream(entry).use {
                        Jsoup.parse(it, "UTF-8", "", Parser.xmlParser())
                    }
                    val body = doc.selectFirst("body") ?: doc
                    elements.addAll(elementMapper.map(body, binaries))
                }
            }

            return BookUIModel(
                id = id,
                localPath = file.path,
                titleInfo = PreviewScreenUIModel(
                    title = title,
                    author = author,
                    coverImage = coverImage,
                    date = metadata?.selectFirst("dc|date")?.text(),
                    annotation = metadata?.selectFirst("dc|description")?.text(),
                    genre = metadata?.selectFirst("dc|subject")?.text(),
                    lang = metadata?.selectFirst("dc|language")?.text(),
                    manyImages = emptyList()
                ),
                elements = elements
                    .splitPartsBySentences()
                    .normalizeEmptyTextUI()
                    .normalizeEmptyLines(),
                progressIndex = scrollIndex,
                progressOffset = scrollOffset
            )
        }
    }

    private fun getOpfPath(zip: ZipFile): String? {
        val entry = zip.getEntry("META-INF/container.xml") ?: return null
        val doc = zip.getInputStream(entry).use { Jsoup.parse(it, "UTF-8", "", Parser.xmlParser()) }
        return doc.selectFirst("rootfile")?.attr("full-path")
    }

    private fun resolvePath(base: String, relative: String): String {
        if (!base.contains("/")) return relative
        val parent = base.substringBeforeLast("/")
        val parts = (parent.split("/") + relative.split("/")).filter { it.isNotEmpty() }
        val result = mutableListOf<String>()
        for (part in parts) {
            when (part) {
                "." -> continue
                ".." -> if (result.isNotEmpty()) result.removeAt(result.size - 1)
                else -> result.add(part)
            }
        }
        return result.joinToString("/")
    }
}
