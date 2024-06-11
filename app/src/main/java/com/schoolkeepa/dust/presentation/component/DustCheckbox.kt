package com.schoolkeepa.dust.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.schoolkeepa.dust.R
import com.schoolkeepa.dust.ui.theme.Blue300
import com.schoolkeepa.dust.ui.theme.Gray200
import com.schoolkeepa.dust.ui.theme.Gray300


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DustCheckboxComponent(
    checkBoxState: Boolean,
) {
    Card(
        modifier = Modifier
            .size(24.dp),
        shape = RoundedCornerShape(5.dp),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        if (checkBoxState) CheckBoxSelected()
        else CheckBoxUnSelected()
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DustCheckbox(
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit
) {
    var checkBoxState by remember { mutableStateOf(checked) }

    Card(
        modifier = modifier
            .size(24.dp),
        shape = RoundedCornerShape(5.dp),
        onClick = {
            checkBoxState = !checkBoxState
            onCheckedChange(checkBoxState)
        },
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        if (checkBoxState) CheckBoxSelected()
        else CheckBoxUnSelected()
    }
}

@Composable
private fun CheckBoxSelected() {
    Box(
        modifier = Modifier
            .size(30.dp)
            .background(Blue300),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            tint = Color.White,
            painter = painterResource(id = R.drawable.ic_check), contentDescription = null
        )
    }
}

@Composable
private fun CheckBoxUnSelected() {
    Box(
        modifier = Modifier
            .size(30.dp)
            .background(Gray300),
    )
}