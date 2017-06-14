package com.mcn.apwmu;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.Collections;
import java.util.List;

public class AdapterMahasiswa extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<DataMahasiswa> data= Collections.emptyList();
    DataMahasiswa current;
    int currentPos=0;

    // create constructor to innitilize context and data sent from MainActivity
    public AdapterMahasiswa(Context context, List<DataMahasiswa> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }



    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.container_mahasiswa, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder= (MyHolder) holder;
        DataMahasiswa current=data.get(position);
        myHolder.textMhsName.setText(current.m_nama);
        myHolder.textMhsNim.setText(current.m_nim);
        myHolder.textMhsFakultas.setText( current.m_fakultas);

//        myHolder.textPrice.setText("Rs. " + current.price + "\\Kg");
//        myHolder.textPrice.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));

//         load image into imageview using glide
        Glide.with(context).load(current.m_foto)
                .placeholder(R.drawable.dummy)
                .error(R.drawable.dummy)
                .into(myHolder.img_Mhs);

    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder{

        TextView textMhsName;
        ImageView img_Mhs;
        TextView textMhsNim;
        TextView textMhsFakultas;
//        TextView textPrice;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            textMhsName= (TextView) itemView.findViewById(R.id.textMhsName);
            img_Mhs= (ImageView) itemView.findViewById(R.id.i_mhs);
            textMhsFakultas= (TextView) itemView.findViewById(R.id.textMhsFakultas);
            textMhsNim = (TextView) itemView.findViewById(R.id.textMhsNim);
//            textPrice = (TextView) itemView.findViewById(R.id.textPrice);
        }

    }

}
