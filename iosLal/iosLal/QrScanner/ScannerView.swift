//
//  ScannerView.swift
//  iosLal
//
//  Created by 서동철 on 2023/06/13.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct ScannerView: View {
    @ObservedObject var viewModel = ScannerViewModel()
    var qrScannerStateViewModel: QrScannerStateViewModel
    
    var body: some View {
        VStack {
            HStack {
                Button(action: {
                    self.viewModel.torchIsOn.toggle()
                }, label: {
                    Image(systemName: self.viewModel.torchIsOn ? "bolt.fill" : "bolt.slash.fill")
                        .imageScale(.large)
                        .foregroundColor(self.viewModel.torchIsOn ? Color.yellow : Color.blue)
                        .padding()
                })
            }
            .background(Color.white)
            .cornerRadius(10)
            Spacer()
            QrCodeScannerView()
                .found(r: self.viewModel.onFoundQrCode)
                .torchLight(isOn: self.viewModel.torchIsOn)
                .interval(delay: self.viewModel.scanInterval)
            Spacer()
            VStack {
                Button(action: {
                    qrScannerStateViewModel.onEvent(event: QrScannerEvent.CloseQrScreenScanner())
                }, label: {
                    Text("나가기")
                        .font(.system(size: 18)).foregroundColor(.black)
                        .bold()
                })
                .frame(width: 90, height: 40)
                .background(Color("yellow_button"))
                .cornerRadius(50)
                //                Text(self.viewModel.lastQrCode)
                //                    .bold()
                //                    .lineLimit(5)
                //                    .padding()
            }
            .padding(.vertical, 20)
        }.padding()
    }
}
