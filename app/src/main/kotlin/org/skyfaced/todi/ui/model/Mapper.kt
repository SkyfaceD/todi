package org.skyfaced.todi.ui.model

interface Mapper<I, O> {
    fun map(input: I): O
}