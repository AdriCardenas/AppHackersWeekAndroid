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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

import static hackersweek.a3in.com.apphackersweek.tenor.utils.Tenor.getAnonId;
import static hackersweek.a3in.com.apphackersweek.tenor.utils.Tenor.getGifUrl;
import static hackersweek.a3in.com.apphackersweek.tenor.utils.Tenor.getSearchResults;

public class AddGifActivity extends AppCompatActivity {

    private Button mButton;
    private String searchTag;
    private TextInputLayout mTextInputLayout;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref = database.getReference();

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
                          SharedPreferences mPrefs = getSharedPreferences("tenor", 0);
                          String anonId = mPrefs.getString("anonymousId","");

                          if(anonId == "") // first time user, so get an anonymous ID for them and store it for later use
                          {
                              anonId = getAnonId();
                              SharedPreferences.Editor mEditor = mPrefs.edit();
                              mEditor.putString("anonymousId", anonId).commit();
                          }

                          // make initial search request for the first 8 items
                          JSONObject searchResult = getSearchResults(anonId, mTextInputLayout.getEditText().getText().toString());

                          // load the results for the user
                          Log.v("TenorTest", "Search Results: " + searchResult.toString());
                          ref.push().setValue(new Gif(searchTag, getGifUrl(searchResult)));

                          finish();
                      }
                    }.start();
                }
            }
        });

    }

    private boolean camposCorrectos() {
        return mTextInputLayout.getEditText().getText() != null && !mTextInputLayout.getEditText().getText().toString().equals("");
    }


}
