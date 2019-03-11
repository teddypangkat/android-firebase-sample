package id.eudeka.firebasesample;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> listUser;
    private ClickMenuListener clickMenuListener;

    public interface ClickMenuListener {
        void onClickMenu(ImageView view, int position);
    }

    public UserAdapter(List<User> listUser, ClickMenuListener clickMenuListener) {
        this.listUser = listUser;
        this.clickMenuListener = clickMenuListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.item_row, viewGroup, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserViewHolder userViewHolder, final int i) {
        final User user = listUser.get(i);
        userViewHolder.nama.setText(user.getNama());
        userViewHolder.email.setText(user.getEmail());
        userViewHolder.noTelp.setText(user.getTelp());


        userViewHolder.imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickMenuListener.onClickMenu(userViewHolder.imgMenu, i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        private TextView nama, email, noTelp;
        private ImageView imgMenu;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            nama = (TextView) itemView.findViewById(R.id.txtNama);
            email = (TextView) itemView.findViewById(R.id.txtEmail);
            noTelp = (TextView) itemView.findViewById(R.id.txtNoTelp);
            imgMenu = (ImageView) itemView.findViewById(R.id.imgMenu);



        }
    }

}
