package com.example.v_closet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.v_closet.R;
import com.example.v_closet.entity.Order;
import com.example.v_closet.entity.OrderItem;
import com.example.v_closet.service.OrderService;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Adapter hiển thị danh sách đơn hàng
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    
    private Context context;
    private List<Order> orders;
    private OrderService orderService;
    private NumberFormat currencyFormat;
    private SimpleDateFormat dateFormat;
    
    public OrderAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
        this.orderService = new OrderService(context);
        this.currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
    }
    
    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        
        holder.tvOrderId.setText("Đơn hàng #" + order.getId());
        holder.tvOrderDate.setText(dateFormat.format(order.getOrderDate()));
        holder.tvTotalAmount.setText(currencyFormat.format(order.getTotalAmount()) + "đ");
        holder.tvPaymentMethod.setText("Thanh toán: " + getPaymentMethodText(order.getPaymentMethod().name()));
        
        // Load order items
        List<OrderItem> items = orderService.getOrderItems(order.getId());
        OrderItemAdapter itemAdapter = new OrderItemAdapter(context, items);
        holder.rvOrderItems.setLayoutManager(new LinearLayoutManager(context));
        holder.rvOrderItems.setAdapter(itemAdapter);
    }
    
    @Override
    public int getItemCount() {
        return orders != null ? orders.size() : 0;
    }
    
    private String getPaymentMethodText(String method) {
        switch (method) {
            case "COD":
                return "Tiền mặt";
            case "BANK_TRANSFER":
                return "Chuyển khoản";
            default:
                return method;
        }
    }
    
    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId;
        TextView tvOrderDate;
        TextView tvTotalAmount;
        TextView tvPaymentMethod;
        RecyclerView rvOrderItems;
        
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);
            tvPaymentMethod = itemView.findViewById(R.id.tvPaymentMethod);
            rvOrderItems = itemView.findViewById(R.id.rvOrderItems);
        }
    }
}
