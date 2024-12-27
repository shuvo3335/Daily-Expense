package com.example.dailyexpensetracker

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyexpensetracker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var expenseDatabaseHelper: ExpenseDatabaseHelper
    lateinit var expenseAdapter: ExpenseAdapter
    lateinit var expenseDataModel: ExpenseDataModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        expenseDatabaseHelper = ExpenseDatabaseHelper(this)
        binding.recyclerViewExpenses.layoutManager = LinearLayoutManager(this)
        loadExpense()

        binding.btnAddExpense.setOnClickListener {
            val intent = Intent(this,AddExpenseActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadExpense()
    }

    private fun loadExpense() {
        val getExpense = expenseDatabaseHelper.getAllExpense()
        expenseAdapter = ExpenseAdapter(getExpense,
            onEdit = {
                expenseDataModel->
                val intent = Intent(this,AddExpenseActivity::class.java)
                intent.putExtra("expense_id",expenseDataModel.id)
                startActivity(intent)
            },
            onDelete = {
                expenseDataModel->
                expenseDatabaseHelper.deleteExpense(expenseDataModel.id)
                loadExpense()
                Toast.makeText(this,"expense deleted",Toast.LENGTH_SHORT).show()
            })
        binding.recyclerViewExpenses.adapter = expenseAdapter
    }
}