package com.example.vn.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vn.Interface.WardOnClick;
import com.example.vn.Models.Ward;
import com.example.vn.R;
import com.example.vn.Tools.ADDRESS;

import java.util.List;

public class WardAdapter extends RecyclerView.Adapter<WardAdapter.WardViewHolder> {
    private final Context context;
    private List<Ward> list;

    private final WardOnClick wardOnClick;

    public WardAdapter(Context context, WardOnClick wardOnClick) {
        this.context = context;
        this.wardOnClick = wardOnClick;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Ward> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WardAdapter.WardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.province_layout, parent, false);
        return new WardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WardAdapter.WardViewHolder holder, int position) {
        if (ADDRESS.ward != null) {
            holder.btn_choose.setText(ADDRESS.ward.getName());
            holder.btn_choose.setOnClickListener(v -> wardOnClick.ItemClick(ADDRESS.ward));
            return;
        }
        Ward ward = list.get(position);
        if (ward != null) {
            holder.btn_choose.setText(ward.getName());
            holder.btn_choose.setOnClickListener(v -> wardOnClick.ItemClick(ward));
        }

    }

    @Override
    public int getItemCount() {
        if (list != null) {
            if (ADDRESS.ward != null) {
                return 1;
            }
            return list.size();
        }
        return 0;
    }

    public static class WardViewHolder extends RecyclerView.ViewHolder {

        private final Button btn_choose;

        public WardViewHolder(@NonNull View itemView) {
            super(itemView);
            btn_choose = itemView.findViewById(R.id.btn_choose);
        }
    }
}
