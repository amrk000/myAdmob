package amrk000.myadmob;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class BannersPreview extends Fragment {
    RecyclerView recyclerView;
    ArrayList<BannerModel> banners;

    final String defaultID = "ca-app-pub-3940256099942544/6300978111";

    public BannersPreview() {
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
        return inflater.inflate(R.layout.fragment_banners_preview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.bannersList);
        banners=new ArrayList<>();

        banners.add(new BannerModel("Banner",320,50));
        banners.add(new BannerModel("Large Banner",320,100));
        banners.add(new BannerModel("Smart Banner",0,90));
        banners.add(new BannerModel("Rectangle Banner",300,250));

        //detect tablet
        if ((getContext().getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE) {
            banners.add(new BannerModel("Full Banner",468,60));
            banners.add(new BannerModel("Leaderboard Banner",728,90));
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

        BannersListAdapter recyclerAdapter = new BannersListAdapter(getContext(),banners,defaultID,false);
        recyclerView.setAdapter(recyclerAdapter);


    }

    public void changeId(String adID){
        BannersListAdapter recyclerAdapter;

        if(adID.isEmpty()) recyclerAdapter = new BannersListAdapter(getContext(),banners,defaultID,false);
        else recyclerAdapter = new BannersListAdapter(getContext(),banners,adID,true);
        recyclerView.setAdapter(recyclerAdapter);
    }
}