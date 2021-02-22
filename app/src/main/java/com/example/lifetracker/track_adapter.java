package com.example.lifetracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class track_adapter extends RecyclerView.Adapter<track_adapter.ViewHolder>{
    List<track_model>modelList;

    public track_adapter(List<track_model> modelList) {
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dailytrackerlayout , parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String date = modelList.get(position).getDate();
        String temp = modelList.get(position).getTemp();
        String oxy = modelList.get(position).getOxygen();
        String heart_rate = modelList.get(position).getHeart_rate();
        String symptoms = modelList.get(position).getSymptoms();
        holder.loadDta(date,temp,oxy,heart_rate,symptoms);
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView datetxt,temptxt,oxygentxt,hearttxt,symptomstxt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            datetxt = itemView.findViewById(R.id.datetxt);
            temptxt = itemView.findViewById(R.id.temptxt);
            oxygentxt = itemView.findViewById(R.id.oxygentxt);
            hearttxt = itemView.findViewById(R.id.heartratetxt);
            symptomstxt = itemView.findViewById(R.id.symptomstxt);
        }
        private void loadDta(String date,String Temp,String Oxy,String Heart_rate,String Symptoms){
            datetxt.setText("Updated At : "+date);
            temptxt.setText(Temp+" Celsius");
            oxygentxt.setText(Oxy+" %");
            hearttxt.setText(Heart_rate+" BPM");
            symptomstxt.setText(Symptoms);

        }
    }
}
