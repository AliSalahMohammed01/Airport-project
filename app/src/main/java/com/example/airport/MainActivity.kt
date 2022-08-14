package com.example.airport
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.airport.R
import com.example.airport.Airport
import com.example.airport.Airport.Companion.id

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val location: Button = findViewById(R.id.button4)
        location.setOnClickListener() {

            val mapIntent: Intent =
                Uri.parse("geo:0,0?q=1600+Amphitheatre+Parkway,+Mountain+View,+California")
                    .let { location ->
                        Intent(Intent.ACTION_VIEW, location)
                    }
            startActivity((mapIntent))
        }
        var flag: String = "amman airport"
        val spinnerVal: Spinner = findViewById(R.id.spinner1)
        var options = arrayOf("amman airport", "aqapa airport")
        spinnerVal.adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options)
        var flag2: String = "Erbil airport"
        val spinnerVal2: Spinner = findViewById(R.id.spinner2)
        var options2 = arrayOf("Erbil airport", "LAX", "Toronto airport")
        spinnerVal2.adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options2)
        spinnerVal.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                flag = options.get(p2)
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        spinnerVal2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                flag2 = options2.get(p2)
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        val btn2: Button = findViewById(R.id.button2)
        val btn3: Button = findViewById(R.id.button3)

        /*
        btn2.setOnClickListener()
        {

            val ed1: EditText = findViewById(R.id.editText);
            val values = ContentValues()
            values.put(
                Airport.NAME,
                ed1.text.toString()
            )
            val uri = contentResolver.delete(
                Airport.CONTENT_URI, "name like ?",Array(1){"$ed1"}
            )
        }
*/
    /*
        btn3.setOnClickListener()
        {
            val ed1: EditText = findViewById(R.id.editText);
            val values = ContentValues()
            values.put(
                Airport.NAME,ed1.text.toString()
            )
            val uri = contentResolver.update(
                Airport.CONTENT_URI, values,"name like ?",Array(1){"$ed1"}
            )
        }

*/
    }

    fun onClickDelete(view : View?)
    {
        val ed1: EditText = findViewById(R.id.editText);
        val values = ContentValues()
        values.put(
            Airport.NAME,
            ed1.text.toString()
        )
        val uri = contentResolver.delete(
            Airport.CONTENT_URI, "name like ?",Array(1){"$ed1"}
        )

    }



    fun onClickAddName(view: View?) {

        val values = ContentValues()
        values.put(
            Airport.NAME,
            (findViewById<View>(R.id.editText) as EditText).text.toString()
        )
        values.put(
            Airport.pass_id,
            (findViewById<View>(R.id.editText2) as EditText).text.toString()
        )
        val uri = contentResolver.insert(
            Airport.CONTENT_URI, values
        )
        Toast.makeText(baseContext, uri.toString(), Toast.LENGTH_LONG).show()
    }

    @SuppressLint("Range")
    fun onClickRetrieve(view: View?) {
        val URL = "content://com.example.airport.Airport"
        val airport = Uri.parse(URL)

        var c = contentResolver.query(airport, null, null, null,null)

        if (c != null)
        {
            if (c.moveToFirst())
            {
                do {
                    Toast.makeText(this, c.getString(c.getColumnIndex(Airport.id)) +
                            ", " + c.getString(c.getColumnIndex(Airport.NAME)) + ", "
                            + c.getString(c.getColumnIndex(Airport.pass_id)), Toast.LENGTH_SHORT).show()
                }while (c.moveToNext())
                }
            }
        }
/*

    fun onClickAddData(view: View?) {
        val values = ContentValues()
        values.put(
            Airport.NAME, editText.text.toString()
        )
        val uri = contentResolver.update(
            Airport.CONTENT_URI, values, "name like ?", Array(1) { "$ed1" }
        )


    }

    }
*/

}













