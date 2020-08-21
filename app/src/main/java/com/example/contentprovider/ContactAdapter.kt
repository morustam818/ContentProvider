package com.example.contentprovider


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.contact_list.view.*

class ContactAdapter(private val userList : List<UserContact>) : RecyclerView.Adapter<ContactAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.contact_list,parent,false))
    }

    override fun getItemCount() = userList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listPosition = userList[position]
        holder.view.userProfile.text = listPosition.prefix
        holder.view.tv_name.text = listPosition.name
        holder.view.tv_phone.text = listPosition.phone
    }

    class ViewHolder(val view : View) : RecyclerView.ViewHolder(view)
}