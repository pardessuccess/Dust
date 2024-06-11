package com.schoolkeepa.dust.presentation.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.schoolkeepa.dust.R
import com.schoolkeepa.dust.data.model.surveydata.Option
import com.schoolkeepa.dust.ui.theme.Blue300
import com.schoolkeepa.dust.ui.theme.Gray200
import com.schoolkeepa.dust.ui.theme.Gray700

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DustRadioGroup(
    dataList: List<Option>,
    data: Int,
    setData: (Int) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Canvas(
            modifier = Modifier
                .padding(top = 10.dp, start = 30.dp, end = 30.dp)
                .fillMaxWidth()
                .height(10.dp)
                .align(Alignment.TopCenter),
            onDraw = {
                drawRect(
                    color = Gray200,
                )
            }
        )
        Row(
            modifier = Modifier.align(Alignment.Center)
        ) {
            dataList.forEachIndexed { index, text ->
                Column(
                    modifier = Modifier
                        .height(60.dp)
                        .width(60.dp)
                        .background(Color.White.copy(0f))
                        .clickable {
                            setData(index)
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (index == data) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_select_radio),
                            contentDescription = "selected_radio"
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.ic_unselect_radio),
                            contentDescription = "unselected_radio"
                        )
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = text.text,
                        color = if (index == data) {
                            Blue300
                        } else {
                            Gray700
                        },
                        fontSize = 12.sp,
                        maxLines = 1
                    )
                }
                if (index != dataList.size - 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
//            }
        }
    }
}


@Preview
@Composable
fun Previewdfasd() {
//    DustRadioGroup(
//        dataList = listOf(
//            "매우 추움", "추움", "보통", "더움", "매우 더움"
//        )
//    )
}