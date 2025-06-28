package com.denisgasparoto.minderset.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.denisgasparoto.minderset.presentation.composable.FlashCardScreen

internal class FlashCardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlashCardScreen()
        }
    }
}
