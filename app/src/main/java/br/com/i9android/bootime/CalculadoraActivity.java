package br.com.i9android.bootime;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.appupdate.AppUpdateOptions;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.i9android.bootime.util.DateMaskWatcher;

/**
 * Classe Calculadora - Responsável pelo cálculo da diferença entre duas datas.
 * <p>
 * Esta atividade recebe duas datas de entrada e calcula a diferença entre elas
 * em anos, meses e dias. Também permite ativar ou desativar a contagem de anos bissextos.
 * </p>
 *
 * @author Arnaldo Junior Dev
 * @version 1.0
 */
public class CalculadoraActivity extends AppCompatActivity {

    // Elementos da interface do usuário
    private EditText editTextDataInicial, editTextDataFinal;
    private TextView textViewResultado;
    private SwitchCompat switchBissexto;
    protected ImageButton btnWhatsapp;
    protected ImageButton btnFacebook;

    // Formato de data utilizado para entrada e saída

    protected SimpleDateFormat sdf;
    protected Button buttonCalcular;
    private AppUpdateManager appUpdateManager;
    protected AdView adView;


    /**
     * Metodo chamado na criação da atividade.
     * Inicializa os componentes da interface e configura os eventos de clique e máscara de entrada.
     *
     * @param savedInstanceState Estado salvo da atividade anterior (caso exista).
     */
    @SuppressLint({"SimpleDateFormat", "SourceLockedOrientationActivity"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculadora);
        MobileAds.initialize(this, initializationStatus -> {
        });
        adView = findViewById(R.id.adView);
        if (adView != null) {
            carregarAnuncio();
        } else {
            Log.e("AdView", "Erro:adView não foi encontrado no layout");
        }
        verificarAtualizacao();
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        // Inicialização dos componentes da interface
        editTextDataInicial = findViewById(R.id.idDataInicial);
        editTextDataFinal = findViewById(R.id.idDataFinal);
        textViewResultado = findViewById(R.id.idTextResultado);
        buttonCalcular = findViewById(R.id.btnCalcularTempoDeServico);
        switchBissexto = findViewById(R.id.idBissextoOuNao);
        btnWhatsapp = findViewById(R.id.idimageWhatsapp);
        btnFacebook = findViewById(R.id.idimageFacebook);
        // Aplicação da máscara de entrada para datas
        editTextDataInicial.addTextChangedListener(new DateMaskWatcher(editTextDataInicial, editTextDataFinal, this));
        editTextDataFinal.addTextChangedListener(new DateMaskWatcher(editTextDataFinal, this));

        // Configuração do evento de clique no botão de cálculo
        buttonCalcular.setOnClickListener(view -> calcularDiferencaDatas());
        btnWhatsapp.setOnClickListener(view -> compartilharDireto("com.whatsapp"));
        btnFacebook.setOnClickListener(view -> compartilharDireto("com.facebook.katana"));
    }

