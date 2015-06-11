package madelyntav.c4q.nyc.asyncpractice;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.net.URL;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    Button button;
    ImagesAdapter adapter;
    ArrayList<String> imageList;
    ListView list;
    ImageAsyncTask imageAsyncTask;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = (ListView) findViewById(R.id.listView);
        button = (Button) findViewById(R.id.button);
        imageList = new ArrayList<String>();

    }

//    @Override
//    protected void onResume() {
//        super.onResume();
////        if (imageAsyncTask != null) {
////            imageAsyncTask.cancel(true);
////        }
////        imageAsyncTask = new ImageAsyncTask();
////        imageAsyncTask.execute();
//    }

    //execute ASyncTask when button is pressed
    public void onPress(View v) {
        super.onResume();
//        if (imageAsyncTask != null) {
//            imageAsyncTask.cancel(true);
//        }

        imageAsyncTask = new ImageAsyncTask();
        imageAsyncTask.execute();


    }

    public class ImageAsyncTask extends AsyncTask<Void, Void, ArrayList<Bitmap>> {
        ArrayList<Bitmap> imageBitmaps = new ArrayList<>();

        @Override
        protected ArrayList<Bitmap> doInBackground(Void... voids) {


            try {

                String url = "https://api.flickr.com/services/feeds/photos_public.gne?format=json&nojsoncallback=1";

                //------------------>>

                //get response code from httpGet by passing the URL
                HttpGet httppost = new HttpGet(url);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);


                int status = response.getStatusLine().getStatusCode();


                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);

                    //create a JSONObject from data retrieved from url
                    //from that object get the JsON Array with a key of "items"

                    JSONObject jsono = new JSONObject(data);
                    JSONArray jarray = jsono.getJSONArray("items");

                    //for all objects in items
                    for (int i = 0; i < jarray.length(); i++) {

                        //refer to the current object
                        JSONObject object = jarray.getJSONObject(i);

                        //get object refered to with key media
                        //retrieve String url with the key "m"
                        JSONObject list = (JSONObject) object.get("media");
                        String imageUrl = (String) list.get("m");

                        //add imageURL to arrayList of String
                        imageList.add(imageUrl);
                    }
                    //for all strings in the arrayList, convert the string into a bitmap
                    for (int i = 0; i < imageList.size(); i++) {
                        Bitmap image = getBitmapFromURL(imageList.get(i));

                        //add all bitmaps to bitmap arrayList
                        imageBitmaps.add(image);
                    }

                    //return BitmapArrayList for onPostExecute to use
                    return imageBitmaps;
                }


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return imageBitmaps;
        }

        protected void onPostExecute(ArrayList<Bitmap> imageList) {

            //pass Bitmap to onPost execute, if imageList is null display error message
            if (imageList == null) {
                Toast.makeText(getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();

            } else {
                //if the bitmap array has anything inside of it, then set an imageAdapter
                //which contains the imageBitmap, to the listView
                ImagesAdapter imagesAdapter = new ImagesAdapter(MainActivity.this, imageBitmaps);
                list.setAdapter(imagesAdapter);
            }


        }

        public Bitmap getBitmapFromURL(String src) {
            try {

                //Retrieving information from a String url and turning it into a Bitmap
                URL url = new URL(src);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();

                //create inputStream from information retrieved by the connection and create Bitmap from input stream
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (IOException e) {
                // Log exception
                return null;
            }
        }
    }
}
