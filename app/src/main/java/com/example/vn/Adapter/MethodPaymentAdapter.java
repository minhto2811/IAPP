package com.example.vn.Adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vn.Interface.MethodPaymentOnClick;
import com.example.vn.Models.MethodPayment;
import com.example.vn.R;

import java.util.List;

public class MethodPaymentAdapter extends RecyclerView.Adapter<MethodPaymentAdapter.MethodPaymentViewHolder> {
    private List<MethodPayment> list;
    private final MethodPaymentOnClick methodPaymentOnClick;

    public MethodPaymentAdapter(MethodPaymentOnClick methodPaymentOnClick) {
        this.methodPaymentOnClick = methodPaymentOnClick;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<MethodPayment> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MethodPaymentAdapter.MethodPaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.method_payment_layout, parent, false);
        return new MethodPaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MethodPaymentAdapter.MethodPaymentViewHolder holder, int position) {
        MethodPayment payment = list.get(position);
        if (payment != null) {

            if(!payment.isEnable()){
                String string = payment.getName()+"(không khả dụng)";
                SpannableString spannableString = new SpannableString(string);
                spannableString.setSpan(new ForegroundColorSpan(Color.RED),payment.getName().length(),string.length(),0);
                holder.tv_name_mp.setText(spannableString);
                holder.ln_mp.setBackgroundResource(R.color.gray);
                holder.ln_mp.setEnabled(false);
            }else {
                String string = payment.getName()+"(khả dụng)";
                SpannableString spannableString = new SpannableString(string);
                spannableString.setSpan(new ForegroundColorSpan(Color.GREEN),payment.getName().length(),string.length(),0);
                holder.tv_name_mp.setText(spannableString);
            }
            holder.ln_mp.setOnClickListener(v -> methodPaymentOnClick.ItemClick(payment));
        }

    }

    @Override
    public int getItemCount() {
        return (list != null) ? list.size() : 0;
    }

    public static class MethodPaymentViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_name_mp;
        private final LinearLayout ln_mp;

        public MethodPaymentViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name_mp = itemView.findViewById(R.id.tv_name_mp);
            ln_mp = itemView.findViewById(R.id.ln_mp);
        }
    }
}
