package com.example.jsonwithasync;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jsonwithasync.R;

import java.util.ArrayList;

public class JsonAdapter extends RecyclerView.Adapter<JsonAdapter.UserViewHolder> {

    private ArrayList<User> dataList;

    public JsonAdapter(ArrayList<User> dataList) {
        this.dataList = dataList;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_user, parent, false);
        return new UserViewHolder(view);
    }
    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        holder.txtName.setText(dataList.get(position).getName());
        holder.txtId.setText(dataList.get(position).getId());
        holder.txtUserName.setText(dataList.get(position).getUsername());
        holder.txtEmail.setText(dataList.get(position).getEmail());
        holder.txtAddress.setText(dataList.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{
        private TextView txtName, txtId,txtUserName, txtEmail,txtAddress;


        public UserViewHolder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.textName);
            txtId = (TextView) itemView.findViewById(R.id.textId);
            txtUserName = (TextView) itemView.findViewById(R.id.textUserName);
            txtEmail = (TextView) itemView.findViewById(R.id.textEmail);
            txtAddress = (TextView)itemView.findViewById(R.id.textAddress);


        }


    }


}
