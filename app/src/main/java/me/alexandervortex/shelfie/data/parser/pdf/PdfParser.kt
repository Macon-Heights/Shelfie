package me.alexandervortex.shelfie.data.parser.pdf

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import me.alexandervortex.shelfie.ui.component.new.ImageUIModel
import me.alexandervortex.shelfie.ui.model.BookUIModel
import me.alexandervortex.shelfie.ui.model.ElementUIModel
import me.alexandervortex.shelfie.feature.preview.PreviewScreenUIModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import javax.inject.Inject

class PdfParser
@Inject constructor() {

    fun getPreview(inputStream: InputStream): PreviewScreenUIModel {
        val tempFile = File.createTempFile("pdf_preview", ".pdf")
        return try {
            inputStream.use { input ->
                tempFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            val parcelFileDescriptor = ParcelFileDescriptor.open(tempFile, ParcelFileDescriptor.MODE_READ_ONLY)
            val pdfRenderer = PdfRenderer(parcelFileDescriptor)
            
            val coverImage = if (pdfRenderer.pageCount > 0) {
                val page = pdfRenderer.openPage(0)
                // Use a smaller size for preview to save memory
                val bitmap = Bitmap.createBitmap(page.width / 2, page.height / 2, Bitmap.Config.ARGB_8888)
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                page.close()
                
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
                stream.toByteArray()
            } else null

            val pageCount = pdfRenderer.pageCount
            pdfRenderer.close()
            parcelFileDescriptor.close()

            PreviewScreenUIModel(
                title = tempFile.nameWithoutExtension,
                author = "PDF Document",
                coverImage = ImageUIModel(coverImage),
                date = null,
                annotation = "PDF Document ($pageCount pages)",
                genre = "PDF",
                lang = null,
                manyImages = emptyList()
            )
        } catch (_: Exception) {
            PreviewScreenUIModel(null, null, null, null, null, null, null, manyImages = emptyList())
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
        val parcelFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
        val pdfRenderer = PdfRenderer(parcelFileDescriptor)
        
        val elements = mutableListOf<ElementUIModel>()
        
        // Note: For large PDFs, rendering all pages to ByteArrays will cause OOM.
        // In a production app, we would use a specialized PDF viewer or render pages lazily.
        // For demonstration, we render all pages as images.
        for (i in 0 until pdfRenderer.pageCount) {
            val page = pdfRenderer.openPage(i)
            val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
            elements.add(ElementUIModel.ImageUIModel(stream.toByteArray()))
            
            page.close()
            bitmap.recycle()
        }

        val coverImage = (elements.firstOrNull() as? ElementUIModel.ImageUIModel)?.image

        val model = BookUIModel(
            id = id,
            localPath = file.path,
            titleInfo = PreviewScreenUIModel(
                title = file.nameWithoutExtension,
                author = "PDF Document",
                coverImage = ImageUIModel(coverImage),
                date = null,
                annotation = "PDF Document (${pdfRenderer.pageCount} pages)",
                genre = "PDF",
                lang = null,
                manyImages = emptyList()
            ),
            elements = elements,
            progressIndex = scrollIndex,
            progressOffset = scrollOffset
        )
        
        pdfRenderer.close()
        parcelFileDescriptor.close()
        
        return model
    }
}
