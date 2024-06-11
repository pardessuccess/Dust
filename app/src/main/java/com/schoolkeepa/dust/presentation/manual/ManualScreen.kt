package com.schoolkeepa.dust.presentation.manual

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.schoolkeepa.dust.R
import com.schoolkeepa.dust.presentation.navigation.Screen
import com.schoolkeepa.dust.ui.theme.Gray200
import com.schoolkeepa.dust.ui.theme.Gray500
import com.schoolkeepa.dust.ui.theme.Gray900
import com.schoolkeepa.dust.ui.theme.Green100
import com.schoolkeepa.dust.ui.theme.Green300
import com.schoolkeepa.dust.ui.theme.Green400
import com.schoolkeepa.dust.util.CustomToast
import java.io.IOException
import java.io.InputStream

fun search(text: String, searchQuery: String): Boolean {
    return text.contains(searchQuery)
}

fun searchInPdf(
    userType: String,
    context: Context,
    searchText: String,
    setShowToast: (Boolean) -> Unit
) {
    val inputStream: InputStream?
    var text = ""
    var id = ""
    when (userType) {
        "teacher" -> {
            inputStream =
                context.resources.openRawResource(
                    R.raw.teacher_manual
                )
            text = "교직원 매뉴얼"
            id = "teacher_manual.pdf"
        }

        "middle", "high" -> {
            inputStream =
                context.resources.openRawResource(
                    R.raw.middle_high_manual
                )
            text = "중고등학생 매뉴얼"
            id = "middle_high_manual.pdf"
        }

        "elementary" -> {
            inputStream =
                context.resources.openRawResource(
                    R.raw.elementary_text
                )
            text = "초등학생 매뉴얼"
            id = "elementary_manual.pdf"
        }

        else -> {
            inputStream =
                context.resources.openRawResource(
                    R.raw.elementary_text
                )
        }
    }
    try {
        val b = ByteArray(inputStream.available())
        inputStream.read(b)
        val pdfText = String(b)
//                                                        Log.e("test", pdfText)
        if (search(pdfText, searchText)) {
            val intent = Intent(
                context,
                ManualActivity::class.java
            )
            intent.putExtra("title", text)
            intent.putExtra("pdf", id)
            intent.putExtra(
                "search_query",
                searchText
            )
            context.startActivity(intent)
        } else {
            setShowToast(true)
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManualScreen(
    onNavigateToRoute: (String) -> Unit,
    upPress: () -> Unit,
    userType: String
) {

    val context = LocalContext.current

    var searchState by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    var topicList by remember { mutableStateOf(listOf<String>()) }
    var manualList by remember { mutableStateOf(listOf<List<String>>()) }
    var pdfType by remember { mutableStateOf("") }
    var isShowToast by remember { mutableStateOf(false) }

    if (isShowToast) {
        val customToast = CustomToast(context)
        val msg = if (searchText.length < 8) {
            searchText
        } else {
            searchText.substring(0, 8) + "..."
        }
        customToast.MakeText(message = "'$msg' 검색 자료가 없습니다.")
        isShowToast = false
    }

    when (userType) {
        "elementary" -> {
            topicList = listOf(
                "미세먼지란?",
                "학교 미세먼지 문제 이해하기",
                "학교 미세먼지 관리방법",
            )

            manualList = listOf(
                listOf(
                    "미세먼지가 뭐지?",
                    "미세먼지는 왜 생기는 걸까?",
                    "우리나라 미세먼지 농도는 어느 정도일까?",
                    "미세먼지 때문에 건강이 나빠질 수 있다던데?"
                ), listOf(
                    "학생들이 더 위험할 수 있는 이유는 뭘까?",
                    "학교 근처에서 미세먼지가 생기는 이유는 뭘까?",
                ), listOf(
                    "미세먼지 예방을 위해 이렇게 행동해요",
                    "교실 안에서 미세먼지 줄이는 방법은 뭐가 있을까?"
                )
            )
            pdfType = "e"
        }

        "middle", "high" -> {
            topicList = listOf(
                "미세먼지란?",
                "학교 미세먼지 문제 이해하기",
                "학교 미세먼지 관리방법",
                "관련기관 비상연락망"
            )

            manualList = listOf(
                listOf(
                    "미세먼지가 뭐지?",
                    "미세먼지는 어떻게 구성되어 있을까?",
                    "미세먼지는 왜 생기는 걸까?",
                    "우리나라와 외국의 미세먼지 환경기준은?",
                    "국내·외 실내공기질 관리 기준",
                    "우리나라 미세먼지 농도는 어느 정도일까?",
                    "미세먼지 때문에 건강이 나빠질 수 있다던데?"
                ),
                listOf(
                    "학교 공간의 특수성",
                    "학생의 미세먼지 노출 특성",
                    "학교 내·외부 미세먼지 발생원",
                    "학교 미세먼지 농도 현황",
                    "학교 입지에 따른 미세먼지 특성"
                ),
                listOf(
                    "미세먼지에 의한 건강영향 예방을 위한 평상시 건강수칙",
                    "미세먼지 영향을 줄이기 위한 실내 미세먼지 관리 방법",
                    "학생의 학교 미세먼지 대응 방법"
                ),
                listOf(
                    "연락망 안내", "중앙행정기관", "시·도교육청", "지방자치단체", "지역 보건소"
                )
            )
            pdfType = "m"
        }

        "teacher" -> {
            topicList = listOf(
                "미세먼지란?",
                "학교 미세먼지 문제 이해하기",
                "학교 미세먼지 점검표 및 작성 요령",
                "학교 미세먼지 관리 및 대응 전략",
                "관련기관 비상연락망"
            )

            manualList = listOf(
                listOf(
                    "미세먼지의 정의 및 개념",
                    "미세먼지의 구성 성분",
                    "미세먼지의 발생원",
                    "국내·외 대기환경기준",
                    "국내·외 실내공기질 관리 기준",
                    "우리나라 미세먼지 농도 현황",
                    "미세먼지로 인한 건강영향"
                ),
                listOf(
                    "학교 공간의 특수성",
                    "학생의 미세먼지 노출 특성",
                    "학교 내·외부 미세먼지 발생원",
                    "학교 미세먼지 농도 현황",
                    "학교 입지에 따른 미세먼지 특성"
                ),
                listOf(
                    "학생용 학교 미세먼지 점검표와 작성 요령", "교직원용 학교 미세먼지 점검표와 작성 요령", "학교 공간유형별 미세먼지 진단 체크리스트"
                ),
                listOf(
                    "학교 미세먼지 관리 절차 및 대응 전략",
                    "학교 미세먼지 관리를 위한 정보 확인",
                    "미세먼지 노출 예방을 위한 평상시 건강수칙",
                    "실내 미세먼지 관리 방법",
                    "학교 구성원별 학교 미세먼지 대응 방법",
                    "학교 미세먼지 관리 시 고려사항"
                ),
                listOf(
                    "연락망 안내", "중앙행정기관", "시·도교육청", "지방자치단체", "지역 보건소"
                )
            )
            pdfType = "t"
        }
    }
    println(userType)

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(searchState) {
        if (searchState) {
            focusRequester.requestFocus()
        }
    }

    Scaffold(
        topBar = {
            if (searchState) {
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
                                    .padding(start = 16.dp)
                                    .focusRequester(focusRequester),
                                value = searchText,
                                textStyle = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = Gray900
                                ),
                                keyboardActions = KeyboardActions(onSearch = {
                                    searchInPdf(
                                        context = context,
                                        userType = userType,
                                        searchText = searchText
                                    ) {
                                        isShowToast = it
                                    }
                                }
                                ),
                                maxLines = 1,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Search
                                ),
                                singleLine = true,
                                onValueChange = { searchText = it },
                                decorationBox = { innerTextField ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxSize(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {

                                        Icon(
                                            modifier = Modifier.padding(end = 5.dp),
                                            painter = painterResource(id = R.drawable.ic_search),
                                            contentDescription = ""
                                        )
                                        Box(modifier = Modifier.weight(1f)) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .padding(end = 3.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                innerTextField()
                                            }
                                            if (searchText.isEmpty()) {
                                                Text(
                                                    modifier = Modifier
                                                        .align(Alignment.CenterStart),
                                                    text = "미세먼지에 대해 검색해 보세요.",
                                                    fontSize = 18.sp,
                                                    color = Gray500
                                                )
                                            }
                                        }

                                        if (searchText.isNotEmpty()) {
                                            TextButton(
                                                modifier = Modifier
                                                    .wrapContentWidth()
                                                    .fillMaxHeight()
                                                    .padding(top = 3.dp, bottom = 3.dp, end = 3.dp),
                                                colors = ButtonDefaults.buttonColors(Green300),
                                                shape = RoundedCornerShape(25),
                                                onClick = {
                                                    searchInPdf(
                                                        context = context,
                                                        userType = userType,
                                                        searchText = searchText
                                                    ) {
                                                        isShowToast = it
                                                    }
                                                }
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxHeight()
                                                ) {
                                                    Text(
                                                        modifier = Modifier
                                                            .align(Alignment.Center),
                                                        text = "검색",
                                                        color = Color.White
                                                    )
                                                }
                                            }
                                        }
                                    }
                                },
                            )
                        }
                        TextButton(
                            modifier = Modifier.align(Alignment.Bottom),
                            onClick = {
                                searchText = ""
                                searchState = false
                            }) {
                            Text(text = "취소")
                        }
                    }
                })
            } else {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = Screen.Manual.title,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { upPress() }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow_left),
                                contentDescription = "arrow left icon"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            searchState = true
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_search),
                                contentDescription = "arrow left icon"
                            )
                        }
                    }
                )
            }
        }
    ) {
        ManualSection(
            modifier = Modifier.padding(
                top = it.calculateTopPadding(),
                start = 24.dp,
                end = 24.dp
            ),
            onNavigateToRoute = onNavigateToRoute,
            topics = topicList,
            manuals = manualList,
            pdfType = pdfType
        )
    }
}

