package com.example.v_closet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.v_closet.R;
import com.example.v_closet.entity.Address;

import java.util.List;

/**
 * Adapter cho RecyclerView hiển thị danh sách địa chỉ
 */
public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {
    
    private Context context;
    private List<Address> addresses;
    private OnAddressItemClickListener listener;
    
    public interface OnAddressItemClickListener {
        void onSetDefaultClick(Address address);
        void onEditClick(Address address);
        void onDeleteClick(Address address);
        void onItemClick(Address address); // For selection mode
    }
    
    public AddressAdapter(Context context, List<Address> addresses, OnAddressItemClickListener listener) {
        this.context = context;
        this.addresses = addresses;
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_address, parent, false);
        return new AddressViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        Address address = addresses.get(position);
        
        // Set tên người nhận
        holder.tvReceiverName.setText(address.getReceiverName());
        
        // Set số điện thoại
        holder.tvPhoneNumber.setText(address.getPhoneNumber());
        
        // Set địa chỉ đầy đủ
        holder.tvFullAddress.setText(address.getFullAddress());
        
        // Cập nhật UI của btnSetDefault theo trạng thái
        if (address.isDefault()) {
            // Địa chỉ mặc định: nền xanh lá, chữ đen, text "Mặc định"
            holder.btnSetDefault.setText("Mặc định");
            holder.btnSetDefault.setTextColor(context.getResources().getColor(R.color.black));
            holder.btnSetDefault.setBackgroundTintList(context.getResources().getColorStateList(R.color.green));
            holder.btnSetDefault.setEnabled(false);
        } else {
            // Không phải mặc định: nền trắng, viền xanh lá, chữ xanh lá, text "Đặt mặc định"
            holder.btnSetDefault.setText("Đặt mặc định");
            holder.btnSetDefault.setTextColor(context.getResources().getColor(R.color.green));
            holder.btnSetDefault.setBackgroundTintList(context.getResources().getColorStateList(R.color.white));
            holder.btnSetDefault.setEnabled(true);
        }
        
        // Click item (for selection mode)
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(address);
            }
        });
        
        // Click nút đặt mặc định
        holder.btnSetDefault.setOnClickListener(v -> {
            if (listener != null && !address.isDefault()) {
                listener.onSetDefaultClick(address);
            }
        });
        
        // Click nút sửa
        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(address);
            }
        });
        
        // Click nút xóa
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(address);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return addresses != null ? addresses.size() : 0;
    }
    
    /**
     * Cập nhật danh sách địa chỉ
     */
    public void updateAddresses(List<Address> newAddresses) {
        this.addresses = newAddresses;
        notifyDataSetChanged();
    }
    
    /**
     * ViewHolder cho address item
     */
    static class AddressViewHolder extends RecyclerView.ViewHolder {
        TextView tvReceiverName;
        TextView tvPhoneNumber;
        TextView tvFullAddress;
        Button btnSetDefault;
        ImageButton btnEdit;
        ImageButton btnDelete;
        
        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            tvReceiverName = itemView.findViewById(R.id.tvReceiverName);
            tvPhoneNumber = itemView.findViewById(R.id.tvPhoneNumber);
            tvFullAddress = itemView.findViewById(R.id.tvFullAddress);
            btnSetDefault = itemView.findViewById(R.id.btnSetDefault);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
