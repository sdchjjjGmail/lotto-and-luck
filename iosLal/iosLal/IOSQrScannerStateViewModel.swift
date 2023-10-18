//
//  IOSQrScannerStateViewModel.swift
//  iosLal
//
//  Created by 서동철 on 2023/06/14.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

extension ContentView {
    @MainActor class IOSQrScannerStateViewModel: ObservableObject {
        let viewModel: QrScannerStateViewModel
        
        @Published var qrScannerState: QrScannerState_ = QrScannerState_(
            openQrScanner: false
        )
        
        private var handle: Kotlinx_coroutines_coreDisposableHandle?
        
        init() {
            self.viewModel = QrScannerStateViewModel(coroutineScope: nil)
        }
        
        func onEvent(event: QrScannerEvent) {
            self.viewModel.onEvent(event: event)
        }
        
        func startObserving() {
            handle = viewModel.qrScannerState.subscribe(onCollect: { state in
                if let qrScannerState = state {
                    self.qrScannerState = qrScannerState
                }
            })
        }
        
        func dispose() {
            handle?.dispose()
        }
    }
}
