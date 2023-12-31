package com.example.vn.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.vn.Activities.BannerActivity;
import com.example.vn.Models.Banner;
import com.example.vn.R;
import com.example.vn.Tools.TOOLS;


public class BannerFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_banner, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView imageView = view.findViewById(R.id.imv_banner);
        Bundle bundle = getArguments();
        assert bundle != null;
        Banner banner = (Banner) bundle.get("banner");
        Glide.with(this).load(TOOLS.doMainDevice + banner.getImage()).into(imageView);
        imageView.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), BannerActivity.class);
            intent.putExtra("banner", banner);
            requireContext().startActivity(intent);
            requireActivity().overridePendingTransition(R.anim.next_enter, R.anim.next_exit);
        });
    }
}