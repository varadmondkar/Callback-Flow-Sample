package com.varad.core.util

sealed class UiEvent {
    data class Navigate(val route: String): UiEvent()
}