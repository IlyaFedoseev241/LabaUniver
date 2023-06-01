package com.example.laba222.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laba222.Data.Faculty
import com.example.laba222.Data.Group
import com.example.laba222.repository.FacultyRepository
import com.example.laba222.repository.FacultyRepository.Companion.get
import kotlinx.coroutines.launch
import java.util.UUID

class GroupViewModel : ViewModel() {
    var faculty_groups: MutableLiveData<List<Group>> = MutableLiveData()
    private var facultyID:Int=-1
    private var _faculty:Faculty?=null
     val faculty:Faculty?
         get()=_faculty
    init{
        FacultyRepository.get().faculty.observeForever{
            faculty_groups.postValue(it)
        }
    }
    fun setFacultyID(facultyID:Int){
        this.facultyID=facultyID
        loadGroups()
    }
    private fun loadGroups(){
        viewModelScope.launch {
            FacultyRepository.get().getFacultyGroups(facultyID)
        }
    }
    suspend fun getFaculty():Faculty?{
        var f: Faculty?=null
        val job=viewModelScope.launch {
            f=FacultyRepository.get().getFaculty(facultyID)
        }
        job.join()
        return f
    }
}