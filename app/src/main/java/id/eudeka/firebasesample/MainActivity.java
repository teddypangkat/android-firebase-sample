package id.eudeka.firebasesample;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private AlertDialog.Builder dialog;
    private LayoutInflater inflater;
    private View dialogView;

    private RecyclerView recyclerViewUser;
    private FloatingActionButton fbAddUser;

    private UserAdapter adapter;
    private List<User> listUser = new ArrayList<>();

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initAdapter();


        firebaseDatabase = FirebaseDatabase.getInstance();

        //referensi ke node users
        databaseReference = firebaseDatabase.getReference("users");

        //menyimpan referensi node app title
        firebaseDatabase.getReference("app_title").setValue("Eudeka Realtime Database");

        addUserEventListener();

//        firebaseDatabase.getReference("app_title").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                String appTitle = dataSnapshot.getValue(String.class);
//                getSupportActionBar().setTitle(appTitle);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                //error ketika baca nilai
//                Log.e("ERROR", "Failed to read app title value.", databaseError.toException());
//            }
//        });
//


    }


    private void initView() {
        recyclerViewUser = findViewById(R.id.recyclerUser);
        fbAddUser = findViewById(R.id.fbAdduser);
        fbAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogAddUser();
            }
        });
    }

    private void showDialogAddUser() {

        final EditText inputNama, inputEmail, inputNoTelp;
        Button btnSimpan;

        dialog = new AlertDialog.Builder(MainActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.add_data_user, null);

        inputNama = dialogView.findViewById(R.id.edNama);
        inputEmail = dialogView.findViewById(R.id.edEmail);
        inputNoTelp = dialogView.findViewById(R.id.edTelp);
        btnSimpan = dialogView.findViewById(R.id.btnSimpan);

        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setTitle("Tambah data user");

        final AlertDialog ad = dialog.show();
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createuser(inputNama.getText().toString(),
                        inputEmail.getText().toString(), inputNoTelp.getText().toString());


                ad.dismiss();
            }
        });

        ad.show();
    }


    private void createuser(String nama, String email, String noTelp) {

        User user = new User(nama, email, noTelp);
        String key = databaseReference.push().getKey();
        databaseReference.child(key).setValue(user);
    }


    private void initAdapter() {
        adapter = new UserAdapter(listUser);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewUser.setLayoutManager(layoutManager);
        recyclerViewUser.setAdapter(adapter);
    }


    private void addUserEventListener() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                if (!listUser.isEmpty()) {
                    listUser.clear();
                }

                while (iterator.hasNext()) {

                    User user = iterator.next().getValue(User.class);
                    listUser.add(user);
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("ERROR", databaseError.getMessage());
            }
        });
    }
}
