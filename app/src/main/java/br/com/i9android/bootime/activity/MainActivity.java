package br.com.i9android.bootime.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import br.com.i9android.bootime.R;

/**
 * Classe principal responsável por inicializar a interface do aplicativo e
 * configurar a navegação entre os fragments utilizando a {@link BottomNavigationView}.
 *
 * <p>Esta atividade representa o ponto de entrada do aplicativo, onde a navegação
 * é controlada pelo componente {@link NavController} e gerenciada através do
 * {@link NavHostFragment} e do menu inferior (Bottom Navigation).</p>
 *
 * <p>Funcionalidades principais:
 * <ul>
 *     <li>Define o layout principal com {@code activity_main.xml};</li>
 *     <li>Obtém o {@code NavHostFragment} responsável por hospedar os fragments de navegação;</li>
 *     <li>Integra o {@link BottomNavigationView} com o {@link NavController}
 *     usando {@link NavigationUI}.</li>
 * </ul>
 * </p>
 *
 * <p>Exemplo de navegação:
 * <ul>
 *     <li>RodadasFragment</li>
 *     <li>ClassificaçãoFragment</li>
 *     <li>ArtilhariaFragment</li>
 * </ul>
 * </p>
 *
 * @author ArnaldoJuniorDev
 * @since 1.0
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Método chamado na criação da atividade.
     *
     * <p>Configura o layout principal, inicializa o {@link BottomNavigationView}
     * e conecta-o ao {@link NavController} responsável pela navegação entre fragments.</p>
     *
     * @param savedInstanceState estado salvo da instância anterior da atividade, se houver
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializa o BottomNavigationView presente no layout
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Obtém o NavHostFragment associado ao container de navegação
        NavHostFragment navHostFragment = (NavHostFragment)
                getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        // Conecta o NavController à BottomNavigationView, se o NavHostFragment for encontrado
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(bottomNavigationView, navController);
        }
    }
}
