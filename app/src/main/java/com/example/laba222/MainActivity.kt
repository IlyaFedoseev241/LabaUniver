package com.example.laba222

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentTransaction
import com.example.laba222.Data.Student
import com.example.laba222.repository.FacultyRepository
import com.example.laba222.ui.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import java.util.*



class MainActivity() : AppCompatActivity(),GroupFragment.Callbacks,GroupListFragment.Callbacks,FacultyFragment.Callbacks{

    private var mItemFaculty: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameMain,FacultyFragment.newInstance(), FACULTY_TAG)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
        onBackPressedDispatcher.addCallback(this,object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if(supportFragmentManager.backStackEntryCount>0){
                    supportFragmentManager.popBackStack()
                }else
                    finish()
            }
        })

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        mItemFaculty = menu?.findItem(R.id.mItemFacultyGroup)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.mItemFacultyGroup -> {
                val myFragment=supportFragmentManager.findFragmentByTag(GROUP_TAG)
                if(myFragment==null)
                    showNameInputDialog(0)
                else
                    showNameInputDialog(1)
                true
            }
            else->super.onOptionsItemSelected(item)
        }
    }
    private fun showNameInputDialog(index:Int=-1){
        val builder=AlertDialog.Builder(this)
        builder.setCancelable(true)
        val dialogView=LayoutInflater.from(this).inflate(R.layout.input_name,null)
        builder.setView(dialogView)
        val nameInput=dialogView.findViewById(R.id.editName) as EditText
        val tvInfo=dialogView.findViewById(R.id.tvInfo) as TextView
        when(index){
            0 -> {
                tvInfo.text=getString(R.string.inputFaculty)
                builder.setPositiveButton(getString(R.string.commit)){ _, _ ->
                    val s=nameInput.text.toString()
                    if(s.isNotBlank()){
                      //  FacultyRepository.get().newFaculty(s)

                        CoroutineScope(Dispatchers.Main).launch {
                            FacultyRepository.get().newFaculty(s)
                        }
                    }
                }
            }
            1 -> {
                tvInfo.text=getString(R.string.inputGroup)
                builder.setPositiveButton("Подтверждение"){_, _ ->
                    val s=nameInput.text.toString()
                    if(s.isNotBlank()){
                        CoroutineScope(Dispatchers.Main).launch {
                            FacultyRepository.get().newGroup(GroupFragment.getFacultyID,s)                        }
                    }

                }

            }
        }
        builder.setNegativeButton("отмена",null)
        val alert=builder.create()
        alert.show()
    }

    override fun setTitle(_title: String) {
        title=_title
    }

    override fun showStudent(groudID:UUID,student: Student?) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameMain, StudentFragment.newInstance(groudID ,student),STUDENT_TAG)
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()

    }

    override fun showGroupFragment(facultyID: Int?) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameMain,GroupFragment.newInstance(facultyID),GROUP_TAG)
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }

    override fun onStop() {
        //FacultyRepository.get().saveFaculty()
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        //FacultyRepository.get().loadingFaculty()
    }

}