package com.example.administrator.cse476currencyconverter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.view.View.OnClickListener;
import android.widget.TextView;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import java.net.HttpURLConnection;
import java.net.URL;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    TextView currency;
    Button convert;
    EditText editText;
    Toolbar mToolbar;
    String oldcurrency;
    String newcurrency;
    ListView mListview;
    ListView mListview2;
    TextView mTextview;
    TextView mTextview2;
    String[] countryNames = {"TRY", "USD", "CAD", "GBP", "Euro", "AUD", "UAH"};
    int[] countryFlags = {  R.drawable.f_turkey,
                            R.drawable.f_usd,
                            R.drawable.f_cad,
                            R.drawable.f_gbp,
                            R.drawable.f_euro,
                            R.drawable.f_aud,
                            R.drawable.f_uah};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(getResources().getString(R.string.app_name));
        mListview = (ListView) findViewById(R.id.listview);
        mListview2 = (ListView) findViewById(R.id.listview2);
        convert = (Button) findViewById(R.id.convert);
        convert.setOnClickListener(this);
        mTextview = (TextView) findViewById(R.id.textView5);
        mTextview2 = (TextView) findViewById(R.id.textView6);
        currency = (TextView) findViewById(R.id.currency);
        editText = (EditText) findViewById(R.id.editText);

        Log.d("isil", "Onpost: end");
        MyAdapter myAdapter = new MyAdapter(MainActivity.this, countryNames, countryFlags);
        mListview.setAdapter(myAdapter);
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                oldcurrency = countryNames[i];

                putOldClick(oldcurrency);

            }

        });
        MyAdapter2 myAdapter2 = new MyAdapter2(MainActivity.this, countryNames, countryFlags);
        mListview2.setAdapter(myAdapter2);
        mListview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                newcurrency = countryNames[i];

                putNewClick(newcurrency);

            }

        });


    }
    public void putOldClick(String oldcurrency) {


        mTextview.setText(oldcurrency);


    }
    public void putNewClick(String newcurrency){

        mTextview2.setText(newcurrency);




    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {


            case R.id.convert:
                oldcurrency = mTextview.getText().toString();
                newcurrency = mTextview2.getText().toString();

                if (checkNetworkConnection()) {

                    if ((newcurrency!= null) && (oldcurrency!= null) ){

                        getConversionInfo(oldcurrency, newcurrency);
                    }

                }



                break;
        }

    }
    private boolean checkNetworkConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        if (ni!=null ) {
            return ni.isConnected();

        }
        else {
            return false;
        }
    }
    private void getConversionInfo(String oldcurrency, String newcurrency) {

        new HTTPAsyncTask().execute("http://currencyconverter.kowabunga.net/converter.asmx/GetLastUpdateDate",
                "http://currencyconverter.kowabunga.net/converter.asmx/GetConversionRate?CurrencyFrom=" + oldcurrency + "&CurrencyTo=" + newcurrency + "&RateDate=");
    }

    private class HTTPAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                String dateresult= HttpGet(urls[0]);
                String[] split= dateresult.split("T");
                String date= split[0];

                return HttpGet(urls[1]+date);

            } catch (IOException e) {

                return null;

            } catch(XmlPullParserException e){
                e.printStackTrace();
            }

            return null;
        }
        protected void onPostExecute (String result) {
            Log.d("isil", "Onpost: start");
            String mine =  editText.getText().toString();

            double first = Double.parseDouble(mine);
            double value = Double.parseDouble(result);
            double newresult= first*value;
            String s = Double.toString(newresult);

            String upToNCharacters = s.substring(0, Math.min(s.length(), 6));
            currency.setText(upToNCharacters);
            Log.d("isil", "Onpost: end");}
    }

    private String HttpGet(String urlString) throws IOException, XmlPullParserException {
        InputStream is = null;
        String result = "";
        URL url = new URL(urlString);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.connect();
        is = conn.getInputStream();

//        Toast.makeText(getApplicationContext(), "Connection established.",
//                Toast.LENGTH_SHORT).show();

        if(is != null) {
//            result = convertInputStreamToString(is);
            result = XMLParser(is);
            Log.d("ASDF", "ASDSAD");
        }
        else {
            result = "Did not work!";
        }

        return result;
    }



    public String XMLParser (InputStream is) throws XmlPullParserException, IOException {

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();

        xpp.setInput( new InputStreamReader(is));

        int eventType = xpp.getEventType();
        String result = "";
        while(eventType != XmlPullParser.END_DOCUMENT) {

            if(eventType == XmlPullParser.START_DOCUMENT){
                Log.d("isil", "XMLParser: startparser");
                //result = "Document Begining";
                System.out.println("Start");

            } else if(eventType == XmlPullParser.END_DOCUMENT){
                //result = result + System.getProperty("line.separator") + "End of Document";
                Log.d("isil", "XMLParser: enddocument");
            } else if(eventType == XmlPullParser.START_TAG) {
                String checker = xpp.getName();

                //result = result + System.getProperty("line.separator") + "Start tag:" +xpp.getName();



            } else if (eventType == XmlPullParser.END_TAG) {


                //result = result + System.getProperty("line.separator:") + "End tag:" +xpp.getName();
            } else if (eventType == XmlPullParser.TEXT) {

                result = xpp.getText();
                Log.d("text", "XMLParser: dateText " + result);
            }
            eventType = xpp.next();
        }
        //tv1.setText(result);

        return result;
    }
}
