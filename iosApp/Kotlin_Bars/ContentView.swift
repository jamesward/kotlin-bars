/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
        NSLog("init start")
        barsRPC = BarsRPC(barsUrl: barsUrl)
        NSLog("init done")
    }

    func refresh() async throws {
        NSLog("do refresh")
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
                NSLog("VStack.task")
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
