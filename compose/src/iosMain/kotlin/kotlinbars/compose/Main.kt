package kotlinbars.compose

import androidx.compose.ui.window.ComposeUIViewController
import kotlinbars.rpc.BarsRPC
import platform.UIKit.UIViewController

fun MainViewController(barsUrl: String) : UIViewController = ComposeUIViewController {
    Bars(BarsRPC(barsUrl))
}
