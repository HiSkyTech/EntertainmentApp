package com.urdubolo.com.pk.Ui;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.urdubolo.com.pk.Model.TrackSelectionDialog;
import com.urdubolo.com.pk.R;

public class ActivtyPlayer extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private DefaultTrackSelector trackSelector;
    private SimpleExoPlayer simpleExoPlayer;
    private PlayerView playerView;
    private TextView qualityTxt;
    private final String[] speedOptions = {"0.25x", "0.5x", "Normal", "1.5x", "2x"};
    private  String videoUrl = "";
    private long currentPosition = 0; // To store current playback position
    private Handler handler;
    private Runnable updatePositionRunnable;
    private boolean isShowingTrackSelectionDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        handler = new Handler(Looper.getMainLooper());
        Intent intent = getIntent();

        videoUrl = intent.getStringExtra("videourl");
        setupUIControls();
        setupExoPlayer();

        // Restore playback position if available
        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getLong("currentPosition", 0);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("currentPosition", currentPosition);
    }

    @Override
    protected void onPause() {
        super.onPause();
        pausePlayer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumePlayer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    private void setupUIControls() {
        playerView = findViewById(R.id.exoPlayerView);
        qualityTxt = findViewById(R.id.qualityTxt); // Initialize qualityTxt

        ImageView forwardBtn = playerView.findViewById(R.id.fwd);
        ImageView rewindBtn = playerView.findViewById(R.id.rew);
        ImageView speedBtn = playerView.findViewById(R.id.exo_playback_speed);
        ImageView fullscreenBtn = playerView.findViewById(R.id.fullscreen);
        ImageView backBtn = playerView.findViewById(R.id.backExo);
        ImageView quality = playerView.findViewById(R.id.exo_track_selection_view);

        backBtn.setOnClickListener(view -> finish());

        speedBtn.setOnClickListener(v -> showSpeedDialog());

        forwardBtn.setOnClickListener(v -> {
            if (simpleExoPlayer != null) {
                simpleExoPlayer.seekTo(simpleExoPlayer.getCurrentPosition() + 10000);
            }
        });

        rewindBtn.setOnClickListener(v -> {
            if (simpleExoPlayer != null) {
                simpleExoPlayer.seekTo(Math.max(simpleExoPlayer.getCurrentPosition() - 10000, 0));
            }
        });

        quality.setOnClickListener(view -> {
            Log.d(TAG, "Quality button clicked");
            if (!isShowingTrackSelectionDialog && TrackSelectionDialog.willHaveContent(trackSelector)) {
                Log.d(TAG, "Showing TrackSelectionDialog");
                isShowingTrackSelectionDialog = true;
                TrackSelectionDialog trackSelectionDialog = TrackSelectionDialog.createForTrackSelector(trackSelector,
                        dismissDialog -> isShowingTrackSelectionDialog = false);
                trackSelectionDialog.show(getSupportFragmentManager(), null);
            } else {
                Log.d(TAG, "TrackSelectionDialog not shown");
            }
        });

        fullscreenBtn.setOnClickListener(view -> toggleFullscreen());

        playerView.findViewById(R.id.exo_play).setOnClickListener(v -> {
            if (simpleExoPlayer != null) {
                simpleExoPlayer.play();
            }
        });

        playerView.findViewById(R.id.exo_pause).setOnClickListener(v -> {
            if (simpleExoPlayer != null && simpleExoPlayer.isPlaying()) {
                simpleExoPlayer.pause();
                currentPosition = simpleExoPlayer.getCurrentPosition(); // Save current position when paused
                cancelUpdatePositionRunnable(); // Cancel any ongoing position update
            }
        });
    }

    private void setupExoPlayer() {
        trackSelector = new DefaultTrackSelector(this);
        DefaultTrackSelector.Parameters parameters = trackSelector.buildUponParameters().setForceLowestBitrate(true).build();
        trackSelector.setParameters(parameters);

        // Initialize ExoPlayer
        simpleExoPlayer = new SimpleExoPlayer.Builder(this)
                .setTrackSelector(trackSelector)
                .setLoadControl(new DefaultLoadControl())
                .build();

        playerView.setPlayer(simpleExoPlayer);
        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);

        MediaItem mediaItem = MediaItem.fromUri("https://hiskytechs.com/video_adminpenal/"+videoUrl);
        simpleExoPlayer.setMediaItem(mediaItem);

        simpleExoPlayer.prepare();
        simpleExoPlayer.seekTo(currentPosition); // Seek to saved position
        simpleExoPlayer.play();

        // Start updating position while playing
        updatePositionRunnable = new Runnable() {
            @Override
            public void run() {
                if (simpleExoPlayer != null && simpleExoPlayer.isPlaying()) {
                    currentPosition = simpleExoPlayer.getCurrentPosition();
                    handler.postDelayed(this, 1000); // Update position every second
                }
            }
        };
        handler.postDelayed(updatePositionRunnable, 1000); // Start position update runnable

        // Error handling for ExoPlayer
        simpleExoPlayer.addListener(new SimpleExoPlayer.Listener() {
            @Override
            public void onPlaybackStateChanged(int state) {
                // Handle state changes if needed
            }

            public void onPlayerError(ExoPlaybackException error) {
                // Log error or show error message
                Toast.makeText(ActivtyPlayer.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void releasePlayer() {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
        cancelUpdatePositionRunnable();
    }

    private void cancelUpdatePositionRunnable() {
        if (handler != null && updatePositionRunnable != null) {
            handler.removeCallbacks(updatePositionRunnable);
        }
    }

    private void showSpeedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivtyPlayer.this);
        builder.setTitle("Set Speed");
        builder.setItems(speedOptions, (dialog, which) -> {
            PlaybackParameters params;
            switch (which) {
                case 0:
                    qualityTxt.setText("0.25X");
                    params = new PlaybackParameters(0.25f);
                    break;
                case 1:
                    qualityTxt.setText("0.5X");
                    params = new PlaybackParameters(0.5f);
                    break;
                case 2:
                    qualityTxt.setText("1.0X");
                    params = new PlaybackParameters(1f);
                    break;
                case 3:
                    qualityTxt.setText("1.5X");
                    params = new PlaybackParameters(1.5f);
                    break;
                case 4:
                    qualityTxt.setText("2.0X");
                    params = new PlaybackParameters(2f);
                    break;
                default:
                    return;
            }
            if (simpleExoPlayer != null) {
                simpleExoPlayer.setPlaybackParameters(params);
            }
        });
        builder.show();
    }

    private void toggleFullscreen() {
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    private void pausePlayer() {
        if (simpleExoPlayer != null && simpleExoPlayer.isPlaying()) {
            simpleExoPlayer.pause();
            currentPosition = simpleExoPlayer.getCurrentPosition(); // Save current position when paused
            cancelUpdatePositionRunnable(); // Cancel any ongoing position update
        }
    }

    private void resumePlayer() {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.seekTo(currentPosition); // Restore saved position
            simpleExoPlayer.play();
            handler.postDelayed(updatePositionRunnable, 1000); // Resume position update runnable
        }
    }
}


