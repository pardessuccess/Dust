package com.schoolkeepa.dust.presentation.intro

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.schoolkeepa.dust.presentation.component.DustTopAppBar
import com.schoolkeepa.dust.presentation.survey.SurveyViewModel

@Composable
fun SignUpAgreementScreen(
    upPress: () -> Unit,
    surveyViewModel: SurveyViewModel
) {
    Scaffold(
        topBar = {
            DustTopAppBar(title = "개인정보동의", upPress = { upPress() })
        }
    ) {
        Surface(modifier = Modifier.padding(top = it.calculateTopPadding())) {

            Box(modifier = Modifier) {
                Text(
                    text = "개인정보 동의",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}