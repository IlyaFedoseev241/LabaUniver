package com.example.laba222.Data

import androidx.room.*
import java.util.*

@Entity(tableName = "faculty",
    indices = [Index("group_name")],
    foreignKeys = [
        ForeignKey(
            entity = Faculty::class,
            parentColumns = ["id"],
            childColumns = ["faculty_id"]
        )
    ]
)
data class Group(
    @PrimaryKey(autoGenerate = true) val id:Int?,
    @ColumnInfo(name="group_name")val name:String?,
    @ColumnInfo(name="faculty_id")val facultyID:Int
)

