package com.example.laba222.ui

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.laba222.Data.Faculty
import com.example.laba222.Data.Group
import com.example.laba222.Data.Student
import com.example.laba222.R
import com.example.laba222.databinding.FragmentGroupBinding
import com.example.laba222.databinding.FragmentGroupListBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID
import javax.security.auth.callback.Callback
const val GROUP_TAG="GroupFragment"
class GroupFragment private constructor(): Fragment() {

    private var _binding:FragmentGroupBinding?=null
    private val binding get()=_binding!!

    companion object {
        private var _facultyID: Int=-1
        fun newInstance(facultyID: Int?): GroupFragment {
            _facultyID = facultyID?: -1
            return GroupFragment()
        }
        val getFacultyID
        get()= _facultyID
    }

        private lateinit var viewModel: GroupViewModel

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            _binding = FragmentGroupBinding.inflate(inflater, container, false)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            viewModel = ViewModelProvider(this).get(GroupViewModel::class.java)
           // viewModel.setFacultyID(_facultyID)
            viewModel.faculty_groups.observe(viewLifecycleOwner) {
                updateUI(it)
                CoroutineScope(Dispatchers.Main).launch {
                    val f=viewModel.getFaculty()
                    callbacks?.setTitle(f?.name?:"UNKNOWN")
                }
            }
viewModel.setFacultyID(_facultyID)
        }

    private var tabPosition :Int=0
        private fun updateUI(groups:List<Group>) {
            binding.tabGroup.clearOnTabSelectedListeners()
            binding.tabGroup.removeAllTabs()
            binding.faAddNewStudent.visibility=if((groups?.size?:0)>0) {
                binding.faAddNewStudent.setOnClickListener{
                   // callbacks?.showStudent(faculty?.groups!![tabPosition].id,null)
                }
                View.VISIBLE
            }else
                                                   View.GONE
            for (i in 0  until  (groups?.size?: 0)){
                binding.tabGroup.addTab(binding.tabGroup.newTab().apply {
                    text = i.toString()
                })
            }



            val adapter = GroupPageAdapter(requireActivity(), groups)
            binding.vpGroup.adapter=adapter
            TabLayoutMediator(binding.tabGroup,binding.vpGroup,true,true){
               tab,pos -> tab.text= groups?.get(pos)!!.name
            }.attach()
            binding.tabGroup.selectTab(binding.tabGroup.getTabAt(tabPosition),true)
            binding.tabGroup.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tabPosition=tab?.position!!
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }

                override fun onTabReselected(tab: TabLayout.Tab?) {

                }
            })
        }

    inner class GroupPageAdapter(fa: FragmentActivity, private val groups: List<Group>):FragmentStateAdapter(fa) {
        override fun getItemCount(): Int {
            return (groups.size)
        }

        override fun createFragment(position: Int): Fragment {
               return GroupListFragment(groups[position])

        }
    }


    interface Callbacks{
        fun setTitle(title:String)
        fun showStudent(groupID:UUID,student: Student?)
    }
    var callbacks:Callbacks?=null

    override fun onAttach(context: Context){
        super.onAttach(context)
        callbacks=context as Callbacks

    }

    override fun onDetach() {
        callbacks=null
        super.onDetach()
    }
    override fun onDestroy(){
        super.onDestroy()
        _binding=null
    }

}