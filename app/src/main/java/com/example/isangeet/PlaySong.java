package com.example.isangeet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlaySong extends AppCompatActivity {
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaplayer.stop();
        mediaplayer.release();
    }

    ArrayList<File> songs;
    MediaPlayer mediaplayer;
    ImageView play,previous,next;
    TextView textView;
    String textContent;
    int position;
    Thread updateSeek;
    SeekBar seekbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        play=findViewById(R.id.play);
        previous=findViewById(R.id.previous);
        next=findViewById(R.id.next);
        textView=findViewById(R.id.textView);
        seekbar=findViewById(R.id.seekBar);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        songs = (ArrayList) bundle.getParcelableArrayList("songList");
        textContent=intent.getStringExtra("currentSong");
        textView.setText(textContent);
        position=intent.getIntExtra("position",0);
        Uri uri= Uri.parse(songs.get(position).toString());
        mediaplayer=MediaPlayer.create(this,uri);
        mediaplayer.start();
        play.setImageResource(R.drawable.pause);
        seekbar.setMax(mediaplayer.getDuration());
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaplayer.seekTo(seekBar.getProgress());
            }
        });
        updateSeek=new Thread(){
            @Override
            public void run() {
                int currentPosition=0;
                try{
                    while(currentPosition<mediaplayer.getDuration())
                    {
                        currentPosition=mediaplayer.getCurrentPosition();
                        seekbar.setProgress(currentPosition);
                        sleep(800);
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }

            }
        };
        updateSeek.start();
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaplayer.isPlaying()){
                    play.setImageResource(R.drawable.play);
                    mediaplayer.pause();
                }
                else{
                    play.setImageResource(R.drawable.pause);
                    mediaplayer.start();
                }
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaplayer.stop();
                mediaplayer.release();
                if(position!=0)
                    position=position-1;
                else
                    position=songs.size()-1;
                Uri uri= Uri.parse(songs.get(position).toString());
                mediaplayer=MediaPlayer.create(PlaySong.this,uri);
                mediaplayer.start();
                play.setImageResource(R.drawable.pause);
                seekbar.setMax(mediaplayer.getDuration());
                textContent=songs.get(position).getName().toString();
                textView.setText(textContent);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaplayer.stop();
                mediaplayer.release();
                if(position!=songs.size()-1)
                    position=position+1;
                else
                   position=0;
                Uri uri= Uri.parse(songs.get(position).toString());
                mediaplayer=MediaPlayer.create(PlaySong.this,uri);
                mediaplayer.start();
                play.setImageResource(R.drawable.pause);
                seekbar.setMax(mediaplayer.getDuration());
                textContent=songs.get(position).getName().toString();
                textView.setText(textContent);
            }
        });
    }
}