    private void carregarAnuncio() {
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private void verificarAtualizacao() {
        appUpdateManager = AppUpdateManagerFactory.create(this);

        // Obtém informações sobre a atualização disponível
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                try {
                    // Inicia a atualização usando o novo metodo
                    appUpdateManager.startUpdateFlow(appUpdateInfo, this, AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build());
                } catch (Exception e) {
                    Log.e("AtualizacaoApp", "Erro ao iniciar atualização", e); // ✅ Melhor log
                    Toast.makeText(this, "Erro ao iniciar atualização.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void compartilharDireto(String pacote) {
        String appPackageName = getPackageName();
        String linkPlayStore = "https://play.google.com/store/apps/details?id=" + appPackageName;
        String mensagem = "Olá! Baixe o app BootTime e descubra seu tempo de serviço militar! 🚀📲\n" + linkPlayStore;

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, mensagem);
        intent.setPackage(pacote); // Define o app de destino (WhatsApp ou Facebook)

        // Verifica se o app de destino está instalado
        if (getPackageManager().getLaunchIntentForPackage(pacote) != null) {
            startActivity(intent);
        } else {
            // Caso o app não esteja instalado, exibe opções de compartilhamento
            startActivity(Intent.createChooser(intent, "Compartilhar via"));
        }
    }


    /**
     * Metodo responsável por calcular a diferença entre as duas datas inseridas.
     * Exibe a diferença em anos, meses e dias, considerando ou não anos bissextos.
     */
    private void calcularDiferencaDatas() {
        esconderTeclado();
        // Validação de preenchimento dos campos
        if (TextUtils.isEmpty(editTextDataInicial.getText()) || TextUtils.isEmpty(editTextDataFinal.getText())) {
            Toast.makeText(this, "Preencha ambas as datas!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Conversão das strings de entrada para objetos Date
            Date dataInicio = sdf.parse(editTextDataInicial.getText().toString());
            Date dataFim = sdf.parse(editTextDataFinal.getText().toString());

            // Verifica se a conversão foi bem-sucedida
            if (dataInicio == null || dataFim == null) {
                Toast.makeText(this, "Data inválida! Verifique o formato correto (DD/MM/YYYY).", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validação para garantir que a data inicial seja anterior à data final
            if (dataInicio.after(dataFim)) {
                Toast.makeText(this, "A data de início deve ser anterior à data de término", Toast.LENGTH_SHORT).show();
                return;
            }

            // Conversão das datas para Calendar para manipulação
            Calendar inicio = Calendar.getInstance();
            inicio.setTime(dataInicio);
            Calendar fim = Calendar.getInstance();
            fim.setTime(dataFim);

            // Cálculo da diferença inicial entre os anos, meses e dias
            int anos = fim.get(Calendar.YEAR) - inicio.get(Calendar.YEAR);
            int meses = fim.get(Calendar.MONTH) - inicio.get(Calendar.MONTH);
            int dias = fim.get(Calendar.DAY_OF_MONTH) - inicio.get(Calendar.DAY_OF_MONTH);

            // Ajuste caso os dias sejam negativos
            if (dias < 0) {
                meses--;
                fim.add(Calendar.MONTH, -1);
                dias += fim.getActualMaximum(Calendar.DAY_OF_MONTH);
            }

            // Ajuste caso os meses sejam negativos
            if (meses < 0) {
                anos--;
                meses += 12;
            }

            // Contagem de anos bissextos se a opção estiver ativada
            if (switchBissexto.isChecked()) {
                // Variável para armazenar a contagem de dias bissextos
                int diasBissextos = contarDiasBissextos(inicio.get(Calendar.YEAR), fim.get(Calendar.YEAR));
                dias += diasBissextos; // Soma os dias bissextos antes da conversão para meses
            }

            // Conversão de dias excedentes para meses
            while (dias >= 30) {
                meses++;
                dias -= 30;
            }

            // Conversão de meses excedentes para anos
            while (meses >= 12) {
                anos++;
                meses -= 12;
            }

            // Exibição do resultado formatado
            String resultado = "Diferença: " + anos + (anos <= 1 ? " ano " : " anos ") +
                    meses + (meses <= 1 ? " mês " : " meses ") +
                    dias + (dias <= 1 ? " dia.\n" : " dias.\n");

            textViewResultado.setText(resultado);
            textViewResultado.setVisibility(View.VISIBLE);

        } catch (ParseException e) {
            // Tratamento de erro para formato de data inválido
            Toast.makeText(this, "Data inválida! Use o formato correto.", Toast.LENGTH_SHORT).show();
        }
    }

    private void esconderTeclado() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Conta quantos anos bissextos existem no intervalo entre os anos inicial e final.
     * <p>
     * Se o intervalo começar após fevereiro em um ano bissexto, esse ano não será contado.
     * </p>
     *
     * @param anoInicio Ano inicial do intervalo
     * @param anoFim    Ano final do intervalo
     * @return Número de anos bissextos no período
     */
    private int contarDiasBissextos(int anoInicio, int anoFim) {
        int count = 0;
        for (int ano = anoInicio; ano <= anoFim; ano++) {
            if (ehAnoBissexto(ano)) {
                // Se for o primeiro ano do período, verifica se começou antes de março
                if (ano == anoInicio) {
                    Calendar inicio = Calendar.getInstance();
                    inicio.set(Calendar.YEAR, anoInicio);
                    if (inicio.get(Calendar.MONTH) > Calendar.FEBRUARY) {
                        continue; // Se o período começou depois de fevereiro, não conta esse ano bissexto
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
     * @param ano Ano a ser verificado
     * @return true se for bissexto, false caso contrário
     */
    private boolean ehAnoBissexto(int ano) {
        return (ano % 4 == 0 && ano % 100 != 0) || (ano % 400 == 0);
    }
}
