package com.schoolkeepa.dust.presentation.setting

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.schoolkeepa.dust.DustApplication
import com.schoolkeepa.dust.R
import com.schoolkeepa.dust.data.AndroidDownloader
import com.schoolkeepa.dust.data.model.school.SchoolListDto
import com.schoolkeepa.dust.data.response.login.UserProfile
import com.schoolkeepa.dust.presentation.component.DustBottomSheet
import com.schoolkeepa.dust.presentation.component.DustTopAppBar
import com.schoolkeepa.dust.ui.theme.*
import com.schoolkeepa.dust.util.CustomToast
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    title: String,
    viewModel: SettingViewModel,
    userData: String,
    userType: String,
    upPress: () -> Unit,
) {

    val context = LocalContext.current

    val data = userData.split("/")
    println(userData + data + userType + "@@@@@USERTYPE")

    val scope = rememberCoroutineScope()
    val application = DustApplication
    var showDialog by remember { mutableStateOf(false) }
    if (showDialog) {
        CustomDialog(
            title = "",
            message = "학습자를 다시 선택하면\n저장된 정보가 사라집니다.", cancel = "취소", confirm = "다시선택",
            setShowDialog = {
                showDialog = false
            },
            setConfirmClick = {
                showDialog = false
                application.preferences.deleteAll()
                if (userType == "teacher") {
                    scope.launch {
                        viewModel.signOut()
                    }
                } else {
                    viewModel.resetUserEntry()
                }
            }
        )
    }
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        CustomDialog(
            title = "",
            message = "로그아웃 시\n학습자 화면으로 이동합니다.", cancel = "취소", confirm = "로그아웃",
            setShowDialog = {
                showLogoutDialog = false
            },
            setConfirmClick = {
                showLogoutDialog = false
                application.preferences.deleteAll()
                if (userType == "teacher") {
                    scope.launch {
                        viewModel.signOut()
                    }
                } else {
                    viewModel.resetUserEntry()
                }
            }
        )
    }

    var showBottomSheet by remember { mutableStateOf(false) }
    var schoolName by remember { mutableStateOf("학교를 입력해 주세요.") }
    var schoolCode by remember { mutableStateOf("") }
    var schoolAddress by remember { mutableStateOf("") }
    val schoolData = remember { mutableStateOf(SchoolListDto()) }


    var searchText by remember { mutableStateOf("") }

    var saveBool by remember { mutableStateOf(false) }

    var changeData by remember { mutableStateOf(userData) }

    var isShowToast by remember { mutableStateOf(false) }


    if (isShowToast) {
        val customToast = CustomToast(context)
        customToast.MakeText(message = "모든 정보를 입력해주세요.")
        isShowToast = false
    }

    Scaffold(
        topBar = {
            DustTopAppBar(title, actions = true,
                upPress = upPress,
                onSaveClick = {
                    scope.launch {
                        if (saveBool) {
                            isShowToast = true
                        } else {
                            if (userType == "teacher") {
                                val data = userData.split("/")
                                viewModel.saveTeacherData(
                                    UserProfile(
                                        school_code = data[6].toInt(),
                                        grade = data[3].toInt(),
                                        class_num = data[4].toInt(),
                                        name = data[1],
                                        user_type = data[0],
                                        teacher_code = data[7],
                                        school_name = data[2],
                                        school_address = data[8],
                                    )
                                )
                                viewModel.saveUserData(changeData + "/" + data[7] + "/" + data[8])
                            } else {
                                viewModel.saveUserData(changeData)
                            }
                        }
                    }
                })
        }
    ) {
        Column(
            modifier = Modifier
                .padding(top = it.calculateTopPadding())
                .fillMaxSize()
                .background(Color.White)
        ) {
            val bottomSheetState = rememberModalBottomSheetState(false)

            SettingInfoSection(
                modifier = Modifier,
                data = data,
                setShowDialog = { showDialog = true },
                setShowLogoutDialog = { showLogoutDialog = true },
                userType = userType,
                clickChangeSchool = {
                    showBottomSheet = true
                },
                checkEmpty = {
                    saveBool = it
                },
                changeData = {
                    changeData = it
                },
                schoolName = schoolName,
                setSchoolName = {
                    schoolName = it
                },
                setSchoolCode = {
                    schoolCode = it
                },
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .background(Gray100)
            )
            val keyboard = LocalSoftwareKeyboardController.current
            LaunchedEffect(true) {
                keyboard!!.show()
            }
            SettingAppSection(
                userType = userType, viewModel = viewModel,
                schoolCode = schoolCode,
                grade = data[3],
                classNum = data[4]
            )
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
                    setSchoolAddress = { schoolAddress = it }
                )
            }
        }
    }
}

