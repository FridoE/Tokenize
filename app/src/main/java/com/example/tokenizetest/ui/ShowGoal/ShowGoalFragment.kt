// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright © 2020 F. Engel
package com.example.tokenizetest.ui.showgoal

import android.graphics.drawable.Icon
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tokenizetest.R
import com.example.tokenizetest.data.Goal
import com.example.tokenizetest.databinding.GoalFragmentBinding
import com.example.tokenizetest.ui.main.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.goal_fragment.*

class ShowGoalFragment : Fragment() {
    private lateinit var showgoalViewModel: ShowGoalViewModel
    private lateinit var binding: GoalFragmentBinding
    private lateinit var rootView: View
    val args: ShowGoalFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = GoalFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        var test: MutableLiveData<Boolean> = MutableLiveData(false)
        rootView = binding.root //inflater.inflate(R.layout.goal_fragment, container, false)
        val listView = rootView.findViewById<RecyclerView>(R.id.activityList)
        val activityHistoryView = rootView.findViewById<RecyclerView>(R.id.activityHistoryList)
        val activityListAdapter =
            ShowGoalActivityListAdapter({ avm: TokenizedActivityViewModel ->
                onClickActivity(avm)
            })
        val activityHistoryListAdapter =
            ActivityHistoryListAdapter({ ahlivm: ActivityHistoryListItemVM ->
                onClickRemoveActivityHistoryItem(ahlivm)
            }, { ahlivm: ActivityHistoryListItemVM ->
                onLongClickActivityHistoryItem(ahlivm)
            }, test)//TODO: livedata

        showgoalViewModel = activity?.run {
            ViewModelProvider(this,
                ShowGoalViewModelFactory(
                    this.application,args.goalID)).get(ShowGoalViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        // TODO: implement delete history items

        binding.gvm = showgoalViewModel
        showgoalViewModel.activityList.observe(this.viewLifecycleOwner, Observer { list -> activityListAdapter.submitList(list)
        binding.invalidateAll()
        })
        showgoalViewModel.activityHistoryList.observe(this.viewLifecycleOwner, Observer { list -> activityHistoryListAdapter.submitList(list)
            //activityHistoryListAdapter.notifyItemInserted(activityHistoryListAdapter.itemCount-1)
            activityHistoryListAdapter.notifyDataSetChanged()
            binding.invalidateAll()
            Log.d("deleteHI", "showgoalfragment")
        })

        showgoalViewModel.goalReached.observe(this.viewLifecycleOwner, Observer { if(it) onGoalReached()})

        listView.layoutManager = LinearLayoutManager(this.context)
        listView.adapter = activityListAdapter
        activityHistoryView.layoutManager = LinearLayoutManager(this.context)
        activityHistoryView.adapter = activityHistoryListAdapter

        binding.imageIconGoal.setImageIcon(getIconFromResName(showgoalViewModel.goalIconName, requireContext()))

        return rootView
    }

    fun onClickActivity(activityVM: TokenizedActivityViewModel) {
        ActivityDoneDialog(
            activityVM,
            showgoalViewModel
        ).show(childFragmentManager, "f")
    }

    fun onGoalReached() {
        Snackbar.make(rootView, "Congratulations! You have reached your goal!", Snackbar.LENGTH_INDEFINITE)
            .setAction("OK") {
                // Responds to click on the action
            }
            .show()
    }

    fun onClickRemoveActivityHistoryItem(activityHLIVM: ActivityHistoryListItemVM) {
        showgoalViewModel.removeActivityHistoryItem(activityHLIVM)
    }

    fun onLongClickActivityHistoryItem(activityHLIVM: ActivityHistoryListItemVM): Boolean {
        return true
    }

}