package hackersweek.a3in.com.apphackersweek;

/**
 * Created by Adrian on 26/02/2018.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by AdrianCardenasJimenez on 20/02/2018.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
    private List<Gif> mGifList;
    private Context context;

    public MyAdapter(List<Gif> mGifList, Context context){
        this.mGifList = mGifList;
        this.context = context;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
        holder.mTextView.setText(mGifList.get(position).getTitle());
        Glide.with(context)
                .load(mGifList.get(position).getUrl()).asGif().crossFade()
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mGifList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        public TextView mTextView;
        public ImageView mImageView;

        public ViewHolder(View v) {
            super(v);
            mView = v;
            mTextView = v.findViewById(R.id.title);
            mImageView = v.findViewById(R.id.gif);
        }
    }

}
