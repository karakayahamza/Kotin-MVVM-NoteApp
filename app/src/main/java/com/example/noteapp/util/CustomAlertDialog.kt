package com.example.noteapp.util

import android.app.AlertDialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.example.noteapp.databinding.CustomAlertdialogBinding

class CustomAlertDialog(private val context: Context,
                        private val onOkButtonClickListener: () -> Unit) {

    fun showDialog(message: String) {
        val binding = CustomAlertdialogBinding.inflate(LayoutInflater.from(context))
        val dialogBuilder = AlertDialog.Builder(context)
            .setView(binding.root)

        val alertDialog = dialogBuilder.create()

        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.setCancelable(false)

        binding.messageTextView.text = message

        binding.noDelete.setOnClickListener {
            alertDialog.dismiss()
        }

        binding.delete.setOnClickListener {
            onOkButtonClickListener.invoke()
            alertDialog.dismiss()
        }

        alertDialog.show()

        alertDialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        alertDialog.window?.setGravity(Gravity.BOTTOM)
    }
}