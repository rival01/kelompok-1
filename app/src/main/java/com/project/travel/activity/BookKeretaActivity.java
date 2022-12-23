package com.project.travel.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.project.travel.R;
import com.project.travel.database.DatabaseHelper;
import com.project.travel.session.SessionManager;

import java.util.Calendar;
import java.util.HashMap;

public class BookKeretaActivity extends AppCompatActivity {

    protected Cursor cursor;
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    Spinner spinAsal, spinTujuan, spinDewasa, spinAnak;
    SessionManager session;
    String email;
    int id_book;
    public String sAsal, sTujuan, sTanggal, sDewasa, sAnak;
    int jmlDewasa, jmlAnak;
    int hargaDewasa, hargaAnak;
    int hargaTotalDewasa, hargaTotalAnak, hargaTotal;
    private EditText etTanggal;
    private DatePickerDialog dpTanggal;
    Calendar newCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_kereta);

        dbHelper = new DatabaseHelper(BookKeretaActivity.this);
        db = dbHelper.getReadableDatabase();

        final String[] asal = {"Pelabuhan Merak", "Jakarta", "Bandung", "Tasikmalaya", "Pekalongan","Purwokerto","Semarang","Kebumen","Yogyakarta","Madiun","Kediri","Pacitan","Tulung Agung","Surabaya"};
        final String[] tujuan = {"Pelabuhan Merak", "Jakarta", "Bandung", "Tasikmalaya", "Pekalongan","Purwokerto","Semarang","Kebumen","Yogyakarta","Madiun","Kediri","Pacitan","Tulung Agung","Surabaya"};
        final String[] dewasa = {"0", "1", "2", "3", "4", "5","6","7","8","9","10"};
        final String[] anak = {"0", "1", "2", "3", "4", "5","6","7","8","9","10"};

        spinAsal = findViewById(R.id.asal);
        spinTujuan = findViewById(R.id.tujuan);
        spinDewasa = findViewById(R.id.dewasa);
        spinAnak = findViewById(R.id.anak);

        ArrayAdapter<CharSequence> adapterAsal = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, asal);
        adapterAsal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinAsal.setAdapter(adapterAsal);

        ArrayAdapter<CharSequence> adapterTujuan = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, tujuan);
        adapterTujuan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinTujuan.setAdapter(adapterTujuan);

        ArrayAdapter<CharSequence> adapterDewasa = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, dewasa);
        adapterDewasa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinDewasa.setAdapter(adapterDewasa);

        ArrayAdapter<CharSequence> adapterAnak = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, anak);
        adapterAnak.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinAnak.setAdapter(adapterAnak);

        spinAsal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sAsal = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinTujuan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sTujuan = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinDewasa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sDewasa = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinAnak.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sAnak = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button btnBook = findViewById(R.id.book);

        etTanggal = findViewById(R.id.tanggal_berangkat);
        etTanggal.setInputType(InputType.TYPE_NULL);
        etTanggal.requestFocus();
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        email = user.get(SessionManager.KEY_EMAIL);
        setDateTimeField();

        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                perhitunganHarga();
                if (sAsal != null && sTujuan != null && sTanggal != null && sDewasa != null) {
                    if ((sAsal.equalsIgnoreCase("Pelabuhan Merak") && sTujuan.equalsIgnoreCase("Pelabuhan Merak"))
                            || (sAsal.equalsIgnoreCase("Jakarta") && sTujuan.equalsIgnoreCase("Jakarta"))
                            || (sAsal.equalsIgnoreCase("bandung") && sTujuan.equalsIgnoreCase("bandung"))
                            || (sAsal.equalsIgnoreCase("Tasikmalaya") && sTujuan.equalsIgnoreCase("Tasikmalaya"))
                            || (sAsal.equalsIgnoreCase("Pekalongan") && sTujuan.equalsIgnoreCase("Pekalongan"))
                            || (sAsal.equalsIgnoreCase("purwokerto") && sTujuan.equalsIgnoreCase("purwokerto"))
                            || (sAsal.equalsIgnoreCase("Semarang") && sTujuan.equalsIgnoreCase("Semarang"))
                            || (sAsal.equalsIgnoreCase("Kebumen") && sTujuan.equalsIgnoreCase("Kebumen"))
                            || (sAsal.equalsIgnoreCase("yogyakarta") && sTujuan.equalsIgnoreCase("yogyakarta"))
                            || (sAsal.equalsIgnoreCase("Madiun") && sTujuan.equalsIgnoreCase("Madiun"))
                            || (sAsal.equalsIgnoreCase("Kediri") && sTujuan.equalsIgnoreCase("Kediri"))
                            || (sAsal.equalsIgnoreCase("Pacitan") && sTujuan.equalsIgnoreCase("Pacitan"))
                            || (sAsal.equalsIgnoreCase("Tulung Agung") && sTujuan.equalsIgnoreCase("Tulung Agung"))
                            || (sAsal.equalsIgnoreCase("surabaya") && sTujuan.equalsIgnoreCase("surabaya"))) {
                        Toast.makeText(BookKeretaActivity.this, "Asal dan Tujuan tidak boleh sama !", Toast.LENGTH_LONG).show();
                    } else {
                        AlertDialog dialog = new AlertDialog.Builder(BookKeretaActivity.this)
                                .setTitle("Ingin booking kereta sekarang?")
                                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        try {
                                            db.execSQL("INSERT INTO TB_BOOK (asal, tujuan, tanggal, dewasa, anak) VALUES ('" +
                                                    sAsal + "','" +
                                                    sTujuan + "','" +
                                                    sTanggal + "','" +
                                                    sDewasa + "','" +
                                                    sAnak + "');");
                                            cursor = db.rawQuery("SELECT id_book FROM TB_BOOK ORDER BY id_book DESC", null);
                                            cursor.moveToLast();
                                            if (cursor.getCount() > 0) {
                                                cursor.moveToPosition(0);
                                                id_book = cursor.getInt(0);
                                            }
                                            db.execSQL("INSERT INTO TB_HARGA (username, id_book, harga_dewasa, harga_anak, harga_total) VALUES ('" +
                                                    email + "','" +
                                                    id_book + "','" +
                                                    hargaTotalDewasa + "','" +
                                                    hargaTotalAnak + "','" +
                                                    hargaTotal + "');");
                                            Toast.makeText(BookKeretaActivity.this, "Booking berhasil", Toast.LENGTH_LONG).show();
                                            finish();
                                        } catch (Exception e) {
                                            Toast.makeText(BookKeretaActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                })
                                .setNegativeButton("Tidak", null)
                                .create();
                        dialog.show();
                    }
                } else {
                    Toast.makeText(BookKeretaActivity.this, "Mohon lengkapi data pemesanan!", Toast.LENGTH_LONG).show();
                }
            }
        });

        setupToolbar();

    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.tbKrl);
        toolbar.setTitle("Form Booking");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void perhitunganHarga() {
        if (sAsal.equalsIgnoreCase("Pelabuhan Merak") && sTujuan.equalsIgnoreCase("Jakarta")) {
            hargaDewasa = 100000;
            hargaAnak = 70000;
        } else if (sAsal.equalsIgnoreCase("Pelabuhan Merak") && sTujuan.equalsIgnoreCase("Bandung")) {
            hargaDewasa = 200000;
            hargaAnak = 150000;
        } else if (sAsal.equalsIgnoreCase("Pelabuhan Merak") && sTujuan.equalsIgnoreCase("Tasikmalaya")) {
            hargaDewasa = 150000;
            hargaAnak = 120000;
        } else if (sAsal.equalsIgnoreCase("Pelabuhan Merak") && sTujuan.equalsIgnoreCase("Pekalongan")) {
            hargaDewasa = 180000;
            hargaAnak = 140000;
        } else if (sAsal.equalsIgnoreCase("Pelabuhan Merak") && sTujuan.equalsIgnoreCase("Purwokerto")) {
            hargaDewasa = 100000;
            hargaAnak = 70000;
        } else if (sAsal.equalsIgnoreCase("Pelabuhan Merak") && sTujuan.equalsIgnoreCase("Semarang")) {
            hargaDewasa = 120000;
            hargaAnak = 100000;
        } else if (sAsal.equalsIgnoreCase("Pelabuhan Merak") && sTujuan.equalsIgnoreCase("Kebumen")) {
            hargaDewasa = 120000;
            hargaAnak = 90000;
        } else if (sAsal.equalsIgnoreCase("Pelabuhan Merak") && sTujuan.equalsIgnoreCase("Yogyakarta")) {
            hargaDewasa = 190000;
            hargaAnak = 160000;
        } else if (sAsal.equalsIgnoreCase("Pelabuhan Merak") && sTujuan.equalsIgnoreCase("Madiun")) {
            hargaDewasa = 200000;
            hargaAnak = 150000;
        } else if (sAsal.equalsIgnoreCase("Pelabuhan Merak") && sTujuan.equalsIgnoreCase("Kediri")) {
            hargaDewasa = 120000;
            hargaAnak = 100000;
        } else if (sAsal.equalsIgnoreCase("Pelabuhan Merak") && sTujuan.equalsIgnoreCase("Tulung Agung")) {
            hargaDewasa = 170000;
            hargaAnak = 130000;
        } else if (sAsal.equalsIgnoreCase("Pelabuhan Merak") && sTujuan.equalsIgnoreCase("Surabaya")) {
            hargaDewasa = 180000;
            hargaAnak = 150000;


        } else if (sAsal.equalsIgnoreCase("Jakarta") && sTujuan.equalsIgnoreCase("Pelabuhan Merak")) {
            hargaDewasa = 100000;
            hargaAnak = 70000;
        } else if (sAsal.equalsIgnoreCase("Jakarta") && sTujuan.equalsIgnoreCase("Bandung")) {
            hargaDewasa = 200000;
            hargaAnak = 150000;
        } else if (sAsal.equalsIgnoreCase("Jakarta") && sTujuan.equalsIgnoreCase("Tasikmalaya")) {
            hargaDewasa = 150000;
            hargaAnak = 120000;
        } else if (sAsal.equalsIgnoreCase("Jakarta") && sTujuan.equalsIgnoreCase("Pekalongan")) {
            hargaDewasa = 180000;
            hargaAnak = 140000;
        } else if (sAsal.equalsIgnoreCase("Jakarta") && sTujuan.equalsIgnoreCase("Purwokerto")) {
            hargaDewasa = 100000;
            hargaAnak = 70000;
        } else if (sAsal.equalsIgnoreCase("Jakarta") && sTujuan.equalsIgnoreCase("Semarang")) {
            hargaDewasa = 120000;
            hargaAnak = 100000;
        } else if (sAsal.equalsIgnoreCase("Jakarta") && sTujuan.equalsIgnoreCase("Kebumen")) {
            hargaDewasa = 120000;
            hargaAnak = 90000;
        } else if (sAsal.equalsIgnoreCase("Jakarta") && sTujuan.equalsIgnoreCase("Yogyakarta")) {
            hargaDewasa = 190000;
            hargaAnak = 160000;
        } else if (sAsal.equalsIgnoreCase("Jakarta") && sTujuan.equalsIgnoreCase("Madiun")) {
            hargaDewasa = 200000;
            hargaAnak = 150000;
        } else if (sAsal.equalsIgnoreCase("Jakarta") && sTujuan.equalsIgnoreCase("Kediri")) {
            hargaDewasa = 120000;
            hargaAnak = 100000;
        } else if (sAsal.equalsIgnoreCase("Jakarta") && sTujuan.equalsIgnoreCase("Tulung Agung")) {
            hargaDewasa = 170000;
            hargaAnak = 130000;
        } else if (sAsal.equalsIgnoreCase("Jakarta") && sTujuan.equalsIgnoreCase("Surabaya")) {
            hargaDewasa = 180000;
            hargaAnak = 150000;



        } else if (sAsal.equalsIgnoreCase("Bandung") && sTujuan.equalsIgnoreCase("Pelabuhan Merak")) {
            hargaDewasa = 100000;
            hargaAnak = 70000;
        } else if (sAsal.equalsIgnoreCase("Bandung") && sTujuan.equalsIgnoreCase("Jakarta")) {
            hargaDewasa = 200000;
            hargaAnak = 150000;
        } else if (sAsal.equalsIgnoreCase("Bandung") && sTujuan.equalsIgnoreCase("Tasikmalaya")) {
            hargaDewasa = 150000;
            hargaAnak = 120000;
        } else if (sAsal.equalsIgnoreCase("Bandung") && sTujuan.equalsIgnoreCase("Pekalongan")) {
            hargaDewasa = 180000;
            hargaAnak = 140000;
        } else if (sAsal.equalsIgnoreCase("Bandung") && sTujuan.equalsIgnoreCase("Purwokerto")) {
            hargaDewasa = 100000;
            hargaAnak = 70000;
        } else if (sAsal.equalsIgnoreCase("Bandung") && sTujuan.equalsIgnoreCase("Semarang")) {
            hargaDewasa = 120000;
            hargaAnak = 100000;
        } else if (sAsal.equalsIgnoreCase("Bandung") && sTujuan.equalsIgnoreCase("Kebumen")) {
            hargaDewasa = 120000;
            hargaAnak = 90000;
        } else if (sAsal.equalsIgnoreCase("Bandung") && sTujuan.equalsIgnoreCase("Yogyakarta")) {
            hargaDewasa = 190000;
            hargaAnak = 160000;
        } else if (sAsal.equalsIgnoreCase("Bandung") && sTujuan.equalsIgnoreCase("Madiun")) {
            hargaDewasa = 200000;
            hargaAnak = 150000;
        } else if (sAsal.equalsIgnoreCase("Bandung") && sTujuan.equalsIgnoreCase("Kediri")) {
            hargaDewasa = 120000;
            hargaAnak = 100000;
        } else if (sAsal.equalsIgnoreCase("Bandung") && sTujuan.equalsIgnoreCase("Tulung Agung")) {
            hargaDewasa = 170000;
            hargaAnak = 130000;
        } else if (sAsal.equalsIgnoreCase("Bandung") && sTujuan.equalsIgnoreCase("Surabaya")) {
            hargaDewasa = 180000;
            hargaAnak = 150000;



        } else if (sAsal.equalsIgnoreCase("Tasikmalaya") && sTujuan.equalsIgnoreCase("Pelabuhan Merak")) {
            hargaDewasa = 100000;
            hargaAnak = 70000;
        } else if (sAsal.equalsIgnoreCase("Tasikmalaya") && sTujuan.equalsIgnoreCase("Jakarta")) {
            hargaDewasa = 200000;
            hargaAnak = 150000;
        } else if (sAsal.equalsIgnoreCase("Tasikmalaya") && sTujuan.equalsIgnoreCase("Bandung")) {
            hargaDewasa = 150000;
            hargaAnak = 120000;
        } else if (sAsal.equalsIgnoreCase("Tasikmalaya") && sTujuan.equalsIgnoreCase("Pekalongan")) {
            hargaDewasa = 180000;
            hargaAnak = 140000;
        } else if (sAsal.equalsIgnoreCase("Tasikmalaya") && sTujuan.equalsIgnoreCase("Purwokerto")) {
            hargaDewasa = 100000;
            hargaAnak = 70000;
        } else if (sAsal.equalsIgnoreCase("Tasikmalaya") && sTujuan.equalsIgnoreCase("Semarang")) {
            hargaDewasa = 120000;
            hargaAnak = 100000;
        } else if (sAsal.equalsIgnoreCase("Tasikmalaya") && sTujuan.equalsIgnoreCase("Kebumen")) {
            hargaDewasa = 120000;
            hargaAnak = 90000;
        } else if (sAsal.equalsIgnoreCase("Tasikmalaya") && sTujuan.equalsIgnoreCase("Yogyakarta")) {
            hargaDewasa = 190000;
            hargaAnak = 160000;
        } else if (sAsal.equalsIgnoreCase("Tasikmalaya") && sTujuan.equalsIgnoreCase("Madiun")) {
            hargaDewasa = 200000;
            hargaAnak = 150000;
        } else if (sAsal.equalsIgnoreCase("Tasikmalaya") && sTujuan.equalsIgnoreCase("Kediri")) {
            hargaDewasa = 120000;
            hargaAnak = 100000;
        } else if (sAsal.equalsIgnoreCase("Tasikmalaya") && sTujuan.equalsIgnoreCase("Tulung Agung")) {
            hargaDewasa = 170000;
            hargaAnak = 130000;
        } else if (sAsal.equalsIgnoreCase("Tasikmalaya") && sTujuan.equalsIgnoreCase("Surabaya")) {
            hargaDewasa = 180000;
            hargaAnak = 150000;



        } else if (sAsal.equalsIgnoreCase("Pekalongan") && sTujuan.equalsIgnoreCase("Pelabuhan Merak")) {
            hargaDewasa = 100000;
            hargaAnak = 70000;
        } else if (sAsal.equalsIgnoreCase("Pekalongan") && sTujuan.equalsIgnoreCase("Jakarta")) {
            hargaDewasa = 200000;
            hargaAnak = 150000;
        } else if (sAsal.equalsIgnoreCase("Pekalongan") && sTujuan.equalsIgnoreCase("Bandung")) {
            hargaDewasa = 150000;
            hargaAnak = 120000;
        } else if (sAsal.equalsIgnoreCase("Pekalongan") && sTujuan.equalsIgnoreCase("Tasikmalaya")) {
            hargaDewasa = 180000;
            hargaAnak = 140000;
        } else if (sAsal.equalsIgnoreCase("Pekalongan") && sTujuan.equalsIgnoreCase("Purwokerto")) {
            hargaDewasa = 100000;
            hargaAnak = 70000;
        } else if (sAsal.equalsIgnoreCase("Pekalongan") && sTujuan.equalsIgnoreCase("Semarang")) {
            hargaDewasa = 120000;
            hargaAnak = 100000;
        } else if (sAsal.equalsIgnoreCase("Pekalongan") && sTujuan.equalsIgnoreCase("Kebumen")) {
            hargaDewasa = 120000;
            hargaAnak = 90000;
        } else if (sAsal.equalsIgnoreCase("Pekalongan") && sTujuan.equalsIgnoreCase("Yogyakarta")) {
            hargaDewasa = 190000;
            hargaAnak = 160000;
        } else if (sAsal.equalsIgnoreCase("Pekalongan") && sTujuan.equalsIgnoreCase("Madiun")) {
            hargaDewasa = 200000;
            hargaAnak = 150000;
        } else if (sAsal.equalsIgnoreCase("Pekalongan") && sTujuan.equalsIgnoreCase("Kediri")) {
            hargaDewasa = 120000;
            hargaAnak = 100000;
        } else if (sAsal.equalsIgnoreCase("Pekalongan") && sTujuan.equalsIgnoreCase("Tulung Agung")) {
            hargaDewasa = 170000;
            hargaAnak = 130000;
        } else if (sAsal.equalsIgnoreCase("Pekalongan") && sTujuan.equalsIgnoreCase("Surabaya")) {
            hargaDewasa = 180000;
            hargaAnak = 150000;


        } else if (sAsal.equalsIgnoreCase("Purwokerto") && sTujuan.equalsIgnoreCase("Pelabuhan Merak")) {
            hargaDewasa = 100000;
            hargaAnak = 70000;
        } else if (sAsal.equalsIgnoreCase("Purwokerto") && sTujuan.equalsIgnoreCase("Jakarta")) {
            hargaDewasa = 200000;
            hargaAnak = 150000;
        } else if (sAsal.equalsIgnoreCase("Purwokerto") && sTujuan.equalsIgnoreCase("Bandung")) {
            hargaDewasa = 150000;
            hargaAnak = 120000;
        } else if (sAsal.equalsIgnoreCase("Purwokerto") && sTujuan.equalsIgnoreCase("Tasikmalaya")) {
            hargaDewasa = 180000;
            hargaAnak = 140000;
        } else if (sAsal.equalsIgnoreCase("Purwokerto") && sTujuan.equalsIgnoreCase("Pekalongan")) {
            hargaDewasa = 100000;
            hargaAnak = 70000;
        } else if (sAsal.equalsIgnoreCase("Purwokerto") && sTujuan.equalsIgnoreCase("Semarang")) {
            hargaDewasa = 120000;
            hargaAnak = 100000;
        } else if (sAsal.equalsIgnoreCase("Purwokerto") && sTujuan.equalsIgnoreCase("Kebumen")) {
            hargaDewasa = 120000;
            hargaAnak = 90000;
        } else if (sAsal.equalsIgnoreCase("Purwokerto") && sTujuan.equalsIgnoreCase("Yogyakarta")) {
            hargaDewasa = 190000;
            hargaAnak = 160000;
        } else if (sAsal.equalsIgnoreCase("Purwokerto") && sTujuan.equalsIgnoreCase("Madiun")) {
            hargaDewasa = 200000;
            hargaAnak = 150000;
        } else if (sAsal.equalsIgnoreCase("Purwokerto") && sTujuan.equalsIgnoreCase("Kediri")) {
            hargaDewasa = 120000;
            hargaAnak = 100000;
        } else if (sAsal.equalsIgnoreCase("Purwokerto") && sTujuan.equalsIgnoreCase("Tulung Agung")) {
            hargaDewasa = 170000;
            hargaAnak = 130000;
        } else if (sAsal.equalsIgnoreCase("Purwokerto") && sTujuan.equalsIgnoreCase("Surabaya")) {
            hargaDewasa = 180000;
            hargaAnak = 150000;


        } else if (sAsal.equalsIgnoreCase("Semarang") && sTujuan.equalsIgnoreCase("Pelabuhan Merak")) {
            hargaDewasa = 100000;
            hargaAnak = 70000;
        } else if (sAsal.equalsIgnoreCase("Semarang") && sTujuan.equalsIgnoreCase("Jakarta")) {
            hargaDewasa = 200000;
            hargaAnak = 150000;
        } else if (sAsal.equalsIgnoreCase("Semarang") && sTujuan.equalsIgnoreCase("Bandung")) {
            hargaDewasa = 150000;
            hargaAnak = 120000;
        } else if (sAsal.equalsIgnoreCase("Semarang") && sTujuan.equalsIgnoreCase("Tasikmalaya")) {
            hargaDewasa = 180000;
            hargaAnak = 140000;
        } else if (sAsal.equalsIgnoreCase("Semarang") && sTujuan.equalsIgnoreCase("Pekalongan")) {
            hargaDewasa = 100000;
            hargaAnak = 70000;
        } else if (sAsal.equalsIgnoreCase("Semarang") && sTujuan.equalsIgnoreCase("Purwokerto")) {
            hargaDewasa = 120000;
            hargaAnak = 100000;
        } else if (sAsal.equalsIgnoreCase("Semarang") && sTujuan.equalsIgnoreCase("Kebumen")) {
            hargaDewasa = 120000;
            hargaAnak = 90000;
        } else if (sAsal.equalsIgnoreCase("Semarang") && sTujuan.equalsIgnoreCase("Yogyakarta")) {
            hargaDewasa = 190000;
            hargaAnak = 160000;
        } else if (sAsal.equalsIgnoreCase("Semarang") && sTujuan.equalsIgnoreCase("Madiun")) {
            hargaDewasa = 200000;
            hargaAnak = 150000;
        } else if (sAsal.equalsIgnoreCase("Semarang") && sTujuan.equalsIgnoreCase("Kediri")) {
            hargaDewasa = 120000;
            hargaAnak = 100000;
        } else if (sAsal.equalsIgnoreCase("Semarang") && sTujuan.equalsIgnoreCase("Tulung Agung")) {
            hargaDewasa = 170000;
            hargaAnak = 130000;
        } else if (sAsal.equalsIgnoreCase("Semarang") && sTujuan.equalsIgnoreCase("Surabaya")) {
            hargaDewasa = 180000;
            hargaAnak = 150000;


        } else if (sAsal.equalsIgnoreCase("Kebumen") && sTujuan.equalsIgnoreCase("Pelabuhan Merak")) {
            hargaDewasa = 100000;
            hargaAnak = 70000;
        } else if (sAsal.equalsIgnoreCase("Kebumen") && sTujuan.equalsIgnoreCase("Jakarta")) {
            hargaDewasa = 200000;
            hargaAnak = 150000;
        } else if (sAsal.equalsIgnoreCase("Kebumen") && sTujuan.equalsIgnoreCase("Bandung")) {
            hargaDewasa = 150000;
            hargaAnak = 120000;
        } else if (sAsal.equalsIgnoreCase("Kebumen") && sTujuan.equalsIgnoreCase("Tasikmalaya")) {
            hargaDewasa = 180000;
            hargaAnak = 140000;
        } else if (sAsal.equalsIgnoreCase("Kebumen") && sTujuan.equalsIgnoreCase("Pekalongan")) {
            hargaDewasa = 100000;
            hargaAnak = 70000;
        } else if (sAsal.equalsIgnoreCase("Kebumen") && sTujuan.equalsIgnoreCase("Purwokerto")) {
            hargaDewasa = 120000;
            hargaAnak = 100000;
        } else if (sAsal.equalsIgnoreCase("Kebumen") && sTujuan.equalsIgnoreCase("Semarang")) {
            hargaDewasa = 120000;
            hargaAnak = 90000;
        } else if (sAsal.equalsIgnoreCase("Kebumen") && sTujuan.equalsIgnoreCase("Yogyakarta")) {
            hargaDewasa = 190000;
            hargaAnak = 160000;
        } else if (sAsal.equalsIgnoreCase("Kebumen") && sTujuan.equalsIgnoreCase("Madiun")) {
            hargaDewasa = 200000;
            hargaAnak = 150000;
        } else if (sAsal.equalsIgnoreCase("Kebumen") && sTujuan.equalsIgnoreCase("Kediri")) {
            hargaDewasa = 120000;
            hargaAnak = 100000;
        } else if (sAsal.equalsIgnoreCase("Kebumen") && sTujuan.equalsIgnoreCase("Tulung Agung")) {
            hargaDewasa = 170000;
            hargaAnak = 130000;
        } else if (sAsal.equalsIgnoreCase("Kebumen") && sTujuan.equalsIgnoreCase("Surabaya")) {
            hargaDewasa = 180000;
            hargaAnak = 150000;



        } else if (sAsal.equalsIgnoreCase("Yogyakarta") && sTujuan.equalsIgnoreCase("Pelabuhan Merak")) {
            hargaDewasa = 100000;
            hargaAnak = 70000;
        } else if (sAsal.equalsIgnoreCase("Yogyakarta") && sTujuan.equalsIgnoreCase("Jakarta")) {
            hargaDewasa = 200000;
            hargaAnak = 150000;
        } else if (sAsal.equalsIgnoreCase("Yogyakarta") && sTujuan.equalsIgnoreCase("Bandung")) {
            hargaDewasa = 150000;
            hargaAnak = 120000;
        } else if (sAsal.equalsIgnoreCase("Yogyakarta") && sTujuan.equalsIgnoreCase("Tasikmalaya")) {
            hargaDewasa = 180000;
            hargaAnak = 140000;
        } else if (sAsal.equalsIgnoreCase("Yogyakarta") && sTujuan.equalsIgnoreCase("Pekalongan")) {
            hargaDewasa = 100000;
            hargaAnak = 70000;
        } else if (sAsal.equalsIgnoreCase("Yogyakarta") && sTujuan.equalsIgnoreCase("Purwokerto")) {
            hargaDewasa = 120000;
            hargaAnak = 100000;
        } else if (sAsal.equalsIgnoreCase("Yogyakarta") && sTujuan.equalsIgnoreCase("Semarang")) {
            hargaDewasa = 120000;
            hargaAnak = 90000;
        } else if (sAsal.equalsIgnoreCase("Yogyakarta") && sTujuan.equalsIgnoreCase("Kebumen")) {
            hargaDewasa = 190000;
            hargaAnak = 160000;
        } else if (sAsal.equalsIgnoreCase("Yogyakarta") && sTujuan.equalsIgnoreCase("Madiun")) {
            hargaDewasa = 200000;
            hargaAnak = 150000;
        } else if (sAsal.equalsIgnoreCase("Yogyakarta") && sTujuan.equalsIgnoreCase("Kediri")) {
            hargaDewasa = 120000;
            hargaAnak = 100000;
        } else if (sAsal.equalsIgnoreCase("Yogyakarta") && sTujuan.equalsIgnoreCase("Tulung Agung")) {
            hargaDewasa = 170000;
            hargaAnak = 130000;
        } else if (sAsal.equalsIgnoreCase("Yogyakarta") && sTujuan.equalsIgnoreCase("Surabaya")) {
            hargaDewasa = 180000;
            hargaAnak = 150000;



        } else if (sAsal.equalsIgnoreCase("Madiun") && sTujuan.equalsIgnoreCase("Pelabuhan Merak")) {
            hargaDewasa = 100000;
            hargaAnak = 70000;
        } else if (sAsal.equalsIgnoreCase("Madiun") && sTujuan.equalsIgnoreCase("Jakarta")) {
            hargaDewasa = 200000;
            hargaAnak = 150000;
        } else if (sAsal.equalsIgnoreCase("Madiun") && sTujuan.equalsIgnoreCase("Bandung")) {
            hargaDewasa = 150000;
            hargaAnak = 120000;
        } else if (sAsal.equalsIgnoreCase("Madiun") && sTujuan.equalsIgnoreCase("Tasikmalaya")) {
            hargaDewasa = 180000;
            hargaAnak = 140000;
        } else if (sAsal.equalsIgnoreCase("Madiun") && sTujuan.equalsIgnoreCase("Pekalongan")) {
            hargaDewasa = 100000;
            hargaAnak = 70000;
        } else if (sAsal.equalsIgnoreCase("Madiun") && sTujuan.equalsIgnoreCase("Purwokerto")) {
            hargaDewasa = 120000;
            hargaAnak = 100000;
        } else if (sAsal.equalsIgnoreCase("Madiun") && sTujuan.equalsIgnoreCase("Semarang")) {
            hargaDewasa = 120000;
            hargaAnak = 90000;
        } else if (sAsal.equalsIgnoreCase("Madiun") && sTujuan.equalsIgnoreCase("Kebumen")) {
            hargaDewasa = 190000;
            hargaAnak = 160000;
        } else if (sAsal.equalsIgnoreCase("Madiun") && sTujuan.equalsIgnoreCase("Yogyakarta")) {
            hargaDewasa = 200000;
            hargaAnak = 150000;
        } else if (sAsal.equalsIgnoreCase("Madiun") && sTujuan.equalsIgnoreCase("Kediri")) {
            hargaDewasa = 120000;
            hargaAnak = 100000;
        } else if (sAsal.equalsIgnoreCase("Madiun") && sTujuan.equalsIgnoreCase("Tulung Agung")) {
            hargaDewasa = 170000;
            hargaAnak = 130000;
        } else if (sAsal.equalsIgnoreCase("Madiun") && sTujuan.equalsIgnoreCase("Surabaya")) {
            hargaDewasa = 180000;
            hargaAnak = 150000;


        } else if (sAsal.equalsIgnoreCase("Kediri") && sTujuan.equalsIgnoreCase("Pelabuhan Merak")) {
            hargaDewasa = 100000;
            hargaAnak = 70000;
        } else if (sAsal.equalsIgnoreCase("Kediri") && sTujuan.equalsIgnoreCase("Jakarta")) {
            hargaDewasa = 200000;
            hargaAnak = 150000;
        } else if (sAsal.equalsIgnoreCase("Kediri") && sTujuan.equalsIgnoreCase("Bandung")) {
            hargaDewasa = 150000;
            hargaAnak = 120000;
        } else if (sAsal.equalsIgnoreCase("Kediri") && sTujuan.equalsIgnoreCase("Tasikmalaya")) {
            hargaDewasa = 180000;
            hargaAnak = 140000;
        } else if (sAsal.equalsIgnoreCase("Kediri") && sTujuan.equalsIgnoreCase("Pekalongan")) {
            hargaDewasa = 100000;
            hargaAnak = 70000;
        } else if (sAsal.equalsIgnoreCase("Kediri") && sTujuan.equalsIgnoreCase("Purwokerto")) {
            hargaDewasa = 120000;
            hargaAnak = 100000;
        } else if (sAsal.equalsIgnoreCase("Kediri") && sTujuan.equalsIgnoreCase("Semarang")) {
            hargaDewasa = 120000;
            hargaAnak = 90000;
        } else if (sAsal.equalsIgnoreCase("Kediri") && sTujuan.equalsIgnoreCase("Kebumen")) {
            hargaDewasa = 190000;
            hargaAnak = 160000;
        } else if (sAsal.equalsIgnoreCase("Kediri") && sTujuan.equalsIgnoreCase("Yogyakarta")) {
            hargaDewasa = 200000;
            hargaAnak = 150000;
        } else if (sAsal.equalsIgnoreCase("Kediri") && sTujuan.equalsIgnoreCase("Madiun")) {
            hargaDewasa = 120000;
            hargaAnak = 100000;
        } else if (sAsal.equalsIgnoreCase("Kediri") && sTujuan.equalsIgnoreCase("Tulung Agung")) {
            hargaDewasa = 170000;
            hargaAnak = 130000;
        } else if (sAsal.equalsIgnoreCase("Kediri") && sTujuan.equalsIgnoreCase("Surabaya")) {
            hargaDewasa = 180000;
            hargaAnak = 150000;



        } else if (sAsal.equalsIgnoreCase("Tulung Agung") && sTujuan.equalsIgnoreCase("Pelabuhan Merak")) {
            hargaDewasa = 100000;
            hargaAnak = 70000;
        } else if (sAsal.equalsIgnoreCase("Tulung Agung") && sTujuan.equalsIgnoreCase("Jakarta")) {
            hargaDewasa = 200000;
            hargaAnak = 150000;
        } else if (sAsal.equalsIgnoreCase("Tulung Agung") && sTujuan.equalsIgnoreCase("Bandung")) {
            hargaDewasa = 150000;
            hargaAnak = 120000;
        } else if (sAsal.equalsIgnoreCase("Tulung Agung") && sTujuan.equalsIgnoreCase("Tasikmalaya")) {
            hargaDewasa = 180000;
            hargaAnak = 140000;
        } else if (sAsal.equalsIgnoreCase("Tulung Agung") && sTujuan.equalsIgnoreCase("Pekalongan")) {
            hargaDewasa = 100000;
            hargaAnak = 70000;
        } else if (sAsal.equalsIgnoreCase("Tulung Agung") && sTujuan.equalsIgnoreCase("Purwokerto")) {
            hargaDewasa = 120000;
            hargaAnak = 100000;
        } else if (sAsal.equalsIgnoreCase("Tulung Agung") && sTujuan.equalsIgnoreCase("Semarang")) {
            hargaDewasa = 120000;
            hargaAnak = 90000;
        } else if (sAsal.equalsIgnoreCase("Tulung Agung") && sTujuan.equalsIgnoreCase("Kebumen")) {
            hargaDewasa = 190000;
            hargaAnak = 160000;
        } else if (sAsal.equalsIgnoreCase("Tulung Agung") && sTujuan.equalsIgnoreCase("Yogyakarta")) {
            hargaDewasa = 200000;
            hargaAnak = 150000;
        } else if (sAsal.equalsIgnoreCase("Tulung Agung") && sTujuan.equalsIgnoreCase("Madiun")) {
            hargaDewasa = 120000;
            hargaAnak = 100000;
        } else if (sAsal.equalsIgnoreCase("Tulung Agung") && sTujuan.equalsIgnoreCase("Kediri")) {
            hargaDewasa = 170000;
            hargaAnak = 130000;
        } else if (sAsal.equalsIgnoreCase("Tulung Agung") && sTujuan.equalsIgnoreCase("Surabaya")) {
            hargaDewasa = 180000;
            hargaAnak = 150000;


        } else if (sAsal.equalsIgnoreCase("Surabaya") && sTujuan.equalsIgnoreCase("Pelabuhan Merak")) {
            hargaDewasa = 100000;
            hargaAnak = 70000;
        } else if (sAsal.equalsIgnoreCase("Surabaya") && sTujuan.equalsIgnoreCase("Jakarta")) {
            hargaDewasa = 200000;
            hargaAnak = 150000;
        } else if (sAsal.equalsIgnoreCase("Surabaya") && sTujuan.equalsIgnoreCase("Bandung")) {
            hargaDewasa = 150000;
            hargaAnak = 120000;
        } else if (sAsal.equalsIgnoreCase("Surabaya") && sTujuan.equalsIgnoreCase("Tasikmalaya")) {
            hargaDewasa = 180000;
            hargaAnak = 140000;
        } else if (sAsal.equalsIgnoreCase("Surabaya") && sTujuan.equalsIgnoreCase("Pekalongan")) {
            hargaDewasa = 100000;
            hargaAnak = 70000;
        } else if (sAsal.equalsIgnoreCase("Surabaya") && sTujuan.equalsIgnoreCase("Purwokerto")) {
            hargaDewasa = 120000;
            hargaAnak = 100000;
        } else if (sAsal.equalsIgnoreCase("Surabaya") && sTujuan.equalsIgnoreCase("Semarang")) {
            hargaDewasa = 120000;
            hargaAnak = 90000;
        } else if (sAsal.equalsIgnoreCase("Surabaya") && sTujuan.equalsIgnoreCase("Kebumen")) {
            hargaDewasa = 190000;
            hargaAnak = 160000;
        } else if (sAsal.equalsIgnoreCase("Surabaya") && sTujuan.equalsIgnoreCase("Yogyakarta")) {
            hargaDewasa = 200000;
            hargaAnak = 150000;
        } else if (sAsal.equalsIgnoreCase("Surabaya") && sTujuan.equalsIgnoreCase("Madiun")) {
            hargaDewasa = 120000;
            hargaAnak = 100000;
        } else if (sAsal.equalsIgnoreCase("Surabaya") && sTujuan.equalsIgnoreCase("Kediri")) {
            hargaDewasa = 170000;
            hargaAnak = 130000;
        } else if (sAsal.equalsIgnoreCase("Surabaya") && sTujuan.equalsIgnoreCase("Tulung Agung")) {
            hargaDewasa = 180000;
            hargaAnak = 150000;
        }

        jmlDewasa = Integer.parseInt(sDewasa);
        jmlAnak = Integer.parseInt(sAnak);

        hargaTotalDewasa = jmlDewasa * hargaDewasa;
        hargaTotalAnak = jmlAnak * hargaAnak;
        hargaTotal = hargaTotalDewasa + hargaTotalAnak;
    }

    private void setDateTimeField() {
        etTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpTanggal.show();
            }
        });

        dpTanggal = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                String[] bulan = {"Januari", "Februari", "Maret", "April", "Mei",
                        "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
                sTanggal = dayOfMonth + " " + bulan[monthOfYear] + " " + year;
                etTanggal.setText(sTanggal);

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }
}