package ab_developer.com.library_management;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ProductHolder>{

    ArrayList<Book> dataset;
    AdapterView.OnItemClickListener onItemClickListener;

    public BookAdapter(ArrayList<Book> dataset, AdapterView.OnItemClickListener onItemClickListener) {
        this.dataset = dataset;
        this.onItemClickListener = onItemClickListener;
    }


    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.book_item_layout, parent, false);
        return new BookAdapter.ProductHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductHolder holder, final int position) {
        holder.tvBook.setText(dataset.get(position).bookName);
        holder.tvIssueDate.setText(dataset.get(position).author_name);
        holder.tvReturnDate.setText(dataset.get(position).dept_name);
        holder.tvFine.setText(dataset.get(position).rake_name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(null, holder.itemView, position, 0);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    class ProductHolder extends RecyclerView.ViewHolder {

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
