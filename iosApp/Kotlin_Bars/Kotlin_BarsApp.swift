import SwiftUI

import kotlinbars_common
import kotlinbars_rpc

@main
struct Kotlin_BarsApp: App {
    
    /*
    let barsRPC: BarsRPC
    
    required init() {
        barsRPC = BarsRPC(barUrl: "https://kotlinbars.jamesward.com/api/bars")
    }
    */
    
    var body: some Scene {
        WindowGroup {
            BarsView<LiveBarsViewModel>(viewModel: LiveBarsViewModel())
        }
    }
}
