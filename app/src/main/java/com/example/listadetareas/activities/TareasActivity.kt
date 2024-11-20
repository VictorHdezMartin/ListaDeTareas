package com.example.listadetareas.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.listadetareas.data.class_Tarea
import com.example.listadetareas.data.providers.Tareas_DAO
import com.example.listadetareas.databinding.ActivityTareasBinding

class TareasActivity : AppCompatActivity() {

    lateinit var binding: ActivityTareasBinding

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

        binding.saveButton.setOnClickListener {

            val nombreTarea = binding.nameTextField.editText?.text.toString()

            if (nombreTarea.isEmpty()) {
                binding.nameTextField.error = "Debes de introducir una tarea"
                return@setOnClickListener
            }

            if (nombreTarea.length > 50) {
                binding.nameTextField.error =
                    "Tienes un máximo de 50 caracteres para definir la tarea"
                return@setOnClickListener
            }

            val tarea = class_Tarea(-1, nombreTarea)

            val taskDAO = Tareas_DAO(this)
            taskDAO.insert(tarea)

            finish()
        }
    }
}