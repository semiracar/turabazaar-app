import SwiftUI

@main
struct TurabazaarSaleStudioApp: App {
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}

struct ContentView: View {
    let startURL = URL(string: "https://seller.turabazaar.com/")!

    var body: some View {
        WebView(url: startURL)
            .edgesIgnoringSafeArea(.bottom)
    }
}
