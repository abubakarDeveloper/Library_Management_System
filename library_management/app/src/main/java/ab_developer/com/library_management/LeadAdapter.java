package ab_developer.com.library_management;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LeadAdapter extends RecyclerView.Adapter<LeadAdapter.ProductHolder> {

    ArrayList<Book> dataset;
    AdapterView.OnItemClickListener onItemClickListener;

    public LeadAdapter(ArrayList<Book> dataset, AdapterView.OnItemClickListener onItemClickListener) {
        this.dataset = dataset;
        this.onItemClickListener = onItemClickListener;
    }


    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.issue_book_item_layout, parent, false);
        return new ProductHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductHolder holder, final int position) {
        //    holder.tvId.setText("" + dataset.get(position).ID);

        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMdd");
        try {
            Date date = originalFormat.parse(String.valueOf(dataset.get(position).issueDate));
            Date date2 = originalFormat.parse(String.valueOf(dataset.get(position).returnDate));
            holder.tvIssueDate.setText(String.valueOf(date));
            holder.tvReturnDate.setText(String.valueOf(date2));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.tvBook.setText(dataset.get(position).bookName);
        holder.tvFine.setText(dataset.get(position).fine);
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

