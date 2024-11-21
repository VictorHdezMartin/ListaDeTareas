package com.example.listadetareas.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listadetareas.R
import com.example.listadetareas.data.entities.class_Tarea
import com.example.listadetareas.data.providers.Tareas_DAO
import com.example.listadetareas.databinding.ActivityMainBinding
import com.example.listadetareas.adapters.TareasAdapter

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var adapter: TareasAdapter
    lateinit var tareasDAO: Tareas_DAO
    var tareasList: MutableList<class_Tarea> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()               // vemos la funciones de la barra de arriba

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tareasDAO = Tareas_DAO(this)

        adapter = TareasAdapter(tareasList,
            {  // editar tarea
                val tarea = tareasList[it]
                showTask(tarea)},

            { //marcar tarea
                val tarea = tareasList[it]
                checkTarea(tarea)},

            {  // borrar tarea
                val tarea = tareasList[it]
                deleteTarea(tarea)
            })

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

     // crear tarea
        binding.addTaskButton.setOnClickListener {
            val intent = Intent(this, TareasActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

     // Cargamos la lista por si se hubiera añadido una tarea nueva
        tareasList = tareasDAO.findAll().toMutableList()
        adapter.updateItems(tareasList)
    }

 // Funcion para cuando marcamos una tarea (finalizada/pendiente)
    fun checkTarea(tarea: class_Tarea) {
        tarea.done = !tarea.done
        Tareas_DAO.update(tarea)
        adapter.updateItems(tareasList)
    }

 // Funciona para mostrar un dialogo para borrar la tarea
    fun deleteTarea(tarea: class_Tarea) {

     // Mostramos un dialogo para asegurarnos de que el usuario quiere borrar la tarea
        AlertDialog.Builder(this)
            .setTitle("Borrar tarea")
            .setMessage("Estas seguro de que quieres borrar la tarea?")
            .setPositiveButton(android.R.string.ok) { dialog, which ->

             // Borramos la tarea en caso de pulsar el boton OK
                Tareas_DAO.delete(tarea)
                tareasList.remove(tarea)
                adapter.updateItems(tareasList)
            }
            .setNegativeButton(android.R.string.cancel, null)
            .setIcon(R.drawable.icono_delete)
            .show()
    }

 // Mostramos la tarea para editarla
    fun showTask(tarea: class_Tarea) {
        val intent = Intent(this, TareasActivity::class.java)
        intent.putExtra(TareasActivity.EXTRA_TASK_ID, tarea.id)
        startActivity(intent)
    }
}