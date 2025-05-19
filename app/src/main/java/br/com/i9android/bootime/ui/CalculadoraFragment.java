package br.com.i9android.bootime.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.i9android.bootime.R;
import br.com.i9android.bootime.util.DateMaskWatcher;

/**
 * {@code CalculadoraFragment} é um fragmento que permite ao usuário calcular a diferença entre duas datas.
 * <p>
 * O cálculo apresenta o resultado em anos, meses e dias, considerando a opção de incluir ou não
 * anos bissextos. Além disso, permite compartilhar o aplicativo via WhatsApp e Facebook e exibe
 * anúncios usando o AdMob.
 * </p>
 *
 * <p>Componentes da UI:
 * <ul>
 *   <li>Dois {@link EditText} para datas de início e fim</li>
 *   <li>{@link TextView} para exibição do resultado</li>
 *   <li>{@link SwitchCompat} para considerar anos bissextos</li>
 *   <li>Botões para compartilhar via apps sociais</li>
 *   <li>Anúncio do AdMob</li>
 * </ul>
 * </p>
 *
 * @author Arnaldo Junior Dev
 * @version 1.0
 */
public class CalculadoraFragment extends Fragment {

    private EditText editTextDataInicial, editTextDataFinal;
    private TextView textViewResultado;
    private SwitchCompat switchBissexto;
    private ImageButton btnWhatsapp, btnFacebook;
    private Button buttonCalcular;
    private AdView adView;

    private SimpleDateFormat sdf;

