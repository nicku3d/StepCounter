package com.gmail.nicku3d.stepcounter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StepsListAdapter extends RecyclerView.Adapter<StepsListAdapter.StepsViewHolder> {

    private LayoutInflater mInflater;
    private List<DailySteps> stepsList;
    StepsListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @NonNull
    @Override
    public StepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.stepslist_item, parent, false);
        return new StepsViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsViewHolder holder, int position) {
        DailySteps current = stepsList.get(position);
        holder.stepsItemView.setText("Steps:" +current.getSteps() +" Date:"+  current.getDate());
    }

    void setSteps(List<DailySteps> steps){
        stepsList = steps;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(stepsList != null)
            return stepsList.size();
        else return 0;
    }

    class StepsViewHolder extends RecyclerView.ViewHolder {
        public final TextView stepsItemView;
        final StepsListAdapter adapter;

        public StepsViewHolder(View itemView, StepsListAdapter adapter) {
            super(itemView);
            stepsItemView = itemView.findViewById(R.id.daily_steps);
            this.adapter = adapter;
        }
    }
}
