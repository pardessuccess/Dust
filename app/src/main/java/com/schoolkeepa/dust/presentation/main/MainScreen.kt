package com.schoolkeepa.dust.presentation.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.schoolkeepa.dust.DustApplication
import com.schoolkeepa.dust.R
import com.schoolkeepa.dust.Resource
import com.schoolkeepa.dust.data.response.login.UserProfile
import com.schoolkeepa.dust.data.response.post.classroom.ClassStatusRes
import com.schoolkeepa.dust.data.response.post.classroom.Classroom
import com.schoolkeepa.dust.presentation.DustApp
import com.schoolkeepa.dust.presentation.manual.ManualActivity
import com.schoolkeepa.dust.presentation.manual.search
import com.schoolkeepa.dust.presentation.navigation.Screen
import com.schoolkeepa.dust.ui.theme.Blue000
import com.schoolkeepa.dust.ui.theme.Blue100
import com.schoolkeepa.dust.ui.theme.Blue200
import com.schoolkeepa.dust.ui.theme.Blue300
import com.schoolkeepa.dust.ui.theme.Blue400
import com.schoolkeepa.dust.ui.theme.Gray100
import com.schoolkeepa.dust.ui.theme.Gray200
import com.schoolkeepa.dust.ui.theme.Gray300
import com.schoolkeepa.dust.ui.theme.Gray400
import com.schoolkeepa.dust.ui.theme.Gray500
import com.schoolkeepa.dust.ui.theme.Gray600
import com.schoolkeepa.dust.ui.theme.Gray700
import com.schoolkeepa.dust.ui.theme.Gray800
import com.schoolkeepa.dust.ui.theme.Gray900
import com.schoolkeepa.dust.ui.theme.Green300
import com.schoolkeepa.dust.ui.theme.Green400
import com.schoolkeepa.dust.ui.theme.Orange300
import com.schoolkeepa.dust.ui.theme.Red000
import com.schoolkeepa.dust.ui.theme.Red300
import com.schoolkeepa.dust.util.CustomToast
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream
import java.util.Date
import java.util.Random

lateinit var locationCallback: LocationCallback

lateinit var locationProvider: FusedLocationProviderClient

data class DustDay(
    val date: Long, val dayOfWeek: String, val status: Int
)

