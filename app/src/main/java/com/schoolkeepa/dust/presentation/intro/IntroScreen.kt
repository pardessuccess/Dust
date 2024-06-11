package com.schoolkeepa.dust.presentation.intro

import android.util.Patterns
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.schoolkeepa.dust.R
import com.schoolkeepa.dust.presentation.component.DustBottomButton
import com.schoolkeepa.dust.presentation.navigation.Screen
import com.schoolkeepa.dust.ui.theme.Blue100
import com.schoolkeepa.dust.ui.theme.Blue300
import com.schoolkeepa.dust.ui.theme.Gray100
import com.schoolkeepa.dust.ui.theme.Gray300
import com.schoolkeepa.dust.ui.theme.Gray500
import com.schoolkeepa.dust.ui.theme.Gray600
import com.schoolkeepa.dust.ui.theme.Gray700
import com.schoolkeepa.dust.ui.theme.Gray900
import com.schoolkeepa.dust.ui.theme.Red300
import kotlinx.coroutines.launch

@Preview
@Composable
fun fasdfdas() {

    val height = LocalConfiguration.current.screenHeightDp
    var bottomPadding by remember { mutableStateOf(0) }
    var imageBox by remember { mutableStateOf(235) }

    if (height >= 688) {
        if (height - 688 >= 40) {
            imageBox = 275
            bottomPadding = height - 725
        } else {
            imageBox = height - 688
            bottomPadding = 0
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = Blue100
    ) {
        Image(painter = painterResource(id = R.drawable.intro_bg), contentDescription = null)
        Box {
            Column() {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 30.dp)
                            .height(172.dp)
                    ) {
                        Text(
                            modifier = Modifier.padding(top = 48.dp, bottom = 10.dp),
                            text = "반가워요!\n" +
                                    "누구로 접속할까요?",
                            fontWeight = FontWeight.Bold,
                            fontSize = 25.sp,
                        )
                        Text(
                            text = "학습자에 따라 매뉴얼과 설문 내용이 달라요!",
                            color = Gray600,
                            textAlign = TextAlign.Center
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(imageBox.dp)
                    ) {
                        Image(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .height(235.dp),
                            contentScale = ContentScale.Crop,
                            painter = painterResource(id = R.drawable.intro_main_icon),
                            contentDescription = null
                        )
                    }
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(236.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 30.dp)
                                .align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            OutlinedButton(
                                onClick = {
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(62.dp)
                                    .padding(bottom = 7.dp),
                                border = BorderStroke(
                                    1.5.dp,
                                    color = Gray300
                                ),
                                colors = ButtonDefaults.buttonColors(Gray100),
                                shape = RoundedCornerShape(
                                    10.dp,
                                )
                            ) {
                                Text(
                                    text = "type",
                                    color = Gray900,
                                )
                            }
                            OutlinedButton(
                                onClick = {
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(62.dp)
                                    .padding(bottom = 7.dp),
                                border = BorderStroke(
                                    1.5.dp,
                                    color = Gray300
                                ),
                                colors = ButtonDefaults.buttonColors(Gray100),
                                shape = RoundedCornerShape(
                                    10.dp,
                                )
                            ) {
                                Text(
                                    text = "type",
                                    color = Gray900,
                                )
                            }
                            OutlinedButton(
                                onClick = {
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(62.dp)
                                    .padding(bottom = 7.dp),
                                border = BorderStroke(
                                    1.5.dp,
                                    color = Gray300
                                ),
                                colors = ButtonDefaults.buttonColors(Gray100),
                                shape = RoundedCornerShape(
                                    10.dp,
                                )
                            ) {
                                Text(
                                    text = "type",
                                    color = Gray900,
                                )
                            }
//                        TypeButton({}, viewModel, "중학생")
//                        TypeButton({}, viewModel, "고등학생")
                        }
                    }
                    Box(
                        modifier = Modifier
                            .height(45.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
//                        .clickable { showBottomSheet = true },
                            text = "선생님으로 로그인",
                            textDecoration = TextDecoration.Underline,
                            color = Gray700
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IntroScreen(
    onNavigateToRoute: (String) -> Unit,
    viewModel: IntroViewModel
) {

    var showBottomSheet by remember { mutableStateOf(false) }
    var bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )
    var emailText by remember { mutableStateOf("") }
    var passwordText by remember { mutableStateOf("") }

    val height = LocalConfiguration.current.screenHeightDp
    var bottomPadding by remember { mutableStateOf(0) }
    var imageBox by remember { mutableStateOf(235) }

    if (height >= 688) {
        if (height - 688 >= 40) {
            imageBox = 275
            bottomPadding = height - 725
        } else {
            imageBox = height - 688
            bottomPadding = 0
        }
    }

    var str by remember { mutableStateOf("") }
    str = viewModel.readUserGrade().collectAsState("").value

    var s by remember { mutableStateOf("") }
    s = viewModel.readSurvey().collectAsState("").value

    Scaffold {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding()),
            color = Blue100
        ) {
            Image(painter = painterResource(id = R.drawable.intro_bg), contentDescription = null)
            Box {
                Column() {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 30.dp)
                            .height(172.dp)
                    ) {
                        Text(
                            modifier = Modifier.padding(top = 48.dp, bottom = 10.dp),
                            text = "반가워요!\n" +
                                    "누구로 접속할까요?",
                            fontWeight = FontWeight.Bold,
                            fontSize = 25.sp,
                        )
                        Text(
                            text = "학습자에 따라 매뉴얼과 설문 내용이 달라요!",
                            color = Gray600,
                            textAlign = TextAlign.Center
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(imageBox.dp)
                    ) {
                        Image(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .height(235.dp),
                            contentScale = ContentScale.Crop,
                            painter = painterResource(id = R.drawable.intro_main_icon),
                            contentDescription = null
                        )
                    }
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(236.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 30.dp)
                                .align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            TypeButton(onNavigateToRoute, viewModel, "초등학생")
                            TypeButton(onNavigateToRoute, viewModel, "중학생")
                            TypeButton(onNavigateToRoute, viewModel, "고등학생")
                        }
                    }
                    Box(
                        modifier = Modifier
                            .height(45.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .clickable { showBottomSheet = true },
                            text = "선생님으로 로그인",
                            textDecoration = TextDecoration.Underline,
                            color = Gray700
                        )
                    }
                }

                if (showBottomSheet) {
                    BottomSheetSection(
                        viewModel = viewModel,
                        onNavigateToRoute = onNavigateToRoute,
                        bottomSheetState = bottomSheetState,
                        setShowBottomSheet = { showBottomSheet = it },
                        emailText = emailText,
                        setEmailText = { emailText = it },
                        passwordText = passwordText,
                        setPassword = { passwordText = it },
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetSection(
    viewModel: IntroViewModel,
    onNavigateToRoute: (String) -> Unit,
    bottomSheetState: SheetState,
    setShowBottomSheet: (Boolean) -> Unit,
    emailText: String,
    setEmailText: (String) -> Unit,
    passwordText: String,
    setPassword: (String) -> Unit,
) {

    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        modifier = Modifier.fillMaxSize(),
        onDismissRequest = {
            setShowBottomSheet(false)
        },
        sheetState = bottomSheetState,
        containerColor = Color.White,
        contentColor = Color.White,
        content = {
            Box(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.5f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "로그인",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 13.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Box(
                        modifier = Modifier
                            .height(61.dp)
                            .padding(bottom = 8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 24.dp)
                                .background(
                                    shape = RoundedCornerShape(14.dp),
                                    color = Gray100,
                                )
                        ) {
                            BasicTextField(
                                modifier = Modifier
                                    .height(56.dp)
                                    .align(Alignment.CenterStart)
                                    .padding(start = 16.dp),
                                value = emailText,
                                textStyle = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = Gray900
                                ),
                                maxLines = 1,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Next
                                ),
                                onValueChange = { setEmailText(it) },
                                decorationBox = { innerTextField ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    ) {
                                        if (emailText.isEmpty()) {
                                            Text(
                                                modifier = Modifier
                                                    .align(Alignment.CenterStart),
                                                text = "이메일을 입력해 주세요.",
                                                fontSize = 18.sp,
                                                color = Gray500
                                            )
                                        }

                                        Row(
                                            modifier = Modifier
                                                .fillMaxHeight(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            innerTextField()
                                        }
                                    }
                                },
                            )
                        }
                    }
                    val emailPattern = Patterns.EMAIL_ADDRESS.matcher(emailText).matches()

                    if (!emailPattern && emailText.isNotEmpty()) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                            text = "유효하지 않은 이메일 형식입니다.", color = Red300
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                    }

                    Box(
                        modifier = Modifier
                            .height(56.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 24.dp)
                                .background(
                                    shape = RoundedCornerShape(14.dp),
                                    color = Gray100,
                                )
                        ) {
                            BasicTextField(
                                modifier = Modifier
                                    .height(56.dp)
                                    .align(Alignment.CenterStart)
                                    .padding(start = 16.dp),
                                value = passwordText,
                                textStyle = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = Gray900
                                ),
                                maxLines = 1,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Done
                                ),
                                visualTransformation = PasswordVisualTransformation(),
                                onValueChange = { setPassword(it) },
                                decorationBox = { innerTextField ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    ) {
                                        if (passwordText.isEmpty()) {
                                            Text(
                                                modifier = Modifier
                                                    .align(Alignment.CenterStart),
                                                text = "비밀번호를 입력해 주세요.",
                                                fontSize = 18.sp,
                                                color = Gray500
                                            )
                                        }
                                        Row(
                                            modifier = Modifier
                                                .fillMaxHeight(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            innerTextField()
                                        }
                                    }
                                },
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.padding(bottom = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(modifier = Modifier.padding(bottom = 10.dp, top = 10.dp)) {
                            Text(
                                modifier = Modifier
                                    .clickable {
                                        setShowBottomSheet(false)
                                        onNavigateToRoute(Screen.SignUp.route)
                                    },
                                text = "회원가입",
                                color = Blue300,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = " | ",
                                color = Gray300
                            )
                            Text(
                                text = "이메일 찾기",
                                color = Gray700,
                                modifier = Modifier.clickable {
                                    setShowBottomSheet(false)
                                    onNavigateToRoute(Screen.FindEmail.route)
                                }
                            )
                            Text(
                                text = " | ",
                                color = Gray300
                            )
                            Text(
                                text = "비밀번호 재설정",
                                color = Gray700,
                                modifier = Modifier.clickable {
                                    setShowBottomSheet(false)
                                    onNavigateToRoute(Screen.ResetPassword.route)
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        DustBottomButton(
                            onClick = {
                                scope.launch {
                                    setShowBottomSheet(false)
//                                onNavigateToRoute(Screen.Main.route)
//                                viewModel.saveUserGrade("teacher//////")
                                    viewModel.loginUser(emailText, passwordText, onNavigateToRoute)
                                }
                            }, text = "로그인", isValid = emailPattern && emailText.isNotEmpty()
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    )
}

@Composable
fun TypeButton(
    onNavigateToRoute: (String) -> Unit,
    signUpViewModel: IntroViewModel,
    type: String
) {
    val scope = rememberCoroutineScope()
    OutlinedButton(
        onClick = {
            when (type) {
                "초등학생" -> {
                    scope.launch {
                        signUpViewModel.saveUserGrade("elementary//////")
                        signUpViewModel.saveSurvey("")
                        onNavigateToRoute(Screen.Main.route)
                    }
                }

                "고등학생" -> {
                    scope.launch {
                        signUpViewModel.saveUserGrade("high//////")
                        signUpViewModel.saveSurvey("")
                        onNavigateToRoute(Screen.Main.route)
                    }
                }

                "중학생" -> scope.launch {
                    signUpViewModel.saveUserGrade("middle//////")
                    signUpViewModel.saveSurvey("")
                    onNavigateToRoute(Screen.Main.route)
                }
            }

        },
        modifier = Modifier
            .fillMaxWidth()
            .height(62.dp)
            .padding(bottom = 7.dp),
        border = BorderStroke(
            1.5.dp,
            color = Gray300
        ),
        colors = ButtonDefaults.buttonColors(Color.White),
        shape = RoundedCornerShape(
            10.dp,
        )
    ) {
        Text(
            text = type,
            color = Gray900,
        )
    }
}

@Preview
@Composable
fun fasda() {
    IntroScreen(
        onNavigateToRoute = {},
        hiltViewModel()
    )
}