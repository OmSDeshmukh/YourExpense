package com.example.expensetracker.presentation.Pots

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.data.model.Pot
import com.example.expensetracker.data.repo.PotRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PotsScreenViewModel @Inject constructor(
    private val potRepo: PotRepo
) : ViewModel()
{
    private val _state = MutableStateFlow(PotsScreenState())
    val state : StateFlow<PotsScreenState> = _state

    var pot : Pot? = null

    init {
        fetchAllPots()
    }

    private fun fetchAllPots()
    {
        viewModelScope.launch {
            potRepo.getAllPots().collect { pots ->
                _state.value = _state.value.copy(pots = pots)
            }
        }
    }

    fun onEvent(event : PotsScreenEvent)
    {
        when( event )
        {
            is PotsScreenEvent.OnPotNameChanged -> { _state.value = _state.value.copy(potName = event.name) }
            is PotsScreenEvent.OnPotGoalChanged -> { _state.value = _state.value.copy(potGoal = event.goal) }
            is PotsScreenEvent.OnPotAmountChanged -> { _state.value = _state.value.copy(potAmount = event.amount) }
            is PotsScreenEvent.OnAddPotClicked -> {
                viewModelScope.launch {
                    potRepo.upsertPot(
                        Pot(
                            id = _state.value.id,
                            name = _state.value.potName,
                            goal = _state.value.potGoal,
                            sofar = _state.value.potAmount
                        )
                    )
                }

                _state.value = _state.value.copy(
                    id = null,
                    potName = "",
                    potGoal = 0,
                    potAmount = 0
                )
            }
            is PotsScreenEvent.OnPotSelected -> {
                pot = _state.value.pots.find { it.id == event.id }
                if( pot != null )
                {
                    _state.value = _state.value.copy(
                        id = pot!!.id,
                        potName = pot!!.name,
                        potGoal = pot!!.goal,
                        potAmount = pot!!.sofar
                    )
                }
            }
            is PotsScreenEvent.OnUpdatePot -> {
                viewModelScope.launch {
                    potRepo.upsertPot(
                        Pot(
                            id = _state.value.id,
                            name = _state.value.potName,
                            goal = _state.value.potGoal,
                            sofar = pot!!.sofar + (_state.value.potAmount.toDouble().toInt() * event.i)
                        )
                    )
                }

                _state.value = _state.value.copy(
                    id = null,
                    potName = "",
                    potGoal = 0,
                    potAmount = 0
                )
            }
            is PotsScreenEvent.OnDeletePotClicked -> {
                viewModelScope.launch {
                    pot!!.id?.let { id -> potRepo.deletePotById(id) }
                }

                _state.value = _state.value.copy(
                    id = null,
                    potName = "",
                    potGoal = 0,
                    potAmount = 0
                )
            }
        }
    }
}