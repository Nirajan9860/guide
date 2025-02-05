package com.example.guide.ui.forex;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.guide.Model.Currency.CurrencyData;
import com.example.guide.Model.Forex.ForexData;
import com.example.guide.Model.Forex.Rates;
import com.example.guide.Model.ForexModel;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ForexViewModel extends AndroidViewModel {

    private MutableLiveData<ForexModel> forexData;
    private MutableLiveData<String[]> arrayAdapterString ;
    private ArrayList<Object> forexList;
    private ArrayList<String> pairList;
    private HashMap<String, String> countryNameMap;
    public MutableLiveData<String> errorText;
    public MutableLiveData<Boolean> isClicked ;
    public MutableLiveData<Boolean> isBusy ;
    public MutableLiveData<String> countryNameSearch ;
    public MutableLiveData<String> convertNumber ;
    public MutableLiveData<String> convertedValue ;


    private String supportedPairs = "AUDUSD,EURGBP,EURUSD,GBPUSD,NZDUSD,USDAED,USDAFN,USDALL,USDAMD,USDANG,USDAOA,USDARS,USDATS,USDAUD,USDAWG,USDAZM,USDAZN,USDBAM,USDBBD,USDBDT,USDBEF,USDBGN,USDBHD,USDBIF,USDBMD,USDBND,USDBOB,USDBRL,USDBSD,USDBTN,USDBWP,USDBYN,USDBYR,USDBZD,USDCAD,USDCDF,USDCHF,USDCLP,USDCNH,USDCNY,USDCOP,USDCRC,USDCUC,USDCUP,USDCVE,USDCYP,USDCZK,USDDEM,USDDJF,USDDKK,USDDOP,USDDZD,USDEEK,USDEGP,USDERN,USDESP,USDETB,USDEUR,USDFIM,USDFJD,USDFKP,USDFRF,USDGBP,USDGEL,USDGGP,USDGHC,USDGHS,USDGIP,USDGMD,USDGNF,USDGRD,USDGTQ,USDGYD,USDHKD,USDHNL,USDHRK,USDHTG,USDHUF,USDIDR,USDIEP,USDILS,USDIMP,USDINR,USDIQD,USDIRR,USDISK,USDITL,USDJEP,USDJMD,USDJOD,USDJPY,USDKES,USDKGS,USDKHR,USDKMF,USDKPW,USDKRW,USDKWD,USDKYD,USDKZT,USDLAK,USDLBP,USDLKR,USDLRD,USDLSL,USDLTL,USDLUF,USDLVL,USDLYD,USDMAD,USDMDL,USDMGA,USDMGF,USDMKD,USDMMK,USDMNT,USDMOP,USDMRO,USDMRU,USDMTL,USDMUR,USDMVR,USDMWK,USDMXN,USDMYR,USDMZM,USDMZN,USDNAD,USDNGN,USDNIO,USDNLG,USDNOK,USDNPR,USDNZD,USDOMR,USDPAB,USDPEN,USDPGK,USDPHP,USDPKR,USDPLN,USDPTE,USDPYG,USDQAR,USDROL,USDRON,USDRSD,USDRUB,USDRWF,USDSAR,USDSBD,USDSCR,USDSDD,USDSDG,USDSEK,USDSGD,USDSHP,USDSIT,USDSKK,USDSLL,USDSOS,USDSPL,USDSRD,USDSRG,USDSTD,USDSTN,USDSVC,USDSYP,USDSZL,USDTHB,USDTJS,USDTMM,USDTMT,USDTND,USDTOP,USDTRL,USDTRY,USDTTD,USDTVD,USDTWD,USDTZS,USDUAH,USDUGX,USDUSD,USDUYU,USDUZS,USDVAL,USDVEB,USDVEF,USDVES,USDVND,USDVUV,USDWST,USDXAF,USDXAG,USDXAU,USDXBT,USDXCD,USDXDR,USDXOF,USDXPD,USDXPF,USDXPT,USDYER,USDZAR,USDZMK,USDZMW,USDZWD ";
    private String forexUrl = "https://www.freeforexapi.com/api/live?pairs=" + supportedPairs;
    private String currencyUrl = "https://free.currconv.com/api/v7/currencies?apiKey=" + "de548f61b8d4321b60ed";
    private String[] listCurrecyName;
    private Double fromRatio, toRatio;

    public MutableLiveData<Boolean> busy;
    public ForexViewModel(@NonNull Application application) {
        super(application);
        isClicked = new MutableLiveData<>();
        isBusy = new MutableLiveData<>();
        countryNameSearch = new MutableLiveData<>();
        convertNumber = new MutableLiveData<>();
        convertedValue = new MutableLiveData<>();
        errorText = new MutableLiveData<>();
        errorText.setValue("");
        countryNameSearch.setValue("");
        convertedValue.setValue("");
        convertNumber.setValue("");
        isClicked.setValue(Boolean.FALSE);
        isBusy.setValue(Boolean.TRUE);
    }

    public MutableLiveData<ForexModel> loadForexData(){
        if(forexData == null){
            forexData = new MutableLiveData<ForexModel>();
            getCurrencyData();
        }
        return forexData;
    }

    public MutableLiveData<String[]> loadSpinnerData(){
        if(arrayAdapterString == null){
            arrayAdapterString = new MutableLiveData<String[]>();
            loadSpinner();
        }
        return arrayAdapterString;
    }

    private void loadSpinner() {

    }


    public void onCurrencyTextChanged(CharSequence s, int start, int before, int count){
        filter(s.toString());
    }

    public void onConversionTextChanged(CharSequence s, int start, int before, int count){
        try {
            convertedValue.setValue("" + (Double.parseDouble(s.toString()) * toRatio / fromRatio));
        }catch (Exception e){
            Log.i("Forex View Model", e.getLocalizedMessage());
        }
    }

    public void onConvesionClicked(){
        try{
            convertedValue.setValue( "" + (Double.parseDouble(convertNumber.getValue()) * toRatio / fromRatio));
        }catch (Exception e){
            Log.i("Forex View Model", e.getLocalizedMessage());
        }
    }


    public void onFabClicked(){

        isClicked.setValue(Boolean.TRUE); //View.VISIBLE
    }

    public void onRefresh(){  getCurrencyData();}

    public void spinnerForexFromSelected(AdapterView<?> parent, View view, int position, long id) {
        if (pairList != null) {
            new FindFromRatio().execute(position);

        }
    }

    public void spinnerForexToSelected(AdapterView<?> parent, View view, int position, long id){
        if(pairList != null) {
            new FindToRatio().execute(position);
        }
    }



    private List<String> filteredListPair = new ArrayList<>();
    private List<Object> filteredListForex = new ArrayList<>();

    private void filter(String text){
        filteredListPair.clear();
        filteredListForex.clear();


        for (Map.Entry<String, String> stringStringEntry : countryNameMap.entrySet()) {

            if (((Map.Entry) stringStringEntry).getValue().toString().toLowerCase().contains(text.toLowerCase())) {

                for (String forexPair : pairList) {
                    if (forexPair.contains(((Map.Entry) stringStringEntry).getKey().toString())) {
                        int index = pairList.indexOf(forexPair);
                        filteredListForex.add(forexList.get(index));
                        filteredListPair.add(pairList.get(index));
                    }
                }
            }

        }
        ForexModel forexModel = new ForexModel(filteredListPair, filteredListForex, countryNameMap);

        forexData.setValue(forexModel);



    }

    /************** FOREX Data Function************/

    public void getForexData() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, forexUrl, null, response -> {


//                Log.i("Forex Data Function", countryNameMap.toString());

            ForexModel forexModel = new ForexModel(pairList, forexList, countryNameMap);
            forexData.setValue(forexModel);
            isBusy.setValue(Boolean.FALSE);
            errorText.setValue("");
        }, error -> {
                errorText.setValue(new String("No Internet Connection! Cannot load fresh Data."));
           }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }
                    final long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                    final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                    long now = System.currentTimeMillis();
                    final long softExpire = now + cacheHitButRefreshed;
                    final long ttl = now + cacheExpired;
                    cacheEntry.data = response.data;
                    cacheEntry.softTtl = softExpire;
                    cacheEntry.ttl = ttl;
                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));

                    // parseNetworkResponse works on background thread and doesnot slow down UI Thread so parsing of data is done here for performance
                    // Here JsonString contains Json data
                    ForexData forexData = new Gson().fromJson(jsonString, ForexData.class);

                    forexList = new ArrayList<>();
                    pairList = new ArrayList<>();

                    Rates rates;

                    java.lang.reflect.Method ratesMethod;
                    try {
                        ratesMethod = ForexData.class.getMethod("getRates");

                        java.lang.reflect.Method execRatesMethod = forexData.getClass().getMethod(ratesMethod.getName());
                        rates = (Rates) execRatesMethod.invoke(forexData);

                        java.lang.reflect.Method[] methods = Rates.class.getMethods();
                        Object obj;
                        for (java.lang.reflect.Method method : methods) {
                            String name = method.getName();
                            if (name.startsWith("get") && !name.endsWith("Class")) {
                                assert rates != null;
                                java.lang.reflect.Method execMethod = rates.getClass().getMethod(name);
                                //  String className = "" + execMethod.getClass().getSimpleName();
                                String className = "com.example.guide.Model.Forex.";
                                String classNameAdd = name.substring(3);
                                className += classNameAdd;

                                Class<?> c = Class.forName(className);

                                obj = c.cast(execMethod.invoke(rates));

                                if (obj != null) {
                                    //   message += obj.toString() + "\n\n";

//                                message += name +" " +name.substring(3)+" "+ obj.toString() +" "+execMethod.getReturnType()+ "\n\n";
                                    java.lang.reflect.Method[] objMethods = obj.getClass().getMethods();
                                    for (java.lang.reflect.Method objMethod : objMethods) {
                                        String objMethodName = objMethod.getName();
                                        if (objMethodName.equals("getRate")) {
                                            pairList.add(classNameAdd);
                                            forexList.add(objMethod.invoke(obj));

                                        }
                                    }
                                }
                            }
                        }


                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                    return Response.success(new JSONObject(jsonString), cacheEntry);
                } catch (UnsupportedEncodingException | JSONException e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(JSONObject response) {
                super.deliverResponse(response);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplication());
        requestQueue.add(jsonObjectRequest);
    }


    /******************************Currency Function*******************************/

    /* Currency Data function*/
    public void getCurrencyData() {

         //forexProgressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, currencyUrl, null, response -> {
            getForexData();
            if(countryNameMap != null){
                new SetupDropdownList().execute();

            }
            //    Log.i("ForexActivity.class",response.toString());
        }, error -> Log.e("ForexActivity", error.toString())) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }
                    final long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                    final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                    long now = System.currentTimeMillis();
                    final long softExpire = now + cacheHitButRefreshed;
                    final long ttl = now + cacheExpired;
                    cacheEntry.data = response.data;
                    cacheEntry.softTtl = softExpire;
                    cacheEntry.ttl = ttl;
                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));

                    CurrencyData currencyData = new Gson().fromJson(jsonString, CurrencyData.class);

                    com.example.guide.Model.Currency.Results results;

                    ArrayList<Object> forexList = new ArrayList<>();


                    java.lang.reflect.Method resultsMethod;
                    try {
                        resultsMethod = CurrencyData.class.getMethod("getResults");

                        java.lang.reflect.Method execRatesMethod = currencyData.getClass().getMethod(resultsMethod.getName());
                        results = (com.example.guide.Model.Currency.Results) execRatesMethod.invoke(currencyData);

                        //  message += results.toString() + " ";
                        java.lang.reflect.Method[] methods = com.example.guide.Model.Currency.Results.class.getMethods();
                        Object obj;
                        countryNameMap = new HashMap<>();
                        for (java.lang.reflect.Method method : methods) {
                            String name = method.getName();
                            if (name.startsWith("get") && !name.endsWith("Class")) {
                                assert results != null;
                                java.lang.reflect.Method execMethod = results.getClass().getMethod(name);
                                //String className = "" + execMethod.getClass().getSimpleName();
                                String className = "com.example.guide.Model.Currency.";
                                className += name.substring(3);
//                                Log.i("TorpeCurrency",className);
                                Class<?> c = Class.forName(className);

                                obj = c.cast(execMethod.invoke(results));

                                if (obj != null) {
                                    //    message += obj.toString() + "\n\n";

                                    //   message += name +" " +name.substring(3)+" "+ obj.toString() +" "+execMethod.getReturnType()+ "\n\n";
                                    String currencyName = "";
                                    String currencyId = "";
                                    java.lang.reflect.Method[] objMethods = obj.getClass().getMethods();
                                    for (java.lang.reflect.Method objMethod : objMethods) {
                                        String objMethodName = objMethod.getName();
                                        if (objMethodName.startsWith("get") && !objMethodName.endsWith("Class")) {
                                            if (objMethodName.equals("getCurrencyName")) {
                                                currencyName = (String) objMethod.invoke(obj);
                                            } else if (objMethodName.equals("getId")) {
                                                currencyId = (String) objMethod.invoke(obj);
                                            }
                                            //  message += objMethodName + ": "+ objMethod.invoke(obj).toString() + " " ;
                                        }
                                    }
                                    countryNameMap.put(currencyId, currencyName);
//                                   message += currencyId + " " + countryNameMap.get(currencyId) + "\n\n";
//                                   Log.i("ForexActivity",message);
//                                    message = "";
                                }
                            }
                        }


                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    //   Log.i("Torpe","" + currencyData);


                    return Response.success(new JSONObject(jsonString), cacheEntry);
                } catch (UnsupportedEncodingException | JSONException e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(JSONObject response) {
                super.deliverResponse(response);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplication());
        requestQueue.add(jsonObjectRequest);
    }

    private class FindFromRatio extends AsyncTask<Integer, Integer, Double> {

        @Override
        protected void onPostExecute(Double aFloat) {
            fromRatio = aFloat;
            super.onPostExecute(aFloat);
        }

        @Override
        protected Double doInBackground(Integer... integer) {
            int listPosition = 0;
            Double ratio = 0.0;

            for (String s : pairList) {

                Log.i("sdf", s.substring(3));
                if(s.substring(0,3).equals("USD")) {
                    if (s.substring(3).contains(listCurrecyName[integer[0]])) {
                        if(listCurrecyName[integer[0]].equals("USD") || listCurrecyName[integer[0]].equals("BMD") || listCurrecyName[integer[0]].equals("CUC")){
                            ratio = (Double) 1.0;
                        }else {
                            ratio = (Double) forexList.get(listPosition) * 1.0;
                            Log.i("PRICE", ratio.toString());
                        }
                        break;

                    }
                }
                listPosition++;
            }
            return ratio;
        }
    }

    private class FindToRatio extends AsyncTask<Integer, Integer, Double> {

        @Override
        protected void onPostExecute(Double aFloat) {
            toRatio = aFloat;
            super.onPostExecute(aFloat);
        }

        @Override
        protected Double doInBackground(Integer... integer) {
            int listPosition = 0;
            Double ratio = 0.0;
            for (String s : pairList) {
                if(s.substring(0,3).equals("USD")) {
                    if (s.substring(3).contains(listCurrecyName[integer[0]])) {
                       if(listCurrecyName[integer[0]].equals("USD") || listCurrecyName[integer[0]].equals("BMD") || listCurrecyName[integer[0]].equals("CUC")){
                           ratio = (Double) 1.0;
                       }else {
                           ratio = (Double) forexList.get(listPosition);
                           Log.i("PRICE", ratio.toString());
                       }
                        break;

                    }
                }
                listPosition++;
            }
            return ratio;
        }
    }

    private class SetupDropdownList extends AsyncTask<HashMap<String, String>, Integer, String[]> {

        @Override
        protected String[] doInBackground(HashMap<String, String>... hashMaps) {
            String[] strings = new String[countryNameMap.size()];
            listCurrecyName = new String[countryNameMap.size()];
            int i = 0;

            // TreeMap to store values of HashMap
            TreeMap<String, String> sorted = new TreeMap<>();

            // Copy all data from hashMap into TreeMap
            sorted.putAll(countryNameMap);
            for (Map.Entry<String, String> entry : sorted.entrySet()) {
                strings[i] = entry.getValue();
                listCurrecyName[i] = entry.getKey();
                i++;
            }
            return strings;
        }

        @Override
        protected void onPostExecute(String[] strings) {

            arrayAdapterString.setValue(strings);
            super.onPostExecute(strings);

        }
    }
}

