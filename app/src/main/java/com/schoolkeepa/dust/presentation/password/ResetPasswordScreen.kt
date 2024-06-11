package com.schoolkeepa.dust.presentation.password

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.schoolkeepa.dust.Resource
import com.schoolkeepa.dust.presentation.component.DustBottomButton
import com.schoolkeepa.dust.presentation.component.DustTopAppBar
import com.schoolkeepa.dust.presentation.navigation.Screen
import com.schoolkeepa.dust.ui.theme.Gray100
import com.schoolkeepa.dust.ui.theme.Gray500
import com.schoolkeepa.dust.ui.theme.Gray900
import com.schoolkeepa.dust.ui.theme.Red300
import com.schoolkeepa.dust.util.CustomToast
import kotlinx.coroutines.launch

@Composable
fun ResetPasswordScreen(
    onNavigateToRoute: (String) -> Unit,
    upPress: () -> Unit,
) {

    val viewModel: ResetPasswordViewModel = hiltViewModel()
    val context = LocalContext.current

    val result by viewModel.registerState.collectAsState()



    var emailText by remember { mutableStateOf("") }
    var valid by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    var isShowToast by remember { mutableStateOf(false) }

    var toastMessage by remember { mutableStateOf("") }

    if (isShowToast) {
        val customToast = CustomToast(context)
        customToast.MakeText(message = toastMessage)
        isShowToast = false
    }

    LaunchedEffect(result){
        if (result.isSuccess.isNotEmpty()){
            toastMessage = "비밀번호 재설정 이메일을 발송했습니다."
            isShowToast = true

        } else if(!result.isError.isNullOrEmpty()){
            toastMessage = "비밀번호 재설정 이메일을 발송을 실패했습니다."
            isShowToast = true
        }
    }

    Scaffold(
        topBar = {
            DustTopAppBar(title = "", upPress = upPress)
        },
    ) {
        Box(
            modifier = Modifier
                .padding(top = it.calculateTopPadding())
                .fillMaxSize().imePadding()
        ) {
            Column {
                Text(
                    text = Screen.ResetPassword.title,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(
                        start = 24.dp,
                        bottom = 10.dp
                    ),
                    fontSize = 24.sp
                )

                Text(
                    text = "가입하신 이메일을 입력 후 비밀번호 재설정을 누르면 입력한 이메일로 비밀번호 재설정 이메일이 전송됩니다.",
                    color = Color(0xFF6B7684),
                    modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 48.dp)
                )

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
                                .height(61.dp)
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
                                keyboardType = KeyboardType.Text
                            ),
                            onValueChange = { emailText = it },
                            decorationBox = { innerTextField ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    if (emailText.isEmpty()) {
                                        Text(
                                            modifier = Modifier
                                                .align(Alignment.CenterStart),
                                            text = "이메일을 입력해 주세요",
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
                        fontSize = 14.sp,
                        text = "유효하지 않은 이메일 형식입니다.", color = Red300
                    )
                    valid = false
                    Spacer(modifier = Modifier.height(5.dp))
                } else if (emailText.isNotEmpty() && emailPattern) {
                    valid = true
                }

                if (emailText.isNotEmpty()) {
                    Text(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        fontSize = 14.sp,
                        text = "이메일을 받지 못했다면 스팸함을 확인해주세요.",
                        color = Color(0xFF6B7684),
                    )
                }

                Spacer(modifier = Modifier.weight(1f))
                DustBottomButton(
                    modifier = Modifier,
                    onClick = {
                        scope.launch {
                            viewModel.sendPasswordResetEmail(emailText.trim())
                        }
                    }, text = "비밀번호 재설정", isValid = valid
                )
            }
        }
    }
}