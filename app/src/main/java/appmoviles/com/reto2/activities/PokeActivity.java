package appmoviles.com.reto2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import appmoviles.com.reto2.R;
import appmoviles.com.reto2.model.User;
import appmoviles.com.reto2.model.pokemon.Pokemon;
import appmoviles.com.reto2.model.pokemon.PokemonType;

public class PokeActivity extends AppCompatActivity implements View.OnClickListener {
    private Button leavePokemonBtn;
    private ImageView pokeImg;
    private TextView pokeNameTxt;
    private TextView pokeDefenseTxt;
    private TextView pokeAttackTxt;
    private TextView pokeSpeedTxt;
    private TextView pokeHpTxt;
    private TextView pokeTypeTxt;

    private User user;
    private Pokemon pokemon;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poke);

        leavePokemonBtn = findViewById(R.id.leavePokemonBtn);
        pokeImg = findViewById(R.id.pokeImg);
        pokeNameTxt = findViewById(R.id.pokeNameTxt);
        pokeDefenseTxt = findViewById(R.id.pokeDefenseTxt);
        pokeAttackTxt = findViewById(R.id.pokeAttackTxt);
        pokeSpeedTxt = findViewById(R.id.pokeSpeedTxt);
        pokeHpTxt = findViewById(R.id.pokeHpTxt);
        pokeTypeTxt = findViewById(R.id.pokeTypeTxt);

        pokemon = (Pokemon) getIntent().getExtras().getSerializable("pokemon");
        user = (User) getIntent().getExtras().getSerializable("user");

        pokeNameTxt.setText(pokemon.getName());
        pokeDefenseTxt.setText(pokemon.getStats().get(2).getBase_stat());
        pokeAttackTxt.setText(pokemon.getStats().get(1).getBase_stat());
        pokeSpeedTxt.setText(pokemon.getStats().get(5).getBase_stat());
        pokeHpTxt.setText(pokemon.getStats().get(0).getBase_stat());
        db = FirebaseFirestore.getInstance();
        String type = "(";
        List<PokemonType> types = pokemon.getTypes();
        boolean pos = false;
        for (PokemonType ty:types) {
            if(!pos){
                type +=ty.getType().getName();
                pos = true;
            }else{
                type +=" "+ty.getType().getName();
            }

        }
        type +=")";
        pokeTypeTxt.setText(type.replace(" ",","));
        leavePokemonBtn.setOnClickListener(this);
        Glide.with(pokeImg).load(pokemon.getSprites().getFront_default()).into(pokeImg);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.leavePokemonBtn:
                db.collection("pokemons").document(user.getId()).collection("catch").document(pokemon.getName()).delete();
                finish();
                break;
        }
    }
}