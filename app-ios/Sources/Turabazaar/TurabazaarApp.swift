import SwiftUI

@main
struct TurabazaarApp: App {
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}

struct ContentView: View {
    let startURL = URL(string: "https://turabazaar.com")!

    var body: some View {
        WebView(url: startURL)
            .edgesIgnoringSafeArea(.bottom)
    }
}
