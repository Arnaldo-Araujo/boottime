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

/**
 * {@code SplashFragment} é um fragmento de introdução do aplicativo BootTime.
 *
 * <p>Este fragmento é utilizado como tela de abertura do aplicativo e exibe
 * um banner de anúncio utilizando o Google AdMob. Ele é normalmente o primeiro fragmento
 * exibido ao iniciar o app, funcionando como uma tela de boas-vindas temporária.</p>
 *
 * <h2>Funcionalidades:</h2>
 * <ul>
 *   <li>Infla o layout {@code fragment_splash.xml};</li>
 *   <li>Inicializa o Mobile Ads SDK com o contexto atual do app;</li>
 *   <li>Carrega um anúncio do tipo banner utilizando o {@link AdView};</li>
 * </ul>
 *
 * <p>Este fragmento é leve e rápido, ideal para ser utilizado antes do carregamento
 * dos dados principais do aplicativo.</p>
 *
 * @author ArnaldoJuniorDev
 * @since 1.0
 */
public class SplashFragment extends Fragment {

    /**
     * Instância da view de banner de anúncio do AdMob.
     */
    private AdView adView;

    /**
     * Método de ciclo de vida do Fragment, responsável por inflar o layout da splash
     * e carregar o anúncio publicitário.
     *
     * @param inflater O LayoutInflater para inflar qualquer view no fragmento.
     * @param container O ViewGroup pai da view retornada.
     * @param savedInstanceState Bundle com o estado salvo, se houver.
     * @return A view raiz inflada do layout {@code fragment_splash.xml}.
     */
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

    /**
     * Cria e carrega uma requisição de anúncio do tipo {@link AdRequest}
     * para o banner presente no layout.
     */
    private void carregarAnuncio() {
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }
}
