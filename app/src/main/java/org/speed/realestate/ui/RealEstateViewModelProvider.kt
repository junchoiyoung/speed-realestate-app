package org.speed.realestate.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import org.speed.realestate.RealEstateApplication
import org.speed.realestate.ui.viewmodel.RealEstateViewModel

object RealEstateViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            val application = realEstateApplication()

            RealEstateViewModel(
                realEstateRepository = application.container.realEstateRepository
            )
        }
    }
}

fun CreationExtras.realEstateApplication(): RealEstateApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as RealEstateApplication)