package com.example.mytodo

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mytodo.adapters.ToDoAdapter
import com.example.mytodo.database.TodoDatabase
import com.example.mytodo.database.entities.Todo
import com.example.mytodo.database.repository.TodoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->

            val button1: Button =findViewById(R.id.button_home_2)
            button1.setOnClickListener{
                val btn1= Intent(this@Home,CountDown::class.java)
                startActivity(btn1)

            }
            val button2: Button =findViewById(R.id.home_btn_3)
            button2.setOnClickListener{
                val btn2= Intent(this@Home,CountDown::class.java)
                startActivity(btn2)

            }
            val button3: Button =findViewById(R.id.home_btn_4)
            button3.setOnClickListener{
                val btn3= Intent(this@Home,Welcome::class.java)
                startActivity(btn3)

            }
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val viewModel = ViewModelProvider(this)[MyActivityData::class.java]
        val repository=TodoRepository(TodoDatabase.getInstance(this))


        val rvTodoList:RecyclerView =findViewById(R.id.rvTodoList)

        viewModel.data.observe(this){
            val adapter=ToDoAdapter(it, repository,viewModel)
            rvTodoList.adapter=adapter
            rvTodoList.layoutManager=LinearLayoutManager(this)
        }
        CoroutineScope(Dispatchers.IO).launch{
            val data = repository.getAllTodoItems()
            runOnUiThread {
                viewModel.setData(data)
            }
        }

        val btnAddItem:Button=findViewById(R.id.btnAddItem)
        btnAddItem.setOnClickListener {
            displayDialog(repository,viewModel)
        }


    }

    fun displayDialog(repository:TodoRepository,viewModel:MyActivityData){
        val builder = AlertDialog.Builder(this)

        // Set the alert dialog title and message
        builder.setTitle("Enter New Todo item:")
        builder.setMessage("Enter the todo item below:")

        // Create an EditText input field
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        // Set the positive button action
        builder.setPositiveButton("OK"){dialog, which ->
            val item = input.text.toString()
                CoroutineScope(Dispatchers.IO).launch{
                    repository.insert(Todo(item))
                    val data = repository.getAllTodoItems()
                    runOnUiThread{
                        viewModel.setData(data)
                    }
                }
            }
            // Set the negative button action
            builder.setNegativeButton("Cancel") { dialog, which ->
                dialog.cancel()
            }

            // Create and show the alert dialog
            val alertDialog = builder.create()
            alertDialog.show()


        }

    }
