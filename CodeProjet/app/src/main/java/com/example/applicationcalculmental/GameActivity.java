package com.example.applicationcalculmental;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.applicationcalculmental.BDD.Helper;
import com.example.applicationcalculmental.BDD.Score;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private Long Reponse =0L;
    private Long point = 0L;
    private Long nbVies = 3L;
    private TextView textViewCalcul, textViewResultat, textViewScore, textViewNom;
    private Long BORNE_SUPERIEUR = 9999L;
    private Long BORNE_INFERIEUR = -9999L;
    private int chosenDifficulty = 1;
    private String chosenDifficultyString;
    private String chosenGameName;
    private int chosenNbCalcul;
    private android.support.v7.widget.Toolbar toolbar;
    private boolean negatif = false;
    private Long resultat;
    private int compteur = 0;
    private boolean validiteValider = false;
    private Helper BDD = new Helper(GameActivity.this);
    private ImageView coeur0, coeur1, coeur2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();

        if(intent != null) {
            if(intent.hasExtra("chosenGameName")) chosenGameName = intent.getStringExtra("chosenGameName");
            if(intent.hasExtra("chosenDifficulty")) chosenDifficulty = intent.getIntExtra("chosenDifficulty", 1);
            if(intent.hasExtra("chosenNbCalcul")) chosenNbCalcul = intent.getIntExtra("chosenNbCalcul", 5);
        }

        textViewCalcul = findViewById(R.id.textviewCalcul);
        textViewResultat = findViewById(R.id.textViewResultat);
        textViewScore = findViewById(R.id.textViewScore);
        textViewNom = findViewById(R.id.textViewNomPartie);
        majTextViewNom();
        majTextViewScore();

        resultat = calcul(chosenDifficulty);

        toolbar = findViewById(R.id.toolbarGame);
        setSupportActionBar(toolbar);

        Button bouton1 = findViewById(R.id.button_1);
        bouton1.setOnClickListener(view -> ajouterNombre(1));
        Button bouton2 = findViewById(R.id.button_2);
        bouton2.setOnClickListener(view -> ajouterNombre(2));
        Button bouton3 = findViewById(R.id.button_3);
        bouton3.setOnClickListener(view -> ajouterNombre(3));
        Button bouton4 = findViewById(R.id.button_4);
        bouton4.setOnClickListener(view -> ajouterNombre(4));
        Button bouton5 = findViewById(R.id.button_5);
        bouton5.setOnClickListener(view -> ajouterNombre(5));
        Button bouton6 = findViewById(R.id.button_6);
        bouton6.setOnClickListener(view -> ajouterNombre(6));
        Button bouton7 = findViewById(R.id.button_7);
        bouton7.setOnClickListener(view -> ajouterNombre(7));
        Button bouton8 = findViewById(R.id.button_8);
        bouton8.setOnClickListener(view -> ajouterNombre(8));
        Button bouton9 = findViewById(R.id.button_9);
        bouton9.setOnClickListener(view -> ajouterNombre(9));
        Button bouton0 = findViewById(R.id.button_0);
        bouton0.setOnClickListener(view -> ajouterNombre(0));
        Button boutonMoins = findViewById(R.id.buttonMoins);
        boutonMoins.setOnClickListener(view -> nombreNegatif());
        Button boutonValider = findViewById(R.id.buttonValider);

        coeur0 = (ImageView) findViewById(R.id.imageView0);
        coeur1 = (ImageView) findViewById(R.id.imageView1);
        coeur2 = (ImageView) findViewById(R.id.imageView2);

        boutonValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (compteur <= chosenNbCalcul){
                    if(!validiteValider){
                        verifCalcul();
                        validiteValider = true;
                        compteur++;
                        boutonValider.setText((compteur < chosenNbCalcul && nbVies > 0) ? getString(R.string.next) : getString(R.string.finish));
                    }else if(validiteValider && compteur < chosenNbCalcul && nbVies > 0){
                        videTextViewResultat();
                        textViewResultat.setTextColor(Color.DKGRAY);
                        resultat = calcul(chosenDifficulty);
                        boutonValider.setText(R.string.validBtn);
                        validiteValider = false;
                    }
                    else if(compteur == chosenNbCalcul || nbVies <= 0){
                        switchToEndActivity();
                    }
                }
            }
        });
        Button boutonEffacer = findViewById(R.id.buttonEffacer);
        boutonEffacer.setOnClickListener(view -> videTextViewResultat());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.default_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                openHomeActivity(item);
                return true;
            case R.id.settings:
                openSettingsActivity(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void switchToEndActivity() {
        Intent intent = new Intent(this, EndActivity.class);

        Bundle bundle = new Bundle();

        double score = Double.valueOf(point * 100 )/ Double.valueOf(chosenNbCalcul);

        bundle.putDouble("score", score);

        intent.putExtras(bundle);

        BDD.insertScore(new Score(chosenGameName, score, chosenDifficulty));

        startActivity(intent);
    }

    private void ajouterNombre(Integer valeur){
            if(10*Reponse+valeur > BORNE_SUPERIEUR){
                Toast.makeText(this,getString(R.string.message_valeur_trop_grande),Toast.LENGTH_LONG).show();
            }
            else if(10*Reponse-valeur < BORNE_INFERIEUR){
                Toast.makeText(this,getString(R.string.message_valeur_trop_petite),Toast.LENGTH_LONG).show();
            }
            else if(Reponse < 0) {
                Reponse = 10 * Reponse-valeur;
            }else if (Reponse == 0 && negatif){
                Reponse = 10 * Reponse+valeur;
                Reponse *= (-1);
            }
            else {
                Reponse = 10 * Reponse+valeur;
            }
        majTextViewResultat();
    }

    private void nombreNegatif(){
        negatif = true;
        Reponse *= (-1);
        majTextViewResultat();
    }

    private void majTextViewResultat() {
        String valeurAAfficher = "";
        if(negatif && Reponse == 0){
            valeurAAfficher = "-" + Reponse;
        }else{
            valeurAAfficher = Reponse.toString();
        }
        textViewResultat.setText(valeurAAfficher);
    }

    private void majTextViewScore() {
        String valeurAAfficher = "";

        valeurAAfficher = "Score : " + point;

        textViewScore.setText(valeurAAfficher);

    }

    private void majTextViewNom() {
        String valeurAAfficher = "";

        valeurAAfficher = getString(R.string.gameName) + " " +chosenGameName;

        textViewNom.setText(valeurAAfficher);

    }

    private List<Long> genereNombreAlea(int difficulte){
        List<Long> nombre = new ArrayList<Long>();
        Random random = new Random();
        switch (difficulte){
            case 1:
                nombre.add(0,(long) random.nextInt(10) + 1);
                nombre.add(1,(long) random.nextInt(10) + 1);
                break;

            case 2:
                nombre.add(0,(long) random.nextInt(25) + 1);
                nombre.add(1,(long) random.nextInt(25) + 1);
                break;

            case 3:
                nombre.add(0,(long) random.nextInt(50) + 1);
                nombre.add(1,(long) random.nextInt(50) + 1);
                break;
            default:
                throw new IllegalStateException(getString(R.string.unexpectedValue) + difficulte);
        }
        return nombre;
    }

    private Long calcul(int difficulte)  {
        List<Long> nombre;
        List<String> Elements = new ArrayList<String>();
        int operateur;
        Long resultat = 0L;
        String texte;

        nombre = genereNombreAlea(difficulte);

        for(Long nb : nombre){
            Elements.add(Long.toString(nb));
        }

        Random random = new Random();
        operateur =  random.nextInt(4) + 1;
        switch (operateur){
            case 1:
                resultat = nombre.get(0) + nombre.get(1);
                texte = Elements.get(0) + " + " + Elements.get(1);
                textViewCalcul.setText(texte);
                break;

            case 2:
                resultat = nombre.get(0) - nombre.get(1);
                texte = Elements.get(0) + " - " + Elements.get(1);
                textViewCalcul.setText(texte);
                break;

            case 3:
                resultat = nombre.get(0) * nombre.get(1);
                texte = Elements.get(0) + " x " + Elements.get(1);
                textViewCalcul.setText(texte);
                break;

            case 4:
                if (chosenDifficulty == 1){
                    while(!divisionExceptionFacile(nombre.get(0), nombre.get(1))){
                        nombre = genereNombreAlea(difficulte);
                    }
                }else{
                    while(!divisionExceptionMoyenDifficile(nombre.get(0), nombre.get(1))){
                        nombre = genereNombreAlea(difficulte);
                    }
                }

                Elements.add(0,Long.toString(nombre.get(0)));
                Elements.add(1,Long.toString(nombre.get(1)));
                resultat = nombre.get(0) / nombre.get(1);
                texte = Elements.get(0) + " / " + Elements.get(1);
                textViewCalcul.setText(texte);
                break;
        }
        return resultat;
    }

    private boolean divisionExceptionFacile(long nombre1, long nombre2){
        return ((nombre1 % nombre2) == 0);
    }

    private boolean divisionExceptionMoyenDifficile(long nombre1, long nombre2){
        return ((nombre1 % nombre2) == 0 && (nombre1 / nombre2) != 1);
    }

    private void verifCalcul() {
        String res = Long.toString(resultat);
        String texte = textViewCalcul.getText() + " = " + res;
        textViewCalcul.setText(texte);
        if (resultat.equals(Reponse)){
            point++;
            textViewResultat.setTextColor(Color.GREEN);
            majTextViewScore();
        }
        else{
            nbVies--;
            textViewResultat.setTextColor(Color.RED);

            if (nbVies == 0) {
                coeur0.setImageResource(R.drawable.coeur_vide);
            } else if (nbVies == 1) {
                coeur1.setImageResource(R.drawable.coeur_vide);
            } else if (nbVies == 2) {
                coeur2.setImageResource(R.drawable.coeur_vide);
            }
        }
    }

    private boolean videTextViewResultat() {
        textViewResultat.setText("");
        Reponse = 0L;
        negatif = false;
        return true;
    }

    public void openHomeActivity(MenuItem item) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void openSettingsActivity(MenuItem item) {
        startActivity(new Intent(this, SettingsActivity.class));
    }
}