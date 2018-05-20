package com.example.user.to_do_list;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import model.TodoModel;

public class ViewActivity extends AppCompatActivity {
    TextView editTextName, editTextMessage, editTextDate;
    int mPriority;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            TodoModel todo = (TodoModel) extras.get("todo");
            if (todo != null) {
                editTextName = (TextView) findViewById(R.id.editTextName);
                editTextMessage = (TextView) findViewById(R.id.editTextMessage);
                editTextDate = (TextView) findViewById(R.id.editTextDate);

                editTextName.setText(todo.getName());
                editTextMessage.setText(todo.getMessage());
                editTextDate.setText(todo.getDate());

                mPriority = Integer.parseInt(todo.getPriority());
                id = todo.getId();

                if (mPriority == 1) {
                    ((RadioButton) findViewById(R.id.radButton1)).setChecked(true);
                } else if (mPriority == 2) {
                    ((RadioButton) findViewById(R.id.radButton2)).setChecked(true);
                } else if (mPriority == 3) {
                    ((RadioButton) findViewById(R.id.radButton3)).setChecked(true);
                }
            }
        }

    }

    public void fnDone(View vw) {

        Runnable run = new Runnable() {
            @Override
            public void run() {
                String status = "1";
                updateUser(status);
            }
        };
        Thread thr1 = new Thread(run);
        thr1.start();


    }

    public void updateUser(final String status) {

        DatabaseReference database = FirebaseDatabase.getInstance().getReference("todoList");
        database.child(id).child("status").setValue(status);
        finish();


            }

    public void fnDelete(View vw) {

        Runnable run = new Runnable() {
            @Override
            public void run() {
                DatabaseReference database = FirebaseDatabase.getInstance().getReference("todoList");
                database.child(id).setValue(null);
                finish();
            }
        };
        Thread thr1 = new Thread(run);
        thr1.start();


    }



}


