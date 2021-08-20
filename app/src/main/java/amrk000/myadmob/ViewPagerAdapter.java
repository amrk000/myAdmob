package amrk000.myadmob;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private String[] titles = new String[]{"Home","My Apps","Reports","Blocking Control","Campaigns","Policy Center","Payments","Ad Units Preview","Definitions","about"}; //tab titles

    private Fragment[] fragments = {
            new BrowserFragment("https://apps.admob.com/v2/home",false),
            new BrowserFragment("https://apps.admob.com/v2/apps/list",true),
            new BrowserFragment("https://apps.admob.com/v2/reports/library",false),
            new BrowserFragment("https://apps.admob.com/v2/pubcontrols",false),
            new BrowserFragment("https://apps.admob.com/v2/campaigns/list",true),
            new BrowserFragment("https://apps.admob.com/v2/policycenter",true),
            new BrowserFragment("https://apps.admob.com/v2/payments/overview",true),
            new AdUnitsPreview(),
            new Definitions(),
            new About()
    };

    private boolean singlePage=false;
    private String singlePageTitle;

    private int currentPosition=0;

    public ViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Override
    public Fragment getItem(int position) {
        currentPosition=position;
        return fragments[position];
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (singlePage) return singlePageTitle;
        return titles[position]; //return title
    }

    @Override
    public int getCount() {
        if (singlePage) return 1; //return one page when login page load
        return titles.length; //number of titles to show
    }

    public void setOutOfAdmob(String title){
        singlePage=true;
        singlePageTitle=title;
        notifyDataSetChanged();
    }

    public void setInAdmob(){
        singlePage=false;
        notifyDataSetChanged();
    }

}
