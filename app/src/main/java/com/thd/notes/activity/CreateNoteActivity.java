package com.thd.notes.activity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.thd.notes.R;
import com.thd.notes.adapter.ImagesAdapter;
import com.thd.notes.dialog.ChoosePicDialogFragment;
import com.thd.notes.dialog.ColorDialogFragment;
import com.thd.notes.model.Note;
import com.thd.notes.notification.PushNotification;
import com.thd.notes.utils.DbBitmapUtility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class CreateNoteActivity extends AppCompatActivity {
    public static final int TAKE_PICTURE_REQUEST = 123;
    public static final int GALLERY_REQUEST = 345;
    private EditText etTitle;
    private EditText etContent;
    private TextView tvDate;
    private TextView tvTime;
    private TextView tvCreateDate;
    private ImageButton imageButton;
    private Note note;
    private RelativeLayout relativeLayout;
    private GridView gvImage;
    private ImagesAdapter adapter;
    private ArrayList<Bitmap> imageList;
    Intent fromMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bindView();
        fromMain = getIntent();
        note = new Note();
        imageList = new ArrayList<>();
        adapter = new ImagesAdapter(this,R.layout.item_image,imageList);
        gvImage.setAdapter(adapter);
        Calendar calendar = Calendar.getInstance();
        String date = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))
                +"/"
                +(calendar.get(Calendar.MONTH)+1) + "/"
                +calendar.get(Calendar.YEAR);
        String m;
        if (calendar.get(Calendar.MINUTE)<10) m = "0"+calendar.get(Calendar.MINUTE);
        else m = String.valueOf(calendar.get(Calendar.MINUTE));
        String time = calendar.get(Calendar.HOUR_OF_DAY)+":"+m;
        note.setDate(date);
        note.setTime(time);
        tvCreateDate.setText(date +" "+time);
        adapter.setOnDeleteClick(new ImagesAdapter.OnDeleteClick() {
            @Override
            public void onClick(int position) {
                imageList.remove(position);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void bindView() {
        etTitle = (EditText) findViewById(R.id.etNoteTitle);
        etContent = (EditText) findViewById(R.id.etNoteContent);
        relativeLayout = (RelativeLayout) findViewById(R.id.noteBackground);
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvCreateDate = (TextView) findViewById(R.id.tvCreateDate);
        imageButton = (ImageButton) findViewById(R.id.cancelAlarm);
        gvImage = (GridView) findViewById(R.id.gvImages);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionSave :
                note.setNoteTitle(etTitle.getText().toString());
                note.setNoteContent(etContent.getText().toString());
                ArrayList<String> name = new ArrayList<>();
                for (Bitmap bitmap : imageList) {
                    String na = String.valueOf(Calendar.getInstance().getTimeInMillis());
                    DbBitmapUtility.saveFile(this,bitmap,na);
                    name.add(na);
                }
                note.setImageList(name);
                if(note.getAlarm() == Note.ALARM_TRUE ) {
                    note.setDateAlarm(tvDate.getText().toString());
                    note.setTimeAlarm(tvTime.getText().toString());
                    String[] temp = note.getDateAlarm().split("/");
                    String[] temp1 = note.getTimeAlarm().split(":");
                    int year = Integer.parseInt(temp[2]);
                    int month = Integer.parseInt(temp[1]);
                    int day = Integer.parseInt(temp[0]);
                    int h = Integer.parseInt(temp1[0]), m = Integer.parseInt(temp1[1]);
                    Calendar c = Calendar.getInstance();
                    c.set(year,month,day,h,m);
                    scheduleNotification(getNotification("TEST"),c.getTimeInMillis());
                }
                int id = fromMain.getIntExtra("id",-1)+1;
                note.setId(id);
                Intent intent = new Intent();
                intent.putExtra("note",note);
                Log.d("LIST", note.toString());
                setResult(RESULT_OK,intent);
                finish();
                break;
            case R.id.actionChangeColor :
                ColorDialogFragment cdf = new ColorDialogFragment();
                cdf.setOnColorPicked(new ColorDialogFragment.OnColorPicked() {
                    @Override
                    public void onCLick(String color) {
                        relativeLayout.setBackgroundColor(Color.parseColor(color));
                        note.setColor(color);
                    }
                });
                cdf.show(getFragmentManager(),"Detail");
                break;
            case R.id.actionAddPicture :
                ChoosePicDialogFragment picDialogFragment = new ChoosePicDialogFragment();
                picDialogFragment.setOnActionClicked(new ChoosePicDialogFragment.OnActionClicked() {
                    @Override
                    public void onClick(View v) {
                        if(v.getId()==R.id.btnCamera) {
                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                startActivityForResult(takePictureIntent,TAKE_PICTURE_REQUEST);
                            }
                        }
                        if (v.getId()==R.id.btnGallery) {
                            Intent takePic = new Intent();
                            takePic.setType("image/*");
                            takePic.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(takePic,"Add Picture"),GALLERY_REQUEST);
                        }
                    }
                });
                picDialogFragment.show(getFragmentManager(),"Choose picture");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setAlarm(View view) {
        tvTime.setVisibility(View.VISIBLE);
        tvDate.setVisibility(View.VISIBLE);
        imageButton.setVisibility(View.VISIBLE);
        note.setAlarm(Note.ALARM_TRUE);
    }

    public void cancelAlarm(View view) {
        tvTime.setVisibility(View.INVISIBLE);
        tvDate.setVisibility(View.INVISIBLE);
        imageButton.setVisibility(View.INVISIBLE);
        note.setAlarm(Note.ALARM_FALSE);
    }

    public void pickDate(View view) {
        int mYear;
        int mMonth;
        int mDay;
        DatePickerDialog.OnDateSetListener dateSet = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                tvDate.setText(dayOfMonth+"/"+(month+1)+"/"+year);
            }
        };
        Calendar calendar = Calendar.getInstance();
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mMonth = calendar.get(Calendar.MONTH);
        mYear = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,dateSet,mYear,mMonth,mDay);
        datePickerDialog.show();
    }

    public void pickTime(View view) {
        final int h,m;
        TimePickerDialog.OnTimeSetListener timeSet = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                tvTime.setText(hourOfDay+":"+minute);
            }
        };
        Calendar calendar = Calendar.getInstance();
        h = calendar.get(Calendar.HOUR);
        m = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,timeSet,h,m,true);
        timePickerDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == TAKE_PICTURE_REQUEST && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageList.add(imageBitmap);
            adapter.notifyDataSetChanged();
        }
        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
                Uri image =  data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),image);
                    imageList.add(bitmap);
                    adapter.notifyDataSetChanged();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
    private void scheduleNotification(Notification notification, long time) {

        Intent notificationIntent = new Intent(this, PushNotification.class);
        notificationIntent.putExtra(PushNotification.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(PushNotification.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime()+5000, pendingIntent);
    }
    private Notification getNotification(String content) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Scheduled Notification");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_color_lens_24dp);
        return builder.build();
    }
}
