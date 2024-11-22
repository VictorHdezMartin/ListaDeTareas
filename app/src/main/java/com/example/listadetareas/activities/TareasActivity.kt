package com.example.listadetareas.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
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

        binding = ActivityTareasBinding.inflate(layoutInflater)                  // creamos el binding para este activity
        setContentView(binding.root)                                             // creamos el CONTEXT del activity

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        taskDAO = Tareas_DAO(this)                                       // instanciamos la operatividad de DAO para este activity

        // Si nos pasan un id es que queremos editar una tarea existente
        val id = intent.getLongExtra(EXTRA_TASK_ID, -1L)

        var isEdit: Boolean = id != -1L                                          // vemos is editamos la tarea

        if (isEdit) {
            tarea = taskDAO.findById(id)!!                                       // buscamos la tarea en cuestión
            binding.nameTextField.editText?.setText(tarea.name)                  // class_Tarea.name -> TareaTextEdit
            binding.saveButton.setText("Actualizar tarea")
        } else {
            tarea = class_Tarea(-1, "")                                // tarea nueva
            binding.saveButton.setText("Guardar tarea")
        }

        enabledSaveButton()                                                     // ¿Habilitar botón guardar tarea?

     // listener mientrar estamos escribimos  ------------------------------------------------------
        binding.nameTextField.editText?.addTextChangedListener {
            val nombreTarea = binding.nameTextField.editText?.text.toString()     // TareaEditText -> nombreTarea

            enabledSaveButton()                                                   // ¿Habilitar botón guardar tarea?
            checkTamanoTarea(nombreTarea)                                         // comprobamos tamaño encaracteres de la tarea
        }

     // listener al pulsar el botón de guardar
        binding.saveButton.setOnClickListener {
            val nombreTarea = binding.nameTextField.editText?.text.toString()     // TareaEditText -> nombreTarea

            if (validarNuevaTarea(nombreTarea) != true) {                         // validamos la tarea (nombreTarea)
                return@setOnClickListener
            } else {
                tarea.name = nombreTarea                                          // nombreTarea -> class_Tarea.Name

                if (isEdit) {
                    taskDAO.update(tarea)                                         // como existe, la actualizamos
                } else {
                    taskDAO.insert(tarea)                                         // al ser nueva la creamos
                }

                finish()                                                          // salimos del TareasActivity y vamos al MainActivity
            }
        }
    }

 // Habilitamos botón "guardar tarea" si la tarea NO esta vacía y es menor de 51 caracteres --------
    fun enabledSaveButton() {
        binding.saveButton.isEnabled = !binding.nameTextField.editText?.text.toString().trim().isEmpty() &&
                                        binding.nameTextField.editText?.text.toString().length < 51
    }

 // Verificamos que el tamaño no supere los 50 caracteres  -----------------------------------------
    fun checkTamanoTarea(nombreTarea: String) {
        if (nombreTarea.length > 50)
            binding.nameTextField.error = "Tienes un máximo de 50 caracteres para definir la tarea"
        else
            binding.nameTextField.error = null
}

 // Comprobamos el texto introducido para mostrar posibles errores  --------------------------------
    fun validarNuevaTarea(nombreTarea: String): Boolean {

        var tempText: String = nombreTarea.lowercase()
        var abecedario: String = "abcdefghijklmnñopqrstuvwxyzáéíóú"
        var numeros: String = "0123456789"

        for (i in 0 .. tempText.length ){
            if (tempText[i] in abecedario || tempText[i] in numeros)
                return true
            else {
                binding.nameTextField.error = "Debes de introducir una tarea válida"
                return false
            }
        }

     return false

    }
}