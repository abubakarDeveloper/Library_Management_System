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

import java.util.ArrayList;

public class IssuedBookActivity extends AppCompatActivity {

    RecyclerView rvBook;
    SwipeRefreshLayout swipeRefreshLayout;

    int userId = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issued_book);
        rvBook = findViewById(R.id.rv_books);
        swipeRefreshLayout = findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getBook();
            }
        });
        getBook();

    }
    public void getBook(){
        try {
            JSONObject userObject = SessionHelper.getCurrentUser(IssuedBookActivity.this);
            String name = userObject.getString("student_name");
            String email = userObject.getString("student_email");
            userId = userObject.getInt("student_id");
            Log.i("UserName", name);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final ProgressDialog pDialog = new ProgressDialog(IssuedBookActivity.this);
        pDialog.setMessage("Please Wait");
        pDialog.setCancelable(false);
        pDialog.show();


        String BOOK_URL = "http://abubakar.androidstudent.net/test_dir/issue_book_api.php?cat_id="+ String.valueOf(userId);
        StringRequest request = new StringRequest(Request.Method.GET, BOOK_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                Log.i("myTag", response);
                swipeRefreshLayout.setRefreshing(false);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray productsArray = jsonObject.getJSONArray("book_list");
                    final ArrayList<Book> bookList = new ArrayList<>();
                    for (int i = 0; i < productsArray.length(); i++) {
                        JSONObject productObject = productsArray.getJSONObject(i);
                        Book b = new Book();
                        b.ID = productObject.getInt("issue_book_id");
                        b.bookName = productObject.getString("book_name");
                        b.issueDate = productObject.getString("issue_date");
                        b.returnDate = productObject.getString("return_date");
                        b.fine = productObject.getString("fine");
                        b.student_registration_no = productObject.getString("student_registration_no");
                        b.return_book = productObject.getString("return_book");
                        bookList.add(b);
                    }

                    LeadAdapter adapter = new LeadAdapter(bookList, new AdapterView.OnItemClickListener(){

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        }
                    });
                    LinearLayoutManager manager = new LinearLayoutManager(IssuedBookActivity.this, LinearLayoutManager.VERTICAL, false);
                    rvBook.setLayoutManager(manager);
                    rvBook.setAdapter(adapter);

                } catch (JSONException e1) {
                    swipeRefreshLayout.setRefreshing(false);
                    e1.printStackTrace();
                    pDialog.dismiss();
                    Toast.makeText(IssuedBookActivity.this, "Parsig Error", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                pDialog.dismiss();
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(IssuedBookActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }
}
