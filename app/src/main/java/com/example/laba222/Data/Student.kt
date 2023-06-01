package com.example.laba222.Data

import android.provider.ContactsContract
import androidx.room.*
import java.util.*


@Entity(
    indices = [Index("last_name","first_name","middle_name")],
    foreignKeys = [
        ForeignKey(
            entity = Faculty::class,
            parentColumns = ["id"],
            childColumns = ["group_id"]
        )
    ]
)
data class Student(
    @PrimaryKey(autoGenerate = true) val id:Int,
    @ColumnInfo(name="first_name")val firstName:String?,
    @ColumnInfo(name="last_name")val lastName:String?,
    @ColumnInfo(name="middle_name")val middleName:String?,
    val phone:String?,
    @ColumnInfo(name="birth_date")val birthDate:String?,
    @ColumnInfo(name="group_id")val groupID:Int
)
