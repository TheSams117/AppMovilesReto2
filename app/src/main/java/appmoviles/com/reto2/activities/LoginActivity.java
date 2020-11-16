package appmoviles.com.reto2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.UUID;

import appmoviles.com.reto2.R;
import appmoviles.com.reto2.model.User;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText usernameEdt;
    private Button loginBtn;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEdt = findViewById(R.id.usernameEdt);
        loginBtn = findViewById(R.id.loginBtn);

        db = FirebaseFirestore.getInstance();

        loginBtn.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.loginBtn:

                String username = usernameEdt.getText().toString();

                if(username==null || username.equals("")){
                    runOnUiThread( ()->{
                        Toast.makeText(this,"Ingresa un nombre de usuario",Toast.LENGTH_LONG).show();
                    });
                    return;
                }


                //Saber si el usuario ya estaba registrado
                CollectionReference usersRef = db.collection("users");
                Query query = usersRef.whereEqualTo("username", username);
                query.get().addOnCompleteListener(
                        task -> {
                            User user = new User(UUID.randomUUID().toString(), username);
                            Intent i = new Intent(this, PokeListActivity.class);
                            if (task.isSuccessful()) {

                                if (task.getResult().size() > 0) {
                                    for(QueryDocumentSnapshot document : task.getResult()){
                                        user = document.toObject(User.class);

                                        break;
                                    }
                                } else {
                                    db.collection("users").document(user.getId()).set(user);
                                }
                            }
                            i.putExtra("user",user);
                            startActivity(i);
                        }
                );

                break;
        }
    }
}