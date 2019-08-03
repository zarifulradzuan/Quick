package com.example.quick.controller;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.quick.adapter.CommentAdapter;
import com.example.quick.model.Comment;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class CommentController {

    public static void insertComment(Comment comment) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("comments/" + comment.getPlaceId());
        databaseReference = databaseReference.push();
        databaseReference.setValue(comment);
    }

    public static void getComments(final CommentAdapter commentAdapter, String placeId) {
        final ArrayList<Comment> places = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query databaseQuery = database.getReference("comments/" + placeId).limitToLast(5);
        databaseQuery.addChildEventListener(new ChildEventListener() {
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                Comment comment = null;
                try {
                    comment = dataSnapshot.getValue(Comment.class);
                    places.add(0, comment);
                    commentAdapter.setComments(places);
                    commentAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Comment comment = null;
                try {
                    comment = dataSnapshot.getValue(Comment.class);
                    System.out.println("Key: " + dataSnapshot.getKey());
                    places.add(0, comment);
                    commentAdapter.setComments(places);
                    commentAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });
    }


}
