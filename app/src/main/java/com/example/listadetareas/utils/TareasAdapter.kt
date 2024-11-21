package com.example.listadetareas.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.listadetareas.R
import com.example.listadetareas.data.class_Tarea
import com.example.listadetareas.databinding.ItemTareaBinding

class TareasAdapter (var items: List<class_Tarea>,
                     val onItemClick: (Int) -> Unit,
                     val onItemCheck: (Int) -> Unit,
                     val onItemDelete: (Int) -> Unit) : RecyclerView.Adapter<ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = items[position]
        holder.render(task)

        holder.itemView.setOnClickListener {
            onItemClick(position)
        }

        holder.binding.doneCheckBox.setOnCheckedChangeListener { checkBox, isChecked ->
            if (checkBox.isPressed) {
                onItemCheck(position)
            }
        }

        holder.binding.deleteButton.setOnClickListener {
            onItemDelete(position)
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
        binding.nameTextView.text = task.name
        binding.doneCheckBox.isChecked = task.done

        if (task.done)
           binding.estadoTarea.setBackgroundResource(R.color.purple_200)
        else
            binding.estadoTarea.setBackgroundResource(R.color.white)
    }
}

