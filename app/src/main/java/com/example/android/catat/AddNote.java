package com.example.android.catat;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.Calendar;
import java.text.DateFormat;

public class AddNote extends AppCompatActivity{
    DatabaseHelper myDb;
    EditText judul, lokasi, tgl_acara, deskripsi;
    Spinner tag, alarm;
    ImageButton tgl;
    DateFormat formatDate = DateFormat.getDateInstance();
    DateFormat formatTime = DateFormat.getTimeInstance();
    Calendar dateTime = Calendar.getInstance();
    String setTanggalJam, ValueAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        myDb = new DatabaseHelper(this);

        //identifikasi atribut
        judul = (EditText)findViewById(R.id.edt_title);
        lokasi = (EditText)findViewById(R.id.edt_location);
        tgl_acara = (EditText)findViewById(R.id.edt_datetime);
        deskripsi = (EditText)findViewById(R.id.edt_note);
        tgl = (ImageButton)findViewById(R.id.btn_datetime);
        tag = (Spinner)findViewById(R.id.tag);
        alarm = (Spinner)findViewById(R.id.reminder);

        updateDateTime();

        ArrayAdapter<CharSequence> bb = ArrayAdapter.createFromResource(this,
                R.array.tag_note, android.R.layout.simple_spinner_item);
        bb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tag.setAdapter(bb);
        ArrayAdapter<CharSequence> aa = ArrayAdapter.createFromResource(this,
                R.array.value_alarm, android.R.layout.simple_spinner_item);
        bb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        alarm.setAdapter(aa);

        tgl.setOnClickListener(pickdate);
    }

    public void AddData(String jdl, String lks, String kategori, String tglacara, String des, String alr, String frmtgl){
        boolean isInserted = myDb.insertData(jdl, lks, kategori, tglacara, des, alr, frmtgl);
        if (isInserted) {
            Toast.makeText(AddNote.this, "Note Added!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(AddNote.this, Home.class);
            finish();
            startActivity(intent);
        } else
            Toast.makeText(AddNote.this, "Something went wrong!", Toast.LENGTH_LONG).show();
    }

    private View.OnClickListener pickdate=new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            updateDate();
        }
    };

    private void updateDate(){
        new DatePickerDialog(this, d, dateTime.get(Calendar.YEAR), dateTime.get(Calendar.MONTH), dateTime.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateTime(){
        new TimePickerDialog(this, t, dateTime.get(Calendar.HOUR_OF_DAY), dateTime.get(Calendar.MINUTE), false).show();
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            dateTime.set(Calendar.YEAR, year);
            dateTime.set(Calendar.MONTH, month);
            dateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            ValueAlarm = Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(dayOfMonth) + " ";
            updateTime();
        }
    };

    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateTime.set(Calendar.MINUTE, minute);
            updateDateTime();
            ValueAlarm = ValueAlarm + Integer.toString(hourOfDay) + ":" + Integer.toString(minute);
        }
    };

    public void updateDateTime(){
        setTanggalJam = formatDate.format(dateTime.getTime());
        setTanggalJam = setTanggalJam + " " + formatTime.format(dateTime.getTime());
        ValueAlarm = setTanggalJam;
        tgl_acara.setText(setTanggalJam);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate dari menu; disini akan menambahkan item menu pada Actionbar
        getMenuInflater().inflate(R.menu.menu_add_note, menu);//Memanggil file bernama menu_add_note di folder menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        myDb = new DatabaseHelper(this);
        switch (item.getItemId()) {
            case R.id.action_save:
                String jdl = judul.getText().toString();
                String lks = lokasi.getText().toString();
                String kategori = tag.getSelectedItem().toString();
                String tglacara = ValueAlarm;
                String des = deskripsi.getText().toString();
                String alr = alarm.getSelectedItem().toString();

                if (judul.length()!= 0 && des.length()!= 0 && kategori.equals("Uncategorized")) {
                    kategori = " ";
                    AddData(jdl, lks, kategori, tglacara, des, alr, setTanggalJam);
                }
                else if(judul.length()!= 0 && des.length()!= 0 && kategori.compareTo("Uncategorized")!= 0){
                    AddData(jdl, lks, kategori, tglacara, des, alr, setTanggalJam);
                }
                else
                    Toast.makeText(AddNote.this, "Please fill all field!", Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_cancel:
                Intent i = new Intent(AddNote.this, Home.class);
                finish();
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
