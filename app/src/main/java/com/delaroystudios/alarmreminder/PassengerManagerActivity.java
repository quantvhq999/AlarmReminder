package com.delaroystudios.alarmreminder;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.delaroystudios.alarmreminder.Adapter.ChildManageAdapter;
import com.delaroystudios.alarmreminder.Adapter.IClickListenerChildManageAdapter;
import com.delaroystudios.alarmreminder.Database.DBChild;
import com.delaroystudios.alarmreminder.Model.Child;

import java.util.ArrayList;

public class PassengerManagerActivity extends AppCompatActivity implements IClickListenerChildManageAdapter {
    RecyclerView rvChildrenManager;
    DBChild dbChild;
    ChildManageAdapter childManageAdapter;
    ArrayList<Child> allChildren = new ArrayList<Child>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_manager);

        rvChildrenManager = findViewById(R.id.rv_children_manage);
        dbChild= new DBChild(this);
        allChildren = dbChild.getAllChild();
        childManageAdapter = new ChildManageAdapter(allChildren,this,this);
        if (childManageAdapter!=null) {
            rvChildrenManager.setVisibility(View.VISIBLE);
            rvChildrenManager.setAdapter(childManageAdapter);
            rvChildrenManager.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    public void backActivity(View view) {
        onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        allChildren = dbChild.getAllChild();
        childManageAdapter = new ChildManageAdapter(allChildren,this,this);
        if (childManageAdapter!=null) {
            rvChildrenManager.setVisibility(View.VISIBLE);
            rvChildrenManager.setAdapter(childManageAdapter);
            rvChildrenManager.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    @Override
    public void deleteChild(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to delete this child?");
        builder.setCancelable(false);
        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dbChild.deleteChild(allChildren.get(position));
                childManageAdapter.deleteChild(position);
                if (childManageAdapter!=null){
                    childManageAdapter.notifyDataSetChanged();
                }else {
                    rvChildrenManager.setVisibility(View.INVISIBLE);
                }
                Log.d("position2", String.valueOf(position));
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void gotoChild(int position) {
        Intent intent = new Intent(this,ChildActivity.class);
        intent.putExtra("child_id_update",allChildren.get(position).getId());
        startActivity(intent);
    }
}
