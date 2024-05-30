package com.example.notes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateNoteActivity extends AppCompatActivity {

    Button btnAdd;
    EditText noteTitle, note;
    private FirebaseFirestore mFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_note);


        String id = getIntent().getStringExtra("id_note");

        mFirestore = FirebaseFirestore.getInstance();
        noteTitle = findViewById(R.id.noteTitle);
        note = findViewById(R.id.note);
        btnAdd = findViewById(R.id.btnAdd);

        if(id == null || id == ""){
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String noteT = noteTitle.getText().toString().trim();
                    String noteC = note.getText().toString().trim();

                    if(noteT.isEmpty() && noteC.isEmpty()){
                        Toast.makeText(CreateNoteActivity.this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
                    }else{
                        postNote(noteT, noteC);
                    }
                }
            });
        }else{
            btnAdd.setText("Actualizar nota");
            getNote(id);
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String noteT = noteTitle.getText().toString().trim();
                    String noteC = note.getText().toString().trim();

                    if(noteT.isEmpty() && noteC.isEmpty()){
                        Toast.makeText(CreateNoteActivity.this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
                    }else{
                        updateNote(noteT, noteC, id);
                    }

                }
            });
        }

    }

    private void updateNote(String noteT, String noteC, String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("noteTitle", noteT);
        map.put("note", noteC);

        mFirestore.collection("notes").document(id).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                startActivity(new Intent(CreateNoteActivity.this, MainActivity.class));
                Toast.makeText(CreateNoteActivity.this, "Nota editada con exito", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateNoteActivity.this, "Error al editar la nota", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void postNote(String noteT, String noteC) {
        Map<String, Object> map = new HashMap<>();
        map.put("noteTitle", noteT);
        map.put("note", noteC);

        mFirestore.collection("notes").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                startActivity(new Intent(CreateNoteActivity.this, MainActivity.class));
                Toast.makeText(CreateNoteActivity.this, "Nota guardada con exito", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateNoteActivity.this, "Error al registrar la nota", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getNote(String id){
        mFirestore.collection("notes").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String noteT = documentSnapshot.getString("noteTitle");
                String noteC = documentSnapshot.getString("note");

                noteTitle.setText(noteT);
                note.setText(noteC);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateNoteActivity.this, "Error al obtener los datos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}