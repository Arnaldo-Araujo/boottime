package br.com.i9android.bootime.util;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Classe DateMaskWatcher - Aplicação de máscara para campo de data em um EditText.
 * <p>
 * Esta classe formata automaticamente a entrada do usuário no formato **DD/MM/AAAA**
 * e valida se a data inserida é válida (considerando anos bissextos).
 * </p>
 *
 * @author Arnaldo Junior
 * @version 1.0
 */
public class DateMaskWatcher implements TextWatcher {

    private boolean isUpdating = false; // Flag para evitar loops de atualização
    private final String mask = "##/##/####"; // Máscara do campo de data
    private final EditText editText; // Campo de texto onde será aplicada a máscara
    private final Context context; // Contexto para exibir mensagens ao usuário
    private EditText editTextDataFinal;
    private boolean isDataInicial = false;

    /**
     * Construtor da classe DateMaskWatcher.
     *
     * @param editText Campo de entrada de texto que receberá a máscara.
     * @param context  Contexto da aplicação para exibir mensagens de erro.
     */
    public DateMaskWatcher(EditText editText, Context context) {
        this.editText = editText;
        this.context = context;
    }
    public DateMaskWatcher(EditText dataInicial, EditText dataFinal, Context context) {
        this.editText = dataInicial;
        this.editTextDataFinal = dataFinal;
        this.context = context;
        this.isDataInicial = true;
    }

    /**
     * Método chamado antes da alteração do texto. Não utilizado nesta implementação.
     *
     * @param s      Texto atual antes da mudança.
     * @param start  Posição inicial.
     * @param count  Quantidade de caracteres a serem modificados.
     * @param after  Quantidade de caracteres após a modificação.
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    /**
     * Método chamado durante a alteração do texto.
     * Aplica a máscara de data e verifica se a data completa é válida.
     *
     * @param s      Texto atualizado.
     * @param start  Posição inicial da alteração.
     * @param before Quantidade de caracteres removidos.
     * @param count  Quantidade de caracteres inseridos.
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (isUpdating) {
            isUpdating = false;
            return;
        }

        // Remove qualquer caractere que não seja número
        String str = s.toString().replaceAll("[^0-9]", "");
        StringBuilder formatted = new StringBuilder();
        int i = 0;

        // Aplica a máscara "##/##/####"
        for (char m : mask.toCharArray()) {
            if (m != '#' && str.length() > i) {
                formatted.append(m);
                continue;
            }
            try {
                formatted.append(str.charAt(i));
            } catch (Exception e) {
                break;
            }
            i++;
        }

        // Define a flag para evitar loops infinitos
        isUpdating = true;
        editText.setText(formatted.toString());
        editText.setSelection(formatted.length());

        // Verifica se a data inserida é válida ao completar 10 caracteres (DD/MM/AAAA)
        if (formatted.length() == 10) {
            if (!isValidDate(formatted.toString())) {
                editText.setError("Data inválida!");
                Toast.makeText(context, "Data inválida!", Toast.LENGTH_SHORT).show();
            }
        }
        if(isDataInicial){
            if (s.length() == 10) { // Formato DD/MM/YYYY (10 caracteres)
                editTextDataFinal.requestFocus(); // Move o foco para o próximo campo
            }
        }
    }

    /**
     * Metodo chamado após a alteração do texto. Não utilizado nesta implementação.
     *
     * @param s Texto atualizado após a mudança.
     */
    @Override
    public void afterTextChanged(Editable s) { }

    /**
     * Verifica se a data informada pelo usuário é válida.
     *
     * @param date String contendo a data no formato "DD/MM/AAAA".
     * @return true se a data for válida, false caso contrário.
     */
    private boolean isValidDate(String date) {
        String[] parts = date.split("/");
        if (parts.length != 3) return false;

        int day, month, year;
        try {
            day = Integer.parseInt(parts[0]);
            month = Integer.parseInt(parts[1]);
            year = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            return false;
        }

        // Verifica se o mês está dentro do intervalo válido (1-12)
        if (month < 1 || month > 12) return false;

        // Array contendo a quantidade de dias de cada mês (exceto anos bissextos)
        int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        // Se for um ano bissexto, fevereiro tem 29 dias
        if (isLeapYear(year)) {
            daysInMonth[1] = 29;
        }

        // Verifica se o dia está dentro do intervalo válido para o mês
        return day >= 1 && day <= daysInMonth[month - 1];
    }

    /**
     * Verifica se um determinado ano é bissexto.
     *
     * @param year Ano a ser verificado.
     * @return true se for um ano bissexto, false caso contrário.
     */
    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }
}
