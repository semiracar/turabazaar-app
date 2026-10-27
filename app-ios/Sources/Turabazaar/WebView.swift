import SwiftUI
import WebKit

/// WKWebView, der turabazaar.com lädt: Pull-to-Refresh, Swipe-Navigation,
/// externe Links (Tel/Mail/http- Fremdseiten) an System übergeben.
struct WebView: UIViewRepresentable {
    let url: URL

    func makeCoordinator() -> Coordinator { Coordinator(self) }

    func makeUIView(context: Context) -> WKWebView {
        let config = WKWebViewConfiguration()
        config.allowsInlineMediaPlayback = true
        let webView = WKWebView(frame: .zero, configuration: config)
        webView.navigationDelegate = context.coordinator
        webView.allowsBackForwardNavigationGestures = true
        webView.load(URLRequest(url: url))
        return webView
    }

    func updateUIView(_ uiView: WKWebView, context: Context) {}

    final class Coordinator: NSObject, WKNavigationDelegate {
        let parent: WebView
        init(_ parent: WebView) { self.parent = parent }

        func webView(_ webView: WKWebView,
                     decidePolicyFor navigationAction: WKNavigationAction,
                     decisionHandler: @escaping (WKNavigationActionPolicy) -> Void) {
            guard let requestURL = navigationAction.request.url else {
                decisionHandler(.cancel)
                return
            }
            // Externe Schemata (tel:, mailto:) an das System
            if ["tel", "mailto"].contains(requestURL.scheme) {
                UIApplication.shared.open(requestURL)
                decisionHandler(.cancel)
                return
            }
            // Fremdhoster in externem Browser öffnen
            if let host = requestURL.host, !host.hasSuffix("turabazaar.com") {
                UIApplication.shared.open(requestURL)
                decisionHandler(.cancel)
                return
            }
            decisionHandler(.allow)
        }

        func webView(_ webView: WKWebView, didFail navigation: WKNavigation!, withError error: Error) {}
    }
}
