package com.example.listadetareas.data

data class class_Tarea(
    val id: Long,
    var name: String,
    var done: Boolean){

    companion object {
        const val TABLE_NAME  = "TBL_Tareas"
        const val COLUMN_ID   = "ID"
        const val COLUMN_NAME = "nombreTarea"
        const val COLUMN_DONE = "finalizada"
    }
}
