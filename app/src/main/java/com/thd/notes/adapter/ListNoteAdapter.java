package com.thd.notes.adapter;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thd.notes.R;
import com.thd.notes.model.Note;

import java.util.List;


/**
 * Created by Tran Hai Dang on 10/31/2017.
 * Email : tranhaidang2320@gmail.com
 */

public class ListNoteAdapter extends RecyclerView.Adapter<ListNoteAdapter.NoteViewHolder> {
    private List<Note> noteList;
    private OnItemClickListener onClickListener;
    public interface OnItemClickListener {
        void onClick(View view, int position);
    }
    public ListNoteAdapter(List<Note> noteList) {
        this.noteList = noteList;
    }

    public void setOnClickListener(OnItemClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_note,parent,false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.tvTitle.setText(note.getNoteTitle());
        holder.tvContent.setText(note.getBrief());
        holder.tvDateCreate.setText(note.getDate()+" "+note.getTime());
        holder.noteHolder.setBackgroundColor(Color.parseColor(note.getColor()));
    }

    @Override
    public int getItemCount() {
        return null != noteList ? noteList.size():0;
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvContent;
        TextView tvDateCreate;
        CardView noteHolder;
        NoteViewHolder(final View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvNoteTitle);
            tvContent = (TextView) itemView.findViewById(R.id.tvContentBrief);
            tvDateCreate = (TextView) itemView.findViewById(R.id.tvCreateDate);
            noteHolder = (CardView) itemView.findViewById(R.id.noteHolder);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onClickListener!=null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            onClickListener.onClick(itemView,position);
                        }
                    }
                }
            });
        }
    }
}
