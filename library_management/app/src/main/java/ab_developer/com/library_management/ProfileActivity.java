package ab_developer.com.library_management;

import android.app.ProgressDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
RecyclerView rvProfile;
    String email;
    String pass;
    String name;
    String rolNo;
    TextView tvName, tvEmail, tvPasssword, tvRollNo;
int userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        tvName = findViewById(R.id.tv_name);
        tvEmail = findViewById(R.id.tv_email);
        tvPasssword = findViewById(R.id.tv_pass);
        tvRollNo = findViewById(R.id.tv_roll_no);

        try {
            JSONObject userObject = SessionHelper.getCurrentUser(ProfileActivity.this);
             name = userObject.getString("student_name");
            email = userObject.getString("student_email");
            pass = userObject.getString("student_password");
            userId = userObject.getInt("student_id");
            rolNo = userObject.getString("student_registration_no");
            Log.i("UserName", name +  email  +""+  pass);
            tvRollNo.setText(rolNo);
            tvPasssword.setText(pass);
            tvEmail.setText(email);
            tvName.setText(name);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getBook(){
        try {
            JSONObject userObject = SessionHelper.getCurrentUser(ProfileActivity.this);
            String name = userObject.getString("student_name");
            email = userObject.getString("student_email");
            pass = userObject.getString("student_password");
            userId = userObject.getInt("student_id");
            Log.i("UserName", name +  email  +""+  pass);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final ProgressDialog pDialog = new ProgressDialog(ProfileActivity.this);
        pDialog.setMessage("Please Wait");
        pDialog.setCancelable(false);
        pDialog.show();

        String url = "http://abubakar.androidstudent.net/test_dir/login.php?email=" + email + "&pass=" + pass;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                Log.i("myTag", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray productsArray = jsonObject.getJSONArray("book_list");
                    final ArrayList<Student> bookList = new ArrayList<>();
                    for (int i = 0; i < productsArray.length(); i++) {
                        JSONObject productObject = productsArray.getJSONObject(i);
                        Student s = new Student();
                        s.studant_id = productObject.getInt("student_id");
                        s.student_name = productObject.getString("student_name");
                        s.email = productObject.optString("student_email");
                        s.password = productObject.getString("student_password");
                        bookList.add(s);
                    }

                    ProfileAdapter adapter = new ProfileAdapter(bookList, new AdapterView.OnItemClickListener(){

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        }
                    });
                    LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                    rvProfile.setLayoutManager(manager);
                    rvProfile.setAdapter(adapter);

                } catch (JSONException e1) {
                    e1.printStackTrace();
                    pDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Parsig Error", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }
}
