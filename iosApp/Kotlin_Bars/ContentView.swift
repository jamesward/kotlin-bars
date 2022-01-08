import SwiftUI
import kotlinbars_common
import kotlinbars_rpc

@MainActor
class BarsViewModel: ObservableObject {
    @Published var bars: [CommonBar] = []

    let barsRPC: BarsRPC
    
    required init() {
        barsRPC = BarsRPC(barUrl: "https://kotlinbars.jamesward.com/api/bars")
    }
    
    func refresh() async throws {
        bars = try await barsRPC.fetchBars()
    }
    
    deinit {
        barsRPC.close()
    }
}
 

struct BarsView: View {
    @StateObject var viewModel = BarsViewModel()
    
    //    let bar = Bar(id: 1, name: "asdf")
    
    var body: some View {
        List(viewModel.bars, id: \.id) { bar in
            Text(bar.name).padding()
        }
        .task {
            try? await viewModel.refresh()
        }
        .refreshable {
            try? await viewModel.refresh()
        }
    }
    
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        BarsView()
    }
}
