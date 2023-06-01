package com.example.laba222.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.laba222.Data.Faculty
import com.example.laba222.Data.Group
import com.example.laba222.Data.Student
import com.example.laba222.dao.UniversityDAO


@Database(
    version = 1,
    entities = [
        Faculty::class,
    Group::class,
    Student::class
    ]
)
abstract class UniversityDatabase :RoomDatabase(){
    abstract fun getDao(): UniversityDAO
}