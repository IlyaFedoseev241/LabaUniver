package com.example.laba222.ui

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import com.example.laba222.Data.Student
import com.example.laba222.R
import com.example.laba222.StudentViewModel
import com.example.laba222.databinding.FragmentStudentBinding
import com.example.laba222.repository.FacultyRepository
import java.lang.reflect.Array.set
import java.util.*

const val STUDENT_TAG="StudentFragment"
class StudentFragment : Fragment() {

    private var _binding:FragmentStudentBinding?=null
    private val binding get()=_binding!!
    companion object {
        private lateinit var groudID:UUID
        private var student:Student?=null
        fun newInstance(groudID:UUID,student: Student?): StudentFragment{
            this.student= student
            this.groudID=groudID
            return StudentFragment()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentStudentBinding.inflate(inflater,container,false)
        return binding.root
    }


    private lateinit var viewModel:StudentViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(student!=null){
            binding.etName.setText(student!!.firstName)
            binding.etSurname.setText(student!!.lastName)
            binding.etPapa.setText(student!!.middleName)
            binding.etNumber.setText(student!!.phone.toString())
            val dt=GregorianCalendar().apply {

            }
            binding.dpCalendar.init(dt.get(Calendar.YEAR),dt.get(Calendar.MONTH),dt.get(Calendar.DAY_OF_MONTH),null)

        }
        viewModel=ViewModelProvider(this).get(StudentViewModel::class.java)

    }
    val backPressedCallback=object :OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            //showCommitDialog()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(this,backPressedCallback)
    }
/*
    private fun showCommitDialog(index:Int=-1) {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setCancelable(true)
        builder.setMessage("Сохранить измениния?")
        builder.setTitle("Подтверждение")
        builder.setPositiveButton("Подтвержение") { _, _ ->
            var p:Boolean=true
            binding.etName.text.toString().ifBlank{
                p=false
                binding.etName.error="Укажите значение"
            }
            binding.etSurname.text.toString().ifBlank{
                p=false
                binding.etSurname.error="Укажите значение"
            }
            binding.etPapa.text.toString().ifBlank{
                p=false
                binding.etPapa.error="Укажите значение"
            }
            binding.etNumber.text.toString().ifBlank{
                p=false
                binding.etNumber.error="Укажите значение"
            }


            if(GregorianCalendar().get(GregorianCalendar.YEAR)-binding.dpCalendar.year<10){
                p=false
                Toast.makeText(context,"Укажите правильный возраст",Toast.LENGTH_SHORT).show()
            }
            if(p){
                var selectedDate=GregorianCalendar().apply {
                    set(GregorianCalendar.YEAR,binding.dpCalendar.year)
                    set(GregorianCalendar.MONTH,binding.dpCalendar.month)
                    set(GregorianCalendar.DAY_OF_MONTH,binding.dpCalendar.dayOfMonth)
                }

                if(student==null){
                    student= Student()
                    student?.apply{
                        firstName = binding.etName.text.toString()
                        lastName = binding.etSurname.text.toString()
                        middleName = binding.etPapa.text.toString()
                        phone = binding.etNumber.text.toString()
                        birthData = selectedDate.time
                    }
                    viewModel.newStudent(groudID!!, student!!)
                }else{
                    student?.apply {
                        firstName = binding.etName.text.toString()
                        lastName = binding.etSurname.text.toString()
                        middleName = binding.etPapa.text.toString()
                        phone = binding.etNumber.text.toString()
                        birthData = selectedDate.time
                    }
                    viewModel.editStudent(groudID!!,student!!)
                }
                backPressedCallback.isEnabled=false
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
        builder.setNegativeButton("отмена") { _, _ ->
            backPressedCallback.isEnabled=false
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        val alert = builder.create()
        alert.show()
    }

 */
}