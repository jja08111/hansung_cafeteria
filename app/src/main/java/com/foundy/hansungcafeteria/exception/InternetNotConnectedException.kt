package com.foundy.hansungcafeteria.exception

class InternetNotConnectedException: Exception() {
    override val message: String
        get() = "인터넷 연결이되지 않음"
}