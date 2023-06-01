package com.example.laba222.Data


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName="university")
data class Faculty(
    @PrimaryKey(autoGenerate = true) val id:Int?,
    @ColumnInfo(name="faculty_name") val name:String?
)
class Faculties{
    @SerializedName("items")
    lateinit var items:List<Faculty>
}
