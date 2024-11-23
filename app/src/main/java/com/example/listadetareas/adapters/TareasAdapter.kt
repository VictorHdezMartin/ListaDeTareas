package com.example.listadetareas.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.listadetareas.R
import com.example.listadetareas.data.entities.class_Tarea
import com.example.listadetareas.databinding.ItemTareaBinding


class TareasAdapter (var items: List<class_Tarea>,
                     val onItemClick: (Int) -> Unit,
                     val onItemCheck: (Int) -> Unit,
                     val onItemDelete: (Int) -> Unit,
                     val onItemDuplicate: (Int) -> Unit) : RecyclerView.Adapter<ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val task = items[position]
        holder.render(task)

     // cuando hacemos CLICK en el item seleccionado del MainActivity
        holder.itemView.setOnClickListener {
              onItemClick(position)                        // evitamos que al dar click en el item de la tarea nos vaya a su edición
        }
     // cuando hacemos click en el CHECKBOX del item en el MainActivity
        holder.binding.doneCheckBox.setOnCheckedChangeListener { checkBox, isChecked ->
            if (checkBox.isPressed) {
                onItemCheck(position)
            }
        }
     // cuando hacemos click en el boton DELETE del item en el MainActivity
        holder.binding.deleteButton.setOnClickListener {
            onItemDelete(position)
        }
     // cuando hacemos CLICK en el item seleccionado del MainActivity
        holder.binding.editButton.setOnClickListener {
            onItemClick(position)
        }
     // Cuando hacemos click al boton DUPLICAR del item en el MainActivity
        holder.binding.copyButton.setOnClickListener {
            onItemDuplicate(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTareaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateItems(items: List<class_Tarea>) {
        this.items = items
        notifyDataSetChanged()
    }
}

class ViewHolder(val binding: ItemTareaBinding) : RecyclerView.ViewHolder(binding.root) {
    fun render(task: class_Tarea) {

        val context = itemView.context     // contexto para poder cambiar el color del icono "ICONO_DELETE"

        with (binding) {
            nameTextView.text = task.name
            doneCheckBox.isChecked = task.done

            if (task.done) {
                estadoTarea.setBackgroundResource(R.color.purple_200)
                deleteButton.isEnabled = true
                deleteButton.setColorFilter(context.getColor(R.color.delete))
            } else {
                estadoTarea.setBackgroundResource(R.color.white)
                deleteButton.isEnabled = false
                deleteButton.setColorFilter(context.getColor(R.color.white))
            }
        }
    }
}

