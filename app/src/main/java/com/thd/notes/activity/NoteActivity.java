package com.thd.notes.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
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
import com.thd.notes.utils.DbBitmapUtility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class NoteActivity extends AppCompatActivity {
    public static final int TAKE_PICTURE_REQUEST = 123;
    public static final int GALLERY_REQUEST = 345;
    public static final String UPDATE = "update";
    public static final String DELETE = "del";
    public static final String ID_DELETE = "delete";
    public static final String POS_UPDATE = "POS";
    private EditText etNoteTitle;
    private EditText etNoteContent;
    private ArrayList<Note> noteList;
    private TextView tvDate;
    private TextView tvTime;
    private TextView tvCreateDate;
    private ImageButton imageButton;
    private GridView gvImages;
    private ImagesAdapter adapter;
    private ArrayList<Bitmap> imageList;
    private int position;
    private Note note;
    private RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        etNoteTitle = (EditText) findViewById(R.id.etNoteTitle);
        etNoteContent = (EditText) findViewById(R.id.etNoteContent);
        relativeLayout = (RelativeLayout) findViewById(R.id.noteBackground);
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvCreateDate = (TextView) findViewById(R.id.tvCreateDate);
        imageButton = (ImageButton) findViewById(R.id.cancelAlarm);
        gvImages = (GridView) findViewById(R.id.gvImages);
        imageList = new ArrayList<>();
        Intent i = getIntent();
        noteList = i.getExtras().getParcelableArrayList(MainActivity.KEY_PASS_NOTE);
        position = i.getExtras().getInt(MainActivity.KEY_PASS_POSITION);
        if(noteList!=null) {
            note =noteList.get(position);
            etNoteTitle.setText( note.getNoteTitle());
            //noinspection ConstantConditions
            getSupportActionBar().setTitle(note.getNoteTitle());
            etNoteContent.setText(note.getNoteContent());
            relativeLayout.setBackgroundColor(Color.parseColor(note.getColor()));
            tvCreateDate.setText(note.getDate()+" "+note.getTime());
            if (note.getAlarm()== Note.ALARM_TRUE) {
                tvTime.setVisibility(View.VISIBLE);
                tvDate.setVisibility(View.VISIBLE);
                imageButton.setVisibility(View.VISIBLE);
                tvTime.setText(note.getTimeAlarm());
                tvDate.setText(note.getDateAlarm());
            }
            for(String name : note.getImageList()) {
                imageList.add(DbBitmapUtility.loadBitmap(this,name));
            }
            adapter = new ImagesAdapter(this,R.layout.item_image,imageList);
            gvImages.setAdapter(adapter);
            adapter.setOnDeleteClick(new ImagesAdapter.OnDeleteClick() {
                @Override
                public void onClick(int position) {
                    imageList.remove(position);
                    adapter.notifyDataSetChanged();
                }
            });
            Log.d("key",note.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail_activity,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionSave :
                Note newNote = noteList.get(position);
                ArrayList<String> name = new ArrayList<>();
                for (Bitmap bitmap : imageList) {
                    String na = String.valueOf(Calendar.getInstance().getTimeInMillis());
                    DbBitmapUtility.saveFile(this,bitmap,na);
                    name.add(na);
                }
                newNote.setImageList(name);
                newNote.setNoteTitle(etNoteTitle.getText().toString());
                newNote.setNoteContent(etNoteContent.getText().toString());
                noteList.set(position,newNote);
                Intent i = new Intent();
                i.putExtra("action",UPDATE);
                i.putExtra(POS_UPDATE,position);
                i.putParcelableArrayListExtra(MainActivity.KEY_PASS_NOTE,noteList);
                setResult(RESULT_OK,i);
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
            case R.id.actionCreateNote :
                Intent createNew = new Intent(this,CreateNoteActivity.class);
                startActivityForResult(createNew,MainActivity.CREATE_NOTE_REQUEST);
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
            case R.id.actionDelete : {
                Intent deleteIntent = new Intent();
                deleteIntent.putParcelableArrayListExtra(MainActivity.KEY_PASS_NOTE,noteList);
                deleteIntent.putExtra("action",DELETE);
                deleteIntent.putExtra(ID_DELETE,noteList.get(position).getId());
                noteList.remove(position);
                setResult(RESULT_OK,deleteIntent);
                finish();
            }
            case R.id.actionShare :
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,etNoteContent.getText().toString());
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent,"Send to"));

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == MainActivity.CREATE_NOTE_REQUEST)
            if (resultCode==RESULT_OK) {
                Note note = data.getParcelableExtra("note");
                noteList.add(0,note);
                Intent i = new Intent();
                i.putParcelableArrayListExtra(MainActivity.KEY_PASS_NOTE,noteList);
                setResult(RESULT_OK,i);
                finish();
            }
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

}
