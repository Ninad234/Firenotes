package com.example.firenotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.Nullable;

import android.content.DialogInterface;
import android.os.Bundle;
import android.content.Intent;
import java.util.*;

import android.text.TextUtils;
import android.widget.Toast;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

import org.w3c.dom.Document;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.List;
import android.util.Log;


public class NoteDetails extends AppCompatActivity {

    EditText titleEditText, contentEditText;
    ImageButton saveBtn, backBtn, delBtn, mic;
    TextView textDateTime, tagsText;
    String title, content, time, docId;
    List<String> tags;
    boolean isEditMode = false;
    String[] KEYWORDS = {
       "meeting", "urgent", "project", "client","homework", "idea", "todo", "work", "assignment"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        titleEditText = findViewById(R.id.notes_title_text);
        contentEditText = findViewById(R.id.notes_content_text);
        saveBtn = findViewById(R.id.save_note_btn);
        backBtn = findViewById(R.id.back_btn);
        delBtn = findViewById(R.id.delete_btn);
        textDateTime = findViewById(R.id.date_time_text);
        mic = findViewById(R.id.imageButton2);
        tagsText = findViewById(R.id.tagsText);

        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        time = getIntent().getStringExtra("time");
        docId = getIntent().getStringExtra("docId");

        tags = (List<String>) getIntent().getSerializableExtra("tags");
        if(tags == null){
            tags = new ArrayList<>(); // Naya note hai toh khali list
        }
        tagsText.setText(TextUtils.join(", ",tags));
        mic.setOnClickListener(v -> {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                    intent.putExtra(
                            RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(
                            RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                    intent.putExtra(
                            RecognizerIntent.EXTRA_PROMPT,"Speak To Text"
                    );

            try {
                startActivityForResult(intent, 100);
            } catch (Exception e) {
               Toast.makeText(this, "Speech to text has been supported sorry", Toast.LENGTH_SHORT).show();
            }
        });

        if (docId != null && !docId.isEmpty()){
            isEditMode = true;
        }

        titleEditText.setText(title);
        contentEditText.setText(content);
        textDateTime.setText(time);
        tagsText.setText(TextUtils.join(", ",tags));

        saveBtn.setEnabled(false);
        saveBtn.setImageAlpha(0x3F);
        delBtn.setVisibility(View.GONE);

        if (isEditMode) {
            saveBtn.setEnabled(true);
            saveBtn.setImageAlpha(0xFF);
            delBtn.setVisibility(View.VISIBLE);
        }

        titleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

                if (!s.toString().equals("") || !s.toString().equals(null)) {
                    saveBtn.setEnabled(true);
                    saveBtn.setImageAlpha(0xFF);
                }
                if (s.toString().equals("") || s.toString().equals(null)) {
                    saveBtn.setEnabled(false);
                    saveBtn.setImageAlpha(0x3F);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        saveBtn.setOnClickListener((v) -> saveNote());

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        delBtn.setOnClickListener((v)-> deleteNoteFromFirebase());

        textDateTime.setText(
                new SimpleDateFormat("EEE, dd MMMM | hh:mm a ", Locale.getDefault()).format(new Date()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data!= null){
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()){
                contentEditText.append(result.get(0));
            }
        }
    }
    void saveNote() {
        String noteTitle = titleEditText.getText().toString();
        String noteContent = contentEditText.getText().toString();
        String noteTime = textDateTime.getText().toString();
        List<String> tags = generateTags(noteContent);

        Note note = new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTime(noteTime);
        note.setTags(tags);


        saveNoteToFirebase(note);
    }

    private List<String> generateTags(String text){
        List<String> tags = new ArrayList<>();
        String lower = text.toLowerCase();

        for (String word:KEYWORDS){
            if (lower.contains(word)){
                tags.add(word);
            }
        }
        return tags;
    }

    void saveNoteToFirebase(Note note) {
        DocumentReference documentReference;

        if (isEditMode){
            documentReference = Utility.getCollectionReferenceForNotes().document(docId);
        }
        else {
            documentReference = Utility.getCollectionReferenceForNotes().document();
        }

        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Utility.showToast(NoteDetails.this, "Note Saved Successfully :D");
                    finish();
                } else {
                    String errorMsg = task.getException() != null ? task.getException().getMessage() : "Unknown Error";
                    Utility.showToast(NoteDetails.this, "Failed to Save the Note :(" + errorMsg);
                    new AlertDialog.Builder(NoteDetails.this)
                            .setTitle("Failed to Save the Note :(")
                            .setMessage("Details:\n\n"+errorMsg) // Isme pura error message bina kate dikhega
                            .setPositiveButton("OK", null)
                            .show();
                    Log.e("APP_DEBUG","Error Details: "+ errorMsg);
                }
            }
        });
    }

    void deleteNoteFromFirebase(){
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForNotes().document(docId);

        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Utility.showToast(NoteDetails.this, "Note Deleted Successfully!");
                    finish();
                } else {
                    Utility.showToast(NoteDetails.this, "Failed to Deleted the Note!");
                }
            }
        });
    }

}