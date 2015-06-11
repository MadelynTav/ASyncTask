package madelyntav.c4q.nyc.asyncpractice;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by c4q-madelyntavarez on 6/10/15.
 */
public class ImagesAdapter extends BaseAdapter {

    ArrayList<Bitmap> arrayListImages;
    int Resource;
    Context context;
    LayoutInflater vi;



    public ImagesAdapter(Context context, ArrayList<Bitmap> images){

        this.context=context;
        arrayListImages=images;
        //inflate the layout through a layoutInflaterService
        vi=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }



    @Override
    public int getCount() {
        return arrayListImages.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //initiate the row view
        View rowView;

        //inflate the row view as a reference to the xml layout which contains one row which
        //only contains an imageView
        rowView = vi.inflate(R.layout.row, null);

        //get the imageView from the layout
        ImageView img=(ImageView) rowView.findViewById(R.id.imageView);

        //place the imagebitmap from each position within the arrayList to the imageView
        img.setImageBitmap(arrayListImages.get(position));

        //return the populated xml layout
        return rowView;

    }
    }

