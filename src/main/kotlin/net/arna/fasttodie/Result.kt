package net.arna.fasttodie

class Result {

    val isSuccess: Boolean
    val message: String

    constructor()
    {
        isSuccess = true;
        message = ""
    }

    constructor(message: String)
    {
        isSuccess = false
        this.message = message
    }
}