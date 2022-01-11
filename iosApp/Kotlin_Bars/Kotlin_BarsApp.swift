import SwiftUI

import KotlinbarsCommon
import KotlinbarsRpc

@main
struct Kotlin_BarsApp: App {
    
    let barsUrl = Bundle.main.infoDictionary?["BarsUrl"] as! String
    
    init() {
        if (barsUrl == "") {
            fatalError("No info for BarsUrl")
        }
    }
    
    var body: some Scene {
        WindowGroup {
            BarsView<LiveBarsViewModel>(viewModel: LiveBarsViewModel(barsUrl: barsUrl))
        }
    }
}
