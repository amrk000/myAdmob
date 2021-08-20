package amrk000.myadmob;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.google.android.material.tabs.TabLayout;

public class AdUnitsPreview extends Fragment {
    TabLayout tabLayout;
    EditText adUnitID;
    ImageButton loadPreview;
    FrameLayout adPreviewFrameLayout;

    BannersPreview bannersPreview;
    InterstitialPreview interstitialPreview;

    public AdUnitsPreview() {
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
        return inflater.inflate(R.layout.fragment_ad_units_preview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabLayout = view.findViewById(R.id.adunits_tablayout);
        adUnitID = view.findViewById(R.id.adUnitID);
        loadPreview = view.findViewById(R.id.loadPreview);
        adPreviewFrameLayout = view.findViewById(R.id.adsPreviewFrameLayout);

        bannersPreview = new BannersPreview();
        interstitialPreview = new InterstitialPreview(loadPreview);

        getActivity().getSupportFragmentManager().beginTransaction().replace(adPreviewFrameLayout.getId(),bannersPreview).commit();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        adUnitID.setText("");
                        getActivity().getSupportFragmentManager().beginTransaction().replace(adPreviewFrameLayout.getId(),bannersPreview).commit();
                        break;

                    case 1:
                        adUnitID.setText("");
                        getActivity().getSupportFragmentManager().beginTransaction().replace(adPreviewFrameLayout.getId(),interstitialPreview).commit();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        loadPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(tabLayout.getSelectedTabPosition()==0){
                    bannersPreview.changeId(adUnitID.getText().toString());

                    loadPreview.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.loading_rotate));
                    loadPreview.setImageResource(R.drawable.ic_baseline_hourglass_top_24);

                    new Handler().postDelayed(()->{
                        loadPreview.clearAnimation();
                        loadPreview.setImageResource(R.drawable.ic_baseline_done_24);
                    },4000);

                }
                else{
                    interstitialPreview.changeId(adUnitID.getText().toString());

                    loadPreview.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.loading_rotate));
                    loadPreview.setImageResource(R.drawable.ic_baseline_hourglass_top_24);
                }
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        adUnitID.clearFocus();

    }

}