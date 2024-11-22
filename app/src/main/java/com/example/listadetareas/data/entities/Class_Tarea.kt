package com.example.listadetareas.data.entities

data class class_Tarea (
    val id: Long,
    var name: String,
  //  var fechaStart: Long = 35789,
  //  var fechaEnd: Long = 35789,
  //  var hora: Long = 2000,
    var done: Boolean = false){

    companion object {
        const val TABLE_NAME  = "TBL_Tareas"
        const val COLUMN_ID   = "id"
        const val COLUMN_NAME = "nombreTarea"
     //   const val COLUMN_FECHASTART = "fechaInicio"
     //   const val COLUMN_FECHAEND = "fechaFin"
     //   const val COLUMN_HORAFIN = "horaFin"
        const val COLUMN_DONE = "realizada"
    }
}
