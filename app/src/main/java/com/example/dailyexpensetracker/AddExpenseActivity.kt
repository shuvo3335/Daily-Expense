package com.example.dailyexpensetracker

import android.app.DatePickerDialog
import android.health.connect.datatypes.units.Length
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dailyexpensetracker.databinding.ActivityAddExpenseBinding
import java.util.Calendar

class AddExpenseActivity : AppCompatActivity() {

    lateinit var expenseDatabaseHelper: ExpenseDatabaseHelper
    lateinit var Expensebinding: ActivityAddExpenseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        Expensebinding = ActivityAddExpenseBinding.inflate(layoutInflater)
        setContentView(Expensebinding.root)

        expenseDatabaseHelper = ExpenseDatabaseHelper(this)

        //setup Spinner

        val categories = listOf("Food","Bazar","Medicine","Transport","Fuel","Bike Service","Mobil","Shopping","House Rent",
            "Utility Bill","Mobile Bill","Gadgets","Others")
        val adapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,categories)
        Expensebinding.spinnerCategory.adapter = adapter

        //DatePickerDialog setup

        val calender = Calendar.getInstance()
        Expensebinding.tvDate.setOnClickListener {
            DatePickerDialog(this,
                { _, year, month, dayOfMonth ->
                    Expensebinding.tvDate.text = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
                },
                calender.get(Calendar.YEAR),
                calender.get(Calendar.MONTH),
                calender.get(Calendar.DAY_OF_MONTH)
                ).show()
        }

        val expenseID = intent.getIntExtra("expense_id",-1)
        if (expenseID != -1){
            val expense = expenseDatabaseHelper.getExpenseById(expenseID)
//            Expensebinding.etAmount.setText(expense.amount.toString())
            Expensebinding.etAmount.setText(expense?.amount.toString())
            Expensebinding.spinnerCategory.setSelection((Expensebinding.spinnerCategory.adapter as ArrayAdapter<String>).getPosition(expense?.category))
            Expensebinding.tvDate.text = expense?.date
            Expensebinding.etDescription.setText(expense?.description)
        }

        Expensebinding.btnSubmit.setOnClickListener {

            val amount = Expensebinding.etAmount.text.toString().toDoubleOrNull()
            val category = Expensebinding.spinnerCategory.selectedItem.toString()
            val date = Expensebinding.tvDate.text.toString()
            val description = Expensebinding.etDescription.text.toString()

            if (amount!=null && date.isNotEmpty()){

                val expenseDataModel = ExpenseDataModel(
                    id = expenseID.takeIf { it != -1 } ?: 0, // Use ID for updates
                    amount = amount,
                    category = category,
                    date = date,
                    description = description
                )
                if (expenseID == -1) {
                    expenseDatabaseHelper.addExpense(expenseDataModel)
                    Toast.makeText(this, "Expense Added", Toast.LENGTH_SHORT).show()
                } else {
                    expenseDatabaseHelper.updateExpense(expenseDataModel)
                    Toast.makeText(this, "Expense Updated", Toast.LENGTH_SHORT).show()
                }
                finish()
            }
            else Toast.makeText(this,"please fill all the fields",Toast.LENGTH_SHORT).show()
        }
    }
}