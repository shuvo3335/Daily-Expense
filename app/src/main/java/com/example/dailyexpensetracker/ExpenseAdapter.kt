package com.example.dailyexpensetracker

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupMenu
import android.widget.Spinner
import android.widget.TextView
import androidx.collection.emptyLongSet
import androidx.recyclerview.widget.RecyclerView

class ExpenseAdapter(val expenseList: List<ExpenseDataModel>,
                     val onEdit: (ExpenseDataModel)-> Unit,
                     val onDelete: (ExpenseDataModel)-> Unit) :
    RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_expense,parent,false)
        return ExpenseViewHolder(view)
    }

    override fun getItemCount(): Int {
        return expenseList.size
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {

        val expense = expenseList[position]
        holder.expense_amonut.text = "Amount: ${expense.amount} Taka"
        holder.expense_category.text = "Category: ${expense.category}"
        holder.expense_date.text = "Date: ${expense.date}"
        holder.expense_description.text = expense.description ?: "No description"
        holder.button_action.setOnClickListener {
            showPopUpManue(it,expense)
        }
    }

    class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val expense_amonut : TextView = itemView.findViewById(R.id.tvAmount)
        val expense_category : TextView = itemView.findViewById(R.id.tvCategory)
        val expense_date: TextView = itemView.findViewById(R.id.tvDate)
        val expense_description: TextView = itemView.findViewById(R.id.tvDescription)
        val button_action: Button = itemView.findViewById(R.id.btnActions)
    }

    private fun showPopUpManue(view: View, expense: ExpenseDataModel) {
        val popupMenu = PopupMenu(view.context,view)
        popupMenu.menuInflater.inflate(R.menu.menu_expense_actions,popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menueItem: MenuItem ->
            when (menueItem.itemId){
                R.id.action_edit -> {
                    onEdit(expense)
                    true
                }
                R.id.action_delete->{
                    onDelete(expense)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }
}

