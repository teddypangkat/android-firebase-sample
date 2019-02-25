package id.eudeka.firebasesample;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> listUser;

    public UserAdapter(List<User> listUser) {
        this.listUser = listUser;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.item_row, viewGroup, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder userViewHolder, int i) {
        User user = listUser.get(i);
        userViewHolder.nama.setText(user.getNama());
        userViewHolder.email.setText(user.getEmail());
        userViewHolder.noTelp.setText(user.getTelp());

    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        private TextView nama, email, noTelp;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            nama = (TextView) itemView.findViewById(R.id.txtNama);
            email = (TextView) itemView.findViewById(R.id.txtEmail);
            noTelp = (TextView) itemView.findViewById(R.id.txtNoTelp);
        }
    }

}
