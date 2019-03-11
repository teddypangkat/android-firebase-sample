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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements UserAdapter.ClickMenuListener {


    private AlertDialog.Builder dialog;
    private LayoutInflater inflater;
    private View dialogView;

    private RecyclerView recyclerViewUser;
    private FloatingActionButton fbAddUser;

    private UserAdapter adapter;
    private List<User> listUser = new ArrayList<>();

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    private User mUser;

    private List<String> listKey = new ArrayList<>();

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


        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {

                        if (!task.isSuccessful()) {
                            return;
                        }

                        String token = task.getResult().getToken();
                        Log.d("TOKEN", token);

                    }
                });

    }


    private void initView() {
        recyclerViewUser = findViewById(R.id.recyclerUser);
        fbAddUser = findViewById(R.id.fbAdduser);
        fbAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogAddUser(false, 0);
            }
        });
    }

    private void showDialogAddUser(final boolean isUpdate, final int positionUser) {

        final EditText inputNama, inputEmail, inputNoTelp;
        Button btnSimpan;

        dialog = new AlertDialog.Builder(MainActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.add_data_user, null);

        inputNama = dialogView.findViewById(R.id.edNama);
        inputEmail = dialogView.findViewById(R.id.edEmail);
        inputNoTelp = dialogView.findViewById(R.id.edTelp);
        btnSimpan = dialogView.findViewById(R.id.btnSimpan);

        if (isUpdate) {
            //jika masuk kondisi update
            inputNama.setText(mUser.getNama());
            inputEmail.setText(mUser.getEmail());
            inputNoTelp.setText(mUser.getTelp());
            btnSimpan.setText("Update");
        }

        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setTitle("Tambah data user");

        final AlertDialog ad = dialog.show();
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isUpdate) {

                    updateUser(listKey.get(positionUser), inputNama.getText().toString(),
                            inputEmail.getText().toString(), inputNoTelp.getText().toString());
                } else {
                    createUser(inputNama.getText().toString(),
                            inputEmail.getText().toString(), inputNoTelp.getText().toString());

                }
                ad.dismiss();
                addUserEventListener();
            }
        });

        ad.show();
    }


    private void createUser(String nama, String email, String noTelp) {

        User user = new User(nama, email, noTelp);
        String key = databaseReference.push().getKey();
        databaseReference.child(key).setValue(user);
    }


    private void updateUser(String keyUser, String nama, String email, String noTlp) {

        databaseReference.child(keyUser).child("nama").setValue(nama);
        databaseReference.child(keyUser).child("email").setValue(email);
        databaseReference.child(keyUser).child("telp").setValue(noTlp);

    }

    private void deleteUser(String userKey) {
        databaseReference.child(userKey).removeValue();
        addUserEventListener();
    }

    private void initAdapter() {
        adapter = new UserAdapter(listUser, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewUser.setLayoutManager(layoutManager);
        recyclerViewUser.setAdapter(adapter);
    }


    private void addUserEventListener() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                //Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                emptyDataList();

                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {

                    User user = childSnapshot.getValue(User.class);
                    String userKey = childSnapshot.getKey();

                    listUser.add(user);
                    listKey.add(userKey);
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("ERROR", databaseError.getMessage());
            }
        });
    }


    private void emptyDataList() {
        if (!listUser.isEmpty()) {
            listUser.clear();
        }

        if (!listKey.isEmpty()) {
            listKey.clear();
        }
    }

    @Override
    public void onClickMenu(ImageView view, final int position) {

        PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
        popupMenu.getMenuInflater().inflate(R.menu.option_menu, popupMenu.getMenu());


        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.update:
                        mUser = listUser.get(position);
                        showDialogAddUser(true, position);
                        break;

                    case R.id.delete:
                        deleteUser(listKey.get(position));
                }

                return true;
            }
        });

        popupMenu.show();

    }
}
