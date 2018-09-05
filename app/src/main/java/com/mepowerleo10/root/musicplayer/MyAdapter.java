package com.mepowerleo10.root.musicplayer;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private ArrayList<Song> list;
    MediaMetadataRetriever mediaInfo;
    Context context;
    Activity activity;
    ContentResolver contentResolver;
    Cursor cursor, genCursor;

    public MyAdapter(Context context, Activity activity, ArrayList<Song> list, ContentResolver contentResolver) {
        this.context = context;
        this.list = list;
        this.activity = activity;
        this.contentResolver = contentResolver;
        if(this.activity != null)
            Log.d("MyAdapter: ", "Activity Received");
        else
            Log.d("MyAdapter: ", "Activity not Received");
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        public final View view;
        public final TextView artist, genre, title;
        public final CircleImageView album_art;
        public final RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            artist = view.findViewById(R.id.textView_artist);
            genre = view.findViewById(R.id.textView_genre);
            title = view.findViewById(R.id.textView_title);
            album_art = view.findViewById(R.id.album_art);
            parentLayout = view.findViewById(R.id.parent_layout);
            title.setSelected(true);
            artist.setSelected(true);
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_item, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.title.setText(list.get(position).getTitle());
        holder.artist.setText(list.get(position).getArtist());
        //holder.genre.setText(list.get(position).getGenre());

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SongHomeActivity.class);
                intent.putExtra("position", position);
                activity.startActivityForResult(intent, 1);
            }
        });


    }

    @Override
    public int getItemCount() {
        if(list != null)
            return list.size();
        else
            return 0;
    }
    


}
