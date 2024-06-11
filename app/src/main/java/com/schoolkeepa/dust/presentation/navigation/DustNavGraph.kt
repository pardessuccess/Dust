package com.schoolkeepa.dust.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.schoolkeepa.dust.DustApplication
import com.schoolkeepa.dust.presentation.auth.FindEmailScreen
import com.schoolkeepa.dust.presentation.intro.IntroScreen
import com.schoolkeepa.dust.presentation.intro.SignUpScreen
import com.schoolkeepa.dust.presentation.intro.IntroViewModel
import com.schoolkeepa.dust.presentation.main.MainScreen
import com.schoolkeepa.dust.presentation.main.MainViewModel
import com.schoolkeepa.dust.presentation.survey.SurveyAgreementScreen
import com.schoolkeepa.dust.presentation.manual.ManualDetailScreen
import com.schoolkeepa.dust.presentation.manual.ManualScreen
import com.schoolkeepa.dust.presentation.navgraph.DustDestinations.HOME_ROUTE
import com.schoolkeepa.dust.presentation.navgraph.DustNavController
import com.schoolkeepa.dust.presentation.password.ResetPasswordScreen
import com.schoolkeepa.dust.presentation.setting.SettingScreen
import com.schoolkeepa.dust.presentation.setting.SettingViewModel
import com.schoolkeepa.dust.presentation.survey.SurveyMainScreen
import com.schoolkeepa.dust.presentation.survey.SurveyScreen
import com.schoolkeepa.dust.presentation.survey.SurveyViewModel


enum class UserType {
    ELEMENTARY, MIDDLE, HIGH, TEACHER
}

@Composable
fun DustNavGraph(
    dustNavController: DustNavController,
    mainViewModel: MainViewModel,
) {
    val context = LocalContext.current

    val app = DustApplication()

    val surveyViewModel: SurveyViewModel = hiltViewModel()
    val introViewModel: IntroViewModel = hiltViewModel()
    val settingViewModel: SettingViewModel = hiltViewModel()
    println("@@@@@mainViewModel.userData.value" + mainViewModel.userData.value)
    println("@@@@@mainViewModel.userType.value" + mainViewModel.userType.value)
    println("@@@@@mainViewModel.startDestination.value" + mainViewModel.startDestination.value)

//    val isTeach = introViewModel.isTeacher.value

    NavHost(
        navController = dustNavController.navController,
        startDestination = HOME_ROUTE
    ) {
        mainGraph(
            startDestination = mainViewModel.startDestination.value,
            onNavigateToRoute = dustNavController::navigateToRoute,
            upPress = dustNavController::upPress,
            mainViewModel = mainViewModel,
            surveyViewModel = surveyViewModel,
            introViewModel = introViewModel,
            settingViewModel = settingViewModel,
            userData = mainViewModel.userData.value,
            userType = mainViewModel.userType.value
        )
    }
}

fun NavGraphBuilder.mainGraph(
    startDestination: String,
    onNavigateToRoute: (String) -> Unit,
    upPress: () -> Unit,
    mainViewModel: MainViewModel,
    surveyViewModel: SurveyViewModel,
    introViewModel: IntroViewModel,
    settingViewModel: SettingViewModel,
    userData: String,
    userType: String,
//    uType: String
) {
    navigation(
        route = HOME_ROUTE,
        startDestination = startDestination
    ) {

        composable(route = Screen.SurveyAgreement.route) {
            SurveyAgreementScreen(
                upPress = upPress,
                surveyViewModel = surveyViewModel
            )
        }

        composable(route = Screen.ResetPassword.route) {
            ResetPasswordScreen(
                onNavigateToRoute = onNavigateToRoute,
                upPress = upPress
            )
        }

        composable(route = Screen.FindEmail.route) {
            FindEmailScreen(
                onNavigateToRoute = onNavigateToRoute,
                upPress = upPress,
            )
        }

        composable(route = Screen.Intro.route) {
            IntroScreen(
                onNavigateToRoute = onNavigateToRoute,
                viewModel = introViewModel
            )
        }
        composable(route = Screen.SignUp.route) {
            SignUpScreen(
                onNavigateToRoute = onNavigateToRoute,
                upPress = upPress,
                viewModel = introViewModel
            )
        }
        composable(route = Screen.Main.route) {
            MainScreen(
                onNavigateToRoute = onNavigateToRoute,
            )
        }
        composable(route = Screen.Setting.route) {
            SettingScreen(
                title = Screen.Setting.title,
                userData = userData,
                userType = userType,
                upPress = upPress,
                viewModel = settingViewModel
            )
        }

        composable(route = Screen.SurveyMain.route + "/{date}") { navBackStackEntry ->
            val date = navBackStackEntry.arguments?.getString("date")
            SurveyMainScreen(
                onNavigateToRoute = onNavigateToRoute,
                upPress = upPress,
                viewModel = surveyViewModel,
                userType = userType,
                date = date!!
            )
        }

        composable(route = Screen.Survey.route + "/{date}") { navBackStackEntry ->
            val date = navBackStackEntry.arguments?.getString("date")
            date?.let {
                SurveyScreen(
                    viewModel = surveyViewModel,
                    upPress = upPress,
                    userData = userData,
                    userType = userType,
                    date = it
                )
            }
        }

        composable(route = Screen.Manual.route) {
            ManualScreen(
                onNavigateToRoute = onNavigateToRoute,
                upPress = upPress,
                userType = userType,
            )
        }

        composable(route = Screen.ManualDetail.route + "/{manualId}/{title}") { navBackStackEntry ->
            val manualId = navBackStackEntry.arguments?.getString("manualId")
            val title = navBackStackEntry.arguments?.getString("title")
            manualId?.let {
                ManualDetailScreen(
                    manualId = it,
                    title = title!!
                )
            }
        }
    }
}
