package com.example.testing2fire.core.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Factory base para crear instancias de ViewModel
 */
abstract class ViewModelFactory<T : ViewModel> : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(getViewModelClass())) {
            return createViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    /**
     * Retorna la clase del ViewModel que esta factory puede crear
     */
    abstract fun getViewModelClass(): Class<T>

    /**
     * Metodo que crea una instancia del ViewModel con sus dependencias
     */
    abstract fun createViewModel(): T
}