@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen(
    onNavigateToRoute: (String) -> Unit, viewModel: MainViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()

    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )
    val context = LocalContext.current

    val c = LocalConfiguration.current
    val snackbarHostState = remember { SnackbarHostState() }

    var locationPermission by remember { mutableStateOf(false) }

    locationPermission = locationPermissionsState.allPermissionsGranted

    println("allPermissionsRevoked@@@@@" + locationPermissionsState.allPermissionsGranted)

    println(c.screenHeightDp.toString() + c.screenWidthDp.toString() + "c.screenWidthDp")

    val date = Calendar.getInstance()
    val formatter = SimpleDateFormat("yyyy.MM.dd")
    val application = DustApplication

    val calendarDayOfWeek = date.get(Calendar.DAY_OF_WEEK)
    val daysInMillis = 24 * 60 * 60 * 1000

    var dayOfWeek by remember { mutableStateOf(0) }

    var outsideFine by remember { mutableStateOf("") }
    var outsideUltra by remember { mutableStateOf("") }
    var insideFine by remember { mutableStateOf("") }
    var insideUltra by remember { mutableStateOf("") }

    val surveyStatus by viewModel.surveyData.collectAsState()

    var userGrade by remember { mutableStateOf(viewModel.userDataList.value[3]) }
    var userBan by remember { mutableStateOf(viewModel.userDataList.value[4]) }
    var userSchoolCode by remember { mutableStateOf(viewModel.userDataList.value[6]) }

    val userData = produceState(initialValue = "") {
        value = viewModel.userData.value
    }.value


    var stored by remember { mutableStateOf("") }

    val storeData = application.preferences.getString(formatter.format(date), "")

    if (storeData.isNotEmpty()) {
        val a = storeData.split("/")
        if (a[0] != a[1]) {
            stored = storeData
        }
        println("stored" + stored)
    } else {
        println("stored" + stored)
    }

    val dList = arrayListOf(0, 0, 0, 0, 0)

    dayOfWeek = calendarDayOfWeek
    print("$dayOfWeek@@@@@")
    var days = when (dayOfWeek) {
        2 -> {
            if (surveyStatus.contains(formatter.format(date))) {
                dList[0] = 1
            }
//
//            for (i in 1..4) {
//                dList[i] = 4
//            }

            listOf(
                DustDay(Date(date.time.time - daysInMillis).time, "일", 6),
                DustDay(Date(date.time.time).time, "월", dList[0]),
                DustDay(Date(date.time.time + daysInMillis).time, "화", 4),
//                DustDay(Date(date.time.time + daysInMillis * 2).time, "수", dList[2]),
//                DustDay(Date(date.time.time + daysInMillis * 3).time, "목", dList[3]),
//                DustDay(Date(date.time.time + daysInMillis * 4).time, "금", dList[4]),
//                DustDay(Date(date.time.time + daysInMillis * 4).time, "", 5),
            )
        }

        3 -> {
            if (surveyStatus.contains(formatter.format(date.time.time - daysInMillis))) {
                dList[0] = 3
            } else {
                dList[0] = 2
            }
            if (surveyStatus.contains(formatter.format(date))) {
                dList[1] = 1
            }
            for (i in dayOfWeek - 1..4) {
                dList[i] = 4
            }
            listOf(
                DustDay(Date(date.time.time - daysInMillis).time, "월", dList[0]),
                DustDay(Date(date.time.time - daysInMillis * 0).time, "화", dList[1]),
                DustDay(Date(date.time.time + daysInMillis).time, "수", dList[2]),
//                DustDay(Date(date.time.time + daysInMillis * 2).time, "목", dList[3]),
//                DustDay(Date(date.time.time + daysInMillis * 3).time, "금", dList[4]),
//                DustDay(Date(date.time.time + daysInMillis * 3).time, "", 5),
            )
        }

        4 -> {
            if (surveyStatus.contains(formatter.format(date.time.time - daysInMillis * 2))) {
                dList[0] = 3
            } else {
                dList[0] = 2
            }
            if (surveyStatus.contains(formatter.format(date.time.time - daysInMillis))) {
                dList[1] = 3
            } else {
                dList[1] = 2
            }
            if (surveyStatus.contains(formatter.format(date))) {
                dList[2] = 1
            }
            for (i in dayOfWeek - 1..4) {
                dList[i] = 4
            }
            listOf(
//                DustDay(Date(date.time.time - daysInMillis * 2).time, "월", dList[0]),
                DustDay(Date(date.time.time - daysInMillis).time, "화", dList[1]),
                DustDay(Date(date.time.time).time, "수", dList[2]),
                DustDay(Date(date.time.time + daysInMillis).time, "목", dList[3]),
//                DustDay(Date(date.time.time + daysInMillis * 2).time, "금", dList[4]),
//                DustDay(Date(date.time.time + daysInMillis * 2).time, "", 5),
            )
        }

        5 -> {
            if (surveyStatus.contains(formatter.format(date.time.time - daysInMillis * 3))) {
                dList[0] = 3
            } else {
                dList[0] = 2
            }
            if (surveyStatus.contains(formatter.format(date.time.time - daysInMillis * 2))) {
                dList[1] = 3
            } else {
                dList[1] = 2
            }
            if (surveyStatus.contains(formatter.format(date.time.time - daysInMillis))) {
                dList[2] = 3
            } else {
                dList[2] = 2
            }
            if (surveyStatus.contains(formatter.format(date))) {
                dList[3] = 1
            }
            for (i in dayOfWeek - 1..4) {
                dList[i] = 4
            }
            listOf(
                DustDay(Date(date.time.time - daysInMillis).time, "수", dList[2]),
                DustDay(Date(date.time.time).time, "목", dList[3]),
                DustDay(Date(date.time.time + daysInMillis).time, "금", dList[4]),
            )
        }

        6 -> {
            if (surveyStatus.contains(formatter.format(date.time.time - daysInMillis * 4))) {
                dList[0] = 3
            } else {
                dList[0] = 2
            }
            if (surveyStatus.contains(formatter.format(date.time.time - daysInMillis * 3))) {
                dList[1] = 3
            } else {
                dList[1] = 2
            }
            if (surveyStatus.contains(formatter.format(date.time.time - daysInMillis * 2))) {
                dList[2] = 3
            } else {
                dList[2] = 2
            }
            if (surveyStatus.contains(formatter.format(date.time.time - daysInMillis))) {
                dList[3] = 3
            } else {
                dList[3] = 2
            }
            if (surveyStatus.contains(formatter.format(date))) {
                dList[4] = 1
            }
            listOf(
//                DustDay(Date(date.time.time - daysInMillis * 4).time, "월", dList[0]),
//                DustDay(Date(date.time.time - daysInMillis * 3).time, "화", dList[1]),
//                DustDay(Date(date.time.time - daysInMillis * 2).time, "수", dList[2]),
                DustDay(Date(date.time.time - daysInMillis).time, "목", dList[3]),
                DustDay(Date(date.time.time).time, "금", dList[4]),
                DustDay(Date(date.time.time + daysInMillis * 1).time, "", 7),
            )
        }

        7 -> {
            if (surveyStatus.contains(formatter.format(date.time.time - daysInMillis * 5))) {
                dList[0] = 3
            } else {
                dList[0] = 2
            }
            if (surveyStatus.contains(formatter.format(date.time.time - daysInMillis * 4))) {
                dList[1] = 3
            } else {
                dList[1] = 2
            }
            if (surveyStatus.contains(formatter.format(date.time.time - daysInMillis * 3))) {
                dList[2] = 3
            } else {
                dList[2] = 2
            }
            if (surveyStatus.contains(formatter.format(date.time.time - daysInMillis * 2))) {
                dList[3] = 3
            } else {
                dList[3] = 2
            }
            if (surveyStatus.contains(formatter.format(date.time.time - daysInMillis))) {
                dList[4] = 3
            } else {
                dList[4] = 2
            }
            listOf(
//                DustDay(Date(date.time.time - daysInMillis * 5).time, "월", dList[0]),
//                DustDay(Date(date.time.time - daysInMillis * 4).time, "화", 0),
//                DustDay(Date(date.time.time - daysInMillis * 3).time, "수", 0),
//                DustDay(Date(date.time.time - daysInMillis * 2).time, "목", 0),
                DustDay(Date(date.time.time - daysInMillis * 1).time, "금", dList[4]),
                DustDay(Date(date.time.time - daysInMillis).time, "", 5),
                DustDay(Date(date.time.time + daysInMillis * 1).time, "", 7),
            )
        }

        else -> {
            if (surveyStatus.contains(formatter.format(date.time.time - daysInMillis * 6))) {
                dList[0] = 3
            } else {
                dList[0] = 2
            }
            if (surveyStatus.contains(formatter.format(date.time.time - daysInMillis * 5))) {
                dList[1] = 3
            } else {
                dList[1] = 2
            }
            if (surveyStatus.contains(formatter.format(date.time.time - daysInMillis * 4))) {
                dList[2] = 3
            } else {
                dList[2] = 2
            }
            if (surveyStatus.contains(formatter.format(date.time.time - daysInMillis * 3))) {
                dList[3] = 3
            } else {
                dList[3] = 2
            }
            if (surveyStatus.contains(formatter.format(date.time.time - daysInMillis * 2))) {
                dList[4] = 3
            } else {
                dList[4] = 2
            }

            listOf(
//                DustDay(Date(date.time.time - daysInMillis * 6).time, "월", dList[0]),
//                DustDay(Date(date.time.time - daysInMillis * 5).time, "화", dList[1]),
//                DustDay(Date(date.time.time - daysInMillis * 4).time, "수", dList[2]),
//                DustDay(Date(date.time.time - daysInMillis * 3).time, "목", dList[3]),
//                DustDay(Date(date.time.time - daysInMillis * 2).time, "금", dList[4]),
//                DustDay(Date(date.time.time - daysInMillis * 1).time, "금", dList[4]),
                DustDay(Date(date.time.time - daysInMillis * 1).time, "", 6),
                DustDay(Date(date.time.time - daysInMillis * 0).time, "", 5),
                DustDay(Date(date.time.time + daysInMillis * 1).time, "월", 4),
            )
        }
    }


    var humidity by remember { mutableStateOf("") }
    var temperature by remember { mutableStateOf("") }


    var currentUserLocation by remember { mutableStateOf(LatLng()) }

    locationProvider = LocationServices.getFusedLocationProviderClient(context)
    locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            for (location in result.locations) {
                println("LOCATION PROVIDER @@@@@@@")
                currentUserLocation = LatLng(location.latitude, location.longitude)
            }
        }
    }

    LaunchedEffect(true) {
        if (userSchoolCode.isNotEmpty()) {
            viewModel.getOutsideFineStatus(userSchoolCode).apply {
                println("FINESTATUSDTO " + this.data.toString())
                if (this is Resource.Success) {
                    this.data?.let {
                        outsideFine = it.fine_status
                        outsideUltra = it.ultra_status
                    }
                }
            }
        }
    }

    var fs by remember { mutableStateOf(-1) }
    var ufs by remember { mutableStateOf(-1) }


    println("@@@@@@@@@" + fs + ufs)
    var dustChange by remember { mutableStateOf(false) }
    var insideFineInt by remember { mutableStateOf("0") }
    var insideUltraInt by remember { mutableStateOf("0") }
    LaunchedEffect(dustChange) {
        viewModel.getUserData()
        println(userBan + userGrade + userSchoolCode)
        if (userSchoolCode.isNotEmpty()) {
            viewModel.getClassFine(userSchoolCode, userGrade, userBan).apply {
                println("INSIDE FINESTATUSDTO " + this.data.toString())
                if (this is Resource.Success) {
                    this.data?.let {
                        insideFine = it.fine_status
                        insideUltra = it.ultra_status
                        insideFineInt = it.finedust_factor
                        insideUltraInt = it.ultrafine_factor
                    }
                }
            }
        }
        if (insideFineInt.isNotEmpty()) {
            (if (insideFineInt.toInt() >= 150) {
                4
            } else if (insideFineInt.toInt() >= 81) {
                3
            } else if (insideFineInt.toInt() >= 31) {
                2
            } else {
                1
            }).also { fs = it }
        }

        if (insideUltraInt.isNotEmpty()) {
            (if (insideUltraInt.isNotEmpty() && insideUltraInt.toInt() >= 75) {
                4
            } else if (insideUltraInt.toInt() >= 36) {
                3
            } else if (insideUltraInt.toInt() >= 16) {
                2
            } else {
                1
            }).also { ufs = it }
        }
    }



    LaunchedEffect(locationPermission) {

        viewModel.getUserInfoSaved()
        viewModel.setSurveyStatus()

        locationProvider.lastLocation.addOnSuccessListener { location ->
            val formatter = SimpleDateFormat("yyyyMMdd-HHmm")
            val current = formatter.format(date)
            println("CURRENT @@@@@@@ $current")
            currentUserLocation = LatLng(location.latitude, location.longitude)
            println(currentUserLocation.toString() + current + "current@@@@@")
            scope.launch {
                val weatherDto = viewModel.getCurrentWeather(
                    currentUserLocation.latitude.toString(),
                    currentUserLocation.longitude.toString(),
                    current
                )
                println(weatherDto.data.toString() + "@@@@@")
                if (weatherDto is Resource.Success) {
                    if (weatherDto.data!!.result == "complete") {
                        humidity = weatherDto.data.humidity
                        temperature = weatherDto.data.temperature
                    }
                }
            }
        }
    }


    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {
        Column(
            modifier = Modifier
                .background(Blue100)
                .padding(top = it.calculateTopPadding()),
        ) {
            SearchSection(
                onNavigateToRoute = onNavigateToRoute,
                modifier = Modifier.height(80.dp),
                viewModel.userType.value
            )
            val passingDate = Calendar.getInstance()
            val formatter2 = SimpleDateFormat("yyyy.MM.dd")
            val day = formatter2.format(passingDate)
            if (viewModel.userType.value == "teacher") {
                TeacherSection(
                    Modifier
                        .height(450.dp)
                        .padding(bottom = 7.dp),
                    viewModel = viewModel,
                    userData = userData,
                    day = day,
                    days = days,
                    onNavigateToRoute = onNavigateToRoute,
                    notSaved = viewModel.isUserInfo.value,
                    dustChange = dustChange,
                    setDustChange = { dustChange = it },
                    insideFine = fs,
                    insideUltra = ufs,
                    stored = stored,
                    setFine = {
                        fs = it
                        println("@@@@@@@@@" + fs)
                    },
                    setUltra = {
                        ufs = it
                        println("@@@@@@@@@" + ufs)
                    }
                )
            } else {
                println(days.toString() + "@@@@@@@" + dayOfWeek.toString())
                MainSection(
                    Modifier
                        .wrapContentHeight()
                        .padding(bottom = 7.dp),
                    days = days,
                    dayOfWeek = dayOfWeek,
                    day = day,
                    onNavigateToRoute = onNavigateToRoute,
                    notSaved = viewModel.isUserInfo.value,
                    dayOfWeek - 2,
                    stored = stored
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            TodaySection(
                Modifier.weight(1f),
                humidity = humidity,
                temperature = temperature,
                date = date.time,
                outsideFine,
                outsideUltra,
                insideFine,
                insideUltra,
                locationPermissionsState,
                locationPermission,
                userSchoolCode
            )
        }
    }
}

@Preview
@Composable
fun safad() {
//    SearchSection({})
}

@Composable
fun SearchSection(
    onNavigateToRoute: (String) -> Unit,
    modifier: Modifier,
    userType: String,
) {
    val context = LocalContext.current

    var searchText by remember { mutableStateOf("") }
    val placeholder = "미세먼지에 대해 검색해 보세요."


    var isShowToast by remember { mutableStateOf(false) }

    if (isShowToast) {
        val customToast = CustomToast(context)
        var msg = ""
        msg = if (searchText.length < 8) {
            searchText
        } else {
            searchText.substring(0, 8) + "..."
        }
        customToast.MakeText(message = "'$msg' 검색 자료가 없습니다.")
        isShowToast = false
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        modifier = modifier
            .padding(
                start = 16.dp,
                end = 16.dp,
            )
            .fillMaxHeight()
            .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .background(
                    shape = RoundedCornerShape(14.dp),
                    color = Color.White,
                )
        ) {
            BasicTextField(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 16.dp),
                value = searchText,
                textStyle = TextStyle(
                    fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Gray900
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(onSearch = {
                    keyboardController?.hide()
                    searchInPdf(
                        context = context,
                        userType = userType,
                        searchText = searchText
                    ) {
                        isShowToast = it
                    }
                }),
                maxLines = 1,
                singleLine = true,
                onValueChange = { searchText = it },
                decorationBox = { innerTextField ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_search),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize(),
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
                                    com.schoolkeepa.dust.presentation.manual.searchInPdf(
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
//                        Box(
//                            modifier = Modifier.fillMaxWidth()
//                        ) {
//                            if (searchText.isEmpty()) {
//                                Text(
//                                    text = placeholder, fontSize = 16.sp, color = Gray500
//                                )
//                            }
//                            innerTextField()
//                        }
                    }
                },
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Box(modifier = Modifier
            .height(48.dp)
            .width(48.dp)
            .background(
                shape = RoundedCornerShape(15.dp), color = Blue200
            )
            .clip(
                RoundedCornerShape(15.dp)
            )
            .clickable { onNavigateToRoute(Screen.Setting.route) }) {
            Image(
                painterResource(id = R.drawable.ic_settings),
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(24.dp),
                contentDescription = null
            )
        }
    }
}

@Composable
fun CardComponent(
    cardType: Int,
    cardWidth: Int,
    cardHeight: Int,
    date: String,
    day: String,
    dayOfWeek: String,
    notSaved: Boolean,
    onNavigateToRoute: (String) -> Unit,
    dragColor: Boolean = false,
    stored: String = ""
) {
    Box(
        modifier = Modifier
            .width(cardWidth.dp)
            .height(cardHeight.dp)
            .padding(horizontal = 4.dp)
            .background(
                color = when (cardType) {
                    0 -> {
                        Blue300
                    }

                    1 -> {
                        Blue300
                    }

                    2 -> {
                        if (dragColor) {
                            Gray600
                        } else {
                            Gray600.copy(0.3f)
                        }
                    }

                    3 -> {
                        if (dragColor) {
                            Green300
                        } else {
                            Green300.copy(0.3f)
                        }
                    }

                    4 -> {
                        Gray200
                    }

                    5 -> {
                        Blue300
                    }

                    6 -> {
                        if (dragColor) {
                            Green300
                        } else {
                            Green300.copy(0.3f)
                        }
                    }

                    7 -> {
                        Gray200
                    }

                    else -> {
                        Gray200
                    }
                }, shape = RoundedCornerShape(20.dp)
            ),
    ) {
        Text(
            modifier = Modifier.padding(top = 15.dp, start = 15.dp),
            fontWeight = FontWeight.Bold,
            color = when (cardType) {
                0, 1, 2, 3, 5, 6 -> {
                    Color.White
                }

                7 -> {
                    Gray700
                }

                else -> {
                    Gray500
                }
            },
            text = when (cardType) {
                5, 6, 7 -> {
                    "$date (공휴일)"
                }

                else -> {
                    date
                }
            },
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box() {
                Image(
                    modifier = Modifier
                        .size((cardWidth / 17 * 11).dp)
                        .padding(12.dp),
                    painter = painterResource(
                        id = when (cardType) {
                            0 -> R.drawable.img_main_empty_dust
                            1 -> random()
                            2 -> randomNegative()
                            3 -> random()
                            4 -> R.drawable.img_main_empty_dust
                            5, 6, 7 -> R.drawable.img_holiday2
                            else -> R.drawable.img_main_empty_dust
                        }
                    ),
                    contentDescription = null
                )
                when (cardType) {
                    0, 4 -> {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = dayOfWeek,
                            fontSize = 36.sp,
                            color = when (cardType) {
                                0 -> {
                                    Color.White
                                }

                                else -> {
                                    Gray400
                                }
                            }
                        )
                    }
                }
            }
        }
        TextButton(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp, bottom = 10.dp)
            .background(
                color = when (cardType) {
                    0 -> {
                        Blue400
                    }

                    1 -> {
                        Blue400.copy(0.2f)
                    }

                    2 -> {
                        Gray800.copy(0.2f)
                    }

                    3 -> {
                        Green400.copy(0.2f)
                    }

                    4 -> {
                        Gray800.copy(0.2f)
                    }

                    5 -> {
                        Blue300
                    }

                    6 -> {
                        Green300
                    }

                    7 -> {
                        Gray200
                    }

                    else -> {
                        Gray200
                    }
                }, shape = RoundedCornerShape(50)
            )
            .height(44.dp)
            .align(Alignment.BottomCenter), enabled = cardType == 0, onClick = {
            if (!notSaved) {
                onNavigateToRoute(
                    Screen.SurveyMain.route + "/" + day
                )
            } else {
                onNavigateToRoute(
                    Screen.Survey.route + "/" + day
                )
            }
        }) {
            if (cardType == 1 || cardType == 3) {
                Icon(
                    tint = Color.White.copy(0.2f),
                    modifier = Modifier.padding(end = 5.dp),
                    painter = painterResource(id = R.drawable.ic_check),
                    contentDescription = null
                )
            }
            if (stored.isEmpty()) {
                Text(
                    fontSize = 16.sp,
                    color = when (cardType) {
                        1, 2, 3 -> Color.White.copy(0.2f)
                        7 -> Gray600
                        else -> Color.White
                    },
                    text = when (cardType) {
                        0 -> "설문조사 시작하기"
                        1 -> "설문 완료!"
                        2 -> "설문 실패"
                        3 -> "설문 완료!"
                        4 -> "설문 준비중"
                        5 -> "쉬어 갈게요!"
                        6 -> "쉬어 갈게요!"
                        7 -> "쉬어 갈게요!"
                        else -> ""
                    }
                )
            } else {
                Row {
                    Text(text = "설문 진행중 ", color = Color.White, fontSize = 16.sp)
                    Text(
                        text = "(" + stored + ")",
                        color = Color.White.copy(0.5f),
                        fontSize = 16.sp,
                    )
                }
            }
        }
    }

}

