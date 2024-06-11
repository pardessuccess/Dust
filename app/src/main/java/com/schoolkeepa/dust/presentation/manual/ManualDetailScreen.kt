package com.schoolkeepa.dust.presentation.manual

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.schoolkeepa.dust.R
import com.schoolkeepa.dust.pdf.BlackPage
import com.schoolkeepa.dust.pdf.Content
import com.schoolkeepa.dust.pdf.HorizontalPDFReader
import com.schoolkeepa.dust.pdf.HorizontalPdfReaderState
import com.schoolkeepa.dust.pdf.PageContentInt
import com.schoolkeepa.dust.pdf.PdfImage
import com.schoolkeepa.dust.pdf.ResourceType
import com.schoolkeepa.dust.pdf.SearchSection
import com.schoolkeepa.dust.pdf.VerticalPDFReader
import com.schoolkeepa.dust.pdf.VerticalPdfReaderState
import com.schoolkeepa.dust.pdf.load
import com.schoolkeepa.dust.pdf.search
import com.schoolkeepa.dust.pdf.tapToZoomVertical
import com.schoolkeepa.dust.presentation.component.DustTopAppBar
import com.schoolkeepa.dust.presentation.navigation.Screen
import com.schoolkeepa.dust.ui.theme.Gray200
import com.schoolkeepa.dust.ui.theme.Gray500
import com.schoolkeepa.dust.ui.theme.Gray900
import com.schoolkeepa.dust.ui.theme.Green300
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManualDetailScreen(
    viewModel: ManualViewModel = hiltViewModel(),
    manualId: String,
    title: String,
) {
    val state = viewModel.stateFlow.collectAsState()
    var searchState = remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    println(manualId.toString() + "@@@@")
    viewModel.openResource(
        ResourceType.Asset(manualId.toInt())
    )
    Scaffold(
        topBar = {
            if (searchState.value) {
                TopAppBar(title = {
                    Row(
                        modifier = Modifier
                            .padding(
                            )
                            .fillMaxWidth()
                            .height(56.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp)
                                .padding(
                                    top = 10.dp,
                                )
                                .background(
                                    shape = RoundedCornerShape(14.dp),
                                    color = Gray200,
                                )
                        ) {
                            BasicTextField(
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .padding(start = 16.dp),
                                value = searchText,
                                textStyle = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = Gray900
                                ),
                                maxLines = 1,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text
                                ),
                                onValueChange = { searchText = it },
                                decorationBox = { innerTextField ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .fillMaxHeight()
                                    ) {
                                        if (searchText.isEmpty()) {
                                            Text(
                                                modifier = Modifier
                                                    .align(Alignment.CenterStart)
                                                    .padding(start = 24.dp),
                                                text = "미세먼지에 대해 검색해 보세요.",
                                                fontSize = 18.sp,
                                                color = Gray500
                                            )
                                        } else {
                                            TextButton(
                                                modifier = Modifier
                                                    .wrapContentWidth()
                                                    .fillMaxHeight()
                                                    .padding(top = 3.dp, bottom = 3.dp, end = 3.dp)
                                                    .align(Alignment.CenterEnd),
                                                colors = ButtonDefaults.buttonColors(Green300),
                                                shape = RoundedCornerShape(25),
                                                onClick = {  }
                                            ) {
                                                Text(
                                                    modifier = Modifier
                                                        .fillMaxHeight(),
                                                    text = "검색",
                                                    color = Color.White
                                                )
                                            }
                                        }

                                        Row(
                                            modifier = Modifier
                                                .fillMaxHeight(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                modifier = Modifier.padding(end = 5.dp),
                                                painter = painterResource(id = R.drawable.ic_search),
                                                contentDescription = ""
                                            )
                                            innerTextField()
                                        }
                                    }

                                },
                            )
                        }
                        TextButton(
                            modifier = Modifier.align(Alignment.Bottom),
                            onClick = {
                                searchText = ""
                                searchState.value = false
                            }) {
                            Text(text = "취소")
                        }
                    }
                })
            } else {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = title,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {  }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow_left),
                                contentDescription = "arrow left icon"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { searchState.value = true }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_search),
                                contentDescription = "arrow left icon"
                            )
                        }
                    }
                )
            }

        },
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (val actualState = state.value) {
                is VerticalPdfReaderState -> PDFView(
                    pdfState = actualState,
                    searchState = { searchState.value },
                )
            }
        }
    }

}

