package com.example.listadetareas.activities

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listadetareas.R
import com.example.listadetareas.adapters.TareasAdapter
import com.example.listadetareas.data.entities.class_Tarea
import com.example.listadetareas.data.providers.Tareas_DAO
import com.example.listadetareas.databinding.ActivityMainBinding


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
                showTarea(tarea)
            },

            { //marcar tarea
                val tarea = tareasList[it]
                checkTarea(tarea)
            },

            {  // borrar tarea
                val tarea = tareasList[it]
                deleteTarea(tarea)
            })

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        // abrimos el layout "activity_tareas" para dar de alta una tarea
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
        tareasDAO.update(tarea)
        adapter.updateItems(tareasList)
    }

    // Funciona para mostrar un dialogo para borrar la tarea
    fun deleteTarea(tarea: class_Tarea) {

        var msgCab: String = "¿Estás seguro de querer borrar la tarea? \n\n"
        var msgTarea: String = tarea.name

        // Mostramos un dialogo para asegurarnos de que el usuario quiere borrar la tarea
        AlertDialog.Builder(this)
            .setTitle("Borrar tarea")
            .setMessage(spannableDialogMSG(msgCab, msgTarea, true, "uppercase"))
            .setPositiveButton(android.R.string.ok) { dialog, which ->

             // Borramos la tarea en caso de pulsar el boton OK
                tareasDAO.delete(tarea)
                tareasList.remove(tarea)
                adapter.updateItems(tareasList)
            }
            .setNegativeButton(android.R.string.cancel, null)
            .setIcon(R.drawable.icono_delete)
            .show()
    }

    // Mostramos la tarea para editarla
    fun showTarea(tarea: class_Tarea) {
        val intent = Intent(this, TareasActivity::class.java)
        intent.putExtra(TareasActivity.EXTRA_TASK_ID, tarea.id)
        startActivity(intent)
    }

    // Cambiamos el texto en mayusculas, negrita y color
    fun spannableDialogMSG(msgCab: String, msgTarea: String, negrita: Boolean, tipoLetra: String): SpannableStringBuilder {

        val dialogMSG = SpannableStringBuilder()
        dialogMSG.append(msgCab)

        var msg = when (tipoLetra.uppercase()) {
            "UPPERCASE" -> msgTarea.uppercase()
            "LOWERCASE" -> msgTarea.lowercase()
            else -> msgTarea
        }

        var spanTarea = SpannableString("${msg}")

        if (negrita)
            spanTarea.setSpan(StyleSpan(Typeface.BOLD), 0, msgTarea.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        dialogMSG.append(spanTarea)
        return dialogMSG
    }
}