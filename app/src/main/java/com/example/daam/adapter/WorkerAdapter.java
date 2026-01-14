package com.example.daam.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daam.R;
import com.example.daam.model.UserDTO;

import java.util.List;

public class WorkerAdapter extends RecyclerView.Adapter<WorkerAdapter.WorkerViewHolder> {

    private List<UserDTO> workerList;
    private OnWorkerClickListener listener;

    public interface OnWorkerClickListener {
        void onHireClick(UserDTO worker);
    }

    public WorkerAdapter(List<UserDTO> workerList, OnWorkerClickListener listener) {
        this.workerList = workerList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public WorkerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_worker, parent, false);
        return new WorkerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkerViewHolder holder, int position) {
        UserDTO worker = workerList.get(position);
        holder.tvName.setText(worker.getFirstName() + " " + worker.getLastName());
        holder.tvRating.setText(String.valueOf(worker.getRating() != null ? worker.getRating() : 0.0));
        holder.btnHire.setOnClickListener(v -> listener.onHireClick(worker));
    }

    @Override
    public int getItemCount() {
        return workerList.size();
    }

    public static class WorkerViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvRating;
        Button btnHire;

        public WorkerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvWorkerName);
            tvRating = itemView.findViewById(R.id.tvWorkerRating);
            btnHire = itemView.findViewById(R.id.btnHire);
        }
    }
}
