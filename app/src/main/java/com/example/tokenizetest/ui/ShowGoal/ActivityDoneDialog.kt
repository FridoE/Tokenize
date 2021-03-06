// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright © 2020 F. Engel
package com.example.tokenizetest.ui.showgoal

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class ActivityDoneDialog(var activityVM: TokenizedActivityViewModel, var showGoalVM: ShowGoalViewModel) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Have you done this activity?")
                .setPositiveButton("Yes",
                    DialogInterface.OnClickListener { dialog, id ->
                        showGoalVM.doneActivity(activityVM)
                    })
                .setNegativeButton("No",
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                    })
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}