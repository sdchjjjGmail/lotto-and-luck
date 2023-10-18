package com.content.lottoandluck.data.model

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmUUID
import io.realm.kotlin.types.annotations.PrimaryKey


class SavedLotteryNumberEntity : RealmObject {
    @PrimaryKey
    var id: RealmUUID = RealmUUID.random()
    var numbers: RealmList<Int> = realmListOf()
}
