package com.team6.connectbca.ui.fragment.adapter

import android.app.DatePickerDialog
import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.view.isVisible
import java.text.SimpleDateFormat
import java.util.*

class SpinnerAdapter(
    private val context: Context,
    private val transferFrequencySpinner: Spinner,
    private val scheduleTransferSpinner: Spinner,
    private val startDateSpinner: Spinner,
    private val endDateSpinner: Spinner
) {
    private val frequencyOptions = arrayOf("Frekuensi Transfer", "Satu Kali", "Mingguan", "Bulanan")
    private val scheduleTransferOptionsOneTime = arrayOf("Pilih Jadwal Transfer", "Pilih Tanggal")
    private val scheduleTransferOptionsWeekly = arrayOf("Pilih Jadwal Transfer", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu")
    private val scheduleTransferOptionsMonthly = (1..31).map { it.toString() }.toTypedArray()

    init {
        initializeSpinners()
        setupTransferFrequencySpinner()
    }

    private fun initializeSpinners() {
        setupSpinner(transferFrequencySpinner, frequencyOptions)
        setupSpinner(scheduleTransferSpinner, arrayOf("Pilih Jadwal Transfer"))
        setupSpinner(startDateSpinner, arrayOf("Pilih Tanggal Mulai"))
        setupSpinner(endDateSpinner, arrayOf("Pilih Tanggal Akhir"))
    }

    private fun setupSpinner(spinner: Spinner, options: Array<String>) {
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spinner.adapter = adapter
    }

    private fun setupTransferFrequencySpinner() {
        transferFrequencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                handleFrequencySelection(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun handleFrequencySelection(position: Int) {
        when (position) {
            1 -> {
                scheduleTransferSpinner.isVisible = true
                startDateSpinner.isVisible = false
                endDateSpinner.isVisible = false
                setupSpinner(scheduleTransferSpinner, scheduleTransferOptionsOneTime)
                setupDatePickerForOneTimeTransfer()
            }
            2 -> {
                scheduleTransferSpinner.isVisible = true
                startDateSpinner.isVisible = true
                endDateSpinner.isVisible = true
                setupSpinner(scheduleTransferSpinner, scheduleTransferOptionsWeekly)
                setupDatePickers()
            }
            3 -> {
                scheduleTransferSpinner.isVisible = true
                startDateSpinner.isVisible = true
                endDateSpinner.isVisible = true
                setupSpinner(scheduleTransferSpinner, scheduleTransferOptionsMonthly)
                setupDatePickers()
            }
            else -> {
                scheduleTransferSpinner.isVisible = false
                startDateSpinner.isVisible = false
                endDateSpinner.isVisible = false
            }
        }
    }

    private fun setupDatePickers() {
        setupDatePicker(startDateSpinner, "Pilih Tanggal Mulai")
        setupDatePicker(endDateSpinner, "Pilih Tanggal Akhir")
    }

    private fun setupDatePickerForOneTimeTransfer() {
        scheduleTransferSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position == 1) { // Pilih Tanggal
                    showDatePickerDialog(scheduleTransferSpinner, "Pilih Tanggal")
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupDatePicker(spinner: Spinner, title: String) {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position == 0) {
                    showDatePickerDialog(spinner, title)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun showDatePickerDialog(spinner: Spinner, title: String) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(calendar.time)
                updateSpinnerWithSelectedDate(spinner, formattedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            setTitle(title)
            show()
        }
    }

    private fun updateSpinnerWithSelectedDate(spinner: Spinner, date: String) {
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, arrayOf(date))
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spinner.adapter = adapter
    }
}
