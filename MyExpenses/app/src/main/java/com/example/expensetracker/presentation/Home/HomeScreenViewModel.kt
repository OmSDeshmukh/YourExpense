package com.example.expensetracker.presentation.Home

import android.graphics.Bitmap
import android.os.Build
import android.util.Base64
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.api.ImageData
import com.example.expensetracker.api.NetworkResponse
import com.example.expensetracker.api.PhotoRequest
import com.example.expensetracker.api.sendPhoto
import com.example.expensetracker.data.model.Item
import com.example.expensetracker.data.model.ItemType
import com.example.expensetracker.data.repo.BalanceRepo
import com.example.expensetracker.data.repo.CategoryRepo
import com.example.expensetracker.data.repo.ItemRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val categoryRepo: CategoryRepo,
    private val itemRepo: ItemRepo,
    private val api : sendPhoto
) : ViewModel()
{
    private val _state = MutableStateFlow(HomeScreenState())
    val state : StateFlow<HomeScreenState> = _state

    private val _data = MutableStateFlow<NetworkResponse<ImageData>>(NetworkResponse.Loading)
    var data : StateFlow<NetworkResponse<ImageData>> = _data

    init {
        fetchItems(LocalDate.now())
    }

    private fun fetchItems(date : LocalDate) {
        viewModelScope.launch {
            itemRepo.getItemsByDate(date.format(DateTimeFormatter.ISO_LOCAL_DATE)).collect { items ->
                _state.value = _state.value.copy(
                    itemId = null,
                    itemName = "",
                    itemPrice = "",
                    itemCategory = null,
                    itemType = ItemType.DEBIT,
                    date = date,
                    items = items,
                )
            }
        }
    }

    fun onEvent(event : HomeScreenEvent) {
        when(event) {
            is HomeScreenEvent.OnItemNameChanged -> { _state.value = _state.value.copy(itemName = event.itemName) }
            is HomeScreenEvent.OnItemPriceChanged -> { _state.value = _state.value.copy(itemPrice = event.itemPrice) }
            is HomeScreenEvent.OnItemCategoryChanged -> { _state.value = _state.value.copy(itemCategory = event.itemCategory) }
            is HomeScreenEvent.OnItemTypeChanged -> {
                when ( _state.value.itemType ) {
                    ItemType.DEBIT -> _state.value = _state.value.copy(itemType = ItemType.CREDIT)
                    ItemType.CREDIT -> _state.value = _state.value.copy(itemType = ItemType.DEBIT)
                }
            }
            is HomeScreenEvent.OnDateChanged -> { fetchItems(event.date) }
            is HomeScreenEvent.OnItemLongPress -> {
                val item = _state.value.items.find { it.id == event.itemId }

                _state.value = _state.value.copy(
                    itemId = item?.id,
                    itemName = item?.name ?: "",
                    itemPrice = item?.amount.toString(),
                    itemCategory = item?.categoryId,
                    itemType = item?.type ?: ItemType.DEBIT,
                )
            }
            is HomeScreenEvent.OnAddItemClicked -> {

                viewModelScope.launch {
                    itemRepo.upsertItem(
                        Item(
                            id = _state.value.itemId,
                            name = _state.value.itemName,
                            amount = _state.value.itemPrice.toDouble(),
                            categoryId = _state.value.itemCategory,
                            type = _state.value.itemType,
                            date = LocalDate.parse(_state.value.date.format(DateTimeFormatter.ISO_LOCAL_DATE)),
                        )
                    )

                    fetchItems(_state.value.date)
                }
            }
            is HomeScreenEvent.OnDeleteItemClicked -> {
                val itemId = _state.value.itemId

                viewModelScope.launch {
                    if (itemId != null)
                        itemRepo.deleteItemById(itemId)

                    fetchItems(_state.value.date)
                }
            }
            is HomeScreenEvent.OnRefresh -> {
                _state.value = _state.value.copy(
                    itemId = null,
                    itemName = "",
                    itemPrice = "",
                    itemCategory = null,
                    itemType = ItemType.DEBIT)
            }
        }
    }

    fun sendPhoto(image: Bitmap) {

//        val byteArray = bitmapToByteArray(image)
//        val requestBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(), byteArray)
//        val multipartBody = MultipartBody.Part.createFormData("image", "photo.jpg", requestBody)

        val byteArray = bitmapToByteArray(image)
        val base64 = byteArrayToBase64(byteArray)
        val photoRequest = PhotoRequest(image_base64 = base64)
        Log.d("Encoding...", base64)

        viewModelScope.launch {
            try {
                Log.d("Camera Sending the Image", "Sending the Image")
                val response = api.sendPhoto(photoRequest)
                Log.d("Camera", "Sent Photo: ${response.body()?.date} ${response.body()?.total_amount} ${response.body()?.heading}")

                if (response.isSuccessful) {
//                    data = NetworkResponse.Success(response.body())
                    Log.d("Camera", "Success ${data.value}")
                } else {
//                    data = NetworkResponse.Error("Failed to load data " + response.code())
                    Log.d("Camera", "Failed to load data " + response.code())
                }
            } catch (e: Exception) {
//                data = NetworkResponse.Error("Error to load data " + e.message)
                Log.d("CameraError", "Error: ${e.message}")
            }
        }
    }
}

fun bitmapToByteArray(bitmap: Bitmap): ByteArray{
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}

fun byteArrayToBase64(byteArray: ByteArray): String {
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}