@Composable
fun NotToday(
    cardWidth: Int,
    cardHeight: Int,
    date: String,
    day: String,
    dayOfWeek: String,
    notSaved: Boolean,
    onNavigateToRoute: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .width(cardWidth.dp)
            .height(cardHeight.dp)
            .padding(horizontal = 5.dp)
            .background(
                color = Blue300, shape = RoundedCornerShape(20.dp)
            ),
    ) {
        Text(
            modifier = Modifier.padding(top = 15.dp, start = 15.dp),
            fontWeight = FontWeight.Bold,
            color = Color.White,
            text = date,
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box() {
                Image(
                    modifier = Modifier
                        .size((cardWidth / 17 * 11).dp)
                        .padding(12.dp),
                    painter = painterResource(id = R.drawable.img_main_empty_dust),
                    contentDescription = null
                )
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = dayOfWeek,
                    fontSize = 36.sp,
                    color = Gray100
                )
            }
        }
        TextButton(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp, bottom = 10.dp)
            .background(
                color = Blue400, shape = RoundedCornerShape(50)
            )
            .height(44.dp)
            .align(Alignment.BottomCenter), onClick = {
            if (!notSaved) {
                onNavigateToRoute(
                    Screen.SurveyMain.route + "/" + day
                )
            } else {
                onNavigateToRoute(
                    Screen.Survey.route + "/" + day
                )
            }
        }) {
            Text(
                color = Color.White,
                text = "설문조사 시작하기",
            )
        }
    }
}

