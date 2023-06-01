package com.example.laba222.repository

import android.content.SharedPreferences
import android.telecom.Call
import android.util.DisplayMetrics
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import androidx.room.Room
import com.example.laba222.Data.*
import com.example.laba222.Laba222aplication
import com.example.laba222.api.ServerApi
import com.example.laba222.dao.UniversityDAO

import com.example.laba222.database.UniversityDatabase
import com.google.gson.Gson
import kotlinx.coroutines.*
import okhttp3.Callback
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class FacultyRepository private constructor(){
    var university: MutableLiveData<List<Faculty>> = MutableLiveData()
    var faculty: MutableLiveData<List<Group>> = MutableLiveData()

    companion object{
        private var INSTANCE: FacultyRepository?=null
        fun newInstance(){
            if(INSTANCE==null){
                INSTANCE= FacultyRepository()
            }
        }
        fun get():FacultyRepository{
            return INSTANCE?:
            throw IllegalStateException("Репозиторий FacultyRepository не инициализирован")
        }
    }

    val db = Room.databaseBuilder(
        Laba222aplication.applicationContext(),
        UniversityDatabase::class.java,"uniDB.db"
    ).build()

    val universityDao=db.getDao()

    suspend fun newFaculty(name:String){
        val faculty=Faculty(id=null,name=name)
        withContext(Dispatchers.IO){
            universityDao.insertNewFaculty(faculty)
            university.postValue(universityDao.loadFaculty())
        }
    }
    suspend fun loadFaculty() {
        withContext(Dispatchers.IO) {
            university.postValue(universityDao.loadFaculty())
        }
    }
    suspend fun newGroup(facultyID:Int,name:String){
        val group=Group(id=null, facultyID = facultyID,name=name)

        withContext(Dispatchers.IO){
            universityDao.insertNewGroup(group)
            faculty.postValue(universityDao.loadGroup(facultyID))
        }
    }
    suspend fun getFacultyGroups(facultyID:Int){
        withContext(Dispatchers.IO){
            faculty.postValue(universityDao.loadGroup(facultyID))
        }
    }
    suspend fun getFaculty(facultyID:Int):Faculty?{
        var f:Faculty?=null
        val job= CoroutineScope(Dispatchers.IO).launch{
            f=universityDao.getFaculty(facultyID)
        }
        job.join()
        return f
    }
    private var myServerAPI: ServerApi?=null

    private val client = OkHttpClient.Builder()
        .connectTimeout(15,TimeUnit.SECONDS)
        .readTimeout(15,TimeUnit.SECONDS)
        .writeTimeout(15,TimeUnit.SECONDS)
        .build()
    private fun getAPI(){
        val url="62.183.54.90.14871"
        Retrofit.Builder()
            .baseUrl("http://${url}")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build().apply {
                myServerAPI=create(ServerApi::class.java)
            }
    }

    fun getServerFaculty(){
        if(myServerAPI==null)getAPI()
        CoroutineScope(Dispatchers.Main).launch {
            fetchFaculty()
        }
    }

    private suspend fun fetchFaculty() {

        if(myServerAPI!=null){
            val job= CoroutineScope(Dispatchers.IO).launch {
                val r=myServerAPI!!.getFaculty().execute()
                if(r.isSuccessful){
                    val job=CoroutineScope(Dispatchers.IO).launch {
                        universityDao.deleteAllFaculty()
                    }
                    job.join()
                    universityDao.deleteAllFaculty()
                    val facultyList=r.body()
                    if(facultyList!=null){
                        for(f in facultyList){
                            universityDao.insertNewFaculty(f)
                        }
                    }
                }
            }
            job.join()
            universityDao.loadFaculty()
        }
    }

    /*
    fun newFaculty(name:String){
        val faculty=Faculty(name=name)
        val list : ArrayList<Faculty>
        if(university.value!=null){
            list=(university.value as ArrayList<Faculty>)
        }
        else
            list=ArrayList<Faculty>()
        list.add(faculty)
        university.postValue(list)
    }
    fun newGroup(facultyID: UUID,name:String){
        if(university.value==null)return
        val u=university.value!!
        val faculty=u.find{it.id==facultyID} ?: return
        val group=Group(name=name)
        val list : ArrayList<Group> = if(faculty.groups.isEmpty()) ArrayList()
                                       else (faculty.groups as ArrayList<Group>)
        list.add(group)
        faculty.groups=list
        university.postValue(u)
    }
    fun newStudent(groupID: UUID,student:Student){
        Log.d("TAG","$groupID $student")

        val u=university.value?:return
        val faculty=u.find{it.groups.find{it.id==groupID}!=null } ?: return
        val group=faculty.groups.find{it.id==groupID}
        val list : ArrayList<Student> = if(group!!.students.isEmpty())
            ArrayList()
        else
            group.students as ArrayList<Student>
        list.add(student)
        group.students=list
        Log.d("TAG","$list$")
        Log.d("TAG","${group.students}$")
        university.postValue(u)
    }
    fun deleteStudent(groupID: UUID,student:Student){
        Log.d("TAG","$groupID $student")

        val u=university.value?:return
        val faculty=u.find{it.groups.find{it.id==groupID}!=null } ?: return
        val group=faculty.groups.find{it.id==groupID}
        val list : ArrayList<Student> = if(group!!.students.isEmpty())
           return
        else
            group.students as ArrayList<Student>
        list.remove(student)
        group.students=list
        Log.d("TAG","$list$")
        Log.d("TAG","${group.students}$")
        university.postValue(u)
    }
    fun editStudent(groupID: UUID,student:Student){
        val u=university.value?:return
        val faculty=u.find{it.groups.find{it.id==groupID}!=null } ?: return
        val group=faculty.groups.find{it.id==groupID}?:return
        val _student=group.students.find{it.id==student.id}
        if(_student==null){
            newStudent(groupID,student)
            return
        }
        val list=group.students as ArrayList<Student>
        val i=list.indexOf(_student)
        list.remove(student)
        list.add(i,student)
        group.students=list
        university.postValue(u)
    }
    fun saveFaculty(){
        if(university.value==null)return
        val u=University(university.value!!)
        val s= Gson().toJson(u)
        val context=Laba222aplication.applicationContext()
        val sharedPreferences=PreferenceManager.getDefaultSharedPreferences(context)
        sharedPreferences.edit().apply {
            putString("university",s)
            apply()
        }
    }
    fun loadingFaculty(){
        val context=Laba222aplication.applicationContext()
        val sharedPreferences=PreferenceManager.getDefaultSharedPreferences(context)
        val s=sharedPreferences.getString("university","")
        if(s.isNullOrBlank())return
        val u=Gson().fromJson(s,University::class.java)
        university.postValue(u.items)
    }

     */

}