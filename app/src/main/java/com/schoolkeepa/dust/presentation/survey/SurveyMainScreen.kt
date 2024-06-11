package com.schoolkeepa.dust.presentation.survey

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.schoolkeepa.dust.R
import com.schoolkeepa.dust.data.model.school.SchoolInfo
import com.schoolkeepa.dust.data.model.school.SchoolListDto
import com.schoolkeepa.dust.presentation.component.DustBottomSheet
import com.schoolkeepa.dust.presentation.component.DustCheckbox
import com.schoolkeepa.dust.presentation.component.DustTopAppBar
import com.schoolkeepa.dust.presentation.navigation.Screen
import com.schoolkeepa.dust.presentation.setting.InputTextField
import com.schoolkeepa.dust.ui.theme.Blue100
import com.schoolkeepa.dust.ui.theme.Blue300
import com.schoolkeepa.dust.ui.theme.Gray200
import com.schoolkeepa.dust.ui.theme.Gray400
import com.schoolkeepa.dust.ui.theme.Gray500
import com.schoolkeepa.dust.ui.theme.Gray600
import com.schoolkeepa.dust.ui.theme.Gray900
import com.schoolkeepa.dust.ui.theme.Green300
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurveyMainScreen(
    onNavigateToRoute: (String) -> Unit,
    upPress: () -> Unit,
    userType: String,
    viewModel: SurveyViewModel,
    date: String,
) {
    val scope = rememberCoroutineScope()

    var schoolList = remember { mutableStateListOf<SchoolInfo>() }

    var schoolData = remember { mutableStateOf(SchoolListDto()) }

    Scaffold(
        topBar = {
            DustTopAppBar(
                title = "",
                color = Blue100,
                upPress = upPress
            )
        }
    ) {
        var filled by remember { mutableStateOf(false) }

        var showBan by remember { mutableStateOf(false) }

        var enabled by remember { mutableStateOf(true) }
        var bottomSheetState = rememberModalBottomSheetState(false)
        var showBottomSheet by remember { mutableStateOf(false) }
        var schoolName by remember { mutableStateOf(viewModel.schoolName.value) }
        var schoolCode by remember { mutableStateOf(viewModel.schoolCode.value) }
        var name by remember { mutableStateOf(viewModel.name.value) }
        var grade by remember { mutableStateOf(viewModel.grade.value) }
        var ban by remember { mutableStateOf(viewModel.ban.value) }
        var studentNumber by remember { mutableStateOf(viewModel.studentNumber.value) }

        var agree by remember { mutableStateOf(viewModel.agreement.value) }

        println(filled.toString() + "@@@@@@")

        filled =
            schoolName!!.isNotEmpty() && schoolName != "학교를 입력해 주세요." && name!!.isNotEmpty() && grade!!.isNotEmpty() && ban!!.isNotEmpty() && (userType == "teacher" || studentNumber!!.isNotEmpty()) && agree!!

        showBan = name!!.isNotEmpty() && schoolName != "학교를 입력해 주세요." && schoolName!!.isNotEmpty()

        var searchText by remember { mutableStateOf("") }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding()),
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(Blue100),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 10.dp),
                    text = "설문조사를 시작할게요!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                )
                Text(
                    text = "질문지를 통해서 미세먼지가\n잘 관리되고 있는지 살펴볼게요",
                    color = Gray600,
                    textAlign = TextAlign.Center
                )
                Image(
                    modifier = Modifier
                        .fillMaxWidth(),
                    painter = painterResource(id = R.drawable.survey_img),
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .imePadding()
                    .padding(start = 24.dp, end = 24.dp, top = 30.dp)
            ) {
                InputTextField(
                    text = name!!,
                    placeholder = "이름을 입력해 주세요.",
                    typing = true,
                    keyboardType = KeyboardType.Text,
                    onValueChange = { name = it }
                )
                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(61.dp)
                        .padding()
                        .clip(
                            shape = RoundedCornerShape(14.dp),
                        )
                        .background(
                            shape = RoundedCornerShape(14.dp),
                            color = Gray200,
                        )
                        .clickable {
                            showBottomSheet = true
                        },
                ) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 16.dp),
                        fontWeight = if (schoolName != "학교를 입력해 주세요.") {
                            FontWeight.Bold
                        } else {
                            FontWeight.Normal
                        },
                        fontSize = 18.sp,
                        color = if (schoolName == "학교를 입력해 주세요.") {
                            Gray500
                        } else {
                            Gray900
                        },
                        text = schoolName!!,
                    )
                    if (schoolName != "학교를 입력해 주세요.") {
                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(16.dp),
                            color = Blue300,
                            text = "변경"
                        )
                    }
                }
                if (showBan) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            InputTextField(
                                text = grade!!,
                                typing = true,
                                keyboardType = KeyboardType.Number,
                                trailingText = "학년",
                                onValueChange = {
                                    if (it.isNotEmpty()) {
                                        if (it.toInt() <= 6) {
                                            grade = it
                                        }
                                    } else {
                                        grade = it
                                    }
                                }
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Box(modifier = Modifier.weight(1f)) {
                            InputTextField(
                                text = ban!!,
                                typing = true,
                                keyboardType = KeyboardType.Number,
                                trailingText = "반",
                                onValueChange = {
                                    if (it.isNotEmpty()) {
                                        if (it.toInt() <= 99) {
                                            ban = it
                                        }
                                    } else {
                                        ban = it
                                    }
                                },
                            )
                        }
                        if (userType != "teacher") {
                            Spacer(modifier = Modifier.width(10.dp))
                            Box(modifier = Modifier.weight(1f)) {
                                InputTextField(
                                    text = studentNumber!!,
                                    typing = true,
                                    keyboardType = KeyboardType.Number,
                                    trailingText = "번",
                                    onValueChange = {
                                        if (it.isNotEmpty()) {
                                            if (it.toInt() <= 99) {
                                                studentNumber = it
                                            }
                                        } else {
                                            studentNumber = it
                                        }
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        imeAction = ImeAction.Done
                                    )
                                )
                            }
                        }
                    }
                }

            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {
                Row(
                    modifier = Modifier.padding(bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DustCheckbox(
                        Modifier.padding(start = 15.dp, end = 10.dp),
                        agree!!,
                        { agree = !agree!! }
                    )
                    Text(text = "개인정보동의")
                    Text(
                        text = "(필수)",
                        color = Gray400
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "내용 보기",
                        textDecoration = TextDecoration.Underline,
                        color = Gray600,
                        modifier = Modifier.clickable {
                            viewModel.setInfo(
                                name!!,
                                schoolName!!,
                                grade!!,
                                ban!!,
                                studentNumber!!,
                                agree!!
                            )
                            onNavigateToRoute(Screen.SurveyAgreement.route)
                        }
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                }
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 15.dp),
                    shape = RoundedCornerShape(25),
                    colors = ButtonDefaults.buttonColors(
                        if (filled) {
                            Green300
                        } else {
                            Gray200
                        }
                    ),
                    onClick = {
                        if (filled) {
                            viewModel.saveUserData("$userType/$name/$schoolName/$grade/$ban/$studentNumber/$schoolCode")
                            upPress()
                            onNavigateToRoute(Screen.Survey.route + "/" + date)
                        }
                    }) {
                    Text(
                        text = "시작하기",
                        color = if (filled) {
                            Color.White
                        } else {
                            Gray400
                        }
                    )
                }
                Spacer(modifier = Modifier.height(35.dp))

            }

            if (showBottomSheet) {
                DustBottomSheet(
                    bottomSheetState = bottomSheetState,
                    setShowBottomSheet = { showBottomSheet = it },
                    searchText = searchText,
                    setSearchText = { searchText = it },
                    schoolData = schoolData,
                    setSearchSchool = {
                        scope.launch {
                            viewModel.searchSchoolList(
                                searchText.trim()
                            ).data?.let {
                                println(it)
                                schoolData.value = it
                            }
                        }
                    },
                    setSchoolName = { schoolName = it },
                    setSchoolCode = { schoolCode = it },
                    setSchoolAddress = { }
                )
            }
        }
    }
}
