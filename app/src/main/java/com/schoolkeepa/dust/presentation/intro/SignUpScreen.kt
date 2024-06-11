package com.schoolkeepa.dust.presentation.intro

import android.util.Patterns
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.schoolkeepa.dust.R
import com.schoolkeepa.dust.data.model.school.SchoolListDto
import com.schoolkeepa.dust.data.response.login.UserProfile
import com.schoolkeepa.dust.presentation.component.DustBottomButton
import com.schoolkeepa.dust.presentation.component.DustBottomSheet
import com.schoolkeepa.dust.presentation.component.DustCheckbox
import com.schoolkeepa.dust.presentation.component.DustTopAppBar
import com.schoolkeepa.dust.presentation.navigation.Screen
import com.schoolkeepa.dust.presentation.setting.InputTextField
import com.schoolkeepa.dust.ui.theme.Blue300
import com.schoolkeepa.dust.ui.theme.Gray100
import com.schoolkeepa.dust.ui.theme.Gray200
import com.schoolkeepa.dust.ui.theme.Gray300
import com.schoolkeepa.dust.ui.theme.Gray400
import com.schoolkeepa.dust.ui.theme.Gray500
import com.schoolkeepa.dust.ui.theme.Gray600
import com.schoolkeepa.dust.ui.theme.Gray900
import com.schoolkeepa.dust.ui.theme.Green300
import com.schoolkeepa.dust.ui.theme.Red200
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.crypto.KeyAgreement

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    viewModel: IntroViewModel,
    onNavigateToRoute: (String) -> Unit,
    upPress: () -> Unit
) {
    var infoValid by remember { mutableStateOf(false) }
    var nameText by remember { mutableStateOf("") }
    var schoolNameText by remember { mutableStateOf("학교 이름을 입력해주세요") }
    var emailText by remember { mutableStateOf("") }
    var passwordText by remember { mutableStateOf("") }
    var rePasswordText by remember { mutableStateOf("") }
    var grade by remember { mutableStateOf("") }
    var ban by remember { mutableStateOf("") }
    var passCheck by remember { mutableStateOf(false) }
    var agree by remember { mutableStateOf(false) }
    var schoolCode by remember { mutableStateOf("") }
    var schoolAddress by remember { mutableStateOf("") }

    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    var viewAgreement by remember { mutableStateOf(false) }

    BackHandler {
        if (viewAgreement) {
            viewAgreement = false
        } else {
            upPress()
        }
    }

    Scaffold(
        topBar = {
            if (viewAgreement) {
                DustTopAppBar(title = "개인정보동의", upPress = { viewAgreement = false })
            } else {
                DustTopAppBar(title = "회원가입", upPress = upPress)
            }
        },

        ) {
        var columnHeightDp by remember {
            mutableStateOf(0.dp)
        }

        var userProfile by remember {
            mutableStateOf(
                UserProfile(
                    class_num = 0,
                    grade = 0,
                    name = "",
                    school_name = "",
                    school_code = 0,
                    user_type = "",
                    teacher_code = "",
                    school_address = ""
                )
            )
        }
        if (viewAgreement) {
            Column(modifier = Modifier.padding(it.calculateTopPadding())
                .fillMaxSize() ){
                Text(text = "개인정보 동의")
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(top = it.calculateTopPadding())
                    .fillMaxSize()
                    .imePadding()
            ) {
                InputSection(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .weight(1f),
                    viewModel = viewModel,
                    nameText,
                    setName = { nameText = it },
                    schoolNameText,
                    setSchoolName = { schoolNameText = it },
                    emailText,
                    setEmail = { emailText = it },
                    passwordText,
                    setPassword = { passwordText = it },
                    rePasswordText,
                    setRePassword = { rePasswordText = it },
                    grade = grade,
                    setGrade = { grade = it },
                    ban = ban,
                    setBan = { ban = it },
                    setPassCheck = { passCheck = it },
                    setSchoolAddress = { schoolAddress = it },
                    setSchoolCode = { schoolCode = it },
                )

                infoValid =
                    schoolNameText != "학교 이름을 입력해주세요" && grade.isNotEmpty() && ban.isNotEmpty() && nameText.isNotEmpty() && emailText.isNotEmpty()
//                        && passCheck && agree

                if (infoValid) {
                    userProfile = UserProfile(
                        school_code = schoolCode.toInt(),
                        grade = grade.toInt(),
                        class_num = ban.toInt(),
                        school_name = schoolNameText,
                        name = nameText,
                        user_type = "teacher",
                        school_address = schoolAddress,
                        teacher_code = "123456"
                    )
                }

                SignUpButtonSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .height(128.dp),
                    viewModel = viewModel,
                    scope = scope,
                    onNavigateToRoute = onNavigateToRoute,
                    infoValid = infoValid,
                    setColumnHeight = { columnHeightDp = it },
                    agree = agree,
                    setAgree = {
                        agree = it
                    },
                    emailText = emailText,
                    passwordText = passwordText,
                    userProfile = userProfile,
                    setAgreement = { viewAgreement = it }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpButtonSection(
    modifier: Modifier,
    viewModel: IntroViewModel,
    scope: CoroutineScope,
    onNavigateToRoute: (String) -> Unit,
    infoValid: Boolean,
    setColumnHeight: (Dp) -> Unit,
    agree: Boolean,
    setAgree: (Boolean) -> Unit,
    emailText: String,
    passwordText: String,
    userProfile: UserProfile,
    setAgreement: (Boolean) -> Unit
) {
    val localDensity = LocalDensity.current

    Column(
        modifier = modifier.onGloballyPositioned {
            setColumnHeight(with(localDensity) { it.size.height.toDp() })
        }
    ) {
//        println(columnHeightDp.toString() + "@@@@DPDP")
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.padding(bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DustCheckbox(
                Modifier.padding(start = 15.dp, end = 10.dp),
                agree,
                { setAgree(!agree) }
            )
            Text(text = "개인정보동의", modifier = Modifier)
            Text(
                text = " (필수)",
                color = Gray400
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "내용 보기",
                textDecoration = TextDecoration.Underline,
                color = Gray600,
                modifier = Modifier.clickable { setAgreement(true) }
            )
            Spacer(modifier = Modifier.width(20.dp))
        }
        DustBottomButton(
            onClick = {
                scope.launch {
                    println(emailText.toString() + passwordText + userProfile.toString())
//                            signUpViewModel.loginUser(emailText, passwordText)
                    viewModel.registerUser(emailText, passwordText, userProfile)
//                            if (signUpViewModel.registerState.value.isSuccess.isNotEmpty()){
//                                Toast.makeText(context, emailText, Toast.LENGTH_SHORT).show()

                    onNavigateToRoute(Screen.Intro.route)
//                            }
                }
            },
            text = "가입하기",
            isValid = infoValid
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputSection(
    modifier: Modifier,
    viewModel: IntroViewModel,
    nameText: String,
    setName: (String) -> Unit,
    schoolNameText: String,
    setSchoolName: (String) -> Unit,
    emailText: String,
    setEmail: (String) -> Unit,
    passwordText: String,
    setPassword: (String) -> Unit,
    rePasswordText: String,
    setRePassword: (String) -> Unit,
    grade: String,
    setGrade: (String) -> Unit,
    ban: String,
    setBan: (String) -> Unit,
    setPassCheck: (Boolean) -> Unit,
    setSchoolCode: (String) -> Unit,
    setSchoolAddress: (String) -> Unit,
) {

    val passwordPattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[\$@\$!%*#?&]).{8,15}.\$")
    var passwordCheck by remember { mutableStateOf(true) }
    var passwordBothCheck by remember { mutableStateOf(true) }

    LaunchedEffect(passwordText) {
        if (passwordText.isNotEmpty()) {
            delay(500L)
            passwordCheck = passwordPattern.matcher(passwordText).find()
            if (passwordCheck && passwordBothCheck) {
                setPassCheck(true)
            }
        }
    }
    LaunchedEffect(rePasswordText) {
        if (rePasswordText.isNotEmpty()) {
            delay(300L)
            passwordBothCheck = passwordText == rePasswordText
            if (passwordCheck && passwordBothCheck) {
                setPassCheck(true)
            }
        }
    }
    val scrollState = rememberScrollState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var bottomSheetState = rememberModalBottomSheetState(false)
    var searchText by remember { mutableStateOf("") }
    var scope = rememberCoroutineScope()

    var schoolData = remember { mutableStateOf(SchoolListDto()) }
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
            setSchoolName = { setSchoolName(it) },
            setSchoolCode = { setSchoolCode(it) },
            setSchoolAddress = { setSchoolAddress(it) }
        )
    }
    Column(
        modifier = modifier.verticalScroll(scrollState)
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Column(
            modifier = Modifier
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(61.dp)
                    .padding()
                    .background(
                        shape = RoundedCornerShape(14.dp),
                        color = Gray200,
                    ).clip(
                        shape = RoundedCornerShape(14.dp)
                    )
                    .clickable {
                        showBottomSheet = true
                    },
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 16.dp),
                    fontSize = 18.sp,
                    color = if (schoolNameText == "학교 이름을 입력해주세요") {
                        Gray500
                    } else {
                        Gray900
                    },
                    text = schoolNameText,
                )
            }

            Row(
                modifier = Modifier
                    .padding(top = 10.dp)
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
                                    setGrade(it)
                                }
                            } else {
                                setGrade(it)
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
                                    setBan(it)
                                }
                            } else {
                                setBan(it)
                            }
                        },
                    )
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
        InputComponent(
            "이름",
            nameText,
            placeholder = "이름을 입력해 주세요",
            { setName(it) }
        )

        Spacer(modifier = Modifier.height(30.dp))

        InputComponent(
            "이메일",
            emailText,
            placeholder = "이메일을 입력해 주세요",
            { setEmail(it) }
        )

        val emailPattern = Patterns.EMAIL_ADDRESS.matcher(emailText).matches()

        if (!emailPattern && emailText.isNotEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "비밀번호 조건에 맞게 비밀번호를 설정해 주세요.", color = Red200
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Column {
            Text(
                text = "비밀번호",
                fontWeight = FontWeight.Bold,
                color = Gray900
            )
            Spacer(modifier = Modifier.height(10.dp))
            InputTextField(
                text = passwordText,
                placeholder = "8자 이상 비밀번호를 설정해 주세요",
                typing = true,
                keyboardType = KeyboardType.Password,
                onValueChange = { setPassword(it) },
                visibility = false,
            )
            Spacer(modifier = Modifier.height(10.dp))
            InputTextField(
                text = rePasswordText,
                placeholder = "비밀번호를 확인해 주세요",
                typing = true,
                keyboardType = KeyboardType.Password,
                onValueChange = { setRePassword(it) },
                visibility = false,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                )
            )
            if (!passwordCheck) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "비밀번호 조건에 맞게 비밀번호를 설정해 주세요.", color = Red200
                )
            } else if (!passwordBothCheck) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "비밀번호가 일치하지 않습니다.", color = Red200
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "문자, 숫자, 특수문자를 혼합하여 8자 이상")
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun InputComponent(
    topic: String,
    text: String,
    placeholder: String,
    setText: (String) -> Unit,
) {
    Column {
        Text(
            text = topic,
            fontWeight = FontWeight.Bold,
            color = Gray900
        )
        Spacer(modifier = Modifier.height(10.dp))
        InputTextField(
            text = text,
            placeholder = placeholder,
            typing = true,
            keyboardType = KeyboardType.Text,
            onValueChange = { setText(it) }
        )
    }
}

@Preview
@Composable
fun afdsa() {
    SignUpScreen(
        hiltViewModel(),
        onNavigateToRoute = {},
    ) {

    }
}