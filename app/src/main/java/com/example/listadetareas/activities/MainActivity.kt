package com.example.listadetareas.activities

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listadetareas.R
import com.example.listadetareas.data.class_Tarea
import com.example.listadetareas.data.providers.Tareas_DAO
import com.example.listadetareas.databinding.ActivityMainBinding
import com.example.listadetareas.utils.DataBase_Manager
import com.example.listadetareas.utils.TareasAdapter

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var adapter: TareasAdapter
    lateinit var tareasDAO: Tareas_DAO
    var tareasList: List<class_Tarea> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()     // vemos la funciones de la barra de arriba

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tareasDAO = Tareas_DAO(this)

        adapter = TareasAdapter(tareasList) {
            val tarea = tareasList[it]
            tarea.done = !tarea.done
            tareasDAO.update(tarea)
            adapter.updateItems(tareasList)
        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.addTaskButton.setOnClickListener {
            val intent = Intent(this, TareasActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        tareasList = tareasDAO.findAll()

        adapter.updateItems(tareasList)
    }

}