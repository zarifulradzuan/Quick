package com.example.quick.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.quick.R;
import com.example.quick.model.Comment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private CommentSizeInterface commentSizeInterface;
    private ArrayList<Comment> comments = new ArrayList<>();

    public CommentAdapter(CommentSizeInterface commentSizeListener) {
        this.commentSizeInterface = commentSizeListener;
    }

    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_card, parent, false);
        return new CommentAdapter.ViewHolder(itemView);
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
        this.commentSizeInterface.commentSizeListener(getItemCount());

    }

    @Override
    public void onBindViewHolder(CommentAdapter.ViewHolder holder, int position) {
        holder.message.setText(":  " + comments.get(position).getMessage());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, MMM d\nh:mm a");
        LocalDateTime localDateTime = LocalDateTime.parse(comments.get(position).getDateTime());
        holder.dateTime.setText(localDateTime.format(formatter));

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public interface CommentSizeInterface {
        void commentSizeListener(int size);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView dateTime;
        public TextView message;

        public ViewHolder(View itemView) {
            super(itemView);
            dateTime = itemView.findViewById(R.id.dateTimeComment);
            message = itemView.findViewById(R.id.messageComment);
        }
    }
}
