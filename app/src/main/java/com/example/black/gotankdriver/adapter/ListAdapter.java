package com.example.black.gotankdriver.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.black.gotankdriver.DetailPemesan;
import com.example.black.gotankdriver.R;
import com.example.black.gotankdriver.model.PemesanModel;
import com.example.black.gotankdriver.utils.ApiUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ProductViewHolder> {

    private Context context;
    private List<PemesanModel> productPemesanList;

    public ListAdapter(Context context, List<PemesanModel> productPemesanList) {
        this.context = context;
        this.productPemesanList = productPemesanList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_pemesan,null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.binding(productPemesanList.get(position), context);
    }

    @Override
    public int getItemCount() {
        return productPemesanList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder{

        CircleImageView image_pemesan;
        TextView namaPemesan, alamatPemesan, hpPemesan;
        Button btn_detailPemesan;
        public ProductViewHolder(View itemView){
            super(itemView);

            image_pemesan = itemView.findViewById(R.id.iv_pemesan);
            namaPemesan = itemView.findViewById(R.id.tvNamaPemesan);
            alamatPemesan = itemView.findViewById(R.id.tvAlamatPemesan);
            hpPemesan = itemView.findViewById(R.id.tvNoHpPemesan);
            btn_detailPemesan = itemView.findViewById(R.id.btn_lihatPemesan);
        }

        void binding(final PemesanModel pemesanModel, final Context context){
            namaPemesan.setText(pemesanModel.getName());
            alamatPemesan.setText(pemesanModel.getAddress());
            hpPemesan.setText(pemesanModel.getPhone());
            Glide.with(context)
                    .load(ApiUtils.ENDPOINT+"img/"+pemesanModel.getAvatar())
//                    .placeholder(R.drawable.no_image)
//                    .error(R.drawable.no_image)
                    .into(image_pemesan);

            btn_detailPemesan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context, company.getName()+" tapped", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, DetailPemesan.class);
                    intent.putExtra("ID", pemesanModel.getId());
                    //Toast.makeText(context, ""+pesanModel.getId(), Toast.LENGTH_SHORT).show();
                    context.startActivity(intent);

                }
            });

        }
    }
}
