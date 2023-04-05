package com.example.vmusic;

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

public class SongInterface extends AppCompatActivity {
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateseek.interrupt();
    }

    TextView textView;
    ImageView play,previous,next,logo;
    ArrayList<File> songs;
    MediaPlayer mediaPlayer;
    String textContent;
    int position;
    SeekBar seekBar;
    Thread updateseek;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_interface);
        textView = findViewById(R.id.textView);
        play = findViewById(R.id.play);
        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);
        logo = findViewById(R.id.logo);
        seekBar = findViewById(R.id.seekBar);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        songs = (ArrayList) bundle.getParcelableArrayList("songlist");
        textContent = intent.getStringExtra("currentsong");
        textView.setText(textContent);
        textView.setSelected(true);
        position = intent.getIntExtra("position",0);
        Uri uri = Uri.parse(songs.get(position).toString());
        mediaPlayer = MediaPlayer.create(this,uri);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
        updateseek = new Thread(){
            @Override
            public void run() {
                int currentposition = 0;
                try {
                    while(currentposition<mediaPlayer.getDuration()){
                        currentposition = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentposition);
                        sleep(300);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        updateseek.start();

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    play.setImageResource(R.drawable.play);
                    mediaPlayer.pause();
                }
                else{
                    play.setImageResource(R.drawable.pause);
                    mediaPlayer.start();
                }
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateseek.interrupt();
                mediaPlayer.seekTo(0);
                seekBar.setProgress(0);
                mediaPlayer.stop();
                mediaPlayer.release();
                if(position!=0){
                    position-=1;

                }
                else{
                    position=songs.size()-1;
                }
                Uri uri = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(SongInterface.this,uri);
                mediaPlayer.start();
                play.setImageResource(R.drawable.pause);
                seekBar.setMax(mediaPlayer.getDuration());
                updateseek = new Thread(){
                    @Override
                    public void run() {
                        int currentposition = 0;
                        try {
                            while(currentposition<mediaPlayer.getDuration()){
                                currentposition = mediaPlayer.getCurrentPosition();
                                seekBar.setProgress(currentposition);
                                sleep(300);
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                updateseek.start();
                textContent = songs.get(position).getName();
                textView.setText(textContent);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateseek.interrupt();
                mediaPlayer.seekTo(0);
                seekBar.setProgress(0);
                mediaPlayer.stop();
                mediaPlayer.release();
                if(position!= songs.size()-1){
                    position+=1;

                }
                else{
                    position=0;
                }
                Uri uri = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(SongInterface.this,uri);
                mediaPlayer.start();
                play.setImageResource(R.drawable.pause);
                seekBar.setMax(mediaPlayer.getDuration());
                updateseek = new Thread(){
                    @Override
                    public void run() {
                        int currentposition = 0;
                        try {
                            while(currentposition<mediaPlayer.getDuration()){
                                currentposition = mediaPlayer.getCurrentPosition();
                                seekBar.setProgress(currentposition);
                                sleep(300);
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                updateseek.start();
                textContent = songs.get(position).getName();
                textView.setText(textContent);
            }
        });
    }
}