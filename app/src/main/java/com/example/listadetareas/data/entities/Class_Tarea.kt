package com.example.listadetareas.data.entities

data class class_Tarea (
    val id: Long,
    var name: String,
    var fechaCreate: Long,
    var fechaStart: Long,
    var fechaEnd: Long,
    var done: Boolean = false){

    companion object {
        const val TABLE_NAME         = "TBL_Tareas"
        const val COLUMN_ID          = "id"
        const val COLUMN_NAME        = "nombreTarea"
        const val COLUMN_FECHACREATE = "fechaCreacion"
        const val COLUMN_FECHASTART  = "fechaInicio"
        const val COLUMN_FECHAEND    = "fechaFin"
        const val COLUMN_DONE        = "realizada"
    }
}
