import SwiftUI
import KotlinbarsCommon
import KotlinbarsRpc

@MainActor
protocol BarsViewModel: ObservableObject {
    var bars: [CommonBar] { get }
    
    func refresh() async throws
    func add(bar: CommonBar) async throws
}

@MainActor
class LiveBarsViewModel: BarsViewModel {
    @Published var bars: [CommonBar] = []

    let barsRPC: BarsRPC
    
    required init(barsUrl: String) {
        print("Connecting to: " + barsUrl)
        barsRPC = BarsRPC(barsUrl: barsUrl)
    }
    
    func refresh() async throws {
        bars = try await barsRPC.fetchBars()
    }
    
    func add(bar: CommonBar) async throws {
        try await barsRPC.addBar(bar: bar)
        try await refresh()
    }
    
    deinit {
        barsRPC.close()
    }
}
 

struct BarsView<ViewModel>: View where ViewModel: BarsViewModel {
    @ObservedObject var viewModel: ViewModel
    
    @State private var barName: String = ""
    
    var body: some View {
        VStack {
            Text("Bars:")
            
            List(viewModel.bars, id: \.id) { bar in
                Text(bar.name).padding()
            }
            .task {
                try? await viewModel.refresh()
            }
            .refreshable {
                try? await viewModel.refresh()
            }
            
            TextField(
                    "New Bar",
                    text: $barName
                )
                .onSubmit {
                    Task {
                        let bar = CommonBar(id: nil, name: barName)
                        try? await viewModel.add(bar: bar)
                        barName = ""
                    }
                }
                .textFieldStyle(.roundedBorder)
                .padding()

        }
    }
    
}

struct BarsView_Previews: PreviewProvider {
    class PreviewBarsViewModel: BarsViewModel {
        @Published var bars: [CommonBar] = [
            CommonBar(id: 1, name: "Foo"),
            CommonBar(id: 2, name: "Bar")
        ]
        
        func refresh() async throws {
            // do nothing
        }
        
        func add(bar: CommonBar) async throws {
            bars.append(bar) // todo: increment id
        }
    }
    
    static var previews: some View {
        BarsView<PreviewBarsViewModel>(viewModel: PreviewBarsViewModel())
    }
}