@Composable
fun CustomDialog(
    title: String = "",
    message: String,
    cancel: String,
    confirm: String,
    setShowDialog: (Boolean) -> Unit,
    setConfirmClick: () -> Unit
) {
    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                if (title.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = title, fontSize = 20.sp, fontWeight = FontWeight.Bold,
                        modifier = Modifier,
                    )
                }
                Spacer(
                    modifier = Modifier.weight(1f)
                )
                Text(
                    modifier = Modifier.padding(),
                    text = message,
                    textAlign = TextAlign.Center
                )
                Spacer(
                    modifier = Modifier.weight(1f)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
                ) {
                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .height(55.dp),
                        shape = RoundedCornerShape(25),
                        colors = ButtonDefaults.buttonColors(Gray100),
                        onClick = { setShowDialog(false) }) {
                        Text(
                            color = Gray900,
                            text = cancel,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .height(55.dp),
                        shape = RoundedCornerShape(25),
                        colors = ButtonDefaults.buttonColors(Green300),
                        onClick = {
                            setConfirmClick()
                        }) {
                        Text(
                            text = confirm,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SettingInfoSection(
    modifier: Modifier,
    data: List<String> = listOf("", "", "", "", "", "", "", ""),
    setShowDialog: (Boolean) -> Unit,
    setShowLogoutDialog: (Boolean) -> Unit,
    userType: String,
    clickChangeSchool: () -> Unit,
    checkEmpty: (Boolean) -> Unit,
    changeData: (String) -> Unit,
    schoolName: String,
    setSchoolName: (String) -> Unit,
    setSchoolCode: (String) -> Unit,
) {
    var verticalScrollState = rememberScrollState()
    var school by remember { mutableStateOf(data[0]) }
    var name by remember { mutableStateOf(data[1]) }
    if (data[2].isNotEmpty()) {
        setSchoolName(data[2])
        setSchoolCode(data[6])
    }
    var grade by remember { mutableStateOf(data[3]) }
    var ban by remember { mutableStateOf(data[4]) }
    var studentNumber by remember { mutableStateOf(data[5]) }
    val schoolCode by remember { mutableStateOf(data[6]) }


    changeData("$school/$name/$schoolName/$grade/$ban/$studentNumber/$schoolCode")
    LaunchedEffect(school, name, schoolName, grade, ban, studentNumber) {
        if (school.isNotEmpty() && name.isEmpty() && schoolName.isNotEmpty() && grade.isNotEmpty() && ban.isNotEmpty() && studentNumber.isNotEmpty()) {
            checkEmpty(true)
        } else {
            println(school + name + schoolName + grade + ban + studentNumber)
            checkEmpty(false)
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .imePadding()
            .verticalScroll(verticalScrollState)
    ) {
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = "학습자",
            fontWeight = FontWeight.Bold,
            color = Gray700
        )
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(61.dp)
                .padding()
                .background(
                    shape = RoundedCornerShape(14.dp),
                    color = Gray100,
                )
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 16.dp),
                text = when (school) {
                    "elementary" -> {
                        "초등학생"
                    }

                    "middle" -> {
                        "중학생"
                    }

                    "high" -> {
                        "고등학생"
                    }

                    "teacher" -> {
                        "선생님"
                    }

                    else -> {
                        ""
                    }
                },
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(16.dp)
                    .clickable {
                        if (school == "teacher") {
                            setShowLogoutDialog(true)
                        } else {
                            setShowDialog(true)
                        }
                    },
                color = Blue300,
                text = if (school == "teacher") {
                    "로그아웃"
                } else {
                    "변경"
                }
            )
        }


        Text(
            modifier = Modifier.padding(vertical = 10.dp),
            text = "학습자 설정에 따라 설문지, 매뉴얼 내용이 달라요.",
            color = Gray700
        )

        Spacer(modifier = Modifier.height(25.dp))

        Text(
            text = "학교", color = Gray700,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(61.dp)
                .background(
                    shape = RoundedCornerShape(14.dp),
                    color = Gray100,
                )
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 16.dp),
                text = schoolName,
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(16.dp)
                    .clickable {
                        clickChangeSchool()
//                        setShowDialog(true)
                    },
                color = Blue300,
                text = "변경"
            )
        }
        Row(
            modifier = Modifier.padding(top = 10.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(61.dp)
                    .padding()
                    .background(
                        shape = RoundedCornerShape(14.dp),
                        color = Gray100,
                    )
            ) {
                InputTextField(
                    text = grade,
                    Modifier,
                    placeholder = "",
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
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(61.dp)
                    .padding()
                    .background(
                        shape = RoundedCornerShape(14.dp),
                        color = Gray100,
                    )
            ) {
                InputTextField(
                    text = ban,
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
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(61.dp)
                        .padding()
                        .background(
                            shape = RoundedCornerShape(14.dp),
                            color = Gray100,
                        )
                ) {
                    InputTextField(
                        text = studentNumber,
                        typing = true,
                        keyboardType = KeyboardType.Number,
                        trailingText = "번호",
                        onValueChange = {
                            if (it.isNotEmpty()) {
                                if (it.toInt() <= 99) {
                                    studentNumber = it
                                }
                            } else {
                                studentNumber = it
                            }
                        },
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(25.dp))
        Text(
            text = "이름",
            color = Gray700,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(61.dp)
                .padding()
                .background(
                    shape = RoundedCornerShape(14.dp),
                    color = Gray100,
                )
        ) {
            InputTextField(
                text = name,
                typing = true,
                keyboardType = KeyboardType.Text,
                onValueChange = {
                    name = it
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                )
            )
//            Text(
//                modifier = Modifier
//                    .align(Alignment.CenterStart)
//                    .padding(start = 16.dp),
//                text = name,
//                fontWeight = FontWeight.Bold
//            )
        }
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun SettingAppSection(
    viewModel: SettingViewModel,
    userType: String,
    schoolCode: String,
    grade: String,
    classNum: String,
) {

    val context = LocalContext.current

    val downloader = AndroidDownloader(context)
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("label", "text")
    val manager = LocalClipboardManager.current
    var isShowToast by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    if (isShowToast) {
        val customToast = CustomToast(context)
        customToast.MakeText(message = "이메일을 복사했습니다!")
        isShowToast = false
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        if (userType == "teacher") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(start = 24.dp, end = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "설문조사 자료 다운로드")
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = {
                    scope.launch {
                        downloader.downloadFile("https://finedustlab-api-ko.net/survey/download?schoolCode=$schoolCode&grade=$grade&class_num=$classNum")
//                        viewModel.getXls(schoolCode = schoolCode, grade = grade, classNum = classNum)
                    }
                }) {
                    Image(
                        painterResource(id = R.drawable.ic_download),
                        contentDescription = "ic_download"
                    )
                }
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = Gray300),
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(start = 24.dp, end = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "문의사항")
            Spacer(modifier = Modifier.weight(1f))
            Text(
                modifier = Modifier.padding(),
                text = "pardess@naver.com"
            )

            IconButton(onClick = {
                manager.setText(AnnotatedString("pardess@naver.com"))
            }) {
                Image(
                    painterResource(id = R.drawable.ic_copy), contentDescription = "copy icon"
                )
            }
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(color = Gray300),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "업데이트 내용", color = Gray400)
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painterResource(id = R.drawable.ic_gray_right_arrow),
                contentDescription = "right arrow icon"
            )
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(color = Gray300),
        )
    }
}

