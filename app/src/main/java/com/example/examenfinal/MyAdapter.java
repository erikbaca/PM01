package com.example.examenfinal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

    private Context context;
    ArrayList<Medicamentos> list;
    ArrayList<Medicamentos> listaOriginal;


    public MyAdapter(Context context, ArrayList<Medicamentos> list ) {
        this.context = context;
        this.list = list;
        listaOriginal = new ArrayList<>();
        listaOriginal.addAll(list);

    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.iteam,parent, false);
        return  new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Medicamentos item = list.get(holder.getAdapterPosition());
        Medicamentos user = list.get(holder.getAdapterPosition());

        holder.txtnombre.setText(user.getNombre());
        holder.txtdescripcion.setText(user.getDescripcion());
        Glide.with(holder.img1.getContext()).load(user.getUrl()).into(holder.img1);

        final Medicamentos infoData = list.get(holder.getAdapterPosition());



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(holder.itemView.getContext(), Detalles.class);
                holder.itemView.getContext().startActivity(intent);
            }
        });
        holder.eliminarlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.txtnombre.getContext());
                builder.setTitle("Estas Seguro(a)");
                builder.setMessage("Los datos no se podran recuperar");
                builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String key1 = list.get(position).getId();
                        FirebaseDatabase.getInstance().getReference().child("nuevos")
                                .child(key1).removeValue();


                        Intent intent = new Intent(holder.itemView.getContext(),MainActivity.class);
                        holder.itemView.getContext().startActivity(intent);


                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(holder.txtnombre.getContext(), "Cancelar", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();

            }
        });



    }



    @Override
    public int getItemCount() {

        return list.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView img1;
        TextView txtnombre,txtdescripcion;
        Button btnModificar,eliminarlist;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            eliminarlist = itemView.findViewById(R.id.eliminarlist);


            img1 = itemView.findViewById(R.id.img1);
            txtnombre = itemView.findViewById(R.id.txtnombrelist);
            txtdescripcion = itemView.findViewById(R.id.txtdescripcion);







        }
    }



}


