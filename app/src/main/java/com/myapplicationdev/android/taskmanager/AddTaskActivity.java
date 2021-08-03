package com.myapplicationdev.android.taskmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class AddTaskActivity extends AppCompatActivity {

    int requestCode = 123;
    int notificationID = 888;
    EditText etName, etDesc;
    Button btnAdd, btnCancel;
    ArrayList<Task> al;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        etName = findViewById(R.id.etName);
        etDesc = findViewById(R.id.etDesc);
        btnAdd = findViewById(R.id.btnAdd);
        btnCancel = findViewById(R.id.btnCancel);

        al = new ArrayList<Task>();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationManager notificationManager = (NotificationManager)
                        getSystemService(NOTIFICATION_SERVICE);
                DBHelper dbh = new DBHelper(getApplicationContext());
                if (etName.getText().toString() != null && etDesc.getText().toString() != null) {
                    //inserting task
                    dbh.insertTask(etDesc.getText().toString(), etName.getText().toString());
                    dbh.close();

                    //sending noti
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel channel = new
                                NotificationChannel("default", "Default Channel",
                                NotificationManager.IMPORTANCE_DEFAULT);

                        channel.setDescription("This is for default notification");
                        notificationManager.createNotificationChannel(channel);
                    }

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    PendingIntent pIntent = PendingIntent.getActivity
                            (getApplicationContext(), requestCode, intent,
                                    PendingIntent.FLAG_CANCEL_CURRENT);

                    // Build notification
                    NotificationCompat.Builder builder = new
                            NotificationCompat.Builder(getApplicationContext(), "default");
                    builder.setContentTitle("Task Manager Reminder");
                    builder.setContentText("Post Letters");
                    builder.setSmallIcon(android.R.drawable.btn_star_big_off);
                    builder.setContentIntent(pIntent);
                    builder.setAutoCancel(true);

                    Notification n = builder.build();

                    // An integer good to have, for you to programmatically cancel it
                    notificationManager.notify(notificationID, n);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Do not leave any fields blank",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}