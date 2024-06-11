package com.schoolkeepa.dust.presentation.navigation

enum class Screen(
    val title: String,
    val route: String,
) {
    Intro("Intro", "intro"),
    SignUp("SignUp", "intro/signup"),
    FindEmail("이메일 찾기", "intro/find_email"),
    ResetPassword("비밀번호 재설정", "intro/reset_password"),
    Main("Main", "main"),
    SurveyMain("SurveyMain", "main/survey_main"),
    Survey("설문조사", "main/survey_main/survey"),
    Setting("설정", "main/setting"),
    Search("Search", "main/search"),
    Manual("미세먼지 매뉴얼", "main/manual"),
    ManualDetail("", "main/manual/detail"),
    SurveyAgreement("개인정보동의", "main/manual/agreement"),
    SignUpAgreement("개인정보동의", "main/intro/agreement"),
}