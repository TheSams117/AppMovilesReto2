package appmoviles.com.reto2.list.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import appmoviles.com.reto2.R;
import appmoviles.com.reto2.list.viewmodel.PokemonViewModel;
import appmoviles.com.reto2.model.pokemon.Pokemon;

public class PokemonAdapter extends RecyclerView.Adapter<PokemonViewModel> {
    private ArrayList<Pokemon> pokemons;
    private OnPokemonClickListener listener;
    public PokemonAdapter(){
        pokemons = new ArrayList<>();

    }

    public void addPokemon(Pokemon pokemon){
        pokemons.add(pokemon);
        notifyDataSetChanged();
    }

    public void clear(){
        pokemons.clear();
        notifyDataSetChanged();
    }

    @Override
    public PokemonViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.pokerow,parent,false);
        PokemonViewModel pokemonViewModel = new PokemonViewModel(view);

        return pokemonViewModel;
    }

    @Override
    public void onBindViewHolder(@NonNull PokemonViewModel holder, int position) {
        Pokemon pokemon = pokemons.get(position);
        holder.getNameRow().setText(pokemon.getName());
        holder.getActionRow().setOnClickListener(
                v->{
                    listener.onPokemonClick(pokemon);
                }
        );
        Glide.with(holder.getImageRow()).load(pokemon.getSprites().getFront_default()).into(holder.getImageRow());

    }

    @Override
    public int getItemCount() {
        return pokemons.size();
    }

    public void setListener(OnPokemonClickListener onPokemonClickListener){
        this.listener = onPokemonClickListener;
    }

    public interface OnPokemonClickListener{
        void onPokemonClick(Pokemon pokemon);
    }
}
