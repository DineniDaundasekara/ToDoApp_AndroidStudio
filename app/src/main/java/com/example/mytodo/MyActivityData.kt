package com.example.mytodo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mytodo.database.entities.Todo

class MyActivityData: ViewModel()  {
    private val _data = MutableLiveData<List<Todo>>()
    val data: LiveData<List<Todo>> = _data
    fun setData(data:List<Todo>){
        _data.value = data
    }
}