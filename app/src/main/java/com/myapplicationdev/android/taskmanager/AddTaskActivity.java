package com.myapplicationdev.android.taskmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class AddTaskActivity extends AppCompatActivity {

    int requestCode = 12345;
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

                if (!etName.getText().toString().isEmpty() && !etDesc.getText().toString().isEmpty()) {
                    //inserting task
                    String name = etName.getText().toString();
                    String desc = etDesc.getText().toString();

                    DBHelper db = new DBHelper(AddTaskActivity.this);
                    db.insertTask(desc, name);
                    Toast.makeText(getApplicationContext(), "Inserted", Toast.LENGTH_SHORT).show();
                    Log.i("info", "Task: " + name + " Desc: " + desc);
                    etName.setText("");
                    etDesc.setText("");
                    db.close();


                    //sending noti
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel channel = new
                                NotificationChannel("default", "Default Channel",
                                NotificationManager.IMPORTANCE_DEFAULT);

                        channel.setDescription("This is for default notification");
                        notificationManager.createNotificationChannel(channel);
                    }

                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.SECOND, 2);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    PendingIntent pIntent = PendingIntent.getActivity
                            (AddTaskActivity.this, requestCode, intent,
                                    PendingIntent.FLAG_CANCEL_CURRENT);

                    // Build notification



                    AlarmManager am = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
                    am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pIntent);

//                    Bitmap picture = BitmapFactory.decodeResource(getResources(), R.drawable.sentosa);
//                    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//                    long[] vibrate = {0, 100, 200,300};
//
//                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "default");
//                    builder.setContentTitle("Task Manager Reminder");
//                    builder.setContentText("Post Letters");
//                    builder.setSmallIcon(android.R.drawable.ic_dialog_info);
//                    builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(picture).bigLargeIcon(null));
//                    builder.setSound(alarmSound);
//                    builder.setVibrate(vibrate);
//                    builder.setContentIntent(pIntent);
//                    builder.setAutoCancel(true);
//
//                    Notification n = builder.build();
//
//                    // An integer good to have, for you to programmatically cancel it
//                    notificationManager.notify(notificationID, n);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Do not leave any fields blank",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}