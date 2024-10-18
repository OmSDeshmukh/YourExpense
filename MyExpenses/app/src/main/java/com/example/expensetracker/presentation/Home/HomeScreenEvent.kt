package com.example.expensetracker.presentation.Home

import java.time.LocalDate

sealed class HomeScreenEvent
{
    data class OnItemNameChanged(val itemName : String) : HomeScreenEvent()
    data class OnItemPriceChanged(val itemPrice : String) : HomeScreenEvent()
    data class OnItemCategoryChanged(val itemCategory : Int?) : HomeScreenEvent()
    data class OnDateChanged(val date : LocalDate) : HomeScreenEvent()
    data class OnItemLongPress(val itemId : Int?) : HomeScreenEvent()
    data class OnPagerPageChanged(val page : Int) : HomeScreenEvent()
    data object OnItemTypeChanged : HomeScreenEvent()
    data object OnAddItemClicked : HomeScreenEvent()
    data object OnDeleteItemClicked : HomeScreenEvent()
    data object OnRefresh : HomeScreenEvent()
}