@Composable
fun ManualSection(
    modifier: Modifier,
    onNavigateToRoute: (String) -> Unit,
    topics: List<String>,
    manuals: List<List<String>>,
    pdfType: String,
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .verticalScroll(scrollState)
    ) {
        topics.forEachIndexed { index, topic ->
            TopicComponent(
                topicNumber = (index + 1).toString(), topic = topic
            )
            manuals[index].forEachIndexed { idx, manual ->
                ManualComponent(
                    text = manual,
                    "$pdfType${index + 1}_${idx + 1}.pdf",
                    onNavigateToRoute = { onNavigateToRoute(it) }
                )
            }
        }
    }
}


@Composable
fun TopicComponent(
    topicNumber: String,
    topic: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 25.dp, bottom = 10.dp)
    ) {
        Text(
            modifier = Modifier
                .background(
                    shape = RoundedCornerShape(6.dp),
                    color = Green100
                )
                .padding(3.dp),
            color = Green400,
            text = "Chapter $topicNumber"
        )
        Text(
            modifier = Modifier.padding(start = 10.dp),
            text = topic,
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
fun ManualComponent(
    text: String,
    id: String,
    onNavigateToRoute: (String) -> Unit,
) {
    val context = LocalContext.current
//    print(id + "@@@@@@@@@@@@")
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(
                    shape = RoundedCornerShape(14.dp),
                    color = Gray200
                )
                .clip(
                    RoundedCornerShape(14.dp)
                )
                .clickable {
                    val intent = Intent(context, ManualActivity::class.java)
                    intent.putExtra("title", text)
                    intent.putExtra("pdf", id)
                    intent.putExtra("search_query", "")
                    context.startActivity(intent)
//                    println(id + "@@@@@@@@@@@@")
//                    onNavigateToRoute(Screen.ManualDetail.route + "/${getAsset(id)}/${text}")
                }
                .padding(horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                modifier = Modifier.fillMaxWidth(0.9f)
            )
            Spacer(
                modifier = Modifier
                    .weight(1f)
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_right_arrow),
                contentDescription = ""
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Preview
@Composable
fun asfd() {
    ManualScreen({}, {}, "UserType.ELEMENTARY")
}