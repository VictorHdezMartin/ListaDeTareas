package com.example.listadetareas.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.listadetareas.data.entities.class_Tarea
import com.example.listadetareas.data.providers.Tareas_DAO
import com.example.listadetareas.databinding.ActivityTareasBinding

class TareasActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_TASK_ID = "TASK_ID"
    }

    lateinit var binding: ActivityTareasBinding

    lateinit var taskDAO: Tareas_DAO
    lateinit var tarea: class_Tarea

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityTareasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        taskDAO = Tareas_DAO(this)

     // Si nos pasan un id es que queremos editar una tarea existente
        val id = intent.getLongExtra(EXTRA_TASK_ID, -1L)

        if (id != -1L) {
            tarea = taskDAO.findById(id)!!
            binding.nameTextField.editText?.setText(tarea.name)
        } else {
            tarea = class_Tarea(-1, "")
        }

        binding.saveButton.setOnClickListener {
            val nombreTarea = binding.nameTextField.editText?.text.toString()

            if (validarNuevaTarea(nombreTarea) != true){
                return@setOnClickListener
            } else {
                val tarea = class_Tarea(-1, nombreTarea)
                val taskDAO = Tareas_DAO(this)
                taskDAO.insert(tarea)

                finish()
            }
        }
    }

 // Comprobamos el texto introducido para mostrar posibles errores
    fun validarNuevaTarea(nombreTarea: String): Boolean {
        if (nombreTarea.isEmpty()) {
            binding.nameTextField.error = "Debes de introducir una tarea"
            return false
        }

        if (nombreTarea.length > 50) {
            binding.nameTextField.error = "Tienes un máximo de 50 caracteres para definir la tarea"
            return false
        }

        return true
    }
}