package com.example.firenotes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class NoteAdapter extends FirestoreRecyclerAdapter<Note, NoteAdapter.NoteViewHolder> {

    Context context;

    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Note note) {
        holder.titleText.setText(note.title);
        holder.contentText.setText(note.content);
        holder.timeText.setText(note.time);

        holder.itemView.setOnClickListener((v)->{
            Intent intent = new Intent(context, NoteDetails.class);

            String docId = this.getSnapshots().getSnapshot(position).getId();

            intent.putExtra("title", note.title);
            intent.putExtra("content", note.content);
            intent.putExtra("time", note.time);
            intent.putExtra("docId", docId);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyler_note_item, parent, false);
        return new NoteViewHolder(view);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView titleText, contentText, timeText;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            titleText = itemView.findViewById(R.id.note_title_textview);
            contentText = itemView.findViewById(R.id.note_content_textview);
            timeText = itemView.findViewById(R.id.note_time_textview);

        }
    }

}
