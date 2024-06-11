package com.schoolkeepa.dust.presentation.manual

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.schoolkeepa.dust.pdf.HorizontalPdfReaderState
import com.schoolkeepa.dust.pdf.PdfReaderState
import com.schoolkeepa.dust.pdf.ResourceType
import com.schoolkeepa.dust.pdf.VerticalPdfReaderState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ManualViewModel @Inject constructor() : ViewModel() {

    private val mStateFlow = MutableStateFlow<PdfReaderState?>(null)
    val stateFlow: StateFlow<PdfReaderState?> = mStateFlow

    val switchState = mutableStateOf(false)

    fun openResource(resourceType: ResourceType) {
        mStateFlow.tryEmit(
            if (switchState.value) {
                HorizontalPdfReaderState(resourceType, true)
            } else {
                VerticalPdfReaderState(resourceType, true)
            }
        )
    }

    fun clearResource() {
        mStateFlow.tryEmit(null)
    }

}