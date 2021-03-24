package kotlinbars.android

import kotlinbars.compose.Bars
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val url = resources.getString(R.string.bars_url) + "/api/bars"

        setContent {
            Bars(url)
        }
    }
}
