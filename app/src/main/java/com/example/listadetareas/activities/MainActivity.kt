package com.example.listadetareas.activities

import android.content.ContentValues
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.listadetareas.R
import com.example.listadetareas.utils.DataBase_Manager

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_main)





        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val db = DataBase_Manager(this).writableDatabase

        db.execSQL("")


// para INSERT

        var values = ContentValues()
        values.put("name", "Comprar leche")
        values.put("done", false)

        val id = db.insert("Task", null, values)


    }
}