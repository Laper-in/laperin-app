package com.capstone.laperinapp.ui.register

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.capstone.laperinapp.R
import com.capstone.laperinapp.costumView.ButtonRegister
import com.capstone.laperinapp.databinding.ActivityRegisterBinding
import java.util.Calendar

class RegisterActivity : AppCompatActivity() {

    private lateinit var edTtl: EditText
    private lateinit var binding : ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val buttonLogin = findViewById<ButtonRegister>(R.id.bt_daftar_register)
        buttonLogin.isEnabled = true
        buttonLogin.isEnabled = false
        edTtl = findViewById(R.id.ed_ttl)
    }
    fun showDatePickerDialog(view: View) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                val selectedDate = "$dayOfMonth/${month + 1}/$year"
                edTtl.setText(selectedDate)
            },
            year, month, dayOfMonth
        )

        datePickerDialog.show()
    }
}