package com.example.hoopstats

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var editText: EditText

    fun setEditText(editText: EditText) {
        this.editText = editText
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Ustawia domyślną datę jako bieżącą przy tworzeniu instancji DatePickerDialog
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(requireContext(), this, year, month, day)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        // Ustawia wybraną datę w polu EditText
        val selectedDate = "$year-${month + 1}-$dayOfMonth"
        editText.setText(selectedDate)
    }
}

