package com.example.notes.model;

public class Note {
   String noteTitle, note;
   public Note (){}

    public Note(String noteTitle, String note) {
        this.noteTitle = noteTitle;
        this.note = note;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
