package com.example.mirrorgram.models

import java.io.Serializable

class SecondUser :Serializable{
    var email: String? = null
    var displayName: String? = null
    var phoneNumber: String? = null
    var photoUri: String? = null
    var uid: String? = null
    var isHave: Boolean? = null
    var time: String? = null
    var lastMessage: String? = null
    constructor()
    constructor(
        email: String?,
        displayName: String?,
        phoneNumber: String?,
        photoUri: String?,
        uid: String?,
        isHave: Boolean?
    ) {
        this.email = email
        this.displayName = displayName
        this.phoneNumber = phoneNumber
        this.photoUri = photoUri
        this.uid = uid
        this.isHave = isHave
    }

    constructor(
        email: String?,
        displayName: String?,
        phoneNumber: String?,
        photoUri: String?,
        uid: String?,
        isHave: Boolean?,
        time: String?,
        lastMessage: String?
    ) {
        this.email = email
        this.displayName = displayName
        this.phoneNumber = phoneNumber
        this.photoUri = photoUri
        this.uid = uid
        this.isHave = isHave
        this.time = time
        this.lastMessage = lastMessage
    }


}