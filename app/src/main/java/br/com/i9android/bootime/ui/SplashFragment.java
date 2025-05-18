package br.com.i9android.bootime.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import br.com.i9android.bootime.R;

public class SplashFragment extends Fragment {
    private AdView adView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash, container, false);
        adView = view.findViewById(R.id.adViewSplash);
        MobileAds.initialize(requireContext(), initializationStatus -> {});
        if (adView != null) {
            carregarAnuncio();
        }

        return view;
    }
    private void carregarAnuncio() {
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }
}
