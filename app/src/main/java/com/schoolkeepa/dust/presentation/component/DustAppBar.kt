package com.schoolkeepa.dust.presentation.component

import androidx.compose.foundation.background
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.schoolkeepa.dust.R
import com.schoolkeepa.dust.ui.theme.Blue100
import com.schoolkeepa.dust.ui.theme.Gray100
import com.schoolkeepa.dust.ui.theme.Green400

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DustTopAppBar(
    title: String,
    color: Color = Color.White,
    upPress: () -> Unit,
    actions: Boolean = false,
    onSaveClick: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(color),
        navigationIcon = {
            IconButton(onClick = { upPress() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_left),
                    contentDescription = "arrow left icon"
                )
            }
        },
        actions = {
            if (actions) {
                TextButton(onClick = { onSaveClick() }) {
                    Text(
                        text = "저장",
                        color = Green400,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }
    )
}