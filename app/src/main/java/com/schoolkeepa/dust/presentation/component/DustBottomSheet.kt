package com.schoolkeepa.dust.presentation.component

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.schoolkeepa.dust.R
import com.schoolkeepa.dust.data.model.school.SchoolListDto
import com.schoolkeepa.dust.ui.theme.Gray200
import com.schoolkeepa.dust.ui.theme.Gray300
import com.schoolkeepa.dust.ui.theme.Gray500
import com.schoolkeepa.dust.ui.theme.Gray900
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DustBottomSheet(
    modifier: Modifier = Modifier,
    bottomSheetState: SheetState,
    setShowBottomSheet: (Boolean) -> Unit,
    searchText: String,
    setSearchText: (String) -> Unit,
    schoolData: MutableState<SchoolListDto>,
    setSearchSchool: () -> Unit,
    setSchoolName: (String) -> Unit,
    setSchoolCode: (String) -> Unit,
    setSchoolAddress: (String) -> Unit = {},
) {


    val scope = rememberCoroutineScope()

    val focusRequest = remember {
        FocusRequester()
    }

    LaunchedEffect(true) {
        focusRequest.requestFocus()
    }

    LaunchedEffect(searchText) {
        if (searchText.isNotEmpty()) {
            delay(800)
            setSearchSchool()
        }
    }

    ModalBottomSheet(
        modifier = Modifier.fillMaxSize(),
        onDismissRequest = {
            setShowBottomSheet(false)
        },
        sheetState = bottomSheetState,
        containerColor = Color.White,
        dragHandle = null,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                Spacer(modifier = Modifier.height(25.dp))
                Box(
                    modifier = Modifier
                        .height(56.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                            .background(
                                shape = RoundedCornerShape(14.dp),
                                color = Gray200,
                            )
                    ) {
                        BasicTextField(
                            modifier = modifier
                                .height(56.dp)
                                .align(Alignment.CenterStart)
                                .padding(start = 16.dp)
                                .focusRequester(focusRequest),
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
                            onValueChange = { setSearchText(it) },
                            decorationBox = { innerTextField ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    if (searchText.isEmpty()) {
                                        Text(
                                            modifier = Modifier
                                                .align(Alignment.CenterStart)
                                                .padding(start = 24.dp),
                                            text = "학교를 검색해 주세요",
                                            fontSize = 18.sp,
                                            color = Gray500
                                        )
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
                }
                if (schoolData.value.schoolInfo.isEmpty()) {
                    Column {
                    }
                } else {
//                                Text(text = schoolData.value.schoolInfo.toString() + "@@@@@")
                    schoolData.value.schoolInfo.let { schoolList ->
                        LazyColumn() {
                            items(schoolList[1].row.size) { index ->
                                val school = schoolList[1].row[index]
                                Box(
                                    modifier = Modifier.clickable {
                                        setSchoolName(school.SCHUL_NM)
                                        setSchoolCode(school.SD_SCHUL_CODE)
                                        setSchoolAddress(school.ORG_RDNMA)
                                        setShowBottomSheet(false)
                                    }
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .padding(
                                                vertical = 15.dp,
                                                horizontal = 24.dp
                                            )
                                    ) {
                                        Text(
                                            modifier = Modifier.padding(bottom = 10.dp),
                                            text = school.SCHUL_NM,
                                        )
                                        Text(
                                            text = school.ORG_RDNMA,
                                            color = Gray500
                                        )
                                    }
                                    if (index != 0) {
                                        Spacer(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(1.dp)
                                                .background(
                                                    Gray300
                                                )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}