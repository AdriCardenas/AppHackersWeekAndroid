package hackersweek.a3in.com.apphackersweek;

import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import static hackersweek.a3in.com.apphackersweek.tenor.utils.Tenor.getAnonId;
import static hackersweek.a3in.com.apphackersweek.tenor.utils.Tenor.getGifUrl;
import static hackersweek.a3in.com.apphackersweek.tenor.utils.Tenor.getSearchResults;

public class AddGifActivity extends AppCompatActivity {

    private Button mButton;
    private String searchTag;
    private TextInputLayout mTextInputLayout;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private String anonId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gif);

        mTextInputLayout = (TextInputLayout) findViewById(R.id.text_input_layout);
        mButton = (Button) findViewById(R.id.save_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (camposCorrectos()){
                    searchTag = mTextInputLayout.getEditText().getText().toString();

                    new Thread(){
                      @Override
                        public void run(){
                          // check if there is an anonymous ID already stored
                          setApiParameter();

                          // make initial search request for the first 8 items
                          JSONObject searchResult = getSearchResults(anonId, searchTag);

                          // load the results for the user
                          Log.v("TenorTest", "Search Results: " + searchResult.toString());
                          databaseReference.push().setValue(new Gif(searchTag, getGifUrl(searchResult)));

                          finish();
                      }
                    }.start();
                }
            }
        });

    }

    private void setApiParameter(){
        SharedPreferences mPrefs = getSharedPreferences("tenor", 0);
        anonId = mPrefs.getString("anonymousId","");

        if(anonId == "") // first time user, so get an anonymous ID for them and store it for later use
        {
            anonId = getAnonId();
            SharedPreferences.Editor mEditor = mPrefs.edit();
            mEditor.putString("anonymousId", anonId).commit();
        }
    }

    private boolean camposCorrectos() {
        return mTextInputLayout.getEditText().getText() != null && !mTextInputLayout.getEditText().getText().toString().equals("");
    }


}