@Composable
fun PDFView(
    pdfState: VerticalPdfReaderState,
    searchState: () -> Boolean,
) {
    Box(
        contentAlignment = Alignment.TopStart
    ) {
        val mList = mutableListOf<Content>()
        val lazyState = pdfState.lazyState
        val coroutineScope = rememberCoroutineScope()
        var currentSearch by remember { mutableStateOf(0) }
        var findResult by remember { mutableStateOf(listOf<Int>()) }
        Column {
//            SearchSection {
//                findResult = search(it, mList)
//                if (findResult.last() != 0) {
//                    currentSearch = 0
//                    coroutineScope.launch {
//                        lazyState.animateScrollToItem(findResult[0])
//                    }
//                    println(findResult.toString() + currentSearch.toString())
//                }
//            }
            Box() {
                BoxWithConstraints(
                    modifier = Modifier,
                    contentAlignment = Alignment.TopCenter
                ) {
                    val ctx = LocalContext.current
                    DisposableEffect(key1 = Unit) {
                        load(
                            coroutineScope,
                            ctx,
                            pdfState,
                            constraints.maxWidth,
                            constraints.maxHeight,
                            true
                        )
                        onDispose {
                            pdfState.close()
                        }
                    }
                    pdfState.pdfRender?.let { pdf ->
                        coroutineScope.launch {
                            pdf.pageLists.forEach { page ->
                                page.load()
                                coroutineScope.launch {
                                    if (!page.isLoaded) {
                                        page.text.collectLatest {
                                            println("page.text.collectLatest" + it.toString())
                                            if (it.content.isNotEmpty()) {
                                                mList.add(it)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    pdfState.pdfRender?.let { pdf ->
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .tapToZoomVertical(pdfState, constraints),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            state = lazyState
                        ) {

                            items(pdf.pageCount) {
                                val pageContent = pdf.pageLists[it].stateFlow.collectAsState().value
                                when (pageContent) {
                                    is PageContentInt.PageContent -> {
                                        PdfImage(
                                            bitmap = { pageContent.bitmap.asImageBitmap() },
                                            contentDescription = pageContent.contentDescription
                                        )
                                    }

                                    is PageContentInt.BlankPage -> BlackPage(
                                        width = pageContent.width,
                                        height = pageContent.height
                                    )
                                }
                            }
                        }
                    }
                }
                if (searchState()) {

                }
            }
        }
    }
}

@Composable
fun HPDFView(
    pdfState: HorizontalPdfReaderState,
) {
    Box(
        contentAlignment = Alignment.TopStart
    ) {
        HorizontalPDFReader(
            state = pdfState,
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Gray)
        )
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                            shape = MaterialTheme.shapes.medium
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Page: ${pdfState.currentPage}/${pdfState.pdfPageCount}",
                        modifier = Modifier.padding(
                            start = 8.dp,
                            end = 8.dp,
                            bottom = 4.dp,
                            top = 8.dp
                        )
                    )
                    Text(
                        text = if (pdfState.isScrolling) {
                            "Scrolling"
                        } else {
                            "Stationary"
                        },
                        color = if (pdfState.isScrolling) {
                            MaterialTheme.colorScheme.onSurface
                        } else {
                            MaterialTheme.colorScheme.error
                        },
                        modifier = Modifier.padding(
                            start = 8.dp,
                            end = 8.dp,
                            bottom = 8.dp,
                            top = 4.dp
                        )
                    )
                    Text(text = "${pdfState.scale}")
                }
            }
        }
    }
}