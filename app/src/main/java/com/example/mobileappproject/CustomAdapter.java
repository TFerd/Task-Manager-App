package com.example.mobileappproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

//@Todo
//  Change the ArrayList<String> parameter to ArrayList<Task> when the Task class is set up


public class CustomAdapter extends BaseAdapter implements ListAdapter {

    private static final String TAG = "CustomAdapter";

    private ArrayList<String> list = new ArrayList<String>();
    private Context context;

    public CustomAdapter(ArrayList<String> list, Context context){
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        //return list.get(position).getId();
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null){
            Log.d(TAG, "getView: view == null passed");
            
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_list_layout, null);
            
            Log.i(TAG, "getView: View inflated successfully");
        }

        //Displays the String from the List
        TextView listItemText = (TextView)view.findViewById(R.id.list_item_text);
        listItemText.setText(list.get(position));

        //Button handlers
        Button deleteButton = (Button)view.findViewById(R.id.delete_btn);
        Button addButton = (Button)view.findViewById(R.id.add_btn);


        //@TODO
        //  Add confirmation to delete items
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                notifyDataSetChanged();

                Log.i(TAG, "onClick: Task deleted");
            }
        });

        //This is where you add Tasks to the List i think
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //list.add();
                notifyDataSetChanged();

                Log.i(TAG, "onClick: Task added");
            }
        });



        return view;
    }
}
