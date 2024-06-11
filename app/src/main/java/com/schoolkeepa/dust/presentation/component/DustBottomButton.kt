package com.schoolkeepa.dust.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.schoolkeepa.dust.ui.theme.Gray100
import com.schoolkeepa.dust.ui.theme.Gray200
import com.schoolkeepa.dust.ui.theme.Gray400
import com.schoolkeepa.dust.ui.theme.Green300

@Composable
fun DustBottomButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    isValid: Boolean,
) {
    Column(
        modifier = modifier
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 15.dp),
            shape = RoundedCornerShape(25),
            colors = ButtonDefaults.buttonColors(if (isValid) Green300 else Gray100),
            onClick = {
                if (isValid) {
                    onClick()
                }
            }) {
            Text(
                text = text,
                color = if (isValid) Color.White else Gray400
            )
        }
        Spacer(modifier = Modifier.height(25.dp))
    }
}