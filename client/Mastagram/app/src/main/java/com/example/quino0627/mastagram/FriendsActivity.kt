package com.example.quino0627.mastagram

import android.content.ContentResolver
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.example.quino0627.mastagram.Model.User
import kotlinx.android.synthetic.main.post_view.*
import org.jetbrains.anko.support.v4.toast

class FriendsActivity: Fragment(){

    companion object {
        var friendsList: ArrayList<User> = ArrayList()
        var adapder : FriendsAdapter = FriendsAdapter(friendsList)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return inflater.inflate(R.layout.fragment_friends, container, false)

        val rootView = inflater!!.inflate(R.layout.fragment_friends, container, false)
        val recyclerView = rootView.findViewById<RecyclerView>(R.id.friends_recycler_view) as RecyclerView

        friendsList = getFriends()

        val adapter = FriendsAdapter(friendsList)
        val formanage = LinearLayoutManager(activity, LinearLayout.VERTICAL, false)
        recyclerView.layoutManager = formanage
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(false)

        var myNameTextView = rootView.findViewById<AppCompatTextView>(R.id.my_name)
        var myPhoneTextView = rootView.findViewById<AppCompatTextView>(R.id.my_name)

        return rootView

    }

    private fun getFriends(): ArrayList<User> {
        val tempList = ArrayList<User>()
        val resolver: ContentResolver = activity!!.contentResolver
        var contentResolver = activity!!.contentResolver
        val cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.Contacts.SORT_KEY_PRIMARY)

        if(cursor.count>0){
            while (cursor.moveToNext()){
                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val phoneNumber = (cursor.getString(
                    cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)
                )).toInt()
                if (phoneNumber > 0){
                    val cursorPhone = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", arrayOf(id), null)
                    if(cursorPhone.count>0){
                        while (cursorPhone.moveToNext()){
                            val phoneNumValue = cursorPhone.getString(
                                cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                            )
                            val tempUser = User()
                            tempUser.name = name
                            tempUser.phone = phoneNumValue
                            tempList.add(tempUser)
                        }
                    }
                    cursorPhone.close()
                }
            }
        } else{
            toast("No Contacts Availiable!")
        }
        cursor.close()
        return tempList
    }
}