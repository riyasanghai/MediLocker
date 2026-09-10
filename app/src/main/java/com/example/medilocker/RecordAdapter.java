package com.example.medilocker;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder>{
    ArrayList<Record> records;
    public RecordAdapter(ArrayList<Record> records){
        this.records=records;
    }

    // creates card
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_record, parent, false);
        return new ViewHolder(view);
    }

    //putting data into card
    @Override
    public  void onBindViewHolder(ViewHolder holder, int position){
        Record record = records.get(position);
        holder.title.setText(record.getTitle());
        holder.details.setText(
                record.getDoctor() + " | " +
                        record.getCategory() + " | " +
                        record.getDate()
        );

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), recordsdetails.class);

            intent.putExtra("title", record.getTitle());
            intent.putExtra("doctor", record.getDoctor());
            intent.putExtra("hospital", record.getHospital());
            intent.putExtra("category", record.getCategory());
            intent.putExtra("date", record.getDate());
            intent.putExtra("condition", record.getCondition());
            intent.putExtra("fileUrl", record.getFileUrl());

            view.getContext().startActivity(intent);
        });
    }

    //number of records
    @Override
    public int getItemCount() {
        return records.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView title, details;

        public ViewHolder(View itemView){
            super(itemView);

            title=itemView.findViewById(R.id.recordtitle);
            details=itemView.findViewById(R.id.recorddetail);
        }
    }

}