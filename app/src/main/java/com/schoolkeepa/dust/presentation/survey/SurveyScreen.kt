package com.schoolkeepa.dust.presentation.survey

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality.Companion.High
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.schoolkeepa.dust.DustApplication
import com.schoolkeepa.dust.R
import com.schoolkeepa.dust.Resource
import com.schoolkeepa.dust.data.model.surveydata.Data
import com.schoolkeepa.dust.data.model.surveydata.SubQuestion
import com.schoolkeepa.dust.data.model.surveydata.SurveyDataDto
import com.schoolkeepa.dust.data.response.set_survey.Answer
import com.schoolkeepa.dust.data.response.set_survey.SurveyData
import com.schoolkeepa.dust.data.response.set_survey.SurveySetRes
import com.schoolkeepa.dust.data.response.set_survey.User
import com.schoolkeepa.dust.presentation.component.DustCheckbox
import com.schoolkeepa.dust.presentation.component.DustCheckboxComponent
import com.schoolkeepa.dust.presentation.component.DustRadioGroup
import com.schoolkeepa.dust.presentation.navigation.Screen
import com.schoolkeepa.dust.presentation.setting.CustomDialog
import com.schoolkeepa.dust.presentation.setting.InputTextField
import com.schoolkeepa.dust.ui.theme.Blue300
import com.schoolkeepa.dust.ui.theme.Gray100
import com.schoolkeepa.dust.ui.theme.Gray200
import com.schoolkeepa.dust.ui.theme.Gray300
import com.schoolkeepa.dust.ui.theme.Gray400
import com.schoolkeepa.dust.ui.theme.Gray500
import com.schoolkeepa.dust.ui.theme.Gray600
import com.schoolkeepa.dust.ui.theme.Gray700
import com.schoolkeepa.dust.ui.theme.Gray900
import com.schoolkeepa.dust.ui.theme.Green000
import com.schoolkeepa.dust.ui.theme.Green300
import com.schoolkeepa.dust.ui.theme.Orange000
import com.schoolkeepa.dust.ui.theme.Orange300
import com.schoolkeepa.dust.ui.theme.Red000
import com.schoolkeepa.dust.ui.theme.Red200
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SurveyScreen(
    viewModel: SurveyViewModel = hiltViewModel(),
    date: String,
    userData: String,
    userType: String,
    upPress: () -> Unit
) {
    val application = DustApplication
    var stored by remember { mutableStateOf("") }

    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.KOREAN)
    val formattedDate = sdf.format(Date())

    val storeData = application.preferences.getString(date, "")

    var startPage by remember { mutableStateOf(0) }

    if (storeData.isNotEmpty()) {
        val a = storeData.split("/")
        if (a[0] != a[1]) {
            stored = storeData
            startPage = a[0].toInt()
        }
        println("stored" + stored)
    } else {
        println("stored" + stored)
    }
    var checkList = List(10) { false }.toMutableList()

    println("SURVEY$date$userData")
    val surveyData = produceState<Resource<SurveyDataDto>>(initialValue = Resource.Loading()) {
        value = viewModel.getSurvey(userType)
    }.value

    val userDataList = userData.split("/")

    println("@@@@@@ userDataList" + userDataList)

    var checked by remember { mutableStateOf(false) }
    val surveyList = viewModel.surveyData.value!!.data
    val pagerCount = surveyList.size

    var currentPage by remember { mutableStateOf(0) }
    if (surveyData is Resource.Success) {
        viewModel.setSurveyData(surveyData.data!!)
//        println("surveyData.toString()@@@@@" + surveyData.data.toString())
    } else if (surveyData is Resource.Error) {
        println(surveyData.message)
    }

    val pagerState = rememberPagerState(
        initialPage = startPage,
        initialPageOffsetFraction = 0f
    ) { pagerCount }
    val scope = rememberCoroutineScope()
    var help by remember { mutableStateOf("") }
    var questionAnswerList = mutableListOf<Answer>()

    var pictureView by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Box(modifier = Modifier.fillMaxWidth()) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = Screen.Survey.title,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_close),
                                contentDescription = "close"
                            )
                        }
                    },
                    actions = {
                        Text(
                            text = (currentPage + 1).toString(),
                            color = Blue300,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "/${pagerCount}",
                            color = Gray400,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.width(14.dp))
                    }
                )
            }
        }
    ) {

        var helpVisible by remember { mutableStateOf(false) }

        LaunchedEffect(pagerState.currentPage) {
            currentPage = pagerState.currentPage
        }

        var selected by remember { mutableStateOf(false) }

        var surData by remember {
            mutableStateOf(
                SurveyData(
                    question_id = 0,
                    answers = listOf(),
                    date = ""
                )
            )
        }

        var showDialog by remember { mutableStateOf(false) }
        if (showDialog) {
            CustomDialog(
                title = "",
                message = "설문조사를 완료하면 수정이 어렵습니다.\n설문 조사를 완료할까요?", cancel = "다시 확인", confirm = "완료!",
                setShowDialog = {
                    showDialog = false
                },
                {
                    application.preferences.setString(date, "")
                    println(pagerState.currentPage.toString() + surveyList.size)
                    scope.launch {
                        checkList = List(10) { false }.toMutableList()
                        submitSurveyData(
                            viewModel = viewModel,
                            pagerState = pagerState,
                            userDataList = userDataList,
                            surveyList = surveyList,
                            scope = scope,
                            surveyData = surData,
                            userType = userType,
                            setSelected = {
                                selected = it
                            },
                        )
                        viewModel.finishSurvey(date)
                        viewModel.saveSurvey(date)
                        upPress()
                        showDialog = false
                    }
                }
            )
        }


//        var questionAnswerList = remember { mutableStateListOf<Answer>() }
        var sliderSize by remember { mutableStateOf(0) }
        val sliderCheck = remember { mutableStateListOf(0, 0, 0, 0, 0, 0) }
        var choiceSize by remember { mutableStateOf(0) }
        val choiceCheck = remember { mutableStateListOf(0, 0, 0, 0, 0, 0) }


        var chkSlider by remember { mutableStateOf(true) }

//        var isSlider by remember { mutableStateOf(true) }
//        var isChoice by remember { mutableStateOf(true) }
//        var isOx by remember { mutableStateOf(true) }
//        var isNumberInput by remember { mutableStateOf(true) }
//        var isText by remember { mutableStateOf(true) }

        var cnt = 0

        sliderCheck.map {
            if (it == 1) {
                cnt++
            }
        }

        var choiceCnt = 0

        choiceCheck.map {
            if (it == 1) {
                choiceCnt++
            }
        }

        var oxCnt by remember { mutableStateOf(0) }


        var data by remember { mutableStateOf(Data()) }
        if (surveyList.isNotEmpty()) {
            data = surveyList[currentPage]
            println(currentPage.toString() + surveyList[currentPage])
        }

        var isSlider by remember { mutableStateOf(true) }
        var isChoice by remember { mutableStateOf(true) }
        var isOx by remember { mutableStateOf(true) }
        var isNumberInput by remember { mutableStateOf(true) }
        var isText by remember { mutableStateOf(true) }
        var isCheckbox by remember { mutableStateOf(true) }
        var checkBoxClicked by remember { mutableStateOf(false) }
        var inputState by remember { mutableStateOf("") }
        var choiceInput by remember { mutableStateOf("") }

        LaunchedEffect(currentPage) {
            inputState = ""
            isSlider = true
            isChoice = true
            isOx = true
            isNumberInput = true
            isText = true
        }

        BackHandler {
            if (pictureView) {
                pictureView = false
            } else {
                if (pagerState.currentPage == 0) {
                    upPress()
                } else {
                    scope.launch {
                        helpVisible = false
                        cnt = 0
                        choiceCnt = 0
                        oxCnt = 0
                        inputState = ""
                        sliderCheck.clear()
                        sliderCheck.addAll(listOf(0, 0, 0, 0, 0, 0))
                        choiceCheck.clear()
                        choiceCheck.addAll(listOf(0, 0, 0, 0, 0, 0))
                        println(
                            "questionAnswerList" + questionAnswerList.toList().toString()
                        )
                        questionAnswerList = mutableListOf()
                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .padding(top = it.calculateTopPadding(), bottom = it.calculateBottomPadding())
                .fillMaxSize()
                .imePadding()
        ) {
            HorizontalPager(
                modifier = Modifier
                    .weight(1f),
                state = pagerState,
                userScrollEnabled = false
            ) { index ->
                help = data.help
//                println("questionAnswerList" + questionAnswerList.toList().toString())
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        modifier = Modifier.padding(top = 18.dp),
                        fontSize = 16.sp,
                        color = Color(android.graphics.Color.parseColor(data.category_color)),
                        text = data.category_name
                    )
                    Text(
                        modifier = Modifier.padding(top = 15.dp),
                        fontSize = 22.sp,
                        lineHeight = 28.sp,
                        fontWeight = FontWeight.Bold,
                        text = data.question
                    )

                    val scrollState = rememberScrollState()


                    scope.launch {
                        delay(250)
                        selected =
                            isSlider && isChoice && isOx && isNumberInput && isText && isCheckbox
                        println("@@@@@@ selected check " + isSlider.toString() + isChoice.toString() + isOx.toString() + isNumberInput.toString() + isText.toString() + isCheckbox.toString())
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .weight(1f)
                            .verticalScroll(scrollState)
                    ) {
                        Spacer(modifier = Modifier.height(10.dp))
//                        println("data.sub_questions" + data.sub_questions)
                        println("@@@@@@ selected check " + isSlider.toString() + isChoice.toString() + isOx.toString() + isNumberInput.toString() + isText.toString())
                        Column(modifier = Modifier) {

                            if (data.sub_text.isNotEmpty()) {
                                Text(
                                    text = data.sub_text,
                                    color = Gray600,
                                    modifier = Modifier.padding(vertical = 10.dp)
                                )
                            }

                            data.sub_questions.forEachIndexed { index, subQuestion ->
                                when (subQuestion.type) {
                                    "slider" -> {
                                        chkSlider = true
                                        val qIdx = data.sub_questions[index].sub_question_id
                                        if (qIdx == 1) {
                                            SlideSurvey(
                                                subQuestion = subQuestion,
                                                setSelected = { num ->
                                                    sliderCheck[0] = 1
                                                    println(
                                                        "@@@@@@@ sliderCheck" + sliderCheck.toList()
                                                            .toString()
                                                    )
                                                },
                                                setData = { ans ->
                                                    var exist = false
                                                    for (i in questionAnswerList) {
                                                        if (i.sub_question_id == ans.sub_question_id) {
                                                            println("@@@@@ sub_question_id" + i.sub_question_id + ans.sub_question_id + questionAnswerList)
                                                            exist = true
                                                            questionAnswerList.remove(i)
                                                            questionAnswerList.add(ans)
                                                            break
                                                        }
                                                    }

                                                    if (!exist) {
                                                        questionAnswerList.add(ans)
                                                    }
                                                    println("@@@@@@@ questionAnswerList $questionAnswerList")
                                                    surData = SurveyData(
                                                        date = formattedDate,
                                                        answers = questionAnswerList.toList(),
                                                        question_id = data.id
                                                    )
                                                }
                                            )
                                        } else if (sliderCheck[data.sub_questions[index].sub_question_id - 2] == 1) {
                                            SlideSurvey(
                                                subQuestion = subQuestion,
                                                setSelected = { num ->
                                                    sliderCheck[qIdx - 1] = 1
                                                },
                                                setData = { ans ->
                                                    var exist = false
                                                    for (i in questionAnswerList) {
                                                        println("@@@@@ questionAnswer" + i.toString())
                                                        if (i.sub_question_id == ans.sub_question_id) {
                                                            println("@@@@@ sub_question_id" + i.sub_question_id + ans.sub_question_id + questionAnswerList)
                                                            exist = true
                                                            questionAnswerList.remove(i)
                                                            questionAnswerList.add(ans)
                                                            break
                                                        }
                                                    }

                                                    println("@@@@@@@ before questionAnswerList $questionAnswerList")
                                                    if (!exist) {
                                                        questionAnswerList.add(ans)
                                                    }
                                                    println("@@@@@@@ after questionAnswerList $questionAnswerList")
                                                    surData = SurveyData(
                                                        date = formattedDate,
                                                        answers = questionAnswerList.toList(),
                                                        question_id = data.id
                                                    )
                                                }
                                            )
                                        }
                                        isSlider = cnt == data.sub_questions.size
                                    }

                                    "choice" -> {
//                                        println("THISISCHOICE${sliderCheck.toList()}${choiceCheck.toList()}")
                                        println("sub_questions" + data.sub_questions)
                                        var choiceCount = 0

                                        data.sub_questions.forEach {
                                            if (it.type == "choice") {
                                                choiceCount++
                                            }
                                        }

                                        val qIdx = data.sub_questions[index].sub_question_id
                                        if (qIdx == 1) {
                                            ChoiceSurvey(
                                                subQuestion = subQuestion,
                                                setSelected = { num ->
                                                    choiceCheck[0] = 1
                                                    println(
                                                        "@@@@@@@ sliderCheck" + choiceCheck.toList()
                                                            .toString()
                                                    )
                                                },
                                                setData = { answer ->
                                                    var exist = false
                                                    for (ans in questionAnswerList) {
                                                        if (answer.sub_question_id == ans.sub_question_id) {
                                                            exist = true
                                                            questionAnswerList.remove(ans)
                                                            questionAnswerList.add(answer)
                                                            break
                                                        }
                                                    }
                                                    if (!exist) {
                                                        questionAnswerList.add(answer)
                                                    }
                                                    surData = SurveyData(
                                                        date = formattedDate,
                                                        answers = questionAnswerList.toList(),
                                                        question_id = data.id
                                                    )
                                                }
                                            )
                                        } else if (choiceCheck[data.sub_questions[index].sub_question_id - 2] == 1) {
                                            ChoiceSurvey(
                                                subQuestion = subQuestion,
                                                setSelected = { num ->
                                                    choiceCheck[qIdx - 1] = 1
                                                },
                                                setData = { answer ->
                                                    var exist = false
                                                    for (ans in questionAnswerList) {
                                                        if (answer.sub_question_id == ans.sub_question_id) {
                                                            exist = true
                                                            questionAnswerList.remove(ans)
                                                            questionAnswerList.add(answer)
                                                            break
                                                        }
                                                    }
                                                    if (!exist) {
                                                        questionAnswerList.add(answer)
                                                    }
                                                    println("@@@@@@@ questionAnswerList $answer")
                                                    surData = SurveyData(
                                                        date = formattedDate,
                                                        answers = questionAnswerList.toList(),
                                                        question_id = data.id
                                                    )
                                                }
                                            )
                                        }
                                        isChoice = choiceCnt == choiceCount
                                    }

                                    "number_picker" -> {
                                        isNumberInput = inputState.isNotEmpty()
                                        NumberSurvey(
                                            subQuestion = subQuestion,
                                            setInput = { input ->
                                                println("@@@@@ input" + input)
                                                inputState = input
                                            },
                                            setData = {
                                                println("@@@@@@@number input" + it.toString())
                                                var exist = false
                                                for (i in questionAnswerList) {
                                                    if (it.sub_question_id == i.sub_question_id) {
                                                        exist = true
                                                        questionAnswerList.remove(i)
                                                        questionAnswerList.add(it)
                                                        break
                                                    }
                                                }
                                                if (!exist) {
                                                    questionAnswerList.add(it)
                                                }
                                                println("@@@@@@@number questionAnswerList" + questionAnswerList.toString())
                                                surData = SurveyData(
                                                    date = formattedDate,
                                                    answers = questionAnswerList.toList(),
                                                    question_id = data.id
                                                )
                                            },
                                            onNext = {
//                                                questionCount = 0
                                                if (selected && pagerState.currentPage == surveyList.size - 1) {
                                                    showDialog = true
                                                } else if (selected) {
                                                    checkList = List(10) { false }.toMutableList()

                                                    submitSurveyData(
                                                        viewModel = viewModel,
                                                        pagerState = pagerState,
                                                        userDataList = userDataList,
                                                        surveyList = surveyList,
                                                        scope = scope,
                                                        surveyData = surData,
                                                        userType = userType,
                                                        setSelected = {
                                                            selected = it
                                                        },
                                                    )
                                                }
                                            }
                                        )
                                    }

                                    "checkbox" -> {
                                        CheckboxSurvey(
                                            subQuestion = subQuestion,
                                            setSelected = {
                                                println("@@ isCheckbox" + it.toString())
                                                isCheckbox = it
                                                selected =
                                                    isSlider && isChoice && isOx && isNumberInput && isText && isCheckbox
                                            },
                                            setData = { answer ->
                                                println("@@@@@@ answer" + answer)
                                                var exist = false
                                                for (ans in questionAnswerList) {
                                                    if (answer.sub_question_id == ans.sub_question_id) {
                                                        exist = true
                                                        questionAnswerList.remove(ans)
                                                        questionAnswerList.add(answer)
                                                        break
                                                    }
                                                }
                                                if (!exist) {
                                                    questionAnswerList.add(answer)
                                                }
                                                checkBoxClicked = !checkBoxClicked
                                                println("@@@@@@@ checkbox questionAnswerList" + questionAnswerList.toString())
                                                surData = SurveyData(
                                                    date = formattedDate,
                                                    answers = questionAnswerList.toList(),
                                                    question_id = data.id
                                                )
                                            },
                                            checkList = checkList
                                        )
                                    }

                                    "ox" -> {
                                        OxSurvey(
                                            subQuestion = subQuestion,
                                            setSelected = { num ->
                                                oxCnt++
                                            },
                                            setData = { answer ->
                                                var exist = false
                                                for (ans in questionAnswerList) {
                                                    if (answer.sub_question_id == ans.sub_question_id) {
                                                        exist = true
                                                        questionAnswerList.remove(ans)
                                                        questionAnswerList.add(answer)
                                                        break
                                                    }
                                                }
                                                if (!exist) {
                                                    questionAnswerList.add(answer)
                                                }
                                                surData = SurveyData(
                                                    date = formattedDate,
                                                    answers = questionAnswerList.toList(),
                                                    question_id = data.id
                                                )
                                            }
                                        )
                                        isOx = oxCnt > 0

                                    }

                                    "text" -> {
                                        isText = inputState.isNotEmpty()
                                        TextSurvey(
                                            subQuestion = subQuestion,
                                            setInput = { input ->
                                                inputState = input
                                            },
                                            setData = { answer ->
                                                var exist = false
                                                for (ans in questionAnswerList) {
                                                    if (answer.sub_question_id == ans.sub_question_id) {
                                                        exist = true
                                                        questionAnswerList.remove(ans)
                                                        questionAnswerList.add(answer)
                                                        break
                                                    }
                                                }
                                                if (!exist) {
                                                    questionAnswerList.add(answer)
                                                }
                                                surData = SurveyData(
                                                    date = formattedDate,
                                                    answers = questionAnswerList.toList(),
                                                    question_id = data.id
                                                )
                                            }
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(30.dp))
                        Text(
                            modifier = Modifier
                                .clip(
                                    shape = RoundedCornerShape(50),
                                )
                                .clickable {
                                    helpVisible = !helpVisible
                                }
                                .background(
                                    shape = RoundedCornerShape(50),
                                    color = if (helpVisible) Orange000 else Gray300
                                )
                                .padding(top = 10.dp, bottom = 10.dp, start = 10.dp, end = 14.dp),
                            color = if (helpVisible) Orange300 else Gray700,
                            fontWeight = FontWeight.Bold,
                            text = "💡도움말"
                        )
                        if (helpVisible) {
                            Spacer(modifier = Modifier.height(8.dp))
                            AsyncImage(
                                contentScale = ContentScale.FillWidth,
                                filterQuality = High,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        pictureView = true
                                    },
                                model = ImageRequest.Builder(LocalContext.current).data(data.help)
                                    .build(),
                                contentDescription = "help"
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 10.dp)
            ) {
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                        .padding(end = 4.dp),
                    shape = RoundedCornerShape(25),
                    colors = ButtonDefaults.buttonColors(Gray300),
                    onClick = {
                        scope.launch {
                            if (pagerState.currentPage == 0) {
                                upPress()
                            } else {
                                cnt = 0
                                choiceCnt = 0
                                oxCnt = 0
                                checkList = List(10) { false }.toMutableList()
                                inputState = ""
                                helpVisible = false
                                sliderCheck.clear()
                                sliderCheck.addAll(listOf(0, 0, 0, 0, 0, 0))
                                choiceCheck.clear()
                                choiceCheck.addAll(listOf(0, 0, 0, 0, 0, 0))
                                println(
                                    "questionAnswerList" + questionAnswerList.toList().toString()
                                )
                                questionAnswerList = mutableListOf()
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        }
                    }
                ) {
                    Text(
                        text = "이전",
                        color = Gray700,
                        fontSize = 16.sp
                    )
                }
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                        .padding(start = 4.dp),
                    shape = RoundedCornerShape(25),
                    colors = if (selected) {
                        ButtonDefaults.buttonColors(Green300)
                    } else {
                        ButtonDefaults.buttonColors(Gray300)
                    },
                    onClick = {
                        if (selected && pagerState.currentPage == surveyList.size - 1) {
                            showDialog = true
                        } else if (selected) {
                            cnt = 0
                            inputState = ""
                            choiceCnt = 0
                            oxCnt = 0
                            helpVisible = false
                            sliderCheck.clear()
                            sliderCheck.addAll(listOf(0, 0, 0, 0, 0, 0))
                            choiceCheck.clear()
                            choiceCheck.addAll(listOf(0, 0, 0, 0, 0, 0))
                            application.preferences.setString(
                                date,
                                "${pagerState.currentPage + 1}/${surveyList.size}"
                            )
                            questionAnswerList = mutableListOf()
//                            println("questionAnswerList" + questionAnswerList.toList().toString())
                            checkList = List(10) { false }.toMutableList()
                            submitSurveyData(
                                viewModel = viewModel,
                                pagerState = pagerState,
                                userDataList = userDataList,
                                surveyList = surveyList,
                                scope = scope,
                                surveyData = surData,
                                userType = userType,
                                setSelected = {
                                    selected = it
                                },
                            )
                        }
                    }
                ) {
                    Text(
                        text =
                        if (pagerState.currentPage == surveyList.size - 1) {
                            "설문조사 완료"
                        } else {
                            "다음"
                        },
                        fontSize = 16.sp,
                        color = if (selected) {
                            Color.White
                        } else {
                            Gray400
                        },
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
    if (pictureView) {
        if (help.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                var scale by remember { mutableStateOf(1f) }
                var offset by remember { mutableStateOf(Offset(0f, 0f)) }
                Box(
                    modifier = Modifier
                        .background(Color.Black.copy(0.8f))
                        .fillMaxSize()
                        .clickable {
                            pictureView = false
                        }
                )
                AsyncImage(
                    contentScale = ContentScale.FillWidth,
                    filterQuality = High,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .pointerInput(Unit) {
                            detectTransformGestures { _, pan, zoom, _ ->
                                // Update the scale based on zoom gestures.
                                scale *= zoom

                                // Limit the zoom levels within a certain range (optional).
                                scale = scale.coerceIn(1f, 3f)

                                // Update the offset to implement panning when zoomed.
                                offset = if (scale == 1f) Offset(0f, 0f) else offset + pan
                            }
                        }
                        .graphicsLayer(
                            scaleX = scale, scaleY = scale,
                            translationX = offset.x, translationY = offset.y
                        ),
                    model = ImageRequest.Builder(LocalContext.current).data(help)
                        .build(),
                    contentDescription = "help"
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun submitSurveyData(
    viewModel: SurveyViewModel,
    pagerState: PagerState,
    userDataList: List<String>,
    surveyList: List<Data>,
    scope: CoroutineScope,
    surveyData: SurveyData,
    userType: String,
    setSelected: (Boolean) -> Unit,
) {
    scope.launch {
        val a = if (userDataList[5].isEmpty()) {
            0
        } else {
            userDataList[5].toInt()
        }

        println("surveyData" + surveyData.toString())

        println(
            "POST_SURVEY" +
                    viewModel.postSurvey(
                        SurveySetRes(
                            survey_data = surveyData,
                            user = User(
                                school_code = userDataList[6].toInt(),
                                grade = userDataList[3].toInt(),
                                class_num = userDataList[4].toInt(),
                                student_num = a,
                                name = userDataList[1],
                                user_type = userType
                            )
                        )
                    ).toString()
        )
        println("POST_SURVEY!")
        setSelected(false)
        if (pagerState.currentPage == surveyList.size - 1) {
//            println(pagerState.currentPage.toString() + surveyList.size)
//            viewModel.finishSurvey(date)
//            viewModel.saveSurvey(date)
//            upPress()
        } else {
            pagerState.animateScrollToPage(pagerState.currentPage + 1)
        }
    }
}

@Composable
fun SlideSurvey(
    subQuestion: SubQuestion,
    setSelected: (Int) -> Unit,
    setData: (Answer) -> Unit,
) {
    var choiceState by remember {
        mutableStateOf(-1)
    }

//    println("subQuestion" + subQuestion)
    LaunchedEffect(choiceState) {
        if (choiceState != -1) {
            setSelected(1)
            println(subQuestion.options[choiceState].text + subQuestion.toString())
            println(
                Answer(
                    sub_question_answer = subQuestion.options[choiceState].text,
                    sub_question_id = subQuestion.sub_question_id,
                    type = subQuestion.type,
                    sub_question_input = ""
                ).toString() + "@@@@@@"
            )
            setData(
                Answer(
                    sub_question_answer = subQuestion.options[choiceState].text,
                    sub_question_id = subQuestion.sub_question_id,
                    type = subQuestion.type,
                    sub_question_input = ""
                )
            )
        }
    }

    Column {
        subQuestion.text?.let { Text(text = " $it") }
        Spacer(modifier = Modifier.height(20.dp))
        Column() {
            DustRadioGroup(
                dataList = subQuestion.options,
                choiceState,
                setData = { choiceState = it },
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun OxSurvey(
    subQuestion: SubQuestion,
    setSelected: (Int) -> Unit,
    setData: (Answer) -> Unit
) {
    var choiceState by remember { mutableStateOf(-1) }

    LaunchedEffect(choiceState) {
        if (choiceState != -1) {
            setSelected(1)
            setData(
                Answer(
                    sub_question_id = subQuestion.sub_question_id,
                    sub_question_answer = subQuestion.options[choiceState - 1].text,
                    type = "ox",
                    sub_question_input = ""
                )
            )
        }
    }

    Row {
        OutlinedButton(
            modifier = Modifier
                .weight(1f)
                .height(132.dp),
            onClick = { choiceState = 1 },
            shape = RoundedCornerShape(
                16.dp
            ),
            colors = ButtonDefaults.buttonColors(
                if (choiceState == 1) {
                    Green000
                } else {
                    Gray200
                }
            ),
            border = BorderStroke(
                if (choiceState == 1) {
                    2.dp
                } else {
                    (-1).dp
                },
                color = Green300
            )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(painter = painterResource(id = R.drawable.image_o), contentDescription = "")
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = subQuestion.options[0].text,
                    color = Green300,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
        OutlinedButton(
            modifier = Modifier
                .weight(1f)
                .height(132.dp),
            onClick = { choiceState = 2 },
            colors = ButtonDefaults.buttonColors(
                if (choiceState == 2) {
                    Red000
                } else {
                    Gray200
                }
            ),
            shape = RoundedCornerShape(
                16.dp
            ),
            border = BorderStroke(
                if (choiceState == 2) {
                    2.dp
                } else {
                    (-1).dp
                },
                color = Red200
            )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(painter = painterResource(id = R.drawable.image_x), contentDescription = "")
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = subQuestion.options[1].text,
                    color = Red200,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ChoiceSurvey(
    subQuestion: SubQuestion,
    setSelected: (Int) -> Unit,
    setData: (Answer) -> Unit,
) {
    var clicked by remember { mutableStateOf(false) }

    var textData by remember { mutableStateOf("") }

    var inputComponent by remember { mutableStateOf(false) }

    var choiceState by remember {
        mutableStateOf(-1)
    }

    LaunchedEffect(textData) {
        if (choiceState >= 0) {
            setData(
                Answer(
                    sub_question_answer = subQuestion.options[choiceState].text,
                    sub_question_id = subQuestion.sub_question_id,
                    type = subQuestion.type,
                    sub_question_input = textData
                )
            )
        }
    }

    LaunchedEffect(choiceState) {
        if (choiceState != -1) {
            setSelected(1)
            println(subQuestion.options[choiceState].text + subQuestion.toString())
            println(
                Answer(
                    sub_question_answer = subQuestion.options[choiceState].text,
                    sub_question_id = subQuestion.sub_question_id,
                    type = subQuestion.type,
                    sub_question_input = ""

                ).toString() + "@@@@@@"
            )

            inputComponent = subQuestion.options[choiceState].input

            setData(
                Answer(
                    sub_question_answer = subQuestion.options[choiceState].text,
                    sub_question_id = subQuestion.sub_question_id,
                    type = subQuestion.type,
                    sub_question_input = textData
                )
            )
        }
    }

    Column {
        Spacer(modifier = Modifier.height(20.dp))
        subQuestion.text?.let {
            Text(
                text = it,
                color = Gray700,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        if (subQuestion.options.size == 2) {
            Row() {
                subQuestion.options.forEachIndexed { index, optionX ->
                    OutlinedButton(
                        modifier = Modifier
                            .weight(1f)
                            .height(61.dp)
                            .padding(bottom = 8.dp),
                        onClick = { choiceState = index },
                        colors = ButtonDefaults.buttonColors(
                            if (choiceState == index) {
                                Green000
                            } else {
                                Gray100
                            }
                        ),
                        shape = RoundedCornerShape(
                            16.dp
                        ),
                        border = BorderStroke(
                            if (choiceState == index) {
                                2.dp
                            } else {
                                (-1).dp
                            },
                            color = Green300
                        )
                    ) {
                        Text(
                            text = optionX.text,
                            color = if (choiceState == index) {
                                Green300
                            } else {
                                Gray900
                            },
                            fontWeight = if (choiceState == index) {
                                FontWeight.Bold
                            } else {
                                FontWeight.Normal
                            }
                        )
                    }
                    if (index == 0) {
                        Spacer(modifier = Modifier.width(10.dp))
                    }
                }
            }
        } else {
            Column() {
                subQuestion.options.forEachIndexed { index, data ->
                    Column() {
                        OutlinedButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(61.dp)
                                .padding(bottom = 8.dp),
                            onClick = { choiceState = index },
                            colors = ButtonDefaults.buttonColors(
                                if (choiceState == index) {
                                    Green000
                                } else {
                                    Gray100
                                }
                            ),
                            shape = RoundedCornerShape(
                                16.dp
                            ),
                            border = BorderStroke(
                                if (choiceState == index) {
                                    2.dp
                                } else {
                                    (-1).dp
                                },
                                color = Green300
                            )
                        ) {
                            Text(
                                text = data.text,
                                color = if (choiceState == index) {
                                    Green300
                                } else {
                                    Gray900
                                },
                                fontWeight = if (choiceState == index) {
                                    FontWeight.Bold
                                } else {
                                    FontWeight.Normal
                                }
                            )
                        }
                    }
                    if (subQuestion.options[index].input && inputComponent) {
                        InputTextField(
                            text = textData,
                            placeholder = data.placeholder,
                            keyboardType = KeyboardType.Text,
                            onValueChange = {
                                textData = it
                            }
                        )
                    }
                }
            }

        }
    }
}

@Preview
@Composable
fun PreviewCheckboxSurvey() {
    Column(
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(61.dp)
                .background(
                    color = Gray200,
                    RoundedCornerShape(16.dp)
                )
                .clickable {

                },
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(20.dp))
            DustCheckbox(
                onCheckedChange = {

                }
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "창문",
                color = Gray700,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun CheckboxSurvey(
    subQuestion: SubQuestion,
    setSelected: (Boolean) -> Unit,
    setData: (Answer) -> Unit,
    checkList: MutableList<Boolean>
) {

    println(subQuestion.options.size.toString() + subQuestion.options)

    var clicked by remember { mutableStateOf(false) }

    var textData by remember { mutableStateOf("") }

    var inputComponent by remember { mutableStateOf(false) }
    LaunchedEffect(textData) {
        println(inputComponent.toString() + textData)
        if (inputComponent && textData.isEmpty()) {
            setSelected(false)
        } else {
            setSelected(true)
        }
    }

    LaunchedEffect(key1 = clicked) {
        val checks = mutableListOf<String>()
        for (i in 0 until subQuestion.options.size) {
            println(subQuestion.options[i])
            if (subQuestion.options[i].input) {
                inputComponent = checkList[i]
                if (inputComponent && textData.isEmpty()) {
                    setSelected(false)
                } else {
                    setSelected(true)
                }
            }
            if (checkList[i]) {
                checks.add(subQuestion.options[i].text)
            }
        }
        val str = checks.joinToString(",")
        println("str + textData" + str + textData)
        setData(
            Answer(
                sub_question_id = subQuestion.options[0].id,
                type = "checkbox",
                sub_question_answer = str,
                sub_question_input = textData
            )
        )
    }


    Column() {
        subQuestion.options.forEachIndexed { index, data ->
            Column(
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(61.dp)
                        .background(
                            color = Gray200,
                            RoundedCornerShape(16.dp)
                        )
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            clicked = !clicked
                            checkList[index] = !checkList[index]
                            println(checkList)
                        },
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(20.dp))
                    DustCheckboxComponent(
                        checkList[index]
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = data.text,
                        color = Gray700,
                        fontWeight = FontWeight.Bold
                    )
                }
                if (subQuestion.options[index].input && inputComponent) {
                    Spacer(modifier = Modifier.height(8.dp))
                    InputTextField(
                        text = textData,
                        placeholder = data.placeholder,
                        keyboardType = KeyboardType.Text,
                        onValueChange = {
                            textData = it
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TextSurvey(
    subQuestion: SubQuestion,
    setInput: (String) -> Unit,
    setData: (Answer) -> Unit,
) {
    var numberState by remember {
        mutableStateOf("")
    }

    LaunchedEffect(numberState) {
        if (numberState.isNotEmpty()) {
            delay(300)
            setInput(numberState)
            setData(
                Answer(
                    sub_question_answer = numberState,
                    sub_question_id = subQuestion.sub_question_id,
                    type = subQuestion.type,
                    sub_question_input = ""
                )
            )
        }
    }

    Column() {
        Spacer(modifier = Modifier.height(25.dp))
        if (subQuestion.text?.isNotEmpty() == true) {
            Text(
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                text = subQuestion.sub_question_id.toString() + ". " + subQuestion.text.toString(),
                color = Gray700
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row {
            InputTextField(
                text = numberState,
                typing = true,
                keyboardType = KeyboardType.Text,
                placeholder = "",
                onValueChange = {
                    numberState = it
                }
            )
        }
    }

}

@Composable
fun NumberSurvey(
    subQuestion: SubQuestion,
    setInput: (String) -> Unit,
    setData: (Answer) -> Unit,
    onNext: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    var numberState by remember {
        mutableStateOf("")
    }

    LaunchedEffect(numberState) {
        if (numberState.isNotEmpty()) {
            delay(300)
            setInput(numberState)
            println("@@@@@ numberState" + numberState)
            setData(
                Answer(
                    sub_question_answer = numberState,
                    sub_question_id = subQuestion.sub_question_id,
                    type = subQuestion.type,
                    sub_question_input = ""
                )
            )
        } else {
            setInput("")
        }
    }
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(61.dp)
                .background(
                    shape = RoundedCornerShape(14.dp),
                    color = Gray200,
                )
        ) {
            BasicTextField(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 16.dp),
                value = numberState,
                textStyle = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Gray900
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        keyboardController!!.hide()
                        onNext()
                    }
                ),
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                onValueChange = {
                    if (it.isNotEmpty()) {
                        if (it.toIntOrNull() != null) {
                            if (it.toInt() <= subQuestion.options[0].range.max) {
                                numberState = it
                            }
                        }
                    } else {
                        numberState = ""
                    }
                },
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        innerTextField()
                    }
                },
            )
        }
        Text(
            modifier = Modifier
                .padding(start = 10.dp, end = 15.dp),
            color = Gray500,
            text = subQuestion.options[0].unit
        )
    }
}

@Preview
@Composable
fun Radio() {
    CustomRadioButtonExample()

}

@Composable
fun CustomDesignRadioButton() {
    val radioOptions = listOf("옵션 1", "옵션 2", "옵션 3")
    var selectedOption by remember { mutableStateOf(radioOptions[0]) }

    radioOptions.forEach { option ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(24.dp)
                .clickable { selectedOption = option }
        ) {
            Surface(
                modifier = Modifier.size(24.dp),
                shape = CircleShape,
                color = if (option == selectedOption) Color.Magenta else Color.Gray
            ) {
                // 선택된 상태에 따라 내부에 추가적인 UI 요소를 넣을 수 있습니다.
            }
        }
    }
}

@Composable
fun CustomRadioButtonExample() {
    val radioOptions = listOf("옵션 1", "옵션 2", "옵션 3")
    var selectedOption by remember { mutableStateOf(radioOptions[0]) }

    Column {
        radioOptions.forEach { text ->
            Row(Modifier.padding(8.dp)) {
                RadioButton(
                    selected = text == selectedOption,
                    onClick = { selectedOption = text },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color.Blue,
                    )
                )
                Text(
                    text = text,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

//@Preview
//@Composable
//fun sfad() {
//    SurveyScreen(
//        hiltViewModel(), "", "", {},
//    )
//}

//val surveyPostData = SurveySetRes(
//                                    SurveyData(
//                                        question_id = 103,
//                                        answers = listOf(
//                                            Answer(
//                                                sub_question_id = 0,
//                                                type = "choice",
//                                                sub_question_answer = "답변"
//                                            )
//                                        ),
//                                        date = date.replace(".", "")
//                                    ),
//                                    User(
//                                        school_code = userDataList[6].toInt(),
//                                        grade = userDataList[3].toInt(),
//                                        class_num = userDataList[4].toInt(),
//                                        student_num = if (userDataList[5].isEmpty()) {
//                                            0
//                                        } else {
//                                            userDataList[5].toInt()
//                                        },
//                                        name = userDataList[1],
//                                        user_type = userType
//                                    )
//                                )