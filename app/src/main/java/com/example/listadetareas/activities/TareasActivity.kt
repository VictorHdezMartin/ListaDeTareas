package com.example.listadetareas.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.example.listadetareas.data.entities.class_Tarea
import com.example.listadetareas.data.providers.Tareas_DAO
import com.example.listadetareas.databinding.ActivityTareasBinding
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Locale

class TareasActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_TASK_ID = "TASK_ID"
    }

    lateinit var binding: ActivityTareasBinding
    lateinit var taskDAO: Tareas_DAO
    lateinit var tarea: class_Tarea

    private val calendar =
        Calendar.getInstance()                                                   // nos hace falta para instanciar el DATAPICKER

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()                                                                          // habilitamos la barra estado superior, propio del teléfono

        binding = ActivityTareasBinding.inflate(layoutInflater)                                     // creamos el binding para este activity
        setContentView(binding.root)                                                                // creamos el CONTEXT del activity

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->                      // esto es siempre igual
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setLongTextFields()

        taskDAO = Tareas_DAO(this)                                                          // instanciamos la operatividad de DAO para este activity
        val id = intent.getLongExtra(EXTRA_TASK_ID,-1L)                                 // Si nos pasan un id es que queremos editar una tarea existente
        var isEdit: Boolean = id != -1L                                                             // vemos is editamos / insertamos la tarea

        if (isEdit) {
            tarea = taskDAO.findById(id)!!                                                          // buscamos la tarea en cuestión
            loadData_Binding()                                                                      // Pasamos los datos de la tarea al binding
            binding.saveButton.setText("Actualizar tarea")
        } else {
            tarea = class_Tarea(-1, "", getDateTimeSys(), getDateTimeSys(), getDateTimeSys(), "")

            loadData_Binding()                                                                      // Pasamos los datos de la tarea al binding

            binding.saveButton.setText("Crear tarea")
        }

        enabledSaveButton(1)                                                                 // ¿Habilitar botón guardar tarea?

    // listener mientrar estamos escribimos el nombre de la tarea  --------------------------------
        binding.nameTextField.editText?.addTextChangedListener {
            var accion = 1
            enabledSaveButton(accion)                                                               // Habilitar botón guardar/actualizar tarea
            checkTamanoTextField(accion,                                                            // comprobamos tamaño encaracteres de la tarea
                                 binding.nameTextField.editText?.text.toString(),
                                 binding.nameTextField.counterMaxLength,
                    ""
            )
        }

    // listener de la fecha de inicio  ------------------------------------------------------------
        binding.fechaInicioTextField.editText?.addTextChangedListener {
            var accion = 2
            enabledSaveButton(accion)                                                               // Habilitar botón guardar/actualizar tarea
            checkTamanoTextField(accion,                                                            // comprobamos tamaño encaracteres de la fecha inicio
                                 binding.fechaInicioTextField.editText?.text.toString(),
                                 binding.fechaInicioTextField.counterMaxLength,
                   ""
            )
        }

    // listener de la fecha de fin  ---------------------------------------------------------------
        binding.fechaFinTextField.editText?.addTextChangedListener {
            var accion = 3
            enabledSaveButton(accion)                                                               // Habilitar botón guardar/actualizar tarea
            checkTamanoTextField(accion,                                                            // comprobamos tamaño encaracteres de la fecha fin
                                 binding.fechaFinTextField.editText?.text.toString(),
                                 binding.fechaFinTextField.counterMaxLength,
                    ""
            )
        }

     // listener del comentario  -------------------------------------------------------------------
        binding.blogTextField.editText?.addTextChangedListener {
            var accion = 4
            enabledSaveButton(accion)                                                               // Habilitar botón guardar/actualizar tarea
            checkTamanoTextField(accion,                                                            // comprobamos tamaño en caracteres del comentario
                                 binding.blogTextField.editText?.text.toString(),
                                 binding.blogTextField.counterMaxLength,
                   "las observaciones"
            )
        }

     // Listener al pulsar el boton de calendario fecha inicio
        binding.fechaInicioButton.setOnClickListener {
            showDatePicker(1)
        }

     // Listener al pulsar el boton de calendario fecha inicio
        binding.fechaFinButton.setOnClickListener {
            showDatePicker(2)
        }

        // Listener al pulsar el bonton del reloj de hora inicio
        binding.horaInicioButton.setOnClickListener {
            showTimePicker(1)
        }

        // Listener al pulsar el boton del reloj de hora fin
        binding.horaFinButton.setOnClickListener {
            showTimePicker(2)
        }

        // listener al pulsar el botón de guardar  ----------------------------------------------------
        binding.saveButton.setOnClickListener {

            val nombreTarea = binding.nameTextField.editText?.text.toString()

            if (validarNuevaTarea(nombreTarea) != true) {                                           // validamos la tarea (nombreTarea)
                return@setOnClickListener
            } else {
                loadData_Tarea()                                                                     // cargamos los datos del TareasActivity (binding)

                if (isEdit) {
                    taskDAO.update(tarea)                                                           // como existe, la actualizamos
                } else {
                    taskDAO.insert(tarea)                                                           // al ser nueva la creamos
                }

                finish()                                                                            // salimos del TareasActivity y vamos al MainActivity
            }
        }
    }

 // Funcion para generar un DATEPICKER desde código ------------------------------------------------
    private fun showDatePicker(accion: Int) {
        var hora: String
        val datePickerDialog = DatePickerDialog(
            this, { DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val selectedDate = Calendar.getInstance()                                           // Create a new Calendar instance to hold the selected date
                selectedDate.set(year, monthOfYear,dayOfMonth)                                      // Set the selected date using the values received from the DatePicker dialog
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())        // Create a SimpleDateFormat to format the date as "dd/MM/yyyy"
                val formattedDate = dateFormat.format(selectedDate.time)                            // Format the selected date into a string

                when (accion) {
                    1 -> {
                           hora = binding.fechaInicioTextField.editText?.text.toString().substring(10, binding.fechaInicioTextField.editText?.text.toString().length)
                           binding.fechaInicioTextField.editText?.setText("$formattedDate" + hora)
                    }
                    else -> {
                              hora = binding.fechaFinTextField.editText?.text.toString().substring(10, binding.fechaFinTextField.editText?.text.toString().length)
                              binding.fechaFinTextField.editText?.setText("$formattedDate" + hora)
                            }
                }
            },

            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()                                                                 // Show the DatePicker dialog
    }

