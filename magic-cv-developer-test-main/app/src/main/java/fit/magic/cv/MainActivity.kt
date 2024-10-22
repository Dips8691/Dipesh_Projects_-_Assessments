package fit.magic.cv

import android.os.Bundle
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView

class MainActivity : YouTubeBaseActivity() {

    // Video ID of the YouTube video you want to play
    private val VIDEO_ID = "tTej-ax9XiA"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val youtubePlayerView: YouTubePlayerView = findViewById(R.id.youtubePlayerView)

        youtubePlayerView.initialize(getString(R.string.YOUTUBE_API_KEY), object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider?,
                youTubePlayer: YouTubePlayer?,
                wasRestored: Boolean
            ) {
                if (!wasRestored) {
                    // Load the video
                    youTubePlayer?.loadVideo(VIDEO_ID)
                }
            }

            override fun onInitializationFailure(
                provider: YouTubePlayer.Provider?,
                youTubeInitializationResult: YouTubeInitializationResult?
            ) {
                // Handle failure
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
