package kotlinbars.android

import kotlinbars.compose.Bars
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import kotlinbars.rpc.BarsRPC

class MainActivity : AppCompatActivity() {

    private val barsRpc by lazy {
        val url = resources.getString(R.string.bars_url)
        BarsRPC(url)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Bars(barsRpc)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        barsRpc.close()
    }
}
