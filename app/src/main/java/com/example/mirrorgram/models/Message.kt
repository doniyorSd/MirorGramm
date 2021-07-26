package com.example.mirrorgram.models

import java.io.Serializable

class Message :Serializable{
    var fromId:String?= null
    var toId:String?= null
    var message:String?= null
    var date :String?= null
    var name:String?= null

    constructor()
    constructor(
        fromId: String?,
        toId: String?,
        message: String?,
        date: String?,
        name:String?
    ) {
        this.fromId = fromId
        this.toId = toId
        this.message = message
        this.date = date
        this.name = name
    }

}