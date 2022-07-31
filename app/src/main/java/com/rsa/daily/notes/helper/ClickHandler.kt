package com.rsa.daily.notes.helper

import java.util.*

interface ClickHandler {
    fun onClick(vararg objects: Any?)
    fun onEdit(position:Int,type:String)
    fun onDelete(position: Int)
}