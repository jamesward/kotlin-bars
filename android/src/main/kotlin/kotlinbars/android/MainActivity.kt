package kotlinbars.android

import kotlinbars.compose.Bars
import kotlinbars.compose.BarsLive
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import kotlinbars.common.Bar
import kotlinbars.rpc.BarsRPC

class MainActivity : ComponentActivity() {

    private val barsRpc by lazy {
        val url = resources.getString(R.string.bars_url)
        BarsRPC(url)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BarsLive(barsRpc)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        barsRpc.close()
    }
}

@Preview
@Composable
fun PreviewBars() {
    val barsState = remember { mutableStateListOf<Bar>() }
    Bars(barsState.toList()) { barName ->
        barsState.add(Bar(barsState.size.toLong(), barName))
    }
}