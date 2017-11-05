package com.thd.notes.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.stetho.Stetho;
import com.thd.notes.R;
import com.thd.notes.adapter.ListNoteAdapter;
import com.thd.notes.db.DatabaseManager;
import com.thd.notes.model.Note;

import java.security.PublicKey;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String KEY_PASS_NOTE = "KEY_PASS_NOTE";
    public static final String KEY_PASS_POSITION = "KEY_PASS_POSITION";
    public static final int CREATE_NOTE_REQUEST = 1;
    private static final int UPDATE_NOTE_REQUEST = 2;
    private TextView tvEmpty;
    private ListNoteAdapter adapter;
    private ArrayList<Note> noteList;
    private RecyclerView rvListNote;
    private DatabaseManager databaseManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bindView();
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());
        databaseManager = new DatabaseManager(this);
        noteList = new ArrayList<>();
        noteList.addAll(databaseManager.getAllNote());
        adapter = new ListNoteAdapter(noteList);
        rvListNote.setLayoutManager(new GridLayoutManager(this,2));
        rvListNote.setItemAnimator(new DefaultItemAnimator());
        rvListNote.setAdapter(adapter);
        if(adapter.getItemCount()!=0) tvEmpty.setVisibility(View.INVISIBLE);
        // item on click
        adapter.setOnClickListener(new ListNoteAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent i = new Intent(MainActivity.this,NoteActivity.class);
                i.putParcelableArrayListExtra(KEY_PASS_NOTE,databaseManager.getAllNote());
                i.putExtra(KEY_PASS_POSITION,position);
                startActivityForResult(i,UPDATE_NOTE_REQUEST);
            }
        });

    }
    private void bindView() {
        rvListNote = (RecyclerView) findViewById(R.id.rvListNote);
        tvEmpty = (TextView) findViewById(R.id.tvEmpty);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.createNote :
                Intent i = new Intent(MainActivity.this,CreateNoteActivity.class);
                if(noteList.size()==0) i.putExtra("id",1);
                else i.putExtra("id",noteList.get(0).getId());
                startActivityForResult(i,CREATE_NOTE_REQUEST);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CREATE_NOTE_REQUEST) {
            if(resultCode == RESULT_OK) {
                Note note = data.getParcelableExtra("note");
                noteList.add(0,note);
                adapter.notifyItemInserted(0);
                rvListNote.smoothScrollToPosition(0);
                Log.d("OKE",note.toString());
                databaseManager.insert(note);
            }
        }
        if (requestCode == UPDATE_NOTE_REQUEST ) {
            if(resultCode == RESULT_OK) {
                ArrayList<Note> temp = data.getParcelableArrayListExtra(KEY_PASS_NOTE);
                if(data.getStringExtra("action").equals(NoteActivity.DELETE)) {
                    int id = data.getIntExtra(NoteActivity.ID_DELETE,-1);
                    Log.d("TEST","del");
                    databaseManager.delete(id);
                }
                if (data.getStringExtra("action").equals(NoteActivity.UPDATE)) {
                    int pos = data.getIntExtra(NoteActivity.POS_UPDATE,-1);
                    Log.d("TEST","upadte " + pos);
                    databaseManager.update(temp.get(pos));
                }
                if(temp!=null) {
                    noteList.clear();
                    noteList.addAll(temp);
                    adapter.notifyDataSetChanged();
                }
            }
        }
        if(adapter.getItemCount()!=0) tvEmpty.setVisibility(View.INVISIBLE);
    }
}
