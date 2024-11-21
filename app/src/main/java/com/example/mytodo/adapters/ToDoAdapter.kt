package com.example.mytodo.adapters

import android.content.Context
import android.text.InputType
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.mytodo.MyActivityData
import com.example.mytodo.R
import com.example.mytodo.database.entities.Todo
import com.example.mytodo.database.repository.TodoRepository
import com.example.mytodo.viewHolders.ToDoViewHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ToDoAdapter(items:List<Todo>,
                  repository: TodoRepository,
                  viewModel: MyActivityData):RecyclerView.Adapter<ToDoViewHolder>(){
            var context: Context? = null

    val items = items
    val repository = repository
    val viewModel = viewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_item,parent,false)
        context = parent.context
        return ToDoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        //aluthen

        holder.cbTodo.text = items.get(position).item

        // Edit function
        holder.ivEdit.setOnClickListener {
            val currentItem = items[position]

            // Display a dialog for editing the item
            val builder = AlertDialog.Builder(context!!)
            builder.setTitle("Edit Todo item:")
            builder.setMessage("Modify the todo item below:")

            val input = EditText(context)
            input.inputType = InputType.TYPE_CLASS_TEXT
            input.setText(currentItem.item)
            builder.setView(input)

            builder.setPositiveButton("OK") { dialog, _ ->
                val updatedItem = input.text.toString()
                currentItem.item = updatedItem

                CoroutineScope(Dispatchers.IO).launch {
                    repository.update(currentItem)

                    // Update the view with the latest data
                    val data = repository.getAllTodoItems()
                    withContext(Dispatchers.Main) {
                        viewModel.setData(data)
                    }
                }
                Toast.makeText(context, "Item Updated", Toast.LENGTH_LONG).show()
            }
            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }

            val alertDialog = builder.create()
            alertDialog.show()
        }
        //delete function
        holder.ivDelete.setOnClickListener{
            val isChecked = holder.cbTodo.isChecked
            if(isChecked){
                CoroutineScope(Dispatchers.IO).launch{
                    repository.delete(items.get(position))

                    val data = repository.getAllTodoItems()
                    withContext(Dispatchers.Main){
                        viewModel.setData(data)
                    }
            }
                Toast.makeText(context,"Item Deleted",Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(context,"Select the item to delete",Toast.LENGTH_LONG).show()

            }
        }




    }


}