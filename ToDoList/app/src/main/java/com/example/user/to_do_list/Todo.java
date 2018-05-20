package com.example.user.to_do_list;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import model.TodoModel;

public class Todo extends AppCompatActivity {

    EditText editTextName,editTextMessage,editTextDate;
    Button add;
    DatePickerDialog datePickerDialog;
    int mYear,mMonth,mDay,mPriority;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        ((RadioButton) findViewById(R.id.radButton1)).setChecked(true);
        mPriority = 1;
        editTextName = (EditText)findViewById(R.id.editTextName);
        editTextMessage = (EditText)findViewById(R.id.editTextMessage);
        editTextDate = (EditText) findViewById(R.id.editTextDate);

        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR); // current year
                mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(Todo.this,new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                editTextDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        }

    public void onPrioritySelected(View view)
    {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (((RadioButton) findViewById(R.id.radButton1)).isChecked()) {
                    mPriority = 1;
                } else if (((RadioButton) findViewById(R.id.radButton2)).isChecked()) {
                    mPriority = 2;
                } else if (((RadioButton) findViewById(R.id.radButton3)).isChecked()) {
                    mPriority = 3;
                }
            }
        };
        Thread thr1 = new Thread(runnable);
        thr1.start();
    }




    public void fnSave(View vw)
    {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                String key = database.getReference("todoList").push().getKey();
                TodoModel todo = new TodoModel();
                todo.setId(key);
                todo.setName(editTextName.getText().toString());
                todo.setMessage(editTextMessage.getText().toString());
                todo.setDate(editTextDate.getText().toString());
                todo.setStatus("0");
                todo.setPriority(Integer.toString(mPriority));

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put( key, todo.toFirebaseObject());
                database.getReference("todoList").updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            finish();
                        }
                    }
                });
            }
        };
        Thread thr = new Thread(runnable);
        thr.start();
    }




}