fun random(): Int {
    return when (Random().nextInt(6)) {
        0 -> {
            R.drawable.img_done1
        }

        1 -> {
            R.drawable.img_done2
        }

        2 -> {
            R.drawable.img_done3
        }

        3 -> {
            R.drawable.img_done4
        }

        4 -> {
            R.drawable.img_done5
        }

        else -> {
            R.drawable.img_done6
        }
    }
}

@Composable
fun OkToday(
    date: String, dayOfWeek: String, notSaved: Boolean, onNavigateToRoute: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .width(192.dp)
            .height(232.dp)
            .padding(horizontal = 5.dp)
            .background(
                color = Blue300, shape = RoundedCornerShape(20.dp)
            ),
    ) {
        Text(
            modifier = Modifier.padding(top = 15.dp, start = 15.dp),
            fontWeight = FontWeight.Bold,
            color = Color.White,
            text = date,
        )
        Column(
            modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box() {
                Image(
                    modifier = Modifier.size(132.dp),
                    painter = painterResource(id = random()),
                    contentDescription = null
                )
//                Text(
//                    modifier = Modifier.align(Alignment.Center),
//                    text = dayOfWeek,
//                    fontSize = 36.sp,
//                    color = Gray100
//                )
            }
        }
        TextButton(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
            .background(
                color = Blue400.copy(0.2f), shape = RoundedCornerShape(50)
            ), onClick = {}) {
            Row() {
//                Icon(
//                    painter = painterResource(id = R.drawable.icon_ok_today_check),
//                    contentDescription = null
//                )
                Text(
                    modifier = Modifier,
                    color = Color.White.copy(0.2f),
                    text = "설문 완료!",
                )
            }
        }
    }
}

