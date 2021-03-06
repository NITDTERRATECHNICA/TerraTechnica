package delhi.android.nit.com.terratechnica;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class insta extends Fragment {
    RecyclerView galleryRV;
    List<Images> setofFlowers;
    ProgressBar progressBar;
    Adapter adapter;
    String URL = "https://www.instagram.com/terratechnica/media/";

    public insta() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_insta, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        galleryRV = (RecyclerView) view.findViewById(R.id.galleryRV);
        galleryRV.setLayoutManager(new GridLayoutManager(getContext(), 1));
        progressBar = (ProgressBar) view.findViewById(R.id.prog);
        if (isOnline()) {
            new Background().execute();
            //new MyTask().execute("http://saptrang2016.comlu.com/index.php");
        } else {
            Toast.makeText(getContext(), "Network not available!", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean isOnline() {
        ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    private class Background extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            loadImages();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressBar.setVisibility(View.GONE);
            if (setofFlowers != null) {
                adapter = new Adapter();
                galleryRV.setAdapter(adapter);
            } else {
                Toast.makeText(getContext(), "setofFlower is null", Toast.LENGTH_SHORT).show();
            }
        }
    }


    void loadImages() {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(URL).build();
            Response response = client.newCall(request).execute();
            String result = response.body().string();

            setofFlowers = new ArrayList<Images>();
            JSONObject jo1 = new JSONObject(result);
            JSONArray ja1 = jo1.getJSONArray("items");
            //Log.d("After", "items");
            JSONObject jo2, jo3, jo4, jo5, jo6;
            JSONObject low = new JSONObject(), thumb = new JSONObject(), standard = new JSONObject();
            for (int i = 0; i < ja1.length(); i++) {
                jo2 = ja1.getJSONObject(i);
                //Log.d("After", "i");
                jo3 = jo2.getJSONObject("images");
                //Log.d("After", "images");
                jo4 = jo3.getJSONObject("low_resolution");
                //Log.d("After", "low_resolution");
                jo5 = jo3.getJSONObject("thumbnail");
                //Log.d("After", "thumbnail");
                jo6 = jo3.getJSONObject("standard_resolution");
                //Log.d("After", "standard_resolution");
                low.put(String.valueOf(i), jo4.getString("url"));
                //Log.d("After", "url");
                thumb.put(String.valueOf(i), jo5.getString("url"));
                Log.d("After", jo6.getString("url"));
                standard.put(String.valueOf(i), jo6.getString("url"));
                Log.d("After", jo5.getString("url"));
                setofFlowers.add(new Images(jo5.getString("url"), jo6.getString("url")));
            }
            //HERE LOAD IMAGES IN PICASO VIA ANYONE OF low,thumb,standard JSON OBjects. They represent quality of pics.

        } catch (Exception e) {

        }
        /*client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Failure", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if (result == null) {
                    //Toast.makeText(getApplicationContext(), "Failed to Load Images!\n Please Try Again", Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    setofFlowers = new ArrayList<Images>();
                    JSONObject jo1 = new JSONObject(result);
                    JSONArray ja1 = jo1.getJSONArray("items");
                    //Log.d("After", "items");
                    JSONObject jo2, jo3, jo4, jo5, jo6;
                    JSONObject low = new JSONObject(), thumb = new JSONObject(), standard = new JSONObject();
                    for (int i = 0; i < ja1.length(); i++) {
                        jo2 = ja1.getJSONObject(i);
                        //Log.d("After", "i");
                        jo3 = jo2.getJSONObject("images");
                        //Log.d("After", "images");
                        jo4 = jo3.getJSONObject("low_resolution");
                        //Log.d("After", "low_resolution");
                        jo5 = jo3.getJSONObject("thumbnail");
                        //Log.d("After", "thumbnail");
                        jo6 = jo3.getJSONObject("standard_resolution");
                        //Log.d("After", "standard_resolution");
                        low.put(String.valueOf(i), jo4.getString("url"));
                        //Log.d("After", "url");
                        thumb.put(String.valueOf(i), jo5.getString("url"));
                        Log.d("After", jo6.getString("url"));
                        standard.put(String.valueOf(i), jo6.getString("url"));
                        Log.d("After", jo5.getString("url"));
                        setofFlowers.add(new Images(jo5.getString("url"),jo6.getString("url")));
                    }
                    //HERE LOAD IMAGES IN PICASO VIA ANYONE OF low,thumb,standard JSON OBjects. They represent quality of pics.
                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(getApplicationContext(), "XXXXCEPTION.!!!Failed to Load Images!\n Please Try Again", Toast.LENGTH_LONG).show();
                }
            }
        });*/
    }

    private class Adapter extends RecyclerView.Adapter<Holder> {

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_instagram, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            Log.e("ManojitM", "" + setofFlowers.size());
            if ((3 * position + 2) < setofFlowers.size()) {
                Images image1 = setofFlowers.get(3 * position);
                Images image2 = setofFlowers.get(3 * position + 1);
                Images image3 = setofFlowers.get(3 * position + 2);
                Log.e("ManojitM", "" + 3 * position + " " + (3 * position + 1) + " " + (3 * position + 2));

                try {
                    /*Bitmap bitmap1 = Picasso.with(getBaseContext())
                            .load(IMAGE_BASE_URL+image1.getImageLink())
                            .get();
                    image1.setImage(bitmap1);
                    holder.image1.setImageBitmap(bitmap1);

                    Bitmap bitmap2 = Picasso.with(getBaseContext())
                            .load(IMAGE_BASE_URL+image2.getImageLink())
                            .get();
                    image2.setImage(bitmap2);
                    holder.image2.setImageBitmap(bitmap2);

                    Bitmap bitmap3 = Picasso.with(getBaseContext())
                            .load(IMAGE_BASE_URL+image3.getImageLink())
                            .get();
                    image3.setImage(bitmap3);
                    holder.image3.setImageBitmap(bitmap3);*/
                    Picasso.with(getContext())
                            .load(image1.getThumnailLink())
                            .into(holder.image1);
                    Picasso.with(getContext())
                            .load(image2.getThumnailLink())
                            .into(holder.image2);
                    Picasso.with(getContext())
                            .load(image3.getThumnailLink())
                            .into(holder.image3);
                    holder.image2.setVisibility(View.VISIBLE);
                    holder.image3.setVisibility(View.VISIBLE);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if ((3 * position + 1) < setofFlowers.size()) {
                Images image1 = setofFlowers.get(3 * position);
                Images image2 = setofFlowers.get(3 * position + 1);
                Log.e("ManojitM", "" + 3 * position + " " + (3 * position + 1));
                try {
                    /*Bitmap bitmap1 = Picasso.with(getBaseContext())
                            .load(IMAGE_BASE_URL+image1.getImageLink())
                            .get();
                    image1.setImage(bitmap1);
                    holder.image1.setImageBitmap(bitmap1);

                    Bitmap bitmap2 = Picasso.with(getBaseContext())
                            .load(IMAGE_BASE_URL+image2.getImageLink())
                            .get();
                    image2.setImage(bitmap2);
                    holder.image2.setImageBitmap(bitmap2);*/
                    Picasso.with(getContext())
                            .load(image1.getThumnailLink())
                            .into(holder.image1);
                    Picasso.with(getContext())
                            .load(image2.getThumnailLink())
                            .into(holder.image2);
                    holder.image2.setVisibility(View.VISIBLE);
                    holder.image3.setVisibility(View.GONE);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Images image1 = setofFlowers.get(3 * position);
                Log.e("ManojitM", "" + 3 * position);
                try {
                    /*Bitmap bitmap1 = Picasso.with(getBaseContext())
                            .load(IMAGE_BASE_URL+image1.getImageLink())
                            .get();
                    image1.setImage(bitmap1);
                    holder.image1.setImageBitmap(bitmap1);*/
                    Picasso.with(getContext())
                            .load(image1.getThumnailLink())
                            .into(holder.image1);
                    holder.image2.setVisibility(View.GONE);
                    holder.image3.setVisibility(View.GONE);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }

        @Override
        public int getItemCount() {
            if (setofFlowers == null) {
                return 0;
            }
            if (setofFlowers.size() % 3 == 0) {
                return (setofFlowers.size() / 3);
            } else {
                return ((setofFlowers.size() / 3) + 1);
            }
        }
    }

    private class Holder extends RecyclerView.ViewHolder {

        ImageView image1, image2, image3;

        public Holder(View itemView) {
            super(itemView);
            image1 = (ImageView) itemView.findViewById(R.id.image1);
            image2 = (ImageView) itemView.findViewById(R.id.image2);
            image3 = (ImageView) itemView.findViewById(R.id.image3);
        }
    }
}
