package com.example.user.to_do_list;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import model.TodoModel;


public class MainActivity extends AppCompatActivity {

    ArrayList<TodoModel> todoList;
    final ArrayList<String> keyList = new ArrayList<>();
    RecycleAdapter adapter;
    Context mContext;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(MainActivity.this, Todo.class);
                MainActivity.this.startActivity(newIntent);
            }
        });

        todoList = new ArrayList<>();

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        adapter = new RecycleAdapter();
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();


    }


    @Override
    protected void onResume() {
        super.onResume();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        database.getReference("todoList").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        todoList.clear();

                        Log.w("TodoApp", "getUser:onCancelled " + dataSnapshot.toString());
                        Log.w("TodoApp", "count = " + String.valueOf(dataSnapshot.getChildrenCount()) + " values " + dataSnapshot.getKey());
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            TodoModel todo = data.getValue(TodoModel.class);
                            todoList.add(todo);
                        }

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("TodoApp", "getUser:onCancelled", databaseError.toException());
                    }
                });
    }

    private class RecycleAdapter extends RecyclerView.Adapter {


        @Override
        public int getItemCount() {
            return todoList.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
            SimpleItemViewHolder pvh = new SimpleItemViewHolder(v);
            return pvh;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


            SimpleItemViewHolder viewHolder = (SimpleItemViewHolder) holder;
            viewHolder.position = position;
            TodoModel todoModel = todoList.get(position);

            int priority = Integer.parseInt(todoModel.getPriority());
            int status = Integer.parseInt(todoModel.getStatus());

            ((SimpleItemViewHolder) holder).title.setText(todoModel.getName());
            ((SimpleItemViewHolder) holder).priorityView.setText(todoModel.getPriority());

            int stColor = getStatusColor(status);
            ((SimpleItemViewHolder) holder).title.setTextColor(stColor);




            GradientDrawable priorityCircle = (GradientDrawable) ((SimpleItemViewHolder) holder).priorityView.getBackground();
            // Get the appropriate background color based on the priority
            int priorityColor = getPriorityColor(priority);
            priorityCircle.setColor(priorityColor);
        }

        private int getPriorityColor(int priority) {
            int priorityColor = 0;

            switch(priority) {
                case 1: priorityColor = Color.parseColor("#E74C3C");
                    break;
                case 2: priorityColor = Color.parseColor("#E67E22");
                    break;
                case 3: priorityColor = Color.parseColor("#F1C40F");
                    break;
                default: break;
            }
            return priorityColor;
        }
        private int  getStatusColor(int status) {
            int stColor = 0;

            switch(status) {
                case 0: stColor = Color.parseColor("#000000");
                    break;
                case 1: stColor = Color.parseColor("#FF0055");
                    break;

                default: break;
            }
            return stColor;
        }

        public final class SimpleItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView title, priorityView;
            public int position;

            public SimpleItemViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                title = (TextView) itemView.findViewById(R.id.taskDescription);
                priorityView = (TextView) itemView.findViewById(R.id.priorityTextView);
            }

            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(MainActivity.this,ViewActivity.class);
                newIntent.putExtra("todo", todoList.get(position));
                MainActivity.this.startActivity(newIntent);


            }
        }
    }

}