// Funcion para generar un DATEPICKER desde código  ------------------------------------------------
    fun showTimePicker(accion: Int) {
        var fecha: String
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            when (accion) {
                1 -> {
                       fecha =  binding.fechaInicioTextField.editText?.text.toString().substring(0,10)                         // guardamos la fecha inicio
                       binding.fechaInicioTextField.editText?.setText(fecha + "  -  " + SimpleDateFormat("HH:mm").format(cal.time))
                     }
                else -> {
                          fecha =  binding.fechaFinTextField.editText?.text.toString().substring(0,10)                         // guardamos la fecha fin
                          binding.fechaFinTextField.editText?.setText(fecha + "  -  " + SimpleDateFormat("HH:mm").format(cal.time))
                        }
            }
        }

        TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),true).show()
    }

// Marcamos el tamaño maximo que deben tener los campos de la tarea -------------------------------
    fun setLongTextFields() {
        binding.nameTextField.counterMaxLength = 40
        binding.fechaInicioTextField.counterMaxLength = 30
        binding.fechaFinTextField.counterMaxLength = 30
        binding.blogTextField.counterMaxLength = 500
    }

 // Cargar los datos de la tarea al binding  -------------------------------------------------------
    fun loadData_Binding() {
        binding.nameTextField.editText?.setText(tarea.name)
        binding.fechaInicioTextField.editText?.setText(tarea.fechaInicio)
        binding.fechaFinTextField.editText?.setText(tarea.fechaFin)
        binding.blogTextField.editText?.setText(tarea.comentario)
    }

 // Cargar datos para guardar tarea  ---------------------------------------------------------------
    fun loadData_Tarea() {
        tarea.name = binding.nameTextField.editText?.text.toString()
        tarea.fechaInicio = binding.fechaInicioTextField.editText?.text.toString()
        tarea.fechaFin = binding.fechaFinTextField.editText?.text.toString()
        tarea.comentario = binding.blogTextField.editText?.text.toString()
    }

 // Habilitamos botón "guardar tarea" si la tarea NO esta vacía y es menor de 51 caracteres --------
    fun enabledSaveButton(accion: Int) {
        when (accion) {
            1 -> binding.saveButton.isEnabled = !binding.nameTextField.editText?.text.toString().trim().isEmpty() &&
                                                 binding.nameTextField.editText?.text.toString().length < binding.nameTextField.counterMaxLength + 1

            2 ->  binding.saveButton.isEnabled = !binding.fechaInicioTextField.editText?.text.toString().trim().isEmpty() &&
                                                  binding.fechaInicioTextField .editText?.text.toString().length < binding.fechaInicioTextField.counterMaxLength + 1

            3 -> binding.saveButton.isEnabled = !binding.fechaFinTextField.editText?.text.toString().trim().isEmpty() &&
                                                 binding.fechaFinTextField .editText?.text.toString().length < binding.fechaFinTextField.counterMaxLength + 1

            else -> binding.saveButton.isEnabled = binding.blogTextField.editText?.text.toString().length < binding.blogTextField.counterMaxLength + 1
        }
    }

 // Verificamos que el tamaño no supere los 50 caracteres  -----------------------------------------
    fun checkTamanoTextField(accion: Int, itemSelect: String, chars: Int, complemento: String) {
        when (accion) {
            1 -> if (itemSelect.length > chars)
                     binding.nameTextField.error = "Dispone de un máximo de ${chars.toString()} caracteres ${complemento}"
                 else
                     binding.nameTextField.error = null

            2 ->  if (itemSelect.length > chars)
                      binding.fechaInicioTextField.error = "Dispone de un máximo de ${chars.toString()} caracteres ${complemento}"
                  else
                     binding.fechaInicioTextField .error = null

            3 ->  if (itemSelect.length > chars)
                     binding.fechaFinTextField.error = "Dispone de un máximo de ${chars.toString()} caracteres ${complemento}"
                  else
                     binding.fechaFinTextField.error = null

            else ->  if (itemSelect.length > chars)
                        binding.blogTextField.error = "Dispone de un máximo de ${chars.toString()} caracteres ${complemento}"
                     else
                        binding.blogTextField.error = null
        }
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

// Funcion para obtener la fecha y hora del sistema
   fun getDateTimeSys(): String {
       var LocalDateTime = LocalDateTime.now();

       var year = LocalDateTime.getYear();
       var month = LocalDateTime.getMonthValue();
       var dayOfMonth = LocalDateTime.getDayOfMonth();
       var hour = LocalDateTime.getHour();
       var minute = LocalDateTime.getMinute();

       return "${dayOfMonth}/${month}/${year}  -  ${hour}:${minute}"
   }
}
