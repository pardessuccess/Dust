package com.github.barteksc.pdfviewer

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import com.github.barteksc.pdfviewer.RenderingHandler.RenderingTask
import com.github.barteksc.pdfviewer.exception.PageRenderingException
import com.github.barteksc.pdfviewer.model.PagePart
import com.github.barteksc.pdfviewer.util.addArea
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * A [Handler] that will process incoming [RenderingTask] messages
 * and alert [PDFView.onBitmapRendered] when the portion of the
 * PDF is ready to render.
 */
internal class RenderingHandler(
    looper: Looper,
    private val pdfView: PDFView
) : Handler(looper) {

    private val renderBounds = RectF()
    private val roundedRenderBounds = Rect()
    private val renderMatrix = Matrix()
    private var running = false

    fun addPartRenderingTask(
        page: Int,
        width: Float,
        height: Float,
        bounds: RectF,
        thumbnail: Boolean,
        cacheOrder: Int,
        bestQuality: Boolean,
        annotationRendering: Boolean,
        searchQuery: String?
    ) {
        val task = RenderingTask(
            width,
            height,
            bounds,
            page,
            thumbnail,
            cacheOrder,
            bestQuality,
            annotationRendering,
            searchQuery
        )
        val msg = obtainMessage(
            MSG_RENDER_PART_TASK,
            task
        )
        sendMessage(msg)
    }

    fun addParseTextTask(page: Int) {
        val task = RenderingTask(
            page,
            true
        )
        val msg = obtainMessage(
            MSG_PARSE_TEXT_TASK,
            task
        )
        sendMessage(msg)
    }

    fun addPageRenderingTask(
        page: Int,
        width: Float,
        height: Float,
    ) {
        val task = RenderingTask(
            page = page,
            width = width,
            height = height
        )
        val msg = obtainMessage(
            MSG_RENDER_PAGE_TASK,
            task
        )
        sendMessage(msg)
    }

    override fun handleMessage(message: Message) {
        val task = message.obj as RenderingTask
        try {
            if (task.parseTextFromPdf && message.what == MSG_PARSE_TEXT_TASK) {
                val pageStr = parseText(task)
                if (running) {
                    pdfView.post {
                        pdfView.onTextParsed(
                            task.page,
                            pageStr
                        )
                    }
                }
            } else if (message.what == MSG_RENDER_PAGE_TASK) {
                val bitmap = renderPage(renderingTask = task)
                if (running) {
                    pdfView.post {
                        pdfView.onPageRendered(
                            pageIndex = task.page,
                            bitmap = bitmap
                        )
                    }
                } else {
                    bitmap?.recycle()
                }
            } else {
                val part = proceed(task)
                if (part != null) {
                    if (running) {
                        pdfView.post { pdfView.onBitmapRendered(part) }
                    } else {
                        val b = part.renderedBitmap
                        b?.recycle()
                    }
                }
            }
        } catch (ex: PageRenderingException) {
            pdfView.post { pdfView.onPageError(ex) }
        }
    }

    @Throws(PageRenderingException::class)
    private fun parseText(renderingTask: RenderingTask): String? {
        val pdfFile = pdfView.pdfFile
        pdfFile?.openPage(renderingTask.page)
        val countCharsOnPage = pdfFile?.countCharactersOnPage(renderingTask.page) ?: 0
        return if (countCharsOnPage > 0) {
            pdfFile?.extractCharacters(
                renderingTask.page,
                0,
                countCharsOnPage
            )
        } else {
            null
        }
    }

    private fun renderPage(renderingTask: RenderingTask): Bitmap? {
        val pdfFile = pdfView.pdfFile
        pdfFile?.openPage(renderingTask.page)
        val w = renderingTask.width.roundToInt()
        val h = renderingTask.height.roundToInt()
        val pageBitmap: Bitmap = try {
            Bitmap.createBitmap(
                w,
                h,  /*renderingTask.bestQuality ? */
                Bitmap.Config.ARGB_8888 /* : Bitmap.Config.RGB_565*/
            )
        } catch (e: IllegalArgumentException) {
            Log.e(
                TAG,
                "Cannot create bitmap",
                e
            )
            return null
        }

        pdfFile?.renderPageBitmap(
            bitmap = pageBitmap,
            pageIndex = renderingTask.page,
            bounds = Rect(
                0,
                0,
                w,
                h
            ),
            annotationRendering = renderingTask.annotationRendering,
            colorScheme = pdfView.colorScheme
        )

        return pageBitmap
    }

    @Throws(PageRenderingException::class)
    private fun proceed(renderingTask: RenderingTask): PagePart? {
        val pdfFile = pdfView.pdfFile
        pdfFile?.openPage(renderingTask.page)
        val w = renderingTask.width.roundToInt()
        val h = renderingTask.height.roundToInt()
        val nativePageSize = pdfFile?.getPageSizeNative(renderingTask.page)
        val nativePageWidth = nativePageSize?.width?.toFloat() ?: 0f
        val nativePageHeight = nativePageSize?.height?.toFloat() ?: 0f
        if (w == 0 || h == 0 || pdfFile?.pageHasError(renderingTask.page) == true) {
            return null
        }
        val pageBitmap: Bitmap = try {
            Bitmap.createBitmap(
                w,
                h,
                Bitmap.Config.ARGB_8888
            )
        } catch (e: IllegalArgumentException) {
            Log.e(
                TAG,
                "Cannot create bitmap",
                e
            )
            return null
        }
        val c = Canvas(pageBitmap)
        val p = Paint()

        p.color = Color.WHITE
        pdfView.colorScheme?.backgroundColor?.let {
            p.color = it
        }
        val bRect = Rect(
            0,
            0,
            pageBitmap.width,
            pageBitmap.height
        )
        c.drawRect(
            bRect,
            p
        )
        calculateBounds(
            w,
            h,
            renderingTask.bounds
        )

        val searchQuery = renderingTask.searchQuery ?: ""
        if (searchQuery.isNotBlank()) {
            val search = pdfFile?.newPageSearch(
                pageIndex = renderingTask.page,
                query = searchQuery,
                matchCase = false,
                matchWholeWord = false
            )

            search?.searchNext()

            if (search?.hasNext() == true) {
                while (true) {
                    val rect = search.searchNext() ?: break

                    //If thumbnail
                    if (roundedRenderBounds.width() <= nativePageWidth.toInt()) {
                        val currentRenderedRealRectByBounds = RectF(
                            renderingTask.bounds.left * nativePageWidth,
                            renderingTask.bounds.top * nativePageHeight,
                            renderingTask.bounds.right * nativePageWidth,
                            renderingTask.bounds.bottom * nativePageHeight
                        )
                        if (rect.intersect(currentRenderedRealRectByBounds)) {
                            val l1 = rect.left * roundedRenderBounds.width() / nativePageWidth
                            val t1 =
                                roundedRenderBounds.height() - rect.top * roundedRenderBounds.height() / nativePageHeight
                            val r1 = rect.right * roundedRenderBounds.width() / nativePageWidth
                            val b1 =
                                roundedRenderBounds.height() - rect.bottom * roundedRenderBounds.height() / nativePageHeight
                            var strLen = searchQuery.length
                            if (strLen < 1) {
                                strLen = 1
                            }
                            val oneSymbolWidth = r1 - l1
                            val w1 = l1 + oneSymbolWidth * strLen
                            p.color = pdfFile.textHighlightColor
                            c.drawRect(
                                RectF(
                                    l1,
                                    t1,
                                    w1,
                                    b1
                                ).also {
                                    it.addArea(
                                        scaleFactorVertical = 0.3f
                                    )
                                },
                                p
                            )
                        } else {
                            break
                        }
                    } else {
                        val rectForBitmap = RectF(
                            renderingTask.bounds.left * renderBounds.width(),
                            renderingTask.bounds.top * renderBounds.height(),
                            renderingTask.bounds.right * renderBounds.width(),
                            renderingTask.bounds.bottom * renderBounds.height()
                        )
                        val left = rect.left / nativePageWidth * renderBounds.width()
                        val rr = rect.right / nativePageWidth * renderBounds.width()
                        var strLen = searchQuery.length
                        if (strLen < 1) {
                            strLen = 1
                        }
                        val symbolWidth = rr - left
                        val ww1 = left + (symbolWidth * (strLen + 1))
                        val rectForSearch = RectF(
                            left,
                            renderBounds.height() - rect.top / nativePageHeight * renderBounds.height(),
                            ww1,
                            renderBounds.height() - rect.bottom / nativePageHeight * renderBounds.height()
                        )
                        if (rectForSearch.intersect(rectForBitmap)) {

                            val l1 = abs(
                                abs(rectForSearch.left) - abs(rectForBitmap.left)
                            )
                            val t1 = abs(
                                abs(rectForSearch.top) - abs(rectForBitmap.top)
                            )
                            val r1 = l1 + rectForSearch.width()
                            val b1 = t1 + rectForSearch.height()

                            val realRect = RectF(
                                max(
                                    0f,
                                    min(
                                        pageBitmap.width.toFloat(),
                                        l1
                                    )
                                ),
                                max(
                                    0f,
                                    min(
                                        pageBitmap.height.toFloat(),
                                        t1
                                    )
                                ),
                                min(
                                    r1,
                                    pageBitmap.width.toFloat()
                                ),  /*min(pageBitmap.getHeight(), b1)*/
                                min(
                                    pageBitmap.height.toFloat(),
                                    b1
                                )
                            ).also {
                                it.addArea(
                                    scaleFactorVertical = 0.3f
                                )
                            }
                            p.color = pdfFile.textHighlightColor
                            c.drawRect(
                                realRect,
                                p
                            )
                        }
                    }
                }
            }
        }
        pdfFile?.renderPageBitmap(
            pageBitmap,
            renderingTask.page,
            roundedRenderBounds,
            renderingTask.annotationRendering,
            pdfView.colorScheme
        )
        return PagePart(
            renderingTask.page,
            pageBitmap,
            renderingTask.bounds,
            renderingTask.thumbnail,
            renderingTask.cacheOrder,
            searchQuery
        )
    }

    private fun calculateBounds(
        width: Int,
        height: Int,
        pageSliceBounds: RectF?
    ) {
        renderMatrix.reset()
        renderMatrix.postTranslate(
            -(pageSliceBounds?.left ?: 0f) * width,
            -(pageSliceBounds?.top ?: 0f) * height
        )
        renderMatrix.postScale(
            1 / (pageSliceBounds?.width() ?: 0f),
            1 / (pageSliceBounds?.height() ?: 0f)
        )
        renderBounds[0f, 0f, width.toFloat()] = height.toFloat()
        renderMatrix.mapRect(renderBounds)
        renderBounds.round(roundedRenderBounds)
    }

    fun stop() {
        running = false
    }

    fun start() {
        running = true
    }

    private class RenderingTask {
        var width: Float
        var height: Float
        var bounds: RectF
        var page: Int
        var thumbnail: Boolean
        var cacheOrder: Int
        var bestQuality: Boolean
        var annotationRendering: Boolean
        var searchQuery: String? = ""
        var parseTextFromPdf = false

        constructor(
            width: Float,
            height: Float,
            bounds: RectF,
            page: Int,
            thumbnail: Boolean,
            cacheOrder: Int,
            bestQuality: Boolean,
            annotationRendering: Boolean,
            searchQuery: String?
        ) {
            this.page = page
            this.width = width
            this.height = height
            this.bounds = bounds
            this.thumbnail = thumbnail
            this.cacheOrder = cacheOrder
            this.bestQuality = bestQuality
            this.annotationRendering = annotationRendering
            this.searchQuery = searchQuery
        }

        constructor(
            page: Int,
            parseText: Boolean
        ) {
            parseTextFromPdf = parseText
            this.page = page
            width = 0f
            height = 0f
            bounds = RectF(
                0f,
                0f,
                0f,
                0f
            )
            thumbnail = false
            cacheOrder = 0
            bestQuality = false
            annotationRendering = false
        }

        constructor(
            width: Float,
            height: Float,
            page: Int,

            ) {
            this.page = page
            this.width = width
            this.height = height
            this.bounds = RectF()
            this.thumbnail = true
            this.cacheOrder = 0
            this.bestQuality = false
            this.annotationRendering = false
        }
    }

    companion object {
        /**
         * [Message.what] kind of message this handler processes.
         */
        const val MSG_RENDER_PART_TASK = 1
        const val MSG_PARSE_TEXT_TASK = 2
        const val MSG_RENDER_PAGE_TASK = 3
        private val TAG = RenderingHandler::class.java.name
    }
}