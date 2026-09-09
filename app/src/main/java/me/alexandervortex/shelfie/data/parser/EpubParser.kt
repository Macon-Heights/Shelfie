package me.alexandervortex.shelfie.data.parser

import me.alexandervortex.shelfie.base.ext.toSpineSection
import me.alexandervortex.shelfie.data.mapper.ElementMapper
import me.alexandervortex.shelfie.model.BookDocument
import me.alexandervortex.shelfie.model.ByteImageModel
import me.alexandervortex.shelfie.model.ParsedBookModel
import me.alexandervortex.shelfie.model.PreviewBookModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import java.util.zip.ZipFile
import javax.inject.Inject

private data class SpineItem(
    val id: String,
    val path: String,
)

class EpubParser
@Inject constructor(
    private val elementMapper: ElementMapper,
) {

    suspend fun parse(
        zip: ZipFile
    ): ParsedBookModel = coroutineScope {
        val opfPath = withContext(Dispatchers.IO) { getOpfPath(zip) } ?: throw Exception("OPF not found")
        val opfDoc = withContext(Dispatchers.IO) {
            zip.getInputStream(zip.getEntry(opfPath)).use {
                Jsoup.parse(it, "UTF-8", "", Parser.xmlParser())
            }
        }

        val metadata = opfDoc.selectFirst("metadata")
        val title = metadata?.selectFirst("dc|title")?.text()
            ?: metadata?.selectFirst("title")?.text()
        val author = metadata?.selectFirst("dc|creator")?.text()
            ?: metadata?.selectFirst("creator")?.text()

        val manifest = opfDoc.select("manifest > item")
            .associate { it.attr("id") to it.attr("href") }

        val spine = opfDoc.select("spine > itemref")
            .mapNotNull { item ->
                val id = item.attr("idref")
                val path = manifest[id] ?: return@mapNotNull null
                SpineItem(id = id, path = path)
            }

        val binaries = manifest.values
            .filter { path ->
                path.endsWith(".jpg", true) || path.endsWith(".png", true) || path.endsWith(".jpeg", true)
            }
            .map { path ->
                async(Dispatchers.IO) {
                    val fullPath = resolvePath(opfPath, path)
                    zip.getEntry(fullPath)?.let { entry ->
                        val data = zip.getInputStream(entry).use { it.readBytes() }
                        path to data
                    }
                }
            }
            .awaitAll()
            .filterNotNull()
            .flatMap { (path, data) ->
                listOf(path to data, path.substringAfterLast("/") to data)
            }
            .toMap()

        // Get cover image specifically
        val coverId = metadata?.selectFirst("meta[name=cover]")?.attr("content")
        val coverPath = coverId?.let { manifest[it] }
            ?: manifest.values.find {
                it.contains("cover", true) && (it.endsWith(".jpg", true) || it.endsWith(
                    ".png",
                    true
                ))
            }

        val coverImage =
            ByteImageModel(coverPath?.let { binaries[it] ?: binaries[it.substringAfterLast("/")] })

        val chapters = spine.map { item ->
            async {
                val fullPath = resolvePath(base = opfPath, relative = item.path)
                val entry = withContext(Dispatchers.IO) { zip.getEntry(fullPath) } ?: return@async null
                val doc = withContext(Dispatchers.IO) {
                    zip.getInputStream(entry).use {
                        Jsoup.parse(
                            it,
                            "UTF-8",
                            "",
                            Parser.xmlParser()
                        )
                    }
                }

                val body = doc.selectFirst("body")
                    ?: return@async null

                val document = elementMapper.map(
                    root = body,
                    binaries = binaries,
                )

                document.toSpineSection(
                    id = item.id
                )
            }
        }.awaitAll().filterNotNull()

        ParsedBookModel(
            titleInfo = PreviewBookModel(
                title = title,
                author = author,
                coverImage = coverImage,
                date = metadata?.selectFirst("dc|date")?.text(),
                annotation = metadata?.selectFirst("dc|description")?.text(),
                genre = metadata?.selectFirst("dc|subject")?.text(),
                lang = metadata?.selectFirst("dc|language")?.text(),
                gallery = emptyList(),
            ),
            document = BookDocument(
                children = chapters
            ),
        )
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