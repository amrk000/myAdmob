package amrk000.myadmob;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class About extends Fragment {
    TextView appversion;
    Button moreapps;
    ImageView appicon;
    View credit;

    public About() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        appicon = view.findViewById(R.id.appcion);
        appversion=view.findViewById(R.id.appversion);
        moreapps = view.findViewById(R.id.moreapps);
        credit=view.findViewById(R.id.credit);

        credit.setVisibility(View.INVISIBLE);

        appversion.setText("Version "+BuildConfig.VERSION_NAME);

        moreapps.setOnClickListener((View v)->{
            Intent profilelink = new Intent(Intent.ACTION_VIEW);
            profilelink.setData(Uri.parse("https://play.google.com/store/apps/dev?id=5289896800613171020"));
            startActivity(profilelink);
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        new Handler().postDelayed(()->{
            credit.setVisibility(View.VISIBLE);
            credit.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.slide));
        },250);
    }

    @Override
    public void onPause() {
        super.onPause();
        credit.setVisibility(View.INVISIBLE);
    }
}