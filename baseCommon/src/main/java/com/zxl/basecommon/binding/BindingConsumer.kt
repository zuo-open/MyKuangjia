package com.pansoft.basecode.binding

interface BindingConsumer<T> {
    fun call(t: T)
}
