package com.example.votersdata
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainViewModel : ViewModel() {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://3.111.87.210:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(ApiService::class.java)

    private val _voterData = MutableStateFlow<List<VoterData>>(emptyList())
    val voterData: StateFlow<List<VoterData>> = _voterData

    fun fetchVoterData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = apiService.getVoterData(
                    GetVoterDataRequest(
                        constituency = "tuni",
                        fromSNO = 1,
                        pbNo = "1",
                        status = "pending",
                        toSNO = 60
                    )
                )
                // Preprocess the data to remove leading spaces in names
                val processedResponse = response.map { voter ->
                    voter.copy(
                        first_name = voter.first_name.trimStart(),
                        last_name = voter.last_name.trimStart(),
                        relationshipName = voter.relationshipName.trimStart(),
                        relationshipSurname = voter.relationshipSurname.trimStart()
                    )
                }
                _voterData.value = processedResponse
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle network errors
            }
        }
    }


    fun sortVoterDataByName(ascending: Boolean) {
        _voterData.value = if (ascending) {
            _voterData.value.sortedBy { it.first_name.trimStart().lowercase() }
        } else {
            _voterData.value.sortedByDescending { it.first_name.trimStart().lowercase() }
        }
    }






}
