package com.example.mytodo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Welcome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_welcome)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->

            val button1: Button =findViewById(R.id.button_2)
            button1.setOnClickListener{
                val btn1= Intent(this@Welcome,Home::class.java)
                startActivity(btn1)

            }
            val button2: Button =findViewById(R.id.welcome_btn_2)
            button2.setOnClickListener{
                val btn2= Intent(this@Welcome,Home::class.java)
                startActivity(btn2)

            }
            val button3: Button =findViewById(R.id.welcome_btn_3)
            button3.setOnClickListener{
                val btn3= Intent(this@Welcome,MainActivity::class.java)
                startActivity(btn3)

            }
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}