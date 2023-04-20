package com.example.codeapp.services

class Singleton private constructor(value: String) {
    var value: String

    init {
        this.value = value
    }

    companion object {
        private var instance: Singleton? = null
        fun getInstance(value: String): Singleton? {
            if (instance == null) {
                instance = Singleton(value)
            }
            return instance
        }
        fun getValue(): String? {
            return instance?.value
        }
    }
}