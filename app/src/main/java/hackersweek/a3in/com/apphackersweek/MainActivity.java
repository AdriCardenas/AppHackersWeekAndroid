package hackersweek.a3in.com.apphackersweek;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseReference mDatabase;
    private List<Gif> mGifList;
    private FloatingActionButton mFloatingActionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.floating_action_button);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddGifActivity.class);
                startActivity(intent);
            }
        });

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new GridLayoutManager(this,2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mGifList = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGifList.clear();
        new CargarDatos().execute();
    }

    private class CargarDatos extends AsyncTask<Void, Void, Boolean> {


        @Override
        protected Boolean doInBackground(Void... voids) {
            //TODO FIREBASE
            mDatabase = FirebaseDatabase.getInstance().getReference();
            Query mGifQuery = mDatabase.orderByKey();

            mGifQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        Gif gif = singleSnapshot.getValue(Gif.class);
                        mGifList.add(gif);
                    }
                    mAdapter = new MyAdapter(mGifList, getApplicationContext());
                    mRecyclerView.setAdapter(mAdapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            return true;
        }
    }
}
