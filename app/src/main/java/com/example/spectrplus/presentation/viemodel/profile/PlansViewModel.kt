package com.example.spectrplus.presentation.viemodel.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spectrplus.domain.model.profile.Plan
import com.example.spectrplus.domain.interactor.profile.PlanInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class PlansViewModel @Inject constructor(
    private val planInteractor: PlanInteractor
) : ViewModel() {

    var plans by mutableStateOf<List<Plan>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    fun load(month: YearMonth) {
        viewModelScope.launch {
            isLoading = true
            error = null
            try {
                plans = planInteractor.getPlans(month.toString())
            } catch (e: Exception) {
                error = e.message
            }
            isLoading = false
        }
    }

    fun addTask(date: LocalDate, title: String, time: String) {
        if (title.isBlank()) return
        viewModelScope.launch {
            try {
                val added = planInteractor.addPlan(date, title, time)
                plans = plans + added
            } catch (e: Exception) {
                error = e.message
            }
        }
    }

    fun toggleDone(id: Long) {
        viewModelScope.launch {
            try {
                val updated = planInteractor.toggleDone(id)
                plans = plans.map { if (it.id == id) updated else it }
            } catch (e: Exception) {
                error = e.message
            }
        }
    }

    fun deleteTask(id: Long) {
        viewModelScope.launch {
            try {
                planInteractor.deletePlan(id)
                plans = plans.filter { it.id != id }
            } catch (e: Exception) {
                error = e.message
            }
        }
    }

    fun updateTask(id: Long, date: LocalDate, title: String, time: String, done: Boolean) {
        viewModelScope.launch {
            try {
                val updated = planInteractor.updatePlan(id, date, title, time, done)
                plans = plans.map { if (it.id == id) updated else it }
            } catch (e: Exception) {
                error = e.message
            }
        }
    }
}
