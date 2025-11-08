package com.jkcq.homebike.fragment.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.jkcq.homebike.fragment.OneFragment;
import com.jkcq.homebike.fragment.ThreeFragment;
import com.jkcq.homebike.fragment.TwoFragment;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class fragmentAdapter extends FragmentStateAdapter {
    private ArrayList<Fragment> mFragmentList;

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return 3;
    }

    public fragmentAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        this.mFragmentList = new ArrayList<>();
    }

    @Override // androidx.viewpager2.adapter.FragmentStateAdapter
    public Fragment createFragment(int i) {
        if (i == 0) {
            return OneFragment.newInstance("", "");
        }
        if (i == 1) {
            return TwoFragment.newInstance("", "");
        }
        if (i != 2) {
            return null;
        }
        return ThreeFragment.newInstance("", "");
    }

    public ArrayList<Fragment> getFragmentList() {
        return this.mFragmentList;
    }
}
