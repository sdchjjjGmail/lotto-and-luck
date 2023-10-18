//
//  Utils.swift
//  iosLal
//
//  Created by 서동철 on 2023/06/15.
//  Copyright © 2023 orgName. All rights reserved.
//

import UIKit

class Utils {

    /**
     # openExternalLink
     - Parameters:
     - urlStr : String 타입 링크
     - handler : Completion Handler
     - Note: 외부 브라우저로 링크 오픈
     */
    static func openExternalLink(urlStr: String, _ handler:(() -> Void)? = nil) {
        guard let url = URL(string: urlStr) else {
            return
        }
        
        if #available(iOS 10.0, *) {
            UIApplication.shared.open(url, options: [:]) { (result) in
                handler?()
            }
            
        } else {
            UIApplication.shared.openURL(url)
            handler?()
        }
    }
}
