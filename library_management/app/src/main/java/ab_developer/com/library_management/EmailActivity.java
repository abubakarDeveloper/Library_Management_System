package ab_developer.com.library_management;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class EmailActivity extends AppCompatActivity {

    TextInputLayout tilEmail;
    EditText etEmail;
    TextInputLayout tilPassword;
    EditText etPassword;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        tilEmail = (TextInputLayout) findViewById(R.id.til_email);
        etEmail = findViewById(R.id.et_email);


        btnLogin = findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tilEmail.setError(null);
                final String email = etEmail.getText().toString().trim();
                if(email.isEmpty()){

                    tilEmail.setError("Please Enter Email");
                    etEmail.requestFocus();

                }else if(!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")){
                    tilEmail.setError("Invalid Email");
                    etEmail.requestFocus();

                }else {

                    String url = "http://abubakar.androidstudent.net/test_dir/email.php?email=" + email;
                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("myTag", response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int status = jsonObject.getInt("status");
                                String message = jsonObject.getString("message");
                                if (status == 0) {
                                    //failure
                                    Log.i("mytag", message);
                                    Toast.makeText(EmailActivity.this, message, Toast.LENGTH_SHORT).show();
                                } else {
                                    JSONObject userObject = jsonObject.getJSONObject("user");
                                    Intent intent = new Intent(EmailActivity.this, LoginActivity.class);
                                    //intent.putExtra("email", String.valueOf(userObject));
                                    startActivity(intent);
                                    finish();
                                    // intent.putExtra("userName",fullName);
                                    //intent.putExtra("userImage",image);


                                    Toast.makeText(EmailActivity.this, message, Toast.LENGTH_SHORT).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Toast.makeText(EmailActivity.this, "Volley Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    queue.add(request);
                }
            }
        });
    }

}