fun randomNegative(): Int {
    return when (Random().nextInt(3)) {
        0 -> {
            R.drawable.img_not_done3
        }

        1 -> {
            R.drawable.img_not_done2
        }

        else -> {
            R.drawable.img_not_done1
        }
    }
}

@Composable
fun MainSection(
    modifier: Modifier,
    days: List<DustDay>,
    dayOfWeek: Int,
    day: String,
    onNavigateToRoute: (String) -> Unit,
    notSaved: Boolean,
    today: Int,
    stored: String,
) {
    val formatter = SimpleDateFormat("M월 d일")


//    println("@@@@@DAYS $days")

    val lazyListState = rememberLazyListState(1, -60)

    val scope = rememberCoroutineScope()
//    println(lazyListState.isScrollInProgress.toString())
    val isFocused by lazyListState.interactionSource.interactions
        .distinctUntilChanged()
        .filterIsInstance<DragInteraction>()
        .map { dragInteraction ->
            println(lazyListState.firstVisibleItemIndex.toString() + " " + lazyListState.firstVisibleItemScrollOffset.toString())
            dragInteraction is DragInteraction.Start
        }
        .collectAsState(false)
    var dragColor by remember { mutableStateOf(false) }

    LaunchedEffect(isFocused) {
        lazyListState.animateScrollToItem(1, scrollOffset = -60)
        if (!isFocused) {
            dragColor = false
        }
    }

    if (isFocused) {
        if (lazyListState.firstVisibleItemIndex == 0 && lazyListState.firstVisibleItemScrollOffset < 400) {
            dragColor = true
        } else {
            dragColor = false
        }
    }


    val configuration = LocalConfiguration.current

    val cardWidth = configuration.screenWidthDp * 55 / 100
    val cardHeight = cardWidth * 100 / 82
//    println("$cardWidth @@@@@@ $cardHeight")

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                shape = RoundedCornerShape(30.dp), color = Color.White
            ),
    ) {
        Column() {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 22.dp)
            ) {
                Text(
                    text = "오늘도 청소했다면?",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "설문조사를 완료해 주세요!",
                    fontSize = 20.sp,
                )
            }
            LazyRow(
                state = lazyListState, modifier = Modifier.height(254.dp)
            ) {
                items(days.size) { index ->
                    val dayInfo = days[index]
                    if (index == 0) {
                        Spacer(modifier = Modifier.width(24.dp))
                    }
                    when (dayInfo.status) {
                        0 -> CardComponent(
                            cardType = dayInfo.status,
                            cardWidth = cardWidth,
                            cardHeight = cardHeight,
                            date = formatter.format(dayInfo.date),
                            day = day,
                            dayOfWeek = dayInfo.dayOfWeek,
                            notSaved = notSaved,
                            onNavigateToRoute = onNavigateToRoute,
                            stored = stored
                        ) //0 not today

                        1 -> CardComponent(
                            cardType = dayInfo.status,
                            cardWidth = cardWidth,
                            cardHeight = cardHeight,
                            date = formatter.format(dayInfo.date),
                            day = day,
                            dayOfWeek = dayInfo.dayOfWeek,
                            notSaved = notSaved,
                            onNavigateToRoute = onNavigateToRoute
                        ) // 1 yes today

                        2 -> CardComponent(
                            cardType = dayInfo.status,
                            cardWidth = cardWidth,
                            cardHeight = cardHeight,
                            date = formatter.format(dayInfo.date),
                            day = day,
                            dayOfWeek = dayInfo.dayOfWeek,
                            notSaved = notSaved,
                            onNavigateToRoute = onNavigateToRoute,
                            dragColor
                        ) //2 not yesterday


                        3 -> CardComponent(
                            cardType = dayInfo.status,
                            cardWidth = cardWidth,
                            cardHeight = cardHeight,
                            date = formatter.format(dayInfo.date),
                            day = day,
                            dayOfWeek = dayInfo.dayOfWeek,
                            notSaved = notSaved,
                            onNavigateToRoute = onNavigateToRoute,
                            dragColor
                        ) //3 ok yesterday


                        4 -> CardComponent(
                            cardType = dayInfo.status,
                            cardWidth = cardWidth,
                            cardHeight = cardHeight,
                            date = formatter.format(dayInfo.date),
                            day = day,
                            dayOfWeek = dayInfo.dayOfWeek,
                            notSaved = notSaved,
                            onNavigateToRoute = onNavigateToRoute
                        ) // 4 future

                        5 -> CardComponent(
                            cardType = dayInfo.status,
                            cardWidth = cardWidth,
                            cardHeight = cardHeight,
                            date = formatter.format(dayInfo.date),
                            day = day,
                            dayOfWeek = dayInfo.dayOfWeek,
                            notSaved = notSaved,
                            onNavigateToRoute = onNavigateToRoute
                        ) // 5 today holiday

                        6 -> CardComponent(
                            cardType = dayInfo.status,
                            cardWidth = cardWidth,
                            cardHeight = cardHeight,
                            date = formatter.format(dayInfo.date),
                            day = day,
                            dayOfWeek = dayInfo.dayOfWeek,
                            notSaved = notSaved,
                            onNavigateToRoute = onNavigateToRoute,
                            dragColor
                        ) // 6 past holiday

                        7 -> CardComponent(
                            cardType = dayInfo.status,
                            cardWidth = cardWidth,
                            cardHeight = cardHeight,
                            date = formatter.format(dayInfo.date),
                            day = day,
                            dayOfWeek = dayInfo.dayOfWeek,
                            notSaved = notSaved,
                            onNavigateToRoute = onNavigateToRoute
                        ) //7 future holiday
                    }
                    if (index == 2) {
                        Spacer(modifier = Modifier.width(24.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(horizontal = 24.dp)
                    .clip(
                        RoundedCornerShape(14.dp)
                    )
                    .clickable { onNavigateToRoute("main/manual") },
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .background(
                            shape = RoundedCornerShape(14.dp), color = Green300
                        )
                        .clip(
                            RoundedCornerShape(14.dp)
                        )
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier, text = "📚 한 눈에 보는 ", color = Color.White
                    )
                    Text(
                        text = "미세먼지 매뉴얼", fontWeight = FontWeight.Bold, color = Color.White
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_manul_right_arrow),
                        tint = Color.White,
                        contentDescription = "right arrow icon"
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun MyTabIndicator(
    indicatorWidth: Dp,
    indicatorOffset: Dp,
    indicatorColor: Color,
) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(
                width = indicatorWidth,
            )
            .offset(
                x = indicatorOffset,
            )
            .clip(
                shape = CircleShape,
            )
            .border(
                width = 1.dp, shape = CircleShape, color = Gray300
            )
            .background(
                color = indicatorColor,
            ),
    )
}

@Composable
private fun MyTabItem(
    isSelected: Boolean,
    onClick: () -> Unit,
    tabWidth: Dp,
    text: String,
) {
    val tabTextColor: Color by animateColorAsState(
        targetValue = Color.Black,
        animationSpec = tween(easing = LinearEasing),
        label = "",
    )
    Box(modifier = Modifier.fillMaxHeight()) {
        Text(modifier = Modifier
            .clip(
                CircleShape
            )
            .clickable {
                onClick()
            }
            .width(tabWidth)
            .padding(
                vertical = 10.dp,
                horizontal = 12.dp,
            )
            .align(Alignment.Center),
            text = text,
            color = tabTextColor,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold)
    }
}

@Composable
fun CustomTab(
    selectedItemIndex: Int,
    items: List<String>,
    modifier: Modifier = Modifier,
    onClick: (index: Int) -> Unit,
    tabWidth: Dp = 100.dp,
) {
    val indicatorOffset: Dp by animateDpAsState(
        targetValue = tabWidth * selectedItemIndex,
        animationSpec = tween(easing = LinearEasing),
        label = ""
    )

    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(Gray200),
    ) {
        MyTabIndicator(
            indicatorWidth = tabWidth,
            indicatorOffset = indicatorOffset,
            indicatorColor = Color.White,
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clip(CircleShape),
        ) {
            items.mapIndexed { index, text ->
                val isSelected = index == selectedItemIndex
                MyTabItem(
                    isSelected = isSelected,
                    onClick = {
                        onClick(index)
                    },
                    tabWidth = tabWidth,
                    text = text,
                )
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CircumstanceSection(
    permissions: MultiplePermissionsState,
    locationPermission: Boolean,
    humidity: String,
    temperature: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "오늘의 미세먼지",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.weight(1f))
        if (locationPermission) {
            EnvironmentSection(
                modifier = Modifier.padding(end = 2.dp),
                icon = R.drawable.ic_humidity,
                text = "습도",
                degree = humidity,
            )
            Text("/", color = Gray300)
            EnvironmentSection(
                icon = R.drawable.ic_temperature, text = "온도", degree = temperature,
            )
        } else {
            Image(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(99.dp)
                    )
                    .clickable {
                        permissions.launchMultiplePermissionRequest()
                    }
                    .height(28.dp),
                painter = painterResource(id = R.drawable.ic_location_section),
                contentDescription = null
            )
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun TodaySection(
    modifier: Modifier,
    humidity: String,
    temperature: String,
    date: Date,
    outsideFine: String,
    outsideUltra: String,
    insideFine: String,
    insideUltra: String,
    permissions: MultiplePermissionsState,
    locationPermission: Boolean,
    userSchoolCode: String,
) {

    println("FINESTATUS" + outsideFine + outsideUltra + insideFine + insideUltra)

    val formatter = SimpleDateFormat("yyyy/MM/dd")
    val current = formatter.format(date)
    var tabIndex by remember { mutableStateOf(0) }

    var heightPx by remember { mutableStateOf(0f) }

    var heightDp by remember { mutableStateOf(0.dp) }

    println("heightDp" + heightDp)

    var heightA by remember { mutableStateOf(24.dp) }
    var heightA1 by remember { mutableStateOf(0.dp) }
    var heightA2 by remember { mutableStateOf(0.dp) }
    var heightB by remember { mutableStateOf(34.dp) }
    var refreshButton by remember { mutableStateOf(false) }

    if (heightDp > 242.dp) {
        heightA = 36.dp
        refreshButton = true
        heightA1 = 6.dp
        if (heightDp > 242.dp + heightA1) {
            heightA2 = 20.dp
            if (heightDp > 242.dp + 6.dp + 20.dp) {
                heightB = 44.dp
            }
        }
    }

    val density = LocalDensity.current

    Box(
        modifier = modifier
            .onGloballyPositioned { coordinates ->
                heightPx = coordinates.size.height.toFloat()
                heightDp = with(density) { coordinates.size.height.toDp() }
            }
            .fillMaxWidth()
            .background(
                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp), color = Color.White
            )
            .padding(start = 24.dp, end = 24.dp, top = 24.dp)
    ) {
        println("heightA" + heightA.toString() + "heightB" + heightB.toString() + "heightA2" + heightA2.toString() + "heightA1" + heightA1.toString())
        val screenWidth = LocalConfiguration.current.screenWidthDp.dp - 48.dp
        Column(modifier = Modifier.fillMaxWidth()) {
            CircumstanceSection(
                permissions = permissions,
                locationPermission = locationPermission,
                humidity = humidity,
                temperature = temperature
            )
            Spacer(modifier = Modifier.weight(1f))
            CustomTab(
                tabIndex,
                listOf("학교 내부", "학교 외부"),
                Modifier
                    .fillMaxWidth()
                    .height(heightB),
                onClick = { tabIndex = it },
                tabWidth = screenWidth / 2,
            )
            Spacer(modifier = Modifier.weight(1f))
            if (tabIndex == 0) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(102.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = Blue000,
                                shape = RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp)
                            )
                            .fillMaxWidth(0.5f)
                            .fillMaxHeight(),
                    ) {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.height(42.dp)
                            ) {
                                if (userSchoolCode.isEmpty()) {
                                    Text(
                                        text = "설정에서 학교 정보\n입력 후 확인 가능해요.",
                                        fontSize = 12.sp,
                                        lineHeight = 14.sp,
                                        color = Gray500,
                                        textAlign = TextAlign.Center,
                                    )
                                } else if (insideFine.isEmpty()) {
                                    Text(
                                        text = "아직 등록된 미세먼지\n정보가 없어요.",
                                        fontSize = 12.sp,
                                        lineHeight = 14.sp,
                                        color = Gray500,
                                        textAlign = TextAlign.Center,
                                    )
                                } else {
                                    Image(
                                        painter = painterResource(
                                            when (insideFine) {
                                                "good" -> {
                                                    R.drawable.img_finestatus_good
                                                }

                                                "fine" -> {
                                                    R.drawable.img_finestatus_okay
                                                }

                                                else -> {
                                                    R.drawable.img_finestatus_bad
                                                }
                                            }
                                        ), contentDescription = ""
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        fontWeight = FontWeight.Bold,
                                        color = when (insideFine) {
                                            "good" -> {
                                                Orange300
                                            }

                                            "fine" -> {
                                                Orange300
                                            }

                                            else -> {
                                                Gray600
                                            }
                                        },
                                        text = when (insideFine) {
                                            "good" -> {
                                                "좋아요!"
                                            }

                                            "fine" -> {
                                                "괜찮아요"
                                            }

                                            else -> {
                                                "안 좋아요"
                                            }
                                        }
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = "미세먼지",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Gray900
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .background(
                                color = Red000,
                                shape = RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp)
                            )
                            .fillMaxWidth(1f)
                            .fillMaxHeight()
                            .padding()
                    ) {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.height(42.dp)
                            ) {
                                if (userSchoolCode.isEmpty()) {
                                    Text(
                                        text = "설정에서 학교 정보\n입력 후 확인 가능해요.",
                                        fontSize = 12.sp,
                                        lineHeight = 14.sp,
                                        color = Gray500,
                                        textAlign = TextAlign.Center,
                                    )
                                } else if (insideFine.isEmpty()) {
                                    Text(
                                        text = "아직 등록된 미세먼지\n정보가 없어요.",
                                        fontSize = 12.sp,
                                        lineHeight = 14.sp,
                                        color = Gray500,
                                        textAlign = TextAlign.Center,
                                    )
                                } else {
                                    Image(
                                        painter = painterResource(
                                            when (insideUltra) {
                                                "good" -> {
                                                    R.drawable.img_finestatus_good
                                                }

                                                "fine" -> {
                                                    R.drawable.img_finestatus_okay
                                                }

                                                else -> {
                                                    R.drawable.img_finestatus_bad
                                                }
                                            }
                                        ), contentDescription = ""
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        fontWeight = FontWeight.Bold,
                                        color = when (insideUltra) {
                                            "good" -> {
                                                Orange300
                                            }

                                            "fine" -> {
                                                Orange300
                                            }

                                            else -> {
                                                Gray600
                                            }
                                        },
                                        text = when (insideUltra) {
                                            "good" -> {
                                                "좋아요!"
                                            }

                                            "fine" -> {
                                                "괜찮아요"
                                            }

                                            else -> {
                                                "안 좋아요"
                                            }
                                        }
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = "초미세먼지",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Gray900
                            )
                        }
                    }
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(102.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = Blue000,
                                shape = RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp)
                            )
                            .fillMaxWidth(0.5f)
                            .fillMaxHeight(),
                    ) {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.height(42.dp)
                            ) {
                                if (userSchoolCode.isEmpty()) {
                                    Text(
                                        text = "설정에서 학교 정보\n입력 후 확인 가능해요.",
                                        fontSize = 12.sp,
                                        lineHeight = 14.sp,
                                        color = Gray500,
                                        textAlign = TextAlign.Center,
                                    )
                                } else {
                                    Image(
                                        painter = painterResource(
                                            when (outsideFine) {
                                                "good" -> {
                                                    R.drawable.img_finestatus_good
                                                }

                                                "fine" -> {
                                                    R.drawable.img_finestatus_okay
                                                }

                                                else -> {
                                                    R.drawable.img_finestatus_bad
                                                }
                                            }
                                        ), contentDescription = ""
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        fontWeight = FontWeight.Bold,
                                        color = when (outsideFine) {
                                            "good" -> {
                                                Orange300
                                            }

                                            "fine" -> {
                                                Orange300
                                            }

                                            else -> {
                                                Gray600
                                            }
                                        },
                                        text = when (outsideFine) {
                                            "good" -> {
                                                "좋아요!"
                                            }

                                            "fine" -> {
                                                "괜찮아요"
                                            }

                                            else -> {
                                                "안 좋아요"
                                            }
                                        }
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = "미세먼지",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Gray900
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .background(
                                color = Red000,
                                shape = RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp)
                            )
                            .fillMaxWidth(1f)
                            .fillMaxHeight()
                            .padding()
                    ) {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.height(42.dp)
                            ) {
                                if (userSchoolCode.isEmpty()) {
                                    Text(
                                        text = "설정에서 학교 정보\n입력 후 확인 가능해요.",
                                        fontSize = 12.sp,
                                        lineHeight = 14.sp,
                                        color = Gray500,
                                        textAlign = TextAlign.Center,
                                    )
                                } else {
                                    Image(
                                        painter = painterResource(
                                            when (outsideUltra) {
                                                "good" -> {
                                                    R.drawable.img_finestatus_good
                                                }

                                                "fine" -> {
                                                    R.drawable.img_finestatus_okay
                                                }

                                                else -> {
                                                    R.drawable.img_finestatus_bad
                                                }
                                            }
                                        ), contentDescription = ""
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        fontWeight = FontWeight.Bold,
                                        color = when (outsideUltra) {
                                            "good" -> {
                                                Orange300
                                            }

                                            "fine" -> {
                                                Orange300
                                            }

                                            else -> {
                                                Gray600
                                            }
                                        },
                                        text = when (outsideUltra) {
                                            "good" -> {
                                                "좋아요!"
                                            }

                                            "fine" -> {
                                                "괜찮아요"
                                            }

                                            else -> {
                                                "안 좋아요"
                                            }
                                        }
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = "초미세먼지",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Gray900
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(heightA1))
            if (refreshButton) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(37.dp)
                ) {
                    Row(
                        modifier = Modifier.align(Alignment.Center),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = current,
                            color = Gray600
                        )
                        Icon(
                            modifier = Modifier.padding(start = 2.dp),
                            tint = Gray600,
                            painter = painterResource(id = R.drawable.ic_rotate),
                            contentDescription = ""
                        )
                    }
                }
            } else {
                Spacer(modifier = Modifier.height(heightA))
            }
            Spacer(modifier = Modifier.height(heightA2))
        }
    }
}

@Composable
fun EnvironmentSection(
    modifier: Modifier = Modifier, icon: Int, text: String, degree: String,
) {
    Row(
        modifier = modifier
            .height(28.dp)
            .padding(horizontal = 2.dp, vertical = 0.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.padding(top = 1.dp),
            painter = painterResource(icon),
            contentDescription = "$text icon"
        )
        Text(text = text, fontSize = 13.sp)
        Text(
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold, text = " $degree"
        )
    }
}

@Composable
fun TeacherSection(
    modifier: Modifier,
    viewModel: MainViewModel,
    userData: String,
    day: String,
    days: List<DustDay>,
    onNavigateToRoute: (String) -> Unit,
    notSaved: Boolean,
    dustChange: Boolean,
    setDustChange: (Boolean) -> Unit,
    insideFine: Int,
    insideUltra: Int,
    stored: String,
    setFine: (Int) -> Unit,
    setUltra: (Int) -> Unit,
) {

    var fineStatus by remember { mutableStateOf(-1) }
    var ultraFineStatus by remember { mutableStateOf(-1) }

    fineStatus = insideFine
    ultraFineStatus = insideUltra

    var clickCount by remember { mutableStateOf(0) }
    var ultraClickCount by remember { mutableStateOf(0) }

    val fineStatusList = listOf("15", "40", "100", "200")
    val ultraStatusList = listOf("10", "25", "50", "100")

    val fineList = listOf("", "0-30", "31-80", "81-150", "150 이상")
    val ultraFineList = listOf("", "0-15", "16-35", "36-75", "75 이상")

    val data = userData.split("/")


//    println("@@@@@@@@@fineStatus$fineStatus$ultraFineStatus")
//    println("@@@@@@@@@clickCount" + clickCount.toString() + ultraClickCount.toString() + fineStatus + ultraFineStatus)

    val colorList = listOf(Blue300, Blue300, Green300, Orange300, Red300)

    if (clickCount > 0 || ultraClickCount > 0) {
        LaunchedEffect(fineStatus * ultraFineStatus) {
            val classStatusRes = ClassStatusRes(
                Classroom(
                    fineStatusList[fineStatus - 1], ultraStatusList[ultraFineStatus - 1]
                ), UserProfile(
                    class_num = data[4].toInt(),
                    grade = data[3].toInt(),
                    school_code = data[6].toInt(),
                    name = data[1],
                    user_type = "teacher",
                    school_name = data[2],
                    school_address = data[8],
                    teacher_code = data[7]
                )
            )
            viewModel.setClassFine(classStatusRes)
            setDustChange(!dustChange)
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                shape = RoundedCornerShape(30.dp), color = Color.White
            )
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            Text(
                text = "오늘의 미세먼지 정보를",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "입력하고 학생들과 공유해요!",
                fontSize = 22.sp,
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "초미세먼지", fontSize = 14.sp, color = Gray700, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(10.dp))
            Row() {
                ultraFineList.forEachIndexed { index, str ->
                    if (str.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .background(
                                    if (ultraFineStatus == index) {
                                        colorList[index].copy(0.2f)
                                    } else {
                                        Color.White
                                    }, RoundedCornerShape(
                                        40.dp,
                                    )
                                )
                                .clip(
                                    RoundedCornerShape(
                                        40.dp,
                                    )
                                )
                                .border(
                                    BorderStroke(
                                        if (ultraFineStatus == index) {
                                            1.dp
                                        } else {
                                            1.dp
                                        },
                                        color = if (ultraFineStatus == index) {
                                            colorList[index]
                                        } else {
                                            Gray300
                                        },
                                    ), shape = RoundedCornerShape(40.dp)
                                )
                                .clickable {
                                    ultraClickCount++
                                    ultraFineStatus = index
                                    println("@@@@@@@@@" + index)
                                    setUltra(index)
                                },
                        ) {
                            Text(
                                modifier = Modifier.align(Alignment.Center),
                                text = str,
                                color = if (ultraFineStatus == index) {
                                    colorList[index]
                                } else {
                                    Gray800
                                },
                                fontWeight = if (ultraFineStatus == index) {
                                    FontWeight.Bold
                                } else {
                                    FontWeight.Normal
                                }
                            )
                        }
                        if (index != ultraFineList.size - 1) {
                            Spacer(
                                modifier = Modifier.width(
                                    5.dp
                                )
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "미세먼지", fontSize = 14.sp, color = Gray700, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(10.dp))
            Row() {
                fineList.forEachIndexed { index, str ->
                    if (str.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .background(
                                    color = if (fineStatus == index) {
                                        colorList[index].copy(0.2f)
                                    } else {
                                        Color.White
                                    }, RoundedCornerShape(
                                        40.dp,
                                    )
                                )
                                .clip(
                                    RoundedCornerShape(
                                        40.dp,
                                    )
                                )
                                .border(
                                    BorderStroke(
                                        if (fineStatus == index) {
                                            1.dp
                                        } else {
                                            1.dp
                                        },
                                        color = if (fineStatus == index) {
                                            colorList[index]
                                        } else {
                                            Gray300
                                        },
                                    ), shape = RoundedCornerShape(40.dp)
                                )
                                .clickable {
                                    clickCount++
                                    fineStatus = index
                                    println("@@@@@@@@@" + index)
                                    setFine(index)
                                },
                        ) {
                            Text(
                                modifier = Modifier.align(Alignment.Center),
                                text = str,
                                color = if (fineStatus == index) {
                                    colorList[index]
                                } else {
                                    Gray800
                                },
                                fontWeight = if (fineStatus == index) {
                                    FontWeight.Bold
                                } else {
                                    FontWeight.Normal
                                }
                            )
                        }
                        if (index != fineList.size - 1) {
                            Spacer(
                                modifier = Modifier.width(
                                    5.dp
                                )
                            )
                        }
                    }
                }
            }
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(start = 24.dp, bottom = 24.dp, end = 24.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clip(RoundedCornerShape(14.dp)),
            ) {
                when (days[1].status) {
                    0 -> Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .background(
                                shape = RoundedCornerShape(14.dp), color = Blue300
                            )
                            .clip(RoundedCornerShape(14.dp))
                            .padding(horizontal = 20.dp)
                            .clickable(
                                onClick = {
                                    if (stored.isEmpty()) {
                                        if (!notSaved) {
                                            onNavigateToRoute(
                                                Screen.SurveyMain.route + "/" + day
                                            )
                                        } else {
                                            onNavigateToRoute(
                                                Screen.Survey.route + "/" + day
                                            )
                                        }
                                    } else {

                                    }
                                }
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (stored.isEmpty()) {
                            Text(
                                modifier = Modifier, text = "🧹 학교 미세먼지 점검", color = Color.White
                            )
                            Text(
                                text = "설문조사", fontWeight = FontWeight.Bold, color = Color.White
                            )
                        } else {
                            Row {
                                Text(
                                    text = "설문조사 작성 중 ",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = "(" + stored + ")",
                                    color = Color.White.copy(0.5f),
                                    fontSize = 16.sp,
                                )
                            }
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            tint = Color.White,
                            painter = painterResource(id = R.drawable.ic_right_arrow),
                            contentDescription = "right arrow icon"
                        )
                    }

                    1 -> {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .background(
                                    shape = RoundedCornerShape(14.dp), color = Gray200
                                )
                                .clip(RoundedCornerShape(14.dp))
                                .padding(horizontal = 20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier, text = "오늘의 설문조사를 완료했어요!", color = Gray500
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(
                                tint = Gray500,
                                painter = painterResource(id = R.drawable.ic_right_arrow),
                                contentDescription = "right arrow icon"
                            )
                        }
                    }

                    5 -> {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .background(
                                    shape = RoundedCornerShape(14.dp), color = Gray200
                                )
                                .clip(RoundedCornerShape(14.dp))
                                .padding(horizontal = 20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier, text = "휴일은 설문조사를 쉬었다 갈게요", color = Gray500
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(
                                tint = Gray500,
                                painter = painterResource(id = R.drawable.ic_right_arrow),
                                contentDescription = "right arrow icon"
                            )
                        }
                    }
                }

            }
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { onNavigateToRoute("main/manual") },
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .background(
                            shape = RoundedCornerShape(14.dp), color = Green300
                        )
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier, text = "📚 한 눈에 보는 ", color = Color.White
                    )
                    Text(
                        text = "미세먼지 매뉴얼", fontWeight = FontWeight.Bold, color = Color.White
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_right_arrow),
                        tint = Color.White,
                        contentDescription = "right arrow icon"
                    )
                }
            }
        }
    }
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
@Preview
@Composable
fun afsdasdf() {
//    MainScreen(onNavigateToRoute = {})
}