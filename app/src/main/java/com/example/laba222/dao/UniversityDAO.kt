package com.example.laba222.dao

import androidx.room.*
import com.example.laba222.Data.Faculty
import com.example.laba222.Data.Group
import com.example.laba222.Data.Student

@Dao
interface UniversityDAO {
    @Insert(entity = Faculty::class)
    fun insertNewFaculty(faculty: Faculty)

    @Query("DELETE FROM university")
    fun deleteAllFaculty()

    @Query("DELETE FROM university WHERE id = :facultyID")
    fun deleteFacultyByID(facultyID:Long)

    @Delete(entity = Faculty::class)
    fun deleteFaculty(faculty: Faculty)

    @Query("SELECT id, faculty_name FROM university order by faculty_name")
    fun loadFaculty():List<Faculty>

    @Query("SELECT id, faculty_name FROM university where id=:id")
    fun getFaculty(id:Int):Faculty

    @Update(entity = Faculty::class)
    fun updateFaculty(faculty: Faculty)

    @Insert(entity = Group::class)
    fun insertNewGroup(group: Group)

    @Delete(entity = Group::class)
    fun deleteGroup(group: Group)

    @Query("SELECT * FROM faculty order by group_name")
    fun loadGroup():List<Group>

    @Query("SELECT * FROM faculty where faculty_id =:faculty_id order by group_name")
    fun loadFacultyGroup(faculty_id:Int):List<Group>

    @Query("SELECT * FROM faculty where faculty_id=:facultyID order by group_name")
    fun loadGroup(facultyID:Int):List<Group>


    @Update(entity = Group::class)
    fun updateGroup(group: Group)

    @Insert(entity = Student::class)
    fun insertNewGroup(student: Student)

    @Query("DELETE FROM university WHERE id = :studentID")
    fun deleteStudentByID(studentID:Long)

    @Delete(entity = Student::class)
    fun deleteStudent(student: Student)

    @Query("SELECT * FROM student order by last_name")
    fun loadStudent():List<Student>

    @Update(entity = Student::class )
    fun updateStudent(student: Student)
}