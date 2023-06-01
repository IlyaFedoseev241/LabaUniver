package com.example.laba222.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laba222.Data.Faculty
import com.example.laba222.repository.FacultyRepository
import kotlinx.coroutines.launch

class FacultyViewModel : ViewModel() {
    var university:MutableLiveData<List<Faculty>> = MutableLiveData()

    init {
        FacultyRepository.get().university.observeForever{
            university.postValue(it)
        }
        loadFaculty()
    }
    fun newFaculty(name:String){
       // FacultyRepository.get().newFaculty(name)
    }
    fun loadFaculty(){
        viewModelScope.launch {
            FacultyRepository.get().loadFaculty()
        }
    }
}