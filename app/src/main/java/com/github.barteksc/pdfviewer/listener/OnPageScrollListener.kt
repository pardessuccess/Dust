/**
 * Copyright 2016 Bartosz Schiller
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
package com.github.barteksc.pdfviewer.listener

/**
 * Implements this interface to receive events from PDFView
 * when a page has been scrolled
 */
interface OnPageScrollListener {
    /**
     * Called on every move while scrolling
     *
     * @param page current page index
     * @param positionOffset see [com.github.barteksc.pdfviewer.PDFView.getPositionOffset]
     */
    fun onPageScrolled(
        page: Int,
        positionOffset: Float
    )
}