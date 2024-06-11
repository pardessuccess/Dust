package com.schoolkeepa.dust.util

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.schoolkeepa.dust.ui.theme.Gray1000

class CustomToast(context: Context) : Toast(context) {

    @Composable
    fun MakeText(
        message: String,
        duration: Int = LENGTH_SHORT
    ) {
        val context = LocalContext.current
        val views = ComposeView(context)

        views.setContent {
            CustomToastUtil.SetView(messageTxt = message)
        }

        views.setViewTreeLifecycleOwner(LocalLifecycleOwner.current)
        views.setViewTreeSavedStateRegistryOwner(LocalSavedStateRegistryOwner.current)
        views.setViewTreeViewModelStoreOwner(LocalViewModelStoreOwner.current)

        this.duration = duration
        this.view = views
        this.show()
    }

}

object CustomToastUtil {

    @Composable
    fun SetView(
        messageTxt: String,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(60.dp)
                .background(
                    color = Gray1000.copy(0.8f),
                    shape = RoundedCornerShape(size = 16.dp)
                )
                .padding(vertical = 12.dp)
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center),
                text = messageTxt,
                color = Color.White,
                maxLines = 1
            )
        }
    }
}