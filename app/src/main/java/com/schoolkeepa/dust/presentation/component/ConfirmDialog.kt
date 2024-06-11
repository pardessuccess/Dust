package com.schoolkeepa.dust.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.schoolkeepa.dust.ui.theme.Gray100
import com.schoolkeepa.dust.ui.theme.Gray900
import com.schoolkeepa.dust.ui.theme.Green300

@Composable
fun ConfirmDialog(
    title: String = "",
    message: String,
    confirm: String,
    setShowDialog: (Boolean) -> Unit,
    setConfirmClick: () -> Unit
) {
    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = title, fontSize = 20.sp, fontWeight = FontWeight.Bold,
                    modifier = Modifier,
                )
                Spacer(
                    modifier = Modifier.weight(1f)
                )
                Text(
                    modifier = Modifier.padding(),
                    text = message,
                    textAlign = TextAlign.Center
                )
                Spacer(
                    modifier = Modifier.weight(1f)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
                ) {
                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .height(55.dp),
                        shape = RoundedCornerShape(25),
                        colors = ButtonDefaults.buttonColors(Green300),
                        onClick = {
                            setConfirmClick()
                        }) {
                        Text(
                            text = confirm,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}