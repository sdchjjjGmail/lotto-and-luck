import UIKit
import SwiftUI
import shared

struct ComposeView: UIViewControllerRepresentable {
    var qrScannerStateViewModel: QrScannerStateViewModel
    
    func makeUIViewController(context: Context) -> UIViewController {
        Main_iosKt.MainViewController(qrScannerStateViewModel: qrScannerStateViewModel)
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
        
    }
}

struct ContentView: View {
    @ObservedObject var viewModel: IOSQrScannerStateViewModel
    
    init() {
        self.viewModel = IOSQrScannerStateViewModel()
    }
    
    var body: some View {
        ZStack {
            if (viewModel.qrScannerState.openQrScanner) {
                ScannerView(qrScannerStateViewModel: viewModel.viewModel)
            } else {
                ComposeView(qrScannerStateViewModel: viewModel.viewModel)
                    .ignoresSafeArea(.keyboard) // Compose has own keyboard handler
            }
        }
        .onAppear {
            viewModel.startObserving()
        }
        .onDisappear {
            viewModel.dispose()
        }
    }
}
