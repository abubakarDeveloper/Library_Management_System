package ab_developer.com.library_management;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProductHolder> {

    ArrayList<Student> dataset;
    AdapterView.OnItemClickListener onItemClickListener;

    public ProfileAdapter(ArrayList<Student> dataset, AdapterView.OnItemClickListener onItemClickListener) {
        this.dataset = dataset;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.issue_book_item_layout, parent, false);
        return new ProfileAdapter.ProductHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull final ProductHolder holder, final int position) {
        holder.tvBook.setText(dataset.get(position).student_name);
        holder.tvFine.setText(dataset.get(position).password);
        holder.tvReturnDate.setText(dataset.get(position).email);
        holder.tvIssueDate.setText(dataset.get(position).roll_noo);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(null, holder.itemView, position, 0);
            }
        });

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ProductHolder extends RecyclerView.ViewHolder {

        TextView tvBook;
        TextView tvIssueDate;
        TextView tvReturnDate;
        TextView tvFine;

        public ProductHolder(View itemView) {
            super(itemView);
            tvBook = itemView.findViewById(R.id.tv_book_name);
            tvIssueDate = itemView.findViewById(R.id.tv_issue_date);
            tvReturnDate = itemView.findViewById(R.id.tv_return_date);
            tvFine = itemView.findViewById(R.id.tv_fine);

        }
    }
}
