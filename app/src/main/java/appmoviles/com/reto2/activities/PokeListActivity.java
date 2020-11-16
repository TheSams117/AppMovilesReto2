package appmoviles.com.reto2.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.Gson;

import java.util.Date;

import appmoviles.com.reto2.R;
import appmoviles.com.reto2.comm.HTTPSWebUtilDomi;
import appmoviles.com.reto2.list.adapter.PokemonAdapter;
import appmoviles.com.reto2.model.pokemon.Pokemon;
import appmoviles.com.reto2.model.User;

public class PokeListActivity extends AppCompatActivity implements View.OnClickListener, PokemonAdapter.OnPokemonClickListener {
    private EditText namePokeET;
    private Button catchPokeBtn;
    private EditText searchPokemonET;
    private Button searchPokemonBtn;
    private RecyclerView pokemonList;

    private PokemonAdapter adapter;


    private HTTPSWebUtilDomi webUtilDomi;
    private Gson gson;
    private FirebaseFirestore db;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poke_list);

        gson = new Gson();
        webUtilDomi = new HTTPSWebUtilDomi();
        db = FirebaseFirestore.getInstance();

        namePokeET = findViewById(R.id.namePokeET);
        catchPokeBtn = findViewById(R.id.catchPokeBtn);
        searchPokemonET = findViewById(R.id.searchPokemonET);
        searchPokemonBtn = findViewById(R.id.searchPokemonBtn);
        pokemonList = findViewById(R.id.pokemonList);

        adapter = new PokemonAdapter();
        adapter.setListener(this);
        pokemonList.setAdapter(adapter);
        pokemonList.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        pokemonList.setLayoutManager(manager);

        catchPokeBtn.setOnClickListener(this);
        searchPokemonBtn.setOnClickListener(this);


        user = (User) getIntent().getExtras().getSerializable("user");


    }

    public void loadPokemonList() {
        Query pokeReference = db.collection("pokemons").document(user.getId()).collection("catch").orderBy("timestamp", Query.Direction.DESCENDING);
        if(pokeReference!=null){
            pokeReference.get().addOnCompleteListener(
                    task -> {
                        adapter.clear();
                        for(QueryDocumentSnapshot doc : task.getResult()){
                            Pokemon pokemon = doc.toObject(Pokemon.class);
                            adapter.addPokemon(pokemon);
                        }
                    }
            );
        }

    }

    @Override
    protected void onResume() {
        loadPokemonList();
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.catchPokeBtn:
                new Thread(
                        ()->{
                            String json = webUtilDomi.GETrequest("https://pokeapi.co/api/v2/pokemon/"+namePokeET.getText().toString().toLowerCase().replace(" ","-"));
                            Log.e(">>>",namePokeET.getText()+" Entra");
                            Log.e(">>>",json);

                            if (json!=null && !json.equals("") && !namePokeET.getText().toString().equals("")) {
                                Pokemon pokemon = gson.fromJson(json,Pokemon.class);
                                pokemon.setTimestamp(new Date().getTime());
                                db.collection("pokemons").document(user.getId()).collection("catch").document(pokemon.getName()).set(pokemon);
                                runOnUiThread( ()->{

                                    Toast.makeText(this,"El pokemon "+ namePokeET.getText().toString()+" fue atrapado",Toast.LENGTH_LONG).show();
                                    namePokeET.setText("");
                                    loadPokemonList();
                                });
                            }else{
                                runOnUiThread( ()->{
                                    Toast.makeText(this,"El pokemon "+ namePokeET.getText().toString()+" no existe",Toast.LENGTH_LONG).show();
                                });

                            }
                        }
                ).start();



                break;

            case R.id.searchPokemonBtn:
                if (searchPokemonET.getText().toString().equals("")){
                    loadPokemonList();
                }else{
                    new Thread(
                            ()->{
                                CollectionReference pokeReference = db.collection("pokemons").document(user.getId()).collection("catch");
                                Query query = pokeReference.whereEqualTo("name",searchPokemonET.getText().toString().toLowerCase());
                                query.get().addOnCompleteListener(task -> {
                                    if(task.isSuccessful() && task.getResult().size()>0){
                                        adapter.clear();
                                        for(QueryDocumentSnapshot doc : task.getResult()){
                                            Pokemon pokemon = doc.toObject(Pokemon.class);
                                            adapter.addPokemon(pokemon);
                                            searchPokemonET.setText("");
                                        }
                                    }else {
                                        runOnUiThread(()->{Toast.makeText(this,"El pokemon "+searchPokemonET.getText().toString()+" no ha sido atrapado o no existe",Toast.LENGTH_LONG).show();});
                                    }
                                });
                            }
                    ).start();

                }
                break;
        }
    }

    @Override
    public void onPokemonClick(Pokemon pokemon) {
        Intent intent = new Intent(this,PokeActivity.class);
        intent.putExtra("pokemon",pokemon);
        intent.putExtra("user",user);
        startActivity(intent);
    }
}