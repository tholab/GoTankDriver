package com.example.black.gotankdriver.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.black.gotankdriver.DetailHistori;
import com.example.black.gotankdriver.DetailPemesan;
import com.example.black.gotankdriver.R;
import com.example.black.gotankdriver.model.PemesanModel;
import com.example.black.gotankdriver.utils.ApiUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HistoriAdapter extends RecyclerView.Adapter<HistoriAdapter.ProductViewHolder> {

    private Context context;
    private List<PemesanModel> productHistoriList;

    public HistoriAdapter(Context context, List<PemesanModel> productHistoriList) {
        this.context = context;
        this.productHistoriList = productHistoriList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_histori,null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.binding(productHistoriList.get(position), context);

    }

    @Override
    public int getItemCount() {
        return productHistoriList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder{

        CircleImageView image_histori;
        TextView namaHistori, alamatHistori, hpHistori, statusHistori;
        public ProductViewHolder(View itemView){
            super(itemView);

            image_histori = itemView.findViewById(R.id.iv_histori);
            namaHistori = itemView.findViewById(R.id.tvNamaHistori);
            alamatHistori = itemView.findViewById(R.id.tvAlamatHistori);
            hpHistori = itemView.findViewById(R.id.tvNohpHistori);
            statusHistori = itemView.findViewById(R.id.tvStatusHistori);
        }

        void binding(final PemesanModel pemesanModel, final Context context){
            namaHistori.setText(pemesanModel.getName());
            alamatHistori.setText(pemesanModel.getAddress());
            hpHistori.setText(pemesanModel.getPhone());
            statusHistori.setText(pemesanModel.getStatus());
            Glide.with(context)
                    .load(ApiUtils.ENDPOINT+"img/"+pemesanModel.getAvatar())
//                    .placeholder(R.drawable.no_image)
//                    .error(R.drawable.no_image)
                    .into(image_histori);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailHistori.class);
                    intent.putExtra("ID", pemesanModel.getId());
                    context.startActivity(intent);
                }
            });
        }
    }
}