@Composable
fun InputTextField(
    text: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    typing: Boolean = true,
    keyboardType: KeyboardType,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Next,
        keyboardType = keyboardType
    ),
    trailingText: String = "",
    onValueChange: (String) -> Unit,
    visibility: Boolean = true,
    showNext: (Boolean) -> Unit = {

    },
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(61.dp)
            .background(
                shape = RoundedCornerShape(14.dp),
                color = Gray100,
            )
    ) {
        BasicTextField(
            modifier = modifier
                .align(Alignment.CenterStart)
                .padding(start = 16.dp),
            value = text,
            textStyle = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Gray900
            ),
            visualTransformation = if (visibility) VisualTransformation.None else PasswordVisualTransformation(),
            maxLines = 1,
            keyboardOptions = keyboardOptions,
            onValueChange = onValueChange,
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (text.isEmpty()) {
                        Text(
                            text = placeholder,
                            fontSize = 18.sp,
                            color = Gray500
                        )
                    }
                    if (typing) {
                        innerTextField()
                    }
                    if (trailingText.isNotEmpty()) {
                        Text(
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .align(Alignment.CenterEnd),
                            color = Gray600,
                            fontWeight = FontWeight.Bold,
                            text = trailingText
                        )
                    }
                }
            },
        )
    }
}
//
//@Preview
//@Composable
//fun SettingScreenPreview() {
//    SettingScreen("S", hiltViewModel(), "", false)
//}