    /**
     * Inicializa os componentes visuais do fragmento e define eventos de clique
     * e máscaras de formatação de data.
     *
     * @param inflater           O LayoutInflater usado para inflar o layout.
     * @param container          O container pai onde o fragmento será inserido.
     * @param savedInstanceState Estado anterior salvo do fragmento.
     * @return A view criada e configurada do fragmento.
     */
    @SuppressLint("SimpleDateFormat")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calculadora, container, false);

        MobileAds.initialize(requireContext(), initializationStatus -> {});

        sdf = new SimpleDateFormat("dd/MM/yyyy");

        editTextDataInicial = view.findViewById(R.id.idDataInicial);
        editTextDataFinal = view.findViewById(R.id.idDataFinal);
        textViewResultado = view.findViewById(R.id.idTextResultado);
        buttonCalcular = view.findViewById(R.id.btnCalcularTempoDeServico);
        switchBissexto = view.findViewById(R.id.idBissextoOuNao);
        btnWhatsapp = view.findViewById(R.id.idimageWhatsapp);
        btnFacebook = view.findViewById(R.id.idimageFacebook);
        adView = view.findViewById(R.id.adViewCalculadora);

        if (adView != null) {
            carregarAnuncio();
        }

        editTextDataInicial.addTextChangedListener(new DateMaskWatcher(editTextDataInicial, editTextDataFinal, requireContext()));
        editTextDataFinal.addTextChangedListener(new DateMaskWatcher(editTextDataFinal, requireContext()));

        buttonCalcular.setOnClickListener(v -> calcularDiferencaDatas());
        btnWhatsapp.setOnClickListener(v -> compartilharDireto("com.whatsapp"));
        btnFacebook.setOnClickListener(v -> compartilharDireto("com.facebook.katana"));

        return view;
    }

    /**
     * Carrega o anúncio do AdMob na view {@code adView}.
     */
    private void carregarAnuncio() {
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    /**
     * Compartilha o link do aplicativo diretamente no aplicativo definido.
     *
     * @param pacote Nome do pacote do app (ex: WhatsApp, Facebook).
     */
    private void compartilharDireto(String pacote) {
        String link = "https://play.google.com/store/apps/details?id=" + requireContext().getPackageName();
        String mensagem = "Olá! Baixe o app BootTime e descubra seu tempo de serviço militar! 🚀📲\n" + link;

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, mensagem);
        intent.setPackage(pacote);

        if (requireContext().getPackageManager().getLaunchIntentForPackage(pacote) != null) {
            startActivity(intent);
        } else {
            startActivity(Intent.createChooser(intent, "Compartilhar via"));
        }
    }

    /**
     * Calcula a diferença entre duas datas inseridas pelo usuário.
     * Considera a opção de adicionar anos bissextos caso o switch esteja ativado.
     * Exibe o resultado no {@link TextView}.
     */
    private void calcularDiferencaDatas() {
        esconderTeclado();
        if (TextUtils.isEmpty(editTextDataInicial.getText()) || TextUtils.isEmpty(editTextDataFinal.getText())) {
            Toast.makeText(getContext(), "Preencha ambas as datas!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Date dataInicio = sdf.parse(editTextDataInicial.getText().toString());
            Date dataFim = sdf.parse(editTextDataFinal.getText().toString());

            if (dataInicio == null || dataFim == null) {
                Toast.makeText(getContext(), "Data inválida!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dataInicio.after(dataFim)) {
                Toast.makeText(getContext(), "A data de início deve ser anterior à data de término", Toast.LENGTH_SHORT).show();
                return;
            }

            Calendar inicio = Calendar.getInstance();
            inicio.setTime(dataInicio);
            Calendar fim = Calendar.getInstance();
            fim.setTime(dataFim);

            int anos = fim.get(Calendar.YEAR) - inicio.get(Calendar.YEAR);
            int meses = fim.get(Calendar.MONTH) - inicio.get(Calendar.MONTH);
            int dias = fim.get(Calendar.DAY_OF_MONTH) - inicio.get(Calendar.DAY_OF_MONTH);

            if (dias < 0) {
                meses--;
                fim.add(Calendar.MONTH, -1);
                dias += fim.getActualMaximum(Calendar.DAY_OF_MONTH);
            }

            if (meses < 0) {
                anos--;
                meses += 12;
            }

            if (switchBissexto.isChecked()) {
                int diasBissextos = contarDiasBissextos(inicio.get(Calendar.YEAR), fim.get(Calendar.YEAR));
                dias += diasBissextos;
            }

            while (dias >= 30) {
                meses++;
                dias -= 30;
            }

            while (meses >= 12) {
                anos++;
                meses -= 12;
            }

            String resultado = "Diferença: " + anos + (anos == 1 ? " ano " : " anos ") +
                    meses + (meses == 1 ? " mês " : " meses ") +
                    dias + (dias == 1 ? " dia." : " dias.");

            textViewResultado.setText(resultado);
            textViewResultado.setVisibility(View.VISIBLE);

        } catch (ParseException e) {
            Toast.makeText(getContext(), "Data inválida! Use o formato correto.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Oculta o teclado do dispositivo, se estiver visível.
     */
    private void esconderTeclado() {
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Conta os anos bissextos entre dois anos.
     *
     * @param anoInicio Ano inicial.
     * @param anoFim    Ano final.
     * @return Quantidade de anos bissextos no intervalo.
     */
    private int contarDiasBissextos(int anoInicio, int anoFim) {
        int count = 0;
        for (int ano = anoInicio; ano <= anoFim; ano++) {
            if (ehAnoBissexto(ano)) {
                if (ano == anoInicio) {
                    Calendar inicio = Calendar.getInstance();
                    inicio.set(Calendar.YEAR, anoInicio);
                    if (inicio.get(Calendar.MONTH) > Calendar.FEBRUARY) {
                        continue;
                    }
                }
                count++;
            }
        }
        return count;
    }

    /**
     * Verifica se um ano é bissexto.
     *
     * @param ano Ano a ser verificado.
     * @return {@code true} se o ano for bissexto, caso contrário {@code false}.
     */
    private boolean ehAnoBissexto(int ano) {
        return (ano % 4 == 0 && ano % 100 != 0) || (ano % 400 == 0);
    }
}
