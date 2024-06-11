package com.schoolkeepa.dust.presentation.auth

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.schoolkeepa.dust.Resource
import com.schoolkeepa.dust.data.model.school.SchoolListDto
import com.schoolkeepa.dust.presentation.component.ConfirmDialog
import com.schoolkeepa.dust.presentation.component.DustBottomButton
import com.schoolkeepa.dust.presentation.component.DustBottomSheet
import com.schoolkeepa.dust.presentation.component.DustTopAppBar
import com.schoolkeepa.dust.presentation.manual.rememberImeState
import com.schoolkeepa.dust.presentation.navigation.Screen
import com.schoolkeepa.dust.presentation.setting.CustomDialog
import com.schoolkeepa.dust.ui.theme.Blue300
import com.schoolkeepa.dust.ui.theme.Gray100
import com.schoolkeepa.dust.ui.theme.Gray500
import com.schoolkeepa.dust.ui.theme.Gray900
import com.schoolkeepa.dust.util.CustomToast
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindEmailScreen(
    onNavigateToRoute: (String) -> Unit,
    upPress: () -> Unit,
) {

    val context = LocalContext.current
    val viewModel: FineEmailViewModel = hiltViewModel()

    val scope = rememberCoroutineScope()

    var nameText by remember { mutableStateOf("") }
    var schoolText by remember { mutableStateOf("") }
    var valid by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var bottomSheetState = rememberModalBottomSheetState(false)
    var schoolData = remember { mutableStateOf(SchoolListDto()) }
    var schoolName by remember { mutableStateOf("학교를 입력해 주세요.") }
    var schoolCode by remember { mutableStateOf("") }
    var schoolAddress by remember { mutableStateOf("") }
    var searchText by remember { mutableStateOf("") }

    if (nameText.isNotEmpty() && schoolName != "학교를 입력해 주세요.") {
        valid = true
    }

    val imeState = rememberImeState()
    val scrollState = rememberScrollState()
    var showDialog by remember { mutableStateOf(false) }
    var emailInfo by remember { mutableStateOf("") }
    val customToast = CustomToast(context)
    var showToast by remember { mutableStateOf(false) }

    if (showToast) {
        customToast.MakeText(message = "아래 정보로 등록된 계정이 없습니다.")
    }

    val keyboardController = LocalSoftwareKeyboardController.current


    if (showDialog) {
        ConfirmDialog(
            title = "등록된 이메일",
            message = emailInfo,
            confirm = "확인",
            setShowDialog = {
                showDialog = false
            },
            setConfirmClick = {
                scope.launch {
                    keyboardController!!.hide()
                    showDialog = false
                    upPress()
                }
            }
        )
    }
    LaunchedEffect(imeState.value) {
        if (imeState.value) {
            scrollState.scrollTo(scrollState.maxValue)
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
                .fillMaxSize()
                .imePadding()
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
            ) {
                Text(
                    text = Screen.FindEmail.title,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(
                        start = 24.dp,
                        bottom = 10.dp
                    ),
                    fontSize = 24.sp
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
                            value = nameText,
                            textStyle = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Gray900
                            ),
                            maxLines = 1,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text
                            ),
                            onValueChange = { nameText = it },
                            decorationBox = { innerTextField ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    if (nameText.isEmpty()) {
                                        Text(
                                            modifier = Modifier
                                                .align(Alignment.CenterStart),
                                            text = "이름을 입력해 주세요",
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(61.dp)
                        .padding(
                            horizontal = 24.dp
                        )
                        .background(
                            shape = RoundedCornerShape(14.dp),
                            color = Gray100,
                        )
                        .clip(
                            shape = RoundedCornerShape(14.dp),
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
                        text = schoolName,
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
            }

            DustBottomButton(
                modifier = Modifier.align(Alignment.BottomCenter),
                onClick = {
                    scope.launch {
                        viewModel.findEmail(nameText, schoolName).apply {
                            if (this is Resource.Success) {
                                this.data?.let {
                                    if (it.result == "complete") {
                                        val emailList = it.email.split("@")
                                        emailInfo =
                                            emailList[0][0] + "*".repeat(emailList[0].length - 1) + "@" + emailList[1]
                                        println(emailInfo)
                                        showDialog = true
                                    } else {
                                        showToast = true
                                    }
                                }
                            }
                        }
                    }
                },
                text = "이메일 찾기",
                isValid = valid
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
                    setSchoolAddress = {schoolAddress = it}
                )
            }
        }
    }
}

