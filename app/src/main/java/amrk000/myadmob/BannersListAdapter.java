package amrk000.myadmob;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;

import java.util.ArrayList;

public class BannersListAdapter extends RecyclerView.Adapter<BannersListAdapter.ViewHolder> {

    ArrayList<BannerModel> Banners;
    Context context;
    String adID;
    boolean selfClickShield;

    public BannersListAdapter(Context context,ArrayList<BannerModel> Banners,String adID,boolean selfClickShield){
        this.Banners=Banners;
        this.context=context;
        this.adID=adID;
        this.selfClickShield=selfClickShield;
    }

    //item view inner class
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView bannerName,bannerWidth,bannerHeight;
        private final FrameLayout bannerFrame;
        private final View selfClickShield;

        public ViewHolder(View view) {
            super(view);

            bannerName = view.findViewById(R.id.bannerName);
            bannerWidth = view.findViewById(R.id.bannerWidth);
            bannerHeight = view.findViewById(R.id.bannerHeight);
            bannerFrame = view.findViewById(R.id.bannerFrame);
            selfClickShield = view.findViewById(R.id.selfClickShield);
        }

        public FrameLayout getBannerFrame() {
            return bannerFrame;
        }

        public TextView getBannerName() {
            return bannerName;
        }

        public TextView getBannerWidth() {
            return bannerWidth;
        }

        public TextView getBannerHeight() {
            return bannerHeight;
        }

        public View getSelfClickShield() {
            return selfClickShield;
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bannerslist_item, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.getBannerName().setText(Banners.get(position).getName());
        holder.getBannerWidth().setText(Banners.get(position).getWidth() + "dp");
        holder.getBannerHeight().setText(Banners.get(position).getHeight() + "dp");

        AdView adView = new AdView(context);

        if(Banners.get(position).getWidth()==320 && Banners.get(position).getHeight()==50) adView.setAdSize(AdSize.BANNER);
        else if(Banners.get(position).getWidth()==320 && Banners.get(position).getHeight()==100) adView.setAdSize(AdSize.LARGE_BANNER);
        else if(Banners.get(position).getWidth()==300 && Banners.get(position).getHeight()==250) adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
        else if(Banners.get(position).getWidth()==468 && Banners.get(position).getHeight()==60) adView.setAdSize(AdSize.FULL_BANNER);
        else if(Banners.get(position).getWidth()==728 && Banners.get(position).getHeight()==90) adView.setAdSize(AdSize.LEADERBOARD);
        else{
            adView.setAdSize(AdSize.SMART_BANNER);
            holder.getBannerWidth().setText("Screen width");
            holder.getBannerHeight().setText("32|50|90 dp");
        }

        adView.setAdUnitId(adID);
        adView.loadAd(new AdRequest.Builder().build());

        float density = context.getResources().getDisplayMetrics().density;
        holder.getBannerFrame().getLayoutParams().width = (int) (Banners.get(position).getWidth()* density);
        holder.getBannerFrame().getLayoutParams().height = (int) (Banners.get(position).getHeight() * density);
        holder.getBannerFrame().requestLayout();

        adView.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();

                holder.getBannerFrame().setBackgroundColor(Color.WHITE);
                holder.getBannerFrame().removeAllViews();
                holder.getBannerFrame().addView(adView);
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);

                holder.getBannerFrame().setBackgroundColor(context.getColor(R.color.colorAccent));
            }
        });

        if(selfClickShield){
            holder.getSelfClickShield().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "⛔ Don't Click Your Ads", Toast.LENGTH_LONG).show();
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return Banners.size();
    }


}
