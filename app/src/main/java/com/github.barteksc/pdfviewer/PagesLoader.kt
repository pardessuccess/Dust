/**
 * Copyright 2017 Bartosz Schiller
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.barteksc.pdfviewer

import android.graphics.RectF
import com.github.barteksc.pdfviewer.util.Constants
import com.github.barteksc.pdfviewer.util.Constants.Cache.CACHE_SIZE
import com.github.barteksc.pdfviewer.util.Constants.PRELOAD_OFFSET
import com.github.barteksc.pdfviewer.util.MathUtils.ceil
import com.github.barteksc.pdfviewer.util.MathUtils.floor
import com.github.barteksc.pdfviewer.util.MathUtils.max
import com.github.barteksc.pdfviewer.util.MathUtils.min
import com.github.barteksc.pdfviewer.util.Util.getDP
import org.benjinus.pdfium.util.SizeF
import java.util.LinkedList
import kotlin.math.abs

internal class PagesLoader(private val pdfView: PDFView) {
    private var cacheOrder = 0
    private var xOffset = 0f
    private var yOffset = 0f
    private var pageRelativePartWidth = 0f
    private var pageRelativePartHeight = 0f
    private var partRenderWidth = 0f
    private var partRenderHeight = 0f
    private val thumbnailRect = RectF(
        0f,
        0f,
        1f,
        1f
    )
    private val preloadOffset: Int = getDP(
        pdfView.context,
        PRELOAD_OFFSET
    )

    private class Holder {
        var row = 0
        var col = 0
        override fun toString(): String {
            return "Holder{row=$row, col=$col}"
        }
    }

    private class RenderRange() {
        var page = 0
        var gridSize: GridSize = GridSize()
        var leftTop: Holder = Holder()
        var rightBottom: Holder = Holder()
        override fun toString(): String {
            return "RenderRange{page=$page, gridSize=$gridSize, leftTop=$leftTop, rightBottom=$rightBottom}"
        }

    }

    private class GridSize {
        var rows = 0
        var cols = 0
        override fun toString(): String {
            return "GridSize{rows=$rows, cols=$cols}"
        }
    }

    private fun getPageColsRows(
        grid: GridSize,
        pageIndex: Int
    ) {
        val size = pdfView.pdfFile?.getPageSize(pageIndex) ?: SizeF(
            0f,
            0f
        )
        val ratioX = 1f / size.width
        val ratioY = 1f / size.height
        val partHeight = Constants.PART_SIZE * ratioY / pdfView.zoom
        val partWidth = Constants.PART_SIZE * ratioX / pdfView.zoom
        grid.rows = ceil(1f / partHeight)
        grid.cols = ceil(1f / partWidth)
    }

    private fun calculatePartSize(grid: GridSize) {
        pageRelativePartWidth = 1f / grid.cols.toFloat()
        pageRelativePartHeight = 1f / grid.rows.toFloat()
        partRenderWidth = Constants.PART_SIZE / pageRelativePartWidth
        partRenderHeight = Constants.PART_SIZE / pageRelativePartHeight
    }

    /**
     * calculate the render range of each page
     */
    private fun getRenderRangeList(
        firstXOffset: Float,
        firstYOffset: Float,
        lastXOffset: Float,
        lastYOffset: Float
    ): List<RenderRange> {
        val fixedFirstXOffset = -max(
            firstXOffset,
            0f
        )
        val fixedFirstYOffset = -max(
            firstYOffset,
            0f
        )
        val fixedLastXOffset = -max(
            lastXOffset,
            0f
        )
        val fixedLastYOffset = -max(
            lastYOffset,
            0f
        )
        val offsetFirst = if (pdfView.isSwipeVertical) fixedFirstYOffset else fixedFirstXOffset
        val offsetLast = if (pdfView.isSwipeVertical) fixedLastYOffset else fixedLastXOffset
        val firstPage = pdfView.pdfFile?.getPageAtOffset(
            offsetFirst,
            pdfView.zoom
        ) ?: 0
        val lastPage = pdfView.pdfFile?.getPageAtOffset(
            offsetLast,
            pdfView.zoom
        ) ?: 0
        val pageCount = lastPage - firstPage + 1
//        println("pageCount@@@@" + pageCount)
        val renderRanges: MutableList<RenderRange> = LinkedList()
        for (page in firstPage..lastPage) {
            val range = RenderRange()
            range.page = page
            var pageFirstXOffset: Float
            var pageFirstYOffset: Float
            var pageLastXOffset: Float
            var pageLastYOffset: Float
            if (page == firstPage) {
                pageFirstXOffset = fixedFirstXOffset
                pageFirstYOffset = fixedFirstYOffset
                if (pageCount == 1) {
                    pageLastXOffset = fixedLastXOffset
                    pageLastYOffset = fixedLastYOffset
                } else {
                    val pageOffset = pdfView.pdfFile?.getPageOffset(
                        page,
                        pdfView.zoom
                    ) ?: 0f
                    val pageSize = pdfView.pdfFile?.getScaledPageSize(
                        page,
                        pdfView.zoom
                    )
                    if (pdfView.isSwipeVertical) {
                        pageLastXOffset = fixedLastXOffset
                        pageLastYOffset = pageOffset + (pageSize?.height ?: 0f)
                    } else {
                        pageLastYOffset = fixedLastYOffset
                        pageLastXOffset = pageOffset + (pageSize?.width ?: 0f)
                    }
                }
            } else if (page == lastPage) {
                val pageOffset = pdfView.pdfFile?.getPageOffset(
                    page,
                    pdfView.zoom
                ) ?: 0f
                if (pdfView.isSwipeVertical) {
                    pageFirstXOffset = fixedFirstXOffset
                    pageFirstYOffset = pageOffset
                } else {
                    pageFirstYOffset = fixedFirstYOffset
                    pageFirstXOffset = pageOffset
                }
                pageLastXOffset = fixedLastXOffset
                pageLastYOffset = fixedLastYOffset
            } else {
                val pageOffset = pdfView.pdfFile?.getPageOffset(
                    page,
                    pdfView.zoom
                ) ?: 0f
                val pageSize = pdfView.pdfFile?.getScaledPageSize(
                    page,
                    pdfView.zoom
                )
                if (pdfView.isSwipeVertical) {
                    pageFirstXOffset = fixedFirstXOffset
                    pageFirstYOffset = pageOffset
                    pageLastXOffset = fixedLastXOffset
                    pageLastYOffset = pageOffset + (pageSize?.height ?: 0f)
                } else {
                    pageFirstXOffset = pageOffset
                    pageFirstYOffset = fixedFirstYOffset
                    pageLastXOffset = pageOffset + (pageSize?.width ?: 0f)
                    pageLastYOffset = fixedLastYOffset
                }
            }
            getPageColsRows(
                range.gridSize,
                range.page
            ) // get the page's grid size that rows and cols
            val scaledPageSize = pdfView.pdfFile?.getScaledPageSize(
                range.page,
                pdfView.zoom
            ) ?: SizeF(
                0f,
                0f
            )
            val rowHeight = scaledPageSize.height / range.gridSize.rows
            val colWidth = scaledPageSize.width / range.gridSize.cols

            // get the page offset int the whole file
            // ---------------------------------------
            // |            |           |            |
            // |<--offset-->|   (page)  |<--offset-->|
            // |            |           |            |
            // |            |           |            |
            // ---------------------------------------
            val secondaryOffset = pdfView.pdfFile?.getSecondaryPageOffset(
                page,
                pdfView.zoom
            ) ?: 0f

            // calculate the row,col of the point in the leftTop and rightBottom
            if (pdfView.isSwipeVertical) {
                range.leftTop.row = floor(
                    abs(
                        pageFirstYOffset - (pdfView.pdfFile?.getPageOffset(
                            range.page,
                            pdfView.zoom
                        ) ?: 0f)
                    ) / rowHeight
                )
                range.leftTop.col = floor(
                    min(
                        pageFirstXOffset - secondaryOffset,
                        0f
                    ) / colWidth
                )
                range.rightBottom.row = ceil(
                    abs(
                        pageLastYOffset - (pdfView.pdfFile?.getPageOffset(
                            range.page,
                            pdfView.zoom
                        ) ?: 0f)
                    ) / rowHeight
                )
                range.rightBottom.col = floor(
                    min(
                        pageLastXOffset - secondaryOffset,
                        0f
                    ) / colWidth
                )
            } else {
                range.leftTop.col = floor(
                    abs(
                        pageFirstXOffset - (pdfView.pdfFile?.getPageOffset(
                            range.page,
                            pdfView.zoom
                        ) ?: 0f)
                    ) / colWidth
                )
                range.leftTop.row = floor(
                    min(
                        pageFirstYOffset - secondaryOffset,
                        0f
                    ) / rowHeight
                )
                range.rightBottom.col = floor(
                    abs(
                        pageLastXOffset - (pdfView.pdfFile?.getPageOffset(
                            range.page,
                            pdfView.zoom
                        ) ?: 0f)
                    ) / colWidth
                )
                range.rightBottom.row = floor(
                    min(
                        pageLastYOffset - secondaryOffset,
                        0f
                    ) / rowHeight
                )
            }
            renderRanges.add(range)
        }
        return renderRanges
    }

    private fun loadVisible(searchQuery: String) {
        var parts = 0
        val scaledPreloadOffset = preloadOffset.toFloat()
        val firstXOffset = -xOffset + scaledPreloadOffset
        val lastXOffset = -xOffset - pdfView.width - scaledPreloadOffset
        val firstYOffset = -yOffset + scaledPreloadOffset
        val lastYOffset = -yOffset - pdfView.height - scaledPreloadOffset
        val rangeList = getRenderRangeList(
            firstXOffset,
            firstYOffset,
            lastXOffset,
            lastYOffset
        )
        for (range in rangeList) {
            loadThumbnail(
                range.page,
                searchQuery
            )
        }
        for (range in rangeList) {
            calculatePartSize(range.gridSize)
            parts += loadPage(
                range.page,
                range.leftTop.row,
                range.rightBottom.row,
                range.leftTop.col,
                range.rightBottom.col,
                CACHE_SIZE - parts,
                searchQuery
            )
            if (parts >= CACHE_SIZE) {
                break
            }
        }
    }

    private fun loadPage(
        page: Int,
        firstRow: Int,
        lastRow: Int,
        firstCol: Int,
        lastCol: Int,
        nbOfPartsLoadable: Int,
        searchQuery: String
    ): Int {
        var loaded = 0
        for (row in firstRow..lastRow) {
            for (col in firstCol..lastCol) {
                if (loadCell(
                        page,
                        row,
                        col,
                        pageRelativePartWidth,
                        pageRelativePartHeight,
                        searchQuery
                    )
                ) {
                    loaded++
                }
                if (loaded >= nbOfPartsLoadable) {
                    return loaded
                }
            }
        }
        return loaded
    }

    private fun loadCell(
        page: Int,
        row: Int,
        col: Int,
        pageRelativePartWidth: Float,
        pageRelativePartHeight: Float,
        searchQuery: String
    ): Boolean {
        val relX = pageRelativePartWidth * col
        val relY = pageRelativePartHeight * row
        var relWidth = pageRelativePartWidth
        var relHeight = pageRelativePartHeight
        var renderWidth = partRenderWidth
        var renderHeight = partRenderHeight
        if (relX + relWidth > 1) {
            relWidth = 1 - relX
        }
        if (relY + relHeight > 1) {
            relHeight = 1 - relY
        }
        renderWidth *= relWidth
        renderHeight *= relHeight
        val pageRelativeBounds = RectF(
            relX,
            relY,
            relX + relWidth,
            relY + relHeight
        )
        if (renderWidth > 0 && renderHeight > 0) {
            if (!pdfView.cacheManager.upPartIfContained(
                    page,
                    pageRelativeBounds,
                    cacheOrder,
                    searchQuery
                )
            ) {
                pdfView.renderingHandler?.addPartRenderingTask(
                    page,
                    renderWidth,
                    renderHeight,
                    pageRelativeBounds,
                    false,
                    cacheOrder,
                    pdfView.isBestQuality,
                    pdfView.isAnnotationRendering,
                    searchQuery
                )
            }
            cacheOrder++
            return true
        }
        return false
    }

    private fun loadThumbnail(
        page: Int,
        searchQuery: String
    ) {
        val pageSize = pdfView.pdfFile?.getPageSize(page) ?: SizeF(
            0f,
            0f
        )
        val thumbnailWidth = pageSize.width * Constants.THUMBNAIL_RATIO
        val thumbnailHeight = pageSize.height * Constants.THUMBNAIL_RATIO
        if (!pdfView.cacheManager.containsThumbnail(
                page,
                thumbnailRect,
                searchQuery
            )
        ) {
            pdfView.renderingHandler?.addPartRenderingTask(
                page,
                thumbnailWidth,
                thumbnailHeight,
                thumbnailRect,
                true,
                0,
                pdfView.isBestQuality,
                pdfView.isAnnotationRendering,
                searchQuery
            )
        }
    }

    fun loadPages(searchQuery: String) {
        cacheOrder = 1
        xOffset = -max(
            pdfView.currentXOffset,
            0f
        )
        yOffset = -max(
            pdfView.currentYOffset,
            0f
        )
        loadVisible(searchQuery)
    }

    fun parseText(pagesIndexes: List<Int>) {
        if (pagesIndexes.isNotEmpty()) {
            for (index in pagesIndexes) {
                pdfView.renderingHandler?.addParseTextTask(index)
            }
        }
    }

    fun renderPage(
        page: Int,
        isThumbnail: Boolean = true,
        size: SizeF = pdfView.pdfFile?.getPageSize(page) ?: SizeF(
            0f,
            0f
        )
    ) {
        val ratio = if (isThumbnail) Constants.THUMBNAIL_RATIO else 1.0f
        val width = size.width * ratio
        val height = size.height * ratio

        pdfView.renderingHandler?.addPageRenderingTask(
            page = page,
            width = width,
            height = height
        )

    }

}