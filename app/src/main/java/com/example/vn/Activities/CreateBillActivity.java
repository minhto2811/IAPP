package com.example.vn.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vn.Adapter.CartAdapter;
import com.example.vn.Api.ApiService;
import com.example.vn.Models.Address;
import com.example.vn.Models.Bill;
import com.example.vn.Models.MethodPayment;
import com.example.vn.Models.MethodShipping;
import com.example.vn.R;
import com.example.vn.Tools.ACCOUNT;
import com.example.vn.Tools.ADDRESS;
import com.example.vn.Tools.LIST;
import com.example.vn.Tools.TOOLS;
import com.example.vn.UI.CartFragment;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateBillActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Bill bill;
    private RecyclerView recyclerView;

    private TextView tv_name, tv_rules, tv_total_price, tv_total_shipping, tv_total_all, tv_method_payment, tv_numberphone, tv_more, tv_address, tv_tilte_address, tv_type_ms, tv_price_ms, tv_time_ms;
    private Address address;

    private LinearLayout ln_address_child, ln_address, ln_method_shipping, ln_color_ms, ln_method_payment;

    private final int REQUESR_ADDRESS_CHOOSE = 777;
    private final int REQUESR_METHOD_SHIPPING = 888;
    private final int REQUESR_METHOD_PAYMENT = 999;
    private MethodShipping methodShipping;
    private int total_product;
    private Button btn_confirm_buy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_create_bill);
        mapping();
        setToolbar();
        getBill();
        showAddress();
        chooseAddress();
        showList();
        methodShipping = LIST.listMethodShipping().get(1);
        showMethodShipping(methodShipping);
        methodShipping();
        showMethodPayment(LIST.listMethodPayment().get(0));
        methodPayment();
        showDetailPayment(total_product, methodShipping.getPrice());
        rules();
        buy();
    }



    private void buy() {
        btn_confirm_buy.setOnClickListener(v -> {
            if (bill.getAddress() == null) {
                Toast.makeText(CreateBillActivity.this, "Vui lòng chọn địa chỉ", Toast.LENGTH_SHORT).show();
                return;
            }
            Dialog dialog = TOOLS.createDialog(CreateBillActivity.this);
            dialog.show();
            ApiService.apiService.createBill(ACCOUNT.user.getTokenNotify(),bill).enqueue(new Callback<Bill>() {
                @Override
                public void onResponse(@NonNull Call<Bill> call, @NonNull Response<Bill> response) {
                    dialog.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        if (!getIntent().getBooleanExtra("buy", false)) {
                            if (LIST.listBuyCart.size() != CartFragment.cartList.size()) {
                                for (int i = 0; i < CartFragment.cartList.size(); i++) {
                                    for (int j = 0; j < LIST.listBuyCart.size(); j++) {
                                        if (CartFragment.cartList.get(i).get_id().equals(LIST.listBuyCart.get(j).get_id())) {
                                            CartFragment.cartList.remove(i);
                                            break;
                                        }
                                    }
                                }
                            }
                            LIST.listBuyCart.clear();
                            TOOLS.checkAllCarts = false;
                        }
                        goResult(CreateBillActivity.this,0);
                    }else {
                        goResult(CreateBillActivity.this,-1);
                    }
                }


                @Override
                public void onFailure(@NonNull Call<Bill> call, @NonNull Throwable t) {
                    Toast.makeText(CreateBillActivity.this, "Lỗi!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
        });
    }

    public static void goResult(Context context, int index){
        Intent intent = new Intent(context, ResultActivity.class);
        intent.putExtra("result", index);
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.next_enter, R.anim.next_exit);
        ((Activity)context).finish();
    }

    private void rules() {
        String s = "Nhấn \"Đặt hàng\" đồng nghĩa với việc bạn đồng ý tuân theo Điều khoản StyleZone.";
        SpannableString spannableString = new SpannableString(s);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Toast.makeText(CreateBillActivity.this, "Điều khoản mua sắm", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.CYAN);
            }
        };
        spannableString.setSpan(clickableSpan, 57, s.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tv_rules.setText(spannableString);
        tv_rules.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void showDetailPayment(int tp, int ts) {
        tv_total_price.setText(TOOLS.convertPrice(tp));
        tv_total_shipping.setText(TOOLS.convertPrice(ts));
        tv_total_all.setText(TOOLS.convertPrice(tp + ts));
        bill.setTotal(tp + ts);
    }

    private void methodPayment() {
        ln_method_payment.setOnClickListener(v -> gotoActivityForResult(REQUESR_METHOD_PAYMENT, MethodPaymentActivity.class));
    }

    private void gotoActivityForResult(int code, Class<?> aClass) {
        Intent intent = new Intent(CreateBillActivity.this, aClass);
        startActivityForResult(intent, code);
        overridePendingTransition(R.anim.next_enter, R.anim.next_exit);
    }

    private void showMethodPayment(MethodPayment methodPayment) {
        bill.setMethodPayment(methodPayment.getName());
        tv_method_payment.setText(methodPayment.getName());
    }

    private void showMethodShipping(MethodShipping methodShipping) {
        bill.setMethodShipping(methodShipping.getName());
        ln_color_ms.setBackgroundResource(methodShipping.getColor());
        tv_type_ms.setText(methodShipping.getName());
        tv_price_ms.setText(TOOLS.convertPrice(methodShipping.getPrice()));
        tv_time_ms.setText(methodShipping.getTime());
    }

    private void methodShipping() {
        ln_method_shipping.setOnClickListener(v -> gotoActivityForResult(REQUESR_METHOD_SHIPPING, MethodShippingActivity.class));
    }

    private void chooseAddress() {
        ln_address.setOnClickListener(v -> {
            Intent intent = new Intent(CreateBillActivity.this, AddressActivity.class);
            intent.putExtra("choose", true);
            startActivityForResult(intent, REQUESR_ADDRESS_CHOOSE);
            overridePendingTransition(R.anim.next_enter, R.anim.next_exit);
        });
    }


    @SuppressLint("SetTextI18n")
    private void showAddress() {
        address = ADDRESS.aDefault(CreateBillActivity.this);
        if (address != null) {
            bill.setName(address.getFullname());
            bill.setPhone(address.getNumberphone());
            bill.setAddress(address.getAddress() + ", " + address.getWards() + ", " + address.getDistrict() + ", " + address.getProvince() + ".");
            tv_name.setText(address.getFullname());
            tv_numberphone.setText(address.getNumberphone());
            tv_more.setText(address.getAddress());
            tv_address.setText(address.getWards() + ", " + address.getDistrict() + ", " + address.getProvince());
        } else {
            ln_address_child.setVisibility(View.GONE);
            tv_tilte_address.setText("Chưa chọn địa chỉ nhận hàng");
        }
    }

    private void mapping() {
        btn_confirm_buy = findViewById(R.id.btn_confirm_buy);
        tv_total_all = findViewById(R.id.tv_total_all);
        tv_total_shipping = findViewById(R.id.tv_total_shipping);
        tv_total_price = findViewById(R.id.tv_total_price);
        ln_method_payment = findViewById(R.id.ln_method_payment);
        tv_method_payment = findViewById(R.id.tv_method_payment);
        ln_method_shipping = findViewById(R.id.ln_method_shipping);
        ln_address = findViewById(R.id.ln_address);
        ln_address_child = findViewById(R.id.ln_address_child);
        tv_tilte_address = findViewById(R.id.tv_tilte_address);
        tv_more = findViewById(R.id.tv_more);
        tv_address = findViewById(R.id.tv_address);
        tv_numberphone = findViewById(R.id.tv_numberphone);
        tv_name = findViewById(R.id.tv_name);
        toolbar = findViewById(R.id.toolbar_bill);
        recyclerView = findViewById(R.id.rcv_pay);
        tv_type_ms = findViewById(R.id.tv_type_ms);
        tv_time_ms = findViewById(R.id.tv_time_ms);
        tv_price_ms = findViewById(R.id.tv_price_ms);
        ln_color_ms = findViewById(R.id.ln_color_ms);
        tv_rules = findViewById(R.id.tv_rules);
    }

    private void showList() {
        LinearLayoutManager manager = new LinearLayoutManager(CreateBillActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(CreateBillActivity.this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        CartAdapter adapter = new CartAdapter(CreateBillActivity.this, true);
        recyclerView.setAdapter(adapter);
        adapter.setData(bill.getList());
    }

    private void getBill() {
        bill = (Bill) getIntent().getSerializableExtra("bill");
        total_product = bill.getTotal();
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.title_buy);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESR_ADDRESS_CHOOSE) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                address = (Address) data.getSerializableExtra("address");
                bill.setName(address.getFullname());
                bill.setPhone(address.getNumberphone());
                bill.setAddress(address.getAddress() + ", " + address.getWards() + ", " + address.getDistrict() + ", " + address.getProvince() + ".");
                tv_name.setText(address.getFullname());
                tv_numberphone.setText(address.getNumberphone());
                tv_more.setText(address.getAddress());
                tv_address.setText(address.getWards() + ", " + address.getDistrict() + ", " + address.getProvince());
                ln_address_child.setVisibility(View.VISIBLE);
            }
        } else if (requestCode == REQUESR_METHOD_SHIPPING) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                methodShipping = (MethodShipping) data.getSerializableExtra("method_shipping");
                showMethodShipping(methodShipping);
                showDetailPayment(total_product, methodShipping.getPrice());
            }
        } else if (requestCode == REQUESR_METHOD_PAYMENT) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                MethodPayment payment = (MethodPayment) data.getSerializableExtra("method_payment");
                showMethodPayment(payment);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.prev_enter, R.anim.prev_exit);
    }


}