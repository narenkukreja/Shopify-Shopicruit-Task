import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class Shopify {
    public static void main(String[] args) {

        String requestURL = "http://shopicruit.myshopify.com/products.json";
        priceCalculation(requestURL);

    }

    private static void priceCalculation(String requestURL) {

        double priceBeforeTax = 0;

        try {

            JSONObject jsonObject = new JSONObject(readUrl(requestURL));
            JSONArray productsArray = jsonObject.getJSONArray("products");

            for (int i = 0; i < productsArray.length(); i++) {

                String product_type = productsArray.getJSONObject(i).getString("product_type");

                if (product_type.equals("Wallet") || product_type.equals("Lamp")) {

                    JSONArray variantsArray = productsArray.getJSONObject(i).getJSONArray("variants");

                    for (int j = 0; j < variantsArray.length(); j++) {

                        String stringPrice = variantsArray.getJSONObject(j).getString("price");

                        double price = Double.parseDouble(stringPrice);
                        priceBeforeTax += price;

                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        double taxRate = 1.13;
        double finalPrice = priceBeforeTax * taxRate;

        System.out.printf("Price (Lamps & Wallets) without tax: $%.2f \n" +
                "Price (Lamps & Wallets) after tax: $%.2f", priceBeforeTax, finalPrice);

    }

    private static String readUrl(String requestUrl) throws Exception {

        BufferedReader reader = null;

        try {

            URL url = new URL(requestUrl);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));

            StringBuffer buffer = new StringBuffer();

            int read;
            char[] chars = new char[1024];

            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();

        } finally {

            if (reader != null)
                reader.close();
        }
    }
}
