package com.personal.personalsafetyguard.contacts

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.personal.personalsafetyguard.R

class CustomAdapter(private val adapterContext: Context, private val contacts: MutableList<ContactModel>) :
    ArrayAdapter<ContactModel?>(
        adapterContext, 0, contacts as MutableList<ContactModel?>
    ) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        //create a database helper object to handle the database manipulations
        var convertView = convertView
        val db = DbHelper(adapterContext)

        // Get the data item for this position
        val c: ContactModel? = getItem(position)
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView =
                LayoutInflater.from(adapterContext).inflate(R.layout.item_user, parent, false)
        }
        val linearLayout: LinearLayout? = convertView?.findViewById<View>(R.id.linear) as LinearLayout

        // Lookup view for data population
        val tvName: TextView? = convertView?.findViewById<View>(R.id.tvName) as? TextView
        val tvPhone: TextView? = convertView?.findViewById<View>(R.id.tvPhone) as? TextView

        // Populate the data into the template view using the data object
        tvName?.text = c?.name
        tvPhone?.text = c?.phoneNo

        linearLayout?.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(view: View): Boolean {

                //generate a MaterialAlertDialog Box
                MaterialAlertDialogBuilder(adapterContext)
                    .setTitle("Hapus kontak")
                    .setMessage("Apakah Anda yakin ingin menghapus kontak ini?")
                    .setPositiveButton("YES", object : DialogInterface.OnClickListener {
                        override fun onClick(dialogInterface: DialogInterface, i: Int) {
                            //delete the specified contact from the database
                            c?.let { db.deleteContact(it) }
                            //remove the item from the list
                            contacts.remove(c)
                            //notify the listview that dataset has been changed
                            notifyDataSetChanged()
                            Toast.makeText(adapterContext, "Kontak terhapus!", Toast.LENGTH_SHORT).show()
                        }
                    })
                    .setNegativeButton("NO", object : DialogInterface.OnClickListener {
                        override fun onClick(dialogInterface: DialogInterface, i: Int) {}
                    })
                    .show()
                return false
            }
        })
        // Return the completed view to render on screen
        return convertView
    }

    //this method will update the ListView
    fun refresh(list: List<ContactModel>?) {
        contacts.clear()
        contacts.addAll(list!!)
        notifyDataSetChanged()
    }
}
