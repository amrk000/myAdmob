package amrk000.myadmob;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;


public class InterstitialPreview extends Fragment {
    InterstitialAd mInterstitialAd;
    Button launch;
    TextView stats;
    ImageButton loadPreview;

    boolean ready;

    final String defaultID = "ca-app-pub-3940256099942544/1033173712";

    public InterstitialPreview(ImageButton loadPreview) {
        // Required empty public constructor
        this.loadPreview = loadPreview;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_interstitial_preview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        launch = view.findViewById(R.id.launchInterstitial);
        stats = view.findViewById(R.id.interstitialStats);

        InterstitialAd(defaultID);

        launch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mInterstitialAd!=null) mInterstitialAd.show(getActivity());

                else{
                    loadPreview.performClick();
                    launch.setText("Reloading");
                }

            }
        });


    }

    public void InterstitialAd(String id){
        ready = false;
        launch.setEnabled(false);
        stats.setText("Loading Ad");

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(getContext(),id, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
                mInterstitialAd.setImmersiveMode(false);

                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){

                    @Override
                    public void onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent();
                        stats.setText("Not Loaded");
                        launch.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.ic_baseline_autorenew_24),null,null);
                        launch.setText("Load Again");
                        mInterstitialAd = null;

                    }

                });


                launch.setEnabled(true);
                stats.setText("Ad is Ready to Launch");
                launch.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.ic_baseline_zoom_out_map_24),null,null);
                launch.setText("Launch Interstitial");

                loadPreview.clearAnimation();
                loadPreview.setImageResource(R.drawable.ic_baseline_done_24);

            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                mInterstitialAd = null;

                launch.setEnabled(true);
                stats.setText("Not Loaded");
                launch.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.ic_baseline_autorenew_24),null,null);
                launch.setText("Load Again");

                loadPreview.clearAnimation();
                loadPreview.setImageResource(R.drawable.ic_baseline_done_24);

            }

        });

    }

    public void changeId(String adID){

        if(adID.isEmpty()) InterstitialAd(defaultID);
        else InterstitialAd(adID);
    }
}