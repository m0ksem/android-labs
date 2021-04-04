package com.example.nubip_md.lr7

import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nubip_md.R


class Contact(val id: String, val name: String) {
    var numbers: ArrayList<String> = arrayListOf()
    var emails: ArrayList<String> = arrayListOf()
}

class ContactsListAdapter(private val dataSet: List<Contact>) :
        RecyclerView.Adapter<ContactsListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.contact_name)
        val phone: TextView = view.findViewById(R.id.contact_phone_value)
        val email: TextView = view.findViewById(R.id.contact_email_value)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.activity_address_book_list_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val contact = dataSet[position]
        viewHolder.name.text = contact.name
        if (contact.numbers.count() > 0) {
            viewHolder.phone.text = dataSet[position].numbers[0]
        } else {
            viewHolder.phone.text = "No number"
        }

        if (contact.emails.count() > 0) {
            viewHolder.email.text = dataSet[position].emails[0]
        } else {
            viewHolder.email.text = "No email"
        }
    }

    override fun getItemCount() = dataSet.size
}

class AddressBook : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_book)

        val contacts = fetchContacts()
        val contactsSorted = contacts.sortedBy { it.name }

        renderList(contactsSorted)

        findViewById<Button>(R.id.address_book_search_button).setOnClickListener {
            showFiltered(contactsSorted, findViewById<TextView>(R.id.address_book_search_input).text.toString())
        }
    }

    fun showFiltered(contacts: List<Contact>, searchString: String) {
        val filteredContacts = contacts.filter {
            it.name.indexOf(searchString) != -1
        }
        this.renderList(filteredContacts)
    }

    fun renderList(contacts: List<Contact>) {
        val list = findViewById<RecyclerView>(R.id.contacts_list)
        list.setHasFixedSize(false)
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = ContactsListAdapter(contacts.toList())
    }

    fun getContactsList(): ArrayList<Contact> {
        val contactsList = ArrayList<Contact>()
        val cr = contentResolver
        val contactsCursor = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null)

        if (contactsCursor != null && contactsCursor.count > 0) {
            val idIndex = contactsCursor.getColumnIndex(ContactsContract.Contacts._ID)
            val nameIndex = contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)

            while (contactsCursor.moveToNext()) {
                val id = contactsCursor.getString(idIndex)
                val name = contactsCursor.getString(nameIndex)
                if (name != null) {
                    contactsList.add(Contact(id, name))
                }
            }
            contactsCursor.close()
        }
        return contactsList
    }

    private fun getContactNumbers(): HashMap<String, ArrayList<String>> {
        val contactsNumberMap = HashMap<String, ArrayList<String>>()
        val phoneCursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                null
        )
        if (phoneCursor != null && phoneCursor.count > 0) {
            val contactIdIndex = phoneCursor!!.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
            val numberIndex = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            while (phoneCursor.moveToNext()) {
                val contactId = phoneCursor.getString(contactIdIndex)
                val number: String = phoneCursor.getString(numberIndex)
                //check if the map contains key or not, if not then create a new array list with number
                if (contactsNumberMap.containsKey(contactId)) {
                    contactsNumberMap[contactId]?.add(number)
                } else {
                    contactsNumberMap[contactId] = arrayListOf(number)
                }
            }
            //contact contains all the number of a particular contact
            phoneCursor.close()
        }
        return contactsNumberMap
    }

    private fun getContactEmails(): HashMap<String, ArrayList<String>> {
        val contactsEmails = HashMap<String, ArrayList<String>>()
        val phoneCursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                null,
                null,
                null,
                null
        )
        if (phoneCursor != null && phoneCursor.count > 0) {
            val contactIdIndex = phoneCursor!!.getColumnIndex(ContactsContract.CommonDataKinds.Email.CONTACT_ID)
            val emailIndex = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)
            while (phoneCursor.moveToNext()) {
                val contactId = phoneCursor.getString(contactIdIndex)
                val email: String = phoneCursor.getString(emailIndex)
                //check if the map contains key or not, if not then create a new array list with number
                if (contactsEmails.containsKey(contactId)) {
                    contactsEmails[contactId]?.add(email)
                } else {
                    contactsEmails[contactId] = arrayListOf(email)
                }
            }
            //contact contains all the number of a particular contact
            phoneCursor.close()
        }
        return contactsEmails
    }

    fun fetchContacts(): ArrayList<Contact> {
        val contactsList = this.getContactsList()
        val contactNumbers = getContactNumbers()
        val contactEmails = getContactEmails()

        contactsList.forEach {
            contactNumbers[it.id]?.let { numbers ->
                it.numbers = numbers
            }
            contactEmails[it.id]?.let { emails ->
                it.emails = emails
            }
        }

        return contactsList
    }
}