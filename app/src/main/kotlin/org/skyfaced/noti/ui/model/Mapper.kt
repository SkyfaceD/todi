package org.skyfaced.noti.ui.model

interface Mapper<I, O> {
    fun map(input: I): O
}