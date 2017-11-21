package shop.ineed.app.ineed.domain;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import shop.ineed.app.ineed.R;

/**
 * Created by silva on 21/11/17.
 */

public class PaymentMethods {

    private String key;
    private String name;
    private int icon;

    public PaymentMethods(String key, String name, int icon) {
        this.key = key;
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public static List<PaymentMethods> getPaymentMethods(List<String> paymentWays){
        List<PaymentMethods> paymentMethods = new ArrayList<>();

        Log.i("Payment", paymentWays.size() + " ");

        for(int index = 0; index < paymentWays.size(); index++){
            if(paymentWays.get(index).equals("bitcoin")){
                paymentMethods.add(new PaymentMethods("bitcoin", "bitcoin", R.drawable.ic_bitcoin));
            }else if(paymentWays.get(index).equals("check")){
                paymentMethods.add(new PaymentMethods("check", "cheque", R.drawable.ic_check));
            }else if(paymentWays.get(index).equals("credit")){
                paymentMethods.add(new PaymentMethods("credit", "Credito", R.drawable.ic_card));
            }else if(paymentWays.get(index).equals("debit")){
                paymentMethods.add(new PaymentMethods("debit", "Debito", R.drawable.ic_card));
            }else if(paymentWays.get(index).equals("money")){
                paymentMethods.add(new PaymentMethods("money", "Dinheiro", R.drawable.ic_money));
            }else if(paymentWays.get(index).equals("payment-slip")){
                paymentMethods.add(new PaymentMethods("payment-slip", "Boleto", R.drawable.ic_payment_slip));
            }else if(paymentWays.get(index).equals("paypal")){
                paymentMethods.add(new PaymentMethods("paypal", "Paypal", R.drawable.ic_paypal));
            }


            Log.i("Payment",  paymentMethods.size() + " ");
        }
        return paymentMethods;
    }
}
