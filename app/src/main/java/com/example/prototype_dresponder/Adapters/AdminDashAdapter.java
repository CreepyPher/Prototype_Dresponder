package com.example.prototype_dresponder.Adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prototype_dresponder.AdminDash;
import com.example.prototype_dresponder.Modals.AdminDashList;
import com.example.prototype_dresponder.R;
import com.example.prototype_dresponder.Utils.FirebaseUtility;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

public class AdminDashAdapter extends RecyclerView.Adapter<AdminDashAdapter.AdminDashHolder> {

    private AdminDash adminDash;
    private List<AdminDashList> Dashlist;
    public AdminDashAdapter(AdminDash dash,List<AdminDashList> list){
        adminDash = dash;
        Dashlist = list;
    }

    @NonNull
    @Override
    public AdminDashAdapter.AdminDashHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(adminDash).inflate(R.layout.admindash_list,parent,false);
        return new AdminDashHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminDashAdapter.AdminDashHolder holder,int position) {
        holder.Name.setText(Dashlist.get(position).getName());
        holder.AccId.setText(Dashlist.get(position).getAccID());
        String ImgLink = Dashlist.get(position).getImglink();

        FirebaseUtility.getResSignupDocs(ImgLink+".png")
                .getBytes(Long.MAX_VALUE)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap1 = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                        holder.Img.setImageBitmap(bitmap1);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        FirebaseUtility.getResSignupDocs(ImgLink+".jpeg")
                                .getBytes(Long.MAX_VALUE)
                                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        Bitmap bitmap2 = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                                        holder.Img.setImageBitmap(bitmap2);
                                    }
                                });
                    }
                });



    }

    @Override
    public int getItemCount() {
        return Dashlist.size();
    }

    public static class AdminDashHolder extends RecyclerView.ViewHolder{
        private TextView Name,AccId;
        private ImageView Img;
        public AdminDashHolder(@NonNull View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.txtName);
            AccId = itemView.findViewById(R.id.txtAccID);
            Img = itemView.findViewById(R.id.imageViewAdmin);
        }

    }
}
